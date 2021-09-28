package com.google.android.systemui.statusbar.notification.voicereplies;

import android.view.View;
import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.concurrent.atomic.AtomicBoolean;
import kotlin.Result;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
import kotlinx.coroutines.CancellableContinuation;
/* compiled from: NotificationVoiceReplyManager.kt */
/* loaded from: classes2.dex */
public final class VoiceReplyTarget$awaitFocusState$2$listener$1 implements View.OnFocusChangeListener {
    final /* synthetic */ boolean $expected;
    final /* synthetic */ CancellableContinuation<Unit> $k;
    final /* synthetic */ AtomicBoolean $latch;
    final /* synthetic */ RemoteInputView $this_awaitFocusState;

    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlinx.coroutines.CancellableContinuation<? super kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    /* access modifiers changed from: package-private */
    public VoiceReplyTarget$awaitFocusState$2$listener$1(boolean z, AtomicBoolean atomicBoolean, RemoteInputView remoteInputView, CancellableContinuation<? super Unit> cancellableContinuation) {
        this.$expected = z;
        this.$latch = atomicBoolean;
        this.$this_awaitFocusState = remoteInputView;
        this.$k = cancellableContinuation;
    }

    @Override // android.view.View.OnFocusChangeListener
    public void onFocusChange(View view, boolean z) {
        Intrinsics.checkNotNullParameter(view, "v");
        if (z == this.$expected) {
            AtomicBoolean atomicBoolean = this.$latch;
            RemoteInputView remoteInputView = this.$this_awaitFocusState;
            CancellableContinuation<Unit> cancellableContinuation = this.$k;
            if (atomicBoolean.getAndSet(false)) {
                remoteInputView.removeOnEditTextFocusChangedListener(this);
                Unit unit = Unit.INSTANCE;
                Result.Companion companion = Result.Companion;
                cancellableContinuation.resumeWith(Result.m670constructorimpl(unit));
            }
        }
    }
}
