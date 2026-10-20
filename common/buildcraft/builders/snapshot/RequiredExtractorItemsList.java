/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.builders.snapshot;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import buildcraft.lib.misc.NBTUtilBC;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;



import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class RequiredExtractorItemsList extends RequiredExtractor {
    private NbtPath path = null;

    @NotNull
    @Override
    public List<ItemStack> extractItemsFromBlock(@NotNull BlockState blockState, @Nullable CompoundTag tileNbt) {
        return Optional.ofNullable(path.get(tileNbt))
                .map(NBTUtilBC::readCompoundList)
//                .map(stream -> stream.map(ItemStack::new).collect(Collectors.toList()))
                .map(stream -> stream.map(ItemStack::of).collect(Collectors.toList()))
                .map(Collections::unmodifiableList)
                .orElseGet(Collections::emptyList);
    }

    @NotNull
    @Override
    public List<ItemStack> extractItemsFromEntity(@NotNull CompoundTag entityNbt) {
        return Optional.ofNullable(path.get(entityNbt))
                .map(NBTUtilBC::readCompoundList)
//                .map(stream -> stream.map(ItemStack::new).collect(Collectors.toList()))
                .map(stream -> stream.map(ItemStack::of).collect(Collectors.toList()))
                .map(Collections::unmodifiableList)
                .orElseGet(Collections::emptyList);
    }
}
