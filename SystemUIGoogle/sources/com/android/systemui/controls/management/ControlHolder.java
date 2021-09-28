package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Icon;
import android.view.View;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.controls.ControlInterface;
import com.android.systemui.controls.management.ControlsModel;
import com.android.systemui.controls.ui.RenderInfo;
import kotlin.Unit;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlAdapter.kt */
/* loaded from: classes.dex */
public final class ControlHolder extends Holder {
    private final ControlHolderAccessibilityDelegate accessibilityDelegate;
    private final CheckBox favorite;
    private final Function2<String, Boolean, Unit> favoriteCallback;
    private final ImageView icon;
    private final ControlsModel.MoveHelper moveHelper;
    private final TextView removed;
    private final TextView subtitle;
    private final TextView title;
    private final String favoriteStateDescription = this.itemView.getContext().getString(R$string.accessibility_control_favorite);
    private final String notFavoriteStateDescription = this.itemView.getContext().getString(R$string.accessibility_control_not_favorite);

    public final Function2<String, Boolean, Unit> getFavoriteCallback() {
        return this.favoriteCallback;
    }

    /* JADX DEBUG: Multi-variable search result rejected for r4v0, resolved type: kotlin.jvm.functions.Function2<? super java.lang.String, ? super java.lang.Boolean, kotlin.Unit> */
    /* JADX WARN: Multi-variable type inference failed */
    /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
    public ControlHolder(View view, ControlsModel.MoveHelper moveHelper, Function2<? super String, ? super Boolean, Unit> function2) {
        super(view, null);
        Intrinsics.checkNotNullParameter(view, "view");
        Intrinsics.checkNotNullParameter(function2, "favoriteCallback");
        this.moveHelper = moveHelper;
        this.favoriteCallback = function2;
        View requireViewById = this.itemView.requireViewById(R$id.icon);
        Intrinsics.checkNotNullExpressionValue(requireViewById, "itemView.requireViewById(R.id.icon)");
        this.icon = (ImageView) requireViewById;
        View requireViewById2 = this.itemView.requireViewById(R$id.title);
        Intrinsics.checkNotNullExpressionValue(requireViewById2, "itemView.requireViewById(R.id.title)");
        this.title = (TextView) requireViewById2;
        View requireViewById3 = this.itemView.requireViewById(R$id.subtitle);
        Intrinsics.checkNotNullExpressionValue(requireViewById3, "itemView.requireViewById(R.id.subtitle)");
        this.subtitle = (TextView) requireViewById3;
        View requireViewById4 = this.itemView.requireViewById(R$id.status);
        Intrinsics.checkNotNullExpressionValue(requireViewById4, "itemView.requireViewById(R.id.status)");
        this.removed = (TextView) requireViewById4;
        View requireViewById5 = this.itemView.requireViewById(R$id.favorite);
        CheckBox checkBox = (CheckBox) requireViewById5;
        checkBox.setVisibility(0);
        Unit unit = Unit.INSTANCE;
        Intrinsics.checkNotNullExpressionValue(requireViewById5, "itemView.requireViewById<CheckBox>(R.id.favorite).apply {\n        visibility = View.VISIBLE\n    }");
        this.favorite = checkBox;
        ControlHolderAccessibilityDelegate controlHolderAccessibilityDelegate = new ControlHolderAccessibilityDelegate(new Function1<Boolean, CharSequence>(this) { // from class: com.android.systemui.controls.management.ControlHolder$accessibilityDelegate$1
            public final CharSequence invoke(boolean z) {
                return ControlHolder.access$stateDescription((ControlHolder) this.receiver, z);
            }

            /* Return type fixed from 'java.lang.Object' to match base method */
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
            @Override // kotlin.jvm.functions.Function1
            public /* bridge */ /* synthetic */ CharSequence invoke(Boolean bool) {
                return invoke(bool.booleanValue());
            }
        }, new Function0<Integer>(this) { // from class: com.android.systemui.controls.management.ControlHolder$accessibilityDelegate$2
            /* Return type fixed from 'int' to match base method */
            /* JADX WARN: Type inference failed for: r0v3, types: [int, java.lang.Integer] */
            @Override // kotlin.jvm.functions.Function0
            public final Integer invoke() {
                return ((ControlHolder) this.receiver).getLayoutPosition();
            }
        }, moveHelper);
        this.accessibilityDelegate = controlHolderAccessibilityDelegate;
        ViewCompat.setAccessibilityDelegate(this.itemView, controlHolderAccessibilityDelegate);
    }

    public final CharSequence stateDescription(boolean z) {
        if (!z) {
            return this.notFavoriteStateDescription;
        }
        if (this.moveHelper == null) {
            return this.favoriteStateDescription;
        }
        return this.itemView.getContext().getString(R$string.accessibility_control_favorite_position, Integer.valueOf(getLayoutPosition() + 1));
    }

    @Override // com.android.systemui.controls.management.Holder
    public void bindData(ElementWrapper elementWrapper) {
        Intrinsics.checkNotNullParameter(elementWrapper, "wrapper");
        ControlInterface controlInterface = (ControlInterface) elementWrapper;
        RenderInfo renderInfo = getRenderInfo(controlInterface.getComponent(), controlInterface.getDeviceType());
        this.title.setText(controlInterface.getTitle());
        this.subtitle.setText(controlInterface.getSubtitle());
        updateFavorite(controlInterface.getFavorite());
        this.removed.setText(controlInterface.getRemoved() ? this.itemView.getContext().getText(R$string.controls_removed) : "");
        this.itemView.setOnClickListener(new View.OnClickListener(this, elementWrapper) { // from class: com.android.systemui.controls.management.ControlHolder$bindData$1
            final /* synthetic */ ElementWrapper $wrapper;
            final /* synthetic */ ControlHolder this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
                this.$wrapper = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlHolder controlHolder = this.this$0;
                controlHolder.updateFavorite(!controlHolder.favorite.isChecked());
                this.this$0.getFavoriteCallback().invoke(((ControlInterface) this.$wrapper).getControlId(), Boolean.valueOf(this.this$0.favorite.isChecked()));
            }
        });
        applyRenderInfo(renderInfo, controlInterface);
    }

    @Override // com.android.systemui.controls.management.Holder
    public void updateFavorite(boolean z) {
        this.favorite.setChecked(z);
        this.accessibilityDelegate.setFavorite(z);
        this.itemView.setStateDescription(stateDescription(z));
    }

    private final RenderInfo getRenderInfo(ComponentName componentName, int i) {
        RenderInfo.Companion companion = RenderInfo.Companion;
        Context context = this.itemView.getContext();
        Intrinsics.checkNotNullExpressionValue(context, "itemView.context");
        return RenderInfo.Companion.lookup$default(companion, context, componentName, i, 0, 8, null);
    }

    private final void applyRenderInfo(RenderInfo renderInfo, ControlInterface controlInterface) {
        Context context = this.itemView.getContext();
        ColorStateList colorStateList = context.getResources().getColorStateList(renderInfo.getForeground(), context.getTheme());
        Unit unit = null;
        this.icon.setImageTintList(null);
        Icon customIcon = controlInterface.getCustomIcon();
        if (customIcon != null) {
            this.icon.setImageIcon(customIcon);
            unit = Unit.INSTANCE;
        }
        if (unit == null) {
            this.icon.setImageDrawable(renderInfo.getIcon());
            if (controlInterface.getDeviceType() != 52) {
                this.icon.setImageTintList(colorStateList);
            }
        }
    }
}
