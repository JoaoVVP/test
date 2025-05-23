/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2IntSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2IntSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public abstract class AbstractInt2IntSortedMap
extends AbstractInt2IntMap
implements Int2IntSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractInt2IntSortedMap() {
    }

    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public IntCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements IntIterator {
        protected final ObjectBidirectionalIterator<Int2IntMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Int2IntMap.Entry> i) {
            this.i = i;
        }

        @Override
        public int nextInt() {
            return ((Int2IntMap.Entry)this.i.next()).getIntValue();
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
            return new ValuesIterator(Int2IntSortedMaps.fastIterator(AbstractInt2IntSortedMap.this));
        }

        @Override
        public boolean contains(int k) {
            return AbstractInt2IntSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractInt2IntSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractInt2IntSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2IntMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Int2IntMap.Entry> i) {
            this.i = i;
        }

        @Override
        public int nextInt() {
            return ((Int2IntMap.Entry)this.i.next()).getIntKey();
        }

        @Override
        public int previousInt() {
            return ((Int2IntMap.Entry)this.i.previous()).getIntKey();
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
    extends AbstractIntSortedSet {
        protected KeySet() {
        }

        @Override
        public boolean contains(int k) {
            return AbstractInt2IntSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractInt2IntSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractInt2IntSortedMap.this.clear();
        }

        @Override
        public IntComparator comparator() {
            return AbstractInt2IntSortedMap.this.comparator();
        }

        @Override
        public int firstInt() {
            return AbstractInt2IntSortedMap.this.firstIntKey();
        }

        @Override
        public int lastInt() {
            return AbstractInt2IntSortedMap.this.lastIntKey();
        }

        @Override
        public IntSortedSet headSet(int to) {
            return AbstractInt2IntSortedMap.this.headMap(to).keySet();
        }

        @Override
        public IntSortedSet tailSet(int from) {
            return AbstractInt2IntSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public IntSortedSet subSet(int from, int to) {
            return AbstractInt2IntSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public IntBidirectionalIterator iterator(int from) {
            return new KeySetIterator(AbstractInt2IntSortedMap.this.int2IntEntrySet().iterator(new AbstractInt2IntMap.BasicEntry(from, 0)));
        }

        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2IntSortedMaps.fastIterator(AbstractInt2IntSortedMap.this));
        }
    }
}

