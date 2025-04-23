/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.Intrinsics
 *  net.minecraft.block.state.IBlockState
 *  net.minecraft.init.Blocks
 *  net.minecraft.util.BlockPos
 *  net.minecraft.util.BlockPos$MutableBlockPos
 *  net.minecraft.world.IBlockAccess
 *  net.minecraft.world.World
 *  net.minecraft.world.chunk.Chunk
 *  net.minecraft.world.chunk.IChunkProvider
 *  org.jetbrains.annotations.NotNull
 *  org.jetbrains.annotations.Nullable
 */
package com.jelly.mightyminerv2.pathfinder.helper;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import com.jelly.mightyminerv2.pathfinder.helper.BlockStateInterfaceAccessWrapper;
import com.jelly.mightyminerv2.util.IChunkProviderClient;
import java.util.List;
import java.util.Map;
import kotlin.Metadata;
import kotlin.jvm.internal.Intrinsics;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000J\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0002\b\u0005\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\n\u0002\u0018\u0002\n\u0000\n\u0002\u0018\u0002\n\u0000\n\u0002\u0010\b\n\u0002\b\u0003\n\u0002\u0010\t\n\u0000\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\r\u0012\u0006\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u001e\u0010\u0012\u001a\u00020\u00132\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0016\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015J\u0018\u0010\u0018\u001a\u00020\u00192\u0006\u0010\u0014\u001a\u00020\u00152\u0006\u0010\u0017\u001a\u00020\u0015H\u0002J\u0016\u0010\u001a\u001a\u00020\u001b2\u0006\u0010\u001c\u001a\u00020\u00152\u0006\u0010\u001d\u001a\u00020\u0015R\u001a\u0010\u0005\u001a\u00020\u0006X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u0007\u0010\b\"\u0004\b\t\u0010\nR\u0010\u0010\u000b\u001a\u0004\u0018\u00010\fX\u0082\u000e\u00a2\u0006\u0002\n\u0000R\u0011\u0010\r\u001a\u00020\u000e\u00a2\u0006\b\n\u0000\u001a\u0004\b\r\u0010\u000fR\u0014\u0010\u0010\u001a\b\u0012\u0004\u0012\u00020\f0\u0011X\u0082\u0004\u00a2\u0006\u0002\n\u0000R\u000e\u0010\u0002\u001a\u00020\u0003X\u0082\u0004\u00a2\u0006\u0002\n\u0000\u00a8\u0006\u001e"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/helper/BlockStateAccessor;", "", "world", "Lnet/minecraft/world/World;", "(Lnet/minecraft/world/World;)V", "access", "Lnet/minecraft/world/IBlockAccess;", "getAccess", "()Lnet/minecraft/world/IBlockAccess;", "setAccess", "(Lnet/minecraft/world/IBlockAccess;)V", "cached", "Lnet/minecraft/world/chunk/Chunk;", "isPassableBlockPos", "Lnet/minecraft/util/BlockPos$MutableBlockPos;", "()Lnet/minecraft/util/BlockPos$MutableBlockPos;", "loadedChunks", "Lcom/jelly/mightyminerv2/deps/it/unimi/dsi/fastutil/longs/Long2ObjectMap;", "get", "Lnet/minecraft/block/state/IBlockState;", "x", "", "y", "z", "getKey", "", "isBlockInLoadedChunks", "", "blockX", "blockZ", "MightyMinerV2"})
public final class BlockStateAccessor {
    @NotNull
    private final World world;
    @NotNull
    private final Long2ObjectMap<Chunk> loadedChunks;
    @Nullable
    private Chunk cached;
    @NotNull
    private IBlockAccess access;
    @NotNull
    private final BlockPos.MutableBlockPos isPassableBlockPos;

    public BlockStateAccessor(@NotNull World world) {
        Intrinsics.checkNotNullParameter((Object)world, (String)"world");
        this.world = world;
        this.loadedChunks = new Long2ObjectOpenHashMap();
        IChunkProvider iChunkProvider = this.world.func_72863_F();
        Intrinsics.checkNotNull((Object)iChunkProvider, (String)"null cannot be cast to non-null type com.jelly.mightyminerv2.util.IChunkProviderClient");
        List<Chunk> list = ((IChunkProviderClient)iChunkProvider).chunkListing();
        Intrinsics.checkNotNullExpressionValue(list, (String)"chunkListing(...)");
        List<Chunk> loadedWorld = list;
        for (Chunk chunk : loadedWorld) {
            ((Map)this.loadedChunks).put(this.getKey(chunk.field_76635_g, chunk.field_76647_h), chunk);
        }
        this.isPassableBlockPos = new BlockPos.MutableBlockPos();
        this.access = new BlockStateInterfaceAccessWrapper(this, (IBlockAccess)this.world);
    }

    @NotNull
    public final IBlockAccess getAccess() {
        return this.access;
    }

    public final void setAccess(@NotNull IBlockAccess iBlockAccess) {
        Intrinsics.checkNotNullParameter((Object)iBlockAccess, (String)"<set-?>");
        this.access = iBlockAccess;
    }

    @NotNull
    public final BlockPos.MutableBlockPos isPassableBlockPos() {
        return this.isPassableBlockPos;
    }

    @NotNull
    public final IBlockState get(int x, int y, int z) {
        Chunk current = this.cached;
        if (current != null && current.field_76635_g == x >> 4 && current.field_76647_h == z >> 4) {
            IBlockState iBlockState = current.func_177435_g(new BlockPos(x, y, z));
            Intrinsics.checkNotNullExpressionValue((Object)iBlockState, (String)"getBlockState(...)");
            return iBlockState;
        }
        current = (Chunk)this.loadedChunks.get(this.getKey(x >> 4, z >> 4));
        if (current != null && current.func_177410_o()) {
            this.cached = current;
            IBlockState iBlockState = current.func_177435_g(new BlockPos(x, y, z));
            Intrinsics.checkNotNullExpressionValue((Object)iBlockState, (String)"getBlockState(...)");
            return iBlockState;
        }
        IBlockState iBlockState = Blocks.field_150350_a.func_176223_P();
        Intrinsics.checkNotNullExpressionValue((Object)iBlockState, (String)"getDefaultState(...)");
        return iBlockState;
    }

    public final boolean isBlockInLoadedChunks(int blockX, int blockZ) {
        return this.loadedChunks.containsKey(this.getKey(blockX >> 4, blockZ >> 4));
    }

    private final long getKey(int x, int z) {
        return (long)x & 0xFFFFFFFFL | ((long)z & 0xFFFFFFFFL) << 32;
    }
}

