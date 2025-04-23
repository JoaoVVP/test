/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.BigListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;

public interface CharBigListIterator
extends CharBidirectionalIterator,
BigListIterator<Character> {
    @Override
    default public void set(char k) {
        throw new UnsupportedOperationException();
    }

    @Override
    default public void add(char k) {
        throw new UnsupportedOperationException();
    }

    @Override
    @Deprecated
    default public void set(Character k) {
        this.set(k.charValue());
    }

    @Override
    @Deprecated
    default public void add(Character k) {
        this.add(k.charValue());
    }

    default public long skip(long n) {
        long i = n;
        while (i-- != 0L && this.hasNext()) {
            this.nextChar();
        }
        return n - i - 1L;
    }

    default public long back(long n) {
        long i = n;
        while (i-- != 0L && this.hasPrevious()) {
            this.previousChar();
        }
        return n - i - 1L;
    }

    @Override
    default public int skip(int n) {
        return SafeMath.safeLongToInt(this.skip((long)n));
    }
}

