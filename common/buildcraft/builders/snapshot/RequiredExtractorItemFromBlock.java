/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.builders.snapshot;
import org.jetbrains.annotations.NotNull;

import org.jetbrains.annotations.Nullable;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;



import java.util.Collections;
import java.util.List;

public class RequiredExtractorItemFromBlock extends RequiredExtractor {
    @NotNull
    @Override
    public List<ItemStack> extractItemsFromBlock(@NotNull BlockState blockState, @Nullable CompoundTag tileNbt) {
        return Collections.singletonList(
                new ItemStack(
//                        Item.getItemFromBlock(blockState.getBlock()),
                        blockState.getBlock().asItem(),
                        1
//                        blockState.getBlock().damageDropped(blockState) // 1.18.2: no meta
                )
        );
    }
}
