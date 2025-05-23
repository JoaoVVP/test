/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLong2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ShortMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2ShortSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSortedSets;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

public final class Long2ShortSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Long2ShortSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Long, ?>> entryComparator(LongComparator comparator) {
        return (x, y) -> comparator.compare((long)((Long)x.getKey()), (long)((Long)y.getKey()));
    }

    public static ObjectBidirectionalIterator<Long2ShortMap.Entry> fastIterator(Long2ShortSortedMap map) {
        ObjectSet entries = map.long2ShortEntrySet();
        return entries instanceof Long2ShortSortedMap.FastSortedEntrySet ? ((Long2ShortSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }

    public static ObjectBidirectionalIterable<Long2ShortMap.Entry> fastIterable(Long2ShortSortedMap map) {
        ObjectSet entries = map.long2ShortEntrySet();
        return entries instanceof Long2ShortSortedMap.FastSortedEntrySet ? ((Long2ShortSortedMap.FastSortedEntrySet)entries)::fastIterator : entries;
    }

    public static Long2ShortSortedMap singleton(Long key, Short value) {
        return new Singleton(key, value);
    }

    public static Long2ShortSortedMap singleton(Long key, Short value, LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }

    public static Long2ShortSortedMap singleton(long key, short value) {
        return new Singleton(key, value);
    }

    public static Long2ShortSortedMap singleton(long key, short value, LongComparator comparator) {
        return new Singleton(key, value, comparator);
    }

    public static Long2ShortSortedMap synchronize(Long2ShortSortedMap m) {
        return new SynchronizedSortedMap(m);
    }

    public static Long2ShortSortedMap synchronize(Long2ShortSortedMap m, Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }

    public static Long2ShortSortedMap unmodifiable(Long2ShortSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }

    public static class UnmodifiableSortedMap
    extends Long2ShortMaps.UnmodifiableMap
    implements Long2ShortSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ShortSortedMap sortedMap;

        protected UnmodifiableSortedMap(Long2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }

        @Override
        public LongComparator comparator() {
            return this.sortedMap.comparator();
        }

        @Override
        public ObjectSortedSet<Long2ShortMap.Entry> long2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.long2ShortEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, Short>> entrySet() {
            return this.long2ShortEntrySet();
        }

        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (LongSortedSet)this.keys;
        }

        @Override
        public Long2ShortSortedMap subMap(long from, long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override
        public Long2ShortSortedMap headMap(long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override
        public Long2ShortSortedMap tailMap(long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }

        @Override
        public long firstLongKey() {
            return this.sortedMap.firstLongKey();
        }

        @Override
        public long lastLongKey() {
            return this.sortedMap.lastLongKey();
        }

        @Override
        @Deprecated
        public Long firstKey() {
            return this.sortedMap.firstKey();
        }

        @Override
        @Deprecated
        public Long lastKey() {
            return this.sortedMap.lastKey();
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap subMap(Long from, Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap headMap(Long to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap tailMap(Long from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }

    public static class SynchronizedSortedMap
    extends Long2ShortMaps.SynchronizedMap
    implements Long2ShortSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Long2ShortSortedMap sortedMap;

        protected SynchronizedSortedMap(Long2ShortSortedMap m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Long2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public LongComparator comparator() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.comparator();
            }
        }

        @Override
        public ObjectSortedSet<Long2ShortMap.Entry> long2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.long2ShortEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, Short>> entrySet() {
            return this.long2ShortEntrySet();
        }

        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (LongSortedSet)this.keys;
        }

        @Override
        public Long2ShortSortedMap subMap(long from, long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override
        public Long2ShortSortedMap headMap(long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override
        public Long2ShortSortedMap tailMap(long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long firstLongKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstLongKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long lastLongKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastLongKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Long firstKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Long lastKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastKey();
            }
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap subMap(Long from, Long to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap headMap(Long to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap tailMap(Long from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }

    public static class Singleton
    extends Long2ShortMaps.Singleton
    implements Long2ShortSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final LongComparator comparator;

        protected Singleton(long key, short value, LongComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(long key, short value) {
            this(key, value, null);
        }

        final int compare(long k1, long k2) {
            return this.comparator == null ? Long.compare(k1, k2) : this.comparator.compare(k1, k2);
        }

        @Override
        public LongComparator comparator() {
            return this.comparator;
        }

        @Override
        public ObjectSortedSet<Long2ShortMap.Entry> long2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractLong2ShortMap.BasicEntry(this.key, this.value), Long2ShortSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, Short>> entrySet() {
            return this.long2ShortEntrySet();
        }

        @Override
        public LongSortedSet keySet() {
            if (this.keys == null) {
                this.keys = LongSortedSets.singleton(this.key, this.comparator);
            }
            return (LongSortedSet)this.keys;
        }

        @Override
        public Long2ShortSortedMap subMap(long from, long to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Long2ShortSortedMap headMap(long to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Long2ShortSortedMap tailMap(long from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public long firstLongKey() {
            return this.key;
        }

        @Override
        public long lastLongKey() {
            return this.key;
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap headMap(Long oto) {
            return this.headMap((long)oto);
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap tailMap(Long ofrom) {
            return this.tailMap((long)ofrom);
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap subMap(Long ofrom, Long oto) {
            return this.subMap((long)ofrom, (long)oto);
        }

        @Override
        @Deprecated
        public Long firstKey() {
            return this.firstLongKey();
        }

        @Override
        @Deprecated
        public Long lastKey() {
            return this.lastLongKey();
        }
    }

    public static class EmptySortedMap
    extends Long2ShortMaps.EmptyMap
    implements Long2ShortSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override
        public LongComparator comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Long2ShortMap.Entry> long2ShortEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Long, Short>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        public LongSortedSet keySet() {
            return LongSortedSets.EMPTY_SET;
        }

        @Override
        public Long2ShortSortedMap subMap(long from, long to) {
            return EMPTY_MAP;
        }

        @Override
        public Long2ShortSortedMap headMap(long to) {
            return EMPTY_MAP;
        }

        @Override
        public Long2ShortSortedMap tailMap(long from) {
            return EMPTY_MAP;
        }

        @Override
        public long firstLongKey() {
            throw new NoSuchElementException();
        }

        @Override
        public long lastLongKey() {
            throw new NoSuchElementException();
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap headMap(Long oto) {
            return this.headMap((long)oto);
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap tailMap(Long ofrom) {
            return this.tailMap((long)ofrom);
        }

        @Override
        @Deprecated
        public Long2ShortSortedMap subMap(Long ofrom, Long oto) {
            return this.subMap((long)ofrom, (long)oto);
        }

        @Override
        @Deprecated
        public Long firstKey() {
            return this.firstLongKey();
        }

        @Override
        @Deprecated
        public Long lastKey() {
            return this.lastLongKey();
        }
    }
}

