/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.HashCommon;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractChar2DoubleMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractChar2DoubleSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2DoubleMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.Char2DoubleSortedMap;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparators;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.AbstractDoubleCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.doubles.DoubleListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.AbstractObjectSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.objects.ObjectSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Comparator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.SortedMap;

public class Char2DoubleRBTreeMap
extends AbstractChar2DoubleSortedMap
implements Serializable,
Cloneable {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected transient ObjectSortedSet<Char2DoubleMap.Entry> entries;
    protected transient CharSortedSet keys;
    protected transient DoubleCollection values;
    protected transient boolean modified;
    protected Comparator<? super Character> storedComparator;
    protected transient CharComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353129L;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;

    public Char2DoubleRBTreeMap() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = CharComparators.asCharComparator(this.storedComparator);
    }

    public Char2DoubleRBTreeMap(Comparator<? super Character> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }

    public Char2DoubleRBTreeMap(Map<? extends Character, ? extends Double> m) {
        this();
        this.putAll(m);
    }

    public Char2DoubleRBTreeMap(SortedMap<Character, Double> m) {
        this(m.comparator());
        this.putAll((Map<? extends Character, ? extends Double>)m);
    }

    public Char2DoubleRBTreeMap(Char2DoubleMap m) {
        this();
        this.putAll(m);
    }

    public Char2DoubleRBTreeMap(Char2DoubleSortedMap m) {
        this(m.comparator());
        this.putAll(m);
    }

    public Char2DoubleRBTreeMap(char[] k, double[] v, Comparator<? super Character> c) {
        this(c);
        if (k.length != v.length) {
            throw new IllegalArgumentException("The key array and the value array have different lengths (" + k.length + " and " + v.length + ")");
        }
        for (int i = 0; i < k.length; ++i) {
            this.put(k[i], v[i]);
        }
    }

    public Char2DoubleRBTreeMap(char[] k, double[] v) {
        this(k, v, null);
    }

    final int compare(char k1, char k2) {
        return this.actualComparator == null ? Character.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }

    final Entry findKey(char k) {
        int cmp;
        Entry e = this.tree;
        while (e != null && (cmp = this.compare(k, e.key)) != 0) {
            e = cmp < 0 ? e.left() : e.right();
        }
        return e;
    }

    final Entry locateKey(char k) {
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

    public double addTo(char k, double incr) {
        Entry e = this.add(k);
        double oldValue = e.value;
        e.value += incr;
        return oldValue;
    }

    @Override
    public double put(char k, double v) {
        Entry e = this.add(k);
        double oldValue = e.value;
        e.value = v;
        return oldValue;
    }

    private Entry add(char k) {
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
    public double remove(char k) {
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
                                        char kk = k;
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
    public boolean containsValue(double v) {
        ValueIterator i = new ValueIterator();
        int j = this.count;
        while (j-- != 0) {
            double ev = i.nextDouble();
            if (Double.doubleToLongBits(ev) != Double.doubleToLongBits(v)) continue;
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
    public boolean containsKey(char k) {
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
    public double get(char k) {
        Entry e = this.findKey(k);
        return e == null ? this.defRetValue : e.value;
    }

    @Override
    public char firstCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override
    public char lastCharKey() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    @Override
    public ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
        if (this.entries == null) {
            this.entries = new AbstractObjectSortedSet<Char2DoubleMap.Entry>(){
                final Comparator<? super Char2DoubleMap.Entry> comparator = (x, y) -> Char2DoubleRBTreeMap.this.actualComparator.compare(x.getCharKey(), y.getCharKey());

                @Override
                public Comparator<? super Char2DoubleMap.Entry> comparator() {
                    return this.comparator;
                }

                @Override
                public ObjectBidirectionalIterator<Char2DoubleMap.Entry> iterator() {
                    return new EntryIterator();
                }

                @Override
                public ObjectBidirectionalIterator<Char2DoubleMap.Entry> iterator(Char2DoubleMap.Entry from) {
                    return new EntryIterator(from.getCharKey());
                }

                @Override
                public boolean contains(Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry e = (Map.Entry)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                        return false;
                    }
                    Entry f = Char2DoubleRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
                    return e.equals(f);
                }

                @Override
                public boolean remove(Object o) {
                    if (!(o instanceof Map.Entry)) {
                        return false;
                    }
                    Map.Entry e = (Map.Entry)o;
                    if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                        return false;
                    }
                    if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                        return false;
                    }
                    Entry f = Char2DoubleRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
                    if (f == null || Double.doubleToLongBits(f.getDoubleValue()) != Double.doubleToLongBits((Double)e.getValue())) {
                        return false;
                    }
                    Char2DoubleRBTreeMap.this.remove(f.key);
                    return true;
                }

                @Override
                public int size() {
                    return Char2DoubleRBTreeMap.this.count;
                }

                @Override
                public void clear() {
                    Char2DoubleRBTreeMap.this.clear();
                }

                @Override
                public Char2DoubleMap.Entry first() {
                    return Char2DoubleRBTreeMap.this.firstEntry;
                }

                @Override
                public Char2DoubleMap.Entry last() {
                    return Char2DoubleRBTreeMap.this.lastEntry;
                }

                @Override
                public ObjectSortedSet<Char2DoubleMap.Entry> subSet(Char2DoubleMap.Entry from, Char2DoubleMap.Entry to) {
                    return Char2DoubleRBTreeMap.this.subMap(from.getCharKey(), to.getCharKey()).char2DoubleEntrySet();
                }

                @Override
                public ObjectSortedSet<Char2DoubleMap.Entry> headSet(Char2DoubleMap.Entry to) {
                    return Char2DoubleRBTreeMap.this.headMap(to.getCharKey()).char2DoubleEntrySet();
                }

                @Override
                public ObjectSortedSet<Char2DoubleMap.Entry> tailSet(Char2DoubleMap.Entry from) {
                    return Char2DoubleRBTreeMap.this.tailMap(from.getCharKey()).char2DoubleEntrySet();
                }
            };
        }
        return this.entries;
    }

    @Override
    public CharSortedSet keySet() {
        if (this.keys == null) {
            this.keys = new KeySet();
        }
        return this.keys;
    }

    @Override
    public DoubleCollection values() {
        if (this.values == null) {
            this.values = new AbstractDoubleCollection(){

                @Override
                public DoubleIterator iterator() {
                    return new ValueIterator();
                }

                @Override
                public boolean contains(double k) {
                    return Char2DoubleRBTreeMap.this.containsValue(k);
                }

                @Override
                public int size() {
                    return Char2DoubleRBTreeMap.this.count;
                }

                @Override
                public void clear() {
                    Char2DoubleRBTreeMap.this.clear();
                }
            };
        }
        return this.values;
    }

    @Override
    public CharComparator comparator() {
        return this.actualComparator;
    }

    @Override
    public Char2DoubleSortedMap headMap(char to) {
        return new Submap('\u0000', true, to, false);
    }

    @Override
    public Char2DoubleSortedMap tailMap(char from) {
        return new Submap(from, false, '\u0000', true);
    }

    @Override
    public Char2DoubleSortedMap subMap(char from, char to) {
        return new Submap(from, false, to, false);
    }

    public Char2DoubleRBTreeMap clone() {
        Char2DoubleRBTreeMap c;
        try {
            c = (Char2DoubleRBTreeMap)super.clone();
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
            s.writeChar(e.key);
            s.writeDouble(e.value);
        }
    }

    private Entry readTree(ObjectInputStream s, int n, Entry pred, Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry top = new Entry(s.readChar(), s.readDouble());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            Entry top = new Entry(s.readChar(), s.readDouble());
            top.black(true);
            top.right(new Entry(s.readChar(), s.readDouble()));
            top.right.pred(top);
            top.pred(pred);
            top.right.succ(succ);
            return top;
        }
        int rightN = n / 2;
        int leftN = n - rightN - 1;
        Entry top = new Entry();
        top.left(this.readTree(s, leftN, pred, top));
        top.key = s.readChar();
        top.value = s.readDouble();
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
    extends AbstractChar2DoubleSortedMap
    implements Serializable {
        private static final long serialVersionUID = -7046029254386353129L;
        char from;
        char to;
        boolean bottom;
        boolean top;
        protected transient ObjectSortedSet<Char2DoubleMap.Entry> entries;
        protected transient CharSortedSet keys;
        protected transient DoubleCollection values;

        public Submap(char from, boolean bottom, char to, boolean top) {
            if (!bottom && !top && Char2DoubleRBTreeMap.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start key (" + from + ") is larger than end key (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
            this.defRetValue = Char2DoubleRBTreeMap.this.defRetValue;
        }

        @Override
        public void clear() {
            SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                i.nextEntry();
                i.remove();
            }
        }

        final boolean in(char k) {
            return !(!this.bottom && Char2DoubleRBTreeMap.this.compare(k, this.from) < 0 || !this.top && Char2DoubleRBTreeMap.this.compare(k, this.to) >= 0);
        }

        @Override
        public ObjectSortedSet<Char2DoubleMap.Entry> char2DoubleEntrySet() {
            if (this.entries == null) {
                this.entries = new AbstractObjectSortedSet<Char2DoubleMap.Entry>(){

                    @Override
                    public ObjectBidirectionalIterator<Char2DoubleMap.Entry> iterator() {
                        return new SubmapEntryIterator();
                    }

                    @Override
                    public ObjectBidirectionalIterator<Char2DoubleMap.Entry> iterator(Char2DoubleMap.Entry from) {
                        return new SubmapEntryIterator(from.getCharKey());
                    }

                    @Override
                    public Comparator<? super Char2DoubleMap.Entry> comparator() {
                        return Char2DoubleRBTreeMap.this.char2DoubleEntrySet().comparator();
                    }

                    @Override
                    public boolean contains(Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry e = (Map.Entry)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                            return false;
                        }
                        Entry f = Char2DoubleRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
                        return f != null && Submap.this.in(f.key) && e.equals(f);
                    }

                    @Override
                    public boolean remove(Object o) {
                        if (!(o instanceof Map.Entry)) {
                            return false;
                        }
                        Map.Entry e = (Map.Entry)o;
                        if (e.getKey() == null || !(e.getKey() instanceof Character)) {
                            return false;
                        }
                        if (e.getValue() == null || !(e.getValue() instanceof Double)) {
                            return false;
                        }
                        Entry f = Char2DoubleRBTreeMap.this.findKey(((Character)e.getKey()).charValue());
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
                    public Char2DoubleMap.Entry first() {
                        return Submap.this.firstEntry();
                    }

                    @Override
                    public Char2DoubleMap.Entry last() {
                        return Submap.this.lastEntry();
                    }

                    @Override
                    public ObjectSortedSet<Char2DoubleMap.Entry> subSet(Char2DoubleMap.Entry from, Char2DoubleMap.Entry to) {
                        return Submap.this.subMap(from.getCharKey(), to.getCharKey()).char2DoubleEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Char2DoubleMap.Entry> headSet(Char2DoubleMap.Entry to) {
                        return Submap.this.headMap(to.getCharKey()).char2DoubleEntrySet();
                    }

                    @Override
                    public ObjectSortedSet<Char2DoubleMap.Entry> tailSet(Char2DoubleMap.Entry from) {
                        return Submap.this.tailMap(from.getCharKey()).char2DoubleEntrySet();
                    }
                };
            }
            return this.entries;
        }

        @Override
        public CharSortedSet keySet() {
            if (this.keys == null) {
                this.keys = new KeySet();
            }
            return this.keys;
        }

        @Override
        public DoubleCollection values() {
            if (this.values == null) {
                this.values = new AbstractDoubleCollection(){

                    @Override
                    public DoubleIterator iterator() {
                        return new SubmapValueIterator();
                    }

                    @Override
                    public boolean contains(double k) {
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
        public boolean containsKey(char k) {
            return this.in(k) && Char2DoubleRBTreeMap.this.containsKey(k);
        }

        @Override
        public boolean containsValue(double v) {
            SubmapIterator i = new SubmapIterator();
            while (i.hasNext()) {
                double ev = i.nextEntry().value;
                if (Double.doubleToLongBits(ev) != Double.doubleToLongBits(v)) continue;
                return true;
            }
            return false;
        }

        @Override
        public double get(char k) {
            Entry e;
            char kk = k;
            return this.in(kk) && (e = Char2DoubleRBTreeMap.this.findKey(kk)) != null ? e.value : this.defRetValue;
        }

        @Override
        public double put(char k, double v) {
            Char2DoubleRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                throw new IllegalArgumentException("Key (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            double oldValue = Char2DoubleRBTreeMap.this.put(k, v);
            return Char2DoubleRBTreeMap.this.modified ? this.defRetValue : oldValue;
        }

        @Override
        public double remove(char k) {
            Char2DoubleRBTreeMap.this.modified = false;
            if (!this.in(k)) {
                return this.defRetValue;
            }
            double oldValue = Char2DoubleRBTreeMap.this.remove(k);
            return Char2DoubleRBTreeMap.this.modified ? oldValue : this.defRetValue;
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
        public CharComparator comparator() {
            return Char2DoubleRBTreeMap.this.actualComparator;
        }

        @Override
        public Char2DoubleSortedMap headMap(char to) {
            if (this.top) {
                return new Submap(this.from, this.bottom, to, false);
            }
            return Char2DoubleRBTreeMap.this.compare(to, this.to) < 0 ? new Submap(this.from, this.bottom, to, false) : this;
        }

        @Override
        public Char2DoubleSortedMap tailMap(char from) {
            if (this.bottom) {
                return new Submap(from, false, this.to, this.top);
            }
            return Char2DoubleRBTreeMap.this.compare(from, this.from) > 0 ? new Submap(from, false, this.to, this.top) : this;
        }

        @Override
        public Char2DoubleSortedMap subMap(char from, char to) {
            if (this.top && this.bottom) {
                return new Submap(from, false, to, false);
            }
            if (!this.top) {
                char c = to = Char2DoubleRBTreeMap.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                char c = from = Char2DoubleRBTreeMap.this.compare(from, this.from) > 0 ? from : this.from;
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Submap(from, false, to, false);
        }

        public Entry firstEntry() {
            Entry e;
            if (Char2DoubleRBTreeMap.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = Char2DoubleRBTreeMap.this.firstEntry;
            } else {
                e = Char2DoubleRBTreeMap.this.locateKey(this.from);
                if (Char2DoubleRBTreeMap.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || !this.top && Char2DoubleRBTreeMap.this.compare(e.key, this.to) >= 0) {
                return null;
            }
            return e;
        }

        public Entry lastEntry() {
            Entry e;
            if (Char2DoubleRBTreeMap.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = Char2DoubleRBTreeMap.this.lastEntry;
            } else {
                e = Char2DoubleRBTreeMap.this.locateKey(this.to);
                if (Char2DoubleRBTreeMap.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || !this.bottom && Char2DoubleRBTreeMap.this.compare(e.key, this.from) < 0) {
                return null;
            }
            return e;
        }

        @Override
        public char firstCharKey() {
            Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override
        public char lastCharKey() {
            Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        private final class SubmapValueIterator
        extends SubmapIterator
        implements DoubleListIterator {
            private SubmapValueIterator() {
            }

            @Override
            public double nextDouble() {
                return this.nextEntry().value;
            }

            @Override
            public double previousDouble() {
                return this.previousEntry().value;
            }
        }

        private final class SubmapKeyIterator
        extends SubmapIterator
        implements CharListIterator {
            public SubmapKeyIterator() {
            }

            public SubmapKeyIterator(char from) {
                super(from);
            }

            @Override
            public char nextChar() {
                return this.nextEntry().key;
            }

            @Override
            public char previousChar() {
                return this.previousEntry().key;
            }
        }

        private class SubmapEntryIterator
        extends SubmapIterator
        implements ObjectListIterator<Char2DoubleMap.Entry> {
            SubmapEntryIterator() {
            }

            SubmapEntryIterator(char k) {
                super(k);
            }

            @Override
            public Char2DoubleMap.Entry next() {
                return this.nextEntry();
            }

            @Override
            public Char2DoubleMap.Entry previous() {
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
            SubmapIterator(char k) {
                this();
                if (this.next == null) return;
                if (!submap.bottom && submap.Char2DoubleRBTreeMap.this.compare(k, this.next.key) < 0) {
                    this.prev = null;
                    return;
                }
                if (!submap.top) {
                    this.prev = submap.lastEntry();
                    if (submap.Char2DoubleRBTreeMap.this.compare(k, this.prev.key) >= 0) {
                        this.next = null;
                        return;
                    }
                }
                this.next = submap.Char2DoubleRBTreeMap.this.locateKey(k);
                if (submap.Char2DoubleRBTreeMap.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                    return;
                }
                this.prev = this.next.prev();
            }

            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Submap.this.bottom && this.prev != null && Char2DoubleRBTreeMap.this.compare(this.prev.key, Submap.this.from) < 0) {
                    this.prev = null;
                }
            }

            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Submap.this.top && this.next != null && Char2DoubleRBTreeMap.this.compare(this.next.key, Submap.this.to) >= 0) {
                    this.next = null;
                }
            }
        }

        private class KeySet
        extends AbstractChar2DoubleSortedMap.KeySet {
            private KeySet() {
                super(Submap.this);
            }

            @Override
            public CharBidirectionalIterator iterator() {
                return new SubmapKeyIterator();
            }

            @Override
            public CharBidirectionalIterator iterator(char from) {
                return new SubmapKeyIterator(from);
            }
        }
    }

    private final class ValueIterator
    extends TreeIterator
    implements DoubleListIterator {
        private ValueIterator() {
        }

        @Override
        public double nextDouble() {
            return this.nextEntry().value;
        }

        @Override
        public double previousDouble() {
            return this.previousEntry().value;
        }
    }

    private class KeySet
    extends AbstractChar2DoubleSortedMap.KeySet {
        private KeySet() {
            super(Char2DoubleRBTreeMap.this);
        }

        @Override
        public CharBidirectionalIterator iterator() {
            return new KeyIterator();
        }

        @Override
        public CharBidirectionalIterator iterator(char from) {
            return new KeyIterator(from);
        }
    }

    private final class KeyIterator
    extends TreeIterator
    implements CharListIterator {
        public KeyIterator() {
        }

        public KeyIterator(char k) {
            super(k);
        }

        @Override
        public char nextChar() {
            return this.nextEntry().key;
        }

        @Override
        public char previousChar() {
            return this.previousEntry().key;
        }
    }

    private class EntryIterator
    extends TreeIterator
    implements ObjectListIterator<Char2DoubleMap.Entry> {
        EntryIterator() {
        }

        EntryIterator(char k) {
            super(k);
        }

        @Override
        public Char2DoubleMap.Entry next() {
            return this.nextEntry();
        }

        @Override
        public Char2DoubleMap.Entry previous() {
            return this.previousEntry();
        }
    }

    private class TreeIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index = 0;

        TreeIterator() {
            this.next = Char2DoubleRBTreeMap.this.firstEntry;
        }

        TreeIterator(char k) {
            this.next = Char2DoubleRBTreeMap.this.locateKey(k);
            if (this.next != null) {
                if (Char2DoubleRBTreeMap.this.compare(this.next.key, k) <= 0) {
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
            Char2DoubleRBTreeMap.this.remove(this.curr.key);
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
    extends AbstractChar2DoubleMap.BasicEntry
    implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 0x40000000;
        Entry left;
        Entry right;
        int info;

        Entry() {
            super('\u0000', 0.0);
        }

        Entry(char k, double v) {
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
        public double setValue(double value) {
            double oldValue = this.value;
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
            return this.key == ((Character)e.getKey()).charValue() && Double.doubleToLongBits(this.value) == Double.doubleToLongBits((Double)e.getValue());
        }

        @Override
        public int hashCode() {
            return this.key ^ HashCommon.double2int(this.value);
        }

        @Override
        public String toString() {
            return this.key + "=>" + this.value;
        }
    }
}

