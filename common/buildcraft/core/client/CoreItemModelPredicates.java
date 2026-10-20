package buildcraft.core.client;

import buildcraft.api.items.IMapLocation;
import buildcraft.core.BCCoreItems;
import buildcraft.core.item.ItemList_BC8;
import buildcraft.lib.misc.StackUtil;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.Identifier;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@OnlyIn(Dist.CLIENT)
public class CoreItemModelPredicates {
    public static final Identifier PREDICATE_USED = Identifier.fromNamespaceAndPath("buildcraft", "used");
    public static final Identifier PREDICATE_MAP_TYPE = Identifier.fromNamespaceAndPath("buildcraft", "map_type");

    public static void register(FMLClientSetupEvent event) {
        event.enqueueWork(
                () ->
                {
                    ItemProperties.register(
                            BCCoreItems.list.get(),
                            PREDICATE_USED,
                            (stack, world, entity, pSeed) -> ItemList_BC8.isUsed(stack) ? 1 : 0
                    );
                    ItemProperties.register(
                            BCCoreItems.mapLocation.get(),
                            PREDICATE_MAP_TYPE,
                            (stack, world, entity, pSeed) -> IMapLocation.MapLocationType.getFromStack(StackUtil.asNonNull(stack)).meta
                    );
                }
        );
    }
}
