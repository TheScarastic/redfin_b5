package com.android.wm.shell.legacysplitscreen;

import android.animation.AnimationHandler;
import android.app.ActivityManager;
import android.app.ActivityTaskManager;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.os.RemoteException;
import android.provider.Settings;
import android.util.Slog;
import android.view.LayoutInflater;
import android.view.SurfaceControl;
import android.view.ViewGroup;
import android.widget.Toast;
import android.window.TaskOrganizer;
import android.window.WindowContainerToken;
import android.window.WindowContainerTransaction;
import com.android.wm.shell.R;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.DisplayImeController;
import com.android.wm.shell.common.DisplayLayout;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.SyncTransactionQueue;
import com.android.wm.shell.common.SystemWindows;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.common.TransactionPool;
import com.android.wm.shell.transition.Transitions;
import java.io.PrintWriter;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Predicate;
/* loaded from: classes2.dex */
public class LegacySplitScreenController implements DisplayController.OnDisplaysChangedListener {
    private final Context mContext;
    private final DisplayController mDisplayController;
    private final ForcedResizableInfoActivityController mForcedResizableController;
    private final DisplayImeController mImeController;
    private final DividerImeController mImePositionProcessor;
    private boolean mIsKeyguardShowing;
    private final ShellExecutor mMainExecutor;
    private LegacySplitDisplayLayout mRotateSplitLayout;
    private final AnimationHandler mSfVsyncAnimationHandler;
    private LegacySplitDisplayLayout mSplitLayout;
    private final LegacySplitScreenTaskListener mSplits;
    private final SystemWindows mSystemWindows;
    private final TaskOrganizer mTaskOrganizer;
    final TransactionPool mTransactionPool;
    private DividerView mView;
    private DividerWindowManager mWindowManager;
    private final WindowManagerProxy mWindowManagerProxy;
    private final DividerState mDividerState = new DividerState();
    private final SplitScreenImpl mImpl = new SplitScreenImpl();
    private final CopyOnWriteArrayList<WeakReference<Consumer<Boolean>>> mDockedStackExistsListeners = new CopyOnWriteArrayList<>();
    private final ArrayList<WeakReference<BiConsumer<Rect, Rect>>> mBoundsChangedListeners = new ArrayList<>();
    private boolean mVisible = false;
    private volatile boolean mMinimized = false;
    private volatile boolean mAdjustedForIme = false;
    private boolean mHomeStackResizable = false;
    private final DisplayChangeController.OnDisplayChangingListener mRotationController = new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda0
        @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
        public final void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
            LegacySplitScreenController.this.lambda$new$0(i, i2, i3, windowContainerTransaction);
        }
    };

    public LegacySplitScreenController(Context context, DisplayController displayController, SystemWindows systemWindows, DisplayImeController displayImeController, TransactionPool transactionPool, ShellTaskOrganizer shellTaskOrganizer, SyncTransactionQueue syncTransactionQueue, TaskStackListenerImpl taskStackListenerImpl, Transitions transitions, ShellExecutor shellExecutor, AnimationHandler animationHandler) {
        this.mContext = context;
        this.mDisplayController = displayController;
        this.mSystemWindows = systemWindows;
        this.mImeController = displayImeController;
        this.mMainExecutor = shellExecutor;
        this.mSfVsyncAnimationHandler = animationHandler;
        this.mForcedResizableController = new ForcedResizableInfoActivityController(context, this, shellExecutor);
        this.mTransactionPool = transactionPool;
        this.mWindowManagerProxy = new WindowManagerProxy(syncTransactionQueue, shellTaskOrganizer);
        this.mTaskOrganizer = shellTaskOrganizer;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = new LegacySplitScreenTaskListener(this, shellTaskOrganizer, transitions, syncTransactionQueue);
        this.mSplits = legacySplitScreenTaskListener;
        this.mImePositionProcessor = new DividerImeController(legacySplitScreenTaskListener, transactionPool, shellExecutor, shellTaskOrganizer);
        this.mWindowManager = new DividerWindowManager(systemWindows);
        displayController.addDisplayWindowListener(this);
        taskStackListenerImpl.addListener(new TaskStackListenerCallback() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController.1
            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                if (z3 && runningTaskInfo.getWindowingMode() == 3 && LegacySplitScreenController.this.mSplits.isSplitScreenSupported() && LegacySplitScreenController.this.isMinimized()) {
                    LegacySplitScreenController.this.onUndockingTask();
                }
            }

            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityForcedResizable(String str, int i, int i2) {
                LegacySplitScreenController.this.mForcedResizableController.activityForcedResizable(str, i, i2);
            }

            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityDismissingDockedStack() {
                LegacySplitScreenController.this.mForcedResizableController.activityDismissingSplitScreen();
            }

            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityLaunchOnSecondaryDisplayFailed() {
                LegacySplitScreenController.this.mForcedResizableController.activityLaunchOnSecondaryDisplayFailed();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
        int i4;
        if (this.mSplits.isSplitScreenSupported() && this.mWindowManagerProxy != null) {
            WindowContainerTransaction windowContainerTransaction2 = new WindowContainerTransaction();
            LegacySplitDisplayLayout legacySplitDisplayLayout = new LegacySplitDisplayLayout(this.mContext, new DisplayLayout(this.mDisplayController.getDisplayLayout(i)), this.mSplits);
            legacySplitDisplayLayout.rotateTo(i3);
            this.mRotateSplitLayout = legacySplitDisplayLayout;
            if (this.mMinimized) {
                i4 = this.mView.mSnapTargetBeforeMinimized.position;
            } else {
                i4 = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget().position;
            }
            legacySplitDisplayLayout.resizeSplits(legacySplitDisplayLayout.getSnapAlgorithm().calculateNonDismissingSnapTarget(i4).position, windowContainerTransaction2);
            if (isSplitActive() && this.mHomeStackResizable) {
                this.mWindowManagerProxy.applyHomeTasksMinimized(legacySplitDisplayLayout, this.mSplits.mSecondary.token, windowContainerTransaction2);
            }
            if (this.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction2)) {
                Slog.w("SplitScreenCtrl", "Screen rotated while other operations were pending, this may result in some graphical artifacts.");
            } else {
                windowContainerTransaction.merge(windowContainerTransaction2, true);
            }
        }
    }

    public LegacySplitScreen asLegacySplitScreen() {
        return this.mImpl;
    }

    public void onSplitScreenSupported() {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        this.mSplitLayout.resizeSplits(this.mSplitLayout.getSnapAlgorithm().getMiddleTarget().position, windowContainerTransaction);
        this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
    }

    public void onKeyguardVisibilityChanged(boolean z) {
        DividerView dividerView;
        if (isSplitActive() && (dividerView = this.mView) != null) {
            dividerView.setHidden(z);
            this.mIsKeyguardShowing = z;
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onDisplayAdded(int i) {
        if (i == 0) {
            this.mSplitLayout = new LegacySplitDisplayLayout(this.mDisplayController.getDisplayContext(i), this.mDisplayController.getDisplayLayout(i), this.mSplits);
            this.mImeController.addPositionProcessor(this.mImePositionProcessor);
            this.mDisplayController.addDisplayChangingController(this.mRotationController);
            if (!ActivityTaskManager.supportsSplitScreenMultiWindow(this.mContext)) {
                removeDivider();
                return;
            }
            try {
                this.mSplits.init();
            } catch (Exception e) {
                Slog.e("SplitScreenCtrl", "Failed to register docked stack listener", e);
                removeDivider();
            }
        }
    }

    @Override // com.android.wm.shell.common.DisplayController.OnDisplaysChangedListener
    public void onDisplayConfigurationChanged(int i, Configuration configuration) {
        if (i == 0 && this.mSplits.isSplitScreenSupported()) {
            LegacySplitDisplayLayout legacySplitDisplayLayout = new LegacySplitDisplayLayout(this.mDisplayController.getDisplayContext(i), this.mDisplayController.getDisplayLayout(i), this.mSplits);
            this.mSplitLayout = legacySplitDisplayLayout;
            if (this.mRotateSplitLayout == null) {
                int i2 = legacySplitDisplayLayout.getSnapAlgorithm().getMiddleTarget().position;
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                this.mSplitLayout.resizeSplits(i2, windowContainerTransaction);
                this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
            } else if (legacySplitDisplayLayout.mDisplayLayout.rotation() == this.mRotateSplitLayout.mDisplayLayout.rotation()) {
                this.mSplitLayout.mPrimary = new Rect(this.mRotateSplitLayout.mPrimary);
                this.mSplitLayout.mSecondary = new Rect(this.mRotateSplitLayout.mSecondary);
                this.mRotateSplitLayout = null;
            }
            if (isSplitActive()) {
                update(configuration);
            }
        }
    }

    public boolean isMinimized() {
        return this.mMinimized;
    }

    public boolean isHomeStackResizable() {
        return this.mHomeStackResizable;
    }

    public DividerView getDividerView() {
        return this.mView;
    }

    public boolean isDividerVisible() {
        DividerView dividerView = this.mView;
        return dividerView != null && dividerView.getVisibility() == 0;
    }

    public boolean isSplitActive() {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        ActivityManager.RunningTaskInfo runningTaskInfo2 = legacySplitScreenTaskListener.mPrimary;
        return (runningTaskInfo2 == null || (runningTaskInfo = legacySplitScreenTaskListener.mSecondary) == null || (runningTaskInfo2.topActivityType == 0 && runningTaskInfo.topActivityType == 0)) ? false : true;
    }

    public void addDivider(Configuration configuration) {
        int i;
        Context displayContext = this.mDisplayController.getDisplayContext(this.mContext.getDisplayId());
        DividerView dividerView = (DividerView) LayoutInflater.from(displayContext).inflate(R.layout.docked_stack_divider, (ViewGroup) null);
        this.mView = dividerView;
        dividerView.setAnimationHandler(this.mSfVsyncAnimationHandler);
        DisplayLayout displayLayout = this.mDisplayController.getDisplayLayout(this.mContext.getDisplayId());
        this.mView.injectDependencies(this, this.mWindowManager, this.mDividerState, this.mForcedResizableController, this.mSplits, this.mSplitLayout, this.mImePositionProcessor, this.mWindowManagerProxy);
        boolean z = false;
        this.mView.setVisibility(this.mVisible ? 0 : 4);
        this.mView.setMinimizedDockStack(this.mMinimized, this.mHomeStackResizable, (SurfaceControl.Transaction) null);
        int dimensionPixelSize = displayContext.getResources().getDimensionPixelSize(17105198);
        if (configuration.orientation == 2) {
            z = true;
        }
        if (z) {
            i = dimensionPixelSize;
        } else {
            i = displayLayout.width();
        }
        if (z) {
            dimensionPixelSize = displayLayout.height();
        }
        this.mWindowManager.add(this.mView, i, dimensionPixelSize, this.mContext.getDisplayId());
    }

    public void removeDivider() {
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            dividerView.onDividerRemoved();
        }
        this.mWindowManager.remove();
    }

    public void update(Configuration configuration) {
        boolean z = this.mView != null && this.mIsKeyguardShowing;
        removeDivider();
        addDivider(configuration);
        if (this.mMinimized) {
            this.mView.setMinimizedDockStack(true, this.mHomeStackResizable, (SurfaceControl.Transaction) null);
            updateTouchable();
        }
        this.mView.setHidden(z);
    }

    public void onTaskVanished() {
        removeDivider();
    }

    public void updateVisibility(boolean z) {
        if (this.mVisible != z) {
            this.mVisible = z;
            this.mView.setVisibility(z ? 0 : 4);
            if (z) {
                this.mView.enterSplitMode(this.mHomeStackResizable);
                this.mWindowManagerProxy.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda1
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        LegacySplitScreenController.this.lambda$updateVisibility$1(transaction);
                    }
                });
            } else {
                this.mView.exitSplitMode();
                this.mWindowManagerProxy.runInSync(new SyncTransactionQueue.TransactionRunnable() { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda2
                    @Override // com.android.wm.shell.common.SyncTransactionQueue.TransactionRunnable
                    public final void runWithTransaction(SurfaceControl.Transaction transaction) {
                        LegacySplitScreenController.this.lambda$updateVisibility$2(transaction);
                    }
                });
            }
            synchronized (this.mDockedStackExistsListeners) {
                this.mDockedStackExistsListeners.removeIf(new Predicate(z) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda5
                    public final /* synthetic */ boolean f$0;

                    {
                        this.f$0 = r1;
                    }

                    @Override // java.util.function.Predicate
                    public final boolean test(Object obj) {
                        return LegacySplitScreenController.lambda$updateVisibility$3(this.f$0, (WeakReference) obj);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateVisibility$1(SurfaceControl.Transaction transaction) {
        this.mView.setMinimizedDockStack(this.mMinimized, this.mHomeStackResizable, transaction);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateVisibility$2(SurfaceControl.Transaction transaction) {
        this.mView.setMinimizedDockStack(false, this.mHomeStackResizable, transaction);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$updateVisibility$3(boolean z, WeakReference weakReference) {
        Consumer consumer = (Consumer) weakReference.get();
        if (consumer != null) {
            consumer.accept(Boolean.valueOf(z));
        }
        return consumer == null;
    }

    public void setMinimized(boolean z) {
        this.mMainExecutor.execute(new Runnable(z) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda3
            public final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                LegacySplitScreenController.this.lambda$setMinimized$4(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setMinimized$4(boolean z) {
        if (this.mVisible) {
            setHomeMinimized(z);
        }
    }

    public void setHomeMinimized(boolean z) {
        WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
        int i = 0;
        boolean z2 = this.mMinimized != z;
        if (z2) {
            this.mMinimized = z;
        }
        windowContainerTransaction.setFocusable(this.mSplits.mPrimary.token, true ^ this.mMinimized);
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            if (dividerView.getDisplay() != null) {
                i = this.mView.getDisplay().getDisplayId();
            }
            if (this.mMinimized) {
                this.mImePositionProcessor.pause(i);
            }
            if (z2) {
                this.mView.setMinimizedDockStack(z, getAnimDuration(), this.mHomeStackResizable);
            }
            if (!this.mMinimized) {
                this.mImePositionProcessor.resume(i);
            }
        }
        updateTouchable();
        if (!this.mWindowManagerProxy.queueSyncTransactionIfWaiting(windowContainerTransaction)) {
            this.mTaskOrganizer.applyTransaction(windowContainerTransaction);
        }
    }

    public void setAdjustedForIme(boolean z) {
        if (this.mAdjustedForIme != z) {
            this.mAdjustedForIme = z;
            updateTouchable();
        }
    }

    public void updateTouchable() {
        this.mWindowManager.setTouchable(!this.mAdjustedForIme);
    }

    public void onUndockingTask() {
        DividerView dividerView = this.mView;
        if (dividerView != null) {
            dividerView.onUndockingTask();
        }
    }

    public void onAppTransitionFinished() {
        if (this.mView != null) {
            this.mForcedResizableController.onAppTransitionFinished();
        }
    }

    public void dump(PrintWriter printWriter) {
        printWriter.print("  mVisible=");
        printWriter.println(this.mVisible);
        printWriter.print("  mMinimized=");
        printWriter.println(this.mMinimized);
        printWriter.print("  mAdjustedForIme=");
        printWriter.println(this.mAdjustedForIme);
    }

    public long getAnimDuration() {
        return (long) (Settings.Global.getFloat(this.mContext.getContentResolver(), "transition_animation_scale", this.mContext.getResources().getFloat(17105060)) * 336.0f);
    }

    public void registerInSplitScreenListener(Consumer<Boolean> consumer) {
        consumer.accept(Boolean.valueOf(isDividerVisible()));
        synchronized (this.mDockedStackExistsListeners) {
            this.mDockedStackExistsListeners.add(new WeakReference<>(consumer));
        }
    }

    public void registerBoundsChangeListener(BiConsumer<Rect, Rect> biConsumer) {
        synchronized (this.mBoundsChangedListeners) {
            this.mBoundsChangedListeners.add(new WeakReference<>(biConsumer));
        }
    }

    public boolean splitPrimaryTask() {
        List tasks;
        ActivityManager.RunningTaskInfo runningTaskInfo;
        int activityType;
        try {
            if (!(ActivityTaskManager.getService().getLockTaskModeState() == 2 || isSplitActive() || this.mSplits.mPrimary == null || (tasks = ActivityTaskManager.getInstance().getTasks(1)) == null || tasks.isEmpty() || (activityType = (runningTaskInfo = (ActivityManager.RunningTaskInfo) tasks.get(0)).getActivityType()) == 2 || activityType == 3)) {
                if (!runningTaskInfo.supportsSplitScreenMultiWindow) {
                    Toast.makeText(this.mContext, R.string.dock_non_resizeble_failed_to_dock_text, 0).show();
                    return false;
                }
                WindowContainerTransaction windowContainerTransaction = new WindowContainerTransaction();
                windowContainerTransaction.setWindowingMode(runningTaskInfo.token, 0);
                windowContainerTransaction.reparent(runningTaskInfo.token, this.mSplits.mPrimary.token, true);
                this.mWindowManagerProxy.applySyncTransaction(windowContainerTransaction);
                return true;
            }
        } catch (RemoteException unused) {
        }
        return false;
    }

    public void notifyBoundsChanged(Rect rect, Rect rect2) {
        synchronized (this.mBoundsChangedListeners) {
            this.mBoundsChangedListeners.removeIf(new Predicate(rect, rect2) { // from class: com.android.wm.shell.legacysplitscreen.LegacySplitScreenController$$ExternalSyntheticLambda4
                public final /* synthetic */ Rect f$0;
                public final /* synthetic */ Rect f$1;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                }

                @Override // java.util.function.Predicate
                public final boolean test(Object obj) {
                    return LegacySplitScreenController.lambda$notifyBoundsChanged$5(this.f$0, this.f$1, (WeakReference) obj);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ boolean lambda$notifyBoundsChanged$5(Rect rect, Rect rect2, WeakReference weakReference) {
        BiConsumer biConsumer = (BiConsumer) weakReference.get();
        if (biConsumer != null) {
            biConsumer.accept(rect, rect2);
        }
        return biConsumer == null;
    }

    public void startEnterSplit() {
        update(this.mDisplayController.getDisplayContext(this.mContext.getDisplayId()).getResources().getConfiguration());
        WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mRotateSplitLayout;
        if (legacySplitDisplayLayout == null) {
            legacySplitDisplayLayout = this.mSplitLayout;
        }
        this.mHomeStackResizable = windowManagerProxy.applyEnterSplit(legacySplitScreenTaskListener, legacySplitDisplayLayout);
    }

    public void prepareEnterSplitTransition(WindowContainerTransaction windowContainerTransaction) {
        WindowManagerProxy windowManagerProxy = this.mWindowManagerProxy;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        LegacySplitDisplayLayout legacySplitDisplayLayout = this.mRotateSplitLayout;
        if (legacySplitDisplayLayout == null) {
            legacySplitDisplayLayout = this.mSplitLayout;
        }
        this.mHomeStackResizable = windowManagerProxy.buildEnterSplit(windowContainerTransaction, legacySplitScreenTaskListener, legacySplitDisplayLayout);
    }

    public void finishEnterSplitTransition(boolean z) {
        update(this.mDisplayController.getDisplayContext(this.mContext.getDisplayId()).getResources().getConfiguration());
        if (z) {
            ensureMinimizedSplit();
        } else {
            ensureNormalSplit();
        }
    }

    public void startDismissSplit(boolean z) {
        startDismissSplit(z, false);
    }

    public void startDismissSplit(boolean z, boolean z2) {
        if (Transitions.ENABLE_SHELL_TRANSITIONS) {
            this.mSplits.getSplitTransitions().dismissSplit(this.mSplits, this.mSplitLayout, !z, z2);
            return;
        }
        this.mWindowManagerProxy.applyDismissSplit(this.mSplits, this.mSplitLayout, !z);
        onDismissSplit();
    }

    public void onDismissSplit() {
        updateVisibility(false);
        this.mMinimized = false;
        this.mDividerState.mRatioPositionBeforeMinimized = 0.0f;
        removeDivider();
        this.mImePositionProcessor.reset();
    }

    public void ensureMinimizedSplit() {
        setHomeMinimized(true);
        if (this.mView != null && !isDividerVisible()) {
            updateVisibility(true);
        }
    }

    public void ensureNormalSplit() {
        setHomeMinimized(false);
        if (this.mView != null && !isDividerVisible()) {
            updateVisibility(true);
        }
    }

    public LegacySplitDisplayLayout getSplitLayout() {
        return this.mSplitLayout;
    }

    public WindowManagerProxy getWmProxy() {
        return this.mWindowManagerProxy;
    }

    public WindowContainerToken getSecondaryRoot() {
        ActivityManager.RunningTaskInfo runningTaskInfo;
        LegacySplitScreenTaskListener legacySplitScreenTaskListener = this.mSplits;
        if (legacySplitScreenTaskListener == null || (runningTaskInfo = legacySplitScreenTaskListener.mSecondary) == null) {
            return null;
        }
        return runningTaskInfo.token;
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class SplitScreenImpl implements LegacySplitScreen {
        private SplitScreenImpl() {
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public boolean isMinimized() {
            return LegacySplitScreenController.this.mMinimized;
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public boolean isHomeStackResizable() {
            return LegacySplitScreenController.this.mHomeStackResizable;
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public DividerView getDividerView() {
            return LegacySplitScreenController.this.getDividerView();
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public boolean isDividerVisible() {
            boolean[] zArr = new boolean[1];
            try {
                LegacySplitScreenController.this.mMainExecutor.executeBlocking(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda7(this, zArr));
            } catch (InterruptedException unused) {
                Slog.e("SplitScreenCtrl", "Failed to get divider visible");
            }
            return zArr[0];
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$isDividerVisible$0(boolean[] zArr) {
            zArr[0] = LegacySplitScreenController.this.isDividerVisible();
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public void onKeyguardVisibilityChanged(boolean z) {
            LegacySplitScreenController.this.mMainExecutor.execute(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda4(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onKeyguardVisibilityChanged$1(boolean z) {
            LegacySplitScreenController.this.onKeyguardVisibilityChanged(z);
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public void setMinimized(boolean z) {
            LegacySplitScreenController.this.mMainExecutor.execute(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda5(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setMinimized$2(boolean z) {
            LegacySplitScreenController.this.setMinimized(z);
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public void onUndockingTask() {
            LegacySplitScreenController.this.mMainExecutor.execute(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUndockingTask$3() {
            LegacySplitScreenController.this.onUndockingTask();
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public void onAppTransitionFinished() {
            LegacySplitScreenController.this.mMainExecutor.execute(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onAppTransitionFinished$4() {
            LegacySplitScreenController.this.onAppTransitionFinished();
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public void registerInSplitScreenListener(Consumer<Boolean> consumer) {
            LegacySplitScreenController.this.mMainExecutor.execute(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda3(this, consumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerInSplitScreenListener$5(Consumer consumer) {
            LegacySplitScreenController.this.registerInSplitScreenListener(consumer);
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public void registerBoundsChangeListener(BiConsumer<Rect, Rect> biConsumer) {
            LegacySplitScreenController.this.mMainExecutor.execute(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda2(this, biConsumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$registerBoundsChangeListener$7(BiConsumer biConsumer) {
            LegacySplitScreenController.this.registerBoundsChangeListener(biConsumer);
        }

        @Override // com.android.wm.shell.legacysplitscreen.LegacySplitScreen
        public boolean splitPrimaryTask() {
            boolean[] zArr = new boolean[1];
            try {
                LegacySplitScreenController.this.mMainExecutor.executeBlocking(new LegacySplitScreenController$SplitScreenImpl$$ExternalSyntheticLambda6(this, zArr));
            } catch (InterruptedException unused) {
                Slog.e("SplitScreenCtrl", "Failed to split primary task");
            }
            return zArr[0];
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$splitPrimaryTask$9(boolean[] zArr) {
            zArr[0] = LegacySplitScreenController.this.splitPrimaryTask();
        }
    }
}
