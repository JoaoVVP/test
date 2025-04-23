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

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000,\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u0018\u0000 \u000e2\u00020\u0001:\u0001\u000eB\u001d\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0005\u0012\u0006\u0010\u0006\u001a\u00020\u0005\u00a2\u0006\u0002\u0010\u0007J\u0018\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\rH\u0016\u00a8\u0006\u000f"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/movements/MovementAscend;", "Lcom/jelly/mightyminerv2/pathfinder/movement/Movement;", "mm", "Lcom/jelly/mightyminerv2/MightyMiner;", "from", "Lnet/minecraft/util/BlockPos;", "to", "(Lcom/jelly/mightyminerv2/MightyMiner;Lnet/minecraft/util/BlockPos;Lnet/minecraft/util/BlockPos;)V", "calculateCost", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "Companion", "MightyMinerV2"})
public final class MovementAscend
extends Movement {
    @NotNull
    public static final Companion Companion = new Companion(null);

    public MovementAscend(@NotNull MightyMiner mm, @NotNull BlockPos from, @NotNull BlockPos to) {
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

    @Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000(\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0002\b\u0002\b\u0086\u0003\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J>\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eJ@\u0010\u000f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\b2\u0006\u0010\n\u001a\u00020\b2\u0006\u0010\u000b\u001a\u00020\b2\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\u000eH\u0002\u00a8\u0006\u0010"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/movements/MovementAscend$Companion;", "", "()V", "calculateCost", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "x", "", "y", "z", "destX", "destZ", "res", "Lcom/jelly/mightyminerv2/pathfinder/movement/MovementResult;", "cost", "MightyMinerV2"})
    public static final class Companion {
        private Companion() {
        }

        public final void calculateCost(@NotNull CalculationContext ctx, int x, int y, int z, int destX, int destZ, @NotNull MovementResult res) {
            Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
            Intrinsics.checkNotNullParameter((Object)res, (String)"res");
            res.set(destX, y + 1, destZ);
            this.cost(ctx, x, y, z, destX, destZ, res);
        }

        private final void cost(CalculationContext ctx, int x, int y, int z, int destX, int destZ, MovementResult res) {
            IBlockState destState = ctx.get(destX, y + 1, destZ);
            if (!MovementHelper.INSTANCE.canStandOn(ctx.getBsa(), destX, y + 1, destZ, destState)) {
                return;
            }
            if (!MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, ctx.getBsa(), destX, y + 3, destZ, null, 16, null)) {
                return;
            }
            if (!MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, ctx.getBsa(), destX, y + 2, destZ, null, 16, null)) {
                return;
            }
            if (!MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, ctx.getBsa(), x, y + 3, z, null, 16, null)) {
                return;
            }
            IBlockState sourceState = ctx.get(x, y, z);
            if (MovementHelper.INSTANCE.isLadder(sourceState)) {
                return;
            }
            if (MovementHelper.INSTANCE.isLadder(destState) && !MovementHelper.INSTANCE.canWalkIntoLadder(destState, destX - x, destZ - z)) {
                return;
            }
            AxisAlignedBB axisAlignedBB = sourceState.func_177230_c().func_180640_a((World)ctx.getWorld(), new BlockPos(x, y, z), sourceState);
            if (axisAlignedBB == null) {
                return;
            }
            double sourceHeight = axisAlignedBB.field_72337_e;
            AxisAlignedBB axisAlignedBB2 = destState.func_177230_c().func_180640_a((World)ctx.getWorld(), new BlockPos(destX, y + 1, destZ), destState);
            if (axisAlignedBB2 == null) {
                return;
            }
            double destHeight = axisAlignedBB2.field_72337_e;
            double diff = destHeight - sourceHeight;
            res.setCost(diff <= 0.5 ? ctx.getCost().getONE_BLOCK_SPRINT_COST() : (diff <= 1.125 ? ctx.getCost().getJUMP_ONE_BLOCK_COST() : ctx.getCost().getINF_COST()));
        }

        public /* synthetic */ Companion(DefaultConstructorMarker $constructor_marker) {
            this();
        }
    }
}

