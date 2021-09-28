package com.google.android.systemui.elmyra;

import com.google.android.systemui.elmyra.actions.Action;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.gates.Gate;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Collections;
import java.util.List;
/* loaded from: classes2.dex */
public interface ServiceConfiguration {
    default GestureSensor getGestureSensor() {
        return null;
    }

    default List<Action> getActions() {
        return Collections.emptyList();
    }

    default List<FeedbackEffect> getFeedbackEffects() {
        return Collections.emptyList();
    }

    default List<Gate> getGates() {
        return Collections.emptyList();
    }
}
