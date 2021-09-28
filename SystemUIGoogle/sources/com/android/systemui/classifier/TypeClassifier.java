package com.android.systemui.classifier;
/* loaded from: classes.dex */
public class TypeClassifier extends FalsingClassifier {
    /* access modifiers changed from: package-private */
    public TypeClassifier(FalsingDataProvider falsingDataProvider) {
        super(falsingDataProvider);
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0022, code lost:
        if (r10 != false) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x002c, code lost:
        if (r10 != false) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0031, code lost:
        if (r10 != false) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0036, code lost:
        if (r10 != false) goto L_0x0025;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:22:0x003e, code lost:
        if (r10 == false) goto L_0x0025;
     */
    @Override // com.android.systemui.classifier.FalsingClassifier
    /* Code decompiled incorrectly, please refer to instructions dump. */
    com.android.systemui.classifier.FalsingClassifier.Result calculateFalsingResult(int r6, double r7, double r9) {
        /*
            r5 = this;
            r7 = 0
            r9 = 13
            if (r6 == r9) goto L_0x0053
            r9 = 14
            if (r6 != r9) goto L_0x000b
            goto L_0x0053
        L_0x000b:
            boolean r9 = r5.isVertical()
            boolean r10 = r5.isUp()
            boolean r0 = r5.isRight()
            r1 = 4607182418800017408(0x3ff0000000000000, double:1.0)
            r3 = 0
            r4 = 1
            switch(r6) {
                case 0: goto L_0x003c;
                case 1: goto L_0x0039;
                case 2: goto L_0x003c;
                case 3: goto L_0x001e;
                case 4: goto L_0x0034;
                case 5: goto L_0x002f;
                case 6: goto L_0x002a;
                case 7: goto L_0x001e;
                case 8: goto L_0x0034;
                case 9: goto L_0x003c;
                case 10: goto L_0x003a;
                case 11: goto L_0x0027;
                case 12: goto L_0x0020;
                case 13: goto L_0x001e;
                case 14: goto L_0x001e;
                case 15: goto L_0x0041;
                default: goto L_0x001e;
            }
        L_0x001e:
            r9 = r4
            goto L_0x0041
        L_0x0020:
            if (r9 == 0) goto L_0x001e
            if (r10 != 0) goto L_0x0025
            goto L_0x001e
        L_0x0025:
            r9 = r3
            goto L_0x0041
        L_0x0027:
            r9 = r9 ^ 1
            goto L_0x0041
        L_0x002a:
            if (r0 != 0) goto L_0x001e
            if (r10 != 0) goto L_0x0025
            goto L_0x001e
        L_0x002f:
            if (r0 == 0) goto L_0x001e
            if (r10 != 0) goto L_0x0025
            goto L_0x001e
        L_0x0034:
            if (r9 == 0) goto L_0x001e
            if (r10 != 0) goto L_0x0025
            goto L_0x001e
        L_0x0039:
            r7 = r1
        L_0x003a:
            r1 = r7
            goto L_0x0041
        L_0x003c:
            if (r9 == 0) goto L_0x001e
            if (r10 == 0) goto L_0x0025
            goto L_0x001e
        L_0x0041:
            if (r9 == 0) goto L_0x004c
            java.lang.String r6 = r5.getReason(r6)
            com.android.systemui.classifier.FalsingClassifier$Result r5 = r5.falsed(r1, r6)
            goto L_0x0052
        L_0x004c:
            r5 = 4602678819172646912(0x3fe0000000000000, double:0.5)
            com.android.systemui.classifier.FalsingClassifier$Result r5 = com.android.systemui.classifier.FalsingClassifier.Result.passed(r5)
        L_0x0052:
            return r5
        L_0x0053:
            com.android.systemui.classifier.FalsingClassifier$Result r5 = com.android.systemui.classifier.FalsingClassifier.Result.passed(r7)
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.classifier.TypeClassifier.calculateFalsingResult(int, double, double):com.android.systemui.classifier.FalsingClassifier$Result");
    }

    private String getReason(int i) {
        return String.format("{interaction=%s, vertical=%s, up=%s, right=%s}", Integer.valueOf(i), Boolean.valueOf(isVertical()), Boolean.valueOf(isUp()), Boolean.valueOf(isRight()));
    }
}
