package com.google.android.systemui.smartspace;

import android.content.ComponentName;
import android.content.Context;
import com.android.systemui.statusbar.FeatureFlags;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardSmartspaceController.kt */
/* loaded from: classes2.dex */
public final class KeyguardSmartspaceController {
    private final Context context;
    private final FeatureFlags featureFlags;
    private final KeyguardMediaViewController mediaController;
    private final KeyguardZenAlarmViewController zenController;

    public KeyguardSmartspaceController(Context context, FeatureFlags featureFlags, KeyguardZenAlarmViewController keyguardZenAlarmViewController, KeyguardMediaViewController keyguardMediaViewController) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(keyguardZenAlarmViewController, "zenController");
        Intrinsics.checkNotNullParameter(keyguardMediaViewController, "mediaController");
        this.context = context;
        this.featureFlags = featureFlags;
        this.zenController = keyguardZenAlarmViewController;
        this.mediaController = keyguardMediaViewController;
        if (!featureFlags.isSmartspaceEnabled()) {
            context.getPackageManager().setComponentEnabledSetting(new ComponentName("com.android.systemui", "com.google.android.systemui.keyguard.KeyguardSliceProviderGoogle"), 1, 1);
            return;
        }
        keyguardZenAlarmViewController.init();
        keyguardMediaViewController.init();
    }
}
