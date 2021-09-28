package kotlin.collections;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _Sets.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class SetsKt___SetsKt extends SetsKt__SetsKt {
    public static <T> Set<T> minus(Set<? extends T> set, Iterable<? extends T> iterable) {
        Intrinsics.checkNotNullParameter(set, "$this$minus");
        Intrinsics.checkNotNullParameter(iterable, "elements");
        Collection<?> convertToSetForSetOperationWith = CollectionsKt__IterablesKt.convertToSetForSetOperationWith(iterable, set);
        if (convertToSetForSetOperationWith.isEmpty()) {
            return CollectionsKt___CollectionsKt.toSet(set);
        }
        if (convertToSetForSetOperationWith instanceof Set) {
            LinkedHashSet linkedHashSet = new LinkedHashSet();
            for (T t : set) {
                if (!convertToSetForSetOperationWith.contains(t)) {
                    linkedHashSet.add(t);
                }
            }
            return linkedHashSet;
        }
        LinkedHashSet linkedHashSet2 = new LinkedHashSet(set);
        linkedHashSet2.removeAll(convertToSetForSetOperationWith);
        return linkedHashSet2;
    }
}
