/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Hash;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShort2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShort2ByteSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShortSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ByteSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import java.util.function.IntFunction;
import java.util.function.IntUnaryOperator;

public class Short2ByteLinkedOpenHashMap
extends AbstractShort2ByteSortedMap
implements Serializable,
Cloneable,
Hash {
    private static final long serialVersionUID = 0L;
    private static final boolean ASSERTS = false;
    protected transient short[] key;
    protected transient byte[] value;
    protected transient int mask;
    protected transient boolean containsNullKey;
    protected transient int first = -1;
    protected transient int last = -1;
    protected transient long[] link;
    protected transient int n;
    protected transient int maxFill;
    protected final transient int minN;
    protected int size;
    protected final float f;
    protected transient Short2ByteSortedMap.FastSortedEntrySet entries;
    protected transient ShortSortedSet keys;
    protected transient ByteCollection values;

    public Short2ByteLinkedOpenHashMap(int expected, float f) {
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
        this.value = new byte[this.n + 1];
        this.link = new long[this.n + 1];
    }

    public Short2ByteLinkedOpenHashMap(int expected) {
        this(expected, 0.75f);
    }

    public Short2ByteLinkedOpenHashMap() {
        this(16, 0.75f);
    }

    public Short2ByteLinkedOpenHashMap(Map<? extends Short, ? extends Byte> m, float f) {
        this(m.size(), f);
        this.putAll(m);
    }

    public Short2ByteLinkedOpenHashMap(Map<? extends Short, ? extends Byte> m) {
        this(m, 0.75f);
    }

    public Short2ByteLinkedOpenHashMap(Short2ByteMap m, float f) {
        this(m.size(), f);
        this.putAll(m);
    }

    public Short2ByteLinkedOpenHashMap(Short2ByteMap m) {
        this(m, 0.75f);
    }

    public Short2ByteLinkedOpenHashMap(short[] k, byte[] v, float f) {
        this(k.length, f);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }

    public Short2ByteLinkedOpenHashMap(short[] k, byte[] v) {
        this(k, v, 0.75f);
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

    private byte removeEntry(int pos) {
        byte oldValue = this.value[pos];
        --this.size;
        this.fixPointers(pos);
        this.shiftKeys(pos);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    private byte removeNullEntry() {
        this.containsNullKey = false;
        byte oldValue = this.value[this.n];
        --this.size;
        this.fixPointers(this.n);
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return oldValue;
    }

    @Override
    public void putAll(Map<? extends Short, ? extends Byte> m) {
        if ((double)this.f <= 0.5) {
            this.ensureCapacity(m.size());
        } else {
            this.tryCapacity(this.size() + m.size());
        }
        super.putAll(m);
    }

    private int find(short k) {
        if (k == 0) {
            return this.containsNullKey ? this.n : -(this.n + 1);
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return -(pos + 1);
        }
        if (k == curr) {
            return pos;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return -(pos + 1);
        } while (k != curr);
        return pos;
    }

    private void insert(int pos, short k, byte v) {
        if (pos == this.n) {
            this.containsNullKey = true;
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n = this.last;
            this.link[n] = this.link[n] ^ (this.link[this.last] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[pos] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
    }

    @Override
    public byte put(short k, byte v) {
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return this.defRetValue;
        }
        byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    private byte addToValue(int pos, byte incr) {
        byte oldValue = this.value[pos];
        this.value[pos] = (byte)(oldValue + incr);
        return oldValue;
    }

    public byte addTo(short k, byte incr) {
        int pos;
        if (k == 0) {
            if (this.containsNullKey) {
                return this.addToValue(this.n, incr);
            }
            pos = this.n;
            this.containsNullKey = true;
        } else {
            short[] key = this.key;
            pos = HashCommon.mix(k) & this.mask;
            short curr = key[pos];
            if (curr != 0) {
                if (curr == k) {
                    return this.addToValue(pos, incr);
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (curr != k) continue;
                    return this.addToValue(pos, incr);
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = (byte)(this.defRetValue + incr);
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n = this.last;
            this.link[n] = this.link[n] ^ (this.link[this.last] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[pos] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size + 1, this.f));
        }
        return this.defRetValue;
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
                int slot = HashCommon.mix(curr) & this.mask;
                if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                pos = pos + 1 & this.mask;
            }
            key[last] = curr;
            this.value[last] = this.value[pos];
            this.fixPointers(pos, last);
        }
    }

    @Override
    public byte remove(short k) {
        if (k == 0) {
            if (this.containsNullKey) {
                return this.removeNullEntry();
            }
            return this.defRetValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.removeEntry(pos);
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (k != curr);
        return this.removeEntry(pos);
    }

    private byte setValue(int pos, byte v) {
        byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    public byte removeFirstByte() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int pos = this.first;
        this.first = (int)this.link[pos];
        if (0 <= this.first) {
            int n = this.first;
            this.link[n] = this.link[n] | 0xFFFFFFFF00000000L;
        }
        --this.size;
        byte v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        } else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }

    public byte removeLastByte() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        int pos = this.last;
        this.last = (int)(this.link[pos] >>> 32);
        if (0 <= this.last) {
            int n = this.last;
            this.link[n] = this.link[n] | 0xFFFFFFFFL;
        }
        --this.size;
        byte v = this.value[pos];
        if (pos == this.n) {
            this.containsNullKey = false;
        } else {
            this.shiftKeys(pos);
        }
        if (this.n > this.minN && this.size < this.maxFill / 4 && this.n > 16) {
            this.rehash(this.n / 2);
        }
        return v;
    }

    private void moveIndexToFirst(int i) {
        if (this.size == 1 || this.first == i) {
            return;
        }
        if (this.last == i) {
            int n = this.last = (int)(this.link[i] >>> 32);
            this.link[n] = this.link[n] | 0xFFFFFFFFL;
        } else {
            long linki = this.link[i];
            int prev = (int)(linki >>> 32);
            int next = (int)linki;
            int n = prev;
            this.link[n] = this.link[n] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n2 = next;
            this.link[n2] = this.link[n2] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n = this.first;
        this.link[n] = this.link[n] ^ (this.link[this.first] ^ ((long)i & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[i] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
        this.first = i;
    }

    private void moveIndexToLast(int i) {
        if (this.size == 1 || this.last == i) {
            return;
        }
        if (this.first == i) {
            int n = this.first = (int)this.link[i];
            this.link[n] = this.link[n] | 0xFFFFFFFF00000000L;
        } else {
            long linki = this.link[i];
            int prev = (int)(linki >>> 32);
            int next = (int)linki;
            int n = prev;
            this.link[n] = this.link[n] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            int n2 = next;
            this.link[n2] = this.link[n2] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
        }
        int n = this.last;
        this.link[n] = this.link[n] ^ (this.link[this.last] ^ (long)i & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        this.link[i] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
        this.last = i;
    }

    public byte getAndMoveToFirst(short k) {
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return this.defRetValue;
        }
        if (k == curr) {
            this.moveIndexToFirst(pos);
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (k != curr);
        this.moveIndexToFirst(pos);
        return this.value[pos];
    }

    public byte getAndMoveToLast(short k) {
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.value[this.n];
            }
            return this.defRetValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return this.defRetValue;
        }
        if (k == curr) {
            this.moveIndexToLast(pos);
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (k != curr);
        this.moveIndexToLast(pos);
        return this.value[pos];
    }

    public byte putAndMoveToFirst(short k, byte v) {
        int pos;
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToFirst(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            short[] key = this.key;
            pos = HashCommon.mix(k) & this.mask;
            short curr = key[pos];
            if (curr != 0) {
                if (curr == k) {
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (curr != k) continue;
                    this.moveIndexToFirst(pos);
                    return this.setValue(pos, v);
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n = this.first;
            this.link[n] = this.link[n] ^ (this.link[this.first] ^ ((long)pos & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[pos] = 0xFFFFFFFF00000000L | (long)this.first & 0xFFFFFFFFL;
            this.first = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }

    public byte putAndMoveToLast(short k, byte v) {
        int pos;
        if (k == 0) {
            if (this.containsNullKey) {
                this.moveIndexToLast(this.n);
                return this.setValue(this.n, v);
            }
            this.containsNullKey = true;
            pos = this.n;
        } else {
            short[] key = this.key;
            pos = HashCommon.mix(k) & this.mask;
            short curr = key[pos];
            if (curr != 0) {
                if (curr == k) {
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
                while ((curr = key[pos = pos + 1 & this.mask]) != 0) {
                    if (curr != k) continue;
                    this.moveIndexToLast(pos);
                    return this.setValue(pos, v);
                }
            }
        }
        this.key[pos] = k;
        this.value[pos] = v;
        if (this.size == 0) {
            this.first = this.last = pos;
            this.link[pos] = -1L;
        } else {
            int n = this.last;
            this.link[n] = this.link[n] ^ (this.link[this.last] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[pos] = ((long)this.last & 0xFFFFFFFFL) << 32 | 0xFFFFFFFFL;
            this.last = pos;
        }
        if (this.size++ >= this.maxFill) {
            this.rehash(HashCommon.arraySize(this.size, this.f));
        }
        return this.defRetValue;
    }

    @Override
    public byte get(short k) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : this.defRetValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return this.defRetValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return this.defRetValue;
        } while (k != curr);
        return this.value[pos];
    }

    @Override
    public boolean containsKey(short k) {
        if (k == 0) {
            return this.containsNullKey;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (k == curr) {
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (k != curr);
        return true;
    }

    @Override
    public boolean containsValue(byte v) {
        byte[] value = this.value;
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
    public byte getOrDefault(short k, byte defaultValue) {
        if (k == 0) {
            return this.containsNullKey ? this.value[this.n] : defaultValue;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return defaultValue;
        }
        if (k == curr) {
            return this.value[pos];
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return defaultValue;
        } while (k != curr);
        return this.value[pos];
    }

    @Override
    public byte putIfAbsent(short k, byte v) {
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        this.insert(-pos - 1, k, v);
        return this.defRetValue;
    }

    @Override
    public boolean remove(short k, byte v) {
        if (k == 0) {
            if (this.containsNullKey && v == this.value[this.n]) {
                this.removeNullEntry();
                return true;
            }
            return false;
        }
        short[] key = this.key;
        int pos = HashCommon.mix(k) & this.mask;
        short curr = key[pos];
        if (curr == 0) {
            return false;
        }
        if (k == curr && v == this.value[pos]) {
            this.removeEntry(pos);
            return true;
        }
        do {
            if ((curr = key[pos = pos + 1 & this.mask]) != 0) continue;
            return false;
        } while (k != curr || v != this.value[pos]);
        this.removeEntry(pos);
        return true;
    }

    @Override
    public boolean replace(short k, byte oldValue, byte v) {
        int pos = this.find(k);
        if (pos < 0 || oldValue != this.value[pos]) {
            return false;
        }
        this.value[pos] = v;
        return true;
    }

    @Override
    public byte replace(short k, byte v) {
        int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        byte oldValue = this.value[pos];
        this.value[pos] = v;
        return oldValue;
    }

    @Override
    public byte computeIfAbsent(short k, IntUnaryOperator mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        byte newValue = SafeMath.safeIntToByte(mappingFunction.applyAsInt(k));
        this.insert(-pos - 1, k, newValue);
        return newValue;
    }

    @Override
    public byte computeIfAbsentNullable(short k, IntFunction<? extends Byte> mappingFunction) {
        Objects.requireNonNull(mappingFunction);
        int pos = this.find(k);
        if (pos >= 0) {
            return this.value[pos];
        }
        Byte newValue = mappingFunction.apply(k);
        if (newValue == null) {
            return this.defRetValue;
        }
        byte v = newValue;
        this.insert(-pos - 1, k, v);
        return v;
    }

    @Override
    public byte computeIfPresent(short k, BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0) {
            return this.defRetValue;
        }
        Byte newValue = remappingFunction.apply((Short)k, (Byte)this.value[pos]);
        if (newValue == null) {
            if (k == 0) {
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
    public byte compute(short k, BiFunction<? super Short, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        Byte newValue = remappingFunction.apply((Short)k, pos >= 0 ? Byte.valueOf(this.value[pos]) : null);
        if (newValue == null) {
            if (pos >= 0) {
                if (k == 0) {
                    this.removeNullEntry();
                } else {
                    this.removeEntry(pos);
                }
            }
            return this.defRetValue;
        }
        byte newVal = newValue;
        if (pos < 0) {
            this.insert(-pos - 1, k, newVal);
            return newVal;
        }
        this.value[pos] = newVal;
        return this.value[pos];
    }

    @Override
    public byte merge(short k, byte v, BiFunction<? super Byte, ? super Byte, ? extends Byte> remappingFunction) {
        Objects.requireNonNull(remappingFunction);
        int pos = this.find(k);
        if (pos < 0) {
            this.insert(-pos - 1, k, v);
            return v;
        }
        Byte newValue = remappingFunction.apply((Byte)this.value[pos], (Byte)v);
        if (newValue == null) {
            if (k == 0) {
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
        this.last = -1;
        this.first = -1;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    protected void fixPointers(int i) {
        if (this.size == 0) {
            this.last = -1;
            this.first = -1;
            return;
        }
        if (this.first == i) {
            this.first = (int)this.link[i];
            if (0 <= this.first) {
                int n = this.first;
                this.link[n] = this.link[n] | 0xFFFFFFFF00000000L;
            }
            return;
        }
        if (this.last == i) {
            this.last = (int)(this.link[i] >>> 32);
            if (0 <= this.last) {
                int n = this.last;
                this.link[n] = this.link[n] | 0xFFFFFFFFL;
            }
            return;
        }
        long linki = this.link[i];
        int prev = (int)(linki >>> 32);
        int next = (int)linki;
        int n = prev;
        this.link[n] = this.link[n] ^ (this.link[prev] ^ linki & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n2 = next;
        this.link[n2] = this.link[n2] ^ (this.link[next] ^ linki & 0xFFFFFFFF00000000L) & 0xFFFFFFFF00000000L;
    }

    protected void fixPointers(int s, int d) {
        if (this.size == 1) {
            this.first = this.last = d;
            this.link[d] = -1L;
            return;
        }
        if (this.first == s) {
            this.first = d;
            int n = (int)this.link[s];
            this.link[n] = this.link[n] ^ (this.link[(int)this.link[s]] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            this.link[d] = this.link[s];
            return;
        }
        if (this.last == s) {
            this.last = d;
            int n = (int)(this.link[s] >>> 32);
            this.link[n] = this.link[n] ^ (this.link[(int)(this.link[s] >>> 32)] ^ (long)d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            this.link[d] = this.link[s];
            return;
        }
        long links = this.link[s];
        int prev = (int)(links >>> 32);
        int next = (int)links;
        int n = prev;
        this.link[n] = this.link[n] ^ (this.link[prev] ^ (long)d & 0xFFFFFFFFL) & 0xFFFFFFFFL;
        int n2 = next;
        this.link[n2] = this.link[n2] ^ (this.link[next] ^ ((long)d & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
        this.link[d] = links;
    }

    @Override
    public short firstShortKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.first];
    }

    @Override
    public short lastShortKey() {
        if (this.size == 0) {
            throw new NoSuchElementException();
        }
        return this.key[this.last];
    }

    @Override
    public Short2ByteSortedMap tailMap(short from) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Short2ByteSortedMap headMap(short to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Short2ByteSortedMap subMap(short from, short to) {
        throw new UnsupportedOperationException();
    }

    @Override
    public ShortComparator comparator() {
        return null;
    }

    @Override
    public Short2ByteSortedMap.FastSortedEntrySet short2ByteEntrySet() {
        if (this.entries == null) {
            this.entries = new MapEntrySet();
        }
        return this.entries;
    }

    @Override
    public ShortSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public ByteCollection values() {
        if (this.values == null) {
            this.values = new AbstractByteCollection(){

                @Override
                public ByteIterator iterator() {
                    return new ValueIterator();
                }

                @Override
                public int size() {
                    return Short2ByteLinkedOpenHashMap.this.size;
                }

                @Override
                public boolean contains(byte v) {
                    return Short2ByteLinkedOpenHashMap.this.containsValue(v);
                }

                @Override
                public void clear() {
                    Short2ByteLinkedOpenHashMap.this.clear();
                }

                @Override
                public void forEach(IntConsumer consumer) {
                    if (Short2ByteLinkedOpenHashMap.this.containsNullKey) {
                        consumer.accept(Short2ByteLinkedOpenHashMap.this.value[Short2ByteLinkedOpenHashMap.this.n]);
                    }
                    int pos = Short2ByteLinkedOpenHashMap.this.n;
                    while (pos-- != 0) {
                        if (Short2ByteLinkedOpenHashMap.this.key[pos] == 0) continue;
                        consumer.accept(Short2ByteLinkedOpenHashMap.this.value[pos]);
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
        byte[] value = this.value;
        int mask = newN - 1;
        short[] newKey = new short[newN + 1];
        byte[] newValue = new byte[newN + 1];
        int i = this.first;
        int prev = -1;
        int newPrev = -1;
        long[] link = this.link;
        long[] newLink = new long[newN + 1];
        this.first = -1;
        int j = this.size;
        while (j-- != 0) {
            int pos;
            if (key[i] == 0) {
                pos = newN;
            } else {
                pos = HashCommon.mix(key[i]) & mask;
                while (newKey[pos] != 0) {
                    pos = pos + 1 & mask;
                }
            }
            newKey[pos] = key[i];
            newValue[pos] = value[i];
            if (prev != -1) {
                int n = newPrev;
                newLink[n] = newLink[n] ^ (newLink[newPrev] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n2 = pos;
                newLink[n2] = newLink[n2] ^ (newLink[pos] ^ ((long)newPrev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                newPrev = pos;
            } else {
                newPrev = this.first = pos;
                newLink[pos] = -1L;
            }
            int t = i;
            i = (int)link[i];
            prev = t;
        }
        this.link = newLink;
        this.last = newPrev;
        if (newPrev != -1) {
            int n = newPrev;
            newLink[n] = newLink[n] | 0xFFFFFFFFL;
        }
        this.n = newN;
        this.mask = mask;
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.key = newKey;
        this.value = newValue;
    }

    public Short2ByteLinkedOpenHashMap clone() {
        Short2ByteLinkedOpenHashMap c;
        try {
            c = (Short2ByteLinkedOpenHashMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.containsNullKey = this.containsNullKey;
        c.key = (short[])this.key.clone();
        c.value = (byte[])this.value.clone();
        c.link = (long[])this.link.clone();
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
            t = this.key[i];
            h += (t ^= this.value[i]);
            ++i;
        }
        if (this.containsNullKey) {
            h += this.value[this.n];
        }
        return h;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        short[] key = this.key;
        byte[] value = this.value;
        MapIterator i = new MapIterator();
        s.defaultWriteObject();
        int j = this.size;
        while (j-- != 0) {
            int e = i.nextEntry();
            s.writeShort(key[e]);
            s.writeByte(value[e]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.n = HashCommon.arraySize(this.size, this.f);
        this.maxFill = HashCommon.maxFill(this.n, this.f);
        this.mask = this.n - 1;
        this.key = new short[this.n + 1];
        short[] key = this.key;
        this.value = new byte[this.n + 1];
        byte[] value = this.value;
        this.link = new long[this.n + 1];
        long[] link = this.link;
        int prev = -1;
        this.last = -1;
        this.first = -1;
        int i = this.size;
        while (i-- != 0) {
            int pos;
            short k = s.readShort();
            byte v = s.readByte();
            if (k == 0) {
                pos = this.n;
                this.containsNullKey = true;
            } else {
                pos = HashCommon.mix(k) & this.mask;
                while (key[pos] != 0) {
                    pos = pos + 1 & this.mask;
                }
            }
            key[pos] = k;
            value[pos] = v;
            if (this.first != -1) {
                int n = prev;
                link[n] = link[n] ^ (link[prev] ^ (long)pos & 0xFFFFFFFFL) & 0xFFFFFFFFL;
                int n2 = pos;
                link[n2] = link[n2] ^ (link[pos] ^ ((long)prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
                prev = pos;
                continue;
            }
            prev = this.first = pos;
            int n = pos;
            link[n] = link[n] | 0xFFFFFFFF00000000L;
        }
        this.last = prev;
        if (prev != -1) {
            int n = prev;
            link[n] = link[n] | 0xFFFFFFFFL;
        }
    }

    private void checkTable() {
    }

    private final class ValueIterator
    extends MapIterator
    implements ByteListIterator {
        @Override
        public byte previousByte() {
            return Short2ByteLinkedOpenHashMap.this.value[this.previousEntry()];
        }

        @Override
        public byte nextByte() {
            return Short2ByteLinkedOpenHashMap.this.value[this.nextEntry()];
        }
    }

    private final class KeySet
    extends AbstractShortSortedSet {
        private KeySet() {
        }

        @Override
        public ShortListIterator iterator(short from) {
            return new KeyIterator(from);
        }

        @Override
        public ShortListIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public void forEach(IntConsumer consumer) {
            if (Short2ByteLinkedOpenHashMap.this.containsNullKey) {
                consumer.accept(Short2ByteLinkedOpenHashMap.this.key[Short2ByteLinkedOpenHashMap.this.n]);
            }
            int pos = Short2ByteLinkedOpenHashMap.this.n;
            while (pos-- != 0) {
                short k = Short2ByteLinkedOpenHashMap.this.key[pos];
                if (k == 0) continue;
                consumer.accept(k);
            }
        }

        @Override
        public int size() {
            return Short2ByteLinkedOpenHashMap.this.size;
        }

        @Override
        public boolean contains(short k) {
            return Short2ByteLinkedOpenHashMap.this.containsKey(k);
        }

        @Override
        public boolean remove(short k) {
            int oldSize = Short2ByteLinkedOpenHashMap.this.size;
            Short2ByteLinkedOpenHashMap.this.remove(k);
            return Short2ByteLinkedOpenHashMap.this.size != oldSize;
        }

        @Override
        public void clear() {
            Short2ByteLinkedOpenHashMap.this.clear();
        }

        @Override
        public short firstShort() {
            if (Short2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Short2ByteLinkedOpenHashMap.this.key[Short2ByteLinkedOpenHashMap.this.first];
        }

        @Override
        public short lastShort() {
            if (Short2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return Short2ByteLinkedOpenHashMap.this.key[Short2ByteLinkedOpenHashMap.this.last];
        }

        @Override
        public ShortComparator comparator() {
            return null;
        }

        @Override
        public ShortSortedSet tailSet(short from) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ShortSortedSet headSet(short to) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ShortSortedSet subSet(short from, short to) {
            throw new UnsupportedOperationException();
        }
    }

    private final class KeyIterator
    extends MapIterator
    implements ShortListIterator {
        public KeyIterator(short k) {
            super(k);
        }

        @Override
        public short previousShort() {
            return Short2ByteLinkedOpenHashMap.this.key[this.previousEntry()];
        }

        public KeyIterator() {
        }

        @Override
        public short nextShort() {
            return Short2ByteLinkedOpenHashMap.this.key[this.nextEntry()];
        }
    }

    private final class MapEntrySet
    extends AbstractObjectSortedSet<Short2ByteMap.Entry>
    implements Short2ByteSortedMap.FastSortedEntrySet {
        private MapEntrySet() {
        }

        @Override
        public ObjectBidirectionalIterator<Short2ByteMap.Entry> iterator() {
            return new EntryIterator();
        }

        @Override
        public Comparator<? super Short2ByteMap.Entry> comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Short2ByteMap.Entry> subSet(Short2ByteMap.Entry fromElement, Short2ByteMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSortedSet<Short2ByteMap.Entry> headSet(Short2ByteMap.Entry toElement) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSortedSet<Short2ByteMap.Entry> tailSet(Short2ByteMap.Entry fromElement) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Short2ByteMap.Entry first() {
            if (Short2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Short2ByteLinkedOpenHashMap.this.first);
        }

        @Override
        public Short2ByteMap.Entry last() {
            if (Short2ByteLinkedOpenHashMap.this.size == 0) {
                throw new NoSuchElementException();
            }
            return new MapEntry(Short2ByteLinkedOpenHashMap.this.last);
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
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            short k = (Short)e.getKey();
            byte v = (Byte)e.getValue();
            if (k == 0) {
                return Short2ByteLinkedOpenHashMap.this.containsNullKey && Short2ByteLinkedOpenHashMap.this.value[Short2ByteLinkedOpenHashMap.this.n] == v;
            }
            short[] key = Short2ByteLinkedOpenHashMap.this.key;
            int pos = HashCommon.mix(k) & Short2ByteLinkedOpenHashMap.this.mask;
            short curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (k == curr) {
                return Short2ByteLinkedOpenHashMap.this.value[pos] == v;
            }
            do {
                if ((curr = key[pos = pos + 1 & Short2ByteLinkedOpenHashMap.this.mask]) != 0) continue;
                return false;
            } while (k != curr);
            return Short2ByteLinkedOpenHashMap.this.value[pos] == v;
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
            if (e.getValue() == null || !(e.getValue() instanceof Byte)) {
                return false;
            }
            short k = (Short)e.getKey();
            byte v = (Byte)e.getValue();
            if (k == 0) {
                if (Short2ByteLinkedOpenHashMap.this.containsNullKey && Short2ByteLinkedOpenHashMap.this.value[Short2ByteLinkedOpenHashMap.this.n] == v) {
                    Short2ByteLinkedOpenHashMap.this.removeNullEntry();
                    return true;
                }
                return false;
            }
            short[] key = Short2ByteLinkedOpenHashMap.this.key;
            int pos = HashCommon.mix(k) & Short2ByteLinkedOpenHashMap.this.mask;
            short curr = key[pos];
            if (curr == 0) {
                return false;
            }
            if (curr == k) {
                if (Short2ByteLinkedOpenHashMap.this.value[pos] == v) {
                    Short2ByteLinkedOpenHashMap.this.removeEntry(pos);
                    return true;
                }
                return false;
            }
            do {
                if ((curr = key[pos = pos + 1 & Short2ByteLinkedOpenHashMap.this.mask]) != 0) continue;
                return false;
            } while (curr != k || Short2ByteLinkedOpenHashMap.this.value[pos] != v);
            Short2ByteLinkedOpenHashMap.this.removeEntry(pos);
            return true;
        }

        @Override
        public int size() {
            return Short2ByteLinkedOpenHashMap.this.size;
        }

        @Override
        public void clear() {
            Short2ByteLinkedOpenHashMap.this.clear();
        }

        @Override
        public ObjectListIterator<Short2ByteMap.Entry> iterator(Short2ByteMap.Entry from) {
            return new EntryIterator(from.getShortKey());
        }

        @Override
        public ObjectListIterator<Short2ByteMap.Entry> fastIterator() {
            return new FastEntryIterator();
        }

        public ObjectListIterator<Short2ByteMap.Entry> fastIterator(Short2ByteMap.Entry from) {
            return new FastEntryIterator(from.getShortKey());
        }

        @Override
        public void forEach(Consumer<? super Short2ByteMap.Entry> consumer) {
            int i = Short2ByteLinkedOpenHashMap.this.size;
            int next = Short2ByteLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                int curr = next;
                next = (int)Short2ByteLinkedOpenHashMap.this.link[curr];
                consumer.accept(new AbstractShort2ByteMap.BasicEntry(Short2ByteLinkedOpenHashMap.this.key[curr], Short2ByteLinkedOpenHashMap.this.value[curr]));
            }
        }

        @Override
        public void fastForEach(Consumer<? super Short2ByteMap.Entry> consumer) {
            AbstractShort2ByteMap.BasicEntry entry = new AbstractShort2ByteMap.BasicEntry();
            int i = Short2ByteLinkedOpenHashMap.this.size;
            int next = Short2ByteLinkedOpenHashMap.this.first;
            while (i-- != 0) {
                int curr = next;
                next = (int)Short2ByteLinkedOpenHashMap.this.link[curr];
                entry.key = Short2ByteLinkedOpenHashMap.this.key[curr];
                entry.value = Short2ByteLinkedOpenHashMap.this.value[curr];
                consumer.accept(entry);
            }
        }
    }

    private class FastEntryIterator
    extends MapIterator
    implements ObjectListIterator<Short2ByteMap.Entry> {
        final MapEntry entry;

        public FastEntryIterator() {
            this.entry = new MapEntry();
        }

        public FastEntryIterator(short from) {
            super(from);
            this.entry = new MapEntry();
        }

        @Override
        public MapEntry next() {
            this.entry.index = this.nextEntry();
            return this.entry;
        }

        @Override
        public MapEntry previous() {
            this.entry.index = this.previousEntry();
            return this.entry;
        }
    }

    private class EntryIterator
    extends MapIterator
    implements ObjectListIterator<Short2ByteMap.Entry> {
        private MapEntry entry;

        public EntryIterator() {
        }

        public EntryIterator(short from) {
            super(from);
        }

        @Override
        public MapEntry next() {
            this.entry = new MapEntry(this.nextEntry());
            return this.entry;
        }

        @Override
        public MapEntry previous() {
            this.entry = new MapEntry(this.previousEntry());
            return this.entry;
        }

        @Override
        public void remove() {
            super.remove();
            this.entry.index = -1;
        }
    }

    private class MapIterator {
        int prev = -1;
        int next = -1;
        int curr = -1;
        int index = -1;

        protected MapIterator() {
            this.next = Short2ByteLinkedOpenHashMap.this.first;
            this.index = 0;
        }

        private MapIterator(short from) {
            if (from == 0) {
                if (Short2ByteLinkedOpenHashMap.this.containsNullKey) {
                    this.next = (int)Short2ByteLinkedOpenHashMap.this.link[Short2ByteLinkedOpenHashMap.this.n];
                    this.prev = Short2ByteLinkedOpenHashMap.this.n;
                    return;
                }
                throw new NoSuchElementException("The key " + from + " does not belong to this map.");
            }
            if (Short2ByteLinkedOpenHashMap.this.key[Short2ByteLinkedOpenHashMap.this.last] == from) {
                this.prev = Short2ByteLinkedOpenHashMap.this.last;
                this.index = Short2ByteLinkedOpenHashMap.this.size;
                return;
            }
            int pos = HashCommon.mix(from) & Short2ByteLinkedOpenHashMap.this.mask;
            while (Short2ByteLinkedOpenHashMap.this.key[pos] != 0) {
                if (Short2ByteLinkedOpenHashMap.this.key[pos] == from) {
                    this.next = (int)Short2ByteLinkedOpenHashMap.this.link[pos];
                    this.prev = pos;
                    return;
                }
                pos = pos + 1 & Short2ByteLinkedOpenHashMap.this.mask;
            }
            throw new NoSuchElementException("The key " + from + " does not belong to this map.");
        }

        public boolean hasNext() {
            return this.next != -1;
        }

        public boolean hasPrevious() {
            return this.prev != -1;
        }

        private final void ensureIndexKnown() {
            if (this.index >= 0) {
                return;
            }
            if (this.prev == -1) {
                this.index = 0;
                return;
            }
            if (this.next == -1) {
                this.index = Short2ByteLinkedOpenHashMap.this.size;
                return;
            }
            int pos = Short2ByteLinkedOpenHashMap.this.first;
            this.index = 1;
            while (pos != this.prev) {
                pos = (int)Short2ByteLinkedOpenHashMap.this.link[pos];
                ++this.index;
            }
        }

        public int nextIndex() {
            this.ensureIndexKnown();
            return this.index;
        }

        public int previousIndex() {
            this.ensureIndexKnown();
            return this.index - 1;
        }

        public int nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next;
            this.next = (int)Short2ByteLinkedOpenHashMap.this.link[this.curr];
            this.prev = this.curr;
            if (this.index >= 0) {
                ++this.index;
            }
            return this.curr;
        }

        public int previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev;
            this.prev = (int)(Short2ByteLinkedOpenHashMap.this.link[this.curr] >>> 32);
            this.next = this.curr;
            if (this.index >= 0) {
                --this.index;
            }
            return this.curr;
        }

        public void remove() {
            this.ensureIndexKnown();
            if (this.curr == -1) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
                this.prev = (int)(Short2ByteLinkedOpenHashMap.this.link[this.curr] >>> 32);
            } else {
                this.next = (int)Short2ByteLinkedOpenHashMap.this.link[this.curr];
            }
            --Short2ByteLinkedOpenHashMap.this.size;
            if (this.prev == -1) {
                Short2ByteLinkedOpenHashMap.this.first = this.next;
            } else {
                int n = this.prev;
                Short2ByteLinkedOpenHashMap.this.link[n] = Short2ByteLinkedOpenHashMap.this.link[n] ^ (Short2ByteLinkedOpenHashMap.this.link[this.prev] ^ (long)this.next & 0xFFFFFFFFL) & 0xFFFFFFFFL;
            }
            if (this.next == -1) {
                Short2ByteLinkedOpenHashMap.this.last = this.prev;
            } else {
                int n = this.next;
                Short2ByteLinkedOpenHashMap.this.link[n] = Short2ByteLinkedOpenHashMap.this.link[n] ^ (Short2ByteLinkedOpenHashMap.this.link[this.next] ^ ((long)this.prev & 0xFFFFFFFFL) << 32) & 0xFFFFFFFF00000000L;
            }
            int pos = this.curr;
            this.curr = -1;
            if (pos != Short2ByteLinkedOpenHashMap.this.n) {
                short[] key = Short2ByteLinkedOpenHashMap.this.key;
                while (true) {
                    short curr;
                    int last = pos;
                    pos = last + 1 & Short2ByteLinkedOpenHashMap.this.mask;
                    while (true) {
                        if ((curr = key[pos]) == 0) {
                            key[last] = 0;
                            return;
                        }
                        int slot = HashCommon.mix(curr) & Short2ByteLinkedOpenHashMap.this.mask;
                        if (last <= pos ? last >= slot || slot > pos : last >= slot && slot > pos) break;
                        pos = pos + 1 & Short2ByteLinkedOpenHashMap.this.mask;
                    }
                    key[last] = curr;
                    Short2ByteLinkedOpenHashMap.this.value[last] = Short2ByteLinkedOpenHashMap.this.value[pos];
                    if (this.next == pos) {
                        this.next = last;
                    }
                    if (this.prev == pos) {
                        this.prev = last;
                    }
                    Short2ByteLinkedOpenHashMap.this.fixPointers(pos, last);
                }
            }
            Short2ByteLinkedOpenHashMap.this.containsNullKey = false;
        }

        public int skip(int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }

        public int back(int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }

        public void set(Short2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }

        public void add(Short2ByteMap.Entry ok) {
            throw new UnsupportedOperationException();
        }
    }

    final class MapEntry
    implements Short2ByteMap.Entry,
    Map.Entry<Short, Byte> {
        int index;

        MapEntry(int index) {
            this.index = index;
        }

        MapEntry() {
        }

        @Override
        public short getShortKey() {
            return Short2ByteLinkedOpenHashMap.this.key[this.index];
        }

        @Override
        public byte getByteValue() {
            return Short2ByteLinkedOpenHashMap.this.value[this.index];
        }

        @Override
        public byte setValue(byte v) {
            byte oldValue = Short2ByteLinkedOpenHashMap.this.value[this.index];
            Short2ByteLinkedOpenHashMap.this.value[this.index] = v;
            return oldValue;
        }

        @Override
        @Deprecated
        public Short getKey() {
            return Short2ByteLinkedOpenHashMap.this.key[this.index];
        }

        @Override
        @Deprecated
        public Byte getValue() {
            return Short2ByteLinkedOpenHashMap.this.value[this.index];
        }

        @Override
        @Deprecated
        public Byte setValue(Byte v) {
            return this.setValue((byte)v);
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            return Short2ByteLinkedOpenHashMap.this.key[this.index] == (Short)e.getKey() && Short2ByteLinkedOpenHashMap.this.value[this.index] == (Byte)e.getValue();
        }

        @Override
        public int hashCode() {
            return Short2ByteLinkedOpenHashMap.this.key[this.index] ^ Short2ByteLinkedOpenHashMap.this.value[this.index];
        }

        public String toString() {
            return Short2ByteLinkedOpenHashMap.this.key[this.index] + "=>" + Short2ByteLinkedOpenHashMap.this.value[this.index];
        }
    }
}

