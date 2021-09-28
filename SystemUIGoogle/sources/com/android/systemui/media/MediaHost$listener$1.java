package com.android.systemui.media;

import com.android.systemui.media.MediaDataManager;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: MediaHost.kt */
/* loaded from: classes.dex */
public final class MediaHost$listener$1 implements MediaDataManager.Listener {
    final /* synthetic */ MediaHost this$0;

    /* access modifiers changed from: package-private */
    public MediaHost$listener$1(MediaHost mediaHost) {
        this.this$0 = mediaHost;
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataLoaded(String str, String str2, MediaData mediaData, boolean z, boolean z2) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(mediaData, "data");
        if (z) {
            this.this$0.updateViewVisibility();
        }
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataLoaded(String str, SmartspaceMediaData smartspaceMediaData, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        Intrinsics.checkNotNullParameter(smartspaceMediaData, "data");
        this.this$0.updateViewVisibility();
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onMediaDataRemoved(String str) {
        Intrinsics.checkNotNullParameter(str, "key");
        this.this$0.updateViewVisibility();
    }

    @Override // com.android.systemui.media.MediaDataManager.Listener
    public void onSmartspaceMediaDataRemoved(String str, boolean z) {
        Intrinsics.checkNotNullParameter(str, "key");
        if (z) {
            this.this$0.updateViewVisibility();
        }
    }
}
