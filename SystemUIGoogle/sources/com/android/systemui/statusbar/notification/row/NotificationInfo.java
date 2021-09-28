package com.android.systemui.statusbar.notification.row;

import android.animation.TimeInterpolator;
import android.app.INotificationManager;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.metrics.LogMaker;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.service.notification.StatusBarNotification;
import android.text.Html;
import android.text.TextUtils;
import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.transition.TransitionSet;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.MetricsLogger;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.notification.AssistantFeedbackController;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public class NotificationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    private int mActualHeight;
    private String mAppName;
    private OnAppSettingsClickListener mAppSettingsClickListener;
    private int mAppUid;
    private AssistantFeedbackController mAssistantFeedbackController;
    private TextView mAutomaticDescriptionView;
    private ChannelEditorDialogController mChannelEditorDialogController;
    private Integer mChosenImportance;
    private String mDelegatePkg;
    private NotificationEntry mEntry;
    private NotificationGuts mGutsContainer;
    private INotificationManager mINotificationManager;
    private boolean mIsAutomaticChosen;
    private boolean mIsDeviceProvisioned;
    private boolean mIsNonblockable;
    private boolean mIsSingleDefaultChannel;
    private MetricsLogger mMetricsLogger;
    private int mNumUniqueChannelsInRow;
    private OnSettingsClickListener mOnSettingsClickListener;
    private OnUserInteractionCallback mOnUserInteractionCallback;
    private String mPackageName;
    private Drawable mPkgIcon;
    private PackageManager mPm;
    private boolean mPressedApply;
    private TextView mPriorityDescriptionView;
    private StatusBarNotification mSbn;
    private boolean mShowAutomaticSetting;
    private TextView mSilentDescriptionView;
    private NotificationChannel mSingleNotificationChannel;
    private int mStartingChannelImportance;
    private UiEventLogger mUiEventLogger;
    private Set<NotificationChannel> mUniqueChannelsInRow;
    private boolean mWasShownHighPriority;
    private boolean mPresentingChannelEditorDialog = false;
    @VisibleForTesting
    boolean mSkipPost = false;
    private View.OnClickListener mOnAutomatic = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda3
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationInfo.this.lambda$new$0(view);
        }
    };
    private View.OnClickListener mOnAlert = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda0
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationInfo.this.lambda$new$1(view);
        }
    };
    private View.OnClickListener mOnSilent = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda2
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationInfo.this.lambda$new$2(view);
        }
    };
    private View.OnClickListener mOnDismissSettings = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda4
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationInfo.this.lambda$new$3(view);
        }
    };

    /* loaded from: classes.dex */
    public interface CheckSaveListener {
    }

    /* loaded from: classes.dex */
    public interface OnAppSettingsClickListener {
        void onClick(View view, Intent intent);
    }

    /* loaded from: classes.dex */
    public interface OnSettingsClickListener {
        void onClick(View view, NotificationChannel notificationChannel, int i);
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public View getContentView() {
        return this;
    }

    @VisibleForTesting
    public boolean isAnimating() {
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public boolean needsFalsingProtection() {
        return true;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public boolean willBeRemoved() {
        return false;
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$0(View view) {
        this.mIsAutomaticChosen = true;
        applyAlertingBehavior(2, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        this.mChosenImportance = 3;
        this.mIsAutomaticChosen = false;
        applyAlertingBehavior(0, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        this.mChosenImportance = 2;
        this.mIsAutomaticChosen = false;
        applyAlertingBehavior(1, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        this.mPressedApply = true;
        this.mGutsContainer.closeControls(view, true);
    }

    public NotificationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPriorityDescriptionView = (TextView) findViewById(R$id.alert_summary);
        this.mSilentDescriptionView = (TextView) findViewById(R$id.silence_summary);
        this.mAutomaticDescriptionView = (TextView) findViewById(R$id.automatic_summary);
    }

    public void bindNotification(PackageManager packageManager, INotificationManager iNotificationManager, OnUserInteractionCallback onUserInteractionCallback, ChannelEditorDialogController channelEditorDialogController, String str, NotificationChannel notificationChannel, Set<NotificationChannel> set, NotificationEntry notificationEntry, OnSettingsClickListener onSettingsClickListener, OnAppSettingsClickListener onAppSettingsClickListener, UiEventLogger uiEventLogger, boolean z, boolean z2, boolean z3, AssistantFeedbackController assistantFeedbackController) throws RemoteException {
        this.mINotificationManager = iNotificationManager;
        this.mMetricsLogger = (MetricsLogger) Dependency.get(MetricsLogger.class);
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mChannelEditorDialogController = channelEditorDialogController;
        this.mAssistantFeedbackController = assistantFeedbackController;
        this.mPackageName = str;
        this.mUniqueChannelsInRow = set;
        this.mNumUniqueChannelsInRow = set.size();
        this.mEntry = notificationEntry;
        this.mSbn = notificationEntry.getSbn();
        this.mPm = packageManager;
        this.mAppSettingsClickListener = onAppSettingsClickListener;
        this.mAppName = this.mPackageName;
        this.mOnSettingsClickListener = onSettingsClickListener;
        this.mSingleNotificationChannel = notificationChannel;
        this.mStartingChannelImportance = notificationChannel.getImportance();
        this.mWasShownHighPriority = z3;
        this.mIsNonblockable = z2;
        this.mAppUid = this.mSbn.getUid();
        this.mDelegatePkg = this.mSbn.getOpPkg();
        this.mIsDeviceProvisioned = z;
        this.mShowAutomaticSetting = this.mAssistantFeedbackController.isFeedbackEnabled();
        this.mUiEventLogger = uiEventLogger;
        boolean z4 = false;
        int numNotificationChannelsForPackage = this.mINotificationManager.getNumNotificationChannelsForPackage(str, this.mAppUid, false);
        int i = this.mNumUniqueChannelsInRow;
        if (i != 0) {
            this.mIsSingleDefaultChannel = i == 1 && this.mSingleNotificationChannel.getId().equals("miscellaneous") && numNotificationChannelsForPackage == 1;
            if (getAlertingBehavior() == 2) {
                z4 = true;
            }
            this.mIsAutomaticChosen = z4;
            bindHeader();
            bindChannelDetails();
            bindInlineControls();
            logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_OPEN);
            this.mMetricsLogger.write(notificationControlsLogMaker());
            return;
        }
        throw new IllegalArgumentException("bindNotification requires at least one channel");
    }

    private void bindInlineControls() {
        if (this.mIsNonblockable) {
            findViewById(R$id.non_configurable_text).setVisibility(0);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(8);
            ((TextView) findViewById(R$id.done)).setText(R$string.inline_done_button);
            findViewById(R$id.turn_off_notifications).setVisibility(8);
        } else if (this.mNumUniqueChannelsInRow > 1) {
            findViewById(R$id.non_configurable_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(8);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(0);
        } else {
            findViewById(R$id.non_configurable_text).setVisibility(8);
            findViewById(R$id.non_configurable_multichannel_text).setVisibility(8);
            findViewById(R$id.interruptiveness_settings).setVisibility(0);
        }
        View findViewById = findViewById(R$id.turn_off_notifications);
        findViewById.setOnClickListener(getTurnOffNotificationsClickListener());
        findViewById.setVisibility((!findViewById.hasOnClickListeners() || this.mIsNonblockable) ? 8 : 0);
        View findViewById2 = findViewById(R$id.done);
        findViewById2.setOnClickListener(this.mOnDismissSettings);
        findViewById2.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
        View findViewById3 = findViewById(R$id.silence);
        View findViewById4 = findViewById(R$id.alert);
        findViewById3.setOnClickListener(this.mOnSilent);
        findViewById4.setOnClickListener(this.mOnAlert);
        View findViewById5 = findViewById(R$id.automatic);
        if (this.mShowAutomaticSetting) {
            this.mAutomaticDescriptionView.setText(Html.fromHtml(((LinearLayout) this).mContext.getText(this.mAssistantFeedbackController.getInlineDescriptionResource(this.mEntry)).toString()));
            findViewById5.setVisibility(0);
            findViewById5.setOnClickListener(this.mOnAutomatic);
        } else {
            findViewById5.setVisibility(8);
        }
        applyAlertingBehavior(getAlertingBehavior(), false);
    }

    private void bindHeader() {
        this.mPkgIcon = null;
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPackageName, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf(this.mPm.getApplicationLabel(applicationInfo));
                this.mPkgIcon = this.mPm.getApplicationIcon(applicationInfo);
            }
        } catch (PackageManager.NameNotFoundException unused) {
            this.mPkgIcon = this.mPm.getDefaultActivityIcon();
        }
        ((ImageView) findViewById(R$id.pkg_icon)).setImageDrawable(this.mPkgIcon);
        ((TextView) findViewById(R$id.pkg_name)).setText(this.mAppName);
        bindDelegate();
        View findViewById = findViewById(R$id.app_settings);
        Intent appSettingsIntent = getAppSettingsIntent(this.mPm, this.mPackageName, this.mSingleNotificationChannel, this.mSbn.getId(), this.mSbn.getTag());
        int i = 0;
        if (appSettingsIntent == null || TextUtils.isEmpty(this.mSbn.getNotification().getSettingsText())) {
            findViewById.setVisibility(8);
        } else {
            findViewById.setVisibility(0);
            findViewById.setOnClickListener(new View.OnClickListener(appSettingsIntent) { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda6
                public final /* synthetic */ Intent f$1;

                {
                    this.f$1 = r2;
                }

                @Override // android.view.View.OnClickListener
                public final void onClick(View view) {
                    NotificationInfo.this.lambda$bindHeader$4(this.f$1, view);
                }
            });
        }
        View findViewById2 = findViewById(R$id.info);
        findViewById2.setOnClickListener(getSettingsOnClickListener());
        if (!findViewById2.hasOnClickListeners()) {
            i = 8;
        }
        findViewById2.setVisibility(i);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$bindHeader$4(Intent intent, View view) {
        this.mAppSettingsClickListener.onClick(view, intent);
    }

    private View.OnClickListener getSettingsOnClickListener() {
        int i = this.mAppUid;
        if (i < 0 || this.mOnSettingsClickListener == null || !this.mIsDeviceProvisioned) {
            return null;
        }
        return new View.OnClickListener(i) { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda5
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NotificationInfo.this.lambda$getSettingsOnClickListener$5(this.f$1, view);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSettingsOnClickListener$5(int i, View view) {
        this.mOnSettingsClickListener.onClick(view, this.mNumUniqueChannelsInRow > 1 ? null : this.mSingleNotificationChannel, i);
    }

    private View.OnClickListener getTurnOffNotificationsClickListener() {
        return new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NotificationInfo.this.lambda$getTurnOffNotificationsClickListener$7(view);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getTurnOffNotificationsClickListener$7(View view) {
        ChannelEditorDialogController channelEditorDialogController;
        if (!this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = true;
            channelEditorDialogController.prepareDialogForApp(this.mAppName, this.mPackageName, this.mAppUid, this.mUniqueChannelsInRow, this.mPkgIcon, this.mOnSettingsClickListener);
            this.mChannelEditorDialogController.setOnFinishListener(new OnChannelEditorDialogFinishedListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda7
                @Override // com.android.systemui.statusbar.notification.row.OnChannelEditorDialogFinishedListener
                public final void onChannelEditorDialogFinished() {
                    NotificationInfo.this.lambda$getTurnOffNotificationsClickListener$6();
                }
            });
            this.mChannelEditorDialogController.show();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getTurnOffNotificationsClickListener$6() {
        this.mPresentingChannelEditorDialog = false;
        this.mGutsContainer.closeControls(this, false);
    }

    private void bindChannelDetails() throws RemoteException {
        bindName();
        bindGroup();
    }

    private void bindName() {
        TextView textView = (TextView) findViewById(R$id.channel_name);
        if (this.mIsSingleDefaultChannel || this.mNumUniqueChannelsInRow > 1) {
            textView.setVisibility(8);
        } else {
            textView.setText(this.mSingleNotificationChannel.getName());
        }
    }

    private void bindDelegate() {
        TextView textView = (TextView) findViewById(R$id.delegate_name);
        if (!TextUtils.equals(this.mPackageName, this.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
    }

    private void bindGroup() throws RemoteException {
        NotificationChannelGroup notificationChannelGroupForPackage;
        NotificationChannel notificationChannel = this.mSingleNotificationChannel;
        CharSequence name = (notificationChannel == null || notificationChannel.getGroup() == null || (notificationChannelGroupForPackage = this.mINotificationManager.getNotificationChannelGroupForPackage(this.mSingleNotificationChannel.getGroup(), this.mPackageName, this.mAppUid)) == null) ? null : notificationChannelGroupForPackage.getName();
        TextView textView = (TextView) findViewById(R$id.group_name);
        if (name != null) {
            textView.setText(name);
            textView.setVisibility(0);
            return;
        }
        textView.setVisibility(8);
    }

    private void saveImportance() {
        if (!this.mIsNonblockable) {
            if (this.mChosenImportance == null) {
                this.mChosenImportance = Integer.valueOf(this.mStartingChannelImportance);
            }
            updateImportance();
        }
    }

    private void updateImportance() {
        if (this.mChosenImportance != null) {
            logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_SAVE_IMPORTANCE);
            this.mMetricsLogger.write(importanceChangeLogMaker());
            int intValue = this.mChosenImportance.intValue();
            if (this.mStartingChannelImportance != -1000 && ((this.mWasShownHighPriority && this.mChosenImportance.intValue() >= 3) || (!this.mWasShownHighPriority && this.mChosenImportance.intValue() < 3))) {
                intValue = this.mStartingChannelImportance;
            }
            new Handler((Looper) Dependency.get(Dependency.BG_LOOPER)).post(new UpdateImportanceRunnable(this.mINotificationManager, this.mPackageName, this.mAppUid, this.mNumUniqueChannelsInRow == 1 ? this.mSingleNotificationChannel : null, this.mStartingChannelImportance, intValue, this.mIsAutomaticChosen));
            this.mOnUserInteractionCallback.onImportanceChanged(this.mEntry);
        }
    }

    @Override // android.view.View
    public boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    private void applyAlertingBehavior(int i, boolean z) {
        int i2;
        boolean z2 = true;
        if (z) {
            TransitionSet transitionSet = new TransitionSet();
            transitionSet.setOrdering(0);
            TransitionSet addTransition = transitionSet.addTransition(new Fade(2)).addTransition(new ChangeBounds());
            Transition duration = new Fade(1).setStartDelay(150).setDuration(200);
            Interpolator interpolator = Interpolators.FAST_OUT_SLOW_IN;
            addTransition.addTransition(duration.setInterpolator(interpolator));
            transitionSet.setDuration(350L);
            transitionSet.setInterpolator((TimeInterpolator) interpolator);
            TransitionManager.beginDelayedTransition(this, transitionSet);
        }
        View findViewById = findViewById(R$id.alert);
        View findViewById2 = findViewById(R$id.silence);
        View findViewById3 = findViewById(R$id.automatic);
        if (i == 0) {
            this.mPriorityDescriptionView.setVisibility(0);
            this.mSilentDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new Runnable(findViewById, findViewById2, findViewById3) { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda8
                public final /* synthetic */ View f$0;
                public final /* synthetic */ View f$1;
                public final /* synthetic */ View f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationInfo.lambda$applyAlertingBehavior$8(this.f$0, this.f$1, this.f$2);
                }
            });
        } else if (i == 1) {
            this.mSilentDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mAutomaticDescriptionView.setVisibility(8);
            post(new Runnable(findViewById, findViewById2, findViewById3) { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda9
                public final /* synthetic */ View f$0;
                public final /* synthetic */ View f$1;
                public final /* synthetic */ View f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationInfo.lambda$applyAlertingBehavior$9(this.f$0, this.f$1, this.f$2);
                }
            });
        } else if (i == 2) {
            this.mAutomaticDescriptionView.setVisibility(0);
            this.mPriorityDescriptionView.setVisibility(8);
            this.mSilentDescriptionView.setVisibility(8);
            post(new Runnable(findViewById3, findViewById, findViewById2) { // from class: com.android.systemui.statusbar.notification.row.NotificationInfo$$ExternalSyntheticLambda10
                public final /* synthetic */ View f$0;
                public final /* synthetic */ View f$1;
                public final /* synthetic */ View f$2;

                {
                    this.f$0 = r1;
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    NotificationInfo.lambda$applyAlertingBehavior$10(this.f$0, this.f$1, this.f$2);
                }
            });
        } else {
            throw new IllegalArgumentException("Unrecognized alerting behavior: " + i);
        }
        if (getAlertingBehavior() == i) {
            z2 = false;
        }
        TextView textView = (TextView) findViewById(R$id.done);
        if (z2) {
            i2 = R$string.inline_ok_button;
        } else {
            i2 = R$string.inline_done_button;
        }
        textView.setText(i2);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyAlertingBehavior$8(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyAlertingBehavior$9(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(true);
        view3.setSelected(false);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$applyAlertingBehavior$10(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public void onFinishedClosing() {
        Integer num = this.mChosenImportance;
        if (num != null) {
            this.mStartingChannelImportance = num.intValue();
        }
        bindInlineControls();
        logUiEvent(NotificationControlsEvent.NOTIFICATION_CONTROLS_CLOSE);
        this.mMetricsLogger.write(notificationControlsLogMaker().setType(2));
    }

    @Override // android.view.View
    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        if (this.mGutsContainer != null && accessibilityEvent.getEventType() == 32) {
            if (this.mGutsContainer.isExposed()) {
                accessibilityEvent.getText().add(((LinearLayout) this).mContext.getString(R$string.notification_channel_controls_opened_accessibility, this.mAppName));
            } else {
                accessibilityEvent.getText().add(((LinearLayout) this).mContext.getString(R$string.notification_channel_controls_closed_accessibility, this.mAppName));
            }
        }
    }

    private Intent getAppSettingsIntent(PackageManager packageManager, String str, NotificationChannel notificationChannel, int i, String str2) {
        Intent intent = new Intent("android.intent.action.MAIN").addCategory("android.intent.category.NOTIFICATION_PREFERENCES").setPackage(str);
        List<ResolveInfo> queryIntentActivities = packageManager.queryIntentActivities(intent, 65536);
        if (queryIntentActivities == null || queryIntentActivities.size() == 0 || queryIntentActivities.get(0) == null) {
            return null;
        }
        ActivityInfo activityInfo = queryIntentActivities.get(0).activityInfo;
        intent.setClassName(activityInfo.packageName, activityInfo.name);
        if (notificationChannel != null) {
            intent.putExtra("android.intent.extra.CHANNEL_ID", notificationChannel.getId());
        }
        intent.putExtra("android.intent.extra.NOTIFICATION_ID", i);
        intent.putExtra("android.intent.extra.NOTIFICATION_TAG", str2);
        return intent;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public void setGutsParent(NotificationGuts notificationGuts) {
        this.mGutsContainer = notificationGuts;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public boolean shouldBeSaved() {
        return this.mPressedApply;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public boolean handleCloseControls(boolean z, boolean z2) {
        ChannelEditorDialogController channelEditorDialogController;
        if (this.mPresentingChannelEditorDialog && (channelEditorDialogController = this.mChannelEditorDialogController) != null) {
            this.mPresentingChannelEditorDialog = false;
            channelEditorDialogController.setOnFinishListener(null);
            this.mChannelEditorDialogController.close();
        }
        if (z) {
            saveImportance();
        }
        return false;
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public int getActualHeight() {
        return this.mActualHeight;
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    protected void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        this.mActualHeight = getHeight();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UpdateImportanceRunnable implements Runnable {
        private final int mAppUid;
        private final NotificationChannel mChannelToUpdate;
        private final int mCurrentImportance;
        private final INotificationManager mINotificationManager;
        private final int mNewImportance;
        private final String mPackageName;
        private final boolean mUnlockImportance;

        public UpdateImportanceRunnable(INotificationManager iNotificationManager, String str, int i, NotificationChannel notificationChannel, int i2, int i3, boolean z) {
            this.mINotificationManager = iNotificationManager;
            this.mPackageName = str;
            this.mAppUid = i;
            this.mChannelToUpdate = notificationChannel;
            this.mCurrentImportance = i2;
            this.mNewImportance = i3;
            this.mUnlockImportance = z;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                NotificationChannel notificationChannel = this.mChannelToUpdate;
                if (notificationChannel == null) {
                    this.mINotificationManager.setNotificationsEnabledWithImportanceLockForPackage(this.mPackageName, this.mAppUid, this.mNewImportance >= this.mCurrentImportance);
                } else if (this.mUnlockImportance) {
                    this.mINotificationManager.unlockNotificationChannel(this.mPackageName, this.mAppUid, notificationChannel.getId());
                } else {
                    notificationChannel.setImportance(this.mNewImportance);
                    this.mChannelToUpdate.lockFields(4);
                    this.mINotificationManager.updateNotificationChannelForPackage(this.mPackageName, this.mAppUid, this.mChannelToUpdate);
                }
            } catch (RemoteException e) {
                while (true) {
                    Log.e("InfoGuts", "Unable to update notification importance", e);
                    return;
                }
            }
        }
    }

    private void logUiEvent(NotificationControlsEvent notificationControlsEvent) {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification != null) {
            this.mUiEventLogger.logWithInstanceId(notificationControlsEvent, statusBarNotification.getUid(), this.mSbn.getPackageName(), this.mSbn.getInstanceId());
        }
    }

    private LogMaker getLogMaker() {
        StatusBarNotification statusBarNotification = this.mSbn;
        if (statusBarNotification == null) {
            return new LogMaker(1621);
        }
        return statusBarNotification.getLogMaker().setCategory(1621);
    }

    private LogMaker importanceChangeLogMaker() {
        Integer num = this.mChosenImportance;
        return getLogMaker().setCategory(291).setType(4).setSubtype(Integer.valueOf(num != null ? num.intValue() : this.mStartingChannelImportance).intValue() - this.mStartingChannelImportance);
    }

    private LogMaker notificationControlsLogMaker() {
        return getLogMaker().setCategory(204).setType(1).setSubtype(0);
    }

    private int getAlertingBehavior() {
        if (!this.mShowAutomaticSetting || this.mSingleNotificationChannel.hasUserSetImportance()) {
            return !this.mWasShownHighPriority ? 1 : 0;
        }
        return 2;
    }
}
