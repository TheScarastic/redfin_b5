package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Binder;
import android.os.RemoteException;
import android.view.IWindowManager;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.SuperStatusBarViewFactory;
/* loaded from: classes.dex */
public class StatusBarWindowController {
    private int mBarHeight;
    private final StatusBarContentInsetsProvider mContentInsetsProvider;
    private final Context mContext;
    private final IWindowManager mIWindowManager;
    private ViewGroup mLaunchAnimationContainer;
    private WindowManager.LayoutParams mLp;
    private final Resources mResources;
    private ViewGroup mStatusBarView;
    private final SuperStatusBarViewFactory mSuperStatusBarViewFactory;
    private final WindowManager mWindowManager;
    private final State mCurrentState = new State();
    private final WindowManager.LayoutParams mLpChanged = new WindowManager.LayoutParams();

    public StatusBarWindowController(Context context, WindowManager windowManager, IWindowManager iWindowManager, SuperStatusBarViewFactory superStatusBarViewFactory, StatusBarContentInsetsProvider statusBarContentInsetsProvider, Resources resources) {
        this.mBarHeight = -1;
        this.mContext = context;
        this.mWindowManager = windowManager;
        this.mIWindowManager = iWindowManager;
        this.mContentInsetsProvider = statusBarContentInsetsProvider;
        this.mSuperStatusBarViewFactory = superStatusBarViewFactory;
        StatusBarWindowView statusBarWindowView = superStatusBarViewFactory.getStatusBarWindowView();
        this.mStatusBarView = statusBarWindowView;
        this.mLaunchAnimationContainer = (ViewGroup) statusBarWindowView.findViewById(R$id.status_bar_launch_animation_container);
        this.mResources = resources;
        if (this.mBarHeight < 0) {
            this.mBarHeight = resources.getDimensionPixelSize(17105525);
        }
    }

    public int getStatusBarHeight() {
        return this.mBarHeight;
    }

    public void refreshStatusBarHeight() {
        int dimensionPixelSize = this.mResources.getDimensionPixelSize(17105525);
        if (this.mBarHeight != dimensionPixelSize) {
            this.mBarHeight = dimensionPixelSize;
            apply(this.mCurrentState);
        }
    }

    public void attach() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, this.mBarHeight, 2000, -2139095032, -3);
        this.mLp = layoutParams;
        layoutParams.privateFlags |= 16777216;
        layoutParams.token = new Binder();
        WindowManager.LayoutParams layoutParams2 = this.mLp;
        layoutParams2.gravity = 48;
        layoutParams2.setFitInsetsTypes(0);
        this.mLp.setTitle("StatusBar");
        this.mLp.packageName = this.mContext.getPackageName();
        WindowManager.LayoutParams layoutParams3 = this.mLp;
        layoutParams3.layoutInDisplayCutoutMode = 3;
        this.mWindowManager.addView(this.mStatusBarView, layoutParams3);
        this.mLpChanged.copyFrom(this.mLp);
        this.mContentInsetsProvider.addCallback((StatusBarContentInsetsChangedListener) new StatusBarContentInsetsChangedListener() { // from class: com.android.systemui.statusbar.phone.StatusBarWindowController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.phone.StatusBarContentInsetsChangedListener
            public final void onStatusBarContentInsetsChanged() {
                StatusBarWindowController.this.calculateStatusBarLocationsForAllRotations();
            }
        });
        calculateStatusBarLocationsForAllRotations();
    }

    /* access modifiers changed from: private */
    public void calculateStatusBarLocationsForAllRotations() {
        try {
            this.mIWindowManager.updateStaticPrivacyIndicatorBounds(this.mContext.getDisplayId(), new Rect[]{this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(0), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(1), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(2), this.mContentInsetsProvider.getBoundingRectForPrivacyChipForRotation(3)});
        } catch (RemoteException unused) {
        }
    }

    public void setForceStatusBarVisible(boolean z) {
        State state = this.mCurrentState;
        state.mForceStatusBarVisible = z;
        apply(state);
    }

    public ViewGroup getLaunchAnimationContainer() {
        return this.mLaunchAnimationContainer;
    }

    public void setLaunchAnimationRunning(boolean z) {
        State state = this.mCurrentState;
        if (z != state.mIsLaunchAnimationRunning) {
            state.mIsLaunchAnimationRunning = z;
            apply(state);
        }
    }

    private void applyHeight(State state) {
        this.mLpChanged.height = state.mIsLaunchAnimationRunning ? -1 : this.mBarHeight;
    }

    private void apply(State state) {
        applyForceStatusBarVisibleFlag(state);
        applyHeight(state);
        WindowManager.LayoutParams layoutParams = this.mLp;
        if (layoutParams != null && layoutParams.copyFrom(this.mLpChanged) != 0) {
            this.mWindowManager.updateViewLayout(this.mStatusBarView, this.mLp);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class State {
        boolean mForceStatusBarVisible;
        boolean mIsLaunchAnimationRunning;

        private State() {
        }
    }

    private void applyForceStatusBarVisibleFlag(State state) {
        if (state.mForceStatusBarVisible || state.mIsLaunchAnimationRunning) {
            this.mLpChanged.privateFlags |= 4096;
            return;
        }
        this.mLpChanged.privateFlags &= -4097;
    }
}
