package kotlin.comparisons;

import java.util.Comparator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Comparisons.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ComparisonsKt__ComparisonsKt {
    /* access modifiers changed from: private */
    public static final <T> int compareValuesByImpl$ComparisonsKt__ComparisonsKt(T t, T t2, Function1<? super T, ? extends Comparable<?>>[] function1Arr) {
        int compareValues;
        for (Function1<? super T, ? extends Comparable<?>> function1 : function1Arr) {
            compareValues = compareValues((Comparable) function1.invoke(t), (Comparable) function1.invoke(t2));
            if (compareValues != 0) {
                return compareValues;
            }
        }
        return 0;
    }

    public static <T extends Comparable<?>> int compareValues(T t, T t2) {
        if (t == t2) {
            return 0;
        }
        if (t == null) {
            return -1;
        }
        if (t2 == null) {
            return 1;
        }
        return t.compareTo(t2);
    }

    public static <T> Comparator<T> compareBy(Function1<? super T, ? extends Comparable<?>>... function1Arr) {
        Intrinsics.checkNotNullParameter(function1Arr, "selectors");
        if (function1Arr.length > 0) {
            return new Comparator<T>(function1Arr) { // from class: kotlin.comparisons.ComparisonsKt__ComparisonsKt$compareBy$1
                final /* synthetic */ Function1[] $selectors;

                /* access modifiers changed from: package-private */
                {
                    this.$selectors = r1;
                }

                @Override // java.util.Comparator
                public final int compare(T t, T t2) {
                    return ComparisonsKt__ComparisonsKt.compareValuesByImpl$ComparisonsKt__ComparisonsKt(t, t2, this.$selectors);
                }
            };
        }
        throw new IllegalArgumentException("Failed requirement.".toString());
    }
}
