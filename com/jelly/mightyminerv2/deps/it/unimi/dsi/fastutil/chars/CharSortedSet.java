/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSet;
import java.util.SortedSet;

public interface CharSortedSet
extends CharSet,
SortedSet<Character>,
CharBidirectionalIterable {
    public CharBidirectionalIterator iterator(char var1);

    @Override
    public CharBidirectionalIterator iterator();

    public CharSortedSet subSet(char var1, char var2);

    public CharSortedSet headSet(char var1);

    public CharSortedSet tailSet(char var1);

    public CharComparator comparator();

    public char firstChar();

    public char lastChar();

    @Deprecated
    default public CharSortedSet subSet(Character from, Character to) {
        return this.subSet(from.charValue(), to.charValue());
    }

    @Deprecated
    default public CharSortedSet headSet(Character to) {
        return this.headSet(to.charValue());
    }

    @Deprecated
    default public CharSortedSet tailSet(Character from) {
        return this.tailSet(from.charValue());
    }

    @Override
    @Deprecated
    default public Character first() {
        return Character.valueOf(this.firstChar());
    }

    @Override
    @Deprecated
    default public Character last() {
        return Character.valueOf(this.lastChar());
    }
}

