package com.google.android.systemui.columbus;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusServiceProxy.kt */
/* loaded from: classes2.dex */
public final class ColumbusServiceProxy extends Service {
    public static final Companion Companion = new Companion(null);
    private final List<ColumbusServiceListener> columbusServiceListeners = new ArrayList();
    private final ColumbusServiceProxy$binder$1 binder = new ColumbusServiceProxy$binder$1(this);

    @Override // android.app.Service
    public int onStartCommand(Intent intent, int i, int i2) {
        Intrinsics.checkNotNullParameter(intent, "intent");
        return 0;
    }

    @Override // android.app.Service
    public IBinder onBind(Intent intent) {
        return this.binder;
    }

    /* access modifiers changed from: private */
    public final void checkPermission() {
        enforceCallingOrSelfPermission("com.google.android.columbus.permission.CONFIGURE_COLUMBUS_GESTURE", "Must have com.google.android.columbus.permission.CONFIGURE_COLUMBUS_GESTURE permission");
    }

    /* compiled from: ColumbusServiceProxy.kt */
    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class ColumbusServiceListener implements IBinder.DeathRecipient {
        private IColumbusServiceListener listener;
        private IBinder token;

        public ColumbusServiceListener(IBinder iBinder, IColumbusServiceListener iColumbusServiceListener) {
            this.token = iBinder;
            this.listener = iColumbusServiceListener;
            linkToDeath();
        }

        public final IBinder getToken() {
            return this.token;
        }

        public final IColumbusServiceListener getListener() {
            return this.listener;
        }

        private final void linkToDeath() {
            IBinder iBinder = this.token;
            if (iBinder != null) {
                try {
                    iBinder.linkToDeath(this, 0);
                    Unit unit = Unit.INSTANCE;
                } catch (RemoteException e) {
                    Log.e("Columbus/ColumbusProxy", "Unable to linkToDeath", e);
                }
            }
        }

        public final Boolean unlinkToDeath() {
            IBinder iBinder = this.token;
            if (iBinder == null) {
                return null;
            }
            return Boolean.valueOf(iBinder.unlinkToDeath(this, 0));
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            Log.w("Columbus/ColumbusProxy", "ColumbusServiceListener binder died");
            this.token = null;
            this.listener = null;
        }
    }

    /* compiled from: ColumbusServiceProxy.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
