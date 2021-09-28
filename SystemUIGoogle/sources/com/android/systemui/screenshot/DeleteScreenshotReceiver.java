package com.android.systemui.screenshot;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class DeleteScreenshotReceiver extends BroadcastReceiver {
    private final Executor mBackgroundExecutor;
    private final ScreenshotSmartActions mScreenshotSmartActions;

    public DeleteScreenshotReceiver(ScreenshotSmartActions screenshotSmartActions, Executor executor) {
        this.mScreenshotSmartActions = screenshotSmartActions;
        this.mBackgroundExecutor = executor;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        if (intent.hasExtra("android:screenshot_uri_id")) {
            this.mBackgroundExecutor.execute(new Runnable(context, Uri.parse(intent.getStringExtra("android:screenshot_uri_id"))) { // from class: com.android.systemui.screenshot.DeleteScreenshotReceiver$$ExternalSyntheticLambda0
                public final /* synthetic */ Context f$0;
                public final /* synthetic */ Uri f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    DeleteScreenshotReceiver.$r8$lambda$CEZI5sqJSK54hW_kaFE5KM0z6bY(this.f$0, this.f$1);
                }
            });
            if (intent.getBooleanExtra("android:smart_actions_enabled", false)) {
                this.mScreenshotSmartActions.notifyScreenshotAction(context, intent.getStringExtra("android:screenshot_id"), "Delete", false, null);
            }
        }
    }

    public static /* synthetic */ void lambda$onReceive$0(Context context, Uri uri) {
        context.getContentResolver().delete(uri, null, null);
    }
}
