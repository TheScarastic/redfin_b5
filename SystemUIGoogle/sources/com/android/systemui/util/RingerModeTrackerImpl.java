package com.android.systemui.util;

import android.media.AudioManager;
import androidx.lifecycle.LiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RingerModeTrackerImpl.kt */
/* loaded from: classes2.dex */
public final class RingerModeTrackerImpl implements RingerModeTracker {
    private final LiveData<Integer> ringerMode;
    private final LiveData<Integer> ringerModeInternal;

    public RingerModeTrackerImpl(AudioManager audioManager, BroadcastDispatcher broadcastDispatcher, Executor executor) {
        Intrinsics.checkNotNullParameter(audioManager, "audioManager");
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.ringerMode = new RingerModeLiveData(broadcastDispatcher, executor, "android.media.RINGER_MODE_CHANGED", new Function0<Integer>(audioManager) { // from class: com.android.systemui.util.RingerModeTrackerImpl$ringerMode$1
            /* Return type fixed from 'int' to match base method */
            /* JADX WARN: Type inference failed for: r0v3, types: [int, java.lang.Integer] */
            @Override // kotlin.jvm.functions.Function0
            public final Integer invoke() {
                return ((AudioManager) this.receiver).getRingerMode();
            }
        });
        this.ringerModeInternal = new RingerModeLiveData(broadcastDispatcher, executor, "android.media.INTERNAL_RINGER_MODE_CHANGED_ACTION", new Function0<Integer>(audioManager) { // from class: com.android.systemui.util.RingerModeTrackerImpl$ringerModeInternal$1
            /* Return type fixed from 'int' to match base method */
            /* JADX WARN: Type inference failed for: r0v3, types: [int, java.lang.Integer] */
            @Override // kotlin.jvm.functions.Function0
            public final Integer invoke() {
                return ((AudioManager) this.receiver).getRingerModeInternal();
            }
        });
    }

    @Override // com.android.systemui.util.RingerModeTracker
    public LiveData<Integer> getRingerMode() {
        return this.ringerMode;
    }

    @Override // com.android.systemui.util.RingerModeTracker
    public LiveData<Integer> getRingerModeInternal() {
        return this.ringerModeInternal;
    }
}
