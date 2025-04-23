/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterable;

public interface ByteBidirectionalIterable
extends ByteIterable {
    @Override
    public ByteBidirectionalIterator iterator();
}

