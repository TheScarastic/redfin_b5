package com.android.wm.shell.bubbles;

import android.app.ActivityManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.LocusId;
import android.content.pm.ActivityInfo;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.UserInfo;
import android.content.res.Configuration;
import android.graphics.PointF;
import android.graphics.Rect;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.service.notification.NotificationListenerService;
import android.util.ArraySet;
import android.util.Log;
import android.util.Pair;
import android.util.Slog;
import android.util.SparseArray;
import android.util.SparseSetArray;
import android.view.View;
import android.view.WindowManager;
import android.window.WindowContainerTransaction;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.statusbar.IStatusBarService;
import com.android.wm.shell.ShellTaskOrganizer;
import com.android.wm.shell.WindowManagerShellWrapper;
import com.android.wm.shell.bubbles.BubbleData;
import com.android.wm.shell.bubbles.BubbleLogger;
import com.android.wm.shell.bubbles.BubbleStackView;
import com.android.wm.shell.bubbles.BubbleViewInfoTask;
import com.android.wm.shell.bubbles.Bubbles;
import com.android.wm.shell.common.DisplayChangeController;
import com.android.wm.shell.common.DisplayController;
import com.android.wm.shell.common.FloatingContentCoordinator;
import com.android.wm.shell.common.ShellExecutor;
import com.android.wm.shell.common.TaskStackListenerCallback;
import com.android.wm.shell.common.TaskStackListenerImpl;
import com.android.wm.shell.pip.PinnedStackListenerForwarder;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executor;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.IntConsumer;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
/* loaded from: classes2.dex */
public class BubbleController {
    private final IStatusBarService mBarService;
    private BubbleData mBubbleData;
    private BubbleIconFactory mBubbleIconFactory;
    private BubblePositioner mBubblePositioner;
    private View mBubbleScrim;
    private final Context mContext;
    private SparseArray<UserInfo> mCurrentProfiles;
    private int mCurrentUserId;
    private final BubbleDataRepository mDataRepository;
    private final DisplayController mDisplayController;
    private Bubbles.BubbleExpandListener mExpandListener;
    private final FloatingContentCoordinator mFloatingContentCoordinator;
    private boolean mInflateSynchronously;
    private final LauncherApps mLauncherApps;
    private BubbleLogger mLogger;
    private final ShellExecutor mMainExecutor;
    private final Handler mMainHandler;
    private BubbleEntry mNotifEntryToExpandOnShadeUnlock;
    private final SparseSetArray<String> mSavedBubbleKeysPerUser;
    private BubbleStackView mStackView;
    private BubbleStackView.SurfaceSynchronizer mSurfaceSynchronizer;
    private Bubbles.SysuiProxy mSysuiProxy;
    private final ShellTaskOrganizer mTaskOrganizer;
    private final TaskStackListenerImpl mTaskStackListener;
    private NotificationListenerService.Ranking mTmpRanking;
    private final WindowManager mWindowManager;
    private final WindowManagerShellWrapper mWindowManagerShellWrapper;
    private WindowManager.LayoutParams mWmLayoutParams;
    private final BubblesImpl mImpl = new BubblesImpl();
    private BubbleData.Listener mOverflowListener = null;
    private boolean mOverflowDataLoadNeeded = true;
    private boolean mAddedToWindowManager = false;
    private int mDensityDpi = 0;
    private Rect mScreenBounds = new Rect();
    private float mFontScale = 0.0f;
    private int mLayoutDirection = -1;
    private boolean mIsStatusBarShade = true;
    private final BubbleData.Listener mBubbleDataListener = new BubbleData.Listener() { // from class: com.android.wm.shell.bubbles.BubbleController.4
        @Override // com.android.wm.shell.bubbles.BubbleData.Listener
        public void applyUpdate(BubbleData.Update update) {
            BubbleController.this.ensureStackViewCreated();
            BubbleController.this.loadOverflowBubblesFromDisk();
            BubbleController.this.mStackView.updateOverflowButtonDot();
            if (BubbleController.this.mOverflowListener != null) {
                BubbleController.this.mOverflowListener.applyUpdate(update);
            }
            if (update.expandedChanged && !update.expanded) {
                BubbleController.this.mStackView.setExpanded(false);
                BubbleController.this.mSysuiProxy.requestNotificationShadeTopUi(false, "Bubbles");
            }
            ArrayList arrayList = new ArrayList(update.removedBubbles);
            ArrayList arrayList2 = new ArrayList();
            Iterator it = arrayList.iterator();
            while (it.hasNext()) {
                Pair pair = (Pair) it.next();
                Bubble bubble = (Bubble) pair.first;
                int intValue = ((Integer) pair.second).intValue();
                if (BubbleController.this.mStackView != null) {
                    BubbleController.this.mStackView.removeBubble(bubble);
                }
                if (!(intValue == 8 || intValue == 14)) {
                    if (intValue == 5) {
                        arrayList2.add(bubble);
                    }
                    if (!BubbleController.this.mBubbleData.hasBubbleInStackWithKey(bubble.getKey())) {
                        if (BubbleController.this.mBubbleData.hasOverflowBubbleWithKey(bubble.getKey()) || !(!bubble.showInShade() || intValue == 5 || intValue == 9)) {
                            if (bubble.isBubble()) {
                                BubbleController.this.setIsBubble(bubble, false);
                            }
                            BubbleController.this.mSysuiProxy.updateNotificationBubbleButton(bubble.getKey());
                        } else {
                            BubbleController.this.mSysuiProxy.notifyRemoveNotification(bubble.getKey(), 2);
                        }
                    }
                    BubbleController.this.mSysuiProxy.getPendingOrActiveEntry(bubble.getKey(), new BubbleController$4$$ExternalSyntheticLambda1(this, bubble));
                }
            }
            BubbleController.this.mDataRepository.removeBubbles(BubbleController.this.mCurrentUserId, arrayList2);
            if (!(update.addedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mDataRepository.addBubble(BubbleController.this.mCurrentUserId, update.addedBubble);
                BubbleController.this.mStackView.addBubble(update.addedBubble);
            }
            if (!(update.updatedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mStackView.updateBubble(update.updatedBubble);
            }
            if (update.orderChanged && BubbleController.this.mStackView != null) {
                BubbleController.this.mDataRepository.addBubbles(BubbleController.this.mCurrentUserId, update.bubbles);
                BubbleController.this.mStackView.updateBubbleOrder(update.bubbles);
            }
            if (update.selectionChanged && BubbleController.this.mStackView != null) {
                BubbleController.this.mStackView.setSelectedBubble(update.selectedBubble);
                if (update.selectedBubble != null) {
                    BubbleController.this.mSysuiProxy.updateNotificationSuppression(update.selectedBubble.getKey());
                }
            }
            if (!(update.suppressedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mStackView.setBubbleVisibility(update.suppressedBubble, false);
            }
            if (!(update.unsuppressedBubble == null || BubbleController.this.mStackView == null)) {
                BubbleController.this.mStackView.setBubbleVisibility(update.unsuppressedBubble, true);
            }
            if (update.expandedChanged && update.expanded && BubbleController.this.mStackView != null) {
                BubbleController.this.mStackView.setExpanded(true);
                BubbleController.this.mSysuiProxy.requestNotificationShadeTopUi(true, "Bubbles");
            }
            BubbleController.this.mSysuiProxy.notifyInvalidateNotifications("BubbleData.Listener.applyUpdate");
            BubbleController.this.updateStack();
            BubbleController.this.mImpl.mCachedState.update(update);
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$applyUpdate$1(Bubble bubble, BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$4$$ExternalSyntheticLambda0(this, bubbleEntry, bubble));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$applyUpdate$0(BubbleEntry bubbleEntry, Bubble bubble) {
            if (bubbleEntry != null) {
                if (BubbleController.this.getBubblesInGroup(bubbleEntry.getStatusBarNotification().getGroupKey()).isEmpty()) {
                    BubbleController.this.mSysuiProxy.notifyMaybeCancelSummary(bubble.getKey());
                }
            }
        }
    };

    public static BubbleController create(Context context, BubbleStackView.SurfaceSynchronizer surfaceSynchronizer, FloatingContentCoordinator floatingContentCoordinator, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, LauncherApps launcherApps, TaskStackListenerImpl taskStackListenerImpl, UiEventLogger uiEventLogger, ShellTaskOrganizer shellTaskOrganizer, DisplayController displayController, ShellExecutor shellExecutor, Handler handler) {
        BubbleLogger bubbleLogger = new BubbleLogger(uiEventLogger);
        BubblePositioner bubblePositioner = new BubblePositioner(context, windowManager);
        return new BubbleController(context, new BubbleData(context, bubbleLogger, bubblePositioner, shellExecutor), surfaceSynchronizer, floatingContentCoordinator, new BubbleDataRepository(context, launcherApps, shellExecutor), iStatusBarService, windowManager, windowManagerShellWrapper, launcherApps, bubbleLogger, taskStackListenerImpl, shellTaskOrganizer, bubblePositioner, displayController, shellExecutor, handler);
    }

    @VisibleForTesting
    protected BubbleController(Context context, BubbleData bubbleData, BubbleStackView.SurfaceSynchronizer surfaceSynchronizer, FloatingContentCoordinator floatingContentCoordinator, BubbleDataRepository bubbleDataRepository, IStatusBarService iStatusBarService, WindowManager windowManager, WindowManagerShellWrapper windowManagerShellWrapper, LauncherApps launcherApps, BubbleLogger bubbleLogger, TaskStackListenerImpl taskStackListenerImpl, ShellTaskOrganizer shellTaskOrganizer, BubblePositioner bubblePositioner, DisplayController displayController, ShellExecutor shellExecutor, Handler handler) {
        this.mContext = context;
        this.mLauncherApps = launcherApps;
        this.mBarService = iStatusBarService == null ? IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar")) : iStatusBarService;
        this.mWindowManager = windowManager;
        this.mWindowManagerShellWrapper = windowManagerShellWrapper;
        this.mFloatingContentCoordinator = floatingContentCoordinator;
        this.mDataRepository = bubbleDataRepository;
        this.mLogger = bubbleLogger;
        this.mMainExecutor = shellExecutor;
        this.mMainHandler = handler;
        this.mTaskStackListener = taskStackListenerImpl;
        this.mTaskOrganizer = shellTaskOrganizer;
        this.mSurfaceSynchronizer = surfaceSynchronizer;
        this.mCurrentUserId = ActivityManager.getCurrentUser();
        this.mBubblePositioner = bubblePositioner;
        this.mBubbleData = bubbleData;
        this.mSavedBubbleKeysPerUser = new SparseSetArray<>();
        this.mBubbleIconFactory = new BubbleIconFactory(context);
        this.mDisplayController = displayController;
    }

    public void initialize() {
        this.mBubbleData.setListener(this.mBubbleDataListener);
        this.mBubbleData.setSuppressionChangedListener(new Bubbles.SuppressionChangedListener() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda5
            @Override // com.android.wm.shell.bubbles.Bubbles.SuppressionChangedListener
            public final void onBubbleNotificationSuppressionChange(Bubble bubble) {
                BubbleController.this.onBubbleNotificationSuppressionChanged(bubble);
            }
        });
        this.mBubbleData.setPendingIntentCancelledListener(new Bubbles.PendingIntentCanceledListener() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda4
            @Override // com.android.wm.shell.bubbles.Bubbles.PendingIntentCanceledListener
            public final void onPendingIntentCanceled(Bubble bubble) {
                BubbleController.this.lambda$initialize$1(bubble);
            }
        });
        try {
            this.mWindowManagerShellWrapper.addPinnedStackListener(new BubblesImeListener());
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.mBubbleData.setCurrentUserId(this.mCurrentUserId);
        this.mTaskOrganizer.addLocusIdListener(new ShellTaskOrganizer.LocusIdListener() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda0
            @Override // com.android.wm.shell.ShellTaskOrganizer.LocusIdListener
            public final void onVisibilityChanged(int i, LocusId locusId, boolean z) {
                BubbleController.this.lambda$initialize$2(i, locusId, z);
            }
        });
        this.mLauncherApps.registerCallback(new LauncherApps.Callback() { // from class: com.android.wm.shell.bubbles.BubbleController.1
            @Override // android.content.pm.LauncherApps.Callback
            public void onPackageAdded(String str, UserHandle userHandle) {
            }

            @Override // android.content.pm.LauncherApps.Callback
            public void onPackageChanged(String str, UserHandle userHandle) {
            }

            @Override // android.content.pm.LauncherApps.Callback
            public void onPackagesAvailable(String[] strArr, UserHandle userHandle, boolean z) {
            }

            @Override // android.content.pm.LauncherApps.Callback
            public void onPackageRemoved(String str, UserHandle userHandle) {
                BubbleController.this.mBubbleData.removeBubblesWithPackageName(str, 13);
            }

            @Override // android.content.pm.LauncherApps.Callback
            public void onPackagesUnavailable(String[] strArr, UserHandle userHandle, boolean z) {
                for (String str : strArr) {
                    BubbleController.this.mBubbleData.removeBubblesWithPackageName(str, 13);
                }
            }

            @Override // android.content.pm.LauncherApps.Callback
            public void onShortcutsChanged(String str, List<ShortcutInfo> list, UserHandle userHandle) {
                super.onShortcutsChanged(str, list, userHandle);
                BubbleController.this.mBubbleData.removeBubblesWithInvalidShortcuts(str, list, 12);
            }
        }, this.mMainHandler);
        this.mTaskStackListener.addListener(new TaskStackListenerCallback() { // from class: com.android.wm.shell.bubbles.BubbleController.2
            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onTaskMovedToFront(int i) {
                if (BubbleController.this.mSysuiProxy != null) {
                    BubbleController.this.mSysuiProxy.isNotificationShadeExpand(new BubbleController$2$$ExternalSyntheticLambda1(this, i));
                }
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onTaskMovedToFront$1(int i, Boolean bool) {
                BubbleController.this.mMainExecutor.execute(new BubbleController$2$$ExternalSyntheticLambda0(this, bool, i));
            }

            /* access modifiers changed from: private */
            public /* synthetic */ void lambda$onTaskMovedToFront$0(Boolean bool, int i) {
                int taskId = (BubbleController.this.mStackView == null || BubbleController.this.mStackView.getExpandedBubble() == null || !BubbleController.this.isStackExpanded() || BubbleController.this.mStackView.isExpansionAnimating() || bool.booleanValue()) ? -1 : BubbleController.this.mStackView.getExpandedBubble().getTaskId();
                if (taskId != -1 && taskId != i) {
                    BubbleController.this.mBubbleData.setExpanded(false);
                }
            }

            @Override // com.android.wm.shell.common.TaskStackListenerCallback
            public void onActivityRestartAttempt(ActivityManager.RunningTaskInfo runningTaskInfo, boolean z, boolean z2, boolean z3) {
                for (Bubble bubble : BubbleController.this.mBubbleData.getBubbles()) {
                    if (runningTaskInfo.taskId == bubble.getTaskId()) {
                        BubbleController.this.mBubbleData.setSelectedBubble(bubble);
                        BubbleController.this.mBubbleData.setExpanded(true);
                        return;
                    }
                }
                for (Bubble bubble2 : BubbleController.this.mBubbleData.getOverflowBubbles()) {
                    if (runningTaskInfo.taskId == bubble2.getTaskId()) {
                        BubbleController.this.promoteBubbleFromOverflow(bubble2);
                        BubbleController.this.mBubbleData.setExpanded(true);
                        return;
                    }
                }
            }
        });
        this.mDisplayController.addDisplayChangingController(new DisplayChangeController.OnDisplayChangingListener() { // from class: com.android.wm.shell.bubbles.BubbleController.3
            @Override // com.android.wm.shell.common.DisplayChangeController.OnDisplayChangingListener
            public void onRotateDisplay(int i, int i2, int i3, WindowContainerTransaction windowContainerTransaction) {
                if (i2 != i3) {
                    BubbleController.this.mBubblePositioner.setRotation(i3);
                    if (BubbleController.this.mStackView != null) {
                        BubbleController.this.mStackView.onOrientationChanged();
                    }
                }
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$1(Bubble bubble) {
        if (bubble.getBubbleIntent() != null) {
            if (bubble.isIntentActive() || this.mBubbleData.hasBubbleInStackWithKey(bubble.getKey())) {
                bubble.setPendingIntentCanceled();
            } else {
                this.mMainExecutor.execute(new Runnable(bubble) { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda6
                    public final /* synthetic */ Bubble f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        BubbleController.this.lambda$initialize$0(this.f$1);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$0(Bubble bubble) {
        removeBubble(bubble.getKey(), 10);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initialize$2(int i, LocusId locusId, boolean z) {
        this.mBubbleData.onLocusVisibilityChanged(i, locusId, z);
    }

    @VisibleForTesting
    public Bubbles asBubbles() {
        return this.mImpl;
    }

    @VisibleForTesting
    public BubblesImpl.CachedState getImplCachedState() {
        return this.mImpl.mCachedState;
    }

    public ShellExecutor getMainExecutor() {
        return this.mMainExecutor;
    }

    /* access modifiers changed from: package-private */
    public void hideCurrentInputMethod() {
        try {
            this.mBarService.hideCurrentInputMethodForBubbles();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void openBubbleOverflow() {
        ensureStackViewCreated();
        this.mBubbleData.setShowingOverflow(true);
        BubbleData bubbleData = this.mBubbleData;
        bubbleData.setSelectedBubble(bubbleData.getOverflow());
        this.mBubbleData.setExpanded(true);
    }

    /* access modifiers changed from: private */
    /* JADX INFO: Can't fix incorrect switch cases order, some code will duplicate */
    public void onTaskbarChanged(Bundle bundle) {
        boolean z;
        if (bundle != null) {
            boolean z2 = bundle.getBoolean("taskbarVisible", false);
            String string = bundle.getString("taskbarPosition", "Right");
            string.hashCode();
            int i = 2;
            switch (string.hashCode()) {
                case 2364455:
                    if (string.equals("Left")) {
                        z = false;
                        break;
                    }
                    z = true;
                    break;
                case 78959100:
                    if (string.equals("Right")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                case 1995605579:
                    if (string.equals("Bottom")) {
                        z = true;
                        break;
                    }
                    z = true;
                    break;
                default:
                    z = true;
                    break;
            }
            switch (z) {
                case false:
                    i = 1;
                    break;
                case true:
                    i = 0;
                    break;
                case true:
                    break;
                default:
                    i = -1;
                    break;
            }
            int[] intArray = bundle.getIntArray("taskbarBubbleXY");
            int i2 = bundle.getInt("taskbarIconSize");
            int i3 = bundle.getInt("taskbarSize");
            Log.w("Bubbles", "onTaskbarChanged: isVisible: " + z2 + " position: " + string + " itemPosition: " + intArray[0] + "," + intArray[1] + " iconSize: " + i2);
            PointF pointF = new PointF((float) intArray[0], (float) intArray[1]);
            BubblePositioner bubblePositioner = this.mBubblePositioner;
            if (!z2) {
                pointF = null;
            }
            bubblePositioner.setPinnedLocation(pointF);
            this.mBubblePositioner.updateForTaskbar(i2, i, z2, i3);
            if (this.mStackView != null) {
                if (z2 && bundle.getBoolean("taskbarCreated", false)) {
                    removeFromWindowManagerMaybe();
                    addToWindowManagerMaybe();
                }
                this.mStackView.updateStackPosition();
                this.mBubbleIconFactory = new BubbleIconFactory(this.mContext);
                this.mStackView.onDisplaySizeChanged();
            }
            if (bundle.getBoolean("bubbleOverflowOpened", false)) {
                openBubbleOverflow();
            }
        }
    }

    /* access modifiers changed from: private */
    public void onStatusBarVisibilityChanged(boolean z) {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.setTemporarilyInvisible(!z && !isStackExpanded());
        }
    }

    /* access modifiers changed from: private */
    public void onZenStateChanged() {
        for (Bubble bubble : this.mBubbleData.getBubbles()) {
            bubble.setShowDot(bubble.showInShade());
        }
    }

    /* access modifiers changed from: private */
    public void onStatusBarStateChanged(boolean z) {
        this.mIsStatusBarShade = z;
        if (!z) {
            collapseStack();
        }
        BubbleEntry bubbleEntry = this.mNotifEntryToExpandOnShadeUnlock;
        if (bubbleEntry != null) {
            expandStackAndSelectBubble(bubbleEntry);
            this.mNotifEntryToExpandOnShadeUnlock = null;
        }
        updateStack();
    }

    @VisibleForTesting
    public void onBubbleNotificationSuppressionChanged(Bubble bubble) {
        try {
            this.mBarService.onBubbleNotificationSuppressionChanged(bubble.getKey(), !bubble.showInShade(), bubble.isSuppressed());
        } catch (RemoteException unused) {
        }
        this.mImpl.mCachedState.updateBubbleSuppressedState(bubble);
    }

    @VisibleForTesting
    public void onUserChanged(int i) {
        saveBubbles(this.mCurrentUserId);
        this.mCurrentUserId = i;
        this.mBubbleData.dismissAll(8);
        this.mBubbleData.clearOverflow();
        this.mOverflowDataLoadNeeded = true;
        restoreBubbles(i);
        this.mBubbleData.setCurrentUserId(i);
    }

    public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        this.mCurrentProfiles = sparseArray;
    }

    private boolean isCurrentProfile(int i) {
        SparseArray<UserInfo> sparseArray;
        return i == -1 || !((sparseArray = this.mCurrentProfiles) == null || sparseArray.get(i) == null);
    }

    @VisibleForTesting
    public void setInflateSynchronously(boolean z) {
        this.mInflateSynchronously = z;
    }

    public void setOverflowListener(BubbleData.Listener listener) {
        this.mOverflowListener = listener;
    }

    /* access modifiers changed from: package-private */
    public List<Bubble> getOverflowBubbles() {
        return this.mBubbleData.getOverflowBubbles();
    }

    public ShellTaskOrganizer getTaskOrganizer() {
        return this.mTaskOrganizer;
    }

    /* access modifiers changed from: package-private */
    public BubblePositioner getPositioner() {
        return this.mBubblePositioner;
    }

    /* access modifiers changed from: package-private */
    public Bubbles.SysuiProxy getSysuiProxy() {
        return this.mSysuiProxy;
    }

    /* access modifiers changed from: private */
    public void ensureStackViewCreated() {
        if (this.mStackView == null) {
            BubbleStackView bubbleStackView = new BubbleStackView(this.mContext, this, this.mBubbleData, this.mSurfaceSynchronizer, this.mFloatingContentCoordinator, this.mMainExecutor);
            this.mStackView = bubbleStackView;
            bubbleStackView.onOrientationChanged();
            Bubbles.BubbleExpandListener bubbleExpandListener = this.mExpandListener;
            if (bubbleExpandListener != null) {
                this.mStackView.setExpandListener(bubbleExpandListener);
            }
            BubbleStackView bubbleStackView2 = this.mStackView;
            Bubbles.SysuiProxy sysuiProxy = this.mSysuiProxy;
            Objects.requireNonNull(sysuiProxy);
            bubbleStackView2.setUnbubbleConversationCallback(new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda12
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    Bubbles.SysuiProxy.this.onUnbubbleConversation((String) obj);
                }
            });
        }
        addToWindowManagerMaybe();
    }

    private void addToWindowManagerMaybe() {
        if (this.mStackView != null && !this.mAddedToWindowManager) {
            WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams(-1, -1, 2038, 16777256, -3);
            this.mWmLayoutParams = layoutParams;
            layoutParams.setTrustedOverlay();
            this.mWmLayoutParams.setFitInsetsTypes(0);
            WindowManager.LayoutParams layoutParams2 = this.mWmLayoutParams;
            layoutParams2.softInputMode = 16;
            layoutParams2.token = new Binder();
            this.mWmLayoutParams.setTitle("Bubbles!");
            this.mWmLayoutParams.packageName = this.mContext.getPackageName();
            WindowManager.LayoutParams layoutParams3 = this.mWmLayoutParams;
            layoutParams3.layoutInDisplayCutoutMode = 3;
            layoutParams3.privateFlags = 16 | layoutParams3.privateFlags;
            try {
                this.mAddedToWindowManager = true;
                this.mBubbleData.getOverflow().initialize(this);
                this.mStackView.addView(this.mBubbleScrim);
                this.mWindowManager.addView(this.mStackView, this.mWmLayoutParams);
                this.mBubblePositioner.update();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void updateWindowFlagsForOverflow(boolean z) {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null && this.mAddedToWindowManager) {
            WindowManager.LayoutParams layoutParams = this.mWmLayoutParams;
            int i = z ? 0 : 40;
            layoutParams.flags = i;
            layoutParams.flags = i | 16777216;
            this.mWindowManager.updateViewLayout(bubbleStackView, layoutParams);
        }
    }

    private void removeFromWindowManagerMaybe() {
        if (this.mAddedToWindowManager) {
            try {
                this.mAddedToWindowManager = false;
                BubbleStackView bubbleStackView = this.mStackView;
                if (bubbleStackView != null) {
                    this.mWindowManager.removeView(bubbleStackView);
                    this.mStackView.removeView(this.mBubbleScrim);
                    this.mBubbleData.getOverflow().cleanUpExpandedState();
                    return;
                }
                Log.w("Bubbles", "StackView added to WindowManager, but was null when removing!");
            } catch (IllegalArgumentException e) {
                while (true) {
                    e.printStackTrace();
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: package-private */
    public void onAllBubblesAnimatedOut() {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.setVisibility(4);
            removeFromWindowManagerMaybe();
        }
    }

    private void saveBubbles(int i) {
        this.mSavedBubbleKeysPerUser.remove(i);
        for (Bubble bubble : this.mBubbleData.getBubbles()) {
            this.mSavedBubbleKeysPerUser.add(i, bubble.getKey());
        }
    }

    private void restoreBubbles(int i) {
        ArraySet<String> arraySet = this.mSavedBubbleKeysPerUser.get(i);
        if (arraySet != null) {
            this.mSysuiProxy.getShouldRestoredEntries(arraySet, new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda10
                @Override // java.util.function.Consumer
                public final void accept(Object obj) {
                    BubbleController.this.lambda$restoreBubbles$4((List) obj);
                }
            });
            this.mSavedBubbleKeysPerUser.remove(i);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreBubbles$4(List list) {
        this.mMainExecutor.execute(new Runnable(list) { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda8
            public final /* synthetic */ List f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                BubbleController.this.lambda$restoreBubbles$3(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$restoreBubbles$3(List list) {
        Iterator it = list.iterator();
        while (it.hasNext()) {
            BubbleEntry bubbleEntry = (BubbleEntry) it.next();
            if (canLaunchInTaskView(this.mContext, bubbleEntry)) {
                updateBubble(bubbleEntry, true, false);
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateForThemeChanges() {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.onThemeChanged();
        }
        this.mBubbleIconFactory = new BubbleIconFactory(this.mContext);
        for (Bubble bubble : this.mBubbleData.getBubbles()) {
            bubble.inflate(null, this.mContext, this, this.mStackView, this.mBubbleIconFactory, false);
        }
        for (Bubble bubble2 : this.mBubbleData.getOverflowBubbles()) {
            bubble2.inflate(null, this.mContext, this, this.mStackView, this.mBubbleIconFactory, false);
        }
    }

    /* access modifiers changed from: private */
    public void onConfigChanged(Configuration configuration) {
        BubblePositioner bubblePositioner = this.mBubblePositioner;
        if (bubblePositioner != null) {
            bubblePositioner.update();
        }
        if (this.mStackView != null && configuration != null) {
            if (configuration.densityDpi != this.mDensityDpi || !configuration.windowConfiguration.getBounds().equals(this.mScreenBounds)) {
                this.mDensityDpi = configuration.densityDpi;
                this.mScreenBounds.set(configuration.windowConfiguration.getBounds());
                this.mBubbleData.onMaxBubblesChanged();
                this.mBubbleIconFactory = new BubbleIconFactory(this.mContext);
                this.mStackView.onDisplaySizeChanged();
            }
            float f = configuration.fontScale;
            if (f != this.mFontScale) {
                this.mFontScale = f;
                this.mStackView.updateFontScale();
            }
            if (configuration.getLayoutDirection() != this.mLayoutDirection) {
                int layoutDirection = configuration.getLayoutDirection();
                this.mLayoutDirection = layoutDirection;
                this.mStackView.onLayoutDirectionChanged(layoutDirection);
            }
        }
    }

    /* access modifiers changed from: private */
    public void setBubbleScrim(View view, BiConsumer<Executor, Looper> biConsumer) {
        this.mBubbleScrim = view;
        ShellExecutor shellExecutor = this.mMainExecutor;
        biConsumer.accept(shellExecutor, (Looper) shellExecutor.executeBlockingForResult(BubbleController$$ExternalSyntheticLambda13.INSTANCE, Looper.class));
    }

    /* access modifiers changed from: private */
    public void setSysuiProxy(Bubbles.SysuiProxy sysuiProxy) {
        this.mSysuiProxy = sysuiProxy;
    }

    @VisibleForTesting
    public void setExpandListener(Bubbles.BubbleExpandListener bubbleExpandListener) {
        BubbleController$$ExternalSyntheticLambda3 bubbleController$$ExternalSyntheticLambda3 = new Bubbles.BubbleExpandListener() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda3
            @Override // com.android.wm.shell.bubbles.Bubbles.BubbleExpandListener
            public final void onBubbleExpandChanged(boolean z, String str) {
                BubbleController.lambda$setExpandListener$6(Bubbles.BubbleExpandListener.this, z, str);
            }
        };
        this.mExpandListener = bubbleController$$ExternalSyntheticLambda3;
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.setExpandListener(bubbleController$$ExternalSyntheticLambda3);
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$setExpandListener$6(Bubbles.BubbleExpandListener bubbleExpandListener, boolean z, String str) {
        if (bubbleExpandListener != null) {
            bubbleExpandListener.onBubbleExpandChanged(z, str);
        }
    }

    @VisibleForTesting
    public boolean hasBubbles() {
        if (this.mStackView == null) {
            return false;
        }
        if (this.mBubbleData.hasBubbles() || this.mBubbleData.isShowingOverflow()) {
            return true;
        }
        return false;
    }

    @VisibleForTesting
    public boolean isStackExpanded() {
        return this.mBubbleData.isExpanded();
    }

    @VisibleForTesting
    public void collapseStack() {
        this.mBubbleData.setExpanded(false);
    }

    @VisibleForTesting
    public boolean isBubbleNotificationSuppressedFromShade(String str, String str2) {
        return (str.equals(this.mBubbleData.getSummaryKey(str2)) && this.mBubbleData.isSummarySuppressed(str2)) || (this.mBubbleData.hasAnyBubbleWithKey(str) && !this.mBubbleData.getAnyBubbleWithkey(str).showInShade());
    }

    /* access modifiers changed from: private */
    public void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer) {
        if (this.mBubbleData.isSummarySuppressed(str)) {
            this.mBubbleData.removeSuppressedSummary(str);
            if (consumer != null) {
                consumer.accept(this.mBubbleData.getSummaryKey(str));
            }
        }
    }

    public void promoteBubbleFromOverflow(Bubble bubble) {
        this.mLogger.log(bubble, BubbleLogger.Event.BUBBLE_OVERFLOW_REMOVE_BACK_TO_STACK);
        bubble.setInflateSynchronously(this.mInflateSynchronously);
        bubble.setShouldAutoExpand(true);
        bubble.markAsAccessedAt(System.currentTimeMillis());
        setIsBubble(bubble, true);
    }

    public void expandStackAndSelectBubble(Bubble bubble) {
        if (bubble != null) {
            if (this.mBubbleData.hasBubbleInStackWithKey(bubble.getKey())) {
                this.mBubbleData.setSelectedBubble(bubble);
                this.mBubbleData.setExpanded(true);
            } else if (this.mBubbleData.hasOverflowBubbleWithKey(bubble.getKey())) {
                promoteBubbleFromOverflow(bubble);
            }
        }
    }

    public void expandStackAndSelectBubble(BubbleEntry bubbleEntry) {
        if (this.mIsStatusBarShade) {
            this.mNotifEntryToExpandOnShadeUnlock = null;
            String key = bubbleEntry.getKey();
            Bubble bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(key);
            if (bubbleInStackWithKey != null) {
                this.mBubbleData.setSelectedBubble(bubbleInStackWithKey);
                this.mBubbleData.setExpanded(true);
                return;
            }
            Bubble overflowBubbleWithKey = this.mBubbleData.getOverflowBubbleWithKey(key);
            if (overflowBubbleWithKey != null) {
                promoteBubbleFromOverflow(overflowBubbleWithKey);
            } else if (bubbleEntry.canBubble()) {
                setIsBubble(bubbleEntry, true, true);
            }
        } else {
            this.mNotifEntryToExpandOnShadeUnlock = bubbleEntry;
        }
    }

    @VisibleForTesting
    public void updateBubble(BubbleEntry bubbleEntry) {
        updateBubble(bubbleEntry, false, true);
    }

    void loadOverflowBubblesFromDisk() {
        if (this.mBubbleData.getOverflowBubbles().isEmpty() || this.mOverflowDataLoadNeeded) {
            this.mOverflowDataLoadNeeded = false;
            this.mDataRepository.loadBubbles(this.mCurrentUserId, new Function1() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda14
                @Override // kotlin.jvm.functions.Function1
                public final Object invoke(Object obj) {
                    return BubbleController.this.lambda$loadOverflowBubblesFromDisk$9((List) obj);
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ Unit lambda$loadOverflowBubblesFromDisk$9(List list) {
        list.forEach(new Consumer() { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda9
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BubbleController.this.lambda$loadOverflowBubblesFromDisk$8((Bubble) obj);
            }
        });
        return null;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOverflowBubblesFromDisk$8(Bubble bubble) {
        if (!this.mBubbleData.hasAnyBubbleWithKey(bubble.getKey())) {
            bubble.inflate(new BubbleViewInfoTask.Callback(bubble) { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda1
                public final /* synthetic */ Bubble f$1;

                {
                    this.f$1 = r2;
                }

                @Override // com.android.wm.shell.bubbles.BubbleViewInfoTask.Callback
                public final void onBubbleViewsReady(Bubble bubble2) {
                    BubbleController.this.lambda$loadOverflowBubblesFromDisk$7(this.f$1, bubble2);
                }
            }, this.mContext, this, this.mStackView, this.mBubbleIconFactory, true);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$loadOverflowBubblesFromDisk$7(Bubble bubble, Bubble bubble2) {
        this.mBubbleData.overflowBubble(15, bubble);
    }

    @VisibleForTesting
    public void updateBubble(BubbleEntry bubbleEntry, boolean z, boolean z2) {
        this.mSysuiProxy.setNotificationInterruption(bubbleEntry.getKey());
        if (bubbleEntry.getRanking().visuallyInterruptive() || bubbleEntry.getBubbleMetadata() == null || bubbleEntry.getBubbleMetadata().getAutoExpandBubble() || !this.mBubbleData.hasOverflowBubbleWithKey(bubbleEntry.getKey())) {
            inflateAndAdd(this.mBubbleData.getOrCreateBubble(bubbleEntry, null), z, z2);
        } else {
            this.mBubbleData.getOverflowBubbleWithKey(bubbleEntry.getKey()).setEntry(bubbleEntry);
        }
    }

    void inflateAndAdd(Bubble bubble, boolean z, boolean z2) {
        ensureStackViewCreated();
        bubble.setInflateSynchronously(this.mInflateSynchronously);
        bubble.inflate(new BubbleViewInfoTask.Callback(z, z2) { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda2
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ boolean f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.android.wm.shell.bubbles.BubbleViewInfoTask.Callback
            public final void onBubbleViewsReady(Bubble bubble2) {
                BubbleController.this.lambda$inflateAndAdd$10(this.f$1, this.f$2, bubble2);
            }
        }, this.mContext, this, this.mStackView, this.mBubbleIconFactory, false);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$inflateAndAdd$10(boolean z, boolean z2, Bubble bubble) {
        this.mBubbleData.notificationEntryUpdated(bubble, z, z2);
    }

    @VisibleForTesting
    public void removeBubble(String str, int i) {
        if (this.mBubbleData.hasAnyBubbleWithKey(str)) {
            this.mBubbleData.dismissBubbleWithKey(str, i);
        }
    }

    /* access modifiers changed from: private */
    public void onEntryAdded(BubbleEntry bubbleEntry) {
        if (canLaunchInTaskView(this.mContext, bubbleEntry)) {
            updateBubble(bubbleEntry);
        }
    }

    /* access modifiers changed from: private */
    public void onEntryUpdated(BubbleEntry bubbleEntry, boolean z) {
        boolean z2 = z && canLaunchInTaskView(this.mContext, bubbleEntry);
        if (!z2 && this.mBubbleData.hasAnyBubbleWithKey(bubbleEntry.getKey())) {
            removeBubble(bubbleEntry.getKey(), 7);
        } else if (z2 && bubbleEntry.isBubble()) {
            updateBubble(bubbleEntry);
        }
    }

    /* access modifiers changed from: private */
    public void onEntryRemoved(BubbleEntry bubbleEntry) {
        if (isSummaryOfBubbles(bubbleEntry)) {
            String groupKey = bubbleEntry.getStatusBarNotification().getGroupKey();
            this.mBubbleData.removeSuppressedSummary(groupKey);
            ArrayList<Bubble> bubblesInGroup = getBubblesInGroup(groupKey);
            for (int i = 0; i < bubblesInGroup.size(); i++) {
                removeBubble(bubblesInGroup.get(i).getKey(), 9);
            }
            return;
        }
        removeBubble(bubbleEntry.getKey(), 5);
    }

    /* access modifiers changed from: private */
    public void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap) {
        if (this.mTmpRanking == null) {
            this.mTmpRanking = new NotificationListenerService.Ranking();
        }
        String[] orderedKeys = rankingMap.getOrderedKeys();
        for (String str : orderedKeys) {
            Pair<BubbleEntry, Boolean> pair = hashMap.get(str);
            BubbleEntry bubbleEntry = (BubbleEntry) pair.first;
            boolean booleanValue = ((Boolean) pair.second).booleanValue();
            if (bubbleEntry == null || isCurrentProfile(bubbleEntry.getStatusBarNotification().getUser().getIdentifier())) {
                rankingMap.getRanking(str, this.mTmpRanking);
                boolean hasAnyBubbleWithKey = this.mBubbleData.hasAnyBubbleWithKey(str);
                if (hasAnyBubbleWithKey && !this.mTmpRanking.canBubble()) {
                    this.mBubbleData.dismissBubbleWithKey(str, 4);
                } else if (hasAnyBubbleWithKey && !booleanValue) {
                    this.mBubbleData.dismissBubbleWithKey(str, 14);
                } else if (bubbleEntry != null && this.mTmpRanking.isBubble() && !hasAnyBubbleWithKey) {
                    bubbleEntry.setFlagBubble(true);
                    onEntryUpdated(bubbleEntry, true);
                }
            } else {
                return;
            }
        }
    }

    /* access modifiers changed from: private */
    public ArrayList<Bubble> getBubblesInGroup(String str) {
        ArrayList<Bubble> arrayList = new ArrayList<>();
        if (str == null) {
            return arrayList;
        }
        for (Bubble bubble : this.mBubbleData.getActiveBubbles()) {
            if (bubble.getGroupKey() != null && str.equals(bubble.getGroupKey())) {
                arrayList.add(bubble);
            }
        }
        return arrayList;
    }

    private void setIsBubble(BubbleEntry bubbleEntry, boolean z, boolean z2) {
        Objects.requireNonNull(bubbleEntry);
        bubbleEntry.setFlagBubble(z);
        int i = 0;
        if (z2) {
            i = 3;
        }
        try {
            this.mBarService.onNotificationBubbleChanged(bubbleEntry.getKey(), z, i);
        } catch (RemoteException unused) {
        }
    }

    /* access modifiers changed from: private */
    public void setIsBubble(Bubble bubble, boolean z) {
        Objects.requireNonNull(bubble);
        bubble.setIsBubble(z);
        this.mSysuiProxy.getPendingOrActiveEntry(bubble.getKey(), new Consumer(z, bubble) { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda11
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ Bubble f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                BubbleController.this.lambda$setIsBubble$12(this.f$1, this.f$2, (BubbleEntry) obj);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsBubble$12(boolean z, Bubble bubble, BubbleEntry bubbleEntry) {
        this.mMainExecutor.execute(new Runnable(bubbleEntry, z, bubble) { // from class: com.android.wm.shell.bubbles.BubbleController$$ExternalSyntheticLambda7
            public final /* synthetic */ BubbleEntry f$1;
            public final /* synthetic */ boolean f$2;
            public final /* synthetic */ Bubble f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // java.lang.Runnable
            public final void run() {
                BubbleController.this.lambda$setIsBubble$11(this.f$1, this.f$2, this.f$3);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$setIsBubble$11(BubbleEntry bubbleEntry, boolean z, Bubble bubble) {
        if (bubbleEntry != null) {
            setIsBubble(bubbleEntry, z, bubble.shouldAutoExpand());
        } else if (z) {
            Bubble orCreateBubble = this.mBubbleData.getOrCreateBubble(null, bubble);
            inflateAndAdd(orCreateBubble, orCreateBubble.shouldAutoExpand(), !orCreateBubble.shouldAutoExpand());
        }
    }

    /* access modifiers changed from: private */
    public boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer) {
        if (isSummaryOfBubbles(bubbleEntry)) {
            handleSummaryDismissalInterception(bubbleEntry, list, intConsumer);
        } else {
            Bubble bubbleInStackWithKey = this.mBubbleData.getBubbleInStackWithKey(bubbleEntry.getKey());
            if (bubbleInStackWithKey == null || !bubbleEntry.isBubble()) {
                bubbleInStackWithKey = this.mBubbleData.getOverflowBubbleWithKey(bubbleEntry.getKey());
            }
            if (bubbleInStackWithKey == null) {
                return false;
            }
            bubbleInStackWithKey.setSuppressNotification(true);
            bubbleInStackWithKey.setShowDot(false);
        }
        this.mSysuiProxy.notifyInvalidateNotifications("BubbleController.handleDismissalInterception");
        return true;
    }

    private boolean isSummaryOfBubbles(BubbleEntry bubbleEntry) {
        String groupKey = bubbleEntry.getStatusBarNotification().getGroupKey();
        return ((this.mBubbleData.isSummarySuppressed(groupKey) && this.mBubbleData.getSummaryKey(groupKey).equals(bubbleEntry.getKey())) || bubbleEntry.getStatusBarNotification().getNotification().isGroupSummary()) && !getBubblesInGroup(groupKey).isEmpty();
    }

    private void handleSummaryDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                BubbleEntry bubbleEntry2 = list.get(i);
                if (this.mBubbleData.hasAnyBubbleWithKey(bubbleEntry2.getKey())) {
                    Bubble anyBubbleWithkey = this.mBubbleData.getAnyBubbleWithkey(bubbleEntry2.getKey());
                    if (anyBubbleWithkey != null) {
                        this.mSysuiProxy.removeNotificationEntry(anyBubbleWithkey.getKey());
                        anyBubbleWithkey.setSuppressNotification(true);
                        anyBubbleWithkey.setShowDot(false);
                    }
                } else {
                    intConsumer.accept(i);
                }
            }
        }
        intConsumer.accept(-1);
        this.mBubbleData.addSummaryToSuppress(bubbleEntry.getStatusBarNotification().getGroupKey(), bubbleEntry.getKey());
    }

    public void updateStack() {
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            if (!this.mIsStatusBarShade) {
                bubbleStackView.setVisibility(4);
            } else if (hasBubbles()) {
                this.mStackView.setVisibility(0);
            }
            this.mStackView.updateContentDescription();
        }
    }

    @VisibleForTesting
    public BubbleStackView getStackView() {
        return this.mStackView;
    }

    /* access modifiers changed from: private */
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("BubbleController state:");
        this.mBubbleData.dump(fileDescriptor, printWriter, strArr);
        printWriter.println();
        BubbleStackView bubbleStackView = this.mStackView;
        if (bubbleStackView != null) {
            bubbleStackView.dump(fileDescriptor, printWriter, strArr);
        }
        printWriter.println();
    }

    static boolean canLaunchInTaskView(Context context, BubbleEntry bubbleEntry) {
        PendingIntent intent = bubbleEntry.getBubbleMetadata() != null ? bubbleEntry.getBubbleMetadata().getIntent() : null;
        if (bubbleEntry.getBubbleMetadata() != null && bubbleEntry.getBubbleMetadata().getShortcutId() != null) {
            return true;
        }
        if (intent == null) {
            Log.w("Bubbles", "Unable to create bubble -- no intent: " + bubbleEntry.getKey());
            return false;
        }
        ActivityInfo resolveActivityInfo = intent.getIntent().resolveActivityInfo(getPackageManagerForUser(context, bubbleEntry.getStatusBarNotification().getUser().getIdentifier()), 0);
        if (resolveActivityInfo == null) {
            Log.w("Bubbles", "Unable to send as bubble, " + bubbleEntry.getKey() + " couldn't find activity info for intent: " + intent);
            return false;
        } else if (ActivityInfo.isResizeableMode(resolveActivityInfo.resizeMode)) {
            return true;
        } else {
            Log.w("Bubbles", "Unable to send as bubble, " + bubbleEntry.getKey() + " activity is not resizable for intent: " + intent);
            return false;
        }
    }

    /* access modifiers changed from: package-private */
    public static PackageManager getPackageManagerForUser(Context context, int i) {
        if (i >= 0) {
            try {
                context = context.createPackageContextAsUser(context.getPackageName(), 4, new UserHandle(i));
            } catch (PackageManager.NameNotFoundException unused) {
            }
        }
        return context.getPackageManager();
    }

    /* loaded from: classes2.dex */
    private class BubblesImeListener extends PinnedStackListenerForwarder.PinnedTaskListener {
        private BubblesImeListener() {
        }

        @Override // com.android.wm.shell.pip.PinnedStackListenerForwarder.PinnedTaskListener
        public void onImeVisibilityChanged(boolean z, int i) {
            if (BubbleController.this.mStackView != null) {
                BubbleController.this.mStackView.onImeVisibilityChanged(z, i);
            }
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public class BubblesImpl implements Bubbles {
        private CachedState mCachedState;

        private BubblesImpl() {
            this.mCachedState = new CachedState();
        }

        @VisibleForTesting
        /* loaded from: classes2.dex */
        public class CachedState {
            private boolean mIsStackExpanded;
            private String mSelectedBubbleKey;
            private HashSet<String> mSuppressedBubbleKeys = new HashSet<>();
            private HashMap<String, String> mSuppressedGroupToNotifKeys = new HashMap<>();
            private HashMap<String, Bubble> mShortcutIdToBubble = new HashMap<>();
            private ArrayList<Bubble> mTmpBubbles = new ArrayList<>();

            public CachedState() {
            }

            synchronized void update(BubbleData.Update update) {
                if (update.selectionChanged) {
                    BubbleViewProvider bubbleViewProvider = update.selectedBubble;
                    this.mSelectedBubbleKey = bubbleViewProvider != null ? bubbleViewProvider.getKey() : null;
                }
                if (update.expandedChanged) {
                    this.mIsStackExpanded = update.expanded;
                }
                if (update.suppressedSummaryChanged) {
                    String summaryKey = BubbleController.this.mBubbleData.getSummaryKey(update.suppressedSummaryGroup);
                    if (summaryKey != null) {
                        this.mSuppressedGroupToNotifKeys.put(update.suppressedSummaryGroup, summaryKey);
                    } else {
                        this.mSuppressedGroupToNotifKeys.remove(update.suppressedSummaryGroup);
                    }
                }
                this.mTmpBubbles.clear();
                this.mTmpBubbles.addAll(update.bubbles);
                this.mTmpBubbles.addAll(update.overflowBubbles);
                this.mSuppressedBubbleKeys.clear();
                this.mShortcutIdToBubble.clear();
                Iterator<Bubble> it = this.mTmpBubbles.iterator();
                while (it.hasNext()) {
                    Bubble next = it.next();
                    this.mShortcutIdToBubble.put(next.getShortcutId(), next);
                    updateBubbleSuppressedState(next);
                }
            }

            synchronized void updateBubbleSuppressedState(Bubble bubble) {
                if (!bubble.showInShade()) {
                    this.mSuppressedBubbleKeys.add(bubble.getKey());
                } else {
                    this.mSuppressedBubbleKeys.remove(bubble.getKey());
                }
            }

            public synchronized boolean isStackExpanded() {
                return this.mIsStackExpanded;
            }

            public synchronized boolean isBubbleExpanded(String str) {
                boolean z;
                if (this.mIsStackExpanded) {
                    if (str.equals(this.mSelectedBubbleKey)) {
                        z = true;
                    }
                }
                z = false;
                return z;
            }

            /* JADX WARNING: Code restructure failed: missing block: B:8:0x001b, code lost:
                if (r2.equals(r1.mSuppressedGroupToNotifKeys.get(r3)) != false) goto L_0x0020;
             */
            /* Code decompiled incorrectly, please refer to instructions dump. */
            public synchronized boolean isBubbleNotificationSuppressedFromShade(java.lang.String r2, java.lang.String r3) {
                /*
                    r1 = this;
                    monitor-enter(r1)
                    java.util.HashSet<java.lang.String> r0 = r1.mSuppressedBubbleKeys     // Catch: all -> 0x0023
                    boolean r0 = r0.contains(r2)     // Catch: all -> 0x0023
                    if (r0 != 0) goto L_0x0020
                    java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.mSuppressedGroupToNotifKeys     // Catch: all -> 0x0023
                    boolean r0 = r0.containsKey(r3)     // Catch: all -> 0x0023
                    if (r0 == 0) goto L_0x001e
                    java.util.HashMap<java.lang.String, java.lang.String> r0 = r1.mSuppressedGroupToNotifKeys     // Catch: all -> 0x0023
                    java.lang.Object r3 = r0.get(r3)     // Catch: all -> 0x0023
                    boolean r2 = r2.equals(r3)     // Catch: all -> 0x0023
                    if (r2 == 0) goto L_0x001e
                    goto L_0x0020
                L_0x001e:
                    r2 = 0
                    goto L_0x0021
                L_0x0020:
                    r2 = 1
                L_0x0021:
                    monitor-exit(r1)
                    return r2
                L_0x0023:
                    r2 = move-exception
                    monitor-exit(r1)
                    throw r2
                */
                throw new UnsupportedOperationException("Method not decompiled: com.android.wm.shell.bubbles.BubbleController.BubblesImpl.CachedState.isBubbleNotificationSuppressedFromShade(java.lang.String, java.lang.String):boolean");
            }

            public synchronized Bubble getBubbleWithShortcutId(String str) {
                return this.mShortcutIdToBubble.get(str);
            }

            synchronized void dump(PrintWriter printWriter) {
                printWriter.println("BubbleImpl.CachedState state:");
                printWriter.println("mIsStackExpanded: " + this.mIsStackExpanded);
                printWriter.println("mSelectedBubbleKey: " + this.mSelectedBubbleKey);
                printWriter.print("mSuppressedBubbleKeys: ");
                printWriter.println(this.mSuppressedBubbleKeys.size());
                Iterator<String> it = this.mSuppressedBubbleKeys.iterator();
                while (it.hasNext()) {
                    printWriter.println("   suppressing: " + it.next());
                }
                printWriter.print("mSuppressedGroupToNotifKeys: ");
                printWriter.println(this.mSuppressedGroupToNotifKeys.size());
                Iterator<String> it2 = this.mSuppressedGroupToNotifKeys.keySet().iterator();
                while (it2.hasNext()) {
                    printWriter.println("   suppressing: " + it2.next());
                }
            }
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public boolean isBubbleNotificationSuppressedFromShade(String str, String str2) {
            return this.mCachedState.isBubbleNotificationSuppressedFromShade(str, str2);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public boolean isBubbleExpanded(String str) {
            return this.mCachedState.isBubbleExpanded(str);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public boolean isStackExpanded() {
            return this.mCachedState.isStackExpanded();
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public Bubble getBubbleWithShortcutId(String str) {
            return this.mCachedState.getBubbleWithShortcutId(str);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void removeSuppressedSummaryIfNecessary(String str, Consumer<String> consumer, Executor executor) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda17(this, consumer, executor, str));
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$removeSuppressedSummaryIfNecessary$1(Executor executor, Consumer consumer, String str) {
            executor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda20(consumer, str));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$removeSuppressedSummaryIfNecessary$2(Consumer consumer, Executor executor, String str) {
            BubbleController.this.removeSuppressedSummaryIfNecessary(str, consumer != null ? new BubbleController$BubblesImpl$$ExternalSyntheticLambda22(executor, consumer) : null);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void collapseStack() {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda2(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$collapseStack$3() {
            BubbleController.this.collapseStack();
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void updateForThemeChanges() {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda0(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$updateForThemeChanges$4() {
            BubbleController.this.updateForThemeChanges();
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void expandStackAndSelectBubble(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda12(this, bubbleEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$expandStackAndSelectBubble$5(BubbleEntry bubbleEntry) {
            BubbleController.this.expandStackAndSelectBubble(bubbleEntry);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void expandStackAndSelectBubble(Bubble bubble) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda9(this, bubble));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$expandStackAndSelectBubble$6(Bubble bubble) {
            BubbleController.this.expandStackAndSelectBubble(bubble);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onTaskbarChanged(Bundle bundle) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda5(this, bundle));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onTaskbarChanged$7(Bundle bundle) {
            BubbleController.this.onTaskbarChanged(bundle);
        }

        /* access modifiers changed from: private */
        public static /* synthetic */ void lambda$handleDismissalInterception$10(Executor executor, IntConsumer intConsumer, int i) {
            executor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda21(intConsumer, i));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public boolean handleDismissalInterception(BubbleEntry bubbleEntry, List<BubbleEntry> list, IntConsumer intConsumer, Executor executor) {
            return ((Boolean) BubbleController.this.mMainExecutor.executeBlockingForResult(new BubbleController$BubblesImpl$$ExternalSyntheticLambda24(this, bubbleEntry, list, intConsumer != null ? new BubbleController$BubblesImpl$$ExternalSyntheticLambda23(executor, intConsumer) : null), Boolean.class)).booleanValue();
        }

        /* access modifiers changed from: private */
        public /* synthetic */ Boolean lambda$handleDismissalInterception$11(BubbleEntry bubbleEntry, List list, IntConsumer intConsumer) {
            return Boolean.valueOf(BubbleController.this.handleDismissalInterception(bubbleEntry, list, intConsumer));
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void setSysuiProxy(Bubbles.SysuiProxy sysuiProxy) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda15(this, sysuiProxy));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setSysuiProxy$12(Bubbles.SysuiProxy sysuiProxy) {
            BubbleController.this.setSysuiProxy(sysuiProxy);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void setBubbleScrim(View view, BiConsumer<Executor, Looper> biConsumer) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda8(this, view, biConsumer));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setBubbleScrim$13(View view, BiConsumer biConsumer) {
            BubbleController.this.setBubbleScrim(view, biConsumer);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void setExpandListener(Bubbles.BubbleExpandListener bubbleExpandListener) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda14(this, bubbleExpandListener));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$setExpandListener$14(Bubbles.BubbleExpandListener bubbleExpandListener) {
            BubbleController.this.setExpandListener(bubbleExpandListener);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onEntryAdded(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda10(this, bubbleEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEntryAdded$15(BubbleEntry bubbleEntry) {
            BubbleController.this.onEntryAdded(bubbleEntry);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onEntryUpdated(BubbleEntry bubbleEntry, boolean z) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda13(this, bubbleEntry, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEntryUpdated$16(BubbleEntry bubbleEntry, boolean z) {
            BubbleController.this.onEntryUpdated(bubbleEntry, z);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onEntryRemoved(BubbleEntry bubbleEntry) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda11(this, bubbleEntry));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onEntryRemoved$17(BubbleEntry bubbleEntry) {
            BubbleController.this.onEntryRemoved(bubbleEntry);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onRankingUpdated(NotificationListenerService.RankingMap rankingMap, HashMap<String, Pair<BubbleEntry, Boolean>> hashMap) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda6(this, rankingMap, hashMap));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onRankingUpdated$18(NotificationListenerService.RankingMap rankingMap, HashMap hashMap) {
            BubbleController.this.onRankingUpdated(rankingMap, hashMap);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onStatusBarVisibilityChanged(boolean z) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda19(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarVisibilityChanged$19(boolean z) {
            BubbleController.this.onStatusBarVisibilityChanged(z);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onZenStateChanged() {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda1(this));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onZenStateChanged$20() {
            BubbleController.this.onZenStateChanged();
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onStatusBarStateChanged(boolean z) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda18(this, z));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onStatusBarStateChanged$21(boolean z) {
            BubbleController.this.onStatusBarStateChanged(z);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onUserChanged(int i) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda3(this, i));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onUserChanged$22(int i) {
            BubbleController.this.onUserChanged(i);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda7(this, sparseArray));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onCurrentProfilesChanged$23(SparseArray sparseArray) {
            BubbleController.this.onCurrentProfilesChanged(sparseArray);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void onConfigChanged(Configuration configuration) {
            BubbleController.this.mMainExecutor.execute(new BubbleController$BubblesImpl$$ExternalSyntheticLambda4(this, configuration));
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$onConfigChanged$24(Configuration configuration) {
            BubbleController.this.onConfigChanged(configuration);
        }

        @Override // com.android.wm.shell.bubbles.Bubbles
        public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            try {
                BubbleController.this.mMainExecutor.executeBlocking(new BubbleController$BubblesImpl$$ExternalSyntheticLambda16(this, fileDescriptor, printWriter, strArr));
            } catch (InterruptedException unused) {
                Slog.e("Bubbles", "Failed to dump BubbleController in 2s");
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$dump$25(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
            BubbleController.this.dump(fileDescriptor, printWriter, strArr);
            this.mCachedState.dump(printWriter);
        }
    }
}
