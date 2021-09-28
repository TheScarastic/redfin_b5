package com.google.common.collect;

import com.google.common.collect.Cut;
import java.lang.Comparable;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Range<C extends Comparable> extends RangeGwtSerializationDependencies {
    public static final Range<Comparable> ALL = new Range<>(Cut.BelowAll.INSTANCE, Cut.AboveAll.INSTANCE);
    private static final long serialVersionUID = 0;
    public final Cut<C> lowerBound;
    public final Cut<C> upperBound;

    public Range(Cut<C> cut, Cut<C> cut2) {
        Objects.requireNonNull(cut);
        this.lowerBound = cut;
        Objects.requireNonNull(cut2);
        this.upperBound = cut2;
        if (cut.compareTo((Cut) cut2) > 0 || cut == Cut.AboveAll.INSTANCE || cut2 == Cut.BelowAll.INSTANCE) {
            StringBuilder sb = new StringBuilder(16);
            cut.describeAsLowerBound(sb);
            sb.append("..");
            cut2.describeAsUpperBound(sb);
            String valueOf = String.valueOf(sb.toString());
            throw new IllegalArgumentException(valueOf.length() != 0 ? "Invalid range: ".concat(valueOf) : new String("Invalid range: "));
        }
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (!(obj instanceof Range)) {
            return false;
        }
        Range range = (Range) obj;
        if (!this.lowerBound.equals(range.lowerBound) || !this.upperBound.equals(range.upperBound)) {
            return false;
        }
        return true;
    }

    @Override // java.lang.Object
    public int hashCode() {
        return this.upperBound.hashCode() + (this.lowerBound.hashCode() * 31);
    }

    public Object readResolve() {
        Range<Comparable> range = ALL;
        return equals(range) ? range : this;
    }

    @Override // java.lang.Object
    public String toString() {
        Cut<C> cut = this.lowerBound;
        Cut<C> cut2 = this.upperBound;
        StringBuilder sb = new StringBuilder(16);
        cut.describeAsLowerBound(sb);
        sb.append("..");
        cut2.describeAsUpperBound(sb);
        return sb.toString();
    }
}
