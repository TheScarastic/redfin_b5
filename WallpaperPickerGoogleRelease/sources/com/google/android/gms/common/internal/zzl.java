package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IInterface;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class zzl<T extends IInterface> extends BaseGmsClient<T> implements Api.Client {
    public final Set<Scope> zzd;
    public final Account zze;

    /* JADX WARNING: Illegal instructions before constructor call */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public zzl(android.content.Context r12, android.os.Looper r13, int r14, com.google.android.gms.common.internal.ClientSettings r15, com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks r16, com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener r17) {
        /*
            r11 = this;
            r0 = r11
            r10 = r15
            r1 = r16
            r2 = r17
            java.lang.Object r3 = com.google.android.gms.common.internal.GmsClientSupervisor.zza
            monitor-enter(r3)
            com.google.android.gms.common.internal.GmsClientSupervisor r4 = com.google.android.gms.common.internal.GmsClientSupervisor.zzb     // Catch: all -> 0x0064
            if (r4 != 0) goto L_0x0018
            com.google.android.gms.common.internal.zzq r4 = new com.google.android.gms.common.internal.zzq     // Catch: all -> 0x0064
            android.content.Context r5 = r12.getApplicationContext()     // Catch: all -> 0x0064
            r4.<init>(r5)     // Catch: all -> 0x0064
            com.google.android.gms.common.internal.GmsClientSupervisor.zzb = r4     // Catch: all -> 0x0064
        L_0x0018:
            monitor-exit(r3)     // Catch: all -> 0x0064
            com.google.android.gms.common.internal.GmsClientSupervisor r4 = com.google.android.gms.common.internal.GmsClientSupervisor.zzb
            java.lang.Object r3 = com.google.android.gms.common.GoogleApiAvailability.zza
            com.google.android.gms.common.GoogleApiAvailability r5 = com.google.android.gms.common.GoogleApiAvailability.zzb
            java.lang.String r3 = "null reference"
            java.util.Objects.requireNonNull(r1, r3)
            java.lang.String r3 = "null reference"
            java.util.Objects.requireNonNull(r2, r3)
            com.google.android.gms.common.internal.zzm r7 = new com.google.android.gms.common.internal.zzm
            r7.<init>(r1)
            com.google.android.gms.common.internal.zzn r8 = new com.google.android.gms.common.internal.zzn
            r8.<init>(r2)
            java.lang.String r9 = r10.zzh
            r1 = r11
            r2 = r12
            r3 = r13
            r6 = r14
            r1.<init>(r2, r3, r4, r5, r6, r7, r8, r9)
            android.accounts.Account r1 = r10.zza
            r0.zze = r1
            java.util.Set<com.google.android.gms.common.api.Scope> r1 = r10.zzc
            java.util.Iterator r2 = r1.iterator()
        L_0x0046:
            boolean r3 = r2.hasNext()
            if (r3 == 0) goto L_0x0061
            java.lang.Object r3 = r2.next()
            com.google.android.gms.common.api.Scope r3 = (com.google.android.gms.common.api.Scope) r3
            boolean r3 = r1.contains(r3)
            if (r3 == 0) goto L_0x0059
            goto L_0x0046
        L_0x0059:
            java.lang.IllegalStateException r0 = new java.lang.IllegalStateException
            java.lang.String r1 = "Expanding scopes is not permitted, use implied scopes instead"
            r0.<init>(r1)
            throw r0
        L_0x0061:
            r0.zzd = r1
            return
        L_0x0064:
            r0 = move-exception
            monitor-exit(r3)     // Catch: all -> 0x0064
            throw r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.common.internal.zzl.<init>(android.content.Context, android.os.Looper, int, com.google.android.gms.common.internal.ClientSettings, com.google.android.gms.common.api.GoogleApiClient$ConnectionCallbacks, com.google.android.gms.common.api.GoogleApiClient$OnConnectionFailedListener):void");
    }
}
