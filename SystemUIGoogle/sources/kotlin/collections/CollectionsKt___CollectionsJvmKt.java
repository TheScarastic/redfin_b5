package kotlin.collections;

import java.util.Collections;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _CollectionsJvm.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class CollectionsKt___CollectionsJvmKt extends CollectionsKt__ReversedViewsKt {
    public static final <T> void reverse(List<T> list) {
        Intrinsics.checkNotNullParameter(list, "$this$reverse");
        Collections.reverse(list);
    }
}
