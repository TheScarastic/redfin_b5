package com.google.android.systemui.face;

import android.content.Context;
import android.provider.Settings;
/* loaded from: classes2.dex */
class FaceNotificationSettings {
    /* access modifiers changed from: package-private */
    public static boolean isReenrollRequired(Context context) {
        return Settings.Secure.getIntForUser(context.getContentResolver(), "face_unlock_re_enroll", 0, -2) == 3;
    }

    /* access modifiers changed from: package-private */
    public static void updateReenrollSetting(Context context, int i) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "face_unlock_re_enroll", i, -2);
    }
}
