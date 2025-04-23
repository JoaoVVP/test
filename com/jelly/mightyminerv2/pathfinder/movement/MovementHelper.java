/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.Block
 *  net.minecraft.block.BlockCauldron
 *  net.minecraft.block.BlockDoor
 *  net.minecraft.block.BlockFenceGate
 *  net.minecraft.block.BlockFire
 *  net.minecraft.block.BlockLadder
 *  net.minecraft.block.BlockLilyPad
 *  net.minecraft.block.BlockLiquid
 *  net.minecraft.block.BlockSkull
 *  net.minecraft.block.BlockSlab
 *  net.minecraft.block.BlockSlab$EnumBlockHalf
 *  net.minecraft.block.BlockSnow
 *  net.minecraft.block.BlockStairs
 *  net.minecraft.block.BlockStairs$EnumHalf
 *  net.minecraft.block.BlockTrapDoor
 *  net.minecraft.block.properties.IProperty
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.jelly.mightyminerv2.pathfinder.movement;

import com.jelly.mightyminerv2.pathfinder.helper.BlockStateAccessor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.Block;
import net.minecraft.block.BlockCauldron;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockFenceGate;
import net.minecraft.block.BlockFire;
import net.minecraft.block.BlockLadder;
import net.minecraft.block.BlockLilyPad;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSkull;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.BlockSnow;
import net.minecraft.block.BlockStairs;
import net.minecraft.block.BlockTrapDoor;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u00000\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u000b\n\u0002\u0018\u0002\n\u0002\b\f\b\u00c6\u0002\u0018\u00002\u00020\u0001B\u0007\b\u0002\u00a2\u0006\u0002\u0010\u0002J\u000e\u0010\u0003\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J0\u0010\u0007\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u0005\u001a\u00020\u0006J\u001e\u0010\u000e\u001a\u00020\u00042\u0006\u0010\u000f\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bJ0\u0010\u0012\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\b\b\u0002\u0010\u0005\u001a\u00020\u0006J\u0015\u0010\u0013\u001a\u0004\u0018\u00010\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a2\u0006\u0002\u0010\u0014J.\u0010\u0015\u001a\u00020\u00042\u0006\u0010\b\u001a\u00020\t2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u0006J\u0016\u0010\u0016\u001a\u00020\u00172\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bJ\u001e\u0010\u0018\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0019\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u000bJ\u000e\u0010\u001b\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J.\u0010\u001c\u001a\u00020\u00042\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\f\u001a\u00020\u000b2\u0006\u0010\r\u001a\u00020\u000b2\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\b\u001a\u00020\tJ\u000e\u0010\u001d\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\u001e\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u001e\u0010\u001f\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bJ\u001e\u0010 \u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u00062\u0006\u0010\u0010\u001a\u00020\u000b2\u0006\u0010\u0011\u001a\u00020\u000bJ\u000e\u0010!\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006J\u000e\u0010\"\u001a\u00020\u00042\u0006\u0010\u0005\u001a\u00020\u0006\u00a8\u0006#"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/movement/MovementHelper;", "", "()V", "avoidWalkingInto", "", "state", "Lnet/minecraft/block/state/IBlockState;", "canStandOn", "bsa", "Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateAccessor;", "x", "", "y", "z", "canWalkIntoLadder", "ladderState", "dx", "dz", "canWalkThrough", "canWalkThroughBlockState", "(Lnet/minecraft/block/state/IBlockState;)Ljava/lang/Boolean;", "canWalkThroughPosition", "getFacing", "Lnet/minecraft/util/EnumFacing;", "hasTop", "dX", "dZ", "isBottomSlab", "isFlowing", "isLadder", "isLava", "isValidReversedStair", "isValidStair", "isWotah", "possiblyFlowing", "MightyMinerV2"})
public final class MovementHelper {
    @NotNull
    public static final MovementHelper INSTANCE = new MovementHelper();

    private MovementHelper() {
    }

    public final boolean canWalkThrough(@NotNull BlockStateAccessor bsa, int x, int y, int z, @NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)bsa, (String)"bsa");
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Boolean canWalk = this.canWalkThroughBlockState(state);
        if (canWalk != null) {
            return canWalk;
        }
        return this.canWalkThroughPosition(bsa, x, y, z, state);
    }

    public static /* synthetic */ boolean canWalkThrough$default(MovementHelper movementHelper, BlockStateAccessor blockStateAccessor, int n, int n2, int n3, IBlockState iBlockState, int n4, Object object) {
        if ((n4 & 0x10) != 0) {
            iBlockState = blockStateAccessor.get(n, n2, n3);
        }
        return movementHelper.canWalkThrough(blockStateAccessor, n, n2, n3, iBlockState);
    }

    @Nullable
    public final Boolean canWalkThroughBlockState(@NotNull IBlockState state) {
        Boolean bl;
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Block block = state.func_177230_c();
        if (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150350_a)) {
            bl = true;
        } else if (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150480_ab) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150473_bD) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150321_G) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150384_bq) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150375_by) || block instanceof BlockSkull || block instanceof BlockTrapDoor) {
            bl = false;
        } else if (block instanceof BlockDoor || block instanceof BlockFenceGate) {
            bl = !Intrinsics.areEqual((Object)block, (Object)Blocks.field_150454_av);
        } else if (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150404_cg)) {
            bl = null;
        } else if (block instanceof BlockSnow) {
            bl = null;
        } else if (block instanceof BlockLiquid) {
            Integer n = (Integer)state.func_177229_b((IProperty)BlockLiquid.field_176367_b);
            bl = n == null || n != 0 ? Boolean.valueOf(false) : null;
        } else if (block instanceof BlockCauldron) {
            bl = false;
        } else if (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150468_ap)) {
            bl = false;
        } else {
            Boolean bl2;
            try {
                bl2 = block.func_176205_b(null, null);
            }
            catch (Throwable exception) {
                System.out.println((Object)("The block " + state.func_177230_c().func_149732_F() + " requires a special case due to the exception " + exception.getMessage()));
                bl2 = null;
            }
            bl = bl2;
        }
        return bl;
    }

    public final boolean canWalkThroughPosition(@NotNull BlockStateAccessor bsa, int x, int y, int z, @NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)bsa, (String)"bsa");
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Block block = state.func_177230_c();
        if (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150404_cg)) {
            return MovementHelper.canStandOn$default(this, bsa, x, y - 1, z, null, 16, null);
        }
        if (block instanceof BlockSnow) {
            if (!bsa.isBlockInLoadedChunks(x, z)) {
                return true;
            }
            Comparable comparable = state.func_177229_b((IProperty)BlockSnow.field_176315_a);
            Intrinsics.checkNotNullExpressionValue((Object)comparable, (String)"getValue(...)");
            if (((Number)((Object)comparable)).intValue() >= 1) {
                return false;
            }
            return MovementHelper.canStandOn$default(this, bsa, x, y - 1, z, null, 16, null);
        }
        if (block instanceof BlockLiquid) {
            if (this.isFlowing(x, y, z, state, bsa)) {
                return false;
            }
            IBlockState up = bsa.get(x, y + 1, z);
            if (up.func_177230_c() instanceof BlockLiquid || up.func_177230_c() instanceof BlockLilyPad) {
                return false;
            }
            return Intrinsics.areEqual((Object)block, (Object)Blocks.field_150355_j) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150358_i);
        }
        return block.func_176205_b(bsa.getAccess(), (BlockPos)bsa.isPassableBlockPos().func_181079_c(x, y, z));
    }

    public final boolean canStandOn(@NotNull BlockStateAccessor bsa, int x, int y, int z, @NotNull IBlockState state) {
        Block up;
        Intrinsics.checkNotNullParameter((Object)bsa, (String)"bsa");
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Block block = state.func_177230_c();
        boolean bl = block.func_149721_r() ? true : (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150468_ap) ? true : (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150458_ak) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150349_c) ? true : (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150477_bB) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150486_ae) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150447_bR) ? true : (Intrinsics.areEqual((Object)block, (Object)Blocks.field_150359_w) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150399_cn) ? true : (block instanceof BlockStairs ? true : (Intrinsics.areEqual((Object)block, (Object)Blocks.field_180398_cJ) ? true : (this.isWotah(state) ? Intrinsics.areEqual((Object)(up = bsa.get(x, y + 1, z).func_177230_c()), (Object)Blocks.field_150392_bi) || Intrinsics.areEqual((Object)up, (Object)Blocks.field_150404_cg) : (this.isLava(state) ? false : (block instanceof BlockSlab ? true : block instanceof BlockSnow)))))))));
        return bl;
    }

    public static /* synthetic */ boolean canStandOn$default(MovementHelper movementHelper, BlockStateAccessor blockStateAccessor, int n, int n2, int n3, IBlockState iBlockState, int n4, Object object) {
        if ((n4 & 0x10) != 0) {
            iBlockState = blockStateAccessor.get(n, n2, n3);
        }
        return movementHelper.canStandOn(blockStateAccessor, n, n2, n3, iBlockState);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean possiblyFlowing(@NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        if (!(state.func_177230_c() instanceof BlockLiquid)) return false;
        Integer n = (Integer)state.func_177229_b((IProperty)BlockLiquid.field_176367_b);
        if (n == null) return true;
        if (n == 0) return false;
        return true;
    }

    public final boolean isFlowing(int x, int y, int z, @NotNull IBlockState state, @NotNull BlockStateAccessor bsa) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Intrinsics.checkNotNullParameter((Object)bsa, (String)"bsa");
        if (!(state.func_177230_c() instanceof BlockLiquid)) {
            return false;
        }
        Integer n = (Integer)state.func_177229_b((IProperty)BlockLiquid.field_176367_b);
        if (n == null || n != 0) {
            return true;
        }
        return this.possiblyFlowing(bsa.get(x + 1, y, z)) || this.possiblyFlowing(bsa.get(x - 1, y, z)) || this.possiblyFlowing(bsa.get(x, y, z + 1)) || this.possiblyFlowing(bsa.get(x, y, z - 1));
    }

    public final boolean isWotah(@NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Block block = state.func_177230_c();
        return Intrinsics.areEqual((Object)block, (Object)Blocks.field_150355_j) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150358_i);
    }

    public final boolean isLava(@NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Block block = state.func_177230_c();
        return Intrinsics.areEqual((Object)block, (Object)Blocks.field_150353_l) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150356_k);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public final boolean isBottomSlab(@NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        if (!(state.func_177230_c() instanceof BlockSlab)) return false;
        Block block = state.func_177230_c();
        Intrinsics.checkNotNull((Object)block, (String)"null cannot be cast to non-null type net.minecraft.block.BlockSlab");
        if (((BlockSlab)block).func_176552_j()) return false;
        if (state.func_177229_b((IProperty)BlockSlab.field_176554_a) != BlockSlab.EnumBlockHalf.BOTTOM) return false;
        return true;
    }

    public final boolean isValidStair(@NotNull IBlockState state, int dx, int dz) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        if (dx == dz) {
            return false;
        }
        if (!(state.func_177230_c() instanceof BlockStairs)) {
            return false;
        }
        if (state.func_177229_b((IProperty)BlockStairs.field_176308_b) != BlockStairs.EnumHalf.BOTTOM) {
            return false;
        }
        EnumFacing stairFacing = (EnumFacing)state.func_177229_b((IProperty)BlockStairs.field_176309_a);
        return dz == -1 ? stairFacing == EnumFacing.NORTH : (dz == 1 ? stairFacing == EnumFacing.SOUTH : (dx == -1 ? stairFacing == EnumFacing.WEST : (dx == 1 ? stairFacing == EnumFacing.EAST : false)));
    }

    public final boolean isValidReversedStair(@NotNull IBlockState state, int dx, int dz) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        if (dx == dz) {
            return false;
        }
        if (!(state.func_177230_c() instanceof BlockStairs)) {
            return false;
        }
        if (state.func_177229_b((IProperty)BlockStairs.field_176308_b) != BlockStairs.EnumHalf.BOTTOM) {
            return false;
        }
        EnumFacing stairFacing = (EnumFacing)state.func_177229_b((IProperty)BlockStairs.field_176309_a);
        return dz == 1 ? stairFacing == EnumFacing.NORTH : (dz == -1 ? stairFacing == EnumFacing.SOUTH : (dx == 1 ? stairFacing == EnumFacing.WEST : (dx == -1 ? stairFacing == EnumFacing.EAST : false)));
    }

    public final boolean hasTop(@NotNull IBlockState state, int dX, int dZ) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        return !this.isBottomSlab(state) && !this.isValidStair(state, dX, dZ);
    }

    public final boolean avoidWalkingInto(@NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        Block block = state.func_177230_c();
        return block instanceof BlockLiquid || block instanceof BlockFire || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150434_aF) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150384_bq) || Intrinsics.areEqual((Object)block, (Object)Blocks.field_150321_G);
    }

    @NotNull
    public final EnumFacing getFacing(int dx, int dz) {
        EnumFacing enumFacing;
        if (dx == 0 && dz == 0) {
            enumFacing = EnumFacing.UP;
        } else {
            EnumFacing enumFacing2 = EnumFacing.field_176754_o[Math.abs(dx) * (2 + dx) + Math.abs(dz) * (1 - dz)];
            enumFacing = enumFacing2;
            Intrinsics.checkNotNullExpressionValue((Object)enumFacing2, (String)"get(...)");
        }
        return enumFacing;
    }

    public final boolean isLadder(@NotNull IBlockState state) {
        Intrinsics.checkNotNullParameter((Object)state, (String)"state");
        return Intrinsics.areEqual((Object)state.func_177230_c(), (Object)Blocks.field_150468_ap);
    }

    public final boolean canWalkIntoLadder(@NotNull IBlockState ladderState, int dx, int dz) {
        Intrinsics.checkNotNullParameter((Object)ladderState, (String)"ladderState");
        return this.isLadder(ladderState) && ladderState.func_177229_b((IProperty)BlockLadder.field_176382_a) != this.getFacing(dx, dz);
    }
}

