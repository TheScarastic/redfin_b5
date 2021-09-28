package com.android.systemui.broadcast;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.os.UserHandle;
import java.util.concurrent.Executor;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BroadcastDispatcher.kt */
/* loaded from: classes.dex */
public final class ReceiverData {
    private final Executor executor;
    private final IntentFilter filter;
    private final BroadcastReceiver receiver;
    private final UserHandle user;

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ReceiverData)) {
            return false;
        }
        ReceiverData receiverData = (ReceiverData) obj;
        return Intrinsics.areEqual(this.receiver, receiverData.receiver) && Intrinsics.areEqual(this.filter, receiverData.filter) && Intrinsics.areEqual(this.executor, receiverData.executor) && Intrinsics.areEqual(this.user, receiverData.user);
    }

    public int hashCode() {
        return (((((this.receiver.hashCode() * 31) + this.filter.hashCode()) * 31) + this.executor.hashCode()) * 31) + this.user.hashCode();
    }

    public String toString() {
        return "ReceiverData(receiver=" + this.receiver + ", filter=" + this.filter + ", executor=" + this.executor + ", user=" + this.user + ')';
    }

    public ReceiverData(BroadcastReceiver broadcastReceiver, IntentFilter intentFilter, Executor executor, UserHandle userHandle) {
        Intrinsics.checkNotNullParameter(broadcastReceiver, "receiver");
        Intrinsics.checkNotNullParameter(intentFilter, "filter");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(userHandle, "user");
        this.receiver = broadcastReceiver;
        this.filter = intentFilter;
        this.executor = executor;
        this.user = userHandle;
    }

    public final BroadcastReceiver getReceiver() {
        return this.receiver;
    }

    public final IntentFilter getFilter() {
        return this.filter;
    }

    public final Executor getExecutor() {
        return this.executor;
    }

    public final UserHandle getUser() {
        return this.user;
    }
}
