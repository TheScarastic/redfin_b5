package com.google.android.systemui.assist.uihints;

import android.content.Context;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: AssistantWarmer.kt */
/* loaded from: classes2.dex */
public final class AssistantWarmer implements NgaMessageHandler.WarmingListener {
    private final Context context;
    private boolean primed;
    private NgaMessageHandler.WarmingRequest request;

    public AssistantWarmer(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.WarmingListener
    public void onWarmingRequest(NgaMessageHandler.WarmingRequest warmingRequest) {
        Intrinsics.checkNotNullParameter(warmingRequest, "request");
        this.request = warmingRequest;
    }

    public final void onInvocationProgress(float f) {
        NgaMessageHandler.WarmingRequest warmingRequest;
        boolean z = true;
        if (f >= 1.0f) {
            this.primed = false;
        } else {
            if (f > 0.0f || !this.primed) {
                NgaMessageHandler.WarmingRequest warmingRequest2 = this.request;
                if (f > (warmingRequest2 == null ? 0.1f : warmingRequest2.getThreshold()) && !this.primed) {
                    this.primed = true;
                }
            } else {
                this.primed = false;
            }
            if (z && (warmingRequest = this.request) != null) {
                warmingRequest.notify(this.context, this.primed);
                return;
            }
        }
        z = false;
        if (z) {
        }
    }
}
