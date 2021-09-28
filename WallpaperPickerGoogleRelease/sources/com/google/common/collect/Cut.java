package com.google.common.collect;

import androidx.viewpager2.widget.FakeDrag$$ExternalSyntheticOutline0;
import java.io.Serializable;
import java.lang.Comparable;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class Cut<C extends Comparable> implements Comparable<Cut<C>>, Serializable {
    private static final long serialVersionUID = 0;
    public final C endpoint;

    /* loaded from: classes.dex */
    public static final class AboveAll extends Cut<Comparable<?>> {
        public static final AboveAll INSTANCE = new AboveAll();
        private static final long serialVersionUID = 0;

        public AboveAll() {
            super(null);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.Cut
        public int compareTo(Cut<Comparable<?>> cut) {
            return cut == this ? 0 : 1;
        }

        @Override // com.google.common.collect.Cut
        public void describeAsLowerBound(StringBuilder sb) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        public void describeAsUpperBound(StringBuilder sb) {
            sb.append("+∞)");
        }

        @Override // com.google.common.collect.Cut
        public Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // com.google.common.collect.Cut
        public boolean isLessThan(Comparable<?> comparable) {
            return false;
        }

        @Override // java.lang.Object
        public String toString() {
            return "+∞";
        }
    }

    /* loaded from: classes.dex */
    public static final class AboveValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public AboveValue(C c) {
            super(c);
            Objects.requireNonNull(c);
        }

        @Override // com.google.common.collect.Cut
        public void describeAsLowerBound(StringBuilder sb) {
            sb.append('(');
            sb.append(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        public void describeAsUpperBound(StringBuilder sb) {
            sb.append(this.endpoint);
            sb.append(']');
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return ~this.endpoint.hashCode();
        }

        @Override // com.google.common.collect.Cut
        public boolean isLessThan(C c) {
            C c2 = this.endpoint;
            Range<Comparable> range = Range.ALL;
            return c2.compareTo(c) < 0;
        }

        @Override // java.lang.Object
        public String toString() {
            String valueOf = String.valueOf(this.endpoint);
            return FakeDrag$$ExternalSyntheticOutline0.m(valueOf.length() + 2, "/", valueOf, "\\");
        }
    }

    /* loaded from: classes.dex */
    public static final class BelowAll extends Cut<Comparable<?>> {
        public static final BelowAll INSTANCE = new BelowAll();
        private static final long serialVersionUID = 0;

        public BelowAll() {
            super(null);
        }

        private Object readResolve() {
            return INSTANCE;
        }

        @Override // com.google.common.collect.Cut
        public int compareTo(Cut<Comparable<?>> cut) {
            return cut == this ? 0 : -1;
        }

        @Override // com.google.common.collect.Cut
        public void describeAsLowerBound(StringBuilder sb) {
            sb.append("(-∞");
        }

        @Override // com.google.common.collect.Cut
        public void describeAsUpperBound(StringBuilder sb) {
            throw new AssertionError();
        }

        @Override // com.google.common.collect.Cut
        public Comparable<?> endpoint() {
            throw new IllegalStateException("range unbounded on this side");
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return System.identityHashCode(this);
        }

        @Override // com.google.common.collect.Cut
        public boolean isLessThan(Comparable<?> comparable) {
            return true;
        }

        @Override // java.lang.Object
        public String toString() {
            return "-∞";
        }
    }

    /* loaded from: classes.dex */
    public static final class BelowValue<C extends Comparable> extends Cut<C> {
        private static final long serialVersionUID = 0;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        public BelowValue(C c) {
            super(c);
            Objects.requireNonNull(c);
        }

        @Override // com.google.common.collect.Cut
        public void describeAsLowerBound(StringBuilder sb) {
            sb.append('[');
            sb.append(this.endpoint);
        }

        @Override // com.google.common.collect.Cut
        public void describeAsUpperBound(StringBuilder sb) {
            sb.append(this.endpoint);
            sb.append(')');
        }

        @Override // com.google.common.collect.Cut, java.lang.Object
        public int hashCode() {
            return this.endpoint.hashCode();
        }

        @Override // com.google.common.collect.Cut
        public boolean isLessThan(C c) {
            C c2 = this.endpoint;
            Range<Comparable> range = Range.ALL;
            return c2.compareTo(c) <= 0;
        }

        @Override // java.lang.Object
        public String toString() {
            String valueOf = String.valueOf(this.endpoint);
            return FakeDrag$$ExternalSyntheticOutline0.m(valueOf.length() + 2, "\\", valueOf, "/");
        }
    }

    public Cut(C c) {
        this.endpoint = c;
    }

    @Override // java.lang.Comparable
    public /* bridge */ /* synthetic */ int compareTo(Object obj) {
        return compareTo((Cut) ((Cut) obj));
    }

    public abstract void describeAsLowerBound(StringBuilder sb);

    public abstract void describeAsUpperBound(StringBuilder sb);

    public C endpoint() {
        return this.endpoint;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        if (!(obj instanceof Cut)) {
            return false;
        }
        try {
            if (compareTo((Cut) ((Cut) obj)) == 0) {
                return true;
            }
            return false;
        } catch (ClassCastException unused) {
            return false;
        }
    }

    @Override // java.lang.Object
    public abstract int hashCode();

    public abstract boolean isLessThan(C c);

    public int compareTo(Cut<C> cut) {
        if (cut == BelowAll.INSTANCE) {
            return 1;
        }
        if (cut == AboveAll.INSTANCE) {
            return -1;
        }
        C c = this.endpoint;
        C c2 = cut.endpoint;
        Range<Comparable> range = Range.ALL;
        int compareTo = c.compareTo(c2);
        if (compareTo != 0) {
            return compareTo;
        }
        boolean z = this instanceof AboveValue;
        if (z == (cut instanceof AboveValue)) {
            return 0;
        }
        if (z) {
            return 1;
        }
        return -1;
    }
}
