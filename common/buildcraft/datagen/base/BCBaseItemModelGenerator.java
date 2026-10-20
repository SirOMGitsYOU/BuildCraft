package buildcraft.datagen.base;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;

public abstract class BCBaseItemModelGenerator extends ItemModelProvider {
    protected static final Identifier GENERATED = Identifier.fromNamespaceAndPath("minecraft", "item/generated");
    protected static final Identifier CUBE_ALL = Identifier.fromNamespaceAndPath("minecraft", "block/cube_all");
    protected static final Identifier HANDHELD = Identifier.fromNamespaceAndPath("minecraft", "item/handheld");
    protected static final Identifier BLOCK = Identifier.fromNamespaceAndPath("minecraft", "block/block");
    protected static final ModelFile BUILTIN_ENTITY = new ModelFile.UncheckedModelFile(Identifier.fromNamespaceAndPath("minecraft", "builtin/entity"));

    public BCBaseItemModelGenerator(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }
}
