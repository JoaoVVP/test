/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles;

public interface DoubleHash {

    public static interface Strategy {
        public int hashCode(double var1);

        public boolean equals(double var1, double var3);
    }
}

