package com.android.systemui.controls.management;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import com.android.systemui.R$string;
import com.android.systemui.controls.TooltipManager;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsFavoritingActivity.kt */
/* loaded from: classes.dex */
public final class ControlsFavoritingActivity$loadControls$1$1$2$1$1 extends AnimatorListenerAdapter {
    final /* synthetic */ ControlsFavoritingActivity this$0;

    /* access modifiers changed from: package-private */
    public ControlsFavoritingActivity$loadControls$1$1$2$1$1(ControlsFavoritingActivity controlsFavoritingActivity) {
        this.this$0 = controlsFavoritingActivity;
    }

    @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
    public void onAnimationEnd(Animator animator) {
        ManagementPageIndicator managementPageIndicator = this.this$0.pageIndicator;
        if (managementPageIndicator == null) {
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            throw null;
        } else if (managementPageIndicator.getVisibility() == 0 && this.this$0.mTooltipManager != null) {
            int[] iArr = new int[2];
            ManagementPageIndicator managementPageIndicator2 = this.this$0.pageIndicator;
            if (managementPageIndicator2 != null) {
                managementPageIndicator2.getLocationOnScreen(iArr);
                int i = iArr[0];
                ManagementPageIndicator managementPageIndicator3 = this.this$0.pageIndicator;
                if (managementPageIndicator3 != null) {
                    int width = i + (managementPageIndicator3.getWidth() / 2);
                    int i2 = iArr[1];
                    ManagementPageIndicator managementPageIndicator4 = this.this$0.pageIndicator;
                    if (managementPageIndicator4 != null) {
                        int height = i2 + managementPageIndicator4.getHeight();
                        TooltipManager tooltipManager = this.this$0.mTooltipManager;
                        if (tooltipManager != null) {
                            tooltipManager.show(R$string.controls_structure_tooltip, width, height);
                            return;
                        }
                        return;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("pageIndicator");
            throw null;
        }
    }
}
