package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.view.View;
import androidx.collection.ArraySet;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.signin.SignInOptions;
import java.util.Collections;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
/* loaded from: classes.dex */
public final class ClientSettings {
    public final Account zza;
    public final Set<Scope> zzb;
    public final Set<Scope> zzc;
    public final Map<Api<?>, OptionalApiSettings> zzd;
    public final String zzg;
    public final String zzh;
    public final SignInOptions zzi;
    public Integer zzj;

    /* loaded from: classes.dex */
    public static final class OptionalApiSettings {
    }

    /* loaded from: classes.dex */
    public static final class zza {
        public Account zza;
        public ArraySet<Scope> zzb;
        public String zzd;
        public String zze;

        public final ClientSettings zza() {
            return new ClientSettings(this.zza, this.zzb, null, 0, null, this.zzd, this.zze, SignInOptions.DEFAULT);
        }
    }

    public ClientSettings(Account account, Set<Scope> set, Map<Api<?>, OptionalApiSettings> map, int i, View view, String str, String str2, SignInOptions signInOptions) {
        this.zza = account;
        Set<Scope> unmodifiableSet = set == null ? Collections.EMPTY_SET : Collections.unmodifiableSet(set);
        this.zzb = unmodifiableSet;
        map = map == null ? Collections.EMPTY_MAP : map;
        this.zzd = map;
        this.zzg = str;
        this.zzh = str2;
        this.zzi = signInOptions;
        HashSet hashSet = new HashSet(unmodifiableSet);
        for (OptionalApiSettings optionalApiSettings : map.values()) {
            Objects.requireNonNull(optionalApiSettings);
            hashSet.addAll(null);
        }
        this.zzc = Collections.unmodifiableSet(hashSet);
    }
}
