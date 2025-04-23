/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntFunction;

@FunctionalInterface
public interface Short2ObjectFunction<V>
extends Function<Short, V>,
IntFunction<V> {
    @Override
    @Deprecated
    default public V apply(int operand) {
        return this.get(SafeMath.safeIntToShort(operand));
    }

    @Override
    default public V put(short key, V value) {
        throw new UnsupportedOperationException();
    }

    public V get(short var1);

    default public V remove(short key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public V put(Short key, V value) {
        short k = key;
        boolean containsKey = this.containsKey(k);
        V v = this.put(k, value);
        return (V)(containsKey ? v : null);
    }

    @Override
    @Deprecated
    default public V get(Object key) {
        if (key == null) {
            return null;
        }
        short k = (Short)key;
        V v = this.get(k);
        return (V)(v != this.defaultReturnValue() || this.containsKey(k) ? v : null);
    }

    @Override
    @Deprecated
    default public V remove(Object key) {
        if (key == null) {
            return null;
        }
        short k = (Short)key;
        return this.containsKey(k) ? (V)this.remove(k) : null;
    }

    default public boolean containsKey(short key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Short)key);
    }

    default public void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    default public V defaultReturnValue() {
        return null;
    }
}

