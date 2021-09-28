package com.android.systemui.shared.system;

import android.app.ActivityOptions;
import android.content.Context;
import android.os.Handler;
import com.android.systemui.shared.recents.model.Task;
/* loaded from: classes.dex */
public abstract class ActivityOptionsCompat {
    public static void addTaskInfo(ActivityOptions activityOptions, Task.TaskKey taskKey) {
        if (taskKey.windowingMode == 3) {
            activityOptions.setLaunchWindowingMode(4);
        }
    }

    public static ActivityOptions makeCustomAnimation(Context context, int i, int i2, final Runnable runnable, final Handler handler) {
        return ActivityOptions.makeCustomTaskAnimation(context, i, i2, handler, new ActivityOptions.OnAnimationStartedListener() { // from class: com.android.systemui.shared.system.ActivityOptionsCompat.1
            public void onAnimationStarted() {
                Runnable runnable2 = runnable;
                if (runnable2 != null) {
                    handler.post(runnable2);
                }
            }
        }, null);
    }

    public static ActivityOptions makeFreeformOptions() {
        ActivityOptions makeBasic = ActivityOptions.makeBasic();
        makeBasic.setLaunchWindowingMode(5);
        return makeBasic;
    }

    public static ActivityOptions makeRemoteAnimation(RemoteAnimationAdapterCompat remoteAnimationAdapterCompat) {
        return ActivityOptions.makeRemoteAnimation(remoteAnimationAdapterCompat.getWrapped(), remoteAnimationAdapterCompat.getRemoteTransition().getTransition());
    }

    public static ActivityOptions makeRemoteTransition(RemoteTransitionCompat remoteTransitionCompat) {
        return ActivityOptions.makeRemoteTransition(remoteTransitionCompat.getTransition());
    }

    public static ActivityOptions makeSplitScreenOptions(boolean z) {
        return makeSplitScreenOptions(z, true);
    }

    public static ActivityOptions setFreezeRecentTasksList(ActivityOptions activityOptions) {
        activityOptions.setFreezeRecentTasksReordering();
        return activityOptions;
    }

    public static ActivityOptions setLauncherSourceInfo(ActivityOptions activityOptions, long j) {
        activityOptions.setSourceInfo(1, j);
        return activityOptions;
    }

    public static ActivityOptions makeSplitScreenOptions(boolean z, boolean z2) {
        ActivityOptions makeBasic = ActivityOptions.makeBasic();
        makeBasic.setLaunchWindowingMode(z2 ? 3 : 4);
        return makeBasic;
    }
}
