package com.android.wm.shell.transition;

import android.os.IBinder;
import android.os.RemoteException;
import android.util.ArrayMap;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.IRemoteTransitionFinishedCallback;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class RemoteTransitionHandler implements Transitions.TransitionHandler {
    private final ShellExecutor mMainExecutor;
    private final ArrayMap<IBinder, IRemoteTransition> mRequestedRemotes = new ArrayMap<>();
    private final ArrayList<Pair<TransitionFilter, IRemoteTransition>> mFilters = new ArrayList<>();
    private final IBinder.DeathRecipient mTransitionDeathRecipient = new IBinder.DeathRecipient() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler.1
        public /* synthetic */ void lambda$binderDied$0() {
            RemoteTransitionHandler.this.mFilters.clear();
        }

        @Override // android.os.IBinder.DeathRecipient
        public void binderDied() {
            RemoteTransitionHandler.this.mMainExecutor.execute(new RemoteTransitionHandler$1$$ExternalSyntheticLambda0(this));
        }
    };

    public RemoteTransitionHandler(ShellExecutor shellExecutor) {
        this.mMainExecutor = shellExecutor;
    }

    public void addFiltered(TransitionFilter transitionFilter, IRemoteTransition iRemoteTransition) {
        try {
            iRemoteTransition.asBinder().linkToDeath(this.mTransitionDeathRecipient, 0);
            this.mFilters.add(new Pair<>(transitionFilter, iRemoteTransition));
        } catch (RemoteException unused) {
            Slog.e("RemoteTransitionHandler", "Failed to link to death");
        }
    }

    public void removeFiltered(IRemoteTransition iRemoteTransition) {
        boolean z = false;
        for (int size = this.mFilters.size() - 1; size >= 0; size--) {
            if (this.mFilters.get(size).second == iRemoteTransition) {
                this.mFilters.remove(size);
                z = true;
            }
        }
        if (z) {
            iRemoteTransition.asBinder().unlinkToDeath(this.mTransitionDeathRecipient, 0);
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void onTransitionMerged(IBinder iBinder) {
        this.mRequestedRemotes.remove(iBinder);
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public boolean startAnimation(final IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        final IRemoteTransition iRemoteTransition = this.mRequestedRemotes.get(iBinder);
        if (iRemoteTransition == null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1269886472, 0, "Transition %s doesn't have explicit remote, search filters for match for %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
            }
            int size = this.mFilters.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 990371881, 0, " Checking filter %s", String.valueOf(this.mFilters.get(size)));
                }
                if (((TransitionFilter) this.mFilters.get(size).first).matches(transitionInfo)) {
                    iRemoteTransition = (IRemoteTransition) this.mFilters.get(size).second;
                    this.mRequestedRemotes.put(iBinder, iRemoteTransition);
                    break;
                }
                size--;
            }
        }
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1671119352, 0, " Delegate animation for %s to %s", String.valueOf(iBinder), String.valueOf(iRemoteTransition));
        }
        if (iRemoteTransition == null) {
            return false;
        }
        final RemoteTransitionHandler$$ExternalSyntheticLambda0 remoteTransitionHandler$$ExternalSyntheticLambda0 = new IBinder.DeathRecipient(iBinder, transitionFinishCallback) { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$$ExternalSyntheticLambda0
            public final /* synthetic */ IBinder f$1;
            public final /* synthetic */ Transitions.TransitionFinishCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // android.os.IBinder.DeathRecipient
            public final void binderDied() {
                RemoteTransitionHandler.$r8$lambda$dNa1e2Zm7bFNZSER14hZDFPxXMg(RemoteTransitionHandler.this, this.f$1, this.f$2);
            }
        };
        AnonymousClass2 r4 = new IRemoteTransitionFinishedCallback.Stub() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler.2
            public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                if (iRemoteTransition.asBinder() != null) {
                    iRemoteTransition.asBinder().unlinkToDeath(remoteTransitionHandler$$ExternalSyntheticLambda0, 0);
                }
                RemoteTransitionHandler.this.mMainExecutor.execute(new RemoteTransitionHandler$2$$ExternalSyntheticLambda0(this, iBinder, transitionFinishCallback, windowContainerTransaction));
            }

            public /* synthetic */ void lambda$onTransitionFinished$0(IBinder iBinder2, Transitions.TransitionFinishCallback transitionFinishCallback2, WindowContainerTransaction windowContainerTransaction) {
                RemoteTransitionHandler.this.mRequestedRemotes.remove(iBinder2);
                transitionFinishCallback2.onTransitionFinished(windowContainerTransaction, null);
            }
        };
        try {
            if (iRemoteTransition.asBinder() != null) {
                iRemoteTransition.asBinder().linkToDeath(remoteTransitionHandler$$ExternalSyntheticLambda0, 0);
            }
            iRemoteTransition.startAnimation(iBinder, transitionInfo, transaction, r4);
        } catch (RemoteException e) {
            Log.e("ShellTransitions", "Error running remote transition.", e);
            if (iRemoteTransition.asBinder() != null) {
                iRemoteTransition.asBinder().unlinkToDeath(remoteTransitionHandler$$ExternalSyntheticLambda0, 0);
            }
            this.mRequestedRemotes.remove(iBinder);
            this.mMainExecutor.execute(new Runnable() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    RemoteTransitionHandler.lambda$startAnimation$2(Transitions.TransitionFinishCallback.this);
                }
            });
        }
        return true;
    }

    public /* synthetic */ void lambda$startAnimation$1(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        Log.e("ShellTransitions", "Remote transition died, finishing");
        this.mMainExecutor.execute(new Runnable(iBinder, transitionFinishCallback) { // from class: com.android.wm.shell.transition.RemoteTransitionHandler$$ExternalSyntheticLambda1
            public final /* synthetic */ IBinder f$1;
            public final /* synthetic */ Transitions.TransitionFinishCallback f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                RemoteTransitionHandler.this.lambda$startAnimation$0(this.f$1, this.f$2);
            }
        });
    }

    public /* synthetic */ void lambda$startAnimation$0(IBinder iBinder, Transitions.TransitionFinishCallback transitionFinishCallback) {
        this.mRequestedRemotes.remove(iBinder);
        transitionFinishCallback.onTransitionFinished(null, null);
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, final IBinder iBinder2, final Transitions.TransitionFinishCallback transitionFinishCallback) {
        IRemoteTransition iRemoteTransition = this.mRequestedRemotes.get(iBinder2);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -114556030, 0, " Attempt merge %s into %s", String.valueOf(iBinder), String.valueOf(iRemoteTransition));
        }
        if (iRemoteTransition != null) {
            try {
                iRemoteTransition.mergeAnimation(iBinder, transitionInfo, transaction, iBinder2, new IRemoteTransitionFinishedCallback.Stub() { // from class: com.android.wm.shell.transition.RemoteTransitionHandler.3
                    public void onTransitionFinished(WindowContainerTransaction windowContainerTransaction) {
                        RemoteTransitionHandler.this.mMainExecutor.execute(new RemoteTransitionHandler$3$$ExternalSyntheticLambda0(this, iBinder2, transitionFinishCallback, windowContainerTransaction));
                    }

                    public /* synthetic */ void lambda$onTransitionFinished$0(IBinder iBinder3, Transitions.TransitionFinishCallback transitionFinishCallback2, WindowContainerTransaction windowContainerTransaction) {
                        if (!RemoteTransitionHandler.this.mRequestedRemotes.containsKey(iBinder3)) {
                            Log.e("RemoteTransitionHandler", "Merged transition finished after it's mergeTarget (the transition it was supposed to merge into). This usually means that the mergeTarget's RemoteTransition impl erroneously accepted/ran the merge request after finishing the mergeTarget");
                        }
                        transitionFinishCallback2.onTransitionFinished(windowContainerTransaction, null);
                    }
                });
            } catch (RemoteException e) {
                Log.e("ShellTransitions", "Error attempting to merge remote transition.", e);
            }
        }
    }

    @Override // com.android.wm.shell.transition.Transitions.TransitionHandler
    public WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        IRemoteTransition remoteTransition = transitionRequestInfo.getRemoteTransition();
        if (remoteTransition == null) {
            return null;
        }
        this.mRequestedRemotes.put(iBinder, remoteTransition);
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 214412327, 0, "RemoteTransition directly requested for %s: %s", String.valueOf(iBinder), String.valueOf(remoteTransition));
        }
        return new WindowContainerTransaction();
    }
}
