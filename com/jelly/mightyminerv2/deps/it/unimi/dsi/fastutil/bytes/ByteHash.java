/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.bytes;

public interface ByteHash {

    public static interface Strategy {
        public int hashCode(byte var1);

        public boolean equals(byte var1, byte var2);
    }
}

