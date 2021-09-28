package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.internal.zzez;
import com.google.android.gms.internal.zzfb;
/* loaded from: classes.dex */
public final class zzw extends zzez implements IAccountAccessor {
    public zzw(IBinder iBinder) {
        super(iBinder, "com.google.android.gms.common.internal.IAccountAccessor");
    }

    @Override // com.google.android.gms.common.internal.IAccountAccessor
    public final Account getAccount() throws RemoteException {
        Parcel zza = zza(2, a_());
        Account account = (Account) zzfb.zza(zza, Account.CREATOR);
        zza.recycle();
        return account;
    }
}
