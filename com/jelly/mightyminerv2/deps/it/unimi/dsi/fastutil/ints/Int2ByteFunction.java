/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface Int2ByteFunction
extends Function<Integer, Byte>,
IntUnaryOperator {
    @Override
    default public int applyAsInt(int operand) {
        return this.get(operand);
    }

    @Override
    default public byte put(int key, byte value) {
        throw new UnsupportedOperationException();
    }

    public byte get(int var1);

    default public byte remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Byte put(Integer key, Byte value) {
        int k = key;
        boolean containsKey = this.containsKey(k);
        byte v = this.put(k, (byte)value);
        return containsKey ? Byte.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Byte get(Object key) {
        if (key == null) {
            return null;
        }
        int k = (Integer)key;
        byte v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Byte.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Byte remove(Object key) {
        if (key == null) {
            return null;
        }
        int k = (Integer)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(int key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Integer)key);
    }

    default public void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default public byte defaultReturnValue() {
        return 0;
    }
}

