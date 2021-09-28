package com.android.wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public interface TaskSurfaceHelper {
    default void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
    }

    default void setGameModeForTask(int i, int i2) {
    }
}
