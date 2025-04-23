/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;

public abstract class AbstractObjectSortedSet<K>
extends AbstractObjectSet<K>
implements ObjectSortedSet<K> {
    protected AbstractObjectSortedSet() {
    }

    @Override
    public abstract ObjectBidirectionalIterator<K> iterator();
}

