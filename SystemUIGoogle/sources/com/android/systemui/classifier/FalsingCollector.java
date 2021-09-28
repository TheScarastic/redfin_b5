package com.android.systemui.classifier;

import android.view.MotionEvent;
import com.android.systemui.classifier.FalsingClassifier;
/* loaded from: classes.dex */
public interface FalsingCollector {
    void avoidGesture();

    boolean isReportingEnabled();

    void onAffordanceSwipingAborted();

    void onAffordanceSwipingStarted(boolean z);

    void onBouncerHidden();

    void onBouncerShown();

    void onCameraHintStarted();

    void onCameraOn();

    void onExpansionFromPulseStopped();

    void onLeftAffordanceHintStarted();

    void onLeftAffordanceOn();

    void onMotionEventComplete();

    void onNotificationActive();

    void onNotificationDismissed();

    void onNotificationStartDismissing();

    void onNotificationStartDraggingDown();

    void onNotificationStopDismissing();

    void onNotificationStopDraggingDown();

    void onQsDown();

    void onScreenOff();

    void onScreenOnFromTouch();

    void onScreenTurningOn();

    void onStartExpandingFromPulse();

    void onSuccessfulUnlock();

    void onTouchEvent(MotionEvent motionEvent);

    void onTrackingStarted(boolean z);

    void onTrackingStopped();

    void onUnlockHintStarted();

    void setNotificationExpanded();

    void setQsExpanded(boolean z);

    void setShowingAod(boolean z);

    boolean shouldEnforceBouncer();

    void updateFalseConfidence(FalsingClassifier.Result result);
}
