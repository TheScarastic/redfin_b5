package kotlin.text;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import kotlin.Pair;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt___RangesKt;
import kotlin.sequences.Sequence;
/* compiled from: Strings.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class DelimitedRangesSequence implements Sequence<IntRange> {
    private final Function2<CharSequence, Integer, Pair<Integer, Integer>> getNextMatch;
    private final CharSequence input;
    private final int limit;
    private final int startIndex;

    /* JADX DEBUG: Multi-variable search result rejected for r5v0, resolved type: kotlin.jvm.functions.Function2<? super java.lang.CharSequence, ? super java.lang.Integer, kotlin.Pair<java.lang.Integer, java.lang.Integer>> */
    /* JADX WARN: Multi-variable type inference failed */
    public DelimitedRangesSequence(CharSequence charSequence, int i, int i2, Function2<? super CharSequence, ? super Integer, Pair<Integer, Integer>> function2) {
        Intrinsics.checkNotNullParameter(charSequence, "input");
        Intrinsics.checkNotNullParameter(function2, "getNextMatch");
        this.input = charSequence;
        this.startIndex = i;
        this.limit = i2;
        this.getNextMatch = function2;
    }

    @Override // kotlin.sequences.Sequence
    public Iterator<IntRange> iterator() {
        return new Object(this) { // from class: kotlin.text.DelimitedRangesSequence$iterator$1
            private int counter;
            private int currentStartIndex;
            private IntRange nextItem;
            private int nextSearchIndex;
            private int nextState = -1;
            final /* synthetic */ DelimitedRangesSequence this$0;

            @Override // java.util.Iterator
            public void remove() {
                throw new UnsupportedOperationException("Operation is not supported for read-only collection");
            }

            /* JADX WARN: Incorrect args count in method signature: ()V */
            /* access modifiers changed from: package-private */
            {
                this.this$0 = r3;
                int i = RangesKt___RangesKt.coerceIn(DelimitedRangesSequence.access$getStartIndex$p(r3), 0, DelimitedRangesSequence.access$getInput$p(r3).length());
                this.currentStartIndex = i;
                this.nextSearchIndex = i;
            }

            /* JADX WARNING: Code restructure failed: missing block: B:8:0x0021, code lost:
                if (r0 < kotlin.text.DelimitedRangesSequence.access$getLimit$p(r6.this$0)) goto L_0x0023;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            private final void calcNext() {
                /*
                    r6 = this;
                    int r0 = r6.nextSearchIndex
                    r1 = 0
                    if (r0 >= 0) goto L_0x000c
                    r6.nextState = r1
                    r0 = 0
                    r6.nextItem = r0
                    goto L_0x009e
                L_0x000c:
                    kotlin.text.DelimitedRangesSequence r0 = r6.this$0
                    int r0 = kotlin.text.DelimitedRangesSequence.access$getLimit$p(r0)
                    r2 = -1
                    r3 = 1
                    if (r0 <= 0) goto L_0x0023
                    int r0 = r6.counter
                    int r0 = r0 + r3
                    r6.counter = r0
                    kotlin.text.DelimitedRangesSequence r4 = r6.this$0
                    int r4 = kotlin.text.DelimitedRangesSequence.access$getLimit$p(r4)
                    if (r0 >= r4) goto L_0x0031
                L_0x0023:
                    int r0 = r6.nextSearchIndex
                    kotlin.text.DelimitedRangesSequence r4 = r6.this$0
                    java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                    int r4 = r4.length()
                    if (r0 <= r4) goto L_0x0047
                L_0x0031:
                    int r0 = r6.currentStartIndex
                    kotlin.ranges.IntRange r1 = new kotlin.ranges.IntRange
                    kotlin.text.DelimitedRangesSequence r4 = r6.this$0
                    java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                    int r4 = kotlin.text.StringsKt__StringsKt.getLastIndex(r4)
                    r1.<init>(r0, r4)
                    r6.nextItem = r1
                    r6.nextSearchIndex = r2
                    goto L_0x009c
                L_0x0047:
                    kotlin.text.DelimitedRangesSequence r0 = r6.this$0
                    kotlin.jvm.functions.Function2 r0 = kotlin.text.DelimitedRangesSequence.access$getGetNextMatch$p(r0)
                    kotlin.text.DelimitedRangesSequence r4 = r6.this$0
                    java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                    int r5 = r6.nextSearchIndex
                    java.lang.Integer r5 = java.lang.Integer.valueOf(r5)
                    java.lang.Object r0 = r0.invoke(r4, r5)
                    kotlin.Pair r0 = (kotlin.Pair) r0
                    if (r0 != 0) goto L_0x0077
                    int r0 = r6.currentStartIndex
                    kotlin.ranges.IntRange r1 = new kotlin.ranges.IntRange
                    kotlin.text.DelimitedRangesSequence r4 = r6.this$0
                    java.lang.CharSequence r4 = kotlin.text.DelimitedRangesSequence.access$getInput$p(r4)
                    int r4 = kotlin.text.StringsKt__StringsKt.getLastIndex(r4)
                    r1.<init>(r0, r4)
                    r6.nextItem = r1
                    r6.nextSearchIndex = r2
                    goto L_0x009c
                L_0x0077:
                    java.lang.Object r2 = r0.component1()
                    java.lang.Number r2 = (java.lang.Number) r2
                    int r2 = r2.intValue()
                    java.lang.Object r0 = r0.component2()
                    java.lang.Number r0 = (java.lang.Number) r0
                    int r0 = r0.intValue()
                    int r4 = r6.currentStartIndex
                    kotlin.ranges.IntRange r4 = kotlin.ranges.RangesKt.until(r4, r2)
                    r6.nextItem = r4
                    int r2 = r2 + r0
                    r6.currentStartIndex = r2
                    if (r0 != 0) goto L_0x0099
                    r1 = r3
                L_0x0099:
                    int r2 = r2 + r1
                    r6.nextSearchIndex = r2
                L_0x009c:
                    r6.nextState = r3
                L_0x009e:
                    return
                */
                throw new UnsupportedOperationException("Method not decompiled: kotlin.text.DelimitedRangesSequence$iterator$1.calcNext():void");
            }

            @Override // java.util.Iterator
            public IntRange next() {
                if (this.nextState == -1) {
                    calcNext();
                }
                if (this.nextState != 0) {
                    IntRange intRange = this.nextItem;
                    Objects.requireNonNull(intRange, "null cannot be cast to non-null type kotlin.ranges.IntRange");
                    this.nextItem = null;
                    this.nextState = -1;
                    return intRange;
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
