/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReference2CharMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.Reference2CharSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSortedSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

public final class Reference2CharSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Reference2CharSortedMaps() {
    }

    public static <K> Comparator<? super Map.Entry<K, ?>> entryComparator(Comparator<? super K> comparator) {
        return (x, y) -> comparator.compare((Object)x.getKey(), (Object)y.getKey());
    }

    public static <K> ObjectBidirectionalIterator<Reference2CharMap.Entry<K>> fastIterator(Reference2CharSortedMap<K> map) {
        ObjectSet entries = map.reference2CharEntrySet();
        return entries instanceof Reference2CharSortedMap.FastSortedEntrySet ? ((Reference2CharSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }

    public static <K> ObjectBidirectionalIterable<Reference2CharMap.Entry<K>> fastIterable(Reference2CharSortedMap<K> map) {
        ObjectSet entries = map.reference2CharEntrySet();
        return entries instanceof Reference2CharSortedMap.FastSortedEntrySet ? ((Reference2CharSortedMap.FastSortedEntrySet)entries)::fastIterator : entries;
    }

    public static <K> Reference2CharSortedMap<K> emptyMap() {
        return EMPTY_MAP;
    }

    public static <K> Reference2CharSortedMap<K> singleton(K key, Character value) {
        return new Singleton<K>(key, value.charValue());
    }

    public static <K> Reference2CharSortedMap<K> singleton(K key, Character value, Comparator<? super K> comparator) {
        return new Singleton<K>(key, value.charValue(), comparator);
    }

    public static <K> Reference2CharSortedMap<K> singleton(K key, char value) {
        return new Singleton<K>(key, value);
    }

    public static <K> Reference2CharSortedMap<K> singleton(K key, char value, Comparator<? super K> comparator) {
        return new Singleton<K>(key, value, comparator);
    }

    public static <K> Reference2CharSortedMap<K> synchronize(Reference2CharSortedMap<K> m) {
        return new SynchronizedSortedMap<K>(m);
    }

    public static <K> Reference2CharSortedMap<K> synchronize(Reference2CharSortedMap<K> m, Object sync) {
        return new SynchronizedSortedMap<K>(m, sync);
    }

    public static <K> Reference2CharSortedMap<K> unmodifiable(Reference2CharSortedMap<K> m) {
        return new UnmodifiableSortedMap<K>(m);
    }

    public static class UnmodifiableSortedMap<K>
    extends Reference2CharMaps.UnmodifiableMap<K>
    implements Reference2CharSortedMap<K>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharSortedMap<K> sortedMap;

        protected UnmodifiableSortedMap(Reference2CharSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.sortedMap.comparator();
        }

        @Override
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.reference2CharEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return this.reference2CharEntrySet();
        }

        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (ReferenceSortedSet)this.keys;
        }

        @Override
        public Reference2CharSortedMap<K> subMap(K from, K to) {
            return new UnmodifiableSortedMap<K>(this.sortedMap.subMap((Object)from, (Object)to));
        }

        @Override
        public Reference2CharSortedMap<K> headMap(K to) {
            return new UnmodifiableSortedMap<K>(this.sortedMap.headMap((Object)to));
        }

        @Override
        public Reference2CharSortedMap<K> tailMap(K from) {
            return new UnmodifiableSortedMap<K>(this.sortedMap.tailMap((Object)from));
        }

        @Override
        public K firstKey() {
            return this.sortedMap.firstKey();
        }

        @Override
        public K lastKey() {
            return this.sortedMap.lastKey();
        }
    }

    public static class SynchronizedSortedMap<K>
    extends Reference2CharMaps.SynchronizedMap<K>
    implements Reference2CharSortedMap<K>,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Reference2CharSortedMap<K> sortedMap;

        protected SynchronizedSortedMap(Reference2CharSortedMap<K> m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Reference2CharSortedMap<K> m) {
            super(m);
            this.sortedMap = m;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public Comparator<? super K> comparator() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.comparator();
            }
        }

        @Override
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.reference2CharEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return this.reference2CharEntrySet();
        }

        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (ReferenceSortedSet)this.keys;
        }

        @Override
        public Reference2CharSortedMap<K> subMap(K from, K to) {
            return new SynchronizedSortedMap<K>(this.sortedMap.subMap((Object)from, (Object)to), this.sync);
        }

        @Override
        public Reference2CharSortedMap<K> headMap(K to) {
            return new SynchronizedSortedMap<K>(this.sortedMap.headMap((Object)to), this.sync);
        }

        @Override
        public Reference2CharSortedMap<K> tailMap(K from) {
            return new SynchronizedSortedMap<K>(this.sortedMap.tailMap((Object)from), this.sync);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K firstKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public K lastKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastKey();
            }
        }
    }

    public static class Singleton<K>
    extends Reference2CharMaps.Singleton<K>
    implements Reference2CharSortedMap<K>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Comparator<? super K> comparator;

        protected Singleton(K key, char value, Comparator<? super K> comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(K key, char value) {
            this(key, value, null);
        }

        final int compare(K k1, K k2) {
            return this.comparator == null ? ((Comparable)k1).compareTo(k2) : this.comparator.compare(k1, k2);
        }

        @Override
        public Comparator<? super K> comparator() {
            return this.comparator;
        }

        @Override
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractReference2CharMap.BasicEntry<Object>(this.key, this.value), Reference2CharSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return this.reference2CharEntrySet();
        }

        @Override
        public ReferenceSortedSet<K> keySet() {
            if (this.keys == null) {
                this.keys = ReferenceSortedSets.singleton(this.key, this.comparator);
            }
            return (ReferenceSortedSet)this.keys;
        }

        @Override
        public Reference2CharSortedMap<K> subMap(K from, K to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Reference2CharSortedMap<K> headMap(K to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Reference2CharSortedMap<K> tailMap(K from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public K firstKey() {
            return (K)this.key;
        }

        @Override
        public K lastKey() {
            return (K)this.key;
        }
    }

    public static class EmptySortedMap<K>
    extends Reference2CharMaps.EmptyMap<K>
    implements Reference2CharSortedMap<K>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override
        public Comparator<? super K> comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Reference2CharMap.Entry<K>> reference2CharEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<K, Character>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        public ReferenceSortedSet<K> keySet() {
            return ReferenceSortedSets.EMPTY_SET;
        }

        @Override
        public Reference2CharSortedMap<K> subMap(K from, K to) {
            return EMPTY_MAP;
        }

        @Override
        public Reference2CharSortedMap<K> headMap(K to) {
            return EMPTY_MAP;
        }

        @Override
        public Reference2CharSortedMap<K> tailMap(K from) {
            return EMPTY_MAP;
        }

        @Override
        public K firstKey() {
            throw new NoSuchElementException();
        }

        @Override
        public K lastKey() {
            throw new NoSuchElementException();
        }
    }
}

