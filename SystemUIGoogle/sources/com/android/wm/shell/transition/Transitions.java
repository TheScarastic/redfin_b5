package com.android.wm.shell.transition;

import android.content.ContentResolver;
import android.content.Context;
import android.database.ContentObserver;
import android.os.IBinder;
import android.os.RemoteException;
import android.os.SystemProperties;
import android.provider.Settings;
import android.util.Log;
import android.view.SurfaceControl;
import android.window.IRemoteTransition;
import android.window.ITransitionPlayer;
import android.window.TransitionFilter;
import android.window.TransitionInfo;
import android.window.TransitionRequestInfo;
import android.window.WindowContainerTransaction;
import android.window.WindowContainerTransactionCallback;
import android.window.WindowOrganizer;
import com.android.internal.annotations.VisibleForTesting;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.ExecutorUtils;
import com.android.wm.shell.common.RemoteCallable;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.protolog.ShellProtoLogCache;
import com.android.wm.shell.protolog.ShellProtoLogGroup;
import com.android.wm.shell.protolog.ShellProtoLogImpl;
import com.android.wm.shell.transition.IShellTransitions;
import com.android.wm.shell.transition.Transitions;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes2.dex */
public class Transitions implements RemoteCallable<Transitions> {
    public static final boolean ENABLE_SHELL_TRANSITIONS = SystemProperties.getBoolean("persist.debug.shell_transit", false);
    private final ArrayList<ActiveTransition> mActiveTransitions;
    private final ShellExecutor mAnimExecutor;
    private final Context mContext;
    private final ArrayList<TransitionHandler> mHandlers;
    private final ShellTransitionImpl mImpl;
    private final ShellExecutor mMainExecutor;
    private final WindowOrganizer mOrganizer;
    private final TransitionPlayerImpl mPlayerImpl;
    private final RemoteTransitionHandler mRemoteTransitionHandler;
    private float mTransitionAnimationScaleSetting;

    /* loaded from: classes2.dex */
    public interface TransitionFinishCallback {
        void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback);
    }

    /* loaded from: classes2.dex */
    public interface TransitionHandler {
        WindowContainerTransaction handleRequest(IBinder iBinder, TransitionRequestInfo transitionRequestInfo);

        default void mergeAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, IBinder iBinder2, TransitionFinishCallback transitionFinishCallback) {
        }

        default void onTransitionMerged(IBinder iBinder) {
        }

        default void setAnimScaleSetting(float f) {
        }

        boolean startAnimation(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, TransitionFinishCallback transitionFinishCallback);
    }

    public static boolean isClosingType(int i) {
        return i == 2 || i == 4;
    }

    public static boolean isOpeningType(int i) {
        return i == 1 || i == 3 || i == 7;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class ActiveTransition {
        SurfaceControl.Transaction mFinishT;
        TransitionHandler mHandler;
        TransitionInfo mInfo;
        boolean mMerged;
        SurfaceControl.Transaction mStartT;
        IBinder mToken;

        private ActiveTransition() {
            this.mToken = null;
            this.mHandler = null;
            this.mMerged = false;
            this.mInfo = null;
            this.mStartT = null;
            this.mFinishT = null;
        }
    }

    public Transitions(WindowOrganizer windowOrganizer, TransactionPool transactionPool, Context context, ShellExecutor shellExecutor, ShellExecutor shellExecutor2) {
        this.mImpl = new ShellTransitionImpl();
        ArrayList<TransitionHandler> arrayList = new ArrayList<>();
        this.mHandlers = arrayList;
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = windowOrganizer;
        this.mContext = context;
        this.mMainExecutor = shellExecutor;
        this.mAnimExecutor = shellExecutor2;
        this.mPlayerImpl = new TransitionPlayerImpl();
        arrayList.add(new DefaultTransitionHandler(transactionPool, context, shellExecutor, shellExecutor2));
        RemoteTransitionHandler remoteTransitionHandler = new RemoteTransitionHandler(shellExecutor);
        this.mRemoteTransitionHandler = remoteTransitionHandler;
        arrayList.add(remoteTransitionHandler);
        ContentResolver contentResolver = context.getContentResolver();
        float f = Settings.Global.getFloat(contentResolver, "transition_animation_scale", context.getResources().getFloat(17105060));
        this.mTransitionAnimationScaleSetting = f;
        dispatchAnimScaleSetting(f);
        contentResolver.registerContentObserver(Settings.Global.getUriFor("transition_animation_scale"), false, new SettingsObserver());
    }

    private Transitions() {
        this.mImpl = new ShellTransitionImpl();
        this.mHandlers = new ArrayList<>();
        this.mTransitionAnimationScaleSetting = 1.0f;
        this.mActiveTransitions = new ArrayList<>();
        this.mOrganizer = null;
        this.mContext = null;
        this.mMainExecutor = null;
        this.mAnimExecutor = null;
        this.mPlayerImpl = null;
        this.mRemoteTransitionHandler = null;
    }

    public ShellTransitions asRemoteTransitions() {
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

    /* access modifiers changed from: private */
    public void dispatchAnimScaleSetting(float f) {
        for (int size = this.mHandlers.size() - 1; size >= 0; size--) {
            this.mHandlers.get(size).setAnimScaleSetting(f);
        }
    }

    @VisibleForTesting
    public static ShellTransitions createEmptyForTesting() {
        return new ShellTransitions() { // from class: com.android.wm.shell.transition.Transitions.1
        };
    }

    public void register(ShellTaskOrganizer shellTaskOrganizer) {
        TransitionPlayerImpl transitionPlayerImpl = this.mPlayerImpl;
        if (transitionPlayerImpl != null) {
            shellTaskOrganizer.registerTransitionPlayer(transitionPlayerImpl);
        }
    }

    public void addHandler(TransitionHandler transitionHandler) {
        this.mHandlers.add(transitionHandler);
    }

    public ShellExecutor getMainExecutor() {
        return this.mMainExecutor;
    }

    public ShellExecutor getAnimExecutor() {
        return this.mAnimExecutor;
    }

    @VisibleForTesting
    void replaceDefaultHandlerForTest(TransitionHandler transitionHandler) {
        this.mHandlers.set(0, transitionHandler);
    }

    private static void setupStartState(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        for (int size = transitionInfo.getChanges().size() - 1; size >= 0; size--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size)).getMode();
            if (!TransitionInfo.isIndependent(change, transitionInfo)) {
                if (mode == 1 || mode == 3 || mode == 6) {
                    transaction.show(leash);
                    transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                    transaction.setAlpha(leash, 1.0f);
                    transaction.setPosition(leash, (float) change.getEndRelOffset().x, (float) change.getEndRelOffset().y);
                }
            } else if (mode == 1 || mode == 3) {
                transaction.show(leash);
                transaction.setMatrix(leash, 1.0f, 0.0f, 0.0f, 1.0f);
                if (isOpeningType && (change.getFlags() & 8) == 0) {
                    transaction.setAlpha(leash, 0.0f);
                    transaction2.setAlpha(leash, 1.0f);
                }
            } else if ((mode == 2 || mode == 4) && (change.getFlags() & 2) == 0) {
                transaction2.hide(leash);
            }
        }
    }

    private static void setupAnimHierarchy(TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        boolean isOpeningType = isOpeningType(transitionInfo.getType());
        if (transitionInfo.getRootLeash().isValid()) {
            transaction.show(transitionInfo.getRootLeash());
        }
        int size = transitionInfo.getChanges().size();
        for (int size2 = transitionInfo.getChanges().size() - 1; size2 >= 0; size2--) {
            TransitionInfo.Change change = (TransitionInfo.Change) transitionInfo.getChanges().get(size2);
            SurfaceControl leash = change.getLeash();
            int mode = ((TransitionInfo.Change) transitionInfo.getChanges().get(size2)).getMode();
            if (TransitionInfo.isIndependent(change, transitionInfo)) {
                if (!(change.getParent() != null)) {
                    transaction.reparent(leash, transitionInfo.getRootLeash());
                    transaction.setPosition(leash, (float) (change.getStartAbsBounds().left - transitionInfo.getRootOffset().x), (float) (change.getStartAbsBounds().top - transitionInfo.getRootOffset().y));
                }
                if (mode == 1 || mode == 3) {
                    if (isOpeningType) {
                        transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                    } else {
                        transaction.setLayer(leash, size - size2);
                    }
                } else if (mode != 2 && mode != 4) {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                } else if (isOpeningType) {
                    transaction.setLayer(leash, size - size2);
                } else {
                    transaction.setLayer(leash, (transitionInfo.getChanges().size() + size) - size2);
                }
            }
        }
    }

    private int findActiveTransition(IBinder iBinder) {
        for (int size = this.mActiveTransitions.size() - 1; size >= 0; size--) {
            if (this.mActiveTransitions.get(size).mToken == iBinder) {
                return size;
            }
        }
        return -1;
    }

    @VisibleForTesting
    void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1070270131, 0, "onTransitionReady %s: %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
        }
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            throw new IllegalStateException("Got transitionReady for non-active transition " + iBinder + ". expecting one of " + Arrays.toString(this.mActiveTransitions.stream().map(Transitions$$ExternalSyntheticLambda2.INSTANCE).toArray()));
        } else if (!transitionInfo.getRootLeash().isValid()) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 410592459, 0, "Invalid root leash (%s): %s", String.valueOf(iBinder), String.valueOf(transitionInfo));
            }
            transaction.apply();
            onAbort(iBinder);
        } else {
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mInfo = transitionInfo;
            activeTransition.mStartT = transaction;
            activeTransition.mFinishT = transaction2;
            setupStartState(transitionInfo, transaction, transaction2);
            if (findActiveTransition > 0) {
                attemptMergeTransition(this.mActiveTransitions.get(0), activeTransition);
            } else {
                playTransition(activeTransition);
            }
        }
    }

    void attemptMergeTransition(ActiveTransition activeTransition, ActiveTransition activeTransition2) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 313857748, 0, "Transition %s ready while another transition %s is still animating. Notify the animating transition in case they can be merged", String.valueOf(activeTransition2.mToken), String.valueOf(activeTransition.mToken));
        }
        activeTransition.mHandler.mergeAnimation(activeTransition2.mToken, activeTransition2.mInfo, activeTransition2.mStartT, activeTransition.mToken, new TransitionFinishCallback(activeTransition2) { // from class: com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda0
            public final /* synthetic */ Transitions.ActiveTransition f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
            public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
                Transitions.this.lambda$attemptMergeTransition$1(this.f$1, windowContainerTransaction, windowContainerTransactionCallback);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$attemptMergeTransition$1(ActiveTransition activeTransition, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(activeTransition.mToken, windowContainerTransaction, windowContainerTransactionCallback);
    }

    boolean startAnimation(ActiveTransition activeTransition, TransitionHandler transitionHandler) {
        return transitionHandler.startAnimation(activeTransition.mToken, activeTransition.mInfo, activeTransition.mStartT, new TransitionFinishCallback(activeTransition) { // from class: com.android.wm.shell.transition.Transitions$$ExternalSyntheticLambda1
            public final /* synthetic */ Transitions.ActiveTransition f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.wm.shell.transition.Transitions.TransitionFinishCallback
            public final void onTransitionFinished(WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
                Transitions.this.lambda$startAnimation$2(this.f$1, windowContainerTransaction, windowContainerTransactionCallback);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startAnimation$2(ActiveTransition activeTransition, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        onFinish(activeTransition.mToken, windowContainerTransaction, windowContainerTransactionCallback);
    }

    void playTransition(ActiveTransition activeTransition) {
        setupAnimHierarchy(activeTransition.mInfo, activeTransition.mStartT, activeTransition.mFinishT);
        TransitionHandler transitionHandler = activeTransition.mHandler;
        if (transitionHandler != null) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 138343607, 0, " try firstHandler %s", String.valueOf(transitionHandler));
            }
            if (startAnimation(activeTransition, activeTransition.mHandler)) {
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 707170340, 0, " animated by firstHandler", null);
                    return;
                }
                return;
            }
        }
        for (int size = this.mHandlers.size() - 1; size >= 0; size--) {
            if (this.mHandlers.get(size) != activeTransition.mHandler) {
                if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1308483871, 0, " try handler %s", String.valueOf(this.mHandlers.get(size)));
                }
                if (startAnimation(activeTransition, this.mHandlers.get(size))) {
                    if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -1297259344, 0, " animated by %s", String.valueOf(this.mHandlers.get(size)));
                    }
                    activeTransition.mHandler = this.mHandlers.get(size);
                    return;
                }
            }
        }
        throw new IllegalStateException("This shouldn't happen, maybe the default handler is broken.");
    }

    private void onAbort(IBinder iBinder) {
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition >= 0) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 977740158, 0, "Transition animation aborted due to no-op, notifying core %s", String.valueOf(iBinder));
            }
            this.mActiveTransitions.remove(findActiveTransition);
            this.mOrganizer.finishTransition(iBinder, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
        }
    }

    private void onFinish(IBinder iBinder, WindowContainerTransaction windowContainerTransaction, WindowContainerTransactionCallback windowContainerTransactionCallback) {
        int findActiveTransition = findActiveTransition(iBinder);
        if (findActiveTransition < 0) {
            Log.e("ShellTransitions", "Trying to finish a non-running transition. Either remote crashed or  a handler didn't properly deal with a merge.", new RuntimeException());
        } else if (findActiveTransition > 0) {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1739415176, 0, "Transition was merged: %s", String.valueOf(iBinder));
            }
            ActiveTransition activeTransition = this.mActiveTransitions.get(findActiveTransition);
            activeTransition.mMerged = true;
            TransitionHandler transitionHandler = activeTransition.mHandler;
            if (transitionHandler != null) {
                transitionHandler.onTransitionMerged(activeTransition.mToken);
            }
        } else {
            if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 1917419979, 0, "Transition animation finished, notifying core %s", String.valueOf(iBinder));
            }
            SurfaceControl.Transaction transaction = this.mActiveTransitions.get(findActiveTransition).mFinishT;
            int i = findActiveTransition + 1;
            while (i < this.mActiveTransitions.size() && this.mActiveTransitions.get(i).mMerged) {
                transaction.merge(this.mActiveTransitions.get(i).mStartT);
                transaction.merge(this.mActiveTransitions.get(i).mFinishT);
                i++;
            }
            transaction.apply();
            this.mActiveTransitions.remove(findActiveTransition);
            this.mOrganizer.finishTransition(iBinder, windowContainerTransaction, windowContainerTransactionCallback);
            while (findActiveTransition < this.mActiveTransitions.size() && this.mActiveTransitions.get(findActiveTransition).mMerged) {
                this.mOrganizer.finishTransition(this.mActiveTransitions.remove(findActiveTransition).mToken, (WindowContainerTransaction) null, (WindowContainerTransactionCallback) null);
            }
            if (this.mActiveTransitions.size() > findActiveTransition) {
                ActiveTransition activeTransition2 = this.mActiveTransitions.get(findActiveTransition);
                if (activeTransition2.mInfo != null) {
                    if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                        ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -821069485, 0, "Pending transitions after one finished, so start the next one.", null);
                    }
                    playTransition(activeTransition2);
                    int findActiveTransition2 = findActiveTransition(activeTransition2.mToken);
                    if (findActiveTransition2 >= 0) {
                        int i2 = findActiveTransition2 + 1;
                        while (i2 < this.mActiveTransitions.size()) {
                            ActiveTransition activeTransition3 = this.mActiveTransitions.get(i2);
                            if (!activeTransition3.mMerged) {
                                attemptMergeTransition(activeTransition2, activeTransition3);
                                int findActiveTransition3 = findActiveTransition(activeTransition3.mToken);
                                if (findActiveTransition3 >= 0) {
                                    i2 = findActiveTransition3 + 1;
                                } else {
                                    return;
                                }
                            } else {
                                throw new IllegalStateException("Can't merge a transition after not-merging a preceding one.");
                            }
                        }
                    }
                } else if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                    ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 305060772, 0, "Pending transition after one finished, but it isn't ready yet.", null);
                }
            } else if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
                ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, 490784151, 0, "All active transition animations finished", null);
            }
        }
    }

    void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
        if (ShellProtoLogCache.WM_SHELL_TRANSITIONS_enabled) {
            ShellProtoLogImpl.v(ShellProtoLogGroup.WM_SHELL_TRANSITIONS, -2076257741, 0, "Transition requested: %s %s", String.valueOf(iBinder), String.valueOf(transitionRequestInfo));
        }
        if (findActiveTransition(iBinder) < 0) {
            WindowContainerTransaction windowContainerTransaction = null;
            ActiveTransition activeTransition = new ActiveTransition();
            int size = this.mHandlers.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                windowContainerTransaction = this.mHandlers.get(size).handleRequest(iBinder, transitionRequestInfo);
                if (windowContainerTransaction != null) {
                    activeTransition.mHandler = this.mHandlers.get(size);
                    break;
                }
                size--;
            }
            activeTransition.mToken = this.mOrganizer.startTransition(transitionRequestInfo.getType(), iBinder, windowContainerTransaction);
            this.mActiveTransitions.add(activeTransition);
            return;
        }
        throw new RuntimeException("Transition already started " + iBinder);
    }

    public IBinder startTransition(int i, WindowContainerTransaction windowContainerTransaction, TransitionHandler transitionHandler) {
        ActiveTransition activeTransition = new ActiveTransition();
        activeTransition.mHandler = transitionHandler;
        activeTransition.mToken = this.mOrganizer.startTransition(i, (IBinder) null, windowContainerTransaction);
        this.mActiveTransitions.add(activeTransition);
        return activeTransition.mToken;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class TransitionPlayerImpl extends ITransitionPlayer.Stub {
        private TransitionPlayerImpl() {
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onTransitionReady$0(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) {
            Transitions.this.onTransitionReady(iBinder, transitionInfo, transaction, transaction2);
        }

        public void onTransitionReady(IBinder iBinder, TransitionInfo transitionInfo, SurfaceControl.Transaction transaction, SurfaceControl.Transaction transaction2) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda0(this, iBinder, transitionInfo, transaction, transaction2));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$requestStartTransition$1(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) {
            Transitions.this.requestStartTransition(iBinder, transitionRequestInfo);
        }

        public void requestStartTransition(IBinder iBinder, TransitionRequestInfo transitionRequestInfo) throws RemoteException {
            Transitions.this.mMainExecutor.execute(new Transitions$TransitionPlayerImpl$$ExternalSyntheticLambda1(this, iBinder, transitionRequestInfo));
        }
    }

    /* loaded from: classes2.dex */
    private class ShellTransitionImpl implements ShellTransitions {
        private IShellTransitionsImpl mIShellTransitions;

        private ShellTransitionImpl() {
        }

        @Override // com.android.wm.shell.transition.ShellTransitions
        public IShellTransitions createExternalInterface() {
            IShellTransitionsImpl iShellTransitionsImpl = this.mIShellTransitions;
            if (iShellTransitionsImpl != null) {
                iShellTransitionsImpl.invalidate();
            }
            IShellTransitionsImpl iShellTransitionsImpl2 = new IShellTransitionsImpl(Transitions.this);
            this.mIShellTransitions = iShellTransitionsImpl2;
            return iShellTransitionsImpl2;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static class IShellTransitionsImpl extends IShellTransitions.Stub {
        private Transitions mTransitions;

        IShellTransitionsImpl(Transitions transitions) {
            this.mTransitions = transitions;
        }

        void invalidate() {
            this.mTransitions = null;
        }

        @Override // com.android.wm.shell.transition.IShellTransitions
        public void registerRemote(TransitionFilter transitionFilter, IRemoteTransition iRemoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mTransitions, "registerRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda1(transitionFilter, iRemoteTransition));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$registerRemote$0(TransitionFilter transitionFilter, IRemoteTransition iRemoteTransition, Transitions transitions) {
            transitions.mRemoteTransitionHandler.addFiltered(transitionFilter, iRemoteTransition);
        }

        @Override // com.android.wm.shell.transition.IShellTransitions
        public void unregisterRemote(IRemoteTransition iRemoteTransition) {
            ExecutorUtils.executeRemoteCallWithTaskPermission(this.mTransitions, "unregisterRemote", new Transitions$IShellTransitionsImpl$$ExternalSyntheticLambda0(iRemoteTransition));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$unregisterRemote$1(IRemoteTransition iRemoteTransition, Transitions transitions) {
            transitions.mRemoteTransitionHandler.removeFiltered(iRemoteTransition);
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SettingsObserver extends ContentObserver {
        SettingsObserver() {
            super(null);
        }

        @Override // android.database.ContentObserver
        public void onChange(boolean z) {
            super.onChange(z);
            Transitions transitions = Transitions.this;
            transitions.mTransitionAnimationScaleSetting = Settings.Global.getFloat(transitions.mContext.getContentResolver(), "transition_animation_scale", Transitions.this.mTransitionAnimationScaleSetting);
            Transitions.this.mMainExecutor.execute(new Transitions$SettingsObserver$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onChange$0() {
            Transitions transitions = Transitions.this;
            transitions.dispatchAnimScaleSetting(transitions.mTransitionAnimationScaleSetting);
        }
    }
}
