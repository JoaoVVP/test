/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDouble2LongFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.Double2LongFunction;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.DoubleToLongFunction;

public final class Double2LongFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Double2LongFunctions() {
    }

    public static Double2LongFunction singleton(double key, long value) {
        return new Singleton(key, value);
    }

    public static Double2LongFunction singleton(Double key, Long value) {
        return new Singleton(key, value);
    }

    public static Double2LongFunction synchronize(Double2LongFunction f) {
        return new SynchronizedFunction(f);
    }

    public static Double2LongFunction synchronize(Double2LongFunction f, Object sync) {
        return new SynchronizedFunction(f, sync);
    }

    public static Double2LongFunction unmodifiable(Double2LongFunction f) {
        return new UnmodifiableFunction(f);
    }

    public static Double2LongFunction primitive(java.util.function.Function<? super Double, ? extends Long> f) {
        Objects.requireNonNull(f);
        if (f instanceof Double2LongFunction) {
            return (Double2LongFunction)f;
        }
        if (f instanceof DoubleToLongFunction) {
            return ((DoubleToLongFunction)((Object)f))::applyAsLong;
        }
        return new PrimitiveFunction(f);
    }

    public static class PrimitiveFunction
    implements Double2LongFunction {
        protected final java.util.function.Function<? super Double, ? extends Long> function;

        protected PrimitiveFunction(java.util.function.Function<? super Double, ? extends Long> function) {
            this.function = function;
        }

        @Override
        public boolean containsKey(double key) {
            return this.function.apply((Double)key) != null;
        }

        @Override
        @Deprecated
        public boolean containsKey(Object key) {
            if (key == null) {
                return false;
            }
            return this.function.apply((Double)key) != null;
        }

        @Override
        public long get(double key) {
            Long v = this.function.apply((Double)key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }

        @Override
        @Deprecated
        public Long get(Object key) {
            if (key == null) {
                return null;
            }
            return this.function.apply((Double)key);
        }

        @Override
        @Deprecated
        public Long put(Double key, Long value) {
            throw new UnsupportedOperationException();
        }
    }

    public static class UnmodifiableFunction
    extends AbstractDouble2LongFunction
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2LongFunction function;

        protected UnmodifiableFunction(Double2LongFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
        }

        @Override
        public int size() {
            return this.function.size();
        }

        @Override
        public long defaultReturnValue() {
            return this.function.defaultReturnValue();
        }

        @Override
        public void defaultReturnValue(long defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsKey(double k) {
            return this.function.containsKey(k);
        }

        @Override
        public long put(double k, long v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public long get(double k) {
            return this.function.get(k);
        }

        @Override
        public long remove(double k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Long put(Double k, Long v) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Long get(Object k) {
            return this.function.get(k);
        }

        @Override
        @Deprecated
        public Long remove(Object k) {
            throw new UnsupportedOperationException();
        }

        public int hashCode() {
            return this.function.hashCode();
        }

        public boolean equals(Object o) {
            return o == this || this.function.equals(o);
        }

        public String toString() {
            return this.function.toString();
        }
    }

    public static class SynchronizedFunction
    implements Double2LongFunction,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Double2LongFunction function;
        protected final Object sync;

        protected SynchronizedFunction(Double2LongFunction f, Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }

        protected SynchronizedFunction(Double2LongFunction f) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = this;
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long applyAsLong(double operand) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.applyAsLong(operand);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Long apply(Double key) {
            Object object = this.sync;
            synchronized (object) {
                return (Long)this.function.apply(key);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public int size() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.size();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long defaultReturnValue() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.defaultReturnValue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void defaultReturnValue(long defRetValue) {
            Object object = this.sync;
            synchronized (object) {
                this.function.defaultReturnValue(defRetValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(double k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.containsKey(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public boolean containsKey(Object k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.containsKey(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long put(double k, long v) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(k, v);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long get(double k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.get(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public long remove(double k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.remove(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void clear() {
            Object object = this.sync;
            synchronized (object) {
                this.function.clear();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Long put(Double k, Long v) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(k, v);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Long get(Object k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.get(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Long remove(Object k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.remove(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public int hashCode() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.hashCode();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            Object object = this.sync;
            synchronized (object) {
                return this.function.equals(o);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        public String toString() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.toString();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        private void writeObject(ObjectOutputStream s) throws IOException {
            Object object = this.sync;
            synchronized (object) {
                s.defaultWriteObject();
            }
        }
    }

    public static class Singleton
    extends AbstractDouble2LongFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final double key;
        protected final long value;

        protected Singleton(double key, long value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean containsKey(double k) {
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k);
        }

        @Override
        public long get(double k) {
            return Double.doubleToLongBits(this.key) == Double.doubleToLongBits(k) ? this.value : this.defRetValue;
        }

        @Override
        public int size() {
            return 1;
        }

        public Object clone() {
            return this;
        }
    }

    public static class EmptyFunction
    extends AbstractDouble2LongFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyFunction() {
        }

        @Override
        public long get(double k) {
            return 0L;
        }

        @Override
        public boolean containsKey(double k) {
            return false;
        }

        @Override
        public long defaultReturnValue() {
            return 0L;
        }

        @Override
        public void defaultReturnValue(long defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int size() {
            return 0;
        }

        @Override
        public void clear() {
        }

        public Object clone() {
            return EMPTY_FUNCTION;
        }

        public int hashCode() {
            return 0;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Function)) {
                return false;
            }
            return ((Function)o).size() == 0;
        }

        public String toString() {
            return "{}";
        }

        private Object readResolve() {
            return EMPTY_FUNCTION;
        }
    }
}

