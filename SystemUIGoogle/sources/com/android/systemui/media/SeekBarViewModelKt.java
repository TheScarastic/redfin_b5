package com.android.systemui.media;

import android.media.session.PlaybackState;
import android.os.SystemClock;
/* compiled from: SeekBarViewModel.kt */
/* loaded from: classes.dex */
public final class SeekBarViewModelKt {
    /* access modifiers changed from: private */
    public static final boolean isInMotion(PlaybackState playbackState) {
        return playbackState.getState() == 3 || playbackState.getState() == 4 || playbackState.getState() == 5;
    }

    /* access modifiers changed from: private */
    public static final long computePosition(PlaybackState playbackState, long j) {
        long position = playbackState.getPosition();
        if (!isInMotion(playbackState)) {
            return position;
        }
        long lastPositionUpdateTime = playbackState.getLastPositionUpdateTime();
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (lastPositionUpdateTime <= 0) {
            return position;
        }
        long playbackSpeed = ((long) (playbackState.getPlaybackSpeed() * ((float) (elapsedRealtime - lastPositionUpdateTime)))) + playbackState.getPosition();
        if (j < 0 || playbackSpeed <= j) {
            j = playbackSpeed < 0 ? 0 : playbackSpeed;
        }
        return j;
    }
}
