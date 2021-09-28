package com.android.systemui.wallet.ui;

import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.wallet.ui.WalletCardCarousel;
import java.util.List;
/* loaded from: classes2.dex */
public class WalletView extends FrameLayout implements WalletCardCarousel.OnCardScrollListener {
    private final Button mActionButton;
    private final float mAnimationTranslationX;
    private final Button mAppButton;
    private final WalletCardCarousel mCardCarousel;
    private final ViewGroup mCardCarouselContainer;
    private final TextView mCardLabel;
    private View.OnClickListener mDeviceLockedActionOnClickListener;
    private final ViewGroup mEmptyStateView;
    private final TextView mErrorView;
    private FalsingCollector mFalsingCollector;
    private final ImageView mIcon;
    private boolean mIsDeviceLocked;
    private boolean mIsUdfpsEnabled;
    private final Interpolator mOutInterpolator;
    private View.OnClickListener mShowWalletAppOnClickListener;
    private final Button mToolbarAppButton;

    public WalletView(Context context) {
        this(context, null);
    }

    public WalletView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mIsDeviceLocked = false;
        this.mIsUdfpsEnabled = false;
        FrameLayout.inflate(context, R$layout.wallet_fullscreen, this);
        this.mCardCarouselContainer = (ViewGroup) requireViewById(R$id.card_carousel_container);
        WalletCardCarousel walletCardCarousel = (WalletCardCarousel) requireViewById(R$id.card_carousel);
        this.mCardCarousel = walletCardCarousel;
        walletCardCarousel.setCardScrollListener(this);
        this.mIcon = (ImageView) requireViewById(R$id.icon);
        this.mCardLabel = (TextView) requireViewById(R$id.label);
        this.mAppButton = (Button) requireViewById(R$id.wallet_app_button);
        this.mToolbarAppButton = (Button) requireViewById(R$id.wallet_toolbar_app_button);
        this.mActionButton = (Button) requireViewById(R$id.wallet_action_button);
        this.mErrorView = (TextView) requireViewById(R$id.error_view);
        this.mEmptyStateView = (ViewGroup) requireViewById(R$id.wallet_empty_state);
        this.mOutInterpolator = AnimationUtils.loadInterpolator(context, 17563650);
        this.mAnimationTranslationX = ((float) walletCardCarousel.getCardWidthPx()) / 4.0f;
    }

    @Override // android.widget.FrameLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mCardCarousel.setExpectedViewWidth(getWidth());
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        updateViewForOrientation(configuration.orientation);
    }

    private void updateViewForOrientation(@Configuration.Orientation int i) {
        if (i == 1) {
            renderViewPortrait();
        } else if (i == 2) {
            renderViewLandscape();
        }
        ViewGroup.LayoutParams layoutParams = this.mCardCarouselContainer.getLayoutParams();
        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
            ((ViewGroup.MarginLayoutParams) layoutParams).topMargin = getResources().getDimensionPixelSize(R$dimen.wallet_card_carousel_container_top_margin);
        }
    }

    private void renderViewPortrait() {
        this.mAppButton.setVisibility(0);
        this.mToolbarAppButton.setVisibility(8);
        this.mCardLabel.setVisibility(0);
        requireViewById(R$id.dynamic_placeholder).setVisibility(0);
        this.mAppButton.setOnClickListener(this.mShowWalletAppOnClickListener);
    }

    private void renderViewLandscape() {
        this.mToolbarAppButton.setVisibility(0);
        this.mAppButton.setVisibility(8);
        this.mCardLabel.setVisibility(8);
        requireViewById(R$id.dynamic_placeholder).setVisibility(8);
        this.mToolbarAppButton.setOnClickListener(this.mShowWalletAppOnClickListener);
    }

    @Override // android.view.View
    public boolean onTouchEvent(MotionEvent motionEvent) {
        return this.mCardCarousel.onTouchEvent(motionEvent) || super.onTouchEvent(motionEvent);
    }

    @Override // com.android.systemui.wallet.ui.WalletCardCarousel.OnCardScrollListener
    public void onCardScroll(WalletCardViewInfo walletCardViewInfo, WalletCardViewInfo walletCardViewInfo2, float f) {
        CharSequence labelText = getLabelText(walletCardViewInfo);
        Drawable headerIcon = getHeaderIcon(((FrameLayout) this).mContext, walletCardViewInfo);
        renderActionButton(walletCardViewInfo, this.mIsDeviceLocked, this.mIsUdfpsEnabled);
        if (walletCardViewInfo.isUiEquivalent(walletCardViewInfo2)) {
            this.mCardLabel.setAlpha(1.0f);
            this.mIcon.setAlpha(1.0f);
            this.mActionButton.setAlpha(1.0f);
            return;
        }
        this.mCardLabel.setText(labelText);
        this.mIcon.setImageDrawable(headerIcon);
        this.mCardLabel.setAlpha(f);
        this.mIcon.setAlpha(f);
        this.mActionButton.setAlpha(f);
    }

    /* access modifiers changed from: package-private */
    public void showCardCarousel(List<WalletCardViewInfo> list, int i, boolean z, boolean z2) {
        boolean data = this.mCardCarousel.setData(list, i, this.mIsDeviceLocked != z);
        this.mIsDeviceLocked = z;
        this.mIsUdfpsEnabled = z2;
        this.mCardCarouselContainer.setVisibility(0);
        this.mCardCarousel.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mEmptyStateView.setVisibility(8);
        this.mIcon.setImageDrawable(getHeaderIcon(((FrameLayout) this).mContext, list.get(i)));
        this.mCardLabel.setText(getLabelText(list.get(i)));
        updateViewForOrientation(getResources().getConfiguration().orientation);
        renderActionButton(list.get(i), z, this.mIsUdfpsEnabled);
        if (data) {
            animateViewsShown(this.mIcon, this.mCardLabel, this.mActionButton);
        }
    }

    /* access modifiers changed from: package-private */
    public void animateDismissal() {
        if (this.mCardCarouselContainer.getVisibility() == 0) {
            this.mCardCarousel.animate().translationX(this.mAnimationTranslationX).setInterpolator(this.mOutInterpolator).setDuration(200).start();
            this.mCardCarouselContainer.animate().alpha(0.0f).setDuration(100).setStartDelay(50).start();
        }
    }

    /* access modifiers changed from: package-private */
    public void showEmptyStateView(Drawable drawable, CharSequence charSequence, CharSequence charSequence2, View.OnClickListener onClickListener) {
        this.mEmptyStateView.setVisibility(0);
        this.mErrorView.setVisibility(8);
        this.mCardCarousel.setVisibility(8);
        this.mIcon.setImageDrawable(drawable);
        this.mIcon.setContentDescription(charSequence);
        this.mCardLabel.setText(R$string.wallet_empty_state_label);
        ((ImageView) this.mEmptyStateView.requireViewById(R$id.empty_state_icon)).setImageDrawable(((FrameLayout) this).mContext.getDrawable(R$drawable.ic_qs_plus));
        ((TextView) this.mEmptyStateView.requireViewById(R$id.empty_state_title)).setText(charSequence2);
        this.mEmptyStateView.setOnClickListener(onClickListener);
    }

    /* access modifiers changed from: package-private */
    public void showErrorMessage(CharSequence charSequence) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = getResources().getText(R$string.wallet_error_generic);
        }
        this.mErrorView.setText(charSequence);
        this.mErrorView.setVisibility(0);
        this.mCardCarouselContainer.setVisibility(8);
        this.mEmptyStateView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void setDeviceLockedActionOnClickListener(View.OnClickListener onClickListener) {
        this.mDeviceLockedActionOnClickListener = onClickListener;
    }

    /* access modifiers changed from: package-private */
    public void setShowWalletAppOnClickListener(View.OnClickListener onClickListener) {
        this.mShowWalletAppOnClickListener = onClickListener;
    }

    /* access modifiers changed from: package-private */
    public void hide() {
        setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public void show() {
        setVisibility(0);
    }

    /* access modifiers changed from: package-private */
    public void hideErrorMessage() {
        this.mErrorView.setVisibility(8);
    }

    /* access modifiers changed from: package-private */
    public WalletCardCarousel getCardCarousel() {
        return this.mCardCarousel;
    }

    @VisibleForTesting
    TextView getErrorView() {
        return this.mErrorView;
    }

    @VisibleForTesting
    ViewGroup getEmptyStateView() {
        return this.mEmptyStateView;
    }

    @VisibleForTesting
    ViewGroup getCardCarouselContainer() {
        return this.mCardCarouselContainer;
    }

    @VisibleForTesting
    TextView getCardLabel() {
        return this.mCardLabel;
    }

    private static Drawable getHeaderIcon(Context context, WalletCardViewInfo walletCardViewInfo) {
        Drawable icon = walletCardViewInfo.getIcon();
        if (icon != null) {
            icon.setTint(Utils.getColorAttrDefaultColor(context, 17956900));
        }
        return icon;
    }

    private void renderActionButton(WalletCardViewInfo walletCardViewInfo, boolean z, boolean z2) {
        View.OnClickListener onClickListener;
        CharSequence actionButtonText = getActionButtonText(walletCardViewInfo);
        if (z2 || actionButtonText == null) {
            this.mActionButton.setVisibility(8);
            return;
        }
        this.mActionButton.setVisibility(0);
        this.mActionButton.setText(actionButtonText);
        Button button = this.mActionButton;
        if (z) {
            onClickListener = this.mDeviceLockedActionOnClickListener;
        } else {
            onClickListener = new View.OnClickListener() { // from class: com.android.systemui.wallet.ui.WalletView$$ExternalSyntheticLambda0
                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    WalletView.lambda$renderActionButton$0(WalletCardViewInfo.this, view);
                }
            };
        }
        button.setOnClickListener(onClickListener);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$renderActionButton$0(WalletCardViewInfo walletCardViewInfo, View view) {
        try {
            walletCardViewInfo.getPendingIntent().send();
        } catch (PendingIntent.CanceledException unused) {
            Log.w("WalletView", "Error sending pending intent for wallet card.");
        }
    }

    private static void animateViewsShown(View... viewArr) {
        for (View view : viewArr) {
            if (view.getVisibility() == 0) {
                view.setAlpha(0.0f);
                view.animate().alpha(1.0f).setDuration(100).start();
            }
        }
    }

    private static CharSequence getLabelText(WalletCardViewInfo walletCardViewInfo) {
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        return split.length == 2 ? split[0] : walletCardViewInfo.getLabel();
    }

    private static CharSequence getActionButtonText(WalletCardViewInfo walletCardViewInfo) {
        String[] split = walletCardViewInfo.getLabel().toString().split("\\n");
        if (split.length == 2) {
            return split[1];
        }
        return null;
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        FalsingCollector falsingCollector = this.mFalsingCollector;
        if (falsingCollector != null) {
            falsingCollector.onTouchEvent(motionEvent);
        }
        boolean dispatchTouchEvent = super.dispatchTouchEvent(motionEvent);
        FalsingCollector falsingCollector2 = this.mFalsingCollector;
        if (falsingCollector2 != null) {
            falsingCollector2.onMotionEventComplete();
        }
        return dispatchTouchEvent;
    }

    public void setFalsingCollector(FalsingCollector falsingCollector) {
        this.mFalsingCollector = falsingCollector;
    }
}
