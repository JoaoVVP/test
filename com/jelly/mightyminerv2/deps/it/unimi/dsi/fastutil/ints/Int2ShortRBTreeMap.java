/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.AbstractInt2ShortSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ShortMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.Int2ShortSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntComparators;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.ints.IntSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.AbstractShortCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.shorts.ShortListIterator;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class Int2ShortRBTreeMap
extends AbstractInt2ShortSortedMap
implements Serializable,
Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Int2ShortMap.Entry> entries;
    protected transient IntSortedSet keys;
    protected transient ShortCollection values;
    protected transient boolean modified;
    protected Comparator<? super Integer> storedComparator;
    protected transient IntComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;

    public Int2ShortRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = IntComparators.asIntComparator(this.storedComparator);
    }

    public Int2ShortRBTreeMap(Comparator<? super Integer> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }

    public Int2ShortRBTreeMap(Map<? extends Integer, ? extends Short> m) {
        this();
        this.putAll(m);
    }

    public Int2ShortRBTreeMap(SortedMap<Integer, Short> m) {
        this(m.comparator());
        this.putAll((Map<? extends Integer, ? extends Short>)m);
    }

    public Int2ShortRBTreeMap(Int2ShortMap m) {
        this();
        this.putAll(m);
    }

    public Int2ShortRBTreeMap(Int2ShortSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }

    public Int2ShortRBTreeMap(int[] k, short[] v, Comparator<? super Integer> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }

    public Int2ShortRBTreeMap(int[] k, short[] v) {
        this(k, v, null);
    }

    final int compare(int k1, int k2) {
        return this.actualComparator == null ? Integer.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }

    final Entry findKey(int k) {
        int cmp;
        Entry e = this.tree;
        while (e != null && (cmp = this.compare(k, e.key)) != 0) {
            e = cmp < 0 ? e.left() : e.right();
        }
        return e;
    }

    final Entry locateKey(int k) {
        Entry e = this.tree;
        Entry last = this.tree;
        int cmp = 0;
        while (e != null && (cmp = this.compare(k, e.key)) != 0) {
            last = e;
            e = cmp < 0 ? e.left() : e.right();
        }
        return cmp == 0 ? e : last;
    }

    private void allocatePaths() {
        this.dirPath = new boolean[64];
        this.nodePath = new Entry[64];
    }

    public short addTo(int k, short incr) {
        Entry e = this.add(k);
        short oldValue = e.value;
        e.value = (short)(e.value + incr);
        return oldValue;
    }

    @Override
    public short put(int k, short v) {
        Entry e = this.add(k);
        short oldValue = e.value;
        e.value = v;
        return oldValue;
    }

    private Entry add(int k) {
        Entry e;
        this.modified = false;
        int maxDepth = 0;
        if (this.tree == null) {
            ++this.count;
            this.lastEntry = this.firstEntry = new Entry(k, this.defRetValue);
            this.tree = this.firstEntry;
            e = this.firstEntry;
        } else {
            Entry p = this.tree;
            int i = 0;
            while (true) {
                int cmp;
                if ((cmp = this.compare(k, p.key)) == 0) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return p;
                }
                this.nodePath[i] = p;
                this.dirPath[i++] = cmp > 0;
                if (this.dirPath[i++]) {
                    if (p.succ()) {
                        ++this.count;
                        e = new Entry(k, this.defRetValue);
                        if (p.right == null) {
                            this.lastEntry = e;
                        }
                        e.left = p;
                        e.right = p.right;
                        p.right(e);
                        break;
                    }
                    p = p.right;
                    continue;
                }
                if (p.pred()) {
                    ++this.count;
                    e = new Entry(k, this.defRetValue);
                    if (p.left == null) {
                        this.firstEntry = e;
                    }
                    e.right = p;
                    e.left = p.left;
                    p.left(e);
                    break;
                }
                p = p.left;
            }
            this.modified = true;
            maxDepth = i--;
            while (i > 0 && !this.nodePath[i].black()) {
                Entry x;
                Entry y;
                if (!this.dirPath[i - 1]) {
                    y = this.nodePath[i - 1].right;
                    if (!this.nodePath[i - 1].succ() && !y.black()) {
                        this.nodePath[i].black(true);
                        y.black(true);
                        this.nodePath[i - 1].black(false);
                        i -= 2;
                        continue;
                    }
                    if (!this.dirPath[i]) {
                        y = this.nodePath[i];
                    } else {
                        x = this.nodePath[i];
                        y = x.right;
                        x.right = y.left;
                        y.left = x;
                        this.nodePath[i - 1].left = y;
                        if (y.pred()) {
                            y.pred(false);
                            x.succ(y);
                        }
                    }
                    x = this.nodePath[i - 1];
                    x.black(false);
                    y.black(true);
                    x.left = y.right;
                    y.right = x;
                    if (i < 2) {
                        this.tree = y;
                    } else if (this.dirPath[i - 2]) {
                        this.nodePath[i - 2].right = y;
                    } else {
                        this.nodePath[i - 2].left = y;
                    }
                    if (!y.succ()) break;
                    y.succ(false);
                    x.pred(y);
                    break;
                }
                y = this.nodePath[i - 1].left;
                if (!this.nodePath[i - 1].pred() && !y.black()) {
                    this.nodePath[i].black(true);
                    y.black(true);
                    this.nodePath[i - 1].black(false);
                    i -= 2;
                    continue;
                }
                if (this.dirPath[i]) {
                    y = this.nodePath[i];
                } else {
                    x = this.nodePath[i];
                    y = x.left;
                    x.left = y.right;
                    y.right = x;
                    this.nodePath[i - 1].right = y;
                    if (y.succ()) {
                        y.succ(false);
                        x.pred(y);
                    }
                }
                x = this.nodePath[i - 1];
                x.black(false);
                y.black(true);
                x.right = y.left;
                y.left = x;
                if (i < 2) {
                    this.tree = y;
                } else if (this.dirPath[i - 2]) {
                    this.nodePath[i - 2].right = y;
                } else {
                    this.nodePath[i - 2].left = y;
                }
                if (!y.pred()) break;
                y.pred(false);
                x.succ(y);
                break;
            }
        }
        this.tree.black(true);
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return e;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public short remove(int k) {
        int i;
        Entry p;
        block68: {
            Entry s;
            int j;
            boolean color;
            block74: {
                block69: {
                    Entry r;
                    block73: {
                        block71: {
                            block72: {
                                block66: {
                                    block67: {
                                        this.modified = false;
                                        if (this.tree == null) {
                                            return this.defRetValue;
                                        }
                                        p = this.tree;
                                        i = 0;
                                        int kk = k;
                                        while (true) {
                                            int cmp;
                                            if ((cmp = this.compare(kk, p.key)) == 0) {
                                                if (p.left != null) break block66;
                                                break block67;
                                            }
                                            this.dirPath[i] = cmp > 0;
                                            this.nodePath[i] = p;
                                            if (this.dirPath[i++]) {
                                                if ((p = p.right()) != null) continue;
                                                while (true) {
                                                    if (i-- == 0) {
                                                        return this.defRetValue;
                                                    }
                                                    this.nodePath[i] = null;
                                                }
                                            }
                                            if ((p = p.left()) == null) break;
                                        }
                                        while (true) {
                                            if (i-- == 0) {
                                                return this.defRetValue;
                                            }
                                            this.nodePath[i] = null;
                                        }
                                    }
                                    this.firstEntry = p.next();
                                }
                                if (p.right == null) {
                                    this.lastEntry = p.prev();
                                }
                                if (!p.succ()) break block71;
                                if (!p.pred()) break block72;
                                if (i == 0) {
                                    this.tree = p.left;
                                    break block68;
                                } else if (this.dirPath[i - 1]) {
                                    this.nodePath[i - 1].succ(p.right);
                                    break block68;
                                } else {
                                    this.nodePath[i - 1].pred(p.left);
                                }
                                break block68;
                            }
                            p.prev().right = p.right;
                            if (i == 0) {
                                this.tree = p.left;
                                break block68;
                            } else if (this.dirPath[i - 1]) {
                                this.nodePath[i - 1].right = p.left;
                                break block68;
                            } else {
                                this.nodePath[i - 1].left = p.left;
                            }
                            break block68;
                        }
                        r = p.right;
                        if (!r.pred()) break block73;
                        r.left = p.left;
                        r.pred(p.pred());
                        if (!r.pred()) {
                            r.prev().right = r;
                        }
                        if (i == 0) {
                            this.tree = r;
                        } else if (this.dirPath[i - 1]) {
                            this.nodePath[i - 1].right = r;
                        } else {
                            this.nodePath[i - 1].left = r;
                        }
                        color = r.black();
                        r.black(p.black());
                        p.black(color);
                        this.dirPath[i] = true;
                        this.nodePath[i++] = r;
                        break block68;
                    }
                    j = i++;
                    while (true) {
                        this.dirPath[i] = false;
                        this.nodePath[i++] = r;
                        s = r.left;
                        if (s.pred()) {
                            this.dirPath[j] = true;
                            this.nodePath[j] = s;
                            if (s.succ()) {
                                break;
                            }
                            break block69;
                        }
                        r = s;
                    }
                    r.pred(s);
                    break block74;
                }
                r.left = s.right;
            }
            s.left = p.left;
            if (!p.pred()) {
                p.prev().right = s;
                s.pred(false);
            }
            s.right(p.right);
            color = s.black();
            s.black(p.black());
            p.black(color);
            if (j == 0) {
                this.tree = s;
            } else if (this.dirPath[j - 1]) {
                this.nodePath[j - 1].right = s;
            } else {
                this.nodePath[j - 1].left = s;
            }
        }
        int maxDepth = i;
        if (p.black()) {
            while (i > 0) {
                block70: {
                    Entry y;
                    Entry w;
                    if (this.dirPath[i - 1] && !this.nodePath[i - 1].succ() || !this.dirPath[i - 1] && !this.nodePath[i - 1].pred()) {
                        Entry x;
                        Entry entry = x = this.dirPath[i - 1] ? this.nodePath[i - 1].right : this.nodePath[i - 1].left;
                        if (!x.black()) {
                            x.black(true);
                            break;
                        }
                    }
                    if (!this.dirPath[i - 1]) {
                        w = this.nodePath[i - 1].right;
                        if (!w.black()) {
                            w.black(true);
                            this.nodePath[i - 1].black(false);
                            this.nodePath[i - 1].right = w.left;
                            w.left = this.nodePath[i - 1];
                            if (i < 2) {
                                this.tree = w;
                            } else if (this.dirPath[i - 2]) {
                                this.nodePath[i - 2].right = w;
                            } else {
                                this.nodePath[i - 2].left = w;
                            }
                            this.nodePath[i] = this.nodePath[i - 1];
                            this.dirPath[i] = false;
                            this.nodePath[i - 1] = w;
                            if (maxDepth == i++) {
                                ++maxDepth;
                            }
                            w = this.nodePath[i - 1].right;
                        }
                        if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                            w.black(false);
                            break block70;
                        } else {
                            if (w.succ() || w.right.black()) {
                                y = w.left;
                                y.black(true);
                                w.black(false);
                                w.left = y.right;
                                y.right = w;
                                w = this.nodePath[i - 1].right = y;
                                if (w.succ()) {
                                    w.succ(false);
                                    w.right.pred(w);
                                }
                            }
                            w.black(this.nodePath[i - 1].black());
                            this.nodePath[i - 1].black(true);
                            w.right.black(true);
                            this.nodePath[i - 1].right = w.left;
                            w.left = this.nodePath[i - 1];
                            if (i < 2) {
                                this.tree = w;
                            } else if (this.dirPath[i - 2]) {
                                this.nodePath[i - 2].right = w;
                            } else {
                                this.nodePath[i - 2].left = w;
                            }
                            if (!w.pred()) break;
                            w.pred(false);
                            this.nodePath[i - 1].succ(w);
                            break;
                        }
                    }
                    w = this.nodePath[i - 1].left;
                    if (!w.black()) {
                        w.black(true);
                        this.nodePath[i - 1].black(false);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        } else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        } else {
                            this.nodePath[i - 2].left = w;
                        }
                        this.nodePath[i] = this.nodePath[i - 1];
                        this.dirPath[i] = true;
                        this.nodePath[i - 1] = w;
                        if (maxDepth == i++) {
                            ++maxDepth;
                        }
                        w = this.nodePath[i - 1].left;
                    }
                    if ((w.pred() || w.left.black()) && (w.succ() || w.right.black())) {
                        w.black(false);
                    } else {
                        if (w.pred() || w.left.black()) {
                            y = w.right;
                            y.black(true);
                            w.black(false);
                            w.right = y.left;
                            y.left = w;
                            w = this.nodePath[i - 1].left = y;
                            if (w.pred()) {
                                w.pred(false);
                                w.left.succ(w);
                            }
                        }
                        w.black(this.nodePath[i - 1].black());
                        this.nodePath[i - 1].black(true);
                        w.left.black(true);
                        this.nodePath[i - 1].left = w.right;
                        w.right = this.nodePath[i - 1];
                        if (i < 2) {
                            this.tree = w;
                        } else if (this.dirPath[i - 2]) {
                            this.nodePath[i - 2].right = w;
                        } else {
                            this.nodePath[i - 2].left = w;
                        }
                        if (!w.succ()) break;
                        w.succ(false);
                        this.nodePath[i - 1].pred(w);
                        break;
                    }
                }
                --i;
            }
            if (this.tree != null) {
                this.tree.black(true);
            }
        }
        this.modified = true;
        --this.count;
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return p.value;
    }

    @Override
    public boolean containsValue(short v) {
        ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            short ev = i.nextShort();
            if (ev != v) continue;
            return true;
        }
        return false;
    }

    @Override
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.entries = null;
        this.values = null;
        this.keys = null;
        this.lastEntry = null;
        this.firstEntry = null;
    }

    @Override
    public boolean containsKey(int k) {
        return this.findKey(k) != null;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public boolean isEmpty() {
        return this.count == 0;
    }

    @Override
    public short get(int k) {
        Entry e = this.findKey(k);
        return e == null ? this.defRetValue : e.value;
    }

    @Override
    public int firstIntKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override
    public int lastIntKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    @Override
    public ObjectSortedSet<Int2ShortMap.Entry> int2ShortEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Int2ShortMap.Entry>(){
                final Comparator<? super Int2ShortMap.Entry> comparator = (x, y) -> Int2ShortRBTreeMap.this.actualComparator.compare(x.getIntKey(), y.getIntKey());

                @Override
                public Comparator<? super Int2ShortMap.Entry> comparator() {
                    return this.comparator;
                }

                @Override
                public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator() {
                    return new EntryIterator();
                }

                @Override
                public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator(Int2ShortMap.Entry from) {
                    return new EntryIterator(from.getIntKey());
                }

                @Override
                public boolean contains(Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry e = (Map.Entry)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                        return false;
                    }
                    Entry f = Int2ShortRBTreeMap.this.findKey((Integer)e.getKey());
                    return e.equals(f);
                }

                @Override
                public boolean remove(Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry e = (Map.Entry)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                        return false;
                    }
                    Entry f = Int2ShortRBTreeMap.this.findKey((Integer)e.getKey());
                    if (f == null || f.getShortValue() != ((Short)e.getValue()).shortValue()) {
                        return false;
                    }
                    Int2ShortRBTreeMap.this.remove(f.key);
                    return true;
                }

                @Override
                public int size() {
                    return Int2ShortRBTreeMap.this.count;
                }

                @Override
                public void clear() {
                    Int2ShortRBTreeMap.this.clear();
                }

                @Override
                public Int2ShortMap.Entry first() {
                    return Int2ShortRBTreeMap.this.firstEntry;
                }

                @Override
                public Int2ShortMap.Entry last() {
                    return Int2ShortRBTreeMap.this.lastEntry;
                }

                @Override
                public ObjectSortedSet<Int2ShortMap.Entry> subSet(Int2ShortMap.Entry from, Int2ShortMap.Entry to) {
                    return Int2ShortRBTreeMap.this.subMap(from.getIntKey(), to.getIntKey()).int2ShortEntrySet();
                }

                @Override
                public ObjectSortedSet<Int2ShortMap.Entry> headSet(Int2ShortMap.Entry to) {
                    return Int2ShortRBTreeMap.this.headMap(to.getIntKey()).int2ShortEntrySet();
                }

                @Override
                public ObjectSortedSet<Int2ShortMap.Entry> tailSet(Int2ShortMap.Entry from) {
                    return Int2ShortRBTreeMap.this.tailMap(from.getIntKey()).int2ShortEntrySet();
                }
            };
        }
        return this.entries;
    }

    @Override
    public IntSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public ShortCollection values() {
        if (this.values == null) {
            this.values = new AbstractShortCollection(){

                @Override
                public ShortIterator iterator() {
                    return new ValueIterator();
                }

                @Override
                public boolean contains(short k) {
                    return Int2ShortRBTreeMap.this.containsValue(k);
                }

                @Override
                public int size() {
                    return Int2ShortRBTreeMap.this.count;
                }

                @Override
                public void clear() {
                    Int2ShortRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }

    @Override
    public IntComparator comparator() {
        return this.actualComparator;
    }

    @Override
    public Int2ShortSortedMap headMap(int to) {
        return new Submap(0, true, to, false);
    }

    @Override
    public Int2ShortSortedMap tailMap(int from) {
        return new Submap(from, false, 0, true);
    }

    @Override
    public Int2ShortSortedMap subMap(int from, int to) {
        return new Submap(from, false, to, false);
    }

    public Int2ShortRBTreeMap clone() {
        Int2ShortRBTreeMap c;
        try {
            c = (Int2ShortRBTreeMap)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
        c.keys = null;
        c.values = null;
        c.entries = null;
        c.allocatePaths();
        if (this.count != 0) {
            Entry rp = new Entry();
            Entry rq = new Entry();
            Entry p = rp;
            rp.left(this.tree);
            Entry q = rq;
            rq.pred(null);
            while (true) {
                Entry e;
                if (!p.pred()) {
                    e = p.left.clone();
                    e.pred(q.left);
                    e.succ(q);
                    q.left(e);
                    p = p.left;
                    q = q.left;
                } else {
                    while (p.succ()) {
                        p = p.right;
                        if (p == null) {
                            q.right = null;
                            c.firstEntry = c.tree = rq.left;
                            while (c.firstEntry.left != null) {
                                c.firstEntry = c.firstEntry.left;
                            }
                            c.lastEntry = c.tree;
                            while (c.lastEntry.right != null) {
                                c.lastEntry = c.lastEntry.right;
                            }
                            return c;
                        }
                        q = q.right;
                    }
                    p = p.right;
                    q = q.right;
                }
                if (p.succ()) continue;
                e = p.right.clone();
                e.succ(q.right);
                e.pred(q);
                q.right(e);
            }
        }
        return c;
    }

    private void writeObject(ObjectOutputStream s) throws IOException {
        int n = this.count;
        EntryIterator i = new EntryIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            Entry e = i.nextEntry();
            s.writeInt(e.key);
            s.writeShort(e.value);
        }
    }

    private Entry readTree(ObjectInputStream s, int n, Entry pred, Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry top = new Entry(s.readInt(), s.readShort());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            Entry top = new Entry(s.readInt(), s.readShort());
            top.black(true);
            top.right(new Entry(s.readInt(), s.readShort()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        int rightN = n / 2;
        int leftN = n - rightN - 1;
        Entry top = new Entry();
        top.left(this.readTree(s, leftN, pred, top));
        top.key = s.readInt();
        top.value = s.readShort();
        top.black(true);
        top.right(this.readTree(s, rightN, top, succ));
        if (n + 2 == (n + 2 & -(n + 2))) {
            top.right.black(false);
        }
        return top;
    }

    private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException {
        s.defaultReadObject();
        this.setActualComparator();
        this.allocatePaths();
        if (this.count != 0) {
            Entry e = this.tree = this.readTree(s, this.count, null, null);
            while (e.left() != null) {
                e = e.left();
            }
            this.firstEntry = e;
            e = this.tree;
            while (e.right() != null) {
                e = e.right();
            }
            this.lastEntry = e;
        }
    }

    private final class Submap
    extends AbstractInt2ShortSortedMap
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        int from;
        int to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Int2ShortMap.Entry> entries;
        protected transient IntSortedSet keys;
        protected transient ShortCollection values;

        public Submap(int from, boolean bottom, int to, boolean top) {
            if (!bottom && !top && Int2ShortRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Int2ShortRBTreeMap.this.defRetValue;
        }

        @Override
        public void clear() {
            SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }

        final boolean in(int k) {
            return !(!this.bottom && Int2ShortRBTreeMap.this.compare(k, this.from) < 0 || !this.top && Int2ShortRBTreeMap.this.compare(k, this.to) >= 0);
        }

        @Override
        public ObjectSortedSet<Int2ShortMap.Entry> int2ShortEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Int2ShortMap.Entry>(){

                    @Override
                    public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }

                    @Override
                    public ObjectBidirectionalIterator<Int2ShortMap.Entry> iterator(Int2ShortMap.Entry from) {
                        return new SubmapEntryIterator(from.getIntKey());
                    }

                    @Override
                    public Comparator<? super Int2ShortMap.Entry> comparator() {
                        return Int2ShortRBTreeMap.this.int2ShortEntrySet().comparator();
                    }

                    @Override
                    public boolean contains(Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry e = (Map.Entry)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                            return false;
                        }
                        Entry f = Int2ShortRBTreeMap.this.findKey((Integer)e.getKey());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }

                    @Override
                    public boolean remove(Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry e = (Map.Entry)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Integer)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Short)) {
                            return false;
                        }
                        Entry f = Int2ShortRBTreeMap.this.findKey((Integer)e.getKey());
                        if (f != null && Submap.this.in(f.key)) {
                            Submap.this.remove(f.key);
                        }
                        return f != null;
                    }

                    @Override
                    public int size() {
                        int c = 0;
                        ObjectIterator i = this.iterator();
                        while (i.hasNext()) {
                            ++c;
                            i.next();
                        }
                        return c;
                    }

                    @Override
                    public boolean isEmpty() {
                        return !new SubmapIterator().hasNext();
                    }

                    @Override
                    public void clear() {
                        Submap.this.clear();
                    }

                    @Override
                    public Int2ShortMap.Entry first() {
                        return Submap.this.firstEntry();
                    }

                    @Override
                    public Int2ShortMap.Entry last() {
                        return Submap.this.lastEntry();
                    }

                    @Override
                    public ObjectSortedSet<Int2ShortMap.Entry> subSet(Int2ShortMap.Entry from, Int2ShortMap.Entry to) {
                        return Submap.this.subMap(from.getIntKey(), to.getIntKey()).int2ShortEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Int2ShortMap.Entry> headSet(Int2ShortMap.Entry to) {
                        return Submap.this.headMap(to.getIntKey()).int2ShortEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Int2ShortMap.Entry> tailSet(Int2ShortMap.Entry from) {
                        return Submap.this.tailMap(from.getIntKey()).int2ShortEntrySet();
                    }
                };
            }
            return this.entries;
        }

        @Override
        public IntSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }

        @Override
        public ShortCollection values() {
            if (this.values == null) {
                this.values = new AbstractShortCollection(){

                    @Override
                    public ShortIterator iterator() {
                        return new SubmapValueIterator();
                    }

                    @Override
                    public boolean contains(short k) {
                        return Submap.this.containsValue(k);
                    }

                    @Override
                    public int size() {
                        return Submap.this.size();
                    }

                    @Override
                    public void clear() {
                        Submap.this.clear();
                    }
                };
            }
            return this.values;
        }

        @Override
        public boolean containsKey(int k) {
            return this.in(k) && Int2ShortRBTreeMap.this.containsKey(k);
        }

        @Override
        public boolean containsValue(short v) {
            SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                short ev = i.nextEntry().value;
                if (ev != v) continue;
                return true;
            }
            return false;
        }

        @Override
        public short get(int k) {
            Entry e;
            int kk = k;
            return this.in(kk) && (e = Int2ShortRBTreeMap.this.findKey(kk)) != null ? e.value : this.defRetValue;
        }

        @Override
        public short put(int k, short v) {
            Int2ShortRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException("Key (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            short oldValue = Int2ShortRBTreeMap.this.put(k, v);
            return Int2ShortRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }

        @Override
        public short remove(int k) {
            Int2ShortRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            short oldValue = Int2ShortRBTreeMap.this.remove(k);
            return Int2ShortRBTreeMap.this.modified ? oldValue : this.defRetValue;
        }

        @Override
        public int size() {
            SubmapIterator i = new SubmapIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextEntry();
            }
            return n;
        }

        @Override
        public boolean isEmpty() {
            return !new SubmapIterator().hasNext();
        }

        @Override
        public IntComparator comparator() {
            return Int2ShortRBTreeMap.this.actualComparator;
        }

        @Override
        public Int2ShortSortedMap headMap(int to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return Int2ShortRBTreeMap.this.compare(to, this.to) < 0 ? new Submap(this.from, this.bottom, to, false) : this;
        }

        @Override
        public Int2ShortSortedMap tailMap(int from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return Int2ShortRBTreeMap.this.compare(from, this.from) > 0 ? new Submap(from, false, this.to, this.top) : this;
        }

        @Override
        public Int2ShortSortedMap subMap(int from, int to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                int n = to = Int2ShortRBTreeMap.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                int n = from = Int2ShortRBTreeMap.this.compare(from, this.from) > 0 ? from : this.from;
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }

        public Entry firstEntry() {
            Entry e;
            if (Int2ShortRBTreeMap.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = Int2ShortRBTreeMap.this.firstEntry;
            } else {
                e = Int2ShortRBTreeMap.this.locateKey(this.from);
                if (Int2ShortRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || !this.top && Int2ShortRBTreeMap.this.compare(e.key, this.to) >= 0) {
                return null;
            }
            return e;
        }

        public Entry lastEntry() {
            Entry e;
            if (Int2ShortRBTreeMap.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = Int2ShortRBTreeMap.this.lastEntry;
            } else {
                e = Int2ShortRBTreeMap.this.locateKey(this.to);
                if (Int2ShortRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || !this.bottom && Int2ShortRBTreeMap.this.compare(e.key, this.from) < 0) {
                return null;
            }
            return e;
        }

        @Override
        public int firstIntKey() {
            Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override
        public int lastIntKey() {
            Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        private final class SubmapValueIterator
        extends SubmapIterator
        implements ShortListIterator {
            private SubmapValueIterator() {
            }

            @Override
            public short nextShort() {
                return this.nextEntry().value;
            }

            @Override
            public short previousShort() {
                return this.previousEntry().value;
            }
        }

        private final class SubmapKeyIterator
        extends SubmapIterator
        implements IntListIterator {
            public SubmapKeyIterator() {
            }

            public SubmapKeyIterator(int from) {
                super(from);
            }

            @Override
            public int nextInt() {
                return this.nextEntry().key;
            }

            @Override
            public int previousInt() {
                return this.previousEntry().key;
            }
        }

        private class SubmapEntryIterator
        extends SubmapIterator
        implements ObjectListIterator<Int2ShortMap.Entry> {
            SubmapEntryIterator() {
            }

            SubmapEntryIterator(int k) {
                super(k);
            }

            @Override
            public Int2ShortMap.Entry next() {
                return this.nextEntry();
            }

            @Override
            public Int2ShortMap.Entry previous() {
                return this.previousEntry();
            }
        }

        private class SubmapIterator
        extends TreeIterator {
            SubmapIterator() {
                this.next = Submap.this.firstEntry();
            }

            /*
             * Enabled aggressive block sorting
             */
            SubmapIterator(int k) {
                this();
                if (this.next == null) return;
                if (!submap.bottom && submap.Int2ShortRBTreeMap.this.compare(k, this.next.key) < 0) {
                    this.prev = null;
                    return;
                }
                if (!submap.top) {
                    this.prev = submap.lastEntry();
                    if (submap.Int2ShortRBTreeMap.this.compare(k, this.prev.key) >= 0) {
                        this.next = null;
                        return;
                    }
                }
                this.next = submap.Int2ShortRBTreeMap.this.locateKey(k);
                if (submap.Int2ShortRBTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                    return;
                }
                this.prev = this.next.prev();
            }

            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Submap.this.bottom && this.prev != null && Int2ShortRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }

            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Int2ShortRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }

        private class KeySet
        extends AbstractInt2ShortSortedMap.KeySet {
            private KeySet() {
                super(Submap.this);
            }

            @Override
            public IntBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }

            @Override
            public IntBidirectionalIterator iterator(int from) {
                return new SubmapKeyIterator(from);
            }
        }
    }

    private final class ValueIterator
    extends TreeIterator
    implements ShortListIterator {
        private ValueIterator() {
        }

        @Override
        public short nextShort() {
            return this.nextEntry().value;
        }

        @Override
        public short previousShort() {
            return this.previousEntry().value;
        }
    }

    private class KeySet
    extends AbstractInt2ShortSortedMap.KeySet {
        private KeySet() {
            super(Int2ShortRBTreeMap.this);
        }

        @Override
        public IntBidirectionalIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public IntBidirectionalIterator iterator(int from) {
            return new KeyIterator(from);
        }
    }

    private final class KeyIterator
    extends TreeIterator
    implements IntListIterator {
        public KeyIterator() {
        }

        public KeyIterator(int k) {
            super(k);
        }

        @Override
        public int nextInt() {
            return this.nextEntry().key;
        }

        @Override
        public int previousInt() {
            return this.previousEntry().key;
        }
    }

    private class EntryIterator
    extends TreeIterator
    implements ObjectListIterator<Int2ShortMap.Entry> {
        EntryIterator() {
        }

        EntryIterator(int k) {
            super(k);
        }

        @Override
        public Int2ShortMap.Entry next() {
            return this.nextEntry();
        }

        @Override
        public Int2ShortMap.Entry previous() {
            return this.previousEntry();
        }
    }

    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index = 0;

        TreeIterator() {
            this.next = Int2ShortRBTreeMap.this.firstEntry;
        }

        TreeIterator(int k) {
            this.next = Int2ShortRBTreeMap.this.locateKey(k);
            if (this.next != null) {
                if (Int2ShortRBTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                } else {
                    this.prev = this.next.prev();
                }
            }
        }

        public boolean hasNext() {
            return this.next != null;
        }

        public boolean hasPrevious() {
            return this.prev != null;
        }

        void updateNext() {
            this.next = this.next.next();
        }

        Entry nextEntry() {
            if (!this.hasNext()) {
                throw new NoSuchElementException();
            }
            this.curr = this.prev = this.next;
            ++this.index;
            this.updateNext();
            return this.curr;
        }

        void updatePrevious() {
            this.prev = this.prev.prev();
        }

        Entry previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next = this.prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }

        public int nextIndex() {
            return this.index;
        }

        public int previousIndex() {
            return this.index - 1;
        }

        public void remove() {
            if (this.curr == null) {
                throw new IllegalStateException();
            }
            if (this.curr == this.prev) {
                --this.index;
            }
            this.next = this.prev = this.curr;
            this.updatePrevious();
            this.updateNext();
            Int2ShortRBTreeMap.this.remove(this.curr.key);
            this.curr = null;
        }

        public int skip(int n) {
            int i = n;
            while (i-- != 0 && this.hasNext()) {
                this.nextEntry();
            }
            return n - i - 1;
        }

        public int back(int n) {
            int i = n;
            while (i-- != 0 && this.hasPrevious()) {
                this.previousEntry();
            }
            return n - i - 1;
        }
    }

    private static final class Entry
    extends AbstractInt2ShortMap.BasicEntry
    implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 0x40000000;
        Entry left;
        Entry right;
        int info;

        Entry() {
            super(0, (short)0);
        }

        Entry(int k, short v) {
            super(k, v);
            this.info = -1073741824;
        }

        Entry left() {
            return (this.info & 0x40000000) != 0 ? null : this.left;
        }

        Entry right() {
            return (this.info & Integer.MIN_VALUE) != 0 ? null : this.right;
        }

        boolean pred() {
            return (this.info & 0x40000000) != 0;
        }

        boolean succ() {
            return (this.info & Integer.MIN_VALUE) != 0;
        }

        void pred(boolean pred) {
            this.info = pred ? (this.info |= 0x40000000) : (this.info &= 0xBFFFFFFF);
        }

        void succ(boolean succ) {
            this.info = succ ? (this.info |= Integer.MIN_VALUE) : (this.info &= Integer.MAX_VALUE);
        }

        void pred(Entry pred) {
            this.info |= 0x40000000;
            this.left = pred;
        }

        void succ(Entry succ) {
            this.info |= Integer.MIN_VALUE;
            this.right = succ;
        }

        void left(Entry left) {
            this.info &= 0xBFFFFFFF;
            this.left = left;
        }

        void right(Entry right) {
            this.info &= Integer.MAX_VALUE;
            this.right = right;
        }

        boolean black() {
            return (this.info & 1) != 0;
        }

        void black(boolean black) {
            this.info = black ? (this.info |= 1) : (this.info &= 0xFFFFFFFE);
        }

        Entry next() {
            Entry next = this.right;
            if ((this.info & Integer.MIN_VALUE) == 0) {
                while ((next.info & 0x40000000) == 0) {
                    next = next.left;
                }
            }
            return next;
        }

        Entry prev() {
            Entry prev = this.left;
            if ((this.info & 0x40000000) == 0) {
                while ((prev.info & Integer.MIN_VALUE) == 0) {
                    prev = prev.right;
                }
            }
            return prev;
        }

        @Override
        public short setValue(short value) {
            short oldValue = this.value;
            this.value = value;
            return oldValue;
        }

        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.value = this.value;
            c.info = this.info;
            return c;
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof Map.Entry)) {
                return false;
            }
            Map.Entry e = (Map.Entry)o;
            return this.key == (Integer)e.getKey() && this.value == (Short)e.getValue();
        }

        @Override
        public int hashCode() {
            return this.key ^ this.value;
        }

        @Override
        public String toString() {
            return this.key + "=>" + this.value;
        }
    }
}

