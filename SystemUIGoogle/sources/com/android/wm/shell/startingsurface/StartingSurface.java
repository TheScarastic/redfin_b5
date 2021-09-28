package com.android.wm.shell.startingsurface;

import android.app.TaskInfo;
/* loaded from: classes2.dex */
public interface StartingSurface {
    default IStartingWindow createExternalInterface() {
        return null;
    }

    default int getBackgroundColor(TaskInfo taskInfo) {
        return -16777216;
    }
}
