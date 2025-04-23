/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.IntConsumer;

public interface CharIterable
extends Iterable<Character> {
    public CharIterator iterator();

    default public void forEach(IntConsumer action) {
        Objects.requireNonNull(action);
        CharIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextChar());
        }
    }

    @Override
    @Deprecated
    default public void forEach(final Consumer<? super Character> action) {
        this.forEach(new IntConsumer(){

            @Override
            public void accept(int key) {
                action.accept(Character.valueOf((char)key));
            }
        });
    }
}

