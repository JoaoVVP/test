/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Hash;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntArrayList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntHash;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
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

public class Int2ObjectOpenCustomHashMap<V>
extends AbstractInt2ObjectMap<V>
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient int[] key;
    protected transient V[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected IntHash.Strategy strategy;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Int2ObjectMap.FastEntrySet<V> entries;
    protected transient IntSet keys;
    protected transient ObjectCollection<V> values;

    public Int2ObjectOpenCustomHashMap(int expected, float f, IntHash.Strategy strategy) {
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
        this.key = new int[this.n + 1];
        this.value = new Object[this.n + 1];
    }

    public Int2ObjectOpenCustomHashMap(int expected, IntHash.Strategy strategy) {
        this(expected, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(IntHash.Strategy strategy) {
        this(16, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(Map<? extends Integer, ? extends V> m, float f, IntHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }

    public Int2ObjectOpenCustomHashMap(Map<? extends Integer, ? extends V> m, IntHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(Int2ObjectMap<V> m, float f, IntHash.Strategy strategy) {
        this(m.size(), f, strategy);
        this.putAll(m);
    }

    public Int2ObjectOpenCustomHashMap(Int2ObjectMap<V> m, IntHash.Strategy strategy) {
        this(m, 0.75f, strategy);
    }

    public Int2ObjectOpenCustomHashMap(int[] k, V[] v, float f, IntHash.Strategy strategy) {
        this(k.length, f, strategy);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }

    public Int2ObjectOpenCustomHashMap(int[] k, V[] v, IntHash.Strategy strategy) {
        this(k, v, 0.75f, strategy);
    }

    public IntHash.Strategy strategy() {
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

    private V removeEntry(int pos) {
        V oldValue = this.value[pos];
        this.value[pos] = null;
        --this.size;
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    private V removeNullEntry() {
        this.containsNullKey = false;
        V oldValue = this.value[this.n];
        this.value[this.n] = null;
        --this.size;
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Integer, ? extends V> m) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(m.size());
        } else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }

    private int find(int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        int[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int curr = key[pos];
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

    private void insert(int pos, int k, V v) {
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
    public V put(int k, V v) {
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return (V)this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    protected final void shiftKeys(int pos) {
        int[] key = this.key;
        while (true) {
            int curr;
            int last = pos;
            pos = last + 1 & this.mask;
            while (true) {
                if ((curr = key[pos]) == 0) {
                    key[last] = 0;
                    this.value[last] = null;
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
    public V remove(int k) {
        if (this.strategy.equals(k, 0)) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return (V)this.defRetValue;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (!this.strategy.equals(k, curr));
        return this.removeEntry(pos);
    }

    @Override
    public V get(int k) {
        if (this.strategy.equals(k, 0)) {
            return (V)(this.containsNullKey ? this.value[this.n] : this.defRetValue);
        }
        int[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return (V)this.defRetValue;
        }
        if (this.strategy.equals(k, curr)) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return (V)this.defRetValue;
        } while (!this.strategy.equals(k, curr));
        return this.value[pos];
    }

    @Override
    public boolean containsKey(int k) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int curr = key[pos];
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
    public boolean containsValue(Object v) {
        V[] value = this.value;
        int[] key = this.key;
        if (this.containsNullKey && Objects.equals(value[this.n], v)) {
            return true;
        }
        int i = this.n;
        while (i-- != 0) {
            if (key[i] == 0 || !Objects.equals(value[i], v)) continue;
            return true;
        }
        return false;
    }

    @Override
    public V getOrDefault(int k, V defaultValue) {
        if (this.strategy.equals(k, 0)) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int curr = key[pos];
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
    public V putIfAbsent(int k, V v) {
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return (V)this.defRetValue;
    }

    @Override
    public boolean remove(int k, Object v) {
        if (this.strategy.equals(k, 0)) {
            if (this.containsNullKey && Objects.equals(v, this.value[this.n])) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        int[] key = this.key;
        int pos = HashCommon.mix(this.strategy.hashCode(k)) & this.mask;
        int curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (this.strategy.equals(k, curr) && Objects.equals(v, this.value[pos])) {
            this.removeEntry(pos);
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (!this.strategy.equals(k, curr) || !Objects.equals(v, this.value[pos]));
        this.removeEntry(pos);
        return true;
    }

    @Override
    public boolean replace(int k, V oldValue, V v) {
        int pos = this.find(k);
        if (pos < 0 || !Objects.equals(oldValue, this.value[pos])) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }

    @Override
    public V replace(int k, V v) {
        int pos = this.find(k);
        if (pos < 0) {
            return (V)this.defRetValue;
        }
        V oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override
    public V computeIfAbsent(int k, IntFunction<? extends V> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        V newValue = mappingFunction.apply(k);
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }

    @Override
    public V computeIfPresent(int k, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0) {
            return (V)this.defRetValue;
        }
        V newValue = remappingFunction.apply(k, this.value[pos]);
        if (newValue == null) {
            if (this.strategy.equals(k, 0)) {
                this.removeNullEntry();
            } else {
                this.removeEntry(pos);
            }
            return (V)this.defRetValue;
        }
        this.value[pos] = newValue;
        return this.value[pos];
    }

    @Override
    public V compute(int k, BiFunction<? super Integer, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        V newValue = remappingFunction.apply(k, pos >= 0 ? (Object)this.value[pos] : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (this.strategy.equals(k, 0)) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }
            }
            return (V)this.defRetValue;
        }
        V newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        this.value[pos] = newVal;
        return this.value[pos];
    }

    @Override
    public V merge(int k, V v, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0 || this.value[pos] == null) {
            if (v == null) {
                return (V)this.defRetValue;
            }
            this.insert(-pos - 1, k, v);
            return v;
        }
        V newValue = remappingFunction.apply(this.value[pos], v);
        if (newValue == null) {
            if (this.strategy.equals(k, 0)) {
                this.removeNullEntry();
            } else {
                this.removeEntry(pos);
            }
            return (V)this.defRetValue;
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
        Arrays.fill(this.key, 0);
        Arrays.fill(this.value, null);
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    public Int2ObjectMap.FastEntrySet<V> int2ObjectEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    @Override
    public IntSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public ObjectCollection<V> values() {
        if (this.values == null) {
            this.values = new AbstractObjectCollection<V>(){

                @Override
                public ObjectIterator<V> iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return Int2ObjectOpenCustomHashMap.this.size;
                }

                @Override
                public boolean contains(Object v) {
                    return Int2ObjectOpenCustomHashMap.this.containsValue(v);
                }

                @Override
                public void clear() {
                    Int2ObjectOpenCustomHashMap.this.clear();
                }

                @Override
                public void forEach(Consumer<? super V> consumer) {
                    if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                        consumer.accept(Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n]);
                    }
                    int pos = Int2ObjectOpenCustomHashMap.this.n;
                    while (pos-- != 0) {
                        if (Int2ObjectOpenCustomHashMap.this.key[pos] == 0) continue;
                        consumer.accept(Int2ObjectOpenCustomHashMap.this.value[pos]);
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
        int[] key = this.key;
        V[] value = this.value;
        int mask = newN - 1;
        int[] newKey = new int[newN + 1];
        Object[] newValue = new Object[newN + 1];
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

    public Int2ObjectOpenCustomHashMap<V> clone() {
        Int2ObjectOpenCustomHashMap c;
        try {
            c = (Int2ObjectOpenCustomHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = (int[])this.key.clone();
        c.value = (Object[])this.value.clone();
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
            if (this != this.value[i]) {
                t ^= this.value[i] == null ? 0 : this.value[i].hashCode();
            }
            h += t;
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n] == null ? 0 : this.value[this.n].hashCode();
        }
        return h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        int[] key = this.key;
        V[] value = this.value;
        MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            int e = i.nextEntry();
            s.writeInt(key[e]);
            s.writeObject(value[e]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new int[this.n + 1];
        int[] key = this.key;
        this.value = new Object[this.n + 1];
        Object[] value = this.value;
        int i = this.size;
        while (i-- != 0) {
            int pos;
            int k = s.readInt();
            Object v = s.readObject();
            if (this.strategy.equals(k, 0)) {
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
    implements ObjectIterator<V> {
        @Override
        public V next() {
            return Int2ObjectOpenCustomHashMap.this.value[this.nextEntry()];
        }
    }

    private final class KeySet
    extends AbstractIntSet {
        private KeySet() {
        }

        @Override
        public IntIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public void forEach(IntConsumer consumer) {
            if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(Int2ObjectOpenCustomHashMap.this.key[Int2ObjectOpenCustomHashMap.this.n]);
            }
            int pos = Int2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                int k = Int2ObjectOpenCustomHashMap.this.key[pos];
                if (k == 0) continue;
                consumer.accept(k);
            }
        }

        @Override
        public int size() {
            return Int2ObjectOpenCustomHashMap.this.size;
        }

        @Override
        public boolean contains(int k) {
            return Int2ObjectOpenCustomHashMap.this.containsKey(k);
        }

        @Override
        public boolean remove(int k) {
            int oldSize = Int2ObjectOpenCustomHashMap.this.size;
            Int2ObjectOpenCustomHashMap.this.remove(k);
            return Int2ObjectOpenCustomHashMap.this.size != oldSize;
        }

        @Override
        public void clear() {
            Int2ObjectOpenCustomHashMap.this.clear();
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements IntIterator {
        @Override
        public int nextInt() {
            return Int2ObjectOpenCustomHashMap.this.key[this.nextEntry()];
        }
    }

    private final class MapEntrySet
    extends AbstractObjectSet<Int2ObjectMap.Entry<V>>
    implements Int2ObjectMap.FastEntrySet<V> {
        private MapEntrySet() {
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> iterator() {
            return new EntryIterator();
        }

        @Override
        public ObjectIterator<Int2ObjectMap.Entry<V>> fastIterator() {
            return new FastEntryIterator();
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = (Integer)e.getKey();
            Object v = e.getValue();
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(k, 0)) {
                return Int2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n], v);
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            int pos = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
            int curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(k, curr)) {
                return Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], v);
            }
            do {
                if ((curr = key[pos = pos + 1 & Int2ObjectOpenCustomHashMap.this.mask]) != 0) continue;
                return false;
            } while (!Int2ObjectOpenCustomHashMap.this.strategy.equals(k, curr));
            return Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], v);
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                return false;
            }
            int k = (Integer)e.getKey();
            Object v = e.getValue();
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(k, 0)) {
                if (Int2ObjectOpenCustomHashMap.this.containsNullKey && Objects.equals(Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n], v)) {
                    Int2ObjectOpenCustomHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            int pos = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
            int curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (Int2ObjectOpenCustomHashMap.this.strategy.equals(curr, k)) {
                if (Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], v)) {
                    Int2ObjectOpenCustomHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
            do {
                if ((curr = key[pos = pos + 1 & Int2ObjectOpenCustomHashMap.this.mask]) != 0) continue;
                return false;
            } while (!Int2ObjectOpenCustomHashMap.this.strategy.equals(curr, k) || !Objects.equals(Int2ObjectOpenCustomHashMap.this.value[pos], v));
            Int2ObjectOpenCustomHashMap.this.removeEntry(pos);
            return true;
        }

        @Override
        public int size() {
            return Int2ObjectOpenCustomHashMap.this.size;
        }

        @Override
        public void clear() {
            Int2ObjectOpenCustomHashMap.this.clear();
        }

        @Override
        public void forEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                consumer.accept(new AbstractInt2ObjectMap.BasicEntry(Int2ObjectOpenCustomHashMap.this.key[Int2ObjectOpenCustomHashMap.this.n], Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n]));
            }
            int pos = Int2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenCustomHashMap.this.key[pos] == 0) continue;
                consumer.accept(new AbstractInt2ObjectMap.BasicEntry(Int2ObjectOpenCustomHashMap.this.key[pos], Int2ObjectOpenCustomHashMap.this.value[pos]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Int2ObjectMap.Entry<V>> consumer) {
            AbstractInt2ObjectMap.BasicEntry entry = new AbstractInt2ObjectMap.BasicEntry();
            if (Int2ObjectOpenCustomHashMap.this.containsNullKey) {
                entry.key = Int2ObjectOpenCustomHashMap.this.key[Int2ObjectOpenCustomHashMap.this.n];
                entry.value = Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n];
                consumer.accept(entry);
            }
            int pos = Int2ObjectOpenCustomHashMap.this.n;
            while (pos-- != 0) {
                if (Int2ObjectOpenCustomHashMap.this.key[pos] == 0) continue;
                entry.key = Int2ObjectOpenCustomHashMap.this.key[pos];
                entry.value = Int2ObjectOpenCustomHashMap.this.value[pos];
                consumer.accept(entry);
            }
        }
    }

    private class FastEntryIterator
    extends MapIterator
    implements ObjectIterator<Int2ObjectMap.Entry<V>> {
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
    implements ObjectIterator<Int2ObjectMap.Entry<V>> {
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
        IntArrayList wrapped;

        private MapIterator() {
            this.pos = Int2ObjectOpenCustomHashMap.this.n;
            this.last = -1;
            this.c = Int2ObjectOpenCustomHashMap.this.size;
            this.mustReturnNullKey = Int2ObjectOpenCustomHashMap.this.containsNullKey;
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
                this.last = Int2ObjectOpenCustomHashMap.this.n;
                return this.last;
            }
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            do {
                if (--this.pos >= 0) continue;
                this.last = Integer.MIN_VALUE;
                int k = this.wrapped.getInt(-this.pos - 1);
                int p = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(k)) & Int2ObjectOpenCustomHashMap.this.mask;
                while (!Int2ObjectOpenCustomHashMap.this.strategy.equals(k, key[p])) {
                    p = p + 1 & Int2ObjectOpenCustomHashMap.this.mask;
                }
                return p;
            } while (key[this.pos] == 0);
            this.last = this.pos;
            return this.last;
        }

        private void shiftKeys(int pos) {
            int[] key = Int2ObjectOpenCustomHashMap.this.key;
            while (true) {
                int curr;
                int last = pos;
                pos = last + 1 & Int2ObjectOpenCustomHashMap.this.mask;
                while (true) {
                    if ((curr = key[pos]) == 0) {
                        key[last] = 0;
                        Int2ObjectOpenCustomHashMap.this.value[last] = null;
                        return;
                    }
                    int slot = HashCommon.mix(Int2ObjectOpenCustomHashMap.this.strategy.hashCode(curr)) & Int2ObjectOpenCustomHashMap.this.mask;
                    if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                    pos = pos + 1 & Int2ObjectOpenCustomHashMap.this.mask;
                }
                if (pos < last) {
                    if (this.wrapped == null) {
                        this.wrapped = new IntArrayList(2);
                    }
                    this.wrapped.add(key[pos]);
                }
                key[last] = curr;
                Int2ObjectOpenCustomHashMap.this.value[last] = Int2ObjectOpenCustomHashMap.this.value[pos];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            }
            if (this.last == Int2ObjectOpenCustomHashMap.this.n) {
                Int2ObjectOpenCustomHashMap.this.containsNullKey = false;
                Int2ObjectOpenCustomHashMap.this.value[Int2ObjectOpenCustomHashMap.this.n] = null;
            } else if (this.pos >= 0) {
                this.shiftKeys(this.last);
            } else {
                Int2ObjectOpenCustomHashMap.this.remove(this.wrapped.getInt(-this.pos - 1));
                this.last = -1;
                return;
            }
            --Int2ObjectOpenCustomHashMap.this.size;
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
    implements Int2ObjectMap.Entry<V>,
    Map.Entry<Integer, V> {
        int index;

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override
        public int getIntKey() {
            return Int2ObjectOpenCustomHashMap.this.key[this.index];
        }

        @Override
        public V getValue() {
            return Int2ObjectOpenCustomHashMap.this.value[this.index];
        }

        @Override
        public V setValue(V v) {
            Object oldValue = Int2ObjectOpenCustomHashMap.this.value[this.index];
            Int2ObjectOpenCustomHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override
        @Deprecated
        public Integer getKey() {
            return Int2ObjectOpenCustomHashMap.this.key[this.index];
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            return Int2ObjectOpenCustomHashMap.this.strategy.equals(Int2ObjectOpenCustomHashMap.this.key[this.index], (Integer)e.getKey()) && Objects.equals(Int2ObjectOpenCustomHashMap.this.value[this.index], e.getValue());
        }

        @Override
        public int hashCode() {
            return Int2ObjectOpenCustomHashMap.this.strategy.hashCode(Int2ObjectOpenCustomHashMap.this.key[this.index]) ^ (Int2ObjectOpenCustomHashMap.this.value[this.index] == null ? 0 : Int2ObjectOpenCustomHashMap.this.value[this.index].hashCode());
        }

        public String toString() {
            return Int2ObjectOpenCustomHashMap.this.key[this.index] + "=>" + Int2ObjectOpenCustomHashMap.this.value[this.index];
        }
    }
}

