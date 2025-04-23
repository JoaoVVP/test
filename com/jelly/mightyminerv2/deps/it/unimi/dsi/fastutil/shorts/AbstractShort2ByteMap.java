/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShort2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShortSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ByteMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortSet;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractShort2ByteMap
extends AbstractShort2ByteFunction
implements Short2ByteMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractShort2ByteMap() {
    }

    @Override
    public boolean containsValue(byte v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(short k) {
        Iterator i = this.short2ByteEntrySet().iterator();
        while (i.hasNext()) {
            if (((Short2ByteMap.Entry)i.next()).getShortKey() != k) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public ShortSet keySet() {
        return new AbstractShortSet(){

            @Override
            public boolean contains(short k) {
                return AbstractShort2ByteMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractShort2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractShort2ByteMap.this.clear();
            }

            @Override
            public ShortIterator iterator() {
                return new ShortIterator(){
                    private final ObjectIterator<Short2ByteMap.Entry> i;
                    {
                        this.i = Short2ByteMaps.fastIterator(AbstractShort2ByteMap.this);
                    }

                    @Override
                    public short nextShort() {
                        return ((Short2ByteMap.Entry)this.i.next()).getShortKey();
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
                return AbstractShort2ByteMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractShort2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractShort2ByteMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Short2ByteMap.Entry> i;
                    {
                        this.i = Short2ByteMaps.fastIterator(AbstractShort2ByteMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Short2ByteMap.Entry)this.i.next()).getByteValue();
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
    public void putAll(Map<? extends Short, ? extends Byte> m) {
        if (m instanceof Short2ByteMap) {
            ObjectIterator<Short2ByteMap.Entry> i = Short2ByteMaps.fastIterator((Short2ByteMap)m);
            while (i.hasNext()) {
                Short2ByteMap.Entry e = (Short2ByteMap.Entry)i.next();
                this.put(e.getShortKey(), e.getByteValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Short, ? extends Byte>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Short, ? extends Byte> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Short2ByteMap.Entry> i = Short2ByteMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Short2ByteMap.Entry)i.next()).hashCode();
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
        return this.short2ByteEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Short2ByteMap.Entry> i = Short2ByteMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Short2ByteMap.Entry e = (Short2ByteMap.Entry)i.next();
            s.append(String.valueOf(e.getShortKey()));
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Short2ByteMap.Entry> {
        protected final Short2ByteMap map;

        public BasicEntrySet(Short2ByteMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Short2ByteMap.Entry) {
                Short2ByteMap.Entry e = (Short2ByteMap.Entry)o;
                short k = e.getShortKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            short k = (Short)key;
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
            if (o instanceof Short2ByteMap.Entry) {
                Short2ByteMap.Entry e = (Short2ByteMap.Entry)o;
                return this.map.remove(e.getShortKey(), e.getByteValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            short k = (Short)key;
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
    implements Short2ByteMap.Entry {
        protected short key;
        protected byte value;

        public BasicEntry() {
        }

        public BasicEntry(Short key, Byte value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(short key, byte value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public short getShortKey() {
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
            if (o instanceof Short2ByteMap.Entry) {
                Short2ByteMap.Entry e = (Short2ByteMap.Entry)o;
                return this.key == e.getShortKey() && this.value == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Short)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return this.key == (Short)key && this.value == (Byte)value;
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

