package com.android.wm.shell.common;

import android.util.Slog;
import android.view.SurfaceControl;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public final class SyncTransactionQueue {
    private final ShellExecutor mMainExecutor;
    private final TransactionPool mTransactionPool;
    private final ArrayList<SyncCallback> mQueue = new ArrayList<>();
    private SyncCallback mInFlight = null;
    private final ArrayList<TransactionRunnable> mRunnables = new ArrayList<>();
    private final Runnable mOnReplyTimeout = new Runnable() { // from class: com.android.wm.shell.common.SyncTransactionQueue$$ExternalSyntheticLambda0
        @Override // java.lang.Runnable
        public final void run() {
            SyncTransactionQueue.this.lambda$new$0();
        }
    };

    /* loaded from: classes2.dex */
    public interface TransactionRunnable {
        void runWithTransaction(SurfaceControl.Transaction transaction);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        synchronized (this.mQueue) {
            SyncCallback syncCallback = this.mInFlight;
            if (syncCallback != null && this.mQueue.contains(syncCallback)) {
                Slog.w("SyncTransactionQueue", "Sync Transaction timed-out: " + this.mInFlight.mWCT);
                SyncCallback syncCallback2 = this.mInFlight;
                syncCallback2.onTransactionReady(syncCallback2.mId, new SurfaceControl.Transaction());
            }
        }
    }

    public SyncTransactionQueue(TransactionPool transactionPool, ShellExecutor shellExecutor) {
        this.mTransactionPool = transactionPool;
        this.mMainExecutor = shellExecutor;
    }

    public void queue(WindowContainerTransaction windowContainerTransaction) {
        SyncCallback syncCallback = new SyncCallback(windowContainerTransaction);
        synchronized (this.mQueue) {
            this.mQueue.add(syncCallback);
            if (this.mQueue.size() == 1) {
                syncCallback.send();
            }
        }
    }

    public boolean queueIfWaiting(WindowContainerTransaction windowContainerTransaction) {
        synchronized (this.mQueue) {
            if (this.mQueue.isEmpty()) {
                return false;
            }
            SyncCallback syncCallback = new SyncCallback(windowContainerTransaction);
            this.mQueue.add(syncCallback);
            if (this.mQueue.size() == 1) {
                syncCallback.send();
            }
            return true;
        }
    }

    public void runInSync(TransactionRunnable transactionRunnable) {
        synchronized (this.mQueue) {
            if (this.mInFlight != null) {
                this.mRunnables.add(transactionRunnable);
                return;
            }
            SurfaceControl.Transaction acquire = this.mTransactionPool.acquire();
            transactionRunnable.runWithTransaction(acquire);
            acquire.apply();
            this.mTransactionPool.release(acquire);
        }
    }

    /* access modifiers changed from: private */
    public void onTransactionReceived(SurfaceControl.Transaction transaction) {
        int size = this.mRunnables.size();
        for (int i = 0; i < size; i++) {
            this.mRunnables.get(i).runWithTransaction(transaction);
        }
        this.mRunnables.clear();
        transaction.apply();
        transaction.close();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SyncCallback extends WindowContainerTransactionCallback {
        int mId = -1;
        final WindowContainerTransaction mWCT;

        SyncCallback(WindowContainerTransaction windowContainerTransaction) {
            this.mWCT = windowContainerTransaction;
        }

        void send() {
            if (SyncTransactionQueue.this.mInFlight == null) {
                SyncTransactionQueue.this.mInFlight = this;
                this.mId = new WindowOrganizer().applySyncTransaction(this.mWCT, this);
                SyncTransactionQueue.this.mMainExecutor.executeDelayed(SyncTransactionQueue.this.mOnReplyTimeout, 5300);
                return;
            }
            throw new IllegalStateException("Sync Transactions must be serialized. In Flight: " + SyncTransactionQueue.this.mInFlight.mId + " - " + SyncTransactionQueue.this.mInFlight.mWCT);
        }

        public void onTransactionReady(int i, SurfaceControl.Transaction transaction) {
            SyncTransactionQueue.this.mMainExecutor.execute(new SyncTransactionQueue$SyncCallback$$ExternalSyntheticLambda0(this, i, transaction));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransactionReady$0(int i, SurfaceControl.Transaction transaction) {
            synchronized (SyncTransactionQueue.this.mQueue) {
                if (this.mId != i) {
                    Slog.e("SyncTransactionQueue", "Got an unexpected onTransactionReady. Expected " + this.mId + " but got " + i);
                    return;
                }
                SyncTransactionQueue.this.mInFlight = null;
                SyncTransactionQueue.this.mMainExecutor.removeCallbacks(SyncTransactionQueue.this.mOnReplyTimeout);
                SyncTransactionQueue.this.mQueue.remove(this);
                SyncTransactionQueue.this.onTransactionReceived(transaction);
                if (!SyncTransactionQueue.this.mQueue.isEmpty()) {
                    ((SyncCallback) SyncTransactionQueue.this.mQueue.get(0)).send();
                }
            }
        }
    }
}
