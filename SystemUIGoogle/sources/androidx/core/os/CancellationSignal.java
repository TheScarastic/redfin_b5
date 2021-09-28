package androidx.core.os;

import android.os.Build;
/* loaded from: classes.dex */
public final class CancellationSignal {
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;

    /* loaded from: classes.dex */
    public interface OnCancelListener {
        void onCancel();
    }

    public boolean isCanceled() {
        boolean z;
        synchronized (this) {
            z = this.mIsCanceled;
        }
        return z;
    }

    public void cancel() {
        synchronized (this) {
            try {
                if (!this.mIsCanceled) {
                    this.mIsCanceled = true;
                    this.mCancelInProgress = true;
                    OnCancelListener onCancelListener = this.mOnCancelListener;
                    Object obj = this.mCancellationSignalObj;
                    if (onCancelListener != null) {
                        try {
                            onCancelListener.onCancel();
                        } catch (Throwable th) {
                            synchronized (this) {
                                try {
                                    this.mCancelInProgress = false;
                                    notifyAll();
                                    throw th;
                                } catch (Throwable th2) {
                                    throw th2;
                                }
                            }
                        }
                    }
                    if (obj != null && Build.VERSION.SDK_INT >= 16) {
                        ((android.os.CancellationSignal) obj).cancel();
                    }
                    synchronized (this) {
                        try {
                            this.mCancelInProgress = false;
                            notifyAll();
                        } catch (Throwable th3) {
                            throw th3;
                        }
                    }
                }
            } catch (Throwable th4) {
                throw th4;
            }
        }
    }

    public void setOnCancelListener(OnCancelListener onCancelListener) {
        synchronized (this) {
            waitForCancelFinishedLocked();
            if (this.mOnCancelListener != onCancelListener) {
                this.mOnCancelListener = onCancelListener;
                if (this.mIsCanceled && onCancelListener != null) {
                    onCancelListener.onCancel();
                }
            }
        }
    }

    private void waitForCancelFinishedLocked() {
        while (this.mCancelInProgress) {
            try {
                wait();
            } catch (InterruptedException unused) {
            }
        }
    }
}
