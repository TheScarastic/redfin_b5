package com.google.common.collect;

import androidx.constraintlayout.solver.SolverVariable$Type$r8$EnumUnboxingUtility;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public abstract class AbstractIterator<T> extends UnmodifiableIterator<T> {
    public T next;
    public int state = 2;

    public abstract T computeNext();

    public final T endOfData() {
        this.state = 3;
        return null;
    }

    @Override // java.util.Iterator
    public final boolean hasNext() {
        int i = this.state;
        if (i != 4) {
            int $enumboxing$ordinal = SolverVariable$Type$r8$EnumUnboxingUtility.$enumboxing$ordinal(i);
            if ($enumboxing$ordinal == 0) {
                return true;
            }
            if ($enumboxing$ordinal == 2) {
                return false;
            }
            this.state = 4;
            this.next = computeNext();
            if (this.state == 3) {
                return false;
            }
            this.state = 1;
            return true;
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
}
