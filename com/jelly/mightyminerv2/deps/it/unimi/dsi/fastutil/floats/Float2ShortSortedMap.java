/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortCollection;
import java.util.Map;
import java.util.SortedMap;

public interface Float2ShortSortedMap
extends Float2ShortMap,
SortedMap<Float, Short> {
    public Float2ShortSortedMap subMap(float var1, float var2);

    public Float2ShortSortedMap headMap(float var1);

    public Float2ShortSortedMap tailMap(float var1);

    public float firstFloatKey();

    public float lastFloatKey();

    @Deprecated
    default public Float2ShortSortedMap subMap(Float from, Float to) {
        return this.subMap(from.floatValue(), to.floatValue());
    }

    @Deprecated
    default public Float2ShortSortedMap headMap(Float to) {
        return this.headMap(to.floatValue());
    }

    @Deprecated
    default public Float2ShortSortedMap tailMap(Float from) {
        return this.tailMap(from.floatValue());
    }

    @Override
    @Deprecated
    default public Float firstKey() {
        return Float.valueOf(this.firstFloatKey());
    }

    @Override
    @Deprecated
    default public Float lastKey() {
        return Float.valueOf(this.lastFloatKey());
    }

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
        return this.float2ShortEntrySet();
    }

    public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet();

    @Override
    public FloatSortedSet keySet();

    @Override
    public ShortCollection values();

    public FloatComparator comparator();

    public static interface FastSortedEntrySet
    extends ObjectSortedSet<Float2ShortMap.Entry>,
    Float2ShortMap.FastEntrySet {
        public ObjectBidirectionalIterator<Float2ShortMap.Entry> fastIterator();

        public ObjectBidirectionalIterator<Float2ShortMap.Entry> fastIterator(Float2ShortMap.Entry var1);
    }
}

