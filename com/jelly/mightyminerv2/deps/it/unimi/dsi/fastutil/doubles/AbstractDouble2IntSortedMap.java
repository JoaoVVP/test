/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDouble2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2IntSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2IntSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class AbstractDouble2IntSortedMap
extends AbstractDouble2IntMap
implements Double2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractDouble2IntSortedMap() {
    }

    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements IntIterator {
        protected final ObjectBidirectionalIterator<Double2IntMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Double2IntMap.Entry> i) {
            this.i = i;
        }

        @Override
        public int nextInt() {
            return ((Double2IntMap.Entry)this.i.next()).getIntValue();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }

    protected class ValuesCollection
    extends AbstractIntCollection {
        protected ValuesCollection() {
        }

        @Override
        public IntIterator iterator() {
            return new ValuesIterator(Double2IntSortedMaps.fastIterator(AbstractDouble2IntSortedMap.this));
        }

        @Override
        public boolean contains(int k) {
            return AbstractDouble2IntSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractDouble2IntSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2IntSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2IntMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Double2IntMap.Entry> i) {
            this.i = i;
        }

        @Override
        public double nextDouble() {
            return ((Double2IntMap.Entry)this.i.next()).getDoubleKey();
        }

        @Override
        public double previousDouble() {
            return ((Double2IntMap.Entry)this.i.previous()).getDoubleKey();
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
            return AbstractDouble2IntSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractDouble2IntSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2IntSortedMap.this.clear();
        }

        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2IntSortedMap.this.comparator();
        }

        @Override
        public double firstDouble() {
            return AbstractDouble2IntSortedMap.this.firstDoubleKey();
        }

        @Override
        public double lastDouble() {
            return AbstractDouble2IntSortedMap.this.lastDoubleKey();
        }

        @Override
        public DoubleSortedSet headSet(double to) {
            return AbstractDouble2IntSortedMap.this.headMap(to).keySet();
        }

        @Override
        public DoubleSortedSet tailSet(double from) {
            return AbstractDouble2IntSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public DoubleSortedSet subSet(double from, double to) {
            return AbstractDouble2IntSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public DoubleBidirectionalIterator iterator(double from) {
            return new KeySetIterator(AbstractDouble2IntSortedMap.this.double2IntEntrySet().iterator(new AbstractDouble2IntMap.BasicEntry(from, 0)));
        }

        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2IntSortedMaps.fastIterator(AbstractDouble2IntSortedMap.this));
        }
    }
}

