package com.google.android.systemui.assist.uihints;

import android.graphics.Rect;
import android.os.Handler;
import android.os.Looper;
import android.view.CompositionSamplingListener;
import android.view.SurfaceControl;
import com.android.systemui.screenshot.SaveImageInBackgroundTask$$ExternalSyntheticLambda0;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
/* access modifiers changed from: package-private */
/* loaded from: classes2.dex */
public final class LightnessProvider implements NgaMessageHandler.CardInfoListener {
    private LightnessListener mListener;
    private boolean mCardVisible = false;
    private int mColorMode = 0;
    private boolean mIsMonitoringColor = false;
    private boolean mMuted = false;
    private final Handler mUiHandler = new Handler(Looper.getMainLooper());
    private final CompositionSamplingListener mColorMonitor = new CompositionSamplingListener(SaveImageInBackgroundTask$$ExternalSyntheticLambda0.INSTANCE) { // from class: com.google.android.systemui.assist.uihints.LightnessProvider.1
        public void onSampleCollected(float f) {
            LightnessProvider.this.mUiHandler.post(new LightnessProvider$1$$ExternalSyntheticLambda0(this, f));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onSampleCollected$0(float f) {
            if (LightnessProvider.this.mListener != null && !LightnessProvider.this.mMuted) {
                if (!LightnessProvider.this.mCardVisible || LightnessProvider.this.mColorMode == 0) {
                    LightnessProvider.this.mListener.onLightnessUpdate(f);
                }
            }
        }
    };

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.CardInfoListener
    public void onCardInfo(boolean z, int i, boolean z2, boolean z3) {
        setCardVisible(z, i);
    }

    /* access modifiers changed from: package-private */
    public void setListener(LightnessListener lightnessListener) {
        this.mListener = lightnessListener;
    }

    /* access modifiers changed from: package-private */
    public void setMuted(boolean z) {
        this.mMuted = z;
    }

    /* access modifiers changed from: package-private */
    public void enableColorMonitoring(boolean z, Rect rect, SurfaceControl surfaceControl) {
        if (this.mIsMonitoringColor != z) {
            this.mIsMonitoringColor = z;
            if (z) {
                CompositionSamplingListener.register(this.mColorMonitor, 0, surfaceControl, rect);
            } else {
                CompositionSamplingListener.unregister(this.mColorMonitor);
            }
        }
    }

    void setCardVisible(boolean z, int i) {
        this.mCardVisible = z;
        this.mColorMode = i;
        LightnessListener lightnessListener = this.mListener;
        if (lightnessListener != null && z) {
            if (i == 1) {
                lightnessListener.onLightnessUpdate(0.0f);
            } else if (i == 2) {
                lightnessListener.onLightnessUpdate(1.0f);
            }
        }
    }
}
