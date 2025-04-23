/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ByteMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractInt2ByteMap
extends AbstractInt2ByteFunction
implements Int2ByteMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractInt2ByteMap() {
    }

    @Override
    public boolean containsValue(byte v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(int k) {
        Iterator i = this.int2ByteEntrySet().iterator();
        while (i.hasNext()) {
            if (((Int2ByteMap.Entry)i.next()).getIntKey() != k) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public IntSet keySet() {
        return new AbstractIntSet(){

            @Override
            public boolean contains(int k) {
                return AbstractInt2ByteMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractInt2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractInt2ByteMap.this.clear();
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator(){
                    private final ObjectIterator<Int2ByteMap.Entry> i;
                    {
                        this.i = Int2ByteMaps.fastIterator(AbstractInt2ByteMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Int2ByteMap.Entry)this.i.next()).getIntKey();
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
                return AbstractInt2ByteMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractInt2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractInt2ByteMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Int2ByteMap.Entry> i;
                    {
                        this.i = Int2ByteMaps.fastIterator(AbstractInt2ByteMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Int2ByteMap.Entry)this.i.next()).getByteValue();
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
    public void putAll(Map<? extends Integer, ? extends Byte> m) {
        if (m instanceof Int2ByteMap) {
            ObjectIterator<Int2ByteMap.Entry> i = Int2ByteMaps.fastIterator((Int2ByteMap)m);
            while (i.hasNext()) {
                Int2ByteMap.Entry e = (Int2ByteMap.Entry)i.next();
                this.put(e.getIntKey(), e.getByteValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Integer, ? extends Byte>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Integer, ? extends Byte> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Int2ByteMap.Entry> i = Int2ByteMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Int2ByteMap.Entry)i.next()).hashCode();
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
        return this.int2ByteEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Int2ByteMap.Entry> i = Int2ByteMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Int2ByteMap.Entry e = (Int2ByteMap.Entry)i.next();
            s.append(String.valueOf(e.getIntKey()));
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Int2ByteMap.Entry> {
        protected final Int2ByteMap map;

        public BasicEntrySet(Int2ByteMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Int2ByteMap.Entry) {
                Int2ByteMap.Entry e = (Int2ByteMap.Entry)o;
                int k = e.getIntKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k = (Integer)key;
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return this.map.containsKey(k) && this.map.get(k) == ((Byte)value).byteValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Int2ByteMap.Entry) {
                Int2ByteMap.Entry e = (Int2ByteMap.Entry)o;
                return this.map.remove(e.getIntKey(), e.getByteValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k = (Integer)key;
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

    public static class BasicEntry
    implements Int2ByteMap.Entry {
        protected int key;
        protected byte value;

        public BasicEntry() {
        }

        public BasicEntry(Integer key, Byte value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(int key, byte value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int getIntKey() {
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
            if (o instanceof Int2ByteMap.Entry) {
                Int2ByteMap.Entry e = (Int2ByteMap.Entry)o;
                return this.key == e.getIntKey() && this.value == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return this.key == (Integer)key && this.value == (Byte)value;
        }

        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

