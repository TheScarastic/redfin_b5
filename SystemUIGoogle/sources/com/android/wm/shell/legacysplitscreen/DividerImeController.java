package com.android.wm.shell.legacysplitscreen;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.view.Choreographer;
import android.view.SurfaceControl;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
/* loaded from: classes2.dex */
public class DividerImeController implements DisplayImeController.ImePositionProcessor {
    private final ShellExecutor mMainExecutor;
    private final LegacySplitScreenTaskListener mSplits;
    private final TaskOrganizer mTaskOrganizer;
    private final TransactionPool mTransactionPool;
    private int mHiddenTop = 0;
    private int mShownTop = 0;
    private boolean mTargetAdjusted = false;
    private boolean mTargetShown = false;
    private float mTargetPrimaryDim = 0.0f;
    private float mTargetSecondaryDim = 0.0f;
    private boolean mSecondaryHasFocus = false;
    private float mLastPrimaryDim = 0.0f;
    private float mLastSecondaryDim = 0.0f;
    private int mLastAdjustTop = -1;
    private boolean mImeWasShown = false;
    private boolean mAdjusted = false;
    private ValueAnimator mAnimation = null;
    private boolean mPaused = true;
    private boolean mPausedTargetAdjusted = false;

    public DividerImeController(LegacySplitScreenTaskListener legacySplitScreenTaskListener, TransactionPool transactionPool, ShellExecutor shellExecutor, TaskOrganizer taskOrganizer) {
        this.mSplits = legacySplitScreenTaskListener;
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
        this.mTaskOrganizer = taskOrganizer;
    }

    private DividerView getView() {
        return this.mSplits.mSplitScreenController.getDividerView();
    }

    private LegacySplitDisplayLayout getLayout() {
        return this.mSplits.mSplitScreenController.getSplitLayout();
    }

    private boolean isDividerHidden() {
        DividerView dividerView = this.mSplits.mSplitScreenController.getDividerView();
        return dividerView == null || dividerView.isHidden();
    }

    private boolean getSecondaryHasFocus(int i) {
        WindowContainerToken imeTarget = this.mTaskOrganizer.getImeTarget(i);
        return imeTarget != null && imeTarget.asBinder() == this.mSplits.mSecondary.token.asBinder();
    }

    public void reset() {
        this.mPaused = true;
        this.mPausedTargetAdjusted = false;
        this.mAnimation = null;
        this.mTargetAdjusted = false;
        this.mAdjusted = false;
        this.mTargetShown = false;
        this.mImeWasShown = false;
        this.mLastSecondaryDim = 0.0f;
        this.mLastPrimaryDim = 0.0f;
        this.mTargetSecondaryDim = 0.0f;
        this.mTargetPrimaryDim = 0.0f;
        this.mSecondaryHasFocus = false;
        this.mLastAdjustTop = -1;
    }

    private void updateDimTargets() {
        boolean z = !getView().isHidden();
        boolean z2 = this.mSecondaryHasFocus;
        float f = 0.3f;
        this.mTargetPrimaryDim = (!z2 || !this.mTargetShown || !z) ? 0.0f : 0.3f;
        if (z2 || !this.mTargetShown || !z) {
            f = 0.0f;
        }
        this.mTargetSecondaryDim = f;
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public void onImeControlTargetChanged(int i, boolean z) {
        if (!z && this.mTargetShown) {
            this.mPaused = false;
            this.mTargetShown = false;
            this.mTargetAdjusted = false;
            this.mTargetSecondaryDim = 0.0f;
            this.mTargetPrimaryDim = 0.0f;
            updateImeAdjustState(true);
            startAsyncAnimation();
        }
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public int onImeStartPositioning(int i, int i2, int i3, boolean z, boolean z2, SurfaceControl.Transaction transaction) {
        if (isDividerHidden()) {
            return 0;
        }
        this.mHiddenTop = i2;
        this.mShownTop = i3;
        this.mTargetShown = z;
        boolean secondaryHasFocus = getSecondaryHasFocus(i);
        this.mSecondaryHasFocus = secondaryHasFocus;
        boolean z3 = z && secondaryHasFocus && !z2 && !getLayout().mDisplayLayout.isLandscape() && !this.mSplits.mSplitScreenController.isMinimized();
        int i4 = this.mLastAdjustTop;
        if (i4 < 0) {
            if (!z) {
                i2 = i3;
            }
            this.mLastAdjustTop = i2;
        } else {
            if (i4 != (z ? this.mShownTop : this.mHiddenTop)) {
                boolean z4 = this.mTargetAdjusted;
                if (z4 != z3 && z3 == this.mAdjusted) {
                    this.mAdjusted = z4;
                } else if (z3 && z4 && this.mAdjusted) {
                    this.mAdjusted = false;
                }
            }
        }
        if (this.mPaused) {
            this.mPausedTargetAdjusted = z3;
            if (z3 || this.mAdjusted) {
                return 1;
            }
            return 0;
        }
        this.mTargetAdjusted = z3;
        updateDimTargets();
        if (this.mAnimation != null || (this.mImeWasShown && z && this.mTargetAdjusted != this.mAdjusted)) {
            startAsyncAnimation();
        }
        updateImeAdjustState();
        if (this.mTargetAdjusted || this.mAdjusted) {
            return 1;
        }
        return 0;
    }

    private void updateImeAdjustState() {
        updateImeAdjustState(false);
    }

    private void updateImeAdjustState(boolean z) {
        DividerView view;
        boolean z2 = false;
        if (this.mAdjusted != this.mTargetAdjusted || z) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            LegacySplitDisplayLayout layout = getLayout();
            if (this.mTargetAdjusted) {
                int i = this.mShownTop;
                layout.updateAdjustedBounds(i, this.mHiddenTop, i);
                windowContainerTransaction.setBounds(this.mSplits.mSecondary.token, layout.mAdjustedSecondary);
                Rect rect = new Rect(this.mSplits.mSecondary.configuration.windowConfiguration.getAppBounds());
                rect.offset(0, layout.mAdjustedSecondary.top - layout.mSecondary.top);
                windowContainerTransaction.setAppBounds(this.mSplits.mSecondary.token, rect);
                ActivityManager.RunningTaskInfo runningTaskInfo = this.mSplits.mSecondary;
                WindowContainerToken windowContainerToken = runningTaskInfo.token;
                Configuration configuration = runningTaskInfo.configuration;
                windowContainerTransaction.setScreenSizeDp(windowContainerToken, configuration.screenWidthDp, configuration.screenHeightDp);
                windowContainerTransaction.setBounds(this.mSplits.mPrimary.token, layout.mAdjustedPrimary);
                Rect rect2 = new Rect(this.mSplits.mPrimary.configuration.windowConfiguration.getAppBounds());
                rect2.offset(0, layout.mAdjustedPrimary.top - layout.mPrimary.top);
                windowContainerTransaction.setAppBounds(this.mSplits.mPrimary.token, rect2);
                ActivityManager.RunningTaskInfo runningTaskInfo2 = this.mSplits.mPrimary;
                WindowContainerToken windowContainerToken2 = runningTaskInfo2.token;
                Configuration configuration2 = runningTaskInfo2.configuration;
                windowContainerTransaction.setScreenSizeDp(windowContainerToken2, configuration2.screenWidthDp, configuration2.screenHeightDp);
            } else {
                windowContainerTransaction.setBounds(this.mSplits.mSecondary.token, layout.mSecondary);
                windowContainerTransaction.setAppBounds(this.mSplits.mSecondary.token, (Rect) null);
                windowContainerTransaction.setScreenSizeDp(this.mSplits.mSecondary.token, 0, 0);
                windowContainerTransaction.setBounds(this.mSplits.mPrimary.token, layout.mPrimary);
                windowContainerTransaction.setAppBounds(this.mSplits.mPrimary.token, (Rect) null);
                windowContainerTransaction.setScreenSizeDp(this.mSplits.mPrimary.token, 0, 0);
            }
            if (!this.mSplits.mSplitScreenController.getWmProxy().queueSyncTransactionIfWaiting(windowContainerTransaction)) {
                this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            }
        }
        if (!this.mPaused && (view = getView()) != null) {
            boolean z3 = this.mTargetShown;
            view.setAdjustedForIme(z3, z3 ? 275 : 340);
        }
        LegacySplitScreenController legacySplitScreenController = this.mSplits.mSplitScreenController;
        if (this.mTargetShown && !this.mPaused) {
            z2 = true;
        }
        legacySplitScreenController.setAdjustedForIme(z2);
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public void onImePositionChanged(int i, int i2, SurfaceControl.Transaction transaction) {
        if (this.mAnimation == null && !isDividerHidden() && !this.mPaused) {
            float f = (float) i2;
            int i3 = this.mHiddenTop;
            float f2 = (f - ((float) i3)) / ((float) (this.mShownTop - i3));
            if (!this.mTargetShown) {
                f2 = 1.0f - f2;
            }
            onProgress(f2, transaction);
        }
    }

    @Override // com.android.wm.shell.common.DisplayImeController.ImePositionProcessor
    public void onImeEndPositioning(int i, boolean z, SurfaceControl.Transaction transaction) {
        if (this.mAnimation == null && !isDividerHidden() && !this.mPaused) {
            onEnd(z, transaction);
        }
    }

    private void onProgress(float f, SurfaceControl.Transaction transaction) {
        DividerView view = getView();
        if (this.mTargetAdjusted != this.mAdjusted && !this.mPaused) {
            LegacySplitDisplayLayout layout = getLayout();
            float f2 = this.mTargetAdjusted ? f : 1.0f - f;
            int i = this.mShownTop;
            int i2 = this.mHiddenTop;
            int i3 = (int) ((((float) i) * f2) + ((1.0f - f2) * ((float) i2)));
            this.mLastAdjustTop = i3;
            layout.updateAdjustedBounds(i3, i2, i);
            view.resizeSplitSurfaces(transaction, layout.mAdjustedPrimary, layout.mAdjustedSecondary);
        }
        float f3 = 1.0f - f;
        view.setResizeDimLayer(transaction, true, (this.mLastPrimaryDim * f3) + (this.mTargetPrimaryDim * f));
        view.setResizeDimLayer(transaction, false, (this.mLastSecondaryDim * f3) + (f * this.mTargetSecondaryDim));
    }

    public void setDimsHidden(SurfaceControl.Transaction transaction, boolean z) {
        DividerView view = getView();
        if (z) {
            view.setResizeDimLayer(transaction, true, 0.0f);
            view.setResizeDimLayer(transaction, false, 0.0f);
            return;
        }
        updateDimTargets();
        view.setResizeDimLayer(transaction, true, this.mTargetPrimaryDim);
        view.setResizeDimLayer(transaction, false, this.mTargetSecondaryDim);
    }

    public void onEnd(boolean z, SurfaceControl.Transaction transaction) {
        if (!z) {
            onProgress(1.0f, transaction);
            boolean z2 = this.mTargetAdjusted;
            this.mAdjusted = z2;
            this.mImeWasShown = this.mTargetShown;
            this.mLastAdjustTop = z2 ? this.mShownTop : this.mHiddenTop;
            this.mLastPrimaryDim = this.mTargetPrimaryDim;
            this.mLastSecondaryDim = this.mTargetSecondaryDim;
        }
    }

    private void startAsyncAnimation() {
        ValueAnimator valueAnimator = this.mAnimation;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        this.mAnimation = ofFloat;
        ofFloat.setDuration(275L);
        boolean z = this.mTargetAdjusted;
        if (z != this.mAdjusted) {
            int i = this.mHiddenTop;
            float f = (((float) this.mLastAdjustTop) - ((float) i)) / ((float) (this.mShownTop - i));
            if (!z) {
                f = 1.0f - f;
            }
            this.mAnimation.setCurrentFraction(f);
        }
        this.mAnimation.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.android.wm.shell.legacysplitscreen.DividerImeController$$ExternalSyntheticLambda0
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                DividerImeController.m531$r8$lambda$0zoGRSniJriOBtdW3MhyX3lZ_A(DividerImeController.this, valueAnimator2);
            }
        });
        this.mAnimation.setInterpolator(DisplayImeController.INTERPOLATOR);
        this.mAnimation.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.legacysplitscreen.DividerImeController.1
            private boolean mCancel = false;

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationCancel(Animator animator) {
                this.mCancel = true;
            }

            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                SurfaceControl.Transaction acquire = DividerImeController.this.mTransactionPool.acquire();
                DividerImeController.this.onEnd(this.mCancel, acquire);
                acquire.apply();
                DividerImeController.this.mTransactionPool.release(acquire);
                DividerImeController.this.mAnimation = null;
            }
        });
        this.mAnimation.start();
    }

    public /* synthetic */ void lambda$startAsyncAnimation$0(ValueAnimator valueAnimator) {
        SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
        onProgress(((Float) valueAnimator.getAnimatedValue()).floatValue(), acquire);
        acquire.setFrameTimelineVsync(Choreographer.getSfInstance().getVsyncId());
        acquire.apply();
        this.mTransactionPool.release(acquire);
    }

    public void pause(int i) {
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.DividerImeController$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                DividerImeController.$r8$lambda$FPsLA1QfnMwsoU3OMou_1_JX_i0(DividerImeController.this);
            }
        });
    }

    public /* synthetic */ void lambda$pause$1() {
        if (!this.mPaused) {
            this.mPaused = true;
            this.mPausedTargetAdjusted = this.mTargetAdjusted;
            this.mTargetAdjusted = false;
            this.mTargetSecondaryDim = 0.0f;
            this.mTargetPrimaryDim = 0.0f;
            updateImeAdjustState();
            startAsyncAnimation();
            ValueAnimator valueAnimator = this.mAnimation;
            if (valueAnimator != null) {
                valueAnimator.end();
            }
        }
    }

    public void resume(int i) {
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.DividerImeController$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                DividerImeController.$r8$lambda$kH8bS5ohAqD7CQ76DQaDBtSUdEY(DividerImeController.this);
            }
        });
    }

    public /* synthetic */ void lambda$resume$2() {
        if (this.mPaused) {
            this.mPaused = false;
            this.mTargetAdjusted = this.mPausedTargetAdjusted;
            updateDimTargets();
            DividerView view = getView();
            if (!(this.mTargetAdjusted == this.mAdjusted || this.mSplits.mSplitScreenController.isMinimized() || view == null)) {
                view.finishAnimations();
            }
            updateImeAdjustState();
            startAsyncAnimation();
        }
    }
}
