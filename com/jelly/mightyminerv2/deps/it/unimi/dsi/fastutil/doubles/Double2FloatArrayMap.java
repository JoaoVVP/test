/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDouble2FloatMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2FloatMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

public class Double2FloatArrayMap
extends AbstractDouble2FloatMap
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient double[] key;
    private transient float[] value;
    private int size;

    public Double2FloatArrayMap(double[] key, float[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Double2FloatArrayMap() {
        this.key = DoubleArrays.EMPTY_ARRAY;
        this.value = FloatArrays.EMPTY_ARRAY;
    }

    public Double2FloatArrayMap(int capacity) {
        this.key = new double[capacity];
        this.value = new float[capacity];
    }

    public Double2FloatArrayMap(Double2FloatMap m) {
        this(m.size());
        this.putAll(m);
    }

    public Double2FloatArrayMap(Map<? extends Double, ? extends Float> m) {
        this(m.size());
        this.putAll(m);
    }

    public Double2FloatArrayMap(double[] key, float[] value, int size) {
        this.key = key;
        this.value = value;
        this.size = size;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
        if (size > key.length) {
            throw new IllegalArgumentException("The provided size (" + size + ") is larger than or equal to the backing-arrays size (" + key.length + ")");
        }
    }

    public Double2FloatMap.FastEntrySet double2FloatEntrySet() {
        return new EntrySet();
    }

    private int findKey(double k) {
        double[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) != Double.doubleToLongBits(k)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public float get(double k) {
        double[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Double.doubleToLongBits(key[i]) != Double.doubleToLongBits(k)) continue;
            return this.value[i];
        }
        return this.defRetValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        this.size = 0;
    }

    @Override
    public boolean containsKey(double k) {
        return this.findKey(k) != -1;
    }

    @Override
    public boolean containsValue(float v) {
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(this.value[i]) != Float.floatToIntBits(v)) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public float put(double k, float v) {
        int oldKey = this.findKey(k);
        if (oldKey != -1) {
            float oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            double[] newKey = new double[this.size == 0 ? 2 : this.size * 2];
            float[] newValue = new float[this.size == 0 ? 2 : this.size * 2];
            int i = this.size;
            while (i-- != 0) {
                newKey[i] = this.key[i];
                newValue[i] = this.value[i];
            }
            this.key = newKey;
            this.value = newValue;
        }
        this.key[this.size] = k;
        this.value[this.size] = v;
        ++this.size;
        return this.defRetValue;
    }

    @Override
    public float remove(double k) {
        int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        float oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        return oldValue;
    }

    @Override
    public DoubleSet keySet() {
        return new AbstractDoubleSet(){

            @Override
            public boolean contains(double k) {
                return Double2FloatArrayMap.this.findKey(k) != -1;
            }

            @Override
            public boolean remove(double k) {
                int oldPos = Double2FloatArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                int tail = Double2FloatArrayMap.this.size - oldPos - 1;
                System.arraycopy(Double2FloatArrayMap.this.key, oldPos + 1, Double2FloatArrayMap.this.key, oldPos, tail);
                System.arraycopy(Double2FloatArrayMap.this.value, oldPos + 1, Double2FloatArrayMap.this.value, oldPos, tail);
                Double2FloatArrayMap.this.size--;
                return true;
            }

            @Override
            public DoubleIterator iterator() {
                return new DoubleIterator(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Double2FloatArrayMap.this.size;
                    }

                    @Override
                    public double nextDouble() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Double2FloatArrayMap.this.key[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Double2FloatArrayMap.this.size - this.pos;
                        System.arraycopy(Double2FloatArrayMap.this.key, this.pos, Double2FloatArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Double2FloatArrayMap.this.value, this.pos, Double2FloatArrayMap.this.value, this.pos - 1, tail);
                        Double2FloatArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Double2FloatArrayMap.this.size;
            }

            @Override
            public void clear() {
                Double2FloatArrayMap.this.clear();
            }
        };
    }

    @Override
    public FloatCollection values() {
        return new AbstractFloatCollection(){

            @Override
            public boolean contains(float v) {
                return Double2FloatArrayMap.this.containsValue(v);
            }

            @Override
            public FloatIterator iterator() {
                return new FloatIterator(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Double2FloatArrayMap.this.size;
                    }

                    @Override
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Double2FloatArrayMap.this.value[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Double2FloatArrayMap.this.size - this.pos;
                        System.arraycopy(Double2FloatArrayMap.this.key, this.pos, Double2FloatArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Double2FloatArrayMap.this.value, this.pos, Double2FloatArrayMap.this.value, this.pos - 1, tail);
                        Double2FloatArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Double2FloatArrayMap.this.size;
            }

            @Override
            public void clear() {
                Double2FloatArrayMap.this.clear();
            }
        };
    }

    public Double2FloatArrayMap clone() {
        Double2FloatArrayMap c;
        try {
            c = (Double2FloatArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = (double[])this.key.clone();
        c.value = (float[])this.value.clone();
        return c;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeDouble(this.key[i]);
            s.writeFloat(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new double[this.size];
        this.value = new float[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readDouble();
            this.value[i] = s.readFloat();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Double2FloatMap.Entry>
    implements Double2FloatMap.FastEntrySet {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Double2FloatMap.Entry> iterator() {
            return new ObjectIterator<Double2FloatMap.Entry>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Double2FloatArrayMap.this.size;
                }

                @Override
                public Double2FloatMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractDouble2FloatMap.BasicEntry(Double2FloatArrayMap.this.key[this.curr], Double2FloatArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Double2FloatArrayMap.this.size-- - this.next--;
                    System.arraycopy(Double2FloatArrayMap.this.key, this.next + 1, Double2FloatArrayMap.this.key, this.next, tail);
                    System.arraycopy(Double2FloatArrayMap.this.value, this.next + 1, Double2FloatArrayMap.this.value, this.next, tail);
                }
            };
        }

        @Override
        public ObjectIterator<Double2FloatMap.Entry> fastIterator() {
            return new ObjectIterator<Double2FloatMap.Entry>(){
                int next = 0;
                int curr = -1;
                final AbstractDouble2FloatMap.BasicEntry entry = new AbstractDouble2FloatMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Double2FloatArrayMap.this.size;
                }

                @Override
                public Double2FloatMap.Entry next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Double2FloatArrayMap.this.key[this.curr];
                    this.entry.value = Double2FloatArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Double2FloatArrayMap.this.size-- - this.next--;
                    System.arraycopy(Double2FloatArrayMap.this.key, this.next + 1, Double2FloatArrayMap.this.key, this.next, tail);
                    System.arraycopy(Double2FloatArrayMap.this.value, this.next + 1, Double2FloatArrayMap.this.value, this.next, tail);
                }
            };
        }

        @Override
        public int size() {
            return Double2FloatArrayMap.this.size;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            double k = (Double)e.getKey();
            return Double2FloatArrayMap.this.containsKey(k) && Float.floatToIntBits(Double2FloatArrayMap.this.get(k)) == Float.floatToIntBits(((Float)e.getValue()).floatValue());
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Double)) {
                return false;
            }
            if (e.getValue() == null || !(e.getValue() instanceof Float)) {
                return false;
            }
            double k = (Double)e.getKey();
            float v = ((Float)e.getValue()).floatValue();
            int oldPos = Double2FloatArrayMap.this.findKey(k);
            if (oldPos == -1 || Float.floatToIntBits(v) != Float.floatToIntBits(Double2FloatArrayMap.this.value[oldPos])) {
                return false;
            }
            int tail = Double2FloatArrayMap.this.size - oldPos - 1;
            System.arraycopy(Double2FloatArrayMap.this.key, oldPos + 1, Double2FloatArrayMap.this.key, oldPos, tail);
            System.arraycopy(Double2FloatArrayMap.this.value, oldPos + 1, Double2FloatArrayMap.this.value, oldPos, tail);
            Double2FloatArrayMap.this.size--;
            return true;
        }
    }
}

