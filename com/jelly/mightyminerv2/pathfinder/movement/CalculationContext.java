/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.entity.EntityPlayerSP
 *  net.minecraft.client.multiplayer.WorldClient
 *  net.minecraft.potion.Potion
 *  net.minecraft.potion.PotionEffect
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.movement;

import com.jelly.mightyminerv2.pathfinder.costs.ActionCosts;
import com.jelly.mightyminerv2.pathfinder.helper.BlockStateAccessor;
import com.jelly.mightyminerv2.pathfinder.helper.player.PlayerContext;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\u0006\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0004\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0002\b\u0004\u0018\u00002\u00020\u0001B#\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0004\u001a\u00020\u0003\u0012\b\b\u0002\u0010\u0005\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0006J\u001e\u0010\"\u001a\u00020#2\u0006\u0010$\u001a\u00020\u00102\u0006\u0010%\u001a\u00020\u00102\u0006\u0010&\u001a\u00020\u0010R\u0011\u0010\u0007\u001a\u00020\b\u00a2\u0006\b\n\u0000\u001a\u0004\b\t\u0010\nR\u0011\u0010\u000b\u001a\u00020\f\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000eR\u0011\u0010\u000f\u001a\u00020\u0010\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0011\u0010\u0012R\u0014\u0010\u0013\u001a\u00020\u0010X\u0086D\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0014\u0010\u0012R\u0019\u0010\u0015\u001a\n \u0017*\u0004\u0018\u00010\u00160\u0016\u00a2\u0006\b\n\u0000\u001a\u0004\b\u0018\u0010\u0019R\u0011\u0010\u001a\u001a\u00020\u001b\u00a2\u0006\b\n\u0000\u001a\u0004\b\u001c\u0010\u001dR\u0019\u0010\u001e\u001a\n \u0017*\u0004\u0018\u00010\u001f0\u001f\u00a2\u0006\b\n\u0000\u001a\u0004\b \u0010!\u00a8\u0006'"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "", "sprintFactor", "", "walkFactor", "sneakFactor", "(DDD)V", "bsa", "Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateAccessor;", "getBsa", "()Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateAccessor;", "cost", "Lcom/jelly/mightyminerv2/pathfinder/costs/ActionCosts;", "getCost", "()Lcom/jelly/mightyminerv2/pathfinder/costs/ActionCosts;", "jumpBoostAmplifier", "", "getJumpBoostAmplifier", "()I", "maxFallHeight", "getMaxFallHeight", "player", "Lnet/minecraft/client/entity/EntityPlayerSP;", "kotlin.jvm.PlatformType", "getPlayer", "()Lnet/minecraft/client/entity/EntityPlayerSP;", "playerContext", "Lcom/jelly/mightyminerv2/pathfinder/helper/player/PlayerContext;", "getPlayerContext", "()Lcom/jelly/mightyminerv2/pathfinder/helper/player/PlayerContext;", "world", "Lnet/minecraft/client/multiplayer/WorldClient;", "getWorld", "()Lnet/minecraft/client/multiplayer/WorldClient;", "get", "Lnet/minecraft/block/state/IBlockState;", "x", "y", "z", "MightyMinerV2"})
public final class CalculationContext {
    @NotNull
    private final PlayerContext playerContext;
    private final WorldClient world;
    private final EntityPlayerSP player;
    @NotNull
    private final BlockStateAccessor bsa;
    private final int jumpBoostAmplifier;
    @NotNull
    private final ActionCosts cost;
    private final int maxFallHeight;

    public CalculationContext(double sprintFactor, double walkFactor, double sneakFactor) {
        Minecraft minecraft = Minecraft.func_71410_x();
        Intrinsics.checkNotNullExpressionValue((Object)minecraft, (String)"getMinecraft(...)");
        this.playerContext = new PlayerContext(minecraft);
        this.world = this.playerContext.getWorld();
        this.player = this.playerContext.getPlayer();
        WorldClient worldClient = this.world;
        Intrinsics.checkNotNull((Object)worldClient);
        this.bsa = new BlockStateAccessor((World)worldClient);
        PotionEffect potionEffect = this.player.func_70660_b(Potion.field_76430_j);
        this.jumpBoostAmplifier = potionEffect != null ? potionEffect.func_76458_c() : -1;
        this.cost = new ActionCosts(sprintFactor, walkFactor, sneakFactor, this.jumpBoostAmplifier);
        this.maxFallHeight = 20;
    }

    public /* synthetic */ CalculationContext(double d, double d2, double d3, int n, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n & 1) != 0) {
            d = 0.13;
        }
        if ((n & 2) != 0) {
            d2 = 0.1;
        }
        if ((n & 4) != 0) {
            d3 = 0.03;
        }
        this(d, d2, d3);
    }

    @NotNull
    public final PlayerContext getPlayerContext() {
        return this.playerContext;
    }

    public final WorldClient getWorld() {
        return this.world;
    }

    public final EntityPlayerSP getPlayer() {
        return this.player;
    }

    @NotNull
    public final BlockStateAccessor getBsa() {
        return this.bsa;
    }

    public final int getJumpBoostAmplifier() {
        return this.jumpBoostAmplifier;
    }

    @NotNull
    public final ActionCosts getCost() {
        return this.cost;
    }

    public final int getMaxFallHeight() {
        return this.maxFallHeight;
    }

    @NotNull
    public final IBlockState get(int x, int y, int z) {
        return this.bsa.get(x, y, z);
    }

    public CalculationContext() {
        this(0.0, 0.0, 0.0, 7, null);
    }
}

