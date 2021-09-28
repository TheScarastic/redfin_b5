package com.android.systemui.controls.management;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.UserHandle;
import android.util.Log;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsRequestReceiver.kt */
/* loaded from: classes.dex */
public final class ControlsRequestReceiver extends BroadcastReceiver {
    public static final Companion Companion = new Companion(null);

    /* compiled from: ControlsRequestReceiver.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean isPackageInForeground(Context context, String str) {
            int i;
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(str, "packageName");
            try {
                int packageUid = context.getPackageManager().getPackageUid(str, 0);
                ActivityManager activityManager = (ActivityManager) context.getSystemService(ActivityManager.class);
                if (activityManager == null) {
                    i = 1000;
                } else {
                    i = activityManager.getUidImportance(packageUid);
                }
                if (i == 100) {
                    return true;
                }
                Log.w("ControlsRequestReceiver", "Uid " + packageUid + " not in foreground");
                return false;
            } catch (PackageManager.NameNotFoundException unused) {
                Log.w("ControlsRequestReceiver", "Package " + str + " not found");
                return false;
            }
        }
    }

    @Override // android.content.BroadcastReceiver
    public void onReceive(Context context, Intent intent) {
        String str;
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(intent, "intent");
        if (context.getPackageManager().hasSystemFeature("android.software.controls")) {
            ComponentName componentName = (ComponentName) intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME");
            if (componentName == null) {
                str = null;
            } else {
                str = componentName.getPackageName();
            }
            if (str != null && Companion.isPackageInForeground(context, str)) {
                Intent intent2 = new Intent(context, ControlsRequestDialog.class);
                intent2.putExtra("android.intent.extra.COMPONENT_NAME", intent.getParcelableExtra("android.intent.extra.COMPONENT_NAME"));
                intent2.putExtra("android.service.controls.extra.CONTROL", intent.getParcelableExtra("android.service.controls.extra.CONTROL"));
                intent2.addFlags(268566528);
                intent2.putExtra("android.intent.extra.USER_ID", context.getUserId());
                context.startActivityAsUser(intent2, UserHandle.SYSTEM);
            }
        }
    }
}
