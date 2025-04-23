/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.AbstractByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes.ByteIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractChar2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2ByteMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2ByteMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

public abstract class AbstractChar2ByteMap
extends AbstractChar2ByteFunction
implements Char2ByteMap,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;

    protected AbstractChar2ByteMap() {
    }

    @Override
    public boolean containsValue(byte v) {
        return this.values().contains(v);
    }

    @Override
    public boolean containsKey(char k) {
        Iterator i = this.char2ByteEntrySet().iterator();
        while (i.hasNext()) {
            if (((Char2ByteMap.Entry)i.next()).getCharKey() != k) continue;
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
                return AbstractChar2ByteMap.this.containsKey(k);
            }

            @Override
            public int size() {
                return AbstractChar2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractChar2ByteMap.this.clear();
            }

            @Override
            public CharIterator iterator() {
                return new CharIterator(){
                    private final ObjectIterator<Char2ByteMap.Entry> i;
                    {
                        this.i = Char2ByteMaps.fastIterator(AbstractChar2ByteMap.this);
                    }

                    @Override
                    public char nextChar() {
                        return ((Char2ByteMap.Entry)this.i.next()).getCharKey();
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
                return AbstractChar2ByteMap.this.containsValue(k);
            }

            @Override
            public int size() {
                return AbstractChar2ByteMap.this.size();
            }

            @Override
            public void clear() {
                AbstractChar2ByteMap.this.clear();
            }

            @Override
            public ByteIterator iterator() {
                return new ByteIterator(){
                    private final ObjectIterator<Char2ByteMap.Entry> i;
                    {
                        this.i = Char2ByteMaps.fastIterator(AbstractChar2ByteMap.this);
                    }

                    @Override
                    public byte nextByte() {
                        return ((Char2ByteMap.Entry)this.i.next()).getByteValue();
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
    public void putAll(Map<? extends Character, ? extends Byte> m) {
        if (m instanceof Char2ByteMap) {
            ObjectIterator<Char2ByteMap.Entry> i = Char2ByteMaps.fastIterator((Char2ByteMap)m);
            while (i.hasNext()) {
                Char2ByteMap.Entry e = (Char2ByteMap.Entry)i.next();
                this.put(e.getCharKey(), e.getByteValue());
            }
        } else {
            int n = m.size();
            Iterator<Map.Entry<? extends Character, ? extends Byte>> i = m.entrySet().iterator();
            while (n-- != 0) {
                Map.Entry<? extends Character, ? extends Byte> e = i.next();
                this.put(e.getKey(), e.getValue());
            }
        }
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        ObjectIterator<Char2ByteMap.Entry> i = Char2ByteMaps.fastIterator(this);
        while (n-- != 0) {
            h += ((Char2ByteMap.Entry)i.next()).hashCode();
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
        return this.char2ByteEntrySet().containsAll(m.entrySet());
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        ObjectIterator<Char2ByteMap.Entry> i = Char2ByteMaps.fastIterator(this);
        int n = this.size();
        boolean first = true;
        s.append("{");
        while (n-- != 0) {
            if (first) {
                first = false;
            } else {
                s.append(", ");
            }
            Char2ByteMap.Entry e = (Char2ByteMap.Entry)i.next();
            s.append(String.valueOf(e.getCharKey()));
            s.append("=>");
            s.append(String.valueOf(e.getByteValue()));
        }
        s.append("}");
        return s.toString();
    }

    public static abstract class BasicEntrySet
    extends AbstractObjectSet<Char2ByteMap.Entry> {
        protected final Char2ByteMap map;

        public BasicEntrySet(Char2ByteMap map) {
            this.map = map;
        }

        @Override
        public boolean contains(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            if (o instanceof Char2ByteMap.Entry) {
                Char2ByteMap.Entry e = (Char2ByteMap.Entry)o;
                char k = e.getCharKey();
                return this.map.containsKey(k) && this.map.get(k) == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            char k = ((Character)key).charValue();
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
            if (o instanceof Char2ByteMap.Entry) {
                Char2ByteMap.Entry e = (Char2ByteMap.Entry)o;
                return this.map.remove(e.getCharKey(), e.getByteValue());
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            char k = ((Character)key).charValue();
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
    implements Char2ByteMap.Entry {
        protected char key;
        protected byte value;

        public BasicEntry() {
        }

        public BasicEntry(Character key, Byte value) {
            this.key = key.charValue();
            this.value = value;
        }

        public BasicEntry(char key, byte value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public char getCharKey() {
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
            if (o instanceof Char2ByteMap.Entry) {
                Char2ByteMap.Entry e = (Char2ByteMap.Entry)o;
                return this.key == e.getCharKey() && this.value == e.getByteValue();
            }
            Map.Entry e = (Map.Entry)o;
            Object key = e.getKey();
            if (key == null || !(key instanceof Character)) {
                return false;
            }
            Object value = e.getValue();
            if (value == null || !(value instanceof Byte)) {
                return false;
            }
            return this.key == ((Character)key).charValue() && this.value == (Byte)value;
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

