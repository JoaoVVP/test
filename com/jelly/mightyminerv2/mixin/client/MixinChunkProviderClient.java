/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.multiplayer.ChunkProviderClient
 *  net.minecraft.util.LongHashMap
 *  net.minecraft.world.chunk.Chunk
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 */
package com.jelly.mightyminerv2.mixin.client;

import com.jelly.mightyminerv2.util.IChunkProviderClient;
import java.util.List;
import net.minecraft.client.multiplayer.ChunkProviderClient;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.Chunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value={ChunkProviderClient.class})
public class MixinChunkProviderClient
implements IChunkProviderClient {
    @Shadow
    private LongHashMap<Chunk> field_73236_b;
    @Shadow
    private List<Chunk> field_73237_c;

    @Override
    public LongHashMap<Chunk> chunkMapping() {
        return this.field_73236_b;
    }

    @Override
    public List<Chunk> chunkListing() {
        return this.field_73237_c;
    }
}

