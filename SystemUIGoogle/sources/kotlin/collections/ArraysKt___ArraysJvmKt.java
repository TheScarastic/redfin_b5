package kotlin.collections;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: _ArraysJvm.kt */
/* loaded from: classes2.dex */
public class ArraysKt___ArraysJvmKt extends ArraysKt__ArraysKt {
    public static <T> List<T> asList(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$asList");
        List<T> asList = ArraysUtilJVM.asList(tArr);
        Intrinsics.checkNotNullExpressionValue(asList, "ArraysUtilJVM.asList(this)");
        return asList;
    }

    public static /* synthetic */ Object[] copyInto$default(Object[] objArr, Object[] objArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = objArr.length;
        }
        return copyInto(objArr, objArr2, i, i2, i3);
    }

    public static final <T> T[] copyInto(T[] tArr, T[] tArr2, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(tArr, "$this$copyInto");
        Intrinsics.checkNotNullParameter(tArr2, "destination");
        System.arraycopy(tArr, i2, tArr2, i, i3 - i2);
        return tArr2;
    }

    public static /* synthetic */ byte[] copyInto$default(byte[] bArr, byte[] bArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = bArr.length;
        }
        return copyInto(bArr, bArr2, i, i2, i3);
    }

    public static byte[] copyInto(byte[] bArr, byte[] bArr2, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(bArr, "$this$copyInto");
        Intrinsics.checkNotNullParameter(bArr2, "destination");
        System.arraycopy(bArr, i2, bArr2, i, i3 - i2);
        return bArr2;
    }

    public static /* synthetic */ float[] copyInto$default(float[] fArr, float[] fArr2, int i, int i2, int i3, int i4, Object obj) {
        if ((i4 & 2) != 0) {
            i = 0;
        }
        if ((i4 & 4) != 0) {
            i2 = 0;
        }
        if ((i4 & 8) != 0) {
            i3 = fArr.length;
        }
        return copyInto(fArr, fArr2, i, i2, i3);
    }

    public static final float[] copyInto(float[] fArr, float[] fArr2, int i, int i2, int i3) {
        Intrinsics.checkNotNullParameter(fArr, "$this$copyInto");
        Intrinsics.checkNotNullParameter(fArr2, "destination");
        System.arraycopy(fArr, i2, fArr2, i, i3 - i2);
        return fArr2;
    }

    public static byte[] copyOfRange(byte[] bArr, int i, int i2) {
        Intrinsics.checkNotNullParameter(bArr, "$this$copyOfRangeImpl");
        ArraysKt__ArraysJVMKt.copyOfRangeToIndexCheck(i2, bArr.length);
        byte[] copyOfRange = Arrays.copyOfRange(bArr, i, i2);
        Intrinsics.checkNotNullExpressionValue(copyOfRange, "java.util.Arrays.copyOfR…this, fromIndex, toIndex)");
        return copyOfRange;
    }

    public static final <T> void fill(T[] tArr, T t, int i, int i2) {
        Intrinsics.checkNotNullParameter(tArr, "$this$fill");
        Arrays.fill(tArr, i, i2, t);
    }

    public static int[] plus(int[] iArr, int[] iArr2) {
        Intrinsics.checkNotNullParameter(iArr, "$this$plus");
        Intrinsics.checkNotNullParameter(iArr2, "elements");
        int length = iArr.length;
        int length2 = iArr2.length;
        int[] copyOf = Arrays.copyOf(iArr, length + length2);
        System.arraycopy(iArr2, 0, copyOf, length, length2);
        Intrinsics.checkNotNullExpressionValue(copyOf, "result");
        return copyOf;
    }

    public static final <T> void sort(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$sort");
        if (tArr.length > 1) {
            Arrays.sort(tArr);
        }
    }

    public static <T> void sortWith(T[] tArr, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(tArr, "$this$sortWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (tArr.length > 1) {
            Arrays.sort(tArr, comparator);
        }
    }
}
