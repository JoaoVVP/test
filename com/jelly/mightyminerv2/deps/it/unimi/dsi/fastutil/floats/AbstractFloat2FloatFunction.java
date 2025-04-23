/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats.Float2FloatFunction;
import java.io.Serializable;

public abstract class AbstractFloat2FloatFunction
implements Float2FloatFunction,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected float defRetValue;

    protected AbstractFloat2FloatFunction() {
    }

    @Override
    public void defaultReturnValue(float rv) {
        this.defRetValue = rv;
    }

    @Override
    public float defaultReturnValue() {
        return this.defRetValue;
    }
}

