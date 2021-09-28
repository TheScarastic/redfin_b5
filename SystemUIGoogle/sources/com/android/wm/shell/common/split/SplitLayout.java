package com.android.wm.shell.common.split;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.SurfaceControl;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.policy.DividerSnapAlgorithm;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.split.SplitWindowManager;
/* loaded from: classes2.dex */
public final class SplitLayout {
    private Context mContext;
    private final DisplayImeController mDisplayImeController;
    private int mDividePosition;
    private final int mDividerInsets;
    private final int mDividerSize;
    private DividerSnapAlgorithm mDividerSnapAlgorithm;
    private final int mDividerWindowWidth;
    private final ImePositionProcessor mImePositionProcessor;
    private final Rect mRootBounds;
    private final SplitLayoutHandler mSplitLayoutHandler;
    private final SplitWindowManager mSplitWindowManager;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final Rect mDividerBounds = new Rect();
    private final Rect mBounds1 = new Rect();
    private final Rect mBounds2 = new Rect();
    private boolean mInitialized = false;

    /* loaded from: classes2.dex */
    public interface SplitLayoutHandler {
        int getSplitItemPosition(WindowContainerToken windowContainerToken);

        void onBoundsChanged(SplitLayout splitLayout);

        void onBoundsChanging(SplitLayout splitLayout);

        default void onDoubleTappedDivider() {
        }

        void onSnappedToDismiss(boolean z);
    }

    public SplitLayout(String str, Context context, Configuration configuration, SplitLayoutHandler splitLayoutHandler, SplitWindowManager.ParentContainerCallbacks parentContainerCallbacks, DisplayImeController displayImeController, ShellTaskOrganizer shellTaskOrganizer) {
        Rect rect = new Rect();
        this.mRootBounds = rect;
        this.mContext = context.createConfigurationContext(configuration);
        this.mSplitLayoutHandler = splitLayoutHandler;
        this.mDisplayImeController = displayImeController;
        this.mSplitWindowManager = new SplitWindowManager(str, this.mContext, configuration, parentContainerCallbacks);
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mImePositionProcessor = new ImePositionProcessor(this.mContext.getDisplayId());
        Resources resources = context.getResources();
        int dimensionPixelSize = resources.getDimensionPixelSize(17105198);
        this.mDividerWindowWidth = dimensionPixelSize;
        int dimensionPixelSize2 = resources.getDimensionPixelSize(17105197);
        this.mDividerInsets = dimensionPixelSize2;
        this.mDividerSize = dimensionPixelSize - (dimensionPixelSize2 * 2);
        rect.set(configuration.windowConfiguration.getBounds());
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, rect);
        resetDividerPosition();
    }

    public Rect getBounds1() {
        return new Rect(this.mBounds1);
    }

    public Rect getBounds2() {
        return new Rect(this.mBounds2);
    }

    public Rect getDividerBounds() {
        return new Rect(this.mDividerBounds);
    }

    public SurfaceControl getDividerLeash() {
        SplitWindowManager splitWindowManager = this.mSplitWindowManager;
        if (splitWindowManager == null) {
            return null;
        }
        return splitWindowManager.getSurfaceControl();
    }

    /* access modifiers changed from: package-private */
    public int getDividePosition() {
        return this.mDividePosition;
    }

    public boolean updateConfiguration(Configuration configuration) {
        Rect bounds = configuration.windowConfiguration.getBounds();
        if (this.mRootBounds.equals(bounds)) {
            return false;
        }
        this.mContext = this.mContext.createConfigurationContext(configuration);
        this.mSplitWindowManager.setConfiguration(configuration);
        this.mRootBounds.set(bounds);
        this.mDividerSnapAlgorithm = getSnapAlgorithm(this.mContext, this.mRootBounds);
        resetDividerPosition();
        if (!this.mInitialized) {
            return false;
        }
        release();
        init();
        return true;
    }

    private void updateBounds(int i) {
        this.mDividerBounds.set(this.mRootBounds);
        this.mBounds1.set(this.mRootBounds);
        this.mBounds2.set(this.mRootBounds);
        if (isLandscape(this.mRootBounds)) {
            int i2 = i + this.mRootBounds.left;
            Rect rect = this.mDividerBounds;
            int i3 = i2 - this.mDividerInsets;
            rect.left = i3;
            rect.right = i3 + this.mDividerWindowWidth;
            this.mBounds1.right = i2;
            this.mBounds2.left = i2 + this.mDividerSize;
            return;
        }
        int i4 = i + this.mRootBounds.top;
        Rect rect2 = this.mDividerBounds;
        int i5 = i4 - this.mDividerInsets;
        rect2.top = i5;
        rect2.bottom = i5 + this.mDividerWindowWidth;
        this.mBounds1.bottom = i4;
        this.mBounds2.top = i4 + this.mDividerSize;
    }

    public void init() {
        if (!this.mInitialized) {
            this.mInitialized = true;
            this.mSplitWindowManager.init(this);
            this.mDisplayImeController.addPositionProcessor(this.mImePositionProcessor);
        }
    }

    public void release() {
        if (this.mInitialized) {
            this.mInitialized = false;
            this.mSplitWindowManager.release();
            this.mDisplayImeController.removePositionProcessor(this.mImePositionProcessor);
            this.mImePositionProcessor.reset();
        }
    }

    /* access modifiers changed from: package-private */
    public void updateDivideBounds(int i) {
        updateBounds(i);
        this.mSplitWindowManager.setResizingSplits(true);
        this.mSplitLayoutHandler.onBoundsChanging(this);
    }

    void setDividePosition(int i) {
        this.mDividePosition = i;
        updateBounds(i);
        this.mSplitLayoutHandler.onBoundsChanged(this);
        this.mSplitWindowManager.setResizingSplits(false);
    }

    public void resetDividerPosition() {
        int i = this.mDividerSnapAlgorithm.getMiddleTarget().position;
        this.mDividePosition = i;
        updateBounds(i);
    }

    public void snapToTarget(int i, DividerSnapAlgorithm.SnapTarget snapTarget) {
        int i2 = snapTarget.flag;
        if (i2 == 1) {
            this.mSplitLayoutHandler.onSnappedToDismiss(false);
            this.mSplitWindowManager.setResizingSplits(false);
        } else if (i2 != 2) {
            flingDividePosition(i, snapTarget.position);
        } else {
            this.mSplitLayoutHandler.onSnappedToDismiss(true);
            this.mSplitWindowManager.setResizingSplits(false);
        }
    }

    /* access modifiers changed from: package-private */
    public void onDoubleTappedDivider() {
        this.mSplitLayoutHandler.onDoubleTappedDivider();
    }

    public DividerSnapAlgorithm.SnapTarget findSnapTarget(int i, float f, boolean z) {
        return this.mDividerSnapAlgorithm.calculateSnapTarget(i, f, z);
    }

    private DividerSnapAlgorithm getSnapAlgorithm(Context context, Rect rect) {
        boolean isLandscape = isLandscape(rect);
        return new DividerSnapAlgorithm(context.getResources(), rect.width(), rect.height(), this.mDividerSize, !isLandscape, getDisplayInsets(context), isLandscape ? 1 : 2);
    }

    private void flingDividePosition(int i, final int i2) {
        if (i != i2) {
            ValueAnimator duration = ValueAnimator.ofInt(i, i2).setDuration(250L);
            duration.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            duration.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.common.split.SplitLayout$$ExternalSyntheticLambda0
                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    SplitLayout.this.lambda$flingDividePosition$0(valueAnimator);
                }
            });
            duration.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.common.split.SplitLayout.1
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    SplitLayout.this.setDividePosition(i2);
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationCancel(Animator animator) {
                    SplitLayout.this.setDividePosition(i2);
                }
            });
            duration.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$flingDividePosition$0(ValueAnimator valueAnimator) {
        updateDivideBounds(((Integer) valueAnimator.getAnimatedValue()).intValue());
    }

    private static Rect getDisplayInsets(Context context) {
        return ((WindowManager) context.getSystemService(WindowManager.class)).getMaximumWindowMetrics().getWindowInsets().getInsets(WindowInsets.Type.navigationBars() | WindowInsets.Type.statusBars() | WindowInsets.Type.displayCutout()).toRect();
    }

    /* access modifiers changed from: private */
    public static boolean isLandscape(Rect rect) {
        return rect.width() > rect.height();
    }

    public void applySurfaceChanges(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2, SurfaceControl surfaceControl3, SurfaceControl surfaceControl4) {
        Rect adjustForIme = this.mImePositionProcessor.adjustForIme(this.mDividerBounds);
        Rect adjustForIme2 = this.mImePositionProcessor.adjustForIme(this.mBounds1);
        Rect adjustForIme3 = this.mImePositionProcessor.adjustForIme(this.mBounds2);
        SurfaceControl dividerLeash = getDividerLeash();
        if (dividerLeash != null) {
            transaction.setPosition(dividerLeash, (float) adjustForIme.left, (float) adjustForIme.top).setLayer(dividerLeash, Integer.MAX_VALUE);
        }
        transaction.setPosition(surfaceControl, (float) adjustForIme2.left, (float) adjustForIme2.top).setWindowCrop(surfaceControl, adjustForIme2.width(), adjustForIme2.height());
        transaction.setPosition(surfaceControl2, (float) adjustForIme3.left, (float) adjustForIme3.top).setWindowCrop(surfaceControl2, adjustForIme3.width(), adjustForIme3.height());
        this.mImePositionProcessor.applySurfaceDimValues(transaction, surfaceControl3, surfaceControl4);
    }

    public void applyTaskChanges(WindowContainerTransaction windowContainerTransaction, ActivityManager.RunningTaskInfo runningTaskInfo, ActivityManager.RunningTaskInfo runningTaskInfo2) {
        windowContainerTransaction.setBounds(runningTaskInfo.token, this.mImePositionProcessor.adjustForIme(this.mBounds1)).setBounds(runningTaskInfo2.token, this.mImePositionProcessor.adjustForIme(this.mBounds2));
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ImePositionProcessor implements DisplayImeController.ImePositionProcessor {
        private float mDimValue1;
        private float mDimValue2;
        private final int mDisplayId;
        private int mEndImeTop;
        private boolean mImeShown;
        private float mLastDim1;
        private float mLastDim2;
        private int mLastYOffset;
        private int mStartImeTop;
        private float mTargetDim1;
        private float mTargetDim2;
        private int mTargetYOffset;
        private int mYOffsetForIme;

        private float getProgressValue(float f, float f2, float f3) {
            return f + ((f2 - f) * f3);
        }

        private ImePositionProcessor(int i) {
            this.mDisplayId = i;
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
            boolean z3 = false;
            if (i != this.mDisplayId) {
                return 0;
            }
            int imeTargetPosition = getImeTargetPosition();
            if (!SplitLayout.this.mInitialized || imeTargetPosition == -1) {
                return 0;
            }
            this.mStartImeTop = z ? i2 : i3;
            if (z) {
                i2 = i3;
            }
            this.mEndImeTop = i2;
            this.mImeShown = z;
            this.mLastDim1 = this.mDimValue1;
            float f = 0.3f;
            this.mTargetDim1 = (imeTargetPosition != 1 || !z) ? 0.0f : 0.3f;
            this.mLastDim2 = this.mDimValue2;
            if (imeTargetPosition != 0 || !z) {
                f = 0.0f;
            }
            this.mTargetDim2 = f;
            this.mLastYOffset = this.mYOffsetForIme;
            int i4 = (imeTargetPosition != 1 || z2 || SplitLayout.isLandscape(SplitLayout.this.mRootBounds) || !z) ? 0 : 1;
            this.mTargetYOffset = i4 != 0 ? getTargetYOffset() : 0;
            SplitWindowManager splitWindowManager = SplitLayout.this.mSplitWindowManager;
            if (!z || imeTargetPosition == -1) {
                z3 = true;
            }
            splitWindowManager.setInteractive(z3);
            return i4;
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
            if (i == this.mDisplayId) {
                onProgress(getProgress(i2));
                SplitLayout.this.mSplitLayoutHandler.onBoundsChanging(SplitLayout.this);
            }
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
            if (i == this.mDisplayId && !z) {
                onProgress(1.0f);
                SplitLayout.this.mSplitLayoutHandler.onBoundsChanging(SplitLayout.this);
            }
        }

        @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
        public void onImeControlTargetChanged(int i, boolean z) {
            if (i == this.mDisplayId && !z && this.mImeShown) {
                reset();
                SplitLayout.this.mSplitWindowManager.setInteractive(true);
                SplitLayout.this.mSplitLayoutHandler.onBoundsChanging(SplitLayout.this);
            }
        }

        private int getTargetYOffset() {
            return -Math.min(Math.abs(this.mEndImeTop - this.mStartImeTop), (int) (((float) SplitLayout.this.mBounds1.height()) * 0.7f));
        }

        private int getImeTargetPosition() {
            return SplitLayout.this.mSplitLayoutHandler.getSplitItemPosition(SplitLayout.this.mTaskOrganizer.getImeTarget(this.mDisplayId));
        }

        private float getProgress(int i) {
            int i2 = this.mStartImeTop;
            return (((float) i) - ((float) i2)) / ((float) (this.mEndImeTop - i2));
        }

        private void onProgress(float f) {
            this.mDimValue1 = getProgressValue(this.mLastDim1, this.mTargetDim1, f);
            this.mDimValue2 = getProgressValue(this.mLastDim2, this.mTargetDim2, f);
            this.mYOffsetForIme = (int) getProgressValue((float) this.mLastYOffset, (float) this.mTargetYOffset, f);
        }

        /* access modifiers changed from: private */
        public void reset() {
            this.mImeShown = false;
            this.mTargetYOffset = 0;
            this.mLastYOffset = 0;
            this.mYOffsetForIme = 0;
            this.mTargetDim1 = 0.0f;
            this.mLastDim1 = 0.0f;
            this.mDimValue1 = 0.0f;
            this.mTargetDim2 = 0.0f;
            this.mLastDim2 = 0.0f;
            this.mDimValue2 = 0.0f;
        }

        /* access modifiers changed from: private */
        public Rect adjustForIme(Rect rect) {
            Rect rect2 = new Rect(rect);
            int i = this.mYOffsetForIme;
            if (i != 0) {
                rect2.offset(0, i);
            }
            return rect2;
        }

        /* access modifiers changed from: private */
        public void applySurfaceDimValues(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, SurfaceControl surfaceControl2) {
            boolean z = true;
            transaction.setAlpha(surfaceControl, this.mDimValue1).setVisibility(surfaceControl, this.mDimValue1 > 0.001f);
            SurfaceControl.Transaction alpha = transaction.setAlpha(surfaceControl2, this.mDimValue2);
            if (this.mDimValue2 <= 0.001f) {
                z = false;
            }
            alpha.setVisibility(surfaceControl2, z);
        }
    }
}
