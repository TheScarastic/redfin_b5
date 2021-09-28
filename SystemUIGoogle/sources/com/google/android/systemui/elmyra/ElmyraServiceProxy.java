package com.google.android.systemui.elmyra;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.elmyra.IElmyraService;
import com.google.android.systemui.elmyra.IElmyraServiceListener;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class ElmyraServiceProxy extends Service {
    private final List<ElmyraServiceListener> mElmyraServiceListeners = new ArrayList();
    private final IElmyraService.Stub mBinder = new IElmyraService.Stub() { // from class: com.google.android.systemui.elmyra.ElmyraServiceProxy.1
        @Override // com.google.android.systemui.elmyra.IElmyraService
        public void registerGestureListener(IBinder iBinder, IBinder iBinder2) {
            ElmyraServiceProxy.this.checkPermission();
            try {
                for (int size = ElmyraServiceProxy.this.mElmyraServiceListeners.size() - 1; size >= 0; size--) {
                    IElmyraServiceListener listener = ((ElmyraServiceListener) ElmyraServiceProxy.this.mElmyraServiceListeners.get(size)).getListener();
                    if (listener == null) {
                        ElmyraServiceProxy.this.mElmyraServiceListeners.remove(size);
                    } else {
                        listener.setListener(iBinder, iBinder2);
                    }
                }
            } catch (RemoteException e) {
                Log.e("Elmyra/ElmyraServiceProxy", "Action isn't connected", e);
            }
        }

        @Override // com.google.android.systemui.elmyra.IElmyraService
        public void triggerAction() {
            ElmyraServiceProxy.this.checkPermission();
            try {
                for (int size = ElmyraServiceProxy.this.mElmyraServiceListeners.size() - 1; size >= 0; size--) {
                    IElmyraServiceListener listener = ((ElmyraServiceListener) ElmyraServiceProxy.this.mElmyraServiceListeners.get(size)).getListener();
                    if (listener == null) {
                        ElmyraServiceProxy.this.mElmyraServiceListeners.remove(size);
                    } else {
                        listener.triggerAction();
                    }
                }
            } catch (RemoteException e) {
                Log.e("Elmyra/ElmyraServiceProxy", "Error launching assistant", e);
            }
        }

        @Override // com.google.android.systemui.elmyra.IElmyraService
        public void registerServiceListener(IBinder iBinder, IBinder iBinder2) {
            ElmyraServiceProxy.this.checkPermission();
            if (iBinder == null) {
                Log.e("Elmyra/ElmyraServiceProxy", "Binder token must not be null");
            } else if (iBinder2 == null) {
                for (int i = 0; i < ElmyraServiceProxy.this.mElmyraServiceListeners.size(); i++) {
                    if (iBinder.equals(((ElmyraServiceListener) ElmyraServiceProxy.this.mElmyraServiceListeners.get(i)).getToken())) {
                        ((ElmyraServiceListener) ElmyraServiceProxy.this.mElmyraServiceListeners.get(i)).unlinkToDeath();
                        ElmyraServiceProxy.this.mElmyraServiceListeners.remove(i);
                        return;
                    }
                }
            } else {
                ElmyraServiceProxy.this.mElmyraServiceListeners.add(new ElmyraServiceListener(iBinder, IElmyraServiceListener.Stub.asInterface(iBinder2)));
            }
        }
    };

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        return 0;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ElmyraServiceListener implements IBinder.DeathRecipient {
        private IElmyraServiceListener mListener;
        private IBinder mToken;

        ElmyraServiceListener(IBinder iBinder, IElmyraServiceListener iElmyraServiceListener) {
            this.mToken = iBinder;
            this.mListener = iElmyraServiceListener;
            linkToDeath();
        }

        public IElmyraServiceListener getListener() {
            return this.mListener;
        }

        public IBinder getToken() {
            return this.mToken;
        }

        private void linkToDeath() {
            IBinder iBinder = this.mToken;
            if (iBinder != null) {
                try {
                    iBinder.linkToDeath(this, 0);
                } catch (RemoteException e) {
                    Log.e("Elmyra/ElmyraServiceProxy", "Unable to linkToDeath", e);
                }
            }
        }

        public void unlinkToDeath() {
            IBinder iBinder = this.mToken;
            if (iBinder != null) {
                iBinder.unlinkToDeath(this, 0);
            }
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.w("Elmyra/ElmyraServiceProxy", "ElmyraServiceListener binder died");
            this.mToken = null;
            this.mListener = null;
        }
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.mBinder;
    }

    /* access modifiers changed from: private */
    public void checkPermission() {
        enforceCallingOrSelfPermission("com.google.android.elmyra.permission.CONFIGURE_ASSIST_GESTURE", "Must have com.google.android.elmyra.permission.CONFIGURE_ASSIST_GESTURE permission");
    }
}
