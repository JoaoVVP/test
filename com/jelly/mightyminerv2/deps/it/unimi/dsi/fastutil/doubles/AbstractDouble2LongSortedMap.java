/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDouble2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2LongMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2LongSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2LongSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class AbstractDouble2LongSortedMap
extends AbstractDouble2LongMap
implements Double2LongSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractDouble2LongSortedMap() {
    }

    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public LongCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements LongIterator {
        protected final ObjectBidirectionalIterator<Double2LongMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Double2LongMap.Entry> i) {
            this.i = i;
        }

        @Override
        public long nextLong() {
            return ((Double2LongMap.Entry)this.i.next()).getLongValue();
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
            return new ValuesIterator(Double2LongSortedMaps.fastIterator(AbstractDouble2LongSortedMap.this));
        }

        @Override
        public boolean contains(long k) {
            return AbstractDouble2LongSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractDouble2LongSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2LongSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2LongMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Double2LongMap.Entry> i) {
            this.i = i;
        }

        @Override
        public double nextDouble() {
            return ((Double2LongMap.Entry)this.i.next()).getDoubleKey();
        }

        @Override
        public double previousDouble() {
            return ((Double2LongMap.Entry)this.i.previous()).getDoubleKey();
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
    extends AbstractDoubleSortedSet {
        protected KeySet() {
        }

        @Override
        public boolean contains(double k) {
            return AbstractDouble2LongSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractDouble2LongSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2LongSortedMap.this.clear();
        }

        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2LongSortedMap.this.comparator();
        }

        @Override
        public double firstDouble() {
            return AbstractDouble2LongSortedMap.this.firstDoubleKey();
        }

        @Override
        public double lastDouble() {
            return AbstractDouble2LongSortedMap.this.lastDoubleKey();
        }

        @Override
        public DoubleSortedSet headSet(double to) {
            return AbstractDouble2LongSortedMap.this.headMap(to).keySet();
        }

        @Override
        public DoubleSortedSet tailSet(double from) {
            return AbstractDouble2LongSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public DoubleSortedSet subSet(double from, double to) {
            return AbstractDouble2LongSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public DoubleBidirectionalIterator iterator(double from) {
            return new KeySetIterator(AbstractDouble2LongSortedMap.this.double2LongEntrySet().iterator(new AbstractDouble2LongMap.BasicEntry(from, 0L)));
        }

        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2LongSortedMaps.fastIterator(AbstractDouble2LongSortedMap.this));
        }
    }
}

