package androidx.leanback.widget;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class WindowAlignment {
    public final Axis horizontal;
    private Axis mMainAxis;
    private int mOrientation = 0;
    private Axis mSecondAxis;
    public final Axis vertical;

    /* access modifiers changed from: package-private */
    public WindowAlignment() {
        Axis axis = new Axis("vertical");
        this.vertical = axis;
        Axis axis2 = new Axis("horizontal");
        this.horizontal = axis2;
        this.mMainAxis = axis2;
        this.mSecondAxis = axis;
    }

    /* loaded from: classes.dex */
    public static class Axis {
        private int mMaxEdge;
        private int mMaxScroll;
        private int mMinEdge;
        private int mMinScroll;
        private int mPaddingMax;
        private int mPaddingMin;
        private boolean mReversedFlow;
        private int mSize;
        private int mPreferredKeyLine = 2;
        private int mWindowAlignment = 3;
        private int mWindowAlignmentOffset = 0;
        private float mWindowAlignmentOffsetPercent = 50.0f;

        final int calculateScrollToKeyLine(int i, int i2) {
            return i - i2;
        }

        public Axis(String str) {
            reset();
        }

        public final void setWindowAlignment(int i) {
            this.mWindowAlignment = i;
        }

        final boolean isPreferKeylineOverHighEdge() {
            return (this.mPreferredKeyLine & 2) != 0;
        }

        final boolean isPreferKeylineOverLowEdge() {
            return (this.mPreferredKeyLine & 1) != 0;
        }

        public final int getMinScroll() {
            return this.mMinScroll;
        }

        public final void invalidateScrollMin() {
            this.mMinEdge = Integer.MIN_VALUE;
            this.mMinScroll = Integer.MIN_VALUE;
        }

        public final int getMaxScroll() {
            return this.mMaxScroll;
        }

        public final void invalidateScrollMax() {
            this.mMaxEdge = Integer.MAX_VALUE;
            this.mMaxScroll = Integer.MAX_VALUE;
        }

        void reset() {
            this.mMinEdge = Integer.MIN_VALUE;
            this.mMaxEdge = Integer.MAX_VALUE;
        }

        public final boolean isMinUnknown() {
            return this.mMinEdge == Integer.MIN_VALUE;
        }

        public final boolean isMaxUnknown() {
            return this.mMaxEdge == Integer.MAX_VALUE;
        }

        public final void setSize(int i) {
            this.mSize = i;
        }

        public final int getSize() {
            return this.mSize;
        }

        public final void setPadding(int i, int i2) {
            this.mPaddingMin = i;
            this.mPaddingMax = i2;
        }

        public final int getPaddingMin() {
            return this.mPaddingMin;
        }

        public final int getPaddingMax() {
            return this.mPaddingMax;
        }

        public final int getClientSize() {
            return (this.mSize - this.mPaddingMin) - this.mPaddingMax;
        }

        final int calculateKeyline() {
            if (!this.mReversedFlow) {
                int i = this.mWindowAlignmentOffset;
                if (i < 0) {
                    i += this.mSize;
                }
                float f = this.mWindowAlignmentOffsetPercent;
                if (f != -1.0f) {
                    return i + ((int) ((((float) this.mSize) * f) / 100.0f));
                }
                return i;
            }
            int i2 = this.mWindowAlignmentOffset;
            int i3 = i2 >= 0 ? this.mSize - i2 : -i2;
            float f2 = this.mWindowAlignmentOffsetPercent;
            return f2 != -1.0f ? i3 - ((int) ((((float) this.mSize) * f2) / 100.0f)) : i3;
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x0027, code lost:
            r4.mMinScroll = r4.mMinEdge - r4.mPaddingMin;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:19:0x0048, code lost:
            r4.mMaxScroll = (r4.mMaxEdge - r4.mPaddingMin) - r5;
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void updateMinMax(int r5, int r6, int r7, int r8) {
            /*
                r4 = this;
                r4.mMinEdge = r5
                r4.mMaxEdge = r6
                int r5 = r4.getClientSize()
                int r6 = r4.calculateKeyline()
                boolean r0 = r4.isMinUnknown()
                boolean r1 = r4.isMaxUnknown()
                if (r0 != 0) goto L_0x0035
                boolean r2 = r4.mReversedFlow
                if (r2 != 0) goto L_0x0021
                int r2 = r4.mWindowAlignment
                r2 = r2 & 1
                if (r2 == 0) goto L_0x002f
                goto L_0x0027
            L_0x0021:
                int r2 = r4.mWindowAlignment
                r2 = r2 & 2
                if (r2 == 0) goto L_0x002f
            L_0x0027:
                int r2 = r4.mMinEdge
                int r3 = r4.mPaddingMin
                int r2 = r2 - r3
                r4.mMinScroll = r2
                goto L_0x0035
            L_0x002f:
                int r2 = r4.calculateScrollToKeyLine(r7, r6)
                r4.mMinScroll = r2
            L_0x0035:
                if (r1 != 0) goto L_0x0057
                boolean r2 = r4.mReversedFlow
                if (r2 != 0) goto L_0x0042
                int r2 = r4.mWindowAlignment
                r2 = r2 & 2
                if (r2 == 0) goto L_0x0051
                goto L_0x0048
            L_0x0042:
                int r2 = r4.mWindowAlignment
                r2 = r2 & 1
                if (r2 == 0) goto L_0x0051
            L_0x0048:
                int r2 = r4.mMaxEdge
                int r3 = r4.mPaddingMin
                int r2 = r2 - r3
                int r2 = r2 - r5
                r4.mMaxScroll = r2
                goto L_0x0057
            L_0x0051:
                int r5 = r4.calculateScrollToKeyLine(r8, r6)
                r4.mMaxScroll = r5
            L_0x0057:
                if (r1 != 0) goto L_0x00e6
                if (r0 != 0) goto L_0x00e6
                boolean r5 = r4.mReversedFlow
                if (r5 != 0) goto L_0x00a3
                int r5 = r4.mWindowAlignment
                r0 = r5 & 1
                if (r0 == 0) goto L_0x0082
                boolean r5 = r4.isPreferKeylineOverLowEdge()
                if (r5 == 0) goto L_0x0077
                int r5 = r4.mMinScroll
                int r6 = r4.calculateScrollToKeyLine(r8, r6)
                int r5 = java.lang.Math.min(r5, r6)
                r4.mMinScroll = r5
            L_0x0077:
                int r5 = r4.mMinScroll
                int r6 = r4.mMaxScroll
                int r5 = java.lang.Math.max(r5, r6)
                r4.mMaxScroll = r5
                goto L_0x00e6
            L_0x0082:
                r5 = r5 & 2
                if (r5 == 0) goto L_0x00e6
                boolean r5 = r4.isPreferKeylineOverHighEdge()
                if (r5 == 0) goto L_0x0098
                int r5 = r4.mMaxScroll
                int r6 = r4.calculateScrollToKeyLine(r7, r6)
                int r5 = java.lang.Math.max(r5, r6)
                r4.mMaxScroll = r5
            L_0x0098:
                int r5 = r4.mMinScroll
                int r6 = r4.mMaxScroll
                int r5 = java.lang.Math.min(r5, r6)
                r4.mMinScroll = r5
                goto L_0x00e6
            L_0x00a3:
                int r5 = r4.mWindowAlignment
                r0 = r5 & 1
                if (r0 == 0) goto L_0x00c6
                boolean r5 = r4.isPreferKeylineOverLowEdge()
                if (r5 == 0) goto L_0x00bb
                int r5 = r4.mMaxScroll
                int r6 = r4.calculateScrollToKeyLine(r7, r6)
                int r5 = java.lang.Math.max(r5, r6)
                r4.mMaxScroll = r5
            L_0x00bb:
                int r5 = r4.mMinScroll
                int r6 = r4.mMaxScroll
                int r5 = java.lang.Math.min(r5, r6)
                r4.mMinScroll = r5
                goto L_0x00e6
            L_0x00c6:
                r5 = r5 & 2
                if (r5 == 0) goto L_0x00e6
                boolean r5 = r4.isPreferKeylineOverHighEdge()
                if (r5 == 0) goto L_0x00dc
                int r5 = r4.mMinScroll
                int r6 = r4.calculateScrollToKeyLine(r8, r6)
                int r5 = java.lang.Math.min(r5, r6)
                r4.mMinScroll = r5
            L_0x00dc:
                int r5 = r4.mMinScroll
                int r6 = r4.mMaxScroll
                int r5 = java.lang.Math.max(r5, r6)
                r4.mMaxScroll = r5
            L_0x00e6:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.leanback.widget.WindowAlignment.Axis.updateMinMax(int, int, int, int):void");
        }

        public final int getScroll(int i) {
            int i2;
            int i3;
            int size = getSize();
            int calculateKeyline = calculateKeyline();
            boolean isMinUnknown = isMinUnknown();
            boolean isMaxUnknown = isMaxUnknown();
            if (!isMinUnknown) {
                int i4 = this.mPaddingMin;
                int i5 = calculateKeyline - i4;
                if (this.mReversedFlow ? (this.mWindowAlignment & 2) != 0 : (this.mWindowAlignment & 1) != 0) {
                    int i6 = this.mMinEdge;
                    if (i - i6 <= i5) {
                        int i7 = i6 - i4;
                        return (isMaxUnknown || i7 <= (i3 = this.mMaxScroll)) ? i7 : i3;
                    }
                }
            }
            if (!isMaxUnknown) {
                int i8 = this.mPaddingMax;
                int i9 = (size - calculateKeyline) - i8;
                if (this.mReversedFlow ? (this.mWindowAlignment & 1) != 0 : (this.mWindowAlignment & 2) != 0) {
                    int i10 = this.mMaxEdge;
                    if (i10 - i <= i9) {
                        int i11 = i10 - (size - i8);
                        return (isMinUnknown || i11 >= (i2 = this.mMinScroll)) ? i11 : i2;
                    }
                }
            }
            return calculateScrollToKeyLine(i, calculateKeyline);
        }

        public final void setReversedFlow(boolean z) {
            this.mReversedFlow = z;
        }

        public String toString() {
            return " min:" + this.mMinEdge + " " + this.mMinScroll + " max:" + this.mMaxEdge + " " + this.mMaxScroll;
        }
    }

    public final Axis mainAxis() {
        return this.mMainAxis;
    }

    public final Axis secondAxis() {
        return this.mSecondAxis;
    }

    public final void setOrientation(int i) {
        this.mOrientation = i;
        if (i == 0) {
            this.mMainAxis = this.horizontal;
            this.mSecondAxis = this.vertical;
            return;
        }
        this.mMainAxis = this.vertical;
        this.mSecondAxis = this.horizontal;
    }

    public final void reset() {
        mainAxis().reset();
    }

    public String toString() {
        return "horizontal=" + this.horizontal + "; vertical=" + this.vertical;
    }
}
