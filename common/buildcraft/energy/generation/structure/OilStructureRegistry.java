package buildcraft.energy.generation.structure;

import buildcraft.energy.BCEnergy;
import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.structure.StructureType;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OilStructureRegistry {
    public static String STRUCTURE_OIL_SPOUT = "oil_spout";
    public static final ResourceLocation STRUCTURE_ID = new ResourceLocation(BCEnergy.MODID, STRUCTURE_OIL_SPOUT);

    private static final DeferredRegister<StructurePieceType> PIECE_TYPES =
            DeferredRegister.create(Registry.STRUCTURE_PIECE_REGISTRY, BCEnergy.MODID);

    private static final DeferredRegister<StructureType<?>> STRUCTURE_TYPES =
            DeferredRegister.create(Registry.STRUCTURE_TYPE_REGISTRY, BCEnergy.MODID);

    public static final RegistryObject<StructurePieceType> STRUCTURE_PIECE_TYPE = PIECE_TYPES.register(
            STRUCTURE_OIL_SPOUT,
            () -> (StructurePieceType.ContextlessType) OilStructure::deserialize
    );

    public static final RegistryObject<StructureType<OilStructureFeature>> STRUCTURE_TYPE = STRUCTURE_TYPES.register(
            STRUCTURE_OIL_SPOUT,
            () -> () -> OilStructureFeature.CODEC
    );

    public static void register(IEventBus modBus) {
        PIECE_TYPES.register(modBus);
        STRUCTURE_TYPES.register(modBus);
    }
}
