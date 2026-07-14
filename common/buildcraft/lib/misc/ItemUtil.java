package buildcraft.lib.misc;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

public final class ItemUtil {
    private ItemUtil() {
    }

    public static Item getItemFromRegistryName(String name) {
        return getItemFromRegistryName(new ResourceLocation(name));
    }

    public static Item getItemFromRegistryName(ResourceLocation name) {
        return ForgeRegistries.ITEMS.getValue(name);
    }

    public static ResourceLocation getRegistryName(Item item) {
        return ForgeRegistries.ITEMS.getKey(item);
    }
}
