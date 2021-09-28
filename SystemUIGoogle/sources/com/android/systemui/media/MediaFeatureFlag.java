package com.android.systemui.media;

import android.content.Context;
import com.android.systemui.util.Utils;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaFeatureFlag.kt */
/* loaded from: classes.dex */
public final class MediaFeatureFlag {
    private final Context context;

    public MediaFeatureFlag(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        this.context = context;
    }

    public final boolean getEnabled() {
        return Utils.useQsMediaPlayer(this.context);
    }
}
