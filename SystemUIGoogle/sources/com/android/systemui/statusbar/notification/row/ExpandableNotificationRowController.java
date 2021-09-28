package com.android.systemui.statusbar.notification.row;

import android.view.View;
import com.android.systemui.classifier.FalsingCollector;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.plugins.statusbar.NotificationMenuRowPlugin;
import com.android.systemui.plugins.statusbar.StatusBarStateController;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.statusbar.NotificationMediaManager;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.statusbar.notification.collection.render.GroupExpansionManager;
import com.android.systemui.statusbar.notification.collection.render.GroupMembershipManager;
import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.logging.NotificationLogger;
import com.android.systemui.statusbar.notification.people.PeopleNotificationIdentifier;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.stack.NotificationListContainer;
import com.android.systemui.statusbar.phone.KeyguardBypassController;
import com.android.systemui.statusbar.policy.HeadsUpManager;
import com.android.systemui.util.time.SystemClock;
import com.android.systemui.wmshell.BubblesManager;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
/* loaded from: classes.dex */
public class ExpandableNotificationRowController implements NodeController {
    private final ActivatableNotificationViewController mActivatableNotificationViewController;
    private final boolean mAllowLongPress;
    private final String mAppName;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private final SystemClock mClock;
    private final FalsingCollector mFalsingCollector;
    private final FalsingManager mFalsingManager;
    private final GroupExpansionManager mGroupExpansionManager;
    private final GroupMembershipManager mGroupMembershipManager;
    private final HeadsUpManager mHeadsUpManager;
    private final KeyguardBypassController mKeyguardBypassController;
    private final NotificationListContainer mListContainer;
    private final NotificationMediaManager mMediaManager;
    private final NotificationGutsManager mNotificationGutsManager;
    private final String mNotificationKey;
    private final NotificationLogger mNotificationLogger;
    private final ExpandableNotificationRow.OnExpandClickListener mOnExpandClickListener;
    private final ExpandableNotificationRow.CoordinateOnClickListener mOnFeedbackClickListener;
    private final OnUserInteractionCallback mOnUserInteractionCallback;
    private final PeopleNotificationIdentifier mPeopleNotificationIdentifier;
    private final PluginManager mPluginManager;
    private final RowContentBindStage mRowContentBindStage;
    private final StatusBarStateController mStatusBarStateController;
    private final ExpandableNotificationRow mView;
    private final ExpandableNotificationRow.ExpansionLogger mExpansionLogger = new ExpandableNotificationRow.ExpansionLogger() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController$$ExternalSyntheticLambda1
        @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.ExpansionLogger
        public final void logNotificationExpansion(String str, boolean z, boolean z2) {
            ExpandableNotificationRowController.this.logNotificationExpansion(str, z, z2);
        }
    };
    private final StatusBarStateController.StateListener mStatusBarStateListener = new StatusBarStateController.StateListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController.2
        @Override // com.android.systemui.plugins.statusbar.StatusBarStateController.StateListener
        public void onStateChanged(int i) {
            ExpandableNotificationRow expandableNotificationRow = ExpandableNotificationRowController.this.mView;
            boolean z = true;
            if (i != 1) {
                z = false;
            }
            expandableNotificationRow.setOnKeyguard(z);
        }
    };

    public ExpandableNotificationRowController(ExpandableNotificationRow expandableNotificationRow, NotificationListContainer notificationListContainer, ActivatableNotificationViewController activatableNotificationViewController, NotificationMediaManager notificationMediaManager, PluginManager pluginManager, SystemClock systemClock, String str, String str2, KeyguardBypassController keyguardBypassController, GroupMembershipManager groupMembershipManager, GroupExpansionManager groupExpansionManager, RowContentBindStage rowContentBindStage, NotificationLogger notificationLogger, HeadsUpManager headsUpManager, ExpandableNotificationRow.OnExpandClickListener onExpandClickListener, StatusBarStateController statusBarStateController, NotificationGutsManager notificationGutsManager, boolean z, OnUserInteractionCallback onUserInteractionCallback, FalsingManager falsingManager, FalsingCollector falsingCollector, PeopleNotificationIdentifier peopleNotificationIdentifier, Optional<BubblesManager> optional) {
        this.mView = expandableNotificationRow;
        this.mListContainer = notificationListContainer;
        this.mActivatableNotificationViewController = activatableNotificationViewController;
        this.mMediaManager = notificationMediaManager;
        this.mPluginManager = pluginManager;
        this.mClock = systemClock;
        this.mAppName = str;
        this.mNotificationKey = str2;
        this.mKeyguardBypassController = keyguardBypassController;
        this.mGroupMembershipManager = groupMembershipManager;
        this.mGroupExpansionManager = groupExpansionManager;
        this.mRowContentBindStage = rowContentBindStage;
        this.mNotificationLogger = notificationLogger;
        this.mHeadsUpManager = headsUpManager;
        this.mOnExpandClickListener = onExpandClickListener;
        this.mStatusBarStateController = statusBarStateController;
        this.mNotificationGutsManager = notificationGutsManager;
        this.mOnUserInteractionCallback = onUserInteractionCallback;
        this.mFalsingManager = falsingManager;
        Objects.requireNonNull(notificationGutsManager);
        this.mOnFeedbackClickListener = new ExpandableNotificationRow.CoordinateOnClickListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.CoordinateOnClickListener
            public final boolean onClick(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
                return NotificationGutsManager.this.openGuts(view, i, i2, menuItem);
            }
        };
        this.mAllowLongPress = z;
        this.mFalsingCollector = falsingCollector;
        this.mPeopleNotificationIdentifier = peopleNotificationIdentifier;
        this.mBubblesManagerOptional = optional;
    }

    public void init(NotificationEntry notificationEntry) {
        this.mActivatableNotificationViewController.init();
        this.mView.initialize(notificationEntry, this.mAppName, this.mNotificationKey, this.mExpansionLogger, this.mKeyguardBypassController, this.mGroupMembershipManager, this.mGroupExpansionManager, this.mHeadsUpManager, this.mRowContentBindStage, this.mOnExpandClickListener, this.mMediaManager, this.mOnFeedbackClickListener, this.mFalsingManager, this.mFalsingCollector, this.mStatusBarStateController, this.mPeopleNotificationIdentifier, this.mOnUserInteractionCallback, this.mBubblesManagerOptional, this.mNotificationGutsManager);
        this.mView.setDescendantFocusability(393216);
        if (this.mAllowLongPress) {
            this.mView.setLongPressListener(new ExpandableNotificationRow.LongPressListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController$$ExternalSyntheticLambda2
                @Override // com.android.systemui.statusbar.notification.row.ExpandableNotificationRow.LongPressListener
                public final boolean onLongPress(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
                    return ExpandableNotificationRowController.this.lambda$init$0(view, i, i2, menuItem);
                }
            });
        }
        if (NotificationRemoteInputManager.ENABLE_REMOTE_INPUT) {
            this.mView.setDescendantFocusability(131072);
        }
        this.mView.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.notification.row.ExpandableNotificationRowController.1
            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewAttachedToWindow(View view) {
                ExpandableNotificationRowController.this.mView.getEntry().setInitializationTime(ExpandableNotificationRowController.this.mClock.elapsedRealtime());
                boolean z = false;
                ExpandableNotificationRowController.this.mPluginManager.addPluginListener((PluginListener) ExpandableNotificationRowController.this.mView, NotificationMenuRowPlugin.class, false);
                ExpandableNotificationRow expandableNotificationRow = ExpandableNotificationRowController.this.mView;
                if (ExpandableNotificationRowController.this.mStatusBarStateController.getState() == 1) {
                    z = true;
                }
                expandableNotificationRow.setOnKeyguard(z);
                ExpandableNotificationRowController.this.mStatusBarStateController.addCallback(ExpandableNotificationRowController.this.mStatusBarStateListener);
            }

            @Override // android.view.View.OnAttachStateChangeListener
            public void onViewDetachedFromWindow(View view) {
                ExpandableNotificationRowController.this.mPluginManager.removePluginListener(ExpandableNotificationRowController.this.mView);
                ExpandableNotificationRowController.this.mStatusBarStateController.removeCallback(ExpandableNotificationRowController.this.mStatusBarStateListener);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$init$0(View view, int i, int i2, NotificationMenuRowPlugin.MenuItem menuItem) {
        if (!this.mView.isSummaryWithChildren()) {
            return this.mNotificationGutsManager.openGuts(view, i, i2, menuItem);
        }
        this.mView.expandNotification();
        return true;
    }

    /* access modifiers changed from: private */
    public void logNotificationExpansion(String str, boolean z, boolean z2) {
        this.mNotificationLogger.onExpansionChanged(str, z, z2);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public String getNodeLabel() {
        return this.mView.getEntry().getKey();
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public View getView() {
        return this.mView;
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public View getChildAt(int i) {
        return this.mView.getChildNotificationAt(i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void addChildAt(NodeController nodeController, int i) {
        this.mView.addChildNotification((ExpandableNotificationRow) nodeController.getView());
        this.mListContainer.notifyGroupChildAdded((ExpandableNotificationRow) nodeController.getView());
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void moveChildTo(NodeController nodeController, int i) {
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) nodeController.getView();
        this.mView.removeChildNotification(expandableNotificationRow);
        this.mView.addChildNotification(expandableNotificationRow, i);
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public void removeChild(NodeController nodeController, boolean z) {
        ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) nodeController.getView();
        this.mView.removeChildNotification(expandableNotificationRow);
        if (!z) {
            this.mListContainer.notifyGroupChildRemoved(expandableNotificationRow, this.mView);
        }
    }

    @Override // com.android.systemui.statusbar.notification.collection.render.NodeController
    public int getChildCount() {
        List<ExpandableNotificationRow> attachedChildren = this.mView.getAttachedChildren();
        if (attachedChildren != null) {
            return attachedChildren.size();
        }
        return 0;
    }
}
