package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.google.android.systemui.columbus.ColumbusSettings;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SilenceAlertsDisabled.kt */
/* loaded from: classes2.dex */
public final class SilenceAlertsDisabled extends Gate {
    private final ColumbusSettings columbusSettings;
    private final SilenceAlertsDisabled$settingsChangeListener$1 settingsChangeListener = new SilenceAlertsDisabled$settingsChangeListener$1(this);
    private boolean silenceAlertsEnabled;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public SilenceAlertsDisabled(Context context, ColumbusSettings columbusSettings) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
        this.columbusSettings = columbusSettings;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.columbusSettings.registerColumbusSettingsChangeListener(this.settingsChangeListener);
        updateSilenceAlertsEnabled(this.columbusSettings.silenceAlertsEnabled());
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.columbusSettings.unregisterColumbusSettingsChangeListener(this.settingsChangeListener);
    }

    /* access modifiers changed from: private */
    public final void updateSilenceAlertsEnabled(boolean z) {
        this.silenceAlertsEnabled = z;
        updateBlocking();
    }

    private final void updateBlocking() {
        setBlocking(!this.silenceAlertsEnabled);
    }
}
