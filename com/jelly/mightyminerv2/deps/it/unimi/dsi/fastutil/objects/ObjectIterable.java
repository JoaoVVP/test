/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;

public interface ObjectIterable<K>
extends Iterable<K> {
    @Override
    public ObjectIterator<K> iterator();
}

