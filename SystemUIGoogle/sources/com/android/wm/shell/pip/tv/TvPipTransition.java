package com.android.wm.shell.pip.tv;

import android.app.TaskInfo;
import android.graphics.Rect;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.pip.PipAnimationController;
import com.android.wm.shell.pip.PipBoundsAlgorithm;
import com.android.wm.shell.pip.PipBoundsState;
import com.android.wm.shell.pip.PipMenuController;
import com.android.wm.shell.pip.PipTransitionController;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes2.dex */
public class TvPipTransition extends PipTransitionController {
    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        return null;
    }

    @Override // com.android.wm.shell.pip.PipTransitionController
    public void onFinishResize(TaskInfo taskInfo, Rect rect, int i, SurfaceControl.Transaction transaction) {
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, Transitions.TransitionFinishCallback transitionFinishCallback) {
        return false;
    }

    public TvPipTransition(PipBoundsState pipBoundsState, PipMenuController pipMenuController, PipBoundsAlgorithm pipBoundsAlgorithm, PipAnimationController pipAnimationController, Transitions transitions, ShellTaskOrganizer shellTaskOrganizer) {
        super(pipBoundsState, pipMenuController, pipBoundsAlgorithm, pipAnimationController, transitions, shellTaskOrganizer);
    }
}
