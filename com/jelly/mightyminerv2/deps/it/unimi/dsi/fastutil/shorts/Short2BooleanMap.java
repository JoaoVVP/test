/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2BooleanFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSet;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public interface Short2BooleanMap
extends Short2BooleanFunction,
Map<Short, Boolean> {
    @Override
    public int size();

    @Override
    default public void clear() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void defaultReturnValue(boolean var1);

    @Override
    public boolean defaultReturnValue();

    public ObjectSet<Entry> short2BooleanEntrySet();

    @Override
    @Deprecated
    default public ObjectSet<Map.Entry<Short, Boolean>> entrySet() {
        return this.short2BooleanEntrySet();
    }

    @Override
    @Deprecated
    default public Boolean put(Short key, Boolean value) {
        return Short2BooleanFunction.super.put(key, value);
    }

    @Override
    @Deprecated
    default public Boolean get(Object key) {
        return Short2BooleanFunction.super.get(key);
    }

    @Override
    @Deprecated
    default public Boolean remove(Object key) {
        return Short2BooleanFunction.super.remove(key);
    }

    public ShortSet keySet();

    public BooleanCollection values();

    @Override
    public boolean containsKey(short var1);

    @Override
    @Deprecated
    default public boolean containsKey(Object key) {
        return Short2BooleanFunction.super.containsKey(key);
    }

    public boolean containsValue(boolean var1);

    @Override
    @Deprecated
    default public boolean containsValue(Object value) {
        return value == null ? false : this.containsValue((Boolean)value);
    }

    default public boolean getOrDefault(short key, boolean defaultValue) {
        boolean v = this.get(key);
        return v != this.defaultReturnValue() || this.containsKey(key) ? v : defaultValue;
    }

    @Override
    default public boolean putIfAbsent(short key, boolean value) {
        boolean drv;
        boolean v = this.get(key);
        if (v != (drv = this.defaultReturnValue()) || this.containsKey(key)) {
            return v;
        }
        this.put(key, value);
        return drv;
    }

    default public boolean remove(short key, boolean value) {
        boolean curValue = this.get(key);
        if (curValue != value || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.remove(key);
        return true;
    }

    @Override
    default public boolean replace(short key, boolean oldValue, boolean newValue) {
        boolean curValue = this.get(key);
        if (curValue != oldValue || curValue == this.defaultReturnValue() && !this.containsKey(key)) {
            return false;
        }
        this.put(key, newValue);
        return true;
    }

    @Override
    default public boolean replace(short key, boolean value) {
        return this.containsKey(key) ? this.put(key, value) : this.defaultReturnValue();
    }

    default public boolean computeIfAbsent(short key, IntPredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        boolean v = this.get(key);
        if (v != this.defaultReturnValue() || this.containsKey(key)) {
            return v;
        }
        boolean newValue = mappingFunction.test(key);
        this.put(key, newValue);
        return newValue;
    }

    default public boolean computeIfAbsentNullable(short key, IntFunction<? extends Boolean> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        boolean v = this.get(key);
        boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        Boolean mappedValue = mappingFunction.apply(key);
        if (mappedValue == null) {
            return drv;
        }
        boolean newValue = mappedValue;
        this.put(key, newValue);
        return newValue;
    }

    default public boolean computeIfAbsentPartial(short key, Short2BooleanFunction mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        boolean v = this.get(key);
        boolean drv = this.defaultReturnValue();
        if (v != drv || this.containsKey(key)) {
            return v;
        }
        if (!mappingFunction.containsKey(key)) {
            return drv;
        }
        boolean newValue = mappingFunction.get(key);
        this.put(key, newValue);
        return newValue;
    }

    @Override
    default public boolean computeIfPresent(short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        boolean oldValue = this.get(key);
        boolean drv = this.defaultReturnValue();
        if (oldValue == drv && !this.containsKey(key)) {
            return drv;
        }
        Boolean newValue = remappingFunction.apply((Short)key, (Boolean)oldValue);
        if (newValue == null) {
            this.remove(key);
            return drv;
        }
        boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }

    @Override
    default public boolean compute(short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        boolean oldValue = this.get(key);
        boolean drv = this.defaultReturnValue();
        boolean contained = oldValue != drv || this.containsKey(key);
        Boolean newValue = remappingFunction.apply((Short)key, contained ? Boolean.valueOf(oldValue) : null);
        if (newValue == null) {
            if (contained) {
                this.remove(key);
            }
            return drv;
        }
        boolean newVal = newValue;
        this.put(key, newVal);
        return newVal;
    }

    @Override
    default public boolean merge(short key, boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        boolean newValue;
        Objects.requireNonNull(remappingFunction);
        boolean oldValue = this.get(key);
        boolean drv = this.defaultReturnValue();
        if (oldValue != drv || this.containsKey(key)) {
            Boolean mergedValue = remappingFunction.apply((Boolean)oldValue, (Boolean)value);
            if (mergedValue == null) {
                this.remove(key);
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
    default public Boolean getOrDefault(Object key, Boolean defaultValue) {
        return Map.super.getOrDefault(key, defaultValue);
    }

    @Override
    @Deprecated
    default public Boolean putIfAbsent(Short key, Boolean value) {
        return Map.super.putIfAbsent(key, value);
    }

    @Override
    @Deprecated
    default public boolean remove(Object key, Object value) {
        return Map.super.remove(key, value);
    }

    @Override
    @Deprecated
    default public boolean replace(Short key, Boolean oldValue, Boolean newValue) {
        return Map.super.replace(key, oldValue, newValue);
    }

    @Override
    @Deprecated
    default public Boolean replace(Short key, Boolean value) {
        return Map.super.replace(key, value);
    }

    @Override
    @Deprecated
    default public Boolean computeIfAbsent(Short key, Function<? super Short, ? extends Boolean> mappingFunction) {
        return Map.super.computeIfAbsent(key, mappingFunction);
    }

    @Override
    @Deprecated
    default public Boolean computeIfPresent(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
        return Map.super.computeIfPresent(key, remappingFunction);
    }

    @Override
    @Deprecated
    default public Boolean compute(Short key, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
        return Map.super.compute(key, remappingFunction);
    }

    @Override
    @Deprecated
    default public Boolean merge(Short key, Boolean value, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        return Map.super.merge(key, value, remappingFunction);
    }

    public static interface Entry
    extends Map.Entry<Short, Boolean> {
        public short getShortKey();

        @Override
        @Deprecated
        default public Short getKey() {
            return this.getShortKey();
        }

        public boolean getBooleanValue();

        @Override
        public boolean setValue(boolean var1);

        @Override
        @Deprecated
        default public Boolean getValue() {
            return this.getBooleanValue();
        }

        @Override
        @Deprecated
        default public Boolean setValue(Boolean value) {
            return this.setValue((boolean)value);
        }
    }

    public static interface FastEntrySet
    extends ObjectSet<Entry> {
        public ObjectIterator<Entry> fastIterator();

        default public void fastForEach(Consumer<? super Entry> consumer) {
            this.forEach(consumer);
        }
    }
}

