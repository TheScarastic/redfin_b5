package com.android.systemui.shared.system;

import android.graphics.HardwareRenderer;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.os.Trace;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewRootImpl;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class SyncRtSurfaceTransactionApplierCompat {
    public static final int FLAG_ALL = -1;
    public static final int FLAG_ALPHA = 1;
    public static final int FLAG_BACKGROUND_BLUR_RADIUS = 32;
    public static final int FLAG_CORNER_RADIUS = 16;
    public static final int FLAG_LAYER = 8;
    public static final int FLAG_MATRIX = 2;
    public static final int FLAG_RELATIVE_LAYER = 128;
    public static final int FLAG_SHADOW_RADIUS = 256;
    public static final int FLAG_VISIBILITY = 64;
    public static final int FLAG_WINDOW_CROP = 4;
    private static final int MSG_UPDATE_SEQUENCE_NUMBER = 0;
    private Runnable mAfterApplyCallback;
    private final Handler mApplyHandler;
    private final SurfaceControl mBarrierSurfaceControl;
    private final ViewRootImpl mTargetViewRootImpl;
    private int mSequenceNumber = 0;
    private int mPendingSequenceNumber = 0;

    /* loaded from: classes.dex */
    public static class SurfaceParams {
        public final float alpha;
        public final int backgroundBlurRadius;
        public final float cornerRadius;
        private final int flags;
        public final int layer;
        private final float[] mTmpValues;
        public final Matrix matrix;
        public final int relativeLayer;
        public final SurfaceControl relativeTo;
        public final float shadowRadius;
        public final SurfaceControl surface;
        public final boolean visible;
        public final Rect windowCrop;

        /* loaded from: classes.dex */
        public static class Builder {
            public float alpha;
            public int backgroundBlurRadius;
            public float cornerRadius;
            public int flags;
            public int layer;
            public Matrix matrix;
            public int relativeLayer;
            public SurfaceControl relativeTo;
            public float shadowRadius;
            public final SurfaceControl surface;
            public boolean visible;
            public Rect windowCrop;

            public Builder(SurfaceControlCompat surfaceControlCompat) {
                this(surfaceControlCompat.mSurfaceControl);
            }

            public SurfaceParams build() {
                return new SurfaceParams(this.surface, this.flags, this.alpha, this.matrix, this.windowCrop, this.layer, this.relativeTo, this.relativeLayer, this.cornerRadius, this.backgroundBlurRadius, this.visible, this.shadowRadius);
            }

            public Builder withAlpha(float f) {
                this.alpha = f;
                this.flags |= 1;
                return this;
            }

            public Builder withBackgroundBlur(int i) {
                this.backgroundBlurRadius = i;
                this.flags |= 32;
                return this;
            }

            public Builder withCornerRadius(float f) {
                this.cornerRadius = f;
                this.flags |= 16;
                return this;
            }

            public Builder withLayer(int i) {
                this.layer = i;
                this.flags |= 8;
                return this;
            }

            public Builder withMatrix(Matrix matrix) {
                this.matrix = new Matrix(matrix);
                this.flags |= 2;
                return this;
            }

            public Builder withRelativeLayerTo(SurfaceControl surfaceControl, int i) {
                this.relativeTo = surfaceControl;
                this.relativeLayer = i;
                this.flags |= 128;
                return this;
            }

            public Builder withShadowRadius(float f) {
                this.shadowRadius = f;
                this.flags |= 256;
                return this;
            }

            public Builder withVisibility(boolean z) {
                this.visible = z;
                this.flags |= 64;
                return this;
            }

            public Builder withWindowCrop(Rect rect) {
                this.windowCrop = new Rect(rect);
                this.flags |= 4;
                return this;
            }

            public Builder(SurfaceControl surfaceControl) {
                this.surface = surfaceControl;
            }
        }

        public void applyTo(SurfaceControl.Transaction transaction) {
            if ((this.flags & 2) != 0) {
                transaction.setMatrix(this.surface, this.matrix, this.mTmpValues);
            }
            if ((this.flags & 4) != 0) {
                transaction.setWindowCrop(this.surface, this.windowCrop);
            }
            if ((this.flags & 1) != 0) {
                transaction.setAlpha(this.surface, this.alpha);
            }
            if ((this.flags & 8) != 0) {
                transaction.setLayer(this.surface, this.layer);
            }
            if ((this.flags & 16) != 0) {
                transaction.setCornerRadius(this.surface, this.cornerRadius);
            }
            if ((this.flags & 32) != 0) {
                transaction.setBackgroundBlurRadius(this.surface, this.backgroundBlurRadius);
            }
            if ((this.flags & 64) != 0) {
                if (this.visible) {
                    transaction.show(this.surface);
                } else {
                    transaction.hide(this.surface);
                }
            }
            if ((this.flags & 128) != 0) {
                transaction.setRelativeLayer(this.surface, this.relativeTo, this.relativeLayer);
            }
            if ((this.flags & 256) != 0) {
                transaction.setShadowRadius(this.surface, this.shadowRadius);
            }
        }

        private SurfaceParams(SurfaceControl surfaceControl, int i, float f, Matrix matrix, Rect rect, int i2, SurfaceControl surfaceControl2, int i3, float f2, int i4, boolean z, float f3) {
            this.mTmpValues = new float[9];
            this.flags = i;
            this.surface = surfaceControl;
            this.alpha = f;
            this.matrix = matrix;
            this.windowCrop = rect;
            this.layer = i2;
            this.relativeTo = surfaceControl2;
            this.relativeLayer = i3;
            this.cornerRadius = f2;
            this.backgroundBlurRadius = i4;
            this.visible = z;
            this.shadowRadius = f3;
        }
    }

    public SyncRtSurfaceTransactionApplierCompat(View view) {
        SurfaceControl surfaceControl = null;
        ViewRootImpl viewRootImpl = view != null ? view.getViewRootImpl() : null;
        this.mTargetViewRootImpl = viewRootImpl;
        this.mBarrierSurfaceControl = viewRootImpl != null ? viewRootImpl.getSurfaceControl() : surfaceControl;
        this.mApplyHandler = new Handler(new Handler.Callback() { // from class: com.android.systemui.shared.system.SyncRtSurfaceTransactionApplierCompat.1
            @Override // android.os.Handler.Callback
            public boolean handleMessage(Message message) {
                if (message.what != 0) {
                    return false;
                }
                SyncRtSurfaceTransactionApplierCompat.this.onApplyMessage(message.arg1);
                return true;
            }
        });
    }

    public static void applyParams(TransactionCompat transactionCompat, SurfaceParams surfaceParams) {
        surfaceParams.applyTo(transactionCompat.mTransaction);
    }

    public static void create(final View view, final Consumer<SyncRtSurfaceTransactionApplierCompat> consumer) {
        if (view == null) {
            consumer.accept(null);
        } else if (view.getViewRootImpl() != null) {
            consumer.accept(new SyncRtSurfaceTransactionApplierCompat(view));
        } else {
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.shared.system.SyncRtSurfaceTransactionApplierCompat.4
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                    view.removeOnAttachStateChangeListener(this);
                    consumer.accept(new SyncRtSurfaceTransactionApplierCompat(view));
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public void onApplyMessage(int i) {
        Runnable runnable;
        this.mSequenceNumber = i;
        if (i == this.mPendingSequenceNumber && (runnable = this.mAfterApplyCallback) != null) {
            this.mAfterApplyCallback = null;
            runnable.run();
        }
    }

    public void addAfterApplyCallback(final Runnable runnable) {
        if (this.mSequenceNumber == this.mPendingSequenceNumber) {
            runnable.run();
            return;
        }
        final Runnable runnable2 = this.mAfterApplyCallback;
        if (runnable2 == null) {
            this.mAfterApplyCallback = runnable;
        } else {
            this.mAfterApplyCallback = new Runnable() { // from class: com.android.systemui.shared.system.SyncRtSurfaceTransactionApplierCompat.3
                @Override // java.lang.Runnable
                public void run() {
                    runnable.run();
                    runnable2.run();
                }
            };
        }
    }

    public void scheduleApply(final SurfaceParams... surfaceParamsArr) {
        ViewRootImpl viewRootImpl = this.mTargetViewRootImpl;
        if (viewRootImpl != null && viewRootImpl.getView() != null) {
            final int i = this.mPendingSequenceNumber + 1;
            this.mPendingSequenceNumber = i;
            this.mTargetViewRootImpl.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: com.android.systemui.shared.system.SyncRtSurfaceTransactionApplierCompat.2
                public void onFrameDraw(long j) {
                    if (SyncRtSurfaceTransactionApplierCompat.this.mBarrierSurfaceControl == null || !SyncRtSurfaceTransactionApplierCompat.this.mBarrierSurfaceControl.isValid()) {
                        Message.obtain(SyncRtSurfaceTransactionApplierCompat.this.mApplyHandler, 0, i, 0).sendToTarget();
                        return;
                    }
                    Trace.traceBegin(8, "Sync transaction frameNumber=" + j);
                    SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                    for (int length = surfaceParamsArr.length + -1; length >= 0; length--) {
                        surfaceParamsArr[length].applyTo(transaction);
                    }
                    if (SyncRtSurfaceTransactionApplierCompat.this.mTargetViewRootImpl != null) {
                        SyncRtSurfaceTransactionApplierCompat.this.mTargetViewRootImpl.mergeWithNextTransaction(transaction, j);
                    } else {
                        transaction.apply();
                    }
                    Trace.traceEnd(8);
                    Message.obtain(SyncRtSurfaceTransactionApplierCompat.this.mApplyHandler, 0, i, 0).sendToTarget();
                }
            });
            this.mTargetViewRootImpl.getView().invalidate();
        }
    }
}
