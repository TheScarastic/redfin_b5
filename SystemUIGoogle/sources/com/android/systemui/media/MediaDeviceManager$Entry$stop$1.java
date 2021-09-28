package com.android.systemui.media;

import android.media.session.MediaController;
import com.android.systemui.media.MediaDeviceManager;
/* compiled from: MediaDeviceManager.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class MediaDeviceManager$Entry$stop$1 implements Runnable {
    final /* synthetic */ MediaDeviceManager.Entry this$0;

    /* access modifiers changed from: package-private */
    public MediaDeviceManager$Entry$stop$1(MediaDeviceManager.Entry entry) {
        this.this$0 = entry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.started = false;
        MediaController controller = this.this$0.getController();
        if (controller != null) {
            controller.unregisterCallback(this.this$0);
        }
        this.this$0.getLocalMediaManager().stopScan();
        this.this$0.getLocalMediaManager().unregisterCallback(this.this$0);
    }
}
