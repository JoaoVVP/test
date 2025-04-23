/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReference2CharMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReferenceSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.NoSuchElementException;

public class Reference2CharArrayMap<K>
extends AbstractReference2CharMap<K>
implements Serializable,
Cloneable {
    private static final long serialVersionUID = 1L;
    private transient Object[] key;
    private transient char[] value;
    private int size;

    public Reference2CharArrayMap(Object[] key, char[] value) {
        this.key = key;
        this.value = value;
        this.size = key.length;
        if (key.length != value.length) {
            throw new IllegalArgumentException("Keys and values have different lengths (" + key.length + ", " + value.length + ")");
        }
    }

    public Reference2CharArrayMap() {
        this.key = ObjectArrays.EMPTY_ARRAY;
        this.value = CharArrays.EMPTY_ARRAY;
    }

    public Reference2CharArrayMap(int capacity) {
        this.key = new Object[capacity];
        this.value = new char[capacity];
    }

    public Reference2CharArrayMap(Reference2CharMap<K> m) {
        this(m.size());
        this.putAll(m);
    }

    public Reference2CharArrayMap(Map<? extends K, ? extends Character> m) {
        this(m.size());
        this.putAll(m);
    }

    public Reference2CharArrayMap(Object[] key, char[] value, int size) {
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

    public Reference2CharMap.FastEntrySet<K> reference2CharEntrySet() {
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
    public char getChar(Object k) {
        Object[] key = this.key;
        int i = this.size;
        while (i-- != 0) {
            if (key[i] != k) continue;
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
        int i = this.size;
        while (i-- != 0) {
            this.key[i] = null;
        }
        this.size = 0;
    }

    @Override
    public boolean containsKey(Object k) {
        return this.findKey(k) != -1;
    }

    @Override
    public boolean containsValue(char v) {
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
    public char put(K k, char v) {
        int oldKey = this.findKey(k);
        if (oldKey != -1) {
            char oldValue = this.value[oldKey];
            this.value[oldKey] = v;
            return oldValue;
        }
        if (this.size == this.key.length) {
            Object[] newKey = new Object[this.size == 0 ? 2 : this.size * 2];
            char[] newValue = new char[this.size == 0 ? 2 : this.size * 2];
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
    public char removeChar(Object k) {
        int oldPos = this.findKey(k);
        if (oldPos == -1) {
            return this.defRetValue;
        }
        char oldValue = this.value[oldPos];
        int tail = this.size - oldPos - 1;
        System.arraycopy(this.key, oldPos + 1, this.key, oldPos, tail);
        System.arraycopy(this.value, oldPos + 1, this.value, oldPos, tail);
        --this.size;
        this.key[this.size] = null;
        return oldValue;
    }

    @Override
    public ReferenceSet<K> keySet() {
        return new AbstractReferenceSet<K>(){

            @Override
            public boolean contains(Object k) {
                return Reference2CharArrayMap.this.findKey(k) != -1;
            }

            @Override
            public boolean remove(Object k) {
                int oldPos = Reference2CharArrayMap.this.findKey(k);
                if (oldPos == -1) {
                    return false;
                }
                int tail = Reference2CharArrayMap.this.size - oldPos - 1;
                System.arraycopy(Reference2CharArrayMap.this.key, oldPos + 1, Reference2CharArrayMap.this.key, oldPos, tail);
                System.arraycopy(Reference2CharArrayMap.this.value, oldPos + 1, Reference2CharArrayMap.this.value, oldPos, tail);
                Reference2CharArrayMap.this.size--;
                return true;
            }

            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Reference2CharArrayMap.this.size;
                    }

                    @Override
                    public K next() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Reference2CharArrayMap.this.key[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Reference2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2CharArrayMap.this.key, this.pos, Reference2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2CharArrayMap.this.value, this.pos, Reference2CharArrayMap.this.value, this.pos - 1, tail);
                        Reference2CharArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Reference2CharArrayMap.this.size;
            }

            @Override
            public void clear() {
                Reference2CharArrayMap.this.clear();
            }
        };
    }

    @Override
    public CharCollection values() {
        return new AbstractCharCollection(){

            @Override
            public boolean contains(char v) {
                return Reference2CharArrayMap.this.containsValue(v);
            }

            @Override
            public CharIterator iterator() {
                return new CharIterator(){
                    int pos = 0;

                    @Override
                    public boolean hasNext() {
                        return this.pos < Reference2CharArrayMap.this.size;
                    }

                    @Override
                    public char nextChar() {
                        if (!this.hasNext()) {
                            throw new NoSuchElementException();
                        }
                        return Reference2CharArrayMap.this.value[this.pos++];
                    }

                    @Override
                    public void remove() {
                        if (this.pos == 0) {
                            throw new IllegalStateException();
                        }
                        int tail = Reference2CharArrayMap.this.size - this.pos;
                        System.arraycopy(Reference2CharArrayMap.this.key, this.pos, Reference2CharArrayMap.this.key, this.pos - 1, tail);
                        System.arraycopy(Reference2CharArrayMap.this.value, this.pos, Reference2CharArrayMap.this.value, this.pos - 1, tail);
                        Reference2CharArrayMap.this.size--;
                    }
                };
            }

            @Override
            public int size() {
                return Reference2CharArrayMap.this.size;
            }

            @Override
            public void clear() {
                Reference2CharArrayMap.this.clear();
            }
        };
    }

    public Reference2CharArrayMap<K> clone() {
        Reference2CharArrayMap c;
        try {
            c = (Reference2CharArrayMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.key = (Object[])this.key.clone();
        c.value = (char[])this.value.clone();
        return c;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        s.defaultWriteObject();
        for (int i = 0; i < this.size; ++i) {
            s.writeObject(this.key[i]);
            s.writeChar(this.value[i]);
        }
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.key = new Object[this.size];
        this.value = new char[this.size];
        for (int i = 0; i < this.size; ++i) {
            this.key[i] = s.readObject();
            this.value[i] = s.readChar();
        }
    }

    private final class EntrySet
    extends AbstractObjectSet<Reference2CharMap.Entry<K>>
    implements Reference2CharMap.FastEntrySet<K> {
        private EntrySet() {
        }

        @Override
        public ObjectIterator<Reference2CharMap.Entry<K>> iterator() {
            return new ObjectIterator<Reference2CharMap.Entry<K>>(){
                int curr = -1;
                int next = 0;

                @Override
                public boolean hasNext() {
                    return this.next < Reference2CharArrayMap.this.size;
                }

                @Override
                public Reference2CharMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    return new AbstractReference2CharMap.BasicEntry<Object>(Reference2CharArrayMap.this.key[this.curr], Reference2CharArrayMap.this.value[this.next++]);
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Reference2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2CharArrayMap.this.key, this.next + 1, Reference2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2CharArrayMap.this.value, this.next + 1, Reference2CharArrayMap.this.value, this.next, tail);
                    ((Reference2CharArrayMap)Reference2CharArrayMap.this).key[((Reference2CharArrayMap)Reference2CharArrayMap.this).size] = null;
                }
            };
        }

        @Override
        public ObjectIterator<Reference2CharMap.Entry<K>> fastIterator() {
            return new ObjectIterator<Reference2CharMap.Entry<K>>(){
                int next = 0;
                int curr = -1;
                final AbstractReference2CharMap.BasicEntry<K> entry = new AbstractReference2CharMap.BasicEntry();

                @Override
                public boolean hasNext() {
                    return this.next < Reference2CharArrayMap.this.size;
                }

                @Override
                public Reference2CharMap.Entry<K> next() {
                    if (!this.hasNext()) {
                        throw new NoSuchElementException();
                    }
                    this.curr = this.next;
                    this.entry.key = Reference2CharArrayMap.this.key[this.curr];
                    this.entry.value = Reference2CharArrayMap.this.value[this.next++];
                    return this.entry;
                }

                @Override
                public void remove() {
                    if (this.curr == -1) {
                        throw new IllegalStateException();
                    }
                    this.curr = -1;
                    int tail = Reference2CharArrayMap.this.size-- - this.next--;
                    System.arraycopy(Reference2CharArrayMap.this.key, this.next + 1, Reference2CharArrayMap.this.key, this.next, tail);
                    System.arraycopy(Reference2CharArrayMap.this.value, this.next + 1, Reference2CharArrayMap.this.value, this.next, tail);
                    ((Reference2CharArrayMap)Reference2CharArrayMap.this).key[((Reference2CharArrayMap)Reference2CharArrayMap.this).size] = null;
                }
            };
        }

        @Override
        public int size() {
            return Reference2CharArrayMap.this.size;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            Object k = e.getKey();
            return Reference2CharArrayMap.this.containsKey(k) && Reference2CharArrayMap.this.getChar(k) == ((Character)e.getValue()).charValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            if (e.getValue() == null || !(e.getValue() instanceof Character)) {
                return false;
            }
            Object k = e.getKey();
            char v = ((Character)e.getValue()).charValue();
            int oldPos = Reference2CharArrayMap.this.findKey(k);
            if (oldPos == -1 || v != Reference2CharArrayMap.this.value[oldPos]) {
                return false;
            }
            int tail = Reference2CharArrayMap.this.size - oldPos - 1;
            System.arraycopy(Reference2CharArrayMap.this.key, oldPos + 1, Reference2CharArrayMap.this.key, oldPos, tail);
            System.arraycopy(Reference2CharArrayMap.this.value, oldPos + 1, Reference2CharArrayMap.this.value, oldPos, tail);
            Reference2CharArrayMap.this.size--;
            ((Reference2CharArrayMap)Reference2CharArrayMap.this).key[((Reference2CharArrayMap)Reference2CharArrayMap.this).size] = null;
            return true;
        }
    }
}

