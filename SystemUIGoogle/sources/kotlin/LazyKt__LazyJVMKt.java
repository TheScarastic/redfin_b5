package kotlin;

import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LazyJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class LazyKt__LazyJVMKt {
    public static <T> Lazy<T> lazy(Function0<? extends T> function0) {
        Intrinsics.checkNotNullParameter(function0, "initializer");
        return new SynchronizedLazyImpl(function0, null, 2, null);
    }
}
