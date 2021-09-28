package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Log;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import com.google.android.systemui.elmyra.UserContentObserver;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Optional;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class UnpinNotifications extends Action {
    private boolean mHasPinnedHeadsUp;
    private final OnHeadsUpChangedListener mHeadsUpChangedListener = new OnHeadsUpChangedListener() { // from class: com.google.android.systemui.elmyra.actions.UnpinNotifications.1
        @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
        public void onHeadsUpPinnedModeChanged(boolean z) {
            if (UnpinNotifications.this.mHasPinnedHeadsUp != z) {
                UnpinNotifications.this.mHasPinnedHeadsUp = z;
                UnpinNotifications.this.notifyListener();
            }
        }
    };
    private final Optional<HeadsUpManager> mHeadsUpManagerOptional;
    private final UserContentObserver mSettingsObserver;
    private boolean mSilenceSettingEnabled;

    public UnpinNotifications(Context context, Optional<HeadsUpManager> optional) {
        super(context, null);
        this.mHeadsUpManagerOptional = optional;
        if (optional.isPresent()) {
            updateHeadsUpListener();
            this.mSettingsObserver = new UserContentObserver(getContext(), Settings.Secure.getUriFor("assist_gesture_silence_alerts_enabled"), new Consumer() { // from class: com.google.android.systemui.elmyra.actions.UnpinNotifications$$ExternalSyntheticLambda0
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    UnpinNotifications.this.lambda$new$0((Uri) obj);
                }
            });
            return;
        }
        this.mSettingsObserver = null;
        Log.w("Elmyra/UnpinNotifications", "No HeadsUpManager");
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Uri uri) {
        updateHeadsUpListener();
    }

    private void updateHeadsUpListener() {
        if (this.mHeadsUpManagerOptional.isPresent()) {
            boolean z = true;
            if (Settings.Secure.getIntForUser(getContext().getContentResolver(), "assist_gesture_silence_alerts_enabled", 1, -2) == 0) {
                z = false;
            }
            if (this.mSilenceSettingEnabled != z) {
                this.mSilenceSettingEnabled = z;
                if (z) {
                    this.mHasPinnedHeadsUp = this.mHeadsUpManagerOptional.get().hasPinnedHeadsUp();
                    this.mHeadsUpManagerOptional.get().addListener(this.mHeadsUpChangedListener);
                } else {
                    this.mHasPinnedHeadsUp = false;
                    this.mHeadsUpManagerOptional.get().removeListener(this.mHeadsUpChangedListener);
                }
                notifyListener();
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public boolean isAvailable() {
        if (this.mSilenceSettingEnabled) {
            return this.mHasPinnedHeadsUp;
        }
        return false;
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mHeadsUpManagerOptional.ifPresent(UnpinNotifications$$ExternalSyntheticLambda1.INSTANCE);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public String toString() {
        return super.toString();
    }
}
