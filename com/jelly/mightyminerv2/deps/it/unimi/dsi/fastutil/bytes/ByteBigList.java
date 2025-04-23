/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Size64;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteBigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;

public interface ByteBigList
extends BigList<Byte>,
ByteCollection,
Size64,
Comparable<BigList<? extends Byte>> {
    @Override
    public ByteBigListIterator iterator();

    public ByteBigListIterator listIterator();

    public ByteBigListIterator listIterator(long var1);

    public ByteBigList subList(long var1, long var3);

    public void getElements(long var1, byte[][] var3, long var4, long var6);

    public void removeElements(long var1, long var3);

    public void addElements(long var1, byte[][] var3);

    public void addElements(long var1, byte[][] var3, long var4, long var6);

    @Override
    public void add(long var1, byte var3);

    public boolean addAll(long var1, ByteCollection var3);

    public boolean addAll(long var1, ByteBigList var3);

    public boolean addAll(ByteBigList var1);

    public byte getByte(long var1);

    public byte removeByte(long var1);

    @Override
    public byte set(long var1, byte var3);

    public long indexOf(byte var1);

    public long lastIndexOf(byte var1);

    @Override
    @Deprecated
    public void add(long var1, Byte var3);

    @Override
    @Deprecated
    public Byte get(long var1);

    @Override
    @Deprecated
    public long indexOf(Object var1);

    @Override
    @Deprecated
    public long lastIndexOf(Object var1);

    @Override
    @Deprecated
    public Byte remove(long var1);

    @Override
    @Deprecated
    public Byte set(long var1, Byte var3);
}

