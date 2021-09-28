package com.google.android.gms.common.internal;

import android.content.Intent;
import android.content.ServiceConnection;
import androidx.core.R$id;
import androidx.preference.R$layout;
import java.util.Arrays;
import java.util.Objects;
/* loaded from: classes.dex */
public abstract class GmsClientSupervisor {
    public static final Object zza = new Object();
    public static GmsClientSupervisor zzb;

    /* loaded from: classes.dex */
    public static final class ConnectionStatusConfig {
        public final String zza;
        public final String zzb;
        public final int zzd;

        public ConnectionStatusConfig(String str, String str2, int i) {
            R$layout.zza(str);
            this.zza = str;
            R$layout.zza(str2);
            this.zzb = str2;
            this.zzd = i;
        }

        public final boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }
            if (!(obj instanceof ConnectionStatusConfig)) {
                return false;
            }
            ConnectionStatusConfig connectionStatusConfig = (ConnectionStatusConfig) obj;
            return R$id.zza(this.zza, connectionStatusConfig.zza) && R$id.zza(this.zzb, connectionStatusConfig.zzb) && R$id.zza(null, null) && this.zzd == connectionStatusConfig.zzd;
        }

        public final Intent getStartServiceIntent() {
            if (this.zza != null) {
                return new Intent(this.zza).setPackage(this.zzb);
            }
            return new Intent().setComponent(null);
        }

        public final int hashCode() {
            return Arrays.hashCode(new Object[]{this.zza, this.zzb, null, Integer.valueOf(this.zzd)});
        }

        public final String toString() {
            String str = this.zza;
            Objects.requireNonNull(str);
            return str;
        }
    }

    public abstract boolean bindService(ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str);

    public abstract void unbindService(ConnectionStatusConfig connectionStatusConfig, ServiceConnection serviceConnection, String str);
}
