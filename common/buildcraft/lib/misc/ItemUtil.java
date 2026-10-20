package buildcraft.lib.misc;

import net.minecraft.core.NonNullList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.ForgeRegistries;

// Calen
public class ItemUtil {
    public static Item getItemFromRegistryName(String name) {
        return getItemFromRegistryName(Identifier.fromNamespaceAndPath(name));
    }

    public static Item getItemFromRegistryName(Identifier name) {
        return ForgeRegistries.ITEMS.getValue(name);
    }

    public static Identifier getRegistryName(Item item) {
        return item.builtInRegistryHolder().key().location();
    }

    public static void fillItemCategory(Item item, ResourceKey<CreativeModeTab> tabKey, NonNullList<ItemStack> stacks) {
        CreativeModeTab tab = BuiltInRegistries.CREATIVE_MODE_TAB.get(tabKey);
        tab.getDisplayItems().forEach(stack -> {
            if (stack.getItem() == item) {
                stacks.add(stack);
            }
        });
    }
}
