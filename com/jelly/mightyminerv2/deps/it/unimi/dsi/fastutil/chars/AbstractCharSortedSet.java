/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSortedSet;

public abstract class AbstractCharSortedSet
extends AbstractCharSet
implements CharSortedSet {
    protected AbstractCharSortedSet() {
    }

    @Override
    public abstract CharBidirectionalIterator iterator();
}

