package kotlin.text;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: StringNumberConversionsJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class StringsKt__StringNumberConversionsJVMKt extends StringsKt__StringBuilderKt {
    public static Float toFloatOrNull(String str) {
        Intrinsics.checkNotNullParameter(str, "$this$toFloatOrNull");
        try {
            if (ScreenFloatValueRegEx.value.matches(str)) {
                return Float.valueOf(Float.parseFloat(str));
            }
            return null;
        } catch (NumberFormatException unused) {
            return null;
        }
    }
}
