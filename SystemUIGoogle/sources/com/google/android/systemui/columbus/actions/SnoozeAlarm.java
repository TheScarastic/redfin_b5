package com.google.android.systemui.columbus.actions;

import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import com.google.android.systemui.columbus.gates.SilenceAlertsDisabled;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SnoozeAlarm.kt */
/* loaded from: classes2.dex */
public final class SnoozeAlarm extends DeskClockAction {
    public static final Companion Companion = new Companion(null);
    private final String tag = "Columbus/SnoozeAlarm";

    /* access modifiers changed from: protected */
    @Override // com.google.android.systemui.columbus.actions.DeskClockAction
    public String getAlertAction() {
        return "com.google.android.deskclock.action.ALARM_ALERT";
    }

    /* access modifiers changed from: protected */
    @Override // com.google.android.systemui.columbus.actions.DeskClockAction
    public String getDoneAction() {
        return "com.google.android.deskclock.action.ALARM_DONE";
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public SnoozeAlarm(Context context, SilenceAlertsDisabled silenceAlertsDisabled, IActivityManager iActivityManager) {
        super(context, silenceAlertsDisabled, iActivityManager);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(silenceAlertsDisabled, "silenceAlertsDisabled");
        Intrinsics.checkNotNullParameter(iActivityManager, "activityManagerService");
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    @Override // com.google.android.systemui.columbus.actions.DeskClockAction
    protected Intent createDismissIntent() {
        return new Intent("android.intent.action.SNOOZE_ALARM");
    }

    /* compiled from: SnoozeAlarm.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
