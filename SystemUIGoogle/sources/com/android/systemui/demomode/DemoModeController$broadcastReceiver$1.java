package com.android.systemui.demomode;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
import kotlin.text.StringsKt__StringsKt;
/* compiled from: DemoModeController.kt */
/* loaded from: classes.dex */
public final class DemoModeController$broadcastReceiver$1 extends BroadcastReceiver {
    final /* synthetic */ DemoModeController this$0;

    /* access modifiers changed from: package-private */
    public DemoModeController$broadcastReceiver$1(DemoModeController demoModeController) {
        this.this$0 = demoModeController;
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        Bundle extras;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if ("com.android.systemui.demo".equals(intent.getAction()) && (extras = intent.getExtras()) != null) {
            String string = extras.getString("command", "");
            Intrinsics.checkNotNullExpressionValue(string, "bundle.getString(\"command\", \"\")");
            String obj = StringsKt__StringsKt.trim(string).toString();
            Objects.requireNonNull(obj, "null cannot be cast to non-null type java.lang.String");
            String lowerCase = obj.toLowerCase();
            Intrinsics.checkNotNullExpressionValue(lowerCase, "(this as java.lang.String).toLowerCase()");
            if (lowerCase.length() != 0) {
                try {
                    this.this$0.dispatchDemoCommand(lowerCase, extras);
                } catch (Throwable th) {
                    Log.w("DemoModeController", "Error running demo command, intent=" + intent + ' ' + th);
                }
            }
        }
    }
}
