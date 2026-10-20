package buildcraft.lib.list;
import org.jetbrains.annotations.NotNull;

import buildcraft.api.lists.ListMatchHandler;
import net.minecraft.world.item.ItemStack;



// Calen
public class ListMatchHandlerFood extends ListMatchHandler {
    @Override
    public boolean matches(Type type, @NotNull ItemStack stack, @NotNull ItemStack target, boolean precise) {
        if (type == Type.TYPE) {
            return stack.getItem().isEdible() && target.getItem().isEdible();
        }
        return false;
    }

    @Override
    public boolean isValidSource(Type type, @NotNull ItemStack stack) {
        return stack.getItem().isEdible();
    }
}
