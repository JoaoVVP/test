/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  kotlin.Metadata
 *  kotlin.jvm.internal.DefaultConstructorMarker
 *  kotlin.jvm.internal.Intrinsics
 *  org.jetbrains.annotations.NotNull
 */
package com.jelly.mightyminerv2.pathfinder.calculate.openset;

import com.jelly.mightyminerv2.pathfinder.calculate.PathNode;
import java.util.Arrays;
import kotlin.Metadata;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import org.jetbrains.annotations.NotNull;

@Metadata(mv={1, 9, 0}, k=1, xi=48, d1={"\u0000.\n\u0002\u0018\u0002\n\u0002\u0010\u0000\n\u0000\n\u0002\u0010\b\n\u0002\b\u0002\n\u0002\u0010\u0011\n\u0002\u0018\u0002\n\u0002\b\n\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010\u000b\n\u0002\b\u0003\u0018\u00002\u00020\u0001B\u000f\u0012\b\b\u0002\u0010\u0002\u001a\u00020\u0003\u00a2\u0006\u0002\u0010\u0004J\u000e\u0010\u0011\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0007J\u0006\u0010\u0014\u001a\u00020\u0015J\u0006\u0010\u0016\u001a\u00020\u0007J\u000e\u0010\u0017\u001a\u00020\u00122\u0006\u0010\u0013\u001a\u00020\u0007R$\u0010\u0005\u001a\n\u0012\u0006\u0012\u0004\u0018\u00010\u00070\u0006X\u0086\u000e\u00a2\u0006\u0010\n\u0002\u0010\f\u001a\u0004\b\b\u0010\t\"\u0004\b\n\u0010\u000bR\u001a\u0010\r\u001a\u00020\u0003X\u0086\u000e\u00a2\u0006\u000e\n\u0000\u001a\u0004\b\u000e\u0010\u000f\"\u0004\b\u0010\u0010\u0004\u00a8\u0006\u0018"}, d2={"Lcom/jelly/mightyminerv2/pathfinder/calculate/openset/BinaryHeapOpenSet;", "", "initialSize", "", "(I)V", "items", "", "Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "getItems", "()[Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "setItems", "([Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;)V", "[Lcom/jelly/mightyminerv2/pathfinder/calculate/PathNode;", "size", "getSize", "()I", "setSize", "add", "", "node", "isEmpty", "", "poll", "relocate", "MightyMinerV2"})
public final class BinaryHeapOpenSet {
    @NotNull
    private PathNode[] items;
    private int size;

    public BinaryHeapOpenSet(int initialSize) {
        this.items = new PathNode[initialSize];
    }

    public /* synthetic */ BinaryHeapOpenSet(int n, int n2, DefaultConstructorMarker defaultConstructorMarker) {
        if ((n2 & 1) != 0) {
            n = 1024;
        }
        this(n);
    }

    @NotNull
    public final PathNode[] getItems() {
        return this.items;
    }

    public final void setItems(@NotNull PathNode[] pathNodeArray) {
        Intrinsics.checkNotNullParameter((Object)pathNodeArray, (String)"<set-?>");
        this.items = pathNodeArray;
    }

    public final int getSize() {
        return this.size;
    }

    public final void setSize(int n) {
        this.size = n;
    }

    public final void add(@NotNull PathNode node) {
        Intrinsics.checkNotNullParameter((Object)node, (String)"node");
        if (this.size >= this.items.length - 1) {
            PathNode[] pathNodeArray = Arrays.copyOf(this.items, this.items.length << 1);
            Intrinsics.checkNotNullExpressionValue((Object)pathNodeArray, (String)"copyOf(...)");
            this.items = pathNodeArray;
        }
        ++this.size;
        node.setHeapPosition(this.size);
        this.items[this.size] = node;
        this.relocate(node);
    }

    public final void relocate(@NotNull PathNode node) {
        Intrinsics.checkNotNullParameter((Object)node, (String)"node");
        int parent = node.getHeapPosition() >>> 1;
        PathNode parentNode = this.items[parent];
        while (node.getHeapPosition() > 1) {
            double d = node.getTotalCost();
            PathNode pathNode = parentNode;
            Intrinsics.checkNotNull((Object)pathNode);
            if (!(d < pathNode.getTotalCost())) break;
            this.items[node.getHeapPosition()] = parentNode;
            this.items[parent] = node;
            node.setHeapPosition(parent);
            parentNode = this.items[parent >>>= 1];
        }
    }

    @NotNull
    public final PathNode poll() {
        PathNode itemToSwap;
        PathNode itemToPoll;
        PathNode pathNode = itemToPoll = this.items[1];
        Intrinsics.checkNotNull((Object)pathNode);
        pathNode.setHeapPosition(-1);
        int n = this.size;
        this.size = n + -1;
        PathNode pathNode2 = itemToSwap = this.items[n];
        Intrinsics.checkNotNull((Object)pathNode2);
        pathNode2.setHeapPosition(1);
        this.items[1] = itemToSwap;
        double itemToSwapCost = itemToSwap.getTotalCost();
        if (this.size <= 1) {
            return itemToPoll;
        }
        int parentIndex = 1;
        int smallestChildIndex = 2;
        while (smallestChildIndex <= this.size) {
            PathNode swapTemp;
            int rightChildIndex = smallestChildIndex + 1;
            if (rightChildIndex < this.size) {
                PathNode pathNode3 = this.items[rightChildIndex];
                Intrinsics.checkNotNull((Object)pathNode3);
                double d = pathNode3.getTotalCost();
                PathNode pathNode4 = this.items[smallestChildIndex];
                Intrinsics.checkNotNull((Object)pathNode4);
                if (d < pathNode4.getTotalCost()) {
                    smallestChildIndex = rightChildIndex;
                }
            }
            PathNode pathNode5 = this.items[smallestChildIndex];
            Intrinsics.checkNotNull((Object)pathNode5);
            if (pathNode5.getTotalCost() >= itemToSwapCost) break;
            PathNode pathNode6 = swapTemp = this.items[smallestChildIndex];
            Intrinsics.checkNotNull((Object)pathNode6);
            pathNode6.setHeapPosition(parentIndex);
            this.items[parentIndex] = swapTemp;
            itemToSwap.setHeapPosition(smallestChildIndex);
            this.items[smallestChildIndex] = itemToSwap;
            parentIndex = smallestChildIndex;
            smallestChildIndex = parentIndex << 1;
        }
        return itemToPoll;
    }

    public final boolean isEmpty() {
        return this.size <= 0;
    }

    public BinaryHeapOpenSet() {
        this(0, 1, null);
    }
}

