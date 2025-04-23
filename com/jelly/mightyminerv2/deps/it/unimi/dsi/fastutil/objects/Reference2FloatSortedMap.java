/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2FloatMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Reference2FloatSortedMap<K>
extends Reference2FloatMap<K>,
SortedMap<K, Float> {
    public Reference2FloatSortedMap<K> subMap(K var1, K var2);

    public Reference2FloatSortedMap<K> headMap(K var1);

    public Reference2FloatSortedMap<K> tailMap(K var1);

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<K, Float>> entrySet() {
        return this.reference2FloatEntrySet();
    }

    @Override
    public ObjectSortedSet<Reference2FloatMap.Entry<K>> reference2FloatEntrySet();

    @Override
    public ReferenceSortedSet<K> keySet();

    @Override
    public FloatCollection values();

    @Override
    public Comparator<? super K> comparator();

    public static interface FastSortedEntrySet<K>
    extends ObjectSortedSet<Reference2FloatMap.Entry<K>>,
    Reference2FloatMap.FastEntrySet<K> {
        @Override
        public ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> fastIterator();

        public ObjectBidirectionalIterator<Reference2FloatMap.Entry<K>> fastIterator(Reference2FloatMap.Entry<K> var1);
    }
}

