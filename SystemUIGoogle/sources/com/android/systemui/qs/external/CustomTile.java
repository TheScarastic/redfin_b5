package com.android.systemui.qs.external;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.metrics.LogMaker;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.service.quicksettings.IQSTileService;
import android.service.quicksettings.Tile;
import android.util.Log;
import android.view.IWindowManager;
import android.view.View;
import android.view.WindowManagerGlobal;
import android.widget.Switch;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.Dependency;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.qs.QSTile;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.qs.QSHost;
import com.android.systemui.qs.external.TileLifecycleManager;
import com.android.systemui.qs.logging.QSLogger;
import com.android.systemui.qs.tileimpl.QSTileImpl;
import dagger.Lazy;
import java.util.Objects;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class CustomTile extends QSTileImpl<QSTile.State> implements TileLifecycleManager.TileChangeListener {
    private final ComponentName mComponent;
    private final CustomTileStatePersister mCustomTileStatePersister;
    private Icon mDefaultIcon;
    private CharSequence mDefaultLabel;
    private boolean mIsShowingDialog;
    private boolean mIsTokenGranted;
    private final TileServiceKey mKey;
    private boolean mListening;
    private final IQSTileService mService;
    private final TileServiceManager mServiceManager;
    private final Tile mTile;
    private final IBinder mToken;
    private final int mUser;
    private final Context mUserContext;
    private final IWindowManager mWindowManager;

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public int getMetricsCategory() {
        return 268;
    }

    private CustomTile(QSHost qSHost, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, String str, Context context, CustomTileStatePersister customTileStatePersister) {
        super(qSHost, looper, handler, falsingManager, metricsLogger, statusBarStateController, activityStarter, qSLogger);
        this.mToken = new Binder();
        this.mWindowManager = WindowManagerGlobal.getWindowManagerService();
        ComponentName unflattenFromString = ComponentName.unflattenFromString(str);
        this.mComponent = unflattenFromString;
        this.mTile = new Tile();
        this.mUserContext = context;
        int userId = context.getUserId();
        this.mUser = userId;
        this.mKey = new TileServiceKey(unflattenFromString, userId);
        TileServiceManager tileWrapper = qSHost.getTileServices().getTileWrapper(this);
        this.mServiceManager = tileWrapper;
        this.mService = tileWrapper.getTileService();
        this.mCustomTileStatePersister = customTileStatePersister;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleInitialize() {
        Tile readState;
        updateDefaultTileAndIcon();
        if (this.mServiceManager.isToggleableTile()) {
            resetStates();
        }
        this.mServiceManager.setTileChangeListener(this);
        if (this.mServiceManager.isActiveTile() && (readState = this.mCustomTileStatePersister.readState(this.mKey)) != null) {
            applyTileState(readState, false);
            this.mServiceManager.clearPendingBind();
            refreshState();
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected long getStaleTimeout() {
        return (((long) this.mHost.indexOf(getTileSpec())) * 60000) + 3600000;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x003f A[Catch: NameNotFoundException -> 0x0079, TryCatch #0 {NameNotFoundException -> 0x0079, blocks: (B:3:0x0001, B:6:0x0012, B:9:0x001d, B:10:0x0021, B:12:0x002b, B:18:0x003f, B:20:0x004b, B:22:0x004f, B:23:0x0054, B:25:0x005c, B:28:0x006b, B:30:0x0073), top: B:33:0x0001 }] */
    /* JADX WARNING: Removed duplicated region for block: B:19:0x004a  */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x004f A[Catch: NameNotFoundException -> 0x0079, TryCatch #0 {NameNotFoundException -> 0x0079, blocks: (B:3:0x0001, B:6:0x0012, B:9:0x001d, B:10:0x0021, B:12:0x002b, B:18:0x003f, B:20:0x004b, B:22:0x004f, B:23:0x0054, B:25:0x005c, B:28:0x006b, B:30:0x0073), top: B:33:0x0001 }] */
    /* JADX WARNING: Removed duplicated region for block: B:30:0x0073 A[Catch: NameNotFoundException -> 0x0079, TRY_LEAVE, TryCatch #0 {NameNotFoundException -> 0x0079, blocks: (B:3:0x0001, B:6:0x0012, B:9:0x001d, B:10:0x0021, B:12:0x002b, B:18:0x003f, B:20:0x004b, B:22:0x004f, B:23:0x0054, B:25:0x005c, B:28:0x006b, B:30:0x0073), top: B:33:0x0001 }] */
    /* JADX WARNING: Removed duplicated region for block: B:35:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void updateDefaultTileAndIcon() {
        /*
            r8 = this;
            r0 = 0
            android.content.Context r1 = r8.mUserContext     // Catch: NameNotFoundException -> 0x0079
            android.content.pm.PackageManager r1 = r1.getPackageManager()     // Catch: NameNotFoundException -> 0x0079
            r2 = 786432(0xc0000, float:1.102026E-39)
            boolean r3 = r8.isSystemApp(r1)     // Catch: NameNotFoundException -> 0x0079
            if (r3 == 0) goto L_0x0012
            r2 = 786944(0xc0200, float:1.102743E-39)
        L_0x0012:
            android.content.ComponentName r3 = r8.mComponent     // Catch: NameNotFoundException -> 0x0079
            android.content.pm.ServiceInfo r2 = r1.getServiceInfo(r3, r2)     // Catch: NameNotFoundException -> 0x0079
            int r3 = r2.icon     // Catch: NameNotFoundException -> 0x0079
            if (r3 == 0) goto L_0x001d
            goto L_0x0021
        L_0x001d:
            android.content.pm.ApplicationInfo r3 = r2.applicationInfo     // Catch: NameNotFoundException -> 0x0079
            int r3 = r3.icon     // Catch: NameNotFoundException -> 0x0079
        L_0x0021:
            android.service.quicksettings.Tile r4 = r8.mTile     // Catch: NameNotFoundException -> 0x0079
            android.graphics.drawable.Icon r4 = r4.getIcon()     // Catch: NameNotFoundException -> 0x0079
            r5 = 0
            r6 = 1
            if (r4 == 0) goto L_0x003c
            android.service.quicksettings.Tile r4 = r8.mTile     // Catch: NameNotFoundException -> 0x0079
            android.graphics.drawable.Icon r4 = r4.getIcon()     // Catch: NameNotFoundException -> 0x0079
            android.graphics.drawable.Icon r7 = r8.mDefaultIcon     // Catch: NameNotFoundException -> 0x0079
            boolean r4 = r8.iconEquals(r4, r7)     // Catch: NameNotFoundException -> 0x0079
            if (r4 == 0) goto L_0x003a
            goto L_0x003c
        L_0x003a:
            r4 = r5
            goto L_0x003d
        L_0x003c:
            r4 = r6
        L_0x003d:
            if (r3 == 0) goto L_0x004a
            android.content.ComponentName r7 = r8.mComponent     // Catch: NameNotFoundException -> 0x0079
            java.lang.String r7 = r7.getPackageName()     // Catch: NameNotFoundException -> 0x0079
            android.graphics.drawable.Icon r3 = android.graphics.drawable.Icon.createWithResource(r7, r3)     // Catch: NameNotFoundException -> 0x0079
            goto L_0x004b
        L_0x004a:
            r3 = r0
        L_0x004b:
            r8.mDefaultIcon = r3     // Catch: NameNotFoundException -> 0x0079
            if (r4 == 0) goto L_0x0054
            android.service.quicksettings.Tile r4 = r8.mTile     // Catch: NameNotFoundException -> 0x0079
            r4.setIcon(r3)     // Catch: NameNotFoundException -> 0x0079
        L_0x0054:
            android.service.quicksettings.Tile r3 = r8.mTile     // Catch: NameNotFoundException -> 0x0079
            java.lang.CharSequence r3 = r3.getLabel()     // Catch: NameNotFoundException -> 0x0079
            if (r3 == 0) goto L_0x006a
            android.service.quicksettings.Tile r3 = r8.mTile     // Catch: NameNotFoundException -> 0x0079
            java.lang.CharSequence r3 = r3.getLabel()     // Catch: NameNotFoundException -> 0x0079
            java.lang.CharSequence r4 = r8.mDefaultLabel     // Catch: NameNotFoundException -> 0x0079
            boolean r3 = android.text.TextUtils.equals(r3, r4)     // Catch: NameNotFoundException -> 0x0079
            if (r3 == 0) goto L_0x006b
        L_0x006a:
            r5 = r6
        L_0x006b:
            java.lang.CharSequence r1 = r2.loadLabel(r1)     // Catch: NameNotFoundException -> 0x0079
            r8.mDefaultLabel = r1     // Catch: NameNotFoundException -> 0x0079
            if (r5 == 0) goto L_0x007d
            android.service.quicksettings.Tile r2 = r8.mTile     // Catch: NameNotFoundException -> 0x0079
            r2.setLabel(r1)     // Catch: NameNotFoundException -> 0x0079
            goto L_0x007d
        L_0x0079:
            r8.mDefaultIcon = r0
            r8.mDefaultLabel = r0
        L_0x007d:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.qs.external.CustomTile.updateDefaultTileAndIcon():void");
    }

    private boolean isSystemApp(PackageManager packageManager) throws PackageManager.NameNotFoundException {
        return packageManager.getApplicationInfo(this.mComponent.getPackageName(), 0).isSystemApp();
    }

    private boolean iconEquals(Icon icon, Icon icon2) {
        if (icon == icon2) {
            return true;
        }
        return icon != null && icon2 != null && icon.getType() == 2 && icon2.getType() == 2 && icon.getResId() == icon2.getResId() && Objects.equals(icon.getResPackage(), icon2.getResPackage());
    }

    @Override // com.android.systemui.qs.external.TileLifecycleManager.TileChangeListener
    public void onTileChanged(ComponentName componentName) {
        this.mHandler.post(new Runnable() { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CustomTile.this.updateDefaultTileAndIcon();
            }
        });
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public boolean isAvailable() {
        return this.mDefaultIcon != null;
    }

    public int getUser() {
        return this.mUser;
    }

    public ComponentName getComponent() {
        return this.mComponent;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public LogMaker populate(LogMaker logMaker) {
        return super.populate(logMaker).setComponentName(this.mComponent);
    }

    public Tile getQsTile() {
        updateDefaultTileAndIcon();
        return this.mTile;
    }

    public void updateTileState(Tile tile) {
        this.mHandler.post(new Runnable(tile) { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda2
            public final /* synthetic */ Tile f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                CustomTile.this.lambda$updateTileState$0(this.f$1);
            }
        });
    }

    /* access modifiers changed from: private */
    /* renamed from: handleUpdateTileState */
    public void lambda$updateTileState$0(Tile tile) {
        applyTileState(tile, true);
        if (this.mServiceManager.isActiveTile()) {
            this.mCustomTileStatePersister.persistState(this.mKey, tile);
        }
    }

    private void applyTileState(Tile tile, boolean z) {
        if (tile.getIcon() != null || z) {
            this.mTile.setIcon(tile.getIcon());
        }
        if (tile.getLabel() != null || z) {
            this.mTile.setLabel(tile.getLabel());
        }
        if (tile.getSubtitle() != null || z) {
            this.mTile.setSubtitle(tile.getSubtitle());
        }
        if (tile.getContentDescription() != null || z) {
            this.mTile.setContentDescription(tile.getContentDescription());
        }
        if (tile.getStateDescription() != null || z) {
            this.mTile.setStateDescription(tile.getStateDescription());
        }
        this.mTile.setState(tile.getState());
    }

    public void onDialogShown() {
        this.mIsShowingDialog = true;
    }

    public void onDialogHidden() {
        this.mIsShowingDialog = false;
        try {
            this.mWindowManager.removeWindowToken(this.mToken, 0);
        } catch (RemoteException unused) {
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public void handleSetListening(boolean z) {
        super.handleSetListening(z);
        if (this.mListening != z) {
            this.mListening = z;
            try {
                if (z) {
                    updateDefaultTileAndIcon();
                    refreshState();
                    if (!this.mServiceManager.isActiveTile()) {
                        this.mServiceManager.setBindRequested(true);
                        this.mService.onStartListening();
                        return;
                    }
                    return;
                }
                this.mService.onStopListening();
                if (this.mIsTokenGranted && !this.mIsShowingDialog) {
                    try {
                        this.mWindowManager.removeWindowToken(this.mToken, 0);
                    } catch (RemoteException unused) {
                    }
                    this.mIsTokenGranted = false;
                }
                this.mIsShowingDialog = false;
                this.mServiceManager.setBindRequested(false);
            } catch (RemoteException unused2) {
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleDestroy() {
        super.handleDestroy();
        if (this.mIsTokenGranted) {
            try {
                this.mWindowManager.removeWindowToken(this.mToken, 0);
            } catch (RemoteException unused) {
            }
        }
        this.mHost.getTileServices().freeService(this, this.mServiceManager);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public QSTile.State newTileState() {
        TileServiceManager tileServiceManager = this.mServiceManager;
        if (tileServiceManager == null || !tileServiceManager.isToggleableTile()) {
            return new QSTile.State();
        }
        return new QSTile.BooleanState();
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    public Intent getLongClickIntent() {
        Intent intent = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
        intent.setPackage(this.mComponent.getPackageName());
        Intent resolveIntent = resolveIntent(intent);
        if (resolveIntent == null) {
            return new Intent("android.settings.APPLICATION_DETAILS_SETTINGS").setData(Uri.fromParts("package", this.mComponent.getPackageName(), null));
        }
        resolveIntent.putExtra("android.intent.extra.COMPONENT_NAME", this.mComponent);
        resolveIntent.putExtra("state", this.mTile.getState());
        return resolveIntent;
    }

    private Intent resolveIntent(Intent intent) {
        ResolveInfo resolveActivityAsUser = this.mContext.getPackageManager().resolveActivityAsUser(intent, 0, this.mUser);
        if (resolveActivityAsUser == null) {
            return null;
        }
        Intent intent2 = new Intent("android.service.quicksettings.action.QS_TILE_PREFERENCES");
        ActivityInfo activityInfo = resolveActivityAsUser.activityInfo;
        return intent2.setClassName(activityInfo.packageName, activityInfo.name);
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleClick(View view) {
        if (this.mTile.getState() != 0) {
            try {
                this.mWindowManager.addWindowToken(this.mToken, 2035, 0, (Bundle) null);
                this.mIsTokenGranted = true;
            } catch (RemoteException unused) {
            }
            try {
                if (this.mServiceManager.isActiveTile()) {
                    this.mServiceManager.setBindRequested(true);
                    this.mService.onStartListening();
                }
                this.mService.onClick(this.mToken);
            } catch (RemoteException unused2) {
            }
        }
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public CharSequence getTileLabel() {
        return getState().label;
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl
    protected void handleUpdateState(QSTile.State state, Object obj) {
        Drawable drawable;
        int state2 = this.mTile.getState();
        boolean z = false;
        if (this.mServiceManager.hasPendingBind()) {
            state2 = 0;
        }
        state.state = state2;
        try {
            drawable = this.mTile.getIcon().loadDrawable(this.mUserContext);
        } catch (Exception unused) {
            Log.w(this.TAG, "Invalid icon, forcing into unavailable state");
            state.state = 0;
            drawable = this.mDefaultIcon.loadDrawable(this.mUserContext);
        }
        state.iconSupplier = new Supplier(drawable) { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda3
            public final /* synthetic */ Drawable f$0;

            {
                this.f$0 = r1;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return CustomTile.lambda$handleUpdateState$1(this.f$0);
            }
        };
        state.label = this.mTile.getLabel();
        CharSequence subtitle = this.mTile.getSubtitle();
        if (subtitle == null || subtitle.length() <= 0) {
            state.secondaryLabel = null;
        } else {
            state.secondaryLabel = subtitle;
        }
        if (this.mTile.getContentDescription() != null) {
            state.contentDescription = this.mTile.getContentDescription();
        } else {
            state.contentDescription = state.label;
        }
        if (this.mTile.getStateDescription() != null) {
            state.stateDescription = this.mTile.getStateDescription();
        } else {
            state.stateDescription = null;
        }
        if (state instanceof QSTile.BooleanState) {
            state.expandedAccessibilityClassName = Switch.class.getName();
            QSTile.BooleanState booleanState = (QSTile.BooleanState) state;
            if (state.state == 2) {
                z = true;
            }
            booleanState.value = z;
        }
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ QSTile.Icon lambda$handleUpdateState$1(Drawable drawable) {
        Drawable.ConstantState constantState;
        if (drawable == null || (constantState = drawable.getConstantState()) == null) {
            return null;
        }
        return new QSTileImpl.DrawableIcon(constantState.newDrawable());
    }

    @Override // com.android.systemui.qs.tileimpl.QSTileImpl, com.android.systemui.plugins.qs.QSTile
    public final String getMetricsSpec() {
        return this.mComponent.getPackageName();
    }

    public void startUnlockAndRun() {
        ((ActivityStarter) Dependency.get(ActivityStarter.class)).postQSRunnableDismissingKeyguard(new Runnable() { // from class: com.android.systemui.qs.external.CustomTile$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                CustomTile.this.lambda$startUnlockAndRun$2();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$startUnlockAndRun$2() {
        try {
            this.mService.onUnlockComplete();
        } catch (RemoteException unused) {
        }
    }

    public static String toSpec(ComponentName componentName) {
        return "custom(" + componentName.flattenToShortString() + ")";
    }

    public static ComponentName getComponentFromSpec(String str) {
        String substring = str.substring(7, str.length() - 1);
        if (!substring.isEmpty()) {
            return ComponentName.unflattenFromString(substring);
        }
        throw new IllegalArgumentException("Empty custom tile spec action");
    }

    /* access modifiers changed from: private */
    public static String getAction(String str) {
        if (str == null || !str.startsWith("custom(") || !str.endsWith(")")) {
            throw new IllegalArgumentException("Bad custom tile spec: " + str);
        }
        String substring = str.substring(7, str.length() - 1);
        if (!substring.isEmpty()) {
            return substring;
        }
        throw new IllegalArgumentException("Empty custom tile spec action");
    }

    public static CustomTile create(Builder builder, String str, Context context) {
        return builder.setSpec(str).setUserContext(context).build();
    }

    /* loaded from: classes.dex */
    public static class Builder {
        final ActivityStarter mActivityStarter;
        final Looper mBackgroundLooper;
        final CustomTileStatePersister mCustomTileStatePersister;
        private final FalsingManager mFalsingManager;
        final Handler mMainHandler;
        final MetricsLogger mMetricsLogger;
        final Lazy<QSHost> mQSHostLazy;
        final QSLogger mQSLogger;
        String mSpec = "";
        final StatusBarStateController mStatusBarStateController;
        Context mUserContext;

        public Builder(Lazy<QSHost> lazy, Looper looper, Handler handler, FalsingManager falsingManager, MetricsLogger metricsLogger, StatusBarStateController statusBarStateController, ActivityStarter activityStarter, QSLogger qSLogger, CustomTileStatePersister customTileStatePersister) {
            this.mQSHostLazy = lazy;
            this.mBackgroundLooper = looper;
            this.mMainHandler = handler;
            this.mFalsingManager = falsingManager;
            this.mMetricsLogger = metricsLogger;
            this.mStatusBarStateController = statusBarStateController;
            this.mActivityStarter = activityStarter;
            this.mQSLogger = qSLogger;
            this.mCustomTileStatePersister = customTileStatePersister;
        }

        Builder setSpec(String str) {
            this.mSpec = str;
            return this;
        }

        Builder setUserContext(Context context) {
            this.mUserContext = context;
            return this;
        }

        CustomTile build() {
            Objects.requireNonNull(this.mUserContext, "UserContext cannot be null");
            return new CustomTile(this.mQSHostLazy.get(), this.mBackgroundLooper, this.mMainHandler, this.mFalsingManager, this.mMetricsLogger, this.mStatusBarStateController, this.mActivityStarter, this.mQSLogger, CustomTile.getAction(this.mSpec), this.mUserContext, this.mCustomTileStatePersister);
        }
    }
}
