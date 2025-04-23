/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.AbstractBooleanSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanCollections;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanIterators;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans.BooleanSet;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

public final class BooleanSets {
    public static final EmptySet EMPTY_SET = new EmptySet();

    private BooleanSets() {
    }

    public static BooleanSet singleton(boolean element) {
        return new Singleton(element);
    }

    public static BooleanSet singleton(Boolean element) {
        return new Singleton(element);
    }

    public static BooleanSet synchronize(BooleanSet s) {
        return new SynchronizedSet(s);
    }

    public static BooleanSet synchronize(BooleanSet s, Object sync) {
        return new SynchronizedSet(s, sync);
    }

    public static BooleanSet unmodifiable(BooleanSet s) {
        return new UnmodifiableSet(s);
    }

    public static class UnmodifiableSet
    extends BooleanCollections.UnmodifiableCollection
    implements BooleanSet,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected UnmodifiableSet(BooleanSet s) {
            super(s);
        }

        @Override
        public boolean remove(boolean k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            return this.collection.equals(o);
        }

        @Override
        public int hashCode() {
            return this.collection.hashCode();
        }

        @Override
        @Deprecated
        public boolean rem(boolean k) {
            return super.rem(k);
        }
    }

    public static class SynchronizedSet
    extends BooleanCollections.SynchronizedCollection
    implements BooleanSet,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected SynchronizedSet(BooleanSet s, Object sync) {
            super(s, sync);
        }

        protected SynchronizedSet(BooleanSet s) {
            super(s);
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean remove(boolean k) {
            Object object = this.sync;
            synchronized (object) {
                return this.collection.rem(k);
            }
        }

        @Override
        @Deprecated
        public boolean rem(boolean k) {
            return super.rem(k);
        }
    }

    public static class Singleton
    extends AbstractBooleanSet
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final boolean element;

        protected Singleton(boolean element) {
            this.element = element;
        }

        @Override
        public boolean contains(boolean k) {
            return k == this.element;
        }

        @Override
        public boolean remove(boolean k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public BooleanListIterator iterator() {
            return BooleanIterators.singleton(this.element);
        }

        @Override
        public int size() {
            return 1;
        }

        @Override
        public boolean addAll(Collection<? extends Boolean> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(Collection<?> c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean addAll(BooleanCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean removeAll(BooleanCollection c) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean retainAll(BooleanCollection c) {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return this;
        }
    }

    public static class EmptySet
    extends BooleanCollections.EmptyCollection
    implements BooleanSet,
    Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptySet() {
        }

        @Override
        public boolean remove(boolean ok) {
            throw new UnsupportedOperationException();
        }

        public Object clone() {
            return EMPTY_SET;
        }

        @Override
        public boolean equals(Object o) {
            return o instanceof Set && ((Set)o).isEmpty();
        }

        @Override
        @Deprecated
        public boolean rem(boolean k) {
            return super.rem(k);
        }

        private Object readResolve() {
            return EMPTY_SET;
        }
    }
}

