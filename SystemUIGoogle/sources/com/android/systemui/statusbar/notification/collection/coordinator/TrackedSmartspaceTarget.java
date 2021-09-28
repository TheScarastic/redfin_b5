package com.android.systemui.statusbar.notification.collection.coordinator;

import kotlin.jvm.internal.Intrinsics;
/* compiled from: SmartspaceDedupingCoordinator.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class TrackedSmartspaceTarget {
    private long alertExceptionExpires;
    private Runnable cancelTimeoutRunnable;
    private final String key;
    private boolean shouldFilter;

    public TrackedSmartspaceTarget(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.key = str;
    }

    public final String getKey() {
        return this.key;
    }

    public final Runnable getCancelTimeoutRunnable() {
        return this.cancelTimeoutRunnable;
    }

    public final void setCancelTimeoutRunnable(Runnable runnable) {
        this.cancelTimeoutRunnable = runnable;
    }

    public final long getAlertExceptionExpires() {
        return this.alertExceptionExpires;
    }

    public final void setAlertExceptionExpires(long j) {
        this.alertExceptionExpires = j;
    }

    public final boolean getShouldFilter() {
        return this.shouldFilter;
    }

    public final void setShouldFilter(boolean z) {
        this.shouldFilter = z;
    }
}
