/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.booleans;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.Stack;

public interface BooleanStack
extends Stack<Boolean> {
    @Override
    public void push(boolean var1);

    public boolean popBoolean();

    public boolean topBoolean();

    public boolean peekBoolean(int var1);

    @Override
    @Deprecated
    default public void push(Boolean o) {
        this.push((boolean)o);
    }

    @Override
    @Deprecated
    default public Boolean pop() {
        return this.popBoolean();
    }

    @Override
    @Deprecated
    default public Boolean top() {
        return this.topBoolean();
    }

    @Override
    @Deprecated
    default public Boolean peek(int i) {
        return this.peekBoolean(i);
    }
}

