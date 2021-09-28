package androidx.core.math;
/* loaded from: classes.dex */
public class MathUtils {
    public static int clamp(int i, int i2, int i3) {
        return i < i2 ? i2 : i > i3 ? i3 : i;
    }
}
