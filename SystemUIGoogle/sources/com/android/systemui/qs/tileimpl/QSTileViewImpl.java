package com.android.systemui.qs.tileimpl;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.systemui.FontSizeUtils;
import com.android.systemui.R$attr;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTileView;
import com.android.systemui.qs.tileimpl.HeightOverrideable;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: QSTileViewImpl.kt */
/* loaded from: classes.dex */
public class QSTileViewImpl extends QSTileView implements HeightOverrideable {
    public static final Companion Companion = new Companion(null);
    private final QSIconView _icon;
    private String accessibilityClass;
    private ImageView chevronView;
    private final boolean collapsed;
    private final int colorActive;
    private Drawable colorBackgroundDrawable;
    private final int colorInactive;
    private final int colorLabelActive;
    private final int colorLabelInactive;
    private final int colorLabelUnavailable;
    private final int colorSecondaryLabelActive;
    private final int colorSecondaryLabelInactive;
    private final int colorSecondaryLabelUnavailable;
    private final int colorUnavailable;
    private ImageView customDrawableView;
    private TextView label;
    private IgnorableChildLinearLayout labelContainer;
    private CharSequence lastStateDescription;
    private int paintColor;
    private RippleDrawable ripple;
    protected TextView secondaryLabel;
    protected ViewGroup sideView;
    private final ValueAnimator singleAnimator;
    private CharSequence stateDescriptionDeltas;
    private boolean tileState;
    private int heightOverride = -1;
    private boolean showRippleEffect = true;
    private int lastState = -1;
    private final int[] locInScreen = new int[2];

    @Override // android.view.View
    public boolean hasOverlappingRendering() {
        return false;
    }

    @Override // com.android.systemui.qs.tileimpl.HeightOverrideable
    public void resetOverride() {
        HeightOverrideable.DefaultImpls.resetOverride(this);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public QSTileViewImpl(Context context, QSIconView qSIconView, boolean z) {
        super(context);
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(qSIconView, "_icon");
        this._icon = qSIconView;
        this.collapsed = z;
        this.colorActive = Utils.getColorAttrDefaultColor(context, 17956900);
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(context, R$attr.offStateColor);
        this.colorInactive = colorAttrDefaultColor;
        this.colorUnavailable = Utils.applyAlpha(0.3f, colorAttrDefaultColor);
        this.colorLabelActive = Utils.getColorAttrDefaultColor(context, 16842809);
        int colorAttrDefaultColor2 = Utils.getColorAttrDefaultColor(context, 16842806);
        this.colorLabelInactive = colorAttrDefaultColor2;
        this.colorLabelUnavailable = Utils.applyAlpha(0.3f, colorAttrDefaultColor2);
        this.colorSecondaryLabelActive = Utils.getColorAttrDefaultColor(context, 16842810);
        int colorAttrDefaultColor3 = Utils.getColorAttrDefaultColor(context, 16842808);
        this.colorSecondaryLabelInactive = colorAttrDefaultColor3;
        this.colorSecondaryLabelUnavailable = Utils.applyAlpha(0.3f, colorAttrDefaultColor3);
        ValueAnimator valueAnimator = new ValueAnimator();
        valueAnimator.setDuration(350L);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$singleAnimator$1$1
            final /* synthetic */ QSTileViewImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                QSTileViewImpl qSTileViewImpl = this.this$0;
                Object animatedValue = valueAnimator2.getAnimatedValue("background");
                Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                int intValue = ((Integer) animatedValue).intValue();
                Object animatedValue2 = valueAnimator2.getAnimatedValue("label");
                Objects.requireNonNull(animatedValue2, "null cannot be cast to non-null type kotlin.Int");
                int intValue2 = ((Integer) animatedValue2).intValue();
                Object animatedValue3 = valueAnimator2.getAnimatedValue("secondaryLabel");
                Objects.requireNonNull(animatedValue3, "null cannot be cast to non-null type kotlin.Int");
                int intValue3 = ((Integer) animatedValue3).intValue();
                Object animatedValue4 = valueAnimator2.getAnimatedValue("chevron");
                Objects.requireNonNull(animatedValue4, "null cannot be cast to non-null type kotlin.Int");
                qSTileViewImpl.setAllColors(intValue, intValue2, intValue3, ((Integer) animatedValue4).intValue());
            }
        });
        Unit unit = Unit.INSTANCE;
        this.singleAnimator = valueAnimator;
        setId(LinearLayout.generateViewId());
        setOrientation(0);
        setGravity(8388627);
        setImportantForAccessibility(1);
        setClipChildren(false);
        setClipToPadding(false);
        setFocusable(true);
        setBackground(createTileBackground());
        setColor(getBackgroundColorForState(2));
        int dimensionPixelSize = getResources().getDimensionPixelSize(R$dimen.qs_tile_padding);
        setPaddingRelative(getResources().getDimensionPixelSize(R$dimen.qs_tile_start_padding), dimensionPixelSize, dimensionPixelSize, dimensionPixelSize);
        int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
        addView(qSIconView, new LinearLayout.LayoutParams(dimensionPixelSize2, dimensionPixelSize2));
        createAndAddLabels();
        createAndAddSideView();
    }

    /* compiled from: QSTileViewImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        public static /* synthetic */ void getTILE_STATE_RES_PREFIX$frameworks__base__packages__SystemUI__android_common__SystemUI_core$annotations() {
        }

        private Companion() {
        }
    }

    public int getHeightOverride() {
        return this.heightOverride;
    }

    @Override // com.android.systemui.qs.tileimpl.HeightOverrideable
    public void setHeightOverride(int i) {
        this.heightOverride = i;
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.plugins.qs.QSTileView
    /* renamed from: getSecondaryLabel  reason: collision with other method in class */
    public final TextView mo193getSecondaryLabel() {
        TextView textView = this.secondaryLabel;
        if (textView != null) {
            return textView;
        }
        Intrinsics.throwUninitializedPropertyAccessException("secondaryLabel");
        throw null;
    }

    protected final void setSecondaryLabel(TextView textView) {
        Intrinsics.checkNotNullParameter(textView, "<set-?>");
        this.secondaryLabel = textView;
    }

    /* access modifiers changed from: protected */
    public final ViewGroup getSideView() {
        ViewGroup viewGroup = this.sideView;
        if (viewGroup != null) {
            return viewGroup;
        }
        Intrinsics.throwUninitializedPropertyAccessException("sideView");
        throw null;
    }

    protected final void setSideView(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "<set-?>");
        this.sideView = viewGroup;
    }

    /* access modifiers changed from: protected */
    public final void setShowRippleEffect(boolean z) {
        this.showRippleEffect = z;
    }

    @Override // android.view.View
    protected void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        updateResources();
    }

    public final void updateResources() {
        TextView textView = this.label;
        if (textView != null) {
            int i = R$dimen.qs_tile_text_size;
            FontSizeUtils.updateFontSize(textView, i);
            FontSizeUtils.updateFontSize(mo193getSecondaryLabel(), i);
            int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.qs_icon_size);
            ViewGroup.LayoutParams layoutParams = this._icon.getLayoutParams();
            layoutParams.height = dimensionPixelSize;
            layoutParams.width = dimensionPixelSize;
            int dimensionPixelSize2 = getResources().getDimensionPixelSize(R$dimen.qs_tile_padding);
            setPaddingRelative(getResources().getDimensionPixelSize(R$dimen.qs_tile_start_padding), dimensionPixelSize2, dimensionPixelSize2, dimensionPixelSize2);
            int dimensionPixelSize3 = getResources().getDimensionPixelSize(R$dimen.qs_label_container_margin);
            IgnorableChildLinearLayout ignorableChildLinearLayout = this.labelContainer;
            if (ignorableChildLinearLayout != null) {
                ViewGroup.LayoutParams layoutParams2 = ignorableChildLinearLayout.getLayoutParams();
                Objects.requireNonNull(layoutParams2, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                ((ViewGroup.MarginLayoutParams) layoutParams2).setMarginStart(dimensionPixelSize3);
                ViewGroup.LayoutParams layoutParams3 = getSideView().getLayoutParams();
                Objects.requireNonNull(layoutParams3, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                ((ViewGroup.MarginLayoutParams) layoutParams3).setMarginStart(dimensionPixelSize3);
                ImageView imageView = this.chevronView;
                if (imageView != null) {
                    ViewGroup.LayoutParams layoutParams4 = imageView.getLayoutParams();
                    Objects.requireNonNull(layoutParams4, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams4;
                    marginLayoutParams.height = dimensionPixelSize;
                    marginLayoutParams.width = dimensionPixelSize;
                    int dimensionPixelSize4 = getResources().getDimensionPixelSize(R$dimen.qs_drawable_end_margin);
                    ImageView imageView2 = this.customDrawableView;
                    if (imageView2 != null) {
                        ViewGroup.LayoutParams layoutParams5 = imageView2.getLayoutParams();
                        Objects.requireNonNull(layoutParams5, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                        ViewGroup.MarginLayoutParams marginLayoutParams2 = (ViewGroup.MarginLayoutParams) layoutParams5;
                        marginLayoutParams2.height = dimensionPixelSize;
                        marginLayoutParams2.setMarginEnd(dimensionPixelSize4);
                        return;
                    }
                    Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                    throw null;
                }
                Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                throw null;
            }
            Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("label");
        throw null;
    }

    private final void createAndAddLabels() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.qs_tile_label, (ViewGroup) this, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type com.android.systemui.qs.tileimpl.IgnorableChildLinearLayout");
        IgnorableChildLinearLayout ignorableChildLinearLayout = (IgnorableChildLinearLayout) inflate;
        this.labelContainer = ignorableChildLinearLayout;
        View requireViewById = ignorableChildLinearLayout.requireViewById(R$id.tile_label);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "labelContainer.requireViewById(R.id.tile_label)");
        this.label = (TextView) requireViewById;
        IgnorableChildLinearLayout ignorableChildLinearLayout2 = this.labelContainer;
        if (ignorableChildLinearLayout2 != null) {
            View requireViewById2 = ignorableChildLinearLayout2.requireViewById(R$id.app_label);
            Intrinsics.checkNotNullExpressionValue(requireViewById2, "labelContainer.requireViewById(R.id.app_label)");
            setSecondaryLabel((TextView) requireViewById2);
            if (this.collapsed) {
                IgnorableChildLinearLayout ignorableChildLinearLayout3 = this.labelContainer;
                if (ignorableChildLinearLayout3 != null) {
                    ignorableChildLinearLayout3.setIgnoreLastView(true);
                    IgnorableChildLinearLayout ignorableChildLinearLayout4 = this.labelContainer;
                    if (ignorableChildLinearLayout4 != null) {
                        ignorableChildLinearLayout4.setForceUnspecifiedMeasure(true);
                        mo193getSecondaryLabel().setAlpha(0.0f);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
                    throw null;
                }
            }
            setLabelColor(getLabelColorForState(2));
            setSecondaryLabelColor(getSecondaryLabelColorForState(2));
            IgnorableChildLinearLayout ignorableChildLinearLayout5 = this.labelContainer;
            if (ignorableChildLinearLayout5 != null) {
                addView(ignorableChildLinearLayout5);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
            throw null;
        }
    }

    private final void createAndAddSideView() {
        View inflate = LayoutInflater.from(getContext()).inflate(R$layout.qs_tile_side_icon, (ViewGroup) this, false);
        Objects.requireNonNull(inflate, "null cannot be cast to non-null type android.view.ViewGroup");
        setSideView((ViewGroup) inflate);
        View requireViewById = getSideView().requireViewById(R$id.customDrawable);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "sideView.requireViewById(R.id.customDrawable)");
        this.customDrawableView = (ImageView) requireViewById;
        View requireViewById2 = getSideView().requireViewById(R$id.chevron);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "sideView.requireViewById(R.id.chevron)");
        this.chevronView = (ImageView) requireViewById2;
        setChevronColor(getChevronColorForState(2));
        addView(getSideView());
    }

    public final Drawable createTileBackground() {
        Drawable drawable = ((LinearLayout) this).mContext.getDrawable(R$drawable.qs_tile_background);
        Objects.requireNonNull(drawable, "null cannot be cast to non-null type android.graphics.drawable.RippleDrawable");
        RippleDrawable rippleDrawable = (RippleDrawable) drawable;
        this.ripple = rippleDrawable;
        Drawable findDrawableByLayerId = rippleDrawable.findDrawableByLayerId(R$id.background);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ripple.findDrawableByLayerId(R.id.background)");
        this.colorBackgroundDrawable = findDrawableByLayerId;
        RippleDrawable rippleDrawable2 = this.ripple;
        if (rippleDrawable2 != null) {
            return rippleDrawable2;
        }
        Intrinsics.throwUninitializedPropertyAccessException("ripple");
        throw null;
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        if (getHeightOverride() != -1) {
            setBottom(getTop() + getHeightOverride());
        }
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public View updateAccessibilityOrder(View view) {
        setAccessibilityTraversalAfter(view == null ? 0 : view.getId());
        return this;
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public QSIconView getIcon() {
        return this._icon;
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public View getIconWithBackground() {
        return getIcon();
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public void init(QSTile qSTile) {
        Intrinsics.checkNotNullParameter(qSTile, "tile");
        init(new View.OnClickListener(qSTile, this) { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$init$1
            final /* synthetic */ QSTile $tile;
            final /* synthetic */ QSTileViewImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.$tile = r1;
                this.this$0 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                this.$tile.click(this.this$0);
            }
        }, new View.OnLongClickListener(qSTile, this) { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$init$2
            final /* synthetic */ QSTile $tile;
            final /* synthetic */ QSTileViewImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.$tile = r1;
                this.this$0 = r2;
            }

            @Override // android.view.View.OnLongClickListener
            public final boolean onLongClick(View view) {
                this.$tile.longClick(this.this$0);
                return true;
            }
        });
    }

    private final void init(View.OnClickListener onClickListener, View.OnLongClickListener onLongClickListener) {
        setOnClickListener(onClickListener);
        setOnLongClickListener(onLongClickListener);
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public void onStateChanged(QSTile.State state) {
        Intrinsics.checkNotNullParameter(state, "state");
        post(new Runnable(this, state) { // from class: com.android.systemui.qs.tileimpl.QSTileViewImpl$onStateChanged$1
            final /* synthetic */ QSTile.State $state;
            final /* synthetic */ QSTileViewImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$state = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                this.this$0.handleStateChanged(this.$state);
            }
        });
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public int getDetailY() {
        return getTop() + (getHeight() / 2);
    }

    @Override // android.view.View
    public void setClickable(boolean z) {
        RippleDrawable rippleDrawable;
        super.setClickable(z);
        if (!z || !this.showRippleEffect) {
            Drawable drawable = this.colorBackgroundDrawable;
            rippleDrawable = drawable;
            if (drawable == null) {
                Intrinsics.throwUninitializedPropertyAccessException("colorBackgroundDrawable");
                throw null;
            }
        } else {
            RippleDrawable rippleDrawable2 = this.ripple;
            if (rippleDrawable2 != null) {
                Drawable drawable2 = this.colorBackgroundDrawable;
                if (drawable2 != null) {
                    drawable2.setCallback(rippleDrawable2);
                    Unit unit = Unit.INSTANCE;
                    rippleDrawable = rippleDrawable2;
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("colorBackgroundDrawable");
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("ripple");
                throw null;
            }
        }
        setBackground(rippleDrawable);
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public View getLabelContainer() {
        IgnorableChildLinearLayout ignorableChildLinearLayout = this.labelContainer;
        if (ignorableChildLinearLayout != null) {
            return ignorableChildLinearLayout;
        }
        Intrinsics.throwUninitializedPropertyAccessException("labelContainer");
        throw null;
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    /* renamed from: getSecondaryLabel */
    public View mo193getSecondaryLabel() {
        return mo193getSecondaryLabel();
    }

    @Override // com.android.systemui.plugins.qs.QSTileView
    public View getSecondaryIcon() {
        return getSideView();
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Intrinsics.checkNotNullParameter(accessibilityEvent, "event");
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            accessibilityEvent.setClassName(this.accessibilityClass);
        }
        if (accessibilityEvent.getContentChangeTypes() == 64 && this.stateDescriptionDeltas != null) {
            accessibilityEvent.getText().add(this.stateDescriptionDeltas);
            this.stateDescriptionDeltas = null;
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        Intrinsics.checkNotNullParameter(accessibilityNodeInfo, "info");
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setSelected(false);
        if (!TextUtils.isEmpty(this.accessibilityClass)) {
            accessibilityNodeInfo.setClassName(this.accessibilityClass);
            if (Intrinsics.areEqual(Switch.class.getName(), this.accessibilityClass)) {
                accessibilityNodeInfo.setText(getResources().getString(this.tileState ? R$string.switch_bar_on : R$string.switch_bar_off));
                accessibilityNodeInfo.setChecked(this.tileState);
                accessibilityNodeInfo.setCheckable(true);
                if (isLongClickable()) {
                    accessibilityNodeInfo.addAction(new AccessibilityNodeInfo.AccessibilityAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_LONG_CLICK.getId(), getResources().getString(R$string.accessibility_long_click_tile)));
                }
            }
        }
    }

    @Override // android.view.View, java.lang.Object
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getSimpleName());
        sb.append('[');
        sb.append("locInScreen=(" + this.locInScreen[0] + ", " + this.locInScreen[1] + ')');
        sb.append(Intrinsics.stringPlus(", iconView=", this._icon));
        sb.append(Intrinsics.stringPlus(", tileState=", Boolean.valueOf(this.tileState)));
        sb.append("]");
        String sb2 = sb.toString();
        Intrinsics.checkNotNullExpressionValue(sb2, "sb.toString()");
        return sb2;
    }

    /* access modifiers changed from: protected */
    public void handleStateChanged(QSTile.State state) {
        String str;
        boolean z;
        Intrinsics.checkNotNullParameter(state, "state");
        boolean animationsEnabled = animationsEnabled();
        this.showRippleEffect = state.showRippleEffect;
        setClickable(state.state != 0);
        setLongClickable(state.handlesLongClick);
        getIcon().setIcon(state, animationsEnabled);
        setContentDescription(state.contentDescription);
        StringBuilder sb = new StringBuilder();
        String stateText = getStateText(state);
        if (!TextUtils.isEmpty(stateText)) {
            sb.append(stateText);
            if (TextUtils.isEmpty(state.secondaryLabel)) {
                state.secondaryLabel = stateText;
            }
        }
        if (!TextUtils.isEmpty(state.stateDescription)) {
            sb.append(", ");
            sb.append(state.stateDescription);
            int i = this.lastState;
            if (i != -1 && state.state == i && !Intrinsics.areEqual(state.stateDescription, this.lastStateDescription)) {
                this.stateDescriptionDeltas = state.stateDescription;
            }
        }
        setStateDescription(sb.toString());
        this.lastStateDescription = state.stateDescription;
        if (state.state == 0) {
            str = null;
        } else {
            str = state.expandedAccessibilityClassName;
        }
        this.accessibilityClass = str;
        if ((state instanceof QSTile.BooleanState) && this.tileState != (z = ((QSTile.BooleanState) state).value)) {
            this.tileState = z;
        }
        TextView textView = this.label;
        if (textView != null) {
            if (!Objects.equals(textView.getText(), state.label)) {
                TextView textView2 = this.label;
                if (textView2 != null) {
                    textView2.setText(state.label);
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("label");
                    throw null;
                }
            }
            if (!Objects.equals(mo193getSecondaryLabel().getText(), state.secondaryLabel)) {
                mo193getSecondaryLabel().setText(state.secondaryLabel);
                mo193getSecondaryLabel().setVisibility(TextUtils.isEmpty(state.secondaryLabel) ? 8 : 0);
            }
            if (state.state != this.lastState) {
                this.singleAnimator.cancel();
                if (animationsEnabled) {
                    ValueAnimator valueAnimator = this.singleAnimator;
                    PropertyValuesHolder[] propertyValuesHolderArr = new PropertyValuesHolder[4];
                    propertyValuesHolderArr[0] = QSTileViewImplKt.access$colorValuesHolder("background", this.paintColor, getBackgroundColorForState(state.state));
                    int[] iArr = new int[2];
                    TextView textView3 = this.label;
                    if (textView3 != null) {
                        iArr[0] = textView3.getCurrentTextColor();
                        iArr[1] = getLabelColorForState(state.state);
                        propertyValuesHolderArr[1] = QSTileViewImplKt.access$colorValuesHolder("label", iArr);
                        propertyValuesHolderArr[2] = QSTileViewImplKt.access$colorValuesHolder("secondaryLabel", mo193getSecondaryLabel().getCurrentTextColor(), getSecondaryLabelColorForState(state.state));
                        int[] iArr2 = new int[2];
                        ImageView imageView = this.chevronView;
                        if (imageView != null) {
                            ColorStateList imageTintList = imageView.getImageTintList();
                            iArr2[0] = imageTintList == null ? 0 : imageTintList.getDefaultColor();
                            iArr2[1] = getChevronColorForState(state.state);
                            propertyValuesHolderArr[3] = QSTileViewImplKt.access$colorValuesHolder("chevron", iArr2);
                            valueAnimator.setValues(propertyValuesHolderArr);
                            this.singleAnimator.start();
                        } else {
                            Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                            throw null;
                        }
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("label");
                        throw null;
                    }
                } else {
                    setAllColors(getBackgroundColorForState(state.state), getLabelColorForState(state.state), getSecondaryLabelColorForState(state.state), getChevronColorForState(state.state));
                }
            }
            loadSideViewDrawableIfNecessary(state);
            TextView textView4 = this.label;
            if (textView4 != null) {
                textView4.setEnabled(!state.disabledByPolicy);
                this.lastState = state.state;
                return;
            }
            Intrinsics.throwUninitializedPropertyAccessException("label");
            throw null;
        }
        Intrinsics.throwUninitializedPropertyAccessException("label");
        throw null;
    }

    /* access modifiers changed from: private */
    public final void setAllColors(int i, int i2, int i3, int i4) {
        setColor(i);
        setLabelColor(i2);
        setSecondaryLabelColor(i3);
        setChevronColor(i4);
    }

    private final void setColor(int i) {
        Drawable drawable = this.colorBackgroundDrawable;
        if (drawable != null) {
            drawable.setTint(i);
            this.paintColor = i;
            return;
        }
        Intrinsics.throwUninitializedPropertyAccessException("colorBackgroundDrawable");
        throw null;
    }

    private final void setLabelColor(int i) {
        TextView textView = this.label;
        if (textView != null) {
            textView.setTextColor(i);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("label");
            throw null;
        }
    }

    private final void setSecondaryLabelColor(int i) {
        mo193getSecondaryLabel().setTextColor(i);
    }

    private final void setChevronColor(int i) {
        ImageView imageView = this.chevronView;
        if (imageView != null) {
            imageView.setImageTintList(ColorStateList.valueOf(i));
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("chevronView");
            throw null;
        }
    }

    private final void loadSideViewDrawableIfNecessary(QSTile.State state) {
        Drawable drawable = state.sideViewCustomDrawable;
        if (drawable != null) {
            ImageView imageView = this.customDrawableView;
            if (imageView != null) {
                imageView.setImageDrawable(drawable);
                ImageView imageView2 = this.customDrawableView;
                if (imageView2 != null) {
                    imageView2.setVisibility(0);
                    ImageView imageView3 = this.chevronView;
                    if (imageView3 != null) {
                        imageView3.setVisibility(8);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
        } else if (!(state instanceof QSTile.BooleanState) || ((QSTile.BooleanState) state).forceExpandIcon) {
            ImageView imageView4 = this.customDrawableView;
            if (imageView4 != null) {
                imageView4.setImageDrawable(null);
                ImageView imageView5 = this.customDrawableView;
                if (imageView5 != null) {
                    imageView5.setVisibility(8);
                    ImageView imageView6 = this.chevronView;
                    if (imageView6 != null) {
                        imageView6.setVisibility(0);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
        } else {
            ImageView imageView7 = this.customDrawableView;
            if (imageView7 != null) {
                imageView7.setImageDrawable(null);
                ImageView imageView8 = this.customDrawableView;
                if (imageView8 != null) {
                    imageView8.setVisibility(8);
                    ImageView imageView9 = this.chevronView;
                    if (imageView9 != null) {
                        imageView9.setVisibility(8);
                    } else {
                        Intrinsics.throwUninitializedPropertyAccessException("chevronView");
                        throw null;
                    }
                } else {
                    Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                    throw null;
                }
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("customDrawableView");
                throw null;
            }
        }
    }

    private final String getStateText(QSTile.State state) {
        if (state.disabledByPolicy) {
            String string = getContext().getString(R$string.tile_disabled);
            Intrinsics.checkNotNullExpressionValue(string, "context.getString(R.string.tile_disabled)");
            return string;
        } else if (state.state != 0 && !(state instanceof QSTile.BooleanState)) {
            return "";
        } else {
            String str = getResources().getStringArray(SubtitleArrayMapping.INSTANCE.getSubtitleId(state.spec))[state.state];
            Intrinsics.checkNotNullExpressionValue(str, "{\n            var arrayResId = SubtitleArrayMapping.getSubtitleId(state.spec)\n            val array = resources.getStringArray(arrayResId)\n            array[state.state]\n        }");
            return str;
        }
    }

    protected boolean animationsEnabled() {
        if (!isShown()) {
            return false;
        }
        if (!(getAlpha() == 1.0f)) {
            return false;
        }
        getLocationOnScreen(this.locInScreen);
        if (this.locInScreen[1] >= (-getHeight())) {
            return true;
        }
        return false;
    }

    private final int getBackgroundColorForState(int i) {
        if (i == 0) {
            return this.colorUnavailable;
        }
        if (i == 1) {
            return this.colorInactive;
        }
        if (i == 2) {
            return this.colorActive;
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    private final int getLabelColorForState(int i) {
        if (i == 0) {
            return this.colorLabelUnavailable;
        }
        if (i == 1) {
            return this.colorLabelInactive;
        }
        if (i == 2) {
            return this.colorLabelActive;
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    private final int getSecondaryLabelColorForState(int i) {
        if (i == 0) {
            return this.colorSecondaryLabelUnavailable;
        }
        if (i == 1) {
            return this.colorSecondaryLabelInactive;
        }
        if (i == 2) {
            return this.colorSecondaryLabelActive;
        }
        Log.e("QSTileViewImpl", Intrinsics.stringPlus("Invalid state ", Integer.valueOf(i)));
        return 0;
    }

    private final int getChevronColorForState(int i) {
        return getSecondaryLabelColorForState(i);
    }
}
