package kotlinx.atomicfu;

import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: AtomicFU.kt */
/* loaded from: classes2.dex */
public final class AtomicRef<T> {
    private static final Companion Companion = new Companion(null);
    @Deprecated
    private static final AtomicReferenceFieldUpdater<AtomicRef<?>, Object> FU = AtomicReferenceFieldUpdater.newUpdater(AtomicRef.class, Object.class, "value");
    private volatile T value;

    public AtomicRef(T t) {
        this.value = t;
    }

    public final T getValue() {
        return this.value;
    }

    public final void setValue(T t) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        this.value = t;
        InterceptorKt.getInterceptor().afterSet(this, t);
    }

    public final boolean compareAndSet(T t, T t2) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        boolean compareAndSet = FU.compareAndSet(this, t, t2);
        if (compareAndSet) {
            InterceptorKt.getInterceptor().afterRMW(this, t, t2);
        }
        return compareAndSet;
    }

    public final T getAndSet(T t) {
        InterceptorKt.getInterceptor().beforeUpdate(this);
        T t2 = (T) FU.getAndSet(this, t);
        InterceptorKt.getInterceptor().afterRMW(this, t2, t);
        return t2;
    }

    public String toString() {
        return String.valueOf(this.value);
    }

    /* compiled from: AtomicFU.kt */
    /* loaded from: classes2.dex */
    private static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
