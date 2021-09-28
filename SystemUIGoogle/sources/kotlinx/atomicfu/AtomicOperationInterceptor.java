package kotlinx.atomicfu;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: Interceptor.kt */
/* loaded from: classes2.dex */
public class AtomicOperationInterceptor {
    public <T> void afterRMW(AtomicRef<T> atomicRef, T t, T t2) {
        Intrinsics.checkNotNullParameter(atomicRef, "ref");
    }

    public <T> void afterSet(AtomicRef<T> atomicRef, T t) {
        Intrinsics.checkNotNullParameter(atomicRef, "ref");
    }

    public <T> void beforeUpdate(AtomicRef<T> atomicRef) {
        Intrinsics.checkNotNullParameter(atomicRef, "ref");
    }
}
