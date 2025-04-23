/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

public interface ShortHash {

    public static interface Strategy {
        public int hashCode(short var1);

        public boolean equals(short var1, short var2);
    }
}

