package buildcraft.energy.generation.structure;

import buildcraft.api.core.BCLog;
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

import java.util.Locale;
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

        GenerationRoll roll = rollChunk(context, chunkX, chunkZ, false);
        if (roll.type == OilGenerator.GenType.NONE) {
            return Optional.empty();
        }
        if (isBlockedByCloserOil(context, chunkX, chunkZ)) {
            return Optional.empty();
        }
        if (OilGenerator.DEBUG_OILGEN_BASIC) {
            BCLog.logger.info(
                    "[energy.oilgen] Generating an oil well (" + roll.type.name().toLowerCase(Locale.ROOT)
                            + ") in chunk " + chunkX + ", " + chunkZ + " at " + roll.xForGen + ", " + roll.zForGen
            );
        }
        OilFeatureConfiguration.Info info = new OilFeatureConfiguration.Info(roll.type, roll.rand, roll.xForGen, roll.zForGen);
        return onTopOfChunkCenter(context, Heightmap.Types.WORLD_SURFACE_WG, structurePiecesBuilder ->
                OilGenerator.generatePieces(structurePiecesBuilder, context, info)
        );
    }

    /** When two oil candidates fall within {@link BCEnergyConfig#oilMinChunkSeparation}, keep only the chunk with the
     * lower priority key so results stay deterministic across chunk generation order. */
    private static boolean isBlockedByCloserOil(GenerationContext context, int chunkX, int chunkZ) {
        int gap = BCEnergyConfig.oilMinChunkSeparation;
        if (gap <= 0) {
            return false;
        }
        long myPriority = chunkPriority(context.seed(), chunkX, chunkZ);
        for (int dx = -gap; dx <= gap; dx++) {
            for (int dz = -gap; dz <= gap; dz++) {
                if (dx == 0 && dz == 0) {
                    continue;
                }
                // Chebyshev distance matches "at least N chunks away in any direction"
                if (Math.max(Math.abs(dx), Math.abs(dz)) > gap) {
                    continue;
                }
                int nx = chunkX + dx;
                int nz = chunkZ + dz;
                if (rollChunk(context, nx, nz, false).type == OilGenerator.GenType.NONE) {
                    continue;
                }
                long otherPriority = chunkPriority(context.seed(), nx, nz);
                if (otherPriority < myPriority) {
                    if (OilGenerator.DEBUG_OILGEN_BASIC) {
                        BCLog.logger.info(
                                "[energy.oilgen] Skipping oil in chunk " + chunkX + ", " + chunkZ
                                        + " because chunk " + nx + ", " + nz
                                        + " also rolled oil within minChunkSeparation=" + gap
                        );
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private static GenerationRoll rollChunk(GenerationContext context, int chunkX, int chunkZ, boolean log) {
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
        OilGenerator.GenType type = OilGenerator.getPieceTypeByRand(rand, biome, chunkX, chunkZ, xForGen, zForGen, log);
        return new GenerationRoll(type, rand, xForGen, zForGen);
    }

    private static long chunkPriority(long worldSeed, int chunkX, int chunkZ) {
        // Stable tie-break independent of generation order
        long mix = worldSeed ^ OilGenerator.MAGIC_GEN_NUMBER;
        mix ^= (long) chunkX * 341873128712L;
        mix ^= (long) chunkZ * 132897987541L;
        mix ^= mix >>> 33;
        mix *= 0xff51afd7ed558ccdL;
        mix ^= mix >>> 33;
        return mix;
    }

    private static final class GenerationRoll {
        final OilGenerator.GenType type;
        final WorldgenRandom rand;
        final int xForGen;
        final int zForGen;

        GenerationRoll(OilGenerator.GenType type, WorldgenRandom rand, int xForGen, int zForGen) {
            this.type = type;
            this.rand = rand;
            this.xForGen = xForGen;
            this.zForGen = zForGen;
        }
    }

    @Override
    public StructureType<?> type() {
        return OilStructureRegistry.STRUCTURE_TYPE.get();
    }
}
