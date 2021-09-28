package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.launcher3.icons.GraphicsUtils;
import com.android.systemui.bcsmartspace.R$dimen;
import com.android.systemui.bcsmartspace.R$id;
import com.android.systemui.bcsmartspace.R$string;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.Locale;
/* loaded from: classes2.dex */
public class BcSmartspaceCard extends LinearLayout {
    private DoubleShadowTextView mBaseActionIconSubtitleView;
    private ImageView mDndImageView;
    private float mDozeAmount;
    private ViewGroup mExtrasGroup;
    private DoubleShadowIconDrawable mIconDrawable;
    private int mIconTintColor;
    private BcSmartspaceCardLoggingInfo mLoggingInfo;
    private ImageView mNextAlarmImageView;
    private TextView mNextAlarmTextView;
    private BcSmartspaceCardSecondary mSecondaryCard;
    private ImageView mSpacerDot;
    private DoubleShadowIconDrawable mSpacerDotIconDrawable;
    private TextView mSubtitleTextView;
    private SmartspaceTarget mTarget;
    private TextView mTitleTextView;
    private boolean mUseDot;
    private boolean mUsePageIndicatorUi;

    public BcSmartspaceCard(Context context) {
        this(context, null);
    }

    public BcSmartspaceCard(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mSecondaryCard = null;
        this.mSpacerDotIconDrawable = null;
        this.mIconTintColor = GraphicsUtils.getAttrColor(getContext(), 16842806);
        this.mTitleTextView = null;
        this.mSubtitleTextView = null;
        this.mBaseActionIconSubtitleView = null;
        this.mExtrasGroup = null;
        this.mDndImageView = null;
        this.mNextAlarmImageView = null;
        this.mNextAlarmTextView = null;
        this.mSpacerDot = null;
        this.mUseDot = false;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mTitleTextView = (TextView) findViewById(R$id.title_text);
        this.mSubtitleTextView = (TextView) findViewById(R$id.subtitle_text);
        this.mBaseActionIconSubtitleView = (DoubleShadowTextView) findViewById(R$id.base_action_icon_subtitle);
        ViewGroup viewGroup = (ViewGroup) findViewById(R$id.smartspace_extras_group);
        this.mExtrasGroup = viewGroup;
        if (viewGroup != null) {
            this.mDndImageView = (ImageView) viewGroup.findViewById(R$id.dnd_icon);
            this.mNextAlarmImageView = (ImageView) this.mExtrasGroup.findViewById(R$id.alarm_icon);
            this.mNextAlarmTextView = (TextView) this.mExtrasGroup.findViewById(R$id.alarm_text);
            this.mSpacerDot = (ImageView) this.mExtrasGroup.findViewById(R$id.spacer_dot);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (this.mSpacerDot != null && this.mSpacerDotIconDrawable == null) {
            DoubleShadowIconDrawable doubleShadowIconDrawable = new DoubleShadowIconDrawable(this.mSpacerDot.getDrawable().mutate(), getContext(), this.mSpacerDot.getDrawable().getIntrinsicWidth(), this.mSpacerDot.getDrawable().getIntrinsicHeight());
            this.mSpacerDotIconDrawable = doubleShadowIconDrawable;
            this.mSpacerDot.setImageDrawable(doubleShadowIconDrawable);
        }
    }

    /* access modifiers changed from: package-private */
    public void setSmartspaceTarget(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo, boolean z) {
        Drawable drawable;
        boolean z2;
        DoubleShadowIconDrawable doubleShadowIconDrawable;
        this.mTarget = smartspaceTarget;
        SmartspaceAction headerAction = smartspaceTarget.getHeaderAction();
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        this.mLoggingInfo = bcSmartspaceCardLoggingInfo;
        this.mUsePageIndicatorUi = z;
        CharSequence charSequence = null;
        if (headerAction != null) {
            BcSmartspaceCardSecondary bcSmartspaceCardSecondary = this.mSecondaryCard;
            if (bcSmartspaceCardSecondary != null) {
                z2 = bcSmartspaceCardSecondary.setSmartspaceActions(smartspaceTarget, smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
                this.mSecondaryCard.setVisibility(z2 ? 0 : 8);
            } else {
                z2 = false;
            }
            Drawable iconDrawable = BcSmartSpaceUtil.getIconDrawable(headerAction.getIcon(), getContext());
            if (iconDrawable == null) {
                doubleShadowIconDrawable = null;
            } else {
                doubleShadowIconDrawable = new DoubleShadowIconDrawable(iconDrawable, getContext());
            }
            this.mIconDrawable = doubleShadowIconDrawable;
            CharSequence title = headerAction.getTitle();
            CharSequence subtitle = headerAction.getSubtitle();
            boolean z3 = true;
            boolean z4 = smartspaceTarget.getFeatureType() == 1 || !TextUtils.isEmpty(title);
            boolean z5 = !TextUtils.isEmpty(subtitle);
            this.mUseDot = z5 && smartspaceTarget.getFeatureType() == 1;
            updateZenVisibility();
            if (!z4) {
                title = subtitle;
            }
            boolean z6 = !this.mUsePageIndicatorUi && z2;
            if (z4 == z5) {
                z3 = false;
            }
            setTitle(title, z6, z3);
            if (!z4 || !z5) {
                subtitle = null;
            }
            setSubtitle(subtitle, headerAction.getContentDescription());
            updateIconTint();
        }
        if (!(baseAction == null || this.mBaseActionIconSubtitleView == null)) {
            if (baseAction.getIcon() == null) {
                drawable = null;
            } else {
                drawable = BcSmartSpaceUtil.getIconDrawable(baseAction.getIcon(), getContext());
            }
            if (drawable == null) {
                this.mBaseActionIconSubtitleView.setVisibility(4);
                this.mBaseActionIconSubtitleView.setOnClickListener(null);
                this.mBaseActionIconSubtitleView.setContentDescription(null);
            } else {
                drawable.setTintList(null);
                this.mBaseActionIconSubtitleView.setText(baseAction.getSubtitle());
                this.mBaseActionIconSubtitleView.setCompoundDrawablesRelative(drawable, null, null, null);
                this.mBaseActionIconSubtitleView.setVisibility(0);
                BcSmartSpaceUtil.setOnClickListener(this.mBaseActionIconSubtitleView, smartspaceTarget, baseAction, "BcSmartspaceCard", smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
                setFullWeatherAccessibilityDescription(this.mBaseActionIconSubtitleView, baseAction.getSubtitle(), baseAction.getContentDescription());
            }
        }
        if (hasIntent(headerAction)) {
            BcSmartSpaceUtil.setOnClickListener(this, smartspaceTarget, headerAction, "BcSmartspaceCard", smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        } else if (hasIntent(baseAction)) {
            BcSmartSpaceUtil.setOnClickListener(this, smartspaceTarget, baseAction, "BcSmartspaceCard", smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        } else {
            setOnClickListener(null);
        }
        if (baseAction != null) {
            charSequence = baseAction.getContentDescription();
        }
        setContentDescription(charSequence);
    }

    /* access modifiers changed from: package-private */
    public void setSecondaryCard(BcSmartspaceCardSecondary bcSmartspaceCardSecondary) {
        this.mSecondaryCard = bcSmartspaceCardSecondary;
        if (getChildAt(1) != null) {
            removeViewAt(1);
        }
        if (bcSmartspaceCardSecondary != null) {
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(0, getResources().getDimensionPixelSize(R$dimen.enhanced_smartspace_height));
            layoutParams.weight = 3.0f;
            layoutParams.setMarginStart(getResources().getDimensionPixelSize(R$dimen.enhanced_smartspace_secondary_card_start_margin));
            layoutParams.setMarginEnd(getResources().getDimensionPixelSize(R$dimen.enhanced_smartspace_secondary_card_end_margin));
            addView(bcSmartspaceCardSecondary, 1, layoutParams);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDozeAmount(float f) {
        this.mDozeAmount = f;
        BcSmartspaceCardSecondary bcSmartspaceCardSecondary = this.mSecondaryCard;
        if (bcSmartspaceCardSecondary != null) {
            bcSmartspaceCardSecondary.setAlpha(1.0f - f);
        }
        ImageView imageView = this.mDndImageView;
        if (imageView != null) {
            imageView.setAlpha(this.mDozeAmount);
        }
        if (this.mSpacerDot != null) {
            ImageView imageView2 = this.mDndImageView;
            boolean z = true;
            boolean z2 = imageView2 != null && imageView2.getVisibility() == 0;
            ImageView imageView3 = this.mNextAlarmImageView;
            if (imageView3 == null || imageView3.getVisibility() != 0) {
                z = false;
            }
            if (z) {
                this.mSpacerDot.setAlpha(1.0f);
            } else if (z2) {
                this.mSpacerDot.setAlpha(this.mDozeAmount);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void setPrimaryTextColor(int i) {
        TextView textView = this.mTitleTextView;
        if (textView != null) {
            textView.setTextColor(i);
        }
        TextView textView2 = (TextView) findViewById(R$id.clock);
        if (textView2 != null) {
            textView2.setTextColor(i);
        }
        TextView textView3 = this.mSubtitleTextView;
        if (textView3 != null) {
            textView3.setTextColor(i);
        }
        DoubleShadowTextView doubleShadowTextView = this.mBaseActionIconSubtitleView;
        if (doubleShadowTextView != null) {
            doubleShadowTextView.setTextColor(i);
        }
        this.mIconTintColor = i;
        updateZenColors();
        updateIconTint();
    }

    @Override // android.view.View
    public AccessibilityNodeInfo createAccessibilityNodeInfo() {
        AccessibilityNodeInfo createAccessibilityNodeInfo = super.createAccessibilityNodeInfo();
        AccessibilityNodeInfoCompat.wrap(createAccessibilityNodeInfo).setRoleDescription(" ");
        return createAccessibilityNodeInfo;
    }

    void setTitle(CharSequence charSequence, boolean z, boolean z2) {
        TextView textView = this.mTitleTextView;
        if (textView == null) {
            Log.w("BcSmartspaceCard", "No title view to update");
            return;
        }
        textView.setText(charSequence);
        this.mTitleTextView.setCompoundDrawablesRelative(z2 ? this.mIconDrawable : null, null, null, null);
        this.mTitleTextView.setMaxLines(z ? 2 : 1);
        if (this.mTarget.getFeatureType() != 2 || !Locale.ENGLISH.getLanguage().equals(((LinearLayout) this).mContext.getResources().getConfiguration().locale.getLanguage())) {
            this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.END);
        } else {
            this.mTitleTextView.setEllipsize(TextUtils.TruncateAt.MIDDLE);
        }
    }

    void setSubtitle(CharSequence charSequence, CharSequence charSequence2) {
        TextView textView = this.mSubtitleTextView;
        if (textView == null) {
            Log.w("BcSmartspaceCard", "No subtitle view to update");
            return;
        }
        textView.setText(charSequence);
        this.mSubtitleTextView.setCompoundDrawablesRelative(TextUtils.isEmpty(charSequence) ? null : this.mIconDrawable, null, null, null);
        this.mSubtitleTextView.setMaxLines((this.mTarget.getFeatureType() != 5 || this.mUsePageIndicatorUi) ? 1 : 2);
        if (this.mTarget.getFeatureType() == 1) {
            setFullWeatherAccessibilityDescription(this.mSubtitleTextView, charSequence, charSequence2);
        } else {
            this.mSubtitleTextView.setContentDescription(null);
        }
    }

    void updateIconTint() {
        SmartspaceTarget smartspaceTarget = this.mTarget;
        if (smartspaceTarget != null && this.mIconDrawable != null) {
            boolean z = true;
            if (smartspaceTarget.getFeatureType() == 1) {
                z = false;
            }
            if (z) {
                this.mIconDrawable.setTint(this.mIconTintColor);
            } else {
                this.mIconDrawable.setTintList(null);
            }
        }
    }

    void updateZenColors() {
        TextView textView = this.mNextAlarmTextView;
        if (textView != null) {
            textView.setTextColor(this.mIconTintColor);
        }
        updateTint(this.mNextAlarmImageView);
        updateTint(this.mDndImageView);
        updateTint(this.mSpacerDot);
    }

    private void updateTint(ImageView imageView) {
        if (imageView != null && imageView.getDrawable() != null) {
            imageView.getDrawable().setTint(this.mIconTintColor);
        }
    }

    /* access modifiers changed from: package-private */
    public void setDnd(Drawable drawable, String str) {
        ImageView imageView = this.mDndImageView;
        if (imageView != null) {
            if (drawable == null) {
                imageView.setVisibility(8);
            } else {
                imageView.setImageDrawable(new DoubleShadowIconDrawable(drawable.mutate(), getContext()));
                this.mDndImageView.setContentDescription(str);
                this.mDndImageView.setVisibility(0);
            }
            updateZenVisibility();
        }
    }

    /* access modifiers changed from: package-private */
    public void setNextAlarm(Drawable drawable, String str) {
        ImageView imageView = this.mNextAlarmImageView;
        if (imageView != null && this.mNextAlarmTextView != null) {
            if (drawable == null) {
                imageView.setVisibility(8);
                this.mNextAlarmTextView.setVisibility(8);
            } else {
                imageView.setImageDrawable(new DoubleShadowIconDrawable(drawable.mutate(), getContext()));
                this.mNextAlarmImageView.setVisibility(0);
                this.mNextAlarmTextView.setText(str);
                this.mNextAlarmTextView.setVisibility(0);
            }
            updateZenVisibility();
        }
    }

    private void updateZenVisibility() {
        if (this.mExtrasGroup != null && this.mSpacerDot != null) {
            ImageView imageView = this.mDndImageView;
            boolean z = true;
            boolean z2 = imageView != null && imageView.getVisibility() == 0;
            ImageView imageView2 = this.mNextAlarmImageView;
            boolean z3 = imageView2 != null && imageView2.getVisibility() == 0;
            if ((!z2 && !z3) || (this.mUsePageIndicatorUi && this.mTarget.getFeatureType() != 1)) {
                z = false;
            }
            if (!z) {
                this.mExtrasGroup.setVisibility(4);
                return;
            }
            this.mExtrasGroup.setVisibility(0);
            if (this.mUseDot) {
                this.mSpacerDot.setVisibility(0);
                setDozeAmount(this.mDozeAmount);
            } else {
                this.mSpacerDot.setVisibility(8);
            }
            updateZenColors();
        }
    }

    public SmartspaceTarget getTarget() {
        return this.mTarget;
    }

    private void setFullWeatherAccessibilityDescription(TextView textView, CharSequence charSequence, CharSequence charSequence2) {
        if (TextUtils.isEmpty(charSequence)) {
            charSequence = charSequence2;
        } else if (!TextUtils.isEmpty(charSequence2)) {
            charSequence = ((LinearLayout) this).mContext.getString(R$string.weather_description, charSequence2, charSequence);
        }
        textView.setContentDescription(charSequence);
    }

    private boolean hasIntent(SmartspaceAction smartspaceAction) {
        return (smartspaceAction == null || (smartspaceAction.getIntent() == null && smartspaceAction.getPendingIntent() == null)) ? false : true;
    }
}
