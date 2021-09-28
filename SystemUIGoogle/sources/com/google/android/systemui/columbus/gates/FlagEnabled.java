package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import com.google.android.systemui.columbus.ColumbusSettings;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: FlagEnabled.kt */
/* loaded from: classes2.dex */
public final class FlagEnabled extends Gate {
    private boolean columbusEnabled;
    private final ColumbusSettings columbusSettings;
    private final FlagEnabled$settingsChangeListener$1 settingsChangeListener = new FlagEnabled$settingsChangeListener$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public FlagEnabled(Context context, ColumbusSettings columbusSettings, Handler handler) {
        super(context, handler);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
        Intrinsics.checkNotNullParameter(handler, "handler");
        this.columbusSettings = columbusSettings;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.settingsChangeListener.onColumbusEnabledChange(this.columbusSettings.isColumbusEnabled());
        this.columbusSettings.registerColumbusSettingsChangeListener(this.settingsChangeListener);
        this.columbusEnabled = this.columbusSettings.isColumbusEnabled();
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.columbusSettings.unregisterColumbusSettingsChangeListener(this.settingsChangeListener);
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(!this.columbusEnabled);
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public String toString() {
        return super.toString() + " [columbusEnabled -> " + this.columbusEnabled + ']';
    }
}
