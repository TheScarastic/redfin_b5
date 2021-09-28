package com.google.android.systemui.elmyra.actions;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Binder;
import android.os.DeadObjectException;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import com.google.android.systemui.elmyra.ElmyraServiceProxy;
import com.google.android.systemui.elmyra.IElmyraService;
import com.google.android.systemui.elmyra.IElmyraServiceGestureListener;
import com.google.android.systemui.elmyra.IElmyraServiceListener;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
/* loaded from: classes2.dex */
public abstract class ServiceAction extends Action implements IBinder.DeathRecipient {
    private IElmyraService mElmyraService;
    private final ElmyraServiceConnection mElmyraServiceConnection;
    private IElmyraServiceGestureListener mElmyraServiceGestureListener;
    private final IBinder mToken = new Binder();
    private final ElmyraServiceListener mElmyraServiceListener = new ElmyraServiceListener();

    protected abstract boolean checkSupportedCaller();

    protected void onServiceConnected() {
    }

    protected void onServiceDisconnected() {
    }

    protected void triggerAction() {
    }

    public ServiceAction(Context context, List<FeedbackEffect> list) {
        super(context, list);
        ElmyraServiceConnection elmyraServiceConnection = new ElmyraServiceConnection();
        this.mElmyraServiceConnection = elmyraServiceConnection;
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(getContext(), ElmyraServiceProxy.class));
            getContext().bindService(intent, elmyraServiceConnection, 1);
        } catch (SecurityException e) {
            Log.e("Elmyra/ServiceAction", "Unable to bind to ElmyraServiceProxy", e);
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public boolean isAvailable() {
        return this.mElmyraServiceGestureListener != null;
    }

    /* access modifiers changed from: protected */
    public boolean checkSupportedCaller(String str) {
        String[] packagesForUid = getContext().getPackageManager().getPackagesForUid(Binder.getCallingUid());
        if (packagesForUid == null) {
            return false;
        }
        return Arrays.asList(packagesForUid).contains(str);
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        if (this.mElmyraServiceGestureListener != null) {
            triggerFeedbackEffects(detectionProperties);
            try {
                this.mElmyraServiceGestureListener.onGestureDetected();
            } catch (DeadObjectException e) {
                Log.e("Elmyra/ServiceAction", "Listener crashed or closed without unregistering", e);
                this.mElmyraServiceGestureListener = null;
                notifyListener();
            } catch (RemoteException e2) {
                Log.e("Elmyra/ServiceAction", "Unable to send onGestureDetected; removing listener", e2);
                this.mElmyraServiceGestureListener = null;
                notifyListener();
            }
        }
    }

    @Override // com.google.android.systemui.elmyra.actions.Action
    public void onProgress(float f, int i) {
        if (this.mElmyraServiceGestureListener != null) {
            updateFeedbackEffects(f, i);
            try {
                this.mElmyraServiceGestureListener.onGestureProgress(f, i);
            } catch (DeadObjectException e) {
                Log.e("Elmyra/ServiceAction", "Listener crashed or closed without unregistering", e);
                this.mElmyraServiceGestureListener = null;
                notifyListener();
            } catch (RemoteException e2) {
                Log.e("Elmyra/ServiceAction", "Unable to send progress, setting listener to null", e2);
                this.mElmyraServiceGestureListener = null;
                notifyListener();
            }
        }
    }

    @Override // android.os.IBinder.DeathRecipient
    public void binderDied() {
        Log.w("Elmyra/ServiceAction", "Binder died");
        this.mElmyraServiceGestureListener = null;
        notifyListener();
    }

    /* loaded from: classes2.dex */
    private class ElmyraServiceConnection implements ServiceConnection {
        private ElmyraServiceConnection() {
        }

        @Override // android.content.ServiceConnection
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            ServiceAction.this.mElmyraService = IElmyraService.Stub.asInterface(iBinder);
            try {
                ServiceAction.this.mElmyraService.registerServiceListener(ServiceAction.this.mToken, ServiceAction.this.mElmyraServiceListener);
            } catch (RemoteException e) {
                Log.e("Elmyra/ServiceAction", "Error registering listener", e);
            }
            ServiceAction.this.onServiceConnected();
        }

        @Override // android.content.ServiceConnection
        public void onServiceDisconnected(ComponentName componentName) {
            ServiceAction.this.mElmyraService = null;
            ServiceAction.this.onServiceDisconnected();
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class ElmyraServiceListener extends IElmyraServiceListener.Stub {
        private ElmyraServiceListener() {
        }

        @Override // com.google.android.systemui.elmyra.IElmyraServiceListener
        public void setListener(IBinder iBinder, IBinder iBinder2) {
            if (ServiceAction.this.checkSupportedCaller()) {
                if (iBinder2 != null || ServiceAction.this.mElmyraServiceGestureListener != null) {
                    IElmyraServiceGestureListener asInterface = IElmyraServiceGestureListener.Stub.asInterface(iBinder2);
                    if (asInterface != ServiceAction.this.mElmyraServiceGestureListener) {
                        ServiceAction.this.mElmyraServiceGestureListener = asInterface;
                        ServiceAction.this.notifyListener();
                    }
                    if (iBinder != null) {
                        try {
                            if (iBinder2 != null) {
                                iBinder.linkToDeath(ServiceAction.this, 0);
                            } else {
                                iBinder.unlinkToDeath(ServiceAction.this, 0);
                            }
                        } catch (RemoteException e) {
                            while (true) {
                                Log.e("Elmyra/ServiceAction", "RemoteException during linkToDeath", e);
                                return;
                            }
                        } catch (NoSuchElementException unused) {
                        }
                    }
                }
            }
        }

        @Override // com.google.android.systemui.elmyra.IElmyraServiceListener
        public void triggerAction() {
            if (ServiceAction.this.checkSupportedCaller()) {
                ServiceAction.this.triggerAction();
            }
        }
    }
}
