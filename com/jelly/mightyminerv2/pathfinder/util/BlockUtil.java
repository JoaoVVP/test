/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.block.material.Material
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.util.AxisAlignedBB
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.util.MathHelper
 *  net.minecraft.util.Vec3
 *  net.minecraft.util.Vec3i
 *  net.minecraft.world.World
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.util;

import com.jelly.mightyminerv2.pathfinder.helper.BlockStateAccessor;
import com.jelly.mightyminerv2.pathfinder.movement.CalculationContext;
import com.jelly.mightyminerv2.pathfinder.movement.MovementHelper;
import com.jelly.mightyminerv2.pathfinder.util.RefKt;
import java.util.ArrayList;
import java.util.List;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.util.Vec3i;
import net.minecraft.world.World;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000D\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0006\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0010 \n\u0002\b\u0002\n\u0002\u0010\b\n\u0002\b\u0006\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\b2\u0006\u0010\t\u001a\u00020\bJ\u001e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0007\u001a\u00020\n2\u0006\u0010\t\u001a\u00020\nJ\u001e\u0010\u000b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\r\u001a\u00020\bJ\u000e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\bJ\u000e\u0010\u0010\u001a\u00020\u00112\u0006\u0010\u0012\u001a\u00020\u0013J\u0016\u0010\u0014\u001a\u00020\u00112\u0006\u0010\f\u001a\u00020\b2\u0006\u0010\u0015\u001a\u00020\bJ\u000e\u0010\u0016\u001a\u00020\u00042\u0006\u0010\u0017\u001a\u00020\bJD\u0010\u0018\u001a\b\u0012\u0004\u0012\u00020\b0\u00192\u0006\u0010\u001a\u001a\u00020\b2\u0006\u0010\u001b\u001a\u00020\u001c2\u0006\u0010\u001d\u001a\u00020\u001c2\u0006\u0010\u001e\u001a\u00020\u001c2\u0006\u0010\u001f\u001a\u00020\u001c2\u0006\u0010 \u001a\u00020\u001c2\u0006\u0010!\u001a\u00020\u001c\u00a8\u0006\""}, d2={"Lcom/jelly/mightyminerv2/pathfinder/util/BlockUtil;", "", "()V", "bresenham", "", "ctx", "Lcom/jelly/mightyminerv2/pathfinder/movement/CalculationContext;", "start", "Lnet/minecraft/util/BlockPos;", "end", "Lnet/minecraft/util/Vec3;", "canWalkOn", "startPos", "endPos", "canWalkOnBlock", "pos", "getDirectionToWalkOnStairs", "Lnet/minecraft/util/EnumFacing;", "state", "Lnet/minecraft/block/state/IBlockState;", "getPlayerDirectionToBeAbleToWalkOnBlock", "endPoss", "isStairSlab", "block", "neighbourGenerator", "", "mainBlock", "xD1", "", "xD2", "yD1", "yD2", "zD1", "zD2", "MightyMinerV2"})
public final class BlockUtil {
    @NotNull
    public static final BlockUtil INSTANCE = new BlockUtil();

    private BlockUtil() {
    }

    public final boolean canWalkOnBlock(@NotNull BlockPos pos) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        Block block = RefKt.getWorld().func_180495_p(pos.func_177982_a(0, 0, 0)).func_177230_c();
        Block blockAbove = RefKt.getWorld().func_180495_p(pos.func_177984_a()).func_177230_c();
        Material material = block.func_149688_o();
        Intrinsics.checkNotNullExpressionValue((Object)material, (String)"getMaterial(...)");
        Material material2 = material;
        Material material3 = blockAbove.func_149688_o();
        Intrinsics.checkNotNullExpressionValue((Object)material3, (String)"getMaterial(...)");
        Material materialAbove = material3;
        return material2.func_76220_a() && !material2.func_76224_d() && Intrinsics.areEqual((Object)materialAbove, (Object)Material.field_151579_a);
    }

    @NotNull
    public final List<BlockPos> neighbourGenerator(@NotNull BlockPos mainBlock, int xD1, int xD2, int yD1, int yD2, int zD1, int zD2) {
        Intrinsics.checkNotNullParameter((Object)mainBlock, (String)"mainBlock");
        List neighbours = new ArrayList();
        int x = xD1;
        if (x <= xD2) {
            while (true) {
                int y;
                if ((y = yD1) <= yD2) {
                    while (true) {
                        int z;
                        if ((z = zD1) <= zD2) {
                            while (true) {
                                neighbours.add(new BlockPos(mainBlock.func_177958_n() + x, mainBlock.func_177956_o() + y, mainBlock.func_177952_p() + z));
                                if (z == zD2) break;
                                ++z;
                            }
                        }
                        if (y == yD2) break;
                        ++y;
                    }
                }
                if (x == xD2) break;
                ++x;
            }
        }
        return neighbours;
    }

    public final boolean isStairSlab(@NotNull BlockPos block) {
        Intrinsics.checkNotNullParameter((Object)block, (String)"block");
        return RefKt.getWorld().func_180495_p(block).func_177230_c() instanceof BlockStairs || RefKt.getWorld().func_180495_p(block).func_177230_c() instanceof BlockStairs;
    }

    @NotNull
    public final EnumFacing getDirectionToWalkOnStairs(@NotNull IBlockState state) {
        EnumFacing enumFacing;
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        switch (state.func_177230_c().func_176201_c(state)) {
            case 0: {
                enumFacing = EnumFacing.EAST;
                break;
            }
            case 1: {
                enumFacing = EnumFacing.WEST;
                break;
            }
            case 2: {
                enumFacing = EnumFacing.SOUTH;
                break;
            }
            case 3: {
                enumFacing = EnumFacing.NORTH;
                break;
            }
            case 4: {
                enumFacing = EnumFacing.DOWN;
                break;
            }
            default: {
                enumFacing = EnumFacing.UP;
            }
        }
        return enumFacing;
    }

    @NotNull
    public final EnumFacing getPlayerDirectionToBeAbleToWalkOnBlock(@NotNull BlockPos startPos, @NotNull BlockPos endPoss) {
        Intrinsics.checkNotNullParameter((Object)startPos, (String)"startPos");
        Intrinsics.checkNotNullParameter((Object)endPoss, (String)"endPoss");
        int deltaX = endPoss.func_177958_n() - startPos.func_177958_n();
        int deltaZ = endPoss.func_177952_p() - startPos.func_177952_p();
        return Math.abs(deltaX) > Math.abs(deltaZ) ? (deltaX > 0 ? EnumFacing.EAST : EnumFacing.WEST) : (deltaZ > 0 ? EnumFacing.SOUTH : EnumFacing.NORTH);
    }

    public final boolean canWalkOn(@NotNull CalculationContext ctx, @NotNull BlockPos startPos, @NotNull BlockPos endPos) {
        double destMaxY;
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        Intrinsics.checkNotNullParameter((Object)startPos, (String)"startPos");
        Intrinsics.checkNotNullParameter((Object)endPos, (String)"endPos");
        IBlockState startState = ctx.getBsa().get(startPos.func_177958_n(), startPos.func_177956_o(), startPos.func_177952_p());
        IBlockState endState = ctx.getBsa().get(endPos.func_177958_n(), endPos.func_177956_o(), endPos.func_177952_p());
        if (!endState.func_177230_c().func_149688_o().func_76220_a()) {
            return endPos.func_177956_o() - startPos.func_177956_o() <= 1;
        }
        AxisAlignedBB axisAlignedBB = startState.func_177230_c().func_180640_a((World)ctx.getWorld(), startPos, startState);
        double sourceMaxY = axisAlignedBB != null ? axisAlignedBB.field_72337_e : (double)startPos.func_177956_o();
        AxisAlignedBB axisAlignedBB2 = endState.func_177230_c().func_180640_a((World)ctx.getWorld(), endPos, endState);
        double d = destMaxY = axisAlignedBB2 != null ? axisAlignedBB2.field_72337_e : (double)startPos.func_177956_o() + 1.0;
        if (endState.func_177230_c() instanceof BlockStairs && destMaxY - sourceMaxY > 1.0) {
            return MovementHelper.INSTANCE.isValidStair(endState, endPos.func_177958_n() - startPos.func_177958_n(), endPos.func_177952_p() - startPos.func_177952_p());
        }
        return destMaxY - sourceMaxY <= 0.5;
    }

    public final boolean bresenham(@NotNull CalculationContext ctx, @NotNull BlockPos start2, @NotNull BlockPos end) {
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        Intrinsics.checkNotNullParameter((Object)start2, (String)"start");
        Intrinsics.checkNotNullParameter((Object)end, (String)"end");
        Vec3 vec3 = new Vec3((Vec3i)start2).func_72441_c(0.5, 0.5, 0.5);
        Intrinsics.checkNotNullExpressionValue((Object)vec3, (String)"addVector(...)");
        Vec3 vec32 = new Vec3((Vec3i)end).func_72441_c(0.5, 0.5, 0.5);
        Intrinsics.checkNotNullExpressionValue((Object)vec32, (String)"addVector(...)");
        return this.bresenham(ctx, vec3, vec32);
    }

    public final boolean bresenham(@NotNull CalculationContext ctx, @NotNull Vec3 start2, @NotNull Vec3 end) {
        Intrinsics.checkNotNullParameter((Object)ctx, (String)"ctx");
        Intrinsics.checkNotNullParameter((Object)start2, (String)"start");
        Intrinsics.checkNotNullParameter((Object)end, (String)"end");
        Vec3 start0 = start2;
        BlockStateAccessor bsa = ctx.getBsa();
        int x1 = MathHelper.func_76128_c((double)end.field_72450_a);
        int y1 = MathHelper.func_76128_c((double)end.field_72448_b);
        int z1 = MathHelper.func_76128_c((double)end.field_72449_c);
        int x0 = MathHelper.func_76128_c((double)start0.field_72450_a);
        int y0 = MathHelper.func_76128_c((double)start0.field_72448_b);
        int z0 = MathHelper.func_76128_c((double)start0.field_72449_c);
        IBlockState lastState = bsa.get(x0, y0, z0);
        BlockPos lastPos = new BlockPos(start2);
        int iterations = 200;
        while (iterations-- >= 0) {
            int delta;
            if (x0 == x1 && y0 == y1 && z0 == z1) {
                return true;
            }
            boolean hasNewX = true;
            boolean hasNewY = true;
            boolean hasNewZ = true;
            double newX = 999.0;
            double newY = 999.0;
            double newZ = 999.0;
            if (x1 > x0) {
                newX = (double)x0 + 1.0;
            } else if (x1 < x0) {
                newX = (double)x0 + 0.0;
            } else {
                hasNewX = false;
            }
            if (y1 > y0) {
                newY = (double)y0 + 1.0;
            } else if (y1 < y0) {
                newY = (double)y0 + 0.0;
            } else {
                hasNewY = false;
            }
            if (z1 > z0) {
                newZ = (double)z0 + 1.0;
            } else if (z1 < z0) {
                newZ = (double)z0 + 0.0;
            } else {
                hasNewZ = false;
            }
            double stepX = 999.0;
            double stepY = 999.0;
            double stepZ = 999.0;
            double dx = end.field_72450_a - start0.field_72450_a;
            double dy = end.field_72448_b - start0.field_72448_b;
            double dz = end.field_72449_c - start0.field_72449_c;
            if (hasNewX) {
                stepX = (newX - start0.field_72450_a) / dx;
            }
            if (hasNewY) {
                stepY = (newY - start0.field_72448_b) / dy;
            }
            if (hasNewZ) {
                stepZ = (newZ - start0.field_72449_c) / dz;
            }
            if (stepX == -0.0) {
                stepX = -1.0E-4;
            }
            if (stepY == -0.0) {
                stepY = -1.0E-4;
            }
            if (stepZ == -0.0) {
                stepZ = -1.0E-4;
            }
            EnumFacing enumfacing = null;
            if (stepX < stepY && stepX < stepZ) {
                enumfacing = x1 > x0 ? EnumFacing.WEST : EnumFacing.EAST;
                start0 = new Vec3(newX, start0.field_72448_b + dy * stepX, start0.field_72449_c + dz * stepX);
            } else if (stepY < stepZ) {
                enumfacing = y1 > y0 ? EnumFacing.DOWN : EnumFacing.UP;
                start0 = new Vec3(start0.field_72450_a + dx * stepY, newY, start0.field_72449_c + dz * stepY);
            } else {
                enumfacing = z1 > z0 ? EnumFacing.NORTH : EnumFacing.SOUTH;
                start0 = new Vec3(start0.field_72450_a + dx * stepZ, start0.field_72448_b + dy * stepZ, newZ);
            }
            x0 = MathHelper.func_76128_c((double)start0.field_72450_a) - (enumfacing == EnumFacing.EAST ? 1 : 0);
            y0 = MathHelper.func_76128_c((double)start0.field_72448_b) - (enumfacing == EnumFacing.UP ? 1 : 0);
            z0 = MathHelper.func_76128_c((double)start0.field_72449_c) - (enumfacing == EnumFacing.SOUTH ? 1 : 0);
            IBlockState currState = ctx.getBsa().get(x0, y0, z0);
            int i = 0;
            if (!(MovementHelper.INSTANCE.canStandOn(bsa, x0, y0, z0, currState) && MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, bsa, x0, y0 + 1, z0, null, 16, null) && MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, bsa, x0, y0 + 2, z0, null, 16, null))) {
                i = -3;
                boolean foundValidBlock = false;
                while (++i <= 3) {
                    if (i == 0) continue;
                    currState = bsa.get(x0, y0 + i, z0);
                    if (!MovementHelper.INSTANCE.canStandOn(ctx.getBsa(), x0, y0 + i, z0, currState) || !MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, bsa, x0, y0 + i + 1, z0, null, 16, null) || !MovementHelper.canWalkThrough$default(MovementHelper.INSTANCE, bsa, x0, y0 + i + 2, z0, null, 16, null)) continue;
                    foundValidBlock = true;
                    break;
                }
                if (!foundValidBlock) {
                    return false;
                }
            }
            if ((delta = y0 + i - lastPos.func_177956_o()) > 0) {
                if (delta > 1) {
                    return false;
                }
                double sourceHeight = -1.0;
                double destHeight = -1.0;
                boolean snow = false;
                if (lastState.func_177230_c() instanceof BlockSnow) {
                    sourceHeight = (double)(((Number)((Object)lastState.func_177229_b((IProperty)BlockSnow.field_176315_a))).intValue() - 1) * 0.125;
                    snow = true;
                }
                if (currState.func_177230_c() instanceof BlockSnow) {
                    destHeight = (double)(((Number)((Object)currState.func_177229_b((IProperty)BlockSnow.field_176315_a))).intValue() - 1) * 0.125;
                    snow = true;
                }
                if (!snow) {
                    boolean srcSmall = MovementHelper.INSTANCE.isBottomSlab(lastState);
                    boolean destSmall = MovementHelper.INSTANCE.isBottomSlab(currState);
                    boolean destSmallStair = MovementHelper.INSTANCE.isValidStair(currState, x0 - lastPos.func_177958_n(), z0 - lastPos.func_177952_p());
                    if (!srcSmall == (!destSmall && !destSmallStair)) {
                        return false;
                    }
                    if (srcSmall) {
                        return false;
                    }
                } else {
                    if (sourceHeight == -1.0) {
                        sourceHeight = lastState.func_177230_c().func_149669_A();
                    }
                    if (destHeight == -1.0) {
                        destHeight = currState.func_177230_c().func_149669_A();
                    }
                    if (destHeight - sourceHeight > -0.5) {
                        return false;
                    }
                }
            }
            lastState = currState;
            lastPos = new BlockPos(x0, y0 + i, z0);
        }
        return false;
    }
}

