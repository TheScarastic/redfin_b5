package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.settingslib.Utils;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import java.util.List;
import java.util.Objects;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: OngoingPrivacyChip.kt */
/* loaded from: classes.dex */
public final class OngoingPrivacyChip extends FrameLayout {
    private int iconColor;
    private int iconMargin;
    private int iconSize;
    private LinearLayout iconsContainer;
    private List<PrivacyItem> privacyList;

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context) {
        this(context, null, 0, 0, 14, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0, 0, 12, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    /* JADX INFO: 'this' call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0, 8, null);
        Intrinsics.checkNotNullParameter(context, "context");
    }

    public /* synthetic */ OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i, int i2, int i3, DefaultConstructorMarker defaultConstructorMarker) {
        this(context, (i3 & 2) != 0 ? null : attributeSet, (i3 & 4) != 0 ? 0 : i, (i3 & 8) != 0 ? 0 : i2);
    }

    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public OngoingPrivacyChip(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        Intrinsics.checkNotNullParameter(context, "context");
        this.privacyList = CollectionsKt__CollectionsKt.emptyList();
    }

    public final List<PrivacyItem> getPrivacyList() {
        return this.privacyList;
    }

    public final void setPrivacyList(List<PrivacyItem> list) {
        Intrinsics.checkNotNullParameter(list, "value");
        this.privacyList = list;
        Context context = getContext();
        Intrinsics.checkNotNullExpressionValue(context, "context");
        updateView(new PrivacyChipBuilder(context, this.privacyList));
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        View requireViewById = requireViewById(R$id.icons_container);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "requireViewById(R.id.icons_container)");
        this.iconsContainer = (LinearLayout) requireViewById;
        updateResources();
    }

    private static final void updateView$setIcons(OngoingPrivacyChip ongoingPrivacyChip, PrivacyChipBuilder privacyChipBuilder, ViewGroup viewGroup) {
        viewGroup.removeAllViews();
        int i = 0;
        for (Object obj : privacyChipBuilder.generateIcons()) {
            int i2 = i + 1;
            if (i < 0) {
                CollectionsKt__CollectionsKt.throwIndexOverflow();
            }
            Drawable drawable = (Drawable) obj;
            drawable.mutate();
            drawable.setTint(ongoingPrivacyChip.iconColor);
            ImageView imageView = new ImageView(ongoingPrivacyChip.getContext());
            imageView.setImageDrawable(drawable);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            int i3 = ongoingPrivacyChip.iconSize;
            viewGroup.addView(imageView, i3, i3);
            if (i != 0) {
                ViewGroup.LayoutParams layoutParams = imageView.getLayoutParams();
                Objects.requireNonNull(layoutParams, "null cannot be cast to non-null type android.view.ViewGroup.MarginLayoutParams");
                ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                marginLayoutParams.setMarginStart(ongoingPrivacyChip.iconMargin);
                imageView.setLayoutParams(marginLayoutParams);
            }
            i = i2;
        }
    }

    private final void updateView(PrivacyChipBuilder privacyChipBuilder) {
        if (!this.privacyList.isEmpty()) {
            generateContentDescription(privacyChipBuilder);
            LinearLayout linearLayout = this.iconsContainer;
            if (linearLayout != null) {
                updateView$setIcons(this, privacyChipBuilder, linearLayout);
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
                throw null;
            }
        } else {
            LinearLayout linearLayout2 = this.iconsContainer;
            if (linearLayout2 != null) {
                linearLayout2.removeAllViews();
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
                throw null;
            }
        }
        requestLayout();
    }

    private final void generateContentDescription(PrivacyChipBuilder privacyChipBuilder) {
        setContentDescription(getContext().getString(R$string.ongoing_privacy_chip_content_multiple_apps, privacyChipBuilder.joinTypes()));
    }

    private final void updateResources() {
        this.iconMargin = getContext().getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_icon_margin);
        this.iconSize = getContext().getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_icon_size);
        this.iconColor = Utils.getColorAttrDefaultColor(getContext(), 16843827);
        int dimensionPixelSize = getContext().getResources().getDimensionPixelSize(R$dimen.ongoing_appops_chip_side_padding);
        LinearLayout linearLayout = this.iconsContainer;
        if (linearLayout != null) {
            linearLayout.setPaddingRelative(dimensionPixelSize, 0, dimensionPixelSize, 0);
            LinearLayout linearLayout2 = this.iconsContainer;
            if (linearLayout2 != null) {
                linearLayout2.setBackground(getContext().getDrawable(R$drawable.privacy_chip_bg));
            } else {
                Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
                throw null;
            }
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("iconsContainer");
            throw null;
        }
    }
}
