package com.android.wm.shell.tasksurfacehelper;

import android.view.SurfaceControl;
import com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelperController;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda3 implements Consumer {
    public final /* synthetic */ Executor f$0;
    public final /* synthetic */ Consumer f$1;

    public /* synthetic */ TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda3(Executor executor, Consumer consumer) {
        this.f$0 = executor;
        this.f$1 = consumer;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        TaskSurfaceHelperController.TaskSurfaceHelperImpl.lambda$screenshotTask$2(this.f$0, this.f$1, (SurfaceControl.ScreenshotHardwareBuffer) obj);
    }
}
