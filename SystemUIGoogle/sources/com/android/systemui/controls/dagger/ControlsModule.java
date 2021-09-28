package com.android.systemui.controls.dagger;

import android.content.pm.PackageManager;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModule.kt */
/* loaded from: classes.dex */
public abstract class ControlsModule {
    public static final Companion Companion = new Companion(null);

    public static final boolean providesControlsFeatureEnabled(PackageManager packageManager) {
        return Companion.providesControlsFeatureEnabled(packageManager);
    }

    /* compiled from: ControlsModule.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final boolean providesControlsFeatureEnabled(PackageManager packageManager) {
            Intrinsics.checkNotNullParameter(packageManager, "pm");
            return packageManager.hasSystemFeature("android.software.controls");
        }
    }
}
