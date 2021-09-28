package com.google.android.gms.common.api;

import android.text.TextUtils;
import androidx.collection.ArrayMap;
import androidx.collection.IndexBasedArrayIterator;
import com.adobe.xmp.XMPPathFactory$$ExternalSyntheticOutline0;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.internal.zzi;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class AvailabilityException extends Exception {
    private final ArrayMap<zzi<?>, ConnectionResult> zza;

    public AvailabilityException(ArrayMap<zzi<?>, ConnectionResult> arrayMap) {
        this.zza = arrayMap;
    }

    @Override // java.lang.Throwable
    public String getMessage() {
        ArrayList arrayList = new ArrayList();
        Iterator it = ((ArrayMap.KeySet) this.zza.keySet()).iterator();
        boolean z = true;
        while (true) {
            IndexBasedArrayIterator indexBasedArrayIterator = (IndexBasedArrayIterator) it;
            if (!indexBasedArrayIterator.hasNext()) {
                break;
            }
            zzi zzi = (zzi) indexBasedArrayIterator.next();
            ConnectionResult connectionResult = this.zza.get(zzi);
            if (connectionResult.isSuccess()) {
                z = false;
            }
            String str = zzi.zzc.zze;
            String valueOf = String.valueOf(connectionResult);
            StringBuilder sb = new StringBuilder(valueOf.length() + XMPPathFactory$$ExternalSyntheticOutline0.m(str, 2));
            sb.append(str);
            sb.append(": ");
            sb.append(valueOf);
            arrayList.add(sb.toString());
        }
        StringBuilder sb2 = new StringBuilder();
        if (z) {
            sb2.append("None of the queried APIs are available. ");
        } else {
            sb2.append("Some of the queried APIs are unavailable. ");
        }
        sb2.append(TextUtils.join("; ", arrayList));
        return sb2.toString();
    }
}
