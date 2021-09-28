package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.statusbar.policy.OnHeadsUpChangedListener;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Pair;
import kotlin.Result;
import kotlin.TuplesKt;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1 implements OnHeadsUpChangedListener {
    final /* synthetic */ CancellableContinuation<Pair<NotificationEntry, Boolean>> $k;
    final /* synthetic */ AtomicBoolean $latch;
    final /* synthetic */ HeadsUpManager $this_awaitHunStateChange;

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlinx.coroutines.CancellableContinuation<? super kotlin.Pair<com.android.systemui.statusbar.notification.collection.NotificationEntry, java.lang.Boolean>> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public NotificationVoiceReplyManagerKt$awaitHunStateChange$2$listener$1(AtomicBoolean atomicBoolean, HeadsUpManager headsUpManager, CancellableContinuation<? super Pair<NotificationEntry, Boolean>> cancellableContinuation) {
        this.$latch = atomicBoolean;
        this.$this_awaitHunStateChange = headsUpManager;
        this.$k = cancellableContinuation;
    }

    @Override // com.android.systemui.statusbar.policy.OnHeadsUpChangedListener
    public void onHeadsUpStateChanged(NotificationEntry notificationEntry, boolean z) {
        Intrinsics.checkNotNullParameter(notificationEntry, "entry");
        AtomicBoolean atomicBoolean = this.$latch;
        HeadsUpManager headsUpManager = this.$this_awaitHunStateChange;
        CancellableContinuation<Pair<NotificationEntry, Boolean>> cancellableContinuation = this.$k;
        if (atomicBoolean.getAndSet(false)) {
            headsUpManager.removeListener(this);
            Pair pair = TuplesKt.to(notificationEntry, Boolean.valueOf(z));
            Result.Companion companion = Result.Companion;
            cancellableContinuation.resumeWith(Result.m670constructorimpl(pair));
        }
    }
}
