/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface Short2ByteFunction
extends Function<Short, Byte>,
IntUnaryOperator {
    @Override
    @Deprecated
    default public int applyAsInt(int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }

    @Override
    default public byte put(short key, byte value) {
        throw new UnsupportedOperationException();
    }

    public byte get(short var1);

    default public byte remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Byte put(Short key, Byte value) {
        short k = key;
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
        short k = (Short)key;
        byte v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Byte.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Byte remove(Object key) {
        if (key == null) {
            return null;
        }
        short k = (Short)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(short key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Short)key);
    }

    default public void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default public byte defaultReturnValue() {
        return 0;
    }
}

