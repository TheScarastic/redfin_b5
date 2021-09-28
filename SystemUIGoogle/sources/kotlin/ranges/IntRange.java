package kotlin.ranges;

import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: Ranges.kt */
/* loaded from: classes2.dex */
public final class IntRange extends IntProgression {
    public static final Companion Companion = new Companion(null);
    private static final IntRange EMPTY = new IntRange(1, 0);

    public IntRange(int i, int i2) {
        super(i, i2, 1);
    }

    public Integer getStart() {
        return Integer.valueOf(getFirst());
    }

    public Integer getEndInclusive() {
        return Integer.valueOf(getLast());
    }

    @Override // kotlin.ranges.IntProgression
    public boolean isEmpty() {
        return getFirst() > getLast();
    }

    @Override // kotlin.ranges.IntProgression, java.lang.Object
    public boolean equals(Object obj) {
        if (obj instanceof IntRange) {
            if (!isEmpty() || !((IntRange) obj).isEmpty()) {
                IntRange intRange = (IntRange) obj;
                if (!(getFirst() == intRange.getFirst() && getLast() == intRange.getLast())) {
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
        return getLast() + (getFirst() * 31);
    }

    @Override // kotlin.ranges.IntProgression, java.lang.Object
    public String toString() {
        return getFirst() + ".." + getLast();
    }

    /* compiled from: Ranges.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final IntRange getEMPTY() {
            return IntRange.EMPTY;
        }
    }
}
