package buildcraft.lib;
import org.jetbrains.annotations.NotNull;

import buildcraft.lib.client.guide.GuiGuide;
import buildcraft.lib.container.ContainerGuide;
import buildcraft.lib.item.ItemGuide;
import buildcraft.lib.misc.StackUtil;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;



public class BCLibScreenConstructors {
    public static final MenuScreens.ScreenConstructor<ContainerGuide, GuiGuide> GUIDE = new MenuScreens.ScreenConstructor<>() {
        @NotNull
        @Override
        public GuiGuide create(@NotNull ContainerGuide container, @NotNull Inventory inv, @NotNull Component title) {
            Player player = inv.player;
            ItemStack stack;
            if (player.getMainHandItem().getItem() == BCLibItems.guide.get()) {
                stack = player.getMainHandItem();
            } else if (player.getOffhandItem().getItem() == BCLibItems.guide.get()) {
                stack = player.getOffhandItem();
            } else {
                stack = StackUtil.EMPTY;
            }
            String name = ItemGuide.getBookName(stack);
            if (name == null || name.isEmpty()) {
                return new GuiGuide(container, title);
            } else {
                return new GuiGuide(container, name, title);
            }
        }
    };
}
