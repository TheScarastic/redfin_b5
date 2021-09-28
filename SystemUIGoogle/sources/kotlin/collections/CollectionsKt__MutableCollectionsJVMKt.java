package kotlin.collections;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MutableCollectionsJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class CollectionsKt__MutableCollectionsJVMKt extends CollectionsKt__IteratorsKt {
    public static <T extends Comparable<? super T>> void sort(List<T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$sort");
        if (list.size() > 1) {
            Collections.sort(list);
        }
    }

    public static <T> void sortWith(List<T> list, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(list, "$this$sortWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (list.size() > 1) {
            Collections.sort(list, comparator);
        }
    }
}
