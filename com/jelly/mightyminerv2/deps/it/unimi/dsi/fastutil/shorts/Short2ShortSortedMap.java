/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSortedSet;
import java.util.Map;
import java.util.SortedMap;

public interface Short2ShortSortedMap
extends Short2ShortMap,
SortedMap<Short, Short> {
    public Short2ShortSortedMap subMap(short var1, short var2);

    public Short2ShortSortedMap headMap(short var1);

    public Short2ShortSortedMap tailMap(short var1);

    public short firstShortKey();

    public short lastShortKey();

    @Deprecated
    default public Short2ShortSortedMap subMap(Short from, Short to) {
        return this.subMap((short)from, (short)to);
    }

    @Deprecated
    default public Short2ShortSortedMap headMap(Short to) {
        return this.headMap((short)to);
    }

    @Deprecated
    default public Short2ShortSortedMap tailMap(Short from) {
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
    default public ObjectSortedSet<Map.Entry<Short, Short>> entrySet() {
        return this.short2ShortEntrySet();
    }

    public ObjectSortedSet<Short2ShortMap.Entry> short2ShortEntrySet();

    @Override
    public ShortSortedSet keySet();

    @Override
    public ShortCollection values();

    public ShortComparator comparator();

    public static interface FastSortedEntrySet
    extends ObjectSortedSet<Short2ShortMap.Entry>,
    Short2ShortMap.FastEntrySet {
        public ObjectBidirectionalIterator<Short2ShortMap.Entry> fastIterator();

        public ObjectBidirectionalIterator<Short2ShortMap.Entry> fastIterator(Short2ShortMap.Entry var1);
    }
}

