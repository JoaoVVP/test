/*
 * Decompiled with CFR 0.152.
 */
package com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars;

import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.AbstractCharSortedSet;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharArrays;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharBidirectionalIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharCollection;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharComparators;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharIterators;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharListIterator;
import com.jelly.mightyminerv2.deps.it.unimi.dsi.fastutil.chars.CharSortedSet;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.SortedSet;

public class CharRBTreeSet
extends AbstractCharSortedSet
implements Serializable,
Cloneable,
CharSortedSet {
    protected transient Entry tree;
    protected int count;
    protected transient Entry firstEntry;
    protected transient Entry lastEntry;
    protected Comparator<? super Character> storedComparator;
    protected transient CharComparator actualComparator;
    private static final long serialVersionUID = -7046029254386353130L;
    private transient boolean[] dirPath;
    private transient Entry[] nodePath;

    public CharRBTreeSet() {
        this.allocatePaths();
        this.tree = null;
        this.count = 0;
    }

    private void setActualComparator() {
        this.actualComparator = CharComparators.asCharComparator(this.storedComparator);
    }

    public CharRBTreeSet(Comparator<? super Character> c) {
        this();
        this.storedComparator = c;
        this.setActualComparator();
    }

    public CharRBTreeSet(Collection<? extends Character> c) {
        this();
        this.addAll(c);
    }

    public CharRBTreeSet(SortedSet<Character> s) {
        this(s.comparator());
        this.addAll(s);
    }

    public CharRBTreeSet(CharCollection c) {
        this();
        this.addAll(c);
    }

    public CharRBTreeSet(CharSortedSet s) {
        this(s.comparator());
        this.addAll(s);
    }

    public CharRBTreeSet(CharIterator i) {
        this.allocatePaths();
        while (i.hasNext()) {
            this.add(i.nextChar());
        }
    }

    public CharRBTreeSet(Iterator<?> i) {
        this(CharIterators.asCharIterator(i));
    }

    public CharRBTreeSet(char[] a, int offset, int length, Comparator<? super Character> c) {
        this(c);
        CharArrays.ensureOffsetLength(a, offset, length);
        for (int i = 0; i < length; ++i) {
            this.add(a[offset + i]);
        }
    }

    public CharRBTreeSet(char[] a, int offset, int length) {
        this(a, offset, length, null);
    }

    public CharRBTreeSet(char[] a) {
        this();
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }

    public CharRBTreeSet(char[] a, Comparator<? super Character> c) {
        this(c);
        int i = a.length;
        while (i-- != 0) {
            this.add(a[i]);
        }
    }

    final int compare(char k1, char k2) {
        return this.actualComparator == null ? Character.compare(k1, k2) : this.actualComparator.compare(k1, k2);
    }

    private Entry findKey(char k) {
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

    @Override
    public boolean add(char k) {
        int maxDepth = 0;
        if (this.tree == null) {
            ++this.count;
            this.lastEntry = this.firstEntry = new Entry(k);
            this.tree = this.firstEntry;
        } else {
            Entry p = this.tree;
            int i = 0;
            while (true) {
                Entry e;
                int cmp;
                if ((cmp = this.compare(k, p.key)) == 0) {
                    while (i-- != 0) {
                        this.nodePath[i] = null;
                    }
                    return false;
                }
                this.nodePath[i] = p;
                this.dirPath[i++] = cmp > 0;
                if (this.dirPath[i++]) {
                    if (p.succ()) {
                        ++this.count;
                        e = new Entry(k);
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
                    e = new Entry(k);
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
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean remove(char k) {
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
                                        if (this.tree == null) {
                                            return false;
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
                                                        return false;
                                                    }
                                                    this.nodePath[i] = null;
                                                }
                                            }
                                            if ((p = p.left()) == null) break;
                                        }
                                        while (true) {
                                            if (i-- == 0) {
                                                return false;
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
        --this.count;
        while (maxDepth-- != 0) {
            this.nodePath[maxDepth] = null;
        }
        return true;
    }

    @Override
    public boolean contains(char k) {
        return this.findKey(k) != null;
    }

    @Override
    public void clear() {
        this.count = 0;
        this.tree = null;
        this.lastEntry = null;
        this.firstEntry = null;
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
    public char firstChar() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.firstEntry.key;
    }

    @Override
    public char lastChar() {
        if (this.tree == null) {
            throw new NoSuchElementException();
        }
        return this.lastEntry.key;
    }

    @Override
    public CharBidirectionalIterator iterator() {
        return new SetIterator();
    }

    @Override
    public CharBidirectionalIterator iterator(char from) {
        return new SetIterator(from);
    }

    @Override
    public CharComparator comparator() {
        return this.actualComparator;
    }

    @Override
    public CharSortedSet headSet(char to) {
        return new Subset('\u0000', true, to, false);
    }

    @Override
    public CharSortedSet tailSet(char from) {
        return new Subset(from, false, '\u0000', true);
    }

    @Override
    public CharSortedSet subSet(char from, char to) {
        return new Subset(from, false, to, false);
    }

    public Object clone() {
        CharRBTreeSet c;
        try {
            c = (CharRBTreeSet)super.clone();
        }
        catch (CloneNotSupportedException cantHappen) {
            throw new InternalError();
        }
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
        SetIterator i = new SetIterator();
        s.defaultWriteObject();
        while (n-- != 0) {
            s.writeChar(i.nextChar());
        }
    }

    private Entry readTree(ObjectInputStream s, int n, Entry pred, Entry succ) throws IOException, ClassNotFoundException {
        if (n == 1) {
            Entry top = new Entry(s.readChar());
            top.pred(pred);
            top.succ(succ);
            top.black(true);
            return top;
        }
        if (n == 2) {
            Entry top = new Entry(s.readChar());
            top.black(true);
            top.right(new Entry(s.readChar()));
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

    private final class Subset
    extends AbstractCharSortedSet
    implements Serializable,
    CharSortedSet {
        private static final long serialVersionUID = -7046029254386353129L;
        char from;
        char to;
        boolean bottom;
        boolean top;

        public Subset(char from, boolean bottom, char to, boolean top) {
            if (!bottom && !top && CharRBTreeSet.this.compare(from, to) > 0) {
                throw new IllegalArgumentException("Start element (" + from + ") is larger than end element (" + to + ")");
            }
            this.from = from;
            this.bottom = bottom;
            this.to = to;
            this.top = top;
        }

        @Override
        public void clear() {
            SubsetIterator i = new SubsetIterator();
            while (i.hasNext()) {
                i.nextChar();
                i.remove();
            }
        }

        final boolean in(char k) {
            return !(!this.bottom && CharRBTreeSet.this.compare(k, this.from) < 0 || !this.top && CharRBTreeSet.this.compare(k, this.to) >= 0);
        }

        @Override
        public boolean contains(char k) {
            return this.in(k) && CharRBTreeSet.this.contains(k);
        }

        @Override
        public boolean add(char k) {
            if (!this.in(k)) {
                throw new IllegalArgumentException("Element (" + k + ") out of range [" + (this.bottom ? "-" : String.valueOf(this.from)) + ", " + (this.top ? "-" : String.valueOf(this.to)) + ")");
            }
            return CharRBTreeSet.this.add(k);
        }

        @Override
        public boolean remove(char k) {
            if (!this.in(k)) {
                return false;
            }
            return CharRBTreeSet.this.remove(k);
        }

        @Override
        public int size() {
            SubsetIterator i = new SubsetIterator();
            int n = 0;
            while (i.hasNext()) {
                ++n;
                i.nextChar();
            }
            return n;
        }

        @Override
        public boolean isEmpty() {
            return !new SubsetIterator().hasNext();
        }

        @Override
        public CharComparator comparator() {
            return CharRBTreeSet.this.actualComparator;
        }

        @Override
        public CharBidirectionalIterator iterator() {
            return new SubsetIterator();
        }

        @Override
        public CharBidirectionalIterator iterator(char from) {
            return new SubsetIterator(from);
        }

        @Override
        public CharSortedSet headSet(char to) {
            if (this.top) {
                return new Subset(this.from, this.bottom, to, false);
            }
            return CharRBTreeSet.this.compare(to, this.to) < 0 ? new Subset(this.from, this.bottom, to, false) : this;
        }

        @Override
        public CharSortedSet tailSet(char from) {
            if (this.bottom) {
                return new Subset(from, false, this.to, this.top);
            }
            return CharRBTreeSet.this.compare(from, this.from) > 0 ? new Subset(from, false, this.to, this.top) : this;
        }

        @Override
        public CharSortedSet subSet(char from, char to) {
            if (this.top && this.bottom) {
                return new Subset(from, false, to, false);
            }
            if (!this.top) {
                char c = to = CharRBTreeSet.this.compare(to, this.to) < 0 ? to : this.to;
            }
            if (!this.bottom) {
                char c = from = CharRBTreeSet.this.compare(from, this.from) > 0 ? from : this.from;
            }
            if (!this.top && !this.bottom && from == this.from && to == this.to) {
                return this;
            }
            return new Subset(from, false, to, false);
        }

        public Entry firstEntry() {
            Entry e;
            if (CharRBTreeSet.this.tree == null) {
                return null;
            }
            if (this.bottom) {
                e = CharRBTreeSet.this.firstEntry;
            } else {
                e = CharRBTreeSet.this.locateKey(this.from);
                if (CharRBTreeSet.this.compare(e.key, this.from) < 0) {
                    e = e.next();
                }
            }
            if (e == null || !this.top && CharRBTreeSet.this.compare(e.key, this.to) >= 0) {
                return null;
            }
            return e;
        }

        public Entry lastEntry() {
            Entry e;
            if (CharRBTreeSet.this.tree == null) {
                return null;
            }
            if (this.top) {
                e = CharRBTreeSet.this.lastEntry;
            } else {
                e = CharRBTreeSet.this.locateKey(this.to);
                if (CharRBTreeSet.this.compare(e.key, this.to) >= 0) {
                    e = e.prev();
                }
            }
            if (e == null || !this.bottom && CharRBTreeSet.this.compare(e.key, this.from) < 0) {
                return null;
            }
            return e;
        }

        @Override
        public char firstChar() {
            Entry e = this.firstEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        @Override
        public char lastChar() {
            Entry e = this.lastEntry();
            if (e == null) {
                throw new NoSuchElementException();
            }
            return e.key;
        }

        private final class SubsetIterator
        extends SetIterator {
            SubsetIterator() {
                this.next = Subset.this.firstEntry();
            }

            /*
             * Enabled aggressive block sorting
             */
            SubsetIterator(char k) {
                this();
                if (this.next == null) return;
                if (!subset.bottom && subset.CharRBTreeSet.this.compare(k, this.next.key) < 0) {
                    this.prev = null;
                    return;
                }
                if (!subset.top) {
                    this.prev = subset.lastEntry();
                    if (subset.CharRBTreeSet.this.compare(k, this.prev.key) >= 0) {
                        this.next = null;
                        return;
                    }
                }
                this.next = subset.CharRBTreeSet.this.locateKey(k);
                if (subset.CharRBTreeSet.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                    return;
                }
                this.prev = this.next.prev();
            }

            @Override
            void updatePrevious() {
                this.prev = this.prev.prev();
                if (!Subset.this.bottom && this.prev != null && CharRBTreeSet.this.compare(this.prev.key, Subset.this.from) < 0) {
                    this.prev = null;
                }
            }

            @Override
            void updateNext() {
                this.next = this.next.next();
                if (!Subset.this.top && this.next != null && CharRBTreeSet.this.compare(this.next.key, Subset.this.to) >= 0) {
                    this.next = null;
                }
            }
        }
    }

    private class SetIterator
    implements CharListIterator {
        Entry prev;
        Entry next;
        Entry curr;
        int index = 0;

        SetIterator() {
            this.next = CharRBTreeSet.this.firstEntry;
        }

        SetIterator(char k) {
            this.next = CharRBTreeSet.this.locateKey(k);
            if (this.next != null) {
                if (CharRBTreeSet.this.compare(this.next.key, k) <= 0) {
                    this.prev = this.next;
                    this.next = this.next.next();
                } else {
                    this.prev = this.next.prev();
                }
            }
        }

        @Override
        public boolean hasNext() {
            return this.next != null;
        }

        @Override
        public boolean hasPrevious() {
            return this.prev != null;
        }

        void updateNext() {
            this.next = this.next.next();
        }

        void updatePrevious() {
            this.prev = this.prev.prev();
        }

        @Override
        public char nextChar() {
            return this.nextEntry().key;
        }

        @Override
        public char previousChar() {
            return this.previousEntry().key;
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

        Entry previousEntry() {
            if (!this.hasPrevious()) {
                throw new NoSuchElementException();
            }
            this.curr = this.next = this.prev;
            --this.index;
            this.updatePrevious();
            return this.curr;
        }

        @Override
        public int nextIndex() {
            return this.index;
        }

        @Override
        public int previousIndex() {
            return this.index - 1;
        }

        @Override
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
            CharRBTreeSet.this.remove(this.curr.key);
            this.curr = null;
        }
    }

    private static final class Entry
    implements Cloneable {
        private static final int BLACK_MASK = 1;
        private static final int SUCC_MASK = Integer.MIN_VALUE;
        private static final int PRED_MASK = 0x40000000;
        char key;
        Entry left;
        Entry right;
        int info;

        Entry() {
        }

        Entry(char k) {
            this.key = k;
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

        public Entry clone() {
            Entry c;
            try {
                c = (Entry)super.clone();
            }
            catch (CloneNotSupportedException cantHappen) {
                throw new InternalError();
            }
            c.key = this.key;
            c.info = this.info;
            return c;
        }

        public boolean equals(Object o) {
            if (!(o instanceof Entry)) {
                return false;
            }
            Entry e = (Entry)o;
            return this.key == e.key;
        }

        public int hashCode() {
            return this.key;
        }

        public String toString() {
            return String.valueOf(this.key);
        }
    }
}

