package kotlin.sequences;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import kotlin.Pair;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.collections.CollectionsKt__MutableCollectionsJVMKt;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__AppendableKt;
/* compiled from: _Sequences.kt */
/* loaded from: classes2.dex */
public class SequencesKt___SequencesKt extends SequencesKt___SequencesJvmKt {
    public static <T> boolean contains(Sequence<? extends T> sequence, T t) {
        Intrinsics.checkNotNullParameter(sequence, "$this$contains");
        return indexOf(sequence, t) >= 0;
    }

    public static <T> T firstOrNull(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$firstOrNull");
        Iterator<? extends T> it = sequence.iterator();
        if (!it.hasNext()) {
            return null;
        }
        return (T) it.next();
    }

    public static final <T> int indexOf(Sequence<? extends T> sequence, T t) {
        Intrinsics.checkNotNullParameter(sequence, "$this$indexOf");
        int i = 0;
        for (Object obj : sequence) {
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            if (Intrinsics.areEqual(t, obj)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public static <T> Sequence<T> filter(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$filter");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        return new FilteringSequence(sequence, true, function1);
    }

    public static <T> Sequence<T> filterNot(Sequence<? extends T> sequence, Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$filterNot");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        return new FilteringSequence(sequence, false, function1);
    }

    public static <T> Sequence<T> filterNotNull(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$filterNotNull");
        Sequence<T> sequence2 = filterNot(sequence, SequencesKt___SequencesKt$filterNotNull$1.INSTANCE);
        Objects.requireNonNull(sequence2, "null cannot be cast to non-null type kotlin.sequences.Sequence<T>");
        return sequence2;
    }

    public static <T> Sequence<T> take(Sequence<? extends T> sequence, int i) {
        Intrinsics.checkNotNullParameter(sequence, "$this$take");
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("Requested element count " + i + " is less than zero.").toString());
        } else if (i == 0) {
            return SequencesKt__SequencesKt.emptySequence();
        } else {
            if (sequence instanceof DropTakeSequence) {
                return ((DropTakeSequence) sequence).take(i);
            }
            return new TakeSequence(sequence, i);
        }
    }

    public static <T> Sequence<T> sortedWith(Sequence<? extends T> sequence, Comparator<? super T> comparator) {
        Intrinsics.checkNotNullParameter(sequence, "$this$sortedWith");
        Intrinsics.checkNotNullParameter(comparator, "comparator");
        return new Sequence<T>(sequence, comparator) { // from class: kotlin.sequences.SequencesKt___SequencesKt$sortedWith$1
            final /* synthetic */ Comparator $comparator;
            final /* synthetic */ Sequence $this_sortedWith;

            /* access modifiers changed from: package-private */
            {
                this.$this_sortedWith = r1;
                this.$comparator = r2;
            }

            @Override // kotlin.sequences.Sequence
            public Iterator<T> iterator() {
                List mutableList = SequencesKt___SequencesKt.toMutableList(this.$this_sortedWith);
                CollectionsKt__MutableCollectionsJVMKt.sortWith(mutableList, this.$comparator);
                return mutableList.iterator();
            }
        };
    }

    public static final <T, C extends Collection<? super T>> C toCollection(Sequence<? extends T> sequence, C c) {
        Intrinsics.checkNotNullParameter(sequence, "$this$toCollection");
        Intrinsics.checkNotNullParameter(c, "destination");
        Iterator<? extends T> it = sequence.iterator();
        while (it.hasNext()) {
            c.add(it.next());
        }
        return c;
    }

    public static <T> List<T> toList(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$toList");
        return CollectionsKt__CollectionsKt.optimizeReadOnlyList(toMutableList(sequence));
    }

    public static final <T> List<T> toMutableList(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$toMutableList");
        return (List) toCollection(sequence, new ArrayList());
    }

    public static <T, R> Sequence<R> flatMap(Sequence<? extends T> sequence, Function1<? super T, ? extends Sequence<? extends R>> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$flatMap");
        Intrinsics.checkNotNullParameter(function1, "transform");
        return new FlatteningSequence(sequence, function1, SequencesKt___SequencesKt$flatMap$2.INSTANCE);
    }

    public static <T, R> Sequence<R> map(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$map");
        Intrinsics.checkNotNullParameter(function1, "transform");
        return new TransformingSequence(sequence, function1);
    }

    public static <T, R> Sequence<R> mapIndexed(Sequence<? extends T> sequence, Function2<? super Integer, ? super T, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(sequence, "$this$mapIndexed");
        Intrinsics.checkNotNullParameter(function2, "transform");
        return new TransformingIndexedSequence(sequence, function2);
    }

    public static <T, R> Sequence<R> mapNotNull(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$mapNotNull");
        Intrinsics.checkNotNullParameter(function1, "transform");
        return filterNotNull(new TransformingSequence(sequence, function1));
    }

    public static <T> Sequence<T> distinct(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$distinct");
        return distinctBy(sequence, SequencesKt___SequencesKt$distinct$1.INSTANCE);
    }

    public static final <T, K> Sequence<T> distinctBy(Sequence<? extends T> sequence, Function1<? super T, ? extends K> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$distinctBy");
        Intrinsics.checkNotNullParameter(function1, "selector");
        return new DistinctSequence(sequence, function1);
    }

    public static <T> Sequence<T> plus(Sequence<? extends T> sequence, Sequence<? extends T> sequence2) {
        Intrinsics.checkNotNullParameter(sequence, "$this$plus");
        Intrinsics.checkNotNullParameter(sequence2, "elements");
        return SequencesKt__SequencesKt.flatten(SequencesKt__SequencesKt.sequenceOf(sequence, sequence2));
    }

    public static <T> Sequence<Pair<T, T>> zipWithNext(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$zipWithNext");
        return zipWithNext(sequence, SequencesKt___SequencesKt$zipWithNext$1.INSTANCE);
    }

    public static final <T, R> Sequence<R> zipWithNext(Sequence<? extends T> sequence, Function2<? super T, ? super T, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(sequence, "$this$zipWithNext");
        Intrinsics.checkNotNullParameter(function2, "transform");
        return SequencesKt__SequenceBuilderKt.sequence(new SequencesKt___SequencesKt$zipWithNext$2(sequence, function2, null));
    }

    public static final <T, A extends Appendable> A joinTo(Sequence<? extends T> sequence, A a, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$joinTo");
        Intrinsics.checkNotNullParameter(a, "buffer");
        Intrinsics.checkNotNullParameter(charSequence, "separator");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        Intrinsics.checkNotNullParameter(charSequence3, "postfix");
        Intrinsics.checkNotNullParameter(charSequence4, "truncated");
        a.append(charSequence2);
        int i2 = 0;
        for (Object obj : sequence) {
            i2++;
            if (i2 > 1) {
                a.append(charSequence);
            }
            if (i >= 0 && i2 > i) {
                break;
            }
            StringsKt__AppendableKt.appendElement(a, obj, function1);
        }
        if (i >= 0 && i2 > i) {
            a.append(charSequence4);
        }
        a.append(charSequence3);
        return a;
    }

    public static /* synthetic */ String joinToString$default(Sequence sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1 function1, int i2, Object obj) {
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
        return joinToString(sequence, charSequence, charSequence6, charSequence5, i, charSequence4, function1);
    }

    public static final <T> String joinToString(Sequence<? extends T> sequence, CharSequence charSequence, CharSequence charSequence2, CharSequence charSequence3, int i, CharSequence charSequence4, Function1<? super T, ? extends CharSequence> function1) {
        Intrinsics.checkNotNullParameter(sequence, "$this$joinToString");
        Intrinsics.checkNotNullParameter(charSequence, "separator");
        Intrinsics.checkNotNullParameter(charSequence2, "prefix");
        Intrinsics.checkNotNullParameter(charSequence3, "postfix");
        Intrinsics.checkNotNullParameter(charSequence4, "truncated");
        String sb = ((StringBuilder) joinTo(sequence, new StringBuilder(), charSequence, charSequence2, charSequence3, i, charSequence4, function1)).toString();
        Intrinsics.checkNotNullExpressionValue(sb, "joinTo(StringBuilder(), …ed, transform).toString()");
        return sb;
    }

    public static <T> Iterable<T> asIterable(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "$this$asIterable");
        return new Object(sequence) { // from class: kotlin.sequences.SequencesKt___SequencesKt$asIterable$$inlined$Iterable$1
            final /* synthetic */ Sequence $this_asIterable$inlined;

            {
                this.$this_asIterable$inlined = r1;
            }

            @Override // java.lang.Iterable
            public Iterator<T> iterator() {
                return this.$this_asIterable$inlined.iterator();
            }
        };
    }
}
