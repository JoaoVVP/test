/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2LongFunction;
import java.io.Serializable;

public abstract class AbstractChar2LongFunction
implements Char2LongFunction,
Serializable {
    private static final long serialVersionUID = -4940583368468432370L;
    protected long defRetValue;

    protected AbstractChar2LongFunction() {
    }

    @Override
    public void defaultReturnValue(long rv) {
        this.defRetValue = rv;
    }

    @Override
    public long defaultReturnValue() {
        return this.defRetValue;
    }
}

