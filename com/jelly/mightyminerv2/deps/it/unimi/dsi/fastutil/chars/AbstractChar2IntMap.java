/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractChar2IntFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2IntMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractChar2IntMap
extends AbstractChar2IntFunction
implements Char2IntMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractChar2IntMap() {
    }

    @Override
    public boolean containsValue(int v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(char k) {
        Iterator i = this.char2IntEntrySet().iterator();
        while (i.hasNext()) {
            if (((Char2IntMap.Entry)i.next()).getCharKey() != k) continue;
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
                return AbstractChar2IntMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractChar2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractChar2IntMap.this.clear();
            }

            @Override
            public CharIterator iterator() {
                return new CharIterator(){
                    private final ObjectIterator<Char2IntMap.Entry> i;
                    {
                        this.i = Char2IntMaps.fastIterator(AbstractChar2IntMap.this);
                    }

                    @Override
                    public char nextChar() {
                        return ((Char2IntMap.Entry)this.i.next()).getCharKey();
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
                return AbstractChar2IntMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractChar2IntMap.this.size();
            }

            @Override
            public void clear() {
                AbstractChar2IntMap.this.clear();
            }

            @Override
            public IntIterator iterator() {
                return new IntIterator(){
                    private final ObjectIterator<Char2IntMap.Entry> i;
                    {
                        this.i = Char2IntMaps.fastIterator(AbstractChar2IntMap.this);
                    }

                    @Override
                    public int nextInt() {
                        return ((Char2IntMap.Entry)this.i.next()).getIntValue();
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
    public void putAll(Map<? extends Character, ? extends Integer> m) {
        if (m instanceof Char2IntMap) {
            ObjectIterator<Char2IntMap.Entry> i = Char2IntMaps.fastIterator((Char2IntMap)m);
            while (i.hasNext()) {
                Char2IntMap.Entry e = (Char2IntMap.Entry)i.next();
                this.put(e.getCharKey(), e.getIntValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Character, ? extends Integer>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Character, ? extends Integer> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Char2IntMap.Entry> i = Char2IntMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Char2IntMap.Entry)i.next()).hashCode();
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
        return this.char2IntEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Char2IntMap.Entry> i = Char2IntMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Char2IntMap.Entry e = (Char2IntMap.Entry)i.next();
            s.append(String.valueOf(e.getCharKey()));
            s.append("=>");
            s.append(String.valueOf(e.getIntValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Char2IntMap.Entry> {
        protected final Char2IntMap map;

        public BasicEntrySet(Char2IntMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Char2IntMap.Entry) {
                Char2IntMap.Entry e = (Char2IntMap.Entry)o;
                char k = e.getCharKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getIntValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            char k = ((Character)key).charValue();
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
            if (o instanceof Char2IntMap.Entry) {
                Char2IntMap.Entry e = (Char2IntMap.Entry)o;
                return this.map.remove(e.getCharKey(), e.getIntValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            char k = ((Character)key).charValue();
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
    implements Char2IntMap.Entry {
        protected char key;
        protected int value;

        public BasicEntry() {
        }

        public BasicEntry(Character key, Integer value) {
            this.key = key.charValue();
            this.value = value;
        }

        public BasicEntry(char key, int value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public char getCharKey() {
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
            if (o instanceof Char2IntMap.Entry) {
                Char2IntMap.Entry e = (Char2IntMap.Entry)o;
                return this.key == e.getCharKey() && this.value == e.getIntValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Integer)) {
                return false;
            }
            return this.key == ((Character)key).charValue() && this.value == (Integer)value;
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

