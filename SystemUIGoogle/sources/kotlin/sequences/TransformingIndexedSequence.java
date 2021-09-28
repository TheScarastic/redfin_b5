package kotlin.sequences;

import java.util.Iterator;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class TransformingIndexedSequence<T, R> implements Sequence<R> {
    private final Sequence<T> sequence;
    private final Function2<Integer, T, R> transformer;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.sequences.Sequence<? extends T> */
    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlin.jvm.functions.Function2<? super java.lang.Integer, ? super T, ? extends R> */
    /* JADX WARN: Multi-variable type inference failed */
    public TransformingIndexedSequence(Sequence<? extends T> sequence, Function2<? super Integer, ? super T, ? extends R> function2) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(function2, "transformer");
        this.sequence = sequence;
        this.transformer = function2;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<R> iterator() {
        return new Object(this) { // from class: kotlin.sequences.TransformingIndexedSequence$iterator$1
            private int index;
            private final Iterator<T> iterator;
            final /* synthetic */ TransformingIndexedSequence this$0;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX WARN: Incorrect args count in method signature: ()V */
            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.iterator = r1.sequence.iterator();
            }

            /* JADX WARN: Type inference failed for: r3v3, types: [R, java.lang.Object] */
            @Override // java.util.Iterator
            public R next() {
                Function2 function2 = this.this$0.transformer;
                int i = this.index;
                this.index = i + 1;
                if (i < 0) {
                    CollectionsKt__CollectionsKt.throwIndexOverflow();
                }
                return function2.invoke(Integer.valueOf(i), this.iterator.next());
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.iterator.hasNext();
            }
        };
    }
}
