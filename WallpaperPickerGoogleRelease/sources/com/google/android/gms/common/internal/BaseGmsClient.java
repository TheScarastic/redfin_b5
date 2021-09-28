package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.DeadObjectException;
import android.os.Handler;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.util.Log;
import androidx.preference.R$layout;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Feature;
import com.google.android.gms.common.GoogleApiAvailabilityLight;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.internal.zzbt;
import com.google.android.gms.common.api.internal.zzbu;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.internal.IGmsCallbacks;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
/* loaded from: classes.dex */
public abstract class BaseGmsClient<T extends IInterface> {
    public ConnectionProgressReportCallbacks mConnectionProgressReportCallbacks;
    public final Handler zza;
    public int zzc;
    public long zzd;
    public long zze;
    public int zzf;
    public long zzg;
    public zzu zzh;
    public final Context zzi;
    public final GmsClientSupervisor zzk;
    public IGmsServiceBroker zzo;
    public T zzp;
    public zze zzr;
    public final BaseConnectionCallbacks zzt;
    public final BaseOnConnectionFailedListener zzu;
    public final int zzv;
    public final String zzw;
    public final Object zzm = new Object();
    public final Object zzn = new Object();
    public final ArrayList<zzc<?>> zzq = new ArrayList<>();
    public int zzs = 1;
    public ConnectionResult zzx = null;
    public boolean zzy = false;
    public AtomicInteger mDisconnectCount = new AtomicInteger(0);

    /* loaded from: classes.dex */
    public interface BaseConnectionCallbacks {
    }

    /* loaded from: classes.dex */
    public interface BaseOnConnectionFailedListener {
    }

    /* loaded from: classes.dex */
    public interface ConnectionProgressReportCallbacks {
        void onReportServiceBinding(ConnectionResult connectionResult);
    }

    /* loaded from: classes.dex */
    public interface SignOutCallbacks {
    }

    /* loaded from: classes.dex */
    public abstract class zza extends zzc<Boolean> {
        public final int zza;
        public final Bundle zzb;

        public zza(int i, Bundle bundle) {
            super(Boolean.TRUE);
            this.zza = i;
            this.zzb = bundle;
        }

        public abstract void zza(ConnectionResult connectionResult);

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // com.google.android.gms.common.internal.BaseGmsClient.zzc
        public final /* synthetic */ void zza(Boolean bool) {
            int i = this.zza;
            PendingIntent pendingIntent = null;
            if (i != 0) {
                if (i != 10) {
                    BaseGmsClient.this.zzb(1, null);
                    Bundle bundle = this.zzb;
                    if (bundle != null) {
                        pendingIntent = (PendingIntent) bundle.getParcelable("pendingIntent");
                    }
                    zza(new ConnectionResult(this.zza, pendingIntent));
                    return;
                }
                BaseGmsClient.this.zzb(1, null);
                throw new IllegalStateException("A fatal developer error has occurred. Check the logs for further information.");
            } else if (!zza()) {
                BaseGmsClient.this.zzb(1, null);
                zza(new ConnectionResult(8, null));
            }
        }

        public abstract boolean zza();

        @Override // com.google.android.gms.common.internal.BaseGmsClient.zzc
        public final void zzb() {
        }
    }

    /* loaded from: classes.dex */
    public final class zzb extends Handler {
        public zzb(Looper looper) {
            super(looper);
        }

        public static boolean zzb(Message message) {
            int i = message.what;
            return i == 2 || i == 1 || i == 7;
        }

        /* JADX WARNING: Removed duplicated region for block: B:29:0x0076  */
        /* JADX WARNING: Removed duplicated region for block: B:35:0x0087  */
        @Override // android.os.Handler
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public final void handleMessage(android.os.Message r8) {
            /*
            // Method dump skipped, instructions count: 378
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.BaseGmsClient.zzb.handleMessage(android.os.Message):void");
        }
    }

    /* loaded from: classes.dex */
    public abstract class zzc<TListener> {
        public TListener zza;
        public boolean zzb = false;

        public zzc(TListener tlistener) {
            this.zza = tlistener;
        }

        public abstract void zza(TListener tlistener);

        public abstract void zzb();

        public final void zzd() {
            synchronized (this) {
                this.zza = null;
            }
            synchronized (BaseGmsClient.this.zzq) {
                BaseGmsClient.this.zzq.remove(this);
            }
        }
    }

    /* loaded from: classes.dex */
    public static final class zzd extends IGmsCallbacks.zza {
        public BaseGmsClient zza;
        public final int zzb;

        public zzd(BaseGmsClient baseGmsClient, int i) {
            this.zza = baseGmsClient;
            this.zzb = i;
        }
    }

    /* loaded from: classes.dex */
    public final class zze implements ServiceConnection {
        public final int zza;

        public zze(int i) {
            this.zza = i;
        }

        @Override // android.content.ServiceConnection
        public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            IGmsServiceBroker iGmsServiceBroker;
            int i;
            boolean z = false;
            if (iBinder == null) {
                BaseGmsClient baseGmsClient = BaseGmsClient.this;
                synchronized (baseGmsClient.zzm) {
                    if (baseGmsClient.zzs == 3) {
                        z = true;
                    }
                }
                if (z) {
                    i = 5;
                    baseGmsClient.zzy = true;
                } else {
                    i = 4;
                }
                Handler handler = baseGmsClient.zza;
                handler.sendMessage(handler.obtainMessage(i, baseGmsClient.mDisconnectCount.get(), 16));
                return;
            }
            synchronized (BaseGmsClient.this.zzn) {
                BaseGmsClient baseGmsClient2 = BaseGmsClient.this;
                IInterface queryLocalInterface = iBinder.queryLocalInterface("com.google.android.gms.common.internal.IGmsServiceBroker");
                if (queryLocalInterface == null || !(queryLocalInterface instanceof IGmsServiceBroker)) {
                    iGmsServiceBroker = new zzad(iBinder);
                } else {
                    iGmsServiceBroker = (IGmsServiceBroker) queryLocalInterface;
                }
                baseGmsClient2.zzo = iGmsServiceBroker;
            }
            BaseGmsClient baseGmsClient3 = BaseGmsClient.this;
            int i2 = this.zza;
            Handler handler2 = baseGmsClient3.zza;
            handler2.sendMessage(handler2.obtainMessage(7, i2, -1, new zzh(0)));
        }

        @Override // android.content.ServiceConnection
        public final void onServiceDisconnected(ComponentName componentName) {
            BaseGmsClient baseGmsClient;
            synchronized (BaseGmsClient.this.zzn) {
                baseGmsClient = BaseGmsClient.this;
                baseGmsClient.zzo = null;
            }
            Handler handler = baseGmsClient.zza;
            handler.sendMessage(handler.obtainMessage(6, this.zza, 1));
        }
    }

    /* loaded from: classes.dex */
    public class zzf implements ConnectionProgressReportCallbacks {
        public zzf() {
        }

        @Override // com.google.android.gms.common.internal.BaseGmsClient.ConnectionProgressReportCallbacks
        public final void onReportServiceBinding(ConnectionResult connectionResult) {
            if (connectionResult.isSuccess()) {
                BaseGmsClient baseGmsClient = BaseGmsClient.this;
                baseGmsClient.getRemoteService(null, ((zzl) baseGmsClient).zzd);
                return;
            }
            BaseOnConnectionFailedListener baseOnConnectionFailedListener = BaseGmsClient.this.zzu;
            if (baseOnConnectionFailedListener != null) {
                ((zzn) baseOnConnectionFailedListener).zza.onConnectionFailed(connectionResult);
            }
        }
    }

    public BaseGmsClient(Context context, Looper looper, GmsClientSupervisor gmsClientSupervisor, GoogleApiAvailabilityLight googleApiAvailabilityLight, int i, BaseConnectionCallbacks baseConnectionCallbacks, BaseOnConnectionFailedListener baseOnConnectionFailedListener, String str) {
        R$layout.zza(context, "Context must not be null");
        this.zzi = context;
        R$layout.zza(looper, "Looper must not be null");
        R$layout.zza(gmsClientSupervisor, "Supervisor must not be null");
        this.zzk = gmsClientSupervisor;
        R$layout.zza(googleApiAvailabilityLight, "API availability must not be null");
        this.zza = new zzb(looper);
        this.zzv = i;
        this.zzt = baseConnectionCallbacks;
        this.zzu = baseOnConnectionFailedListener;
        this.zzw = str;
    }

    public static boolean zza(BaseGmsClient baseGmsClient, int i, int i2, IInterface iInterface) {
        boolean z;
        synchronized (baseGmsClient.zzm) {
            if (baseGmsClient.zzs != i) {
                z = false;
            } else {
                baseGmsClient.zzb(i2, iInterface);
                z = true;
            }
        }
        return z;
    }

    public void connect(ConnectionProgressReportCallbacks connectionProgressReportCallbacks) {
        R$layout.zza(connectionProgressReportCallbacks, "Connection progress callbacks cannot be null.");
        this.mConnectionProgressReportCallbacks = connectionProgressReportCallbacks;
        zzb(2, null);
    }

    public void disconnect() {
        this.mDisconnectCount.incrementAndGet();
        synchronized (this.zzq) {
            int size = this.zzq.size();
            for (int i = 0; i < size; i++) {
                zzc<?> zzc2 = this.zzq.get(i);
                synchronized (zzc2) {
                    zzc2.zza = null;
                }
            }
            this.zzq.clear();
        }
        synchronized (this.zzn) {
            this.zzo = null;
        }
        zzb(1, null);
    }

    public void getRemoteService(IAccountAccessor iAccountAccessor, Set<Scope> set) {
        Bundle zzb2 = zzb();
        GetServiceRequest getServiceRequest = new GetServiceRequest(this.zzv);
        getServiceRequest.zzd = this.zzi.getPackageName();
        getServiceRequest.zzg = zzb2;
        if (set != null) {
            getServiceRequest.zzf = (Scope[]) set.toArray(new Scope[set.size()]);
        }
        if (requiresSignIn()) {
            Account account = ((zzl) this).zze;
            if (account == null) {
                account = new Account("<<default account>>", "com.google");
            }
            getServiceRequest.zzh = account;
            if (iAccountAccessor != null) {
                getServiceRequest.zze = iAccountAccessor.asBinder();
            }
        }
        getServiceRequest.zzi = new Feature[0];
        try {
            try {
                synchronized (this.zzn) {
                    IGmsServiceBroker iGmsServiceBroker = this.zzo;
                    if (iGmsServiceBroker != null) {
                        iGmsServiceBroker.getService(new zzd(this, this.mDisconnectCount.get()), getServiceRequest);
                    } else {
                        Log.w("GmsClient", "mServiceBroker is null, client disconnected");
                    }
                }
            } catch (RemoteException | RuntimeException e) {
                Log.w("GmsClient", "IGmsServiceBroker.getService failed", e);
                int i = this.mDisconnectCount.get();
                Handler handler = this.zza;
                handler.sendMessage(handler.obtainMessage(1, i, -1, new zzg(8, null, null)));
            }
        } catch (DeadObjectException e2) {
            Log.w("GmsClient", "IGmsServiceBroker.getService failed", e2);
            Handler handler2 = this.zza;
            handler2.sendMessage(handler2.obtainMessage(6, this.mDisconnectCount.get(), 1));
        } catch (SecurityException e3) {
            throw e3;
        }
    }

    public abstract String getServiceDescriptor();

    public abstract String getStartServiceAction();

    public boolean isConnected() {
        boolean z;
        synchronized (this.zzm) {
            z = this.zzs == 4;
        }
        return z;
    }

    public boolean isConnecting() {
        boolean z;
        synchronized (this.zzm) {
            int i = this.zzs;
            if (!(i == 2 || i == 3)) {
                z = false;
            }
            z = true;
        }
        return z;
    }

    public void onConnectionFailed(ConnectionResult connectionResult) {
        this.zzf = connectionResult.zzc;
        this.zzg = System.currentTimeMillis();
    }

    public void onUserSignOut(SignOutCallbacks signOutCallbacks) {
        zzbt zzbt = (zzbt) signOutCallbacks;
        zzbt.zza.zza.zzq.post(new zzbu(zzbt));
    }

    public boolean requiresGooglePlayServices() {
        return true;
    }

    @Override // com.google.android.gms.common.api.Api.Client
    public boolean requiresSignIn() {
        return false;
    }

    public abstract T zza(IBinder iBinder);

    public final String zzab() {
        zzu zzu;
        if (!isConnected() || (zzu = this.zzh) == null) {
            throw new RuntimeException("Failed to connect when checking package");
        }
        Objects.requireNonNull(zzu);
        return "com.google.android.gms";
    }

    public final T zzag() throws DeadObjectException {
        T t;
        synchronized (this.zzm) {
            if (this.zzs == 5) {
                throw new DeadObjectException();
            } else if (isConnected()) {
                R$layout.zza(this.zzp != null, "Client is connected but service is null");
                t = this.zzp;
            } else {
                throw new IllegalStateException("Not connected. Call connect() and wait for onConnected() to be called.");
            }
        }
        return t;
    }

    public final void zzb(int i, T t) {
        zzu zzu;
        R$layout.zzb((i == 4) == (t != null));
        synchronized (this.zzm) {
            this.zzs = i;
            this.zzp = t;
            if (i != 1) {
                if (i == 2 || i == 3) {
                    if (!(this.zzr == null || (zzu = this.zzh) == null)) {
                        String str = zzu.zza;
                        StringBuilder sb = new StringBuilder(String.valueOf(str).length() + 70 + "com.google.android.gms".length());
                        sb.append("Calling connect() while still connected, missing disconnect() for ");
                        sb.append(str);
                        sb.append(" on ");
                        sb.append("com.google.android.gms");
                        Log.e("GmsClient", sb.toString());
                        GmsClientSupervisor gmsClientSupervisor = this.zzk;
                        String str2 = this.zzh.zza;
                        zze zze2 = this.zzr;
                        String zzc2 = zzc();
                        Objects.requireNonNull(gmsClientSupervisor);
                        gmsClientSupervisor.unbindService(new GmsClientSupervisor.ConnectionStatusConfig(str2, "com.google.android.gms", 129), zze2, zzc2);
                        this.mDisconnectCount.incrementAndGet();
                    }
                    this.zzr = new zze(this.mDisconnectCount.get());
                    String startServiceAction = getStartServiceAction();
                    this.zzh = new zzu("com.google.android.gms", startServiceAction, false, 129);
                    GmsClientSupervisor gmsClientSupervisor2 = this.zzk;
                    zze zze3 = this.zzr;
                    String zzc3 = zzc();
                    Objects.requireNonNull(gmsClientSupervisor2);
                    if (!gmsClientSupervisor2.bindService(new GmsClientSupervisor.ConnectionStatusConfig(startServiceAction, "com.google.android.gms", 129), zze3, zzc3)) {
                        String str3 = this.zzh.zza;
                        StringBuilder sb2 = new StringBuilder(String.valueOf(str3).length() + 34 + "com.google.android.gms".length());
                        sb2.append("unable to connect to service: ");
                        sb2.append(str3);
                        sb2.append(" on ");
                        sb2.append("com.google.android.gms");
                        Log.e("GmsClient", sb2.toString());
                        int i2 = this.mDisconnectCount.get();
                        Handler handler = this.zza;
                        handler.sendMessage(handler.obtainMessage(7, i2, -1, new zzh(16)));
                    }
                } else if (i == 4) {
                    this.zze = System.currentTimeMillis();
                }
            } else if (this.zzr != null) {
                GmsClientSupervisor gmsClientSupervisor3 = this.zzk;
                String startServiceAction2 = getStartServiceAction();
                zze zze4 = this.zzr;
                String zzc4 = zzc();
                Objects.requireNonNull(gmsClientSupervisor3);
                gmsClientSupervisor3.unbindService(new GmsClientSupervisor.ConnectionStatusConfig(startServiceAction2, "com.google.android.gms", 129), zze4, zzc4);
                this.zzr = null;
            }
        }
    }

    public final String zzc() {
        String str = this.zzw;
        return str == null ? this.zzi.getClass().getName() : str;
    }

    /* loaded from: classes.dex */
    public final class zzh extends zza {
        public zzh(int i) {
            super(i, null);
        }

        @Override // com.google.android.gms.common.internal.BaseGmsClient.zza
        public final void zza(ConnectionResult connectionResult) {
            BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(connectionResult);
            BaseGmsClient.this.onConnectionFailed(connectionResult);
        }

        @Override // com.google.android.gms.common.internal.BaseGmsClient.zza
        public final boolean zza() {
            BaseGmsClient.this.mConnectionProgressReportCallbacks.onReportServiceBinding(ConnectionResult.zza);
            return true;
        }
    }

    /* loaded from: classes.dex */
    public final class zzg extends zza {
        public final IBinder zza;

        public zzg(int i, IBinder iBinder, Bundle bundle) {
            super(i, bundle);
            this.zza = iBinder;
        }

        @Override // com.google.android.gms.common.internal.BaseGmsClient.zza
        public final void zza(ConnectionResult connectionResult) {
            BaseOnConnectionFailedListener baseOnConnectionFailedListener = BaseGmsClient.this.zzu;
            if (baseOnConnectionFailedListener != null) {
                ((zzn) baseOnConnectionFailedListener).zza.onConnectionFailed(connectionResult);
            }
            BaseGmsClient.this.onConnectionFailed(connectionResult);
        }

        @Override // com.google.android.gms.common.internal.BaseGmsClient.zza
        public final boolean zza() {
            try {
                String interfaceDescriptor = this.zza.getInterfaceDescriptor();
                if (!BaseGmsClient.this.getServiceDescriptor().equals(interfaceDescriptor)) {
                    String serviceDescriptor = BaseGmsClient.this.getServiceDescriptor();
                    Log.e("GmsClient", Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline1.m(XMPPathFactory$$ExternalSyntheticOutline0.m(interfaceDescriptor, XMPPathFactory$$ExternalSyntheticOutline0.m(serviceDescriptor, 34)), "service descriptor mismatch: ", serviceDescriptor, " vs. ", interfaceDescriptor));
                    return false;
                }
                IInterface zza = BaseGmsClient.this.zza(this.zza);
                if (zza == null || (!BaseGmsClient.zza(BaseGmsClient.this, 2, 4, zza) && !BaseGmsClient.zza(BaseGmsClient.this, 3, 4, zza))) {
                    return false;
                }
                BaseGmsClient baseGmsClient = BaseGmsClient.this;
                baseGmsClient.zzx = null;
                BaseConnectionCallbacks baseConnectionCallbacks = baseGmsClient.zzt;
                if (baseConnectionCallbacks == null) {
                    return true;
                }
                ((zzm) baseConnectionCallbacks).zza.onConnected(null);
                return true;
            } catch (RemoteException unused) {
                Log.w("GmsClient", "service probably died");
                return false;
            }
        }
    }

    public Bundle zzb() {
        return new Bundle();
    }
}
