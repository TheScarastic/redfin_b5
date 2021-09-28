package com.google.android.systemui.columbus.gates;

import android.app.TaskStackListener;
/* compiled from: CameraVisibility.kt */
/* loaded from: classes2.dex */
public final class CameraVisibility$taskStackListener$1 extends TaskStackListener {
    final /* synthetic */ CameraVisibility this$0;

    /* access modifiers changed from: package-private */
    public CameraVisibility$taskStackListener$1(CameraVisibility cameraVisibility) {
        this.this$0 = cameraVisibility;
    }

    public void onTaskStackChanged() {
        this.this$0.updateHandler.post(new Runnable(this.this$0) { // from class: com.google.android.systemui.columbus.gates.CameraVisibility$taskStackListener$1$onTaskStackChanged$1
            final /* synthetic */ CameraVisibility this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.updateCameraIsShowing();
            }
        });
    }
}
