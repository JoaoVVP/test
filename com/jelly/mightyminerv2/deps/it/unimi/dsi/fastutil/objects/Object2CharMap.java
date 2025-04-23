/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2CharFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToIntFunction;

public interface Object2CharMap<K>
extends Object2CharFunction<K>,
Map<K, Character> {
    @Override
    public int size();

    @Override
    default public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void defaultReturnValue(char var1);

    @Override
    public char defaultReturnValue();

    public ObjectSet<Entry<K>> object2CharEntrySet();

    @Override
    @Deprecated
    default public ObjectSet<Map.Entry<K, Character>> entrySet() {
        return this.object2CharEntrySet();
    }

    @Override
    @Deprecated
    default public Character put(K key, Character value) {
        return Object2CharFunction.super.put(key, value);
    }

    @Override
    @Deprecated
    default public Character get(Object key) {
        return Object2CharFunction.super.get(key);
    }

    @Override
    @Deprecated
    default public Character remove(Object key) {
        return Object2CharFunction.super.remove(key);
    }

    @Override
    public ObjectSet<K> keySet();

    public CharCollection values();

    @Override
    public boolean containsKey(Object var1);

    public boolean containsValue(char var1);

    @Override
    @Deprecated
    default public boolean containsValue(Object value) {
        return value == null ? false : this.containsValue(((Character)value).charValue());
    }

    @Override
    default public char getOrDefault(Object key, char defaultValue) {
        char v = this.getChar(key);
        return v != this.defaultReturnValue() || this.containsKey(key) ? v : defaultValue;
    }

    @Override
    default public char putIfAbsent(K key, char value) {
        char drv;
        char v = this.getChar(key);
        if (v != (drv = this.defaultReturnValue()) || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }

    default public boolean remove(Object key, char value) {
        char curValue = this.getChar(key);
        if (curValue != value || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.removeChar(key);
        return true;
    }

    @Override
    default public boolean replace(K key, char oldValue, char newValue) {
        char curValue = this.getChar(key);
        if (curValue != oldValue || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }

    @Override
    default public char replace(K key, char value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }

    default public char computeCharIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        char v = this.getChar(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        char newValue = SafeMath.safeIntToChar(mappingFunction.applyAsInt(key));
        this.put(key, newValue);
        return newValue;
    }

    default public char computeCharIfAbsentPartial(K key, Object2CharFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        char v = this.getChar(key);
        char drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        char newValue = mappingFunction.getChar(key);
        this.put(key, newValue);
        return newValue;
    }

    default public char computeCharIfPresent(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        char oldValue = this.getChar(key);
        char drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        Character newValue = remappingFunction.apply(key, Character.valueOf(oldValue));
        if (newValue == null) {
            this.removeChar(key);
            return drv;
        }
        char newVal = newValue.charValue();
        this.put(key, newVal);
        return newVal;
    }

    default public char computeChar(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        char oldValue = this.getChar(key);
        char drv = this.defaultReturnValue();
        boolean contained = oldValue != drv || this.containsKey(key);
        Character newValue = remappingFunction.apply(key, contained ? Character.valueOf(oldValue) : null);
        if (newValue == null) {
            if (contained) {
                this.removeChar(key);
            }
            return drv;
        }
        char newVal = newValue.charValue();
        this.put(key, newVal);
        return newVal;
    }

    default public char mergeChar(K key, char value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        char newValue;
        Objects.requireNonNull(remappingFunction);
        char oldValue = this.getChar(key);
        char drv = this.defaultReturnValue();
        if (oldValue != drv || this.containsKey(key)) {
            Character mergedValue = remappingFunction.apply(Character.valueOf(oldValue), Character.valueOf(value));
            if (mergedValue == null) {
                this.removeChar(key);
                return drv;
            }
            newValue = mergedValue.charValue();
        } else {
            newValue = value;
        }
        this.put(key, newValue);
        return newValue;
    }

    @Override
    @Deprecated
    default public Character getOrDefault(Object key, Character defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    @Deprecated
    default public Character putIfAbsent(K key, Character value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    @Deprecated
    default public boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    @Deprecated
    default public boolean replace(K key, Character oldValue, Character newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Override
    @Deprecated
    default public Character replace(K key, Character value) {
        return Map.super.replace(key, value);
    }

    @Override
    @Deprecated
    default public Character merge(K key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

    public static interface Entry<K>
    extends Map.Entry<K, Character> {
        public char getCharValue();

        @Override
        public char setValue(char var1);

        @Override
        @Deprecated
        default public Character getValue() {
            return Character.valueOf(this.getCharValue());
        }

        @Override
        @Deprecated
        default public Character setValue(Character value) {
            return Character.valueOf(this.setValue(value.charValue()));
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

