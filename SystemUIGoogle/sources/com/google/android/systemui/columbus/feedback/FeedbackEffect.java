package com.google.android.systemui.columbus.feedback;

import com.google.android.systemui.columbus.sensors.GestureSensor;
/* compiled from: FeedbackEffect.kt */
/* loaded from: classes2.dex */
public interface FeedbackEffect {
    void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties);
}
