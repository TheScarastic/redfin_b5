package com.google.android.systemui.elmyra.sensors.config;

import android.content.Context;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public abstract class Adjustment {
    private Consumer<Adjustment> mCallback;
    private final Context mContext;

    public abstract float adjustSensitivity(float f);

    public Adjustment(Context context) {
        this.mContext = context;
    }

    public void setCallback(Consumer<Adjustment> consumer) {
        this.mCallback = consumer;
    }

    /* access modifiers changed from: protected */
    public void onSensitivityChanged() {
        Consumer<Adjustment> consumer = this.mCallback;
        if (consumer != null) {
            consumer.accept(this);
        }
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mContext;
    }
}
