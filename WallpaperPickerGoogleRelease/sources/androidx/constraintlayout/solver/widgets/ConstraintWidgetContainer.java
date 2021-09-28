package androidx.constraintlayout.solver.widgets;

import androidx.constraintlayout.solver.LinearSystem;
import androidx.constraintlayout.solver.widgets.analyzer.BasicMeasure;
import androidx.constraintlayout.solver.widgets.analyzer.ChainRun;
import androidx.constraintlayout.solver.widgets.analyzer.DependencyGraph;
import androidx.constraintlayout.solver.widgets.analyzer.WidgetRun;
import java.util.Arrays;
import java.util.Iterator;
/* loaded from: classes.dex */
public class ConstraintWidgetContainer extends WidgetContainer {
    public int mPaddingLeft;
    public int mPaddingTop;
    public BasicMeasure mBasicMeasureSolver = new BasicMeasure(this);
    public DependencyGraph mDependencyGraph = new DependencyGraph(this);
    public BasicMeasure.Measurer mMeasurer = null;
    public boolean mIsRtl = false;
    public LinearSystem mSystem = new LinearSystem();
    public int mHorizontalChainsSize = 0;
    public int mVerticalChainsSize = 0;
    public ChainHead[] mVerticalChainsArray = new ChainHead[4];
    public ChainHead[] mHorizontalChainsArray = new ChainHead[4];
    public int mOptimizationLevel = 7;
    public boolean mWidthMeasuredTooSmall = false;
    public boolean mHeightMeasuredTooSmall = false;

    public void addChain(ConstraintWidget constraintWidget, int i) {
        if (i == 0) {
            int i2 = this.mHorizontalChainsSize + 1;
            ChainHead[] chainHeadArr = this.mHorizontalChainsArray;
            if (i2 >= chainHeadArr.length) {
                this.mHorizontalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr, chainHeadArr.length * 2);
            }
            ChainHead[] chainHeadArr2 = this.mHorizontalChainsArray;
            int i3 = this.mHorizontalChainsSize;
            chainHeadArr2[i3] = new ChainHead(constraintWidget, 0, this.mIsRtl);
            this.mHorizontalChainsSize = i3 + 1;
        } else if (i == 1) {
            int i4 = this.mVerticalChainsSize + 1;
            ChainHead[] chainHeadArr3 = this.mVerticalChainsArray;
            if (i4 >= chainHeadArr3.length) {
                this.mVerticalChainsArray = (ChainHead[]) Arrays.copyOf(chainHeadArr3, chainHeadArr3.length * 2);
            }
            ChainHead[] chainHeadArr4 = this.mVerticalChainsArray;
            int i5 = this.mVerticalChainsSize;
            chainHeadArr4[i5] = new ChainHead(constraintWidget, 1, this.mIsRtl);
            this.mVerticalChainsSize = i5 + 1;
        }
    }

    public boolean addChildrenToSolver(LinearSystem linearSystem) {
        addToSolver(linearSystem);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            ConstraintWidget constraintWidget = this.mChildren.get(i);
            if ((constraintWidget instanceof VirtualLayout) || (constraintWidget instanceof Guideline)) {
                constraintWidget.addToSolver(linearSystem);
            }
        }
        for (int i2 = 0; i2 < size; i2++) {
            ConstraintWidget constraintWidget2 = this.mChildren.get(i2);
            if (constraintWidget2 instanceof ConstraintWidgetContainer) {
                int[] iArr = constraintWidget2.mListDimensionBehaviors;
                int i3 = iArr[0];
                int i4 = iArr[1];
                if (i3 == 2) {
                    iArr[0] = 1;
                }
                if (i4 == 2) {
                    iArr[1] = 1;
                }
                constraintWidget2.addToSolver(linearSystem);
                if (i3 == 2) {
                    constraintWidget2.setHorizontalDimensionBehaviour$enumunboxing$(i3);
                }
                if (i4 == 2) {
                    constraintWidget2.setVerticalDimensionBehaviour$enumunboxing$(i4);
                }
            } else {
                constraintWidget2.mHorizontalResolution = -1;
                constraintWidget2.mVerticalResolution = -1;
                if (this.mListDimensionBehaviors[0] != 2 && constraintWidget2.mListDimensionBehaviors[0] == 4) {
                    int i5 = constraintWidget2.mLeft.mMargin;
                    int width = getWidth() - constraintWidget2.mRight.mMargin;
                    ConstraintAnchor constraintAnchor = constraintWidget2.mLeft;
                    constraintAnchor.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor);
                    ConstraintAnchor constraintAnchor2 = constraintWidget2.mRight;
                    constraintAnchor2.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor2);
                    linearSystem.addEquality(constraintWidget2.mLeft.mSolverVariable, i5);
                    linearSystem.addEquality(constraintWidget2.mRight.mSolverVariable, width);
                    constraintWidget2.mHorizontalResolution = 2;
                    constraintWidget2.mX = i5;
                    int i6 = width - i5;
                    constraintWidget2.mWidth = i6;
                    int i7 = constraintWidget2.mMinWidth;
                    if (i6 < i7) {
                        constraintWidget2.mWidth = i7;
                    }
                }
                if (this.mListDimensionBehaviors[1] != 2 && constraintWidget2.mListDimensionBehaviors[1] == 4) {
                    int i8 = constraintWidget2.mTop.mMargin;
                    int height = getHeight() - constraintWidget2.mBottom.mMargin;
                    ConstraintAnchor constraintAnchor3 = constraintWidget2.mTop;
                    constraintAnchor3.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor3);
                    ConstraintAnchor constraintAnchor4 = constraintWidget2.mBottom;
                    constraintAnchor4.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor4);
                    linearSystem.addEquality(constraintWidget2.mTop.mSolverVariable, i8);
                    linearSystem.addEquality(constraintWidget2.mBottom.mSolverVariable, height);
                    if (constraintWidget2.mBaselineDistance > 0 || constraintWidget2.mVisibility == 8) {
                        ConstraintAnchor constraintAnchor5 = constraintWidget2.mBaseline;
                        constraintAnchor5.mSolverVariable = linearSystem.createObjectVariable(constraintAnchor5);
                        linearSystem.addEquality(constraintWidget2.mBaseline.mSolverVariable, constraintWidget2.mBaselineDistance + i8);
                    }
                    constraintWidget2.mVerticalResolution = 2;
                    constraintWidget2.mY = i8;
                    int i9 = height - i8;
                    constraintWidget2.mHeight = i9;
                    int i10 = constraintWidget2.mMinHeight;
                    if (i9 < i10) {
                        constraintWidget2.mHeight = i10;
                    }
                }
                if (!(constraintWidget2 instanceof VirtualLayout) && !(constraintWidget2 instanceof Guideline)) {
                    constraintWidget2.addToSolver(linearSystem);
                }
            }
        }
        if (this.mHorizontalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 0);
        }
        if (this.mVerticalChainsSize > 0) {
            Chain.applyChainConstraints(this, linearSystem, 1);
        }
        return true;
    }

    public boolean directMeasureWithOrientation(boolean z, int i) {
        boolean z2;
        DependencyGraph dependencyGraph = this.mDependencyGraph;
        boolean z3 = true;
        boolean z4 = z & true;
        int dimensionBehaviour$enumunboxing$ = dependencyGraph.container.getDimensionBehaviour$enumunboxing$(0);
        int dimensionBehaviour$enumunboxing$2 = dependencyGraph.container.getDimensionBehaviour$enumunboxing$(1);
        int x = dependencyGraph.container.getX();
        int y = dependencyGraph.container.getY();
        if (z4 && (dimensionBehaviour$enumunboxing$ == 2 || dimensionBehaviour$enumunboxing$2 == 2)) {
            Iterator<WidgetRun> it = dependencyGraph.mRuns.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                WidgetRun next = it.next();
                if (next.orientation == i && !next.supportsWrapComputation()) {
                    z4 = false;
                    break;
                }
            }
            if (i == 0) {
                if (z4 && dimensionBehaviour$enumunboxing$ == 2) {
                    ConstraintWidgetContainer constraintWidgetContainer = dependencyGraph.container;
                    constraintWidgetContainer.mListDimensionBehaviors[0] = 1;
                    constraintWidgetContainer.setWidth(dependencyGraph.computeWrap(constraintWidgetContainer, 0));
                    ConstraintWidgetContainer constraintWidgetContainer2 = dependencyGraph.container;
                    constraintWidgetContainer2.horizontalRun.dimension.resolve(constraintWidgetContainer2.getWidth());
                }
            } else if (z4 && dimensionBehaviour$enumunboxing$2 == 2) {
                ConstraintWidgetContainer constraintWidgetContainer3 = dependencyGraph.container;
                constraintWidgetContainer3.mListDimensionBehaviors[1] = 1;
                constraintWidgetContainer3.setHeight(dependencyGraph.computeWrap(constraintWidgetContainer3, 1));
                ConstraintWidgetContainer constraintWidgetContainer4 = dependencyGraph.container;
                constraintWidgetContainer4.verticalRun.dimension.resolve(constraintWidgetContainer4.getHeight());
            }
        }
        if (i == 0) {
            ConstraintWidgetContainer constraintWidgetContainer5 = dependencyGraph.container;
            int[] iArr = constraintWidgetContainer5.mListDimensionBehaviors;
            if (iArr[0] == 1 || iArr[0] == 4) {
                int width = constraintWidgetContainer5.getWidth() + x;
                dependencyGraph.container.horizontalRun.end.resolve(width);
                dependencyGraph.container.horizontalRun.dimension.resolve(width - x);
                z2 = true;
            }
            z2 = false;
        } else {
            ConstraintWidgetContainer constraintWidgetContainer6 = dependencyGraph.container;
            int[] iArr2 = constraintWidgetContainer6.mListDimensionBehaviors;
            if (iArr2[1] == 1 || iArr2[1] == 4) {
                int height = constraintWidgetContainer6.getHeight() + y;
                dependencyGraph.container.verticalRun.end.resolve(height);
                dependencyGraph.container.verticalRun.dimension.resolve(height - y);
                z2 = true;
            }
            z2 = false;
        }
        dependencyGraph.measureWidgets();
        Iterator<WidgetRun> it2 = dependencyGraph.mRuns.iterator();
        while (it2.hasNext()) {
            WidgetRun next2 = it2.next();
            if (next2.orientation == i && (next2.widget != dependencyGraph.container || next2.resolved)) {
                next2.applyToWidget();
            }
        }
        Iterator<WidgetRun> it3 = dependencyGraph.mRuns.iterator();
        while (it3.hasNext()) {
            WidgetRun next3 = it3.next();
            if (next3.orientation == i && (z2 || next3.widget != dependencyGraph.container)) {
                if (!next3.start.resolved || !next3.end.resolved || (!(next3 instanceof ChainRun) && !next3.dimension.resolved)) {
                    z3 = false;
                    break;
                }
            }
        }
        dependencyGraph.container.setHorizontalDimensionBehaviour$enumunboxing$(dimensionBehaviour$enumunboxing$);
        dependencyGraph.container.setVerticalDimensionBehaviour$enumunboxing$(dimensionBehaviour$enumunboxing$2);
        return z3;
    }

    public void invalidateGraph() {
        this.mDependencyGraph.mNeedBuildGraph = true;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r0v20, resolved type: int[] */
    /* JADX DEBUG: Multi-variable search result rejected for r0v23, resolved type: int[] */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r2v6, types: [boolean] */
    /* JADX WARN: Type inference failed for: r2v8 */
    /* JADX WARN: Type inference failed for: r2v9 */
    /* JADX WARNING: Removed duplicated region for block: B:107:0x0219  */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x006b  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0084  */
    /* JADX WARNING: Removed duplicated region for block: B:57:0x00fb  */
    /* JADX WARNING: Removed duplicated region for block: B:60:0x011c  */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x01ab  */
    /* JADX WARNING: Removed duplicated region for block: B:85:0x01b8  */
    /* JADX WARNING: Removed duplicated region for block: B:88:0x01cb  */
    /* JADX WARNING: Removed duplicated region for block: B:89:0x01d5  */
    /* JADX WARNING: Removed duplicated region for block: B:91:0x01da  */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // androidx.constraintlayout.solver.widgets.WidgetContainer
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void layout() {
        /*
        // Method dump skipped, instructions count: 553
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.constraintlayout.solver.widgets.ConstraintWidgetContainer.layout():void");
    }

    @Override // androidx.constraintlayout.solver.widgets.WidgetContainer, androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void reset() {
        this.mSystem.reset();
        this.mPaddingLeft = 0;
        this.mPaddingTop = 0;
        super.reset();
    }

    @Override // androidx.constraintlayout.solver.widgets.ConstraintWidget
    public void updateFromRuns(boolean z, boolean z2) {
        super.updateFromRuns(z, z2);
        int size = this.mChildren.size();
        for (int i = 0; i < size; i++) {
            this.mChildren.get(i).updateFromRuns(z, z2);
        }
    }
}
