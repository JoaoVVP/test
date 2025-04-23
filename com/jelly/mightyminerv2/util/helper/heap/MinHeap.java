/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.util.helper.heap;

import com.jelly.mightyminerv2.util.helper.heap.HeapNode;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinHeap<T> {
    private HeapNode<T>[] items;
    private int size;
    private int capacity;

    public MinHeap(int capacity) {
        this.capacity = capacity;
        this.items = new HeapNode[this.capacity];
        this.size = 0;
    }

    public T poll() {
        int small;
        HeapNode<T> root = this.items[1];
        this.items[1] = this.items[this.size];
        this.items[this.size] = null;
        --this.size;
        int index = 1;
        while ((small = index << 1) <= this.size) {
            if (small < this.size && this.items[small].nodeCost > this.items[small + 1].nodeCost) {
                ++small;
            }
            if (!(this.items[index].nodeCost > this.items[small].nodeCost)) break;
            HeapNode<T> temp = this.items[index];
            this.items[index] = this.items[small];
            this.items[small] = temp;
            index = small;
        }
        return root == null ? null : (T)root.nodeVal;
    }

    private void heapDown(int index) {
    }

    public void add(T pos, double cost) {
        this.add(new HeapNode<T>(pos, cost));
    }

    public void add(HeapNode<T> elem) {
        if (this.size >= this.capacity) {
            this.capacity *= 2;
            this.items = Arrays.copyOf(this.items, this.capacity);
        }
        this.items[++this.size] = elem;
        this.heapUp(this.size - 1);
    }

    public void heapUp(int index) {
        int parentIndex = index >>> 1;
        while (parentIndex > 0 && this.items[index].nodeCost < this.items[parentIndex].nodeCost) {
            this.swap(parentIndex, index);
            index = parentIndex;
            parentIndex = index >>> 1;
        }
    }

    public void swap(int i1, int i2) {
        HeapNode<T> temp = this.items[i1];
        this.items[i1] = this.items[i2];
        this.items[i2] = temp;
    }

    public List<T> getBlocks() {
        HeapNode<T> node;
        ArrayList blocks = new ArrayList();
        for (int i = 1; i < this.items.length && (node = this.items[i]) != null; ++i) {
            blocks.add(node.nodeVal);
        }
        return blocks;
    }
}

