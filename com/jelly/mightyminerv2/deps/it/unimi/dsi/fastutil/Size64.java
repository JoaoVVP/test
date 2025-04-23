/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil;

public interface Size64 {
    public long size64();

    @Deprecated
    default public int size() {
        return (int)Math.min(Integer.MAX_VALUE, this.size64());
    }
}

