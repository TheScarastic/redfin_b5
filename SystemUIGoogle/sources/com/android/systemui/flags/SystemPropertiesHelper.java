package com.android.systemui.flags;

import android.os.SystemProperties;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SystemPropertiesHelper.kt */
/* loaded from: classes.dex */
public final class SystemPropertiesHelper {
    public final boolean getBoolean(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "name");
        return SystemProperties.getBoolean(str, z);
    }
}
