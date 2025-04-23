/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import java.util.function.ToIntFunction;

@FunctionalInterface
public interface Object2CharFunction<K>
extends Function<K, Character>,
ToIntFunction<K> {
    @Override
    default public int applyAsInt(K operand) {
        return this.getChar(operand);
    }

    @Override
    default public char put(K key, char value) {
        throw new UnsupportedOperationException();
    }

    public char getChar(Object var1);

    default public char removeChar(Object key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Character put(K key, Character value) {
        K k = key;
        boolean containsKey = this.containsKey(k);
        char v = this.put(k, value.charValue());
        return containsKey ? Character.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Character get(Object key) {
        Object k = key;
        char v = this.getChar(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Character.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Character remove(Object key) {
        Object k = key;
        return this.containsKey(k) ? Character.valueOf(this.removeChar(k)) : null;
    }

    default public void defaultReturnValue(char rv) {
        throw new UnsupportedOperationException();
    }

    default public char defaultReturnValue() {
        return '\u0000';
    }
}

