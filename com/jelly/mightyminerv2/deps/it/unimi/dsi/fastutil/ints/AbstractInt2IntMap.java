/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2IntFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2IntMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractInt2IntMap
extends AbstractInt2IntFunction
implements Int2IntMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractInt2IntMap() {
    }

    @Override
    public boolean containsValue(int v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(int k) {
        Iterator i = this.int2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (((Int2IntMap.Entry)i.next()).getIntKey() != k) continue;
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
                return AbstractInt2IntMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractInt2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractInt2IntMap.this.clear();
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator(){
                    private final ObjectIterator<Int2IntMap.Entry> i;
                    {
                        this.i = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Int2IntMap.Entry)this.i.next()).getIntKey();
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
                return AbstractInt2IntMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractInt2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractInt2IntMap.this.clear();
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator(){
                    private final ObjectIterator<Int2IntMap.Entry> i;
                    {
                        this.i = Int2IntMaps.fastIterator(AbstractInt2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Int2IntMap.Entry)this.i.next()).getIntValue();
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
    public void putAll(Map<? extends Integer, ? extends Integer> m) {
        if (m instanceof Int2IntMap) {
            ObjectIterator<Int2IntMap.Entry> i = Int2IntMaps.fastIterator((Int2IntMap)m);
            while (i.hasNext()) {
                Int2IntMap.Entry e = (Int2IntMap.Entry)i.next();
                this.put(e.getIntKey(), e.getIntValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Integer, ? extends Integer>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Integer, ? extends Integer> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Int2IntMap.Entry> i = Int2IntMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Int2IntMap.Entry)i.next()).hashCode();
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
        return this.int2IntEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Int2IntMap.Entry> i = Int2IntMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Int2IntMap.Entry e = (Int2IntMap.Entry)i.next();
            s.append(String.valueOf(e.getIntKey()));
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Int2IntMap.Entry> {
        protected final Int2IntMap map;

        public BasicEntrySet(Int2IntMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Int2IntMap.Entry) {
                Int2IntMap.Entry e = (Int2IntMap.Entry)o;
                int k = e.getIntKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getIntValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k = (Integer)key;
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
            if (o instanceof Int2IntMap.Entry) {
                Int2IntMap.Entry e = (Int2IntMap.Entry)o;
                return this.map.remove(e.getIntKey(), e.getIntValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            int k = (Integer)key;
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
    implements Int2IntMap.Entry {
        protected int key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(Integer key, Integer value) {
            this.key = key;
            this.value = value;
        }

        public BasicEntry(int key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public int getIntKey() {
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
            if (o instanceof Int2IntMap.Entry) {
                Int2IntMap.Entry e = (Int2IntMap.Entry)o;
                return this.key == e.getIntKey() && this.value == e.getIntValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Integer)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.key == (Integer)key && this.value == (Integer)value;
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

