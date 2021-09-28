package com.google.android.systemui.elmyra.gates;

import android.content.Context;
import com.android.systemui.Dependency;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.shared.system.QuickStepContract;
/* loaded from: classes2.dex */
public class NonGesturalNavigation extends Gate {
    private boolean mCurrentModeIsGestural;
    private final NavigationModeController.ModeChangedListener mModeListener = new NavigationModeController.ModeChangedListener() { // from class: com.google.android.systemui.elmyra.gates.NonGesturalNavigation.1
        @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
        public void onNavigationModeChanged(int i) {
            NonGesturalNavigation.this.mCurrentModeIsGestural = QuickStepContract.isGesturalMode(i);
            NonGesturalNavigation.this.notifyListener();
        }
    };
    private final NavigationModeController mModeController = (NavigationModeController) Dependency.get(NavigationModeController.class);

    public NonGesturalNavigation(Context context) {
        super(context);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onActivate() {
        this.mCurrentModeIsGestural = QuickStepContract.isGesturalMode(this.mModeController.addListener(this.mModeListener));
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected void onDeactivate() {
        this.mModeController.removeListener(this.mModeListener);
    }

    @Override // com.google.android.systemui.elmyra.gates.Gate
    protected boolean isBlocked() {
        return !isNavigationGestural();
    }

    public boolean isNavigationGestural() {
        return this.mCurrentModeIsGestural;
    }
}
