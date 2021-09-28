package com.google.android.systemui.gamedashboard;

import android.view.SurfaceControlFpsListener;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class FpsController {
    private Callback mCallback;
    private final Executor mExecutor;
    private final SurfaceControlFpsListener mListener = new SurfaceControlFpsListener() { // from class: com.google.android.systemui.gamedashboard.FpsController.1
        public void onFpsReported(float f) {
            FpsController.this.mExecutor.execute(new FpsController$1$$ExternalSyntheticLambda0(this, f));
        }

        public /* synthetic */ void lambda$onFpsReported$0(float f) {
            if (FpsController.this.mCallback != null) {
                FpsController.this.mCallback.onFpsUpdated(f);
            }
        }
    };

    /* loaded from: classes2.dex */
    public interface Callback {
        void onFpsUpdated(float f);
    }

    public FpsController(Executor executor) {
        this.mExecutor = executor;
    }

    public void register(int i, Callback callback) {
        this.mListener.register(i);
        this.mCallback = callback;
    }

    public void unregister() {
        if (this.mCallback != null) {
            this.mListener.unregister();
            this.mCallback = null;
        }
    }
}
