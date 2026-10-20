/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.builders.snapshot;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;



import java.util.Collections;
import java.util.List;
import java.util.Optional;

public class RequiredExtractorTank extends RequiredExtractor {
    private NbtPath path = null;

    @NotNull
    @Override
    public List<FluidStack> extractFluidsFromBlock(@NotNull BlockState blockState, @Nullable CompoundTag tileNbt) {
        return Optional.ofNullable(path.get(tileNbt))
                .map(CompoundTag.class::cast)
                .map(nbt -> !nbt.contains("Empty") ? FluidStack.loadFluidStackFromNBT(nbt) : null)
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }

    @NotNull
    @Override
    public List<FluidStack> extractFluidsFromEntity(@NotNull CompoundTag entityNbt) {
        return Optional.ofNullable(path.get(entityNbt))
                .map(CompoundTag.class::cast)
                .map(nbt -> !nbt.contains("Empty") ? FluidStack.loadFluidStackFromNBT(nbt) : null)
                .map(Collections::singletonList)
                .orElseGet(Collections::emptyList);
    }
}
