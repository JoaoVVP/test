/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ReferenceSortedMap<K, V>
extends Object2ReferenceMap<K, V>,
SortedMap<K, V> {
    @Override
    public Object2ReferenceSortedMap<K, V> subMap(K var1, K var2);

    @Override
    public Object2ReferenceSortedMap<K, V> headMap(K var1);

    @Override
    public Object2ReferenceSortedMap<K, V> tailMap(K var1);

    @Override
    default public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return this.object2ReferenceEntrySet();
    }

    @Override
    public ObjectSortedSet<Object2ReferenceMap.Entry<K, V>> object2ReferenceEntrySet();

    @Override
    public ObjectSortedSet<K> keySet();

    @Override
    public ReferenceCollection<V> values();

    @Override
    public Comparator<? super K> comparator();

    public static interface FastSortedEntrySet<K, V>
    extends ObjectSortedSet<Object2ReferenceMap.Entry<K, V>>,
    Object2ReferenceMap.FastEntrySet<K, V> {
        @Override
        public ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> fastIterator();

        public ObjectBidirectionalIterator<Object2ReferenceMap.Entry<K, V>> fastIterator(Object2ReferenceMap.Entry<K, V> var1);
    }
}

