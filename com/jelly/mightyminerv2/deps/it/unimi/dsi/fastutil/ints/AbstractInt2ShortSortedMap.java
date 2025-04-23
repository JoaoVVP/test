/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractIntSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ShortSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ShortSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;

public abstract class AbstractInt2ShortSortedMap
extends AbstractInt2ShortMap
implements Int2ShortSortedMap {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractInt2ShortSortedMap() {
    }

    @Override
    public IntSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public ShortCollection values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator
    implements ShortIterator {
        protected final ObjectBidirectionalIterator<Int2ShortMap.Entry> i;

        public ValuesIterator(ObjectBidirectionalIterator<Int2ShortMap.Entry> i) {
            this.i = i;
        }

        @Override
        public short nextShort() {
            return ((Int2ShortMap.Entry)this.i.next()).getShortValue();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }

    protected class ValuesCollection
    extends AbstractShortCollection {
        protected ValuesCollection() {
        }

        @Override
        public ShortIterator iterator() {
            return new ValuesIterator(Int2ShortSortedMaps.fastIterator(AbstractInt2ShortSortedMap.this));
        }

        @Override
        public boolean contains(short k) {
            return AbstractInt2ShortSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractInt2ShortSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractInt2ShortSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator
    implements IntBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Int2ShortMap.Entry> i;

        public KeySetIterator(ObjectBidirectionalIterator<Int2ShortMap.Entry> i) {
            this.i = i;
        }

        @Override
        public int nextInt() {
            return ((Int2ShortMap.Entry)this.i.next()).getIntKey();
        }

        @Override
        public int previousInt() {
            return ((Int2ShortMap.Entry)this.i.previous()).getIntKey();
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
            return AbstractInt2ShortSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractInt2ShortSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractInt2ShortSortedMap.this.clear();
        }

        @Override
        public IntComparator comparator() {
            return AbstractInt2ShortSortedMap.this.comparator();
        }

        @Override
        public int firstInt() {
            return AbstractInt2ShortSortedMap.this.firstIntKey();
        }

        @Override
        public int lastInt() {
            return AbstractInt2ShortSortedMap.this.lastIntKey();
        }

        @Override
        public IntSortedSet headSet(int to) {
            return AbstractInt2ShortSortedMap.this.headMap(to).keySet();
        }

        @Override
        public IntSortedSet tailSet(int from) {
            return AbstractInt2ShortSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public IntSortedSet subSet(int from, int to) {
            return AbstractInt2ShortSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public IntBidirectionalIterator iterator(int from) {
            return new KeySetIterator(AbstractInt2ShortSortedMap.this.int2ShortEntrySet().iterator(new AbstractInt2ShortMap.BasicEntry(from, 0)));
        }

        @Override
        public IntBidirectionalIterator iterator() {
            return new KeySetIterator(Int2ShortSortedMaps.fastIterator(AbstractInt2ShortSortedMap.this));
        }
    }
}

