package com.android.systemui.util;

import android.content.IntentFilter;
import android.os.UserHandle;
import androidx.lifecycle.MutableLiveData;
import com.android.systemui.broadcast.BroadcastDispatcher;
import java.util.concurrent.Executor;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RingerModeTrackerImpl.kt */
/* loaded from: classes2.dex */
public final class RingerModeLiveData extends MutableLiveData<Integer> {
    private final BroadcastDispatcher broadcastDispatcher;
    private final Executor executor;
    private final IntentFilter filter;
    private final Function0<Integer> getter;
    private boolean initialSticky;
    private final RingerModeLiveData$receiver$1 receiver = new RingerModeLiveData$receiver$1(this);

    public RingerModeLiveData(BroadcastDispatcher broadcastDispatcher, Executor executor, String str, Function0<Integer> function0) {
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(executor, "executor");
        Intrinsics.checkNotNullParameter(str, "intent");
        Intrinsics.checkNotNullParameter(function0, "getter");
        this.broadcastDispatcher = broadcastDispatcher;
        this.executor = executor;
        this.getter = function0;
        this.filter = new IntentFilter(str);
    }

    public final boolean getInitialSticky() {
        return this.initialSticky;
    }

    @Override // androidx.lifecycle.LiveData
    public Integer getValue() {
        Integer num = (Integer) super.getValue();
        return Integer.valueOf(num == null ? -1 : num.intValue());
    }

    @Override // androidx.lifecycle.LiveData
    public void onActive() {
        super.onActive();
        this.broadcastDispatcher.registerReceiver(this.receiver, this.filter, this.executor, UserHandle.ALL);
        this.executor.execute(new Runnable(this) { // from class: com.android.systemui.util.RingerModeLiveData$onActive$1
            final /* synthetic */ RingerModeLiveData this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // java.lang.Runnable
            public final void run() {
                RingerModeLiveData ringerModeLiveData = this.this$0;
                ringerModeLiveData.postValue(RingerModeLiveData.access$getGetter$p(ringerModeLiveData).invoke());
            }
        });
    }

    @Override // androidx.lifecycle.LiveData
    public void onInactive() {
        super.onInactive();
        this.broadcastDispatcher.unregisterReceiver(this.receiver);
    }
}
