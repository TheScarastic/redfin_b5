package kotlin.sequences;

import java.util.Iterator;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
/* compiled from: Sequences.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class SequencesKt__SequencesKt$flatten$1 extends Lambda implements Function1<Sequence<? extends T>, Iterator<? extends T>> {
    public static final SequencesKt__SequencesKt$flatten$1 INSTANCE = new SequencesKt__SequencesKt$flatten$1();

    SequencesKt__SequencesKt$flatten$1() {
        super(1);
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v2, resolved type: java.util.Iterator<T> */
    /* JADX WARN: Multi-variable type inference failed */
    public final Iterator<T> invoke(Sequence<? extends T> sequence) {
        Intrinsics.checkNotNullParameter(sequence, "it");
        return sequence.iterator();
    }
}
