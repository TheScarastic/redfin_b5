package com.google.android.systemui.statusbar.notification.voicereplies;

import com.android.systemui.statusbar.phone.KeyguardBouncer;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Result;
import kotlin.Unit;
import kotlinx.coroutines.CancellableContinuation;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class VoiceReplyTarget$awaitKeyguardReset$2$callback$1 implements KeyguardBouncer.KeyguardResetCallback {
    final /* synthetic */ CancellableContinuation<Unit> $k;
    final /* synthetic */ AtomicBoolean $latch;
    final /* synthetic */ VoiceReplyTarget this$0;

    /* JADX DEBUG: Multi-variable search result rejected for r3v0, resolved type: kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public VoiceReplyTarget$awaitKeyguardReset$2$callback$1(AtomicBoolean atomicBoolean, VoiceReplyTarget voiceReplyTarget, CancellableContinuation<? super Unit> cancellableContinuation) {
        this.$latch = atomicBoolean;
        this.this$0 = voiceReplyTarget;
        this.$k = cancellableContinuation;
    }

    @Override // com.android.systemui.statusbar.phone.KeyguardBouncer.KeyguardResetCallback
    public void onKeyguardReset() {
        AtomicBoolean atomicBoolean = this.$latch;
        VoiceReplyTarget voiceReplyTarget = this.this$0;
        CancellableContinuation<Unit> cancellableContinuation = this.$k;
        if (atomicBoolean.getAndSet(false)) {
            voiceReplyTarget.statusBarKeyguardViewManager.getBouncer().removeKeyguardResetCallback(this);
            Unit unit = Unit.INSTANCE;
            Result.Companion companion = Result.Companion;
            cancellableContinuation.resumeWith(Result.m670constructorimpl(unit));
        }
    }
}
