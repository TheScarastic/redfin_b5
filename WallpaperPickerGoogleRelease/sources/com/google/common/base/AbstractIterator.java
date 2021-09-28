package com.google.common.base;

import androidx.constraintlayout.solver.SolverVariable$Type$r8$EnumUnboxingUtility;
import com.google.common.base.Splitter;
import java.util.Iterator;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public abstract class AbstractIterator<T> implements Iterator<T> {
    public T next;
    public int state = 2;

    @Override // java.util.Iterator
    public final boolean hasNext() {
        T t;
        int separatorStart;
        int i = this.state;
        if (i != 4) {
            int $enumboxing$ordinal = SolverVariable$Type$r8$EnumUnboxingUtility.$enumboxing$ordinal(i);
            if ($enumboxing$ordinal == 0) {
                return true;
            }
            if ($enumboxing$ordinal != 2) {
                this.state = 4;
                Splitter.SplittingIterator splittingIterator = (Splitter.SplittingIterator) this;
                int i2 = splittingIterator.offset;
                while (true) {
                    int i3 = splittingIterator.offset;
                    if (i3 == -1) {
                        splittingIterator.state = 3;
                        t = null;
                        break;
                    }
                    separatorStart = splittingIterator.separatorStart(i3);
                    if (separatorStart == -1) {
                        separatorStart = splittingIterator.toSplit.length();
                        splittingIterator.offset = -1;
                    } else {
                        splittingIterator.offset = splittingIterator.separatorEnd(separatorStart);
                    }
                    int i4 = splittingIterator.offset;
                    if (i4 == i2) {
                        int i5 = i4 + 1;
                        splittingIterator.offset = i5;
                        if (i5 > splittingIterator.toSplit.length()) {
                            splittingIterator.offset = -1;
                        }
                    } else {
                        while (i2 < separatorStart && splittingIterator.trimmer.matches(splittingIterator.toSplit.charAt(i2))) {
                            i2++;
                        }
                        while (separatorStart > i2) {
                            int i6 = separatorStart - 1;
                            if (!splittingIterator.trimmer.matches(splittingIterator.toSplit.charAt(i6))) {
                                break;
                            }
                            separatorStart = i6;
                        }
                        if (!splittingIterator.omitEmptyStrings || i2 != separatorStart) {
                            break;
                        }
                        i2 = splittingIterator.offset;
                    }
                }
                int i7 = splittingIterator.limit;
                if (i7 == 1) {
                    separatorStart = splittingIterator.toSplit.length();
                    splittingIterator.offset = -1;
                    while (separatorStart > i2) {
                        int i8 = separatorStart - 1;
                        if (!splittingIterator.trimmer.matches(splittingIterator.toSplit.charAt(i8))) {
                            break;
                        }
                        separatorStart = i8;
                    }
                } else {
                    splittingIterator.limit = i7 - 1;
                }
                t = (T) splittingIterator.toSplit.subSequence(i2, separatorStart).toString();
                this.next = t;
                if (this.state != 3) {
                    this.state = 1;
                    return true;
                }
            }
            return false;
        }
        throw new IllegalStateException();
    }

    @Override // java.util.Iterator
    public final T next() {
        if (hasNext()) {
            this.state = 2;
            T t = this.next;
            this.next = null;
            return t;
        }
        throw new NoSuchElementException();
    }

    @Override // java.util.Iterator
    public final void remove() {
        throw new UnsupportedOperationException();
    }
}
