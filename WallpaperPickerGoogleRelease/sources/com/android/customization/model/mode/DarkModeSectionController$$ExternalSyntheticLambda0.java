package com.android.customization.model.mode;

import android.content.Context;
import android.content.IntentFilter;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleRegistry;
import com.android.customization.model.mode.DarkModeSectionController;
/* loaded from: classes.dex */
public final /* synthetic */ class DarkModeSectionController$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ int $r8$classId;
    public final /* synthetic */ DarkModeSectionController f$0;

    public /* synthetic */ DarkModeSectionController$$ExternalSyntheticLambda0(DarkModeSectionController darkModeSectionController, int i) {
        this.$r8$classId = i;
        this.f$0 = darkModeSectionController;
    }

    @Override // java.lang.Runnable
    public final void run() {
        DarkModeSectionController.BatterySaverStateReceiver batterySaverStateReceiver;
        switch (this.$r8$classId) {
            case 0:
                DarkModeSectionController darkModeSectionController = this.f$0;
                if (darkModeSectionController.mContext != null) {
                    if (((LifecycleRegistry) darkModeSectionController.mLifecycle).mState.compareTo(Lifecycle.State.STARTED) >= 0) {
                        darkModeSectionController.mContext.registerReceiver(darkModeSectionController.mBatterySaverStateReceiver, new IntentFilter("android.os.action.POWER_SAVE_MODE_CHANGED"));
                        return;
                    }
                    return;
                }
                return;
            default:
                DarkModeSectionController darkModeSectionController2 = this.f$0;
                Context context = darkModeSectionController2.mContext;
                if (context != null && (batterySaverStateReceiver = darkModeSectionController2.mBatterySaverStateReceiver) != null) {
                    context.unregisterReceiver(batterySaverStateReceiver);
                    return;
                }
                return;
        }
    }
}
