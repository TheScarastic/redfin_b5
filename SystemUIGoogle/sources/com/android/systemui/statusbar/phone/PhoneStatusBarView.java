package com.android.systemui.statusbar.phone;

import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.android.systemui.Dependency;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.leak.RotationUtils;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class PhoneStatusBarView extends PanelBar {
    StatusBar mBar;
    private DarkIconDispatcher.DarkReceiver mBattery;
    private View mCenterIconSpace;
    private DarkIconDispatcher.DarkReceiver mClock;
    private View mCutoutSpace;
    private DisplayCutout mDisplayCutout;
    private List<StatusBar.ExpansionChangedListener> mExpansionChangedListeners;
    private boolean mHeadsUpVisible;
    private float mMinFraction;
    private ScrimController mScrimController;
    private int mStatusBarHeight;
    boolean mIsFullyOpenedPanel = false;
    private Runnable mHideExpandedRunnable = new Runnable() { // from class: com.android.systemui.statusbar.phone.PhoneStatusBarView.1
        @Override // java.lang.Runnable
        public void run() {
            PhoneStatusBarView phoneStatusBarView = PhoneStatusBarView.this;
            if (phoneStatusBarView.mPanelFraction == 0.0f) {
                phoneStatusBarView.mBar.makeExpandedInvisible();
            }
        }
    };
    private int mRotationOrientation = -1;
    private int mCutoutSideNudge = 0;
    private final CommandQueue mCommandQueue = (CommandQueue) Dependency.get(CommandQueue.class);
    private final StatusBarContentInsetsProvider mContentInsetsProvider = (StatusBarContentInsetsProvider) Dependency.get(StatusBarContentInsetsProvider.class);

    public PhoneStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setBar(StatusBar statusBar) {
        this.mBar = statusBar;
    }

    public void setExpansionChangedListeners(List<StatusBar.ExpansionChangedListener> list) {
        this.mExpansionChangedListeners = list;
    }

    public void setScrimController(ScrimController scrimController) {
        this.mScrimController = scrimController;
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar, android.view.View
    public void onFinishInflate() {
        this.mBattery = (DarkIconDispatcher.DarkReceiver) findViewById(R$id.battery);
        this.mClock = (DarkIconDispatcher.DarkReceiver) findViewById(R$id.clock);
        this.mCutoutSpace = findViewById(R$id.cutout_space_view);
        this.mCenterIconSpace = findViewById(R$id.centered_icon_area);
        updateResources();
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).addDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).addDarkReceiver(this.mClock);
        if (updateOrientationAndCutout()) {
            updateLayoutForCutout();
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).removeDarkReceiver(this.mBattery);
        ((DarkIconDispatcher) Dependency.get(DarkIconDispatcher.class)).removeDarkReceiver(this.mClock);
        this.mDisplayCutout = null;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        if (updateOrientationAndCutout()) {
            updateLayoutForCutout();
            requestLayout();
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        if (updateOrientationAndCutout()) {
            updateLayoutForCutout();
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    private boolean updateOrientationAndCutout() {
        boolean z;
        int exactRotation = RotationUtils.getExactRotation(((FrameLayout) this).mContext);
        if (exactRotation != this.mRotationOrientation) {
            this.mRotationOrientation = exactRotation;
            z = true;
        } else {
            z = false;
        }
        if (Objects.equals(getRootWindowInsets().getDisplayCutout(), this.mDisplayCutout)) {
            return z;
        }
        this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
        return true;
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public boolean panelEnabled() {
        return this.mCommandQueue.panelsEnabled();
    }

    public boolean onRequestSendAccessibilityEventInternal(View view, AccessibilityEvent accessibilityEvent) {
        if (!super.onRequestSendAccessibilityEventInternal(view, accessibilityEvent)) {
            return false;
        }
        AccessibilityEvent obtain = AccessibilityEvent.obtain();
        onInitializeAccessibilityEvent(obtain);
        dispatchPopulateAccessibilityEvent(obtain);
        accessibilityEvent.appendRecord(obtain);
        return true;
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onPanelPeeked() {
        super.onPanelPeeked();
        this.mBar.makeExpandedVisible(false);
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onPanelCollapsed() {
        super.onPanelCollapsed();
        post(this.mHideExpandedRunnable);
        this.mIsFullyOpenedPanel = false;
    }

    public void removePendingHideExpandedRunnables() {
        removeCallbacks(this.mHideExpandedRunnable);
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onPanelFullyOpened() {
        super.onPanelFullyOpened();
        if (!this.mIsFullyOpenedPanel) {
            this.mPanel.getView().sendAccessibilityEvent(32);
        }
        this.mIsFullyOpenedPanel = true;
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar, android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mBar.interceptTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onTrackingStarted() {
        super.onTrackingStarted();
        this.mBar.onTrackingStarted();
        this.mScrimController.onTrackingStarted();
        removePendingHideExpandedRunnables();
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onClosingFinished() {
        super.onClosingFinished();
        this.mBar.onClosingFinished();
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onTrackingStopped(boolean z) {
        super.onTrackingStopped(z);
        this.mBar.onTrackingStopped(z);
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void onExpandingFinished() {
        super.onExpandingFinished();
        this.mScrimController.onExpandingFinished();
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        return this.mBar.interceptTouchEvent(motionEvent) || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void panelScrimMinFractionChanged(float f) {
        if (Float.isNaN(f)) {
            throw new IllegalArgumentException("minFraction cannot be NaN");
        } else if (this.mMinFraction != f) {
            this.mMinFraction = f;
            updateScrimFraction();
        }
    }

    @Override // com.android.systemui.statusbar.phone.PanelBar
    public void panelExpansionChanged(float f, boolean z) {
        super.panelExpansionChanged(f, z);
        updateScrimFraction();
        if ((f == 0.0f || f == 1.0f) && this.mBar.getNavigationBarView() != null) {
            this.mBar.getNavigationBarView().onStatusBarPanelStateChanged();
        }
        List<StatusBar.ExpansionChangedListener> list = this.mExpansionChangedListeners;
        if (list != null) {
            for (StatusBar.ExpansionChangedListener expansionChangedListener : list) {
                expansionChangedListener.onExpansionChanged(f, z);
            }
        }
    }

    private void updateScrimFraction() {
        float f = this.mPanelFraction;
        float f2 = this.mMinFraction;
        if (f2 < 1.0f) {
            f = Math.max((f - f2) / (1.0f - f2), 0.0f);
        }
        this.mScrimController.setPanelExpansion(f);
    }

    public void updateResources() {
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(R$dimen.display_cutout_margin_consumption);
        updateStatusBarHeight();
    }

    private void updateStatusBarHeight() {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.status_bar_height);
        this.mStatusBarHeight = dimensionPixelSize;
        layoutParams.height = dimensionPixelSize - i;
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.status_bar_padding_top);
        int dimensionPixelSize3 = getResources().getDimensionPixelSize(R$dimen.status_bar_padding_start);
        findViewById(R$id.status_bar_contents).setPaddingRelative(dimensionPixelSize3, dimensionPixelSize2, getResources().getDimensionPixelSize(R$dimen.status_bar_padding_end), 0);
        findViewById(R$id.notification_lights_out).setPaddingRelative(0, dimensionPixelSize3, 0, 0);
        setLayoutParams(layoutParams);
    }

    private void updateLayoutForCutout() {
        updateStatusBarHeight();
        updateCutoutLocation(StatusBarWindowView.cornerCutoutMargins(this.mDisplayCutout, getDisplay()));
        updateSafeInsets();
    }

    private void updateCutoutLocation(Pair<Integer, Integer> pair) {
        if (this.mCutoutSpace != null) {
            DisplayCutout displayCutout = this.mDisplayCutout;
            if (displayCutout == null || displayCutout.isEmpty() || pair != null) {
                this.mCenterIconSpace.setVisibility(0);
                this.mCutoutSpace.setVisibility(8);
                return;
            }
            this.mCenterIconSpace.setVisibility(8);
            this.mCutoutSpace.setVisibility(0);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
            Rect rect = new Rect();
            ScreenDecorations.DisplayCutoutView.boundsFromDirection(this.mDisplayCutout, 48, rect);
            int i = rect.left;
            int i2 = this.mCutoutSideNudge;
            rect.left = i + i2;
            rect.right -= i2;
            layoutParams.width = rect.width();
            layoutParams.height = rect.height();
        }
    }

    private void updateSafeInsets() {
        Rect statusBarContentInsetsForRotation = this.mContentInsetsProvider.getStatusBarContentInsetsForRotation(RotationUtils.getExactRotation(getContext()));
        Point point = new Point();
        getDisplay().getRealSize(point);
        setPadding(statusBarContentInsetsForRotation.left, getPaddingTop(), point.x - statusBarContentInsetsForRotation.right, getPaddingBottom());
    }

    public void setHeadsUpVisible(boolean z) {
        this.mHeadsUpVisible = z;
        updateVisibility();
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.phone.PanelBar
    public boolean shouldPanelBeVisible() {
        return this.mHeadsUpVisible || super.shouldPanelBeVisible();
    }
}
