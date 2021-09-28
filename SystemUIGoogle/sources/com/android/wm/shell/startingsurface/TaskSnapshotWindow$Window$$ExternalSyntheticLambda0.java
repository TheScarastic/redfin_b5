package com.android.wm.shell.startingsurface;

import android.util.MergedConfiguration;
import com.android.wm.shell.startingsurface.TaskSnapshotWindow;
/* loaded from: classes2.dex */
public final /* synthetic */ class TaskSnapshotWindow$Window$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ TaskSnapshotWindow.Window f$0;
    public final /* synthetic */ MergedConfiguration f$1;
    public final /* synthetic */ boolean f$2;

    public /* synthetic */ TaskSnapshotWindow$Window$$ExternalSyntheticLambda0(TaskSnapshotWindow.Window window, MergedConfiguration mergedConfiguration, boolean z) {
        this.f$0 = window;
        this.f$1 = mergedConfiguration;
        this.f$2 = z;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$resized$0(this.f$1, this.f$2);
    }
}
