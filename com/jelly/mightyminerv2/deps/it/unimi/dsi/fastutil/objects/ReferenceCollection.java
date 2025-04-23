/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import java.util.Collection;

public interface ReferenceCollection<K>
extends Collection<K>,
ObjectIterable<K> {
    @Override
    public ObjectIterator<K> iterator();
}

