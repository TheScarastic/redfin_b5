package com.android.wallpaper.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda4;
import com.android.wallpaper.widget.LockScreenPreviewer;
import java.util.concurrent.ExecutorService;
/* loaded from: classes.dex */
public class TimeUtils$TimeTicker extends BroadcastReceiver {
    public TimeListener mListener;

    /* loaded from: classes.dex */
    public interface TimeListener {
    }

    public TimeUtils$TimeTicker(TimeListener timeListener) {
        this.mListener = timeListener;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        TimeListener timeListener = this.mListener;
        if (timeListener != null) {
            LockScreenPreviewer lockScreenPreviewer = ((CategoryFragment$$ExternalSyntheticLambda4) timeListener).f$0;
            ExecutorService executorService = LockScreenPreviewer.sExecutorService;
            lockScreenPreviewer.updateDateTime();
        }
    }
}
