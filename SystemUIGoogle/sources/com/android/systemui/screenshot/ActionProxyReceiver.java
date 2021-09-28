package com.android.systemui.screenshot;

import android.app.ActivityOptions;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.RemoteAnimationAdapter;
import android.view.WindowManagerGlobal;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.Optional;
/* loaded from: classes.dex */
public class ActionProxyReceiver extends BroadcastReceiver {
    private final ActivityManagerWrapper mActivityManagerWrapper;
    private final ScreenshotSmartActions mScreenshotSmartActions;
    private final StatusBar mStatusBar;

    public ActionProxyReceiver(Optional<StatusBar> optional, ActivityManagerWrapper activityManagerWrapper, ScreenshotSmartActions screenshotSmartActions) {
        this.mStatusBar = optional.orElse(null);
        this.mActivityManagerWrapper = activityManagerWrapper;
        this.mScreenshotSmartActions = screenshotSmartActions;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        ActionProxyReceiver$$ExternalSyntheticLambda0 actionProxyReceiver$$ExternalSyntheticLambda0 = new Runnable(intent, context) { // from class: com.android.systemui.screenshot.ActionProxyReceiver$$ExternalSyntheticLambda0
            public final /* synthetic */ Intent f$1;
            public final /* synthetic */ Context f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ActionProxyReceiver.$r8$lambda$ukuT0qmNYLdDpxZysWn0ab2Dw18(ActionProxyReceiver.this, this.f$1, this.f$2);
            }
        };
        StatusBar statusBar = this.mStatusBar;
        if (statusBar != null) {
            statusBar.executeRunnableDismissingKeyguard(actionProxyReceiver$$ExternalSyntheticLambda0, null, true, true, true);
        } else {
            actionProxyReceiver$$ExternalSyntheticLambda0.run();
        }
        if (intent.getBooleanExtra("android:smart_actions_enabled", false)) {
            this.mScreenshotSmartActions.notifyScreenshotAction(context, intent.getStringExtra("android:screenshot_id"), "android.intent.action.EDIT".equals(intent.getAction()) ? "Edit" : "Share", false, null);
        }
    }

    public /* synthetic */ void lambda$onReceive$0(Intent intent, Context context) {
        this.mActivityManagerWrapper.closeSystemWindows("screenshot");
        PendingIntent pendingIntent = (PendingIntent) intent.getParcelableExtra("android:screenshot_action_intent");
        ActivityOptions makeBasic = ActivityOptions.makeBasic();
        makeBasic.setDisallowEnterPictureInPictureWhileLaunching(intent.getBooleanExtra("android:screenshot_disallow_enter_pip", false));
        try {
            pendingIntent.send(context, 0, null, null, null, null, makeBasic.toBundle());
            if (intent.getBooleanExtra("android:screenshot_override_transition", false)) {
                try {
                    WindowManagerGlobal.getWindowManagerService().overridePendingAppTransitionRemote(new RemoteAnimationAdapter(ScreenshotController.SCREENSHOT_REMOTE_RUNNER, 0, 0), 0);
                } catch (Exception e) {
                    Log.e("ActionProxyReceiver", "Error overriding screenshot app transition", e);
                }
            }
        } catch (PendingIntent.CanceledException e2) {
            Log.e("ActionProxyReceiver", "Pending intent canceled", e2);
        }
    }
}
