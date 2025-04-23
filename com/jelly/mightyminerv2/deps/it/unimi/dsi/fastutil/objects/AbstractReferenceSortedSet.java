/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractReferenceSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceSortedSet;

public abstract class AbstractReferenceSortedSet<K>
extends AbstractReferenceSet<K>
implements ReferenceSortedSet<K> {
    protected AbstractReferenceSortedSet() {
    }

    @Override
    public abstract ObjectBidirectionalIterator<K> iterator();
}

