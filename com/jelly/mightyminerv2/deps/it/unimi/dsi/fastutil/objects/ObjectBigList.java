/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Size64;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectCollection;

public interface ObjectBigList<K>
extends BigList<K>,
ObjectCollection<K>,
Size64,
Comparable<BigList<? extends K>> {
    @Override
    public ObjectBigListIterator<K> iterator();

    @Override
    public ObjectBigListIterator<K> listIterator();

    @Override
    public ObjectBigListIterator<K> listIterator(long var1);

    @Override
    public ObjectBigList<K> subList(long var1, long var3);

    public void getElements(long var1, Object[][] var3, long var4, long var6);

    public void removeElements(long var1, long var3);

    public void addElements(long var1, K[][] var3);

    public void addElements(long var1, K[][] var3, long var4, long var6);
}

