package com.android.systemui.people.widget;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.LauncherApps;
import android.os.Bundle;
import android.os.ServiceManager;
import android.os.UserHandle;
import android.os.UserManager;
import android.text.TextUtils;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.app.UnlaunchableAppActivity;
import com.android.internal.logging.UiEventLogger;
import com.android.internal.logging.UiEventLoggerImpl;
import com.android.internal.statusbar.IStatusBarService;
import com.android.internal.statusbar.NotificationVisibility;
import com.android.systemui.people.PeopleSpaceUtils;
import com.android.systemui.statusbar.CommandQueue;
import com.android.systemui.statusbar.notification.NotificationEntryManager;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
import com.android.systemui.wmshell.BubblesManager;
import com.android.wm.shell.bubbles.Bubble;
import java.util.Optional;
/* loaded from: classes.dex */
public class LaunchConversationActivity extends Activity {
    private Bubble mBubble;
    private final Optional<BubblesManager> mBubblesManagerOptional;
    private CommandQueue mCommandQueue;
    private NotificationEntry mEntryToBubble;
    private IStatusBarService mIStatusBarService;
    private boolean mIsForTesting;
    private NotificationEntryManager mNotificationEntryManager;
    private UiEventLogger mUiEventLogger = new UiEventLoggerImpl();
    private final UserManager mUserManager;

    public LaunchConversationActivity(NotificationEntryManager notificationEntryManager, Optional<BubblesManager> optional, UserManager userManager, CommandQueue commandQueue) {
        this.mNotificationEntryManager = notificationEntryManager;
        this.mBubblesManagerOptional = optional;
        this.mUserManager = userManager;
        this.mCommandQueue = commandQueue;
        commandQueue.addCallback((CommandQueue.Callbacks) new CommandQueue.Callbacks() { // from class: com.android.systemui.people.widget.LaunchConversationActivity.1
            @Override // com.android.systemui.statusbar.CommandQueue.Callbacks
            public void appTransitionFinished(int i) {
                if (LaunchConversationActivity.this.mBubblesManagerOptional.isPresent()) {
                    if (LaunchConversationActivity.this.mBubble != null) {
                        ((BubblesManager) LaunchConversationActivity.this.mBubblesManagerOptional.get()).expandStackAndSelectBubble(LaunchConversationActivity.this.mBubble);
                    } else if (LaunchConversationActivity.this.mEntryToBubble != null) {
                        ((BubblesManager) LaunchConversationActivity.this.mBubblesManagerOptional.get()).expandStackAndSelectBubble(LaunchConversationActivity.this.mEntryToBubble);
                    }
                }
                LaunchConversationActivity.this.mCommandQueue.removeCallback((CommandQueue.Callbacks) this);
            }
        });
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        if (!this.mIsForTesting) {
            super.onCreate(bundle);
        }
        Intent intent = getIntent();
        String stringExtra = intent.getStringExtra("extra_tile_id");
        String stringExtra2 = intent.getStringExtra("extra_package_name");
        UserHandle userHandle = (UserHandle) intent.getParcelableExtra("extra_user_handle");
        String stringExtra3 = intent.getStringExtra("extra_notification_key");
        if (!TextUtils.isEmpty(stringExtra)) {
            this.mUiEventLogger.log(PeopleSpaceUtils.PeopleSpaceWidgetEvent.PEOPLE_SPACE_WIDGET_CLICKED);
            try {
                if (this.mUserManager.isQuietModeEnabled(userHandle)) {
                    getApplicationContext().startActivity(UnlaunchableAppActivity.createInQuietModeDialogIntent(userHandle.getIdentifier()));
                    finish();
                    return;
                }
                if (this.mBubblesManagerOptional.isPresent()) {
                    this.mBubble = this.mBubblesManagerOptional.get().getBubbleWithShortcutId(stringExtra);
                    NotificationEntry pendingOrActiveNotif = this.mNotificationEntryManager.getPendingOrActiveNotif(stringExtra3);
                    if (this.mBubble != null || (pendingOrActiveNotif != null && pendingOrActiveNotif.canBubble())) {
                        this.mEntryToBubble = pendingOrActiveNotif;
                        finish();
                        return;
                    }
                }
                if (this.mIStatusBarService == null) {
                    this.mIStatusBarService = IStatusBarService.Stub.asInterface(ServiceManager.getService("statusbar"));
                }
                clearNotificationIfPresent(stringExtra3, stringExtra2, userHandle);
                ((LauncherApps) getApplicationContext().getSystemService(LauncherApps.class)).startShortcut(stringExtra2, stringExtra, null, null, userHandle);
            } catch (Exception e) {
                Log.e("PeopleSpaceLaunchConv", "Exception launching shortcut:" + e);
            }
        }
        finish();
    }

    void clearNotificationIfPresent(String str, String str2, UserHandle userHandle) {
        NotificationEntryManager notificationEntryManager;
        NotificationEntry pendingOrActiveNotif;
        if (!TextUtils.isEmpty(str)) {
            try {
                if (!(this.mIStatusBarService == null || (notificationEntryManager = this.mNotificationEntryManager) == null || (pendingOrActiveNotif = notificationEntryManager.getPendingOrActiveNotif(str)) == null || pendingOrActiveNotif.getRanking() == null)) {
                    this.mIStatusBarService.onNotificationClear(str2, userHandle.getIdentifier(), str, 0, 2, NotificationVisibility.obtain(str, pendingOrActiveNotif.getRanking().getRank(), this.mNotificationEntryManager.getActiveNotificationsCount(), true));
                }
            } catch (Exception e) {
                Log.e("PeopleSpaceLaunchConv", "Exception cancelling notification:" + e);
            }
        }
    }

    @VisibleForTesting
    void setIsForTesting(boolean z, IStatusBarService iStatusBarService) {
        this.mIsForTesting = z;
        this.mIStatusBarService = iStatusBarService;
    }
}
