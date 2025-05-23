/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface DoubleBidirectionalIterator
extends DoubleIterator,
ObjectBidirectionalIterator<Double> {
    public double previousDouble();

    @Override
    @Deprecated
    default public Double previous() {
        return this.previousDouble();
    }

    @Override
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousDouble();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return DoubleIterator.super.skip(n);
    }
}

