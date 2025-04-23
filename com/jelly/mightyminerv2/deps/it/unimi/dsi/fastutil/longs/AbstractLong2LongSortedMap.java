/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLong2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2LongSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.Long2LongSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class AbstractLong2LongSortedMap
extends AbstractLong2LongMap
implements Long2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractLong2LongSortedMap() {
    }

    @Override
    public LongSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements LongIterator {
        protected final ObjectBidirectionalIterator<Long2LongMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Long2LongMap.Entry> i) {
            this.i = i;
        }

        @Override
        public long nextLong() {
            return ((Long2LongMap.Entry)this.i.next()).getLongValue();
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
            return new ValuesIterator(Long2LongSortedMaps.fastIterator(AbstractLong2LongSortedMap.this));
        }

        @Override
        public boolean contains(long k) {
            return AbstractLong2LongSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractLong2LongSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractLong2LongSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements LongBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Long2LongMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Long2LongMap.Entry> i) {
            this.i = i;
        }

        @Override
        public long nextLong() {
            return ((Long2LongMap.Entry)this.i.next()).getLongKey();
        }

        @Override
        public long previousLong() {
            return ((Long2LongMap.Entry)this.i.previous()).getLongKey();
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
    extends AbstractLongSortedSet {
        protected KeySet() {
        }

        @Override
        public boolean contains(long k) {
            return AbstractLong2LongSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractLong2LongSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractLong2LongSortedMap.this.clear();
        }

        @Override
        public LongComparator comparator() {
            return AbstractLong2LongSortedMap.this.comparator();
        }

        @Override
        public long firstLong() {
            return AbstractLong2LongSortedMap.this.firstLongKey();
        }

        @Override
        public long lastLong() {
            return AbstractLong2LongSortedMap.this.lastLongKey();
        }

        @Override
        public LongSortedSet headSet(long to) {
            return AbstractLong2LongSortedMap.this.headMap(to).keySet();
        }

        @Override
        public LongSortedSet tailSet(long from) {
            return AbstractLong2LongSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public LongSortedSet subSet(long from, long to) {
            return AbstractLong2LongSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public LongBidirectionalIterator iterator(long from) {
            return new KeySetIterator(AbstractLong2LongSortedMap.this.long2LongEntrySet().iterator(new AbstractLong2LongMap.BasicEntry(from, 0L)));
        }

        @Override
        public LongBidirectionalIterator iterator() {
            return new KeySetIterator(Long2LongSortedMaps.fastIterator(AbstractLong2LongSortedMap.this));
        }
    }
}

