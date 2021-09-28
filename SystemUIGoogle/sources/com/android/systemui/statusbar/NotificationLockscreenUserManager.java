package com.android.systemui.statusbar;

import android.content.pm.UserInfo;
import android.util.SparseArray;
import com.android.systemui.statusbar.notification.collection.NotificationEntry;
/* loaded from: classes.dex */
public interface NotificationLockscreenUserManager {

    /* loaded from: classes.dex */
    public interface KeyguardNotificationSuppressor {
        boolean shouldSuppressOnKeyguard(NotificationEntry notificationEntry);
    }

    /* loaded from: classes.dex */
    public interface UserChangedListener {
        default void onCurrentProfilesChanged(SparseArray<UserInfo> sparseArray) {
        }

        default void onUserChanged(int i) {
        }
    }

    void addKeyguardNotificationSuppressor(KeyguardNotificationSuppressor keyguardNotificationSuppressor);

    void addUserChangedListener(UserChangedListener userChangedListener);

    int getCurrentUserId();

    boolean isAnyProfilePublicMode();

    boolean isCurrentProfile(int i);

    boolean isLockscreenPublicMode(int i);

    boolean needsRedaction(NotificationEntry notificationEntry);

    default boolean needsSeparateWorkChallenge(int i) {
        return false;
    }

    void removeUserChangedListener(UserChangedListener userChangedListener);

    void setUpWithPresenter(NotificationPresenter notificationPresenter);

    boolean shouldAllowLockscreenRemoteInput();

    boolean shouldHideNotifications(int i);

    boolean shouldHideNotifications(String str);

    boolean shouldShowLockscreenNotifications();

    boolean shouldShowOnKeyguard(NotificationEntry notificationEntry);

    void updatePublicMode();

    boolean userAllowsNotificationsInPublic(int i);

    boolean userAllowsPrivateNotificationsInPublic(int i);
}
