/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.entity.projectile.EntityFireball
 *  net.minecraft.entity.projectile.EntityFishHook
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.projectile.EntityFireball;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class PlayerUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static BlockPos getBlockStandingOn() {
        return new BlockPos(PlayerUtil.mc.field_71439_g.field_70165_t, Math.ceil(PlayerUtil.mc.field_71439_g.field_70163_u - 0.25) - 1.0, PlayerUtil.mc.field_71439_g.field_70161_v);
    }

    public static Vec3 getPlayerEyePos() {
        return PlayerUtil.mc.field_71439_g.func_174824_e(1.0f);
    }

    public static BlockPos getBlockStandingOnFloor() {
        return new BlockPos(PlayerUtil.mc.field_71439_g.field_70165_t, Math.floor(PlayerUtil.mc.field_71439_g.field_70163_u) - 1.0, PlayerUtil.mc.field_71439_g.field_70161_v);
    }

    public static Vec3 getNextTickPosition() {
        return PlayerUtil.mc.field_71439_g.func_174791_d().func_72441_c(PlayerUtil.mc.field_71439_g.field_70159_w, 0.0, PlayerUtil.mc.field_71439_g.field_70179_y);
    }

    public static Vec3 getNextTickPosition(float mult) {
        return PlayerUtil.mc.field_71439_g.func_174791_d().func_72441_c(PlayerUtil.mc.field_71439_g.field_70159_w * (double)mult, 0.0, PlayerUtil.mc.field_71439_g.field_70179_y * (double)mult);
    }

    public static Entity getEntityCuttingOtherEntity(Entity e) {
        return PlayerUtil.getEntityCuttingOtherEntity(e, entity -> true);
    }

    public static Entity getEntityCuttingOtherEntity(Entity e, Predicate<Entity> predicate) {
        List possible = PlayerUtil.mc.field_71441_e.func_175674_a(e, e.func_174813_aQ().func_72314_b(0.3, 2.0, 0.3), a -> {
            boolean flag1 = !a.field_70128_L && !a.equals((Object)PlayerUtil.mc.field_71439_g);
            boolean flag2 = !(a instanceof EntityFireball);
            boolean flag3 = !(a instanceof EntityFishHook);
            boolean flag4 = predicate.test((Entity)a);
            return flag1 && flag2 && flag3 && flag4;
        });
        if (!possible.isEmpty()) {
            return Collections.min(possible, Comparator.comparing(e2 -> Float.valueOf(e2.func_70032_d(e))));
        }
        return null;
    }

    public static boolean isPlayerSuffocating() {
        AxisAlignedBB playerBB = PlayerUtil.mc.field_71439_g.func_174813_aQ().func_72314_b(-0.15, -0.15, -0.15);
        List collidingBoxes = PlayerUtil.mc.field_71441_e.func_72945_a((Entity)PlayerUtil.mc.field_71439_g, playerBB);
        return !collidingBoxes.isEmpty();
    }

    public static EnumFacing getHorizontalFacing(float yaw) {
        return EnumFacing.func_176731_b((int)(MathHelper.func_76128_c((double)((double)(yaw * 4.0f / 360.0f) + 0.5)) & 3));
    }
}

