/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Stack;

public interface FloatStack
extends Stack<Float> {
    @Override
    public void push(float var1);

    public float popFloat();

    public float topFloat();

    public float peekFloat(int var1);

    @Override
    @Deprecated
    default public void push(Float o) {
        this.push(o.floatValue());
    }

    @Override
    @Deprecated
    default public Float pop() {
        return Float.valueOf(this.popFloat());
    }

    @Override
    @Deprecated
    default public Float top() {
        return Float.valueOf(this.topFloat());
    }

    @Override
    @Deprecated
    default public Float peek(int i) {
        return Float.valueOf(this.peekFloat(i));
    }
}

