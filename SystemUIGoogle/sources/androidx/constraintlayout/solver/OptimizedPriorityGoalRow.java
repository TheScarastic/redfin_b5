package androidx.constraintlayout.solver;

import java.util.Arrays;
import java.util.Comparator;
/* loaded from: classes.dex */
public class OptimizedPriorityGoalRow extends ArrayRow {
    Cache mCache;
    private int TABLE_SIZE = 128;
    private SolverVariable[] arrayGoals = new SolverVariable[128];
    private SolverVariable[] sortArray = new SolverVariable[128];
    private int numGoals = 0;
    GoalVariableAccessor accessor = new GoalVariableAccessor(this);

    /* loaded from: classes.dex */
    class GoalVariableAccessor implements Comparable {
        OptimizedPriorityGoalRow row;
        SolverVariable variable;

        public GoalVariableAccessor(OptimizedPriorityGoalRow optimizedPriorityGoalRow) {
            this.row = optimizedPriorityGoalRow;
        }

        public void init(SolverVariable solverVariable) {
            this.variable = solverVariable;
        }

        public boolean addToGoal(SolverVariable solverVariable, float f) {
            boolean z = true;
            if (this.variable.inGoal) {
                for (int i = 0; i < 8; i++) {
                    float[] fArr = this.variable.goalStrengthVector;
                    fArr[i] = fArr[i] + (solverVariable.goalStrengthVector[i] * f);
                    if (Math.abs(fArr[i]) < 1.0E-4f) {
                        this.variable.goalStrengthVector[i] = 0.0f;
                    } else {
                        z = false;
                    }
                }
                if (z) {
                    OptimizedPriorityGoalRow.this.removeGoal(this.variable);
                }
                return false;
            }
            for (int i2 = 0; i2 < 8; i2++) {
                float f2 = solverVariable.goalStrengthVector[i2];
                if (f2 != 0.0f) {
                    float f3 = f2 * f;
                    if (Math.abs(f3) < 1.0E-4f) {
                        f3 = 0.0f;
                    }
                    this.variable.goalStrengthVector[i2] = f3;
                } else {
                    this.variable.goalStrengthVector[i2] = 0.0f;
                }
            }
            return true;
        }

        public final boolean isNegative() {
            for (int i = 7; i >= 0; i--) {
                float f = this.variable.goalStrengthVector[i];
                if (f > 0.0f) {
                    return false;
                }
                if (f < 0.0f) {
                    return true;
                }
            }
            return false;
        }

        public final boolean isSmallerThan(SolverVariable solverVariable) {
            int i = 7;
            while (true) {
                if (i < 0) {
                    break;
                }
                float f = solverVariable.goalStrengthVector[i];
                float f2 = this.variable.goalStrengthVector[i];
                if (f2 == f) {
                    i--;
                } else if (f2 < f) {
                    return true;
                }
            }
            return false;
        }

        @Override // java.lang.Comparable
        public int compareTo(Object obj) {
            return this.variable.id - ((SolverVariable) obj).id;
        }

        public void reset() {
            Arrays.fill(this.variable.goalStrengthVector, 0.0f);
        }

        @Override // java.lang.Object
        public String toString() {
            String str = "[ ";
            if (this.variable != null) {
                for (int i = 0; i < 8; i++) {
                    str = str + this.variable.goalStrengthVector[i] + " ";
                }
            }
            return str + "] " + this.variable;
        }
    }

    @Override // androidx.constraintlayout.solver.ArrayRow, androidx.constraintlayout.solver.LinearSystem.Row
    public void clear() {
        this.numGoals = 0;
        this.constantValue = 0.0f;
    }

    public OptimizedPriorityGoalRow(Cache cache) {
        super(cache);
        this.mCache = cache;
    }

    @Override // androidx.constraintlayout.solver.ArrayRow, androidx.constraintlayout.solver.LinearSystem.Row
    public SolverVariable getPivotCandidate(LinearSystem linearSystem, boolean[] zArr) {
        int i = -1;
        for (int i2 = 0; i2 < this.numGoals; i2++) {
            SolverVariable solverVariable = this.arrayGoals[i2];
            if (!zArr[solverVariable.id]) {
                this.accessor.init(solverVariable);
                if (i == -1) {
                    if (!this.accessor.isNegative()) {
                    }
                    i = i2;
                } else {
                    if (!this.accessor.isSmallerThan(this.arrayGoals[i])) {
                    }
                    i = i2;
                }
            }
        }
        if (i == -1) {
            return null;
        }
        return this.arrayGoals[i];
    }

    @Override // androidx.constraintlayout.solver.ArrayRow, androidx.constraintlayout.solver.LinearSystem.Row
    public void addError(SolverVariable solverVariable) {
        this.accessor.init(solverVariable);
        this.accessor.reset();
        solverVariable.goalStrengthVector[solverVariable.strength] = 1.0f;
        addToGoal(solverVariable);
    }

    private final void addToGoal(SolverVariable solverVariable) {
        int i;
        int i2 = this.numGoals + 1;
        SolverVariable[] solverVariableArr = this.arrayGoals;
        if (i2 > solverVariableArr.length) {
            SolverVariable[] solverVariableArr2 = (SolverVariable[]) Arrays.copyOf(solverVariableArr, solverVariableArr.length * 2);
            this.arrayGoals = solverVariableArr2;
            this.sortArray = (SolverVariable[]) Arrays.copyOf(solverVariableArr2, solverVariableArr2.length * 2);
        }
        SolverVariable[] solverVariableArr3 = this.arrayGoals;
        int i3 = this.numGoals;
        solverVariableArr3[i3] = solverVariable;
        int i4 = i3 + 1;
        this.numGoals = i4;
        if (i4 > 1 && solverVariableArr3[i4 - 1].id > solverVariable.id) {
            int i5 = 0;
            while (true) {
                i = this.numGoals;
                if (i5 >= i) {
                    break;
                }
                this.sortArray[i5] = this.arrayGoals[i5];
                i5++;
            }
            Arrays.sort(this.sortArray, 0, i, new Comparator<SolverVariable>() { // from class: androidx.constraintlayout.solver.OptimizedPriorityGoalRow.1
                public int compare(SolverVariable solverVariable2, SolverVariable solverVariable3) {
                    return solverVariable2.id - solverVariable3.id;
                }
            });
            for (int i6 = 0; i6 < this.numGoals; i6++) {
                this.arrayGoals[i6] = this.sortArray[i6];
            }
        }
        solverVariable.inGoal = true;
        solverVariable.addToRow(this);
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Code restructure failed: missing block: B:10:0x001c, code lost:
        r5.numGoals = r2 - 1;
        r6.inGoal = false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:7:0x000c, code lost:
        r2 = r5.numGoals;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x0010, code lost:
        if (r1 >= (r2 - 1)) goto L_0x001c;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0012, code lost:
        r2 = r5.arrayGoals;
        r3 = r1 + 1;
        r2[r1] = r2[r3];
        r1 = r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void removeGoal(androidx.constraintlayout.solver.SolverVariable r6) {
        /*
            r5 = this;
            r0 = 0
            r1 = r0
        L_0x0002:
            int r2 = r5.numGoals
            if (r1 >= r2) goto L_0x0026
            androidx.constraintlayout.solver.SolverVariable[] r2 = r5.arrayGoals
            r2 = r2[r1]
            if (r2 != r6) goto L_0x0023
        L_0x000c:
            int r2 = r5.numGoals
            int r3 = r2 + -1
            if (r1 >= r3) goto L_0x001c
            androidx.constraintlayout.solver.SolverVariable[] r2 = r5.arrayGoals
            int r3 = r1 + 1
            r4 = r2[r3]
            r2[r1] = r4
            r1 = r3
            goto L_0x000c
        L_0x001c:
            int r2 = r2 + -1
            r5.numGoals = r2
            r6.inGoal = r0
            return
        L_0x0023:
            int r1 = r1 + 1
            goto L_0x0002
        L_0x0026:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.OptimizedPriorityGoalRow.removeGoal(androidx.constraintlayout.solver.SolverVariable):void");
    }

    @Override // androidx.constraintlayout.solver.ArrayRow
    public void updateFromRow(ArrayRow arrayRow, boolean z) {
        SolverVariable solverVariable = arrayRow.variable;
        if (solverVariable != null) {
            int head = arrayRow.variables.getHead();
            int currentSize = arrayRow.variables.getCurrentSize();
            while (head != -1 && currentSize > 0) {
                int id = arrayRow.variables.getId(head);
                float value = arrayRow.variables.getValue(head);
                SolverVariable solverVariable2 = this.mCache.mIndexedVariables[id];
                this.accessor.init(solverVariable2);
                if (this.accessor.addToGoal(solverVariable, value)) {
                    addToGoal(solverVariable2);
                }
                this.constantValue += arrayRow.constantValue * value;
                head = arrayRow.variables.getNextIndice(head);
            }
            removeGoal(solverVariable);
        }
    }

    @Override // androidx.constraintlayout.solver.ArrayRow
    public String toString() {
        String str = " goal -> (" + this.constantValue + ") : ";
        for (int i = 0; i < this.numGoals; i++) {
            this.accessor.init(this.arrayGoals[i]);
            str = str + this.accessor + " ";
        }
        return str;
    }
}
