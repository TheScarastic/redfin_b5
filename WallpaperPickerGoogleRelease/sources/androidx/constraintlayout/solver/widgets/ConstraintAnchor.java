package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.SolverVariable;
/* loaded from: classes.dex */
public class ConstraintAnchor {
    public final ConstraintWidget mOwner;
    public SolverVariable mSolverVariable;
    public ConstraintAnchor mTarget;
    public final Type mType;
    public int mMargin = 0;
    public int mGoneMargin = -1;

    /* loaded from: classes.dex */
    public enum Type {
        /* Fake field, exist only in values array */
        NONE,
        LEFT,
        TOP,
        RIGHT,
        BOTTOM,
        BASELINE,
        CENTER,
        CENTER_X,
        CENTER_Y
    }

    public ConstraintAnchor(ConstraintWidget constraintWidget, Type type) {
        this.mOwner = constraintWidget;
        this.mType = type;
    }

    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0027, code lost:
        if (r6.mOwner.hasBaseline == false) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0041, code lost:
        if (r4 != r10) goto L_0x0043;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x005b, code lost:
        if (r4 != r10) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:43:0x0074, code lost:
        if (r4 != r2) goto L_0x0045;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean connect(androidx.constraintlayout.solver.widgets.ConstraintAnchor r7, int r8, int r9, boolean r10) {
        /*
            r6 = this;
            r0 = 1
            r1 = 0
            if (r7 != 0) goto L_0x000d
            r7 = 0
            r6.mTarget = r7
            r6.mMargin = r1
            r7 = -1
            r6.mGoneMargin = r7
            return r0
        L_0x000d:
            if (r10 != 0) goto L_0x007a
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r10 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER_Y
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.CENTER_X
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r3 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BASELINE
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r4 = r7.mType
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r5 = r6.mType
            if (r4 != r5) goto L_0x002a
            if (r5 != r3) goto L_0x0043
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r7.mOwner
            boolean r10 = r10.hasBaseline
            if (r10 == 0) goto L_0x0045
            androidx.constraintlayout.solver.widgets.ConstraintWidget r10 = r6.mOwner
            boolean r10 = r10.hasBaseline
            if (r10 != 0) goto L_0x0043
            goto L_0x0045
        L_0x002a:
            int r5 = r5.ordinal()
            switch(r5) {
                case 0: goto L_0x0045;
                case 1: goto L_0x0060;
                case 2: goto L_0x0047;
                case 3: goto L_0x0060;
                case 4: goto L_0x0047;
                case 5: goto L_0x0045;
                case 6: goto L_0x003d;
                case 7: goto L_0x0045;
                case 8: goto L_0x0045;
                default: goto L_0x0031;
            }
        L_0x0031:
            java.lang.AssertionError r7 = new java.lang.AssertionError
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r6 = r6.mType
            java.lang.String r6 = r6.name()
            r7.<init>(r6)
            throw r7
        L_0x003d:
            if (r4 == r3) goto L_0x0045
            if (r4 == r2) goto L_0x0045
            if (r4 == r10) goto L_0x0045
        L_0x0043:
            r10 = r0
            goto L_0x0077
        L_0x0045:
            r10 = r1
            goto L_0x0077
        L_0x0047:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.TOP
            if (r4 == r2) goto L_0x0052
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r2 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.BOTTOM
            if (r4 != r2) goto L_0x0050
            goto L_0x0052
        L_0x0050:
            r2 = r1
            goto L_0x0053
        L_0x0052:
            r2 = r0
        L_0x0053:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r7.mOwner
            boolean r3 = r3 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r3 == 0) goto L_0x005e
            if (r2 != 0) goto L_0x0043
            if (r4 != r10) goto L_0x0045
            goto L_0x0043
        L_0x005e:
            r10 = r2
            goto L_0x0077
        L_0x0060:
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r10 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.LEFT
            if (r4 == r10) goto L_0x006b
            androidx.constraintlayout.solver.widgets.ConstraintAnchor$Type r10 = androidx.constraintlayout.solver.widgets.ConstraintAnchor.Type.RIGHT
            if (r4 != r10) goto L_0x0069
            goto L_0x006b
        L_0x0069:
            r10 = r1
            goto L_0x006c
        L_0x006b:
            r10 = r0
        L_0x006c:
            androidx.constraintlayout.solver.widgets.ConstraintWidget r3 = r7.mOwner
            boolean r3 = r3 instanceof androidx.constraintlayout.solver.widgets.Guideline
            if (r3 == 0) goto L_0x0077
            if (r10 != 0) goto L_0x0043
            if (r4 != r2) goto L_0x0045
            goto L_0x0043
        L_0x0077:
            if (r10 != 0) goto L_0x007a
            return r1
        L_0x007a:
            r6.mTarget = r7
            if (r8 <= 0) goto L_0x0081
            r6.mMargin = r8
            goto L_0x0083
        L_0x0081:
            r6.mMargin = r1
        L_0x0083:
            r6.mGoneMargin = r9
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintAnchor.connect(androidx.constraintlayout.solver.widgets.ConstraintAnchor, int, int, boolean):boolean");
    }

    public int getMargin() {
        ConstraintAnchor constraintAnchor;
        if (this.mOwner.mVisibility == 8) {
            return 0;
        }
        int i = this.mGoneMargin;
        if (i <= -1 || (constraintAnchor = this.mTarget) == null || constraintAnchor.mOwner.mVisibility != 8) {
            return this.mMargin;
        }
        return i;
    }

    public boolean isConnected() {
        return this.mTarget != null;
    }

    public void reset() {
        this.mTarget = null;
        this.mMargin = 0;
        this.mGoneMargin = -1;
    }

    public void resetSolverVariable() {
        SolverVariable solverVariable = this.mSolverVariable;
        if (solverVariable == null) {
            this.mSolverVariable = new SolverVariable(1);
        } else {
            solverVariable.reset();
        }
    }

    public String toString() {
        return this.mOwner.mDebugName + ":" + this.mType.toString();
    }
}
