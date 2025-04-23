/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.List;

public interface ReferenceList<K>
extends List<K>,
ReferenceCollection<K> {
    @Override
    public ObjectListIterator<K> iterator();

    @Override
    public ObjectListIterator<K> listIterator();

    @Override
    public ObjectListIterator<K> listIterator(int var1);

    @Override
    public ReferenceList<K> subList(int var1, int var2);

    public void size(int var1);

    public void getElements(int var1, Object[] var2, int var3, int var4);

    public void removeElements(int var1, int var2);

    public void addElements(int var1, K[] var2);

    public void addElements(int var1, K[] var2, int var3, int var4);
}

