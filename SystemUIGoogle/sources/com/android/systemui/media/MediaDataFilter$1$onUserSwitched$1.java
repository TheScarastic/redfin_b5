package com.android.systemui.media;
/* compiled from: MediaDataFilter.kt */
/* loaded from: classes.dex */
final class MediaDataFilter$1$onUserSwitched$1 implements Runnable {
    final /* synthetic */ int $newUserId;
    final /* synthetic */ MediaDataFilter this$0;

    /* access modifiers changed from: package-private */
    public MediaDataFilter$1$onUserSwitched$1(MediaDataFilter mediaDataFilter, int i) {
        this.this$0 = mediaDataFilter;
        this.$newUserId = i;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.this$0.handleUserSwitched$frameworks__base__packages__SystemUI__android_common__SystemUI_core(this.$newUserId);
    }
}
