package com.android.systemui.qs.tileimpl;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.ArraySet;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LifecycleRegistry;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.InstanceId;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.RestrictedLockUtils;
import com.android.settingslib.RestrictedLockUtilsInternal;
import com.android.systemui.Dumpable;
import com.android.systemui.animation.ActivityLaunchAnimator;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.DetailAdapter;
import com.android.systemui.plugins.qs.QSIconView;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.qs.QSTile.State;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSEvent;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.SideLabelTileLayout;
import com.android.systemui.qs.logging.QSLogger;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public abstract class QSTileImpl<TState extends QSTile.State> implements QSTile, LifecycleOwner, Dumpable {
    protected final ActivityStarter mActivityStarter;
    private boolean mAnnounceNextStateChange;
    protected final Context mContext;
    private RestrictedLockUtils.EnforcedAdmin mEnforcedAdmin;
    private final FalsingManager mFalsingManager;
    protected final QSTileImpl<TState>.H mHandler;
    protected final QSHost mHost;
    private final InstanceId mInstanceId;
    private int mIsFullQs;
    private final MetricsLogger mMetricsLogger;
    private final QSLogger mQSLogger;
    private volatile int mReadyState;
    private boolean mShowingDetail;
    protected TState mState;
    private final StatusBarStateController mStatusBarStateController;
    private String mTileSpec;
    private TState mTmpState;
    private final UiEventLogger mUiEventLogger;
    protected final Handler mUiHandler;
    protected static final boolean DEBUG = Log.isLoggable("Tile", 3);
    protected static final Object ARG_SHOW_TRANSIENT_ENABLING = new Object();
    protected final String TAG = "Tile." + getClass().getSimpleName();
    private final ArraySet<Object> mListeners = new ArraySet<>();
    private final ArrayList<QSTile.Callback> mCallbacks = new ArrayList<>();
    private final Object mStaleListener = new Object();
    private final LifecycleRegistry mLifecycle = new LifecycleRegistry(this);

    protected String composeChangeAnnouncement() {
        return null;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public DetailAdapter getDetailAdapter() {
        return null;
    }

    public abstract Intent getLongClickIntent();

    @Override // com.android.systemui.plugins.qs.QSTile
    public abstract int getMetricsCategory();

    protected long getStaleTimeout() {
        return 600000;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public abstract CharSequence getTileLabel();

    protected abstract void handleClick(View view);

    protected void handleInitialize() {
    }

    protected abstract void handleUpdateState(TState tstate, Object obj);

    @Override // com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return true;
    }

    public abstract TState newTileState();

    @Override // com.android.systemui.plugins.qs.QSTile
    public void setDetailListening(boolean z) {
    }

    protected boolean shouldAnnouncementBeDelayed() {
        return false;
    }

    /* access modifiers changed from: protected */
    public QSTileImpl(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger) {
        this.mHost = qSHost;
        this.mContext = qSHost.getContext();
        this.mInstanceId = qSHost.getNewInstanceId();
        this.mUiEventLogger = qSHost.getUiEventLogger();
        this.mUiHandler = handler;
        this.mHandler = new H(looper);
        this.mFalsingManager = falsingManager;
        this.mQSLogger = qSLogger;
        this.mMetricsLogger = metricsLogger;
        this.mStatusBarStateController = statusBarStateController;
        this.mActivityStarter = activityStarter;
        resetStates();
        handler.post(new Runnable() { // from class: com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda3
            @Override // java.lang.Runnable
            public final void run() {
                QSTileImpl.this.lambda$new$0();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0() {
        this.mLifecycle.setCurrentState(Lifecycle.State.CREATED);
    }

    /* access modifiers changed from: protected */
    public final void resetStates() {
        this.mState = newTileState();
        TState newTileState = newTileState();
        this.mTmpState = newTileState;
        TState tstate = this.mState;
        String str = this.mTileSpec;
        tstate.spec = str;
        newTileState.spec = str;
    }

    @Override // androidx.lifecycle.LifecycleOwner
    public Lifecycle getLifecycle() {
        return this.mLifecycle;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public InstanceId getInstanceId() {
        return this.mInstanceId;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void setListening(Object obj, boolean z) {
        this.mHandler.obtainMessage(13, z ? 1 : 0, 0, obj).sendToTarget();
    }

    @VisibleForTesting
    protected void handleStale() {
        setListening(this.mStaleListener, true);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public String getTileSpec() {
        return this.mTileSpec;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void setTileSpec(String str) {
        this.mTileSpec = str;
        this.mState.spec = str;
        this.mTmpState.spec = str;
    }

    public QSHost getHost() {
        return this.mHost;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public QSIconView createTileView(Context context) {
        return new QSIconViewImpl(context);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void addCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(1, callback).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void removeCallback(QSTile.Callback callback) {
        this.mHandler.obtainMessage(12, callback).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void removeCallbacks() {
        this.mHandler.sendEmptyMessage(11);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void click(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(925).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_CLICK, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        if (!this.mFalsingManager.isFalseTap(1)) {
            this.mHandler.obtainMessage(2, view).sendToTarget();
        }
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void secondaryClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(926).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_SECONDARY_CLICK, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileSecondaryClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(3, view).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void longClick(View view) {
        this.mMetricsLogger.write(populate(new LogMaker(366).setType(4).addTaggedData(1592, Integer.valueOf(this.mStatusBarStateController.getState()))));
        this.mUiEventLogger.logWithInstanceId(QSEvent.QS_ACTION_LONG_PRESS, 0, getMetricsSpec(), getInstanceId());
        this.mQSLogger.logTileLongClick(this.mTileSpec, this.mStatusBarStateController.getState(), this.mState.state);
        this.mHandler.obtainMessage(4, view).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public LogMaker populate(LogMaker logMaker) {
        TState tstate = this.mState;
        if (tstate instanceof QSTile.BooleanState) {
            logMaker.addTaggedData(928, Integer.valueOf(((QSTile.BooleanState) tstate).value ? 1 : 0));
        }
        return logMaker.setSubtype(getMetricsCategory()).addTaggedData(1593, Integer.valueOf(this.mIsFullQs)).addTaggedData(927, Integer.valueOf(this.mHost.indexOf(this.mTileSpec)));
    }

    public void showDetail(boolean z) {
        this.mHandler.obtainMessage(6, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void refreshState() {
        refreshState(null);
    }

    /* access modifiers changed from: protected */
    public final void refreshState(Object obj) {
        this.mHandler.obtainMessage(5, obj).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void userSwitch(int i) {
        this.mHandler.obtainMessage(7, i, 0).sendToTarget();
    }

    public void fireToggleStateChanged(boolean z) {
        this.mHandler.obtainMessage(8, z ? 1 : 0, 0).sendToTarget();
    }

    public void fireScanStateChanged(boolean z) {
        this.mHandler.obtainMessage(9, z ? 1 : 0, 0).sendToTarget();
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public void destroy() {
        this.mHandler.sendEmptyMessage(10);
    }

    public void initialize() {
        this.mHandler.sendEmptyMessage(15);
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public TState getState() {
        return this.mState;
    }

    /* access modifiers changed from: private */
    public void handleAddCallback(QSTile.Callback callback) {
        this.mCallbacks.add(callback);
        callback.onStateChanged(this.mState);
    }

    /* access modifiers changed from: private */
    public void handleRemoveCallback(QSTile.Callback callback) {
        this.mCallbacks.remove(callback);
    }

    /* access modifiers changed from: private */
    public void handleRemoveCallbacks() {
        this.mCallbacks.clear();
    }

    public void postStale() {
        this.mHandler.sendEmptyMessage(14);
    }

    protected void handleSecondaryClick(View view) {
        handleClick(view);
    }

    protected void handleLongClick(View view) {
        this.mActivityStarter.postStartActivityDismissingKeyguard(getLongClickIntent(), 0, view != null ? ActivityLaunchAnimator.Controller.fromView(view, 32) : null);
    }

    /* access modifiers changed from: protected */
    public void handleRefreshState(Object obj) {
        handleUpdateState(this.mTmpState, obj);
        boolean copyTo = this.mTmpState.copyTo(this.mState);
        if (this.mReadyState == 1) {
            this.mReadyState = 2;
            copyTo = true;
        }
        if (copyTo) {
            this.mQSLogger.logTileUpdated(this.mTileSpec, this.mState);
            handleStateChanged();
        }
        this.mHandler.removeMessages(14);
        this.mHandler.sendEmptyMessageDelayed(14, getStaleTimeout());
        setListening(this.mStaleListener, false);
    }

    private void handleStateChanged() {
        String composeChangeAnnouncement;
        boolean shouldAnnouncementBeDelayed = shouldAnnouncementBeDelayed();
        boolean z = false;
        if (this.mCallbacks.size() != 0) {
            for (int i = 0; i < this.mCallbacks.size(); i++) {
                this.mCallbacks.get(i).onStateChanged(this.mState);
            }
            if (this.mAnnounceNextStateChange && !shouldAnnouncementBeDelayed && (composeChangeAnnouncement = composeChangeAnnouncement()) != null) {
                this.mCallbacks.get(0).onAnnouncementRequested(composeChangeAnnouncement);
            }
        }
        if (this.mAnnounceNextStateChange && shouldAnnouncementBeDelayed) {
            z = true;
        }
        this.mAnnounceNextStateChange = z;
    }

    /* access modifiers changed from: private */
    public void handleShowDetail(boolean z) {
        this.mShowingDetail = z;
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onShowDetail(z);
        }
    }

    /* access modifiers changed from: protected */
    public boolean isShowingDetail() {
        return this.mShowingDetail;
    }

    /* access modifiers changed from: private */
    public void handleToggleStateChanged(boolean z) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onToggleStateChanged(z);
        }
    }

    /* access modifiers changed from: private */
    public void handleScanStateChanged(boolean z) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            this.mCallbacks.get(i).onScanStateChanged(z);
        }
    }

    /* access modifiers changed from: protected */
    public void handleUserSwitch(int i) {
        handleRefreshState(null);
    }

    /* access modifiers changed from: private */
    public void handleSetListeningInternal(Object obj, boolean z) {
        if (z) {
            if (this.mListeners.add(obj) && this.mListeners.size() == 1) {
                if (DEBUG) {
                    Log.d(this.TAG, "handleSetListening true");
                }
                handleSetListening(z);
                this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda1
                    @Override // java.lang.Runnable
                    public final void run() {
                        QSTileImpl.this.lambda$handleSetListeningInternal$1();
                    }
                });
            }
        } else if (this.mListeners.remove(obj) && this.mListeners.size() == 0) {
            if (DEBUG) {
                Log.d(this.TAG, "handleSetListening false");
            }
            handleSetListening(z);
            this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda2
                @Override // java.lang.Runnable
                public final void run() {
                    QSTileImpl.this.lambda$handleSetListeningInternal$2();
                }
            });
        }
        updateIsFullQs();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListeningInternal$1() {
        if (!this.mLifecycle.getCurrentState().equals(Lifecycle.State.DESTROYED)) {
            this.mLifecycle.setCurrentState(Lifecycle.State.RESUMED);
            if (this.mReadyState == 0) {
                this.mReadyState = 1;
            }
            refreshState();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleSetListeningInternal$2() {
        if (!this.mLifecycle.getCurrentState().equals(Lifecycle.State.DESTROYED)) {
            this.mLifecycle.setCurrentState(Lifecycle.State.STARTED);
        }
    }

    private void updateIsFullQs() {
        Iterator<Object> it = this.mListeners.iterator();
        while (it.hasNext()) {
            if (SideLabelTileLayout.class.equals(it.next().getClass())) {
                this.mIsFullQs = 1;
                return;
            }
        }
        this.mIsFullQs = 0;
    }

    /* access modifiers changed from: protected */
    public void handleSetListening(boolean z) {
        String str = this.mTileSpec;
        if (str != null) {
            this.mQSLogger.logTileChangeListening(str, z);
        }
    }

    /* access modifiers changed from: protected */
    public void handleDestroy() {
        this.mQSLogger.logTileDestroyed(this.mTileSpec, "Handle destroy");
        if (this.mListeners.size() != 0) {
            handleSetListening(false);
            this.mListeners.clear();
        }
        this.mCallbacks.clear();
        this.mHandler.removeCallbacksAndMessages(null);
        this.mUiHandler.post(new Runnable() { // from class: com.android.systemui.qs.tileimpl.QSTileImpl$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                QSTileImpl.this.lambda$handleDestroy$3();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$handleDestroy$3() {
        this.mLifecycle.setCurrentState(Lifecycle.State.DESTROYED);
    }

    /* access modifiers changed from: protected */
    public void checkIfRestrictionEnforcedByAdminOnly(QSTile.State state, String str) {
        RestrictedLockUtils.EnforcedAdmin checkIfRestrictionEnforced = RestrictedLockUtilsInternal.checkIfRestrictionEnforced(this.mContext, str, this.mHost.getUserId());
        if (checkIfRestrictionEnforced == null || RestrictedLockUtilsInternal.hasBaseUserRestriction(this.mContext, str, this.mHost.getUserId())) {
            state.disabledByPolicy = false;
            this.mEnforcedAdmin = null;
            return;
        }
        state.disabledByPolicy = true;
        this.mEnforcedAdmin = checkIfRestrictionEnforced;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public String getMetricsSpec() {
        return this.mTileSpec;
    }

    @Override // com.android.systemui.plugins.qs.QSTile
    public boolean isTileReady() {
        return this.mReadyState == 2;
    }

    /* access modifiers changed from: protected */
    /* loaded from: classes.dex */
    public final class H extends Handler {
        @VisibleForTesting
        protected static final int STALE = 14;

        @VisibleForTesting
        protected H(Looper looper) {
            super(looper);
        }

        @Override // android.os.Handler
        public void handleMessage(Message message) {
            try {
                int i = message.what;
                boolean z = true;
                if (i == 1) {
                    QSTileImpl.this.handleAddCallback((QSTile.Callback) message.obj);
                } else if (i == 11) {
                    QSTileImpl.this.handleRemoveCallbacks();
                } else if (i == 12) {
                    QSTileImpl.this.handleRemoveCallback((QSTile.Callback) message.obj);
                } else if (i == 2) {
                    QSTileImpl qSTileImpl = QSTileImpl.this;
                    if (qSTileImpl.mState.disabledByPolicy) {
                        QSTileImpl.this.mActivityStarter.postStartActivityDismissingKeyguard(RestrictedLockUtils.getShowAdminSupportDetailsIntent(qSTileImpl.mContext, qSTileImpl.mEnforcedAdmin), 0);
                        return;
                    }
                    qSTileImpl.handleClick((View) message.obj);
                } else if (i == 3) {
                    QSTileImpl.this.handleSecondaryClick((View) message.obj);
                } else if (i == 4) {
                    QSTileImpl.this.handleLongClick((View) message.obj);
                } else if (i == 5) {
                    QSTileImpl.this.handleRefreshState(message.obj);
                } else if (i == 6) {
                    QSTileImpl qSTileImpl2 = QSTileImpl.this;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    qSTileImpl2.handleShowDetail(z);
                } else if (i == 7) {
                    QSTileImpl.this.handleUserSwitch(message.arg1);
                } else if (i == 8) {
                    QSTileImpl qSTileImpl3 = QSTileImpl.this;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    qSTileImpl3.handleToggleStateChanged(z);
                } else if (i == 9) {
                    QSTileImpl qSTileImpl4 = QSTileImpl.this;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    qSTileImpl4.handleScanStateChanged(z);
                } else if (i == 10) {
                    QSTileImpl.this.handleDestroy();
                } else if (i == 13) {
                    QSTileImpl qSTileImpl5 = QSTileImpl.this;
                    Object obj = message.obj;
                    if (message.arg1 == 0) {
                        z = false;
                    }
                    qSTileImpl5.handleSetListeningInternal(obj, z);
                } else if (i == 14) {
                    QSTileImpl.this.handleStale();
                } else if (i == 15) {
                    QSTileImpl.this.handleInitialize();
                } else {
                    throw new IllegalArgumentException("Unknown msg: " + message.what);
                }
            } catch (Throwable th) {
                String str = "Error in " + ((String) null);
                Log.w(QSTileImpl.this.TAG, str, th);
                QSTileImpl.this.mHost.warn(str, th);
            }
        }
    }

    /* loaded from: classes.dex */
    public static class DrawableIcon extends QSTile.Icon {
        protected final Drawable mDrawable;
        protected final Drawable mInvisibleDrawable;

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public String toString() {
            return "DrawableIcon";
        }

        public DrawableIcon(Drawable drawable) {
            this.mDrawable = drawable;
            this.mInvisibleDrawable = drawable.getConstantState().newDrawable();
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public Drawable getDrawable(Context context) {
            return this.mDrawable;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public Drawable getInvisibleDrawable(Context context) {
            return this.mInvisibleDrawable;
        }
    }

    /* loaded from: classes.dex */
    public static class ResourceIcon extends QSTile.Icon {
        private static final SparseArray<QSTile.Icon> ICONS = new SparseArray<>();
        protected final int mResId;

        private ResourceIcon(int i) {
            this.mResId = i;
        }

        public static synchronized QSTile.Icon get(int i) {
            QSTile.Icon icon;
            synchronized (ResourceIcon.class) {
                SparseArray<QSTile.Icon> sparseArray = ICONS;
                icon = sparseArray.get(i);
                if (icon == null) {
                    icon = new ResourceIcon(i);
                    sparseArray.put(i, icon);
                }
            }
            return icon;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public Drawable getDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public Drawable getInvisibleDrawable(Context context) {
            return context.getDrawable(this.mResId);
        }

        public boolean equals(Object obj) {
            return (obj instanceof ResourceIcon) && ((ResourceIcon) obj).mResId == this.mResId;
        }

        @Override // com.android.systemui.plugins.qs.QSTile.Icon
        public String toString() {
            return String.format("ResourceIcon[resId=0x%08x]", Integer.valueOf(this.mResId));
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(getClass().getSimpleName() + ":");
        printWriter.print("    ");
        printWriter.println(getState().toString());
    }
}
