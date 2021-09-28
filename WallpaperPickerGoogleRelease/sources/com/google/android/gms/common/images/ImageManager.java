package com.google.android.gms.common.images;

import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.os.ResultReceiver;
import com.google.android.gms.common.annotation.KeepName;
import java.util.HashSet;
import java.util.Objects;
/* loaded from: classes.dex */
public final class ImageManager {
    public static final Object zza = new Object();
    public static HashSet<Uri> zzb = new HashSet<>();

    @KeepName
    /* loaded from: classes.dex */
    public final class ImageReceiver extends ResultReceiver {
        @Override // android.os.ResultReceiver
        public final void onReceiveResult(int i, Bundle bundle) {
            ParcelFileDescriptor parcelFileDescriptor = (ParcelFileDescriptor) bundle.getParcelable("com.google.android.gms.extra.fileDescriptor");
            Object obj = ImageManager.zza;
            Objects.requireNonNull(null);
            throw null;
        }
    }
}
