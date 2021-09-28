package com.google.android.systemui.assist.uihints.edgelights.mode;

import com.android.internal.logging.UiEventLogger;
import com.google.android.systemui.assist.uihints.edgelights.EdgeLightsView;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AssistantModeChangeEvent.kt */
/* loaded from: classes2.dex */
public enum AssistantModeChangeEvent implements UiEventLogger.UiEventEnum {
    ASSISTANT_SESSION_MODE_GONE(585),
    ASSISTANT_SESSION_MODE_HALF_LISTENING(586),
    ASSISTANT_SESSION_MODE_FULL_LISTENING(587),
    ASSISTANT_SESSION_MODE_FULFILL_BOTTOM(588),
    ASSISTANT_SESSION_MODE_FULFILL_PERIMETER(589),
    ASSISTANT_SESSION_MODE_UNKNOWN(590);
    
    public static final Companion Companion = new Companion(null);
    private final int id;

    AssistantModeChangeEvent(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }

    /* compiled from: AssistantModeChangeEvent.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public final UiEventLogger.UiEventEnum getEventFromMode(EdgeLightsView.Mode mode) {
            Intrinsics.checkNotNullParameter(mode, "mode");
            if (mode instanceof Gone) {
                return AssistantModeChangeEvent.ASSISTANT_SESSION_MODE_GONE;
            }
            if (mode instanceof FullListening) {
                return AssistantModeChangeEvent.ASSISTANT_SESSION_MODE_FULL_LISTENING;
            }
            if (mode instanceof FulfillBottom) {
                return AssistantModeChangeEvent.ASSISTANT_SESSION_MODE_FULFILL_BOTTOM;
            }
            if (mode instanceof FulfillPerimeter) {
                return AssistantModeChangeEvent.ASSISTANT_SESSION_MODE_FULFILL_PERIMETER;
            }
            return AssistantModeChangeEvent.ASSISTANT_SESSION_MODE_UNKNOWN;
        }
    }
}
