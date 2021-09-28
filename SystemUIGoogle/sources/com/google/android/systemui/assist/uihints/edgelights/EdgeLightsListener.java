package com.google.android.systemui.assist.uihints.edgelights;

import com.android.systemui.assist.ui.EdgeLight;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
/* loaded from: classes2.dex */
public interface EdgeLightsListener {
    default void onAssistLightsUpdated(EdgeLightsView.Mode mode, EdgeLight[] edgeLightArr) {
    }

    default void onModeStarted(EdgeLightsView.Mode mode) {
    }
}
