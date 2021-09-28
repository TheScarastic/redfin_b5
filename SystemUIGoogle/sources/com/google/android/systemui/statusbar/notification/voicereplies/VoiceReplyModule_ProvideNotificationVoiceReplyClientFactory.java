package com.google.android.systemui.statusbar.notification.voicereplies;

import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Optional;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class VoiceReplyModule_ProvideNotificationVoiceReplyClientFactory implements Factory<Optional<NotificationVoiceReplyClient>> {
    private final Provider<DebugNotificationVoiceReplyClient> debugClientProvider;

    public VoiceReplyModule_ProvideNotificationVoiceReplyClientFactory(Provider<DebugNotificationVoiceReplyClient> provider) {
        this.debugClientProvider = provider;
    }

    @Override // javax.inject.Provider
    public Optional<NotificationVoiceReplyClient> get() {
        return provideNotificationVoiceReplyClient(this.debugClientProvider);
    }

    public static VoiceReplyModule_ProvideNotificationVoiceReplyClientFactory create(Provider<DebugNotificationVoiceReplyClient> provider) {
        return new VoiceReplyModule_ProvideNotificationVoiceReplyClientFactory(provider);
    }

    public static Optional<NotificationVoiceReplyClient> provideNotificationVoiceReplyClient(Provider<DebugNotificationVoiceReplyClient> provider) {
        return (Optional) Preconditions.checkNotNullFromProvides(VoiceReplyModule.provideNotificationVoiceReplyClient(provider));
    }
}
