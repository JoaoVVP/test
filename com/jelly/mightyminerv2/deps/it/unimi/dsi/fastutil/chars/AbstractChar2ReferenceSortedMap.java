/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractChar2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2ReferenceMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2ReferenceSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2ReferenceSortedMaps;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReferenceCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceCollection;

public abstract class AbstractChar2ReferenceSortedMap<V>
extends AbstractChar2ReferenceMap<V>
implements Char2ReferenceSortedMap<V> {
    private static final long serialVersionUID = -1773560792952436569L;

    protected AbstractChar2ReferenceSortedMap() {
    }

    @Override
    public CharSortedSet keySet() {
        return new KeySet();
    }

    @Override
    public ReferenceCollection<V> values() {
        return new ValuesCollection();
    }

    protected static class ValuesIterator<V>
    implements ObjectIterator<V> {
        protected final ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i;

        public ValuesIterator(ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }

        @Override
        public V next() {
            return ((Char2ReferenceMap.Entry)this.i.next()).getValue();
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }
    }

    protected class ValuesCollection
    extends AbstractReferenceCollection<V> {
        protected ValuesCollection() {
        }

        @Override
        public ObjectIterator<V> iterator() {
            return new ValuesIterator(Char2ReferenceSortedMaps.fastIterator(AbstractChar2ReferenceSortedMap.this));
        }

        @Override
        public boolean contains(Object k) {
            return AbstractChar2ReferenceSortedMap.this.containsValue(k);
        }

        @Override
        public int size() {
            return AbstractChar2ReferenceSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractChar2ReferenceSortedMap.this.clear();
        }
    }

    protected static class KeySetIterator<V>
    implements CharBidirectionalIterator {
        protected final ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i;

        public KeySetIterator(ObjectBidirectionalIterator<Char2ReferenceMap.Entry<V>> i) {
            this.i = i;
        }

        @Override
        public char nextChar() {
            return ((Char2ReferenceMap.Entry)this.i.next()).getCharKey();
        }

        @Override
        public char previousChar() {
            return ((Char2ReferenceMap.Entry)this.i.previous()).getCharKey();
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
    extends AbstractCharSortedSet {
        protected KeySet() {
        }

        @Override
        public boolean contains(char k) {
            return AbstractChar2ReferenceSortedMap.this.containsKey(k);
        }

        @Override
        public int size() {
            return AbstractChar2ReferenceSortedMap.this.size();
        }

        @Override
        public void clear() {
            AbstractChar2ReferenceSortedMap.this.clear();
        }

        @Override
        public CharComparator comparator() {
            return AbstractChar2ReferenceSortedMap.this.comparator();
        }

        @Override
        public char firstChar() {
            return AbstractChar2ReferenceSortedMap.this.firstCharKey();
        }

        @Override
        public char lastChar() {
            return AbstractChar2ReferenceSortedMap.this.lastCharKey();
        }

        @Override
        public CharSortedSet headSet(char to) {
            return AbstractChar2ReferenceSortedMap.this.headMap(to).keySet();
        }

        @Override
        public CharSortedSet tailSet(char from) {
            return AbstractChar2ReferenceSortedMap.this.tailMap(from).keySet();
        }

        @Override
        public CharSortedSet subSet(char from, char to) {
            return AbstractChar2ReferenceSortedMap.this.subMap(from, to).keySet();
        }

        @Override
        public CharBidirectionalIterator iterator(char from) {
            return new KeySetIterator(AbstractChar2ReferenceSortedMap.this.char2ReferenceEntrySet().iterator(new AbstractChar2ReferenceMap.BasicEntry<Object>(from, null)));
        }

        @Override
        public CharBidirectionalIterator iterator() {
            return new KeySetIterator(Char2ReferenceSortedMaps.fastIterator(AbstractChar2ReferenceSortedMap.this));
        }
    }
}

