/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2LongSortedMap<K>
extends Object2LongMap<K>,
SortedMap<K, Long> {
    public Object2LongSortedMap<K> subMap(K var1, K var2);

    public Object2LongSortedMap<K> headMap(K var1);

    public Object2LongSortedMap<K> tailMap(K var1);

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<K, Long>> entrySet() {
        return this.object2LongEntrySet();
    }

    @Override
    public ObjectSortedSet<Object2LongMap.Entry<K>> object2LongEntrySet();

    @Override
    public ObjectSortedSet<K> keySet();

    @Override
    public LongCollection values();

    @Override
    public Comparator<? super K> comparator();

    public static interface FastSortedEntrySet<K>
    extends ObjectSortedSet<Object2LongMap.Entry<K>>,
    Object2LongMap.FastEntrySet<K> {
        @Override
        public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> fastIterator();

        public ObjectBidirectionalIterator<Object2LongMap.Entry<K>> fastIterator(Object2LongMap.Entry<K> var1);
    }
}

