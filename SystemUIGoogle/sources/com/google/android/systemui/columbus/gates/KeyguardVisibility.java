package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import dagger.Lazy;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardVisibility.kt */
/* loaded from: classes2.dex */
public class KeyguardVisibility extends Gate {
    private final KeyguardVisibility$keyguardMonitorCallback$1 keyguardMonitorCallback = new KeyguardVisibility$keyguardMonitorCallback$1(this);
    private final Lazy<KeyguardStateController> keyguardStateController;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public KeyguardVisibility(Context context, Lazy<KeyguardStateController> lazy) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(lazy, "keyguardStateController");
        this.keyguardStateController = lazy;
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.keyguardMonitorCallback.onKeyguardShowingChanged();
        this.keyguardStateController.get().addCallback(this.keyguardMonitorCallback);
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.keyguardStateController.get().removeCallback(this.keyguardMonitorCallback);
    }

    public final boolean isKeyguardShowing() {
        return this.keyguardStateController.get().isShowing();
    }

    public final boolean isKeyguardOccluded() {
        return this.keyguardStateController.get().isOccluded();
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(isKeyguardShowing());
    }
}
