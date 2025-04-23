/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloatBigList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatBigArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatBigList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatBigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

public class FloatBigArrayBigList
extends AbstractFloatBigList
implements RandomAccess,
Cloneable,
Serializable {
    private static final long serialVersionUID = -7046029254386353130L;
    public static final int DEFAULT_INITIAL_CAPACITY = 10;
    protected transient float[][] a;
    protected long size;

    protected FloatBigArrayBigList(float[][] a, boolean dummy) {
        this.a = a;
    }

    public FloatBigArrayBigList(long capacity) {
        if (capacity < 0L) {
            throw new IllegalArgumentException("Initial capacity (" + capacity + ") is negative");
        }
        this.a = capacity == 0L ? FloatBigArrays.EMPTY_BIG_ARRAY : FloatBigArrays.newBigArray(capacity);
    }

    public FloatBigArrayBigList() {
        this.a = FloatBigArrays.DEFAULT_EMPTY_BIG_ARRAY;
    }

    public FloatBigArrayBigList(FloatCollection c) {
        this(c.size());
        FloatIterator i = c.iterator();
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }

    public FloatBigArrayBigList(FloatBigList l) {
        this(l.size64());
        this.size = l.size64();
        l.getElements(0L, this.a, 0L, this.size);
    }

    public FloatBigArrayBigList(float[][] a) {
        this(a, 0L, FloatBigArrays.length(a));
    }

    public FloatBigArrayBigList(float[][] a, long offset, long length) {
        this(length);
        FloatBigArrays.copy(a, offset, this.a, 0L, length);
        this.size = length;
    }

    public FloatBigArrayBigList(Iterator<? extends Float> i) {
        this();
        while (i.hasNext()) {
            this.add(i.next().floatValue());
        }
    }

    public FloatBigArrayBigList(FloatIterator i) {
        this();
        while (i.hasNext()) {
            this.add(i.nextFloat());
        }
    }

    public float[][] elements() {
        return this.a;
    }

    public static FloatBigArrayBigList wrap(float[][] a, long length) {
        if (length > FloatBigArrays.length(a)) {
            throw new IllegalArgumentException("The specified length (" + length + ") is greater than the array size (" + FloatBigArrays.length(a) + ")");
        }
        FloatBigArrayBigList l = new FloatBigArrayBigList(a, false);
        l.size = length;
        return l;
    }

    public static FloatBigArrayBigList wrap(float[][] a) {
        return FloatBigArrayBigList.wrap(a, FloatBigArrays.length(a));
    }

    public void ensureCapacity(long capacity) {
        if (capacity <= (long)this.a.length || this.a == FloatBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            return;
        }
        this.a = FloatBigArrays.forceCapacity(this.a, capacity, this.size);
        assert (this.size <= FloatBigArrays.length(this.a));
    }

    private void grow(long capacity) {
        long oldLength = FloatBigArrays.length(this.a);
        if (capacity <= oldLength) {
            return;
        }
        if (this.a != FloatBigArrays.DEFAULT_EMPTY_BIG_ARRAY) {
            capacity = Math.max(oldLength + (oldLength >> 1), capacity);
        } else if (capacity < 10L) {
            capacity = 10L;
        }
        this.a = FloatBigArrays.forceCapacity(this.a, capacity, this.size);
        assert (this.size <= FloatBigArrays.length(this.a));
    }

    @Override
    public void add(long index, float k) {
        this.ensureIndex(index);
        this.grow(this.size + 1L);
        if (index != this.size) {
            FloatBigArrays.copy(this.a, index, this.a, index + 1L, this.size - index);
        }
        FloatBigArrays.set(this.a, index, k);
        ++this.size;
        assert (this.size <= FloatBigArrays.length(this.a));
    }

    @Override
    public boolean add(float k) {
        this.grow(this.size + 1L);
        FloatBigArrays.set(this.a, this.size++, k);
        assert (this.size <= FloatBigArrays.length(this.a));
        return true;
    }

    @Override
    public float getFloat(long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        return FloatBigArrays.get(this.a, index);
    }

    @Override
    public long indexOf(float k) {
        for (long i = 0L; i < this.size; ++i) {
            if (Float.floatToIntBits(k) != Float.floatToIntBits(FloatBigArrays.get(this.a, i))) continue;
            return i;
        }
        return -1L;
    }

    @Override
    public long lastIndexOf(float k) {
        long i = this.size;
        while (i-- != 0L) {
            if (Float.floatToIntBits(k) != Float.floatToIntBits(FloatBigArrays.get(this.a, i))) continue;
            return i;
        }
        return -1L;
    }

    @Override
    public float removeFloat(long index) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        float old = FloatBigArrays.get(this.a, index);
        --this.size;
        if (index != this.size) {
            FloatBigArrays.copy(this.a, index + 1L, this.a, index, this.size - index);
        }
        assert (this.size <= FloatBigArrays.length(this.a));
        return old;
    }

    @Override
    public boolean rem(float k) {
        long index = this.indexOf(k);
        if (index == -1L) {
            return false;
        }
        this.removeFloat(index);
        assert (this.size <= FloatBigArrays.length(this.a));
        return true;
    }

    @Override
    public float set(long index, float k) {
        if (index >= this.size) {
            throw new IndexOutOfBoundsException("Index (" + index + ") is greater than or equal to list size (" + this.size + ")");
        }
        float old = FloatBigArrays.get(this.a, index);
        FloatBigArrays.set(this.a, index, k);
        return old;
    }

    @Override
    public boolean removeAll(FloatCollection c) {
        float[] s = null;
        float[] d = null;
        int ss = -1;
        int sd = 0x8000000;
        int ds = -1;
        int dd = 0x8000000;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 0x8000000) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains((float)s[sd])) {
                if (dd == 0x8000000) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        long j = BigArrays.index(ds, dd);
        boolean modified = this.size != j;
        this.size = j;
        return modified;
    }

    @Override
    public boolean removeAll(Collection<?> c) {
        float[] s = null;
        float[] d = null;
        int ss = -1;
        int sd = 0x8000000;
        int ds = -1;
        int dd = 0x8000000;
        for (long i = 0L; i < this.size; ++i) {
            if (sd == 0x8000000) {
                sd = 0;
                s = this.a[++ss];
            }
            if (!c.contains(Float.valueOf((float)s[sd]))) {
                if (dd == 0x8000000) {
                    d = this.a[++ds];
                    dd = 0;
                }
                d[dd++] = s[sd];
            }
            ++sd;
        }
        long j = BigArrays.index(ds, dd);
        boolean modified = this.size != j;
        this.size = j;
        return modified;
    }

    @Override
    public void clear() {
        this.size = 0L;
        assert (this.size <= FloatBigArrays.length(this.a));
    }

    @Override
    public long size64() {
        return this.size;
    }

    @Override
    public void size(long size) {
        if (size > FloatBigArrays.length(this.a)) {
            this.ensureCapacity(size);
        }
        if (size > this.size) {
            FloatBigArrays.fill(this.a, this.size, size, 0.0f);
        }
        this.size = size;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0L;
    }

    public void trim() {
        this.trim(0L);
    }

    public void trim(long n) {
        long arrayLength = FloatBigArrays.length(this.a);
        if (n >= arrayLength || this.size == arrayLength) {
            return;
        }
        this.a = FloatBigArrays.trim(this.a, Math.max(n, this.size));
        assert (this.size <= FloatBigArrays.length(this.a));
    }

    @Override
    public void getElements(long from, float[][] a, long offset, long length) {
        FloatBigArrays.copy(this.a, from, a, offset, length);
    }

    @Override
    public void removeElements(long from, long to) {
        BigArrays.ensureFromTo(this.size, from, to);
        FloatBigArrays.copy(this.a, to, this.a, from, this.size - to);
        this.size -= to - from;
    }

    @Override
    public void addElements(long index, float[][] a, long offset, long length) {
        this.ensureIndex(index);
        FloatBigArrays.ensureOffsetLength(a, offset, length);
        this.grow(this.size + length);
        FloatBigArrays.copy(this.a, index, this.a, index + length, this.size - index);
        FloatBigArrays.copy(a, offset, this.a, index, length);
        this.size += length;
    }

    @Override
    public FloatBigListIterator listIterator(final long index) {
        this.ensureIndex(index);
        return new FloatBigListIterator(){
            long pos;
            long last;
            {
                this.pos = index;
                this.last = -1L;
            }

            @Override
            public boolean hasNext() {
                return this.pos < FloatBigArrayBigList.this.size;
            }

            @Override
            public boolean hasPrevious() {
                return this.pos > 0L;
            }

            @Override
            public float nextFloat() {
                if (!this.hasNext()) {
                    throw new NoSuchElementException();
                }
                this.last = this.pos++;
                return FloatBigArrays.get(FloatBigArrayBigList.this.a, this.last);
            }

            @Override
            public float previousFloat() {
                if (!this.hasPrevious()) {
                    throw new NoSuchElementException();
                }
                this.last = --this.pos;
                return FloatBigArrays.get(FloatBigArrayBigList.this.a, this.pos);
            }

            @Override
            public long nextIndex() {
                return this.pos;
            }

            @Override
            public long previousIndex() {
                return this.pos - 1L;
            }

            @Override
            public void add(float k) {
                FloatBigArrayBigList.this.add(this.pos++, k);
                this.last = -1L;
            }

            @Override
            public void set(float k) {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                FloatBigArrayBigList.this.set(this.last, k);
            }

            @Override
            public void remove() {
                if (this.last == -1L) {
                    throw new IllegalStateException();
                }
                FloatBigArrayBigList.this.removeFloat(this.last);
                if (this.last < this.pos) {
                    --this.pos;
                }
                this.last = -1L;
            }
        };
    }

    public FloatBigArrayBigList clone() {
        FloatBigArrayBigList c = new FloatBigArrayBigList(this.size);
        FloatBigArrays.copy(this.a, 0L, c.a, 0L, this.size);
        c.size = this.size;
        return c;
    }

    public boolean equals(FloatBigArrayBigList l) {
        if (l == this) {
            return true;
        }
        long s = this.size64();
        if (s != l.size64()) {
            return false;
        }
        float[][] a1 = this.a;
        float[][] a2 = l.a;
        while (s-- != 0L) {
            if (FloatBigArrays.get(a1, s) == FloatBigArrays.get(a2, s)) continue;
            return false;
        }
        return true;
    }

    @Override
    public int compareTo(FloatBigArrayBigList l) {
        long s1 = this.size64();
        long s2 = l.size64();
        float[][] a1 = this.a;
        float[][] a2 = l.a;
        int i = 0;
        while ((long)i < s1 && (long)i < s2) {
            float e2;
            float e1 = FloatBigArrays.get(a1, i);
            int r = Float.compare(e1, e2 = FloatBigArrays.get(a2, i));
            if (r != 0) {
                return r;
            }
            ++i;
        }
        return (long)i < s2 ? -1 : ((long)i < s1 ? 1 : 0);
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        int i = 0;
        while ((long)i < this.size) {
            s.writeFloat(FloatBigArrays.get(this.a, i));
            ++i;
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.a = FloatBigArrays.newBigArray(this.size);
        int i = 0;
        while ((long)i < this.size) {
            FloatBigArrays.set(this.a, i, s.readFloat());
            ++i;
        }
    }
}

