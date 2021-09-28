package kotlin.ranges;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
/* loaded from: classes.dex */
public final class IntRange extends IntProgression {
    static {
        new IntRange(1, 0);
    }

    public IntRange(int i, int i2) {
        super(i, i2, 1);
    }

    @Override // kotlin.ranges.IntProgression, java.lang.Object
    public boolean equals(@Nullable Object obj) {
        if (obj instanceof IntRange) {
            if (!isEmpty() || !((IntRange) obj).isEmpty()) {
                IntRange intRange = (IntRange) obj;
                if (!(this.first == intRange.first && this.last == intRange.last)) {
                }
            }
            return true;
        }
        return false;
    }

    @Override // kotlin.ranges.IntProgression, java.lang.Object
    public int hashCode() {
        if (isEmpty()) {
            return -1;
        }
        return this.last + (this.first * 31);
    }

    @Override // kotlin.ranges.IntProgression
    public boolean isEmpty() {
        return this.first > this.last;
    }

    @Override // kotlin.ranges.IntProgression, java.lang.Object
    @NotNull
    public String toString() {
        return this.first + ".." + this.last;
    }
}
