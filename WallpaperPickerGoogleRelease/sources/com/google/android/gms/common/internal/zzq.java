package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.Context;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import androidx.preference.R$layout;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import com.google.android.gms.common.stats.zza;
import java.util.HashMap;
import java.util.Objects;
/* loaded from: classes.dex */
public final class zzq extends GmsClientSupervisor implements Handler.Callback {
    public final HashMap<GmsClientSupervisor.ConnectionStatusConfig, zzr> zza = new HashMap<>();
    public final Context zzb;
    public final Handler zzc;
    public final zza zzd;
    public final long zze;
    public final long zzf;

    public zzq(Context context) {
        this.zzb = context.getApplicationContext();
        this.zzc = new Handler(context.getMainLooper(), this);
        if (zza.zzb == null) {
            synchronized (zza.zza) {
                if (zza.zzb == null) {
                    zza.zzb = new zza();
                }
            }
        }
        this.zzd = zza.zzb;
        this.zze = 5000;
        this.zzf = 300000;
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    public final boolean bindService(GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str) {
        boolean z;
        R$layout.zza(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zza) {
            zzr zzr = this.zza.get(connectionStatusConfig);
            if (zzr == null) {
                zzr = new zzr(this, connectionStatusConfig);
                connectionStatusConfig.getStartServiceIntent();
                zzr.zza.add(serviceConnection);
                zzr.zza(str);
                this.zza.put(connectionStatusConfig, zzr);
            } else {
                this.zzc.removeMessages(0, connectionStatusConfig);
                if (!zzr.zza.contains(serviceConnection)) {
                    zza zza = zzr.zzg.zzd;
                    zzr.zze.getStartServiceIntent();
                    zzr.zza.add(serviceConnection);
                    int i = zzr.zzb;
                    if (i == 1) {
                        serviceConnection.onServiceConnected(zzr.zzf, zzr.zzd);
                    } else if (i == 2) {
                        zzr.zza(str);
                    }
                } else {
                    String valueOf = String.valueOf(connectionStatusConfig);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 81);
                    sb.append("Trying to bind a GmsServiceConnection that was already connected before.  config=");
                    sb.append(valueOf);
                    throw new IllegalStateException(sb.toString());
                }
            }
            z = zzr.zzc;
        }
        return z;
    }

    @Override // android.os.Handler.Callback
    public final boolean handleMessage(Message message) {
        int i = message.what;
        if (i == 0) {
            synchronized (this.zza) {
                GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig = (GmsClientSupervisor.ConnectionStatusConfig) message.obj;
                zzr zzr = this.zza.get(connectionStatusConfig);
                if (zzr != null && zzr.zza.isEmpty()) {
                    if (zzr.zzc) {
                        zzr.zzg.zzc.removeMessages(1, zzr.zze);
                        zzq zzq = zzr.zzg;
                        zza zza = zzq.zzd;
                        zzq.zzb.unbindService(zzr);
                        zzr.zzc = false;
                        zzr.zzb = 2;
                    }
                    this.zza.remove(connectionStatusConfig);
                }
            }
            return true;
        } else if (i != 1) {
            return false;
        } else {
            synchronized (this.zza) {
                GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig2 = (GmsClientSupervisor.ConnectionStatusConfig) message.obj;
                zzr zzr2 = this.zza.get(connectionStatusConfig2);
                if (zzr2 != null && zzr2.zzb == 3) {
                    String valueOf = String.valueOf(connectionStatusConfig2);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 47);
                    sb.append("Timeout waiting for ServiceConnection callback ");
                    sb.append(valueOf);
                    Log.wtf("GmsClientSupervisor", sb.toString(), new Exception());
                    ComponentName componentName = zzr2.zzf;
                    if (componentName == null) {
                        Objects.requireNonNull(connectionStatusConfig2);
                        componentName = null;
                    }
                    if (componentName == null) {
                        componentName = new ComponentName(connectionStatusConfig2.zzb, "unknown");
                    }
                    zzr2.onServiceDisconnected(componentName);
                }
            }
            return true;
        }
    }

    @Override // com.google.android.gms.common.internal.GmsClientSupervisor
    public final void unbindService(GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str) {
        R$layout.zza(serviceConnection, "ServiceConnection must not be null");
        synchronized (this.zza) {
            zzr zzr = this.zza.get(connectionStatusConfig);
            if (zzr == null) {
                String valueOf = String.valueOf(connectionStatusConfig);
                StringBuilder sb = new StringBuilder(valueOf.length() + 50);
                sb.append("Nonexistent connection status for service config: ");
                sb.append(valueOf);
                throw new IllegalStateException(sb.toString());
            } else if (zzr.zza.contains(serviceConnection)) {
                zza zza = zzr.zzg.zzd;
                zzr.zza.remove(serviceConnection);
                if (zzr.zza.isEmpty()) {
                    this.zzc.sendMessageDelayed(this.zzc.obtainMessage(0, connectionStatusConfig), this.zze);
                }
            } else {
                String valueOf2 = String.valueOf(connectionStatusConfig);
                StringBuilder sb2 = new StringBuilder(valueOf2.length() + 76);
                sb2.append("Trying to unbind a GmsServiceConnection  that was not bound before.  config=");
                sb2.append(valueOf2);
                throw new IllegalStateException(sb2.toString());
            }
        }
    }
}
