/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2FloatMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;
import java.util.SortedMap;

public interface Long2FloatSortedMap
extends Long2FloatMap,
SortedMap<Long, Float> {
    public Long2FloatSortedMap subMap(long var1, long var3);

    public Long2FloatSortedMap headMap(long var1);

    public Long2FloatSortedMap tailMap(long var1);

    public long firstLongKey();

    public long lastLongKey();

    @Deprecated
    default public Long2FloatSortedMap subMap(Long from, Long to) {
        return this.subMap((long)from, (long)to);
    }

    @Deprecated
    default public Long2FloatSortedMap headMap(Long to) {
        return this.headMap((long)to);
    }

    @Deprecated
    default public Long2FloatSortedMap tailMap(Long from) {
        return this.tailMap((long)from);
    }

    @Override
    @Deprecated
    default public Long firstKey() {
        return this.firstLongKey();
    }

    @Override
    @Deprecated
    default public Long lastKey() {
        return this.lastLongKey();
    }

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<Long, Float>> entrySet() {
        return this.long2FloatEntrySet();
    }

    public ObjectSortedSet<Long2FloatMap.Entry> long2FloatEntrySet();

    @Override
    public LongSortedSet keySet();

    @Override
    public FloatCollection values();

    public LongComparator comparator();

    public static interface FastSortedEntrySet
    extends ObjectSortedSet<Long2FloatMap.Entry>,
    Long2FloatMap.FastEntrySet {
        public ObjectBidirectionalIterator<Long2FloatMap.Entry> fastIterator();

        public ObjectBidirectionalIterator<Long2FloatMap.Entry> fastIterator(Long2FloatMap.Entry var1);
    }
}

