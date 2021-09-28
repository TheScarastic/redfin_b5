package androidx.constraintlayout.solver;

import androidx.constraintlayout.solver.widgets.ConstraintAnchor;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public class LinearSystem {
    public static int POOL_SIZE = 1000;
    public final Cache mCache;
    public Row mGoal;
    public ArrayRow[] mRows;
    public final Row mTempGoal;
    public int mVariablesID = 0;
    public int TABLE_SIZE = 32;
    public int mMaxColumns = 32;
    public boolean newgraphOptimizer = false;
    public boolean[] mAlreadyTestedCandidates = new boolean[32];
    public int mNumColumns = 1;
    public int mNumRows = 0;
    public int mMaxRows = 32;
    public SolverVariable[] mPoolVariables = new SolverVariable[POOL_SIZE];
    public int mPoolVariablesCount = 0;

    /* loaded from: classes.dex */
    public interface Row {
        void addError(SolverVariable solverVariable);

        void clear();

        SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] zArr);
    }

    public LinearSystem() {
        this.mRows = null;
        this.mRows = new ArrayRow[32];
        releaseRows();
        Cache cache = new Cache(0);
        this.mCache = cache;
        this.mGoal = new OptimizedPriorityGoalRow(cache);
        this.mTempGoal = new ArrayRow(cache);
    }

    public final SolverVariable acquireSolverVariable$enumunboxing$(int i, String str) {
        SolverVariable solverVariable = (SolverVariable) this.mCache.solverVariablePool.acquire();
        if (solverVariable == null) {
            solverVariable = new SolverVariable(i);
            solverVariable.mType = i;
        } else {
            solverVariable.reset();
            solverVariable.mType = i;
        }
        int i2 = this.mPoolVariablesCount;
        int i3 = POOL_SIZE;
        if (i2 >= i3) {
            int i4 = i3 * 2;
            POOL_SIZE = i4;
            this.mPoolVariables = (SolverVariable[]) Arrays.copyOf(this.mPoolVariables, i4);
        }
        SolverVariable[] solverVariableArr = this.mPoolVariables;
        int i5 = this.mPoolVariablesCount;
        this.mPoolVariablesCount = i5 + 1;
        solverVariableArr[i5] = solverVariable;
        return solverVariable;
    }

    public void addCentering(SolverVariable solverVariable, SolverVariable solverVariable2, int i, float f, SolverVariable solverVariable3, SolverVariable solverVariable4, int i2, int i3) {
        ArrayRow createRow = createRow();
        if (solverVariable2 == solverVariable3) {
            createRow.variables.put(solverVariable, 1.0f);
            createRow.variables.put(solverVariable4, 1.0f);
            createRow.variables.put(solverVariable2, -2.0f);
        } else if (f == 0.5f) {
            createRow.variables.put(solverVariable, 1.0f);
            createRow.variables.put(solverVariable2, -1.0f);
            createRow.variables.put(solverVariable3, -1.0f);
            createRow.variables.put(solverVariable4, 1.0f);
            if (i > 0 || i2 > 0) {
                createRow.constantValue = (float) ((-i) + i2);
            }
        } else if (f <= 0.0f) {
            createRow.variables.put(solverVariable, -1.0f);
            createRow.variables.put(solverVariable2, 1.0f);
            createRow.constantValue = (float) i;
        } else if (f >= 1.0f) {
            createRow.variables.put(solverVariable4, -1.0f);
            createRow.variables.put(solverVariable3, 1.0f);
            createRow.constantValue = (float) (-i2);
        } else {
            float f2 = 1.0f - f;
            createRow.variables.put(solverVariable, f2 * 1.0f);
            createRow.variables.put(solverVariable2, f2 * -1.0f);
            createRow.variables.put(solverVariable3, -1.0f * f);
            createRow.variables.put(solverVariable4, 1.0f * f);
            if (i > 0 || i2 > 0) {
                createRow.constantValue = (((float) i2) * f) + (((float) (-i)) * f2);
            }
        }
        if (i3 != 7) {
            createRow.addError(this, i3);
        }
        addConstraint(createRow);
    }

    /* JADX WARNING: Removed duplicated region for block: B:148:0x0165 A[SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:65:0x011e  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void addConstraint(androidx.constraintlayout.solver.ArrayRow r19) {
        /*
        // Method dump skipped, instructions count: 539
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.LinearSystem.addConstraint(androidx.constraintlayout.solver.ArrayRow):void");
    }

    public ArrayRow addEquality(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow createRow = createRow();
        boolean z = false;
        if (i != 0) {
            if (i < 0) {
                i *= -1;
                z = true;
            }
            createRow.constantValue = (float) i;
        }
        if (!z) {
            createRow.variables.put(solverVariable, -1.0f);
            createRow.variables.put(solverVariable2, 1.0f);
        } else {
            createRow.variables.put(solverVariable, 1.0f);
            createRow.variables.put(solverVariable2, -1.0f);
        }
        if (i2 != 7) {
            createRow.addError(this, i2);
        }
        addConstraint(createRow);
        return createRow;
    }

    public void addGreaterThan(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowGreaterThan(solverVariable, solverVariable2, createSlackVariable, i);
        if (i2 != 7) {
            createRow.variables.put(createErrorVariable(i2, null), (float) ((int) (createRow.variables.get(createSlackVariable) * -1.0f)));
        }
        addConstraint(createRow);
    }

    public void addLowerThan(SolverVariable solverVariable, SolverVariable solverVariable2, int i, int i2) {
        ArrayRow createRow = createRow();
        SolverVariable createSlackVariable = createSlackVariable();
        createSlackVariable.strength = 0;
        createRow.createRowLowerThan(solverVariable, solverVariable2, createSlackVariable, i);
        if (i2 != 7) {
            createRow.variables.put(createErrorVariable(i2, null), (float) ((int) (createRow.variables.get(createSlackVariable) * -1.0f)));
        }
        addConstraint(createRow);
    }

    public void addRatio(SolverVariable solverVariable, SolverVariable solverVariable2, SolverVariable solverVariable3, SolverVariable solverVariable4, float f, int i) {
        ArrayRow createRow = createRow();
        createRow.createRowDimensionRatio(solverVariable, solverVariable2, solverVariable3, solverVariable4, f);
        if (i != 7) {
            createRow.addError(this, i);
        }
        addConstraint(createRow);
    }

    public final void addRow(ArrayRow arrayRow) {
        ArrayRow[] arrayRowArr = this.mRows;
        int i = this.mNumRows;
        if (arrayRowArr[i] != null) {
            this.mCache.arrayRowPool.release(arrayRowArr[i]);
        }
        ArrayRow[] arrayRowArr2 = this.mRows;
        int i2 = this.mNumRows;
        arrayRowArr2[i2] = arrayRow;
        SolverVariable solverVariable = arrayRow.variable;
        solverVariable.definitionId = i2;
        this.mNumRows = i2 + 1;
        solverVariable.updateReferencesWithNewDefinition(arrayRow);
    }

    public final void computeValues() {
        for (int i = 0; i < this.mNumRows; i++) {
            ArrayRow arrayRow = this.mRows[i];
            arrayRow.variable.computedValue = arrayRow.constantValue;
        }
    }

    public SolverVariable createErrorVariable(int i, String str) {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable$enumunboxing$ = acquireSolverVariable$enumunboxing$(4, str);
        int i2 = this.mVariablesID + 1;
        this.mVariablesID = i2;
        this.mNumColumns++;
        acquireSolverVariable$enumunboxing$.id = i2;
        acquireSolverVariable$enumunboxing$.strength = i;
        ((SolverVariable[]) this.mCache.mIndexedVariables)[i2] = acquireSolverVariable$enumunboxing$;
        this.mGoal.addError(acquireSolverVariable$enumunboxing$);
        return acquireSolverVariable$enumunboxing$;
    }

    public SolverVariable createObjectVariable(Object obj) {
        SolverVariable solverVariable = null;
        if (obj == null) {
            return null;
        }
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        if (obj instanceof ConstraintAnchor) {
            ConstraintAnchor constraintAnchor = (ConstraintAnchor) obj;
            solverVariable = constraintAnchor.mSolverVariable;
            if (solverVariable == null) {
                constraintAnchor.resetSolverVariable();
                solverVariable = constraintAnchor.mSolverVariable;
            }
            int i = solverVariable.id;
            if (i == -1 || i > this.mVariablesID || ((SolverVariable[]) this.mCache.mIndexedVariables)[i] == null) {
                if (i != -1) {
                    solverVariable.reset();
                }
                int i2 = this.mVariablesID + 1;
                this.mVariablesID = i2;
                this.mNumColumns++;
                solverVariable.id = i2;
                solverVariable.mType = 1;
                ((SolverVariable[]) this.mCache.mIndexedVariables)[i2] = solverVariable;
            }
        }
        return solverVariable;
    }

    public ArrayRow createRow() {
        ArrayRow arrayRow = (ArrayRow) this.mCache.arrayRowPool.acquire();
        if (arrayRow == null) {
            arrayRow = new ArrayRow(this.mCache);
        } else {
            arrayRow.variable = null;
            arrayRow.variables.clear();
            arrayRow.constantValue = 0.0f;
            arrayRow.isSimpleDefinition = false;
        }
        SolverVariable.uniqueErrorId++;
        return arrayRow;
    }

    public SolverVariable createSlackVariable() {
        if (this.mNumColumns + 1 >= this.mMaxColumns) {
            increaseTableSize();
        }
        SolverVariable acquireSolverVariable$enumunboxing$ = acquireSolverVariable$enumunboxing$(3, null);
        int i = this.mVariablesID + 1;
        this.mVariablesID = i;
        this.mNumColumns++;
        acquireSolverVariable$enumunboxing$.id = i;
        ((SolverVariable[]) this.mCache.mIndexedVariables)[i] = acquireSolverVariable$enumunboxing$;
        return acquireSolverVariable$enumunboxing$;
    }

    public int getObjectVariableValue(Object obj) {
        SolverVariable solverVariable = ((ConstraintAnchor) obj).mSolverVariable;
        if (solverVariable != null) {
            return (int) (solverVariable.computedValue + 0.5f);
        }
        return 0;
    }

    public final void increaseTableSize() {
        int i = this.TABLE_SIZE * 2;
        this.TABLE_SIZE = i;
        this.mRows = (ArrayRow[]) Arrays.copyOf(this.mRows, i);
        Cache cache = this.mCache;
        cache.mIndexedVariables = (SolverVariable[]) Arrays.copyOf((SolverVariable[]) cache.mIndexedVariables, this.TABLE_SIZE);
        int i2 = this.TABLE_SIZE;
        this.mAlreadyTestedCandidates = new boolean[i2];
        this.mMaxColumns = i2;
        this.mMaxRows = i2;
    }

    public void minimizeGoal(Row row) throws Exception {
        float f;
        boolean z;
        int i = 0;
        while (true) {
            f = 0.0f;
            if (i >= this.mNumRows) {
                z = false;
                break;
            }
            ArrayRow[] arrayRowArr = this.mRows;
            if (arrayRowArr[i].variable.mType != 1 && arrayRowArr[i].constantValue < 0.0f) {
                z = true;
                break;
            }
            i++;
        }
        if (z) {
            boolean z2 = false;
            int i2 = 0;
            while (!z2) {
                i2++;
                float f2 = Float.MAX_VALUE;
                int i3 = -1;
                int i4 = -1;
                int i5 = 0;
                int i6 = 0;
                while (i5 < this.mNumRows) {
                    ArrayRow arrayRow = this.mRows[i5];
                    if (arrayRow.variable.mType != 1 && !arrayRow.isSimpleDefinition && arrayRow.constantValue < f) {
                        int i7 = 1;
                        while (i7 < this.mNumColumns) {
                            SolverVariable solverVariable = ((SolverVariable[]) this.mCache.mIndexedVariables)[i7];
                            float f3 = arrayRow.variables.get(solverVariable);
                            if (f3 > f) {
                                for (int i8 = 0; i8 < 8; i8++) {
                                    float f4 = solverVariable.strengthVector[i8] / f3;
                                    if ((f4 < f2 && i8 == i6) || i8 > i6) {
                                        i6 = i8;
                                        f2 = f4;
                                        i3 = i5;
                                        i4 = i7;
                                    }
                                }
                            }
                            i7++;
                            f = 0.0f;
                        }
                    }
                    i5++;
                    f = 0.0f;
                }
                if (i3 != -1) {
                    ArrayRow arrayRow2 = this.mRows[i3];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(((SolverVariable[]) this.mCache.mIndexedVariables)[i4]);
                    SolverVariable solverVariable2 = arrayRow2.variable;
                    solverVariable2.definitionId = i3;
                    solverVariable2.updateReferencesWithNewDefinition(arrayRow2);
                } else {
                    z2 = true;
                }
                if (i2 > this.mNumColumns / 2) {
                    z2 = true;
                }
                f = 0.0f;
            }
        }
        optimize(row);
        computeValues();
    }

    public final int optimize(Row row) {
        boolean z;
        int i = 0;
        for (int i2 = 0; i2 < this.mNumColumns; i2++) {
            this.mAlreadyTestedCandidates[i2] = false;
        }
        boolean z2 = false;
        int i3 = 0;
        while (!z2) {
            i3++;
            if (i3 >= this.mNumColumns * 2) {
                return i3;
            }
            SolverVariable solverVariable = ((ArrayRow) row).variable;
            if (solverVariable != null) {
                this.mAlreadyTestedCandidates[solverVariable.id] = true;
            }
            SolverVariable pivotCandidate = row.getPivotCandidate(this, this.mAlreadyTestedCandidates);
            if (pivotCandidate != null) {
                boolean[] zArr = this.mAlreadyTestedCandidates;
                int i4 = pivotCandidate.id;
                if (zArr[i4]) {
                    return i3;
                }
                zArr[i4] = true;
            }
            if (pivotCandidate != null) {
                float f = Float.MAX_VALUE;
                int i5 = i;
                int i6 = -1;
                while (i5 < this.mNumRows) {
                    ArrayRow arrayRow = this.mRows[i5];
                    if (arrayRow.variable.mType != 1 && !arrayRow.isSimpleDefinition) {
                        ArrayLinkedVariables arrayLinkedVariables = arrayRow.variables;
                        int i7 = arrayLinkedVariables.mHead;
                        if (i7 != -1) {
                            int i8 = i;
                            while (i7 != -1 && i8 < arrayLinkedVariables.currentSize) {
                                if (arrayLinkedVariables.mArrayIndices[i7] == pivotCandidate.id) {
                                    z = true;
                                    break;
                                }
                                i7 = arrayLinkedVariables.mArrayNextIndices[i7];
                                i8++;
                            }
                        }
                        z = false;
                        if (z) {
                            float f2 = arrayRow.variables.get(pivotCandidate);
                            if (f2 < 0.0f) {
                                float f3 = (-arrayRow.constantValue) / f2;
                                if (f3 < f) {
                                    i6 = i5;
                                    f = f3;
                                }
                            }
                        }
                    }
                    i5++;
                    i = 0;
                }
                if (i6 > -1) {
                    ArrayRow arrayRow2 = this.mRows[i6];
                    arrayRow2.variable.definitionId = -1;
                    arrayRow2.pivot(pivotCandidate);
                    SolverVariable solverVariable2 = arrayRow2.variable;
                    solverVariable2.definitionId = i6;
                    solverVariable2.updateReferencesWithNewDefinition(arrayRow2);
                }
            } else {
                z2 = true;
            }
            i = 0;
        }
        return i3;
    }

    public final void releaseRows() {
        int i = 0;
        while (true) {
            ArrayRow[] arrayRowArr = this.mRows;
            if (i < arrayRowArr.length) {
                ArrayRow arrayRow = arrayRowArr[i];
                if (arrayRow != null) {
                    this.mCache.arrayRowPool.release(arrayRow);
                }
                this.mRows[i] = null;
                i++;
            } else {
                return;
            }
        }
    }

    public void reset() {
        Cache cache;
        int i = 0;
        while (true) {
            cache = this.mCache;
            Object obj = cache.mIndexedVariables;
            if (i >= ((SolverVariable[]) obj).length) {
                break;
            }
            SolverVariable solverVariable = ((SolverVariable[]) obj)[i];
            if (solverVariable != null) {
                solverVariable.reset();
            }
            i++;
        }
        Pools$SimplePool pools$SimplePool = cache.solverVariablePool;
        SolverVariable[] solverVariableArr = this.mPoolVariables;
        int i2 = this.mPoolVariablesCount;
        Objects.requireNonNull(pools$SimplePool);
        if (i2 > solverVariableArr.length) {
            i2 = solverVariableArr.length;
        }
        for (int i3 = 0; i3 < i2; i3++) {
            SolverVariable solverVariable2 = solverVariableArr[i3];
            int i4 = pools$SimplePool.mPoolSize;
            Object[] objArr = pools$SimplePool.mPool;
            if (i4 < objArr.length) {
                objArr[i4] = solverVariable2;
                pools$SimplePool.mPoolSize = i4 + 1;
            }
        }
        this.mPoolVariablesCount = 0;
        Arrays.fill((SolverVariable[]) this.mCache.mIndexedVariables, (Object) null);
        this.mVariablesID = 0;
        this.mGoal.clear();
        this.mNumColumns = 1;
        for (int i5 = 0; i5 < this.mNumRows; i5++) {
            Objects.requireNonNull(this.mRows[i5]);
        }
        releaseRows();
        this.mNumRows = 0;
    }

    public void addEquality(SolverVariable solverVariable, int i) {
        int i2 = solverVariable.definitionId;
        if (i2 != -1) {
            ArrayRow arrayRow = this.mRows[i2];
            if (arrayRow.isSimpleDefinition) {
                arrayRow.constantValue = (float) i;
            } else if (arrayRow.variables.currentSize == 0) {
                arrayRow.isSimpleDefinition = true;
                arrayRow.constantValue = (float) i;
            } else {
                ArrayRow createRow = createRow();
                if (i < 0) {
                    createRow.constantValue = (float) (i * -1);
                    createRow.variables.put(solverVariable, 1.0f);
                } else {
                    createRow.constantValue = (float) i;
                    createRow.variables.put(solverVariable, -1.0f);
                }
                addConstraint(createRow);
            }
        } else {
            ArrayRow createRow2 = createRow();
            createRow2.variable = solverVariable;
            float f = (float) i;
            solverVariable.computedValue = f;
            createRow2.constantValue = f;
            createRow2.isSimpleDefinition = true;
            addConstraint(createRow2);
        }
    }
}
