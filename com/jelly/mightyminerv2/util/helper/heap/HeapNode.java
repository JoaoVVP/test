/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.util.helper.heap;

public class HeapNode<T> {
    public final T nodeVal;
    public final double nodeCost;

    public HeapNode(T nodeVal, double nodeCost) {
        this.nodeVal = nodeVal;
        this.nodeCost = nodeCost;
    }
}

