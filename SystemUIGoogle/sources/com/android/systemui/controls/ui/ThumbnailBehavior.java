package com.android.systemui.controls.ui;

import android.graphics.BlendMode;
import android.graphics.BlendModeColorFilter;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.ThumbnailTemplate;
import android.util.TypedValue;
import android.view.View;
import com.android.systemui.R$color;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.util.concurrency.DelayableExecutor;
import java.util.Objects;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ThumbnailBehavior.kt */
/* loaded from: classes.dex */
public final class ThumbnailBehavior implements Behavior {
    public Control control;
    public ControlViewHolder cvh;
    private int shadowColor;
    private float shadowOffsetX;
    private float shadowOffsetY;
    private float shadowRadius;
    public ThumbnailTemplate template;

    public final ThumbnailTemplate getTemplate() {
        ThumbnailTemplate thumbnailTemplate = this.template;
        if (thumbnailTemplate != null) {
            return thumbnailTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("template");
        throw null;
    }

    public final void setTemplate(ThumbnailTemplate thumbnailTemplate) {
        Intrinsics.checkNotNullParameter(thumbnailTemplate, "<set-?>");
        this.template = thumbnailTemplate;
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
        return getTemplate().isActive();
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void initialize(ControlViewHolder controlViewHolder) {
        Intrinsics.checkNotNullParameter(controlViewHolder, "cvh");
        setCvh(controlViewHolder);
        TypedValue typedValue = new TypedValue();
        controlViewHolder.getContext().getResources().getValue(R$dimen.controls_thumbnail_shadow_x, typedValue, true);
        this.shadowOffsetX = typedValue.getFloat();
        controlViewHolder.getContext().getResources().getValue(R$dimen.controls_thumbnail_shadow_y, typedValue, true);
        this.shadowOffsetY = typedValue.getFloat();
        controlViewHolder.getContext().getResources().getValue(R$dimen.controls_thumbnail_shadow_radius, typedValue, true);
        this.shadowRadius = typedValue.getFloat();
        this.shadowColor = controlViewHolder.getContext().getResources().getColor(R$color.control_thumbnail_shadow_color);
        controlViewHolder.getLayout().setOnClickListener(new View.OnClickListener(controlViewHolder, this) { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$initialize$1
            final /* synthetic */ ControlViewHolder $cvh;
            final /* synthetic */ ThumbnailBehavior this$0;

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
            }
        });
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control = controlWithState.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        ControlViewHolder cvh = getCvh();
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        ControlViewHolder.setStatusText$default(cvh, statusText, false, 2, null);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Objects.requireNonNull(controlTemplate, "null cannot be cast to non-null type android.service.controls.templates.ThumbnailTemplate");
        setTemplate((ThumbnailTemplate) controlTemplate);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Objects.requireNonNull(findDrawableByLayerId, "null cannot be cast to non-null type android.graphics.drawable.ClipDrawable");
        ClipDrawable clipDrawable = (ClipDrawable) findDrawableByLayerId;
        clipDrawable.setLevel(getEnabled() ? 10000 : 0);
        if (getTemplate().isActive()) {
            getCvh().getTitle().setVisibility(4);
            getCvh().getSubtitle().setVisibility(4);
            getCvh().getStatus().setShadowLayer(this.shadowOffsetX, this.shadowOffsetY, this.shadowRadius, this.shadowColor);
            getCvh().getBgExecutor().execute(new Runnable(this, clipDrawable, i) { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$bind$1
                final /* synthetic */ ClipDrawable $clipLayer;
                final /* synthetic */ int $colorOffset;
                final /* synthetic */ ThumbnailBehavior this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$clipLayer = r2;
                    this.$colorOffset = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    final Drawable loadDrawable = this.this$0.getTemplate().getThumbnail().loadDrawable(this.this$0.getCvh().getContext());
                    DelayableExecutor uiExecutor = this.this$0.getCvh().getUiExecutor();
                    final ThumbnailBehavior thumbnailBehavior = this.this$0;
                    final ClipDrawable clipDrawable2 = this.$clipLayer;
                    final int i2 = this.$colorOffset;
                    uiExecutor.execute(new Runnable() { // from class: com.android.systemui.controls.ui.ThumbnailBehavior$bind$1.1
                        @Override // java.lang.Runnable
                        public final void run() {
                            ClipDrawable clipDrawable3 = clipDrawable2;
                            Drawable drawable = loadDrawable;
                            Intrinsics.checkNotNullExpressionValue(drawable, "drawable");
                            clipDrawable3.setDrawable(new CornerDrawable(drawable, (float) thumbnailBehavior.getCvh().getContext().getResources().getDimensionPixelSize(R$dimen.control_corner_radius)));
                            clipDrawable2.setColorFilter(new BlendModeColorFilter(thumbnailBehavior.getCvh().getContext().getResources().getColor(R$color.control_thumbnail_tint), BlendMode.LUMINOSITY));
                            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(thumbnailBehavior.getCvh(), thumbnailBehavior.getEnabled(), i2, false, 4, null);
                        }
                    });
                }
            });
        } else {
            getCvh().getTitle().setVisibility(0);
            getCvh().getSubtitle().setVisibility(0);
            getCvh().getStatus().setShadowLayer(0.0f, 0.0f, 0.0f, this.shadowColor);
        }
        ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), getEnabled(), i, false, 4, null);
    }
}
