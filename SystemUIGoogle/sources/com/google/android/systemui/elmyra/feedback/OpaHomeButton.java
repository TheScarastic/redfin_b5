package com.google.android.systemui.elmyra.feedback;

import android.view.View;
import android.view.ViewParent;
import com.android.systemui.keyguard.KeyguardViewMediator;
import com.android.systemui.navigationbar.NavigationBarView;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.statusbar.phone.StatusBar;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class OpaHomeButton extends NavigationBarEffect {
    private final KeyguardViewMediator mKeyguardViewMediator;
    private StatusBar mStatusBar;

    public OpaHomeButton(KeyguardViewMediator keyguardViewMediator, StatusBar statusBar, NavigationModeController navigationModeController) {
        super(statusBar, navigationModeController);
        this.mStatusBar = statusBar;
        this.mKeyguardViewMediator = keyguardViewMediator;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected List<FeedbackEffect> findFeedbackEffects(NavigationBarView navigationBarView) {
        ArrayList arrayList = new ArrayList();
        ArrayList<View> views = navigationBarView.getHomeButton().getViews();
        for (int i = 0; i < views.size(); i++) {
            View view = views.get(i);
            if (view instanceof FeedbackEffect) {
                arrayList.add((FeedbackEffect) view);
            }
        }
        return arrayList;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected boolean validateFeedbackEffects(List<FeedbackEffect> list) {
        for (int i = 0; i < list.size(); i++) {
            if (!((View) list.get(i)).isAttachedToWindow()) {
                return false;
            }
        }
        return true;
    }

    @Override // com.google.android.systemui.elmyra.feedback.NavigationBarEffect
    protected boolean isActiveFeedbackEffect(FeedbackEffect feedbackEffect) {
        if (this.mKeyguardViewMediator.isShowingAndNotOccluded()) {
            return false;
        }
        View currentView = this.mStatusBar.getNavigationBarView().getCurrentView();
        for (ViewParent parent = ((View) feedbackEffect).getParent(); parent != null; parent = parent.getParent()) {
            if (parent.equals(currentView)) {
                return true;
            }
        }
        return false;
    }
}
