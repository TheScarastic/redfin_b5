package kotlin;

import java.io.Serializable;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Result.kt */
/* loaded from: classes2.dex */
public final class Result<T> implements Serializable {
    public static final Companion Companion = new Companion(null);
    private final Object value;

    /* renamed from: constructor-impl  reason: not valid java name */
    public static Object m670constructorimpl(Object obj) {
        return obj;
    }

    /* renamed from: equals-impl  reason: not valid java name */
    public static boolean m671equalsimpl(Object obj, Object obj2) {
        return (obj2 instanceof Result) && Intrinsics.areEqual(obj, ((Result) obj2).m677unboximpl());
    }

    /* renamed from: hashCode-impl  reason: not valid java name */
    public static int m673hashCodeimpl(Object obj) {
        if (obj != null) {
            return obj.hashCode();
        }
        return 0;
    }

    @Override // java.lang.Object
    public boolean equals(Object obj) {
        return m671equalsimpl(this.value, obj);
    }

    @Override // java.lang.Object
    public int hashCode() {
        return m673hashCodeimpl(this.value);
    }

    @Override // java.lang.Object
    public String toString() {
        return m676toStringimpl(this.value);
    }

    /* renamed from: unbox-impl  reason: not valid java name */
    public final /* synthetic */ Object m677unboximpl() {
        return this.value;
    }

    /* renamed from: isSuccess-impl  reason: not valid java name */
    public static final boolean m675isSuccessimpl(Object obj) {
        return !(obj instanceof Failure);
    }

    /* renamed from: isFailure-impl  reason: not valid java name */
    public static final boolean m674isFailureimpl(Object obj) {
        return obj instanceof Failure;
    }

    /* renamed from: exceptionOrNull-impl  reason: not valid java name */
    public static final Throwable m672exceptionOrNullimpl(Object obj) {
        if (obj instanceof Failure) {
            return ((Failure) obj).exception;
        }
        return null;
    }

    /* renamed from: toString-impl  reason: not valid java name */
    public static String m676toStringimpl(Object obj) {
        if (obj instanceof Failure) {
            return obj.toString();
        }
        return "Success(" + obj + ')';
    }

    /* compiled from: Result.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        private Companion() {
        }

        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }
    }

    /* compiled from: Result.kt */
    /* loaded from: classes2.dex */
    public static final class Failure implements Serializable {
        public final Throwable exception;

        public Failure(Throwable th) {
            Intrinsics.checkNotNullParameter(th, "exception");
            this.exception = th;
        }

        @Override // java.lang.Object
        public boolean equals(Object obj) {
            return (obj instanceof Failure) && Intrinsics.areEqual(this.exception, ((Failure) obj).exception);
        }

        @Override // java.lang.Object
        public int hashCode() {
            return this.exception.hashCode();
        }

        @Override // java.lang.Object
        public String toString() {
            return "Failure(" + this.exception + ')';
        }
    }
}
