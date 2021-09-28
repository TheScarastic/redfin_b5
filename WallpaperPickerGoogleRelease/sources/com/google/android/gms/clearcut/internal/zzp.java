package com.google.android.gms.clearcut.internal;

import android.os.Parcel;
import android.os.RemoteException;
import com.google.android.gms.clearcut.CollectForDebugParcelable;
import com.google.android.gms.clearcut.LogEventParcelable;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.data.DataHolder;
import com.google.android.gms.internal.zzfa;
import com.google.android.gms.internal.zzfb;
/* loaded from: classes.dex */
public abstract class zzp extends zzfa implements zzo {
    public zzp() {
        attachInterface(this, "com.google.android.gms.clearcut.internal.IClearcutLoggerCallbacks");
    }

    @Override // android.os.Binder
    public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
        if (zza(i, parcel, parcel2, i2)) {
            return true;
        }
        switch (i) {
            case 1:
                ((zzh) this).zza.zza((zzg) ((Status) zzfb.zza(parcel, Status.CREATOR)));
                return true;
            case 2:
                Status status = (Status) zzfb.zza(parcel, Status.CREATOR);
                throw new UnsupportedOperationException();
            case 3:
                Status status2 = (Status) zzfb.zza(parcel, Status.CREATOR);
                parcel.readLong();
                throw new UnsupportedOperationException();
            case 4:
                Status status3 = (Status) zzfb.zza(parcel, Status.CREATOR);
                throw new UnsupportedOperationException();
            case 5:
                Status status4 = (Status) zzfb.zza(parcel, Status.CREATOR);
                parcel.readLong();
                throw new UnsupportedOperationException();
            case 6:
                Status status5 = (Status) zzfb.zza(parcel, Status.CREATOR);
                LogEventParcelable[] logEventParcelableArr = (LogEventParcelable[]) parcel.createTypedArray(LogEventParcelable.CREATOR);
                throw new UnsupportedOperationException();
            case 7:
                DataHolder dataHolder = (DataHolder) zzfb.zza(parcel, DataHolder.CREATOR);
                throw new UnsupportedOperationException();
            case 8:
                Status status6 = (Status) zzfb.zza(parcel, Status.CREATOR);
                CollectForDebugParcelable collectForDebugParcelable = (CollectForDebugParcelable) zzfb.zza(parcel, CollectForDebugParcelable.CREATOR);
                throw new UnsupportedOperationException();
            case 9:
                Status status7 = (Status) zzfb.zza(parcel, Status.CREATOR);
                CollectForDebugParcelable collectForDebugParcelable2 = (CollectForDebugParcelable) zzfb.zza(parcel, CollectForDebugParcelable.CREATOR);
                throw new UnsupportedOperationException();
            default:
                return false;
        }
    }
}
