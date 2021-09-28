package com.android.systemui.statusbar.phone;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.WindowInsets;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.BatteryMeterView;
import com.android.systemui.DejankUtils;
import com.android.systemui.Dependency;
import com.android.systemui.R$attr;
import com.android.systemui.R$bool;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.ScreenDecorations;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.events.SystemStatusAnimationCallback;
import com.android.systemui.statusbar.events.SystemStatusAnimationScheduler;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.policy.BatteryController;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.statusbar.policy.UserInfoController;
import com.android.systemui.statusbar.policy.UserInfoControllerImpl;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class KeyguardStatusBarView extends RelativeLayout implements BatteryController.BatteryStateChangeCallback, UserInfoController.OnUserInfoChangedListener, ConfigurationController.ConfigurationListener, SystemStatusAnimationCallback {
    private SystemStatusAnimationScheduler mAnimationScheduler;
    private boolean mBatteryCharging;
    private BatteryController mBatteryController;
    private boolean mBatteryListening;
    private BatteryMeterView mBatteryView;
    private TextView mCarrierLabel;
    private View mCutoutSpace;
    private DisplayCutout mDisplayCutout;
    private FeatureFlags mFeatureFlags;
    private StatusBarIconController.TintedIconManager mIconManager;
    private boolean mKeyguardUserSwitcherEnabled;
    private ImageView mMultiUserAvatar;
    private boolean mShowPercentAvailable;
    private ViewGroup mStatusIconArea;
    private StatusIconContainer mStatusIconContainer;
    private int mSystemIconsBaseMargin;
    private View mSystemIconsContainer;
    private int mSystemIconsSwitcherHiddenExpandedMargin;
    private int mTopClipping;
    private final Rect mEmptyRect = new Rect(0, 0, 0, 0);
    private List<String> mBlockedIcons = new ArrayList();
    private int mLayoutState = 0;
    private int mCutoutSideNudge = 0;
    private int mRoundedCornerPadding = 0;
    private Pair<Integer, Integer> mPadding = new Pair<>(0, 0);
    private final Rect mClipRect = new Rect(0, 0, 0, 0);
    private final UserManager mUserManager = UserManager.get(getContext());

    private int calculateMargin(int i, int i2) {
        if (i2 >= i) {
            return 0;
        }
        return i - i2;
    }

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onPowerSaveChanged(boolean z) {
    }

    public KeyguardStatusBarView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mSystemIconsContainer = findViewById(R$id.system_icons_container);
        this.mMultiUserAvatar = (ImageView) findViewById(R$id.multi_user_avatar);
        this.mCarrierLabel = (TextView) findViewById(R$id.keyguard_carrier_text);
        this.mBatteryView = (BatteryMeterView) this.mSystemIconsContainer.findViewById(R$id.battery);
        this.mCutoutSpace = findViewById(R$id.cutout_space_view);
        this.mStatusIconArea = (ViewGroup) findViewById(R$id.status_icon_area);
        this.mStatusIconContainer = (StatusIconContainer) findViewById(R$id.statusIcons);
        loadDimens();
        loadBlockList();
        this.mBatteryController = (BatteryController) Dependency.get(BatteryController.class);
        this.mAnimationScheduler = (SystemStatusAnimationScheduler) Dependency.get(SystemStatusAnimationScheduler.class);
        this.mFeatureFlags = (FeatureFlags) Dependency.get(FeatureFlags.class);
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mMultiUserAvatar.getLayoutParams();
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.multi_user_avatar_keyguard_size);
        marginLayoutParams.height = dimensionPixelSize;
        marginLayoutParams.width = dimensionPixelSize;
        this.mMultiUserAvatar.setLayoutParams(marginLayoutParams);
        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) this.mSystemIconsContainer.getLayoutParams();
        marginLayoutParams2.setMarginStart(getResources().getDimensionPixelSize(R$dimen.system_icons_super_container_margin_start));
        this.mSystemIconsContainer.setLayoutParams(marginLayoutParams2);
        View view = this.mSystemIconsContainer;
        view.setPaddingRelative(view.getPaddingStart(), this.mSystemIconsContainer.getPaddingTop(), getResources().getDimensionPixelSize(R$dimen.system_icons_keyguard_padding_end), this.mSystemIconsContainer.getPaddingBottom());
        this.mCarrierLabel.setTextSize(0, (float) getResources().getDimensionPixelSize(17105553));
        ViewGroup.MarginLayoutParams marginLayoutParams3 = (ViewGroup.MarginLayoutParams) this.mCarrierLabel.getLayoutParams();
        marginLayoutParams3.setMarginStart(calculateMargin(getResources().getDimensionPixelSize(R$dimen.keyguard_carrier_text_margin), ((Integer) this.mPadding.first).intValue()));
        this.mCarrierLabel.setLayoutParams(marginLayoutParams3);
        updateKeyguardStatusBarHeight();
    }

    private void updateKeyguardStatusBarHeight() {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) getLayoutParams();
        marginLayoutParams.height = getResources().getDimensionPixelSize(R$dimen.status_bar_header_height_keyguard) + i;
        setLayoutParams(marginLayoutParams);
    }

    private void loadDimens() {
        Resources resources = getResources();
        this.mSystemIconsSwitcherHiddenExpandedMargin = resources.getDimensionPixelSize(R$dimen.system_icons_switcher_hidden_expanded_margin);
        this.mSystemIconsBaseMargin = resources.getDimensionPixelSize(R$dimen.system_icons_super_container_avatarless_margin_end);
        this.mCutoutSideNudge = getResources().getDimensionPixelSize(R$dimen.display_cutout_margin_consumption);
        this.mShowPercentAvailable = getContext().getResources().getBoolean(17891381);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
    }

    private void loadBlockList() {
        Resources resources = getResources();
        this.mBlockedIcons.add(resources.getString(17041458));
        this.mBlockedIcons.add(resources.getString(17041427));
        this.mBlockedIcons.add(resources.getString(17041430));
    }

    private void updateVisibilities() {
        boolean z = false;
        if (this.mMultiUserAvatar.getParent() == this.mStatusIconArea || this.mKeyguardUserSwitcherEnabled) {
            ViewParent parent = this.mMultiUserAvatar.getParent();
            ViewGroup viewGroup = this.mStatusIconArea;
            if (parent == viewGroup && this.mKeyguardUserSwitcherEnabled) {
                viewGroup.removeView(this.mMultiUserAvatar);
            }
        } else {
            if (this.mMultiUserAvatar.getParent() != null) {
                getOverlay().remove(this.mMultiUserAvatar);
            }
            this.mStatusIconArea.addView(this.mMultiUserAvatar, 0);
        }
        if (!this.mKeyguardUserSwitcherEnabled) {
            if (((Boolean) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.android.systemui.statusbar.phone.KeyguardStatusBarView$$ExternalSyntheticLambda0
                @Override // java.util.function.Supplier
                public final Object get() {
                    return KeyguardStatusBarView.this.lambda$updateVisibilities$0();
                }
            })).booleanValue()) {
                this.mMultiUserAvatar.setVisibility(0);
            } else {
                this.mMultiUserAvatar.setVisibility(8);
            }
        }
        BatteryMeterView batteryMeterView = this.mBatteryView;
        if (this.mBatteryCharging && this.mShowPercentAvailable) {
            z = true;
        }
        batteryMeterView.setForceShowPercent(z);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Boolean lambda$updateVisibilities$0() {
        return Boolean.valueOf(this.mUserManager.isUserSwitcherEnabled(((RelativeLayout) this).mContext.getResources().getBoolean(R$bool.qs_show_user_switcher_for_single_user)));
    }

    private void updateSystemIconsLayoutParams() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams();
        int i = this.mMultiUserAvatar.getVisibility() == 8 ? this.mSystemIconsBaseMargin : 0;
        if (this.mKeyguardUserSwitcherEnabled) {
            i = this.mSystemIconsSwitcherHiddenExpandedMargin;
        }
        int calculateMargin = calculateMargin(i, ((Integer) this.mPadding.second).intValue());
        if (calculateMargin != layoutParams.getMarginEnd()) {
            layoutParams.setMarginEnd(calculateMargin);
            this.mSystemIconsContainer.setLayoutParams(layoutParams);
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        this.mLayoutState = 0;
        if (updateLayoutConsideringCutout()) {
            requestLayout();
        }
        return super.onApplyWindowInsets(windowInsets);
    }

    private boolean updateLayoutConsideringCutout() {
        this.mDisplayCutout = getRootWindowInsets().getDisplayCutout();
        updateKeyguardStatusBarHeight();
        Pair<Integer, Integer> cornerCutoutMargins = StatusBarWindowView.cornerCutoutMargins(this.mDisplayCutout, getDisplay());
        updatePadding(cornerCutoutMargins);
        if (this.mDisplayCutout == null || cornerCutoutMargins != null) {
            return updateLayoutParamsNoCutout();
        }
        return updateLayoutParamsForCutout();
    }

    private void updatePadding(Pair<Integer, Integer> pair) {
        DisplayCutout displayCutout = this.mDisplayCutout;
        int i = displayCutout == null ? 0 : displayCutout.getWaterfallInsets().top;
        Pair<Integer, Integer> paddingNeededForCutoutAndRoundedCorner = StatusBarWindowView.paddingNeededForCutoutAndRoundedCorner(this.mDisplayCutout, pair, this.mRoundedCornerPadding);
        this.mPadding = paddingNeededForCutoutAndRoundedCorner;
        setPadding(((Integer) paddingNeededForCutoutAndRoundedCorner.first).intValue(), i, ((Integer) this.mPadding.second).intValue(), 0);
    }

    private boolean updateLayoutParamsNoCutout() {
        if (this.mLayoutState == 2) {
            return false;
        }
        this.mLayoutState = 2;
        View view = this.mCutoutSpace;
        if (view != null) {
            view.setVisibility(8);
        }
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, R$id.status_icon_area);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams.removeRule(1);
        layoutParams.width = -2;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(getResources().getDimensionPixelSize(R$dimen.system_icons_super_container_margin_start));
        return true;
    }

    private boolean updateLayoutParamsForCutout() {
        if (this.mLayoutState == 1) {
            return false;
        }
        this.mLayoutState = 1;
        if (this.mCutoutSpace == null) {
            updateLayoutParamsNoCutout();
        }
        Rect rect = new Rect();
        ScreenDecorations.DisplayCutoutView.boundsFromDirection(this.mDisplayCutout, 48, rect);
        this.mCutoutSpace.setVisibility(0);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) this.mCutoutSpace.getLayoutParams();
        int i = rect.left;
        int i2 = this.mCutoutSideNudge;
        rect.left = i + i2;
        rect.right -= i2;
        layoutParams.width = rect.width();
        layoutParams.height = rect.height();
        layoutParams.addRule(13);
        int i3 = R$id.cutout_space_view;
        ((RelativeLayout.LayoutParams) this.mCarrierLabel.getLayoutParams()).addRule(16, i3);
        RelativeLayout.LayoutParams layoutParams2 = (RelativeLayout.LayoutParams) this.mStatusIconArea.getLayoutParams();
        layoutParams2.addRule(1, i3);
        layoutParams2.width = -1;
        ((LinearLayout.LayoutParams) this.mSystemIconsContainer.getLayoutParams()).setMarginStart(0);
        return true;
    }

    public void setListening(boolean z) {
        if (z != this.mBatteryListening) {
            this.mBatteryListening = z;
            if (z) {
                this.mBatteryController.addCallback(this);
            } else {
                this.mBatteryController.removeCallback(this);
            }
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        UserInfoController userInfoController = (UserInfoController) Dependency.get(UserInfoController.class);
        userInfoController.addCallback(this);
        userInfoController.reloadUserInfo();
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).addCallback(this);
        StatusBarIconController.TintedIconManager tintedIconManager = new StatusBarIconController.TintedIconManager((ViewGroup) findViewById(R$id.statusIcons), this.mFeatureFlags);
        this.mIconManager = tintedIconManager;
        tintedIconManager.setBlockList(this.mBlockedIcons);
        ((StatusBarIconController) Dependency.get(StatusBarIconController.class)).addIconGroup(this.mIconManager);
        this.mAnimationScheduler.addCallback((SystemStatusAnimationCallback) this);
        onThemeChanged();
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        ((UserInfoController) Dependency.get(UserInfoController.class)).removeCallback(this);
        ((StatusBarIconController) Dependency.get(StatusBarIconController.class)).removeIconGroup(this.mIconManager);
        ((ConfigurationController) Dependency.get(ConfigurationController.class)).removeCallback(this);
        this.mAnimationScheduler.removeCallback((SystemStatusAnimationCallback) this);
    }

    @Override // com.android.systemui.statusbar.policy.UserInfoController.OnUserInfoChangedListener
    public void onUserInfoChanged(String str, Drawable drawable, String str2) {
        this.mMultiUserAvatar.setImageDrawable(drawable);
    }

    @Override // com.android.systemui.statusbar.policy.BatteryController.BatteryStateChangeCallback
    public void onBatteryLevelChanged(int i, boolean z, boolean z2) {
        if (this.mBatteryCharging != z2) {
            this.mBatteryCharging = z2;
            updateVisibilities();
        }
    }

    public void setKeyguardUserSwitcherEnabled(boolean z) {
        this.mKeyguardUserSwitcherEnabled = z;
    }

    @Override // android.view.View
    public void setVisibility(int i) {
        super.setVisibility(i);
        if (i != 0) {
            this.mSystemIconsContainer.animate().cancel();
            this.mSystemIconsContainer.setTranslationX(0.0f);
            this.mMultiUserAvatar.animate().cancel();
            this.mMultiUserAvatar.setAlpha(1.0f);
            return;
        }
        updateVisibilities();
        updateSystemIconsLayoutParams();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onThemeChanged() {
        this.mBatteryView.setColorsFromContext(((RelativeLayout) this).mContext);
        updateIconsAndTextColors();
        ((UserInfoControllerImpl) Dependency.get(UserInfoController.class)).onDensityOrFontScaleChanged();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        loadDimens();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onOverlayChanged() {
        this.mCarrierLabel.setTextAppearance(Utils.getThemeAttr(((RelativeLayout) this).mContext, 16842818));
        onThemeChanged();
        this.mBatteryView.updatePercentView();
    }

    private void updateIconsAndTextColors() {
        int i;
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(((RelativeLayout) this).mContext, R$attr.wallpaperTextColor);
        Context context = ((RelativeLayout) this).mContext;
        if (((double) Color.luminance(colorAttrDefaultColor)) < 0.5d) {
            i = R$color.dark_mode_icon_color_single_tone;
        } else {
            i = R$color.light_mode_icon_color_single_tone;
        }
        int colorStateListDefaultColor = Utils.getColorStateListDefaultColor(context, i);
        float f = colorAttrDefaultColor == -1 ? 0.0f : 1.0f;
        this.mCarrierLabel.setTextColor(colorStateListDefaultColor);
        StatusBarIconController.TintedIconManager tintedIconManager = this.mIconManager;
        if (tintedIconManager != null) {
            tintedIconManager.setTint(colorStateListDefaultColor);
        }
        applyDarkness(R$id.battery, this.mEmptyRect, f, colorStateListDefaultColor);
        applyDarkness(R$id.clock, this.mEmptyRect, f, colorStateListDefaultColor);
    }

    private void applyDarkness(int i, Rect rect, float f, int i2) {
        View findViewById = findViewById(i);
        if (findViewById instanceof DarkIconDispatcher.DarkReceiver) {
            ((DarkIconDispatcher.DarkReceiver) findViewById).onDarkChanged(rect, f, i2);
        }
    }

    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("KeyguardStatusBarView:");
        printWriter.println("  mBatteryCharging: " + this.mBatteryCharging);
        printWriter.println("  mBatteryListening: " + this.mBatteryListening);
        printWriter.println("  mLayoutState: " + this.mLayoutState);
        printWriter.println("  mKeyguardUserSwitcherEnabled: " + this.mKeyguardUserSwitcherEnabled);
        BatteryMeterView batteryMeterView = this.mBatteryView;
        if (batteryMeterView != null) {
            batteryMeterView.dump(fileDescriptor, printWriter, strArr);
        }
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public void onSystemChromeAnimationStart() {
        if (this.mAnimationScheduler.getAnimationState() == 3) {
            this.mSystemIconsContainer.setVisibility(0);
            this.mSystemIconsContainer.setAlpha(0.0f);
        }
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public void onSystemChromeAnimationEnd() {
        if (this.mAnimationScheduler.getAnimationState() == 1) {
            this.mSystemIconsContainer.setVisibility(4);
            this.mSystemIconsContainer.setAlpha(0.0f);
            return;
        }
        this.mSystemIconsContainer.setAlpha(1.0f);
        this.mSystemIconsContainer.setVisibility(0);
    }

    @Override // com.android.systemui.statusbar.events.SystemStatusAnimationCallback
    public void onSystemChromeAnimationUpdate(ValueAnimator valueAnimator) {
        this.mSystemIconsContainer.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
    }

    @Override // android.widget.RelativeLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        updateClipping();
    }

    public void setTopClipping(int i) {
        if (i != this.mTopClipping) {
            this.mTopClipping = i;
            updateClipping();
        }
    }

    private void updateClipping() {
        this.mClipRect.set(0, this.mTopClipping, getWidth(), getHeight());
        setClipBounds(this.mClipRect);
    }
}
