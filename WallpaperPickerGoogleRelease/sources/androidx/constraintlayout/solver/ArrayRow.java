package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.LinearSystem;
/* loaded from: classes.dex */
public class ArrayRow implements LinearSystem.Row {
    public final ArrayLinkedVariables variables;
    public SolverVariable variable = null;
    public float constantValue = 0.0f;
    public boolean isSimpleDefinition = false;

    public ArrayRow(Cache cache) {
        this.variables = new ArrayLinkedVariables(this, cache);
    }

    public ArrayRow addError(LinearSystem linearSystem, int i) {
        this.variables.put(linearSystem.createErrorVariable(i, "ep"), 1.0f);
        this.variables.put(linearSystem.createErrorVariable(i, "em"), -1.0f);
        return this;
    }

    @Override // androidx.constraintlayout.solver.LinearSystem.Row
    public void clear() {
        this.variables.clear();
        this.variable = null;
        this.constantValue = 0.0f;
    }

    public ArrayRow createRowDimensionRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f) {
        this.variables.put(solverVariable, -1.0f);
        this.variables.put(solverVariable2, 1.0f);
        this.variables.put(solverVariable3, f);
        this.variables.put(solverVariable4, -f);
        return this;
    }

    public ArrayRow createRowGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i) {
        boolean z = false;
        if (i != 0) {
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = (float) i;
        }
        if (!z) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, 1.0f);
        } else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, -1.0f);
        }
        return this;
    }

    public ArrayRow createRowLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, int i) {
        boolean z = false;
        if (i != 0) {
            if (i < 0) {
                i *= -1;
                z = true;
            }
            this.constantValue = (float) i;
        }
        if (!z) {
            this.variables.put(solverVariable, -1.0f);
            this.variables.put(solverVariable2, 1.0f);
            this.variables.put(solverVariable3, -1.0f);
        } else {
            this.variables.put(solverVariable, 1.0f);
            this.variables.put(solverVariable2, -1.0f);
            this.variables.put(solverVariable3, 1.0f);
        }
        return this;
    }

    public ArrayRow createRowWithAngle(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f) {
        this.variables.put(solverVariable3, 0.5f);
        this.variables.put(solverVariable4, 0.5f);
        this.variables.put(solverVariable, -0.5f);
        this.variables.put(solverVariable2, -0.5f);
        this.constantValue = -f;
        return this;
    }

    @Override // androidx.constraintlayout.solver.LinearSystem.Row
    public SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] zArr) {
        return this.variables.getPivotCandidate(zArr, null);
    }

    public void pivot(SolverVariable solverVariable) {
        SolverVariable solverVariable2 = this.variable;
        if (solverVariable2 != null) {
            this.variables.put(solverVariable2, -1.0f);
            this.variable = null;
        }
        float remove = this.variables.remove(solverVariable, true) * -1.0f;
        this.variable = solverVariable;
        if (remove != 1.0f) {
            this.constantValue /= remove;
            ArrayLinkedVariables arrayLinkedVariables = this.variables;
            int i = arrayLinkedVariables.mHead;
            int i2 = 0;
            while (i != -1 && i2 < arrayLinkedVariables.currentSize) {
                float[] fArr = arrayLinkedVariables.mArrayValues;
                fArr[i] = fArr[i] / remove;
                i = arrayLinkedVariables.mArrayNextIndices[i];
                i2++;
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:27:0x0076  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x007b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public java.lang.String toString() {
        /*
            r10 = this;
            androidx.constraintlayout.solver.SolverVariable r0 = r10.variable
            if (r0 != 0) goto L_0x0007
            java.lang.String r0 = "0"
            goto L_0x0016
        L_0x0007:
            java.lang.String r0 = ""
            java.lang.StringBuilder r0 = android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m(r0)
            androidx.constraintlayout.solver.SolverVariable r1 = r10.variable
            r0.append(r1)
            java.lang.String r0 = r0.toString()
        L_0x0016:
            java.lang.String r1 = " = "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
            float r1 = r10.constantValue
            r2 = 0
            int r1 = (r1 > r2 ? 1 : (r1 == r2 ? 0 : -1))
            r3 = 0
            r4 = 1
            if (r1 == 0) goto L_0x0034
            java.lang.StringBuilder r0 = android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m(r0)
            float r1 = r10.constantValue
            r0.append(r1)
            java.lang.String r0 = r0.toString()
            r1 = r4
            goto L_0x0035
        L_0x0034:
            r1 = r3
        L_0x0035:
            androidx.constraintlayout.solver.ArrayLinkedVariables r5 = r10.variables
            int r5 = r5.currentSize
        L_0x0039:
            if (r3 >= r5) goto L_0x0096
            androidx.constraintlayout.solver.ArrayLinkedVariables r6 = r10.variables
            androidx.constraintlayout.solver.SolverVariable r6 = r6.getVariable(r3)
            if (r6 != 0) goto L_0x0044
            goto L_0x0093
        L_0x0044:
            androidx.constraintlayout.solver.ArrayLinkedVariables r6 = r10.variables
            float r6 = r6.getVariableValue(r3)
            int r7 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r7 != 0) goto L_0x004f
            goto L_0x0093
        L_0x004f:
            java.lang.String r8 = "null"
            r9 = -1082130432(0xffffffffbf800000, float:-1.0)
            if (r1 != 0) goto L_0x0060
            int r1 = (r6 > r2 ? 1 : (r6 == r2 ? 0 : -1))
            if (r1 >= 0) goto L_0x0070
            java.lang.String r1 = "- "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
            goto L_0x006f
        L_0x0060:
            if (r7 <= 0) goto L_0x0069
            java.lang.String r1 = " + "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
            goto L_0x0070
        L_0x0069:
            java.lang.String r1 = " - "
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r1)
        L_0x006f:
            float r6 = r6 * r9
        L_0x0070:
            r1 = 1065353216(0x3f800000, float:1.0)
            int r1 = (r6 > r1 ? 1 : (r6 == r1 ? 0 : -1))
            if (r1 != 0) goto L_0x007b
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r8)
            goto L_0x0092
        L_0x007b:
            java.lang.StringBuilder r1 = new java.lang.StringBuilder
            r1.<init>()
            r1.append(r0)
            r1.append(r6)
            java.lang.String r0 = " "
            r1.append(r0)
            r1.append(r8)
            java.lang.String r0 = r1.toString()
        L_0x0092:
            r1 = r4
        L_0x0093:
            int r3 = r3 + 1
            goto L_0x0039
        L_0x0096:
            if (r1 != 0) goto L_0x009e
            java.lang.String r10 = "0.0"
            java.lang.String r0 = androidx.appcompat.view.SupportMenuInflater$$ExternalSyntheticOutline0.m(r0, r10)
        L_0x009e:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.ArrayRow.toString():java.lang.String");
    }

    public void updateFromRow(ArrayRow arrayRow, boolean z) {
        ArrayLinkedVariables arrayLinkedVariables = this.variables;
        int i = arrayLinkedVariables.mHead;
        while (true) {
            int i2 = 0;
            while (i != -1 && i2 < arrayLinkedVariables.currentSize) {
                int i3 = arrayLinkedVariables.mArrayIndices[i];
                SolverVariable solverVariable = arrayRow.variable;
                if (i3 == solverVariable.id) {
                    float f = arrayLinkedVariables.mArrayValues[i];
                    arrayLinkedVariables.remove(solverVariable, z);
                    ArrayLinkedVariables arrayLinkedVariables2 = arrayRow.variables;
                    int i4 = arrayLinkedVariables2.mHead;
                    int i5 = 0;
                    while (i4 != -1 && i5 < arrayLinkedVariables2.currentSize) {
                        arrayLinkedVariables.add(((SolverVariable[]) arrayLinkedVariables.mCache.mIndexedVariables)[arrayLinkedVariables2.mArrayIndices[i4]], arrayLinkedVariables2.mArrayValues[i4] * f, z);
                        i4 = arrayLinkedVariables2.mArrayNextIndices[i4];
                        i5++;
                    }
                    this.constantValue = (arrayRow.constantValue * f) + this.constantValue;
                    if (z) {
                        arrayRow.variable.removeFromRow(this);
                    }
                    i = arrayLinkedVariables.mHead;
                } else {
                    i = arrayLinkedVariables.mArrayNextIndices[i];
                    i2++;
                }
            }
            return;
        }
    }

    @Override // androidx.constraintlayout.solver.LinearSystem.Row
    public void addError(SolverVariable solverVariable) {
        float f;
        int i = solverVariable.strength;
        if (i != 1) {
            if (i == 2) {
                f = 1000.0f;
            } else if (i == 3) {
                f = 1000000.0f;
            } else if (i == 4) {
                f = 1.0E9f;
            } else if (i == 5) {
                f = 1.0E12f;
            }
            this.variables.put(solverVariable, f);
        }
        f = 1.0f;
        this.variables.put(solverVariable, f);
    }
}
