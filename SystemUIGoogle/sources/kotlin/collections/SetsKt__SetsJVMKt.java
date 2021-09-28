package kotlin.collections;

import java.util.Collections;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SetsJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SetsKt__SetsJVMKt {
    public static <T> Set<T> setOf(T t) {
        Set<T> singleton = Collections.singleton(t);
        Intrinsics.checkNotNullExpressionValue(singleton, "java.util.Collections.singleton(element)");
        return singleton;
    }
}
