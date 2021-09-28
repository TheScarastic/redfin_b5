package com.android.systemui.media;

import android.os.SystemProperties;
import com.android.internal.annotations.VisibleForTesting;
import java.util.concurrent.TimeUnit;
/* compiled from: MediaDataFilter.kt */
/* loaded from: classes.dex */
public final class MediaDataFilterKt {
    private static final long SMARTSPACE_MAX_AGE = SystemProperties.getLong("debug.sysui.smartspace_max_age", TimeUnit.MINUTES.toMillis(30));

    @VisibleForTesting
    public static /* synthetic */ void getSMARTSPACE_MAX_AGE$annotations() {
    }

    public static final long getSMARTSPACE_MAX_AGE() {
        return SMARTSPACE_MAX_AGE;
    }
}
