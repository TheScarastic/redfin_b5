package com.android.wm.shell.tasksurfacehelper;

import android.app.ActivityManager;
import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ShellExecutor;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class TaskSurfaceHelperController {
    private final TaskSurfaceHelperImpl mImpl = new TaskSurfaceHelperImpl();
    private final ShellExecutor mMainExecutor;
    private final ShellTaskOrganizer mTaskOrganizer;

    public TaskSurfaceHelperController(ShellTaskOrganizer shellTaskOrganizer, ShellExecutor shellExecutor) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mMainExecutor = shellExecutor;
    }

    public TaskSurfaceHelper asTaskSurfaceHelper() {
        return this.mImpl;
    }

    public void setGameModeForTask(int i, int i2) {
        this.mTaskOrganizer.setSurfaceMetadata(i, 8, i2);
    }

    public void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
        this.mTaskOrganizer.screenshotTask(runningTaskInfo, rect, consumer);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class TaskSurfaceHelperImpl implements TaskSurfaceHelper {
        private TaskSurfaceHelperImpl() {
        }

        @Override // com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper
        public void setGameModeForTask(int i, int i2) {
            TaskSurfaceHelperController.this.mMainExecutor.execute(new TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda0(this, i, i2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setGameModeForTask$0(int i, int i2) {
            TaskSurfaceHelperController.this.setGameModeForTask(i, i2);
        }

        @Override // com.android.wm.shell.tasksurfacehelper.TaskSurfaceHelper
        public void screenshotTask(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer<SurfaceControl.ScreenshotHardwareBuffer> consumer) {
            TaskSurfaceHelperController.this.mMainExecutor.execute(new TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda1(this, runningTaskInfo, rect, executor, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$screenshotTask$3(ActivityManager.RunningTaskInfo runningTaskInfo, Rect rect, Executor executor, Consumer consumer) {
            TaskSurfaceHelperController.this.screenshotTask(runningTaskInfo, rect, new TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda3(executor, consumer));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$screenshotTask$2(Executor executor, Consumer consumer, SurfaceControl.ScreenshotHardwareBuffer screenshotHardwareBuffer) {
            executor.execute(new TaskSurfaceHelperController$TaskSurfaceHelperImpl$$ExternalSyntheticLambda2(consumer, screenshotHardwareBuffer));
        }
    }
}
