/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.PlayerControllerMP
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.helper.player;

import kotlin.Metadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.PlayerControllerMP;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00002\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\bf\u0018\u00002\u00020\u0001R\u0012\u0010\u0002\u001a\u00020\u0003X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0004\u0010\u0005R\u0012\u0010\u0006\u001a\u00020\u0007X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\b\u0010\tR\u0012\u0010\n\u001a\u00020\u000bX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\f\u0010\rR\u0012\u0010\u000e\u001a\u00020\u000fX\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0010\u0010\u0011R\u0012\u0010\u0012\u001a\u00020\u0013X\u00a6\u0004\u00a2\u0006\u0006\u001a\u0004\b\u0014\u0010\u0015\u00a8\u0006\u0016"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/helper/player/IPlayerContext;", "", "mc", "Lnet/minecraft/client/Minecraft;", "getMc", "()Lnet/minecraft/client/Minecraft;", "player", "Lnet/minecraft/client/entity/EntityPlayerSP;", "getPlayer", "()Lnet/minecraft/client/entity/EntityPlayerSP;", "playerController", "Lnet/minecraft/client/multiplayer/PlayerControllerMP;", "getPlayerController", "()Lnet/minecraft/client/multiplayer/PlayerControllerMP;", "playerPosition", "Lnet/minecraft/util/BlockPos;", "getPlayerPosition", "()Lnet/minecraft/util/BlockPos;", "world", "Lnet/minecraft/world/World;", "getWorld", "()Lnet/minecraft/world/World;", "MightyMinerV2"})
public interface IPlayerContext {
    @NotNull
    public Minecraft getMc();

    @NotNull
    public EntityPlayerSP getPlayer();

    @NotNull
    public PlayerControllerMP getPlayerController();

    @NotNull
    public World getWorld();

    @NotNull
    public BlockPos getPlayerPosition();
}

