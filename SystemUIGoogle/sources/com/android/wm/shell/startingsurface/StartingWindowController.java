package com.android.wm.shell.startingsurface;

import android.app.ActivityManager;
import android.app.TaskInfo;
import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.Trace;
import android.util.Slog;
import android.util.SparseIntArray;
import android.view.SurfaceControl;
import android.window.StartingWindowInfo;
import com.android.internal.annotations.GuardedBy;
import com.android.internal.util.function.TriConsumer;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.startingsurface.IStartingWindow;
/* loaded from: classes2.dex */
public class StartingWindowController implements RemoteCallable<StartingWindowController> {
    public static final boolean DEBUG_SPLASH_SCREEN = Build.isDebuggable();
    private static final String TAG = "StartingWindowController";
    private final Context mContext;
    private final ShellExecutor mSplashScreenExecutor;
    private final StartingSurfaceDrawer mStartingSurfaceDrawer;
    private final StartingWindowTypeAlgorithm mStartingWindowTypeAlgorithm;
    private TriConsumer<Integer, Integer, Integer> mTaskLaunchingCallback;
    private final StartingSurfaceImpl mImpl = new StartingSurfaceImpl();
    @GuardedBy({"mTaskBackgroundColors"})
    private final SparseIntArray mTaskBackgroundColors = new SparseIntArray();

    private static boolean isSplashScreenType(@StartingWindowInfo.StartingWindowType int i) {
        return i == 1 || i == 3 || i == 4;
    }

    public StartingWindowController(Context context, ShellExecutor shellExecutor, StartingWindowTypeAlgorithm startingWindowTypeAlgorithm, TransactionPool transactionPool) {
        this.mContext = context;
        this.mStartingSurfaceDrawer = new StartingSurfaceDrawer(context, shellExecutor, transactionPool);
        this.mStartingWindowTypeAlgorithm = startingWindowTypeAlgorithm;
        this.mSplashScreenExecutor = shellExecutor;
    }

    public StartingSurface asStartingSurface() {
        return this.mImpl;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public ShellExecutor getRemoteCallExecutor() {
        return this.mSplashScreenExecutor;
    }

    void setStartingWindowListener(TriConsumer<Integer, Integer, Integer> triConsumer) {
        this.mTaskLaunchingCallback = triConsumer;
    }

    public void addStartingWindow(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        this.mSplashScreenExecutor.execute(new Runnable(startingWindowInfo, iBinder) { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda4
            public final /* synthetic */ StartingWindowInfo f$1;
            public final /* synthetic */ IBinder f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$addStartingWindow$0(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addStartingWindow$0(StartingWindowInfo startingWindowInfo, IBinder iBinder) {
        Trace.traceBegin(32, "addStartingWindow");
        int suggestedWindowType = this.mStartingWindowTypeAlgorithm.getSuggestedWindowType(startingWindowInfo);
        ActivityManager.RunningTaskInfo runningTaskInfo = startingWindowInfo.taskInfo;
        if (isSplashScreenType(suggestedWindowType)) {
            this.mStartingSurfaceDrawer.addSplashScreenStartingWindow(startingWindowInfo, iBinder, suggestedWindowType);
        } else if (suggestedWindowType == 2) {
            this.mStartingSurfaceDrawer.makeTaskSnapshotWindow(startingWindowInfo, iBinder, startingWindowInfo.mTaskSnapshot);
        }
        if (suggestedWindowType != 0) {
            int i = runningTaskInfo.taskId;
            int startingWindowBackgroundColorForTask = this.mStartingSurfaceDrawer.getStartingWindowBackgroundColorForTask(i);
            if (startingWindowBackgroundColorForTask != 0) {
                synchronized (this.mTaskBackgroundColors) {
                    this.mTaskBackgroundColors.append(i, startingWindowBackgroundColorForTask);
                }
            }
            if (this.mTaskLaunchingCallback != null && isSplashScreenType(suggestedWindowType)) {
                this.mTaskLaunchingCallback.accept(Integer.valueOf(i), Integer.valueOf(suggestedWindowType), Integer.valueOf(startingWindowBackgroundColorForTask));
            }
        }
        Trace.traceEnd(32);
    }

    public void copySplashScreenView(int i) {
        this.mSplashScreenExecutor.execute(new Runnable(i) { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$copySplashScreenView$1(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$copySplashScreenView$1(int i) {
        this.mStartingSurfaceDrawer.copySplashScreenView(i);
    }

    public void onAppSplashScreenViewRemoved(int i) {
        this.mSplashScreenExecutor.execute(new Runnable(i) { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda0
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$onAppSplashScreenViewRemoved$2(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onAppSplashScreenViewRemoved$2(int i) {
        this.mStartingSurfaceDrawer.onAppSplashScreenViewRemoved(i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStartingWindow$3(int i, SurfaceControl surfaceControl, Rect rect, boolean z) {
        this.mStartingSurfaceDrawer.removeStartingWindow(i, surfaceControl, rect, z);
    }

    public void removeStartingWindow(int i, SurfaceControl surfaceControl, Rect rect, boolean z) {
        this.mSplashScreenExecutor.execute(new Runnable(i, surfaceControl, rect, z) { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda3
            public final /* synthetic */ int f$1;
            public final /* synthetic */ SurfaceControl f$2;
            public final /* synthetic */ Rect f$3;
            public final /* synthetic */ boolean f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$removeStartingWindow$3(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        });
        this.mSplashScreenExecutor.executeDelayed(new Runnable(i) { // from class: com.android.wm.shell.startingsurface.StartingWindowController$$ExternalSyntheticLambda2
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                StartingWindowController.this.lambda$removeStartingWindow$4(this.f$1);
            }
        }, 5000);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$removeStartingWindow$4(int i) {
        synchronized (this.mTaskBackgroundColors) {
            this.mTaskBackgroundColors.delete(i);
        }
    }

    /* loaded from: classes2.dex */
    private class StartingSurfaceImpl implements StartingSurface {
        private IStartingWindowImpl mIStartingWindow;

        private StartingSurfaceImpl() {
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        public IStartingWindowImpl createExternalInterface() {
            IStartingWindowImpl iStartingWindowImpl = this.mIStartingWindow;
            if (iStartingWindowImpl != null) {
                iStartingWindowImpl.invalidate();
            }
            IStartingWindowImpl iStartingWindowImpl2 = new IStartingWindowImpl(StartingWindowController.this);
            this.mIStartingWindow = iStartingWindowImpl2;
            return iStartingWindowImpl2;
        }

        @Override // com.android.wm.shell.startingsurface.StartingSurface
        public int getBackgroundColor(TaskInfo taskInfo) {
            synchronized (StartingWindowController.this.mTaskBackgroundColors) {
                int indexOfKey = StartingWindowController.this.mTaskBackgroundColors.indexOfKey(taskInfo.taskId);
                if (indexOfKey >= 0) {
                    return StartingWindowController.this.mTaskBackgroundColors.valueAt(indexOfKey);
                }
                int estimateTaskBackgroundColor = StartingWindowController.this.mStartingSurfaceDrawer.estimateTaskBackgroundColor(taskInfo);
                return estimateTaskBackgroundColor != 0 ? estimateTaskBackgroundColor : SplashscreenContentDrawer.getSystemBGColor();
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class IStartingWindowImpl extends IStartingWindow.Stub {
        private StartingWindowController mController;
        private IStartingWindowListener mListener;
        private final TriConsumer<Integer, Integer, Integer> mStartingWindowListener = new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda0(this);
        private final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.wm.shell.startingsurface.StartingWindowController.IStartingWindowImpl.1
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                StartingWindowController startingWindowController = IStartingWindowImpl.this.mController;
                startingWindowController.getRemoteCallExecutor().execute(new StartingWindowController$IStartingWindowImpl$1$$ExternalSyntheticLambda0(this, startingWindowController));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$binderDied$0(StartingWindowController startingWindowController) {
                IStartingWindowImpl.this.mListener = null;
                startingWindowController.setStartingWindowListener(null);
            }
        };

        public IStartingWindowImpl(StartingWindowController startingWindowController) {
            this.mController = startingWindowController;
        }

        void invalidate() {
            this.mController = null;
        }

        @Override // com.android.wm.shell.startingsurface.IStartingWindow
        public void setStartingWindowListener(IStartingWindowListener iStartingWindowListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setStartingWindowListener", new StartingWindowController$IStartingWindowImpl$$ExternalSyntheticLambda1(this, iStartingWindowListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setStartingWindowListener$0(IStartingWindowListener iStartingWindowListener, StartingWindowController startingWindowController) {
            IStartingWindowListener iStartingWindowListener2 = this.mListener;
            if (iStartingWindowListener2 != null) {
                iStartingWindowListener2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            if (iStartingWindowListener != null) {
                try {
                    iStartingWindowListener.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
                } catch (RemoteException unused) {
                    Slog.e(StartingWindowController.TAG, "Failed to link to death");
                    return;
                }
            }
            this.mListener = iStartingWindowListener;
            startingWindowController.setStartingWindowListener(this.mStartingWindowListener);
        }

        /* access modifiers changed from: private */
        public void notifyIStartingWindowListener(int i, int i2, int i3) {
            IStartingWindowListener iStartingWindowListener = this.mListener;
            if (iStartingWindowListener != null) {
                try {
                    iStartingWindowListener.onTaskLaunching(i, i2, i3);
                } catch (RemoteException e) {
                    Slog.e(StartingWindowController.TAG, "Failed to notify task launching", e);
                }
            }
        }
    }
}
