package com.google.android.gms.auth.api.signin;

import android.accounts.Account;
import android.os.Parcel;
import android.os.Parcelable;
import androidx.slice.view.R$id;
import com.google.android.gms.auth.api.signin.internal.zzo;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.internal.ReflectedParcelable;
import com.google.android.gms.internal.zzbkv;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
/* loaded from: classes.dex */
public class GoogleSignInOptions extends zzbkv implements Api.ApiOptions, ReflectedParcelable {
    public static final Parcelable.Creator<GoogleSignInOptions> CREATOR;
    public static final Scope zza;
    public static final Scope zzd;
    public final int zzf;
    public final ArrayList<Scope> zzg;
    public Account zzh;
    public boolean zzi;
    public final boolean zzj;
    public final boolean zzk;
    public String zzl;
    public String zzm;
    public ArrayList<zzo> zzn;
    public Map<Integer, zzo> zzo;
    public static final Scope zzc = new Scope(1, "openid");
    public static final Scope zze = new Scope(1, "https://www.googleapis.com/auth/games");

    static {
        Scope scope = new Scope(1, "profile");
        zza = scope;
        new Scope(1, "email");
        Scope scope2 = new Scope(1, "https://www.googleapis.com/auth/games_lite");
        zzd = scope2;
        HashSet hashSet = new HashSet();
        HashMap hashMap = new HashMap();
        hashSet.add(zzc);
        hashSet.add(scope);
        if (hashSet.contains(zze)) {
            Scope scope3 = zzd;
            if (hashSet.contains(scope3)) {
                hashSet.remove(scope3);
            }
        }
        new GoogleSignInOptions(3, new ArrayList(hashSet), null, false, false, false, null, null, hashMap);
        HashSet hashSet2 = new HashSet();
        HashMap hashMap2 = new HashMap();
        hashSet2.add(scope2);
        hashSet2.addAll(Arrays.asList(new Scope[0]));
        if (hashSet2.contains(zze)) {
            Scope scope4 = zzd;
            if (hashSet2.contains(scope4)) {
                hashSet2.remove(scope4);
            }
        }
        new GoogleSignInOptions(3, new ArrayList(hashSet2), null, false, false, false, null, null, hashMap2);
        CREATOR = new zze();
    }

    public GoogleSignInOptions(int i, ArrayList<Scope> arrayList, Account account, boolean z, boolean z2, boolean z3, String str, String str2, Map<Integer, zzo> map) {
        this.zzf = i;
        this.zzg = arrayList;
        this.zzh = account;
        this.zzi = z;
        this.zzj = z2;
        this.zzk = z3;
        this.zzl = str;
        this.zzm = str2;
        this.zzn = new ArrayList<>(map.values());
        this.zzo = map;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0043, code lost:
        if (r1.equals(r4.zzh) != false) goto L_0x0045;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x005e, code lost:
        if (r3.zzl.equals(r4.zzl) != false) goto L_0x0060;
     */
    @Override // java.lang.Object
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean equals(java.lang.Object r4) {
        /*
            r3 = this;
            r0 = 0
            if (r4 != 0) goto L_0x0004
            return r0
        L_0x0004:
            com.google.android.gms.auth.api.signin.GoogleSignInOptions r4 = (com.google.android.gms.auth.api.signin.GoogleSignInOptions) r4     // Catch: ClassCastException -> 0x0074
            java.util.ArrayList<com.google.android.gms.auth.api.signin.internal.zzo> r1 = r3.zzn     // Catch: ClassCastException -> 0x0074
            int r1 = r1.size()     // Catch: ClassCastException -> 0x0074
            if (r1 > 0) goto L_0x0074
            java.util.ArrayList<com.google.android.gms.auth.api.signin.internal.zzo> r1 = r4.zzn     // Catch: ClassCastException -> 0x0074
            int r1 = r1.size()     // Catch: ClassCastException -> 0x0074
            if (r1 <= 0) goto L_0x0017
            goto L_0x0074
        L_0x0017:
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzg     // Catch: ClassCastException -> 0x0074
            int r1 = r1.size()     // Catch: ClassCastException -> 0x0074
            java.util.ArrayList r2 = r4.zza()     // Catch: ClassCastException -> 0x0074
            int r2 = r2.size()     // Catch: ClassCastException -> 0x0074
            if (r1 != r2) goto L_0x0074
            java.util.ArrayList<com.google.android.gms.common.api.Scope> r1 = r3.zzg     // Catch: ClassCastException -> 0x0074
            java.util.ArrayList r2 = r4.zza()     // Catch: ClassCastException -> 0x0074
            boolean r1 = r1.containsAll(r2)     // Catch: ClassCastException -> 0x0074
            if (r1 != 0) goto L_0x0034
            goto L_0x0074
        L_0x0034:
            android.accounts.Account r1 = r3.zzh     // Catch: ClassCastException -> 0x0074
            if (r1 != 0) goto L_0x003d
            android.accounts.Account r1 = r4.zzh     // Catch: ClassCastException -> 0x0074
            if (r1 != 0) goto L_0x0074
            goto L_0x0045
        L_0x003d:
            android.accounts.Account r2 = r4.zzh     // Catch: ClassCastException -> 0x0074
            boolean r1 = r1.equals(r2)     // Catch: ClassCastException -> 0x0074
            if (r1 == 0) goto L_0x0074
        L_0x0045:
            java.lang.String r1 = r3.zzl     // Catch: ClassCastException -> 0x0074
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: ClassCastException -> 0x0074
            if (r1 == 0) goto L_0x0056
            java.lang.String r1 = r4.zzl     // Catch: ClassCastException -> 0x0074
            boolean r1 = android.text.TextUtils.isEmpty(r1)     // Catch: ClassCastException -> 0x0074
            if (r1 == 0) goto L_0x0074
            goto L_0x0060
        L_0x0056:
            java.lang.String r1 = r3.zzl     // Catch: ClassCastException -> 0x0074
            java.lang.String r2 = r4.zzl     // Catch: ClassCastException -> 0x0074
            boolean r1 = r1.equals(r2)     // Catch: ClassCastException -> 0x0074
            if (r1 == 0) goto L_0x0074
        L_0x0060:
            boolean r1 = r3.zzk     // Catch: ClassCastException -> 0x0074
            boolean r2 = r4.zzk     // Catch: ClassCastException -> 0x0074
            if (r1 != r2) goto L_0x0074
            boolean r1 = r3.zzi     // Catch: ClassCastException -> 0x0074
            boolean r2 = r4.zzi     // Catch: ClassCastException -> 0x0074
            if (r1 != r2) goto L_0x0074
            boolean r3 = r3.zzj     // Catch: ClassCastException -> 0x0074
            boolean r4 = r4.zzj     // Catch: ClassCastException -> 0x0074
            if (r3 != r4) goto L_0x0074
            r3 = 1
            return r3
        L_0x0074:
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.gms.auth.api.signin.GoogleSignInOptions.equals(java.lang.Object):boolean");
    }

    @Override // java.lang.Object
    public int hashCode() {
        int i;
        ArrayList arrayList = new ArrayList();
        ArrayList<Scope> arrayList2 = this.zzg;
        int size = arrayList2.size();
        int i2 = 0;
        int i3 = 0;
        while (i3 < size) {
            Scope scope = arrayList2.get(i3);
            i3++;
            arrayList.add(scope.zzb);
        }
        Collections.sort(arrayList);
        int hashCode = (1 * 31) + arrayList.hashCode();
        Account account = this.zzh;
        int i4 = hashCode * 31;
        if (account == null) {
            i = 0;
        } else {
            i = account.hashCode();
        }
        int i5 = i4 + i;
        String str = this.zzl;
        int i6 = i5 * 31;
        if (str != null) {
            i2 = str.hashCode();
        }
        return ((((((i6 + i2) * 31) + (this.zzk ? 1 : 0)) * 31) + (this.zzi ? 1 : 0)) * 31) + (this.zzj ? 1 : 0);
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        int zzb = R$id.zzb(parcel, 20293);
        int i2 = this.zzf;
        R$id.zzb(parcel, 1, 4);
        parcel.writeInt(i2);
        R$id.zzc(parcel, 2, zza(), false);
        R$id.zza(parcel, 3, this.zzh, i, false);
        boolean z = this.zzi;
        R$id.zzb(parcel, 4, 4);
        parcel.writeInt(z ? 1 : 0);
        boolean z2 = this.zzj;
        R$id.zzb(parcel, 5, 4);
        parcel.writeInt(z2 ? 1 : 0);
        boolean z3 = this.zzk;
        R$id.zzb(parcel, 6, 4);
        parcel.writeInt(z3 ? 1 : 0);
        R$id.zza(parcel, 7, this.zzl, false);
        R$id.zza(parcel, 8, this.zzm, false);
        R$id.zzc(parcel, 9, this.zzn, false);
        R$id.zzc(parcel, zzb);
    }

    public final ArrayList<Scope> zza() {
        return new ArrayList<>(this.zzg);
    }
}
