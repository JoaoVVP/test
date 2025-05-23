/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectArrayList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectList;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectListIterator;
import java.io.Serializable;
import java.util.Iterator;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

public final class ObjectIterators {
    public static final EmptyIterator EMPTY_ITERATOR = new EmptyIterator();

    private ObjectIterators() {
    }

    public static <K> ObjectIterator<K> emptyIterator() {
        return EMPTY_ITERATOR;
    }

    public static <K> ObjectListIterator<K> singleton(K element) {
        return new SingletonIterator<K>(element);
    }

    public static <K> ObjectListIterator<K> wrap(K[] array, int offset, int length) {
        ObjectArrays.ensureOffsetLength(array, offset, length);
        return new ArrayIterator<K>(array, offset, length);
    }

    public static <K> ObjectListIterator<K> wrap(K[] array) {
        return new ArrayIterator<K>(array, 0, array.length);
    }

    public static <K> int unwrap(Iterator<? extends K> i, K[] array, int offset, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        if (offset < 0 || offset + max > array.length) {
            throw new IllegalArgumentException();
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            array[offset++] = i.next();
        }
        return max - j - 1;
    }

    public static <K> int unwrap(Iterator<? extends K> i, K[] array) {
        return ObjectIterators.unwrap(i, array, 0, array.length);
    }

    public static <K> K[] unwrap(Iterator<? extends K> i, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        Object[] array = new Object[16];
        int j = 0;
        while (max-- != 0 && i.hasNext()) {
            if (j == array.length) {
                array = ObjectArrays.grow(array, j + 1);
            }
            array[j++] = i.next();
        }
        return ObjectArrays.trim(array, j);
    }

    public static <K> K[] unwrap(Iterator<? extends K> i) {
        return ObjectIterators.unwrap(i, Integer.MAX_VALUE);
    }

    public static <K> int unwrap(Iterator<K> i, ObjectCollection<? super K> c, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            c.add(i.next());
        }
        return max - j - 1;
    }

    public static <K> long unwrap(Iterator<K> i, ObjectCollection<? super K> c) {
        long n = 0L;
        while (i.hasNext()) {
            c.add(i.next());
            ++n;
        }
        return n;
    }

    public static <K> int pour(Iterator<K> i, ObjectCollection<? super K> s, int max) {
        if (max < 0) {
            throw new IllegalArgumentException("The maximum number of elements (" + max + ") is negative");
        }
        int j = max;
        while (j-- != 0 && i.hasNext()) {
            s.add(i.next());
        }
        return max - j - 1;
    }

    public static <K> int pour(Iterator<K> i, ObjectCollection<? super K> s) {
        return ObjectIterators.pour(i, s, Integer.MAX_VALUE);
    }

    public static <K> ObjectList<K> pour(Iterator<K> i, int max) {
        ObjectArrayList l = new ObjectArrayList();
        ObjectIterators.pour(i, l, max);
        l.trim();
        return l;
    }

    public static <K> ObjectList<K> pour(Iterator<K> i) {
        return ObjectIterators.pour(i, Integer.MAX_VALUE);
    }

    public static <K> ObjectIterator<K> asObjectIterator(Iterator<K> i) {
        if (i instanceof ObjectIterator) {
            return (ObjectIterator)i;
        }
        return new IteratorWrapper<K>(i);
    }

    public static <K> ObjectListIterator<K> asObjectIterator(ListIterator<K> i) {
        if (i instanceof ObjectListIterator) {
            return (ObjectListIterator)i;
        }
        return new ListIteratorWrapper<K>(i);
    }

    public static <K> boolean any(ObjectIterator<K> iterator, Predicate<? super K> predicate) {
        return ObjectIterators.indexOf(iterator, predicate) != -1;
    }

    public static <K> boolean all(ObjectIterator<K> iterator, Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        do {
            if (iterator.hasNext()) continue;
            return true;
        } while (predicate.test(iterator.next()));
        return false;
    }

    public static <K> int indexOf(ObjectIterator<K> iterator, Predicate<? super K> predicate) {
        Objects.requireNonNull(predicate);
        int i = 0;
        while (iterator.hasNext()) {
            if (predicate.test(iterator.next())) {
                return i;
            }
            ++i;
        }
        return -1;
    }

    public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>[] a) {
        return ObjectIterators.concat(a, 0, a.length);
    }

    public static <K> ObjectIterator<K> concat(ObjectIterator<? extends K>[] a, int offset, int length) {
        return new IteratorConcatenator<K>(a, offset, length);
    }

    public static <K> ObjectIterator<K> unmodifiable(ObjectIterator<K> i) {
        return new UnmodifiableIterator<K>(i);
    }

    public static <K> ObjectBidirectionalIterator<K> unmodifiable(ObjectBidirectionalIterator<K> i) {
        return new UnmodifiableBidirectionalIterator<K>(i);
    }

    public static <K> ObjectListIterator<K> unmodifiable(ObjectListIterator<K> i) {
        return new UnmodifiableListIterator<K>(i);
    }

    public static class UnmodifiableListIterator<K>
    implements ObjectListIterator<K> {
        protected final ObjectListIterator<K> i;

        public UnmodifiableListIterator(ObjectListIterator<K> i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override
        public K next() {
            return (K)this.i.next();
        }

        @Override
        public K previous() {
            return this.i.previous();
        }

        @Override
        public int nextIndex() {
            return this.i.nextIndex();
        }

        @Override
        public int previousIndex() {
            return this.i.previousIndex();
        }
    }

    public static class UnmodifiableBidirectionalIterator<K>
    implements ObjectBidirectionalIterator<K> {
        protected final ObjectBidirectionalIterator<K> i;

        public UnmodifiableBidirectionalIterator(ObjectBidirectionalIterator<K> i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override
        public K next() {
            return (K)this.i.next();
        }

        @Override
        public K previous() {
            return this.i.previous();
        }
    }

    public static class UnmodifiableIterator<K>
    implements ObjectIterator<K> {
        protected final ObjectIterator<K> i;

        public UnmodifiableIterator(ObjectIterator<K> i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public K next() {
            return (K)this.i.next();
        }
    }

    private static class IteratorConcatenator<K>
    implements ObjectIterator<K> {
        final ObjectIterator<? extends K>[] a;
        int offset;
        int length;
        int lastOffset = -1;

        public IteratorConcatenator(ObjectIterator<? extends K>[] a, int offset, int length) {
            this.a = a;
            this.offset = offset;
            this.length = length;
            this.advance();
        }

        private void advance() {
            while (this.length != 0 && !this.a[this.offset].hasNext()) {
                --this.length;
                ++this.offset;
            }
        }

        @Override
        public boolean hasNext() {
            return this.length > 0;
        }

        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.lastOffset = this.offset;
            Object next = this.a[this.lastOffset].next();
            this.advance();
            return (K)next;
        }

        @Override
        public void remove() {
            if (this.lastOffset == -1) {
                throw new IllegalStateException();
            }
            this.a[this.lastOffset].remove();
        }

        @Override
        public int skip(int n) {
            this.lastOffset = -1;
            int skipped = 0;
            while (skipped < n && this.length != 0) {
                skipped += this.a[this.offset].skip(n - skipped);
                if (this.a[this.offset].hasNext()) break;
                --this.length;
                ++this.offset;
            }
            return skipped;
        }
    }

    private static class ListIteratorWrapper<K>
    implements ObjectListIterator<K> {
        final ListIterator<K> i;

        public ListIteratorWrapper(ListIterator<K> i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public boolean hasPrevious() {
            return this.i.hasPrevious();
        }

        @Override
        public int nextIndex() {
            return this.i.nextIndex();
        }

        @Override
        public int previousIndex() {
            return this.i.previousIndex();
        }

        @Override
        public void set(K k) {
            this.i.set(k);
        }

        @Override
        public void add(K k) {
            this.i.add(k);
        }

        @Override
        public void remove() {
            this.i.remove();
        }

        @Override
        public K next() {
            return this.i.next();
        }

        @Override
        public K previous() {
            return this.i.previous();
        }
    }

    private static class IteratorWrapper<K>
    implements ObjectIterator<K> {
        final Iterator<K> i;

        public IteratorWrapper(Iterator<K> i) {
            this.i = i;
        }

        @Override
        public boolean hasNext() {
            return this.i.hasNext();
        }

        @Override
        public void remove() {
            this.i.remove();
        }

        @Override
        public K next() {
            return this.i.next();
        }
    }

    private static class ArrayIterator<K>
    implements ObjectListIterator<K> {
        private final K[] array;
        private final int offset;
        private final int length;
        private int curr;

        public ArrayIterator(K[] array, int offset, int length) {
            this.array = array;
            this.offset = offset;
            this.length = length;
        }

        @Override
        public boolean hasNext() {
            return this.curr < this.length;
        }

        @Override
        public boolean hasPrevious() {
            return this.curr > 0;
        }

        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + this.curr++];
        }

        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            return this.array[this.offset + --this.curr];
        }

        @Override
        public int skip(int n) {
            if (n <= this.length - this.curr) {
                this.curr += n;
                return n;
            }
            n = this.length - this.curr;
            this.curr = this.length;
            return n;
        }

        @Override
        public int back(int n) {
            if (n <= this.curr) {
                this.curr -= n;
                return n;
            }
            n = this.curr;
            this.curr = 0;
            return n;
        }

        @Override
        public int nextIndex() {
            return this.curr;
        }

        @Override
        public int previousIndex() {
            return this.curr - 1;
        }
    }

    private static class SingletonIterator<K>
    implements ObjectListIterator<K> {
        private final K element;
        private int curr;

        public SingletonIterator(K element) {
            this.element = element;
        }

        @Override
        public boolean hasNext() {
            return this.curr == 0;
        }

        @Override
        public boolean hasPrevious() {
            return this.curr == 1;
        }

        @Override
        public K next() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = 1;
            return this.element;
        }

        @Override
        public K previous() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = 0;
            return this.element;
        }

        @Override
        public int nextIndex() {
            return this.curr;
        }

        @Override
        public int previousIndex() {
            return this.curr - 1;
        }
    }

    public static class EmptyIterator<K>
    implements ObjectListIterator<K>,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyIterator() {
        }

        @Override
        public boolean hasNext() {
            return false;
        }

        @Override
        public boolean hasPrevious() {
            return false;
        }

        @Override
        public K next() {
            throw new NoSuchElementException();
        }

        @Override
        public K previous() {
            throw new NoSuchElementException();
        }

        @Override
        public int nextIndex() {
            return 0;
        }

        @Override
        public int previousIndex() {
            return -1;
        }

        @Override
        public int skip(int n) {
            return 0;
        }

        @Override
        public int back(int n) {
            return 0;
        }

        public Object clone() {
            return EMPTY_ITERATOR;
        }

        private Object readResolve() {
            return EMPTY_ITERATOR;
        }
    }
}

