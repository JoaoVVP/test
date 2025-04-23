/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharConsumer;
import java.util.Iterator;
import java.util.Objects;
import java.util.function.Consumer;

public interface CharIterator
extends Iterator<Character> {
    public char nextChar();

    @Override
    @Deprecated
    default public Character next() {
        return Character.valueOf(this.nextChar());
    }

    default public void forEachRemaining(CharConsumer action) {
        Objects.requireNonNull(action);
        while (this.hasNext()) {
            action.accept(this.nextChar());
        }
    }

    @Override
    @Deprecated
    default public void forEachRemaining(Consumer<? super Character> action) {
        this.forEachRemaining(action::accept);
    }

    default public int skip(int n) {
        if (n < 0) {
            throw new IllegalArgumentException("Argument must be nonnegative: " + n);
        }
        int i = n;
        while (i-- != 0 && this.hasNext()) {
            this.nextChar();
        }
        return n - i - 1;
    }
}

