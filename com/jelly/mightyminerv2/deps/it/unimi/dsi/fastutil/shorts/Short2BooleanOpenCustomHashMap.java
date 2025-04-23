/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Hash;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.AbstractBooleanCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShort2BooleanMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShortSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2BooleanMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortArrayList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortHash;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntPredicate;

public class Short2BooleanOpenCustomHashMap
extends AbstractShort2BooleanMap
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient short[] key;
    protected transient boolean[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected ShortHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Short2BooleanMap.FastEntrySet entries;
    protected transient ShortSet keys;
    protected transient BooleanCollection values;

    public Short2BooleanOpenCustomHashMap(int expected, float f, ShortHash.Strategy strategy) {
        this.strategy = strategy;
        if (f <= 0.0f || f > 1.0f) {
            throw new IllegalArgumentException("Load factor must be greater than 0 and smaller than or equal to 1");
        }
        if (expected < 0) {
            throw new IllegalArgumentException("The expected number of elements must be nonnegative");
        }
        this.f = f;
        this.minN = this.n = HashCommon.arraySize(expected, f);
        this.mask = this.n - 1;
        this.maxFill = HashCommon.maxFill(this.n, f);
        this.key = new short[this.n + 1];
        this.value = new boolean[this.n + 1];
    }

    public Short2BooleanOpenCustomHashMap(int expected, ShortHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }

    public Short2BooleanOpenCustomHashMap(ShortHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }

    public Short2BooleanOpenCustomHashMap(Map<? extends Short, ? extends Boolean> m, float f, ShortHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }

    public Short2BooleanOpenCustomHashMap(Map<? extends Short, ? extends Boolean> m, ShortHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }

    public Short2BooleanOpenCustomHashMap(Short2BooleanMap m, float f, ShortHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }

    public Short2BooleanOpenCustomHashMap(Short2BooleanMap m, ShortHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }

    public Short2BooleanOpenCustomHashMap(short[] k, boolean[] v, float f, ShortHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }

    public Short2BooleanOpenCustomHashMap(short[] k, boolean[] v, ShortHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }

    public ShortHash.Strategy strategy() {
        return this.strategy;
    }

    private int realSize() {
        return this.containsNullKey ? this.size - 1 : this.size;
    }

    private void ensureCapacity(int capacity) {
        int needed = HashCommon.arraySize(capacity, this.f);
        if (needed > this.n) {
            this.rehash(needed);
        }
    }

    private void tryCapacity(long capacity) {
        int needed = (int)Math.min(0x40000000L, Math.max(2L, HashCommon.nextPowerOfTwo((long)Math.ceil((float)capacity / this.f))));
        if (needed > this.n) {
            this.rehash(needed);
        }
    }

    private boolean removeEntry(int pos) {
        boolean oldValue = this.value[pos];
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    private boolean removeNullEntry() {
        this.containsNullKey = false;
        boolean oldValue = this.value[this.n];
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Short, ? extends Boolean> m) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(m.size());
        } else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }

    private int find(short k) {
        if (this.strategy.equals(k, (short)0)) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        short[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return -(pos + 1);
        }
        if (this.strategy.equals(k, curr)) {
            return pos;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return -(pos + 1);
        } while (!this.strategy.equals(k, curr));
        return pos;
    }

    private void insert(int pos, short k, boolean v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override
    public boolean put(short k, boolean v) {
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    protected final void shiftKeys(int pos) {
        short[] key = this.key;
        while (true) {
            short curr;
            int last = pos;
            pos = last + 1 & this.mask;
            while (true) {
                if ((curr = key[pos]) == 0) {
                    key[last] = 0;
                    return;
                }
                int slot = HashCommon.mix(this.strategy.hashCode(curr)) & this.mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = pos + 1 & this.mask;
            }
            key[last] = curr;
            this.value[last] = this.value[pos];
        }
    }

    @Override
    public boolean remove(short k) {
        if (this.strategy.equals(k, (short)0)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (!this.strategy.equals(k, curr));
        return this.removeEntry(pos);
    }

    @Override
    public boolean get(short k) {
        if (this.strategy.equals(k, (short)0)) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override
    public boolean containsKey(short k) {
        if (this.strategy.equals(k, (short)0)) {
            return this.containsNullKey;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr)) {
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (!this.strategy.equals(k, curr));
        return true;
    }

    @Override
    public boolean containsValue(boolean v) {
        boolean[] value = this.value;
        short[] key = this.key;
        if (this.containsNullKey && value[this.n] == v) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] == 0 || value[i] != v) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean getOrDefault(short k, boolean defaultValue) {
        if (this.strategy.equals(k, (short)0)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return defaultValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return defaultValue;
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override
    public boolean putIfAbsent(short k, boolean v) {
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }

    @Override
    public boolean remove(short k, boolean v) {
        if (this.strategy.equals(k, (short)0)) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr) && v == this.value[pos]) {
            this.removeEntry(pos);
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (!this.strategy.equals(k, curr) || v != this.value[pos]);
        this.removeEntry(pos);
        return true;
    }

    @Override
    public boolean replace(short k, boolean oldValue, boolean v) {
        int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }

    @Override
    public boolean replace(short k, boolean v) {
        int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        boolean oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override
    public boolean computeIfAbsent(short k, IntPredicate mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        boolean newValue = mappingFunction.test(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }

    @Override
    public boolean computeIfAbsentNullable(short k, IntFunction<? extends Boolean> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        Boolean newValue = mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        boolean v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }

    @Override
    public boolean computeIfPresent(short k, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        Boolean newValue = remappingFunction.apply((Short)k, (Boolean)this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, (short)0)) {
                this.removeNullEntry();
            } else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        this.value[pos] = newValue;
        return this.value[pos];
    }

    @Override
    public boolean compute(short k, BiFunction<? super Short, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        Boolean newValue = remappingFunction.apply((Short)k, pos >= 0 ? Boolean.valueOf(this.value[pos]) : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, (short)0)) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        boolean newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        this.value[pos] = newVal;
        return this.value[pos];
    }

    @Override
    public boolean merge(short k, boolean v, BiFunction<? super Boolean, ? super Boolean, ? extends Boolean> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        Boolean newValue = remappingFunction.apply((Boolean)this.value[pos], (Boolean)v);
        if (newValue == null) {
            if (this.strategy.equals(k, (short)0)) {
                this.removeNullEntry();
            } else {
                this.removeEntry(pos);
            }
            return this.defRetValue;
        }
        this.value[pos] = newValue;
        return this.value[pos];
    }

    @Override
    public void clear() {
        if (this.size == 0) {
            return;
        }
        this.size = 0;
        this.containsNullKey = false;
        Arrays.fill(this.key, (short)0);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    public Short2BooleanMap.FastEntrySet short2BooleanEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    @Override
    public ShortSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public BooleanCollection values() {
        if (this.values == null) {
            this.values = new AbstractBooleanCollection(){

                @Override
                public BooleanIterator iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return Short2BooleanOpenCustomHashMap.this.size;
                }

                @Override
                public boolean contains(boolean v) {
                    return Short2BooleanOpenCustomHashMap.this.containsValue(v);
                }

                @Override
                public void clear() {
                    Short2BooleanOpenCustomHashMap.this.clear();
                }

                @Override
                public void forEach(BooleanConsumer consumer) {
                    if (Short2BooleanOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Short2BooleanOpenCustomHashMap.this.value[Short2BooleanOpenCustomHashMap.this.n]);
                    }
                    int pos = Short2BooleanOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Short2BooleanOpenCustomHashMap.this.key[pos] == 0) continue;
                        consumer.accept(Short2BooleanOpenCustomHashMap.this.value[pos]);
                    }
                }
            };
        }
        return this.values;
    }

    public boolean trim() {
        int l = HashCommon.arraySize(this.size, this.f);
        if (l >= this.n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }

    public boolean trim(int n) {
        int l = HashCommon.nextPowerOfTwo((int)Math.ceil((float)n / this.f));
        if (l >= n || this.size > HashCommon.maxFill(l, this.f)) {
            return true;
        }
        try {
            this.rehash(l);
        }
        catch (OutOfMemoryError cantDoIt) {
            return false;
        }
        return true;
    }

    protected void rehash(int newN) {
        short[] key = this.key;
        boolean[] value = this.value;
        int mask = newN - 1;
        short[] newKey = new short[newN + 1];
        boolean[] newValue = new boolean[newN + 1];
        int i = this.n;
        int j = this.realSize();
        while (j-- != 0) {
            while (key[--i] == 0) {
            }
            int pos = HashCommon.mix(this.strategy.hashCode(key[i])) & mask;
            if (newKey[pos] != 0) {
                while (newKey[pos = pos + 1 & mask] != 0) {
                }
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
        }
        newValue[newN] = value[this.n];
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }

    public Short2BooleanOpenCustomHashMap clone() {
        Short2BooleanOpenCustomHashMap c;
        try {
            c = (Short2BooleanOpenCustomHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = (short[])this.key.clone();
        c.value = (boolean[])this.value.clone();
        c.strategy = this.strategy;
        return c;
    }

    @Override
    public int hashCode() {
        int h = 0;
        int j = this.realSize();
        int i = 0;
        int t = 0;
        while (j-- != 0) {
            while (this.key[i] == 0) {
                ++i;
            }
            t = this.strategy.hashCode(this.key[i]);
            h += (t ^= this.value[i] ? 1231 : 1237);
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n] ? 1231 : 1237;
        }
        return h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        short[] key = this.key;
        boolean[] value = this.value;
        MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            int e = i.nextEntry();
            s.writeShort(key[e]);
            s.writeBoolean(value[e]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new short[this.n + 1];
        short[] key = this.key;
        this.value = new boolean[this.n + 1];
        boolean[] value = this.value;
        int i = this.size;
        while (i-- != 0) {
            int pos;
            short k = s.readShort();
            boolean v = s.readBoolean();
            if (this.strategy.equals(k, (short)0)) {
                pos = this.n;
                this.containsNullKey = true;
            } else {
                pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
                while (key[pos] != 0) {
                    pos = pos + 1 & this.mask;
                }
            }
            key[pos] = k;
            value[pos] = v;
        }
    }

    private void checkTable() {
    }

    private final class ValueIterator
    extends MapIterator
    implements BooleanIterator {
        @Override
        public boolean nextBoolean() {
            return Short2BooleanOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }

    private final class KeySet
    extends AbstractShortSet {
        private KeySet() {
        }

        @Override
        public ShortIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public void forEach(IntConsumer consumer) {
            if (Short2BooleanOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Short2BooleanOpenCustomHashMap.this.key[Short2BooleanOpenCustomHashMap.this.n]);
            }
            int pos = Short2BooleanOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                short k = Short2BooleanOpenCustomHashMap.this.key[pos];
                if (k == 0) continue;
                consumer.accept(k);
            }
        }

        @Override
        public int size() {
            return Short2BooleanOpenCustomHashMap.this.size;
        }

        @Override
        public boolean contains(short k) {
            return Short2BooleanOpenCustomHashMap.this.containsKey(k);
        }

        @Override
        public boolean remove(short k) {
            int oldSize = Short2BooleanOpenCustomHashMap.this.size;
            Short2BooleanOpenCustomHashMap.this.remove(k);
            return Short2BooleanOpenCustomHashMap.this.size != oldSize;
        }

        @Override
        public void clear() {
            Short2BooleanOpenCustomHashMap.this.clear();
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements ShortIterator {
        @Override
        public short nextShort() {
            return Short2BooleanOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }

    private final class MapEntrySet
    extends AbstractObjectSet<Short2BooleanMap.Entry>
    implements Short2BooleanMap.FastEntrySet {
        private MapEntrySet() {
        }

        @Override
        public ObjectIterator<Short2BooleanMap.Entry> iterator() {
            return new EntryIterator();
        }

        @Override
        public ObjectIterator<Short2BooleanMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            short k = (Short)e.getKey();
            boolean v = (Boolean)e.getValue();
            if (Short2BooleanOpenCustomHashMap.this.strategy.equals(k, (short)0)) {
                return Short2BooleanOpenCustomHashMap.this.containsNullKey && Short2BooleanOpenCustomHashMap.this.value[Short2BooleanOpenCustomHashMap.this.n] == v;
            }
            short[] key = Short2BooleanOpenCustomHashMap.this.key;
            int pos = HashCommon.mix(Short2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Short2BooleanOpenCustomHashMap.this.mask;
            short curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (Short2BooleanOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Short2BooleanOpenCustomHashMap.this.value[pos] == v;
            }
            do {
                if ((curr = key[pos = pos + 1 & Short2BooleanOpenCustomHashMap.this.mask]) != 0) continue;
                return false;
            } while (!Short2BooleanOpenCustomHashMap.this.strategy.equals(k, curr));
            return Short2BooleanOpenCustomHashMap.this.value[pos] == v;
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Short)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Boolean)) {
                return false;
            }
            short k = (Short)e.getKey();
            boolean v = (Boolean)e.getValue();
            if (Short2BooleanOpenCustomHashMap.this.strategy.equals(k, (short)0)) {
                if (Short2BooleanOpenCustomHashMap.this.containsNullKey && Short2BooleanOpenCustomHashMap.this.value[Short2BooleanOpenCustomHashMap.this.n] == v) {
                    Short2BooleanOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            short[] key = Short2BooleanOpenCustomHashMap.this.key;
            int pos = HashCommon.mix(Short2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Short2BooleanOpenCustomHashMap.this.mask;
            short curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (Short2BooleanOpenCustomHashMap.this.strategy.equals(curr, k)) {
                if (Short2BooleanOpenCustomHashMap.this.value[pos] == v) {
                    Short2BooleanOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
            do {
                if ((curr = key[pos = pos + 1 & Short2BooleanOpenCustomHashMap.this.mask]) != 0) continue;
                return false;
            } while (!Short2BooleanOpenCustomHashMap.this.strategy.equals(curr, k) || Short2BooleanOpenCustomHashMap.this.value[pos] != v);
            Short2BooleanOpenCustomHashMap.this.removeEntry(pos);
            return true;
        }

        @Override
        public int size() {
            return Short2BooleanOpenCustomHashMap.this.size;
        }

        @Override
        public void clear() {
            Short2BooleanOpenCustomHashMap.this.clear();
        }

        @Override
        public void forEach(Consumer<? super Short2BooleanMap.Entry> consumer) {
            if (Short2BooleanOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new AbstractShort2BooleanMap.BasicEntry(Short2BooleanOpenCustomHashMap.this.key[Short2BooleanOpenCustomHashMap.this.n], Short2BooleanOpenCustomHashMap.this.value[Short2BooleanOpenCustomHashMap.this.n]));
            }
            int pos = Short2BooleanOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Short2BooleanOpenCustomHashMap.this.key[pos] == 0) continue;
                consumer.accept(new AbstractShort2BooleanMap.BasicEntry(Short2BooleanOpenCustomHashMap.this.key[pos], Short2BooleanOpenCustomHashMap.this.value[pos]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Short2BooleanMap.Entry> consumer) {
            AbstractShort2BooleanMap.BasicEntry entry = new AbstractShort2BooleanMap.BasicEntry();
            if (Short2BooleanOpenCustomHashMap.this.containsNullKey) {
                entry.key = Short2BooleanOpenCustomHashMap.this.key[Short2BooleanOpenCustomHashMap.this.n];
                entry.value = Short2BooleanOpenCustomHashMap.this.value[Short2BooleanOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Short2BooleanOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Short2BooleanOpenCustomHashMap.this.key[pos] == 0) continue;
                entry.key = Short2BooleanOpenCustomHashMap.this.key[pos];
                entry.value = Short2BooleanOpenCustomHashMap.this.value[pos];
                consumer.accept(entry);
            }
        }
    }

    private class FastEntryIterator
    extends MapIterator
    implements ObjectIterator<Short2BooleanMap.Entry> {
        private final MapEntry entry;

        private FastEntryIterator() {
            this.entry = new MapEntry();
        }

        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }
    }

    private class EntryIterator
    extends MapIterator
    implements ObjectIterator<Short2BooleanMap.Entry> {
        private MapEntry entry;

        private EntryIterator() {
        }

        @Override
        public MapEntry next() {
            this.entry = new MapEntry(this.nextEntry());
            return this.entry;
        }

        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    private class MapIterator {
        int pos;
        int last;
        int c;
        boolean mustReturnNullKey;
        ShortArrayList wrapped;

        private MapIterator() {
            this.pos = Short2BooleanOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Short2BooleanOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Short2BooleanOpenCustomHashMap.this.containsNullKey;
        }

        public boolean hasNext() {
            return this.c != 0;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            --this.c;
            if (this.mustReturnNullKey) {
                this.mustReturnNullKey = false;
                this.last = Short2BooleanOpenCustomHashMap.this.n;
                return this.last;
            }
            short[] key = Short2BooleanOpenCustomHashMap.this.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                short k = this.wrapped.getShort(-this.pos - 1);
                int p = HashCommon.mix(Short2BooleanOpenCustomHashMap.this.strategy.hashCode(k)) & Short2BooleanOpenCustomHashMap.this.mask;
                while (!Short2BooleanOpenCustomHashMap.this.strategy.equals(k, key[p])) {
                    p = p + 1 & Short2BooleanOpenCustomHashMap.this.mask;
                }
                return p;
            } while (key[this.pos] == 0);
            this.last = this.pos;
            return this.last;
        }

        private void shiftKeys(int pos) {
            short[] key = Short2BooleanOpenCustomHashMap.this.key;
            while (true) {
                short curr;
                int last = pos;
                pos = last + 1 & Short2BooleanOpenCustomHashMap.this.mask;
                while (true) {
                    if ((curr = key[pos]) == 0) {
                        key[last] = 0;
                        return;
                    }
                    int slot = HashCommon.mix(Short2BooleanOpenCustomHashMap.this.strategy.hashCode(curr)) & Short2BooleanOpenCustomHashMap.this.mask;
                    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                    pos = pos + 1 & Short2BooleanOpenCustomHashMap.this.mask;
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new ShortArrayList(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
                Short2BooleanOpenCustomHashMap.this.value[last] = Short2BooleanOpenCustomHashMap.this.value[pos];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Short2BooleanOpenCustomHashMap.this.n) {
                Short2BooleanOpenCustomHashMap.this.containsNullKey = false;
            } else if (this.pos >= 0) {
                this.shiftKeys(this.last);
            } else {
                Short2BooleanOpenCustomHashMap.this.remove(this.wrapped.getShort(-this.pos - 1));
                this.last = -1;
                return;
            }
            --Short2BooleanOpenCustomHashMap.this.size;
            this.last = -1;
        }

        public int skip(int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }
    }

    final class MapEntry
    implements Short2BooleanMap.Entry,
    Map.Entry<Short, Boolean> {
        int index;

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override
        public short getShortKey() {
            return Short2BooleanOpenCustomHashMap.this.key[this.index];
        }

        @Override
        public boolean getBooleanValue() {
            return Short2BooleanOpenCustomHashMap.this.value[this.index];
        }

        @Override
        public boolean setValue(boolean v) {
            boolean oldValue = Short2BooleanOpenCustomHashMap.this.value[this.index];
            Short2BooleanOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override
        @Deprecated
        public Short getKey() {
            return Short2BooleanOpenCustomHashMap.this.key[this.index];
        }

        @Override
        @Deprecated
        public Boolean getValue() {
            return Short2BooleanOpenCustomHashMap.this.value[this.index];
        }

        @Override
        @Deprecated
        public Boolean setValue(Boolean v) {
            return this.setValue((boolean)v);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            return Short2BooleanOpenCustomHashMap.this.strategy.equals(Short2BooleanOpenCustomHashMap.this.key[this.index], (Short)e.getKey()) && Short2BooleanOpenCustomHashMap.this.value[this.index] == (Boolean)e.getValue();
        }

        @Override
        public int hashCode() {
            return Short2BooleanOpenCustomHashMap.this.strategy.hashCode(Short2BooleanOpenCustomHashMap.this.key[this.index]) ^ (Short2BooleanOpenCustomHashMap.this.value[this.index] ? 1231 : 1237);
        }

        public String toString() {
            return Short2BooleanOpenCustomHashMap.this.key[this.index] + "=>" + Short2BooleanOpenCustomHashMap.this.value[this.index];
        }
    }
}

