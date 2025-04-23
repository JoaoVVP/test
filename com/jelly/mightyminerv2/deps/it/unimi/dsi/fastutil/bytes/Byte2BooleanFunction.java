/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.function.IntPredicate;

@FunctionalInterface
public interface Byte2BooleanFunction
extends Function<Byte, Boolean>,
IntPredicate {
    @Override
    @Deprecated
    default public boolean test(int operand) {
        return this.get(SafeMath.safeIntToByte(operand));
    }

    @Override
    default public boolean put(byte key, boolean value) {
        throw new UnsupportedOperationException();
    }

    public boolean get(byte var1);

    default public boolean remove(byte key) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public Boolean put(Byte key, Boolean value) {
        byte k = key;
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
        byte k = (Byte)key;
        boolean v = this.get(k);
        return v != this.defaultReturnValue() || this.containsKey(k) ? Boolean.valueOf(v) : null;
    }

    @Override
    @Deprecated
    default public Boolean remove(Object key) {
        if (key == null) {
            return null;
        }
        byte k = (Byte)key;
        return this.containsKey(k) ? Boolean.valueOf(this.remove(k)) : null;
    }

    default public boolean containsKey(byte key) {
        return true;
    }

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return key == null ? false : this.containsKey((Byte)key);
    }

    default public void defaultReturnValue(boolean rv) {
        throw new UnsupportedOperationException();
    }

    default public boolean defaultReturnValue() {
        return false;
    }
}

