package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import android.os.Handler;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public abstract class Action {
    private final Context mContext;
    private final List<FeedbackEffect> mFeedbackEffects;
    private final Handler mHandler;
    private Listener mListener;

    /* loaded from: classes2.dex */
    public interface Listener {
        void onActionAvailabilityChanged(Action action);
    }

    public abstract boolean isAvailable();

    public void onProgress(float f, int i) {
    }

    public abstract void onTrigger(GestureSensor.DetectionProperties detectionProperties);

    public Action(Context context, List<FeedbackEffect> list) {
        ArrayList arrayList = new ArrayList();
        this.mFeedbackEffects = arrayList;
        this.mContext = context;
        this.mHandler = new Handler(context.getMainLooper());
        if (list != null) {
            arrayList.addAll(list);
        }
    }

    /* access modifiers changed from: protected */
    public void notifyListener() {
        if (this.mListener != null) {
            this.mHandler.post(new Runnable() { // from class: com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    Action.this.lambda$notifyListener$0();
                }
            });
        }
        if (!isAvailable()) {
            this.mHandler.post(new Runnable() { // from class: com.google.android.systemui.elmyra.actions.Action$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    Action.this.lambda$notifyListener$1();
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListener$0() {
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onActionAvailabilityChanged(this);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$notifyListener$1() {
        updateFeedbackEffects(0.0f, 0);
    }

    public void setListener(Listener listener) {
        this.mListener = listener;
    }

    /* access modifiers changed from: protected */
    public Context getContext() {
        return this.mContext;
    }

    /* access modifiers changed from: protected */
    public void updateFeedbackEffects(float f, int i) {
        int i2 = 0;
        if (f == 0.0f || i == 0) {
            while (i2 < this.mFeedbackEffects.size()) {
                this.mFeedbackEffects.get(i2).onRelease();
                i2++;
            }
        } else if (isAvailable()) {
            while (i2 < this.mFeedbackEffects.size()) {
                this.mFeedbackEffects.get(i2).onProgress(f, i);
                i2++;
            }
        }
    }

    /* access modifiers changed from: protected */
    public void triggerFeedbackEffects(GestureSensor.DetectionProperties detectionProperties) {
        if (isAvailable()) {
            for (int i = 0; i < this.mFeedbackEffects.size(); i++) {
                this.mFeedbackEffects.get(i).onResolve(detectionProperties);
            }
        }
    }

    public String toString() {
        return getClass().getSimpleName();
    }
}
