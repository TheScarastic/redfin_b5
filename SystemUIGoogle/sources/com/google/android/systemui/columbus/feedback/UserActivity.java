package com.google.android.systemui.columbus.feedback;

import android.os.PowerManager;
import android.os.SystemClock;
import com.google.android.systemui.columbus.sensors.GestureSensor;
import dagger.Lazy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserActivity.kt */
/* loaded from: classes2.dex */
public final class UserActivity implements FeedbackEffect {
    private final Lazy<PowerManager> powerManager;

    public UserActivity(Lazy<PowerManager> lazy) {
        Intrinsics.checkNotNullParameter(lazy, "powerManager");
        this.powerManager = lazy;
    }

    @Override // com.google.android.systemui.columbus.feedback.FeedbackEffect
    public void onGestureDetected(int i, GestureSensor.DetectionProperties detectionProperties) {
        if (i != 0) {
            this.powerManager.get().userActivity(SystemClock.uptimeMillis(), 0, 0);
        }
    }
}
