package com.android.wm.shell.pip;

import android.app.PictureInPictureParams;
import android.app.TaskInfo;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.SurfaceControl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class PipTransitionController implements Transitions.TransitionHandler {
    protected final PipAnimationController mPipAnimationController;
    protected final PipBoundsAlgorithm mPipBoundsAlgorithm;
    protected final PipBoundsState mPipBoundsState;
    protected final PipMenuController mPipMenuController;
    protected final ShellTaskOrganizer mShellTaskOrganizer;
    private final List<PipTransitionCallback> mPipTransitionCallbacks = new ArrayList();
    protected final PipAnimationController.PipAnimationCallback mPipAnimationCallback = new PipAnimationController.PipAnimationCallback() { // from class: com.android.wm.shell.pip.PipTransitionController.1
        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public void onPipAnimationStart(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            PipTransitionController.this.sendOnPipTransitionStarted(pipTransitionAnimator.getTransitionDirection());
        }

        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public void onPipAnimationEnd(TaskInfo taskInfo, SurfaceControl.Transaction transaction, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            int transitionDirection = pipTransitionAnimator.getTransitionDirection();
            PipTransitionController.this.mPipBoundsState.setBounds(pipTransitionAnimator.getDestinationBounds());
            if (transitionDirection != 5) {
                PipTransitionController.this.onFinishResize(taskInfo, pipTransitionAnimator.getDestinationBounds(), transitionDirection, transaction);
                PipTransitionController.this.sendOnPipTransitionFinished(transitionDirection);
            }
        }

        @Override // com.android.wm.shell.pip.PipAnimationController.PipAnimationCallback
        public void onPipAnimationCancel(TaskInfo taskInfo, PipAnimationController.PipTransitionAnimator pipTransitionAnimator) {
            PipTransitionController.this.sendOnPipTransitionCancelled(pipTransitionAnimator.getTransitionDirection());
        }
    };
    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    /* loaded from: classes2.dex */
    public interface PipTransitionCallback {
        void onPipTransitionCanceled(int i);

        void onPipTransitionFinished(int i);

        void onPipTransitionStarted(int i, Rect rect);
    }

    public int getOutPipWindowingMode() {
        return 0;
    }

    public void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
    }

    public PipTransitionController(PipBoundsState pipBoundsState, PipMenuController pipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer) {
        this.mPipBoundsState = pipBoundsState;
        this.mPipMenuController = pipMenuController;
        this.mShellTaskOrganizer = shellTaskOrganizer;
        this.mPipBoundsAlgorithm = pipBoundsAlgorithm;
        this.mPipAnimationController = pipAnimationController;
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            transitions.addHandler(this);
        }
    }

    public void registerPipTransitionCallback(PipTransitionCallback pipTransitionCallback) {
        this.mPipTransitionCallbacks.add(pipTransitionCallback);
    }

    /* access modifiers changed from: protected */
    public void sendOnPipTransitionStarted(int i) {
        Rect bounds = this.mPipBoundsState.getBounds();
        for (int size = this.mPipTransitionCallbacks.size() - 1; size >= 0; size--) {
            this.mPipTransitionCallbacks.get(size).onPipTransitionStarted(i, bounds);
        }
    }

    /* access modifiers changed from: protected */
    public void sendOnPipTransitionFinished(int i) {
        for (int size = this.mPipTransitionCallbacks.size() - 1; size >= 0; size--) {
            this.mPipTransitionCallbacks.get(size).onPipTransitionFinished(i);
        }
    }

    /* access modifiers changed from: protected */
    public void sendOnPipTransitionCancelled(int i) {
        for (int size = this.mPipTransitionCallbacks.size() - 1; size >= 0; size--) {
            this.mPipTransitionCallbacks.get(size).onPipTransitionCanceled(i);
        }
    }

    /* access modifiers changed from: protected */
    public void setBoundsStateForEntry(ComponentName componentName, PictureInPictureParams pictureInPictureParams, ActivityInfo activityInfo) {
        this.mPipBoundsState.setBoundsStateForEntry(componentName, this.mPipBoundsAlgorithm.getAspectRatioOrDefault(pictureInPictureParams), this.mPipBoundsAlgorithm.getMinimalSize(activityInfo));
    }
}
