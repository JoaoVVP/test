/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;
import java.util.SortedMap;

public interface Double2LongSortedMap
extends Double2LongMap,
SortedMap<Double, Long> {
    public Double2LongSortedMap subMap(double var1, double var3);

    public Double2LongSortedMap headMap(double var1);

    public Double2LongSortedMap tailMap(double var1);

    public double firstDoubleKey();

    public double lastDoubleKey();

    @Deprecated
    default public Double2LongSortedMap subMap(Double from, Double to) {
        return this.subMap((double)from, (double)to);
    }

    @Deprecated
    default public Double2LongSortedMap headMap(Double to) {
        return this.headMap((double)to);
    }

    @Deprecated
    default public Double2LongSortedMap tailMap(Double from) {
        return this.tailMap((double)from);
    }

    @Override
    @Deprecated
    default public Double firstKey() {
        return this.firstDoubleKey();
    }

    @Override
    @Deprecated
    default public Double lastKey() {
        return this.lastDoubleKey();
    }

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<Double, Long>> entrySet() {
        return this.double2LongEntrySet();
    }

    public ObjectSortedSet<Double2LongMap.Entry> double2LongEntrySet();

    @Override
    public DoubleSortedSet keySet();

    @Override
    public LongCollection values();

    public DoubleComparator comparator();

    public static interface FastSortedEntrySet
    extends ObjectSortedSet<Double2LongMap.Entry>,
    Double2LongMap.FastEntrySet {
        public ObjectBidirectionalIterator<Double2LongMap.Entry> fastIterator();

        public ObjectBidirectionalIterator<Double2LongMap.Entry> fastIterator(Double2LongMap.Entry var1);
    }
}

