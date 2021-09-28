package com.bumptech.glide.request.target;

import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.bumptech.glide.gifdecoder.GifHeaderParser$$ExternalSyntheticOutline0;
import com.bumptech.glide.request.SingleRequest;
import com.bumptech.glide.util.Util;
@Deprecated
/* loaded from: classes.dex */
public abstract class SimpleTarget<Z> extends BaseTarget<Z> {
    public final int width = RecyclerView.UNDEFINED_DURATION;
    public final int height = RecyclerView.UNDEFINED_DURATION;

    @Override // com.bumptech.glide.request.target.Target
    public final void getSize(SizeReadyCallback sizeReadyCallback) {
        if (Util.isValidDimensions(this.width, this.height)) {
            ((SingleRequest) sizeReadyCallback).onSizeReady(this.width, this.height);
            return;
        }
        StringBuilder m = GifHeaderParser$$ExternalSyntheticOutline0.m(SysUiStatsLog.ASSIST_GESTURE_PROGRESS_REPORTED, "Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ", this.width, " and height: ", this.height);
        m.append(", either provide dimensions in the constructor or call override()");
        throw new IllegalArgumentException(m.toString());
    }

    @Override // com.bumptech.glide.request.target.Target
    public void removeCallback(SizeReadyCallback sizeReadyCallback) {
    }
}
