package com.android.systemui.assist;

import com.android.internal.logging.UiEventLogger;
import kotlin.jvm.internal.DefaultConstructorMarker;
/* compiled from: AssistantInvocationEvent.kt */
/* loaded from: classes.dex */
public enum AssistantInvocationEvent implements UiEventLogger.UiEventEnum {
    ASSISTANT_INVOCATION_UNKNOWN(442),
    ASSISTANT_INVOCATION_TOUCH_GESTURE(443),
    ASSISTANT_INVOCATION_TOUCH_GESTURE_ALT(444),
    ASSISTANT_INVOCATION_HOTWORD(445),
    ASSISTANT_INVOCATION_QUICK_SEARCH_BAR(446),
    ASSISTANT_INVOCATION_HOME_LONG_PRESS(447),
    ASSISTANT_INVOCATION_PHYSICAL_GESTURE(448),
    ASSISTANT_INVOCATION_START_UNKNOWN(530),
    ASSISTANT_INVOCATION_START_TOUCH_GESTURE(531),
    ASSISTANT_INVOCATION_START_PHYSICAL_GESTURE(532),
    ASSISTANT_INVOCATION_POWER_LONG_PRESS(758);
    
    public static final Companion Companion = new Companion(null);
    private final int id;

    AssistantInvocationEvent(int i) {
        this.id = i;
    }

    public int getId() {
        return this.id;
    }

    /* compiled from: AssistantInvocationEvent.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public final int deviceStateFromLegacyDeviceState(int i) {
            switch (i) {
                case 1:
                    return 1;
                case 2:
                    return 2;
                case 3:
                    return 3;
                case 4:
                    return 4;
                case 5:
                    return 5;
                case 6:
                    return 6;
                case 7:
                    return 7;
                case 8:
                    return 8;
                case 9:
                    return 9;
                case 10:
                    return 10;
                default:
                    return 0;
            }
        }

        private Companion() {
        }

        public final AssistantInvocationEvent eventFromLegacyInvocationType(int i, boolean z) {
            if (z) {
                switch (i) {
                    case 1:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_TOUCH_GESTURE;
                    case 2:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_PHYSICAL_GESTURE;
                    case 3:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_HOTWORD;
                    case 4:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_QUICK_SEARCH_BAR;
                    case 5:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_HOME_LONG_PRESS;
                    case 6:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_POWER_LONG_PRESS;
                    default:
                        return AssistantInvocationEvent.ASSISTANT_INVOCATION_UNKNOWN;
                }
            } else if (i == 1) {
                return AssistantInvocationEvent.ASSISTANT_INVOCATION_START_TOUCH_GESTURE;
            } else {
                if (i != 2) {
                    return AssistantInvocationEvent.ASSISTANT_INVOCATION_START_UNKNOWN;
                }
                return AssistantInvocationEvent.ASSISTANT_INVOCATION_START_PHYSICAL_GESTURE;
            }
        }
    }
}
