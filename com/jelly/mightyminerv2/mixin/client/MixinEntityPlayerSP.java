/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.authlib.GameProfile
 *  net.minecraft.client.entity.AbstractClientPlayer
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.item.EntityItem
 *  net.minecraft.world.World
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.fml.common.eventhandler.Event
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Unique
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.Redirect
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable
 */
package com.jelly.mightyminerv2.mixin.client;

import com.jelly.mightyminerv2.event.MotionUpdateEvent;
import com.jelly.mightyminerv2.macro.MacroManager;
import com.mojang.authlib.GameProfile;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value={EntityPlayerSP.class}, priority=0x7FFFFFFF)
public abstract class MixinEntityPlayerSP
extends AbstractClientPlayer {
    @Unique
    float mightyMinerv2$serverYaw = 0.0f;
    @Unique
    float mightyMinerv2$serverPitch = 0.0f;

    public MixinEntityPlayerSP(World worldIn, GameProfile playerProfile) {
        super(worldIn, playerProfile);
    }

    @Inject(method={"onUpdateWalkingPlayer"}, at={@At(value="HEAD")})
    public void onUpdateWalkingPlayerPRE(CallbackInfo ci) {
        MotionUpdateEvent event = new MotionUpdateEvent(this.field_70177_z, this.field_70125_A);
        MinecraftForge.EVENT_BUS.post((Event)event);
        this.mightyMinerv2$serverYaw = event.yaw;
        this.mightyMinerv2$serverPitch = event.pitch;
    }

    @Redirect(method={"onUpdateWalkingPlayer"}, at=@At(value="FIELD", target="Lnet/minecraft/client/entity/EntityPlayerSP;rotationYaw:F", opcode=180))
    public float onUpdateWalkingPlayerYaw(EntityPlayerSP instance) {
        return this.mightyMinerv2$serverYaw;
    }

    @Redirect(method={"onUpdateWalkingPlayer"}, at=@At(value="FIELD", target="Lnet/minecraft/client/entity/EntityPlayerSP;rotationPitch:F", opcode=180))
    public float onUpdateWalkingPlayerPitch(EntityPlayerSP instance) {
        return this.mightyMinerv2$serverPitch;
    }

    @Inject(method={"dropOneItem"}, at={@At(value="HEAD")}, cancellable=true)
    public void onDropOneItem(boolean dropAll, CallbackInfoReturnable<EntityItem> cir) {
        if (MacroManager.getInstance().isRunning()) {
            cir.setReturnValue(null);
            cir.cancel();
        }
    }
}

