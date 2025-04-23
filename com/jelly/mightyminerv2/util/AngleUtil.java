/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 */
package com.jelly.mightyminerv2.util;

import com.jelly.mightyminerv2.mixin.client.MinecraftAccessor;
import com.jelly.mightyminerv2.util.helper.Angle;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;

public class AngleUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();
    private static final double randomAddition = Math.random() * 0.3 - 0.15;

    public static Angle getPlayerAngle() {
        return new Angle(AngleUtil.get360RotationYaw(), AngleUtil.mc.field_71439_g.field_70125_A);
    }

    public static float get360RotationYaw(float yaw) {
        return (yaw % 360.0f + 360.0f) % 360.0f;
    }

    public static float normalizeAngle(float yaw) {
        float newYaw = yaw % 360.0f;
        if (newYaw < -180.0f) {
            newYaw += 360.0f;
        }
        if (newYaw > 180.0f) {
            newYaw -= 360.0f;
        }
        return newYaw;
    }

    public static float get360RotationYaw() {
        if (AngleUtil.mc.field_71439_g == null) {
            return 0.0f;
        }
        return AngleUtil.get360RotationYaw(AngleUtil.mc.field_71439_g.field_70177_z);
    }

    public static float clockwiseDifference(float initialYaw360, float targetYaw360) {
        return AngleUtil.get360RotationYaw(targetYaw360 - initialYaw360);
    }

    public static float antiClockwiseDifference(float initialYaw360, float targetYaw360) {
        return AngleUtil.get360RotationYaw(initialYaw360 - targetYaw360);
    }

    public static float smallestAngleDifference(float initialYaw360, float targetYaw360) {
        return Math.min(AngleUtil.clockwiseDifference(initialYaw360, targetYaw360), AngleUtil.antiClockwiseDifference(initialYaw360, targetYaw360));
    }

    public static Vec3 getVectorForRotation(float pitch, float yaw) {
        float f = MathHelper.func_76134_b((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI));
        float f1 = MathHelper.func_76126_a((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI));
        float f2 = -MathHelper.func_76134_b((float)(-pitch * ((float)Math.PI / 180)));
        float f3 = MathHelper.func_76126_a((float)(-pitch * ((float)Math.PI / 180)));
        return new Vec3((double)(f1 * f2), (double)f3, (double)(f * f2));
    }

    public static Vec3 getVectorForRotation(float yaw) {
        return new Vec3((double)(-MathHelper.func_76126_a((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI))), 0.0, (double)(-MathHelper.func_76134_b((float)(-yaw * ((float)Math.PI / 180) - (float)Math.PI))));
    }

    public static Angle getRotation(Vec3 to) {
        return AngleUtil.getRotation(AngleUtil.mc.field_71439_g.func_174824_e(((MinecraftAccessor)AngleUtil.mc).getTimer().field_74281_c), to);
    }

    public static Angle getRotation(Entity to) {
        return AngleUtil.getRotation(AngleUtil.mc.field_71439_g.func_174824_e(((MinecraftAccessor)AngleUtil.mc).getTimer().field_74281_c), to.func_174791_d().func_72441_c(0.0, Math.min((double)to.field_70131_O * 0.85 + randomAddition, 1.7), 0.0));
    }

    public static Angle getRotation(BlockPos pos) {
        return AngleUtil.getRotation(AngleUtil.mc.field_71439_g.func_174824_e(((MinecraftAccessor)AngleUtil.mc).getTimer().field_74281_c), new Vec3((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5));
    }

    public static Angle getRotation(Vec3 from, BlockPos pos) {
        return AngleUtil.getRotation(from, new Vec3((Vec3i)pos).func_72441_c(0.5, 0.5, 0.5));
    }

    public static Angle getRotation(Vec3 from, Vec3 to) {
        double xDiff = to.field_72450_a - from.field_72450_a;
        double yDiff = to.field_72448_b - from.field_72448_b;
        double zDiff = to.field_72449_c - from.field_72449_c;
        double dist = Math.sqrt(xDiff * xDiff + zDiff * zDiff);
        float yaw = (float)Math.toDegrees(Math.atan2(zDiff, xDiff)) - 90.0f;
        float pitch = (float)(-Math.toDegrees(Math.atan2(yDiff, dist)));
        return new Angle(yaw, pitch);
    }

    public static float getRotationYaw(Vec3 to) {
        return (float)(-Math.toDegrees(Math.atan2(to.field_72450_a - AngleUtil.mc.field_71439_g.field_70165_t, to.field_72449_c - AngleUtil.mc.field_71439_g.field_70161_v)));
    }

    public static float getRotationYaw(Vec3 from, Vec3 to) {
        return (float)(-Math.toDegrees(Math.atan2(to.field_72450_a - from.field_72450_a, to.field_72449_c - from.field_72449_c)));
    }

    public static float getRotationYaw360(Vec3 to) {
        float yaw = (float)(-Math.toDegrees(Math.atan2(to.field_72450_a - AngleUtil.mc.field_71439_g.field_70165_t, to.field_72449_c - AngleUtil.mc.field_71439_g.field_70161_v)));
        if (yaw < 0.0f) {
            return yaw + 360.0f;
        }
        return yaw;
    }

    public static float getRotationYaw360(Vec3 from, Vec3 to) {
        float yaw = (float)(-Math.toDegrees(Math.atan2(to.field_72450_a - from.field_72450_a, to.field_72449_c - from.field_72449_c)));
        if (yaw < 0.0f) {
            return yaw + 360.0f;
        }
        return yaw;
    }

    public static float getNeededYawChange(float start2, float end) {
        return AngleUtil.normalizeAngle(end - start2);
    }

    public static Angle getNeededChange(Angle startAngle, Angle endAngle) {
        float yawChange = AngleUtil.normalizeAngle(AngleUtil.normalizeAngle(endAngle.getYaw()) - AngleUtil.normalizeAngle(startAngle.getYaw()));
        return new Angle(yawChange, endAngle.getPitch() - startAngle.getPitch());
    }

    public static boolean isLookingAtDebug(Vec3 vec, float distance) {
        System.out.println("PlayerAngle: " + AngleUtil.getPlayerAngle());
        System.out.println("RotationForVec: " + AngleUtil.getRotation(vec));
        Angle change = AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(vec));
        System.out.println("Change: " + change + ", Dist: " + distance);
        return Math.abs(change.getYaw()) <= distance && Math.abs(change.getPitch()) <= distance;
    }

    public static boolean isLookingAt(Vec3 vec, float distance) {
        Angle change = AngleUtil.getNeededChange(AngleUtil.getPlayerAngle(), AngleUtil.getRotation(vec));
        return Math.abs(change.getYaw()) <= distance && Math.abs(change.getPitch()) <= distance;
    }
}

