package com.google.android.systemui.columbus.sensors.config;

import android.util.Range;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: GestureConfiguration.kt */
/* loaded from: classes2.dex */
public final class GestureConfiguration {
    public static final Companion Companion = new Companion(null);
    private static final Range<Float> SENSITIVITY_RANGE = Range.create(Float.valueOf(0.0f), Float.valueOf(1.0f));
    private final Function1<Adjustment, Unit> adjustmentCallback = new Function1<Adjustment, Unit>(this) { // from class: com.google.android.systemui.columbus.sensors.config.GestureConfiguration$adjustmentCallback$1
        final /* synthetic */ GestureConfiguration this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // kotlin.jvm.functions.Function1
        public /* bridge */ /* synthetic */ Unit invoke(Adjustment adjustment) {
            invoke(adjustment);
            return Unit.INSTANCE;
        }

        public final void invoke(Adjustment adjustment) {
            Intrinsics.checkNotNullParameter(adjustment, "it");
            GestureConfiguration.access$updateSensitivity(this.this$0);
        }
    };
    private final List<Adjustment> adjustments;
    private Listener listener;
    private float sensitivity;
    private final SensorConfiguration sensorConfiguration;

    /* compiled from: GestureConfiguration.kt */
    /* loaded from: classes2.dex */
    public interface Listener {
        void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration);
    }

    public GestureConfiguration(List<Adjustment> list, SensorConfiguration sensorConfiguration) {
        Intrinsics.checkNotNullParameter(list, "adjustments");
        Intrinsics.checkNotNullParameter(sensorConfiguration, "sensorConfiguration");
        this.adjustments = list;
        this.sensorConfiguration = sensorConfiguration;
        this.sensitivity = sensorConfiguration.defaultSensitivityValue;
        for (Adjustment adjustment : list) {
            adjustment.setCallback(this.adjustmentCallback);
        }
        updateSensitivity();
    }

    public final float getSensitivity() {
        return this.sensitivity;
    }

    public final void setListener(Listener listener) {
        this.listener = listener;
    }

    public final void updateSensitivity() {
        float f = this.sensorConfiguration.defaultSensitivityValue;
        for (Adjustment adjustment : this.adjustments) {
            Float clamp = SENSITIVITY_RANGE.clamp(Float.valueOf(adjustment.adjustSensitivity(f)));
            Intrinsics.checkNotNullExpressionValue(clamp, "SENSITIVITY_RANGE.clamp(it.adjustSensitivity(newSensitivity))");
            f = clamp.floatValue();
        }
        if (Math.abs(this.sensitivity - f) >= 0.05f) {
            this.sensitivity = f;
            Listener listener = this.listener;
            if (listener != null) {
                listener.onGestureConfigurationChanged(this);
            }
        }
    }

    /* compiled from: GestureConfiguration.kt */
    /* loaded from: classes2.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }
}
