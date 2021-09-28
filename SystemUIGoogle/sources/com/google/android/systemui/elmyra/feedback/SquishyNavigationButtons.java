package com.google.android.systemui.elmyra.feedback;

import android.content.Context;
import android.view.View;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes2.dex */
public class SquishyNavigationButtons extends NavigationBarEffect {
    private final KeyguardViewMediator mKeyguardViewMediator;
    private final SquishyViewController mViewController;

    public SquishyNavigationButtons(Context context, KeyguardViewMediator keyguardViewMediator, StatusBar statusBar, NavigationModeController navigationModeController) {
        super(statusBar, navigationModeController);
        this.mViewController = new SquishyViewController(context);
        this.mKeyguardViewMediator = keyguardViewMediator;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected List<FeedbackEffect> findFeedbackEffects(NavigationBarView navigationBarView) {
        this.mViewController.clearViews();
        ArrayList<View> views = navigationBarView.getBackButton().getViews();
        for (int i = 0; i < views.size(); i++) {
            this.mViewController.addLeftView(views.get(i));
        }
        ArrayList<View> views2 = navigationBarView.getRecentsButton().getViews();
        for (int i2 = 0; i2 < views2.size(); i2++) {
            this.mViewController.addRightView(views2.get(i2));
        }
        return Arrays.asList(this.mViewController);
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected boolean validateFeedbackEffects(List<FeedbackEffect> list) {
        boolean isAttachedToWindow = this.mViewController.isAttachedToWindow();
        if (!isAttachedToWindow) {
            this.mViewController.clearViews();
        }
        return isAttachedToWindow;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect) {
        return !this.mKeyguardViewMediator.isShowingAndNotOccluded();
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected void reset() {
        super.reset();
        this.mViewController.clearViews();
    }
}
