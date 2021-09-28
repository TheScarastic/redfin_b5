package com.google.android.systemui.assist.uihints;

import com.google.android.systemui.assist.uihints.LightnessProvider;
/* loaded from: classes2.dex */
public final /* synthetic */ class LightnessProvider$1$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ LightnessProvider.AnonymousClass1 f$0;
    public final /* synthetic */ float f$1;

    public /* synthetic */ LightnessProvider$1$$ExternalSyntheticLambda0(LightnessProvider.AnonymousClass1 r1, float f) {
        this.f$0 = r1;
        this.f$1 = f;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$onSampleCollected$0(this.f$1);
    }
}
