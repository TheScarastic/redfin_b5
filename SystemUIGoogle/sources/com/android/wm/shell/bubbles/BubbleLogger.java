package com.android.wm.shell.bubbles;

import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.util.FrameworkStatsLog;
/* loaded from: classes2.dex */
public class BubbleLogger {
    private final UiEventLogger mUiEventLogger;

    @VisibleForTesting
    /* loaded from: classes2.dex */
    public enum Event implements UiEventLogger.UiEventEnum {
        BUBBLE_OVERFLOW_ADD_USER_GESTURE(483),
        BUBBLE_OVERFLOW_ADD_AGED(484),
        BUBBLE_OVERFLOW_REMOVE_MAX_REACHED(485),
        BUBBLE_OVERFLOW_REMOVE_CANCEL(486),
        BUBBLE_OVERFLOW_REMOVE_GROUP_CANCEL(487),
        BUBBLE_OVERFLOW_REMOVE_NO_LONGER_BUBBLE(488),
        BUBBLE_OVERFLOW_REMOVE_BACK_TO_STACK(489),
        BUBBLE_OVERFLOW_REMOVE_BLOCKED(490),
        BUBBLE_OVERFLOW_SELECTED(600),
        BUBBLE_OVERFLOW_RECOVER(691);
        
        private final int mId;

        Event(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }

    public BubbleLogger(UiEventLogger uiEventLogger) {
        this.mUiEventLogger = uiEventLogger;
    }

    public void log(Bubble bubble, UiEventLogger.UiEventEnum uiEventEnum) {
        this.mUiEventLogger.logWithInstanceId(uiEventEnum, bubble.getAppUid(), bubble.getPackageName(), bubble.getInstanceId());
    }

    public void logOverflowRemove(Bubble bubble, int i) {
        if (i == 5) {
            log(bubble, Event.BUBBLE_OVERFLOW_REMOVE_CANCEL);
        } else if (i == 9) {
            log(bubble, Event.BUBBLE_OVERFLOW_REMOVE_GROUP_CANCEL);
        } else if (i == 7) {
            log(bubble, Event.BUBBLE_OVERFLOW_REMOVE_NO_LONGER_BUBBLE);
        } else if (i == 4) {
            log(bubble, Event.BUBBLE_OVERFLOW_REMOVE_BLOCKED);
        }
    }

    public void logOverflowAdd(Bubble bubble, int i) {
        if (i == 2) {
            log(bubble, Event.BUBBLE_OVERFLOW_ADD_AGED);
        } else if (i == 1) {
            log(bubble, Event.BUBBLE_OVERFLOW_ADD_USER_GESTURE);
        } else if (i == 15) {
            log(bubble, Event.BUBBLE_OVERFLOW_RECOVER);
        }
    }

    /* access modifiers changed from: package-private */
    public void logStackUiChanged(String str, int i, int i2, float f, float f2) {
        FrameworkStatsLog.write(149, str, (String) null, 0, 0, i2, i, f, f2, false, false, false);
    }

    /* access modifiers changed from: package-private */
    public void logShowOverflow(String str, int i) {
        this.mUiEventLogger.log(Event.BUBBLE_OVERFLOW_SELECTED, i, str);
    }

    /* access modifiers changed from: package-private */
    public void logBubbleUiChanged(Bubble bubble, String str, int i, int i2, float f, float f2, int i3) {
        FrameworkStatsLog.write(149, str, bubble.getChannelId(), bubble.getNotificationId(), i3, i2, i, f, f2, bubble.showInShade(), false, false);
    }
}
