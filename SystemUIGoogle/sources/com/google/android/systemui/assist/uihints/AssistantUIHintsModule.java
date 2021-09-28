package com.google.android.systemui.assist.uihints;

import android.content.Intent;
import android.util.Log;
import android.view.ViewGroup;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsController;
import com.google.android.systemui.assist.uihints.input.NgaInputHandler;
import dagger.Lazy;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes2.dex */
public abstract class AssistantUIHintsModule {
    /* access modifiers changed from: package-private */
    public static Set<NgaMessageHandler.AudioInfoListener> provideAudioInfoListeners(EdgeLightsController edgeLightsController, GlowController glowController) {
        return new HashSet(Arrays.asList(edgeLightsController, glowController));
    }

    /* access modifiers changed from: package-private */
    public static Set<NgaMessageHandler.CardInfoListener> provideCardInfoListeners(GlowController glowController, ScrimController scrimController, TranscriptionController transcriptionController, LightnessProvider lightnessProvider) {
        return new HashSet(Arrays.asList(glowController, scrimController, transcriptionController, lightnessProvider));
    }

    /* access modifiers changed from: package-private */
    public static Set<NgaMessageHandler.ConfigInfoListener> provideConfigInfoListeners(AssistantPresenceHandler assistantPresenceHandler, TouchInsideHandler touchInsideHandler, TouchOutsideHandler touchOutsideHandler, TaskStackNotifier taskStackNotifier, KeyboardMonitor keyboardMonitor, ColorChangeHandler colorChangeHandler, ConfigurationHandler configurationHandler) {
        return new HashSet(Arrays.asList(assistantPresenceHandler, touchInsideHandler, touchOutsideHandler, taskStackNotifier, keyboardMonitor, colorChangeHandler, configurationHandler));
    }

    /* access modifiers changed from: package-private */
    public static Set<NgaMessageHandler.EdgeLightsInfoListener> bindEdgeLightsInfoListeners(EdgeLightsController edgeLightsController, NgaInputHandler ngaInputHandler) {
        return new HashSet(Arrays.asList(edgeLightsController, ngaInputHandler));
    }

    /* access modifiers changed from: package-private */
    public static NgaMessageHandler.StartActivityInfoListener provideActivityStarter(final Lazy<StatusBar> lazy) {
        return new NgaMessageHandler.StartActivityInfoListener() { // from class: com.google.android.systemui.assist.uihints.AssistantUIHintsModule.1
            @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.StartActivityInfoListener
            public void onStartActivityInfo(Intent intent, boolean z) {
                if (intent == null) {
                    Log.e("ActivityStarter", "Null intent; cannot start activity");
                } else {
                    ((StatusBar) Lazy.this.get()).startActivity(intent, z);
                }
            }
        };
    }

    /* access modifiers changed from: package-private */
    public static ViewGroup provideParentViewGroup(OverlayUiHost overlayUiHost) {
        return overlayUiHost.getParent();
    }
}
