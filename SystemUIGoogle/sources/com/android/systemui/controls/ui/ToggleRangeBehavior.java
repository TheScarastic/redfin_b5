package com.android.systemui.controls.ui;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.service.controls.Control;
import android.service.controls.templates.ControlTemplate;
import android.service.controls.templates.RangeTemplate;
import android.service.controls.templates.TemperatureControlTemplate;
import android.service.controls.templates.ToggleRangeTemplate;
import android.util.Log;
import android.util.MathUtils;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.controls.ui.ToggleRangeBehavior;
import java.util.Arrays;
import java.util.IllegalFormatException;
import java.util.Objects;
import kotlin.Unit;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.StringCompanionObject;
/* compiled from: ToggleRangeBehavior.kt */
/* loaded from: classes.dex */
public final class ToggleRangeBehavior implements Behavior {
    public static final Companion Companion = new Companion(null);
    public Drawable clipLayer;
    private int colorOffset;
    public Context context;
    public Control control;
    public ControlViewHolder cvh;
    private boolean isChecked;
    private boolean isToggleable;
    private ValueAnimator rangeAnimator;
    public RangeTemplate rangeTemplate;
    public String templateId;
    private CharSequence currentStatusText = "";
    private String currentRangeValue = "";

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

    public final String getTemplateId() {
        String str = this.templateId;
        if (str != null) {
            return str;
        }
        Intrinsics.throwUninitializedPropertyAccessException("templateId");
        throw null;
    }

    public final void setTemplateId(String str) {
        Intrinsics.checkNotNullParameter(str, "<set-?>");
        this.templateId = str;
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

    public final RangeTemplate getRangeTemplate() {
        RangeTemplate rangeTemplate = this.rangeTemplate;
        if (rangeTemplate != null) {
            return rangeTemplate;
        }
        Intrinsics.throwUninitializedPropertyAccessException("rangeTemplate");
        throw null;
    }

    public final void setRangeTemplate(RangeTemplate rangeTemplate) {
        Intrinsics.checkNotNullParameter(rangeTemplate, "<set-?>");
        this.rangeTemplate = rangeTemplate;
    }

    public final Context getContext() {
        Context context = this.context;
        if (context != null) {
            return context;
        }
        Intrinsics.throwUninitializedPropertyAccessException("context");
        throw null;
    }

    public final void setContext(Context context) {
        Intrinsics.checkNotNullParameter(context, "<set-?>");
        this.context = context;
    }

    public final boolean isChecked() {
        return this.isChecked;
    }

    public final boolean isToggleable() {
        return this.isToggleable;
    }

    /* compiled from: ToggleRangeBehavior.kt */
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
        setContext(controlViewHolder.getContext());
        ToggleRangeGestureListener toggleRangeGestureListener = new ToggleRangeGestureListener(this, controlViewHolder.getLayout());
        controlViewHolder.getLayout().setOnTouchListener(new View.OnTouchListener(new GestureDetector(getContext(), toggleRangeGestureListener), toggleRangeGestureListener, this) { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$initialize$1
            final /* synthetic */ GestureDetector $gestureDetector;
            final /* synthetic */ ToggleRangeBehavior.ToggleRangeGestureListener $gestureListener;
            final /* synthetic */ ToggleRangeBehavior this$0;

            /* access modifiers changed from: package-private */
            {
                this.$gestureDetector = r1;
                this.$gestureListener = r2;
                this.this$0 = r3;
            }

            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                Intrinsics.checkNotNullParameter(view, "v");
                Intrinsics.checkNotNullParameter(motionEvent, "e");
                if (!this.$gestureDetector.onTouchEvent(motionEvent) && motionEvent.getAction() == 1 && this.$gestureListener.isDragging()) {
                    view.getParent().requestDisallowInterceptTouchEvent(false);
                    this.$gestureListener.setDragging(false);
                    this.this$0.endUpdateRange();
                }
                return false;
            }
        });
    }

    private final void setup(ToggleRangeTemplate toggleRangeTemplate) {
        RangeTemplate range = toggleRangeTemplate.getRange();
        Intrinsics.checkNotNullExpressionValue(range, "template.getRange()");
        setRangeTemplate(range);
        this.isToggleable = true;
        this.isChecked = toggleRangeTemplate.isChecked();
    }

    private final void setup(RangeTemplate rangeTemplate) {
        setRangeTemplate(rangeTemplate);
        this.isChecked = !(getRangeTemplate().getCurrentValue() == getRangeTemplate().getMinValue());
    }

    private final boolean setupTemplate(ControlTemplate controlTemplate) {
        if (controlTemplate instanceof ToggleRangeTemplate) {
            setup((ToggleRangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof RangeTemplate) {
            setup((RangeTemplate) controlTemplate);
            return true;
        } else if (controlTemplate instanceof TemperatureControlTemplate) {
            ControlTemplate template = ((TemperatureControlTemplate) controlTemplate).getTemplate();
            Intrinsics.checkNotNullExpressionValue(template, "template.getTemplate()");
            return setupTemplate(template);
        } else {
            Log.e("ControlsUiController", Intrinsics.stringPlus("Unsupported template type: ", controlTemplate));
            return false;
        }
    }

    @Override // com.android.systemui.controls.ui.Behavior
    public void bind(ControlWithState controlWithState, int i) {
        Intrinsics.checkNotNullParameter(controlWithState, "cws");
        Control control = controlWithState.getControl();
        Intrinsics.checkNotNull(control);
        setControl(control);
        this.colorOffset = i;
        CharSequence statusText = getControl().getStatusText();
        Intrinsics.checkNotNullExpressionValue(statusText, "control.getStatusText()");
        this.currentStatusText = statusText;
        getCvh().getLayout().setOnLongClickListener(null);
        Drawable background = getCvh().getLayout().getBackground();
        Objects.requireNonNull(background, "null cannot be cast to non-null type android.graphics.drawable.LayerDrawable");
        Drawable findDrawableByLayerId = ((LayerDrawable) background).findDrawableByLayerId(R$id.clip_layer);
        Intrinsics.checkNotNullExpressionValue(findDrawableByLayerId, "ld.findDrawableByLayerId(R.id.clip_layer)");
        setClipLayer(findDrawableByLayerId);
        ControlTemplate controlTemplate = getControl().getControlTemplate();
        Intrinsics.checkNotNullExpressionValue(controlTemplate, "template");
        if (setupTemplate(controlTemplate)) {
            String templateId = controlTemplate.getTemplateId();
            Intrinsics.checkNotNullExpressionValue(templateId, "template.getTemplateId()");
            setTemplateId(templateId);
            updateRange(rangeToLevelValue(getRangeTemplate().getCurrentValue()), this.isChecked, false);
            ControlViewHolder.applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core$default(getCvh(), this.isChecked, i, false, 4, null);
            getCvh().getLayout().setAccessibilityDelegate(new View.AccessibilityDelegate(this) { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$bind$1
                final /* synthetic */ ToggleRangeBehavior this$0;

                @Override // android.view.View.AccessibilityDelegate
                public boolean onRequestSendAccessibilityEvent(ViewGroup viewGroup, View view, AccessibilityEvent accessibilityEvent) {
                    Intrinsics.checkNotNullParameter(viewGroup, "host");
                    Intrinsics.checkNotNullParameter(view, "child");
                    Intrinsics.checkNotNullParameter(accessibilityEvent, "event");
                    return true;
                }

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.view.View.AccessibilityDelegate
                public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfo accessibilityNodeInfo) {
                    Intrinsics.checkNotNullParameter(view, "host");
                    Intrinsics.checkNotNullParameter(accessibilityNodeInfo, "info");
                    super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfo);
                    int i2 = 0;
                    float f = this.this$0.levelToRangeValue(0);
                    ToggleRangeBehavior toggleRangeBehavior = this.this$0;
                    float f2 = toggleRangeBehavior.levelToRangeValue(toggleRangeBehavior.getClipLayer().getLevel());
                    float f3 = this.this$0.levelToRangeValue(10000);
                    double stepValue = (double) this.this$0.getRangeTemplate().getStepValue();
                    if (stepValue == Math.floor(stepValue)) {
                        i2 = 1;
                    }
                    int i3 = i2 ^ 1;
                    if (this.this$0.isChecked()) {
                        accessibilityNodeInfo.setRangeInfo(AccessibilityNodeInfo.RangeInfo.obtain(i3, f, f3, f2));
                    }
                    accessibilityNodeInfo.addAction(AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS);
                }

                @Override // android.view.View.AccessibilityDelegate
                public boolean performAccessibilityAction(View view, int i2, Bundle bundle) {
                    boolean z;
                    Intrinsics.checkNotNullParameter(view, "host");
                    if (i2 == 16) {
                        if (this.this$0.isToggleable()) {
                            this.this$0.getCvh().getControlActionCoordinator().toggle(this.this$0.getCvh(), this.this$0.getTemplateId(), this.this$0.isChecked());
                            z = true;
                        }
                        z = false;
                    } else {
                        if (i2 == 32) {
                            this.this$0.getCvh().getControlActionCoordinator().longPress(this.this$0.getCvh());
                        } else {
                            if (i2 == AccessibilityNodeInfo.AccessibilityAction.ACTION_SET_PROGRESS.getId() && bundle != null && bundle.containsKey("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE")) {
                                int i3 = this.this$0.rangeToLevelValue(bundle.getFloat("android.view.accessibility.action.ARGUMENT_PROGRESS_VALUE"));
                                ToggleRangeBehavior toggleRangeBehavior = this.this$0;
                                toggleRangeBehavior.updateRange(i3, toggleRangeBehavior.isChecked(), true);
                                this.this$0.endUpdateRange();
                            }
                            z = false;
                        }
                        z = true;
                    }
                    return z || super.performAccessibilityAction(view, i2, bundle);
                }
            });
        }
    }

    public final void beginUpdateRange() {
        getCvh().setUserInteractionInProgress(true);
        getCvh().setStatusTextSize((float) getContext().getResources().getDimensionPixelSize(R$dimen.control_status_expanded));
    }

    public final void updateRange(int i, boolean z, boolean z2) {
        int max = Math.max(0, Math.min(10000, i));
        if (getClipLayer().getLevel() == 0 && max > 0) {
            getCvh().applyRenderInfo$frameworks__base__packages__SystemUI__android_common__SystemUI_core(z, this.colorOffset, false);
        }
        ValueAnimator valueAnimator = this.rangeAnimator;
        if (valueAnimator != null) {
            valueAnimator.cancel();
        }
        if (z2) {
            boolean z3 = max == 0 || max == 10000;
            if (getClipLayer().getLevel() != max) {
                getCvh().getControlActionCoordinator().drag(z3);
                getClipLayer().setLevel(max);
            }
        } else if (max != getClipLayer().getLevel()) {
            ValueAnimator ofInt = ValueAnimator.ofInt(getCvh().getClipLayer().getLevel(), max);
            ofInt.addUpdateListener(new ValueAnimator.AnimatorUpdateListener(this) { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$updateRange$1$1
                final /* synthetic */ ToggleRangeBehavior this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.animation.ValueAnimator.AnimatorUpdateListener
                public final void onAnimationUpdate(ValueAnimator valueAnimator2) {
                    ClipDrawable clipLayer = this.this$0.getCvh().getClipLayer();
                    Object animatedValue = valueAnimator2.getAnimatedValue();
                    Objects.requireNonNull(animatedValue, "null cannot be cast to non-null type kotlin.Int");
                    clipLayer.setLevel(((Integer) animatedValue).intValue());
                }
            });
            ofInt.addListener(new AnimatorListenerAdapter(this) { // from class: com.android.systemui.controls.ui.ToggleRangeBehavior$updateRange$1$2
                final /* synthetic */ ToggleRangeBehavior this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
                public void onAnimationEnd(Animator animator) {
                    this.this$0.rangeAnimator = null;
                }
            });
            ofInt.setDuration(700L);
            ofInt.setInterpolator(Interpolators.CONTROL_STATE);
            ofInt.start();
            Unit unit = Unit.INSTANCE;
            this.rangeAnimator = ofInt;
        }
        if (z) {
            this.currentRangeValue = format(getRangeTemplate().getFormatString().toString(), "%.1f", levelToRangeValue(max));
            if (z2) {
                getCvh().setStatusText(this.currentRangeValue, true);
                return;
            }
            ControlViewHolder cvh = getCvh();
            ControlViewHolder.setStatusText$default(cvh, ((Object) this.currentStatusText) + ' ' + this.currentRangeValue, false, 2, null);
            return;
        }
        ControlViewHolder.setStatusText$default(getCvh(), this.currentStatusText, false, 2, null);
    }

    private final String format(String str, String str2, float f) {
        try {
            StringCompanionObject stringCompanionObject = StringCompanionObject.INSTANCE;
            String format = String.format(str, Arrays.copyOf(new Object[]{Float.valueOf(findNearestStep(f))}, 1));
            Intrinsics.checkNotNullExpressionValue(format, "java.lang.String.format(format, *args)");
            return format;
        } catch (IllegalFormatException e) {
            Log.w("ControlsUiController", "Illegal format in range template", e);
            if (Intrinsics.areEqual(str2, "")) {
                return "";
            }
            return format(str2, "", f);
        }
    }

    /* access modifiers changed from: private */
    public final float levelToRangeValue(int i) {
        return MathUtils.constrainedMap(getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), 0.0f, 10000.0f, (float) i);
    }

    /* access modifiers changed from: private */
    public final int rangeToLevelValue(float f) {
        return (int) MathUtils.constrainedMap(0.0f, 10000.0f, getRangeTemplate().getMinValue(), getRangeTemplate().getMaxValue(), f);
    }

    public final void endUpdateRange() {
        getCvh().setStatusTextSize((float) getContext().getResources().getDimensionPixelSize(R$dimen.control_status_normal));
        ControlViewHolder cvh = getCvh();
        cvh.setStatusText(((Object) this.currentStatusText) + ' ' + this.currentRangeValue, true);
        ControlActionCoordinator controlActionCoordinator = getCvh().getControlActionCoordinator();
        ControlViewHolder cvh2 = getCvh();
        String templateId = getRangeTemplate().getTemplateId();
        Intrinsics.checkNotNullExpressionValue(templateId, "rangeTemplate.getTemplateId()");
        controlActionCoordinator.setValue(cvh2, templateId, findNearestStep(levelToRangeValue(getClipLayer().getLevel())));
        getCvh().setUserInteractionInProgress(false);
    }

    public final float findNearestStep(float f) {
        float minValue = getRangeTemplate().getMinValue();
        float f2 = Float.MAX_VALUE;
        while (minValue <= getRangeTemplate().getMaxValue()) {
            float abs = Math.abs(f - minValue);
            if (abs >= f2) {
                return minValue - getRangeTemplate().getStepValue();
            }
            minValue += getRangeTemplate().getStepValue();
            f2 = abs;
        }
        return getRangeTemplate().getMaxValue();
    }

    /* compiled from: ToggleRangeBehavior.kt */
    /* loaded from: classes.dex */
    public final class ToggleRangeGestureListener extends GestureDetector.SimpleOnGestureListener {
        private boolean isDragging;
        final /* synthetic */ ToggleRangeBehavior this$0;
        private final View v;

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onDown(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "e");
            return true;
        }

        public ToggleRangeGestureListener(ToggleRangeBehavior toggleRangeBehavior, View view) {
            Intrinsics.checkNotNullParameter(toggleRangeBehavior, "this$0");
            Intrinsics.checkNotNullParameter(view, "v");
            this.this$0 = toggleRangeBehavior;
            this.v = view;
        }

        public final boolean isDragging() {
            return this.isDragging;
        }

        public final void setDragging(boolean z) {
            this.isDragging = z;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public void onLongPress(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "e");
            if (!this.isDragging) {
                this.this$0.getCvh().getControlActionCoordinator().longPress(this.this$0.getCvh());
            }
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            Intrinsics.checkNotNullParameter(motionEvent, "e1");
            Intrinsics.checkNotNullParameter(motionEvent2, "e2");
            if (!this.isDragging) {
                this.v.getParent().requestDisallowInterceptTouchEvent(true);
                this.this$0.beginUpdateRange();
                this.isDragging = true;
            }
            ToggleRangeBehavior toggleRangeBehavior = this.this$0;
            toggleRangeBehavior.updateRange(toggleRangeBehavior.getClipLayer().getLevel() + ((int) (((float) 10000) * ((-f) / ((float) this.v.getWidth())))), true, true);
            return true;
        }

        @Override // android.view.GestureDetector.SimpleOnGestureListener, android.view.GestureDetector.OnGestureListener
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            Intrinsics.checkNotNullParameter(motionEvent, "e");
            if (!this.this$0.isToggleable()) {
                return false;
            }
            this.this$0.getCvh().getControlActionCoordinator().toggle(this.this$0.getCvh(), this.this$0.getTemplateId(), this.this$0.isChecked());
            return true;
        }
    }
}
