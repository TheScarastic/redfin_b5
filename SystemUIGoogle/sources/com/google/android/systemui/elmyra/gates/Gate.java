package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import android.os.Handler;
/* loaded from: classes2.dex */
public abstract class Gate {
    private boolean mActive = false;
    private final Context mContext;
    private Listener mListener;
    private final Handler mNotifyHandler;

    /* loaded from: classes2.dex */
    public interface Listener {
        void onGateChanged(Gate gate);
    }

    protected abstract boolean isBlocked();

    protected abstract void onActivate();

    protected abstract void onDeactivate();

    public Gate(Context context) {
        this.mContext = context;
        this.mNotifyHandler = new Handler(context.getMainLooper());
    }

    public void activate() {
        if (!isActive()) {
            this.mActive = true;
            onActivate();
        }
    }

    public void deactivate() {
        if (isActive()) {
            this.mActive = false;
            onDeactivate();
        }
    }

    public final boolean isActive() {
        return this.mActive;
    }

    public final boolean isBlocking() {
        return isActive() && isBlocked();
    }

    /* access modifiers changed from: protected */
    public void notifyListener() {
        if (isActive() && this.mListener != null) {
            this.mNotifyHandler.post(new Runnable() { // from class: com.google.android.systemui.elmyra.gates.Gate$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Gate.this.lambda$notifyListener$0();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListener$0() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onGateChanged(this);
        }
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mContext;
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
