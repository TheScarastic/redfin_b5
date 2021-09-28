package com.android.systemui.controls.ui;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.StatelessTemplate;
import android.view.View;
import com.android.systemui.R$id;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: TouchBehavior.kt */
/* loaded from: classes.dex */
public final class TouchBehavior implements Behavior {
    public static final Companion Companion = new Companion(null);
    public Drawable clipLayer;
    public Control control;
    public ControlViewHolder cvh;
    private int lastColorOffset;
    private boolean statelessTouch;
    public ControlTemplate template;

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

    public final ControlTemplate getTemplate() {
        ControlTemplate controlTemplate = this.template;
        if (controlTemplate != null) {
            return controlTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        throw null;
    }

    public final void setTemplate(ControlTemplate controlTemplate) {
        Intrinsics.checkNotNullParameter(controlTemplate, "<set-?>");
        this.template = controlTemplate;
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

    /* access modifiers changed from: private */
    public final boolean getEnabled() {
        return this.lastColorOffset > 0 || this.statelessTouch;
    }

    /* compiled from: TouchBehavior.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
        controlViewHolder.getLayout().setOnClickListener(new View.OnClickListener(controlViewHolder, this) { // from class: com.android.systemui.controls.ui.TouchBehavior$initialize$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ TouchBehavior this$0;

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
                controlActionCoordinator.touch(controlViewHolder2, templateId, this.this$0.getControl());
                if (this.this$0.getTemplate() instanceof StatelessTemplate) {
                    this.this$0.statelessTouch = true;
                    ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(this.$cvh, this.this$0.getEnabled(), this.this$0.lastColorOffset, false, 4, null);
                    DelayableExecutor uiExecutor = this.$cvh.getUiExecutor();
                    final TouchBehavior touchBehavior = this.this$0;
                    final ControlViewHolder controlViewHolder3 = this.$cvh;
                    uiExecutor.executeDelayed(new Runnable() { // from class: com.android.systemui.controls.ui.TouchBehavior$initialize$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            touchBehavior.statelessTouch = false;
                            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(controlViewHolder3, touchBehavior.getEnabled(), touchBehavior.lastColorOffset, false, 4, null);
                        }
                    }, 3000);
                }
            }
        });
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control = controlWithState.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        this.lastColorOffset = i;
        ControlViewHolder cvh = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        int i2 = 0;
        ControlViewHolder.setStatusText$default(cvh, statusText, false, 2, null);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Intrinsics.checkNotNullExpressionValue(controlTemplate, "control.getControlTemplate()");
        setTemplate(controlTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
        setClipLayer(findDrawableByLayerId);
        Drawable clipLayer = getClipLayer();
        if (getEnabled()) {
            i2 = 10000;
        }
        clipLayer.setLevel(i2);
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getEnabled(), i, false, 4, null);
    }
}
