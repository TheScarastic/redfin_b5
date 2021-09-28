package kotlin.collections;

import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sets.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SetsKt__SetsKt extends SetsKt__SetsJVMKt {
    public static <T> Set<T> emptySet() {
        return EmptySet.INSTANCE;
    }

    public static <T> Set<T> setOf(T... tArr) {
        Intrinsics.checkNotNullParameter(tArr, "elements");
        return tArr.length > 0 ? ArraysKt___ArraysKt.toSet(tArr) : emptySet();
    }

    public static <T> Set<T> mutableSetOf(T... tArr) {
        Intrinsics.checkNotNullParameter(tArr, "elements");
        return (Set) ArraysKt___ArraysKt.toCollection(tArr, new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(tArr.length)));
    }

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: java.util.Set<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    public static final <T> Set<T> optimizeReadOnlySet(Set<? extends T> set) {
        Intrinsics.checkNotNullParameter(set, "$this$optimizeReadOnlySet");
        int size = set.size();
        if (size == 0) {
            return emptySet();
        }
        if (size != 1) {
            return set;
        }
        return SetsKt__SetsJVMKt.setOf(set.iterator().next());
    }
}
