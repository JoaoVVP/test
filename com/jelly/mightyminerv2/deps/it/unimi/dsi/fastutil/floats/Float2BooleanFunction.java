/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.function.DoublePredicate;

@FunctionalInterface
public interface Float2BooleanFunction
extends Function<Float, Boolean>,
DoublePredicate {
    @Override
    @Deprecated
    default public boolean test(double operand) {
        return this.get(SafeMath.safeDoubleToFloat(operand));
    }

    @Override
    default public boolean put(float key, boolean value) {
        throw new UnsupportedOperationException();
    }

    public boolean get(float var1);

    default public boolean remove(float key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Boolean put(Float key, Boolean value) {
        float k = key.floatValue();
        boolean containsKey = this.containsKey(k);
        boolean v = this.put(k, (boolean)value);
        return containsKey ? Boolean.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Boolean get(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float)key).floatValue();
        boolean v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Boolean.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Boolean remove(Object key) {
        if (key == null) {
            return null;
        }
        float k = ((Float)key).floatValue();
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(float key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey(((Float)key).floatValue());
    }

    default public void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default public boolean defaultReturnValue() {
        return false;
    }
}

