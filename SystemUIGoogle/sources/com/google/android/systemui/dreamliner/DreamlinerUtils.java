package com.google.android.systemui.dreamliner;

import android.content.Context;
import android.text.TextUtils;
import com.android.systemui.R$string;
/* loaded from: classes2.dex */
public final class DreamlinerUtils {
    public static WirelessCharger getInstance(Context context) {
        if (context == null) {
            return null;
        }
        String string = context.getString(R$string.config_dockComponent);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        try {
            return (WirelessCharger) context.getClassLoader().loadClass(string).newInstance();
        } catch (Throwable unused) {
            return null;
        }
    }
}
