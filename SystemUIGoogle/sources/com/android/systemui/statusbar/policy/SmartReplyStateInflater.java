package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public interface SmartReplyStateInflater {
    InflatedSmartReplyState inflateSmartReplyState(NotificationEntry notificationEntry);

    InflatedSmartReplyViewHolder inflateSmartReplyViewHolder(Context context, Context context2, NotificationEntry notificationEntry, InflatedSmartReplyState inflatedSmartReplyState, InflatedSmartReplyState inflatedSmartReplyState2);
}
