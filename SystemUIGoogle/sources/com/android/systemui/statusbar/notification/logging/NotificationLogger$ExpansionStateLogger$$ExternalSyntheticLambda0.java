package com.android.systemui.statusbar.notification.logging;

import com.android.systemui.statusbar.notification.logging.NotificationLogger;
/* loaded from: classes.dex */
public final /* synthetic */ class NotificationLogger$ExpansionStateLogger$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ NotificationLogger.ExpansionStateLogger f$0;
    public final /* synthetic */ String f$1;
    public final /* synthetic */ NotificationLogger.ExpansionStateLogger.State f$2;

    public /* synthetic */ NotificationLogger$ExpansionStateLogger$$ExternalSyntheticLambda0(NotificationLogger.ExpansionStateLogger expansionStateLogger, String str, NotificationLogger.ExpansionStateLogger.State state) {
        this.f$0 = expansionStateLogger;
        this.f$1 = str;
        this.f$2 = state;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$maybeNotifyOnNotificationExpansionChanged$0(this.f$1, this.f$2);
    }
}
