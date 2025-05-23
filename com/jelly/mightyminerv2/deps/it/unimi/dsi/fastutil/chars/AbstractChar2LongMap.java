/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractChar2LongFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2LongMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractChar2LongMap
extends AbstractChar2LongFunction
implements Char2LongMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractChar2LongMap() {
    }

    @Override
    public boolean containsValue(long v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(char k) {
        Iterator i = this.char2LongEntrySet().iterator();
        while (i.hasNext()) {
            if (((Char2LongMap.Entry)i.next()).getCharKey() != k) continue;
            return true;
        }
        return false;
    }

    @Override
    public boolean isEmpty() {
        return this.size() == 0;
    }

    @Override
    public CharSet keySet() {
        return new AbstractCharSet(){

            @Override
            public boolean contains(char k) {
                return AbstractChar2LongMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractChar2LongMap.this.size();
            }

            @Override
            public void clear() {
                AbstractChar2LongMap.this.clear();
            }

            @Override
            public CharIterator iterator() {
                return new CharIterator(){
                    private final ObjectIterator<Char2LongMap.Entry> i;
                    {
                        this.i = Char2LongMaps.fastIterator(AbstractChar2LongMap.this);
                    }

                    @Override
                    public char nextChar() {
                        return ((Char2LongMap.Entry)this.i.next()).getCharKey();
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
                return AbstractChar2LongMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractChar2LongMap.this.size();
            }

            @Override
            public void clear() {
                AbstractChar2LongMap.this.clear();
            }

            @Override
            public LongIterator iterator() {
                return new LongIterator(){
                    private final ObjectIterator<Char2LongMap.Entry> i;
                    {
                        this.i = Char2LongMaps.fastIterator(AbstractChar2LongMap.this);
                    }

                    @Override
                    public long nextLong() {
                        return ((Char2LongMap.Entry)this.i.next()).getLongValue();
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
    public void putAll(Map<? extends Character, ? extends Long> m) {
        if (m instanceof Char2LongMap) {
            ObjectIterator<Char2LongMap.Entry> i = Char2LongMaps.fastIterator((Char2LongMap)m);
            while (i.hasNext()) {
                Char2LongMap.Entry e = (Char2LongMap.Entry)i.next();
                this.put(e.getCharKey(), e.getLongValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Character, ? extends Long>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Character, ? extends Long> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Char2LongMap.Entry> i = Char2LongMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Char2LongMap.Entry)i.next()).hashCode();
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
        return this.char2LongEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Char2LongMap.Entry> i = Char2LongMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Char2LongMap.Entry e = (Char2LongMap.Entry)i.next();
            s.append(String.valueOf(e.getCharKey()));
            s.append("=>");
            s.append(String.valueOf(e.getLongValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Char2LongMap.Entry> {
        protected final Char2LongMap map;

        public BasicEntrySet(Char2LongMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Char2LongMap.Entry) {
                Char2LongMap.Entry e = (Char2LongMap.Entry)o;
                char k = e.getCharKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getLongValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            char k = ((Character)key).charValue();
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
            if (o instanceof Char2LongMap.Entry) {
                Char2LongMap.Entry e = (Char2LongMap.Entry)o;
                return this.map.remove(e.getCharKey(), e.getLongValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            char k = ((Character)key).charValue();
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
    implements Char2LongMap.Entry {
        protected char key;
        protected long value;

        public BasicEntry() {
        }

        public BasicEntry(Character key, Long value) {
            this.key = key.charValue();
            this.value = value;
        }

        public BasicEntry(char key, long value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public char getCharKey() {
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
            if (o instanceof Char2LongMap.Entry) {
                Char2LongMap.Entry e = (Char2LongMap.Entry)o;
                return this.key == e.getCharKey() && this.value == e.getLongValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Long)) {
                return false;
            }
            return this.key == ((Character)key).charValue() && this.value == (Long)value;
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

