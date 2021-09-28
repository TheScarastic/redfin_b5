package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.os.Handler;
import android.provider.DeviceConfig;
import android.service.notification.NotificationListenerService;
import android.util.Pair;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.util.DeviceConfigProxy;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class AssistantFeedbackController {
    private final Context mContext;
    private final DeviceConfigProxy mDeviceConfigProxy;
    private volatile boolean mFeedbackEnabled;
    private final Handler mHandler;
    private final DeviceConfig.OnPropertiesChangedListener mPropertiesChangedListener;

    public AssistantFeedbackController(Handler handler, Context context, DeviceConfigProxy deviceConfigProxy) {
        AnonymousClass1 r0 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.statusbar.notification.AssistantFeedbackController.1
            public void onPropertiesChanged(DeviceConfig.Properties properties) {
                if (properties.getKeyset().contains("enable_nas_feedback")) {
                    AssistantFeedbackController.this.mFeedbackEnabled = properties.getBoolean("enable_nas_feedback", false);
                }
            }
        };
        this.mPropertiesChangedListener = r0;
        this.mHandler = handler;
        this.mContext = context;
        this.mDeviceConfigProxy = deviceConfigProxy;
        this.mFeedbackEnabled = deviceConfigProxy.getBoolean("systemui", "enable_nas_feedback", false);
        deviceConfigProxy.addOnPropertiesChangedListener("systemui", new Executor() { // from class: com.android.systemui.statusbar.notification.AssistantFeedbackController$$ExternalSyntheticLambda0
            @Override // java.util.concurrent.Executor
            public final void execute(Runnable runnable) {
                AssistantFeedbackController.this.postToHandler(runnable);
            }
        }, r0);
    }

    /* access modifiers changed from: private */
    public void postToHandler(Runnable runnable) {
        this.mHandler.post(runnable);
    }

    public boolean isFeedbackEnabled() {
        return this.mFeedbackEnabled;
    }

    public int getFeedbackStatus(NotificationEntry notificationEntry) {
        if (!isFeedbackEnabled()) {
            return 0;
        }
        NotificationListenerService.Ranking ranking = notificationEntry.getRanking();
        int importance = ranking.getChannel().getImportance();
        int importance2 = ranking.getImportance();
        if (importance < 3 && importance2 >= 3) {
            return 1;
        }
        if (importance >= 3 && importance2 < 3) {
            return 2;
        }
        if (importance < importance2 || ranking.getRankingAdjustment() == 1) {
            return 3;
        }
        if (importance > importance2 || ranking.getRankingAdjustment() == -1) {
            return 4;
        }
        return 0;
    }

    public boolean showFeedbackIndicator(NotificationEntry notificationEntry) {
        return getFeedbackStatus(notificationEntry) != 0;
    }

    public Pair<Integer, Integer> getFeedbackResources(NotificationEntry notificationEntry) {
        int feedbackStatus = getFeedbackStatus(notificationEntry);
        if (feedbackStatus == 1) {
            return new Pair<>(17302438, 17040813);
        }
        if (feedbackStatus == 2) {
            return new Pair<>(17302441, 17040816);
        }
        if (feedbackStatus == 3) {
            return new Pair<>(17302442, 17040815);
        }
        if (feedbackStatus != 4) {
            return new Pair<>(0, 0);
        }
        return new Pair<>(17302439, 17040814);
    }

    public int getInlineDescriptionResource(NotificationEntry notificationEntry) {
        int feedbackStatus = getFeedbackStatus(notificationEntry);
        if (feedbackStatus == 1) {
            return R$string.notification_channel_summary_automatic_alerted;
        }
        if (feedbackStatus == 2) {
            return R$string.notification_channel_summary_automatic_silenced;
        }
        if (feedbackStatus == 3) {
            return R$string.notification_channel_summary_automatic_promoted;
        }
        if (feedbackStatus != 4) {
            return R$string.notification_channel_summary_automatic;
        }
        return R$string.notification_channel_summary_automatic_demoted;
    }
}
