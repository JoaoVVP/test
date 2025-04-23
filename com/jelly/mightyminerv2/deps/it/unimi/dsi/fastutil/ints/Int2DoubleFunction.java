/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.IntToDoubleFunction;

@FunctionalInterface
public interface Int2DoubleFunction
extends Function<Integer, Double>,
IntToDoubleFunction {
    @Override
    default public double applyAsDouble(int operand) {
        return this.get(operand);
    }

    @Override
    default public double put(int key, double value) {
        throw new UnsupportedOperationException();
    }

    public double get(int var1);

    default public double remove(int key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Double put(Integer key, Double value) {
        int k = key;
        boolean containsKey = this.containsKey(k);
        double v = this.put(k, (double)value);
        return containsKey ? Double.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Double get(Object key) {
        if (key == null) {
            return null;
        }
        int k = (Integer)key;
        double v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Double.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Double remove(Object key) {
        if (key == null) {
            return null;
        }
        int k = (Integer)key;
        return this.containsKey(k) ? Double.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(int key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Integer)key);
    }

    default public void defaultReturnValue(double rv) {
        throw new UnsupportedOperationException();
    }

    default public double defaultReturnValue() {
        return 0.0;
    }
}

