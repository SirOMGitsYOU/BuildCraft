/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.client.render.tile;
import net.minecraft.util.profiling.Profiler;
import org.jetbrains.annotations.NotNull;

import buildcraft.lib.client.model.MutableQuad;
import buildcraft.lib.engine.TileEngineBase_BC8;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.util.profiling.ProfilerFiller;

public abstract class RenderEngine_BC8<T extends TileEngineBase_BC8> implements BlockEntityRenderer<T> {
    public RenderEngine_BC8(BlockEntityRendererProvider.Context context) {
    }

    // TODO: Cache the model!

    @Override
//    public void renderTileEntityFast(@NotNull T engine, double x, double y, double z, float partialTicks, int destroyStage, float partial, @NotNull BufferBuilder vb)
    public void render(T engine, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lightc, int combinedOverlay) {
        ProfilerFiller profiler = net.minecraft.util.profiling.Profiler.get();
        profiler.push("bc");
        profiler.push("engine");

        profiler.push("compute");
//        vb.setTranslation(x, y, z);
        poseStack.pushPose();
        MutableQuad[] quads = getEngineModel(engine, partialTicks);
        profiler.popPush("render");
        MutableQuad copy = new MutableQuad(0, null);
//        int lightc = engine.getLevel().getLightEngine().getRawBrightness(engine.getBlockPos(), 0);
        int light_block = (lightc >> 4) & 15;
        int light_sky = (lightc >> 20) & 15;
        VertexConsumer vb = bufferSource.getBuffer(RenderType.cutout());
        for (MutableQuad q : quads) {
            copy.copyFrom(q);
            copy.maxLighti((byte) light_block, (byte) light_sky);
            copy.overlay(combinedOverlay);
            copy.multShade();
            copy.render(poseStack.last(), vb);
        }
//        vb.setTranslation(0, 0, 0);
        poseStack.popPose();

        profiler.pop();
        profiler.pop();
        profiler.pop();
    }

    protected abstract MutableQuad[] getEngineModel(T engine, float partialTicks);
}
