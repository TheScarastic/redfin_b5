package com.android.wm.shell.pip;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteException;
import android.os.SystemClock;
import android.util.Log;
import android.util.RotationUtils;
import android.view.SurfaceControl;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.animation.Interpolators;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.ScreenshotUtils;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreenController;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipSurfaceTransactionHelper;
import com.android.wm.shell.pip.PipUiEventLogger;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public class PipTaskOrganizer implements ShellTaskOrganizer.TaskListener, DisplayController.OnDisplaysChangedListener {
    private static final String TAG = "PipTaskOrganizer";
    private final Context mContext;
    private final int mCrossFadeAnimationDuration;
    private int mCurrentRotation;
    private SurfaceControl.Transaction mDeferredAnimEndTransaction;
    private ActivityManager.RunningTaskInfo mDeferredTaskInfo;
    private final int mEnterAnimationDuration;
    private final int mExitAnimationDuration;
    private boolean mHasFadeOut;
    private boolean mInSwipePipToHomeTransition;
    private long mLastOneShotAlphaAnimationTime;
    private SurfaceControl mLeash;
    protected final ShellExecutor mMainExecutor;
    private int mNextRotation;
    private IntConsumer mOnDisplayIdChangeCallback;
    private PictureInPictureParams mPictureInPictureParams;
    private final PipAnimationController mPipAnimationController;
    private final PipBoundsAlgorithm mPipBoundsAlgorithm;
    private final PipBoundsState mPipBoundsState;
    private final PipMenuController mPipMenuController;
    private final PipTransitionController mPipTransitionController;
    private final PipUiEventLogger mPipUiEventLoggerLogger;
    private final Optional<LegacySplitScreenController> mSplitScreenOptional;
    private final PipSurfaceTransactionHelper mSurfaceTransactionHelper;
    private SurfaceControl mSwipePipToHomeOverlay;
    private final SyncTransactionQueue mSyncTransactionQueue;
    private ActivityManager.RunningTaskInfo mTaskInfo;
    protected final ShellTaskOrganizer mTaskOrganizer;
    private WindowContainerToken mToken;
    private boolean mWaitForFixedRotation;
    private final PipAnimationController.PipAnimationCallback mPipAnimationCallback = new PipAnimationController.PipAnimationCallback() { // from class: com.android.wm.shell.pip.PipTaskOrganizer.1
        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public void onPipAnimationStart(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            PipTaskOrganizer.this.sendOnPipTransitionStarted(pipTransitionAnimator.getTransitionDirection());
        }

        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            int animationType = pipTransitionAnimator.getAnimationType();
            Rect destinationBounds = pipTransitionAnimator.getDestinationBounds();
            boolean z = true;
            if (PipAnimationController.isInPipDirection(transitionDirection) && pipTransitionAnimator.getContentOverlay() != null) {
                PipTaskOrganizer.this.fadeOutAndRemoveOverlay(pipTransitionAnimator.getContentOverlay(), new PipTaskOrganizer$$ExternalSyntheticLambda4(pipTransitionAnimator), true);
            }
            if (PipTaskOrganizer.this.mWaitForFixedRotation && animationType == 0 && transitionDirection == 2) {
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.scheduleFinishEnterPip(PipTaskOrganizer.this.mToken, destinationBounds);
                PipTaskOrganizer.this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
                PipTaskOrganizer.this.mSurfaceTransactionHelper.round(transaction, PipTaskOrganizer.this.mLeash, PipTaskOrganizer.this.isInPip());
                PipTaskOrganizer.this.mDeferredAnimEndTransaction = transaction;
                return;
            }
            if (!PipAnimationController.isOutPipDirection(transitionDirection) && !PipAnimationController.isRemovePipDirection(transitionDirection)) {
                z = false;
            }
            if (PipTaskOrganizer.this.mState != State.EXITING_PIP || z) {
                PipTaskOrganizer.this.finishResize(transaction, destinationBounds, transitionDirection, animationType);
                PipTaskOrganizer.this.sendOnPipTransitionFinished(transitionDirection);
            }
        }

        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public void onPipAnimationCancel(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            if (PipAnimationController.isInPipDirection(transitionDirection) && pipTransitionAnimator.getContentOverlay() != null) {
                PipTaskOrganizer.this.fadeOutAndRemoveOverlay(pipTransitionAnimator.getContentOverlay(), new PipTaskOrganizer$$ExternalSyntheticLambda4(pipTransitionAnimator), true);
            }
            PipTaskOrganizer.this.sendOnPipTransitionCancelled(transitionDirection);
        }
    };
    private final PipAnimationController.PipTransactionHandler mPipTransactionHandler = new PipAnimationController.PipTransactionHandler() { // from class: com.android.wm.shell.pip.PipTaskOrganizer.2
        @Override // com.android.wm.shell.pip.PipAnimationController.PipTransactionHandler
        public boolean handlePipTransaction(SurfaceControl surfaceControl, SurfaceControl.Transaction transaction, Rect rect) {
            if (!PipTaskOrganizer.this.mPipMenuController.isMenuVisible()) {
                return false;
            }
            PipTaskOrganizer.this.mPipMenuController.movePipMenu(surfaceControl, transaction, rect);
            return true;
        }
    };
    private State mState = State.UNDEFINED;
    private int mOneShotAnimationType = 0;
    private PipSurfaceTransactionHelper.SurfaceControlTransactionFactory mSurfaceControlTransactionFactory = PipAnimationController$PipTransitionAnimator$$ExternalSyntheticLambda0.INSTANCE;

    public int getOutPipWindowingMode() {
        return 0;
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public boolean supportSizeCompatUI() {
        return false;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public enum State {
        UNDEFINED(0),
        TASK_APPEARED(1),
        ENTRY_SCHEDULED(2),
        ENTERING_PIP(3),
        ENTERED_PIP(4),
        EXITING_PIP(5);
        
        private final int mStateValue;

        State(int i) {
            this.mStateValue = i;
        }

        /* access modifiers changed from: private */
        public boolean isInPip() {
            int i = this.mStateValue;
            return i >= TASK_APPEARED.mStateValue && i != EXITING_PIP.mStateValue;
        }

        /* access modifiers changed from: private */
        public boolean shouldBlockResizeRequest() {
            int i = this.mStateValue;
            return i < ENTERING_PIP.mStateValue || i == EXITING_PIP.mStateValue;
        }
    }

    public PipTaskOrganizer(Context context, SyncTransactionQueue syncTransactionQueue, PipBoundsState pipBoundsState, PipBoundsAlgorithm pipBoundsAlgorithm, PipMenuController pipMenuController, PipAnimationController pipAnimationController, PipSurfaceTransactionHelper pipSurfaceTransactionHelper, PipTransitionController pipTransitionController, Optional<LegacySplitScreenController> optional, DisplayController displayController, PipUiEventLogger pipUiEventLogger, ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mContext = context;
        this.mSyncTransactionQueue = syncTransactionQueue;
        this.mPipBoundsState = pipBoundsState;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipMenuController = pipMenuController;
        this.mPipTransitionController = pipTransitionController;
        this.mEnterAnimationDuration = context.getResources().getInteger(R.integer.config_pipEnterAnimationDuration);
        this.mExitAnimationDuration = context.getResources().getInteger(R.integer.config_pipExitAnimationDuration);
        this.mCrossFadeAnimationDuration = context.getResources().getInteger(R.integer.config_pipCrossfadeAnimationDuration);
        this.mSurfaceTransactionHelper = pipSurfaceTransactionHelper;
        this.mPipAnimationController = pipAnimationController;
        this.mPipUiEventLoggerLogger = pipUiEventLogger;
        this.mSplitScreenOptional = optional;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
        shellExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PipTaskOrganizer.this.lambda$new$0();
            }
        });
        displayController.addDisplayWindowListener(this);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mTaskOrganizer.addListenerForType(this, -4);
    }

    public Rect getCurrentOrAnimatingBounds() {
        PipAnimationController.PipTransitionAnimator currentAnimator = this.mPipAnimationController.getCurrentAnimator();
        if (currentAnimator == null || !currentAnimator.isRunning()) {
            return this.mPipBoundsState.getBounds();
        }
        return new Rect(currentAnimator.getDestinationBounds());
    }

    public boolean isInPip() {
        return this.mState.isInPip();
    }

    public boolean isEntryScheduled() {
        return this.mState == State.ENTRY_SCHEDULED;
    }

    public void registerOnDisplayIdChangeCallback(IntConsumer intConsumer) {
        this.mOnDisplayIdChangeCallback = intConsumer;
    }

    public void setOneShotAnimationType(int i) {
        this.mOneShotAnimationType = i;
        if (i == 1) {
            this.mLastOneShotAlphaAnimationTime = SystemClock.uptimeMillis();
        }
    }

    public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams) {
        this.mInSwipePipToHomeTransition = true;
        sendOnPipTransitionStarted(2);
        setBoundsStateForEntry(componentName, pictureInPictureParams, activityInfo);
        return this.mPipBoundsAlgorithm.getEntryDestinationBounds();
    }

    public void stopSwipePipToHome(ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        if (this.mInSwipePipToHomeTransition) {
            this.mPipBoundsState.setBounds(rect);
            this.mSwipePipToHomeOverlay = surfaceControl;
        }
    }

    public SurfaceControl getSurfaceControl() {
        return this.mLeash;
    }

    private void setBoundsStateForEntry(ComponentName componentName, PictureInPictureParams pictureInPictureParams, ActivityInfo activityInfo) {
        this.mPipBoundsState.setBoundsStateForEntry(componentName, this.mPipBoundsAlgorithm.getAspectRatioOrDefault(pictureInPictureParams), this.mPipBoundsAlgorithm.getMinimalSize(activityInfo));
    }

    public void exitPip(int i) {
        State state;
        if (!this.mState.isInPip() || this.mState == (state = State.EXITING_PIP) || this.mToken == null) {
            String str = TAG;
            Log.wtf(str, "Not allowed to exitPip in current state mState=" + this.mState + " mToken=" + this.mToken);
            return;
        }
        this.mPipUiEventLoggerLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_EXPAND_TO_FULLSCREEN);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
        int i2 = 4;
        int i3 = syncWithSplitScreenBounds(displayBounds) ? 4 : 3;
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.scale(transaction, this.mLeash, displayBounds, this.mPipBoundsState.getBounds());
        transaction.setWindowCrop(this.mLeash, displayBounds.width(), displayBounds.height());
        WindowContainerToken windowContainerToken = this.mToken;
        if (i3 != 4) {
            i2 = 1;
        }
        windowContainerTransaction.setActivityWindowingMode(windowContainerToken, i2);
        windowContainerTransaction.setBounds(this.mToken, displayBounds);
        windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
        this.mState = state;
        this.mSyncTransactionQueue.queue(windowContainerTransaction);
        this.mSyncTransactionQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(displayBounds, i3, i) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda1
            public final /* synthetic */ Rect f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ int f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
            public final void runWithTransaction(SurfaceControl.Transaction transaction2) {
                PipTaskOrganizer.this.lambda$exitPip$1(this.f$1, this.f$2, this.f$3, transaction2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$exitPip$1(Rect rect, int i, int i2, SurfaceControl.Transaction transaction) {
        PipAnimationController.PipTransitionAnimator<?> animateResizePip = animateResizePip(this.mPipBoundsState.getBounds(), rect, PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, rect), i, i2, 0.0f);
        if (animateResizePip != null) {
            animateResizePip.applySurfaceControlTransaction(this.mLeash, transaction, 0.0f);
        }
    }

    private void applyWindowingModeChangeOnExit(WindowContainerTransaction windowContainerTransaction, int i) {
        windowContainerTransaction.setWindowingMode(this.mToken, getOutPipWindowingMode());
        windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        this.mSplitScreenOptional.ifPresent(new Consumer(i, windowContainerTransaction) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda9
            public final /* synthetic */ int f$1;
            public final /* synthetic */ WindowContainerTransaction f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PipTaskOrganizer.this.lambda$applyWindowingModeChangeOnExit$2(this.f$1, this.f$2, (LegacySplitScreenController) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$applyWindowingModeChangeOnExit$2(int i, WindowContainerTransaction windowContainerTransaction, LegacySplitScreenController legacySplitScreenController) {
        if (i == 4) {
            windowContainerTransaction.reparent(this.mToken, legacySplitScreenController.getSecondaryRoot(), true);
        }
    }

    public void removePip() {
        if (!this.mState.isInPip() || this.mToken == null) {
            String str = TAG;
            Log.wtf(str, "Not allowed to removePip in current state mState=" + this.mState + " mToken=" + this.mToken);
            return;
        }
        PipAnimationController.PipTransitionAnimator pipAnimationCallback = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, this.mPipBoundsState.getBounds(), 1.0f, 0.0f).setTransitionDirection(5).setPipTransactionHandler(this.mPipTransactionHandler).setPipAnimationCallback(this.mPipAnimationCallback);
        pipAnimationCallback.setDuration((long) this.mExitAnimationDuration);
        pipAnimationCallback.setInterpolator(Interpolators.ALPHA_OUT);
        pipAnimationCallback.start();
        this.mState = State.EXITING_PIP;
    }

    private void removePipImmediately() {
        try {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            windowContainerTransaction.setBounds(this.mToken, (Rect) null);
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            ActivityTaskManager.getService().removeRootTasksInWindowingModes(new int[]{2});
        } catch (RemoteException e) {
            Log.e(TAG, "Failed to remove PiP", e);
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskAppeared(ActivityManager.RunningTaskInfo runningTaskInfo, SurfaceControl surfaceControl) {
        IntConsumer intConsumer;
        Objects.requireNonNull(runningTaskInfo, "Requires RunningTaskInfo");
        this.mTaskInfo = runningTaskInfo;
        this.mToken = runningTaskInfo.token;
        this.mState = State.TASK_APPEARED;
        this.mLeash = surfaceControl;
        PictureInPictureParams pictureInPictureParams = runningTaskInfo.pictureInPictureParams;
        this.mPictureInPictureParams = pictureInPictureParams;
        setBoundsStateForEntry(runningTaskInfo.topActivity, pictureInPictureParams, runningTaskInfo.topActivityInfo);
        this.mPipUiEventLoggerLogger.setTaskInfo(this.mTaskInfo);
        this.mPipUiEventLoggerLogger.log(PipUiEventLogger.PipUiEventEnum.PICTURE_IN_PICTURE_ENTER);
        if (!(runningTaskInfo.displayId == this.mPipBoundsState.getDisplayId() || (intConsumer = this.mOnDisplayIdChangeCallback) == null)) {
            intConsumer.accept(runningTaskInfo.displayId);
        }
        if (!this.mInSwipePipToHomeTransition) {
            if (this.mOneShotAnimationType == 1 && SystemClock.uptimeMillis() - this.mLastOneShotAlphaAnimationTime > 1000) {
                Log.d(TAG, "Alpha animation is expired. Use bounds animation.");
                this.mOneShotAnimationType = 0;
            }
            if (this.mWaitForFixedRotation) {
                onTaskAppearedWithFixedRotation();
                return;
            }
            Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
            Objects.requireNonNull(entryDestinationBounds, "Missing destination bounds");
            Rect bounds = this.mTaskInfo.configuration.windowConfiguration.getBounds();
            if (!Transitions.ENABLE_SHELL_TRANSITIONS) {
                int i = this.mOneShotAnimationType;
                if (i == 0) {
                    this.mPipMenuController.attach(this.mLeash);
                    scheduleAnimateResizePip(bounds, entryDestinationBounds, 0.0f, PipBoundsAlgorithm.getValidSourceHintRect(runningTaskInfo.pictureInPictureParams, bounds), 2, this.mEnterAnimationDuration, null);
                    this.mState = State.ENTERING_PIP;
                } else if (i == 1) {
                    enterPipWithAlphaAnimation(entryDestinationBounds, (long) this.mEnterAnimationDuration);
                    this.mOneShotAnimationType = 0;
                } else {
                    throw new RuntimeException("Unrecognized animation type: " + this.mOneShotAnimationType);
                }
            } else if (this.mOneShotAnimationType == 0) {
                this.mPipMenuController.attach(this.mLeash);
            }
        } else if (!this.mWaitForFixedRotation) {
            onEndOfSwipePipToHomeTransition();
        } else {
            Log.d(TAG, "Defer onTaskAppeared-SwipePipToHome until end of fixed rotation.");
        }
    }

    private void onTaskAppearedWithFixedRotation() {
        if (this.mOneShotAnimationType == 1) {
            Log.d(TAG, "Defer entering PiP alpha animation, fixed rotation is ongoing");
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.setAlpha(this.mLeash, 0.0f);
            transaction.show(this.mLeash);
            transaction.apply();
            this.mOneShotAnimationType = 0;
            return;
        }
        Rect bounds = this.mTaskInfo.configuration.windowConfiguration.getBounds();
        animateResizePip(bounds, this.mPipBoundsAlgorithm.getEntryDestinationBounds(), PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, bounds), 2, this.mEnterAnimationDuration, 0.0f);
        this.mState = State.ENTERING_PIP;
    }

    public void onDisplayRotationSkipped() {
        if (isEntryScheduled()) {
            enterPipWithAlphaAnimation(this.mPipBoundsAlgorithm.getEntryDestinationBounds(), (long) this.mEnterAnimationDuration);
        }
    }

    @VisibleForTesting
    void enterPipWithAlphaAnimation(Rect rect, long j) {
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        transaction.setAlpha(this.mLeash, 0.0f);
        transaction.apply();
        this.mState = State.ENTRY_SCHEDULED;
        applyEnterPipSyncTransaction(rect, new Runnable(rect, j) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda7
            public final /* synthetic */ Rect f$1;
            public final /* synthetic */ long f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                PipTaskOrganizer.this.lambda$enterPipWithAlphaAnimation$3(this.f$1, this.f$2);
            }
        }, null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$enterPipWithAlphaAnimation$3(Rect rect, long j) {
        this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, rect, 0.0f, 1.0f).setTransitionDirection(2).setPipAnimationCallback(this.mPipAnimationCallback).setPipTransactionHandler(this.mPipTransactionHandler).setDuration(j).start();
        this.mState = State.ENTERING_PIP;
    }

    private void onEndOfSwipePipToHomeTransition() {
        Rect bounds = this.mPipBoundsState.getBounds();
        SurfaceControl surfaceControl = this.mSwipePipToHomeOverlay;
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.resetScale(transaction, this.mLeash, bounds).crop(transaction, this.mLeash, bounds).round(transaction, this.mLeash, isInPip());
        applyEnterPipSyncTransaction(bounds, new Runnable(bounds, surfaceControl) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda8
            public final /* synthetic */ Rect f$1;
            public final /* synthetic */ SurfaceControl f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                PipTaskOrganizer.this.lambda$onEndOfSwipePipToHomeTransition$4(this.f$1, this.f$2);
            }
        }, transaction);
        this.mInSwipePipToHomeTransition = false;
        this.mSwipePipToHomeOverlay = null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onEndOfSwipePipToHomeTransition$4(Rect rect, SurfaceControl surfaceControl) {
        finishResizeForMenu(rect);
        sendOnPipTransitionFinished(2);
        if (surfaceControl != null) {
            fadeOutAndRemoveOverlay(surfaceControl, null, false);
        }
    }

    private void applyEnterPipSyncTransaction(Rect rect, Runnable runnable, SurfaceControl.Transaction transaction) {
        this.mPipMenuController.attach(this.mLeash);
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        windowContainerTransaction.setBounds(this.mToken, rect);
        if (transaction != null) {
            windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
        }
        this.mSyncTransactionQueue.queue(windowContainerTransaction);
        if (runnable != null) {
            this.mSyncTransactionQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(runnable) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda3
                public final /* synthetic */ Runnable f$0;

                {
                    this.f$0 = r1;
                }

                @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                public final void runWithTransaction(SurfaceControl.Transaction transaction2) {
                    this.f$0.run();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void sendOnPipTransitionStarted(int i) {
        if (i == 2) {
            this.mState = State.ENTERING_PIP;
        }
        this.mPipTransitionController.sendOnPipTransitionStarted(i);
    }

    @VisibleForTesting
    void sendOnPipTransitionFinished(int i) {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        if (i == 2) {
            this.mState = State.ENTERED_PIP;
        }
        this.mPipTransitionController.sendOnPipTransitionFinished(i);
        if (i == 2 && (runningTaskInfo = this.mDeferredTaskInfo) != null) {
            onTaskInfoChanged(runningTaskInfo);
            this.mDeferredTaskInfo = null;
        }
    }

    /* access modifiers changed from: private */
    public void sendOnPipTransitionCancelled(int i) {
        this.mPipTransitionController.sendOnPipTransitionCancelled(i);
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskVanished(ActivityManager.RunningTaskInfo runningTaskInfo) {
        IntConsumer intConsumer;
        State state = this.mState;
        State state2 = State.UNDEFINED;
        if (state != state2) {
            WindowContainerToken windowContainerToken = runningTaskInfo.token;
            Objects.requireNonNull(windowContainerToken, "Requires valid WindowContainerToken");
            if (windowContainerToken.asBinder() != this.mToken.asBinder()) {
                String str = TAG;
                Log.wtf(str, "Unrecognized token: " + windowContainerToken);
                return;
            }
            clearWaitForFixedRotation();
            this.mInSwipePipToHomeTransition = false;
            this.mPictureInPictureParams = null;
            this.mState = state2;
            this.mPipBoundsState.setBounds(new Rect());
            this.mPipUiEventLoggerLogger.setTaskInfo(null);
            this.mPipMenuController.detach();
            if (!(runningTaskInfo.displayId == 0 || (intConsumer = this.mOnDisplayIdChangeCallback) == null)) {
                intConsumer.accept(0);
            }
            PipAnimationController.PipTransitionAnimator currentAnimator = this.mPipAnimationController.getCurrentAnimator();
            if (currentAnimator != null) {
                if (currentAnimator.getContentOverlay() != null) {
                    removeContentOverlay(currentAnimator.getContentOverlay(), new PipTaskOrganizer$$ExternalSyntheticLambda4(currentAnimator));
                }
                currentAnimator.removeAllUpdateListeners();
                currentAnimator.removeAllListeners();
                currentAnimator.cancel();
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void onTaskInfoChanged(ActivityManager.RunningTaskInfo runningTaskInfo) {
        Objects.requireNonNull(this.mToken, "onTaskInfoChanged requires valid existing mToken");
        State state = this.mState;
        if (state == State.ENTERED_PIP || state == State.EXITING_PIP) {
            this.mPipBoundsState.setLastPipComponentName(runningTaskInfo.topActivity);
            this.mPipBoundsState.setOverrideMinSize(this.mPipBoundsAlgorithm.getMinimalSize(runningTaskInfo.topActivityInfo));
            PictureInPictureParams pictureInPictureParams = runningTaskInfo.pictureInPictureParams;
            if (pictureInPictureParams == null || !applyPictureInPictureParams(pictureInPictureParams)) {
                String str = TAG;
                Log.d(str, "Ignored onTaskInfoChanged with PiP param: " + pictureInPictureParams);
                return;
            }
            Rect adjustedDestinationBounds = this.mPipBoundsAlgorithm.getAdjustedDestinationBounds(this.mPipBoundsState.getBounds(), this.mPipBoundsState.getAspectRatio());
            Objects.requireNonNull(adjustedDestinationBounds, "Missing destination bounds");
            scheduleAnimateResizePip(adjustedDestinationBounds, this.mEnterAnimationDuration, null);
            return;
        }
        String str2 = TAG;
        Log.d(str2, "Defer onTaskInfoChange in current state: " + this.mState);
        this.mDeferredTaskInfo = runningTaskInfo;
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onFixedRotationStarted(int i, int i2) {
        this.mNextRotation = i2;
        this.mWaitForFixedRotation = true;
        if (this.mState.isInPip()) {
            fadeExistingPip(false);
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onFixedRotationFinished(int i) {
        if (this.mWaitForFixedRotation) {
            State state = this.mState;
            if (state == State.TASK_APPEARED) {
                if (this.mInSwipePipToHomeTransition) {
                    onEndOfSwipePipToHomeTransition();
                } else {
                    enterPipWithAlphaAnimation(this.mPipBoundsAlgorithm.getEntryDestinationBounds(), (long) this.mEnterAnimationDuration);
                }
            } else if (state == State.ENTERED_PIP && this.mHasFadeOut) {
                fadeExistingPip(true);
            } else if (state == State.ENTERING_PIP && this.mDeferredAnimEndTransaction != null) {
                Rect destinationBounds = this.mPipAnimationController.getCurrentAnimator().getDestinationBounds();
                this.mPipBoundsState.setBounds(destinationBounds);
                applyEnterPipSyncTransaction(destinationBounds, new Runnable(destinationBounds) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda6
                    public final /* synthetic */ Rect f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        PipTaskOrganizer.this.lambda$onFixedRotationFinished$6(this.f$1);
                    }
                }, this.mDeferredAnimEndTransaction);
            }
            clearWaitForFixedRotation();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onFixedRotationFinished$6(Rect rect) {
        finishResizeForMenu(rect);
        sendOnPipTransitionFinished(2);
    }

    private void fadeExistingPip(boolean z) {
        this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, this.mPipBoundsState.getBounds(), z ? 0.0f : 1.0f, z ? 1.0f : 0.0f).setTransitionDirection(1).setPipTransactionHandler(this.mPipTransactionHandler).setDuration((long) (z ? this.mEnterAnimationDuration : this.mExitAnimationDuration)).start();
        this.mHasFadeOut = !z;
    }

    private void clearWaitForFixedRotation() {
        this.mWaitForFixedRotation = false;
        this.mDeferredAnimEndTransaction = null;
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        this.mCurrentRotation = configuration.windowConfiguration.getRotation();
    }

    public void onDensityOrFontScaleChanged(Context context) {
        this.mSurfaceTransactionHelper.onDensityOrFontScaleChanged(context);
    }

    public void onMovementBoundsChanged(Rect rect, boolean z, boolean z2, boolean z3, WindowContainerTransaction windowContainerTransaction) {
        boolean z4 = true;
        int i = 0;
        boolean z5 = this.mWaitForFixedRotation && this.mState != State.ENTERED_PIP;
        if ((!this.mInSwipePipToHomeTransition && !z5) || !z) {
            PipAnimationController.PipTransitionAnimator currentAnimator = this.mPipAnimationController.getCurrentAnimator();
            if (currentAnimator == null || !currentAnimator.isRunning() || currentAnimator.getTransitionDirection() != 2) {
                if (!this.mState.isInPip() || !z) {
                    z4 = false;
                }
                if (z4 && this.mWaitForFixedRotation && this.mHasFadeOut) {
                    this.mPipBoundsState.setBounds(rect);
                } else if (z4) {
                    this.mPipBoundsState.setBounds(rect);
                    if (currentAnimator != null) {
                        i = currentAnimator.getTransitionDirection();
                        currentAnimator.removeAllUpdateListeners();
                        currentAnimator.removeAllListeners();
                        currentAnimator.cancel();
                        sendOnPipTransitionCancelled(i);
                        sendOnPipTransitionFinished(i);
                    }
                    prepareFinishResizeTransaction(rect, i, createFinishResizeSurfaceTransaction(rect), windowContainerTransaction);
                } else if (currentAnimator == null || !currentAnimator.isRunning()) {
                    if (!this.mPipBoundsState.getBounds().isEmpty()) {
                        rect.set(this.mPipBoundsState.getBounds());
                    }
                } else if (!currentAnimator.getDestinationBounds().isEmpty()) {
                    rect.set(currentAnimator.getDestinationBounds());
                }
            } else {
                Rect destinationBounds = currentAnimator.getDestinationBounds();
                rect.set(destinationBounds);
                if (z2 || z3 || !this.mPipBoundsState.getDisplayBounds().contains(destinationBounds)) {
                    Rect entryDestinationBounds = this.mPipBoundsAlgorithm.getEntryDestinationBounds();
                    if (!entryDestinationBounds.equals(destinationBounds)) {
                        if (currentAnimator.getAnimationType() == 0) {
                            if (this.mWaitForFixedRotation) {
                                Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
                                Rect rect2 = new Rect(entryDestinationBounds);
                                RotationUtils.rotateBounds(rect2, displayBounds, this.mNextRotation, this.mCurrentRotation);
                                currentAnimator.updateEndValue(rect2);
                            } else {
                                currentAnimator.updateEndValue(entryDestinationBounds);
                            }
                        }
                        currentAnimator.setDestinationBounds(entryDestinationBounds);
                        rect.set(entryDestinationBounds);
                    }
                }
            }
        }
    }

    private boolean applyPictureInPictureParams(PictureInPictureParams pictureInPictureParams) {
        PictureInPictureParams pictureInPictureParams2 = this.mPictureInPictureParams;
        boolean z = !Objects.equals(pictureInPictureParams2 != null ? pictureInPictureParams2.getAspectRatioRational() : null, pictureInPictureParams.getAspectRatioRational());
        this.mPictureInPictureParams = pictureInPictureParams;
        if (z) {
            this.mPipBoundsState.setAspectRatio(pictureInPictureParams.getAspectRatio());
        }
        return z;
    }

    public void scheduleAnimateResizePip(Rect rect, int i, Consumer<Rect> consumer) {
        scheduleAnimateResizePip(rect, i, 0, consumer);
    }

    public void scheduleAnimateResizePip(Rect rect, int i, int i2, Consumer<Rect> consumer) {
        if (this.mWaitForFixedRotation) {
            Log.d(TAG, "skip scheduleAnimateResizePip, entering pip deferred");
        } else {
            scheduleAnimateResizePip(this.mPipBoundsState.getBounds(), rect, 0.0f, null, i2, i, consumer);
        }
    }

    public void scheduleAnimateResizePip(Rect rect, Rect rect2, int i, float f, Consumer<Rect> consumer) {
        if (this.mWaitForFixedRotation) {
            Log.d(TAG, "skip scheduleAnimateResizePip, entering pip deferred");
        } else {
            scheduleAnimateResizePip(rect, rect2, f, null, 6, i, consumer);
        }
    }

    private PipAnimationController.PipTransitionAnimator<?> scheduleAnimateResizePip(Rect rect, Rect rect2, float f, Rect rect3, int i, int i2, Consumer<Rect> consumer) {
        if (!this.mState.isInPip()) {
            return null;
        }
        PipAnimationController.PipTransitionAnimator<?> animateResizePip = animateResizePip(rect, rect2, rect3, i, i2, f);
        if (consumer != null) {
            consumer.accept(rect2);
        }
        return animateResizePip;
    }

    public void scheduleResizePip(Rect rect, Consumer<Rect> consumer) {
        if (this.mToken == null || this.mLeash == null) {
            Log.w(TAG, "Abort animation, invalid leash");
            return;
        }
        this.mPipBoundsState.setBounds(rect);
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.crop(transaction, this.mLeash, rect).round(transaction, this.mLeash, this.mState.isInPip());
        if (this.mPipMenuController.isMenuVisible()) {
            this.mPipMenuController.resizePipMenu(this.mLeash, transaction, rect);
        } else {
            transaction.apply();
        }
        if (consumer != null) {
            consumer.accept(rect);
        }
    }

    public void scheduleUserResizePip(Rect rect, Rect rect2, Consumer<Rect> consumer) {
        scheduleUserResizePip(rect, rect2, 0.0f, consumer);
    }

    public void scheduleUserResizePip(Rect rect, Rect rect2, float f, Consumer<Rect> consumer) {
        if (this.mToken == null || this.mLeash == null) {
            Log.w(TAG, "Abort animation, invalid leash");
        } else if (rect.isEmpty() || rect2.isEmpty()) {
            Log.w(TAG, "Attempted to user resize PIP to or from empty bounds, aborting.");
        } else {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            this.mSurfaceTransactionHelper.scale(transaction, this.mLeash, rect, rect2, f).round(transaction, this.mLeash, rect, rect2);
            if (this.mPipMenuController.isMenuVisible()) {
                this.mPipMenuController.movePipMenu(this.mLeash, transaction, rect2);
            } else {
                transaction.apply();
            }
            if (consumer != null) {
                consumer.accept(rect2);
            }
        }
    }

    public void scheduleFinishResizePip(Rect rect) {
        scheduleFinishResizePip(rect, null);
    }

    public void scheduleFinishResizePip(Rect rect, Consumer<Rect> consumer) {
        scheduleFinishResizePip(rect, 0, consumer);
    }

    public void scheduleFinishResizePip(Rect rect, int i, Consumer<Rect> consumer) {
        if (!this.mState.shouldBlockResizeRequest()) {
            finishResize(createFinishResizeSurfaceTransaction(rect), rect, i, -1);
            if (consumer != null) {
                consumer.accept(rect);
            }
        }
    }

    private SurfaceControl.Transaction createFinishResizeSurfaceTransaction(Rect rect) {
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        this.mSurfaceTransactionHelper.crop(transaction, this.mLeash, rect).resetScale(transaction, this.mLeash, rect).round(transaction, this.mLeash, this.mState.isInPip());
        return transaction;
    }

    public void scheduleOffsetPip(Rect rect, int i, int i2, Consumer<Rect> consumer) {
        if (!this.mState.shouldBlockResizeRequest()) {
            if (this.mWaitForFixedRotation) {
                Log.d(TAG, "skip scheduleOffsetPip, entering pip deferred");
                return;
            }
            offsetPip(rect, 0, i, i2);
            Rect rect2 = new Rect(rect);
            rect2.offset(0, i);
            if (consumer != null) {
                consumer.accept(rect2);
            }
        }
    }

    private void offsetPip(Rect rect, int i, int i2, int i3) {
        if (this.mTaskInfo == null) {
            Log.w(TAG, "mTaskInfo is not set");
            return;
        }
        Rect rect2 = new Rect(rect);
        rect2.offset(i, i2);
        animateResizePip(rect, rect2, null, 1, i3, 0.0f);
    }

    /* access modifiers changed from: private */
    public void finishResize(SurfaceControl.Transaction transaction, Rect rect, int i, int i2) {
        PictureInPictureParams pictureInPictureParams;
        Rect rect2 = new Rect(this.mPipBoundsState.getBounds());
        this.mPipBoundsState.setBounds(rect);
        if (i == 5) {
            removePipImmediately();
            return;
        }
        boolean z = true;
        if (!PipAnimationController.isInPipDirection(i) || i2 != 1) {
            WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
            prepareFinishResizeTransaction(rect, i, transaction, windowContainerTransaction);
            if (!(i == 7 || i == 6 || i == 8) || (pictureInPictureParams = this.mPictureInPictureParams) == null || pictureInPictureParams.isSeamlessResizeEnabled()) {
                z = false;
            }
            if (z) {
                rect2.offsetTo(0, 0);
                Rect rect3 = new Rect(0, 0, rect.width(), rect.height());
                SurfaceControl takeScreenshot = ScreenshotUtils.takeScreenshot(this.mSurfaceControlTransactionFactory.getTransaction(), this.mLeash, rect2, 2147483645);
                if (takeScreenshot != null) {
                    this.mSyncTransactionQueue.queue(windowContainerTransaction);
                    this.mSyncTransactionQueue.runInSync(new SyncTransactionQueue.TransactionRunnable(takeScreenshot, rect2, rect3) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda2
                        public final /* synthetic */ SurfaceControl f$1;
                        public final /* synthetic */ Rect f$2;
                        public final /* synthetic */ Rect f$3;

                        {
                            this.f$1 = r2;
                            this.f$2 = r3;
                            this.f$3 = r4;
                        }

                        @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                        public final void runWithTransaction(SurfaceControl.Transaction transaction2) {
                            PipTaskOrganizer.this.lambda$finishResize$7(this.f$1, this.f$2, this.f$3, transaction2);
                        }
                    });
                } else {
                    applyFinishBoundsResize(windowContainerTransaction, i);
                }
            } else {
                applyFinishBoundsResize(windowContainerTransaction, i);
            }
            finishResizeForMenu(rect);
            return;
        }
        finishResizeForMenu(rect);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$finishResize$7(SurfaceControl surfaceControl, Rect rect, Rect rect2, SurfaceControl.Transaction transaction) {
        this.mSurfaceTransactionHelper.scale(transaction, surfaceControl, rect, rect2);
        fadeOutAndRemoveOverlay(surfaceControl, null, false);
    }

    public void finishResizeForMenu(Rect rect) {
        if (isInPip()) {
            this.mPipMenuController.movePipMenu(null, null, rect);
            this.mPipMenuController.updateMenuBounds(rect);
        }
    }

    private void prepareFinishResizeTransaction(Rect rect, int i, SurfaceControl.Transaction transaction, WindowContainerTransaction windowContainerTransaction) {
        if (PipAnimationController.isInPipDirection(i)) {
            windowContainerTransaction.setActivityWindowingMode(this.mToken, 0);
        } else if (PipAnimationController.isOutPipDirection(i)) {
            rect = null;
            applyWindowingModeChangeOnExit(windowContainerTransaction, i);
        }
        this.mSurfaceTransactionHelper.round(transaction, this.mLeash, isInPip());
        windowContainerTransaction.setBounds(this.mToken, rect);
        windowContainerTransaction.setBoundsChangeTransaction(this.mToken, transaction);
    }

    public void applyFinishBoundsResize(WindowContainerTransaction windowContainerTransaction, int i) {
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
    }

    private PipAnimationController.PipTransitionAnimator<?> animateResizePip(Rect rect, Rect rect2, Rect rect3, int i, int i2, float f) {
        if (this.mToken == null || this.mLeash == null) {
            Log.w(TAG, "Abort animation, invalid leash");
            return null;
        }
        int deltaRotation = this.mWaitForFixedRotation ? RotationUtils.deltaRotation(this.mCurrentRotation, this.mNextRotation) : 0;
        Rect rect4 = rect3;
        if (deltaRotation != 0) {
            rect4 = computeRotatedBounds(deltaRotation, i, rect2, rect4);
        }
        PipAnimationController.PipTransitionAnimator<?> animator = this.mPipAnimationController.getAnimator(this.mTaskInfo, this.mLeash, i == 6 ? this.mPipBoundsState.getBounds() : rect, rect, rect2, rect4, i, f, deltaRotation);
        animator.setTransitionDirection(i).setPipAnimationCallback(this.mPipAnimationCallback).setPipTransactionHandler(this.mPipTransactionHandler).setDuration((long) i2);
        if (PipAnimationController.isInPipDirection(i)) {
            if (rect4 == null) {
                animator.setUseContentOverlay(this.mContext);
            }
            if (deltaRotation != 0) {
                animator.setDestinationBounds(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
            }
        }
        animator.start();
        return animator;
    }

    private Rect computeRotatedBounds(int i, int i2, Rect rect, Rect rect2) {
        Rect rect3;
        if (i2 == 2) {
            this.mPipBoundsState.getDisplayLayout().rotateTo(this.mContext.getResources(), this.mNextRotation);
            Rect displayBounds = this.mPipBoundsState.getDisplayBounds();
            rect.set(this.mPipBoundsAlgorithm.getEntryDestinationBounds());
            RotationUtils.rotateBounds(rect, displayBounds, this.mNextRotation, this.mCurrentRotation);
            if (!(rect2 == null || (rect3 = this.mTaskInfo.displayCutoutInsets) == null || i != 3)) {
                rect2.offset(rect3.left, rect3.top);
            }
        } else if (i2 == 3) {
            Rect rect4 = new Rect(rect);
            RotationUtils.rotateBounds(rect4, this.mPipBoundsState.getDisplayBounds(), i);
            return PipBoundsAlgorithm.getValidSourceHintRect(this.mPictureInPictureParams, rect4);
        }
        return rect2;
    }

    private boolean syncWithSplitScreenBounds(Rect rect) {
        if (!this.mSplitScreenOptional.isPresent()) {
            return false;
        }
        LegacySplitScreenController legacySplitScreenController = this.mSplitScreenOptional.get();
        if (!legacySplitScreenController.isDividerVisible()) {
            return false;
        }
        rect.set(legacySplitScreenController.getDividerView().getNonMinimizedSplitScreenSecondaryBounds());
        return true;
    }

    /* access modifiers changed from: private */
    public void fadeOutAndRemoveOverlay(final SurfaceControl surfaceControl, final Runnable runnable, boolean z) {
        if (surfaceControl != null) {
            ValueAnimator ofFloat = ValueAnimator.ofFloat(1.0f, 0.0f);
            ofFloat.setDuration((long) this.mCrossFadeAnimationDuration);
            ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(surfaceControl) { // from class: com.android.wm.shell.pip.PipTaskOrganizer$$ExternalSyntheticLambda0
                public final /* synthetic */ SurfaceControl f$1;

                {
                    this.f$1 = r2;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                    PipTaskOrganizer.this.lambda$fadeOutAndRemoveOverlay$8(this.f$1, valueAnimator);
                }
            });
            ofFloat.addListener(new AnimatorListenerAdapter() { // from class: com.android.wm.shell.pip.PipTaskOrganizer.3
                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    PipTaskOrganizer.this.removeContentOverlay(surfaceControl, runnable);
                }
            });
            ofFloat.setStartDelay(z ? 500 : 0);
            ofFloat.start();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$fadeOutAndRemoveOverlay$8(SurfaceControl surfaceControl, ValueAnimator valueAnimator) {
        if (this.mState == State.UNDEFINED) {
            Log.d(TAG, "Task vanished, skip fadeOutAndRemoveOverlay");
            valueAnimator.removeAllListeners();
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.cancel();
            return;
        }
        float floatValue = ((Float) valueAnimator.getAnimatedValue()).floatValue();
        SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
        transaction.setAlpha(surfaceControl, floatValue);
        transaction.apply();
    }

    /* access modifiers changed from: private */
    public void removeContentOverlay(SurfaceControl surfaceControl, Runnable runnable) {
        if (this.mState != State.UNDEFINED) {
            SurfaceControl.Transaction transaction = this.mSurfaceControlTransactionFactory.getTransaction();
            transaction.remove(surfaceControl);
            transaction.apply();
            if (runnable != null) {
                runnable.run();
            }
        }
    }

    @Override // com.android.wm.shell.ShellTaskOrganizer.TaskListener
    public void dump(PrintWriter printWriter, String str) {
        String str2 = str + "  ";
        printWriter.println(str + TAG);
        printWriter.println(str2 + "mTaskInfo=" + this.mTaskInfo);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append("mToken=");
        sb.append(this.mToken);
        sb.append(" binder=");
        WindowContainerToken windowContainerToken = this.mToken;
        sb.append(windowContainerToken != null ? windowContainerToken.asBinder() : null);
        printWriter.println(sb.toString());
        printWriter.println(str2 + "mLeash=" + this.mLeash);
        printWriter.println(str2 + "mState=" + this.mState);
        printWriter.println(str2 + "mOneShotAnimationType=" + this.mOneShotAnimationType);
        printWriter.println(str2 + "mPictureInPictureParams=" + this.mPictureInPictureParams);
    }

    public String toString() {
        return TAG + ":" + ShellTaskOrganizer.taskListenerTypeToString(-4);
    }
}
