package com.android.systemui.navigationbar;

import android.content.Context;
import android.os.SystemClock;
import android.util.Slog;
import android.widget.Toast;
import com.android.systemui.R$string;
import com.android.systemui.SysUIToast;
/* loaded from: classes.dex */
public class ScreenPinningNotify {
    private final Context mContext;
    private long mLastShowToastTime;
    private Toast mLastToast;

    public ScreenPinningNotify(Context context) {
        this.mContext = context;
    }

    public void showPinningStartToast() {
        makeAllUserToastAndShow(R$string.screen_pinning_start);
    }

    public void showPinningExitToast() {
        makeAllUserToastAndShow(R$string.screen_pinning_exit);
    }

    public void showEscapeToast(boolean z, boolean z2) {
        int i;
        long elapsedRealtime = SystemClock.elapsedRealtime();
        if (elapsedRealtime - this.mLastShowToastTime < 1000) {
            Slog.i("ScreenPinningNotify", "Ignore toast since it is requested in very short interval.");
            return;
        }
        Toast toast = this.mLastToast;
        if (toast != null) {
            toast.cancel();
        }
        if (z) {
            i = R$string.screen_pinning_toast_gesture_nav;
        } else if (z2) {
            i = R$string.screen_pinning_toast;
        } else {
            i = R$string.screen_pinning_toast_recents_invisible;
        }
        this.mLastToast = makeAllUserToastAndShow(i);
        this.mLastShowToastTime = elapsedRealtime;
    }

    private Toast makeAllUserToastAndShow(int i) {
        Toast makeText = SysUIToast.makeText(this.mContext, i, 1);
        makeText.show();
        return makeText;
    }
}
