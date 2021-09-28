package kotlin.collections;
/* compiled from: ArraysJVM.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public class ArraysKt__ArraysJVMKt {
    public static final void copyOfRangeToIndexCheck(int i, int i2) {
        if (i > i2) {
            throw new IndexOutOfBoundsException("toIndex (" + i + ") is greater than size (" + i2 + ").");
        }
    }
}
