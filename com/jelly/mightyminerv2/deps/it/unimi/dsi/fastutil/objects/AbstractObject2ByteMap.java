/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObject2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Object2ByteMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

public abstract class AbstractObject2ByteMap<K>
extends AbstractObject2ByteFunction<K>
implements Object2ByteMap<K>,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractObject2ByteMap() {
    }

    @Override
    public boolean containsValue(byte v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(Object k) {
        Iterator i = this.object2ByteEntrySet().iterator();
        while (i.hasNext()) {
            if (((Object2ByteMap.Entry)i.next()).getKey() != k) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public ObjectSet<K> keySet() {
        return new AbstractObjectSet<K>(){

            @Override
            public boolean contains(Object k) {
                return AbstractObject2ByteMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractObject2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractObject2ByteMap.this.clear();
            }

            @Override
            public ObjectIterator<K> iterator() {
                return new ObjectIterator<K>(){
                    private final ObjectIterator<Object2ByteMap.Entry<K>> i;
                    {
                        this.i = Object2ByteMaps.fastIterator(AbstractObject2ByteMap.this);
                    }

                    @Override
                    public K next() {
                        return ((Object2ByteMap.Entry)this.i.next()).getKey();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }

                    @Override
                    public void remove() {
                        this.i.remove();
                    }
                };
            }
        };
    }

    @Override
    public ByteCollection values() {
        return new AbstractByteCollection(){

            @Override
            public boolean contains(byte k) {
                return AbstractObject2ByteMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractObject2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractObject2ByteMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Object2ByteMap.Entry<K>> i;
                    {
                        this.i = Object2ByteMaps.fastIterator(AbstractObject2ByteMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Object2ByteMap.Entry)this.i.next()).getByteValue();
                    }

                    @Override
                    public boolean hasNext() {
                        return this.i.hasNext();
                    }
                };
            }
        };
    }

    @Override
    public void putAll(Map<? extends K, ? extends Byte> m) {
        if (m instanceof Object2ByteMap) {
            ObjectIterator i = Object2ByteMaps.fastIterator((Object2ByteMap)m);
            while (i.hasNext()) {
                Object2ByteMap.Entry e = (Object2ByteMap.Entry)i.next();
                this.put(e.getKey(), e.getByteValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<K, Byte>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<K, Byte> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator i = Object2ByteMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Object2ByteMap.Entry)i.next()).hashCode();
        }
        return h;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Map)) {
            return false;
        }
        Map m = (Map)o;
        if (m.size() != this.size()) {
            return false;
        }
        return this.object2ByteEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator i = Object2ByteMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Object2ByteMap.Entry e = (Object2ByteMap.Entry)i.next();
            if (this == e.getKey()) {
                s.append("(this map)");
            } else {
                s.append(String.valueOf(e.getKey()));
            }
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet<K>
    extends AbstractObjectSet<Object2ByteMap.Entry<K>> {
        protected final Object2ByteMap<K> map;

        public BasicEntrySet(Object2ByteMap<K> map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Object2ByteMap.Entry) {
                Object2ByteMap.Entry e = (Object2ByteMap.Entry)o;
                Object k = e.getKey();
                return this.map.containsKey(k) && this.map.getByte(k) == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object k = e.getKey();
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return this.map.containsKey(k) && this.map.getByte(k) == ((Byte)value).byteValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Object2ByteMap.Entry) {
                Object2ByteMap.Entry e = (Object2ByteMap.Entry)o;
                return this.map.remove(e.getKey(), e.getByteValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object k = e.getKey();
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            byte v = (Byte)value;
            return this.map.remove(k, v);
        }

        @Override
        public int size() {
            return this.map.size();
        }
    }

    public static class BasicEntry<K>
    implements Object2ByteMap.Entry<K> {
        protected K key;
        protected byte value;

        public BasicEntry() {
        }

        public BasicEntry(K key, Byte value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(K key, byte value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public K getKey() {
            return this.key;
        }

        @Override
        public byte getByteValue() {
            return this.value;
        }

        @Override
        public byte setValue(byte value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Object2ByteMap.Entry) {
                Object2ByteMap.Entry e = (Object2ByteMap.Entry)o;
                return Objects.equals(this.key, e.getKey()) && this.value == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return Objects.equals(this.key, key) && this.value == (Byte)value;
        }

        @Override
        public int hashCode() {
            return (this.key == null ? 0 : this.key.hashCode()) ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

