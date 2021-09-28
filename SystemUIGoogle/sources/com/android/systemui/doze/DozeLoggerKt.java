package com.android.systemui.doze;

import java.text.SimpleDateFormat;
import java.util.Locale;
/* compiled from: DozeLogger.kt */
/* loaded from: classes.dex */
public final class DozeLoggerKt {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("MM-dd HH:mm:ss.S", Locale.US);

    public static final SimpleDateFormat getDATE_FORMAT() {
        return DATE_FORMAT;
    }
}
