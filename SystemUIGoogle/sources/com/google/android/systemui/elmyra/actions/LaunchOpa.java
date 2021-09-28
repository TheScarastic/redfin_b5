package com.google.android.systemui.elmyra.actions;

import android.app.KeyguardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import com.android.systemui.Dependency;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.tuner.TunerService;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.assist.OpaEnabledListener;
import com.google.android.systemui.elmyra.UserContentObserver;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class LaunchOpa extends Action implements TunerService.Tunable {
    private final AssistManager mAssistManager;
    private boolean mEnableForAnyAssistant;
    private boolean mIsGestureEnabled;
    private boolean mIsOpaEnabled;
    private final KeyguardManager mKeyguardManager;
    private final OpaEnabledListener mOpaEnabledListener;
    private final UserContentObserver mSettingsObserver;
    private final StatusBar mStatusBar;

    private LaunchOpa(Context context, StatusBar statusBar, List<FeedbackEffect> list) {
        super(context, list);
        AnonymousClass1 r4 = new OpaEnabledListener() { // from class: com.google.android.systemui.elmyra.actions.LaunchOpa.1
            @Override // com.google.android.systemui.assist.OpaEnabledListener
            public void onOpaEnabledReceived(Context context2, boolean z, boolean z2, boolean z3, boolean z4) {
                boolean z5 = false;
                boolean z6 = z2 || LaunchOpa.this.mEnableForAnyAssistant;
                if (z && z6 && z3) {
                    z5 = true;
                }
                if (LaunchOpa.this.mIsOpaEnabled != z5) {
                    LaunchOpa.this.mIsOpaEnabled = z5;
                    LaunchOpa.this.notifyListener();
                }
            }
        };
        this.mOpaEnabledListener = r4;
        this.mStatusBar = statusBar;
        AssistManager assistManager = (AssistManager) Dependency.get(AssistManager.class);
        this.mAssistManager = assistManager;
        this.mKeyguardManager = (KeyguardManager) getContext().getSystemService("keyguard");
        this.mIsGestureEnabled = isGestureEnabled();
        this.mSettingsObserver = new UserContentObserver(getContext(), Settings.Secure.getUriFor("assist_gesture_enabled"), new Consumer() { // from class: com.google.android.systemui.elmyra.actions.LaunchOpa$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                LaunchOpa.this.lambda$new$0((Uri) obj);
            }
        });
        ((TunerService) Dependency.get(TunerService.class)).addTunable(this, "assist_gesture_any_assistant");
        this.mEnableForAnyAssistant = Settings.Secure.getInt(getContext().getContentResolver(), "assist_gesture_any_assistant", 0) == 1;
        ((AssistManagerGoogle) assistManager).addOpaEnabledListener(r4);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Uri uri) {
        updateGestureEnabled();
    }

    private void updateGestureEnabled() {
        boolean isGestureEnabled = isGestureEnabled();
        if (this.mIsGestureEnabled != isGestureEnabled) {
            this.mIsGestureEnabled = isGestureEnabled;
            notifyListener();
        }
    }

    private boolean isGestureEnabled() {
        if (Settings.Secure.getIntForUser(getContext().getContentResolver(), "assist_gesture_enabled", 1, -2) != 0) {
            return true;
        }
        return false;
    }

    @Override // com.android.systemui.tuner.TunerService.Tunable
    public void onTuningChanged(String str, String str2) {
        if ("assist_gesture_any_assistant".equals(str)) {
            this.mEnableForAnyAssistant = "1".equals(str2);
            ((AssistManagerGoogle) this.mAssistManager).dispatchOpaEnabledState();
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public boolean isAvailable() {
        return this.mIsGestureEnabled && this.mIsOpaEnabled;
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onProgress(float f, int i) {
        updateFeedbackEffects(f, i);
    }

    public void launchOpa() {
        launchOpa(0);
    }

    public void launchOpa(long j) {
        Bundle bundle = new Bundle();
        bundle.putInt("triggered_by", this.mKeyguardManager.isKeyguardLocked() ? 14 : 13);
        bundle.putLong("latency_id", j);
        bundle.putInt("invocation_type", 2);
        this.mAssistManager.startAssist(bundle);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mStatusBar.collapseShade();
        triggerFeedbackEffects(detectionProperties);
        launchOpa(detectionProperties != null ? detectionProperties.getActionId() : 0);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public String toString() {
        return super.toString() + " [mIsGestureEnabled -> " + this.mIsGestureEnabled + "; mIsOpaEnabled -> " + this.mIsOpaEnabled + "]";
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private final Context mContext;
        List<FeedbackEffect> mFeedbackEffects = new ArrayList();
        private final StatusBar mStatusBar;

        public Builder(Context context, StatusBar statusBar) {
            this.mContext = context;
            this.mStatusBar = statusBar;
        }

        public Builder addFeedbackEffect(FeedbackEffect feedbackEffect) {
            this.mFeedbackEffects.add(feedbackEffect);
            return this;
        }

        public LaunchOpa build() {
            return new LaunchOpa(this.mContext, this.mStatusBar, this.mFeedbackEffects);
        }
    }
}
