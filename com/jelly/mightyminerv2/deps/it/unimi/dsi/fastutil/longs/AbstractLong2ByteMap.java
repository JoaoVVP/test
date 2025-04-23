/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLong2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ByteMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractLong2ByteMap
extends AbstractLong2ByteFunction
implements Long2ByteMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractLong2ByteMap() {
    }

    @Override
    public boolean containsValue(byte v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(long k) {
        Iterator i = this.long2ByteEntrySet().iterator();
        while (i.hasNext()) {
            if (((Long2ByteMap.Entry)i.next()).getLongKey() != k) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public LongSet keySet() {
        return new AbstractLongSet(){

            @Override
            public boolean contains(long k) {
                return AbstractLong2ByteMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractLong2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractLong2ByteMap.this.clear();
            }

            @Override
            public LongIterator iterator() {
                return new LongIterator(){
                    private final ObjectIterator<Long2ByteMap.Entry> i;
                    {
                        this.i = Long2ByteMaps.fastIterator(AbstractLong2ByteMap.this);
                    }

                    @Override
                    public long nextLong() {
                        return ((Long2ByteMap.Entry)this.i.next()).getLongKey();
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
                return AbstractLong2ByteMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractLong2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractLong2ByteMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Long2ByteMap.Entry> i;
                    {
                        this.i = Long2ByteMaps.fastIterator(AbstractLong2ByteMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Long2ByteMap.Entry)this.i.next()).getByteValue();
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
    public void putAll(Map<? extends Long, ? extends Byte> m) {
        if (m instanceof Long2ByteMap) {
            ObjectIterator<Long2ByteMap.Entry> i = Long2ByteMaps.fastIterator((Long2ByteMap)m);
            while (i.hasNext()) {
                Long2ByteMap.Entry e = (Long2ByteMap.Entry)i.next();
                this.put(e.getLongKey(), e.getByteValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Long, ? extends Byte>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Long, ? extends Byte> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Long2ByteMap.Entry> i = Long2ByteMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Long2ByteMap.Entry)i.next()).hashCode();
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
        return this.long2ByteEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Long2ByteMap.Entry> i = Long2ByteMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Long2ByteMap.Entry e = (Long2ByteMap.Entry)i.next();
            s.append(String.valueOf(e.getLongKey()));
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Long2ByteMap.Entry> {
        protected final Long2ByteMap map;

        public BasicEntrySet(Long2ByteMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Long2ByteMap.Entry) {
                Long2ByteMap.Entry e = (Long2ByteMap.Entry)o;
                long k = e.getLongKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            long k = (Long)key;
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
            if (o instanceof Long2ByteMap.Entry) {
                Long2ByteMap.Entry e = (Long2ByteMap.Entry)o;
                return this.map.remove(e.getLongKey(), e.getByteValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            long k = (Long)key;
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
    implements Long2ByteMap.Entry {
        protected long key;
        protected byte value;

        public BasicEntry() {
        }

        public BasicEntry(Long key, Byte value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(long key, byte value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public long getLongKey() {
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
            if (o instanceof Long2ByteMap.Entry) {
                Long2ByteMap.Entry e = (Long2ByteMap.Entry)o;
                return this.key == e.getLongKey() && this.value == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Long)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return this.key == (Long)key && this.value == (Byte)value;
        }

        @Override
        public int hashCode() {
            return HashCommon.long2int(this.key) ^ this.value;
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

