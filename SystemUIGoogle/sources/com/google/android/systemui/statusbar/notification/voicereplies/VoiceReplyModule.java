package com.google.android.systemui.statusbar.notification.voicereplies;

import java.util.Optional;
import javax.inject.Provider;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public interface VoiceReplyModule {
    public static final Companion Companion = Companion.$$INSTANCE;

    static Optional<NotificationVoiceReplyClient> provideNotificationVoiceReplyClient(Provider<DebugNotificationVoiceReplyClient> provider) {
        return Companion.provideNotificationVoiceReplyClient(provider);
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        static final /* synthetic */ Companion $$INSTANCE = new Companion();

        private Companion() {
        }

        public final Optional<NotificationVoiceReplyClient> provideNotificationVoiceReplyClient(Provider<DebugNotificationVoiceReplyClient> provider) {
            Intrinsics.checkNotNullParameter(provider, "debugClient");
            Optional<NotificationVoiceReplyClient> empty = Optional.empty();
            Intrinsics.checkNotNullExpressionValue(empty, "empty()");
            return empty;
        }
    }
}
