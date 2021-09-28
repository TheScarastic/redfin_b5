package com.android.systemui.media.dagger;

import com.android.systemui.media.MediaDataManager;
import com.android.systemui.media.MediaHierarchyManager;
import com.android.systemui.media.MediaHost;
import com.android.systemui.media.MediaHostStatesManager;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class MediaModule_ProvidesKeyguardMediaHostFactory implements Factory<MediaHost> {
    private final Provider<MediaDataManager> dataManagerProvider;
    private final Provider<MediaHierarchyManager> hierarchyManagerProvider;
    private final Provider<MediaHost.MediaHostStateHolder> stateHolderProvider;
    private final Provider<MediaHostStatesManager> statesManagerProvider;

    public MediaModule_ProvidesKeyguardMediaHostFactory(Provider<MediaHost.MediaHostStateHolder> provider, Provider<MediaHierarchyManager> provider2, Provider<MediaDataManager> provider3, Provider<MediaHostStatesManager> provider4) {
        this.stateHolderProvider = provider;
        this.hierarchyManagerProvider = provider2;
        this.dataManagerProvider = provider3;
        this.statesManagerProvider = provider4;
    }

    @Override // javax.inject.Provider
    public MediaHost get() {
        return providesKeyguardMediaHost(this.stateHolderProvider.get(), this.hierarchyManagerProvider.get(), this.dataManagerProvider.get(), this.statesManagerProvider.get());
    }

    public static MediaModule_ProvidesKeyguardMediaHostFactory create(Provider<MediaHost.MediaHostStateHolder> provider, Provider<MediaHierarchyManager> provider2, Provider<MediaDataManager> provider3, Provider<MediaHostStatesManager> provider4) {
        return new MediaModule_ProvidesKeyguardMediaHostFactory(provider, provider2, provider3, provider4);
    }

    public static MediaHost providesKeyguardMediaHost(MediaHost.MediaHostStateHolder mediaHostStateHolder, MediaHierarchyManager mediaHierarchyManager, MediaDataManager mediaDataManager, MediaHostStatesManager mediaHostStatesManager) {
        return (MediaHost) Preconditions.checkNotNullFromProvides(MediaModule.providesKeyguardMediaHost(mediaHostStateHolder, mediaHierarchyManager, mediaDataManager, mediaHostStatesManager));
    }
}
