/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.AbstractLongCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.longs.LongSet;
import java.util.Set;

public abstract class AbstractLongSet
extends AbstractLongCollection
implements Cloneable,
LongSet {
    protected AbstractLongSet() {
    }

    @Override
    public abstract LongIterator iterator();

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Set)) {
            return false;
        }
        Set s = (Set)o;
        if (s.size() != this.size()) {
            return false;
        }
        return this.containsAll(s);
    }

    @Override
    public int hashCode() {
        int h = 0;
        int n = this.size();
        LongIterator i = this.iterator();
        while (n-- != 0) {
            long k = i.nextLong();
            h += HashCommon.long2int(k);
        }
        return h;
    }

    @Override
    public boolean remove(long k) {
        return super.rem(k);
    }

    @Override
    @Deprecated
    public boolean rem(long k) {
        return this.remove(k);
    }
}

