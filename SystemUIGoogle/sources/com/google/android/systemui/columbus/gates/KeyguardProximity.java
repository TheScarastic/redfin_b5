package com.google.android.systemui.columbus.gates;

import android.content.Context;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: KeyguardProximity.kt */
/* loaded from: classes2.dex */
public final class KeyguardProximity extends Gate {
    private boolean isListening;
    private final KeyguardVisibility keyguardGate;
    private final Proximity proximity;
    private final KeyguardProximity$keyguardListener$1 keyguardListener = new KeyguardProximity$keyguardListener$1(this);
    private final KeyguardProximity$proximityListener$1 proximityListener = new KeyguardProximity$proximityListener$1(this);

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public KeyguardProximity(Context context, KeyguardVisibility keyguardVisibility, Proximity proximity) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(keyguardVisibility, "keyguardGate");
        Intrinsics.checkNotNullParameter(proximity, "proximity");
        this.keyguardGate = keyguardVisibility;
        this.proximity = proximity;
        updateProximityListener();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.keyguardGate.registerListener(this.keyguardListener);
        updateProximityListener();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.keyguardGate.unregisterListener(this.keyguardListener);
        updateProximityListener();
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(this.isListening && this.proximity.isBlocking());
    }

    /* access modifiers changed from: private */
    public final void updateProximityListener() {
        if (!getActive() || !this.keyguardGate.isKeyguardShowing() || this.keyguardGate.isKeyguardOccluded()) {
            if (this.isListening) {
                this.proximity.unregisterListener(this.proximityListener);
                this.isListening = false;
            }
        } else if (!this.isListening) {
            this.proximity.registerListener(this.proximityListener);
            this.isListening = true;
        }
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    public String toString() {
        return super.toString() + " [isListening -> " + this.isListening + "; proximityBlocked -> " + this.proximity.isBlocking() + ']';
    }
}
