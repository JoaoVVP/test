/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSortedSet;
import java.util.Map;
import java.util.SortedMap;

public interface Short2ObjectSortedMap<V>
extends Short2ObjectMap<V>,
SortedMap<Short, V> {
    public Short2ObjectSortedMap<V> subMap(short var1, short var2);

    public Short2ObjectSortedMap<V> headMap(short var1);

    public Short2ObjectSortedMap<V> tailMap(short var1);

    public short firstShortKey();

    public short lastShortKey();

    @Deprecated
    default public Short2ObjectSortedMap<V> subMap(Short from, Short to) {
        return this.subMap((short)from, (short)to);
    }

    @Deprecated
    default public Short2ObjectSortedMap<V> headMap(Short to) {
        return this.headMap((short)to);
    }

    @Deprecated
    default public Short2ObjectSortedMap<V> tailMap(Short from) {
        return this.tailMap((short)from);
    }

    @Override
    @Deprecated
    default public Short firstKey() {
        return this.firstShortKey();
    }

    @Override
    @Deprecated
    default public Short lastKey() {
        return this.lastShortKey();
    }

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<Short, V>> entrySet() {
        return this.short2ObjectEntrySet();
    }

    @Override
    public ObjectSortedSet<Short2ObjectMap.Entry<V>> short2ObjectEntrySet();

    @Override
    public ShortSortedSet keySet();

    @Override
    public ObjectCollection<V> values();

    public ShortComparator comparator();

    public static interface FastSortedEntrySet<V>
    extends ObjectSortedSet<Short2ObjectMap.Entry<V>>,
    Short2ObjectMap.FastEntrySet<V> {
        @Override
        public ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> fastIterator();

        public ObjectBidirectionalIterator<Short2ObjectMap.Entry<V>> fastIterator(Short2ObjectMap.Entry<V> var1);
    }
}

