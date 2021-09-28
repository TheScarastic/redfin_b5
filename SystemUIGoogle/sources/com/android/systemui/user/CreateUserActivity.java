package com.android.systemui.user;

import android.app.Activity;
import android.app.Dialog;
import android.app.IActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import com.android.settingslib.R$string;
import com.android.settingslib.users.ActivityStarter;
import com.android.settingslib.users.EditUserInfoController;
import com.android.systemui.R$layout;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class CreateUserActivity extends Activity {
    private final IActivityManager mActivityManager;
    private final EditUserInfoController mEditUserInfoController;
    private Dialog mSetupUserDialog;
    private final UserCreator mUserCreator;

    public static Intent createIntentForStart(Context context) {
        return new Intent(context, CreateUserActivity.class);
    }

    public CreateUserActivity(UserCreator userCreator, EditUserInfoController editUserInfoController, IActivityManager iActivityManager) {
        this.mUserCreator = userCreator;
        this.mEditUserInfoController = editUserInfoController;
        this.mActivityManager = iActivityManager;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setShowWhenLocked(true);
        setContentView(R$layout.activity_create_new_user);
        if (bundle != null) {
            this.mEditUserInfoController.onRestoreInstanceState(bundle);
        }
        Dialog createDialog = createDialog();
        this.mSetupUserDialog = createDialog;
        createDialog.show();
    }

    @Override // android.app.Activity
    protected void onSaveInstanceState(Bundle bundle) {
        Dialog dialog = this.mSetupUserDialog;
        if (dialog != null && dialog.isShowing()) {
            bundle.putBundle("create_user_dialog_state", this.mSetupUserDialog.onSaveInstanceState());
        }
        this.mEditUserInfoController.onSaveInstanceState(bundle);
        super.onSaveInstanceState(bundle);
    }

    @Override // android.app.Activity
    protected void onRestoreInstanceState(Bundle bundle) {
        Dialog dialog;
        super.onRestoreInstanceState(bundle);
        Bundle bundle2 = bundle.getBundle("create_user_dialog_state");
        if (bundle2 != null && (dialog = this.mSetupUserDialog) != null) {
            dialog.onRestoreInstanceState(bundle2);
        }
    }

    private Dialog createDialog() {
        return this.mEditUserInfoController.createDialog(this, new ActivityStarter() { // from class: com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda0
            @Override // com.android.settingslib.users.ActivityStarter
            public final void startActivityForResult(Intent intent, int i) {
                CreateUserActivity.this.lambda$createDialog$0(intent, i);
            }
        }, null, getString(R$string.user_new_user_name), getString(com.android.systemui.R$string.user_add_user), new BiConsumer() { // from class: com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda3
            @Override // java.util.function.BiConsumer
            public final void accept(Object obj, Object obj2) {
                CreateUserActivity.this.addUserNow((String) obj, (Drawable) obj2);
            }
        }, new Runnable() { // from class: com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda1
            @Override // java.lang.Runnable
            public final void run() {
                CreateUserActivity.this.finish();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createDialog$0(Intent intent, int i) {
        this.mEditUserInfoController.startingActivityForResult();
        startActivityForResult(intent, i);
    }

    @Override // android.app.Activity
    protected void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        this.mEditUserInfoController.onActivityResult(i, i2, intent);
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        super.onBackPressed();
        Dialog dialog = this.mSetupUserDialog;
        if (dialog != null) {
            dialog.dismiss();
        }
    }

    /* access modifiers changed from: private */
    public void addUserNow(String str, Drawable drawable) {
        this.mSetupUserDialog.dismiss();
        if (str == null || str.trim().isEmpty()) {
            str = getString(com.android.systemui.R$string.user_new_user_name);
        }
        this.mUserCreator.createUser(str, drawable, new Consumer() { // from class: com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda4
            @Override // java.util.function.Consumer
            public final void accept(Object obj) {
                CreateUserActivity.this.lambda$addUserNow$1((UserInfo) obj);
            }
        }, new Runnable() { // from class: com.android.systemui.user.CreateUserActivity$$ExternalSyntheticLambda2
            @Override // java.lang.Runnable
            public final void run() {
                CreateUserActivity.this.lambda$addUserNow$2();
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addUserNow$1(UserInfo userInfo) {
        switchToUser(userInfo.id);
        finishIfNeeded();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$addUserNow$2() {
        Log.e("CreateUserActivity", "Unable to create user");
        finishIfNeeded();
    }

    private void finishIfNeeded() {
        if (!isFinishing() && !isDestroyed()) {
            finish();
        }
    }

    private void switchToUser(int i) {
        try {
            this.mActivityManager.switchUser(i);
        } catch (RemoteException e) {
            Log.e("CreateUserActivity", "Couldn't switch user.", e);
        }
    }
}
