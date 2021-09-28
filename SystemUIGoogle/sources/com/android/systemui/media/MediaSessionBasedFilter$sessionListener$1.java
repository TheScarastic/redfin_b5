package com.android.systemui.media;

import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import java.util.List;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaSessionBasedFilter.kt */
/* loaded from: classes.dex */
public final class MediaSessionBasedFilter$sessionListener$1 implements MediaSessionManager.OnActiveSessionsChangedListener {
    final /* synthetic */ MediaSessionBasedFilter this$0;

    /* access modifiers changed from: package-private */
    public MediaSessionBasedFilter$sessionListener$1(MediaSessionBasedFilter mediaSessionBasedFilter) {
        this.this$0 = mediaSessionBasedFilter;
    }

    @Override // android.media.session.MediaSessionManager.OnActiveSessionsChangedListener
    public void onActiveSessionsChanged(List<MediaController> list) {
        Intrinsics.checkNotNullParameter(list, "controllers");
        this.this$0.handleControllersChanged(list);
    }
}
