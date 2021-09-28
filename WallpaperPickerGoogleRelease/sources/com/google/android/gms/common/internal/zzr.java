package com.google.android.gms.common.internal;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.google.android.gms.common.internal.GmsClientSupervisor;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public final class zzr implements ServiceConnection {
    public final Set<ServiceConnection> zza = new HashSet();
    public int zzb = 2;
    public boolean zzc;
    public IBinder zzd;
    public final GmsClientSupervisor.ConnectionStatusConfig zze;
    public ComponentName zzf;
    public final /* synthetic */ zzq zzg;

    public zzr(zzq zzq, GmsClientSupervisor.ConnectionStatusConfig connectionStatusConfig) {
        this.zzg = zzq;
        this.zze = connectionStatusConfig;
    }

    @Override // android.content.ServiceConnection
    public final void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        synchronized (this.zzg.zza) {
            this.zzg.zzc.removeMessages(1, this.zze);
            this.zzd = iBinder;
            this.zzf = componentName;
            for (ServiceConnection serviceConnection : this.zza) {
                serviceConnection.onServiceConnected(componentName, iBinder);
            }
            this.zzb = 1;
        }
    }

    @Override // android.content.ServiceConnection
    public final void onServiceDisconnected(ComponentName componentName) {
        synchronized (this.zzg.zza) {
            this.zzg.zzc.removeMessages(1, this.zze);
            this.zzd = null;
            this.zzf = componentName;
            for (ServiceConnection serviceConnection : this.zza) {
                serviceConnection.onServiceDisconnected(componentName);
            }
            this.zzb = 2;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:11:0x0042  */
    /* JADX WARNING: Removed duplicated region for block: B:12:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x0052  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0066  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void zza(java.lang.String r7) {
        /*
            r6 = this;
            r7 = 3
            r6.zzb = r7
            com.google.android.gms.common.internal.zzq r7 = r6.zzg
            com.google.android.gms.common.stats.zza r0 = r7.zzd
            android.content.Context r7 = r7.zzb
            com.google.android.gms.common.internal.GmsClientSupervisor$ConnectionStatusConfig r1 = r6.zze
            android.content.Intent r1 = r1.getStartServiceIntent()
            com.google.android.gms.common.internal.GmsClientSupervisor$ConnectionStatusConfig r2 = r6.zze
            int r2 = r2.zzd
            java.util.Objects.requireNonNull(r0)
            android.content.ComponentName r0 = r1.getComponent()
            r3 = 0
            r4 = 1
            if (r0 != 0) goto L_0x001f
            goto L_0x003f
        L_0x001f:
            java.lang.String r0 = r0.getPackageName()
            java.lang.String r5 = "com.google.android.gms"
            r5.equals(r0)
            com.google.android.gms.internal.zzbmj r5 = com.google.android.gms.internal.zzbmk.zza(r7)     // Catch: NameNotFoundException -> 0x003f
            android.content.Context r5 = r5.zza     // Catch: NameNotFoundException -> 0x003f
            android.content.pm.PackageManager r5 = r5.getPackageManager()     // Catch: NameNotFoundException -> 0x003f
            android.content.pm.ApplicationInfo r0 = r5.getApplicationInfo(r0, r3)     // Catch: NameNotFoundException -> 0x003f
            int r0 = r0.flags     // Catch: NameNotFoundException -> 0x003f
            r5 = 2097152(0x200000, float:2.938736E-39)
            r0 = r0 & r5
            if (r0 == 0) goto L_0x003f
            r0 = r4
            goto L_0x0040
        L_0x003f:
            r0 = r3
        L_0x0040:
            if (r0 == 0) goto L_0x004a
            java.lang.String r7 = "ConnectionTracker"
            java.lang.String r0 = "Attempted to bind to a service in a STOPPED package."
            android.util.Log.w(r7, r0)
            goto L_0x004e
        L_0x004a:
            boolean r3 = r7.bindService(r1, r6, r2)
        L_0x004e:
            r6.zzc = r3
            if (r3 == 0) goto L_0x0066
            com.google.android.gms.common.internal.zzq r7 = r6.zzg
            android.os.Handler r7 = r7.zzc
            com.google.android.gms.common.internal.GmsClientSupervisor$ConnectionStatusConfig r0 = r6.zze
            android.os.Message r7 = r7.obtainMessage(r4, r0)
            com.google.android.gms.common.internal.zzq r6 = r6.zzg
            android.os.Handler r0 = r6.zzc
            long r1 = r6.zzf
            r0.sendMessageDelayed(r7, r1)
            return
        L_0x0066:
            r7 = 2
            r6.zzb = r7
            com.google.android.gms.common.internal.zzq r7 = r6.zzg     // Catch: IllegalArgumentException -> 0x0072
            com.google.android.gms.common.stats.zza r0 = r7.zzd     // Catch: IllegalArgumentException -> 0x0072
            android.content.Context r7 = r7.zzb     // Catch: IllegalArgumentException -> 0x0072
            r7.unbindService(r6)     // Catch: IllegalArgumentException -> 0x0072
        L_0x0072:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzr.zza(java.lang.String):void");
    }
}
