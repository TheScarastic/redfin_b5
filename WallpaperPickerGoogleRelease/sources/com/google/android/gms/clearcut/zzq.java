package com.google.android.gms.clearcut;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.android.gms.clearcut.internal.PlayLoggerContext;
import com.google.android.gms.internal.zzbkw;
import com.google.android.gms.phenotype.ExperimentTokens;
/* loaded from: classes.dex */
public final class zzq implements Parcelable.Creator<LogEventParcelable> {
    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ LogEventParcelable createFromParcel(Parcel parcel) {
        int zza = zzbkw.zza(parcel);
        PlayLoggerContext playLoggerContext = null;
        boolean z = true;
        byte[] bArr = null;
        int[] iArr = null;
        String[] strArr = null;
        int[] iArr2 = null;
        byte[][] bArr2 = null;
        ExperimentTokens[] experimentTokensArr = null;
        while (parcel.dataPosition() < zza) {
            int readInt = parcel.readInt();
            switch (65535 & readInt) {
                case 2:
                    playLoggerContext = (PlayLoggerContext) zzbkw.zza(parcel, readInt, PlayLoggerContext.CREATOR);
                    break;
                case 3:
                    bArr = zzbkw.zzt(parcel, readInt);
                    break;
                case 4:
                    iArr = zzbkw.zzw(parcel, readInt);
                    break;
                case 5:
                    strArr = zzbkw.zzaa(parcel, readInt);
                    break;
                case 6:
                    iArr2 = zzbkw.zzw(parcel, readInt);
                    break;
                case 7:
                    bArr2 = zzbkw.zzu(parcel, readInt);
                    break;
                case 8:
                    z = zzbkw.zzc(parcel, readInt);
                    break;
                case 9:
                    experimentTokensArr = (ExperimentTokens[]) zzbkw.zzb(parcel, readInt, ExperimentTokens.CREATOR);
                    break;
                default:
                    zzbkw.zzb(parcel, readInt);
                    break;
            }
        }
        zzbkw.zzae(parcel, zza);
        return new LogEventParcelable(playLoggerContext, bArr, iArr, strArr, iArr2, bArr2, z, experimentTokensArr);
    }

    /* Return type fixed from 'java.lang.Object[]' to match base method */
    @Override // android.os.Parcelable.Creator
    public final /* synthetic */ LogEventParcelable[] newArray(int i) {
        return new LogEventParcelable[i];
    }
}
