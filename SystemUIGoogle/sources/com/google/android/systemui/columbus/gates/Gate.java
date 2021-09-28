package com.google.android.systemui.columbus.gates;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import com.google.android.systemui.columbus.gates.Gate;
import java.util.LinkedHashSet;
import java.util.Set;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: Gate.kt */
/* loaded from: classes2.dex */
public abstract class Gate {
    private boolean active;
    private final Context context;
    private boolean isBlocked;
    private final Set<Listener> listeners;
    private final Handler notifyHandler;

    /* compiled from: Gate.kt */
    /* loaded from: classes2.dex */
    public interface Listener {
        void onGateChanged(Gate gate);
    }

    protected abstract void onActivate();

    protected abstract void onDeactivate();

    public Gate(Context context, Handler handler) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(handler, "notifyHandler");
        this.context = context;
        this.notifyHandler = handler;
        this.listeners = new LinkedHashSet();
    }

    public final Context getContext() {
        return this.context;
    }

    public /* synthetic */ Gate(Context context, Handler handler, int i, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i & 2) != 0 ? new Handler(Looper.getMainLooper()) : handler);
    }

    public final boolean getActive() {
        return this.active;
    }

    public void registerListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.add(listener);
        maybeActivate();
    }

    public void unregisterListener(Listener listener) {
        Intrinsics.checkNotNullParameter(listener, "listener");
        this.listeners.remove(listener);
        maybeDeactivate();
    }

    private final void maybeActivate() {
        if (!this.active && (!this.listeners.isEmpty())) {
            this.active = true;
            onActivate();
        }
    }

    private final void maybeDeactivate() {
        if (this.active && this.listeners.isEmpty()) {
            this.active = false;
            onDeactivate();
        }
    }

    /* access modifiers changed from: protected */
    public final void setBlocking(boolean z) {
        if (this.isBlocked != z) {
            this.isBlocked = z;
            notifyListeners();
        }
    }

    public boolean isBlocking() {
        return this.active && this.isBlocked;
    }

    private final void notifyListeners() {
        if (this.active) {
            for (Listener listener : this.listeners) {
                this.notifyHandler.post(new Runnable(listener, this) { // from class: com.google.android.systemui.columbus.gates.Gate$notifyListeners$1$1
                    final /* synthetic */ Gate.Listener $it;
                    final /* synthetic */ Gate this$0;

                    /* access modifiers changed from: package-private */
                    {
                        this.$it = r1;
                        this.this$0 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        this.$it.onGateChanged(this.this$0);
                    }
                });
            }
        }
    }

    public String toString() {
        String simpleName = getClass().getSimpleName();
        Intrinsics.checkNotNullExpressionValue(simpleName, "javaClass.simpleName");
        return simpleName;
    }
}
