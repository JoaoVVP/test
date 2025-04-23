/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil;

import java.util.Iterator;

public interface BidirectionalIterator<K>
extends Iterator<K> {
    public K previous();

    public boolean hasPrevious();
}

