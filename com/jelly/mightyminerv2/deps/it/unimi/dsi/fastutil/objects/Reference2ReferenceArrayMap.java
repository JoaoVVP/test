/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReference2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReferenceSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

public class Reference2ReferenceArrayMap<K, V>
extends AbstractReference2ReferenceMap<K, V>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient Object[] value;
    private int size;

    public Reference2ReferenceArrayMap(Object[] key, Object[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Reference2ReferenceArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = ObjectArrays.EMPTY_ARRAY;
    }

    public Reference2ReferenceArrayMap(int capacity) {
        this.key = new Object[capacity];
        this.value = new Object[capacity];
    }

    public Reference2ReferenceArrayMap(Reference2ReferenceMap<K, V> m) {
        this(m.size());
        this.putAll(m);
    }

    public Reference2ReferenceArrayMap(Map<? extends K, ? extends V> m) {
        this(m.size());
        this.putAll(m);
    }

    public Reference2ReferenceArrayMap(Object[] key, Object[] value, int size) {
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

    public Reference2ReferenceMap.FastEntrySet<K, V> reference2ReferenceEntrySet() {
        return new EntrySet();
    }

    private int findKey(Object k) {
        Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] != k) continue;
            return i;
        }
        return -1;
    }

    @Override
    public V get(Object k) {
        Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] != k) continue;
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
            this.key[i] = null;
            this.value[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(Object k) {
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
    public V put(K k, V v) {
        int oldKey = this.findKey(k);
        if (oldKey != -1) {
            Object oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return (V)oldValue;
        }
        if (this.size == this.key.length) {
            Object[] newKey = new Object[this.size == 0 ? 2 : this.size * 2];
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
    public V remove(Object k) {
        int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return (V)this.defRetValue;
        }
        Object oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        this.value[this.size] = null;
        return (V)oldValue;
    }

    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>(){

            @Override
            public boolean contains(Object k) {
                return Reference2ReferenceArrayMap.this.findKey(k) != -1;
            }

            @Override
            public boolean remove(Object k) {
                int oldPos = Reference2ReferenceArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                int tail = Reference2ReferenceArrayMap.this.size - oldPos - 1;
                System.arraycopy(Reference2ReferenceArrayMap.this.key, oldPos + 1, Reference2ReferenceArrayMap.this.key, oldPos, tail);
                System.arraycopy(Reference2ReferenceArrayMap.this.value, oldPos + 1, Reference2ReferenceArrayMap.this.value, oldPos, tail);
                Reference2ReferenceArrayMap.this.size--;
                return true;
            }

            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Reference2ReferenceArrayMap.this.size;
                    }

                    @Override
                    public K next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Reference2ReferenceArrayMap.this.key[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Reference2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2ReferenceArrayMap.this.key, this.pos, Reference2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2ReferenceArrayMap.this.value, this.pos, Reference2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Reference2ReferenceArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Reference2ReferenceArrayMap.this.size;
            }

            @Override
            public void clear() {
                Reference2ReferenceArrayMap.this.clear();
            }
        };
    }

    @Override
    public ReferenceCollection<V> values() {
        return new AbstractReferenceCollection<V>(){

            @Override
            public boolean contains(Object v) {
                return Reference2ReferenceArrayMap.this.containsValue(v);
            }

            @Override
            public ObjectIterator<V> iterator() {
                return new ObjectIterator<V>(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Reference2ReferenceArrayMap.this.size;
                    }

                    @Override
                    public V next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Reference2ReferenceArrayMap.this.value[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Reference2ReferenceArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2ReferenceArrayMap.this.key, this.pos, Reference2ReferenceArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2ReferenceArrayMap.this.value, this.pos, Reference2ReferenceArrayMap.this.value, this.pos - 1, tail);
                        Reference2ReferenceArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Reference2ReferenceArrayMap.this.size;
            }

            @Override
            public void clear() {
                Reference2ReferenceArrayMap.this.clear();
            }
        };
    }

    public Reference2ReferenceArrayMap<K, V> clone() {
        Reference2ReferenceArrayMap c;
        try {
            c = (Reference2ReferenceArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = (Object[])this.key.clone();
        c.value = (Object[])this.value.clone();
        return c;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.key[i]);
            s.writeObject(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new Object[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readObject();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Reference2ReferenceMap.Entry<K, V>>
    implements Reference2ReferenceMap.FastEntrySet<K, V> {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Reference2ReferenceMap.Entry<K, V>> iterator() {
            return new ObjectIterator<Reference2ReferenceMap.Entry<K, V>>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Reference2ReferenceArrayMap.this.size;
                }

                @Override
                public Reference2ReferenceMap.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractReference2ReferenceMap.BasicEntry<Object, Object>(Reference2ReferenceArrayMap.this.key[this.curr], Reference2ReferenceArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Reference2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2ReferenceArrayMap.this.key, this.next + 1, Reference2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2ReferenceArrayMap.this.value, this.next + 1, Reference2ReferenceArrayMap.this.value, this.next, tail);
                    ((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).key[((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).size] = null;
                    ((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).value[((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).size] = null;
                }
            };
        }

        @Override
        public ObjectIterator<Reference2ReferenceMap.Entry<K, V>> fastIterator() {
            return new ObjectIterator<Reference2ReferenceMap.Entry<K, V>>(){
                int next = 0;
                int curr = -1;
                final AbstractReference2ReferenceMap.BasicEntry<K, V> entry = new AbstractReference2ReferenceMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Reference2ReferenceArrayMap.this.size;
                }

                @Override
                public Reference2ReferenceMap.Entry<K, V> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Reference2ReferenceArrayMap.this.key[this.curr];
                    this.entry.value = Reference2ReferenceArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Reference2ReferenceArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2ReferenceArrayMap.this.key, this.next + 1, Reference2ReferenceArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2ReferenceArrayMap.this.value, this.next + 1, Reference2ReferenceArrayMap.this.value, this.next, tail);
                    ((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).key[((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).size] = null;
                    ((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).value[((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).size] = null;
                }
            };
        }

        @Override
        public int size() {
            return Reference2ReferenceArrayMap.this.size;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            Object k = e.getKey();
            return Reference2ReferenceArrayMap.this.containsKey(k) && Reference2ReferenceArrayMap.this.get(k) == e.getValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            Object k = e.getKey();
            Object v = e.getValue();
            int oldPos = Reference2ReferenceArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Reference2ReferenceArrayMap.this.value[oldPos]) {
                return false;
            }
            int tail = Reference2ReferenceArrayMap.this.size - oldPos - 1;
            System.arraycopy(Reference2ReferenceArrayMap.this.key, oldPos + 1, Reference2ReferenceArrayMap.this.key, oldPos, tail);
            System.arraycopy(Reference2ReferenceArrayMap.this.value, oldPos + 1, Reference2ReferenceArrayMap.this.value, oldPos, tail);
            Reference2ReferenceArrayMap.this.size--;
            ((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).key[((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).size] = null;
            ((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).value[((Reference2ReferenceArrayMap)Reference2ReferenceArrayMap.this).size] = null;
            return true;
        }
    }
}

