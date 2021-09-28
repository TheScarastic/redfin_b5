package com.google.android.gms.clearcut.internal;

import android.content.Context;
import com.google.android.gms.clearcut.ClearcutLogger;
import com.google.android.gms.clearcut.ClearcutLoggerApi;
import com.google.android.gms.common.api.GoogleApi;
import com.google.android.gms.common.api.internal.zzh;
/* loaded from: classes.dex */
public final class zzb extends GoogleApi<?> implements ClearcutLoggerApi {
    public zzb(Context context) {
        super(context, ClearcutLogger.API, null, new zzh());
    }
}
