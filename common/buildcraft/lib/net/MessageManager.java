/* Copyright (c) 2016 SpaceToad and the BuildCraft team
 *
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/. */
package buildcraft.lib.net;

import org.jetbrains.annotations.Nullable;

import buildcraft.api.IBuildCraftMod;
import buildcraft.api.core.BCDebugging;
import buildcraft.api.core.BCLog;
import buildcraft.api.net.IMessage;
import buildcraft.api.net.IMessageHandler;
import buildcraft.lib.BCLib;
import buildcraft.lib.BCLibProxy;
import buildcraft.lib.misc.MessageUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;

import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentSkipListMap;

public class MessageManager {
    public static final boolean DEBUG = BCDebugging.shouldDebugLog("lib.messages");

    private static final Map<IBuildCraftMod, PerModHandler> MOD_HANDLERS;
    private static final Map<Class<? extends IMessage>, PerMessageInfo<?>> MESSAGE_HANDLERS = new ConcurrentHashMap<>();

    static {
        MOD_HANDLERS = new ConcurrentSkipListMap<>(MessageManager::compareMods);
    }

    static class PerModHandler {
        final IBuildCraftMod module;
        final SimpleChannel netWrapper;
        final SortedMap<Class<? extends IMessage>, PerMessageInfo<?>> knownMessages;

        PerModHandler(IBuildCraftMod module) {
            this.module = module;
            this.netWrapper = ChannelBuilder.named(Identifier.fromNamespaceAndPath(module.getModId(), "default"))
                    .acceptedVersions((status, version) -> true)
                    .simpleChannel();
            knownMessages = new TreeMap<>(Comparator.comparing(Class::getName));
        }
    }

    static class PerMessageInfo<I extends IMessage> {
        final PerModHandler modHandler;
        final Class<I> messageClass;

        @Nullable
        IMessageHandler<I, ?> clientHandler, serverHandler;

        PerMessageInfo(PerModHandler modHandler, Class<I> messageClass) {
            this.modHandler = modHandler;
            this.messageClass = messageClass;
        }
    }

    private static int compareMods(IBuildCraftMod modA, IBuildCraftMod modB) {
        if (modA instanceof Enum && modB instanceof Enum) {
            Enum<?> enumA = (Enum<?>) modA;
            Enum<?> enumB = (Enum<?>) modB;
            if (enumA.getDeclaringClass() == enumB.getDeclaringClass()) {
                return Integer.compare(enumA.ordinal(), enumB.ordinal());
            }
        }
        return modA.getModId().compareTo(modB.getModId());
    }

    public static <I extends IMessage> void registerMessageClass(IBuildCraftMod module, Class<I> clazz, Dist... sides) {
        registerMessageClass(module, clazz, null, sides);
    }

    public static <I extends IMessage> void registerMessageClass(
            IBuildCraftMod module,
            Class<I> messageClass,
            IMessageHandler<I, ?> messageHandler,
            Dist... sides
    ) {
        PerModHandler modHandler = MOD_HANDLERS.computeIfAbsent(module, PerModHandler::new);
        PerMessageInfo<I> messageInfo = (PerMessageInfo<I>) modHandler.knownMessages.get(messageClass);
        if (messageInfo == null) {
            messageInfo = new PerMessageInfo<>(modHandler, messageClass);
            modHandler.knownMessages.put(messageClass, messageInfo);
            MESSAGE_HANDLERS.put(messageClass, messageInfo);
        }
        String netName = module.getModId();
        if (messageHandler == null) {
            if (DEBUG) {
                BCLog.logger.info("[lib.messages] Registered message " + messageClass + " for " + netName);
            }
            return;
        }
        Dist specificSide = sides != null && sides.length == 1 ? sides[0] : null;
        if (specificSide == null || specificSide == Dist.CLIENT) {
            if (messageInfo.clientHandler != null && DEBUG) {
                BCLog.logger.info("[lib.messages] Replacing existing client handler for " + netName + " " + messageClass
                        + " " + messageInfo.clientHandler + " with " + messageHandler);
            }
            messageInfo.clientHandler = messageHandler;
        }
        if (specificSide == null || specificSide == Dist.DEDICATED_SERVER) {
            if (messageInfo.serverHandler != null && DEBUG) {
                BCLog.logger.info("[lib.messages] Replacing existing server handler for " + netName + " " + messageClass
                        + " " + messageInfo.serverHandler + " with " + messageHandler);
            }
            messageInfo.serverHandler = messageHandler;
        }
    }

    public static <I extends IMessage> void setHandler(Class<I> messageClass, IMessageHandler<I, ?> messageHandler, Dist side) {
        PerMessageInfo<I> messageInfo = (PerMessageInfo<I>) MESSAGE_HANDLERS.get(messageClass);
        if (messageInfo == null) {
            throw new IllegalArgumentException("Cannot set handler for unregistered message: " + messageClass);
        }
        registerMessageClass(messageInfo.modHandler.module, messageClass, messageHandler, side);
    }

    public static void fmlPostInit() {
        if (DEBUG) {
            BCLog.logger.info("[lib.messages] Sorting and registering message classes and orders:");
        }
        for (PerModHandler handler : MOD_HANDLERS.values()) {
            if (DEBUG) {
                BCLog.logger.info("[lib.messages]  - Module: " + handler.module.getModId());
            }
            int wholeId = 0;
            for (PerMessageInfo<?> info : handler.knownMessages.values()) {
                int id = wholeId++;
                postInitSingle(handler, id, info);
            }
            handler.netWrapper.build();
        }
    }

    private static <I extends IMessage> void postInitSingle(PerModHandler handler, int id, PerMessageInfo<I> info) {
        boolean cl = info.clientHandler != null;
        boolean sv = info.serverHandler != null;
        if (!(cl | sv)) {
            if (FMLEnvironment.dist == Dist.CLIENT) {
                throw new IllegalStateException("Found a registered message " + info.messageClass + " for "
                        + info.modHandler.module.getModId() + " that didn't have any handlers!");
            }
        }

        Class<I> msgClass = info.messageClass;

        handler.netWrapper.messageBuilder(msgClass, id)
                .encoder(IMessage::toBytes)
                .decoder(buf -> IMessage.staticFromBytes(msgClass, buf))
                .consumerMainThread((msg, context) -> {
                    IMessageHandler<I, ?> messageHandler = context.isServerSide() ? info.serverHandler : info.clientHandler;
                    wrapHandler(messageHandler, msgClass).onMessage(msg, context);
                    context.setPacketHandled(true);
                })
                .add();
        if (DEBUG) {
            String sides = cl ? (sv ? "{client, server}" : "{client}") : "{server}";
            BCLog.logger.info("[lib.messages]      " + id + ": " + msgClass + " on sides: " + sides);
        }
    }

    private static <I extends IMessage> IMessageHandler<I, ?> wrapHandler(IMessageHandler<I, ?> messageHandler, Class<I> messageClass) {
        if (messageHandler == null) {
            return (message, context) -> {
                if (context.isServerSide()) {
                    Player player = context.getSender();
                    BCLog.logger.warn(
                            "[lib.messages] The client " + player.getName() + " (ID = " + player.getGameProfile().getId()
                                    + ") sent an invalid message " + messageClass + ", when they should only receive them!");
                } else {
                    throw new Error("Received message " + messageClass
                            + " on the client, when it should only be sent by the client and received on the server!");
                }
                return null;
            };
        } else {
            return (message, context) -> {
                Player player = BCLibProxy.getProxy().getPlayerForContext(context);
                if (player == null || player.level() == null) {
                    return null;
                }
                BCLibProxy.getProxy().addScheduledTask(player.level(), () -> {
                    IMessage reply = messageHandler.onMessage(message, context);
                    if (reply != null) {
                        MessageUtil.sendReturnMessage(context, reply);
                    }
                });
                return null;
            };
        }
    }

    private static SimpleChannel getSimpleNetworkWrapper(IMessage message) {
        PerMessageInfo<?> info = MESSAGE_HANDLERS.get(message.getClass());
        if (info == null) {
            throw new IllegalArgumentException("Cannot send unregistered message " + message.getClass());
        }
        return info.modHandler.netWrapper;
    }

    public static void sendToAll(IMessage message) {
        getSimpleNetworkWrapper(message).send(PacketDistributor.ALL.noArg(), message);
    }

    public static void sendTo(IMessage message, ServerPlayer player) {
        getSimpleNetworkWrapper(message).send(PacketDistributor.PLAYER.with(() -> player), message);
    }

    public static void sendToAllAround(IMessage message, PacketDistributor.TargetPoint point) {
        getSimpleNetworkWrapper(message).send(PacketDistributor.NEAR.with(() -> point), message);
    }

    public static void sendToDimension(IMessage message, ResourceKey<Level> dimensionId) {
        getSimpleNetworkWrapper(message).send(PacketDistributor.DIMENSION.with(() -> dimensionId), message);
    }

    public static void sendToServer(IMessage message) {
        getSimpleNetworkWrapper(message).sendToServer(message);
    }

    public static void sendToEntity(IMessage message, Entity entity) {
        getSimpleNetworkWrapper(message).send(PacketDistributor.TRACKING_ENTITY.with(() -> entity), message);
    }
}
