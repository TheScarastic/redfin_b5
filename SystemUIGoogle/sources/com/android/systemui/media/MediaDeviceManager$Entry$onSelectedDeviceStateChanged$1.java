package com.android.systemui.media;

import com.android.systemui.media.MediaDeviceManager;
/* compiled from: MediaDeviceManager.kt */
/* loaded from: classes.dex */
final class MediaDeviceManager$Entry$onSelectedDeviceStateChanged$1 implements Runnable {
    final /* synthetic */ MediaDeviceManager.Entry this$0;

    /* access modifiers changed from: package-private */
    public MediaDeviceManager$Entry$onSelectedDeviceStateChanged$1(MediaDeviceManager.Entry entry) {
        this.this$0 = entry;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.updateCurrent();
    }
}
