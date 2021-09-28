package com.android.settingslib.deviceinfo;

import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.text.format.DateUtils;
import androidx.preference.Preference;
import com.android.settingslib.core.AbstractPreferenceController;
import com.android.settingslib.core.lifecycle.LifecycleObserver;
import com.android.settingslib.core.lifecycle.events.OnStart;
import com.android.settingslib.core.lifecycle.events.OnStop;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
public abstract class AbstractUptimePreferenceController extends AbstractPreferenceController implements LifecycleObserver, OnStart, OnStop {
    static final String KEY_UPTIME = "up_time";
    private Handler mHandler;
    private Preference mUptime;

    @Override // com.android.settingslib.core.lifecycle.events.OnStart
    public void onStart() {
        getHandler().sendEmptyMessage(500);
    }

    @Override // com.android.settingslib.core.lifecycle.events.OnStop
    public void onStop() {
        getHandler().removeMessages(500);
    }

    private Handler getHandler() {
        if (this.mHandler == null) {
            this.mHandler = new MyHandler(this);
        }
        return this.mHandler;
    }

    /* access modifiers changed from: private */
    public void updateTimes() {
        this.mUptime.setSummary(DateUtils.formatElapsedTime(SystemClock.elapsedRealtime() / 1000));
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class MyHandler extends Handler {
        private WeakReference<AbstractUptimePreferenceController> mStatus;

        public MyHandler(AbstractUptimePreferenceController abstractUptimePreferenceController) {
            this.mStatus = new WeakReference<>(abstractUptimePreferenceController);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            AbstractUptimePreferenceController abstractUptimePreferenceController = this.mStatus.get();
            if (abstractUptimePreferenceController != null) {
                if (message.what == 500) {
                    abstractUptimePreferenceController.updateTimes();
                    sendEmptyMessageDelayed(500, 1000);
                    return;
                }
                throw new IllegalStateException("Unknown message " + message.what);
            }
        }
    }
}
