package com.android.systemui.user;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.UserInfo;
import android.graphics.drawable.Drawable;
import android.os.UserManager;
import com.android.internal.util.UserIcons;
import com.android.settingslib.users.UserCreatingDialog;
import com.android.settingslib.utils.ThreadUtils;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public class UserCreator {
    private final Context mContext;
    private final UserManager mUserManager;

    public UserCreator(Context context, UserManager userManager) {
        this.mContext = context;
        this.mUserManager = userManager;
    }

    public void createUser(String str, Drawable drawable, Consumer<UserInfo> consumer, Runnable runnable) {
        UserCreatingDialog userCreatingDialog = new UserCreatingDialog(this.mContext);
        userCreatingDialog.show();
        ThreadUtils.postOnMainThread(new Runnable(str, userCreatingDialog, runnable, drawable, consumer) { // from class: com.android.systemui.user.UserCreator$$ExternalSyntheticLambda0
            public final /* synthetic */ String f$1;
            public final /* synthetic */ Dialog f$2;
            public final /* synthetic */ Runnable f$3;
            public final /* synthetic */ Drawable f$4;
            public final /* synthetic */ Consumer f$5;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
                this.f$5 = r6;
            }

            @Override // java.lang.Runnable
            public final void run() {
                UserCreator.this.lambda$createUser$0(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$createUser$0(String str, Dialog dialog, Runnable runnable, Drawable drawable, Consumer consumer) {
        UserInfo createUser = this.mUserManager.createUser(str, "android.os.usertype.full.SECONDARY", 0);
        if (createUser == null) {
            dialog.dismiss();
            runnable.run();
            return;
        }
        if (drawable == null) {
            drawable = UserIcons.getDefaultUserIcon(this.mContext.getResources(), createUser.id, false);
        }
        this.mUserManager.setUserIcon(createUser.id, UserIcons.convertToBitmap(drawable));
        dialog.dismiss();
        consumer.accept(createUser);
    }
}
