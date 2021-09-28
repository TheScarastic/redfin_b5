package com.android.systemui.statusbar.policy;

import android.widget.Button;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.SmartReplyView;
/* compiled from: SmartReplyStateInflater.kt */
/* loaded from: classes.dex */
public interface SmartReplyInflater {
    Button inflateReplyButton(SmartReplyView smartReplyView, NotificationEntry notificationEntry, SmartReplyView.SmartReplies smartReplies, int i, CharSequence charSequence, boolean z);
}
