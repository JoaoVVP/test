/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloat2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2ShortMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2ShortSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatSortedSets;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSets;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;

public final class Float2ShortSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Float2ShortSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(FloatComparator comparator) {
        return (x, y) -> comparator.compare(((Float)x.getKey()).floatValue(), ((Float)y.getKey()).floatValue());
    }

    public static ObjectBidirectionalIterator<Float2ShortMap.Entry> fastIterator(Float2ShortSortedMap map) {
        ObjectSet entries = map.float2ShortEntrySet();
        return entries instanceof Float2ShortSortedMap.FastSortedEntrySet ? ((Float2ShortSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }

    public static ObjectBidirectionalIterable<Float2ShortMap.Entry> fastIterable(Float2ShortSortedMap map) {
        ObjectSet entries = map.float2ShortEntrySet();
        return entries instanceof Float2ShortSortedMap.FastSortedEntrySet ? ((Float2ShortSortedMap.FastSortedEntrySet)entries)::fastIterator : entries;
    }

    public static Float2ShortSortedMap singleton(Float key, Short value) {
        return new Singleton(key.floatValue(), value);
    }

    public static Float2ShortSortedMap singleton(Float key, Short value, FloatComparator comparator) {
        return new Singleton(key.floatValue(), value, comparator);
    }

    public static Float2ShortSortedMap singleton(float key, short value) {
        return new Singleton(key, value);
    }

    public static Float2ShortSortedMap singleton(float key, short value, FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }

    public static Float2ShortSortedMap synchronize(Float2ShortSortedMap m) {
        return new SynchronizedSortedMap(m);
    }

    public static Float2ShortSortedMap synchronize(Float2ShortSortedMap m, Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }

    public static Float2ShortSortedMap unmodifiable(Float2ShortSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }

    public static class UnmodifiableSortedMap
    extends Float2ShortMaps.UnmodifiableMap
    implements Float2ShortSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ShortSortedMap sortedMap;

        protected UnmodifiableSortedMap(Float2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }

        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }

        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.float2ShortEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return this.float2ShortEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2ShortSortedMap subMap(float from, float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override
        public Float2ShortSortedMap headMap(float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override
        public Float2ShortSortedMap tailMap(float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }

        @Override
        public float firstFloatKey() {
            return this.sortedMap.firstFloatKey();
        }

        @Override
        public float lastFloatKey() {
            return this.sortedMap.lastFloatKey();
        }

        @Override
        @Deprecated
        public Float firstKey() {
            return this.sortedMap.firstKey();
        }

        @Override
        @Deprecated
        public Float lastKey() {
            return this.sortedMap.lastKey();
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap subMap(Float from, Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap headMap(Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap tailMap(Float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }

    public static class SynchronizedSortedMap
    extends Float2ShortMaps.SynchronizedMap
    implements Float2ShortSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2ShortSortedMap sortedMap;

        protected SynchronizedSortedMap(Float2ShortSortedMap m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Float2ShortSortedMap m) {
            super(m);
            this.sortedMap = m;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public FloatComparator comparator() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.comparator();
            }
        }

        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.float2ShortEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return this.float2ShortEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2ShortSortedMap subMap(float from, float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override
        public Float2ShortSortedMap headMap(float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override
        public Float2ShortSortedMap tailMap(float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public float firstFloatKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.firstFloatKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public float lastFloatKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastFloatKey();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Float firstKey() {
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
        public Float lastKey() {
            Object object = this.sync;
            synchronized (object) {
                return this.sortedMap.lastKey();
            }
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap subMap(Float from, Float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap headMap(Float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap tailMap(Float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }

    public static class Singleton
    extends Float2ShortMaps.Singleton
    implements Float2ShortSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;

        protected Singleton(float key, short value, FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(float key, short value) {
            this(key, value, null);
        }

        final int compare(float k1, float k2) {
            return this.comparator == null ? Float.compare(k1, k2) : this.comparator.compare(k1, k2);
        }

        @Override
        public FloatComparator comparator() {
            return this.comparator;
        }

        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractFloat2ShortMap.BasicEntry(this.key, this.value), Float2ShortSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return this.float2ShortEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2ShortSortedMap subMap(float from, float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Float2ShortSortedMap headMap(float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Float2ShortSortedMap tailMap(float from) {
            if (this.compare(from, this.key) <= 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public float firstFloatKey() {
            return this.key;
        }

        @Override
        public float lastFloatKey() {
            return this.key;
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap headMap(Float oto) {
            return this.headMap(oto.floatValue());
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap tailMap(Float ofrom) {
            return this.tailMap(ofrom.floatValue());
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap subMap(Float ofrom, Float oto) {
            return this.subMap(ofrom.floatValue(), oto.floatValue());
        }

        @Override
        @Deprecated
        public Float firstKey() {
            return Float.valueOf(this.firstFloatKey());
        }

        @Override
        @Deprecated
        public Float lastKey() {
            return Float.valueOf(this.lastFloatKey());
        }
    }

    public static class EmptySortedMap
    extends Float2ShortMaps.EmptyMap
    implements Float2ShortSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySortedMap() {
        }

        @Override
        public FloatComparator comparator() {
            return null;
        }

        @Override
        public ObjectSortedSet<Float2ShortMap.Entry> float2ShortEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Short>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }

        @Override
        public Float2ShortSortedMap subMap(float from, float to) {
            return EMPTY_MAP;
        }

        @Override
        public Float2ShortSortedMap headMap(float to) {
            return EMPTY_MAP;
        }

        @Override
        public Float2ShortSortedMap tailMap(float from) {
            return EMPTY_MAP;
        }

        @Override
        public float firstFloatKey() {
            throw new NoSuchElementException();
        }

        @Override
        public float lastFloatKey() {
            throw new NoSuchElementException();
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap headMap(Float oto) {
            return this.headMap(oto.floatValue());
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap tailMap(Float ofrom) {
            return this.tailMap(ofrom.floatValue());
        }

        @Override
        @Deprecated
        public Float2ShortSortedMap subMap(Float ofrom, Float oto) {
            return this.subMap(ofrom.floatValue(), oto.floatValue());
        }

        @Override
        @Deprecated
        public Float firstKey() {
            return Float.valueOf(this.firstFloatKey());
        }

        @Override
        @Deprecated
        public Float lastKey() {
            return Float.valueOf(this.lastFloatKey());
        }
    }
}

