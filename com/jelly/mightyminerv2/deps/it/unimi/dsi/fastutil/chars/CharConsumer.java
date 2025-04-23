/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

@FunctionalInterface
public interface CharConsumer
extends Consumer<Character>,
IntConsumer {
    @Override
    public void accept(char var1);

    @Override
    @Deprecated
    default public void accept(int t) {
        this.accept(SafeMath.safeIntToChar(t));
    }

    @Override
    @Deprecated
    default public void accept(Character t) {
        this.accept(t.charValue());
    }

    default public CharConsumer andThen(CharConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }

    @Override
    @Deprecated
    default public CharConsumer andThen(IntConsumer after) {
        Objects.requireNonNull(after);
        return t -> {
            this.accept(t);
            after.accept(t);
        };
    }

    @Override
    @Deprecated
    default public Consumer<Character> andThen(Consumer<? super Character> after) {
        return Consumer.super.andThen(after);
    }
}

