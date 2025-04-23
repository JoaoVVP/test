/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.movement.movements;

import com.jelly.mightyminerv2.MightyMiner;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.Movement;
import com.jelly.mightyminerv2.pathfinder.movement.MovementHelper;
import com.jelly.mightyminerv2.pathfinder.movement.MovementResult;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016\u00a8\u0006\u000f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/movements/MovementDescend;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Movement;", "mm", "Lcom/jelly/mightyminerv2/MightyMiner;", "from", "Lnet/minecraft/util/BlockPos;", "to", "(Lcom/jelly/mightyminerv2/MightyMiner;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/BlockPos;)V", "calculateCost", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "Companion", "MightyMinerV2"})
public final class MovementDescend
extends Movement {
    @NotNull
    public static final Companion Companion = new Companion(null);

    public MovementDescend(@NotNull MightyMiner mm, @NotNull BlockPos from, @NotNull BlockPos to) {
        Intrinsics.checkNotNullParameter((Object)mm, (String)"mm");
        Intrinsics.checkNotNullParameter((Object)from, (String)"from");
        Intrinsics.checkNotNullParameter((Object)to, (String)"to");
        super(mm, from, to);
    }

    @Override
    public void calculateCost(@NotNull CalculationContext ctx, @NotNull MovementResult res) {
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        Intrinsics.checkNotNullParameter((Object)res, (String)"res");
        Companion.calculateCost(ctx, this.getSource().func_177958_n(), this.getSource().func_177956_o(), this.getSource().func_177952_p(), this.getDest().func_177958_n(), this.getDest().func_177952_p(), res);
        this.setCosts(res.getCost());
    }

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J>\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eJ@\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0002JF\u0010\u0010\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\u0011\u001a\u00020\u00122\u0006\u0010\r\u001a\u00020\u000e\u00a8\u0006\u0013"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/movements/MovementDescend$Companion;", "", "()V", "calculateCost", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "x", "", "y", "z", "destX", "destZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "cost", "freeFallCost", "destState", "Lnet/minecraft/block/state/IBlockState;", "MightyMinerV2"})
    public static final class Companion {
        private Companion() {
        }

        public final void calculateCost(@NotNull CalculationContext ctx, int x, int y, int z, int destX, int destZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            res.set(destX, y - 1, destZ);
            this.cost(ctx, x, y, z, destX, destZ, res);
        }

        private final void cost(CalculationContext ctx, int x, int y, int z, int destX, int destZ, MovementResult res) {
            IBlockState destUpState = ctx.get(destX, y, destZ);
            if (!(MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, ctx.getBsa(), destX, y + 2, destZ, null, 16, null) && MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, ctx.getBsa(), destX, y + 1, destZ, null, 16, null) && MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), destX, y, destZ, destUpState))) {
                return;
            }
            IBlockState sourceState = ctx.get(x, y, z);
            if (MovementHelper.INSTANCE.isLadder(sourceState) || MovementHelper.INSTANCE.isLadder(destUpState)) {
                return;
            }
            IBlockState destState = ctx.get(destX, y - 1, destZ);
            if (!MovementHelper.INSTANCE.canStandOn(ctx.getBsa(), destX, y - 1, destZ, destState) || MovementHelper.INSTANCE.isLadder(destState)) {
                this.freeFallCost(ctx, x, y, z, destX, destZ, destState, res);
                return;
            }
            AxisAlignedBB axisAlignedBB = sourceState.func_177230_c().func_180640_a((World)ctx.getWorld(), new BlockPos(x, y, z), sourceState);
            if (axisAlignedBB == null) {
                return;
            }
            double sourceHeight = axisAlignedBB.field_72337_e;
            AxisAlignedBB axisAlignedBB2 = destState.func_177230_c().func_180640_a((World)ctx.getWorld(), new BlockPos(destX, y - 1, destZ), destState);
            if (axisAlignedBB2 == null) {
                return;
            }
            double destHeight = axisAlignedBB2.field_72337_e;
            double diff = sourceHeight - destHeight;
            res.setCost(diff <= 0.5 ? ctx.getCost().getONE_BLOCK_WALK_COST() : (diff <= 1.125 ? ctx.getCost().getWALK_OFF_ONE_BLOCK_COST() * ctx.getCost().getSPRINT_MULTIPLIER() + ctx.getCost().getN_BLOCK_FALL_COST()[1] : ctx.getCost().getINF_COST()));
        }

        public final void freeFallCost(@NotNull CalculationContext ctx, int x, int y, int z, int destX, int destZ, @NotNull IBlockState destState, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)destState, (String)"destState");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            if (!MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), destX, y - 1, destZ, destState)) {
                return;
            }
            int effStartHeight = y;
            double cost = 0.0;
            int fellSoFar = 2;
            while (true) {
                int newY;
                if ((newY = y - fellSoFar) < 0) {
                    return;
                }
                IBlockState blockOnto = ctx.get(destX, newY, destZ);
                int unprotectedFallHeight = fellSoFar - (y - effStartHeight);
                double costUpUntilThisBlock = ctx.getCost().getWALK_OFF_ONE_BLOCK_COST() + ctx.getCost().getN_BLOCK_FALL_COST()[unprotectedFallHeight] + cost;
                if (!MovementHelper.INSTANCE.canStandOn(ctx.getBsa(), destX, newY, destZ, blockOnto)) {
                    if (MovementHelper.INSTANCE.isWotah(blockOnto)) {
                        if (MovementHelper.canStandOn$default(MovementHelper.INSTANCE, ctx.getBsa(), destX, newY - 1, destZ, null, 16, null)) {
                            res.setY(newY - 1);
                            res.setCost(costUpUntilThisBlock);
                            return;
                        }
                        return;
                    }
                    if (!MovementHelper.INSTANCE.canWalkThrough(ctx.getBsa(), destX, newY, destZ, blockOnto)) {
                        return;
                    }
                } else if (unprotectedFallHeight <= 11 && MovementHelper.INSTANCE.isLadder(blockOnto)) {
                    cost += ctx.getCost().getN_BLOCK_FALL_COST()[unprotectedFallHeight - 1] + ctx.getCost().getONE_DOWN_LADDER_COST();
                    effStartHeight = newY;
                } else {
                    if (fellSoFar <= ctx.getMaxFallHeight()) {
                        res.setY(newY);
                        res.setCost(costUpUntilThisBlock);
                        return;
                    }
                    return;
                }
                if (fellSoFar == Integer.MAX_VALUE) break;
                ++fellSoFar;
            }
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

