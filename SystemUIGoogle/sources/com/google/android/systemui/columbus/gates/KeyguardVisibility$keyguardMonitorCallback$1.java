package com.google.android.systemui.columbus.gates;

import com.android.systemui.statusbar.policy.KeyguardStateController;
/* compiled from: KeyguardVisibility.kt */
/* loaded from: classes2.dex */
public final class KeyguardVisibility$keyguardMonitorCallback$1 implements KeyguardStateController.Callback {
    final /* synthetic */ KeyguardVisibility this$0;

    /* access modifiers changed from: package-private */
    public KeyguardVisibility$keyguardMonitorCallback$1(KeyguardVisibility keyguardVisibility) {
        this.this$0 = keyguardVisibility;
    }

    @Override // com.android.systemui.statusbar.policy.KeyguardStateController.Callback
    public void onKeyguardShowingChanged() {
        this.this$0.updateBlocking();
    }
}
