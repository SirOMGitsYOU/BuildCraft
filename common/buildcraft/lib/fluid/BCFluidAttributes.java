package buildcraft.lib.fluid;

import org.jetbrains.annotations.Nullable;

import buildcraft.energy.BCEnergyFluids;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.common.SoundAction;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;


import java.util.function.BiFunction;
import java.util.function.Consumer;

// Calen add
public class BCFluidAttributes extends FluidType {
    private final int colour, light, dark;
    private final Identifier overlayTexture;

    private final Identifier stillTexture, flowingTexture;

    protected BCFluidAttributes(Builder builder, FluidType.Properties properties) {
        super(properties);
        this.heat = builder.heat;
        this.heatable = builder.heatable;
        this.colour = builder.colour;
        this.light = builder.light;
        this.dark = builder.dark;
        this.overlayTexture = builder.overlayTexture;
        this.stillTexture = builder.stillTexture;
        this.flowingTexture = builder.flowingTexture;
    }

    public int getLightColour() {
        return light;
    }

    public int getDarkColour() {
        return dark;
    }

    private int heat;

    public BCFluidAttributes setHeat(int heat) {
        this.heat = heat;
        return this;
    }

    public int getHeat() {
        return heat;
    }

    private boolean heatable;

    public BCFluidAttributes setHeatable(boolean value) {
        heatable = value;
        return this;
    }

    public boolean isHeatable() {
        return heatable;
    }

    public Identifier getOverlayTexture() {
        return overlayTexture;
    }

    public Identifier getStillTexture() {
        return stillTexture;
    }

    public Identifier getFlowingTexture() {
        return flowingTexture;
    }

    @Override
    public void initializeClient(Consumer<IClientFluidTypeExtensions> consumer) {
        consumer.accept(new IClientFluidTypeExtensions() {
            @Override
            public int getTintColor() {
                return colour;
            }

            @Override
            public Identifier getStillTexture() {
                return stillTexture;
            }

            @Override
            public Identifier getFlowingTexture() {
                return flowingTexture;
            }

            @Override
            public @Nullable Identifier getOverlayTexture() {
                return overlayTexture;
            }
        });
    }

    @Override
    public Component getDescription() {
        if (heat <= 0 && !isHeatable()) {
            return getBareLocalizedName();
        }
        Component name = getBareLocalizedName();
//        return Component.literal(LocaleUtil.localize("buildcraft.fluid.heat_" + heat, name));
        return Component.translatable(BCEnergyFluids.FLUID_TRANSLATION_PREFIX + heat, name);
    }

    @Override
    public Component getDescription(FluidStack stack) {
        return getDescription();
    }

    public Component getBareLocalizedName() {
//        return super.getLocalizedName(stack);
        return Component.translatable(getDescriptionId());
    }

    public static BCFluidAttributes.Builder builder(Identifier stillTexture, Identifier flowingTexture) {
        return new BCFluidAttributes.Builder(stillTexture, flowingTexture, BCFluidAttributes::new);
    }

    public static class Builder {
        private final BiFunction<Builder, Properties, BCFluidAttributes> factory;
        private final Identifier stillTexture, flowingTexture;
        private final FluidType.Properties properties = FluidType.Properties.create();

        protected Builder(Identifier stillTexture, Identifier flowingTexture, BiFunction<Builder, Properties, BCFluidAttributes> factory) {
            this.factory = factory;

            this.stillTexture = stillTexture;
            this.flowingTexture = flowingTexture;
        }

        private int colour = 0xFFFFFFFF, light = 0xFF_FF_FF_FF, dark = 0xFF_FF_FF_FF;

        public Builder setColour(int light, int dark) {
            this.light = light;
            this.dark = dark;
            this.colour = 0xFF_FF_FF_FF;
            return this;
        }

        private int heat;

        public Builder setHeat(int heat) {
            this.heat = heat;
            return this;
        }

        private boolean heatable;

        public Builder setHeatable(boolean value) {
            heatable = value;
            return this;
        }

        private Identifier overlayTexture;

        public final Builder overlay(Identifier texture) {
            overlayTexture = texture;
            return this;
        }

        public Builder descriptionId(String descriptionId) {
            this.properties.descriptionId(descriptionId);
            return this;
        }

        public Builder sound(SoundAction action, SoundEvent event) {
            this.properties.sound(action, event);
            return this;
        }

        public Builder temperature(int temperature) {
            this.properties.temperature(temperature);
            return this;
        }

        public Builder density(int density) {
            this.properties.density(density);
            return this;
        }

        public Builder viscosity(int viscosity) {
            this.properties.viscosity(viscosity);
            return this;
        }

        public BCFluidAttributes build() {
            return factory.apply(this, properties);
        }
    }
}
