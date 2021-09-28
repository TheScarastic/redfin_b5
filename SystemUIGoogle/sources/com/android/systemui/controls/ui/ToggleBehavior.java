package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleTemplate;
import android.util.Log;
import android.view.View;
import com.android.systemui.R$id;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ToggleBehavior.kt */
/* loaded from: classes.dex */
public final class ToggleBehavior implements Behavior {
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    public ToggleTemplate template;

    public final Drawable getClipLayer() {
        Drawable drawable = this.clipLayer;
        if (drawable != null) {
            return drawable;
        }
        Intrinsics.throwUninitializedPropertyAccessException("clipLayer");
        throw null;
    }

    public final void setClipLayer(Drawable drawable) {
        Intrinsics.checkNotNullParameter(drawable, "<set-?>");
        this.clipLayer = drawable;
    }

    public final ToggleTemplate getTemplate() {
        ToggleTemplate toggleTemplate = this.template;
        if (toggleTemplate != null) {
            return toggleTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        throw null;
    }

    public final void setTemplate(ToggleTemplate toggleTemplate) {
        Intrinsics.checkNotNullParameter(toggleTemplate, "<set-?>");
        this.template = toggleTemplate;
    }

    public final Control getControl() {
        Control control = this.control;
        if (control != null) {
            return control;
        }
        Intrinsics.throwUninitializedPropertyAccessException("control");
        throw null;
    }

    public final void setControl(Control control) {
        Intrinsics.checkNotNullParameter(control, "<set-?>");
        this.control = control;
    }

    public final ControlViewHolder getCvh() {
        ControlViewHolder controlViewHolder = this.cvh;
        if (controlViewHolder != null) {
            return controlViewHolder;
        }
        Intrinsics.throwUninitializedPropertyAccessException("cvh");
        throw null;
    }

    public final void setCvh(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "<set-?>");
        this.cvh = controlViewHolder;
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
        controlViewHolder.getLayout().setOnClickListener(new View.OnClickListener(controlViewHolder, this) { // from class: com.android.systemui.controls.ui.ToggleBehavior$initialize$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ ToggleBehavior this$0;

            /* access modifiers changed from: package-private */
            {
                this.$cvh = r1;
                this.this$0 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ControlActionCoordinator controlActionCoordinator = this.$cvh.getControlActionCoordinator();
                ControlViewHolder controlViewHolder2 = this.$cvh;
                String templateId = this.this$0.getTemplate().getTemplateId();
                Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
                controlActionCoordinator.toggle(controlViewHolder2, templateId, this.this$0.getTemplate().isChecked());
            }
        });
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(ControlWithState controlWithState, int i) {
        ToggleTemplate toggleTemplate;
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control = controlWithState.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        ControlViewHolder cvh = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        ControlViewHolder.setStatusText$default(cvh, statusText, false, 2, null);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        if (controlTemplate instanceof ToggleTemplate) {
            Intrinsics.checkNotNullExpressionValue(controlTemplate, "controlTemplate");
            toggleTemplate = (ToggleTemplate) controlTemplate;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            ControlTemplate template = ((TemperatureControlTemplate) controlTemplate).getTemplate();
            Objects.requireNonNull(template, "null cannot be cast to non-null type android.service.controls.templates.ToggleTemplate");
            toggleTemplate = (ToggleTemplate) template;
        } else {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return;
        }
        setTemplate(toggleTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
        setClipLayer(findDrawableByLayerId);
        getClipLayer().setLevel(10000);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getTemplate().isChecked(), i, false, 4, null);
    }
}
