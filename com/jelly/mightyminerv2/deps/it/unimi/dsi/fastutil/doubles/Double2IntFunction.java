/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.DoubleToIntFunction;

@FunctionalInterface
public interface Double2IntFunction
extends Function<Double, Integer>,
DoubleToIntFunction {
    @Override
    default public int applyAsInt(double operand) {
        return this.get(operand);
    }

    @Override
    default public int put(double key, int value) {
        throw new UnsupportedOperationException();
    }

    public int get(double var1);

    default public int remove(double key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Integer put(Double key, Integer value) {
        double k = key;
        boolean containsKey = this.containsKey(k);
        int v = this.put(k, (int)value);
        return containsKey ? Integer.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Integer get(Object key) {
        if (key == null) {
            return null;
        }
        double k = (Double)key;
        int v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Integer.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Integer remove(Object key) {
        if (key == null) {
            return null;
        }
        double k = (Double)key;
        return this.containsKey(k) ? Integer.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(double key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Double)key);
    }

    default public void defaultReturnValue(int rv) {
        throw new UnsupportedOperationException();
    }

    default public int defaultReturnValue() {
        return 0;
    }
}

