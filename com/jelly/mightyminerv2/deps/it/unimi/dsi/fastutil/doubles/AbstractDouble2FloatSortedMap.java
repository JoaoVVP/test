/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDouble2FloatMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2FloatMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2FloatSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2FloatSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.AbstractFloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class AbstractDouble2FloatSortedMap
extends AbstractDouble2FloatMap
implements Double2FloatSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractDouble2FloatSortedMap() {
    }

    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public FloatCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements FloatIterator {
        protected final ObjectBidirectionalIterator<Double2FloatMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Double2FloatMap.Entry> i) {
            this.i = i;
        }

        @Override
        public float nextFloat() {
            return ((Double2FloatMap.Entry)this.i.next()).getFloatValue();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }

    protected class ValuesCollection
    extends AbstractFloatCollection {
        protected ValuesCollection() {
        }

        @Override
        public FloatIterator iterator() {
            return new ValuesIterator(Double2FloatSortedMaps.fastIterator(AbstractDouble2FloatSortedMap.this));
        }

        @Override
        public boolean contains(float k) {
            return AbstractDouble2FloatSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractDouble2FloatSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2FloatSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2FloatMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Double2FloatMap.Entry> i) {
            this.i = i;
        }

        @Override
        public double nextDouble() {
            return ((Double2FloatMap.Entry)this.i.next()).getDoubleKey();
        }

        @Override
        public double previousDouble() {
            return ((Double2FloatMap.Entry)this.i.previous()).getDoubleKey();
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
            return AbstractDouble2FloatSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractDouble2FloatSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2FloatSortedMap.this.clear();
        }

        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2FloatSortedMap.this.comparator();
        }

        @Override
        public double firstDouble() {
            return AbstractDouble2FloatSortedMap.this.firstDoubleKey();
        }

        @Override
        public double lastDouble() {
            return AbstractDouble2FloatSortedMap.this.lastDoubleKey();
        }

        @Override
        public DoubleSortedSet headSet(double to) {
            return AbstractDouble2FloatSortedMap.this.headMap(to).keySet();
        }

        @Override
        public DoubleSortedSet tailSet(double from) {
            return AbstractDouble2FloatSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public DoubleSortedSet subSet(double from, double to) {
            return AbstractDouble2FloatSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public DoubleBidirectionalIterator iterator(double from) {
            return new KeySetIterator(AbstractDouble2FloatSortedMap.this.double2FloatEntrySet().iterator(new AbstractDouble2FloatMap.BasicEntry(from, 0.0f)));
        }

        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2FloatSortedMaps.fastIterator(AbstractDouble2FloatSortedMap.this));
        }
    }
}

