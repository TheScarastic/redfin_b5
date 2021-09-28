package com.google.android.gms.common.internal;

import android.accounts.Account;
import android.os.IInterface;
import android.os.RemoteException;
import com.google.android.gms.internal.zzfa;
/* loaded from: classes.dex */
public interface IAccountAccessor extends IInterface {

    /* loaded from: classes.dex */
    public static abstract class zza extends zzfa implements IAccountAccessor {
    }

    Account getAccount() throws RemoteException;
}
