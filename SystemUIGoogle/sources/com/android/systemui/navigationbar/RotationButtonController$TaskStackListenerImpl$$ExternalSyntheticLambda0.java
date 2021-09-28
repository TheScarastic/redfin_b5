package com.android.systemui.navigationbar;

import android.app.ActivityManager;
import com.android.systemui.navigationbar.RotationButtonController;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ RotationButtonController.TaskStackListenerImpl f$0;
    public final /* synthetic */ int f$1;

    public /* synthetic */ RotationButtonController$TaskStackListenerImpl$$ExternalSyntheticLambda0(RotationButtonController.TaskStackListenerImpl taskStackListenerImpl, int i) {
        this.f$0 = taskStackListenerImpl;
        this.f$1 = i;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$onActivityRequestedOrientationChanged$0(this.f$1, (ActivityManager.RunningTaskInfo) obj);
    }
}
