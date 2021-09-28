package com.android.systemui.shortcut;

import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import com.android.internal.policy.IShortcutService;
/* loaded from: classes.dex */
public class ShortcutKeyServiceProxy extends IShortcutService.Stub {
    private Callbacks mCallbacks;
    private final Object mLock = new Object();
    private final Handler mHandler = new H();

    /* loaded from: classes.dex */
    public interface Callbacks {
        void onShortcutKeyPressed(long j);
    }

    public ShortcutKeyServiceProxy(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public void notifyShortcutKeyPressed(long j) throws RemoteException {
        synchronized (this.mLock) {
            this.mHandler.obtainMessage(1, Long.valueOf(j)).sendToTarget();
        }
    }

    /* loaded from: classes.dex */
    private final class H extends Handler {
        private H() {
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            if (message.what == 1) {
                ShortcutKeyServiceProxy.this.mCallbacks.onShortcutKeyPressed(((Long) message.obj).longValue());
            }
        }
    }
}
