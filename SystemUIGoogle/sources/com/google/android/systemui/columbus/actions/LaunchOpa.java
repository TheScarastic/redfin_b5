package com.google.android.systemui.columbus.actions;

import android.app.KeyguardManager;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import androidx.appcompat.R$styleable;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.assist.AssistManager;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.tuner.TunerService;
import com.google.android.systemui.assist.AssistManagerGoogle;
import com.google.android.systemui.assist.OpaEnabledListener;
import com.google.android.systemui.columbus.ColumbusContentObserver;
import com.google.android.systemui.columbus.ColumbusEvent;
import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import java.util.Set;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LaunchOpa.kt */
/* loaded from: classes2.dex */
public class LaunchOpa extends UserAction {
    public static final Companion Companion = new Companion(null);
    private final AssistManagerGoogle assistManager;
    private boolean enableForAnyAssistant;
    private boolean isGestureEnabled;
    private boolean isOpaEnabled;
    private final Lazy<KeyguardManager> keyguardManager;
    private final OpaEnabledListener opaEnabledListener;
    private final ColumbusContentObserver settingsObserver;
    private final StatusBar statusBar;
    private final String tag = "Columbus/LaunchOpa";
    private final TunerService.Tunable tunable;
    private final UiEventLogger uiEventLogger;

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public boolean availableOnLockscreen() {
        return true;
    }

    @Override // com.google.android.systemui.columbus.actions.UserAction
    public boolean availableOnScreenOff() {
        return true;
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public LaunchOpa(Context context, StatusBar statusBar, Set<FeedbackEffect> set, AssistManager assistManager, Lazy<KeyguardManager> lazy, TunerService tunerService, ColumbusContentObserver.Factory factory, UiEventLogger uiEventLogger) {
        super(context, set);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(statusBar, "statusBar");
        Intrinsics.checkNotNullParameter(set, "feedbackEffects");
        Intrinsics.checkNotNullParameter(assistManager, "assistManager");
        Intrinsics.checkNotNullParameter(lazy, "keyguardManager");
        Intrinsics.checkNotNullParameter(tunerService, "tunerService");
        Intrinsics.checkNotNullParameter(factory, "settingsObserverFactory");
        Intrinsics.checkNotNullParameter(uiEventLogger, "uiEventLogger");
        this.statusBar = statusBar;
        this.keyguardManager = lazy;
        this.uiEventLogger = uiEventLogger;
        AssistManagerGoogle assistManagerGoogle = assistManager instanceof AssistManagerGoogle ? (AssistManagerGoogle) assistManager : null;
        this.assistManager = assistManagerGoogle;
        LaunchOpa$opaEnabledListener$1 launchOpa$opaEnabledListener$1 = new OpaEnabledListener(this) { // from class: com.google.android.systemui.columbus.actions.LaunchOpa$opaEnabledListener$1
            final /* synthetic */ LaunchOpa this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.google.android.systemui.assist.OpaEnabledListener
            public final void onOpaEnabledReceived(Context context2, boolean z, boolean z2, boolean z3, boolean z4) {
                boolean z5 = false;
                boolean z6 = z2 || (this.this$0.enableForAnyAssistant);
                Log.i("Columbus/LaunchOpa", "eligible: " + z + ", supported: " + z6 + ", opa: " + z3);
                LaunchOpa launchOpa = this.this$0;
                if (z && z6 && z3) {
                    z5 = true;
                }
                launchOpa.isOpaEnabled = z5;
                this.this$0.updateAvailable();
            }
        };
        this.opaEnabledListener = launchOpa$opaEnabledListener$1;
        Uri uriFor = Settings.Secure.getUriFor("assist_gesture_enabled");
        Intrinsics.checkNotNullExpressionValue(uriFor, "getUriFor(Settings.Secure.ASSIST_GESTURE_ENABLED)");
        ColumbusContentObserver create = factory.create(uriFor, new Function1<Uri, Unit>(this) { // from class: com.google.android.systemui.columbus.actions.LaunchOpa$settingsObserver$1
            final /* synthetic */ LaunchOpa this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ Unit invoke(Uri uri) {
                invoke(uri);
                return Unit.INSTANCE;
            }

            public final void invoke(Uri uri) {
                Intrinsics.checkNotNullParameter(uri, "it");
                this.this$0.updateGestureEnabled();
            }
        });
        this.settingsObserver = create;
        LaunchOpa$tunable$1 launchOpa$tunable$1 = new TunerService.Tunable(this) { // from class: com.google.android.systemui.columbus.actions.LaunchOpa$tunable$1
            final /* synthetic */ LaunchOpa this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // com.android.systemui.tuner.TunerService.Tunable
            public final void onTuningChanged(String str, String str2) {
                if (Intrinsics.areEqual("assist_gesture_any_assistant", str)) {
                    this.this$0.enableForAnyAssistant = Intrinsics.areEqual("1", str2);
                    AssistManagerGoogle assistManagerGoogle2 = this.this$0.assistManager;
                    if (assistManagerGoogle2 != null) {
                        assistManagerGoogle2.dispatchOpaEnabledState();
                    }
                }
            }
        };
        this.tunable = launchOpa$tunable$1;
        this.isGestureEnabled = fetchIsGestureEnabled();
        this.enableForAnyAssistant = Settings.Secure.getInt(getContext().getContentResolver(), "assist_gesture_any_assistant", 0) == 1;
        create.activate();
        tunerService.addTunable(launchOpa$tunable$1, "assist_gesture_any_assistant");
        if (assistManagerGoogle != null) {
            assistManagerGoogle.addOpaEnabledListener(launchOpa$opaEnabledListener$1);
        }
        updateAvailable();
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String getTag$vendor__unbundled_google__packages__SystemUIGoogle__android_common__sysuig() {
        return this.tag;
    }

    /* access modifiers changed from: private */
    public final void updateGestureEnabled() {
        this.isGestureEnabled = fetchIsGestureEnabled();
        updateAvailable();
    }

    private final boolean fetchIsGestureEnabled() {
        if (Settings.Secure.getIntForUser(getContext().getContentResolver(), "assist_gesture_enabled", 1, -2) != 0) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public final void updateAvailable() {
        setAvailable(this.isGestureEnabled && this.isOpaEnabled);
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        long j;
        this.uiEventLogger.log(ColumbusEvent.COLUMBUS_INVOKED_ASSISTANT);
        this.statusBar.collapseShade();
        if (detectionProperties == null) {
            j = 0;
        } else {
            j = detectionProperties.getActionId();
        }
        launchOpa(j);
    }

    private final void launchOpa(long j) {
        Bundle bundle = new Bundle();
        bundle.putInt("triggered_by", this.keyguardManager.get().isKeyguardLocked() ? R$styleable.AppCompatTheme_windowFixedHeightMajor : R$styleable.AppCompatTheme_windowActionModeOverlay);
        bundle.putLong("latency_id", j);
        bundle.putInt("invocation_type", 2);
        AssistManagerGoogle assistManagerGoogle = this.assistManager;
        if (assistManagerGoogle != null) {
            assistManagerGoogle.startAssist(bundle);
        }
    }

    @Override // com.google.android.systemui.columbus.actions.Action
    public String toString() {
        return super.toString() + " [isGestureEnabled -> " + this.isGestureEnabled + "; isOpaEnabled -> " + this.isOpaEnabled + ']';
    }

    /* compiled from: LaunchOpa.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
