/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2ObjectFunction;
import java.io.Serializable;

public abstract class AbstractFloat2ObjectFunction<V>
implements Float2ObjectFunction<V>,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected V defRetValue;

    protected AbstractFloat2ObjectFunction() {
    }

    @Override
    public void defaultReturnValue(V rv) {
        this.defRetValue = rv;
    }

    @Override
    public V defaultReturnValue() {
        return this.defRetValue;
    }
}

