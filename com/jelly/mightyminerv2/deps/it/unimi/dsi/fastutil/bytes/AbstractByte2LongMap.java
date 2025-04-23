/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByte2LongFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.Byte2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.Byte2LongMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractByte2LongMap
extends AbstractByte2LongFunction
implements Byte2LongMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractByte2LongMap() {
    }

    @Override
    public boolean containsValue(long v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(byte k) {
        Iterator i = this.byte2LongEntrySet().iterator();
        while (i.hasNext()) {
            if (((Byte2LongMap.Entry)i.next()).getByteKey() != k) continue;
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
                return AbstractByte2LongMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractByte2LongMap.this.size();
            }

            @Override
            public void clear() {
                AbstractByte2LongMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Byte2LongMap.Entry> i;
                    {
                        this.i = Byte2LongMaps.fastIterator(AbstractByte2LongMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Byte2LongMap.Entry)this.i.next()).getByteKey();
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
    public LongCollection values() {
        return new AbstractLongCollection(){

            @Override
            public boolean contains(long k) {
                return AbstractByte2LongMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractByte2LongMap.this.size();
            }

            @Override
            public void clear() {
                AbstractByte2LongMap.this.clear();
            }

            @Override
            public LongIterator iterator() {
                return new LongIterator(){
                    private final ObjectIterator<Byte2LongMap.Entry> i;
                    {
                        this.i = Byte2LongMaps.fastIterator(AbstractByte2LongMap.this);
                    }

                    @Override
                    public long nextLong() {
                        return ((Byte2LongMap.Entry)this.i.next()).getLongValue();
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
    public void putAll(Map<? extends Byte, ? extends Long> m) {
        if (m instanceof Byte2LongMap) {
            ObjectIterator<Byte2LongMap.Entry> i = Byte2LongMaps.fastIterator((Byte2LongMap)m);
            while (i.hasNext()) {
                Byte2LongMap.Entry e = (Byte2LongMap.Entry)i.next();
                this.put(e.getByteKey(), e.getLongValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Byte, ? extends Long>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Byte, ? extends Long> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Byte2LongMap.Entry> i = Byte2LongMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Byte2LongMap.Entry)i.next()).hashCode();
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
        return this.byte2LongEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Byte2LongMap.Entry> i = Byte2LongMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Byte2LongMap.Entry e = (Byte2LongMap.Entry)i.next();
            s.append(String.valueOf(e.getByteKey()));
            s.append("=>");
            s.append(String.valueOf(e.getLongValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Byte2LongMap.Entry> {
        protected final Byte2LongMap map;

        public BasicEntrySet(Byte2LongMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Byte2LongMap.Entry) {
                Byte2LongMap.Entry e = (Byte2LongMap.Entry)o;
                byte k = e.getByteKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getLongValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            byte k = (Byte)key;
            Object value = e.getValue();
            if (value == null || !(value instanceof Long)) {
                return false;
            }
            return this.map.containsKey(k) && this.map.get(k) == ((Long)value).longValue();
        }

        @Override
        public boolean remove(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Byte2LongMap.Entry) {
                Byte2LongMap.Entry e = (Byte2LongMap.Entry)o;
                return this.map.remove(e.getByteKey(), e.getLongValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            byte k = (Byte)key;
            Object value = e.getValue();
            if (value == null || !(value instanceof Long)) {
                return false;
            }
            long v = (Long)value;
            return this.map.remove(k, v);
        }

        @Override
        public int size() {
            return this.map.size();
        }
    }

    public static class BasicEntry
    implements Byte2LongMap.Entry {
        protected byte key;
        protected long value;

        public BasicEntry() {
        }

        public BasicEntry(Byte key, Long value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(byte key, long value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public byte getByteKey() {
            return this.key;
        }

        @Override
        public long getLongValue() {
            return this.value;
        }

        @Override
        public long setValue(long value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Byte2LongMap.Entry) {
                Byte2LongMap.Entry e = (Byte2LongMap.Entry)o;
                return this.key == e.getByteKey() && this.value == e.getLongValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Byte)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Long)) {
                return false;
            }
            return this.key == (Byte)key && this.value == (Long)value;
        }

        @Override
        public int hashCode() {
            return this.key ^ HashCommon.long2int(this.value);
        }

        public String toString() {
            return this.key + "->" + this.value;
        }
    }
}

