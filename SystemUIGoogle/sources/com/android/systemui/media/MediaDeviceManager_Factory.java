package com.android.systemui.media;

import android.media.MediaRouter2Manager;
import com.android.systemui.dump.DumpManager;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaDeviceManager_Factory implements Factory<MediaDeviceManager> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<MediaControllerFactory> controllerFactoryProvider;
    private final Provider<DumpManager> dumpManagerProvider;
    private final Provider<Executor> fgExecutorProvider;
    private final Provider<LocalMediaManagerFactory> localMediaManagerFactoryProvider;
    private final Provider<MediaRouter2Manager> mr2managerProvider;

    public MediaDeviceManager_Factory(Provider<MediaControllerFactory> provider, Provider<LocalMediaManagerFactory> provider2, Provider<MediaRouter2Manager> provider3, Provider<Executor> provider4, Provider<Executor> provider5, Provider<DumpManager> provider6) {
        this.controllerFactoryProvider = provider;
        this.localMediaManagerFactoryProvider = provider2;
        this.mr2managerProvider = provider3;
        this.fgExecutorProvider = provider4;
        this.bgExecutorProvider = provider5;
        this.dumpManagerProvider = provider6;
    }

    @Override // javax.inject.Provider
    public MediaDeviceManager get() {
        return newInstance(this.controllerFactoryProvider.get(), this.localMediaManagerFactoryProvider.get(), this.mr2managerProvider.get(), this.fgExecutorProvider.get(), this.bgExecutorProvider.get(), this.dumpManagerProvider.get());
    }

    public static MediaDeviceManager_Factory create(Provider<MediaControllerFactory> provider, Provider<LocalMediaManagerFactory> provider2, Provider<MediaRouter2Manager> provider3, Provider<Executor> provider4, Provider<Executor> provider5, Provider<DumpManager> provider6) {
        return new MediaDeviceManager_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static MediaDeviceManager newInstance(MediaControllerFactory mediaControllerFactory, LocalMediaManagerFactory localMediaManagerFactory, MediaRouter2Manager mediaRouter2Manager, Executor executor, Executor executor2, DumpManager dumpManager) {
        return new MediaDeviceManager(mediaControllerFactory, localMediaManagerFactory, mediaRouter2Manager, executor, executor2, dumpManager);
    }
}
