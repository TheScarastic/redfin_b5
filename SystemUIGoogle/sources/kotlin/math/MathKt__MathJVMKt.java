package kotlin.math;
/* compiled from: MathJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class MathKt__MathJVMKt extends MathKt__MathHKt {
    public static int roundToInt(float f) {
        if (!Float.isNaN(f)) {
            return Math.round(f);
        }
        throw new IllegalArgumentException("Cannot round NaN value.");
    }
}
