/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.LongHashMap
 *  net.minecraft.world.chunk.Chunk
 */
package com.jelly.mightyminerv2.util;

import java.util.List;
import net.minecraft.util.LongHashMap;
import net.minecraft.world.chunk.Chunk;

public interface IChunkProviderClient {
    public LongHashMap<Chunk> chunkMapping();

    public List<Chunk> chunkListing();
}

