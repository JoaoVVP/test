/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface Reference2ShortFunction<K>
extends Function<K, Short>,
ToIntFunction<K> {
    @Override
    default public int applyAsInt(K operand) {
        return this.getShort(operand);
    }

    @Override
    default public short put(K key, short value) {
        throw new UnsupportedOperationException();
    }

    public short getShort(Object var1);

    default public short removeShort(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Short put(K key, Short value) {
        K k = key;
        boolean containsKey = this.containsKey(k);
        short v = this.put(k, (short)value);
        return containsKey ? Short.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Short get(Object key) {
        Object k = key;
        short v = this.getShort(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Short.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Short remove(Object key) {
        Object k = key;
        return this.containsKey(k) ? Short.valueOf(this.removeShort(k)) : null;
    }

    default public void defaultReturnValue(short rv) {
        throw new UnsupportedOperationException();
    }

    default public short defaultReturnValue() {
        return 0;
    }
}

