/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;

public interface ObjectBidirectionalIterator<K>
extends ObjectIterator<K>,
BidirectionalIterator<K> {
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previous();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return ObjectIterator.super.skip(n);
    }
}

