package kotlin.sequences;

import java.util.Iterator;
import java.util.NoSuchElementException;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Sequences.kt */
/* loaded from: classes2.dex */
public final class FilteringSequence<T> implements Sequence<T> {
    private final Function1<T, Boolean> predicate;
    private final boolean sendWhen;
    private final Sequence<T> sequence;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.sequences.Sequence<? extends T> */
    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlin.jvm.functions.Function1<? super T, java.lang.Boolean> */
    /* JADX WARN: Multi-variable type inference failed */
    public FilteringSequence(Sequence<? extends T> sequence, boolean z, Function1<? super T, Boolean> function1) {
        Intrinsics.checkNotNullParameter(sequence, "sequence");
        Intrinsics.checkNotNullParameter(function1, "predicate");
        this.sequence = sequence;
        this.sendWhen = z;
        this.predicate = function1;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<T> iterator() {
        return new Object(this) { // from class: kotlin.sequences.FilteringSequence$iterator$1
            private final Iterator<T> iterator;
            private T nextItem;
            private int nextState = -1;
            final /* synthetic */ FilteringSequence this$0;

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

            /* JADX WARN: Type inference failed for: r0v4, types: [T, java.lang.Object] */
            /* JADX WARNING: Unknown variable types count: 1 */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private final void calcNext() {
                /*
                    r3 = this;
                L_0x0000:
                    java.util.Iterator<T> r0 = r3.iterator
                    boolean r0 = r0.hasNext()
                    if (r0 == 0) goto L_0x002c
                    java.util.Iterator<T> r0 = r3.iterator
                    java.lang.Object r0 = r0.next()
                    kotlin.sequences.FilteringSequence r1 = r3.this$0
                    kotlin.jvm.functions.Function1 r1 = kotlin.sequences.FilteringSequence.access$getPredicate$p(r1)
                    java.lang.Object r1 = r1.invoke(r0)
                    java.lang.Boolean r1 = (java.lang.Boolean) r1
                    boolean r1 = r1.booleanValue()
                    kotlin.sequences.FilteringSequence r2 = r3.this$0
                    boolean r2 = kotlin.sequences.FilteringSequence.access$getSendWhen$p(r2)
                    if (r1 != r2) goto L_0x0000
                    r3.nextItem = r0
                    r0 = 1
                    r3.nextState = r0
                    return
                L_0x002c:
                    r0 = 0
                    r3.nextState = r0
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlin.sequences.FilteringSequence$iterator$1.calcNext():void");
            }

            @Override // java.util.Iterator
            public T next() {
                if (this.nextState == -1) {
                    calcNext();
                }
                if (this.nextState != 0) {
                    T t = this.nextItem;
                    this.nextItem = null;
                    this.nextState = -1;
                    return t;
                }
                throw new NoSuchElementException();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                if (this.nextState == -1) {
                    calcNext();
                }
                return this.nextState == 1;
            }
        };
    }
}
