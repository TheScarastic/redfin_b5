package com.android.wm.shell.bubbles;

import android.app.Notification;
import android.content.LocusId;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
/* loaded from: classes2.dex */
public class BubbleEntry {
    private boolean mIsClearable;
    private NotificationListenerService.Ranking mRanking;
    private StatusBarNotification mSbn;
    private boolean mShouldSuppressNotificationDot;
    private boolean mShouldSuppressNotificationList;
    private boolean mShouldSuppressPeek;

    public BubbleEntry(StatusBarNotification statusBarNotification, NotificationListenerService.Ranking ranking, boolean z, boolean z2, boolean z3, boolean z4) {
        this.mSbn = statusBarNotification;
        this.mRanking = ranking;
        this.mIsClearable = z;
        this.mShouldSuppressNotificationDot = z2;
        this.mShouldSuppressNotificationList = z3;
        this.mShouldSuppressPeek = z4;
    }

    public StatusBarNotification getStatusBarNotification() {
        return this.mSbn;
    }

    public NotificationListenerService.Ranking getRanking() {
        return this.mRanking;
    }

    public String getKey() {
        return this.mSbn.getKey();
    }

    public String getGroupKey() {
        return this.mSbn.getGroupKey();
    }

    public LocusId getLocusId() {
        return this.mSbn.getNotification().getLocusId();
    }

    public Notification.BubbleMetadata getBubbleMetadata() {
        return getStatusBarNotification().getNotification().getBubbleMetadata();
    }

    public boolean setFlagBubble(boolean z) {
        boolean isBubble = isBubble();
        if (!z) {
            this.mSbn.getNotification().flags &= -4097;
        } else if (getBubbleMetadata() != null && canBubble()) {
            this.mSbn.getNotification().flags |= 4096;
        }
        return isBubble != isBubble();
    }

    public boolean isBubble() {
        return (this.mSbn.getNotification().flags & 4096) != 0;
    }

    public boolean canBubble() {
        return this.mRanking.canBubble();
    }

    public boolean isClearable() {
        return this.mIsClearable;
    }

    public boolean shouldSuppressNotificationDot() {
        return this.mShouldSuppressNotificationDot;
    }

    public boolean shouldSuppressNotificationList() {
        return this.mShouldSuppressNotificationList;
    }

    public boolean shouldSuppressPeek() {
        return this.mShouldSuppressPeek;
    }
}
