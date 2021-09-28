package com.android.wallpaper.widget;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.wallpaper.picker.CategoryFragment$$ExternalSyntheticLambda4;
import com.android.wallpaper.util.TimeUtils$TimeTicker;
/* loaded from: classes.dex */
public final /* synthetic */ class LockScreenPreviewer$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ LockScreenPreviewer f$0;

    public /* synthetic */ LockScreenPreviewer$$ExternalSyntheticLambda0(LockScreenPreviewer lockScreenPreviewer, int i) {
        this.$r8$classId = i;
        this.f$0 = lockScreenPreviewer;
    }

    @Override // java.lang.Runnable
    public final void run() {
        TimeUtils$TimeTicker timeUtils$TimeTicker;
        switch (this.$r8$classId) {
            case 0:
                LockScreenPreviewer lockScreenPreviewer = this.f$0;
                Context context = lockScreenPreviewer.mContext;
                if (context != null && (timeUtils$TimeTicker = lockScreenPreviewer.mTicker) != null) {
                    context.unregisterReceiver(timeUtils$TimeTicker);
                    lockScreenPreviewer.mTicker = null;
                    return;
                }
                return;
            default:
                LockScreenPreviewer lockScreenPreviewer2 = this.f$0;
                if (lockScreenPreviewer2.mContext != null) {
                    if (((LifecycleRegistry) lockScreenPreviewer2.mLifecycle).mState.compareTo(Lifecycle.State.RESUMED) >= 0) {
                        Context context2 = lockScreenPreviewer2.mContext;
                        TimeUtils$TimeTicker timeUtils$TimeTicker2 = new TimeUtils$TimeTicker(new CategoryFragment$$ExternalSyntheticLambda4(lockScreenPreviewer2, 2));
                        context2.registerReceiver(timeUtils$TimeTicker2, new IntentFilter("android.intent.action.TIME_TICK"));
                        lockScreenPreviewer2.mTicker = timeUtils$TimeTicker2;
                        return;
                    }
                    return;
                }
                return;
        }
    }
}
