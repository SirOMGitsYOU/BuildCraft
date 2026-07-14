package buildcraft.energy.generation.biome;

import buildcraft.energy.BCEnergy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BCBiomeRegistry {
    public static String BIOME_OIL_OCEAN = "oil_ocean";
    public static String BIOME_OIL_DESERT = "oil_desert";

    public static final DeferredRegister<Biome> BIOMES = DeferredRegister.create(ForgeRegistries.BIOMES, BCEnergy.MODID);

    public static final RegistryObject<Biome> OIL_DESERT = BIOMES.register(BIOME_OIL_DESERT, BCBiomes::makeOilDesertBiome);
    public static final RegistryObject<Biome> OIL_OCEAN = BIOMES.register(BIOME_OIL_OCEAN, BCBiomes::makeOilOceanBiome);

    public static final ResourceKey<Biome> RESOURCE_KEY_BIOME_OIL_DESERT = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BCEnergy.MODID, BIOME_OIL_DESERT));
    public static final ResourceKey<Biome> RESOURCE_KEY_BIOME_OIL_OCEAN = ResourceKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(BCEnergy.MODID, BIOME_OIL_OCEAN));

    public static void register(IEventBus modBus) {
        BIOMES.register(modBus);
    }

    public static void init() {
    }
}
