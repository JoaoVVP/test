/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.SortedSet;

public interface ObjectSortedSet<K>
extends ObjectSet<K>,
SortedSet<K>,
ObjectBidirectionalIterable<K> {
    public ObjectBidirectionalIterator<K> iterator(K var1);

    @Override
    public ObjectBidirectionalIterator<K> iterator();

    @Override
    public ObjectSortedSet<K> subSet(K var1, K var2);

    @Override
    public ObjectSortedSet<K> headSet(K var1);

    @Override
    public ObjectSortedSet<K> tailSet(K var1);
}

