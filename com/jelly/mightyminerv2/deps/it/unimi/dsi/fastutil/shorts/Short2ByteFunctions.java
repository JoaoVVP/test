/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Function;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShort2ByteFunction;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.Short2ByteFunction;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Objects;
import java.util.function.IntUnaryOperator;

public final class Short2ByteFunctions {
    public static final EmptyFunction EMPTY_FUNCTION = new EmptyFunction();

    private Short2ByteFunctions() {
    }

    public static Short2ByteFunction singleton(short key, byte value) {
        return new Singleton(key, value);
    }

    public static Short2ByteFunction singleton(Short key, Byte value) {
        return new Singleton(key, value);
    }

    public static Short2ByteFunction synchronize(Short2ByteFunction f) {
        return new SynchronizedFunction(f);
    }

    public static Short2ByteFunction synchronize(Short2ByteFunction f, Object sync) {
        return new SynchronizedFunction(f, sync);
    }

    public static Short2ByteFunction unmodifiable(Short2ByteFunction f) {
        return new UnmodifiableFunction(f);
    }

    public static Short2ByteFunction primitive(java.util.function.Function<? super Short, ? extends Byte> f) {
        Objects.requireNonNull(f);
        if (f instanceof Short2ByteFunction) {
            return (Short2ByteFunction)f;
        }
        if (f instanceof IntUnaryOperator) {
            return key -> SafeMath.safeIntToByte(((IntUnaryOperator)((Object)f)).applyAsInt(key));
        }
        return new PrimitiveFunction(f);
    }

    public static class PrimitiveFunction
    implements Short2ByteFunction {
        protected final java.util.function.Function<? super Short, ? extends Byte> function;

        protected PrimitiveFunction(java.util.function.Function<? super Short, ? extends Byte> function) {
            this.function = function;
        }

        @Override
        public boolean containsKey(short key) {
            return this.function.apply((Short)key) != null;
        }

        @Override
        @Deprecated
        public boolean containsKey(Object key) {
            if (key == null) {
                return false;
            }
            return this.function.apply((Short)key) != null;
        }

        @Override
        public byte get(short key) {
            Byte v = this.function.apply((Short)key);
            if (v == null) {
                return this.defaultReturnValue();
            }
            return v;
        }

        @Override
        @Deprecated
        public Byte get(Object key) {
            if (key == null) {
                return null;
            }
            return this.function.apply((Short)key);
        }

        @Override
        @Deprecated
        public Byte put(Short key, Byte value) {
            throw new UnsupportedOperationException();
        }
    }

    public static class UnmodifiableFunction
    extends AbstractShort2ByteFunction
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ByteFunction function;

        protected UnmodifiableFunction(Short2ByteFunction f) {
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
        public byte defaultReturnValue() {
            return this.function.defaultReturnValue();
        }

        @Override
        public void defaultReturnValue(byte defRetValue) {
            throw new UnsupportedOperationException();
        }

        @Override
        public boolean containsKey(short k) {
            return this.function.containsKey(k);
        }

        @Override
        public byte put(short k, byte v) {
            throw new UnsupportedOperationException();
        }

        @Override
        public byte get(short k) {
            return this.function.get(k);
        }

        @Override
        public byte remove(short k) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void clear() {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Byte put(Short k, Byte v) {
            throw new UnsupportedOperationException();
        }

        @Override
        @Deprecated
        public Byte get(Object k) {
            return this.function.get(k);
        }

        @Override
        @Deprecated
        public Byte remove(Object k) {
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
    implements Short2ByteFunction,
    Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final Short2ByteFunction function;
        protected final Object sync;

        protected SynchronizedFunction(Short2ByteFunction f, Object sync) {
            if (f == null) {
                throw new NullPointerException();
            }
            this.function = f;
            this.sync = sync;
        }

        protected SynchronizedFunction(Short2ByteFunction f) {
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
        @Deprecated
        public int applyAsInt(int operand) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.applyAsInt(operand);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        @Deprecated
        public Byte apply(Short key) {
            Object object = this.sync;
            synchronized (object) {
                return (Byte)this.function.apply(key);
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
        public byte defaultReturnValue() {
            Object object = this.sync;
            synchronized (object) {
                return this.function.defaultReturnValue();
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public void defaultReturnValue(byte defRetValue) {
            Object object = this.sync;
            synchronized (object) {
                this.function.defaultReturnValue(defRetValue);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public boolean containsKey(short k) {
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
        public byte put(short k, byte v) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.put(k, v);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public byte get(short k) {
            Object object = this.sync;
            synchronized (object) {
                return this.function.get(k);
            }
        }

        /*
         * WARNING - Removed try catching itself - possible behaviour change.
         */
        @Override
        public byte remove(short k) {
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
        public Byte put(Short k, Byte v) {
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
        public Byte get(Object k) {
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
        public Byte remove(Object k) {
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
    extends AbstractShort2ByteFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;
        protected final short key;
        protected final byte value;

        protected Singleton(short key, byte value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public boolean containsKey(short k) {
            return this.key == k;
        }

        @Override
        public byte get(short k) {
            return this.key == k ? this.value : this.defRetValue;
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
    extends AbstractShort2ByteFunction
    implements Serializable,
    Cloneable {
        private static final long serialVersionUID = -7046029254386353129L;

        protected EmptyFunction() {
        }

        @Override
        public byte get(short k) {
            return 0;
        }

        @Override
        public boolean containsKey(short k) {
            return false;
        }

        @Override
        public byte defaultReturnValue() {
            return 0;
        }

        @Override
        public void defaultReturnValue(byte defRetValue) {
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

