/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.world.World
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Redirect
 */
package com.jelly.mightyminerv2.mixin.client;

import com.jelly.mightyminerv2.util.StrafeUtil;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value={EntityLivingBase.class})
public abstract class MixinEntityLivingBase
extends Entity {
    @Shadow
    public float field_70759_as;

    public MixinEntityLivingBase(World worldIn) {
        super(worldIn);
    }

    @Redirect(method={"jump"}, at=@At(value="FIELD", target="net/minecraft/entity/EntityLivingBase.rotationYaw:F"))
    private float overrideYaw(EntityLivingBase self) {
        if (self instanceof EntityPlayerSP && StrafeUtil.shouldEnable()) {
            return StrafeUtil.yaw;
        }
        return self.field_70177_z;
    }

    @Redirect(method={"moveEntityWithHeading"}, at=@At(value="INVOKE", target="Lnet/minecraft/entity/EntityLivingBase;moveFlying(FFF)V"))
    public void moveRelative(EntityLivingBase instance, float s, float f, float fr) {
        if (!StrafeUtil.shouldEnable()) {
            this.func_70060_a(s, f, fr);
            return;
        }
        float originalYaw = this.field_70177_z;
        this.field_70177_z = StrafeUtil.yaw;
        this.func_70060_a(s, f, fr);
        this.field_70177_z = originalYaw;
    }
}

