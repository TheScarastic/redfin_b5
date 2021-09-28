package com.android.systemui.qs;

import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.DisplayCutout;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowInsets;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import com.android.settingslib.Utils;
import com.android.systemui.BatteryMeterView;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.qs.QSDetail;
import com.android.systemui.qs.TouchAnimator;
import com.android.systemui.statusbar.phone.StatusBarIconController;
import com.android.systemui.statusbar.phone.StatusBarWindowView;
import com.android.systemui.statusbar.phone.StatusIconContainer;
import com.android.systemui.statusbar.policy.Clock;
import java.util.List;
/* loaded from: classes.dex */
public class QuickStatusBarHeader extends FrameLayout {
    private TouchAnimator mAlphaAnimator;
    private BatteryMeterView mBatteryRemainingIcon;
    private View mClockIconsSeparator;
    private View mClockIconsView;
    private Clock mClockView;
    private boolean mConfigShowBatteryEstimate;
    private View mContainer;
    private int mCutOutPaddingLeft;
    private int mCutOutPaddingRight;
    private View mDateContainer;
    private Space mDatePrivacySeparator;
    private View mDatePrivacyView;
    private View mDateView;
    private boolean mExpanded;
    private boolean mHasCenterCutout;
    protected QuickQSPanel mHeaderQsPanel;
    private StatusIconContainer mIconContainer;
    private TouchAnimator mIconsAlphaAnimator;
    private TouchAnimator mIconsAlphaAnimatorFixed;
    private boolean mIsSingleCarrier;
    private float mKeyguardExpansionFraction;
    private View mPrivacyChip;
    private View mPrivacyContainer;
    private View mQSCarriers;
    private QSExpansionPathInterpolator mQSExpansionPathInterpolator;
    private boolean mQsDisabled;
    private View mRightLayout;
    private List<String> mRssiIgnoredSlots;
    private View mSecurityHeaderView;
    private boolean mShowClockIconsSeparator;
    private StatusBarIconController.TintedIconManager mTintedIconManager;
    private int mTopViewMeasureHeight;
    private TouchAnimator mTranslationAnimator;
    private int mWaterfallTopInset;
    private int mRoundedCornerPadding = 0;
    private float mViewAlpha = 1.0f;
    private int mTextColorPrimary = 0;

    public QuickStatusBarHeader(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public int getOffsetTranslation() {
        return this.mTopViewMeasureHeight;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mHeaderQsPanel = (QuickQSPanel) findViewById(R$id.quick_qs_panel);
        this.mDatePrivacyView = findViewById(R$id.quick_status_bar_date_privacy);
        this.mClockIconsView = findViewById(R$id.quick_qs_status_icons);
        this.mQSCarriers = findViewById(R$id.carrier_group);
        this.mContainer = findViewById(R$id.qs_container);
        this.mIconContainer = (StatusIconContainer) findViewById(R$id.statusIcons);
        this.mPrivacyChip = findViewById(R$id.privacy_chip);
        this.mDateView = findViewById(R$id.date);
        this.mSecurityHeaderView = findViewById(R$id.header_text_container);
        this.mClockIconsSeparator = findViewById(R$id.separator);
        this.mRightLayout = findViewById(R$id.rightLayout);
        this.mDateContainer = findViewById(R$id.date_container);
        this.mPrivacyContainer = findViewById(R$id.privacy_container);
        this.mClockView = (Clock) findViewById(R$id.clock);
        this.mDatePrivacySeparator = (Space) findViewById(R$id.space);
        this.mBatteryRemainingIcon = (BatteryMeterView) findViewById(R$id.batteryRemainingIcon);
        updateResources();
        this.mBatteryRemainingIcon.setIgnoreTunerUpdates(true);
        this.mBatteryRemainingIcon.setPercentShowMode(3);
        this.mIconsAlphaAnimatorFixed = new TouchAnimator.Builder().addFloat(this.mIconContainer, "alpha", 0.0f, 1.0f).addFloat(this.mBatteryRemainingIcon, "alpha", 0.0f, 1.0f).build();
    }

    /* access modifiers changed from: package-private */
    public void onAttach(StatusBarIconController.TintedIconManager tintedIconManager, QSExpansionPathInterpolator qSExpansionPathInterpolator, List<String> list) {
        this.mTintedIconManager = tintedIconManager;
        this.mRssiIgnoredSlots = list;
        tintedIconManager.setTint(Utils.getColorAttrDefaultColor(getContext(), 16842806));
        this.mQSExpansionPathInterpolator = qSExpansionPathInterpolator;
        updateAnimators();
    }

    /* access modifiers changed from: package-private */
    public void setIsSingleCarrier(boolean z) {
        this.mIsSingleCarrier = z;
        updateAlphaAnimator();
    }

    @Override // android.widget.FrameLayout, android.view.View
    protected void onMeasure(int i, int i2) {
        super.onMeasure(i, i2);
        if (this.mDatePrivacyView.getMeasuredHeight() != this.mTopViewMeasureHeight) {
            this.mTopViewMeasureHeight = this.mDatePrivacyView.getMeasuredHeight();
            updateAnimators();
        }
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
        setDatePrivacyContainersWidth(configuration.orientation == 2);
    }

    @Override // android.view.View
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        updateResources();
    }

    private void setDatePrivacyContainersWidth(boolean z) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDateContainer.getLayoutParams();
        int i = -2;
        layoutParams.width = z ? -2 : 0;
        float f = 0.0f;
        layoutParams.weight = z ? 0.0f : 1.0f;
        this.mDateContainer.setLayoutParams(layoutParams);
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mPrivacyContainer.getLayoutParams();
        if (!z) {
            i = 0;
        }
        layoutParams2.width = i;
        if (!z) {
            f = 1.0f;
        }
        layoutParams2.weight = f;
        this.mPrivacyContainer.setLayoutParams(layoutParams2);
    }

    private void updateBatteryMode() {
        if (!this.mConfigShowBatteryEstimate || this.mHasCenterCutout) {
            this.mBatteryRemainingIcon.setPercentShowMode(1);
        } else {
            this.mBatteryRemainingIcon.setPercentShowMode(3);
        }
    }

    /* access modifiers changed from: package-private */
    public void updateResources() {
        Resources resources = ((FrameLayout) this).mContext.getResources();
        this.mConfigShowBatteryEstimate = resources.getBoolean(R$bool.config_showBatteryEstimateQSBH);
        this.mRoundedCornerPadding = resources.getDimensionPixelSize(R$dimen.rounded_corner_content_padding);
        int dimensionPixelSize = resources.getDimensionPixelSize(17105475);
        this.mDatePrivacyView.getLayoutParams().height = Math.max(dimensionPixelSize, this.mDatePrivacyView.getMinimumHeight());
        View view = this.mDatePrivacyView;
        view.setLayoutParams(view.getLayoutParams());
        this.mClockIconsView.getLayoutParams().height = Math.max(dimensionPixelSize, this.mClockIconsView.getMinimumHeight());
        View view2 = this.mClockIconsView;
        view2.setLayoutParams(view2.getLayoutParams());
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        if (this.mQsDisabled) {
            layoutParams.height = this.mClockIconsView.getLayoutParams().height;
        } else {
            layoutParams.height = -2;
        }
        setLayoutParams(layoutParams);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842806);
        if (colorAttrDefaultColor != this.mTextColorPrimary) {
            int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(((FrameLayout) this).mContext, 16842808);
            this.mTextColorPrimary = colorAttrDefaultColor;
            this.mClockView.setTextColor(colorAttrDefaultColor);
            StatusBarIconController.TintedIconManager tintedIconManager = this.mTintedIconManager;
            if (tintedIconManager != null) {
                tintedIconManager.setTint(colorAttrDefaultColor);
            }
            BatteryMeterView batteryMeterView = this.mBatteryRemainingIcon;
            int i = this.mTextColorPrimary;
            batteryMeterView.updateColors(i, colorAttrDefaultColor2, i);
        }
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) this.mHeaderQsPanel.getLayoutParams();
        marginLayoutParams.topMargin = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.qqs_layout_margin_top);
        this.mHeaderQsPanel.setLayoutParams(marginLayoutParams);
        updateBatteryMode();
        updateHeadersPadding();
        updateAnimators();
    }

    private void updateAnimators() {
        updateAlphaAnimator();
        TouchAnimator.Builder addFloat = new TouchAnimator.Builder().addFloat(this.mContainer, "translationY", 0.0f, (float) this.mTopViewMeasureHeight);
        QSExpansionPathInterpolator qSExpansionPathInterpolator = this.mQSExpansionPathInterpolator;
        this.mTranslationAnimator = addFloat.setInterpolator(qSExpansionPathInterpolator != null ? qSExpansionPathInterpolator.getYInterpolator() : null).build();
    }

    private void updateAlphaAnimator() {
        this.mAlphaAnimator = new TouchAnimator.Builder().addFloat(this.mSecurityHeaderView, "alpha", 0.0f, 1.0f).addFloat(this.mClockView, "alpha", 0.0f, 1.0f).addFloat(this.mQSCarriers, "alpha", 0.0f, 1.0f).setListener(new TouchAnimator.ListenerAdapter() { // from class: com.android.systemui.qs.QuickStatusBarHeader.1
            @Override // com.android.systemui.qs.TouchAnimator.ListenerAdapter, com.android.systemui.qs.TouchAnimator.Listener
            public void onAnimationAtEnd() {
                super.onAnimationAtEnd();
                if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                    QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                }
            }

            @Override // com.android.systemui.qs.TouchAnimator.Listener
            public void onAnimationStarted() {
                QuickStatusBarHeader.this.setSeparatorVisibility(false);
                if (!QuickStatusBarHeader.this.mIsSingleCarrier) {
                    QuickStatusBarHeader.this.mIconContainer.addIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
                }
            }

            @Override // com.android.systemui.qs.TouchAnimator.ListenerAdapter, com.android.systemui.qs.TouchAnimator.Listener
            public void onAnimationAtStart() {
                super.onAnimationAtStart();
                QuickStatusBarHeader quickStatusBarHeader = QuickStatusBarHeader.this;
                quickStatusBarHeader.setSeparatorVisibility(quickStatusBarHeader.mShowClockIconsSeparator);
                QuickStatusBarHeader.this.mIconContainer.removeIgnoredSlots(QuickStatusBarHeader.this.mRssiIgnoredSlots);
            }
        }).build();
    }

    /* access modifiers changed from: package-private */
    public void setChipVisibility(boolean z) {
        this.mPrivacyChip.setVisibility(z ? 0 : 8);
        if (z) {
            TouchAnimator touchAnimator = this.mIconsAlphaAnimatorFixed;
            this.mIconsAlphaAnimator = touchAnimator;
            touchAnimator.setPosition(this.mKeyguardExpansionFraction);
            return;
        }
        this.mIconsAlphaAnimator = null;
        this.mIconContainer.setAlpha(1.0f);
        this.mBatteryRemainingIcon.setAlpha(1.0f);
    }

    public void setExpanded(boolean z, QuickQSPanelController quickQSPanelController) {
        if (this.mExpanded != z) {
            this.mExpanded = z;
            quickQSPanelController.setExpanded(z);
            updateEverything();
        }
    }

    public void setExpansion(boolean z, float f, float f2) {
        if (z) {
            f = 1.0f;
        }
        TouchAnimator touchAnimator = this.mAlphaAnimator;
        if (touchAnimator != null) {
            touchAnimator.setPosition(f);
        }
        TouchAnimator touchAnimator2 = this.mTranslationAnimator;
        if (touchAnimator2 != null) {
            touchAnimator2.setPosition(f);
        }
        TouchAnimator touchAnimator3 = this.mIconsAlphaAnimator;
        if (touchAnimator3 != null) {
            touchAnimator3.setPosition(f);
        }
        if (z) {
            setTranslationY(f2);
        } else {
            setTranslationY(0.0f);
        }
        this.mKeyguardExpansionFraction = f;
    }

    public void disable(int i, int i2, boolean z) {
        boolean z2 = true;
        int i3 = 0;
        if ((i2 & 1) == 0) {
            z2 = false;
        }
        if (z2 != this.mQsDisabled) {
            this.mQsDisabled = z2;
            this.mHeaderQsPanel.setDisabledByPolicy(z2);
            View view = this.mClockIconsView;
            if (this.mQsDisabled) {
                i3 = 8;
            }
            view.setVisibility(i3);
            updateResources();
        }
    }

    @Override // android.view.View
    public WindowInsets onApplyWindowInsets(WindowInsets windowInsets) {
        DisplayCutout displayCutout = windowInsets.getDisplayCutout();
        Pair<Integer, Integer> cornerCutoutMargins = StatusBarWindowView.cornerCutoutMargins(displayCutout, getDisplay());
        Pair<Integer, Integer> paddingNeededForCutoutAndRoundedCorner = StatusBarWindowView.paddingNeededForCutoutAndRoundedCorner(displayCutout, cornerCutoutMargins, -1);
        int i = 0;
        this.mDatePrivacyView.setPadding(((Integer) paddingNeededForCutoutAndRoundedCorner.first).intValue(), 0, ((Integer) paddingNeededForCutoutAndRoundedCorner.second).intValue(), 0);
        this.mClockIconsView.setPadding(((Integer) paddingNeededForCutoutAndRoundedCorner.first).intValue(), 0, ((Integer) paddingNeededForCutoutAndRoundedCorner.second).intValue(), 0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mDatePrivacySeparator.getLayoutParams();
        LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mClockIconsSeparator.getLayoutParams();
        boolean z = cornerCutoutMargins != null && (((Integer) cornerCutoutMargins.first).intValue() == 0 || ((Integer) cornerCutoutMargins.second).intValue() == 0);
        if (displayCutout != null) {
            Rect boundingRectTop = displayCutout.getBoundingRectTop();
            if (boundingRectTop.isEmpty() || z) {
                layoutParams.width = 0;
                this.mDatePrivacySeparator.setVisibility(8);
                layoutParams2.width = 0;
                setSeparatorVisibility(false);
                this.mShowClockIconsSeparator = false;
                this.mHasCenterCutout = false;
            } else {
                layoutParams.width = boundingRectTop.width();
                this.mDatePrivacySeparator.setVisibility(0);
                layoutParams2.width = boundingRectTop.width();
                this.mShowClockIconsSeparator = true;
                setSeparatorVisibility(this.mKeyguardExpansionFraction == 0.0f);
                this.mHasCenterCutout = true;
            }
        }
        this.mDatePrivacySeparator.setLayoutParams(layoutParams);
        this.mClockIconsSeparator.setLayoutParams(layoutParams2);
        this.mCutOutPaddingLeft = ((Integer) paddingNeededForCutoutAndRoundedCorner.first).intValue();
        this.mCutOutPaddingRight = ((Integer) paddingNeededForCutoutAndRoundedCorner.second).intValue();
        if (displayCutout != null) {
            i = displayCutout.getWaterfallInsets().top;
        }
        this.mWaterfallTopInset = i;
        updateBatteryMode();
        updateHeadersPadding();
        return super.onApplyWindowInsets(windowInsets);
    }

    /* access modifiers changed from: private */
    public void setSeparatorVisibility(boolean z) {
        int i = 8;
        int i2 = 0;
        if (this.mClockIconsSeparator.getVisibility() != (z ? 0 : 8)) {
            this.mClockIconsSeparator.setVisibility(z ? 0 : 8);
            View view = this.mQSCarriers;
            if (!z) {
                i = 0;
            }
            view.setVisibility(i);
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mClockView.getLayoutParams();
            layoutParams.width = z ? 0 : -2;
            float f = 1.0f;
            layoutParams.weight = z ? 1.0f : 0.0f;
            this.mClockView.setLayoutParams(layoutParams);
            LinearLayout.LayoutParams layoutParams2 = (LinearLayout.LayoutParams) this.mRightLayout.getLayoutParams();
            if (!z) {
                i2 = -2;
            }
            layoutParams2.width = i2;
            if (!z) {
                f = 0.0f;
            }
            layoutParams2.weight = f;
            this.mRightLayout.setLayoutParams(layoutParams2);
        }
    }

    private void updateHeadersPadding() {
        setContentMargins(this.mDatePrivacyView, 0, 0);
        setContentMargins(this.mClockIconsView, 0, 0);
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) getLayoutParams();
        int i = layoutParams.leftMargin;
        int i2 = layoutParams.rightMargin;
        int i3 = this.mCutOutPaddingLeft;
        int max = i3 > 0 ? Math.max(Math.max(i3, this.mRoundedCornerPadding) - i, 0) : 0;
        int i4 = this.mCutOutPaddingRight;
        int max2 = i4 > 0 ? Math.max(Math.max(i4, this.mRoundedCornerPadding) - i2, 0) : 0;
        this.mDatePrivacyView.setPadding(max, this.mWaterfallTopInset, max2, 0);
        this.mClockIconsView.setPadding(max, this.mWaterfallTopInset, max2, 0);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateEverything$0() {
        setClickable(!this.mExpanded);
    }

    public void updateEverything() {
        post(new Runnable() { // from class: com.android.systemui.qs.QuickStatusBarHeader$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                QuickStatusBarHeader.$r8$lambda$jfxpiBbHcWts__YTTSLpACq44xM(QuickStatusBarHeader.this);
            }
        });
    }

    public void setCallback(QSDetail.Callback callback) {
        this.mHeaderQsPanel.setCallback(callback);
    }

    private void setContentMargins(View view, int i, int i2) {
        ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) view.getLayoutParams();
        marginLayoutParams.setMarginStart(i);
        marginLayoutParams.setMarginEnd(i2);
        view.setLayoutParams(marginLayoutParams);
    }

    public void setExpandedScrollAmount(int i) {
        this.mClockIconsView.setScrollY(i);
        this.mDatePrivacyView.setScrollY(i);
    }
}
