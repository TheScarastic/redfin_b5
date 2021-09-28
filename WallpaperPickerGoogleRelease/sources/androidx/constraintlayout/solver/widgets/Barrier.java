package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.ArrayRow;
import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.SolverVariable;
/* loaded from: classes.dex */
public class Barrier extends HelperWidget {
    public int mBarrierType = 0;
    public boolean mAllowsGoneWidget = true;
    public int mMargin = 0;

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void addToSolver(LinearSystem linearSystem) {
        Object[] objArr;
        boolean z;
        int i;
        int i2;
        ConstraintAnchor[] constraintAnchorArr = this.mListAnchors;
        constraintAnchorArr[0] = this.mLeft;
        constraintAnchorArr[2] = this.mTop;
        constraintAnchorArr[1] = this.mRight;
        constraintAnchorArr[3] = this.mBottom;
        int i3 = 0;
        while (true) {
            objArr = this.mListAnchors;
            if (i3 >= objArr.length) {
                break;
            }
            objArr[i3].mSolverVariable = linearSystem.createObjectVariable(objArr[i3]);
            i3++;
        }
        int i4 = this.mBarrierType;
        if (i4 >= 0) {
            int i5 = 4;
            if (i4 < 4) {
                ConstraintAnchor constraintAnchor = objArr[i4];
                for (int i6 = 0; i6 < this.mWidgetsCount; i6++) {
                    ConstraintWidget constraintWidget = this.mWidgets[i6];
                    if ((this.mAllowsGoneWidget || constraintWidget.allowedInBarrier()) && ((((i = this.mBarrierType) == 0 || i == 1) && constraintWidget.getHorizontalDimensionBehaviour$enumunboxing$() == 3 && constraintWidget.mLeft.mTarget != null && constraintWidget.mRight.mTarget != null) || (((i2 = this.mBarrierType) == 2 || i2 == 3) && constraintWidget.getVerticalDimensionBehaviour$enumunboxing$() == 3 && constraintWidget.mTop.mTarget != null && constraintWidget.mBottom.mTarget != null))) {
                        z = true;
                        break;
                    }
                }
                z = false;
                for (int i7 = 0; i7 < this.mWidgetsCount; i7++) {
                    ConstraintWidget constraintWidget2 = this.mWidgets[i7];
                    if (this.mAllowsGoneWidget || constraintWidget2.allowedInBarrier()) {
                        SolverVariable createObjectVariable = linearSystem.createObjectVariable(constraintWidget2.mListAnchors[this.mBarrierType]);
                        ConstraintAnchor[] constraintAnchorArr2 = constraintWidget2.mListAnchors;
                        int i8 = this.mBarrierType;
                        constraintAnchorArr2[i8].mSolverVariable = createObjectVariable;
                        int i9 = (constraintAnchorArr2[i8].mTarget == null || constraintAnchorArr2[i8].mTarget.mOwner != this) ? 0 : constraintAnchorArr2[i8].mMargin + 0;
                        if (i8 == 0 || i8 == 2) {
                            SolverVariable solverVariable = constraintAnchor.mSolverVariable;
                            int i10 = this.mMargin - i9;
                            ArrayRow createRow = linearSystem.createRow();
                            SolverVariable createSlackVariable = linearSystem.createSlackVariable();
                            createSlackVariable.strength = 0;
                            createRow.createRowLowerThan(solverVariable, createObjectVariable, createSlackVariable, i10);
                            linearSystem.addConstraint(createRow);
                        } else {
                            SolverVariable solverVariable2 = constraintAnchor.mSolverVariable;
                            int i11 = this.mMargin + i9;
                            ArrayRow createRow2 = linearSystem.createRow();
                            SolverVariable createSlackVariable2 = linearSystem.createSlackVariable();
                            createSlackVariable2.strength = 0;
                            createRow2.createRowGreaterThan(solverVariable2, createObjectVariable, createSlackVariable2, i11);
                            linearSystem.addConstraint(createRow2);
                        }
                    }
                }
                i5 = 5;
                if (!z) {
                }
                int i12 = this.mBarrierType;
                if (i12 == 0) {
                    linearSystem.addEquality(this.mRight.mSolverVariable, this.mLeft.mSolverVariable, 0, 7);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, i5);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, 0);
                } else if (i12 == 1) {
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mRight.mSolverVariable, 0, 7);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mLeft.mSolverVariable, 0, i5);
                    linearSystem.addEquality(this.mLeft.mSolverVariable, this.mParent.mRight.mSolverVariable, 0, 0);
                } else if (i12 == 2) {
                    linearSystem.addEquality(this.mBottom.mSolverVariable, this.mTop.mSolverVariable, 0, 7);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, i5);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, 0);
                } else if (i12 == 3) {
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mBottom.mSolverVariable, 0, 7);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mTop.mSolverVariable, 0, i5);
                    linearSystem.addEquality(this.mTop.mSolverVariable, this.mParent.mBottom.mSolverVariable, 0, 0);
                }
            }
        }
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public boolean allowedInBarrier() {
        return true;
    }
}
