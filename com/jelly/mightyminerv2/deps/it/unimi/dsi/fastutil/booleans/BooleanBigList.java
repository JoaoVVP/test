/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Size64;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanBigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanCollection;

public interface BooleanBigList
extends BigList<Boolean>,
BooleanCollection,
Size64,
Comparable<BigList<? extends Boolean>> {
    @Override
    public BooleanBigListIterator iterator();

    public BooleanBigListIterator listIterator();

    public BooleanBigListIterator listIterator(long var1);

    public BooleanBigList subList(long var1, long var3);

    public void getElements(long var1, boolean[][] var3, long var4, long var6);

    public void removeElements(long var1, long var3);

    public void addElements(long var1, boolean[][] var3);

    public void addElements(long var1, boolean[][] var3, long var4, long var6);

    @Override
    public void add(long var1, boolean var3);

    public boolean addAll(long var1, BooleanCollection var3);

    public boolean addAll(long var1, BooleanBigList var3);

    public boolean addAll(BooleanBigList var1);

    public boolean getBoolean(long var1);

    public boolean removeBoolean(long var1);

    @Override
    public boolean set(long var1, boolean var3);

    public long indexOf(boolean var1);

    public long lastIndexOf(boolean var1);

    @Override
    @Deprecated
    public void add(long var1, Boolean var3);

    @Override
    @Deprecated
    public Boolean get(long var1);

    @Override
    @Deprecated
    public long indexOf(Object var1);

    @Override
    @Deprecated
    public long lastIndexOf(Object var1);

    @Override
    @Deprecated
    public Boolean remove(long var1);

    @Override
    @Deprecated
    public Boolean set(long var1, Boolean var3);
}

