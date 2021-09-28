package com.android.systemui.statusbar.lockscreen;

import android.app.PendingIntent;
import android.app.smartspace.SmartspaceConfig;
import android.app.smartspace.SmartspaceManager;
import android.app.smartspace.SmartspaceSession;
import android.app.smartspace.SmartspaceTarget;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.os.Handler;
import android.os.UserHandle;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.Utils;
import com.android.systemui.R$attr;
import com.android.systemui.plugins.ActivityStarter;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserTracker;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.android.systemui.util.concurrency.Execution;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executor;
import kotlin.collections.CollectionsKt__CollectionsKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: LockscreenSmartspaceController.kt */
/* loaded from: classes.dex */
public final class LockscreenSmartspaceController {
    private final ActivityStarter activityStarter;
    private final ConfigurationController configurationController;
    private final ContentResolver contentResolver;
    private final Context context;
    private final Execution execution;
    private final FalsingManager falsingManager;
    private final FeatureFlags featureFlags;
    private final Handler handler;
    private UserHandle managedUserHandle;
    private final BcSmartspaceDataPlugin plugin;
    private final SecureSettings secureSettings;
    private SmartspaceSession session;
    private final LockscreenSmartspaceController$settingsObserver$1 settingsObserver;
    private boolean showSensitiveContentForCurrentUser;
    private boolean showSensitiveContentForManagedUser;
    private final SmartspaceManager smartspaceManager;
    private BcSmartspaceDataPlugin.SmartspaceView smartspaceView;
    private final StatusBarStateController statusBarStateController;
    private final Executor uiExecutor;
    private final UserTracker userTracker;
    private View view;
    private final SmartspaceSession.OnTargetsAvailableListener sessionListener = new SmartspaceSession.OnTargetsAvailableListener(this) { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$sessionListener$1
        final /* synthetic */ LockscreenSmartspaceController this$0;

        /* access modifiers changed from: package-private */
        {
            this.this$0 = r1;
        }

        public final void onTargetsAvailable(List<SmartspaceTarget> list) {
            this.this$0.execution.assertIsMainThread();
            Intrinsics.checkNotNullExpressionValue(list, "targets");
            LockscreenSmartspaceController lockscreenSmartspaceController = this.this$0;
            ArrayList arrayList = new ArrayList();
            for (Object obj : list) {
                if (lockscreenSmartspaceController.filterSmartspaceTarget((SmartspaceTarget) obj)) {
                    arrayList.add(obj);
                }
            }
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.this$0.plugin;
            if (bcSmartspaceDataPlugin != null) {
                bcSmartspaceDataPlugin.onTargetsAvailable(arrayList);
            }
        }
    };
    private final LockscreenSmartspaceController$userTrackerCallback$1 userTrackerCallback = new LockscreenSmartspaceController$userTrackerCallback$1(this);
    private final LockscreenSmartspaceController$configChangeListener$1 configChangeListener = new LockscreenSmartspaceController$configChangeListener$1(this);
    private final LockscreenSmartspaceController$statusBarStateListener$1 statusBarStateListener = new LockscreenSmartspaceController$statusBarStateListener$1(this);

    public LockscreenSmartspaceController(Context context, FeatureFlags featureFlags, SmartspaceManager smartspaceManager, ActivityStarter activityStarter, FalsingManager falsingManager, SecureSettings secureSettings, UserTracker userTracker, ContentResolver contentResolver, ConfigurationController configurationController, StatusBarStateController statusBarStateController, Execution execution, Executor executor, Handler handler, Optional<BcSmartspaceDataPlugin> optional) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(featureFlags, "featureFlags");
        Intrinsics.checkNotNullParameter(smartspaceManager, "smartspaceManager");
        Intrinsics.checkNotNullParameter(activityStarter, "activityStarter");
        Intrinsics.checkNotNullParameter(falsingManager, "falsingManager");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        Intrinsics.checkNotNullParameter(userTracker, "userTracker");
        Intrinsics.checkNotNullParameter(contentResolver, "contentResolver");
        Intrinsics.checkNotNullParameter(configurationController, "configurationController");
        Intrinsics.checkNotNullParameter(statusBarStateController, "statusBarStateController");
        Intrinsics.checkNotNullParameter(execution, "execution");
        Intrinsics.checkNotNullParameter(executor, "uiExecutor");
        Intrinsics.checkNotNullParameter(handler, "handler");
        Intrinsics.checkNotNullParameter(optional, "optionalPlugin");
        this.context = context;
        this.featureFlags = featureFlags;
        this.smartspaceManager = smartspaceManager;
        this.activityStarter = activityStarter;
        this.falsingManager = falsingManager;
        this.secureSettings = secureSettings;
        this.userTracker = userTracker;
        this.contentResolver = contentResolver;
        this.configurationController = configurationController;
        this.statusBarStateController = statusBarStateController;
        this.execution = execution;
        this.uiExecutor = executor;
        this.handler = handler;
        this.plugin = optional.orElse(null);
        this.settingsObserver = new LockscreenSmartspaceController$settingsObserver$1(this, handler);
    }

    public final View getView() {
        View view = this.view;
        if (view != null) {
            return view;
        }
        Intrinsics.throwUninitializedPropertyAccessException("view");
        throw null;
    }

    public final boolean isEnabled() {
        this.execution.assertIsMainThread();
        return this.featureFlags.isSmartspaceEnabled() && this.plugin != null;
    }

    public final View buildAndConnectView(ViewGroup viewGroup) {
        Intrinsics.checkNotNullParameter(viewGroup, "parent");
        this.execution.assertIsMainThread();
        if (isEnabled()) {
            buildView(viewGroup);
            connectSession();
            return getView();
        }
        throw new RuntimeException("Cannot build view when not enabled");
    }

    public final void requestSmartspaceUpdate() {
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    private final void buildView(ViewGroup viewGroup) {
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            if (this.view != null) {
                ViewGroup viewGroup2 = (ViewGroup) getView().getParent();
                if (viewGroup2 != null) {
                    viewGroup2.removeView(getView());
                    return;
                }
                return;
            }
            BcSmartspaceDataPlugin.SmartspaceView view = bcSmartspaceDataPlugin.getView(viewGroup);
            view.registerDataProvider(this.plugin);
            view.setIntentStarter(new BcSmartspaceDataPlugin.IntentStarter(this) { // from class: com.android.systemui.statusbar.lockscreen.LockscreenSmartspaceController$buildView$2
                final /* synthetic */ LockscreenSmartspaceController this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                }

                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
                public void startIntent(View view2, Intent intent) {
                    this.this$0.activityStarter.startActivity(intent, true);
                }

                @Override // com.android.systemui.plugins.BcSmartspaceDataPlugin.IntentStarter
                public void startPendingIntent(PendingIntent pendingIntent) {
                    this.this$0.activityStarter.startPendingIntentDismissingKeyguard(pendingIntent);
                }
            });
            view.setFalsingManager(this.falsingManager);
            Intrinsics.checkNotNullExpressionValue(view, "ssView");
            this.smartspaceView = view;
            this.view = (View) view;
            updateTextColorFromWallpaper();
            this.statusBarStateListener.onDozeAmountChanged(0.0f, this.statusBarStateController.getDozeAmount());
        }
    }

    private final void connectSession() {
        if (this.plugin != null && this.session == null) {
            SmartspaceSession createSmartspaceSession = this.smartspaceManager.createSmartspaceSession(new SmartspaceConfig.Builder(this.context, "lockscreen").build());
            createSmartspaceSession.addOnTargetsAvailableListener(this.uiExecutor, this.sessionListener);
            this.userTracker.addCallback(this.userTrackerCallback, this.uiExecutor);
            this.contentResolver.registerContentObserver(this.secureSettings.getUriFor("lock_screen_allow_private_notifications"), true, this.settingsObserver, -1);
            this.configurationController.addCallback(this.configChangeListener);
            this.statusBarStateController.addCallback(this.statusBarStateListener);
            this.session = createSmartspaceSession;
            reloadSmartspace();
        }
    }

    public final void disconnect() {
        this.execution.assertIsMainThread();
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            if (smartspaceSession != null) {
                smartspaceSession.removeOnTargetsAvailableListener(this.sessionListener);
                smartspaceSession.close();
            }
            this.userTracker.removeCallback(this.userTrackerCallback);
            this.contentResolver.unregisterContentObserver(this.settingsObserver);
            this.configurationController.removeCallback(this.configChangeListener);
            this.statusBarStateController.removeCallback(this.statusBarStateListener);
            this.session = null;
            BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
            if (bcSmartspaceDataPlugin != null) {
                bcSmartspaceDataPlugin.onTargetsAvailable(CollectionsKt__CollectionsKt.emptyList());
            }
        }
    }

    public final void addListener(BcSmartspaceDataPlugin.SmartspaceTargetListener smartspaceTargetListener) {
        Intrinsics.checkNotNullParameter(smartspaceTargetListener, "listener");
        this.execution.assertIsMainThread();
        BcSmartspaceDataPlugin bcSmartspaceDataPlugin = this.plugin;
        if (bcSmartspaceDataPlugin != null) {
            bcSmartspaceDataPlugin.registerListener(smartspaceTargetListener);
        }
    }

    /* access modifiers changed from: private */
    public final boolean filterSmartspaceTarget(SmartspaceTarget smartspaceTarget) {
        UserHandle userHandle = smartspaceTarget.getUserHandle();
        if (Intrinsics.areEqual(userHandle, this.userTracker.getUserHandle())) {
            if (!smartspaceTarget.isSensitive() || this.showSensitiveContentForCurrentUser) {
                return true;
            }
        } else if (Intrinsics.areEqual(userHandle, this.managedUserHandle) && this.userTracker.getUserHandle().getIdentifier() == 0 && (!smartspaceTarget.isSensitive() || this.showSensitiveContentForManagedUser)) {
            return true;
        }
        return false;
    }

    /* access modifiers changed from: private */
    public final void updateTextColorFromWallpaper() {
        int colorAttrDefaultColor = Utils.getColorAttrDefaultColor(this.context, R$attr.wallpaperTextColor);
        BcSmartspaceDataPlugin.SmartspaceView smartspaceView = this.smartspaceView;
        if (smartspaceView != null) {
            smartspaceView.setPrimaryTextColor(colorAttrDefaultColor);
        } else {
            Intrinsics.throwUninitializedPropertyAccessException("smartspaceView");
            throw null;
        }
    }

    /* access modifiers changed from: private */
    public final void reloadSmartspace() {
        Integer num;
        boolean z = false;
        this.showSensitiveContentForCurrentUser = this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, this.userTracker.getUserId()) == 1;
        UserHandle workProfileUser = getWorkProfileUser();
        this.managedUserHandle = workProfileUser;
        if (workProfileUser == null) {
            num = null;
        } else {
            num = Integer.valueOf(workProfileUser.getIdentifier());
        }
        if (num != null) {
            if (this.secureSettings.getIntForUser("lock_screen_allow_private_notifications", 0, num.intValue()) == 1) {
                z = true;
            }
            this.showSensitiveContentForManagedUser = z;
        }
        SmartspaceSession smartspaceSession = this.session;
        if (smartspaceSession != null) {
            smartspaceSession.requestSmartspaceUpdate();
        }
    }

    private final UserHandle getWorkProfileUser() {
        for (UserInfo userInfo : this.userTracker.getUserProfiles()) {
            if (userInfo.isManagedProfile()) {
                return userInfo.getUserHandle();
            }
        }
        return null;
    }
}
