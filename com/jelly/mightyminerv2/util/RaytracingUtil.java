/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.entity.Entity
 *  net.minecraft.util.MovingObjectPosition
 *  net.minecraft.util.Vec3
 */
package com.jelly.mightyminerv2.util;

import com.jelly.mightyminerv2.util.PlayerUtil;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;

public class RaytracingUtil {
    private static final Minecraft mc = Minecraft.func_71410_x();

    public static boolean canSeePoint(Vec3 point) {
        return RaytracingUtil.canSeePoint(PlayerUtil.getPlayerEyePos(), point);
    }

    public static boolean canSeePoint(Vec3 from, Vec3 point) {
        MovingObjectPosition result = RaytracingUtil.raytrace(from, point);
        if (result == null) {
            return true;
        }
        Vec3 r = result.field_72307_f;
        if (r == null) {
            return false;
        }
        return Math.abs(r.field_72450_a - point.field_72450_a) < (double)0.1f && Math.abs(r.field_72448_b - point.field_72448_b) < (double)0.1f && Math.abs(r.field_72449_c - point.field_72449_c) < (double)0.1f;
    }

    public static MovingObjectPosition raytraceTowards(Vec3 v1, Vec3 v2, double distance) {
        Vec3 normalized = v2.func_178788_d(v1).func_72432_b();
        return RaytracingUtil.raytrace(v1, v1.func_178787_e(new Vec3(normalized.field_72450_a * distance, normalized.field_72448_b * distance, normalized.field_72449_c * distance)));
    }

    public static MovingObjectPosition raytrace(Vec3 v1, Vec3 v2) {
        Vec3 v3 = v2.func_178788_d(v1);
        List entities = RaytracingUtil.mc.field_71441_e.func_175674_a((Entity)RaytracingUtil.mc.field_71439_g, RaytracingUtil.mc.field_71439_g.func_174813_aQ().func_72321_a(v3.field_72450_a, v3.field_72448_b, v3.field_72449_c).func_72314_b(1.0, 1.0, 1.0), it -> it.func_70089_S() && it.func_70067_L());
        for (Entity entity : entities) {
            MovingObjectPosition intercept = entity.func_174813_aQ().func_72314_b(0.5, 0.5, 0.5).func_72327_a(v1, v2);
            if (intercept == null) continue;
            return new MovingObjectPosition(entity, intercept.field_72307_f);
        }
        return RaytracingUtil.mc.field_71441_e.func_147447_a(v1, v2, false, true, false);
    }
}

