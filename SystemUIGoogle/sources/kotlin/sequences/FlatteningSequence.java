package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class FlatteningSequence<T, R, E> implements Sequence<E> {
    private final Function1<R, Iterator<E>> iterator;
    private final Sequence<T> sequence;
    private final Function1<T, R> transformer;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.sequences.Sequence<? extends T> */
    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlin.jvm.functions.Function1<? super T, ? extends R> */
    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlin.jvm.functions.Function1<? super R, ? extends java.util.Iterator<? extends E>> */
    /* JADX WARN: Multi-variable type inference failed */
    public FlatteningSequence(Sequence<? extends T> sequence, Function1<? super T, ? extends R> function1, Function1<? super R, ? extends Iterator<? extends E>> function12) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(function1, "transformer");
        Intrinsics.checkNotNullParameter(function12, "iterator");
        this.sequence = sequence;
        this.transformer = function1;
        this.iterator = function12;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<E> iterator() {
        return new Object(this) { // from class: kotlin.sequences.FlatteningSequence$iterator$1
            private Iterator<? extends E> itemIterator;
            private final Iterator<T> iterator;
            final /* synthetic */ FlatteningSequence this$0;

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

            /* JADX WARN: Type inference failed for: r1v3, types: [E, java.lang.Object] */
            @Override // java.util.Iterator
            public E next() {
                if (ensureItemIterator()) {
                    Iterator<? extends E> it = this.itemIterator;
                    Intrinsics.checkNotNull(it);
                    return it.next();
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                return ensureItemIterator();
            }

            private final boolean ensureItemIterator() {
                Iterator<? extends E> it = this.itemIterator;
                if (it != 0 && !it.hasNext()) {
                    this.itemIterator = null;
                }
                while (true) {
                    if (this.itemIterator == null) {
                        if (this.iterator.hasNext()) {
                            Iterator<? extends E> it2 = (Iterator) this.this$0.iterator.invoke(this.this$0.transformer.invoke(this.iterator.next()));
                            if (it2.hasNext()) {
                                this.itemIterator = it2;
                                break;
                            }
                        } else {
                            return false;
                        }
                    } else {
                        break;
                    }
                }
                return true;
            }
        };
    }
}
