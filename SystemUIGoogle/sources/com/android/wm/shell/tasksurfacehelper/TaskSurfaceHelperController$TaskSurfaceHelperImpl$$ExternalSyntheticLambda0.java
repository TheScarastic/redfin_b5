package com.android.wm.shell.tasksurfacehelper;

import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
/* loaded from: classes2.dex */
public final /* synthetic */ class TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TaskSurfaceHelperController.TaskSurfaceHelperImpl f$0;
    public final /* synthetic */ int f$1;
    public final /* synthetic */ int f$2;

    public /* synthetic */ TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda0(TaskSurfaceHelperController.TaskSurfaceHelperImpl taskSurfaceHelperImpl, int i, int i2) {
        this.f$0 = taskSurfaceHelperImpl;
        this.f$1 = i;
        this.f$2 = i2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$setGameModeForTask$0(this.f$1, this.f$2);
    }
}
