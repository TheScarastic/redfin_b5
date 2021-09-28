package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TransientGate.kt */
/* loaded from: classes2.dex */
public abstract class TransientGate extends Gate {
    private final Function0<Unit> resetGate = new Function0<Unit>(this) { // from class: com.google.android.systemui.columbus.gates.TransientGate$resetGate$1
        final /* synthetic */ TransientGate this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        @Override // kotlin.jvm.functions.Function0
        public final void invoke() {
            this.this$0.setBlocking(false);
        }
    };
    private final Handler resetGateHandler;

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public TransientGate(Context context, Handler handler) {
        super(context, null, 2, null);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "resetGateHandler");
        this.resetGateHandler = handler;
    }

    /* access modifiers changed from: protected */
    public final void blockForMillis(long j) {
        this.resetGateHandler.removeCallbacks(new Runnable(this.resetGate) { // from class: com.google.android.systemui.columbus.gates.TransientGate$sam$java_lang_Runnable$0
            private final /* synthetic */ Function0 function;

            /* access modifiers changed from: package-private */
            {
                this.function = r1;
            }

            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                this.function.invoke();
            }
        });
        setBlocking(true);
        this.resetGateHandler.postDelayed(new Runnable(this.resetGate) { // from class: com.google.android.systemui.columbus.gates.TransientGate$sam$java_lang_Runnable$0
            private final /* synthetic */ Function0 function;

            /* access modifiers changed from: package-private */
            {
                this.function = r1;
            }

            @Override // java.lang.Runnable
            public final /* synthetic */ void run() {
                this.function.invoke();
            }
        }, j);
    }
}
