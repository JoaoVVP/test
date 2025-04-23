/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.settings.KeyBinding
 *  net.minecraft.entity.EntityLivingBase
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.Vec3
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.util;

import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.BlockPos;
import net.minecraft.util.Vec3;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000&\n\u0000\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\u001a\n\u0010\u0000\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0003\u001a\u00020\u0001*\u00020\u0002\u001a\n\u0010\u0004\u001a\u00020\u0001*\u00020\u0002\u001a\u0012\u0010\u0005\u001a\u00020\u0006*\u00020\u00072\u0006\u0010\b\u001a\u00020\t\u001a\n\u0010\n\u001a\u00020\u0001*\u00020\u000b\u001a\n\u0010\f\u001a\u00020\u000b*\u00020\u0001\u001a\n\u0010\r\u001a\u00020\u000b*\u00020\u0001\u00a8\u0006\u000e"}, d2={"getStandingOnCeil", "Lnet/minecraft/util/BlockPos;", "Lnet/minecraft/entity/EntityLivingBase;", "getStandingOnFloor", "lastTickPositionCeil", "setPressed", "", "Lnet/minecraft/client/settings/KeyBinding;", "pressed", "", "toBlockPos", "Lnet/minecraft/util/Vec3;", "toVec3", "toVec3Top", "MightyMinerV2"})
public final class ExtensionKt {
    @NotNull
    public static final BlockPos getStandingOnCeil(@NotNull EntityLivingBase $this$getStandingOnCeil) {
        Intrinsics.checkNotNullParameter((Object)$this$getStandingOnCeil, (String)"<this>");
        return new BlockPos($this$getStandingOnCeil.field_70165_t, Math.ceil($this$getStandingOnCeil.field_70163_u) - 1.0, $this$getStandingOnCeil.field_70161_v);
    }

    @NotNull
    public static final BlockPos getStandingOnFloor(@NotNull EntityLivingBase $this$getStandingOnFloor) {
        Intrinsics.checkNotNullParameter((Object)$this$getStandingOnFloor, (String)"<this>");
        return new BlockPos($this$getStandingOnFloor.field_70165_t, Math.floor($this$getStandingOnFloor.field_70163_u) - 1.0, $this$getStandingOnFloor.field_70161_v);
    }

    public static final void setPressed(@NotNull KeyBinding $this$setPressed, boolean pressed) {
        Intrinsics.checkNotNullParameter((Object)$this$setPressed, (String)"<this>");
        KeyBinding.func_74510_a((int)$this$setPressed.func_151463_i(), (boolean)pressed);
    }

    @NotNull
    public static final Vec3 toVec3(@NotNull BlockPos $this$toVec3) {
        Intrinsics.checkNotNullParameter((Object)$this$toVec3, (String)"<this>");
        return new Vec3((double)$this$toVec3.func_177958_n() + 0.5, (double)$this$toVec3.func_177956_o() + 0.5, (double)$this$toVec3.func_177952_p() + 0.5);
    }

    @NotNull
    public static final Vec3 toVec3Top(@NotNull BlockPos $this$toVec3Top) {
        Intrinsics.checkNotNullParameter((Object)$this$toVec3Top, (String)"<this>");
        Vec3 vec3 = ExtensionKt.toVec3($this$toVec3Top).func_72441_c(0.0, 0.5, 0.0);
        Intrinsics.checkNotNullExpressionValue((Object)vec3, (String)"addVector(...)");
        return vec3;
    }

    @NotNull
    public static final BlockPos toBlockPos(@NotNull Vec3 $this$toBlockPos) {
        Intrinsics.checkNotNullParameter((Object)$this$toBlockPos, (String)"<this>");
        return new BlockPos($this$toBlockPos.field_72450_a, $this$toBlockPos.field_72448_b, $this$toBlockPos.field_72449_c);
    }

    @NotNull
    public static final BlockPos lastTickPositionCeil(@NotNull EntityLivingBase $this$lastTickPositionCeil) {
        Intrinsics.checkNotNullParameter((Object)$this$lastTickPositionCeil, (String)"<this>");
        return new BlockPos($this$lastTickPositionCeil.field_70142_S, Math.ceil($this$lastTickPositionCeil.field_70137_T) - 1.0, $this$lastTickPositionCeil.field_70136_U);
    }
}

