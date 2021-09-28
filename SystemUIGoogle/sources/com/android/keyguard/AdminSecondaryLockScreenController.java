package com.android.keyguard;

import android.app.admin.IKeyguardCallback;
import android.app.admin.IKeyguardClient;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import com.android.internal.annotations.VisibleForTesting;
import java.util.NoSuchElementException;
/* loaded from: classes.dex */
public class AdminSecondaryLockScreenController {
    private final IKeyguardCallback mCallback;
    private IKeyguardClient mClient;
    private final ServiceConnection mConnection;
    private final Context mContext;
    private Handler mHandler;
    private KeyguardSecurityCallback mKeyguardCallback;
    private final IBinder.DeathRecipient mKeyguardClientDeathRecipient;
    private final ViewGroup mParent;
    @VisibleForTesting
    protected SurfaceHolder.Callback mSurfaceHolderCallback;
    private final KeyguardUpdateMonitorCallback mUpdateCallback;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private AdminSecurityView mView;

    public /* synthetic */ void lambda$new$0() {
        hide();
        Log.d("AdminSecondaryLockScreenController", "KeyguardClient service died");
    }

    private AdminSecondaryLockScreenController(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, KeyguardSecurityCallback keyguardSecurityCallback, Handler handler) {
        this.mConnection = new ServiceConnection() { // from class: com.android.keyguard.AdminSecondaryLockScreenController.1
            @Override // android.content.ServiceConnection
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
                AdminSecondaryLockScreenController.this.mClient = IKeyguardClient.Stub.asInterface(iBinder);
                if (AdminSecondaryLockScreenController.this.mView.isAttachedToWindow() && AdminSecondaryLockScreenController.this.mClient != null) {
                    AdminSecondaryLockScreenController.this.onSurfaceReady();
                    try {
                        iBinder.linkToDeath(AdminSecondaryLockScreenController.this.mKeyguardClientDeathRecipient, 0);
                    } catch (RemoteException e) {
                        Log.e("AdminSecondaryLockScreenController", "Lost connection to secondary lockscreen service", e);
                        AdminSecondaryLockScreenController.this.dismiss(KeyguardUpdateMonitor.getCurrentUser());
                    }
                }
            }

            @Override // android.content.ServiceConnection
            public void onServiceDisconnected(ComponentName componentName) {
                AdminSecondaryLockScreenController.this.mClient = null;
            }
        };
        this.mKeyguardClientDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.keyguard.AdminSecondaryLockScreenController$$ExternalSyntheticLambda0
            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                AdminSecondaryLockScreenController.m2$r8$lambda$qAfrlb0g71NK6qpAwXaDSRz5zk(AdminSecondaryLockScreenController.this);
            }
        };
        this.mCallback = new IKeyguardCallback.Stub() { // from class: com.android.keyguard.AdminSecondaryLockScreenController.2
            public void onDismiss() {
                AdminSecondaryLockScreenController.this.mHandler.post(new AdminSecondaryLockScreenController$2$$ExternalSyntheticLambda1(this));
            }

            public /* synthetic */ void lambda$onDismiss$0() {
                AdminSecondaryLockScreenController.this.dismiss(UserHandle.getCallingUserId());
            }

            public void onRemoteContentReady(SurfaceControlViewHost.SurfacePackage surfacePackage) {
                if (AdminSecondaryLockScreenController.this.mHandler != null) {
                    AdminSecondaryLockScreenController.this.mHandler.removeCallbacksAndMessages(null);
                }
                if (surfacePackage != null) {
                    AdminSecondaryLockScreenController.this.mView.setChildSurfacePackage(surfacePackage);
                } else {
                    AdminSecondaryLockScreenController.this.mHandler.post(new AdminSecondaryLockScreenController$2$$ExternalSyntheticLambda0(this));
                }
            }

            public /* synthetic */ void lambda$onRemoteContentReady$1() {
                AdminSecondaryLockScreenController.this.dismiss(KeyguardUpdateMonitor.getCurrentUser());
            }
        };
        this.mUpdateCallback = new KeyguardUpdateMonitorCallback() { // from class: com.android.keyguard.AdminSecondaryLockScreenController.3
            @Override // com.android.keyguard.KeyguardUpdateMonitorCallback
            public void onSecondaryLockscreenRequirementChanged(int i) {
                if (AdminSecondaryLockScreenController.this.mUpdateMonitor.getSecondaryLockscreenRequirement(i) == null) {
                    AdminSecondaryLockScreenController.this.dismiss(i);
                }
            }
        };
        this.mSurfaceHolderCallback = new SurfaceHolder.Callback() { // from class: com.android.keyguard.AdminSecondaryLockScreenController.4
            @Override // android.view.SurfaceHolder.Callback
            public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceCreated(SurfaceHolder surfaceHolder) {
                int currentUser = KeyguardUpdateMonitor.getCurrentUser();
                AdminSecondaryLockScreenController.this.mUpdateMonitor.registerCallback(AdminSecondaryLockScreenController.this.mUpdateCallback);
                if (AdminSecondaryLockScreenController.this.mClient != null) {
                    AdminSecondaryLockScreenController.this.onSurfaceReady();
                }
                AdminSecondaryLockScreenController.this.mHandler.postDelayed(new AdminSecondaryLockScreenController$4$$ExternalSyntheticLambda0(this, currentUser), 500);
            }

            public /* synthetic */ void lambda$surfaceCreated$0(int i) {
                AdminSecondaryLockScreenController.this.dismiss(i);
                Log.w("AdminSecondaryLockScreenController", "Timed out waiting for secondary lockscreen content.");
            }

            @Override // android.view.SurfaceHolder.Callback
            public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                AdminSecondaryLockScreenController.this.mUpdateMonitor.removeCallback(AdminSecondaryLockScreenController.this.mUpdateCallback);
            }
        };
        this.mContext = context;
        this.mHandler = handler;
        this.mParent = keyguardSecurityContainer;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mKeyguardCallback = keyguardSecurityCallback;
        this.mView = new AdminSecurityView(context, this.mSurfaceHolderCallback);
    }

    public void show(Intent intent) {
        if (this.mClient == null) {
            this.mContext.bindService(intent, this.mConnection, 1);
        }
        if (!this.mView.isAttachedToWindow()) {
            this.mParent.addView(this.mView);
        }
    }

    public void hide() {
        if (this.mView.isAttachedToWindow()) {
            this.mParent.removeView(this.mView);
        }
        IKeyguardClient iKeyguardClient = this.mClient;
        if (iKeyguardClient != null) {
            try {
                iKeyguardClient.asBinder().unlinkToDeath(this.mKeyguardClientDeathRecipient, 0);
            } catch (NoSuchElementException unused) {
                Log.w("AdminSecondaryLockScreenController", "IKeyguardClient death recipient already released");
            }
            this.mContext.unbindService(this.mConnection);
            this.mClient = null;
        }
    }

    public void onSurfaceReady() {
        try {
            IBinder hostToken = this.mView.getHostToken();
            if (hostToken != null) {
                this.mClient.onCreateKeyguardSurface(hostToken, this.mCallback);
            } else {
                hide();
            }
        } catch (RemoteException e) {
            while (true) {
                Log.e("AdminSecondaryLockScreenController", "Error in onCreateKeyguardSurface", e);
                dismiss(KeyguardUpdateMonitor.getCurrentUser());
                return;
            }
        }
    }

    public void dismiss(int i) {
        this.mHandler.removeCallbacksAndMessages(null);
        if (this.mView.isAttachedToWindow() && i == KeyguardUpdateMonitor.getCurrentUser()) {
            hide();
            KeyguardSecurityCallback keyguardSecurityCallback = this.mKeyguardCallback;
            if (keyguardSecurityCallback != null) {
                keyguardSecurityCallback.dismiss(true, i, true);
            }
        }
    }

    /* loaded from: classes.dex */
    public class AdminSecurityView extends SurfaceView {
        private SurfaceHolder.Callback mSurfaceHolderCallback;

        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        AdminSecurityView(Context context, SurfaceHolder.Callback callback) {
            super(context);
            AdminSecondaryLockScreenController.this = r1;
            this.mSurfaceHolderCallback = callback;
            setZOrderOnTop(true);
        }

        @Override // android.view.SurfaceView, android.view.View
        protected void onAttachedToWindow() {
            super.onAttachedToWindow();
            getHolder().addCallback(this.mSurfaceHolderCallback);
        }

        @Override // android.view.SurfaceView, android.view.View
        protected void onDetachedFromWindow() {
            super.onDetachedFromWindow();
            getHolder().removeCallback(this.mSurfaceHolderCallback);
        }
    }

    /* loaded from: classes.dex */
    public static class Factory {
        private final Context mContext;
        private final Handler mHandler;
        private final KeyguardSecurityContainer mParent;
        private final KeyguardUpdateMonitor mUpdateMonitor;

        public Factory(Context context, KeyguardSecurityContainer keyguardSecurityContainer, KeyguardUpdateMonitor keyguardUpdateMonitor, Handler handler) {
            this.mContext = context;
            this.mParent = keyguardSecurityContainer;
            this.mUpdateMonitor = keyguardUpdateMonitor;
            this.mHandler = handler;
        }

        public AdminSecondaryLockScreenController create(KeyguardSecurityCallback keyguardSecurityCallback) {
            return new AdminSecondaryLockScreenController(this.mContext, this.mParent, this.mUpdateMonitor, keyguardSecurityCallback, this.mHandler);
        }
    }
}
