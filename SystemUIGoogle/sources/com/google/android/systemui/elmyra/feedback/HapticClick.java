package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import android.content.res.Resources;
import android.media.AudioAttributes;
import android.os.VibrationEffect;
import android.os.Vibrator;
import com.android.systemui.R$integer;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
/* loaded from: classes2.dex */
public class HapticClick implements FeedbackEffect {
    private static final AudioAttributes SONIFICATION_AUDIO_ATTRIBUTES = new AudioAttributes.Builder().setContentType(4).setUsage(13).build();
    private int mLastGestureStage;
    private final VibrationEffect mProgressVibrationEffect;
    private final VibrationEffect mResolveVibrationEffect = VibrationEffect.get(0);
    private final Vibrator mVibrator;

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onRelease() {
    }

    public HapticClick(Context context) {
        Vibrator vibrator = (Vibrator) context.getSystemService("vibrator");
        this.mVibrator = vibrator;
        VibrationEffect vibrationEffect = VibrationEffect.get(5);
        this.mProgressVibrationEffect = vibrationEffect;
        if (vibrator != null) {
            try {
                vibrator.setAlwaysOnEffect(context.getResources().getInteger(R$integer.elmyra_progress_always_on_vibration), vibrationEffect, SONIFICATION_AUDIO_ATTRIBUTES);
            } catch (Resources.NotFoundException unused) {
            }
            try {
                this.mVibrator.setAlwaysOnEffect(context.getResources().getInteger(R$integer.elmyra_resolve_always_on_vibration), this.mResolveVibrationEffect, SONIFICATION_AUDIO_ATTRIBUTES);
            } catch (Resources.NotFoundException unused2) {
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onProgress(float f, int i) {
        Vibrator vibrator;
        if (!(this.mLastGestureStage == 2 || i != 2 || (vibrator = this.mVibrator) == null)) {
            vibrator.vibrate(this.mProgressVibrationEffect, SONIFICATION_AUDIO_ATTRIBUTES);
        }
        this.mLastGestureStage = i;
    }

    @Override // com.google.android.systemui.elmyra.feedback.FeedbackEffect
    public void onResolve(GestureSensor.DetectionProperties detectionProperties) {
        Vibrator vibrator;
        if ((detectionProperties == null || !detectionProperties.isHapticConsumed()) && (vibrator = this.mVibrator) != null) {
            vibrator.vibrate(this.mResolveVibrationEffect, SONIFICATION_AUDIO_ATTRIBUTES);
        }
    }
}
