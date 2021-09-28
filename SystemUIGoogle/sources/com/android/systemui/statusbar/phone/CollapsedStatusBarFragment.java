package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.app.Fragment;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.LinearLayout;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallController;
import com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener;
import com.android.systemui.statusbar.policy.EncryptionHelper;
import com.android.systemui.statusbar.policy.KeyguardStateController;
import com.android.systemui.statusbar.policy.NetworkController;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class CollapsedStatusBarFragment extends Fragment implements CommandQueue.Callbacks, StatusBarStateController.StateListener, SystemStatusAnimationCallback {
    private final SystemStatusAnimationScheduler mAnimationScheduler;
    private View mCenteredIconArea;
    private View mClockView;
    private final CommandQueue mCommandQueue;
    private StatusBarIconController.DarkIconManager mDarkIconManager;
    private int mDisabled1;
    private int mDisabled2;
    private final FeatureFlags mFeatureFlags;
    private final KeyguardStateController mKeyguardStateController;
    private final StatusBarLocationPublisher mLocationPublisher;
    private final NetworkController mNetworkController;
    private final NotificationIconAreaController mNotificationIconAreaController;
    private View mNotificationIconAreaInner;
    private View mOngoingCallChip;
    private final OngoingCallController mOngoingCallController;
    private View mOperatorNameFrame;
    private PhoneStatusBarView mStatusBar;
    private final StatusBar mStatusBarComponent;
    private final StatusBarIconController mStatusBarIconController;
    private final StatusBarStateController mStatusBarStateController;
    private LinearLayout mSystemIconArea;
    private List<String> mBlockedIcons = new ArrayList();
    private NetworkController.SignalCallback mSignalCallback = new NetworkController.SignalCallback() { // from class: com.android.systemui.statusbar.phone.CollapsedStatusBarFragment.1
        @Override // com.android.systemui.statusbar.policy.NetworkController.SignalCallback
        public void setIsAirplaneMode(NetworkController.IconState iconState) {
            CollapsedStatusBarFragment.this.mCommandQueue.recomputeDisableFlags(CollapsedStatusBarFragment.this.getContext().getDisplayId(), true);
        }
    };
    private final OngoingCallListener mOngoingCallListener = new OngoingCallListener() { // from class: com.android.systemui.statusbar.phone.CollapsedStatusBarFragment.2
        @Override // com.android.systemui.statusbar.phone.ongoingcall.OngoingCallListener
        public void onOngoingCallStateChanged(boolean z) {
            CollapsedStatusBarFragment collapsedStatusBarFragment = CollapsedStatusBarFragment.this;
            collapsedStatusBarFragment.disable(collapsedStatusBarFragment.getContext().getDisplayId(), CollapsedStatusBarFragment.this.mDisabled1, CollapsedStatusBarFragment.this.mDisabled2, z);
        }
    };
    private View.OnLayoutChangeListener mStatusBarLayoutListener = new View.OnLayoutChangeListener() { // from class: com.android.systemui.statusbar.phone.CollapsedStatusBarFragment$$ExternalSyntheticLambda0
        @Override // android.view.View.OnLayoutChangeListener
        public final void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
            CollapsedStatusBarFragment.this.lambda$new$1(view, i, i2, i3, i4, i5, i6, i7, i8);
        }
    };

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onStateChanged(int i) {
    }

    public CollapsedStatusBarFragment(OngoingCallController ongoingCallController, SystemStatusAnimationScheduler systemStatusAnimationScheduler, StatusBarLocationPublisher statusBarLocationPublisher, NotificationIconAreaController notificationIconAreaController, FeatureFlags featureFlags, StatusBarIconController statusBarIconController, KeyguardStateController keyguardStateController, NetworkController networkController, StatusBarStateController statusBarStateController, StatusBar statusBar, CommandQueue commandQueue) {
        this.mOngoingCallController = ongoingCallController;
        this.mAnimationScheduler = systemStatusAnimationScheduler;
        this.mLocationPublisher = statusBarLocationPublisher;
        this.mNotificationIconAreaController = notificationIconAreaController;
        this.mFeatureFlags = featureFlags;
        this.mStatusBarIconController = statusBarIconController;
        this.mKeyguardStateController = keyguardStateController;
        this.mNetworkController = networkController;
        this.mStatusBarStateController = statusBarStateController;
        this.mStatusBarComponent = statusBar;
        this.mCommandQueue = commandQueue;
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R$layout.status_bar, viewGroup, false);
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        super.onViewCreated(view, bundle);
        PhoneStatusBarView phoneStatusBarView = (PhoneStatusBarView) view;
        this.mStatusBar = phoneStatusBarView;
        View findViewById = phoneStatusBarView.findViewById(R$id.status_bar_contents);
        findViewById.addOnLayoutChangeListener(this.mStatusBarLayoutListener);
        updateStatusBarLocation(findViewById.getLeft(), findViewById.getRight());
        if (bundle != null && bundle.containsKey("panel_state")) {
            this.mStatusBar.restoreHierarchyState(bundle.getSparseParcelableArray("panel_state"));
        }
        StatusBarIconController.DarkIconManager darkIconManager = new StatusBarIconController.DarkIconManager((LinearLayout) view.findViewById(R$id.statusIcons), this.mFeatureFlags);
        this.mDarkIconManager = darkIconManager;
        darkIconManager.setShouldLog(true);
        this.mBlockedIcons.add(getString(17041458));
        this.mBlockedIcons.add(getString(17041427));
        this.mBlockedIcons.add(getString(17041430));
        this.mDarkIconManager.setBlockList(this.mBlockedIcons);
        this.mStatusBarIconController.addIconGroup(this.mDarkIconManager);
        this.mSystemIconArea = (LinearLayout) this.mStatusBar.findViewById(R$id.system_icon_area);
        this.mClockView = this.mStatusBar.findViewById(R$id.clock);
        this.mOngoingCallChip = this.mStatusBar.findViewById(R$id.ongoing_call_chip);
        showSystemIconArea(false);
        showClock(false);
        initEmergencyCryptkeeperText();
        initOperatorName();
        initNotificationIconArea();
        this.mAnimationScheduler.addCallback((SystemStatusAnimationCallback) this);
    }

    @Override // android.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        SparseArray<? extends Parcelable> sparseArray = new SparseArray<>();
        this.mStatusBar.saveHierarchyState(sparseArray);
        bundle.putSparseParcelableArray("panel_state", sparseArray);
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        this.mCommandQueue.addCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.addCallback(this);
        initOngoingCallChip();
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
        this.mStatusBarStateController.removeCallback(this);
        this.mOngoingCallController.removeCallback(this.mOngoingCallListener);
    }

    @Override // android.app.Fragment
    public void onDestroyView() {
        super.onDestroyView();
        this.mStatusBarIconController.removeIconGroup(this.mDarkIconManager);
        this.mAnimationScheduler.removeCallback((SystemStatusAnimationCallback) this);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            this.mNetworkController.removeCallback(this.mSignalCallback);
        }
    }

    public void initNotificationIconArea() {
        ViewGroup viewGroup = (ViewGroup) this.mStatusBar.findViewById(R$id.notification_icon_area);
        View notificationInnerAreaView = this.mNotificationIconAreaController.getNotificationInnerAreaView();
        this.mNotificationIconAreaInner = notificationInnerAreaView;
        if (notificationInnerAreaView.getParent() != null) {
            ((ViewGroup) this.mNotificationIconAreaInner.getParent()).removeView(this.mNotificationIconAreaInner);
        }
        viewGroup.addView(this.mNotificationIconAreaInner);
        ViewGroup viewGroup2 = (ViewGroup) this.mStatusBar.findViewById(R$id.centered_icon_area);
        View centeredNotificationAreaView = this.mNotificationIconAreaController.getCenteredNotificationAreaView();
        this.mCenteredIconArea = centeredNotificationAreaView;
        if (centeredNotificationAreaView.getParent() != null) {
            ((ViewGroup) this.mCenteredIconArea.getParent()).removeView(this.mCenteredIconArea);
        }
        viewGroup2.addView(this.mCenteredIconArea);
        updateNotificationIconAreaAndCallChip(this.mDisabled1, false);
    }

    @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
    public void disable(int i, int i2, int i3, boolean z) {
        if (i == getContext().getDisplayId()) {
            int adjustDisableFlags = adjustDisableFlags(i2);
            int i4 = this.mDisabled1 ^ adjustDisableFlags;
            int i5 = this.mDisabled2 ^ i3;
            this.mDisabled1 = adjustDisableFlags;
            this.mDisabled2 = i3;
            if (!((i4 & 1048576) == 0 && (i5 & 2) == 0)) {
                if ((adjustDisableFlags & 1048576) == 0 && (i3 & 2) == 0) {
                    showSystemIconArea(z);
                    showOperatorName(z);
                } else {
                    hideSystemIconArea(z);
                    hideOperatorName(z);
                }
            }
            if (!((67108864 & i4) == 0 && (131072 & i4) == 0)) {
                updateNotificationIconAreaAndCallChip(adjustDisableFlags, z);
            }
            if ((i4 & 8388608) != 0 || this.mClockView.getVisibility() != clockHiddenMode()) {
                if ((adjustDisableFlags & 8388608) != 0) {
                    hideClock(z);
                } else {
                    showClock(z);
                }
            }
        }
    }

    protected int adjustDisableFlags(int i) {
        boolean headsUpShouldBeVisible = this.mStatusBarComponent.headsUpShouldBeVisible();
        if (headsUpShouldBeVisible) {
            i |= 8388608;
        }
        if (!this.mKeyguardStateController.isLaunchTransitionFadingAway() && !this.mKeyguardStateController.isKeyguardFadingAway() && shouldHideNotificationIcons() && (this.mStatusBarStateController.getState() != 1 || !headsUpShouldBeVisible)) {
            i = i | 131072 | 1048576 | 8388608;
        }
        NetworkController networkController = this.mNetworkController;
        if (networkController != null && EncryptionHelper.IS_DATA_ENCRYPTED) {
            if (networkController.hasEmergencyCryptKeeperText()) {
                i |= 131072;
            }
            if (!this.mNetworkController.isRadioOn()) {
                i |= 1048576;
            }
        }
        if (this.mStatusBarStateController.isDozing() && this.mStatusBarComponent.getPanelController().hasCustomClock()) {
            i |= 9437184;
        }
        return this.mOngoingCallController.hasOngoingCall() ? -67108865 & i : 67108864 | i;
    }

    private void updateNotificationIconAreaAndCallChip(int i, boolean z) {
        boolean z2 = true;
        boolean z3 = (131072 & i) != 0;
        boolean z4 = (i & 67108864) == 0;
        if (z3 || z4) {
            hideNotificationIconArea(z);
        } else {
            showNotificationIconArea(z);
        }
        if (!z4 || z3) {
            z2 = false;
        }
        if (z2) {
            showOngoingCallChip(z);
        } else {
            hideOngoingCallChip(z);
        }
        this.mOngoingCallController.notifyChipVisibilityChanged(z2);
    }

    private boolean shouldHideNotificationIcons() {
        if ((this.mStatusBar.isClosed() || !this.mStatusBarComponent.hideStatusBarIconsWhenExpanded()) && !this.mStatusBarComponent.hideStatusBarIconsForBouncer()) {
            return false;
        }
        return true;
    }

    private void hideSystemIconArea(boolean z) {
        animateHide(this.mSystemIconArea, z);
    }

    private void showSystemIconArea(boolean z) {
        int animationState = this.mAnimationScheduler.getAnimationState();
        if (animationState == 0 || animationState == 4) {
            animateShow(this.mSystemIconArea, z);
        }
    }

    private void hideClock(boolean z) {
        animateHiddenState(this.mClockView, clockHiddenMode(), z);
    }

    private void showClock(boolean z) {
        animateShow(this.mClockView, z);
    }

    public void hideOngoingCallChip(boolean z) {
        animateHiddenState(this.mOngoingCallChip, 8, z);
    }

    public void showOngoingCallChip(boolean z) {
        animateShow(this.mOngoingCallChip, z);
    }

    private int clockHiddenMode() {
        return (this.mStatusBar.isClosed() || this.mKeyguardStateController.isShowing() || this.mStatusBarStateController.isDozing()) ? 8 : 4;
    }

    public void hideNotificationIconArea(boolean z) {
        animateHide(this.mNotificationIconAreaInner, z);
        animateHide(this.mCenteredIconArea, z);
    }

    public void showNotificationIconArea(boolean z) {
        animateShow(this.mNotificationIconAreaInner, z);
        animateShow(this.mCenteredIconArea, z);
    }

    public void hideOperatorName(boolean z) {
        View view = this.mOperatorNameFrame;
        if (view != null) {
            animateHide(view, z);
        }
    }

    public void showOperatorName(boolean z) {
        View view = this.mOperatorNameFrame;
        if (view != null) {
            animateShow(view, z);
        }
    }

    private void animateHiddenState(View view, int i, boolean z) {
        view.animate().cancel();
        if (!z) {
            view.setAlpha(0.0f);
            view.setVisibility(i);
            return;
        }
        view.animate().alpha(0.0f).setDuration(160).setStartDelay(0).setInterpolator(Interpolators.ALPHA_OUT).withEndAction(new Runnable(view, i) { // from class: com.android.systemui.statusbar.phone.CollapsedStatusBarFragment$$ExternalSyntheticLambda1
            public final /* synthetic */ View f$0;
            public final /* synthetic */ int f$1;

            {
                this.f$0 = r1;
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.f$0.setVisibility(this.f$1);
            }
        });
    }

    private void animateHide(View view, boolean z) {
        animateHiddenState(view, 4, z);
    }

    private void animateShow(View view, boolean z) {
        view.animate().cancel();
        view.setVisibility(0);
        if (!z) {
            view.setAlpha(1.0f);
            return;
        }
        view.animate().alpha(1.0f).setDuration(320).setInterpolator(Interpolators.ALPHA_IN).setStartDelay(50).withEndAction(null);
        if (this.mKeyguardStateController.isKeyguardFadingAway()) {
            view.animate().setDuration(this.mKeyguardStateController.getKeyguardFadingAwayDuration()).setInterpolator(Interpolators.LINEAR_OUT_SLOW_IN).setStartDelay(this.mKeyguardStateController.getKeyguardFadingAwayDelay()).start();
        }
    }

    private void initEmergencyCryptkeeperText() {
        View findViewById = this.mStatusBar.findViewById(R$id.emergency_cryptkeeper_text);
        if (this.mNetworkController.hasEmergencyCryptKeeperText()) {
            if (findViewById != null) {
                ((ViewStub) findViewById).inflate();
            }
            this.mNetworkController.addCallback(this.mSignalCallback);
        } else if (findViewById != null) {
            ((ViewGroup) findViewById.getParent()).removeView(findViewById);
        }
    }

    private void initOperatorName() {
        if (getResources().getBoolean(R$bool.config_showOperatorNameInStatusBar)) {
            this.mOperatorNameFrame = ((ViewStub) this.mStatusBar.findViewById(R$id.operator_name)).inflate();
        }
    }

    private void initOngoingCallChip() {
        this.mOngoingCallController.addCallback(this.mOngoingCallListener);
        this.mOngoingCallController.setChipView(this.mOngoingCallChip);
    }

    @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
    public void onDozingChanged(boolean z) {
        disable(getContext().getDisplayId(), this.mDisabled1, this.mDisabled2, false);
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public void onSystemChromeAnimationStart() {
        if (this.mAnimationScheduler.getAnimationState() == 3 && !isSystemIconAreaDisabled()) {
            this.mSystemIconArea.setVisibility(0);
            this.mSystemIconArea.setAlpha(0.0f);
        }
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public void onSystemChromeAnimationEnd() {
        if (this.mAnimationScheduler.getAnimationState() == 1) {
            this.mSystemIconArea.setVisibility(4);
            this.mSystemIconArea.setAlpha(0.0f);
        } else if (!isSystemIconAreaDisabled()) {
            this.mSystemIconArea.setAlpha(1.0f);
            this.mSystemIconArea.setVisibility(0);
        }
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public void onSystemChromeAnimationUpdate(ValueAnimator valueAnimator) {
        this.mSystemIconArea.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    private boolean isSystemIconAreaDisabled() {
        return ((this.mDisabled1 & 1048576) == 0 && (this.mDisabled2 & 2) == 0) ? false : true;
    }

    private void updateStatusBarLocation(int i, int i2) {
        this.mLocationPublisher.updateStatusBarMargin(i - this.mStatusBar.getLeft(), this.mStatusBar.getRight() - i2);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i != i5 || i3 != i7) {
            updateStatusBarLocation(i, i3);
        }
    }
}
