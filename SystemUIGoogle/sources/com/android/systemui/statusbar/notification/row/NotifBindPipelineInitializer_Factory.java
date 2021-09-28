package com.android.systemui.statusbar.notification.row;

import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotifBindPipelineInitializer_Factory implements Factory<NotifBindPipelineInitializer> {
    private final Provider<NotifBindPipeline> pipelineProvider;
    private final Provider<RowContentBindStage> stageProvider;

    public NotifBindPipelineInitializer_Factory(Provider<NotifBindPipeline> provider, Provider<RowContentBindStage> provider2) {
        this.pipelineProvider = provider;
        this.stageProvider = provider2;
    }

    @Override // javax.inject.Provider
    public NotifBindPipelineInitializer get() {
        return newInstance(this.pipelineProvider.get(), this.stageProvider.get());
    }

    public static NotifBindPipelineInitializer_Factory create(Provider<NotifBindPipeline> provider, Provider<RowContentBindStage> provider2) {
        return new NotifBindPipelineInitializer_Factory(provider, provider2);
    }

    public static NotifBindPipelineInitializer newInstance(NotifBindPipeline notifBindPipeline, RowContentBindStage rowContentBindStage) {
        return new NotifBindPipelineInitializer(notifBindPipeline, rowContentBindStage);
    }
}
