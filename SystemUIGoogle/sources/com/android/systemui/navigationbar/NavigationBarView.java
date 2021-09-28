package com.android.systemui.navigationbar;

import android.animation.LayoutTransition;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.TimeInterpolator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowInsets;
import android.view.WindowManager;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.FrameLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.Dependency;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.R$style;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.model.SysUiState;
import com.android.systemui.navigationbar.NavigationModeController;
import com.android.systemui.navigationbar.buttons.ButtonDispatcher;
import com.android.systemui.navigationbar.buttons.ContextualButton;
import com.android.systemui.navigationbar.buttons.ContextualButtonGroup;
import com.android.systemui.navigationbar.buttons.DeadZone;
import com.android.systemui.navigationbar.buttons.KeyButtonDrawable;
import com.android.systemui.navigationbar.buttons.NearestTouchFrame;
import com.android.systemui.navigationbar.buttons.RotationContextButton;
import com.android.systemui.navigationbar.gestural.EdgeBackGestureHandler;
import com.android.systemui.navigationbar.gestural.FloatingRotationButton;
import com.android.systemui.navigationbar.gestural.RegionSamplingHelper;
import com.android.systemui.recents.OverviewProxyService;
import com.android.systemui.recents.Recents;
import com.android.systemui.shared.system.ActivityManagerWrapper;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.systemui.shared.system.SysUiStatsLog;
import com.android.systemui.shared.system.WindowManagerWrapper;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.phone.AutoHideController;
import com.android.systemui.statusbar.phone.LightBarTransitionsController;
import com.android.systemui.statusbar.phone.NotificationPanelViewController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.util.Utils;
import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import com.android.wm.shell.pip.Pip;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class NavigationBarView extends FrameLayout implements NavigationModeController.ModeChangedListener {
    private AutoHideController mAutoHideController;
    private KeyButtonDrawable mBackIcon;
    private final SparseArray<ButtonDispatcher> mButtonDispatchers;
    private final ContextualButtonGroup mContextualButtonGroup;
    private int mDarkIconColor;
    private KeyButtonDrawable mDockedIcon;
    private boolean mDockedStackExists;
    private EdgeBackGestureHandler mEdgeBackGestureHandler;
    private FloatingRotationButton mFloatingRotationButton;
    private KeyButtonDrawable mHomeDefaultIcon;
    private View mHorizontal;
    private boolean mImeVisible;
    private Context mLightContext;
    private int mLightIconColor;
    private NavigationBarOverlayController mNavBarOverlayController;
    private final Consumer<Boolean> mNavbarOverlayVisibilityChangeCallback;
    private NavigationBarInflaterView mNavigationInflaterView;
    private OnVerticalChangedListener mOnVerticalChangedListener;
    private Rect mOrientedHandleSamplingRegion;
    private NotificationPanelViewController mPanelView;
    private KeyButtonDrawable mRecentIcon;
    private RotationButtonController mRotationButtonController;
    private RotationContextButton mRotationContextButton;
    private View mVertical;
    private boolean mWakeAndUnlocking;
    View mCurrentView = null;
    private int mCurrentRotation = -1;
    int mDisabledFlags = 0;
    int mNavigationIconHints = 0;
    private final Region mTmpRegion = new Region();
    private final int[] mTmpPosition = new int[2];
    private Rect mTmpBounds = new Rect();
    private Map<View, Rect> mButtonFullTouchableRegions = new HashMap();
    private boolean mDeadZoneConsuming = false;
    private final NavTransitionListener mTransitionListener = new NavTransitionListener();
    private boolean mLayoutTransitionsEnabled = true;
    private boolean mUseCarModeUi = false;
    private boolean mInCarMode = false;
    private boolean mScreenOn = true;
    private Rect mSamplingBounds = new Rect();
    private final View.AccessibilityDelegate mQuickStepAccessibilityDelegate = new View.AccessibilityDelegate() { // from class: com.android.systemui.navigationbar.NavigationBarView.1
        private AccessibilityNodeInfo.AccessibilityAction mToggleOverviewAction;

        @Override // android.view.View.AccessibilityDelegate
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
            if (this.mToggleOverviewAction == null) {
                this.mToggleOverviewAction = new AccessibilityNodeInfo.AccessibilityAction(R$id.action_toggle_overview, NavigationBarView.this.getContext().getString(R$string.quick_step_accessibility_toggle_overview));
            }
            accessibilityNodeInfo.addAction(this.mToggleOverviewAction);
        }

        @Override // android.view.View.AccessibilityDelegate
        public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
            if (i != R$id.action_toggle_overview) {
                return super.performAccessibilityAction(view, i, bundle);
            }
            ((Recents) Dependency.get(Recents.class)).toggleRecentApps();
            return true;
        }
    };
    private final ViewTreeObserver.OnComputeInternalInsetsListener mOnComputeInternalInsetsListener = new ViewTreeObserver.OnComputeInternalInsetsListener() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda0
        public final void onComputeInternalInsets(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
            NavigationBarView.this.lambda$new$0(internalInsetsInfo);
        }
    };
    private final Consumer<Boolean> mRotationButtonListener = new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda5
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            NavigationBarView.this.lambda$new$1((Boolean) obj);
        }
    };
    private final Consumer<Boolean> mDockedListener = new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda7
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            NavigationBarView.this.lambda$new$4((Boolean) obj);
        }
    };
    private final Consumer<Rect> mPipListener = new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda4
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            NavigationBarView.this.lambda$new$6((Rect) obj);
        }
    };
    private boolean mIsVertical = false;
    boolean mLongClickableAccessibilityButton = false;
    private int mNavBarMode = ((NavigationModeController) Dependency.get(NavigationModeController.class)).addListener(this);
    private final SysUiState mSysUiFlagContainer = (SysUiState) Dependency.get(SysUiState.class);
    private final OverviewProxyService mOverviewProxyService = (OverviewProxyService) Dependency.get(OverviewProxyService.class);
    private Configuration mConfiguration = new Configuration();
    private Configuration mTmpLastConfiguration = new Configuration();
    private ScreenPinningNotify mScreenPinningNotify = new ScreenPinningNotify(((FrameLayout) this).mContext);
    private final NavigationBarTransitions mBarTransitions = new NavigationBarTransitions(this, (CommandQueue) Dependency.get(CommandQueue.class));
    private final DeadZone mDeadZone = new DeadZone(this);
    private final int mNavColorSampleMargin = getResources().getDimensionPixelSize(R$dimen.navigation_handle_sample_horizontal_margin);
    private final RegionSamplingHelper mRegionSamplingHelper = new RegionSamplingHelper(this, new RegionSamplingHelper.SamplingCallback() { // from class: com.android.systemui.navigationbar.NavigationBarView.2
        @Override // com.android.systemui.navigationbar.gestural.RegionSamplingHelper.SamplingCallback
        public void onRegionDarknessChanged(boolean z) {
            NavigationBarView.this.getLightTransitionsController().setIconsDark(!z, true);
        }

        @Override // com.android.systemui.navigationbar.gestural.RegionSamplingHelper.SamplingCallback
        public Rect getSampledRegion(View view) {
            if (NavigationBarView.this.mOrientedHandleSamplingRegion != null) {
                return NavigationBarView.this.mOrientedHandleSamplingRegion;
            }
            NavigationBarView.this.updateSamplingRect();
            return NavigationBarView.this.mSamplingBounds;
        }

        @Override // com.android.systemui.navigationbar.gestural.RegionSamplingHelper.SamplingCallback
        public boolean isSamplingEnabled() {
            return Utils.isGesturalModeOnDefaultDisplay(NavigationBarView.this.getContext(), NavigationBarView.this.mNavBarMode);
        }
    });

    /* loaded from: classes.dex */
    public interface OnVerticalChangedListener {
        void onVerticalChanged(boolean z);
    }

    private static String visibilityToString(int i) {
        return i != 4 ? i != 8 ? "VISIBLE" : "GONE" : "INVISIBLE";
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public class NavTransitionListener implements LayoutTransition.TransitionListener {
        private boolean mBackTransitioning;
        private long mDuration;
        private boolean mHomeAppearing;
        private TimeInterpolator mInterpolator;
        private long mStartDelay;

        private NavTransitionListener() {
        }

        @Override // android.animation.LayoutTransition.TransitionListener
        public void startTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == R$id.back) {
                this.mBackTransitioning = true;
            } else if (view.getId() == R$id.home && i == 2) {
                this.mHomeAppearing = true;
                this.mStartDelay = layoutTransition.getStartDelay(i);
                this.mDuration = layoutTransition.getDuration(i);
                this.mInterpolator = layoutTransition.getInterpolator(i);
            }
        }

        @Override // android.animation.LayoutTransition.TransitionListener
        public void endTransition(LayoutTransition layoutTransition, ViewGroup viewGroup, View view, int i) {
            if (view.getId() == R$id.back) {
                this.mBackTransitioning = false;
            } else if (view.getId() == R$id.home && i == 2) {
                this.mHomeAppearing = false;
            }
        }

        public void onBackAltCleared() {
            ButtonDispatcher backButton = NavigationBarView.this.getBackButton();
            if (!this.mBackTransitioning && backButton.getVisibility() == 0 && this.mHomeAppearing && NavigationBarView.this.getHomeButton().getAlpha() == 0.0f) {
                NavigationBarView.this.getBackButton().setAlpha(0.0f);
                ObjectAnimator ofFloat = ObjectAnimator.ofFloat(backButton, "alpha", 0.0f, 1.0f);
                ofFloat.setStartDelay(this.mStartDelay);
                ofFloat.setDuration(this.mDuration);
                ofFloat.setInterpolator(this.mInterpolator);
                ofFloat.start();
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(ViewTreeObserver.InternalInsetsInfo internalInsetsInfo) {
        if (!this.mEdgeBackGestureHandler.isHandlingGestures()) {
            internalInsetsInfo.setTouchableInsets(0);
            return;
        }
        internalInsetsInfo.setTouchableInsets(3);
        internalInsetsInfo.touchableRegion.set(getButtonLocations(false, false, false));
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Boolean bool) {
        if (bool.booleanValue()) {
            this.mAutoHideController.touchAutoHide();
        }
        notifyActiveTouchRegions();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Boolean bool) {
        if (bool.booleanValue()) {
            this.mAutoHideController.touchAutoHide();
        }
        notifyActiveTouchRegions();
    }

    public NavigationBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        SparseArray<ButtonDispatcher> sparseArray = new SparseArray<>();
        this.mButtonDispatchers = sparseArray;
        NavigationBarView$$ExternalSyntheticLambda6 navigationBarView$$ExternalSyntheticLambda6 = new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda6
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                NavigationBarView.this.lambda$new$2((Boolean) obj);
            }
        };
        this.mNavbarOverlayVisibilityChangeCallback = navigationBarView$$ExternalSyntheticLambda6;
        ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, com.android.settingslib.Utils.getThemeAttr(context, R$attr.darkIconTheme));
        ContextThemeWrapper contextThemeWrapper2 = new ContextThemeWrapper(context, com.android.settingslib.Utils.getThemeAttr(context, R$attr.lightIconTheme));
        this.mLightContext = contextThemeWrapper2;
        int i = R$attr.singleToneColor;
        this.mLightIconColor = com.android.settingslib.Utils.getColorAttrDefaultColor(contextThemeWrapper2, i);
        this.mDarkIconColor = com.android.settingslib.Utils.getColorAttrDefaultColor(contextThemeWrapper, i);
        int i2 = R$id.menu_container;
        ContextualButtonGroup contextualButtonGroup = new ContextualButtonGroup(i2);
        this.mContextualButtonGroup = contextualButtonGroup;
        int i3 = R$id.ime_switcher;
        ContextualButton contextualButton = new ContextualButton(i3, this.mLightContext, R$drawable.ic_ime_switcher_default);
        int i4 = R$id.accessibility_button;
        ContextualButton contextualButton2 = new ContextualButton(i4, this.mLightContext, R$drawable.ic_sysbar_accessibility_button);
        contextualButtonGroup.addButton(contextualButton);
        contextualButtonGroup.addButton(contextualButton2);
        this.mRotationContextButton = new RotationContextButton(R$id.rotate_suggestion, this.mLightContext, R$drawable.ic_sysbar_rotate_button_ccw_start_0);
        this.mFloatingRotationButton = new FloatingRotationButton(context);
        this.mRotationButtonController = new RotationButtonController(this.mLightContext, this.mLightIconColor, this.mDarkIconColor);
        updateRotationButton();
        this.mConfiguration.updateFrom(context.getResources().getConfiguration());
        int i5 = R$id.back;
        sparseArray.put(i5, new ButtonDispatcher(i5));
        int i6 = R$id.home;
        sparseArray.put(i6, new ButtonDispatcher(i6));
        int i7 = R$id.home_handle;
        sparseArray.put(i7, new ButtonDispatcher(i7));
        int i8 = R$id.recent_apps;
        sparseArray.put(i8, new ButtonDispatcher(i8));
        sparseArray.put(i3, contextualButton);
        sparseArray.put(i4, contextualButton2);
        sparseArray.put(i2, contextualButtonGroup);
        EdgeBackGestureHandler create = ((EdgeBackGestureHandler.Factory) Dependency.get(EdgeBackGestureHandler.Factory.class)).create(((FrameLayout) this).mContext);
        this.mEdgeBackGestureHandler = create;
        create.setStateChangeCallback(new Runnable() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                NavigationBarView.this.updateStates();
            }
        });
        NavigationBarOverlayController navigationBarOverlayController = (NavigationBarOverlayController) Dependency.get(NavigationBarOverlayController.class);
        this.mNavBarOverlayController = navigationBarOverlayController;
        if (navigationBarOverlayController.isNavigationBarOverlayEnabled()) {
            NavigationBarOverlayController navigationBarOverlayController2 = this.mNavBarOverlayController;
            EdgeBackGestureHandler edgeBackGestureHandler = this.mEdgeBackGestureHandler;
            Objects.requireNonNull(edgeBackGestureHandler);
            navigationBarOverlayController2.init(navigationBarView$$ExternalSyntheticLambda6, new Consumer() { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda8
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    EdgeBackGestureHandler.this.updateNavigationBarOverlayExcludeRegion((Rect) obj);
                }
            });
        }
    }

    public void setAutoHideController(AutoHideController autoHideController) {
        this.mAutoHideController = autoHideController;
    }

    public NavigationBarTransitions getBarTransitions() {
        return this.mBarTransitions;
    }

    public LightBarTransitionsController getLightTransitionsController() {
        return this.mBarTransitions.getLightTransitionsController();
    }

    public void setComponents(NotificationPanelViewController notificationPanelViewController) {
        this.mPanelView = notificationPanelViewController;
        updatePanelSystemUiStateFlags();
    }

    public void setOnVerticalChangedListener(OnVerticalChangedListener onVerticalChangedListener) {
        this.mOnVerticalChangedListener = onVerticalChangedListener;
        notifyVerticalChangedListener(this.mIsVertical);
    }

    @Override // android.view.ViewGroup
    public boolean onInterceptTouchEvent(MotionEvent motionEvent) {
        if (QuickStepContract.isGesturalMode(this.mNavBarMode) && this.mImeVisible && motionEvent.getAction() == 0) {
            SysUiStatsLog.write(304, (int) motionEvent.getX(), (int) motionEvent.getY());
        }
        return shouldDeadZoneConsumeTouchEvents(motionEvent) || super.onInterceptTouchEvent(motionEvent);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        shouldDeadZoneConsumeTouchEvents(motionEvent);
        return super.onTouchEvent(motionEvent);
    }

    public void setWindowHasBlurs(boolean z) {
        this.mRegionSamplingHelper.setWindowHasBlurs(z);
    }

    /* access modifiers changed from: package-private */
    public void onTransientStateChanged(boolean z) {
        this.mEdgeBackGestureHandler.onNavBarTransientStateChanged(z);
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.setButtonState(z, false);
        }
    }

    /* access modifiers changed from: package-private */
    public void onBarTransition(int i) {
        if (i == 4) {
            this.mRegionSamplingHelper.stop();
            getLightTransitionsController().setIconsDark(false, true);
            return;
        }
        this.mRegionSamplingHelper.start(this.mSamplingBounds);
    }

    private boolean shouldDeadZoneConsumeTouchEvents(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 0) {
            this.mDeadZoneConsuming = false;
        }
        if (!this.mDeadZone.onTouchEvent(motionEvent) && !this.mDeadZoneConsuming) {
            return false;
        }
        if (actionMasked == 0) {
            setSlippery(true);
            this.mDeadZoneConsuming = true;
        } else if (actionMasked == 1 || actionMasked == 3) {
            updateSlippery();
            this.mDeadZoneConsuming = false;
        }
        return true;
    }

    public void abortCurrentGesture() {
        getHomeButton().abortCurrentGesture();
    }

    public View getCurrentView() {
        return this.mCurrentView;
    }

    public RotationButtonController getRotationButtonController() {
        return this.mRotationButtonController;
    }

    public ButtonDispatcher getRecentsButton() {
        return this.mButtonDispatchers.get(R$id.recent_apps);
    }

    public ButtonDispatcher getBackButton() {
        return this.mButtonDispatchers.get(R$id.back);
    }

    public ButtonDispatcher getHomeButton() {
        return this.mButtonDispatchers.get(R$id.home);
    }

    public ButtonDispatcher getImeSwitchButton() {
        return this.mButtonDispatchers.get(R$id.ime_switcher);
    }

    public ButtonDispatcher getAccessibilityButton() {
        return this.mButtonDispatchers.get(R$id.accessibility_button);
    }

    public RotationContextButton getRotateSuggestionButton() {
        return (RotationContextButton) this.mButtonDispatchers.get(R$id.rotate_suggestion);
    }

    public ButtonDispatcher getHomeHandle() {
        return this.mButtonDispatchers.get(R$id.home_handle);
    }

    public SparseArray<ButtonDispatcher> getButtonDispatchers() {
        return this.mButtonDispatchers;
    }

    public boolean isRecentsButtonVisible() {
        return getRecentsButton().getVisibility() == 0;
    }

    public boolean isOverviewEnabled() {
        return (this.mDisabledFlags & 16777216) == 0;
    }

    public boolean isQuickStepSwipeUpEnabled() {
        return this.mOverviewProxyService.shouldShowSwipeUpUI() && isOverviewEnabled();
    }

    private void reloadNavIcons() {
        updateIcons(Configuration.EMPTY);
    }

    private void updateIcons(Configuration configuration) {
        int i = configuration.orientation;
        Configuration configuration2 = this.mConfiguration;
        boolean z = true;
        boolean z2 = i != configuration2.orientation;
        boolean z3 = configuration.densityDpi != configuration2.densityDpi;
        if (configuration.getLayoutDirection() == this.mConfiguration.getLayoutDirection()) {
            z = false;
        }
        if (z2 || z3) {
            this.mDockedIcon = getDrawable(R$drawable.ic_sysbar_docked);
            this.mHomeDefaultIcon = getHomeDrawable();
        }
        if (z3 || z) {
            this.mRecentIcon = getDrawable(R$drawable.ic_sysbar_recent);
            this.mContextualButtonGroup.updateIcons(this.mLightIconColor, this.mDarkIconColor);
        }
        if (z2 || z3 || z) {
            this.mBackIcon = getBackDrawable();
        }
    }

    private void updateRotationButton() {
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            ContextualButtonGroup contextualButtonGroup = this.mContextualButtonGroup;
            int i = R$id.rotate_suggestion;
            contextualButtonGroup.removeButton(i);
            this.mButtonDispatchers.remove(i);
            this.mRotationButtonController.setRotationButton(this.mFloatingRotationButton, this.mRotationButtonListener);
            return;
        }
        ContextualButtonGroup contextualButtonGroup2 = this.mContextualButtonGroup;
        int i2 = R$id.rotate_suggestion;
        if (contextualButtonGroup2.getContextButton(i2) == null) {
            this.mContextualButtonGroup.addButton(this.mRotationContextButton);
            this.mButtonDispatchers.put(i2, this.mRotationContextButton);
            this.mRotationButtonController.setRotationButton(this.mRotationContextButton, this.mRotationButtonListener);
        }
    }

    public KeyButtonDrawable getBackDrawable() {
        KeyButtonDrawable drawable = getDrawable(getBackDrawableRes());
        orientBackButton(drawable);
        return drawable;
    }

    public int getBackDrawableRes() {
        return chooseNavigationIconDrawableRes(R$drawable.ic_sysbar_back, R$drawable.ic_sysbar_back_quick_step);
    }

    public KeyButtonDrawable getHomeDrawable() {
        KeyButtonDrawable keyButtonDrawable;
        if (this.mOverviewProxyService.shouldShowSwipeUpUI()) {
            keyButtonDrawable = getDrawable(R$drawable.ic_sysbar_home_quick_step);
        } else {
            keyButtonDrawable = getDrawable(R$drawable.ic_sysbar_home);
        }
        orientHomeButton(keyButtonDrawable);
        return keyButtonDrawable;
    }

    private void orientBackButton(KeyButtonDrawable keyButtonDrawable) {
        float f;
        boolean z = (this.mNavigationIconHints & 1) != 0;
        boolean z2 = this.mConfiguration.getLayoutDirection() == 1;
        float f2 = 0.0f;
        if (z) {
            f = (float) (z2 ? 90 : -90);
        } else {
            f = 0.0f;
        }
        if (keyButtonDrawable.getRotation() != f) {
            if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
                keyButtonDrawable.setRotation(f);
                return;
            }
            if (!this.mOverviewProxyService.shouldShowSwipeUpUI() && !this.mIsVertical && z) {
                f2 = -getResources().getDimension(R$dimen.navbar_back_button_ime_offset);
            }
            ObjectAnimator ofPropertyValuesHolder = ObjectAnimator.ofPropertyValuesHolder(keyButtonDrawable, PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_ROTATE, f), PropertyValuesHolder.ofFloat(KeyButtonDrawable.KEY_DRAWABLE_TRANSLATE_Y, f2));
            ofPropertyValuesHolder.setInterpolator(Interpolators.FAST_OUT_SLOW_IN);
            ofPropertyValuesHolder.setDuration(200L);
            ofPropertyValuesHolder.start();
        }
    }

    private void orientHomeButton(KeyButtonDrawable keyButtonDrawable) {
        keyButtonDrawable.setRotation(this.mIsVertical ? 90.0f : 0.0f);
    }

    private int chooseNavigationIconDrawableRes(int i, int i2) {
        return this.mOverviewProxyService.shouldShowSwipeUpUI() ? i2 : i;
    }

    private KeyButtonDrawable getDrawable(int i) {
        return KeyButtonDrawable.create(this.mLightContext, this.mLightIconColor, this.mDarkIconColor, i, true, null);
    }

    public void onScreenStateChanged(boolean z) {
        this.mScreenOn = z;
        if (!z) {
            this.mRegionSamplingHelper.stop();
        } else if (Utils.isGesturalModeOnDefaultDisplay(getContext(), this.mNavBarMode)) {
            this.mRegionSamplingHelper.start(this.mSamplingBounds);
        }
    }

    public void setWindowVisible(boolean z) {
        this.mRegionSamplingHelper.setWindowVisible(z);
        this.mRotationButtonController.onNavigationBarWindowVisibilityChange(z);
    }

    public void setBehavior(int i) {
        this.mRotationButtonController.onBehaviorChanged(i);
    }

    @Override // android.view.View
    public void setLayoutDirection(int i) {
        reloadNavIcons();
        super.setLayoutDirection(i);
    }

    public void setNavigationIconHints(int i) {
        int i2 = this.mNavigationIconHints;
        if (i != i2) {
            boolean z = false;
            boolean z2 = (i & 1) != 0;
            if ((i2 & 1) != 0) {
                z = true;
            }
            if (z2 != z) {
                onImeVisibilityChanged(z2);
            }
            this.mNavigationIconHints = i;
            updateNavButtonIcons();
        }
    }

    private void onImeVisibilityChanged(boolean z) {
        if (!z) {
            this.mTransitionListener.onBackAltCleared();
        }
        this.mImeVisible = z;
        this.mRotationButtonController.getRotationButton().setCanShowRotationButton(!this.mImeVisible);
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.setCanShow(!this.mImeVisible);
        }
    }

    public void setDisabledFlags(int i) {
        if (this.mDisabledFlags != i) {
            boolean isOverviewEnabled = isOverviewEnabled();
            this.mDisabledFlags = i;
            if (!isOverviewEnabled && isOverviewEnabled()) {
                reloadNavIcons();
            }
            updateNavButtonIcons();
            updateSlippery();
            updateDisabledSystemUiStateFlags();
        }
    }

    public void updateNavButtonIcons() {
        LayoutTransition layoutTransition;
        int i = 0;
        boolean z = (this.mNavigationIconHints & 1) != 0;
        KeyButtonDrawable keyButtonDrawable = this.mBackIcon;
        orientBackButton(keyButtonDrawable);
        KeyButtonDrawable keyButtonDrawable2 = this.mHomeDefaultIcon;
        if (!this.mUseCarModeUi) {
            orientHomeButton(keyButtonDrawable2);
        }
        getHomeButton().setImageDrawable(keyButtonDrawable2);
        getBackButton().setImageDrawable(keyButtonDrawable);
        updateRecentsIcon();
        this.mContextualButtonGroup.setButtonVisibility(R$id.ime_switcher, (this.mNavigationIconHints & 2) != 0);
        this.mBarTransitions.reapplyDarkIntensity();
        boolean z2 = QuickStepContract.isGesturalMode(this.mNavBarMode) || (this.mDisabledFlags & 2097152) != 0;
        boolean isRecentsButtonDisabled = isRecentsButtonDisabled();
        boolean z3 = isRecentsButtonDisabled && (2097152 & this.mDisabledFlags) != 0;
        boolean z4 = !z && (this.mEdgeBackGestureHandler.isHandlingGestures() || (this.mDisabledFlags & 4194304) != 0);
        boolean isScreenPinningActive = ActivityManagerWrapper.getInstance().isScreenPinningActive();
        if (this.mOverviewProxyService.isEnabled()) {
            isRecentsButtonDisabled |= true ^ QuickStepContract.isLegacyMode(this.mNavBarMode);
            if (isScreenPinningActive && !QuickStepContract.isGesturalMode(this.mNavBarMode)) {
                z4 = false;
                z2 = false;
            }
        } else if (isScreenPinningActive) {
            z4 = false;
            isRecentsButtonDisabled = false;
        }
        ViewGroup viewGroup = (ViewGroup) getCurrentView().findViewById(R$id.nav_buttons);
        if (!(viewGroup == null || (layoutTransition = viewGroup.getLayoutTransition()) == null || layoutTransition.getTransitionListeners().contains(this.mTransitionListener))) {
            layoutTransition.addTransitionListener(this.mTransitionListener);
        }
        getBackButton().setVisibility(z4 ? 4 : 0);
        getHomeButton().setVisibility(z2 ? 4 : 0);
        getRecentsButton().setVisibility(isRecentsButtonDisabled ? 4 : 0);
        ButtonDispatcher homeHandle = getHomeHandle();
        if (z3) {
            i = 4;
        }
        homeHandle.setVisibility(i);
        notifyActiveTouchRegions();
    }

    @VisibleForTesting
    boolean isRecentsButtonDisabled() {
        return this.mUseCarModeUi || !isOverviewEnabled() || getContext().getDisplayId() != 0;
    }

    private Display getContextDisplay() {
        return getContext().getDisplay();
    }

    public void setLayoutTransitionsEnabled(boolean z) {
        this.mLayoutTransitionsEnabled = z;
        updateLayoutTransitionsEnabled();
    }

    public void setWakeAndUnlocking(boolean z) {
        setUseFadingAnimations(z);
        this.mWakeAndUnlocking = z;
        updateLayoutTransitionsEnabled();
    }

    private void updateLayoutTransitionsEnabled() {
        boolean z = !this.mWakeAndUnlocking && this.mLayoutTransitionsEnabled;
        LayoutTransition layoutTransition = ((ViewGroup) getCurrentView().findViewById(R$id.nav_buttons)).getLayoutTransition();
        if (layoutTransition == null) {
            return;
        }
        if (z) {
            layoutTransition.enableTransitionType(2);
            layoutTransition.enableTransitionType(3);
            layoutTransition.enableTransitionType(0);
            layoutTransition.enableTransitionType(1);
            return;
        }
        layoutTransition.disableTransitionType(2);
        layoutTransition.disableTransitionType(3);
        layoutTransition.disableTransitionType(0);
        layoutTransition.disableTransitionType(1);
    }

    private void setUseFadingAnimations(boolean z) {
        WindowManager.LayoutParams layoutParams = (WindowManager.LayoutParams) ((ViewGroup) getParent()).getLayoutParams();
        if (layoutParams != null) {
            boolean z2 = layoutParams.windowAnimations != 0;
            if (!z2 && z) {
                layoutParams.windowAnimations = R$style.Animation_NavigationBarFadeIn;
            } else if (z2 && !z) {
                layoutParams.windowAnimations = 0;
            } else {
                return;
            }
            ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout((View) getParent(), layoutParams);
        }
    }

    public void onStatusBarPanelStateChanged() {
        updateSlippery();
        updatePanelSystemUiStateFlags();
    }

    public void updateDisabledSystemUiStateFlags() {
        int displayId = ((FrameLayout) this).mContext.getDisplayId();
        boolean z = true;
        SysUiState flag = this.mSysUiFlagContainer.setFlag(1, ActivityManagerWrapper.getInstance().isScreenPinningActive()).setFlag(128, (this.mDisabledFlags & 16777216) != 0).setFlag(256, (this.mDisabledFlags & 2097152) != 0);
        if ((this.mDisabledFlags & 33554432) == 0) {
            z = false;
        }
        flag.setFlag(1024, z).commitUpdate(displayId);
    }

    public void updatePanelSystemUiStateFlags() {
        int displayId = ((FrameLayout) this).mContext.getDisplayId();
        NotificationPanelViewController notificationPanelViewController = this.mPanelView;
        if (notificationPanelViewController != null) {
            this.mSysUiFlagContainer.setFlag(4, notificationPanelViewController.isFullyExpanded() && !this.mPanelView.isInSettings()).setFlag(2048, this.mPanelView.isInSettings()).commitUpdate(displayId);
        }
    }

    public void updateStates() {
        boolean shouldShowSwipeUpUI = this.mOverviewProxyService.shouldShowSwipeUpUI();
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.onLikelyDefaultLayoutChange();
        }
        updateSlippery();
        reloadNavIcons();
        updateNavButtonIcons();
        WindowManagerWrapper.getInstance().setNavBarVirtualKeyHapticFeedbackEnabled(!shouldShowSwipeUpUI);
        getHomeButton().setAccessibilityDelegate(shouldShowSwipeUpUI ? this.mQuickStepAccessibilityDelegate : null);
    }

    public void updateSlippery() {
        NotificationPanelViewController notificationPanelViewController;
        setSlippery(!isQuickStepSwipeUpEnabled() || ((notificationPanelViewController = this.mPanelView) != null && notificationPanelViewController.isFullyExpanded() && !this.mPanelView.isCollapsing()));
    }

    private void setSlippery(boolean z) {
        setWindowFlag(536870912, z);
    }

    private void setWindowFlag(int i, boolean z) {
        WindowManager.LayoutParams layoutParams;
        ViewGroup viewGroup = (ViewGroup) getParent();
        if (viewGroup != null && (layoutParams = (WindowManager.LayoutParams) viewGroup.getLayoutParams()) != null) {
            int i2 = layoutParams.flags;
            if (z != ((i2 & i) != 0)) {
                if (z) {
                    layoutParams.flags = i | i2;
                } else {
                    layoutParams.flags = (~i) & i2;
                }
                ((WindowManager) getContext().getSystemService(WindowManager.class)).updateViewLayout(viewGroup, layoutParams);
            }
        }
    }

    @Override // com.android.systemui.navigationbar.NavigationModeController.ModeChangedListener
    public void onNavigationModeChanged(int i) {
        this.mNavBarMode = i;
        this.mBarTransitions.onNavigationModeChanged(i);
        this.mEdgeBackGestureHandler.onNavigationModeChanged(this.mNavBarMode);
        updateRotationButton();
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            this.mRegionSamplingHelper.start(this.mSamplingBounds);
        } else {
            this.mRegionSamplingHelper.stop();
        }
    }

    public void setAccessibilityButtonState(boolean z, boolean z2) {
        this.mLongClickableAccessibilityButton = z2;
        getAccessibilityButton().setLongClickable(z2);
        this.mContextualButtonGroup.setButtonVisibility(R$id.accessibility_button, z);
    }

    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        NavigationBarInflaterView navigationBarInflaterView = (NavigationBarInflaterView) findViewById(R$id.navigation_inflater);
        this.mNavigationInflaterView = navigationBarInflaterView;
        navigationBarInflaterView.setButtonDispatchers(this.mButtonDispatchers);
        updateOrientationViews();
        reloadNavIcons();
    }

    @Override // android.view.View
    protected void onDraw(Canvas canvas) {
        this.mDeadZone.onDraw(canvas);
        super.onDraw(canvas);
    }

    /* access modifiers changed from: private */
    public void updateSamplingRect() {
        this.mSamplingBounds.setEmpty();
        View currentView = getHomeHandle().getCurrentView();
        if (currentView != null) {
            int[] iArr = new int[2];
            currentView.getLocationOnScreen(iArr);
            Point point = new Point();
            currentView.getContext().getDisplay().getRealSize(point);
            this.mSamplingBounds.set(new Rect(iArr[0] - this.mNavColorSampleMargin, point.y - getNavBarHeight(), iArr[0] + currentView.getWidth() + this.mNavColorSampleMargin, point.y));
        }
    }

    /* access modifiers changed from: package-private */
    public void setOrientedHandleSamplingRegion(Rect rect) {
        this.mOrientedHandleSamplingRegion = rect;
        this.mRegionSamplingHelper.updateSamplingRect();
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        notifyActiveTouchRegions();
    }

    public void notifyActiveTouchRegions() {
        this.mOverviewProxyService.onActiveNavBarRegionChanges(getButtonLocations(true, true, true));
    }

    private void updateButtonTouchRegionCache() {
        FrameLayout frameLayout;
        if (this.mIsVertical) {
            frameLayout = this.mNavigationInflaterView.mVertical;
        } else {
            frameLayout = this.mNavigationInflaterView.mHorizontal;
        }
        this.mButtonFullTouchableRegions = ((NearestTouchFrame) frameLayout.findViewById(R$id.nav_buttons)).getFullTouchableChildRegions();
    }

    private Region getButtonLocations(boolean z, boolean z2, boolean z3) {
        if (z3 && !z2) {
            z3 = false;
        }
        this.mTmpRegion.setEmpty();
        updateButtonTouchRegionCache();
        updateButtonLocation(getBackButton(), z2, z3);
        updateButtonLocation(getHomeButton(), z2, z3);
        updateButtonLocation(getRecentsButton(), z2, z3);
        updateButtonLocation(getImeSwitchButton(), z2, z3);
        updateButtonLocation(getAccessibilityButton(), z2, z3);
        if (!z || !this.mFloatingRotationButton.isVisible()) {
            updateButtonLocation(getRotateSuggestionButton(), z2, z3);
        } else {
            updateButtonLocation(this.mFloatingRotationButton.getCurrentView(), z2);
        }
        if (z && this.mNavBarOverlayController.isNavigationBarOverlayEnabled() && this.mNavBarOverlayController.isVisible()) {
            updateButtonLocation(this.mNavBarOverlayController.getCurrentView(), z2);
        }
        return this.mTmpRegion;
    }

    private void updateButtonLocation(ButtonDispatcher buttonDispatcher, boolean z, boolean z2) {
        View currentView;
        if (buttonDispatcher != null && (currentView = buttonDispatcher.getCurrentView()) != null && buttonDispatcher.isVisible()) {
            if (!z2 || !this.mButtonFullTouchableRegions.containsKey(currentView)) {
                updateButtonLocation(currentView, z);
            } else {
                this.mTmpRegion.op(this.mButtonFullTouchableRegions.get(currentView), Region.Op.UNION);
            }
        }
    }

    private void updateButtonLocation(View view, boolean z) {
        if (z) {
            view.getBoundsOnScreen(this.mTmpBounds);
        } else {
            view.getLocationInWindow(this.mTmpPosition);
            Rect rect = this.mTmpBounds;
            int[] iArr = this.mTmpPosition;
            rect.set(iArr[0], iArr[1], iArr[0] + view.getWidth(), this.mTmpPosition[1] + view.getHeight());
        }
        this.mTmpRegion.op(this.mTmpBounds, Region.Op.UNION);
    }

    private void updateOrientationViews() {
        this.mHorizontal = findViewById(R$id.horizontal);
        this.mVertical = findViewById(R$id.vertical);
        updateCurrentView();
    }

    /* access modifiers changed from: package-private */
    public boolean needsReorient(int i) {
        return this.mCurrentRotation != i;
    }

    private void updateCurrentView() {
        resetViews();
        View view = this.mIsVertical ? this.mVertical : this.mHorizontal;
        this.mCurrentView = view;
        boolean z = false;
        view.setVisibility(0);
        this.mNavigationInflaterView.setVertical(this.mIsVertical);
        int rotation = getContextDisplay().getRotation();
        this.mCurrentRotation = rotation;
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (rotation == 1) {
            z = true;
        }
        navigationBarInflaterView.setAlternativeOrder(z);
        this.mNavigationInflaterView.updateButtonDispatchersCurrentView();
        updateLayoutTransitionsEnabled();
    }

    private void resetViews() {
        this.mHorizontal.setVisibility(8);
        this.mVertical.setVisibility(8);
    }

    private void updateRecentsIcon() {
        this.mDockedIcon.setRotation((!this.mDockedStackExists || !this.mIsVertical) ? 0.0f : 90.0f);
        getRecentsButton().setImageDrawable(this.mDockedStackExists ? this.mDockedIcon : this.mRecentIcon);
        this.mBarTransitions.reapplyDarkIntensity();
    }

    public void showPinningEnterExitToast(boolean z) {
        if (z) {
            this.mScreenPinningNotify.showPinningStartToast();
        } else {
            this.mScreenPinningNotify.showPinningExitToast();
        }
    }

    public void showPinningEscapeToast() {
        this.mScreenPinningNotify.showEscapeToast(this.mNavBarMode == 2, isRecentsButtonVisible());
    }

    public void reorient() {
        updateCurrentView();
        ((NavigationBarFrame) getRootView()).setDeadZone(this.mDeadZone);
        this.mDeadZone.onConfigurationChanged(this.mCurrentRotation);
        this.mBarTransitions.init();
        if (!isLayoutDirectionResolved()) {
            resolveLayoutDirection();
        }
        updateNavButtonIcons();
        getHomeButton().setVertical(this.mIsVertical);
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        int i3;
        int size = View.MeasureSpec.getSize(i);
        int size2 = View.MeasureSpec.getSize(i2);
        boolean z = size > 0 && size2 > size && !QuickStepContract.isGesturalMode(this.mNavBarMode);
        if (z != this.mIsVertical) {
            this.mIsVertical = z;
            reorient();
            notifyVerticalChangedListener(z);
        }
        if (QuickStepContract.isGesturalMode(this.mNavBarMode)) {
            if (this.mIsVertical) {
                i3 = getResources().getDimensionPixelSize(17105356);
            } else {
                i3 = getResources().getDimensionPixelSize(17105354);
            }
            this.mBarTransitions.setBackgroundFrame(new Rect(0, getResources().getDimensionPixelSize(17105350) - i3, size, size2));
        } else {
            this.mBarTransitions.setBackgroundFrame(null);
        }
        super.onMeasure(i, i2);
    }

    private int getNavBarHeight() {
        if (this.mIsVertical) {
            return getResources().getDimensionPixelSize(17105356);
        }
        return getResources().getDimensionPixelSize(17105354);
    }

    private void notifyVerticalChangedListener(boolean z) {
        OnVerticalChangedListener onVerticalChangedListener = this.mOnVerticalChangedListener;
        if (onVerticalChangedListener != null) {
            onVerticalChangedListener.onVerticalChanged(z);
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        this.mTmpLastConfiguration.updateFrom(this.mConfiguration);
        this.mConfiguration.updateFrom(configuration);
        boolean updateCarMode = updateCarMode();
        updateIcons(this.mTmpLastConfiguration);
        updateRecentsIcon();
        this.mEdgeBackGestureHandler.onConfigurationChanged(this.mConfiguration);
        if (!updateCarMode) {
            Configuration configuration2 = this.mTmpLastConfiguration;
            if (configuration2.densityDpi == this.mConfiguration.densityDpi && configuration2.getLayoutDirection() == this.mConfiguration.getLayoutDirection()) {
                return;
            }
        }
        updateNavButtonIcons();
    }

    private boolean updateCarMode() {
        Configuration configuration = this.mConfiguration;
        if (configuration != null) {
            boolean z = (configuration.uiMode & 15) == 3;
            if (z != this.mInCarMode) {
                this.mInCarMode = z;
                this.mUseCarModeUi = false;
            }
        }
        return false;
    }

    private String getResourceName(int i) {
        if (i == 0) {
            return "(null)";
        }
        try {
            return getContext().getResources().getResourceName(i);
        } catch (Resources.NotFoundException unused) {
            return "(unknown)";
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        this.mEdgeBackGestureHandler.onNavBarAttached();
        requestApplyInsets();
        reorient();
        onNavigationModeChanged(this.mNavBarMode);
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        if (rotationButtonController != null) {
            rotationButtonController.registerListeners();
        }
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.registerListeners();
        }
        getViewTreeObserver().addOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
        updateNavButtonIcons();
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((NavigationModeController) Dependency.get(NavigationModeController.class)).removeListener(this);
        for (int i = 0; i < this.mButtonDispatchers.size(); i++) {
            this.mButtonDispatchers.valueAt(i).onDestroy();
        }
        RotationButtonController rotationButtonController = this.mRotationButtonController;
        if (rotationButtonController != null) {
            rotationButtonController.unregisterListeners();
        }
        if (this.mNavBarOverlayController.isNavigationBarOverlayEnabled()) {
            this.mNavBarOverlayController.unregisterListeners();
        }
        this.mEdgeBackGestureHandler.onNavBarDetached();
        getViewTreeObserver().removeOnComputeInternalInsetsListener(this.mOnComputeInternalInsetsListener);
    }

    public void dump(PrintWriter printWriter) {
        Rect rect = new Rect();
        Point point = new Point();
        getContextDisplay().getRealSize(point);
        printWriter.println("NavigationBarView:");
        printWriter.println(String.format("      this: " + StatusBar.viewInfo(this) + " " + visibilityToString(getVisibility()), new Object[0]));
        getWindowVisibleDisplayFrame(rect);
        boolean z = rect.right > point.x || rect.bottom > point.y;
        StringBuilder sb = new StringBuilder();
        sb.append("      window: ");
        sb.append(rect.toShortString());
        sb.append(" ");
        sb.append(visibilityToString(getWindowVisibility()));
        sb.append(z ? " OFFSCREEN!" : "");
        printWriter.println(sb.toString());
        printWriter.println(String.format("      mCurrentView: id=%s (%dx%d) %s %f", getResourceName(getCurrentView().getId()), Integer.valueOf(getCurrentView().getWidth()), Integer.valueOf(getCurrentView().getHeight()), visibilityToString(getCurrentView().getVisibility()), Float.valueOf(getCurrentView().getAlpha())));
        Object[] objArr = new Object[3];
        objArr[0] = Integer.valueOf(this.mDisabledFlags);
        objArr[1] = this.mIsVertical ? "true" : "false";
        objArr[2] = Float.valueOf(getLightTransitionsController().getCurrentDarkIntensity());
        printWriter.println(String.format("      disabled=0x%08x vertical=%s darkIntensity=%.2f", objArr));
        printWriter.println("      mOrientedHandleSamplingRegion: " + this.mOrientedHandleSamplingRegion);
        printWriter.println("    mScreenOn: " + this.mScreenOn);
        dumpButton(printWriter, "back", getBackButton());
        dumpButton(printWriter, "home", getHomeButton());
        dumpButton(printWriter, "rcnt", getRecentsButton());
        dumpButton(printWriter, "rota", getRotateSuggestionButton());
        dumpButton(printWriter, "a11y", getAccessibilityButton());
        dumpButton(printWriter, "ime", getImeSwitchButton());
        NavigationBarInflaterView navigationBarInflaterView = this.mNavigationInflaterView;
        if (navigationBarInflaterView != null) {
            navigationBarInflaterView.dump(printWriter);
        }
        this.mBarTransitions.dump(printWriter);
        this.mContextualButtonGroup.dump(printWriter);
        this.mRegionSamplingHelper.dump(printWriter);
        this.mEdgeBackGestureHandler.dump(printWriter);
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        int systemWindowInsetLeft = windowInsets.getSystemWindowInsetLeft();
        int systemWindowInsetRight = windowInsets.getSystemWindowInsetRight();
        setPadding(systemWindowInsetLeft, windowInsets.getSystemWindowInsetTop(), systemWindowInsetRight, windowInsets.getSystemWindowInsetBottom());
        this.mEdgeBackGestureHandler.setInsets(systemWindowInsetLeft, systemWindowInsetRight);
        boolean z = !QuickStepContract.isGesturalMode(this.mNavBarMode) || windowInsets.getSystemWindowInsetBottom() == 0;
        setClipChildren(z);
        setClipToPadding(z);
        return super.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: package-private */
    public void registerDockedListener(LegacySplitScreen legacySplitScreen) {
        legacySplitScreen.registerInSplitScreenListener(this.mDockedListener);
    }

    /* access modifiers changed from: package-private */
    public void registerPipExclusionBoundsChangeListener(Pip pip) {
        pip.setPipExclusionBoundsChangeListener(this.mPipListener);
    }

    private static void dumpButton(PrintWriter printWriter, String str, ButtonDispatcher buttonDispatcher) {
        printWriter.print("      " + str + ": ");
        if (buttonDispatcher == null) {
            printWriter.print("null");
        } else {
            printWriter.print(visibilityToString(buttonDispatcher.getVisibility()) + " alpha=" + buttonDispatcher.getAlpha());
        }
        printWriter.println();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$4(Boolean bool) {
        post(new Runnable(bool) { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda3
            public final /* synthetic */ Boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NavigationBarView.this.lambda$new$3(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(Boolean bool) {
        this.mDockedStackExists = bool.booleanValue();
        updateRecentsIcon();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$6(Rect rect) {
        post(new Runnable(rect) { // from class: com.android.systemui.navigationbar.NavigationBarView$$ExternalSyntheticLambda2
            public final /* synthetic */ Rect f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NavigationBarView.this.lambda$new$5(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$5(Rect rect) {
        this.mEdgeBackGestureHandler.setPipStashExclusionBounds(rect);
    }

    /* access modifiers changed from: package-private */
    public void setNavigationBarLumaSamplingEnabled(boolean z) {
        if (z) {
            this.mRegionSamplingHelper.start(this.mSamplingBounds);
        } else {
            this.mRegionSamplingHelper.stop();
        }
    }
}
