package com.android.systemui.sensorprivacy;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import com.android.internal.app.AlertActivity;
import com.android.internal.util.FrameworkStatsLog;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import com.android.systemui.statusbar.policy.IndividualSensorPrivacyController;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SensorUseStartedActivity.kt */
/* loaded from: classes.dex */
public final class SensorUseStartedActivity extends AlertActivity implements DialogInterface.OnClickListener {
    public static final Companion Companion = new Companion(null);
    private static final String LOG_TAG = SensorUseStartedActivity.class.getSimpleName();
    private final Handler bgHandler;
    private final KeyguardDismissUtil keyguardDismissUtil;
    private final KeyguardStateController keyguardStateController;
    private int sensor = -1;
    private final IndividualSensorPrivacyController sensorPrivacyController;
    private IndividualSensorPrivacyController.Callback sensorPrivacyListener;
    private String sensorUsePackageName;
    private boolean unsuppressImmediately;

    public void onBackPressed() {
    }

    public SensorUseStartedActivity(IndividualSensorPrivacyController individualSensorPrivacyController, KeyguardStateController keyguardStateController, KeyguardDismissUtil keyguardDismissUtil, Handler handler) {
        Intrinsics.checkNotNullParameter(individualSensorPrivacyController, "sensorPrivacyController");
        Intrinsics.checkNotNullParameter(keyguardStateController, "keyguardStateController");
        Intrinsics.checkNotNullParameter(keyguardDismissUtil, "keyguardDismissUtil");
        Intrinsics.checkNotNullParameter(handler, "bgHandler");
        this.sensorPrivacyController = individualSensorPrivacyController;
        this.keyguardStateController = keyguardStateController;
        this.keyguardDismissUtil = keyguardDismissUtil;
        this.bgHandler = handler;
    }

    /* compiled from: SensorUseStartedActivity.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:39:0x00de  */
    /* JADX WARNING: Removed duplicated region for block: B:44:0x00ea A[Catch: NameNotFoundException -> 0x012d, TryCatch #0 {NameNotFoundException -> 0x012d, blocks: (B:19:0x007c, B:24:0x009c, B:25:0x009f, B:26:0x00a2, B:27:0x00a4, B:33:0x00c0, B:37:0x00d7, B:42:0x00e4, B:43:0x00e7, B:44:0x00ea, B:45:0x00ec, B:47:0x00f6, B:50:0x0127, B:51:0x012c), top: B:54:0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x00f6 A[Catch: NameNotFoundException -> 0x012d, TRY_LEAVE, TryCatch #0 {NameNotFoundException -> 0x012d, blocks: (B:19:0x007c, B:24:0x009c, B:25:0x009f, B:26:0x00a2, B:27:0x00a4, B:33:0x00c0, B:37:0x00d7, B:42:0x00e4, B:43:0x00e7, B:44:0x00ea, B:45:0x00ec, B:47:0x00f6, B:50:0x0127, B:51:0x012c), top: B:54:0x007c }] */
    /* JADX WARNING: Removed duplicated region for block: B:50:0x0127 A[Catch: NameNotFoundException -> 0x012d, TRY_ENTER, TryCatch #0 {NameNotFoundException -> 0x012d, blocks: (B:19:0x007c, B:24:0x009c, B:25:0x009f, B:26:0x00a2, B:27:0x00a4, B:33:0x00c0, B:37:0x00d7, B:42:0x00e4, B:43:0x00e7, B:44:0x00ea, B:45:0x00ec, B:47:0x00f6, B:50:0x0127, B:51:0x012c), top: B:54:0x007c }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    protected void onCreate(android.os.Bundle r9) {
        /*
        // Method dump skipped, instructions count: 305
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.sensorprivacy.SensorUseStartedActivity.onCreate(android.os.Bundle):void");
    }

    protected void onStart() {
        SensorUseStartedActivity.super.onStart();
        setSuppressed(true);
        this.unsuppressImmediately = false;
    }

    @Override // android.content.DialogInterface.OnClickListener
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == -2) {
            this.unsuppressImmediately = false;
            String str = this.sensorUsePackageName;
            if (str != null) {
                FrameworkStatsLog.write(382, 2, str);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                throw null;
            }
        } else if (i == -1) {
            if (!this.keyguardStateController.isMethodSecure() || !this.keyguardStateController.isShowing()) {
                disableSensorPrivacy();
                String str2 = this.sensorUsePackageName;
                if (str2 != null) {
                    FrameworkStatsLog.write(382, 1, str2);
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                    throw null;
                }
            } else {
                this.keyguardDismissUtil.executeWhenUnlocked(new ActivityStarter.OnDismissAction(this) { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onClick$1
                    final /* synthetic */ SensorUseStartedActivity this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.this$0 = r1;
                    }

                    @Override // com.android.systemui.plugins.ActivityStarter.OnDismissAction
                    public final boolean onDismiss() {
                        Handler access$getBgHandler$p = SensorUseStartedActivity.access$getBgHandler$p(this.this$0);
                        final SensorUseStartedActivity sensorUseStartedActivity = this.this$0;
                        access$getBgHandler$p.postDelayed(new Runnable() { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onClick$1.1
                            @Override // java.lang.Runnable
                            public final void run() {
                                SensorUseStartedActivity.access$disableSensorPrivacy(sensorUseStartedActivity);
                                String access$getSensorUsePackageName$p = SensorUseStartedActivity.access$getSensorUsePackageName$p(sensorUseStartedActivity);
                                if (access$getSensorUsePackageName$p != null) {
                                    FrameworkStatsLog.write(382, 1, access$getSensorUsePackageName$p);
                                } else {
                                    Intrinsics.throwUninitializedPropertyAccessException("sensorUsePackageName");
                                    throw null;
                                }
                            }
                        }, 200);
                        return false;
                    }
                }, false, true);
            }
        }
        dismiss();
    }

    protected void onStop() {
        SensorUseStartedActivity.super.onStop();
        if (this.unsuppressImmediately) {
            setSuppressed(false);
        } else {
            this.bgHandler.postDelayed(new Runnable(this) { // from class: com.android.systemui.sensorprivacy.SensorUseStartedActivity$onStop$1
                final /* synthetic */ SensorUseStartedActivity this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    this.this$0.setSuppressed(false);
                }
            }, 2000);
        }
    }

    protected void onDestroy() {
        SensorUseStartedActivity.super.onDestroy();
        IndividualSensorPrivacyController individualSensorPrivacyController = this.sensorPrivacyController;
        IndividualSensorPrivacyController.Callback callback = this.sensorPrivacyListener;
        if (callback != null) {
            individualSensorPrivacyController.removeCallback(callback);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("sensorPrivacyListener");
            throw null;
        }
    }

    protected void onNewIntent(Intent intent) {
        setIntent(intent);
        recreate();
    }

    public final void disableSensorPrivacy() {
        int i = this.sensor;
        if (i == Integer.MAX_VALUE) {
            this.sensorPrivacyController.setSensorBlocked(3, 1, false);
            this.sensorPrivacyController.setSensorBlocked(3, 2, false);
        } else {
            this.sensorPrivacyController.setSensorBlocked(3, i, false);
        }
        this.unsuppressImmediately = true;
        setResult(-1);
    }

    public final void setSuppressed(boolean z) {
        int i = this.sensor;
        if (i == Integer.MAX_VALUE) {
            this.sensorPrivacyController.suppressSensorPrivacyReminders(1, z);
            this.sensorPrivacyController.suppressSensorPrivacyReminders(2, z);
            return;
        }
        this.sensorPrivacyController.suppressSensorPrivacyReminders(i, z);
    }
}
