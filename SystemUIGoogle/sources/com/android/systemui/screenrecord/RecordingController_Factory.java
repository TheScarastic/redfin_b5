package com.android.systemui.screenrecord;

import com.android.systemui.broadcast.BroadcastDispatcher;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class RecordingController_Factory implements Factory<RecordingController> {
    private final Provider<BroadcastDispatcher> broadcastDispatcherProvider;

    public RecordingController_Factory(Provider<BroadcastDispatcher> provider) {
        this.broadcastDispatcherProvider = provider;
    }

    @Override // javax.inject.Provider
    public RecordingController get() {
        return newInstance(this.broadcastDispatcherProvider.get());
    }

    public static RecordingController_Factory create(Provider<BroadcastDispatcher> provider) {
        return new RecordingController_Factory(provider);
    }

    public static RecordingController newInstance(BroadcastDispatcher broadcastDispatcher) {
        return new RecordingController(broadcastDispatcher);
    }
}
