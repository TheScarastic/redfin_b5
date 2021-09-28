package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.broadcast.BroadcastDispatcher;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.notification.people.Subscription;
import com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyManager;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.BuildersKt__Builders_commonKt;
import kotlinx.coroutines.Dispatchers;
import kotlinx.coroutines.GlobalScope;
import kotlinx.coroutines.Job;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class DebugNotificationVoiceReplyClient implements NotificationVoiceReplyClient {
    public static final Companion Companion = new Companion(null);
    private final BroadcastDispatcher broadcastDispatcher;
    private final NotificationLockscreenUserManager lockscreenUserManager;
    private final NotificationVoiceReplyManager.Initializer voiceReplyInitializer;

    public DebugNotificationVoiceReplyClient(BroadcastDispatcher broadcastDispatcher, NotificationLockscreenUserManager notificationLockscreenUserManager, NotificationVoiceReplyManager.Initializer initializer) {
        Intrinsics.checkNotNullParameter(broadcastDispatcher, "broadcastDispatcher");
        Intrinsics.checkNotNullParameter(notificationLockscreenUserManager, "lockscreenUserManager");
        Intrinsics.checkNotNullParameter(initializer, "voiceReplyInitializer");
        this.broadcastDispatcher = broadcastDispatcher;
        this.lockscreenUserManager = notificationLockscreenUserManager;
        this.voiceReplyInitializer = initializer;
    }

    @Override // com.google.android.systemui.statusbar.notification.voicereplies.NotificationVoiceReplyClient
    public Subscription startClient() {
        GlobalScope globalScope = GlobalScope.INSTANCE;
        Dispatchers dispatchers = Dispatchers.INSTANCE;
        return NotificationVoiceReplyManagerKt.Subscription(new Function0<Unit>(BuildersKt__Builders_commonKt.launch$default(globalScope, Dispatchers.getMain(), null, new DebugNotificationVoiceReplyClient$startClient$job$1(this, null), 2, null)) { // from class: com.google.android.systemui.statusbar.notification.voicereplies.DebugNotificationVoiceReplyClient$startClient$1
            final /* synthetic */ Job $job;

            /* access modifiers changed from: package-private */
            {
                this.$job = r1;
            }

            @Override // kotlin.jvm.functions.Function0
            public final void invoke() {
                Job.DefaultImpls.cancel$default(this.$job, null, 1, null);
            }
        });
    }

    /* compiled from: NotificationVoiceReplyManager.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
