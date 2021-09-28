package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class TakeSequence<T> implements Sequence<T>, DropTakeSequence<T> {
    private final int count;
    private final Sequence<T> sequence;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.sequences.Sequence<? extends T> */
    /* JADX WARN: Multi-variable type inference failed */
    public TakeSequence(Sequence<? extends T> sequence, int i) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        this.sequence = sequence;
        this.count = i;
        if (!(i >= 0)) {
            throw new IllegalArgumentException(("count must be non-negative, but was " + i + '.').toString());
        }
    }

    @Override // kotlin.sequences.DropTakeSequence
    public Sequence<T> take(int i) {
        return i >= this.count ? this : new TakeSequence(this.sequence, i);
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new Object(this) { // from class: kotlin.sequences.TakeSequence$iterator$1
            private final Iterator<T> iterator;
            private int left;
            final /* synthetic */ TakeSequence this$0;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX WARN: Incorrect args count in method signature: ()V */
            /* access modifiers changed from: package-private */
            {
                this.this$0 = r2;
                this.left = r2.count;
                this.iterator = r2.sequence.iterator();
            }

            /* JADX WARN: Type inference failed for: r1v3, types: [T, java.lang.Object] */
            @Override // java.util.Iterator
            public T next() {
                int i = this.left;
                if (i != 0) {
                    this.left = i - 1;
                    return this.iterator.next();
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return this.left > 0 && this.iterator.hasNext();
            }
        };
    }
}
