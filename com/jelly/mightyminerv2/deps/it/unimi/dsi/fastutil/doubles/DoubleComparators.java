/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleComparator;
import java.io.Serializable;
import java.util.Comparator;

public final class DoubleComparators {
    public static final DoubleComparator NATURAL_COMPARATOR = new NaturalImplicitComparator();
    public static final DoubleComparator OPPOSITE_COMPARATOR = new OppositeImplicitComparator();

    private DoubleComparators() {
    }

    public static DoubleComparator oppositeComparator(DoubleComparator c) {
        return new OppositeComparator(c);
    }

    public static DoubleComparator asDoubleComparator(final Comparator<? super Double> c) {
        if (c == null || c instanceof DoubleComparator) {
            return (DoubleComparator)c;
        }
        return new DoubleComparator(){

            @Override
            public int compare(double x, double y) {
                return c.compare(x, y);
            }

            @Override
            public int compare(Double x, Double y) {
                return c.compare(x, y);
            }
        };
    }

    protected static class OppositeComparator
    implements DoubleComparator,
    Serializable {
        private static final long serialVersionUID = 1L;
        private final DoubleComparator comparator;

        protected OppositeComparator(DoubleComparator c) {
            this.comparator = c;
        }

        @Override
        public final int compare(double a, double b) {
            return this.comparator.compare(b, a);
        }
    }

    protected static class OppositeImplicitComparator
    implements DoubleComparator,
    Serializable {
        private static final long serialVersionUID = 1L;

        protected OppositeImplicitComparator() {
        }

        @Override
        public final int compare(double a, double b) {
            return -Double.compare(a, b);
        }

        private Object readResolve() {
            return OPPOSITE_COMPARATOR;
        }
    }

    protected static class NaturalImplicitComparator
    implements DoubleComparator,
    Serializable {
        private static final long serialVersionUID = 1L;

        protected NaturalImplicitComparator() {
        }

        @Override
        public final int compare(double a, double b) {
            return Double.compare(a, b);
        }

        private Object readResolve() {
            return NATURAL_COMPARATOR;
        }
    }
}

