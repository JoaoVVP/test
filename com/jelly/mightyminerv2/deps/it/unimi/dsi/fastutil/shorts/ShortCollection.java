/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.SafeMath;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterable;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;
import java.util.Collection;
import java.util.Objects;
import java.util.function.IntPredicate;
import java.util.function.Predicate;

public interface ShortCollection
extends Collection<Short>,
ShortIterable {
    @Override
    public ShortIterator iterator();

    @Override
    public boolean add(short var1);

    public boolean contains(short var1);

    public boolean rem(short var1);

    @Override
    @Deprecated
    default public boolean add(Short key) {
        return this.add((short)key);
    }

    @Override
    @Deprecated
    default public boolean contains(Object key) {
        if (key == null) {
            return false;
        }
        return this.contains((Short)key);
    }

    @Override
    @Deprecated
    default public boolean remove(Object key) {
        if (key == null) {
            return false;
        }
        return this.rem((Short)key);
    }

    public short[] toShortArray();

    @Deprecated
    public short[] toShortArray(short[] var1);

    public short[] toArray(short[] var1);

    public boolean addAll(ShortCollection var1);

    public boolean containsAll(ShortCollection var1);

    public boolean removeAll(ShortCollection var1);

    @Override
    @Deprecated
    default public boolean removeIf(Predicate<? super Short> filter) {
        return this.removeIf((int key) -> filter.test(SafeMath.safeIntToShort(key)));
    }

    default public boolean removeIf(IntPredicate filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;
        ShortIterator each = this.iterator();
        while (each.hasNext()) {
            if (!filter.test(each.nextShort())) continue;
            each.remove();
            removed = true;
        }
        return removed;
    }

    public boolean retainAll(ShortCollection var1);
}

