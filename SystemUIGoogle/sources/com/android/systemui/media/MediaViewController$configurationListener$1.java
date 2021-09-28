package com.android.systemui.media;

import android.content.res.Configuration;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.animation.TransitionLayout;
/* compiled from: MediaViewController.kt */
/* loaded from: classes.dex */
public final class MediaViewController$configurationListener$1 implements ConfigurationController.ConfigurationListener {
    final /* synthetic */ MediaViewController this$0;

    /* access modifiers changed from: package-private */
    public MediaViewController$configurationListener$1(MediaViewController mediaViewController) {
        this.this$0 = mediaViewController;
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onConfigChanged(Configuration configuration) {
        if (configuration != null) {
            MediaViewController mediaViewController = this.this$0;
            TransitionLayout transitionLayout = mediaViewController.transitionLayout;
            Integer valueOf = transitionLayout == null ? null : Integer.valueOf(transitionLayout.getRawLayoutDirection());
            int layoutDirection = configuration.getLayoutDirection();
            if (valueOf == null || valueOf.intValue() != layoutDirection) {
                TransitionLayout transitionLayout2 = mediaViewController.transitionLayout;
                if (transitionLayout2 != null) {
                    transitionLayout2.setLayoutDirection(configuration.getLayoutDirection());
                }
                mediaViewController.refreshState();
            }
        }
    }
}
