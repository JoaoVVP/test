/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.gson.annotations.Expose
 */
package com.jelly.mightyminerv2.util.helper.graph;

import com.google.gson.annotations.Expose;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Graph<T> {
    @Expose
    public final Map<T, List<T>> map = new HashMap<T, List<T>>();

    public void add(T source) {
        if (!this.map.isEmpty()) {
            return;
        }
        this.map.computeIfAbsent(source, k -> new ArrayList());
    }

    public void add(T source, T target, boolean bidi) {
        this.map.computeIfAbsent(source, k -> new ArrayList()).add(target);
        this.map.computeIfAbsent(target, k -> new ArrayList());
        if (bidi) {
            this.map.get(target).add(source);
        }
    }

    public void update(T old, T now) {
        if (old == null || now == null) {
            throw new IllegalArgumentException("Nodes cannot be null");
        }
        List<T> edges = this.map.remove(old);
        if (edges != null) {
            this.map.put(now, edges);
        }
        for (Map.Entry<T, List<T>> entry : this.map.entrySet()) {
            ArrayList<T> updatedEdges = new ArrayList<T>();
            for (T edge : entry.getValue()) {
                if (edge.equals(old)) {
                    updatedEdges.add(now);
                    continue;
                }
                updatedEdges.add(edge);
            }
            this.map.put(entry.getKey(), updatedEdges);
        }
    }

    public void remove(T node) {
        if (this.map.remove(node) == null) {
            return;
        }
        for (T key : this.map.keySet()) {
            this.map.get(key).removeIf(edge -> edge.equals(node));
        }
    }

    public List<T> findPath(T start2, T end) {
        if (start2 == null || end == null || !this.map.containsKey(start2) || !this.map.containsKey(end)) {
            return new ArrayList();
        }
        LinkedList<T> queue = new LinkedList<T>();
        HashSet<T> visited = new HashSet<T>();
        HashMap parent = new HashMap();
        queue.add(start2);
        visited.add(start2);
        parent.put(start2, null);
        while (!queue.isEmpty()) {
            Object curr = queue.poll();
            if (curr.equals(end)) {
                LinkedList<T> path = new LinkedList<T>();
                Object at = end;
                while (at != null) {
                    path.addFirst(at);
                    at = parent.get(at);
                }
                return path;
            }
            for (T neighbour : this.map.get(curr)) {
                if (visited.contains(neighbour)) continue;
                queue.offer(neighbour);
                visited.add(neighbour);
                parent.put(neighbour, curr);
            }
        }
        return new ArrayList();
    }
}

