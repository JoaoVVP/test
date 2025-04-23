/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.FloatIterator;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.DoubleConsumer;

public interface FloatIterable
extends Iterable<Float> {
    public FloatIterator iterator();

    default public void forEach(DoubleConsumer action) {
        Objects.requireNonNull(action);
        FloatIterator iterator = this.iterator();
        while (iterator.hasNext()) {
            action.accept(iterator.nextFloat());
        }
    }

    @Override
    @Deprecated
    default public void forEach(final Consumer<? super Float> action) {
        this.forEach(new DoubleConsumer(){

            @Override
            public void accept(double key) {
                action.accept(Float.valueOf((float)key));
            }
        });
    }
}

