package com.android.wm.shell.splitscreen;

import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Slog;
import android.window.IRemoteTransition;
import com.android.wm.shell.RootTaskDisplayAreaOrganizer;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.draganddrop.DragAndDropPolicy;
import com.android.wm.shell.splitscreen.ISplitScreen;
import com.android.wm.shell.splitscreen.SplitScreen;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class SplitScreenController implements DragAndDropPolicy.Starter, RemoteCallable<SplitScreenController> {
    private static final String TAG = "SplitScreenController";
    private final Context mContext;
    private final DisplayImeController mDisplayImeController;
    private final SplitScreenImpl mImpl = new SplitScreenImpl();
    private final ShellExecutor mMainExecutor;
    private final RootTaskDisplayAreaOrganizer mRootTDAOrganizer;
    private StageCoordinator mStageCoordinator;
    private final SyncTransactionQueue mSyncQueue;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final TransactionPool mTransactionPool;
    private final Transitions mTransitions;

    public SplitScreenController(ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, Context context, RootTaskDisplayAreaOrganizer rootTaskDisplayAreaOrganizer, ShellExecutor shellExecutor, DisplayImeController displayImeController, Transitions transitions, TransactionPool transactionPool) {
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSyncQueue = syncTransactionQueue;
        this.mContext = context;
        this.mRootTDAOrganizer = rootTaskDisplayAreaOrganizer;
        this.mMainExecutor = shellExecutor;
        this.mDisplayImeController = displayImeController;
        this.mTransitions = transitions;
        this.mTransactionPool = transactionPool;
    }

    public SplitScreen asSplitScreen() {
        return this.mImpl;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public Context getContext() {
        return this.mContext;
    }

    @Override // com.android.wm.shell.common.RemoteCallable
    public ShellExecutor getRemoteCallExecutor() {
        return this.mMainExecutor;
    }

    public void onOrganizerRegistered() {
        if (this.mStageCoordinator == null) {
            this.mStageCoordinator = new StageCoordinator(this.mContext, 0, this.mSyncQueue, this.mRootTDAOrganizer, this.mTaskOrganizer, this.mDisplayImeController, this.mTransitions, this.mTransactionPool);
        }
    }

    public boolean isSplitScreenVisible() {
        return this.mStageCoordinator.isSplitScreenVisible();
    }

    public boolean moveToSideStage(int i, int i2) {
        ActivityManager.RunningTaskInfo runningTaskInfo = this.mTaskOrganizer.getRunningTaskInfo(i);
        if (runningTaskInfo != null) {
            return moveToSideStage(runningTaskInfo, i2);
        }
        throw new IllegalArgumentException("Unknown taskId" + i);
    }

    public boolean moveToSideStage(ActivityManager.RunningTaskInfo runningTaskInfo, int i) {
        return this.mStageCoordinator.moveToSideStage(runningTaskInfo, i);
    }

    public boolean removeFromSideStage(int i) {
        return this.mStageCoordinator.removeFromSideStage(i);
    }

    public void setSideStagePosition(int i) {
        this.mStageCoordinator.setSideStagePosition(i);
    }

    public void setSideStageVisibility(boolean z) {
        this.mStageCoordinator.setSideStageVisibility(z);
    }

    public void exitSplitScreen() {
        this.mStageCoordinator.exitSplitScreen();
    }

    public void exitSplitScreenOnHide(boolean z) {
        this.mStageCoordinator.exitSplitScreenOnHide(z);
    }

    public void getStageBounds(Rect rect, Rect rect2) {
        this.mStageCoordinator.getStageBounds(rect, rect2);
    }

    public void registerSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.registerSplitScreenListener(splitScreenListener);
    }

    public void unregisterSplitScreenListener(SplitScreen.SplitScreenListener splitScreenListener) {
        this.mStageCoordinator.unregisterSplitScreenListener(splitScreenListener);
    }

    @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
    public void startTask(int i, int i2, int i3, Bundle bundle) {
        try {
            ActivityTaskManager.getService().startActivityFromRecents(i, resolveStartStage(i2, i3, bundle));
        } catch (RemoteException e) {
            Slog.e(TAG, "Failed to launch task", e);
        }
    }

    @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
    public void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) {
        try {
            ((LauncherApps) this.mContext.getSystemService(LauncherApps.class)).startShortcut(str, str2, null, resolveStartStage(i, i2, bundle), userHandle);
        } catch (ActivityNotFoundException e) {
            Slog.e(TAG, "Failed to launch shortcut", e);
        }
    }

    @Override // com.android.wm.shell.draganddrop.DragAndDropPolicy.Starter
    public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) {
        try {
            pendingIntent.send(this.mContext, 0, intent, null, null, null, resolveStartStage(i, i2, bundle));
        } catch (PendingIntent.CanceledException e) {
            Slog.e(TAG, "Failed to launch activity", e);
        }
    }

    private Bundle resolveStartStage(int i, int i2, Bundle bundle) {
        int i3 = 0;
        if (i != -1) {
            if (i == 0) {
                if (i2 != -1) {
                    if (i2 == 0) {
                        i3 = 1;
                    }
                    this.mStageCoordinator.setSideStagePosition(i3);
                } else {
                    i2 = this.mStageCoordinator.getMainStagePosition();
                }
                if (bundle == null) {
                    bundle = new Bundle();
                }
                this.mStageCoordinator.updateActivityOptions(bundle, i2);
                return bundle;
            } else if (i == 1) {
                if (i2 != -1) {
                    this.mStageCoordinator.setSideStagePosition(i2);
                } else {
                    i2 = this.mStageCoordinator.getSideStagePosition();
                }
                if (bundle == null) {
                    bundle = new Bundle();
                }
                this.mStageCoordinator.updateActivityOptions(bundle, i2);
                return bundle;
            } else {
                throw new IllegalArgumentException("Unknown stage=" + i);
            }
        } else if (i2 == -1) {
            this.mStageCoordinator.exitSplitScreen();
            return bundle;
        } else if (i2 == this.mStageCoordinator.getSideStagePosition()) {
            return resolveStartStage(1, i2, bundle);
        } else {
            return resolveStartStage(0, i2, bundle);
        }
    }

    public void dump(PrintWriter printWriter, String str) {
        printWriter.println(str + TAG);
        StageCoordinator stageCoordinator = this.mStageCoordinator;
        if (stageCoordinator != null) {
            stageCoordinator.dump(printWriter, str);
        }
    }

    /* loaded from: classes2.dex */
    private class SplitScreenImpl implements SplitScreen {
        private ISplitScreenImpl mISplitScreen;

        private SplitScreenImpl() {
        }

        @Override // com.android.wm.shell.splitscreen.SplitScreen
        public ISplitScreen createExternalInterface() {
            ISplitScreenImpl iSplitScreenImpl = this.mISplitScreen;
            if (iSplitScreenImpl != null) {
                iSplitScreenImpl.invalidate();
            }
            ISplitScreenImpl iSplitScreenImpl2 = new ISplitScreenImpl(SplitScreenController.this);
            this.mISplitScreen = iSplitScreenImpl2;
            return iSplitScreenImpl2;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class ISplitScreenImpl extends ISplitScreen.Stub {
        private SplitScreenController mController;
        private ISplitScreenListener mListener;
        private final SplitScreen.SplitScreenListener mSplitScreenListener = new SplitScreen.SplitScreenListener() { // from class: com.android.wm.shell.splitscreen.SplitScreenController.ISplitScreenImpl.1
            @Override // com.android.wm.shell.splitscreen.SplitScreen.SplitScreenListener
            public void onStagePositionChanged(int i, int i2) {
                try {
                    if (ISplitScreenImpl.this.mListener != null) {
                        ISplitScreenImpl.this.mListener.onStagePositionChanged(i, i2);
                    }
                } catch (RemoteException e) {
                    Slog.e(SplitScreenController.TAG, "onStagePositionChanged", e);
                }
            }

            @Override // com.android.wm.shell.splitscreen.SplitScreen.SplitScreenListener
            public void onTaskStageChanged(int i, int i2, boolean z) {
                try {
                    if (ISplitScreenImpl.this.mListener != null) {
                        ISplitScreenImpl.this.mListener.onTaskStageChanged(i, i2, z);
                    }
                } catch (RemoteException e) {
                    Slog.e(SplitScreenController.TAG, "onTaskStageChanged", e);
                }
            }
        };
        private final IBinder.DeathRecipient mListenerDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.wm.shell.splitscreen.SplitScreenController.ISplitScreenImpl.2
            @Override // android.os.IBinder.DeathRecipient
            public void binderDied() {
                SplitScreenController splitScreenController = ISplitScreenImpl.this.mController;
                splitScreenController.getRemoteCallExecutor().execute(new SplitScreenController$ISplitScreenImpl$2$$ExternalSyntheticLambda0(this, splitScreenController));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$binderDied$0(SplitScreenController splitScreenController) {
                ISplitScreenImpl.this.mListener = null;
                splitScreenController.unregisterSplitScreenListener(ISplitScreenImpl.this.mSplitScreenListener);
            }
        };

        public ISplitScreenImpl(SplitScreenController splitScreenController) {
            this.mController = splitScreenController;
        }

        void invalidate() {
            this.mController = null;
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void registerSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "registerSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda5(this, iSplitScreenListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerSplitScreenListener$0(ISplitScreenListener iSplitScreenListener, SplitScreenController splitScreenController) {
            ISplitScreenListener iSplitScreenListener2 = this.mListener;
            if (iSplitScreenListener2 != null) {
                iSplitScreenListener2.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            if (iSplitScreenListener != null) {
                try {
                    iSplitScreenListener.asBinder().linkToDeath(this.mListenerDeathRecipient, 0);
                } catch (RemoteException unused) {
                    Slog.e(SplitScreenController.TAG, "Failed to link to death");
                    return;
                }
            }
            this.mListener = iSplitScreenListener;
            splitScreenController.registerSplitScreenListener(this.mSplitScreenListener);
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void unregisterSplitScreenListener(ISplitScreenListener iSplitScreenListener) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "unregisterSplitScreenListener", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda4(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$unregisterSplitScreenListener$1(SplitScreenController splitScreenController) {
            ISplitScreenListener iSplitScreenListener = this.mListener;
            if (iSplitScreenListener != null) {
                iSplitScreenListener.asBinder().unlinkToDeath(this.mListenerDeathRecipient, 0);
            }
            this.mListener = null;
            splitScreenController.unregisterSplitScreenListener(this.mSplitScreenListener);
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void exitSplitScreen() {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreen", SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda9.INSTANCE);
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void exitSplitScreenOnHide(boolean z) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "exitSplitScreenOnHide", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda7(z));
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void setSideStageVisibility(boolean z) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "setSideStageVisibility", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda8(z));
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void removeFromSideStage(int i) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "removeFromSideStage", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda0(i));
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void startTask(int i, int i2, int i3, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTask", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda1(i, i2, i3, bundle));
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void startTasks(int i, Bundle bundle, int i2, Bundle bundle2, int i3, IRemoteTransition iRemoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startTasks", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda2(i, bundle, i2, bundle2, i3, iRemoteTransition));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$startTasks$7(int i, Bundle bundle, int i2, Bundle bundle2, int i3, IRemoteTransition iRemoteTransition, SplitScreenController splitScreenController) {
            splitScreenController.mStageCoordinator.startTasks(i, bundle, i2, bundle2, i3, iRemoteTransition);
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void startShortcut(String str, String str2, int i, int i2, Bundle bundle, UserHandle userHandle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startShortcut", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda6(str, str2, i, i2, bundle, userHandle));
        }

        @Override // com.android.wm.shell.splitscreen.ISplitScreen
        public void startIntent(PendingIntent pendingIntent, Intent intent, int i, int i2, Bundle bundle) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mController, "startIntent", new SplitScreenController$ISplitScreenImpl$$ExternalSyntheticLambda3(pendingIntent, intent, i, i2, bundle));
        }
    }
}
