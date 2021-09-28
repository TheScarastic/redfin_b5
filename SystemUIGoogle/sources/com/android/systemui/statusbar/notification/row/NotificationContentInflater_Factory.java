package com.android.systemui.statusbar.notification.row;

import com.android.systemui.media.MediaFeatureFlag;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.ConversationNotificationProcessor;
import com.android.systemui.statusbar.policy.SmartReplyStateInflater;
import dagger.internal.Factory;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class NotificationContentInflater_Factory implements Factory<NotificationContentInflater> {
    private final Provider<Executor> bgExecutorProvider;
    private final Provider<ConversationNotificationProcessor> conversationProcessorProvider;
    private final Provider<MediaFeatureFlag> mediaFeatureFlagProvider;
    private final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    private final Provider<NotifRemoteViewCache> remoteViewCacheProvider;
    private final Provider<SmartReplyStateInflater> smartRepliesInflaterProvider;

    public NotificationContentInflater_Factory(Provider<NotifRemoteViewCache> provider, Provider<NotificationRemoteInputManager> provider2, Provider<ConversationNotificationProcessor> provider3, Provider<MediaFeatureFlag> provider4, Provider<Executor> provider5, Provider<SmartReplyStateInflater> provider6) {
        this.remoteViewCacheProvider = provider;
        this.remoteInputManagerProvider = provider2;
        this.conversationProcessorProvider = provider3;
        this.mediaFeatureFlagProvider = provider4;
        this.bgExecutorProvider = provider5;
        this.smartRepliesInflaterProvider = provider6;
    }

    @Override // javax.inject.Provider
    public NotificationContentInflater get() {
        return newInstance(this.remoteViewCacheProvider.get(), this.remoteInputManagerProvider.get(), this.conversationProcessorProvider.get(), this.mediaFeatureFlagProvider.get(), this.bgExecutorProvider.get(), this.smartRepliesInflaterProvider.get());
    }

    public static NotificationContentInflater_Factory create(Provider<NotifRemoteViewCache> provider, Provider<NotificationRemoteInputManager> provider2, Provider<ConversationNotificationProcessor> provider3, Provider<MediaFeatureFlag> provider4, Provider<Executor> provider5, Provider<SmartReplyStateInflater> provider6) {
        return new NotificationContentInflater_Factory(provider, provider2, provider3, provider4, provider5, provider6);
    }

    public static NotificationContentInflater newInstance(NotifRemoteViewCache notifRemoteViewCache, NotificationRemoteInputManager notificationRemoteInputManager, ConversationNotificationProcessor conversationNotificationProcessor, MediaFeatureFlag mediaFeatureFlag, Executor executor, SmartReplyStateInflater smartReplyStateInflater) {
        return new NotificationContentInflater(notifRemoteViewCache, notificationRemoteInputManager, conversationNotificationProcessor, mediaFeatureFlag, executor, smartReplyStateInflater);
    }
}
