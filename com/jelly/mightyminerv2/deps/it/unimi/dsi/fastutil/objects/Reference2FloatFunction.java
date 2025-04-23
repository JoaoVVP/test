/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.ToDoubleFunction;

@FunctionalInterface
public interface Reference2FloatFunction<K>
extends Function<K, Float>,
ToDoubleFunction<K> {
    @Override
    default public double applyAsDouble(K operand) {
        return this.getFloat(operand);
    }

    @Override
    default public float put(K key, float value) {
        throw new UnsupportedOperationException();
    }

    public float getFloat(Object var1);

    default public float removeFloat(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Float put(K key, Float value) {
        K k = key;
        boolean containsKey = this.containsKey(k);
        float v = this.put(k, value.floatValue());
        return containsKey ? Float.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Float get(Object key) {
        Object k = key;
        float v = this.getFloat(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Float.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Float remove(Object key) {
        Object k = key;
        return this.containsKey(k) ? Float.valueOf(this.removeFloat(k)) : null;
    }

    default public void defaultReturnValue(float rv) {
        throw new UnsupportedOperationException();
    }

    default public float defaultReturnValue() {
        return 0.0f;
    }
}

