package com.android.systemui.statusbar.notification.collection.coalescer;

import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CoalescedEvent.kt */
/* loaded from: classes.dex */
public final class CoalescedEvent {
    private EventBatch batch;
    private final String key;
    private int position;
    private NotificationListenerService.Ranking ranking;
    private StatusBarNotification sbn;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof CoalescedEvent)) {
            return false;
        }
        CoalescedEvent coalescedEvent = (CoalescedEvent) obj;
        return Intrinsics.areEqual(this.key, coalescedEvent.key) && this.position == coalescedEvent.position && Intrinsics.areEqual(this.sbn, coalescedEvent.sbn) && Intrinsics.areEqual(this.ranking, coalescedEvent.ranking) && Intrinsics.areEqual(this.batch, coalescedEvent.batch);
    }

    public int hashCode() {
        int hashCode = ((((((this.key.hashCode() * 31) + Integer.hashCode(this.position)) * 31) + this.sbn.hashCode()) * 31) + this.ranking.hashCode()) * 31;
        EventBatch eventBatch = this.batch;
        return hashCode + (eventBatch == null ? 0 : eventBatch.hashCode());
    }

    public CoalescedEvent(String str, int i, StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking, EventBatch eventBatch) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(statusBarNotification, "sbn");
        Intrinsics.checkNotNullParameter(ranking, "ranking");
        this.key = str;
        this.position = i;
        this.sbn = statusBarNotification;
        this.ranking = ranking;
        this.batch = eventBatch;
    }

    public final String getKey() {
        return this.key;
    }

    public final int getPosition() {
        return this.position;
    }

    public final StatusBarNotification getSbn() {
        return this.sbn;
    }

    public final NotificationListenerService.Ranking getRanking() {
        return this.ranking;
    }

    public final void setRanking(NotificationListenerService.Ranking ranking) {
        Intrinsics.checkNotNullParameter(ranking, "<set-?>");
        this.ranking = ranking;
    }

    public final EventBatch getBatch() {
        return this.batch;
    }

    public final void setBatch(EventBatch eventBatch) {
        this.batch = eventBatch;
    }

    public String toString() {
        return "CoalescedEvent(key=" + this.key + ')';
    }
}
