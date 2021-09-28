package com.google.android.systemui.smartspace;

import android.media.MediaMetadata;
import com.android.systemui.statusbar.NotificationMediaManager;
/* compiled from: KeyguardMediaViewController.kt */
/* loaded from: classes2.dex */
public final class KeyguardMediaViewController$mediaListener$1 implements NotificationMediaManager.MediaListener {
    final /* synthetic */ KeyguardMediaViewController this$0;

    /* access modifiers changed from: package-private */
    public KeyguardMediaViewController$mediaListener$1(KeyguardMediaViewController keyguardMediaViewController) {
        this.this$0 = keyguardMediaViewController;
    }

    @Override // com.android.systemui.statusbar.NotificationMediaManager.MediaListener
    public void onPrimaryMetadataOrStateChanged(MediaMetadata mediaMetadata, int i) {
        this.this$0.getUiExecutor().execute(new Runnable(this.this$0, mediaMetadata, i) { // from class: com.google.android.systemui.smartspace.KeyguardMediaViewController$mediaListener$1$onPrimaryMetadataOrStateChanged$1
            final /* synthetic */ MediaMetadata $metadata;
            final /* synthetic */ int $state;
            final /* synthetic */ KeyguardMediaViewController this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$metadata = r2;
                this.$state = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.updateMediaInfo(this.$metadata, this.$state);
            }
        });
    }
}
