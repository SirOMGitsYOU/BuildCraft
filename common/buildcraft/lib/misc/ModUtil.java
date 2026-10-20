package buildcraft.lib.misc;

import org.jetbrains.annotations.Nullable;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;



public class ModUtil {

    @Nullable
    public static String getNameOfMod(String domain) {
        ModContainer mod = ModList.get().getModContainerById(domain).get();
        if (mod != null) {
            return mod.getModInfo().getDisplayName();
        }
        return null;
    }
}
