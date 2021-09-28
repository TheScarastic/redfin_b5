package com.android.systemui.media.dialog;

import android.graphics.drawable.Icon;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda1 implements Runnable {
    public final /* synthetic */ MediaOutputBaseAdapter.MediaDeviceBaseViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;
    public final /* synthetic */ Icon f$2;
    public final /* synthetic */ boolean f$3;
    public final /* synthetic */ boolean f$4;

    public /* synthetic */ MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda1(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, MediaDevice mediaDevice, Icon icon, boolean z, boolean z2) {
        this.f$0 = mediaDeviceBaseViewHolder;
        this.f$1 = mediaDevice;
        this.f$2 = icon;
        this.f$3 = z;
        this.f$4 = z2;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onBind$0(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
