/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2DoubleFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.ToDoubleFunction;

public interface Object2DoubleMap<K>
extends Object2DoubleFunction<K>,
Map<K, Double> {
    @Override
    public int size();

    @Override
    default public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void defaultReturnValue(double var1);

    @Override
    public double defaultReturnValue();

    public ObjectSet<Entry<K>> object2DoubleEntrySet();

    @Override
    @Deprecated
    default public ObjectSet<Map.Entry<K, Double>> entrySet() {
        return this.object2DoubleEntrySet();
    }

    @Override
    @Deprecated
    default public Double put(K key, Double value) {
        return Object2DoubleFunction.super.put(key, value);
    }

    @Override
    @Deprecated
    default public Double get(Object key) {
        return Object2DoubleFunction.super.get(key);
    }

    @Override
    @Deprecated
    default public Double remove(Object key) {
        return Object2DoubleFunction.super.remove(key);
    }

    @Override
    public ObjectSet<K> keySet();

    public DoubleCollection values();

    @Override
    public boolean containsKey(Object var1);

    public boolean containsValue(double var1);

    @Override
    @Deprecated
    default public boolean containsValue(Object value) {
        return value == null ? false : this.containsValue((Double)value);
    }

    @Override
    default public double getOrDefault(Object key, double defaultValue) {
        double v = this.getDouble(key);
        return v != this.defaultReturnValue() || this.containsKey(key) ? v : defaultValue;
    }

    @Override
    default public double putIfAbsent(K key, double value) {
        double drv;
        double v = this.getDouble(key);
        if (v != (drv = this.defaultReturnValue()) || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }

    default public boolean remove(Object key, double value) {
        double curValue = this.getDouble(key);
        if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(value) || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.removeDouble(key);
        return true;
    }

    @Override
    default public boolean replace(K key, double oldValue, double newValue) {
        double curValue = this.getDouble(key);
        if (Double.doubleToLongBits(curValue) != Double.doubleToLongBits(oldValue) || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }

    @Override
    default public double replace(K key, double value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }

    default public double computeDoubleIfAbsent(K key, ToDoubleFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        double v = this.getDouble(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        double newValue = mappingFunction.applyAsDouble(key);
        this.put(key, newValue);
        return newValue;
    }

    default public double computeDoubleIfAbsentPartial(K key, Object2DoubleFunction<? super K> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        double v = this.getDouble(key);
        double drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        double newValue = mappingFunction.getDouble(key);
        this.put(key, newValue);
        return newValue;
    }

    default public double computeDoubleIfPresent(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        double oldValue = this.getDouble(key);
        double drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        Double newValue = remappingFunction.apply(key, oldValue);
        if (newValue == null) {
            this.removeDouble(key);
            return drv;
        }
        double newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }

    default public double computeDouble(K key, BiFunction<? super K, ? super Double, ? extends Double> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        double oldValue = this.getDouble(key);
        double drv = this.defaultReturnValue();
        boolean contained = oldValue != drv || this.containsKey(key);
        Double newValue = remappingFunction.apply(key, contained ? Double.valueOf(oldValue) : null);
        if (newValue == null) {
            if (contained) {
                this.removeDouble(key);
            }
            return drv;
        }
        double newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }

    default public double mergeDouble(K key, double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        double newValue;
        Objects.requireNonNull(remappingFunction);
        double oldValue = this.getDouble(key);
        double drv = this.defaultReturnValue();
        if (oldValue != drv || this.containsKey(key)) {
            Double mergedValue = remappingFunction.apply((Double)oldValue, (Double)value);
            if (mergedValue == null) {
                this.removeDouble(key);
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
    default public Double getOrDefault(Object key, Double defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    @Deprecated
    default public Double putIfAbsent(K key, Double value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    @Deprecated
    default public boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    @Deprecated
    default public boolean replace(K key, Double oldValue, Double newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Override
    @Deprecated
    default public Double replace(K key, Double value) {
        return Map.super.replace(key, value);
    }

    @Override
    @Deprecated
    default public Double merge(K key, Double value, BiFunction<? super Double, ? super Double, ? extends Double> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

    public static interface Entry<K>
    extends Map.Entry<K, Double> {
        public double getDoubleValue();

        @Override
        public double setValue(double var1);

        @Override
        @Deprecated
        default public Double getValue() {
            return this.getDoubleValue();
        }

        @Override
        @Deprecated
        default public Double setValue(Double value) {
            return this.setValue((double)value);
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

