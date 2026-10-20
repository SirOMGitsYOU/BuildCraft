/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.tile.item;
import org.jetbrains.annotations.NotNull;

import buildcraft.api.inventory.IItemHandlerFiltered;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;



public class DelegateItemHandler implements IItemHandlerModifiable, IItemHandlerFiltered {
    private final IItemHandlerModifiable delegate;

    public DelegateItemHandler(IItemHandlerModifiable delegate) {
        this.delegate = delegate;
    }

    @Override
    public int getSlots() {
        return delegate.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return delegate.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return delegate.insertItem(slot, stack, simulate);
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return delegate.extractItem(slot, amount, simulate);
    }

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {
        delegate.setStackInSlot(slot, stack);
    }

    @Override
    public int getSlotLimit(int slot) {
        return delegate.getSlotLimit(slot);
    }

    @Override
    public ItemStack getFilter(int slot) {
        if (delegate instanceof IItemHandlerFiltered) {
            return ((IItemHandlerFiltered) delegate).getFilter(slot);
        }
        return IItemHandlerFiltered.super.getFilter(slot);
    }

    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return delegate.isItemValid(slot, stack);
    }
}
