package com.android.systemui.media.dialog;

import android.view.MotionEvent;
import android.view.View;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda0 implements View.OnTouchListener {
    public static final /* synthetic */ MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda0 INSTANCE = new MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda0();

    private /* synthetic */ MediaOutputBaseAdapter$MediaDeviceBaseViewHolder$$ExternalSyntheticLambda0() {
    }

    @Override // android.view.View.OnTouchListener
    public final boolean onTouch(View view, MotionEvent motionEvent) {
        return MediaOutputBaseAdapter.MediaDeviceBaseViewHolder.lambda$disableSeekBar$2(view, motionEvent);
    }
}
