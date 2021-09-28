package com.google.android.systemui.assist.uihints;

import android.util.Log;
import android.view.View;
import com.android.systemui.Dependency;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.statusbar.phone.KeyguardBottomAreaView;
import com.android.systemui.statusbar.phone.StatusBar;
import dagger.Lazy;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class OverlappedElementController {
    private float mAlpha = 1.0f;
    private final Lazy<StatusBar> mStatusBarLazy;

    /* access modifiers changed from: package-private */
    public OverlappedElementController(Lazy<StatusBar> lazy) {
        this.mStatusBarLazy = lazy;
    }

    public void setAlpha(float f) {
        float f2 = this.mAlpha;
        if (f2 != f) {
            if (f2 == 1.0f && f < 1.0f) {
                Log.v("OverlappedElementController", "Overlapped elements becoming transparent.");
            } else if (f2 < 1.0f && f == 1.0f) {
                Log.v("OverlappedElementController", "Overlapped elements becoming opaque.");
            }
            this.mAlpha = f;
            tellOverlappedElementsSetAlpha(f);
        }
    }

    private void tellOverlappedElementsSetAlpha(float f) {
        StatusBar statusBar = this.mStatusBarLazy.get();
        ((OverviewProxyService) Dependency.get(OverviewProxyService.class)).notifyAssistantVisibilityChanged(1.0f - f);
        View ambientIndicationContainer = statusBar.getAmbientIndicationContainer();
        if (ambientIndicationContainer != null) {
            ambientIndicationContainer.setAlpha(f);
        }
        KeyguardBottomAreaView keyguardBottomAreaView = statusBar.getKeyguardBottomAreaView();
        if (keyguardBottomAreaView != null) {
            keyguardBottomAreaView.setAffordanceAlpha(f);
        }
    }
}
