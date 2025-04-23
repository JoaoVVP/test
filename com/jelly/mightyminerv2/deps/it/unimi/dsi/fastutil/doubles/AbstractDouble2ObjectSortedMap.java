/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDouble2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2ObjectMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2ObjectSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;

public abstract class AbstractDouble2ObjectSortedMap<V>
extends AbstractDouble2ObjectMap<V>
implements Double2ObjectSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractDouble2ObjectSortedMap() {
    }

    @Override
    public DoubleSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public ObjectCollection<V> values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator<V>
    implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i;

        public ValuesIterator(ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i) {
            this.i = i;
        }

        @Override
        public V next() {
            return ((Double2ObjectMap.Entry)this.i.next()).getValue();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }

    protected class ValuesCollection
    extends AbstractObjectCollection<V> {
        protected ValuesCollection() {
        }

        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator(Double2ObjectSortedMaps.fastIterator(AbstractDouble2ObjectSortedMap.this));
        }

        @Override
        public boolean contains(Object k) {
            return AbstractDouble2ObjectSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractDouble2ObjectSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2ObjectSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator<V>
    implements DoubleBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i;

        public KeySetIterator(ObjectBidirectionalIterator<Double2ObjectMap.Entry<V>> i) {
            this.i = i;
        }

        @Override
        public double nextDouble() {
            return ((Double2ObjectMap.Entry)this.i.next()).getDoubleKey();
        }

        @Override
        public double previousDouble() {
            return ((Double2ObjectMap.Entry)this.i.previous()).getDoubleKey();
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
            return AbstractDouble2ObjectSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractDouble2ObjectSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractDouble2ObjectSortedMap.this.clear();
        }

        @Override
        public DoubleComparator comparator() {
            return AbstractDouble2ObjectSortedMap.this.comparator();
        }

        @Override
        public double firstDouble() {
            return AbstractDouble2ObjectSortedMap.this.firstDoubleKey();
        }

        @Override
        public double lastDouble() {
            return AbstractDouble2ObjectSortedMap.this.lastDoubleKey();
        }

        @Override
        public DoubleSortedSet headSet(double to) {
            return AbstractDouble2ObjectSortedMap.this.headMap(to).keySet();
        }

        @Override
        public DoubleSortedSet tailSet(double from) {
            return AbstractDouble2ObjectSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public DoubleSortedSet subSet(double from, double to) {
            return AbstractDouble2ObjectSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public DoubleBidirectionalIterator iterator(double from) {
            return new KeySetIterator(AbstractDouble2ObjectSortedMap.this.double2ObjectEntrySet().iterator(new AbstractDouble2ObjectMap.BasicEntry<Object>(from, null)));
        }

        @Override
        public DoubleBidirectionalIterator iterator() {
            return new KeySetIterator(Double2ObjectSortedMaps.fastIterator(AbstractDouble2ObjectSortedMap.this));
        }
    }
}

