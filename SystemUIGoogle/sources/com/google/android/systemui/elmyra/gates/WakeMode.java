package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import com.google.android.systemui.elmyra.UserContentObserver;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class WakeMode extends PowerState {
    private final UserContentObserver mSettingsObserver = new UserContentObserver(getContext(), Settings.Secure.getUriFor("assist_gesture_wake_enabled"), new Consumer() { // from class: com.google.android.systemui.elmyra.gates.WakeMode$$ExternalSyntheticLambda0
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            WakeMode.m642$r8$lambda$ASpt847HMZiqy0FCpyS4vipI(WakeMode.this, (Uri) obj);
        }
    }, false);
    private boolean mWakeSettingEnabled;

    public WakeMode(Context context) {
        super(context);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Uri uri) {
        updateWakeSetting();
    }

    @Override // com.google.android.systemui.elmyra.gates.PowerState, com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        this.mWakeSettingEnabled = isWakeSettingEnabled();
        this.mSettingsObserver.activate();
    }

    @Override // com.google.android.systemui.elmyra.gates.PowerState, com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        this.mSettingsObserver.deactivate();
    }

    private void updateWakeSetting() {
        boolean isWakeSettingEnabled = isWakeSettingEnabled();
        if (isWakeSettingEnabled != this.mWakeSettingEnabled) {
            this.mWakeSettingEnabled = isWakeSettingEnabled;
            notifyListener();
        }
    }

    private boolean isWakeSettingEnabled() {
        return Settings.Secure.getIntForUser(getContext().getContentResolver(), "assist_gesture_wake_enabled", 1, -2) != 0;
    }

    /* access modifiers changed from: protected */
    @Override // com.google.android.systemui.elmyra.gates.PowerState, com.google.android.systemui.elmyra.gates.Gate
    public boolean isBlocked() {
        if (this.mWakeSettingEnabled) {
            return false;
        }
        return super.isBlocked();
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    public String toString() {
        return super.toString() + " [mWakeSettingEnabled -> " + this.mWakeSettingEnabled + "]";
    }
}
