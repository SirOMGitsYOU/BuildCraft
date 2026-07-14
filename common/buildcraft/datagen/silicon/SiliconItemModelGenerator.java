package buildcraft.datagen.silicon;

import buildcraft.datagen.base.BCBaseItemModelGenerator;
import buildcraft.silicon.BCSilicon;
import buildcraft.silicon.BCSiliconBlocks;
import buildcraft.silicon.BCSiliconItems;
import buildcraft.silicon.client.SiliconItemModelPredicates;
import net.minecraft.data.DataGenerator;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.ExistingFileHelper;

import javax.annotation.Nonnull;

public class SiliconItemModelGenerator extends BCBaseItemModelGenerator {
    public SiliconItemModelGenerator(DataGenerator generator, ExistingFileHelper existingFileHelper) {
        super(generator, BCSilicon.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        // gates
        BCSiliconItems.variantGateMap.values().forEach(
                reg ->
                        getBuilder(reg.getId().toString()).parent(BUILTIN_ENTITY)
        );

        // plugLens
        getBuilder(BCSiliconItems.plugLens.getId().toString()).parent(BUILTIN_ENTITY);
        // plugPulsar
        getBuilder(BCSiliconItems.plugPulsar.getId().toString()).parent(BUILTIN_ENTITY);
        // plugLightSensor
        getBuilder(BCSiliconItems.plugLightSensor.getId().toString()).parent(BUILTIN_ENTITY);
        // plugTimer
        getBuilder(BCSiliconItems.plugTimer.getId().toString()).parent(BUILTIN_ENTITY);
        // plugFacade
        getBuilder(BCSiliconItems.plugFacade.getId().toString()).parent(BUILTIN_ENTITY);

        // chipsets
        withExistingParent(BCSiliconItems.chipsetRedstone.getId().toString(), GENERATED)
                .texture("layer0", "buildcraftsilicon:items/redstone_chipset/red");
        withExistingParent(BCSiliconItems.chipsetDiamond.getId().toString(), GENERATED)
                .texture("layer0", "buildcraftsilicon:items/redstone_chipset/diamond");
        withExistingParent(BCSiliconItems.chipsetGold.getId().toString(), GENERATED)
                .texture("layer0", "buildcraftsilicon:items/redstone_chipset/gold");
        withExistingParent(BCSiliconItems.chipsetIron.getId().toString(), GENERATED)
                .texture("layer0", "buildcraftsilicon:items/redstone_chipset/iron");
        withExistingParent(BCSiliconItems.chipsetQuartz.getId().toString(), GENERATED)
                .texture("layer0", "buildcraftsilicon:items/redstone_chipset/quartz");

        // tables
        withExistingParent(BCSiliconBlocks.advancedCraftingTable.getId().toString(), new ResourceLocation("buildcraftsilicon:block/table/advanced_crafting"));
        withExistingParent(BCSiliconBlocks.assemblyTable.getId().toString(), new ResourceLocation("buildcraftsilicon:block/table/assembly"));
        withExistingParent(BCSiliconBlocks.chargingTable.getId().toString(), new ResourceLocation("buildcraftsilicon:block/table/charging"));
        withExistingParent(BCSiliconBlocks.integrationTable.getId().toString(), new ResourceLocation("buildcraftsilicon:block/table/integration"));
        withExistingParent(BCSiliconBlocks.programmingTable.getId().toString(), new ResourceLocation("buildcraftsilicon:block/table/programming"));

        // laser
        withExistingParent(BCSiliconBlocks.laser.getId().toString(), new ResourceLocation("buildcraftsilicon:block/laser"));

        // gate_copier
        ResourceLocation gateCopier = BCSiliconItems.gateCopier.getId();
        getBuilder(gateCopier.toString())
                .override()
                .model(
                        withExistingParent(gateCopier.getNamespace() + ":item/" + gateCopier.getPath() + "/empty", GENERATED)
                                .texture("layer0", "buildcraftsilicon:items/gatecopier/empty")
                )
                .predicate(SiliconItemModelPredicates.PREDICATE_HAS_DATA, 0)
                .end()
                .override()
                .model(
                        withExistingParent(gateCopier.getNamespace() + ":item/" + gateCopier.getPath() + "/full", GENERATED)
                                .texture("layer0", "buildcraftsilicon:items/gatecopier/full")
                )
                .predicate(SiliconItemModelPredicates.PREDICATE_HAS_DATA, 1)
                .end()
        ;

        // redstoneCrystal
        withExistingParent(BCSiliconItems.redstoneCrystal.getId().toString(), GENERATED)
                .texture("layer0", "buildcraftsilicon:items/redstone_crystal");
    }

    @Nonnull
    @Override
    public String getName() {
        return "BuildCraft Silicon Item Model Generator";
    }
}
