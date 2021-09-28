package kotlin.text;

import kotlin.ranges.IntRange;
/* compiled from: CharJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class CharsKt__CharJVMKt {
    public static final boolean isWhitespace(char c) {
        return Character.isWhitespace(c) || Character.isSpaceChar(c);
    }

    public static final int digitOf(char c, int i) {
        return Character.digit((int) c, i);
    }

    public static final int checkRadix(int i) {
        if (2 <= i && 36 >= i) {
            return i;
        }
        throw new IllegalArgumentException("radix " + i + " was not in valid range " + new IntRange(2, 36));
    }
}
