/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;

public interface CharBidirectionalIterator
extends CharIterator,
ObjectBidirectionalIterator<Character> {
    public char previousChar();

    @Override
    @Deprecated
    default public Character previous() {
        return Character.valueOf(this.previousChar());
    }

    @Override
    default public int back(int n) {
        int i = n;
        while (i-- != 0 && this.hasPrevious()) {
            this.previousChar();
        }
        return n - i - 1;
    }

    @Override
    default public int skip(int n) {
        return CharIterator.super.skip(n);
    }
}

