package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
/* compiled from: _Strings.kt */
/* loaded from: classes2.dex */
class StringsKt___StringsKt extends StringsKt___StringsJvmKt {
    public static final String drop(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "$this$drop");
        if (i >= 0) {
            String substring = str.substring(RangesKt___RangesKt.coerceAtMost(i, str.length()));
            Intrinsics.checkNotNullExpressionValue(substring, "(this as java.lang.String).substring(startIndex)");
            return substring;
        }
        throw new IllegalArgumentException(("Requested character count " + i + " is less than zero.").toString());
    }
}
