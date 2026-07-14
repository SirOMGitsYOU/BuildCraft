package buildcraft.datagen;

import buildcraft.core.BCCore;
import buildcraft.datagen.base.*;
import buildcraft.datagen.builders.BuildersAdvancementGenerator;
import buildcraft.datagen.builders.BuildersBlockStateGenerator;
import buildcraft.datagen.builders.BuildersCraftingRecipeGenerator;
import buildcraft.datagen.builders.BuildersItemModelGenerator;
import buildcraft.datagen.core.CoreAdvancementGenerator;
import buildcraft.datagen.core.CoreBlockStateGenerator;
import buildcraft.datagen.core.CoreCraftingRecipeGenerator;
import buildcraft.datagen.core.CoreItemModelGenerator;
import buildcraft.datagen.energy.*;
import buildcraft.datagen.factory.FactoryAdvancementGenerator;
import buildcraft.datagen.factory.FactoryBlockStateGenerator;
import buildcraft.datagen.factory.FactoryCraftingRecipeGenerator;
import buildcraft.datagen.factory.FactoryItemModelGenerator;
import buildcraft.datagen.lib.LibCraftingRecipeGenerator;
import buildcraft.datagen.lib.LibItemModelProvider;
import buildcraft.datagen.robotics.*;
import buildcraft.datagen.silicon.*;
import buildcraft.datagen.transport.*;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.DataProvider;
import net.minecraft.data.tags.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.data.event.GatherDataEvent;

@Mod.EventBusSubscriber(modid = BCCore.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class BCDataGenerators {
    private static void addServer(DataGenerator generator, GatherDataEvent event, DataProvider provider) {
        generator.addProvider(event.includeServer(), provider);
    }

    private static void addClient(DataGenerator generator, GatherDataEvent event, DataProvider provider) {
        generator.addProvider(event.includeClient(), provider);
    }

    @SubscribeEvent
    public static void onGatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();

        // Oil Texture
        addClient(generator, event, new EnergyOilTextureGenerator(generator, existingFileHelper));

        // Tags
        BlockTagsProvider blockTagsProvider = new BCBlockTagsGenerator(generator, existingFileHelper);
        addServer(generator, event, blockTagsProvider);
        addServer(generator, event, new BCItemTagsGenerator(generator, existingFileHelper, blockTagsProvider));
        addServer(generator, event, new BCFluidTagsGenerator(generator, existingFileHelper));
        addServer(generator, event, new BCBiomeTagsGenerator(generator, existingFileHelper));

        // Crafting Recipes
        addServer(generator, event, new BuildersCraftingRecipeGenerator(generator));
        addServer(generator, event, new CoreCraftingRecipeGenerator(generator));
        addServer(generator, event, new EnergyCraftingRecipeGenerator(generator));
        addServer(generator, event, new FactoryCraftingRecipeGenerator(generator));
        addServer(generator, event, new LibCraftingRecipeGenerator(generator));
        addServer(generator, event, new SiliconCraftingRecipeGenerator(generator));
        addServer(generator, event, new TransportCraftingRecipeGenerator(generator));
        addServer(generator, event, new RoboticsCraftingRecipeGenerator(generator));
        // Mod Recipes
        addServer(generator, event, new SiliconFacadeSwapRecipeGenerator(generator));
        addServer(generator, event, new EnergyOilRecipeGenerator(generator, existingFileHelper));
        addServer(generator, event, new SiliconAssemblyRecipeGenerator(generator, existingFileHelper));
        addServer(generator, event, new TransportAssemblyRecipeGenerator(generator, existingFileHelper));
        addServer(generator, event, new RoboticsIntegrationRecipeGenerator(generator, existingFileHelper));
        addServer(generator, event, new RoboticsProgrammingRecipeGenerator(generator, existingFileHelper));

        // Advancement
        addServer(generator, event, new CoreAdvancementGenerator(generator, existingFileHelper));
        addServer(generator, event, new EnergyAdvancementGenerator(generator, existingFileHelper));
        addServer(generator, event, new FactoryAdvancementGenerator(generator, existingFileHelper));
        addServer(generator, event, new SiliconAdvancementGenerator(generator, existingFileHelper));
        addServer(generator, event, new TransportAdvancementGenerator(generator, existingFileHelper));
        addServer(generator, event, new BuildersAdvancementGenerator(generator, existingFileHelper));

        // Loot Table
        addServer(generator, event, new BCLootGenerator(generator));

        // BlockState and Block Model
        addClient(generator, event, new CoreBlockStateGenerator(generator, existingFileHelper));
        addClient(generator, event, new BuildersBlockStateGenerator(generator, existingFileHelper));
        addClient(generator, event, new EnergyBlockStateGenerator(generator, existingFileHelper));
        addClient(generator, event, new FactoryBlockStateGenerator(generator, existingFileHelper));
        addClient(generator, event, new SiliconBlockStateGenerator(generator, existingFileHelper));
        addClient(generator, event, new TransportBlockStateGenerator(generator, existingFileHelper));
        addClient(generator, event, new RoboticsBlockStateGenerator(generator, existingFileHelper));

        // Item Model
        addClient(generator, event, new EnergyOilBucketModelGenerator(generator, existingFileHelper));

        addClient(generator, event, new CoreItemModelGenerator(generator, existingFileHelper));
        addClient(generator, event, new EnergyItemModelGenerator(generator, existingFileHelper));
        addClient(generator, event, new FactoryItemModelGenerator(generator, existingFileHelper));
        addClient(generator, event, new BuildersItemModelGenerator(generator, existingFileHelper));
        addClient(generator, event, new SiliconItemModelGenerator(generator, existingFileHelper));
        addClient(generator, event, new TransportItemModelGenerator(generator, existingFileHelper));
        addClient(generator, event, new LibItemModelProvider(generator, existingFileHelper));
        addClient(generator, event, new RoboticsItemModelGenerator(generator, existingFileHelper));
    }
}
