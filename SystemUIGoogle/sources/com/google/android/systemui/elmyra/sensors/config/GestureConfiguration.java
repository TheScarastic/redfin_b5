package com.google.android.systemui.elmyra.sensors.config;

import android.content.Context;
import android.net.Uri;
import android.provider.Settings;
import android.util.Range;
import com.android.systemui.DejankUtils;
import com.google.android.systemui.elmyra.UserContentObserver;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
/* loaded from: classes2.dex */
public class GestureConfiguration {
    private static final Range<Float> SENSITIVITY_RANGE = Range.create(Float.valueOf(0.0f), Float.valueOf(1.0f));
    private final List<Adjustment> mAdjustments;
    private final Context mContext;
    private Listener mListener;
    private final UserContentObserver mSettingsObserver;
    private final Consumer<Adjustment> mAdjustmentCallback = new Consumer() { // from class: com.google.android.systemui.elmyra.sensors.config.GestureConfiguration$$ExternalSyntheticLambda2
        @Override // java.util.function.Consumer
        public final void accept(Object obj) {
            GestureConfiguration.this.lambda$new$0((Adjustment) obj);
        }
    };
    private float mSensitivity = getUserSensitivity();

    /* loaded from: classes2.dex */
    public interface Listener {
        void onGestureConfigurationChanged(GestureConfiguration gestureConfiguration);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(Adjustment adjustment) {
        onSensitivityChanged();
    }

    public GestureConfiguration(Context context, List<Adjustment> list) {
        this.mContext = context;
        ArrayList arrayList = new ArrayList(list);
        this.mAdjustments = arrayList;
        arrayList.forEach(new Consumer() { // from class: com.google.android.systemui.elmyra.sensors.config.GestureConfiguration$$ExternalSyntheticLambda1
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                GestureConfiguration.this.lambda$new$1((Adjustment) obj);
            }
        });
        this.mSettingsObserver = new UserContentObserver(context, Settings.Secure.getUriFor("assist_gesture_sensitivity"), new Consumer() { // from class: com.google.android.systemui.elmyra.sensors.config.GestureConfiguration$$ExternalSyntheticLambda0
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                GestureConfiguration.this.lambda$new$2((Uri) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(Adjustment adjustment) {
        adjustment.setCallback(this.mAdjustmentCallback);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(Uri uri) {
        onSensitivityChanged();
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    public float getSensitivity() {
        float f = this.mSensitivity;
        for (int i = 0; i < this.mAdjustments.size(); i++) {
            f = SENSITIVITY_RANGE.clamp(Float.valueOf(this.mAdjustments.get(i).adjustSensitivity(f))).floatValue();
        }
        return f;
    }

    private float getUserSensitivity() {
        float floatValue = ((Float) DejankUtils.whitelistIpcs(new Supplier() { // from class: com.google.android.systemui.elmyra.sensors.config.GestureConfiguration$$ExternalSyntheticLambda3
            @Override // java.util.function.Supplier
            public final Object get() {
                return GestureConfiguration.this.lambda$getUserSensitivity$3();
            }
        })).floatValue();
        if (!SENSITIVITY_RANGE.contains((Range<Float>) Float.valueOf(floatValue))) {
            return 0.5f;
        }
        return floatValue;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Float lambda$getUserSensitivity$3() {
        return Float.valueOf(Settings.Secure.getFloatForUser(this.mContext.getContentResolver(), "assist_gesture_sensitivity", 0.5f, -2));
    }

    public void onSensitivityChanged() {
        this.mSensitivity = getUserSensitivity();
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onGestureConfigurationChanged(this);
        }
    }
}
