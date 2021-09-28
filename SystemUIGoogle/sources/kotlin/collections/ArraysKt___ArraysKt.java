package kotlin.collections;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.ArrayIteratorKt;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
import kotlin.sequences.SequencesKt__SequencesKt;
/* compiled from: _Arrays.kt */
/* loaded from: classes2.dex */
public class ArraysKt___ArraysKt extends ArraysKt___ArraysJvmKt {
    public static <T> boolean contains(T[] tArr, T t) {
        Intrinsics.checkNotNullParameter(tArr, "$this$contains");
        return indexOf(tArr, t) >= 0;
    }

    public static boolean contains(int[] iArr, int i) {
        Intrinsics.checkNotNullParameter(iArr, "$this$contains");
        return indexOf(iArr, i) >= 0;
    }

    public static final <T> int indexOf(T[] tArr, T t) {
        Intrinsics.checkNotNullParameter(tArr, "$this$indexOf");
        int i = 0;
        if (t == null) {
            int length = tArr.length;
            while (i < length) {
                if (tArr[i] == null) {
                    return i;
                }
                i++;
            }
            return -1;
        }
        int length2 = tArr.length;
        while (i < length2) {
            if (Intrinsics.areEqual(t, tArr[i])) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static final int indexOf(int[] iArr, int i) {
        Intrinsics.checkNotNullParameter(iArr, "$this$indexOf");
        int length = iArr.length;
        for (int i2 = 0; i2 < length; i2++) {
            if (i == iArr[i2]) {
                return i2;
            }
        }
        return -1;
    }

    public static char single(char[] cArr) {
        Intrinsics.checkNotNullParameter(cArr, "$this$single");
        int length = cArr.length;
        if (length == 0) {
            throw new NoSuchElementException("Array is empty.");
        } else if (length == 1) {
            return cArr[0];
        } else {
            throw new IllegalArgumentException("Array has more than one element.");
        }
    }

    public static <T> T singleOrNull(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$singleOrNull");
        if (tArr.length == 1) {
            return tArr[0];
        }
        return null;
    }

    public static <T> List<T> drop(T[] tArr, int i) {
        Intrinsics.checkNotNullParameter(tArr, "$this$drop");
        if (i >= 0) {
            return takeLast(tArr, RangesKt___RangesKt.coerceAtLeast(tArr.length - i, 0));
        }
        throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
    }

    public static <T> List<T> filterNotNull(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$filterNotNull");
        return (List) filterNotNullTo(tArr, new ArrayList());
    }

    public static final <C extends Collection<? super T>, T> C filterNotNullTo(T[] tArr, C c) {
        Intrinsics.checkNotNullParameter(tArr, "$this$filterNotNullTo");
        Intrinsics.checkNotNullParameter(c, "destination");
        for (T t : tArr) {
            if (t != null) {
                c.add(t);
            }
        }
        return c;
    }

    public static final <T> List<T> takeLast(T[] tArr, int i) {
        Intrinsics.checkNotNullParameter(tArr, "$this$takeLast");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        } else {
            int length = tArr.length;
            if (i >= length) {
                return toList(tArr);
            }
            if (i == 1) {
                return CollectionsKt__CollectionsJVMKt.listOf(tArr[length - 1]);
            }
            ArrayList arrayList = new ArrayList(i);
            for (int i2 = length - i; i2 < length; i2++) {
                arrayList.add(tArr[i2]);
            }
            return arrayList;
        }
    }

    public static final <T> T[] sortedArrayWith(T[] tArr, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(tArr, "$this$sortedArrayWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        if (tArr.length == 0) {
            return tArr;
        }
        T[] tArr2 = (T[]) Arrays.copyOf(tArr, tArr.length);
        Intrinsics.checkNotNullExpressionValue(tArr2, "java.util.Arrays.copyOf(this, size)");
        ArraysKt___ArraysJvmKt.sortWith(tArr2, comparator);
        return tArr2;
    }

    public static <T> List<T> sortedWith(T[] tArr, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(tArr, "$this$sortedWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return ArraysKt___ArraysJvmKt.asList(sortedArrayWith(tArr, comparator));
    }

    public static final <T> int getLastIndex(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$lastIndex");
        return tArr.length - 1;
    }

    public static final <T, C extends Collection<? super T>> C toCollection(T[] tArr, C c) {
        Intrinsics.checkNotNullParameter(tArr, "$this$toCollection");
        Intrinsics.checkNotNullParameter(c, "destination");
        for (T t : tArr) {
            c.add(t);
        }
        return c;
    }

    public static final <T> List<T> toList(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$toList");
        int length = tArr.length;
        if (length == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        if (length != 1) {
            return toMutableList(tArr);
        }
        return CollectionsKt__CollectionsJVMKt.listOf(tArr[0]);
    }

    public static List<Integer> toList(int[] iArr) {
        Intrinsics.checkNotNullParameter(iArr, "$this$toList");
        int length = iArr.length;
        if (length == 0) {
            return CollectionsKt__CollectionsKt.emptyList();
        }
        if (length != 1) {
            return toMutableList(iArr);
        }
        return CollectionsKt__CollectionsJVMKt.listOf(Integer.valueOf(iArr[0]));
    }

    public static <T> List<T> toMutableList(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$toMutableList");
        return new ArrayList(CollectionsKt__CollectionsKt.asCollection(tArr));
    }

    public static List<Integer> toMutableList(int[] iArr) {
        Intrinsics.checkNotNullParameter(iArr, "$this$toMutableList");
        ArrayList arrayList = new ArrayList(iArr.length);
        for (int i : iArr) {
            arrayList.add(Integer.valueOf(i));
        }
        return arrayList;
    }

    public static <T> Set<T> toSet(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$toSet");
        int length = tArr.length;
        if (length == 0) {
            return SetsKt__SetsKt.emptySet();
        }
        if (length != 1) {
            return (Set) toCollection(tArr, new LinkedHashSet(MapsKt__MapsJVMKt.mapCapacity(tArr.length)));
        }
        return SetsKt__SetsJVMKt.setOf(tArr[0]);
    }

    public static final <A extends Appendable> A joinTo(float[] fArr, A a, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1<? super Float, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(fArr, "$this$joinTo");
        Intrinsics.checkNotNullParameter(a, "buffer");
        Intrinsics.checkNotNullParameter(charSequence, "separator");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        Intrinsics.checkNotNullParameter(charSequence3, "postfix");
        Intrinsics.checkNotNullParameter(charSequence4, "truncated");
        a.append(charSequence2);
        int i2 = 0;
        for (float f : fArr) {
            i2++;
            if (i2 > 1) {
                a.append(charSequence);
            }
            if (i >= 0 && i2 > i) {
                break;
            }
            if (function1 != null) {
                a.append((CharSequence) function1.invoke(Float.valueOf(f)));
            } else {
                a.append(String.valueOf(f));
            }
        }
        if (i >= 0 && i2 > i) {
            a.append(charSequence4);
        }
        a.append(charSequence3);
        return a;
    }

    public static /* synthetic */ String joinToString$default(float[] fArr, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
        if ((i2 & 1) != 0) {
            charSequence = ", ";
        }
        CharSequence charSequence5 = "";
        CharSequence charSequence6 = (i2 & 2) != 0 ? charSequence5 : charSequence2;
        if ((i2 & 4) == 0) {
            charSequence5 = charSequence3;
        }
        if ((i2 & 8) != 0) {
            i = -1;
        }
        if ((i2 & 16) != 0) {
            charSequence4 = "...";
        }
        if ((i2 & 32) != 0) {
            function1 = null;
        }
        return joinToString(fArr, charSequence, charSequence6, charSequence5, i, charSequence4, function1);
    }

    public static final String joinToString(float[] fArr, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1<? super Float, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(fArr, "$this$joinToString");
        Intrinsics.checkNotNullParameter(charSequence, "separator");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        Intrinsics.checkNotNullParameter(charSequence3, "postfix");
        Intrinsics.checkNotNullParameter(charSequence4, "truncated");
        String sb = ((StringBuilder) joinTo(fArr, new StringBuilder(), charSequence, charSequence2, charSequence3, i, charSequence4, function1)).toString();
        Intrinsics.checkNotNullExpressionValue(sb, "joinTo(StringBuilder(), …ed, transform).toString()");
        return sb;
    }

    public static <T> Sequence<T> asSequence(T[] tArr) {
        Intrinsics.checkNotNullParameter(tArr, "$this$asSequence");
        if (tArr.length == 0) {
            return SequencesKt__SequencesKt.emptySequence();
        }
        return new Sequence<T>(tArr) { // from class: kotlin.collections.ArraysKt___ArraysKt$asSequence$$inlined$Sequence$1
            final /* synthetic */ Object[] $this_asSequence$inlined;

            {
                this.$this_asSequence$inlined = r1;
            }

            @Override // kotlin.sequences.Sequence
            public Iterator<T> iterator() {
                return ArrayIteratorKt.iterator(this.$this_asSequence$inlined);
            }
        };
    }
}
