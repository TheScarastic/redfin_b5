package com.android.wm.shell.tasksurfacehelper;

import android.view.SurfaceControl;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2 implements Runnable {
    public final /* synthetic */ Consumer f$0;
    public final /* synthetic */ SurfaceControl.ScreenshotHardwareBuffer f$1;

    public /* synthetic */ TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2(Consumer consumer, SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
        this.f$0 = consumer;
        this.f$1 = screenshotHardwareBuffer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.accept(this.f$1);
    }
}
