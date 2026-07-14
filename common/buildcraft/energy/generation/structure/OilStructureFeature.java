package buildcraft.energy.generation.structure;

import buildcraft.energy.BCEnergyConfig;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class OilStructureFeature extends Structure {
    public static final Codec<OilStructureFeature> CODEC = simpleCodec(OilStructureFeature::new);

    public OilStructureFeature(StructureSettings settings) {
        super(settings);
    }

    @Override
    public Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        if (!BCEnergyConfig.enableOilGeneration) {
            return Optional.empty();
        }

        ChunkPos chunkPos = context.chunkPos();
        int chunkX = chunkPos.x;
        int chunkZ = chunkPos.z;

        WorldgenRandom rand = new WorldgenRandom(new LegacyRandomSource(OilGenerator.MAGIC_GEN_NUMBER));
        rand.setLargeFeatureSeed(context.seed(), chunkX, chunkZ);
        int xForGen = chunkX * 16 + 8 + rand.nextInt(16);
        int zForGen = chunkZ * 16 + 8 + rand.nextInt(16);
        Holder<Biome> biome = context.chunkGenerator().getBiomeSource().getNoiseBiome(
                QuartPos.fromBlock(xForGen),
                QuartPos.fromBlock(63),
                QuartPos.fromBlock(zForGen),
                context.randomState().sampler()
        );
        OilGenerator.GenType type = OilGenerator.getPieceTypeByRand(rand, biome, chunkX, chunkZ, xForGen, zForGen, true);
        if (type == OilGenerator.GenType.NONE) {
            return Optional.empty();
        }
        OilFeatureConfiguration.Info info = new OilFeatureConfiguration.Info(type, rand, xForGen, zForGen);
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, structurePiecesBuilder ->
                OilGenerator.generatePieces(structurePiecesBuilder, context, info)
        );
    }

    @Override
    public StructureType<?> type() {
        return OilStructureRegistry.STRUCTURE_TYPE.get();
    }
}
