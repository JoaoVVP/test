/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloat2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloatSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2LongSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2LongSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class AbstractFloat2LongSortedMap
extends AbstractFloat2LongMap
implements Float2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractFloat2LongSortedMap() {
    }

    @Override
    public FloatSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements LongIterator {
        protected final ObjectBidirectionalIterator<Float2LongMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Float2LongMap.Entry> i) {
            this.i = i;
        }

        @Override
        public long nextLong() {
            return ((Float2LongMap.Entry)this.i.next()).getLongValue();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }

    protected class ValuesCollection
    extends AbstractLongCollection {
        protected ValuesCollection() {
        }

        @Override
        public LongIterator iterator() {
            return new ValuesIterator(Float2LongSortedMaps.fastIterator(AbstractFloat2LongSortedMap.this));
        }

        @Override
        public boolean contains(long k) {
            return AbstractFloat2LongSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractFloat2LongSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractFloat2LongSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements FloatBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Float2LongMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Float2LongMap.Entry> i) {
            this.i = i;
        }

        @Override
        public float nextFloat() {
            return ((Float2LongMap.Entry)this.i.next()).getFloatKey();
        }

        @Override
        public float previousFloat() {
            return ((Float2LongMap.Entry)this.i.previous()).getFloatKey();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }
    }

    protected class KeySet
    extends AbstractFloatSortedSet {
        protected KeySet() {
        }

        @Override
        public boolean contains(float k) {
            return AbstractFloat2LongSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractFloat2LongSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractFloat2LongSortedMap.this.clear();
        }

        @Override
        public FloatComparator comparator() {
            return AbstractFloat2LongSortedMap.this.comparator();
        }

        @Override
        public float firstFloat() {
            return AbstractFloat2LongSortedMap.this.firstFloatKey();
        }

        @Override
        public float lastFloat() {
            return AbstractFloat2LongSortedMap.this.lastFloatKey();
        }

        @Override
        public FloatSortedSet headSet(float to) {
            return AbstractFloat2LongSortedMap.this.headMap(to).keySet();
        }

        @Override
        public FloatSortedSet tailSet(float from) {
            return AbstractFloat2LongSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public FloatSortedSet subSet(float from, float to) {
            return AbstractFloat2LongSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public FloatBidirectionalIterator iterator(float from) {
            return new KeySetIterator(AbstractFloat2LongSortedMap.this.float2LongEntrySet().iterator(new AbstractFloat2LongMap.BasicEntry(from, 0L)));
        }

        @Override
        public FloatBidirectionalIterator iterator() {
            return new KeySetIterator(Float2LongSortedMaps.fastIterator(AbstractFloat2LongSortedMap.this));
        }
    }
}

