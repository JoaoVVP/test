/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharCollections;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSets;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReference2CharMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSets;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharFunctions;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSets;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.ToIntFunction;

public final class Reference2CharMaps {
    public static final EmptyMap EMPTY_MAP = new EmptyMap();

    private Reference2CharMaps() {
    }

    public static <K> ObjectIterator<Reference2CharMap.Entry<K>> fastIterator(Reference2CharMap<K> map) {
        ObjectSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        return entries instanceof Reference2CharMap.FastEntrySet ? ((Reference2CharMap.FastEntrySet)entries).fastIterator() : entries.iterator();
    }

    public static <K> void fastForEach(Reference2CharMap<K> map, Consumer<? super Reference2CharMap.Entry<K>> consumer) {
        ObjectSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        if (entries instanceof Reference2CharMap.FastEntrySet) {
            ((Reference2CharMap.FastEntrySet)entries).fastForEach(consumer);
        } else {
            entries.forEach(consumer);
        }
    }

    public static <K> ObjectIterable<Reference2CharMap.Entry<K>> fastIterable(Reference2CharMap<K> map) {
        final ObjectSet<Reference2CharMap.Entry<K>> entries = map.reference2CharEntrySet();
        return entries instanceof Reference2CharMap.FastEntrySet ? new ObjectIterable<Reference2CharMap.Entry<K>>(){

            @Override
            public ObjectIterator<Reference2CharMap.Entry<K>> iterator() {
                return ((Reference2CharMap.FastEntrySet)entries).fastIterator();
            }

            @Override
            public void forEach(Consumer<? super Reference2CharMap.Entry<K>> consumer) {
                ((Reference2CharMap.FastEntrySet)entries).fastForEach(consumer);
            }
        } : entries;
    }

    public static <K> Reference2CharMap<K> emptyMap() {
        return EMPTY_MAP;
    }

    public static <K> Reference2CharMap<K> singleton(K key, char value) {
        return new Singleton<K>(key, value);
    }

    public static <K> Reference2CharMap<K> singleton(K key, Character value) {
        return new Singleton<K>(key, value.charValue());
    }

    public static <K> Reference2CharMap<K> synchronize(Reference2CharMap<K> m) {
        return new SynchronizedMap<K>(m);
    }

    public static <K> Reference2CharMap<K> synchronize(Reference2CharMap<K> m, Object sync) {
        return new SynchronizedMap<K>(m, sync);
    }

    public static <K> Reference2CharMap<K> unmodifiable(Reference2CharMap<K> m) {
        return new UnmodifiableMap<K>(m);
    }

    public static class UnmodifiableMap<K>
    extends Reference2CharFunctions.UnmodifiableFunction<K>
    implements Reference2CharMap<K>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharMap<K> map;
        protected transient ObjectSet<Reference2CharMap.Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient CharCollection values;

        protected UnmodifiableMap(Reference2CharMap<K> m) {
            super(m);
            this.map = m;
        }

        @Override
        public boolean containsValue(char v) {
            return this.map.containsValue(v);
        }

        @Override
        @Deprecated
        public boolean containsValue(Object ov) {
            return this.map.containsValue(ov);
        }

        @Override
        public void putAll(Map<? extends K, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.unmodifiable(this.map.reference2CharEntrySet());
            }
            return this.entries;
        }

        @Override
        @Deprecated
        public ObjectSet<Map.Entry<K, Character>> entrySet() {
            return this.reference2CharEntrySet();
        }

        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.unmodifiable(this.map.keySet());
            }
            return this.keys;
        }

        @Override
        public CharCollection values() {
            if (this.values == null) {
                return CharCollections.unmodifiable(this.map.values());
            }
            return this.values;
        }

        @Override
        public boolean isEmpty() {
            return this.map.isEmpty();
        }

        @Override
        public int hashCode() {
            return this.map.hashCode();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.map.equals(o);
        }

        @Override
        public char getOrDefault(Object key, char defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }

        @Override
        public void forEach(BiConsumer<? super K, ? super Character> action) {
            this.map.forEach(action);
        }

        @Override
        public void replaceAll(BiFunction<? super K, ? super Character, ? extends Character> function) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char putIfAbsent(K key, char value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean remove(Object key, char value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char replace(K key, char value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean replace(K key, char oldValue, char newValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char computeCharIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char computeCharIfAbsentPartial(K key, Reference2CharFunction<? super K> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char computeCharIfPresent(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char computeChar(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public char mergeChar(K key, char value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Character getOrDefault(Object key, Character defaultValue) {
            return this.map.getOrDefault(key, defaultValue);
        }

        @Override
        @Deprecated
        public boolean remove(Object key, Object value) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Character replace(K key, Character value) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public boolean replace(K key, Character oldValue, Character newValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Character putIfAbsent(K key, Character value) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Character computeIfAbsent(K key, Function<? super K, ? extends Character> mappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Character computeIfPresent(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        public Character compute(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Character merge(K key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            throw new UnsupportedOperationException();
        }
    }

    public static class SynchronizedMap<K>
    extends Reference2CharFunctions.SynchronizedFunction<K>
    implements Reference2CharMap<K>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharMap<K> map;
        protected transient ObjectSet<Reference2CharMap.Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient CharCollection values;

        protected SynchronizedMap(Reference2CharMap<K> m, Object sync) {
            super(m, sync);
            this.map = m;
        }

        protected SynchronizedMap(Reference2CharMap<K> m) {
            super(m);
            this.map = m;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsValue(char v) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.containsValue(v);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public boolean containsValue(Object ov) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.containsValue(ov);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void putAll(Map<? extends K, ? extends Character> m) {
            Object object = this.sync;
            synchronized (object) {
                this.map.putAll(m);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public ObjectSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            Object object = this.sync;
            synchronized (object) {
                if (this.entries == null) {
                    this.entries = ObjectSets.synchronize(this.map.reference2CharEntrySet(), this.sync);
                }
                return this.entries;
            }
        }

        @Override
        @Deprecated
        public ObjectSet<Map.Entry<K, Character>> entrySet() {
            return this.reference2CharEntrySet();
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public ReferenceSet<K> keySet() {
            Object object = this.sync;
            synchronized (object) {
                if (this.keys == null) {
                    this.keys = ReferenceSets.synchronize(this.map.keySet(), this.sync);
                }
                return this.keys;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public CharCollection values() {
            Object object = this.sync;
            synchronized (object) {
                if (this.values == null) {
                    return CharCollections.synchronize(this.map.values(), this.sync);
                }
                return this.values;
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean isEmpty() {
            Object object = this.sync;
            synchronized (object) {
                return this.map.isEmpty();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int hashCode() {
            Object object = this.sync;
            synchronized (object) {
                return this.map.hashCode();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            Object object = this.sync;
            synchronized (object) {
                return this.map.equals(o);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            Object object = this.sync;
            synchronized (object) {
                s.defaultWriteObject();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char getOrDefault(Object key, char defaultValue) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void forEach(BiConsumer<? super K, ? super Character> action) {
            Object object = this.sync;
            synchronized (object) {
                this.map.forEach(action);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void replaceAll(BiFunction<? super K, ? super Character, ? extends Character> function) {
            Object object = this.sync;
            synchronized (object) {
                this.map.replaceAll(function);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char putIfAbsent(K key, char value) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.putIfAbsent(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(Object key, char value) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.remove(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char replace(K key, char value) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.replace(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean replace(K key, char oldValue, char newValue) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.replace(key, oldValue, newValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char computeCharIfAbsent(K key, ToIntFunction<? super K> mappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.computeCharIfAbsent((K)key, mappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char computeCharIfAbsentPartial(K key, Reference2CharFunction<? super K> mappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.computeCharIfAbsentPartial((K)key, mappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char computeCharIfPresent(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.computeCharIfPresent((K)key, (BiFunction<? super K, Character, Character>)remappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char computeChar(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.computeChar((K)key, (BiFunction<? super K, Character, Character>)remappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public char mergeChar(K key, char value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.mergeChar(key, value, remappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Character getOrDefault(Object key, Character defaultValue) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.getOrDefault(key, defaultValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public boolean remove(Object key, Object value) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.remove(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Character replace(K key, Character value) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.replace(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public boolean replace(K key, Character oldValue, Character newValue) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.replace(key, oldValue, newValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Character putIfAbsent(K key, Character value) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.putIfAbsent(key, value);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Character computeIfAbsent(K key, Function<? super K, ? extends Character> mappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.computeIfAbsent((K)key, mappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Character computeIfPresent(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.computeIfPresent((K)key, remappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Character compute(K key, BiFunction<? super K, ? super Character, ? extends Character> remappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.compute((K)key, remappingFunction);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Character merge(K key, Character value, BiFunction<? super Character, ? super Character, ? extends Character> remappingFunction) {
            Object object = this.sync;
            synchronized (object) {
                return this.map.merge(key, value, remappingFunction);
            }
        }
    }

    public static class Singleton<K>
    extends Reference2CharFunctions.Singleton<K>
    implements Reference2CharMap<K>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected transient ObjectSet<Reference2CharMap.Entry<K>> entries;
        protected transient ReferenceSet<K> keys;
        protected transient CharCollection values;

        protected Singleton(K key, char value) {
            super(key, value);
        }

        @Override
        public boolean containsValue(char v) {
            return this.value == v;
        }

        @Override
        @Deprecated
        public boolean containsValue(Object ov) {
            return ((Character)ov).charValue() == this.value;
        }

        @Override
        public void putAll(Map<? extends K, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSets.singleton(new AbstractReference2CharMap.BasicEntry<Object>(this.key, this.value));
            }
            return this.entries;
        }

        @Override
        @Deprecated
        public ObjectSet<Map.Entry<K, Character>> entrySet() {
            return this.reference2CharEntrySet();
        }

        @Override
        public ReferenceSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSets.singleton(this.key);
            }
            return this.keys;
        }

        @Override
        public CharCollection values() {
            if (this.values == null) {
                this.values = CharSets.singleton(this.value);
            }
            return this.values;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public int hashCode() {
            return System.identityHashCode(this.key) ^ this.value;
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
            if (m.size() != 1) {
                return false;
            }
            return m.entrySet().iterator().next().equals(this.entrySet().iterator().next());
        }

        public String toString() {
            return "{" + this.key + "=>" + this.value + "}";
        }
    }

    public static class EmptyMap<K>
    extends Reference2CharFunctions.EmptyFunction<K>
    implements Reference2CharMap<K>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyMap() {
        }

        @Override
        public boolean containsValue(char v) {
            return false;
        }

        @Override
        @Deprecated
        public boolean containsValue(Object ov) {
            return false;
        }

        @Override
        public void putAll(Map<? extends K, ? extends Character> m) {
            throw new UnsupportedOperationException();
        }

        @Override
        public ObjectSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            return ObjectSets.EMPTY_SET;
        }

        @Override
        public ReferenceSet<K> keySet() {
            return ReferenceSets.EMPTY_SET;
        }

        @Override
        public CharCollection values() {
            return CharSets.EMPTY_SET;
        }

        @Override
        public Object clone() {
            return EMPTY_MAP;
        }

        @Override
        public boolean isEmpty() {
            return true;
        }

        @Override
        public int hashCode() {
            return 0;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map)) {
                return false;
            }
            return ((Map)o).isEmpty();
        }

        @Override
        public String toString() {
            return "{}";
        }
    }
}

