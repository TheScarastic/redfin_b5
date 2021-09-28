package com.google.android.systemui.columbus.feedback;

import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: HapticClick.kt */
/* loaded from: classes2.dex */
public final class HapticClick implements FeedbackEffect {
    public static final Companion Companion = new Companion(null);
    private static final VibrationEffect GESTURE_DETECTED_VIBRATION_EFFECT = VibrationEffect.get(5);
    private static final AudioAttributes SONIFICATION_AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private final Lazy<Vibrator> vibrator;

    public HapticClick(Lazy<Vibrator> lazy) {
        Intrinsics.checkNotNullParameter(lazy, "vibrator");
        this.vibrator = lazy;
    }

    @Override // com.google.android.systemui.columbus.feedback.FeedbackEffect
    public void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        if (!Intrinsics.areEqual(detectionProperties == null ? null : Boolean.valueOf(detectionProperties.isHapticConsumed()), Boolean.TRUE)) {
            doHapticForGesture(i);
        }
    }

    private final void doHapticForGesture(int i) {
        if (i == 1) {
            this.vibrator.get().vibrate(GESTURE_DETECTED_VIBRATION_EFFECT, SONIFICATION_AUDIO_ATTRIBUTES);
        }
    }

    /* compiled from: HapticClick.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
