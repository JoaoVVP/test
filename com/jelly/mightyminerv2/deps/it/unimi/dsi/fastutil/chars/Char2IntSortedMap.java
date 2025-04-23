/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2IntMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.util.Map;
import java.util.SortedMap;

public interface Char2IntSortedMap
extends Char2IntMap,
SortedMap<Character, Integer> {
    public Char2IntSortedMap subMap(char var1, char var2);

    public Char2IntSortedMap headMap(char var1);

    public Char2IntSortedMap tailMap(char var1);

    public char firstCharKey();

    public char lastCharKey();

    @Deprecated
    default public Char2IntSortedMap subMap(Character from, Character to) {
        return this.subMap(from.charValue(), to.charValue());
    }

    @Deprecated
    default public Char2IntSortedMap headMap(Character to) {
        return this.headMap(to.charValue());
    }

    @Deprecated
    default public Char2IntSortedMap tailMap(Character from) {
        return this.tailMap(from.charValue());
    }

    @Override
    @Deprecated
    default public Character firstKey() {
        return Character.valueOf(this.firstCharKey());
    }

    @Override
    @Deprecated
    default public Character lastKey() {
        return Character.valueOf(this.lastCharKey());
    }

    @Override
    @Deprecated
    default public ObjectSortedSet<Map.Entry<Character, Integer>> entrySet() {
        return this.char2IntEntrySet();
    }

    public ObjectSortedSet<Char2IntMap.Entry> char2IntEntrySet();

    @Override
    public CharSortedSet keySet();

    @Override
    public IntCollection values();

    public CharComparator comparator();

    public static interface FastSortedEntrySet
    extends ObjectSortedSet<Char2IntMap.Entry>,
    Char2IntMap.FastEntrySet {
        public ObjectBidirectionalIterator<Char2IntMap.Entry> fastIterator();

        public ObjectBidirectionalIterator<Char2IntMap.Entry> fastIterator(Char2IntMap.Entry var1);
    }
}

