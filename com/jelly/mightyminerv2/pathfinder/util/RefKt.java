/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.client.renderer.Tessellator
 *  net.minecraft.client.settings.GameSettings
 */
package com.jelly.mightyminerv2.pathfinder.util;

import kotlin.Metadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;

@Metadata(mv={1, 9, 0}, k=2, xi=48, d1={"\u0000*\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\"\u0019\u0010\u0000\u001a\n \u0002*\u0004\u0018\u00010\u00010\u00018F\u00a2\u0006\u0006\u001a\u0004\b\u0003\u0010\u0004\"\u0019\u0010\u0005\u001a\n \u0002*\u0004\u0018\u00010\u00060\u00068F\u00a2\u0006\u0006\u001a\u0004\b\u0007\u0010\b\"\u0019\u0010\t\u001a\n \u0002*\u0004\u0018\u00010\n0\n8F\u00a2\u0006\u0006\u001a\u0004\b\u000b\u0010\f\"\u0019\u0010\r\u001a\n \u0002*\u0004\u0018\u00010\u000e0\u000e8F\u00a2\u0006\u0006\u001a\u0004\b\u000f\u0010\u0010\"\u0019\u0010\u0011\u001a\n \u0002*\u0004\u0018\u00010\u00120\u00128F\u00a2\u0006\u0006\u001a\u0004\b\u0013\u0010\u0014\u00a8\u0006\u0015"}, d2={"gameSettings", "Lnet/minecraft/client/settings/GameSettings;", "kotlin.jvm.PlatformType", "getGameSettings", "()Lnet/minecraft/client/settings/GameSettings;", "mc", "Lnet/minecraft/client/Minecraft;", "getMc", "()Lnet/minecraft/client/Minecraft;", "player", "Lnet/minecraft/client/entity/EntityPlayerSP;", "getPlayer", "()Lnet/minecraft/client/entity/EntityPlayerSP;", "tessellator", "Lnet/minecraft/client/renderer/Tessellator;", "getTessellator", "()Lnet/minecraft/client/renderer/Tessellator;", "world", "Lnet/minecraft/client/multiplayer/WorldClient;", "getWorld", "()Lnet/minecraft/client/multiplayer/WorldClient;", "MightyMinerV2"})
public final class RefKt {
    public static final Minecraft getMc() {
        return Minecraft.func_71410_x();
    }

    public static final EntityPlayerSP getPlayer() {
        return RefKt.getMc().field_71439_g;
    }

    public static final WorldClient getWorld() {
        return RefKt.getMc().field_71441_e;
    }

    public static final Tessellator getTessellator() {
        return Tessellator.func_178181_a();
    }

    public static final GameSettings getGameSettings() {
        return RefKt.getMc().field_71474_y;
    }
}

