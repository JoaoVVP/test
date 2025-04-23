/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.util.BlockPos
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.helper.player;

import com.jelly.mightyminerv2.pathfinder.helper.player.IPlayerContext;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.util.BlockPos;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004R\u0014\u0010\u0002\u001a\u00020\u0003X\u0096\u0004\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0005\u0010\u0006R\u001c\u0010\u0007\u001a\n \t*\u0004\u0018\u00010\b0\b8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\n\u0010\u000bR\u001c\u0010\f\u001a\n \t*\u0004\u0018\u00010\r0\r8VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u000e\u0010\u000fR\u0014\u0010\u0010\u001a\u00020\u00118VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0012\u0010\u0013R\u001c\u0010\u0014\u001a\n \t*\u0004\u0018\u00010\u00150\u00158VX\u0096\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0016\u0010\u0017\u00a8\u0006\u0018"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/helper/player/PlayerContext;", "Lcom/jelly/mightyminerv2/pathfinder/helper/player/IPlayerContext;", "mc", "Lnet/minecraft/client/Minecraft;", "(Lnet/minecraft/client/Minecraft;)V", "getMc", "()Lnet/minecraft/client/Minecraft;", "player", "Lnet/minecraft/client/entity/EntityPlayerSP;", "kotlin.jvm.PlatformType", "getPlayer", "()Lnet/minecraft/client/entity/EntityPlayerSP;", "playerController", "Lnet/minecraft/client/multiplayer/PlayerControllerMP;", "getPlayerController", "()Lnet/minecraft/client/multiplayer/PlayerControllerMP;", "playerPosition", "Lnet/minecraft/util/BlockPos;", "getPlayerPosition", "()Lnet/minecraft/util/BlockPos;", "world", "Lnet/minecraft/client/multiplayer/WorldClient;", "getWorld", "()Lnet/minecraft/client/multiplayer/WorldClient;", "MightyMinerV2"})
public final class PlayerContext
implements IPlayerContext {
    @NotNull
    private final Minecraft mc;

    public PlayerContext(@NotNull Minecraft mc) {
        Intrinsics.checkNotNullParameter((Object)mc, (String)"mc");
        this.mc = mc;
    }

    @Override
    @NotNull
    public Minecraft getMc() {
        return this.mc;
    }

    @Override
    public EntityPlayerSP getPlayer() {
        return this.getMc().field_71439_g;
    }

    @Override
    public PlayerControllerMP getPlayerController() {
        return this.getMc().field_71442_b;
    }

    public WorldClient getWorld() {
        return this.getMc().field_71441_e;
    }

    @Override
    @NotNull
    public BlockPos getPlayerPosition() {
        return new BlockPos(this.getPlayer().field_70165_t, Math.ceil(this.getPlayer().field_70163_u) - 1.0, this.getPlayer().field_70161_v);
    }
}

