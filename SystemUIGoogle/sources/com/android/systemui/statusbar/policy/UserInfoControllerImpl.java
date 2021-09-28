package com.android.systemui.statusbar.policy;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.pm.UserInfo;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.RemoteException;
import android.os.UserHandle;
import android.os.UserManager;
import android.provider.ContactsContract;
import android.util.Log;
import com.android.internal.util.UserIcons;
import com.android.settingslib.drawable.UserIconDrawable;
import com.android.systemui.R$dimen;
import com.android.systemui.R$style;
import com.android.systemui.statusbar.policy.UserInfoController;
import java.util.ArrayList;
import java.util.Iterator;
/* loaded from: classes.dex */
public class UserInfoControllerImpl implements UserInfoController {
    private final ArrayList<UserInfoController.OnUserInfoChangedListener> mCallbacks = new ArrayList<>();
    private final Context mContext;
    private final BroadcastReceiver mProfileReceiver;
    private final BroadcastReceiver mReceiver;
    private String mUserAccount;
    private Drawable mUserDrawable;
    private AsyncTask<Void, Void, UserInfoQueryResult> mUserInfoTask;
    private String mUserName;

    public UserInfoControllerImpl(Context context) {
        AnonymousClass1 r0 = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.UserInfoControllerImpl.1
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                if ("android.intent.action.USER_SWITCHED".equals(intent.getAction())) {
                    UserInfoControllerImpl.this.reloadUserInfo();
                }
            }
        };
        this.mReceiver = r0;
        AnonymousClass2 r2 = new BroadcastReceiver() { // from class: com.android.systemui.statusbar.policy.UserInfoControllerImpl.2
            @Override // android.content.BroadcastReceiver
            public void onReceive(Context context2, Intent intent) {
                String action = intent.getAction();
                if ("android.provider.Contacts.PROFILE_CHANGED".equals(action) || "android.intent.action.USER_INFO_CHANGED".equals(action)) {
                    try {
                        if (intent.getIntExtra("android.intent.extra.user_handle", getSendingUserId()) == ActivityManager.getService().getCurrentUser().id) {
                            UserInfoControllerImpl.this.reloadUserInfo();
                        }
                    } catch (RemoteException e) {
                        Log.e("UserInfoController", "Couldn't get current user id for profile change", e);
                    }
                }
            }
        };
        this.mProfileReceiver = r2;
        this.mContext = context;
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.USER_SWITCHED");
        context.registerReceiver(r0, intentFilter);
        IntentFilter intentFilter2 = new IntentFilter();
        intentFilter2.addAction("android.provider.Contacts.PROFILE_CHANGED");
        intentFilter2.addAction("android.intent.action.USER_INFO_CHANGED");
        context.registerReceiverAsUser(r2, UserHandle.ALL, intentFilter2, null, null);
    }

    public void addCallback(UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener) {
        this.mCallbacks.add(onUserInfoChangedListener);
        onUserInfoChangedListener.onUserInfoChanged(this.mUserName, this.mUserDrawable, this.mUserAccount);
    }

    public void removeCallback(UserInfoController.OnUserInfoChangedListener onUserInfoChangedListener) {
        this.mCallbacks.remove(onUserInfoChangedListener);
    }

    @Override // com.android.systemui.statusbar.policy.UserInfoController
    public void reloadUserInfo() {
        AsyncTask<Void, Void, UserInfoQueryResult> asyncTask = this.mUserInfoTask;
        if (asyncTask != null) {
            asyncTask.cancel(false);
            this.mUserInfoTask = null;
        }
        queryForUserInformation();
    }

    private void queryForUserInformation() {
        try {
            UserInfo currentUser = ActivityManager.getService().getCurrentUser();
            final Context createPackageContextAsUser = this.mContext.createPackageContextAsUser("android", 0, new UserHandle(currentUser.id));
            final int i = currentUser.id;
            final boolean isGuest = currentUser.isGuest();
            final String str = currentUser.name;
            final boolean z = this.mContext.getThemeResId() != R$style.Theme_SystemUI_LightWallpaper;
            Resources resources = this.mContext.getResources();
            final int max = Math.max(resources.getDimensionPixelSize(R$dimen.multi_user_avatar_expanded_size), resources.getDimensionPixelSize(R$dimen.multi_user_avatar_keyguard_size));
            AnonymousClass3 r0 = new AsyncTask<Void, Void, UserInfoQueryResult>() { // from class: com.android.systemui.statusbar.policy.UserInfoControllerImpl.3
                /* access modifiers changed from: protected */
                public UserInfoQueryResult doInBackground(Void... voidArr) {
                    Drawable drawable;
                    Cursor query;
                    UserManager userManager = UserManager.get(UserInfoControllerImpl.this.mContext);
                    String str2 = str;
                    Bitmap userIcon = userManager.getUserIcon(i);
                    if (userIcon != null) {
                        drawable = new UserIconDrawable(max).setIcon(userIcon).setBadgeIfManagedUser(UserInfoControllerImpl.this.mContext, i).bake();
                    } else {
                        drawable = UserIcons.getDefaultUserIcon(createPackageContextAsUser.getResources(), isGuest ? -10000 : i, z);
                    }
                    if (userManager.getUsers().size() <= 1 && (query = createPackageContextAsUser.getContentResolver().query(ContactsContract.Profile.CONTENT_URI, new String[]{"_id", "display_name"}, null, null, null)) != null) {
                        try {
                            if (query.moveToFirst()) {
                                str2 = query.getString(query.getColumnIndex("display_name"));
                            }
                        } finally {
                            query.close();
                        }
                    }
                    return new UserInfoQueryResult(str2, drawable, userManager.getUserAccount(i));
                }

                /* access modifiers changed from: protected */
                public void onPostExecute(UserInfoQueryResult userInfoQueryResult) {
                    UserInfoControllerImpl.this.mUserName = userInfoQueryResult.getName();
                    UserInfoControllerImpl.this.mUserDrawable = userInfoQueryResult.getAvatar();
                    UserInfoControllerImpl.this.mUserAccount = userInfoQueryResult.getUserAccount();
                    UserInfoControllerImpl.this.mUserInfoTask = null;
                    UserInfoControllerImpl.this.notifyChanged();
                }
            };
            this.mUserInfoTask = r0;
            r0.execute(new Void[0]);
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("UserInfoController", "Couldn't create user context", e);
            throw new RuntimeException(e);
        } catch (RemoteException e2) {
            Log.e("UserInfoController", "Couldn't get user info", e2);
            throw new RuntimeException(e2);
        }
    }

    /* access modifiers changed from: private */
    public void notifyChanged() {
        Iterator<UserInfoController.OnUserInfoChangedListener> it = this.mCallbacks.iterator();
        while (it.hasNext()) {
            it.next().onUserInfoChanged(this.mUserName, this.mUserDrawable, this.mUserAccount);
        }
    }

    public void onDensityOrFontScaleChanged() {
        reloadUserInfo();
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class UserInfoQueryResult {
        private Drawable mAvatar;
        private String mName;
        private String mUserAccount;

        public UserInfoQueryResult(String str, Drawable drawable, String str2) {
            this.mName = str;
            this.mAvatar = drawable;
            this.mUserAccount = str2;
        }

        public String getName() {
            return this.mName;
        }

        public Drawable getAvatar() {
            return this.mAvatar;
        }

        public String getUserAccount() {
            return this.mUserAccount;
        }
    }
}
