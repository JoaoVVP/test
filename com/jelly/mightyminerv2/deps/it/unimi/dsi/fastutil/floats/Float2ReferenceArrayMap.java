/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloat2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloatSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

public class Float2ReferenceArrayMap<V>
extends AbstractFloat2ReferenceMap<V>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient float[] key;
    private transient Object[] value;
    private int size;

    public Float2ReferenceArrayMap(float[] key, Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Float2ReferenceArrayMap() {
        this.key = FloatArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }

    public Float2ReferenceArrayMap(int capacity) {
        this.key = new float[capacity];
        this.value = new Object[capacity];
    }

    public Float2ReferenceArrayMap(Float2ReferenceMap<V> m) {
        this(m.size());
        this.putAll(m);
    }

    public Float2ReferenceArrayMap(Map<? extends Float, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }

    public Float2ReferenceArrayMap(float[] key, Object[] value, int size) {
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

    public Float2ReferenceMap.FastEntrySet<V> float2ReferenceEntrySet() {
        return new EntrySet();
    }

    private int findKey(float k) {
        float[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) != Float.floatToIntBits(k)) continue;
            return i;
        }
        return -1;
    }

    @Override
    public V get(float k) {
        float[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (Float.floatToIntBits(key[i]) != Float.floatToIntBits(k)) continue;
            return (V)this.value[i];
        }
        return (V)this.defRetValue;
    }

    @Override
    public int size() {
        return this.size;
    }

    @Override
    public void clear() {
        int i = this.size;
        while (i-- != 0) {
            this.value[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(float k) {
        return this.findKey(k) != -1;
    }

    @Override
    public boolean containsValue(Object v) {
        int i = this.size;
        while (i-- != 0) {
            if (this.value[i] != v) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size == 0;
    }

    @Override
    public V put(float k, V v) {
        int oldKey = this.findKey(k);
        if (oldKey != -1) {
            Object oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return (V)oldValue;
        }
        if (this.size == this.key.length) {
            float[] newKey = new float[this.size == 0 ? 2 : this.size * 2];
            Object[] newValue = new Object[this.size == 0 ? 2 : this.size * 2];
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
        return (V)this.defRetValue;
    }

    @Override
    public V remove(float k) {
        int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return (V)this.defRetValue;
        }
        Object oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.value[this.size] = null;
        return (V)oldValue;
    }

    @Override
    public FloatSet keySet() {
        return new AbstractFloatSet(){

            @Override
            public boolean contains(float k) {
                return Float2ReferenceArrayMap.this.findKey(k) != -1;
            }

            @Override
            public boolean remove(float k) {
                int oldPos = Float2ReferenceArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                int tail = Float2ReferenceArrayMap.this.size - oldPos - 1;
                System.arraycopy(Float2ReferenceArrayMap.this.key, oldPos + 1, Float2ReferenceArrayMap.this.key, oldPos, tail);
                System.arraycopy(Float2ReferenceArrayMap.this.value, oldPos + 1, Float2ReferenceArrayMap.this.value, oldPos, tail);
                Float2ReferenceArrayMap.this.size--;
                return true;
            }

            @Override
            public FloatIterator iterator() {
                return new FloatIterator(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Float2ReferenceArrayMap.this.size;
                    }

                    @Override
                    public float nextFloat() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2ReferenceArrayMap.this.key[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Float2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Float2ReferenceArrayMap.this.key, this.pos, Float2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2ReferenceArrayMap.this.value, this.pos, Float2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Float2ReferenceArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Float2ReferenceArrayMap.this.size;
            }

            @Override
            public void clear() {
                Float2ReferenceArrayMap.this.clear();
            }
        };
    }

    @Override
    public ReferenceCollection<V> values() {
        return new AbstractReferenceCollection<V>(){

            @Override
            public boolean contains(Object v) {
                return Float2ReferenceArrayMap.this.containsValue(v);
            }

            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Float2ReferenceArrayMap.this.size;
                    }

                    @Override
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Float2ReferenceArrayMap.this.value[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Float2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Float2ReferenceArrayMap.this.key, this.pos, Float2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Float2ReferenceArrayMap.this.value, this.pos, Float2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Float2ReferenceArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Float2ReferenceArrayMap.this.size;
            }

            @Override
            public void clear() {
                Float2ReferenceArrayMap.this.clear();
            }
        };
    }

    public Float2ReferenceArrayMap<V> clone() {
        Float2ReferenceArrayMap c;
        try {
            c = (Float2ReferenceArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = (float[])this.key.clone();
        c.value = (Object[])this.value.clone();
        return c;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeFloat(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new float[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readFloat();
            this.value[i] = s.readObject();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Float2ReferenceMap.Entry<V>>
    implements Float2ReferenceMap.FastEntrySet<V> {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Float2ReferenceMap.Entry<V>> iterator() {
            return new ObjectIterator<Float2ReferenceMap.Entry<V>>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Float2ReferenceArrayMap.this.size;
                }

                @Override
                public Float2ReferenceMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractFloat2ReferenceMap.BasicEntry<Object>(Float2ReferenceArrayMap.this.key[this.curr], Float2ReferenceArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Float2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2ReferenceArrayMap.this.key, this.next + 1, Float2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2ReferenceArrayMap.this.value, this.next + 1, Float2ReferenceArrayMap.this.value, this.next, tail);
                    ((Float2ReferenceArrayMap)Float2ReferenceArrayMap.this).value[((Float2ReferenceArrayMap)Float2ReferenceArrayMap.this).size] = null;
                }
            };
        }

        @Override
        public ObjectIterator<Float2ReferenceMap.Entry<V>> fastIterator() {
            return new ObjectIterator<Float2ReferenceMap.Entry<V>>(){
                int next = 0;
                int curr = -1;
                final AbstractFloat2ReferenceMap.BasicEntry<V> entry = new AbstractFloat2ReferenceMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Float2ReferenceArrayMap.this.size;
                }

                @Override
                public Float2ReferenceMap.Entry<V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Float2ReferenceArrayMap.this.key[this.curr];
                    this.entry.value = Float2ReferenceArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Float2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Float2ReferenceArrayMap.this.key, this.next + 1, Float2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Float2ReferenceArrayMap.this.value, this.next + 1, Float2ReferenceArrayMap.this.value, this.next, tail);
                    ((Float2ReferenceArrayMap)Float2ReferenceArrayMap.this).value[((Float2ReferenceArrayMap)Float2ReferenceArrayMap.this).size] = null;
                }
            };
        }

        @Override
        public int size() {
            return Float2ReferenceArrayMap.this.size;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            float k = ((Float)e.getKey()).floatValue();
            return Float2ReferenceArrayMap.this.containsKey(k) && Float2ReferenceArrayMap.this.get(k) == e.getValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getKey() == null || !(e.getKey() instanceof Float)) {
                return false;
            }
            float k = ((Float)e.getKey()).floatValue();
            Object v = e.getValue();
            int oldPos = Float2ReferenceArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Float2ReferenceArrayMap.this.value[oldPos]) {
                return false;
            }
            int tail = Float2ReferenceArrayMap.this.size - oldPos - 1;
            System.arraycopy(Float2ReferenceArrayMap.this.key, oldPos + 1, Float2ReferenceArrayMap.this.key, oldPos, tail);
            System.arraycopy(Float2ReferenceArrayMap.this.value, oldPos + 1, Float2ReferenceArrayMap.this.value, oldPos, tail);
            Float2ReferenceArrayMap.this.size--;
            ((Float2ReferenceArrayMap)Float2ReferenceArrayMap.this).value[((Float2ReferenceArrayMap)Float2ReferenceArrayMap.this).size] = null;
            return true;
        }
    }
}

