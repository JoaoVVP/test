/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.floats;

public interface FloatHash {

    public static interface Strategy {
        public int hashCode(float var1);

        public boolean equals(float var1, float var2);
    }
}

