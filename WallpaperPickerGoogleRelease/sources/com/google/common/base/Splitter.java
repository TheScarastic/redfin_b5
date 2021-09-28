package com.google.common.base;

import com.google.common.base.CharMatcher;
import java.io.IOException;
import java.util.Iterator;
import java.util.Objects;
/* loaded from: classes.dex */
public final class Splitter {
    public final int limit;
    public final boolean omitEmptyStrings;
    public final Strategy strategy;
    public final CharMatcher trimmer;

    /* loaded from: classes.dex */
    public static abstract class SplittingIterator extends AbstractIterator<String> {
        public int limit;
        public int offset = 0;
        public final boolean omitEmptyStrings;
        public final CharSequence toSplit;
        public final CharMatcher trimmer;

        public SplittingIterator(Splitter splitter, CharSequence charSequence) {
            this.trimmer = splitter.trimmer;
            this.omitEmptyStrings = splitter.omitEmptyStrings;
            this.limit = splitter.limit;
            this.toSplit = charSequence;
        }

        public abstract int separatorEnd(int i);

        public abstract int separatorStart(int i);
    }

    /* loaded from: classes.dex */
    public interface Strategy {
        Iterator<String> iterator(Splitter splitter, CharSequence charSequence);
    }

    public Splitter(Strategy strategy, boolean z, CharMatcher charMatcher, int i) {
        this.strategy = strategy;
        this.omitEmptyStrings = z;
        this.trimmer = charMatcher;
        this.limit = i;
    }

    public static Splitter on(final String str) {
        Preconditions.checkArgument(str.length() != 0, "The separator may not be the empty string.");
        if (str.length() == 1) {
            return on(str.charAt(0));
        }
        return new Splitter(new Strategy() { // from class: com.google.common.base.Splitter.2
            @Override // com.google.common.base.Splitter.Strategy
            public Iterator iterator(Splitter splitter, CharSequence charSequence) {
                return new SplittingIterator(splitter, charSequence) { // from class: com.google.common.base.Splitter.2.1
                    @Override // com.google.common.base.Splitter.SplittingIterator
                    public int separatorEnd(int i) {
                        return str.length() + i;
                    }

                    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0026, code lost:
                        r6 = r6 + 1;
                     */
                    @Override // com.google.common.base.Splitter.SplittingIterator
                    /* Code decompiled incorrectly, please refer to instructions dump. */
                    public int separatorStart(int r6) {
                        /*
                            r5 = this;
                            com.google.common.base.Splitter$2 r0 = com.google.common.base.Splitter.AnonymousClass2.this
                            java.lang.String r0 = r4
                            int r0 = r0.length()
                            java.lang.CharSequence r1 = r5.toSplit
                            int r1 = r1.length()
                            int r1 = r1 - r0
                        L_0x000f:
                            if (r6 > r1) goto L_0x002d
                            r2 = 0
                        L_0x0012:
                            if (r2 >= r0) goto L_0x002c
                            java.lang.CharSequence r3 = r5.toSplit
                            int r4 = r2 + r6
                            char r3 = r3.charAt(r4)
                            com.google.common.base.Splitter$2 r4 = com.google.common.base.Splitter.AnonymousClass2.this
                            java.lang.String r4 = r4
                            char r4 = r4.charAt(r2)
                            if (r3 == r4) goto L_0x0029
                            int r6 = r6 + 1
                            goto L_0x000f
                        L_0x0029:
                            int r2 = r2 + 1
                            goto L_0x0012
                        L_0x002c:
                            return r6
                        L_0x002d:
                            r5 = -1
                            return r5
                        */
                        throw new UnsupportedOperationException("Method not decompiled: com.google.common.base.Splitter.AnonymousClass2.AnonymousClass1.separatorStart(int):int");
                    }
                };
            }
        });
    }

    public Iterable<String> split(final CharSequence charSequence) {
        Objects.requireNonNull(charSequence);
        return new Iterable<String>() { // from class: com.google.common.base.Splitter.5
            @Override // java.lang.Iterable
            public Iterator<String> iterator() {
                Splitter splitter = Splitter.this;
                return splitter.strategy.iterator(splitter, charSequence);
            }

            @Override // java.lang.Object
            public String toString() {
                Objects.requireNonNull(", ");
                StringBuilder sb = new StringBuilder();
                sb.append('[');
                Iterator<String> it = iterator();
                try {
                    Objects.requireNonNull(sb);
                    if (it.hasNext()) {
                        String next = it.next();
                        Objects.requireNonNull(next);
                        sb.append((CharSequence) (next instanceof CharSequence ? next : next.toString()));
                        while (it.hasNext()) {
                            sb.append((CharSequence) ", ");
                            String next2 = it.next();
                            Objects.requireNonNull(next2);
                            sb.append((CharSequence) (next2 instanceof CharSequence ? next2 : next2.toString()));
                        }
                    }
                    sb.append(']');
                    return sb.toString();
                } catch (IOException e) {
                    throw new AssertionError(e);
                }
            }
        };
    }

    public static Splitter on(char c) {
        final CharMatcher.Is is = new CharMatcher.Is(c);
        return new Splitter(new Strategy() { // from class: com.google.common.base.Splitter.1
            @Override // com.google.common.base.Splitter.Strategy
            public Iterator iterator(Splitter splitter, CharSequence charSequence) {
                return new SplittingIterator(splitter, charSequence) { // from class: com.google.common.base.Splitter.1.1
                    @Override // com.google.common.base.Splitter.SplittingIterator
                    public int separatorEnd(int i) {
                        return i + 1;
                    }

                    @Override // com.google.common.base.Splitter.SplittingIterator
                    public int separatorStart(int i) {
                        return CharMatcher.this.indexIn(this.toSplit, i);
                    }
                };
            }
        });
    }

    public Splitter(Strategy strategy) {
        CharMatcher.None none = CharMatcher.None.INSTANCE;
        this.strategy = strategy;
        this.omitEmptyStrings = false;
        this.trimmer = none;
        this.limit = Integer.MAX_VALUE;
    }
}
