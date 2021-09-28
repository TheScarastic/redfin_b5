package com.google.android.systemui.power;

import android.os.IHwBinder;
/* loaded from: classes2.dex */
public final /* synthetic */ class PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0 implements IHwBinder.DeathRecipient {
    public static final /* synthetic */ PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0 INSTANCE = new PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0();

    private /* synthetic */ PowerNotificationWarningsGoogleImpl$$ExternalSyntheticLambda0() {
    }

    public final void serviceDied(long j) {
        PowerNotificationWarningsGoogleImpl.lambda$executeBypassActionWithAsync$0(j);
    }
}
