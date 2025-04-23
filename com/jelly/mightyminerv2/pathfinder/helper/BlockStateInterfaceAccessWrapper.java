/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.tileentity.TileEntity
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.EnumFacing
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.WorldType
 *  net.minecraft.world.biome.BiomeGenBase
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.jelly.mightyminerv2.pathfinder.helper;

import com.jelly.mightyminerv2.pathfinder.helper.BlockStateAccessor;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.WorldType;
import net.minecraft.world.biome.BiomeGenBase;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000F\n\u0002\u0018\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0003\n\u0002\u0010\u000b\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0005\u0018\u00002\u00020\u0001B\u0015\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u0012\u0006\u0010\u0004\u001a\u00020\u0001\u00a2\u0006\u0002\u0010\u0005J\b\u0010\u0006\u001a\u00020\u0007H\u0016J\u0012\u0010\b\u001a\u0004\u0018\u00010\t2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0010\u0010\f\u001a\u00020\r2\u0006\u0010\n\u001a\u00020\u000bH\u0016J\u0018\u0010\u000e\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0010\u001a\u00020\u000fH\u0016J\u0018\u0010\u0011\u001a\u00020\u000f2\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u0012\u001a\u00020\u0013H\u0016J\u0012\u0010\u0014\u001a\u0004\u0018\u00010\u00152\u0006\u0010\n\u001a\u00020\u000bH\u0016J\b\u0010\u0016\u001a\u00020\u0017H\u0016J\u0010\u0010\u0018\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000bH\u0016J \u0010\u0019\u001a\u00020\u00072\u0006\u0010\n\u001a\u00020\u000b2\u0006\u0010\u001a\u001a\u00020\u00132\u0006\u0010\u001b\u001a\u00020\u0007H\u0016R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0004\u001a\u00020\u0001X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001c"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateInterfaceAccessWrapper;", "Lnet/minecraft/world/IBlockAccess;", "bsi", "Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateAccessor;", "world", "(Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateAccessor;Lnet/minecraft/world/IBlockAccess;)V", "extendedLevelsInChunkCache", "", "getBiomeGenForCoords", "Lnet/minecraft/world/biome/BiomeGenBase;", "pos", "Lnet/minecraft/util/BlockPos;", "getBlockState", "Lnet/minecraft/block/state/IBlockState;", "getCombinedLight", "", "lightValue", "getStrongPower", "direction", "Lnet/minecraft/util/EnumFacing;", "getTileEntity", "Lnet/minecraft/tileentity/TileEntity;", "getWorldType", "Lnet/minecraft/world/WorldType;", "isAirBlock", "isSideSolid", "side", "_default", "MightyMinerV2"})
public final class BlockStateInterfaceAccessWrapper
implements IBlockAccess {
    @NotNull
    private final BlockStateAccessor bsi;
    @NotNull
    private final IBlockAccess world;

    public BlockStateInterfaceAccessWrapper(@NotNull BlockStateAccessor bsi, @NotNull IBlockAccess world) {
        Intrinsics.checkNotNullParameter((Object)bsi, (String)"bsi");
        Intrinsics.checkNotNullParameter((Object)world, (String)"world");
        this.bsi = bsi;
        this.world = world;
    }

    @Nullable
    public TileEntity func_175625_s(@NotNull BlockPos pos) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        return null;
    }

    public int func_175626_b(@NotNull BlockPos pos, int lightValue) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        return 0;
    }

    @NotNull
    public IBlockState func_180495_p(@NotNull BlockPos pos) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        return this.bsi.get(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p());
    }

    public boolean func_175623_d(@NotNull BlockPos pos) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        return Intrinsics.areEqual((Object)this.bsi.get(pos.func_177958_n(), pos.func_177956_o(), pos.func_177952_p()).func_177230_c(), (Object)Blocks.field_150350_a);
    }

    @Nullable
    public BiomeGenBase func_180494_b(@NotNull BlockPos pos) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        return null;
    }

    public boolean func_72806_N() {
        return false;
    }

    public int func_175627_a(@NotNull BlockPos pos, @NotNull EnumFacing direction) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        Intrinsics.checkNotNullParameter((Object)direction, (String)"direction");
        return 0;
    }

    @NotNull
    public WorldType func_175624_G() {
        WorldType worldType = this.world.func_175624_G();
        Intrinsics.checkNotNullExpressionValue((Object)worldType, (String)"getWorldType(...)");
        return worldType;
    }

    public boolean isSideSolid(@NotNull BlockPos pos, @NotNull EnumFacing side, boolean _default) {
        Intrinsics.checkNotNullParameter((Object)pos, (String)"pos");
        Intrinsics.checkNotNullParameter((Object)side, (String)"side");
        return false;
    }
}

