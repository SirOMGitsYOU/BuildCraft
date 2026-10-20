/*
 * Copyright (c) 2017 SpaceToad and the BuildCraft team
 * This Source Code Form is subject to the terms of the Mozilla Public License, v. 2.0. If a copy of the MPL was not
 * distributed with this file, You can obtain one at https://mozilla.org/MPL/2.0/
 */

package buildcraft.lib.fake;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.block.entity.SignBlockEntity;

public class FakePlayerBC extends ServerPlayer {
    public FakePlayerBC(ServerLevel world, GameProfile name) {
        super(world.getServer(), world, name, ClientInformation.createDefault());
    }

    @Override
    public void openTextEdit(SignBlockEntity sign, boolean front) {
        // Fake players must not open GUIs
    }

    @Override
    public boolean isFakePlayer() {
        return true;
    }
}
