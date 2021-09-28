package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.PlaybackState;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModel$callback$1 extends MediaController.Callback {
    final /* synthetic */ SeekBarViewModel this$0;

    /* access modifiers changed from: package-private */
    public SeekBarViewModel$callback$1(SeekBarViewModel seekBarViewModel) {
        this.this$0 = seekBarViewModel;
    }

    @Override // android.media.session.MediaController.Callback
    public void onPlaybackStateChanged(PlaybackState playbackState) {
        SeekBarViewModel.access$setPlaybackState$p(this.this$0, playbackState);
        if (SeekBarViewModel.access$getPlaybackState$p(this.this$0) != null) {
            Integer num = 0;
            if (!num.equals(SeekBarViewModel.access$getPlaybackState$p(this.this$0))) {
                SeekBarViewModel.access$checkIfPollingNeeded(this.this$0);
                return;
            }
        }
        this.this$0.clearController();
    }

    @Override // android.media.session.MediaController.Callback
    public void onSessionDestroyed() {
        this.this$0.clearController();
    }
}
