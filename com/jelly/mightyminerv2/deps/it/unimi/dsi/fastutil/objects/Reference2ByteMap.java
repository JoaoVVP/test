/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public interface Reference2ByteMap<K>
extends Reference2ByteFunction<K>,
Map<K, Byte> {
    @Override
    public int size();

    @Override
    default public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void defaultReturnValue(byte var1);

    @Override
    public byte defaultReturnValue();

    public ObjectSet<Entry<K>> reference2ByteEntrySet();

    @Override
    @Deprecated
    default public ObjectSet<Map.Entry<K, Byte>> entrySet() {
        return this.reference2ByteEntrySet();
    }

    @Override
    @Deprecated
    default public Byte put(K key, Byte value) {
        return Reference2ByteFunction.super.put(key, value);
    }

    @Override
    @Deprecated
    default public Byte get(Object key) {
        return Reference2ByteFunction.super.get(key);
    }

    @Override
    @Deprecated
    default public Byte remove(Object key) {
        return Reference2ByteFunction.super.remove(key);
    }

    @Override
    public ReferenceSet<K> keySet();

    public ByteCollection values();

    @Override
    public boolean containsKey(Object var1);

    public boolean containsValue(byte var1);

    @Override
    @Deprecated
    default public boolean containsValue(Object value) {
        return value == null ? false : this.containsValue((Byte)value);
    }

    @Override
    default public byte getOrDefault(Object key, byte defaultValue) {
        byte v = this.getByte(key);
        return v != this.defaultReturnValue() || this.containsKey(key) ? v : defaultValue;
    }

    @Override
    default public byte putIfAbsent(K key, byte value) {
        byte drv;
        byte v = this.getByte(key);
        if (v != (drv = this.defaultReturnValue()) || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }

    default public boolean remove(Object key, byte value) {
        byte curValue = this.getByte(key);
        if (curValue != value || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.removeByte(key);
        return true;
    }

    @Override
    default public boolean replace(K key, byte oldValue, byte newValue) {
        byte curValue = this.getByte(key);
        if (curValue != oldValue || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }

    @Override
    default public byte replace(K key, byte value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }

    default public byte computeByteIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        byte v = this.getByte(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }

    default public byte computeByteIfAbsentPartial(K key, Reference2ByteFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        byte v = this.getByte(key);
        byte drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        byte newValue = mappingFunction.getByte(key);
        this.put(key, newValue);
        return newValue;
    }

    default public byte computeByteIfPresent(K key, BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        byte oldValue = this.getByte(key);
        byte drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        Byte newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeByte(key);
            return drv;
        }
        byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }

    default public byte computeByte(K key, BiFunction<? super K, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        byte oldValue = this.getByte(key);
        byte drv = this.defaultReturnValue();
        boolean contained = oldValue != drv || this.containsKey(key);
        Byte newValue = remappingFunction.apply(key, contained ? Byte.valueOf(oldValue) : null);
        if (newValue == null) {
            if (contained) {
                this.removeByte(key);
            }
            return drv;
        }
        byte newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }

    default public byte mergeByte(K key, byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        byte newValue;
        Objects.requireNonNull(remappingFunction);
        byte oldValue = this.getByte(key);
        byte drv = this.defaultReturnValue();
        if (oldValue != drv || this.containsKey(key)) {
            Byte mergedValue = remappingFunction.apply((Byte)oldValue, (Byte)value);
            if (mergedValue == null) {
                this.removeByte(key);
                return drv;
            }
            newValue = mergedValue;
        } else {
            newValue = value;
        }
        this.put(key, newValue);
        return newValue;
    }

    @Override
    @Deprecated
    default public Byte getOrDefault(Object key, Byte defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    @Deprecated
    default public Byte putIfAbsent(K key, Byte value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    @Deprecated
    default public boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    @Deprecated
    default public boolean replace(K key, Byte oldValue, Byte newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Override
    @Deprecated
    default public Byte replace(K key, Byte value) {
        return Map.super.replace(key, value);
    }

    @Override
    @Deprecated
    default public Byte merge(K key, Byte value, BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

    public static interface Entry<K>
    extends Map.Entry<K, Byte> {
        public byte getByteValue();

        @Override
        public byte setValue(byte var1);

        @Override
        @Deprecated
        default public Byte getValue() {
            return this.getByteValue();
        }

        @Override
        @Deprecated
        default public Byte setValue(Byte value) {
            return this.setValue((byte)value);
        }
    }

    public static interface FastEntrySet<K>
    extends ObjectSet<Entry<K>> {
        public ObjectIterator<Entry<K>> fastIterator();

        default public void fastForEach(Consumer<? super Entry<K>> consumer) {
            this.forEach(consumer);
        }
    }
}

