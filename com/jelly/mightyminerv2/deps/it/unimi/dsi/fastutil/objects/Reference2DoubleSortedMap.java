/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2DoubleMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2DoubleSortedMap<K>
extends Reference2DoubleMap<K>,
SortedMap<K, Double> {
    public Reference2DoubleSortedMap<K> subMap(K var1, K var2);

    public Reference2DoubleSortedMap<K> headMap(K var1);

    public Reference2DoubleSortedMap<K> tailMap(K var1);

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<K, Double>> entrySet() {
        return this.reference2DoubleEntrySet();
    }

    @Override
    public ObjectSortedSet<Reference2DoubleMap.Entry<K>> reference2DoubleEntrySet();

    @Override
    public ReferenceSortedSet<K> keySet();

    @Override
    public DoubleCollection values();

    @Override
    public Comparator<? super K> comparator();

    public static interface FastSortedEntrySet<K>
    extends ObjectSortedSet<Reference2DoubleMap.Entry<K>>,
    Reference2DoubleMap.FastEntrySet<K> {
        @Override
        public ObjectBidirectionalIterator<Reference2DoubleMap.Entry<K>> fastIterator();

        public ObjectBidirectionalIterator<Reference2DoubleMap.Entry<K>> fastIterator(Reference2DoubleMap.Entry<K> var1);
    }
}

