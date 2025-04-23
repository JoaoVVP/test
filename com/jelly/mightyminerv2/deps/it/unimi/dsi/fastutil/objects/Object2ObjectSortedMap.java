/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Comparator;
import java.util.Map;
import java.util.SortedMap;

public interface Object2ObjectSortedMap<K, V>
extends Object2ObjectMap<K, V>,
SortedMap<K, V> {
    @Override
    public Object2ObjectSortedMap<K, V> subMap(K var1, K var2);

    @Override
    public Object2ObjectSortedMap<K, V> headMap(K var1);

    @Override
    public Object2ObjectSortedMap<K, V> tailMap(K var1);

    @Override
    default public ObjectSortedSet<Map.Entry<K, V>> entrySet() {
        return this.object2ObjectEntrySet();
    }

    @Override
    public ObjectSortedSet<Object2ObjectMap.Entry<K, V>> object2ObjectEntrySet();

    @Override
    public ObjectSortedSet<K> keySet();

    @Override
    public ObjectCollection<V> values();

    @Override
    public Comparator<? super K> comparator();

    public static interface FastSortedEntrySet<K, V>
    extends ObjectSortedSet<Object2ObjectMap.Entry<K, V>>,
    Object2ObjectMap.FastEntrySet<K, V> {
        @Override
        public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> fastIterator();

        public ObjectBidirectionalIterator<Object2ObjectMap.Entry<K, V>> fastIterator(Object2ObjectMap.Entry<K, V> var1);
    }
}

