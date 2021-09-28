package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.Handler;
/* loaded from: classes2.dex */
abstract class TransientGate extends Gate {
    private final long mBlockDuration;
    private boolean mIsBlocking;
    private final Runnable mResetGate = new Runnable() { // from class: com.google.android.systemui.elmyra.gates.TransientGate.1
        @Override // java.lang.Runnable
        public void run() {
            TransientGate.this.mIsBlocking = false;
            TransientGate.this.notifyListener();
        }
    };
    private final Handler mResetGateHandler;

    /* access modifiers changed from: package-private */
    public TransientGate(Context context, long j) {
        super(context);
        this.mBlockDuration = j;
        this.mResetGateHandler = new Handler(context.getMainLooper());
    }

    /* access modifiers changed from: protected */
    public void block() {
        this.mIsBlocking = true;
        notifyListener();
        this.mResetGateHandler.removeCallbacks(this.mResetGate);
        this.mResetGateHandler.postDelayed(this.mResetGate, this.mBlockDuration);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        return this.mIsBlocking;
    }
}
