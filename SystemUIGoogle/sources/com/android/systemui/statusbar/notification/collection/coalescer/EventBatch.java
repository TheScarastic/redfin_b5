package com.android.systemui.statusbar.notification.collection.coalescer;

import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class EventBatch {
    Runnable mCancelShortTimeout;
    final long mCreatedTimestamp;
    final String mGroupKey;
    final List<CoalescedEvent> mMembers = new ArrayList();

    /* access modifiers changed from: package-private */
    public EventBatch(long j, String str) {
        this.mCreatedTimestamp = j;
        this.mGroupKey = str;
    }
}
