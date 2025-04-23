/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
public interface Double2ByteFunction
extends Function<Double, Byte>,
DoubleToIntFunction {
    @Override
    default public int applyAsInt(double operand) {
        return this.get(operand);
    }

    @Override
    default public byte put(double key, byte value) {
        throw new UnsupportedOperationException();
    }

    public byte get(double var1);

    default public byte remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Byte put(Double key, Byte value) {
        double k = key;
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
        double k = (Double)key;
        byte v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Byte.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Byte remove(Object key) {
        if (key == null) {
            return null;
        }
        double k = (Double)key;
        return this.containsKey(k) ? Byte.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(double key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Double)key);
    }

    default public void defaultReturnValue(byte rv) {
        throw new UnsupportedOperationException();
    }

    default public byte defaultReturnValue() {
        return 0;
    }
}

