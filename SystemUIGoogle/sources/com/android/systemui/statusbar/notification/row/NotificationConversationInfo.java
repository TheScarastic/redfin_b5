package com.android.systemui.statusbar.notification.row;

import android.animation.TimeInterpolator;
import android.app.INotificationManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ShortcutInfo;
import android.content.pm.ShortcutManager;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
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
import com.android.settingslib.notification.ConversationIconFactory;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
import com.android.systemui.statusbar.notification.NotificationChannelHelper;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.row.NotificationGuts;
import com.android.systemui.statusbar.phone.ShadeController;
import com.android.systemui.wmshell.BubblesManager;
import java.util.Optional;
/* loaded from: classes.dex */
public class NotificationConversationInfo extends LinearLayout implements NotificationGuts.GutsContent {
    private int mActualHeight;
    private int mAppBubble;
    private String mAppName;
    private int mAppUid;
    private Handler mBgHandler;
    private Notification.BubbleMetadata mBubbleMetadata;
    private Optional<BubblesManager> mBubblesManagerOptional;
    private TextView mDefaultDescriptionView;
    private String mDelegatePkg;
    private NotificationEntry mEntry;
    private NotificationGuts mGutsContainer;
    private INotificationManager mINotificationManager;
    private ConversationIconFactory mIconFactory;
    private boolean mIsDeviceProvisioned;
    private Handler mMainHandler;
    private NotificationChannel mNotificationChannel;
    private OnConversationSettingsClickListener mOnConversationSettingsClickListener;
    private OnSettingsClickListener mOnSettingsClickListener;
    private OnUserInteractionCallback mOnUserInteractionCallback;
    private String mPackageName;
    private PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;
    private PackageManager mPm;
    private boolean mPressedApply;
    private TextView mPriorityDescriptionView;
    private StatusBarNotification mSbn;
    private ShadeController mShadeController;
    private ShortcutInfo mShortcutInfo;
    private ShortcutManager mShortcutManager;
    private TextView mSilentDescriptionView;
    private Context mUserContext;
    private int mSelectedAction = -1;
    @VisibleForTesting
    boolean mSkipPost = false;
    private View.OnClickListener mOnFavoriteClick = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda1
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationConversationInfo.this.lambda$new$0(view);
        }
    };
    private View.OnClickListener mOnDefaultClick = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda0
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationConversationInfo.this.lambda$new$1(view);
        }
    };
    private View.OnClickListener mOnMuteClick = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda2
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationConversationInfo.this.lambda$new$2(view);
        }
    };
    private View.OnClickListener mOnDone = new View.OnClickListener() { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda3
        @Override // android.view.View.OnClickListener
        public final void onClick(View view) {
            NotificationConversationInfo.this.lambda$new$3(view);
        }
    };

    /* loaded from: classes.dex */
    public interface OnConversationSettingsClickListener {
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
        setSelectedAction(2);
        updateToggleActions(this.mSelectedAction, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(View view) {
        setSelectedAction(0);
        updateToggleActions(this.mSelectedAction, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$2(View view) {
        setSelectedAction(4);
        updateToggleActions(this.mSelectedAction, true);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$3(View view) {
        this.mPressedApply = true;
        if (this.mSelectedAction == 2 && getPriority() != this.mSelectedAction) {
            this.mShadeController.animateCollapsePanels();
            this.mPeopleSpaceWidgetManager.requestPinAppWidget(this.mShortcutInfo, new Bundle());
        }
        this.mGutsContainer.closeControls(view, true);
    }

    public NotificationConversationInfo(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    @VisibleForTesting
    public void setSelectedAction(int i) {
        if (this.mSelectedAction != i) {
            this.mSelectedAction = i;
        }
    }

    public void bindNotification(int i, ShortcutManager shortcutManager, PackageManager packageManager, PeopleSpaceWidgetManager peopleSpaceWidgetManager, INotificationManager iNotificationManager, OnUserInteractionCallback onUserInteractionCallback, String str, NotificationChannel notificationChannel, NotificationEntry notificationEntry, Notification.BubbleMetadata bubbleMetadata, OnSettingsClickListener onSettingsClickListener, ConversationIconFactory conversationIconFactory, Context context, boolean z, Handler handler, Handler handler2, OnConversationSettingsClickListener onConversationSettingsClickListener, Optional<BubblesManager> optional, ShadeController shadeController) {
        this.mPressedApply = false;
        this.mSelectedAction = i;
        this.mINotificationManager = iNotificationManager;
        this.mPeopleSpaceWidgetManager = peopleSpaceWidgetManager;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mPackageName = str;
        this.mEntry = notificationEntry;
        StatusBarNotification sbn = notificationEntry.getSbn();
        this.mSbn = sbn;
        this.mPm = packageManager;
        this.mAppName = this.mPackageName;
        this.mOnSettingsClickListener = onSettingsClickListener;
        this.mNotificationChannel = notificationChannel;
        this.mAppUid = sbn.getUid();
        this.mDelegatePkg = this.mSbn.getOpPkg();
        this.mIsDeviceProvisioned = z;
        this.mOnConversationSettingsClickListener = onConversationSettingsClickListener;
        this.mIconFactory = conversationIconFactory;
        this.mUserContext = context;
        this.mBubbleMetadata = bubbleMetadata;
        this.mBubblesManagerOptional = optional;
        this.mShadeController = shadeController;
        this.mMainHandler = handler;
        this.mBgHandler = handler2;
        this.mShortcutManager = shortcutManager;
        ShortcutInfo conversationShortcutInfo = notificationEntry.getRanking().getConversationShortcutInfo();
        this.mShortcutInfo = conversationShortcutInfo;
        if (conversationShortcutInfo != null) {
            this.mNotificationChannel = NotificationChannelHelper.createConversationChannelIfNeeded(getContext(), this.mINotificationManager, notificationEntry, this.mNotificationChannel);
            try {
                this.mAppBubble = this.mINotificationManager.getBubblePreferenceForPackage(this.mPackageName, this.mAppUid);
            } catch (RemoteException e) {
                Log.e("ConversationGuts", "can't reach OS", e);
                this.mAppBubble = 2;
            }
            bindHeader();
            bindActions();
            View findViewById = findViewById(R$id.done);
            findViewById.setOnClickListener(this.mOnDone);
            findViewById.setAccessibilityDelegate(this.mGutsContainer.getAccessibilityDelegate());
            return;
        }
        throw new IllegalArgumentException("Does not have required information");
    }

    private void bindActions() {
        if (this.mAppBubble == 1) {
            ((TextView) findViewById(R$id.default_summary)).setText(getResources().getString(R$string.notification_channel_summary_default_with_bubbles, this.mAppName));
        }
        findViewById(R$id.priority).setOnClickListener(this.mOnFavoriteClick);
        findViewById(R$id.default_behavior).setOnClickListener(this.mOnDefaultClick);
        findViewById(R$id.silence).setOnClickListener(this.mOnMuteClick);
        View findViewById = findViewById(R$id.info);
        findViewById.setOnClickListener(getSettingsOnClickListener());
        findViewById.setVisibility(findViewById.hasOnClickListeners() ? 0 : 8);
        int i = this.mSelectedAction;
        if (i == -1) {
            i = getPriority();
        }
        updateToggleActions(i, false);
    }

    private void bindHeader() {
        bindConversationDetails();
        bindDelegate();
    }

    private View.OnClickListener getSettingsOnClickListener() {
        int i = this.mAppUid;
        if (i < 0 || this.mOnSettingsClickListener == null || !this.mIsDeviceProvisioned) {
            return null;
        }
        return new View.OnClickListener(i) { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda4
            public final /* synthetic */ int f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                NotificationConversationInfo.this.lambda$getSettingsOnClickListener$4(this.f$1, view);
            }
        };
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$getSettingsOnClickListener$4(int i, View view) {
        this.mOnSettingsClickListener.onClick(view, this.mNotificationChannel, i);
    }

    private void bindConversationDetails() {
        ((TextView) findViewById(R$id.parent_channel_name)).setText(this.mNotificationChannel.getName());
        bindGroup();
        bindPackage();
        bindIcon(this.mNotificationChannel.isImportantConversation());
        this.mPriorityDescriptionView = (TextView) findViewById(R$id.priority_summary);
        if (willShowAsBubble() && willBypassDnd()) {
            this.mPriorityDescriptionView.setText(R$string.notification_channel_summary_priority_all);
        } else if (willShowAsBubble()) {
            this.mPriorityDescriptionView.setText(R$string.notification_channel_summary_priority_bubble);
        } else if (willBypassDnd()) {
            this.mPriorityDescriptionView.setText(R$string.notification_channel_summary_priority_dnd);
        } else {
            this.mPriorityDescriptionView.setText(R$string.notification_channel_summary_priority_baseline);
        }
    }

    private void bindIcon(boolean z) {
        Drawable baseIconDrawable = this.mIconFactory.getBaseIconDrawable(this.mShortcutInfo);
        int i = 0;
        if (baseIconDrawable == null) {
            baseIconDrawable = ((LinearLayout) this).mContext.getDrawable(R$drawable.ic_person).mutate();
            TypedArray obtainStyledAttributes = ((LinearLayout) this).mContext.obtainStyledAttributes(new int[]{16843829});
            int color = obtainStyledAttributes.getColor(0, 0);
            obtainStyledAttributes.recycle();
            baseIconDrawable.setTint(color);
        }
        ((ImageView) findViewById(R$id.conversation_icon)).setImageDrawable(baseIconDrawable);
        ((ImageView) findViewById(R$id.conversation_icon_badge_icon)).setImageDrawable(this.mIconFactory.getAppBadge(this.mPackageName, UserHandle.getUserId(this.mSbn.getUid())));
        View findViewById = findViewById(R$id.conversation_icon_badge_ring);
        if (!z) {
            i = 8;
        }
        findViewById.setVisibility(i);
    }

    private void bindPackage() {
        try {
            ApplicationInfo applicationInfo = this.mPm.getApplicationInfo(this.mPackageName, 795136);
            if (applicationInfo != null) {
                this.mAppName = String.valueOf(this.mPm.getApplicationLabel(applicationInfo));
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        ((TextView) findViewById(R$id.pkg_name)).setText(this.mAppName);
    }

    private void bindDelegate() {
        TextView textView = (TextView) findViewById(R$id.delegate_name);
        if (!TextUtils.equals(this.mPackageName, this.mDelegatePkg)) {
            textView.setVisibility(0);
        } else {
            textView.setVisibility(8);
        }
    }

    private void bindGroup() {
        NotificationChannel notificationChannel = this.mNotificationChannel;
        CharSequence charSequence = null;
        if (!(notificationChannel == null || notificationChannel.getGroup() == null)) {
            try {
                NotificationChannelGroup notificationChannelGroupForPackage = this.mINotificationManager.getNotificationChannelGroupForPackage(this.mNotificationChannel.getGroup(), this.mPackageName, this.mAppUid);
                if (notificationChannelGroupForPackage != null) {
                    charSequence = notificationChannelGroupForPackage.getName();
                }
            } catch (RemoteException unused) {
            }
        }
        TextView textView = (TextView) findViewById(R$id.group_name);
        if (charSequence != null) {
            textView.setText(charSequence);
            textView.setVisibility(0);
            return;
        }
        textView.setVisibility(8);
    }

    @Override // android.view.View
    public boolean post(Runnable runnable) {
        if (!this.mSkipPost) {
            return super.post(runnable);
        }
        runnable.run();
        return true;
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mDefaultDescriptionView = (TextView) findViewById(R$id.default_summary);
        this.mSilentDescriptionView = (TextView) findViewById(R$id.silence_summary);
    }

    @Override // com.android.systemui.statusbar.notification.row.NotificationGuts.GutsContent
    public void onFinishedClosing() {
        this.mSelectedAction = -1;
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

    private void updateToggleActions(int i, boolean z) {
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
        View findViewById = findViewById(R$id.priority);
        View findViewById2 = findViewById(R$id.default_behavior);
        View findViewById3 = findViewById(R$id.silence);
        if (i == 0) {
            this.mDefaultDescriptionView.setVisibility(0);
            this.mSilentDescriptionView.setVisibility(8);
            this.mPriorityDescriptionView.setVisibility(8);
            post(new Runnable(findViewById, findViewById2, findViewById3) { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda7
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
                    NotificationConversationInfo.lambda$updateToggleActions$7(this.f$0, this.f$1, this.f$2);
                }
            });
        } else if (i == 2) {
            this.mPriorityDescriptionView.setVisibility(0);
            this.mDefaultDescriptionView.setVisibility(8);
            this.mSilentDescriptionView.setVisibility(8);
            post(new Runnable(findViewById, findViewById2, findViewById3) { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda5
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
                    NotificationConversationInfo.lambda$updateToggleActions$5(this.f$0, this.f$1, this.f$2);
                }
            });
        } else if (i == 4) {
            this.mSilentDescriptionView.setVisibility(0);
            this.mDefaultDescriptionView.setVisibility(8);
            this.mPriorityDescriptionView.setVisibility(8);
            post(new Runnable(findViewById, findViewById2, findViewById3) { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda6
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
                    NotificationConversationInfo.lambda$updateToggleActions$6(this.f$0, this.f$1, this.f$2);
                }
            });
        } else {
            throw new IllegalArgumentException("Unrecognized behavior: " + this.mSelectedAction);
        }
        boolean z3 = getPriority() != i;
        TextView textView = (TextView) findViewById(R$id.done);
        if (z3) {
            i2 = R$string.inline_ok_button;
        } else {
            i2 = R$string.inline_done_button;
        }
        textView.setText(i2);
        if (i != 2) {
            z2 = false;
        }
        bindIcon(z2);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateToggleActions$5(View view, View view2, View view3) {
        view.setSelected(true);
        view2.setSelected(false);
        view3.setSelected(false);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateToggleActions$6(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(false);
        view3.setSelected(true);
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$updateToggleActions$7(View view, View view2, View view3) {
        view.setSelected(false);
        view2.setSelected(true);
        view3.setSelected(false);
    }

    /* access modifiers changed from: package-private */
    public int getSelectedAction() {
        return this.mSelectedAction;
    }

    private int getPriority() {
        if (this.mNotificationChannel.getImportance() <= 2 && this.mNotificationChannel.getImportance() > -1000) {
            return 4;
        }
        if (this.mNotificationChannel.isImportantConversation()) {
            return 2;
        }
        return 0;
    }

    private void updateChannel() {
        this.mBgHandler.post(new UpdateChannelRunnable(this.mINotificationManager, this.mPackageName, this.mAppUid, this.mSelectedAction, this.mNotificationChannel));
        this.mEntry.markForUserTriggeredMovement(true);
        this.mMainHandler.postDelayed(new Runnable() { // from class: com.android.systemui.statusbar.notification.row.NotificationConversationInfo$$ExternalSyntheticLambda8
            @Override // java.lang.Runnable
            public final void run() {
                NotificationConversationInfo.this.lambda$updateChannel$8();
            }
        }, 360);
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updateChannel$8() {
        this.mOnUserInteractionCallback.onImportanceChanged(this.mEntry);
    }

    private boolean willBypassDnd() {
        try {
            int i = this.mINotificationManager.getConsolidatedNotificationPolicy().priorityConversationSenders;
            if (i == 2 || i == 1) {
                return true;
            }
            return false;
        } catch (RemoteException e) {
            Log.e("ConversationGuts", "Could not check conversation senders", e);
            return false;
        }
    }

    private boolean willShowAsBubble() {
        return this.mBubbleMetadata != null && BubblesManager.areBubblesEnabled(((LinearLayout) this).mContext, this.mSbn.getUser());
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
        if (!z || this.mSelectedAction <= -1) {
            return false;
        }
        updateChannel();
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

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public class UpdateChannelRunnable implements Runnable {
        private final int mAction;
        private final String mAppPkg;
        private final int mAppUid;
        private NotificationChannel mChannelToUpdate;
        private final INotificationManager mINotificationManager;

        public UpdateChannelRunnable(INotificationManager iNotificationManager, String str, int i, int i2, NotificationChannel notificationChannel) {
            this.mINotificationManager = iNotificationManager;
            this.mAppPkg = str;
            this.mAppUid = i;
            this.mChannelToUpdate = notificationChannel;
            this.mAction = i2;
        }

        @Override // java.lang.Runnable
        public void run() {
            try {
                int i = this.mAction;
                if (i == 0) {
                    NotificationChannel notificationChannel = this.mChannelToUpdate;
                    notificationChannel.setImportance(Math.max(notificationChannel.getOriginalImportance(), 3));
                    if (this.mChannelToUpdate.isImportantConversation()) {
                        this.mChannelToUpdate.setImportantConversation(false);
                        this.mChannelToUpdate.setAllowBubbles(false);
                    }
                } else if (i == 2) {
                    this.mChannelToUpdate.setImportantConversation(true);
                    if (this.mChannelToUpdate.isImportantConversation()) {
                        this.mChannelToUpdate.setAllowBubbles(true);
                        if (NotificationConversationInfo.this.mAppBubble == 0) {
                            this.mINotificationManager.setBubblesAllowed(this.mAppPkg, this.mAppUid, 2);
                        }
                        if (NotificationConversationInfo.this.mBubblesManagerOptional.isPresent()) {
                            NotificationConversationInfo.this.post(new NotificationConversationInfo$UpdateChannelRunnable$$ExternalSyntheticLambda0(this));
                        }
                    }
                    NotificationChannel notificationChannel2 = this.mChannelToUpdate;
                    notificationChannel2.setImportance(Math.max(notificationChannel2.getOriginalImportance(), 3));
                } else if (i == 4) {
                    if (this.mChannelToUpdate.getImportance() == -1000 || this.mChannelToUpdate.getImportance() >= 3) {
                        this.mChannelToUpdate.setImportance(2);
                    }
                    if (this.mChannelToUpdate.isImportantConversation()) {
                        this.mChannelToUpdate.setImportantConversation(false);
                        this.mChannelToUpdate.setAllowBubbles(false);
                    }
                }
                this.mINotificationManager.updateNotificationChannelForPackage(this.mAppPkg, this.mAppUid, this.mChannelToUpdate);
            } catch (RemoteException e) {
                Log.e("ConversationGuts", "Unable to update notification channel", e);
            }
        }

        /* access modifiers changed from: private */
        public /* synthetic */ void lambda$run$0() {
            ((BubblesManager) NotificationConversationInfo.this.mBubblesManagerOptional.get()).onUserSetImportantConversation(NotificationConversationInfo.this.mEntry);
        }
    }
}
