package com.android.systemui.shared.system;

import android.app.ActivityManager;
import android.graphics.Bitmap;
/* loaded from: classes.dex */
public class TaskDescriptionCompat {
    private ActivityManager.TaskDescription mTaskDescription;

    public TaskDescriptionCompat(ActivityManager.TaskDescription taskDescription) {
        this.mTaskDescription = taskDescription;
    }

    public static Bitmap getIcon(ActivityManager.TaskDescription taskDescription, int i) {
        if (taskDescription.getInMemoryIcon() != null) {
            return taskDescription.getInMemoryIcon();
        }
        return ActivityManager.TaskDescription.loadTaskDescriptionIcon(taskDescription.getIconFilename(), i);
    }

    public int getBackgroundColor() {
        ActivityManager.TaskDescription taskDescription = this.mTaskDescription;
        if (taskDescription != null) {
            return taskDescription.getBackgroundColor();
        }
        return 0;
    }

    public int getPrimaryColor() {
        ActivityManager.TaskDescription taskDescription = this.mTaskDescription;
        if (taskDescription != null) {
            return taskDescription.getPrimaryColor();
        }
        return 0;
    }
}
