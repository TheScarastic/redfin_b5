package com.google.android.systemui.columbus.gates;

import android.content.Context;
import com.android.systemui.util.sensors.ProximitySensor;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Proximity.kt */
/* loaded from: classes2.dex */
public class Proximity extends Gate {
    public static final Companion Companion = new Companion(null);
    private final Proximity$proximityListener$1 proximityListener = new Proximity$proximityListener$1(this);
    private final ProximitySensor proximitySensor;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public Proximity(Context context, ProximitySensor proximitySensor) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(proximitySensor, "proximitySensor");
        this.proximitySensor = proximitySensor;
        proximitySensor.setTag("Columbus/Proximity");
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onActivate() {
        this.proximitySensor.register(this.proximityListener);
        updateBlocking();
    }

    @Override // com.google.android.systemui.columbus.gates.Gate
    protected void onDeactivate() {
        this.proximitySensor.unregister(this.proximityListener);
    }

    /* access modifiers changed from: private */
    public final void updateBlocking() {
        setBlocking(!Intrinsics.areEqual(this.proximitySensor.isNear(), Boolean.FALSE));
    }

    /* compiled from: Proximity.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
