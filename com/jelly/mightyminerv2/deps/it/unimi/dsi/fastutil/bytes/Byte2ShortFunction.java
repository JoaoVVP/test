/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntUnaryOperator;

@FunctionalInterface
public interface Byte2ShortFunction
extends Function<Byte, Short>,
IntUnaryOperator {
    @Override
    @Deprecated
    default public int applyAsInt(int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }

    @Override
    default public short put(byte key, short value) {
        throw new UnsupportedOperationException();
    }

    public short get(byte var1);

    default public short remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Short put(Byte key, Short value) {
        byte k = key;
        boolean containsKey = this.containsKey(k);
        short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Short get(Object key) {
        if (key == null) {
            return null;
        }
        byte k = (Byte)key;
        short v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Short.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Short remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = (Byte)key;
        return this.containsKey(k) ? Short.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(byte key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Byte)key);
    }

    default public void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default public short defaultReturnValue() {
        return 0;
    }
}

