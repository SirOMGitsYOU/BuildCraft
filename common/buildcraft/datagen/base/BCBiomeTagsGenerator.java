package buildcraft.datagen.base;

import buildcraft.api.BCModules;
import buildcraft.lib.oredictionarytag.OreDictionaryTags;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.tags.BiomeTagsProvider;
import net.minecraft.tags.BiomeTags;
import net.minecraftforge.common.data.ExistingFileHelper;

public class BCBiomeTagsGenerator extends BiomeTagsProvider {
    public BCBiomeTagsGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, BCModules.BUILDCRAFT, existingFileHelper);
    }

    @Override
    protected void addTags() {
        // Oil structures use the vanilla overworld biome tag so generation does not
        // depend on custom oil biomes being present in the datapack registry.
        tag(OreDictionaryTags.OIL_GEN)
                .addTag(BiomeTags.IS_OVERWORLD);
    }

    @Override
    public String getName() {
        return "BuildCraft Biome Tags Generator";
    }
}
