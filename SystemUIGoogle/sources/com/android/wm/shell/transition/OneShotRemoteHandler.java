package com.android.wm.shell.transition;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
/* loaded from: classes2.dex */
public class OneShotRemoteHandler implements Transitions.TransitionHandler {
    private final ShellExecutor mMainExecutor;
    private final IRemoteTransition mRemote;
    private IBinder mTransition = null;

    public OneShotRemoteHandler(ShellExecutor shellExecutor, IRemoteTransition iRemoteTransition) {
        this.mMainExecutor = shellExecutor;
        this.mRemote = iRemoteTransition;
    }

    public void setTransition(IBinder iBinder) {
        this.mTransition = iBinder;
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (this.mTransition != iBinder) {
            return false;
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", String.valueOf(this.mRemote), String.valueOf(iBinder));
        }
        final OneShotRemoteHandler$$ExternalSyntheticLambda0 oneShotRemoteHandler$$ExternalSyntheticLambda0 = new IBinder.DeathRecipient(transitionFinishCallback) { // from class: com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda0
            public final /* synthetic */ Transitions.TransitionFinishCallback f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                OneShotRemoteHandler.this.lambda$startAnimation$1(this.f$1);
            }
        };
        AnonymousClass1 r3 = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler.1
            public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                if (OneShotRemoteHandler.this.mRemote.asBinder() != null) {
                    OneShotRemoteHandler.this.mRemote.asBinder().unlinkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
                }
                OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$1$$ExternalSyntheticLambda0(transitionFinishCallback, windowContainerTransaction));
            }
        };
        try {
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().linkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
            }
            this.mRemote.startAnimation(iBinder, transitionInfo, transaction, r3);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            if (this.mRemote.asBinder() != null) {
                this.mRemote.asBinder().unlinkToDeath(oneShotRemoteHandler$$ExternalSyntheticLambda0, 0);
            }
            transitionFinishCallback.onTransitionFinished(null, null);
        }
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$1(Transitions.TransitionFinishCallback transitionFinishCallback) {
        Log.e("ShellTransitions", "Remote transition died, finishing");
        this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                Transitions.TransitionFinishCallback.this.onTransitionFinished(null, null);
            }
        });
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1649273831, 0, "Using registered One-shot remote transition %s for %s.", String.valueOf(this.mRemote), String.valueOf(iBinder));
        }
        try {
            this.mRemote.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new IRemoteTransitionFinishedCallback.Stub() { // from class: com.android.wm.shell.transition.OneShotRemoteHandler.2
                public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                    OneShotRemoteHandler.this.mMainExecutor.execute(new OneShotRemoteHandler$2$$ExternalSyntheticLambda0(transitionFinishCallback, windowContainerTransaction));
                }
            });
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error merging remote transition.", e);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        IRemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition != this.mRemote) {
            return null;
        }
        this.mTransition = iBinder;
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 967375804, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
