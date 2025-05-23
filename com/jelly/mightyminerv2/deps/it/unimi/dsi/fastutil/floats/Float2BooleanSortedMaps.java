/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloat2BooleanMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2BooleanMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2BooleanMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2BooleanSortedMap;
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

public final class Float2BooleanSortedMaps {
    public static final EmptySortedMap EMPTY_MAP = new EmptySortedMap();

    private Float2BooleanSortedMaps() {
    }

    public static Comparator<? super Map.Entry<Float, ?>> entryComparator(FloatComparator comparator) {
        return (x, y) -> comparator.compare(((Float)x.getKey()).floatValue(), ((Float)y.getKey()).floatValue());
    }

    public static ObjectBidirectionalIterator<Float2BooleanMap.Entry> fastIterator(Float2BooleanSortedMap map) {
        ObjectSet entries = map.float2BooleanEntrySet();
        return entries instanceof Float2BooleanSortedMap.FastSortedEntrySet ? ((Float2BooleanSortedMap.FastSortedEntrySet)entries).fastIterator() : entries.iterator();
    }

    public static ObjectBidirectionalIterable<Float2BooleanMap.Entry> fastIterable(Float2BooleanSortedMap map) {
        ObjectSet entries = map.float2BooleanEntrySet();
        return entries instanceof Float2BooleanSortedMap.FastSortedEntrySet ? ((Float2BooleanSortedMap.FastSortedEntrySet)entries)::fastIterator : entries;
    }

    public static Float2BooleanSortedMap singleton(Float key, Boolean value) {
        return new Singleton(key.floatValue(), value);
    }

    public static Float2BooleanSortedMap singleton(Float key, Boolean value, FloatComparator comparator) {
        return new Singleton(key.floatValue(), value, comparator);
    }

    public static Float2BooleanSortedMap singleton(float key, boolean value) {
        return new Singleton(key, value);
    }

    public static Float2BooleanSortedMap singleton(float key, boolean value, FloatComparator comparator) {
        return new Singleton(key, value, comparator);
    }

    public static Float2BooleanSortedMap synchronize(Float2BooleanSortedMap m) {
        return new SynchronizedSortedMap(m);
    }

    public static Float2BooleanSortedMap synchronize(Float2BooleanSortedMap m, Object sync) {
        return new SynchronizedSortedMap(m, sync);
    }

    public static Float2BooleanSortedMap unmodifiable(Float2BooleanSortedMap m) {
        return new UnmodifiableSortedMap(m);
    }

    public static class UnmodifiableSortedMap
    extends Float2BooleanMaps.UnmodifiableMap
    implements Float2BooleanSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2BooleanSortedMap sortedMap;

        protected UnmodifiableSortedMap(Float2BooleanSortedMap m) {
            super(m);
            this.sortedMap = m;
        }

        @Override
        public FloatComparator comparator() {
            return this.sortedMap.comparator();
        }

        @Override
        public ObjectSortedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.unmodifiable(this.sortedMap.float2BooleanEntrySet());
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Boolean>> entrySet() {
            return this.float2BooleanEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.unmodifiable(this.sortedMap.keySet());
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2BooleanSortedMap subMap(float from, float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override
        public Float2BooleanSortedMap headMap(float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override
        public Float2BooleanSortedMap tailMap(float from) {
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
        public Float2BooleanSortedMap subMap(Float from, Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.subMap(from, to));
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap headMap(Float to) {
            return new UnmodifiableSortedMap(this.sortedMap.headMap(to));
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap tailMap(Float from) {
            return new UnmodifiableSortedMap(this.sortedMap.tailMap(from));
        }
    }

    public static class SynchronizedSortedMap
    extends Float2BooleanMaps.SynchronizedMap
    implements Float2BooleanSortedMap,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Float2BooleanSortedMap sortedMap;

        protected SynchronizedSortedMap(Float2BooleanSortedMap m, Object sync) {
            super(m, sync);
            this.sortedMap = m;
        }

        protected SynchronizedSortedMap(Float2BooleanSortedMap m) {
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
        public ObjectSortedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.synchronize(this.sortedMap.float2BooleanEntrySet(), this.sync);
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Boolean>> entrySet() {
            return this.float2BooleanEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.synchronize(this.sortedMap.keySet(), this.sync);
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2BooleanSortedMap subMap(float from, float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override
        public Float2BooleanSortedMap headMap(float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override
        public Float2BooleanSortedMap tailMap(float from) {
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
        public Float2BooleanSortedMap subMap(Float from, Float to) {
            return new SynchronizedSortedMap(this.sortedMap.subMap(from, to), this.sync);
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap headMap(Float to) {
            return new SynchronizedSortedMap(this.sortedMap.headMap(to), this.sync);
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap tailMap(Float from) {
            return new SynchronizedSortedMap(this.sortedMap.tailMap(from), this.sync);
        }
    }

    public static class Singleton
    extends Float2BooleanMaps.Singleton
    implements Float2BooleanSortedMap,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final FloatComparator comparator;

        protected Singleton(float key, boolean value, FloatComparator comparator) {
            super(key, value);
            this.comparator = comparator;
        }

        protected Singleton(float key, boolean value) {
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
        public ObjectSortedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
            if (this.entries == null) {
                this.entries = ObjectSortedSets.singleton(new AbstractFloat2BooleanMap.BasicEntry(this.key, this.value), Float2BooleanSortedMaps.entryComparator(this.comparator));
            }
            return (ObjectSortedSet)this.entries;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Boolean>> entrySet() {
            return this.float2BooleanEntrySet();
        }

        @Override
        public FloatSortedSet keySet() {
            if (this.keys == null) {
                this.keys = FloatSortedSets.singleton(this.key, this.comparator);
            }
            return (FloatSortedSet)this.keys;
        }

        @Override
        public Float2BooleanSortedMap subMap(float from, float to) {
            if (this.compare(from, this.key) <= 0 && this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Float2BooleanSortedMap headMap(float to) {
            if (this.compare(this.key, to) < 0) {
                return this;
            }
            return EMPTY_MAP;
        }

        @Override
        public Float2BooleanSortedMap tailMap(float from) {
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
        public Float2BooleanSortedMap headMap(Float oto) {
            return this.headMap(oto.floatValue());
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap tailMap(Float ofrom) {
            return this.tailMap(ofrom.floatValue());
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap subMap(Float ofrom, Float oto) {
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
    extends Float2BooleanMaps.EmptyMap
    implements Float2BooleanSortedMap,
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
        public ObjectSortedSet<Float2BooleanMap.Entry> float2BooleanEntrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        @Deprecated
        public ObjectSortedSet<Map.Entry<Float, Boolean>> entrySet() {
            return ObjectSortedSets.EMPTY_SET;
        }

        @Override
        public FloatSortedSet keySet() {
            return FloatSortedSets.EMPTY_SET;
        }

        @Override
        public Float2BooleanSortedMap subMap(float from, float to) {
            return EMPTY_MAP;
        }

        @Override
        public Float2BooleanSortedMap headMap(float to) {
            return EMPTY_MAP;
        }

        @Override
        public Float2BooleanSortedMap tailMap(float from) {
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
        public Float2BooleanSortedMap headMap(Float oto) {
            return this.headMap(oto.floatValue());
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap tailMap(Float ofrom) {
            return this.tailMap(ofrom.floatValue());
        }

        @Override
        @Deprecated
        public Float2BooleanSortedMap subMap(Float ofrom, Float oto) {
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

