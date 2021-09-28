package com.android.systemui.settings;

import com.android.systemui.settings.UserTracker;
import java.lang.ref.WeakReference;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: UserTrackerImpl.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class DataItem {
    private final WeakReference<UserTracker.Callback> callback;
    private final Executor executor;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof DataItem)) {
            return false;
        }
        DataItem dataItem = (DataItem) obj;
        return Intrinsics.areEqual(this.callback, dataItem.callback) && Intrinsics.areEqual(this.executor, dataItem.executor);
    }

    public int hashCode() {
        return (this.callback.hashCode() * 31) + this.executor.hashCode();
    }

    public String toString() {
        return "DataItem(callback=" + this.callback + ", executor=" + this.executor + ')';
    }

    public DataItem(WeakReference<UserTracker.Callback> weakReference, Executor executor) {
        Intrinsics.checkNotNullParameter(weakReference, "callback");
        Intrinsics.checkNotNullParameter(executor, "executor");
        this.callback = weakReference;
        this.executor = executor;
    }

    public final WeakReference<UserTracker.Callback> getCallback() {
        return this.callback;
    }

    public final Executor getExecutor() {
        return this.executor;
    }

    public final boolean sameOrEmpty(UserTracker.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "other");
        UserTracker.Callback callback2 = this.callback.get();
        if (callback2 == null) {
            return true;
        }
        return callback2.equals(callback);
    }
}
