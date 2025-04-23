/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Size64;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharCollection;

public interface CharBigList
extends BigList<Character>,
CharCollection,
Size64,
Comparable<BigList<? extends Character>> {
    @Override
    public CharBigListIterator iterator();

    public CharBigListIterator listIterator();

    public CharBigListIterator listIterator(long var1);

    public CharBigList subList(long var1, long var3);

    public void getElements(long var1, char[][] var3, long var4, long var6);

    public void removeElements(long var1, long var3);

    public void addElements(long var1, char[][] var3);

    public void addElements(long var1, char[][] var3, long var4, long var6);

    @Override
    public void add(long var1, char var3);

    public boolean addAll(long var1, CharCollection var3);

    public boolean addAll(long var1, CharBigList var3);

    public boolean addAll(CharBigList var1);

    public char getChar(long var1);

    public char removeChar(long var1);

    @Override
    public char set(long var1, char var3);

    public long indexOf(char var1);

    public long lastIndexOf(char var1);

    @Override
    @Deprecated
    public void add(long var1, Character var3);

    @Override
    @Deprecated
    public Character get(long var1);

    @Override
    @Deprecated
    public long indexOf(Object var1);

    @Override
    @Deprecated
    public long lastIndexOf(Object var1);

    @Override
    @Deprecated
    public Character remove(long var1);

    @Override
    @Deprecated
    public Character set(long var1, Character var3);
}

