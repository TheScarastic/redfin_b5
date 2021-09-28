package kotlin.ranges;

import java.util.NoSuchElementException;
import kotlin.collections.IntIterator;
/* compiled from: ProgressionIterators.kt */
/* loaded from: classes2.dex */
public final class IntProgressionIterator extends IntIterator {
    private final int finalElement;
    private boolean hasNext;
    private int next;
    private final int step;

    public IntProgressionIterator(int i, int i2, int i3) {
        this.step = i3;
        this.finalElement = i2;
        boolean z = true;
        z = false;
        if (i3 <= 0 ? i < i2 : i > i2) {
        }
        this.hasNext = z;
        this.next = !z ? i2 : i;
    }

    @Override // java.util.Iterator
    public boolean hasNext() {
        return this.hasNext;
    }

    @Override // kotlin.collections.IntIterator
    public int nextInt() {
        int i = this.next;
        if (i != this.finalElement) {
            this.next = this.step + i;
        } else if (this.hasNext) {
            this.hasNext = false;
        } else {
            throw new NoSuchElementException();
        }
        return i;
    }
}
