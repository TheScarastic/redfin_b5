package com.android.wm.shell.pip.phone;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PictureInPictureParams;
import android.app.RemoteAction;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ParceledListSlice;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserManager;
import android.util.Log;
import android.util.Size;
import android.util.Slog;
import android.view.InputEvent;
import android.view.SurfaceControl;
import android.view.WindowManagerGlobal;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.util.function.TriConsumer;
import com.android.wm.shell.R;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.onehanded.OneHandedController;
import com.android.wm.shell.onehanded.OneHandedTransitionCallback;
import com.android.wm.shell.pip.IPip;
import com.android.wm.shell.pip.IPipAnimationListener;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import com.android.wm.shell.pip.Pip;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMediaController;
import com.android.wm.shell.pip.PipSnapAlgorithm;
import com.android.wm.shell.pip.PipTaskOrganizer;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.pip.PipUtils;
import com.android.wm.shell.pip.phone.PipInputConsumer;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
/* loaded from: classes2.dex */
public class PipController implements PipTransitionController.PipTransitionCallback, RemoteCallable<PipController> {
    private PipAppOpsListener mAppOpsListener;
    private Context mContext;
    private DisplayController mDisplayController;
    protected final PipImpl mImpl;
    private boolean mIsInFixedRotation;
    protected ShellExecutor mMainExecutor;
    private PipMediaController mMediaController;
    protected PhonePipMenuController mMenuController;
    private Optional<OneHandedController> mOneHandedController;
    private IPipAnimationListener mPinnedStackAnimationRecentsCallback;
    private PipBoundsAlgorithm mPipBoundsAlgorithm;
    private PipBoundsState mPipBoundsState;
    private PipInputConsumer mPipInputConsumer;
    protected PipTaskOrganizer mPipTaskOrganizer;
    private PipTransitionController mPipTransitionController;
    private TaskStackListenerImpl mTaskStackListener;
    private PipTouchHandler mTouchHandler;
    private WindowManagerShellWrapper mWindowManagerShellWrapper;
    private final Rect mTmpInsetBounds = new Rect();
    protected PinnedStackListenerForwarder.PinnedTaskListener mPinnedTaskListener = new PipControllerPinnedTaskListener();
    private final DisplayChangeController.OnDisplayChangingListener mRotationController = new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda1
        @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
        public final void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
            PipController.this.lambda$new$0(i, i2, i3, windowContainerTransaction);
        }
    };
    @VisibleForTesting
    final DisplayController.OnDisplaysChangedListener mDisplaysChangedListener = new DisplayController.OnDisplaysChangedListener() { // from class: com.android.wm.shell.pip.phone.PipController.1
        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public void onFixedRotationStarted(int i, int i2) {
            PipController.this.mIsInFixedRotation = true;
        }

        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public void onFixedRotationFinished(int i) {
            PipController.this.mIsInFixedRotation = false;
        }

        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public void onDisplayAdded(int i) {
            if (i == PipController.this.mPipBoundsState.getDisplayId()) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), false);
            }
        }

        @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
        public void onDisplayConfigurationChanged(int i, Configuration configuration) {
            if (i == PipController.this.mPipBoundsState.getDisplayId()) {
                PipController pipController = PipController.this;
                pipController.onDisplayChanged(pipController.mDisplayController.getDisplayLayout(i), true);
            }
        }
    };

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (this.mPipBoundsState.getDisplayLayout().rotation() == i3) {
            updateMovementBounds(null, false, false, false, windowContainerTransaction);
        } else if (!this.mPipTaskOrganizer.isInPip() || this.mPipTaskOrganizer.isEntryScheduled()) {
            onDisplayRotationChangedNotInPip(this.mContext, i3);
            updateMovementBounds(this.mPipBoundsState.getNormalBounds(), true, false, false, windowContainerTransaction);
            this.mPipTaskOrganizer.onDisplayRotationSkipped();
        } else {
            Rect currentOrAnimatingBounds = this.mPipTaskOrganizer.getCurrentOrAnimatingBounds();
            Rect rect = new Rect();
            if (onDisplayRotationChanged(this.mContext, rect, currentOrAnimatingBounds, this.mTmpInsetBounds, i, i2, i3, windowContainerTransaction)) {
                this.mTouchHandler.adjustBoundsForRotation(rect, this.mPipBoundsState.getBounds(), this.mTmpInsetBounds);
                if (!this.mIsInFixedRotation) {
                    this.mPipBoundsState.setShelfVisibility(false, 0, false);
                    this.mPipBoundsState.setImeVisibility(false, 0);
                    this.mTouchHandler.onShelfVisibilityChanged(false, 0);
                    this.mTouchHandler.onImeVisibilityChanged(false, 0);
                }
                updateMovementBounds(rect, true, false, false, windowContainerTransaction);
            }
        }
    }

    /* loaded from: classes2.dex */
    private class PipControllerPinnedTaskListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        private PipControllerPinnedTaskListener() {
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public void onImeVisibilityChanged(boolean z, int i) {
            PipController.this.mPipBoundsState.setImeVisibility(z, i);
            PipController.this.mTouchHandler.onImeVisibilityChanged(z, i);
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public void onMovementBoundsChanged(boolean z) {
            PipController.this.updateMovementBounds(null, false, z, false, null);
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public void onActionsChanged(ParceledListSlice<RemoteAction> parceledListSlice) {
            PipController.this.mMenuController.setAppActions(parceledListSlice);
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public void onActivityHidden(ComponentName componentName) {
            if (componentName.equals(PipController.this.mPipBoundsState.getLastPipComponentName())) {
                PipController.this.mPipBoundsState.setLastPipComponentName(null);
            }
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public void onAspectRatioChanged(float f) {
            PipController.this.mPipBoundsState.setAspectRatio(f);
            PipController.this.mTouchHandler.onAspectRatioChanged();
        }
    }

    public static Pip create(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (context.getPackageManager().hasSystemFeature("android.software.picture_in_picture")) {
            return new PipController(context, displayController, pipAppOpsListener, pipBoundsAlgorithm, pipBoundsState, pipMediaController, phonePipMenuController, pipTaskOrganizer, pipTouchHandler, pipTransitionController, windowManagerShellWrapper, taskStackListenerImpl, optional, shellExecutor).mImpl;
        }
        Slog.w("PipController", "Device doesn't support Pip feature");
        return null;
    }

    protected PipController(Context context, DisplayController displayController, PipAppOpsListener pipAppOpsListener, PipBoundsAlgorithm pipBoundsAlgorithm, PipBoundsState pipBoundsState, PipMediaController pipMediaController, PhonePipMenuController phonePipMenuController, PipTaskOrganizer pipTaskOrganizer, PipTouchHandler pipTouchHandler, PipTransitionController pipTransitionController, WindowManagerShellWrapper windowManagerShellWrapper, TaskStackListenerImpl taskStackListenerImpl, Optional<OneHandedController> optional, ShellExecutor shellExecutor) {
        if (UserManager.get(context).getUserHandle() == 0) {
            this.mContext = context;
            this.mImpl = new PipImpl();
            this.mWindowManagerShellWrapper = windowManagerShellWrapper;
            this.mDisplayController = displayController;
            this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
            this.mPipBoundsState = pipBoundsState;
            this.mPipTaskOrganizer = pipTaskOrganizer;
            this.mMainExecutor = shellExecutor;
            this.mMediaController = pipMediaController;
            this.mMenuController = phonePipMenuController;
            this.mTouchHandler = pipTouchHandler;
            this.mAppOpsListener = pipAppOpsListener;
            this.mOneHandedController = optional;
            this.mPipTransitionController = pipTransitionController;
            this.mTaskStackListener = taskStackListenerImpl;
            this.mPipInputConsumer = new PipInputConsumer(WindowManagerGlobal.getWindowManagerService(), "pip_input_consumer", shellExecutor);
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda4
                @Override // java.lang.Runnable
                public final void run() {
                    PipController.this.init();
                }
            });
            return;
        }
        throw new IllegalStateException("Non-primary Pip component not currently supported.");
    }

    public void init() {
        this.mPipTransitionController.registerPipTransitionCallback(this);
        this.mPipTaskOrganizer.registerOnDisplayIdChangeCallback(new IntConsumer() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda8
            @Override // java.util.function.IntConsumer
            public final void accept(int i) {
                PipController.this.lambda$init$1(i);
            }
        });
        this.mPipBoundsState.setOnMinimalSizeChangeCallback(new Runnable() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda5
            @Override // java.lang.Runnable
            public final void run() {
                PipController.this.lambda$init$2();
            }
        });
        this.mPipBoundsState.setOnShelfVisibilityChangeCallback(new TriConsumer() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda0
            public final void accept(Object obj, Object obj2, Object obj3) {
                PipController.this.lambda$init$3((Boolean) obj, (Integer) obj2, (Boolean) obj3);
            }
        });
        PipTouchHandler pipTouchHandler = this.mTouchHandler;
        if (pipTouchHandler != null) {
            PipInputConsumer pipInputConsumer = this.mPipInputConsumer;
            Objects.requireNonNull(pipTouchHandler);
            pipInputConsumer.setInputListener(new PipInputConsumer.InputListener() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda2
                @Override // com.android.wm.shell.pip.phone.PipInputConsumer.InputListener
                public final boolean onInputEvent(InputEvent inputEvent) {
                    return PipTouchHandler.this.handleTouchEvent(inputEvent);
                }
            });
            PipInputConsumer pipInputConsumer2 = this.mPipInputConsumer;
            PipTouchHandler pipTouchHandler2 = this.mTouchHandler;
            Objects.requireNonNull(pipTouchHandler2);
            pipInputConsumer2.setRegistrationListener(new PipInputConsumer.RegistrationListener() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda3
                @Override // com.android.wm.shell.pip.phone.PipInputConsumer.RegistrationListener
                public final void onRegistrationChanged(boolean z) {
                    PipTouchHandler.this.onRegistrationChanged(z);
                }
            });
        }
        this.mDisplayController.addDisplayChangingController(this.mRotationController);
        this.mDisplayController.addDisplayWindowListener(this.mDisplaysChangedListener);
        this.mPipBoundsState.setDisplayId(this.mContext.getDisplayId());
        PipBoundsState pipBoundsState = this.mPipBoundsState;
        Context context = this.mContext;
        pipBoundsState.setDisplayLayout(new DisplayLayout(context, context.getDisplay()));
        try {
            this.mWindowManagerShellWrapper.addPinnedStackListener(this.mPinnedTaskListener);
        } catch (RemoteException e) {
            Slog.e("PipController", "Failed to register pinned stack listener", e);
        }
        try {
            if (ActivityTaskManager.getService().getRootTaskInfo(2, 0) != null) {
                this.mPipInputConsumer.registerInputConsumer();
            }
        } catch (RemoteException | UnsupportedOperationException e2) {
            Log.e("PipController", "Failed to register pinned stack listener", e2);
            e2.printStackTrace();
        }
        this.mTaskStackListener.addListener(new TaskStackListenerCallback() { // from class: com.android.wm.shell.pip.phone.PipController.2
            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityPinned(String str, int i, int i2, int i3) {
                PipController.this.mTouchHandler.onActivityPinned();
                PipController.this.mMediaController.onActivityPinned();
                PipController.this.mAppOpsListener.onActivityPinned(str);
                PipController.this.mPipInputConsumer.registerInputConsumer();
            }

            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityUnpinned() {
                PipController.this.mTouchHandler.onActivityUnpinned((ComponentName) PipUtils.getTopPipActivity(PipController.this.mContext).first);
                PipController.this.mAppOpsListener.onActivityUnpinned();
                PipController.this.mPipInputConsumer.unregisterInputConsumer();
            }

            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                if (runningTaskInfo.getWindowingMode() == 2) {
                    PipController.this.mTouchHandler.getMotionHelper().expandLeavePip(z2);
                }
            }
        });
        this.mOneHandedController.ifPresent(new Consumer() { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda7
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                PipController.this.lambda$init$4((OneHandedController) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$1(int i) {
        this.mPipBoundsState.setDisplayId(i);
        onDisplayChanged(this.mDisplayController.getDisplayLayout(i), false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$2() {
        updateMovementBounds(null, false, false, false, null);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$3(Boolean bool, Integer num, Boolean bool2) {
        this.mTouchHandler.onShelfVisibilityChanged(bool.booleanValue(), num.intValue());
        if (bool2.booleanValue()) {
            updateMovementBounds(this.mPipBoundsState.getBounds(), false, false, true, null);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$init$4(OneHandedController oneHandedController) {
        oneHandedController.asOneHanded().registerTransitionCallback(new OneHandedTransitionCallback() { // from class: com.android.wm.shell.pip.phone.PipController.3
            @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
            public void onStartFinished(Rect rect) {
                PipController.this.mTouchHandler.setOhmOffset(rect.top);
            }

            @Override // com.android.wm.shell.onehanded.OneHandedTransitionCallback
            public void onStopFinished(Rect rect) {
                PipController.this.mTouchHandler.setOhmOffset(rect.top);
            }
        });
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    /* access modifiers changed from: private */
    public void onConfigurationChanged(Configuration configuration) {
        this.mPipBoundsAlgorithm.onConfigurationChanged(this.mContext);
        this.mTouchHandler.onConfigurationChanged();
        this.mPipBoundsState.onConfigurationChanged();
    }

    /* access modifiers changed from: private */
    public void onDensityOrFontScaleChanged() {
        this.mPipTaskOrganizer.onDensityOrFontScaleChanged(this.mContext);
        onPipCornerRadiusChanged();
    }

    /* access modifiers changed from: private */
    public void onOverlayChanged() {
        Context context = this.mContext;
        onDisplayChanged(new DisplayLayout(context, context.getDisplay()), false);
    }

    /* access modifiers changed from: private */
    public void onDisplayChanged(DisplayLayout displayLayout, boolean z) {
        if (!Objects.equals(displayLayout, this.mPipBoundsState.getDisplayLayout())) {
            PipController$$ExternalSyntheticLambda6 pipController$$ExternalSyntheticLambda6 = new Runnable(displayLayout) { // from class: com.android.wm.shell.pip.phone.PipController$$ExternalSyntheticLambda6
                public final /* synthetic */ DisplayLayout f$1;

                {
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    PipController.this.lambda$onDisplayChanged$5(this.f$1);
                }
            };
            if (!this.mPipTaskOrganizer.isInPip() || !z) {
                pipController$$ExternalSyntheticLambda6.run();
                return;
            }
            PipSnapAlgorithm snapAlgorithm = this.mPipBoundsAlgorithm.getSnapAlgorithm();
            Rect rect = new Rect(this.mPipBoundsState.getBounds());
            float snapFraction = snapAlgorithm.getSnapFraction(rect, this.mPipBoundsAlgorithm.getMovementBounds(rect), this.mPipBoundsState.getStashedState());
            pipController$$ExternalSyntheticLambda6.run();
            snapAlgorithm.applySnapFraction(rect, this.mPipBoundsAlgorithm.getMovementBounds(rect, false), snapFraction, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
            this.mTouchHandler.getMotionHelper().movePip(rect);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onDisplayChanged$5(DisplayLayout displayLayout) {
        this.mPipBoundsState.setDisplayLayout(displayLayout);
        updateMovementBounds(null, false, false, false, null);
    }

    /* access modifiers changed from: private */
    public void registerSessionListenerForCurrentUser() {
        this.mMediaController.registerSessionListenerForCurrentUser();
    }

    /* access modifiers changed from: private */
    public void onSystemUiStateChanged(boolean z, int i) {
        this.mTouchHandler.onSystemUiStateChanged(z);
    }

    public void hidePipMenu(Runnable runnable, Runnable runnable2) {
        this.mMenuController.hideMenu(runnable, runnable2);
    }

    public void showPictureInPictureMenu() {
        this.mTouchHandler.showPictureInPictureMenu();
    }

    /* access modifiers changed from: private */
    public void setShelfHeight(boolean z, int i) {
        setShelfHeightLocked(z, i);
    }

    private void setShelfHeightLocked(boolean z, int i) {
        if (!z) {
            i = 0;
        }
        this.mPipBoundsState.setShelfVisibility(z, i);
    }

    /* access modifiers changed from: private */
    public void setPinnedStackAnimationType(int i) {
        this.mPipTaskOrganizer.setOneShotAnimationType(i);
    }

    /* access modifiers changed from: private */
    public void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) {
        this.mPinnedStackAnimationRecentsCallback = iPipAnimationListener;
        onPipCornerRadiusChanged();
    }

    private void onPipCornerRadiusChanged() {
        if (this.mPinnedStackAnimationRecentsCallback != null) {
            try {
                this.mPinnedStackAnimationRecentsCallback.onPipCornerRadiusChanged(this.mContext.getResources().getDimensionPixelSize(R.dimen.pip_corner_radius));
            } catch (RemoteException e) {
                Log.e("PipController", "Failed to call onPipCornerRadiusChanged", e);
            }
        }
    }

    /* access modifiers changed from: private */
    public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
        setShelfHeightLocked(i2 > 0, i2);
        onDisplayRotationChangedNotInPip(this.mContext, i);
        Rect startSwipePipToHome = this.mPipTaskOrganizer.startSwipePipToHome(componentName, activityInfo, pictureInPictureParams);
        this.mPipBoundsState.setNormalBounds(startSwipePipToHome);
        return startSwipePipToHome;
    }

    /* access modifiers changed from: private */
    public void stopSwipePipToHome(ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
        this.mPipTaskOrganizer.stopSwipePipToHome(componentName, rect, surfaceControl);
    }

    @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
    public void onPipTransitionStarted(int i, Rect rect) {
        if (PipAnimationController.isOutPipDirection(i)) {
            saveReentryState(rect);
        }
        this.mTouchHandler.setTouchEnabled(false);
        IPipAnimationListener iPipAnimationListener = this.mPinnedStackAnimationRecentsCallback;
        if (iPipAnimationListener != null) {
            try {
                iPipAnimationListener.onPipAnimationStarted();
            } catch (RemoteException e) {
                Log.e("PipController", "Failed to call onPinnedStackAnimationStarted()", e);
            }
        }
    }

    public void saveReentryState(Rect rect) {
        float snapFraction = this.mPipBoundsAlgorithm.getSnapFraction(rect);
        if (this.mPipBoundsState.hasUserResizedPip()) {
            Rect userResizeBounds = this.mTouchHandler.getUserResizeBounds();
            this.mPipBoundsState.saveReentryState(new Size(userResizeBounds.width(), userResizeBounds.height()), snapFraction);
            return;
        }
        this.mPipBoundsState.saveReentryState(null, snapFraction);
    }

    @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
    public void onPipTransitionFinished(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    @Override // com.android.wm.shell.pip.PipTransitionController.PipTransitionCallback
    public void onPipTransitionCanceled(int i) {
        onPipTransitionFinishedOrCanceled(i);
    }

    private void onPipTransitionFinishedOrCanceled(int i) {
        this.mTouchHandler.setTouchEnabled(true);
        this.mTouchHandler.onPinnedStackAnimationEnded(i);
    }

    /* access modifiers changed from: private */
    public void updateMovementBounds(Rect rect, boolean z, boolean z2, boolean z3, WindowContainerTransaction windowContainerTransaction) {
        Rect rect2 = new Rect(rect);
        int rotation = this.mPipBoundsState.getDisplayLayout().rotation();
        this.mPipBoundsAlgorithm.getInsetBounds(this.mTmpInsetBounds);
        this.mPipBoundsState.setNormalBounds(this.mPipBoundsAlgorithm.getNormalBounds());
        if (rect2.isEmpty()) {
            rect2.set(this.mPipBoundsAlgorithm.getDefaultBounds());
        }
        this.mPipTaskOrganizer.onMovementBoundsChanged(rect2, z, z2, z3, windowContainerTransaction);
        this.mPipTaskOrganizer.finishResizeForMenu(rect2);
        this.mTouchHandler.onMovementBoundsChanged(this.mTmpInsetBounds, this.mPipBoundsState.getNormalBounds(), rect2, z2, z3, rotation);
    }

    private void onDisplayRotationChangedNotInPip(Context context, int i) {
        this.mPipBoundsState.getDisplayLayout().rotateTo(context.getResources(), i);
    }

    private boolean onDisplayRotationChanged(Context context, Rect rect, Rect rect2, Rect rect3, int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        if (i == this.mPipBoundsState.getDisplayId() && i2 != i3) {
            try {
                ActivityTaskManager.RootTaskInfo rootTaskInfo = ActivityTaskManager.getService().getRootTaskInfo(2, 0);
                if (rootTaskInfo == null) {
                    return false;
                }
                PipSnapAlgorithm snapAlgorithm = this.mPipBoundsAlgorithm.getSnapAlgorithm();
                Rect rect4 = new Rect(rect2);
                float snapFraction = snapAlgorithm.getSnapFraction(rect4, this.mPipBoundsAlgorithm.getMovementBounds(rect4), this.mPipBoundsState.getStashedState());
                this.mPipBoundsState.getDisplayLayout().rotateTo(context.getResources(), i3);
                snapAlgorithm.applySnapFraction(rect4, this.mPipBoundsAlgorithm.getMovementBounds(rect4, false), snapFraction, this.mPipBoundsState.getStashedState(), this.mPipBoundsState.getStashOffset(), this.mPipBoundsState.getDisplayBounds(), this.mPipBoundsState.getDisplayLayout().stableInsets());
                this.mPipBoundsAlgorithm.getInsetBounds(rect3);
                rect.set(rect4);
                windowContainerTransaction.setBounds(rootTaskInfo.token, rect);
                return true;
            } catch (RemoteException e) {
                Log.e("PipController", "Failed to get RootTaskInfo for pinned task", e);
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("PipController");
        this.mMenuController.dump(printWriter, "  ");
        this.mTouchHandler.dump(printWriter, "  ");
        this.mPipBoundsAlgorithm.dump(printWriter, "  ");
        this.mPipTaskOrganizer.dump(printWriter, "  ");
        this.mPipBoundsState.dump(printWriter, "  ");
        this.mPipInputConsumer.dump(printWriter, "  ");
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class PipImpl implements Pip {
        private IPipImpl mIPip;

        private PipImpl() {
        }

        @Override // com.android.wm.shell.pip.Pip
        public IPip createExternalInterface() {
            IPipImpl iPipImpl = this.mIPip;
            if (iPipImpl != null) {
                iPipImpl.invalidate();
            }
            IPipImpl iPipImpl2 = new IPipImpl(PipController.this);
            this.mIPip = iPipImpl2;
            return iPipImpl2;
        }

        @Override // com.android.wm.shell.pip.Pip
        public void hidePipMenu(Runnable runnable, Runnable runnable2) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda7(this, runnable, runnable2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$hidePipMenu$0(Runnable runnable, Runnable runnable2) {
            PipController.this.hidePipMenu(runnable, runnable2);
        }

        @Override // com.android.wm.shell.pip.Pip
        public void onConfigurationChanged(Configuration configuration) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda5(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigurationChanged$2(Configuration configuration) {
            PipController.this.onConfigurationChanged(configuration);
        }

        @Override // com.android.wm.shell.pip.Pip
        public void onDensityOrFontScaleChanged() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onDensityOrFontScaleChanged$3() {
            PipController.this.onDensityOrFontScaleChanged();
        }

        @Override // com.android.wm.shell.pip.Pip
        public void onOverlayChanged() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda3(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onOverlayChanged$4() {
            PipController.this.onOverlayChanged();
        }

        @Override // com.android.wm.shell.pip.Pip
        public void onSystemUiStateChanged(boolean z, int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda9(this, z, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSystemUiStateChanged$5(boolean z, int i) {
            PipController.this.onSystemUiStateChanged(z, i);
        }

        @Override // com.android.wm.shell.pip.Pip
        public void registerSessionListenerForCurrentUser() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerSessionListenerForCurrentUser$6() {
            PipController.this.registerSessionListenerForCurrentUser();
        }

        @Override // com.android.wm.shell.pip.Pip
        public void setPinnedStackAnimationType(int i) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda4(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setPinnedStackAnimationType$8(int i) {
            PipController.this.setPinnedStackAnimationType(i);
        }

        @Override // com.android.wm.shell.pip.Pip
        public void setPipExclusionBoundsChangeListener(Consumer<Rect> consumer) {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda8(this, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setPipExclusionBoundsChangeListener$9(Consumer consumer) {
            PipController.this.mPipBoundsState.setPipExclusionBoundsChangeCallback(consumer);
        }

        @Override // com.android.wm.shell.pip.Pip
        public void showPictureInPictureMenu() {
            PipController.this.mMainExecutor.execute(new PipController$PipImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$showPictureInPictureMenu$10() {
            PipController.this.showPictureInPictureMenu();
        }

        @Override // com.android.wm.shell.pip.Pip
        public void dump(PrintWriter printWriter) {
            try {
                PipController.this.mMainExecutor.executeBlocking(new PipController$PipImpl$$ExternalSyntheticLambda6(this, printWriter));
            } catch (InterruptedException unused) {
                Slog.e("PipController", "Failed to dump PipController in 2s");
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$11(PrintWriter printWriter) {
            PipController.this.dump(printWriter);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class IPipImpl extends IPip.Stub {
        private PipController mController;
        private IPipAnimationListener mListener;
        private final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.wm.shell.pip.phone.PipController.IPipImpl.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                PipController pipController = IPipImpl.this.mController;
                pipController.getRemoteCallExecutor().execute(new PipController$IPipImpl$1$$ExternalSyntheticLambda0(this, pipController));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$binderDied$0(PipController pipController) {
                IPipImpl.this.mListener = null;
                pipController.setPinnedStackAnimationListener(null);
            }
        };

        IPipImpl(PipController pipController) {
            this.mController = pipController;
        }

        void invalidate() {
            this.mController = null;
        }

        @Override // com.android.wm.shell.pip.IPip
        public Rect startSwipePipToHome(ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2) {
            Rect[] rectArr = new Rect[1];
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startSwipePipToHome", new PipController$IPipImpl$$ExternalSyntheticLambda3(rectArr, componentName, activityInfo, pictureInPictureParams, i, i2), true);
            return rectArr[0];
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$startSwipePipToHome$0(Rect[] rectArr, ComponentName componentName, ActivityInfo activityInfo, PictureInPictureParams pictureInPictureParams, int i, int i2, PipController pipController) {
            rectArr[0] = pipController.startSwipePipToHome(componentName, activityInfo, pictureInPictureParams, i, i2);
        }

        @Override // com.android.wm.shell.pip.IPip
        public void stopSwipePipToHome(ComponentName componentName, Rect rect, SurfaceControl surfaceControl) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "stopSwipePipToHome", new PipController$IPipImpl$$ExternalSyntheticLambda0(componentName, rect, surfaceControl));
        }

        @Override // com.android.wm.shell.pip.IPip
        public void setShelfHeight(boolean z, int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setShelfHeight", new PipController$IPipImpl$$ExternalSyntheticLambda2(z, i));
        }

        @Override // com.android.wm.shell.pip.IPip
        public void setPinnedStackAnimationListener(IPipAnimationListener iPipAnimationListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setPinnedStackAnimationListener", new PipController$IPipImpl$$ExternalSyntheticLambda1(this, iPipAnimationListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setPinnedStackAnimationListener$3(IPipAnimationListener iPipAnimationListener, PipController pipController) {
            IPipAnimationListener iPipAnimationListener2 = this.mListener;
            if (iPipAnimationListener2 != null) {
                iPipAnimationListener2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            if (iPipAnimationListener != null) {
                try {
                    iPipAnimationListener.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
                } catch (RemoteException unused) {
                    Slog.e("PipController", "Failed to link to death");
                    return;
                }
            }
            this.mListener = iPipAnimationListener;
            pipController.setPinnedStackAnimationListener(iPipAnimationListener);
        }
    }
}
