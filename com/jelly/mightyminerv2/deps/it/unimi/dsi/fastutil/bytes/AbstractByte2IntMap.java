/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByte2IntFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.Byte2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.Byte2IntMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractByte2IntMap
extends AbstractByte2IntFunction
implements Byte2IntMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractByte2IntMap() {
    }

    @Override
    public boolean containsValue(int v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(byte k) {
        Iterator i = this.byte2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (((Byte2IntMap.Entry)i.next()).getByteKey() != k) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public ByteSet keySet() {
        return new AbstractByteSet(){

            @Override
            public boolean contains(byte k) {
                return AbstractByte2IntMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractByte2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractByte2IntMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Byte2IntMap.Entry> i;
                    {
                        this.i = Byte2IntMaps.fastIterator(AbstractByte2IntMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Byte2IntMap.Entry)this.i.next()).getByteKey();
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
    public IntCollection values() {
        return new AbstractIntCollection(){

            @Override
            public boolean contains(int k) {
                return AbstractByte2IntMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractByte2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractByte2IntMap.this.clear();
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator(){
                    private final ObjectIterator<Byte2IntMap.Entry> i;
                    {
                        this.i = Byte2IntMaps.fastIterator(AbstractByte2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Byte2IntMap.Entry)this.i.next()).getIntValue();
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
    public void putAll(Map<? extends Byte, ? extends Integer> m) {
        if (m instanceof Byte2IntMap) {
            ObjectIterator<Byte2IntMap.Entry> i = Byte2IntMaps.fastIterator((Byte2IntMap)m);
            while (i.hasNext()) {
                Byte2IntMap.Entry e = (Byte2IntMap.Entry)i.next();
                this.put(e.getByteKey(), e.getIntValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Byte, ? extends Integer>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Byte, ? extends Integer> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Byte2IntMap.Entry> i = Byte2IntMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Byte2IntMap.Entry)i.next()).hashCode();
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
        return this.byte2IntEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Byte2IntMap.Entry> i = Byte2IntMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Byte2IntMap.Entry e = (Byte2IntMap.Entry)i.next();
            s.append(String.valueOf(e.getByteKey()));
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Byte2IntMap.Entry> {
        protected final Byte2IntMap map;

        public BasicEntrySet(Byte2IntMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Byte2IntMap.Entry) {
                Byte2IntMap.Entry e = (Byte2IntMap.Entry)o;
                byte k = e.getByteKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getIntValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            byte k = (Byte)key;
            Object value = e.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.map.containsKey(k) && this.map.get(k) == ((Integer)value).intValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Byte2IntMap.Entry) {
                Byte2IntMap.Entry e = (Byte2IntMap.Entry)o;
                return this.map.remove(e.getByteKey(), e.getIntValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            byte k = (Byte)key;
            Object value = e.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            int v = (Integer)value;
            return this.map.remove(k, v);
        }

        @Override
        public int size() {
            return this.map.size();
        }
    }

    public static class BasicEntry
    implements Byte2IntMap.Entry {
        protected byte key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(Byte key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(byte key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public byte getByteKey() {
            return this.key;
        }

        @Override
        public int getIntValue() {
            return this.value;
        }

        @Override
        public int setValue(int value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Byte2IntMap.Entry) {
                Byte2IntMap.Entry e = (Byte2IntMap.Entry)o;
                return this.key == e.getByteKey() && this.value == e.getIntValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.key == (Byte)key && this.value == (Integer)value;
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

