package com.android.systemui.util;

import androidx.lifecycle.LiveData;
/* compiled from: RingerModeTracker.kt */
/* loaded from: classes2.dex */
public interface RingerModeTracker {
    LiveData<Integer> getRingerMode();

    LiveData<Integer> getRingerModeInternal();
}
