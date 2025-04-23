/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.entity.player.InventoryPlayer
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package com.jelly.mightyminerv2.mixin.gui;

import com.jelly.mightyminerv2.macro.MacroManager;
import net.minecraft.entity.player.InventoryPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value={InventoryPlayer.class})
public abstract class MixinInventoryPlayer {
    @Inject(method={"changeCurrentItem"}, at={@At(value="HEAD")}, cancellable=true)
    public void changeCurrentItem(int direction, CallbackInfo ci) {
        if (MacroManager.getInstance().isRunning()) {
            ci.cancel();
        }
    }
}

