/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ReferenceCollection;
import java.util.Set;

public interface ReferenceSet<K>
extends ReferenceCollection<K>,
Set<K> {
    @Override
    public ObjectIterator<K> iterator();
}

