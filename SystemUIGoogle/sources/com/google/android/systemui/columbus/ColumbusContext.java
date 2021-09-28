package com.google.android.systemui.columbus;

import android.content.Context;
import android.content.pm.PackageManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusContext.kt */
/* loaded from: classes2.dex */
public final class ColumbusContext {
    public static final Companion Companion = new Companion(null);
    private final PackageManager packageManager;

    public ColumbusContext(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.packageManager = context.getPackageManager();
    }

    public final boolean isAvailable() {
        return isSupportedDevice();
    }

    private final boolean isSupportedDevice() {
        return this.packageManager.hasSystemFeature("com.google.android.feature.PIXEL_2020_EXPERIENCE");
    }

    /* compiled from: ColumbusContext.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
