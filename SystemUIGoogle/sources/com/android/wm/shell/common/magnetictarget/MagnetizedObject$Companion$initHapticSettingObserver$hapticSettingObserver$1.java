package com.android.wm.shell.common.magnetictarget;

import android.content.Context;
import android.database.ContentObserver;
import android.os.Handler;
import android.provider.Settings;
import com.android.wm.shell.common.magnetictarget.MagnetizedObject;
/* compiled from: MagnetizedObject.kt */
/* loaded from: classes2.dex */
public final class MagnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1 extends ContentObserver {
    final /* synthetic */ Context $context;

    /* access modifiers changed from: package-private */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public MagnetizedObject$Companion$initHapticSettingObserver$hapticSettingObserver$1(Context context, Handler handler) {
        super(handler);
        this.$context = context;
    }

    @Override // android.database.ContentObserver
    public void onChange(boolean z) {
        MagnetizedObject.Companion companion = MagnetizedObject.Companion;
        boolean z2 = false;
        if (Settings.System.getIntForUser(this.$context.getContentResolver(), "haptic_feedback_enabled", 0, -2) != 0) {
            z2 = true;
        }
        MagnetizedObject.access$setSystemHapticsEnabled$cp(z2);
    }
}
