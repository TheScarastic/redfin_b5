package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.NotificationShadeWindowController;
import com.android.systemui.statusbar.phone.StatusBarWindowCallback;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Result;
import kotlinx.coroutines.CancellableContinuation;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1 implements StatusBarWindowCallback {
    final /* synthetic */ CancellableContinuation<StatusBarWindowState> $k;
    final /* synthetic */ AtomicBoolean $latch;
    final /* synthetic */ NotificationShadeWindowController $this_awaitStateChange;

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlinx.coroutines.CancellableContinuation<? super com.google.android.systemui.statusbar.notification.voicereplies.StatusBarWindowState> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public NotificationVoiceReplyManagerKt$awaitStateChange$2$cb$1(AtomicBoolean atomicBoolean, NotificationShadeWindowController notificationShadeWindowController, CancellableContinuation<? super StatusBarWindowState> cancellableContinuation) {
        this.$latch = atomicBoolean;
        this.$this_awaitStateChange = notificationShadeWindowController;
        this.$k = cancellableContinuation;
    }

    @Override // com.android.systemui.statusbar.phone.StatusBarWindowCallback
    public void onStateChanged(boolean z, boolean z2, boolean z3) {
        AtomicBoolean atomicBoolean = this.$latch;
        NotificationShadeWindowController notificationShadeWindowController = this.$this_awaitStateChange;
        CancellableContinuation<StatusBarWindowState> cancellableContinuation = this.$k;
        if (atomicBoolean.getAndSet(false)) {
            notificationShadeWindowController.unregisterCallback(this);
            StatusBarWindowState statusBarWindowState = new StatusBarWindowState(z, z2, z3);
            Result.Companion companion = Result.Companion;
            cancellableContinuation.resumeWith(Result.m670constructorimpl(statusBarWindowState));
        }
    }
}
