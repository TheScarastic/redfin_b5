package com.android.systemui.statusbar.notification.row;

import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.IconDrawableFactory;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityManager;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.systemui.Dependency;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.NotificationLifetimeExtender;
import com.android.systemui.statusbar.NotificationLockscreenUserManager;
import com.android.systemui.statusbar.NotificationPresenter;
import com.android.systemui.statusbar.StatusBarStateControllerImpl;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.NotificationActivityStarter;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.provider.HighPriorityProvider;
import com.android.systemui.statusbar.notification.row.NotificationConversationInfo;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.notification.row.NotificationInfo;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.statusbar.phone.StatusBar;
import com.android.systemui.statusbar.policy.DeviceProvisionedController;
import com.android.systemui.wmshell.BubblesManager;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class NotificationGutsManager implements Dumpable, NotificationLifetimeExtender {
    private final AccessibilityManager mAccessibilityManager;
    private final AppWidgetManager mAppWidgetManager;
    private final AssistantFeedbackController mAssistantFeedbackController;
    private final Handler mBgHandler;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final ChannelEditorDialogController mChannelEditorDialogController;
    private NotificationInfo.CheckSaveListener mCheckSaveListener;
    private final Context mContext;
    private final UserContextProvider mContextTracker;
    private NotificationMenuRowPlugin.MenuItem mGutsMenuItem;
    private final HighPriorityProvider mHighPriorityProvider;
    @VisibleForTesting
    protected String mKeyToRemoveOnGutsClosed;
    private final LauncherApps mLauncherApps;
    private NotificationListContainer mListContainer;
    private final Handler mMainHandler;
    private NotificationActivityStarter mNotificationActivityStarter;
    private final NotificationEntryManager mNotificationEntryManager;
    private NotificationGuts mNotificationGutsExposed;
    private NotificationLifetimeExtender.NotificationSafeToRemoveCallback mNotificationLifetimeFinishedCallback;
    private final INotificationManager mNotificationManager;
    private OnSettingsClickListener mOnSettingsClickListener;
    private final OnUserInteractionCallback mOnUserInteractionCallback;
    private Runnable mOpenRunnable;
    private final PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    private NotificationPresenter mPresenter;
    private final ShadeController mShadeController;
    private final ShortcutManager mShortcutManager;
    private final Lazy<StatusBar> mStatusBarLazy;
    private final UiEventLogger mUiEventLogger;
    private final MetricsLogger mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
    private final NotificationLockscreenUserManager mLockscreenUserManager = (NotificationLockscreenUserManager) Dependency.get(NotificationLockscreenUserManager.class);
    private final StatusBarStateController mStatusBarStateController = (StatusBarStateController) Dependency.get(StatusBarStateController.class);
    private final DeviceProvisionedController mDeviceProvisionedController = (DeviceProvisionedController) Dependency.get(DeviceProvisionedController.class);

    /* loaded from: classes.dex */
    public interface OnSettingsClickListener {
        void onSettingsClick(String str);
    }

    public NotificationGutsManager(Context context, Lazy<StatusBar> lazy, Handler handler, Handler handler2, AccessibilityManager accessibilityManager, HighPriorityProvider highPriorityProvider, INotificationManager iNotificationManager, NotificationEntryManager notificationEntryManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, LauncherApps launcherApps, ShortcutManager shortcutManager, ChannelEditorDialogController channelEditorDialogController, UserContextProvider userContextProvider, AssistantFeedbackController assistantFeedbackController, Optional<BubblesManager> optional, UiEventLogger uiEventLogger, OnUserInteractionCallback onUserInteractionCallback, ShadeController shadeController) {
        this.mContext = context;
        this.mStatusBarLazy = lazy;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mAccessibilityManager = accessibilityManager;
        this.mHighPriorityProvider = highPriorityProvider;
        this.mNotificationManager = iNotificationManager;
        this.mNotificationEntryManager = notificationEntryManager;
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
        this.mLauncherApps = launcherApps;
        this.mShortcutManager = shortcutManager;
        this.mContextTracker = userContextProvider;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mBubblesManagerOptional = optional;
        this.mUiEventLogger = uiEventLogger;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mShadeController = shadeController;
        this.mAppWidgetManager = AppWidgetManager.getInstance(context);
    }

    public void setUpWithPresenter(NotificationPresenter notificationPresenter, NotificationListContainer notificationListContainer, NotificationInfo.CheckSaveListener checkSaveListener, OnSettingsClickListener onSettingsClickListener) {
        this.mPresenter = notificationPresenter;
        this.mListContainer = notificationListContainer;
        this.mCheckSaveListener = checkSaveListener;
        this.mOnSettingsClickListener = onSettingsClickListener;
    }

    public void setNotificationActivityStarter(NotificationActivityStarter notificationActivityStarter) {
        this.mNotificationActivityStarter = notificationActivityStarter;
    }

    public void onDensityOrFontScaleChanged(NotificationEntry notificationEntry) {
        setExposedGuts(notificationEntry.getGuts());
        bindGuts(notificationEntry.getRow());
    }

    private void startAppNotificationSettingsActivity(String str, int i, NotificationChannel notificationChannel, ExpandableNotificationRow expandableNotificationRow) {
        Intent intent = new Intent("android.settings.APP_NOTIFICATION_SETTINGS");
        intent.putExtra("android.provider.extra.APP_PACKAGE", str);
        intent.putExtra("app_uid", i);
        if (notificationChannel != null) {
            Bundle bundle = new Bundle();
            intent.putExtra(":settings:fragment_args_key", notificationChannel.getId());
            bundle.putString(":settings:fragment_args_key", notificationChannel.getId());
            intent.putExtra(":settings:show_fragment_args", bundle);
        }
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, i, expandableNotificationRow);
    }

    private boolean bindGuts(ExpandableNotificationRow expandableNotificationRow) {
        expandableNotificationRow.ensureGutsInflated();
        return bindGuts(expandableNotificationRow, this.mGutsMenuItem);
    }

    @VisibleForTesting
    protected boolean bindGuts(ExpandableNotificationRow expandableNotificationRow, NotificationMenuRowPlugin.MenuItem menuItem) {
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        expandableNotificationRow.setGutsView(menuItem);
        expandableNotificationRow.setTag(sbn.getPackageName());
        expandableNotificationRow.getGuts().setClosedListener(new NotificationGuts.OnGutsClosedListener(expandableNotificationRow, sbn) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda2
            public final /* synthetic */ ExpandableNotificationRow f$1;
            public final /* synthetic */ StatusBarNotification f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.OnGutsClosedListener
            public final void onGutsClosed(NotificationGuts notificationGuts) {
                NotificationGutsManager.this.lambda$bindGuts$0(this.f$1, this.f$2, notificationGuts);
            }
        });
        View gutsView = menuItem.getGutsView();
        try {
            if (gutsView instanceof NotificationSnooze) {
                initializeSnoozeView(expandableNotificationRow, (NotificationSnooze) gutsView);
                return true;
            } else if (gutsView instanceof NotificationInfo) {
                initializeNotificationInfo(expandableNotificationRow, (NotificationInfo) gutsView);
                return true;
            } else if (gutsView instanceof NotificationConversationInfo) {
                initializeConversationNotificationInfo(expandableNotificationRow, (NotificationConversationInfo) gutsView);
                return true;
            } else if (gutsView instanceof PartialConversationInfo) {
                initializePartialConversationNotificationInfo(expandableNotificationRow, (PartialConversationInfo) gutsView);
                return true;
            } else if (!(gutsView instanceof FeedbackInfo)) {
                return true;
            } else {
                initializeFeedbackInfo(expandableNotificationRow, (FeedbackInfo) gutsView);
                return true;
            }
        } catch (Exception e) {
            Log.e("NotificationGutsManager", "error binding guts", e);
            return false;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindGuts$0(ExpandableNotificationRow expandableNotificationRow, StatusBarNotification statusBarNotification, NotificationGuts notificationGuts) {
        expandableNotificationRow.onGutsClosed();
        if (!notificationGuts.willBeRemoved() && !expandableNotificationRow.isRemoved()) {
            this.mListContainer.onHeightChanged(expandableNotificationRow, !this.mPresenter.isPresenterFullyCollapsed());
        }
        if (this.mNotificationGutsExposed == notificationGuts) {
            this.mNotificationGutsExposed = null;
            this.mGutsMenuItem = null;
        }
        String key = statusBarNotification.getKey();
        if (key.equals(this.mKeyToRemoveOnGutsClosed)) {
            this.mKeyToRemoveOnGutsClosed = null;
            NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback = this.mNotificationLifetimeFinishedCallback;
            if (notificationSafeToRemoveCallback != null) {
                notificationSafeToRemoveCallback.onSafeToRemove(key);
            }
        }
    }

    private void initializeSnoozeView(ExpandableNotificationRow expandableNotificationRow, NotificationSnooze notificationSnooze) {
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        notificationSnooze.setSnoozeListener(this.mListContainer.getSwipeActionHelper());
        notificationSnooze.setStatusBarNotification(sbn);
        notificationSnooze.setSnoozeOptions(expandableNotificationRow.getEntry().getSnoozeCriteria());
        guts.setHeightChangedListener(new NotificationGuts.OnHeightChangedListener(expandableNotificationRow) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda3
            public final /* synthetic */ ExpandableNotificationRow f$1;

            {
                this.f$1 = r2;
            }

            @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.OnHeightChangedListener
            public final void onHeightChanged(NotificationGuts notificationGuts) {
                NotificationGutsManager.this.lambda$initializeSnoozeView$1(this.f$1, notificationGuts);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeSnoozeView$1(ExpandableNotificationRow expandableNotificationRow, NotificationGuts notificationGuts) {
        this.mListContainer.onHeightChanged(expandableNotificationRow, expandableNotificationRow.isShown());
    }

    private void initializeFeedbackInfo(ExpandableNotificationRow expandableNotificationRow, FeedbackInfo feedbackInfo) {
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.mContext, sbn.getUser().getIdentifier());
        if (this.mAssistantFeedbackController.showFeedbackIndicator(expandableNotificationRow.getEntry())) {
            feedbackInfo.bindGuts(packageManagerForUser, sbn, expandableNotificationRow.getEntry(), expandableNotificationRow, this.mAssistantFeedbackController);
        }
    }

    @VisibleForTesting
    void initializeNotificationInfo(ExpandableNotificationRow expandableNotificationRow, NotificationInfo notificationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda6 notificationGutsManager$$ExternalSyntheticLambda6;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.mContext, user.getIdentifier());
        NotificationGutsManager$$ExternalSyntheticLambda4 notificationGutsManager$$ExternalSyntheticLambda4 = new NotificationInfo.OnAppSettingsClickListener(guts, sbn, expandableNotificationRow) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda4
            public final /* synthetic */ NotificationGuts f$1;
            public final /* synthetic */ StatusBarNotification f$2;
            public final /* synthetic */ ExpandableNotificationRow f$3;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
            }

            @Override // com.android.systemui.statusbar.notification.row.NotificationInfo.OnAppSettingsClickListener
            public final void onClick(View view, Intent intent) {
                NotificationGutsManager.this.lambda$initializeNotificationInfo$2(this.f$1, this.f$2, this.f$3, view, intent);
            }
        };
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda6 = new NotificationInfo.OnSettingsClickListener(guts, sbn, packageName, expandableNotificationRow) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda6
                public final /* synthetic */ NotificationGuts f$1;
                public final /* synthetic */ StatusBarNotification f$2;
                public final /* synthetic */ String f$3;
                public final /* synthetic */ ExpandableNotificationRow f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationInfo.OnSettingsClickListener
                public final void onClick(View view, NotificationChannel notificationChannel, int i) {
                    NotificationGutsManager.this.lambda$initializeNotificationInfo$3(this.f$1, this.f$2, this.f$3, this.f$4, view, notificationChannel, i);
                }
            };
        } else {
            notificationGutsManager$$ExternalSyntheticLambda6 = null;
        }
        notificationInfo.bindNotification(packageManagerForUser, this.mNotificationManager, this.mOnUserInteractionCallback, this.mChannelEditorDialogController, packageName, expandableNotificationRow.getEntry().getChannel(), expandableNotificationRow.getUniqueChannels(), expandableNotificationRow.getEntry(), notificationGutsManager$$ExternalSyntheticLambda6, notificationGutsManager$$ExternalSyntheticLambda4, this.mUiEventLogger, this.mDeviceProvisionedController.isDeviceProvisioned(), expandableNotificationRow.getIsNonblockable(), this.mHighPriorityProvider.isHighPriority(expandableNotificationRow.getEntry()), this.mAssistantFeedbackController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeNotificationInfo$2(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, ExpandableNotificationRow expandableNotificationRow, View view, Intent intent) {
        this.mMetricsLogger.action(206);
        notificationGuts.resetFalsingCheck();
        this.mNotificationActivityStarter.startNotificationGutsIntent(intent, statusBarNotification.getUid(), expandableNotificationRow);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeNotificationInfo$3(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    @VisibleForTesting
    void initializePartialConversationNotificationInfo(ExpandableNotificationRow expandableNotificationRow, PartialConversationInfo partialConversationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda5 notificationGutsManager$$ExternalSyntheticLambda5;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        StatusBarNotification sbn = expandableNotificationRow.getEntry().getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.mContext, user.getIdentifier());
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda5 = new NotificationInfo.OnSettingsClickListener(guts, sbn, packageName, expandableNotificationRow) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda5
                public final /* synthetic */ NotificationGuts f$1;
                public final /* synthetic */ StatusBarNotification f$2;
                public final /* synthetic */ String f$3;
                public final /* synthetic */ ExpandableNotificationRow f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationInfo.OnSettingsClickListener
                public final void onClick(View view, NotificationChannel notificationChannel, int i) {
                    NotificationGutsManager.this.lambda$initializePartialConversationNotificationInfo$4(this.f$1, this.f$2, this.f$3, this.f$4, view, notificationChannel, i);
                }
            };
        } else {
            notificationGutsManager$$ExternalSyntheticLambda5 = null;
        }
        partialConversationInfo.bindNotification(packageManagerForUser, this.mNotificationManager, this.mChannelEditorDialogController, packageName, expandableNotificationRow.getEntry().getChannel(), expandableNotificationRow.getUniqueChannels(), expandableNotificationRow.getEntry(), notificationGutsManager$$ExternalSyntheticLambda5, this.mDeviceProvisionedController.isDeviceProvisioned(), expandableNotificationRow.getIsNonblockable());
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializePartialConversationNotificationInfo$4(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    @VisibleForTesting
    void initializeConversationNotificationInfo(ExpandableNotificationRow expandableNotificationRow, NotificationConversationInfo notificationConversationInfo) throws Exception {
        NotificationGutsManager$$ExternalSyntheticLambda1 notificationGutsManager$$ExternalSyntheticLambda1;
        NotificationGuts guts = expandableNotificationRow.getGuts();
        NotificationEntry entry = expandableNotificationRow.getEntry();
        StatusBarNotification sbn = entry.getSbn();
        String packageName = sbn.getPackageName();
        UserHandle user = sbn.getUser();
        PackageManager packageManagerForUser = StatusBar.getPackageManagerForUser(this.mContext, user.getIdentifier());
        NotificationGutsManager$$ExternalSyntheticLambda0 notificationGutsManager$$ExternalSyntheticLambda0 = new NotificationConversationInfo.OnConversationSettingsClickListener(this, sbn, expandableNotificationRow) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda0
        };
        if (!user.equals(UserHandle.ALL) || this.mLockscreenUserManager.getCurrentUserId() == 0) {
            notificationGutsManager$$ExternalSyntheticLambda1 = new NotificationConversationInfo.OnSettingsClickListener(guts, sbn, packageName, expandableNotificationRow) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda1
                public final /* synthetic */ NotificationGuts f$1;
                public final /* synthetic */ StatusBarNotification f$2;
                public final /* synthetic */ String f$3;
                public final /* synthetic */ ExpandableNotificationRow f$4;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                    this.f$3 = r4;
                    this.f$4 = r5;
                }

                @Override // com.android.systemui.statusbar.notification.row.NotificationConversationInfo.OnSettingsClickListener
                public final void onClick(View view, NotificationChannel notificationChannel, int i) {
                    NotificationGutsManager.this.lambda$initializeConversationNotificationInfo$7(this.f$1, this.f$2, this.f$3, this.f$4, view, notificationChannel, i);
                }
            };
        } else {
            notificationGutsManager$$ExternalSyntheticLambda1 = null;
        }
        Context context = this.mContext;
        notificationConversationInfo.bindNotification(notificationConversationInfo.getSelectedAction(), this.mShortcutManager, packageManagerForUser, this.mPeopleSpaceWidgetManager, this.mNotificationManager, this.mOnUserInteractionCallback, packageName, entry.getChannel(), entry, entry.getBubbleMetadata(), notificationGutsManager$$ExternalSyntheticLambda1, new ConversationIconFactory(context, this.mLauncherApps, packageManagerForUser, IconDrawableFactory.newInstance(context, false), this.mContext.getResources().getDimensionPixelSize(R$dimen.notification_guts_conversation_icon_size)), this.mContextTracker.getUserContext(), this.mDeviceProvisionedController.isDeviceProvisioned(), this.mMainHandler, this.mBgHandler, notificationGutsManager$$ExternalSyntheticLambda0, this.mBubblesManagerOptional, this.mShadeController);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$initializeConversationNotificationInfo$7(NotificationGuts notificationGuts, StatusBarNotification statusBarNotification, String str, ExpandableNotificationRow expandableNotificationRow, View view, NotificationChannel notificationChannel, int i) {
        this.mMetricsLogger.action(205);
        notificationGuts.resetFalsingCheck();
        this.mOnSettingsClickListener.onSettingsClick(statusBarNotification.getKey());
        startAppNotificationSettingsActivity(str, i, notificationChannel, expandableNotificationRow);
    }

    public void closeAndSaveGuts(boolean z, boolean z2, boolean z3, int i, int i2, boolean z4) {
        NotificationGuts notificationGuts = this.mNotificationGutsExposed;
        if (notificationGuts != null) {
            notificationGuts.removeCallbacks(this.mOpenRunnable);
            this.mNotificationGutsExposed.closeControls(z, z3, i, i2, z2);
        }
        if (z4) {
            this.mListContainer.resetExposedMenuView(false, true);
        }
    }

    public NotificationGuts getExposedGuts() {
        return this.mNotificationGutsExposed;
    }

    public void setExposedGuts(NotificationGuts notificationGuts) {
        this.mNotificationGutsExposed = notificationGuts;
    }

    public boolean openGuts(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if (!(menuItem.getGutsView() instanceof NotificationGuts.GutsContent) || !((NotificationGuts.GutsContent) menuItem.getGutsView()).needsFalsingProtection()) {
            return lambda$openGuts$8(view, i, i2, menuItem);
        }
        StatusBarStateController statusBarStateController = this.mStatusBarStateController;
        if (statusBarStateController instanceof StatusBarStateControllerImpl) {
            ((StatusBarStateControllerImpl) statusBarStateController).setLeaveOpenOnKeyguardHide(true);
        }
        this.mStatusBarLazy.get().executeRunnableDismissingKeyguard(new Runnable(view, i, i2, menuItem) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda8
            public final /* synthetic */ View f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ int f$3;
            public final /* synthetic */ NotificationMenuRowPlugin.MenuItem f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NotificationGutsManager.this.lambda$openGuts$9(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        }, null, false, true, true);
        return true;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$openGuts$9(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        this.mMainHandler.post(new Runnable(view, i, i2, menuItem) { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager$$ExternalSyntheticLambda7
            public final /* synthetic */ View f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ int f$3;
            public final /* synthetic */ NotificationMenuRowPlugin.MenuItem f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NotificationGutsManager.this.lambda$openGuts$8(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        });
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    /* renamed from: openGutsInternal */
    public boolean lambda$openGuts$8(View view, final int i, final int i2, final NotificationMenuRowPlugin.MenuItem menuItem) {
        if (!(view instanceof ExpandableNotificationRow)) {
            return false;
        }
        if (view.getWindowToken() == null) {
            Log.e("NotificationGutsManager", "Trying to show notification guts, but not attached to window");
            return false;
        }
        final ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
        view.performHapticFeedback(0);
        if (expandableNotificationRow.areGutsExposed()) {
            closeAndSaveGuts(false, false, true, -1, -1, true);
            return false;
        }
        expandableNotificationRow.ensureGutsInflated();
        final NotificationGuts guts = expandableNotificationRow.getGuts();
        this.mNotificationGutsExposed = guts;
        if (!bindGuts(expandableNotificationRow, menuItem) || guts == null) {
            return false;
        }
        guts.setVisibility(4);
        AnonymousClass1 r7 = new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationGutsManager.1
            @Override // java.lang.Runnable
            public void run() {
                if (expandableNotificationRow.getWindowToken() == null) {
                    Log.e("NotificationGutsManager", "Trying to show notification guts in post(), but not attached to window");
                    return;
                }
                guts.setVisibility(0);
                boolean z = NotificationGutsManager.this.mStatusBarStateController.getState() == 1 && !NotificationGutsManager.this.mAccessibilityManager.isTouchExplorationEnabled();
                NotificationGuts notificationGuts = guts;
                boolean z2 = !expandableNotificationRow.isBlockingHelperShowing();
                int i3 = i;
                int i4 = i2;
                ExpandableNotificationRow expandableNotificationRow2 = expandableNotificationRow;
                Objects.requireNonNull(expandableNotificationRow2);
                notificationGuts.openControls(z2, i3, i4, z, new NotificationGutsManager$1$$ExternalSyntheticLambda0(expandableNotificationRow2));
                expandableNotificationRow.closeRemoteInput();
                NotificationGutsManager.this.mListContainer.onHeightChanged(expandableNotificationRow, true);
                NotificationGutsManager.this.mGutsMenuItem = menuItem;
            }
        };
        this.mOpenRunnable = r7;
        guts.post(r7);
        return true;
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public void setCallback(NotificationLifetimeExtender.NotificationSafeToRemoveCallback notificationSafeToRemoveCallback) {
        this.mNotificationLifetimeFinishedCallback = notificationSafeToRemoveCallback;
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public boolean shouldExtendLifetime(NotificationEntry notificationEntry) {
        return (notificationEntry == null || this.mNotificationGutsExposed == null || notificationEntry.getGuts() == null || this.mNotificationGutsExposed != notificationEntry.getGuts() || this.mNotificationGutsExposed.isLeavebehind()) ? false : true;
    }

    @Override // com.android.systemui.statusbar.NotificationLifetimeExtender
    public void setShouldManageLifetime(NotificationEntry notificationEntry, boolean z) {
        if (z) {
            this.mKeyToRemoveOnGutsClosed = notificationEntry.getKey();
            if (Log.isLoggable("NotificationGutsManager", 3)) {
                Log.d("NotificationGutsManager", "Keeping notification because it's showing guts. " + notificationEntry.getKey());
                return;
            }
            return;
        }
        String str = this.mKeyToRemoveOnGutsClosed;
        if (str != null && str.equals(notificationEntry.getKey())) {
            this.mKeyToRemoveOnGutsClosed = null;
            if (Log.isLoggable("NotificationGutsManager", 3)) {
                Log.d("NotificationGutsManager", "Notification that was kept for guts was updated. " + notificationEntry.getKey());
            }
        }
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println("NotificationGutsManager state:");
        printWriter.print("  mKeyToRemoveOnGutsClosed: ");
        printWriter.println(this.mKeyToRemoveOnGutsClosed);
    }
}
