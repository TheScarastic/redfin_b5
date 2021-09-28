package com.android.systemui;

import android.os.Handler;
import android.os.UserHandle;
import android.service.notification.StatusBarNotification;
import android.util.SparseArray;
import com.android.systemui.appops.AppOpsController;
import com.android.systemui.util.Assert;
/* loaded from: classes.dex */
public class ForegroundServiceController {
    public static final int[] APP_OPS = {24};
    private final Handler mMainHandler;
    private final SparseArray<ForegroundServicesUserState> mUserServices = new SparseArray<>();
    private final Object mMutex = new Object();

    /* loaded from: classes.dex */
    interface UserStateUpdateCallback {
        boolean updateUserState(ForegroundServicesUserState foregroundServicesUserState);
    }

    public ForegroundServiceController(AppOpsController appOpsController, Handler handler) {
        this.mMainHandler = handler;
        appOpsController.addCallback(APP_OPS, new AppOpsController.Callback() { // from class: com.android.systemui.ForegroundServiceController$$ExternalSyntheticLambda0
            @Override // com.android.systemui.appops.AppOpsController.Callback
            public final void onActiveStateChanged(int i, int i2, String str, boolean z) {
                ForegroundServiceController.this.lambda$new$1(i, i2, str, z);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$new$1(int i, int i2, String str, boolean z) {
        this.mMainHandler.post(new Runnable(i, i2, str, z) { // from class: com.android.systemui.ForegroundServiceController$$ExternalSyntheticLambda1
            public final /* synthetic */ int f$1;
            public final /* synthetic */ int f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ boolean f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // java.lang.Runnable
            public final void run() {
                ForegroundServiceController.this.lambda$new$0(this.f$1, this.f$2, this.f$3, this.f$4);
            }
        });
    }

    public boolean isDisclosureNeededForUser(int i) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                return false;
            }
            return foregroundServicesUserState.isDisclosureNeeded();
        }
    }

    /* access modifiers changed from: package-private */
    /* renamed from: onAppOpChanged */
    public void lambda$new$0(int i, int i2, String str, boolean z) {
        Assert.isMainThread();
        int userId = UserHandle.getUserId(i2);
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(userId);
            if (foregroundServicesUserState == null) {
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(userId, foregroundServicesUserState);
            }
            if (z) {
                foregroundServicesUserState.addOp(str, i);
            } else {
                foregroundServicesUserState.removeOp(str, i);
            }
        }
    }

    /* access modifiers changed from: package-private */
    public boolean updateUserState(int i, UserStateUpdateCallback userStateUpdateCallback, boolean z) {
        synchronized (this.mMutex) {
            ForegroundServicesUserState foregroundServicesUserState = this.mUserServices.get(i);
            if (foregroundServicesUserState == null) {
                if (!z) {
                    return false;
                }
                foregroundServicesUserState = new ForegroundServicesUserState();
                this.mUserServices.put(i, foregroundServicesUserState);
            }
            return userStateUpdateCallback.updateUserState(foregroundServicesUserState);
        }
    }

    public boolean isDisclosureNotification(StatusBarNotification statusBarNotification) {
        return statusBarNotification.getId() == 40 && statusBarNotification.getTag() == null && statusBarNotification.getPackageName().equals("android");
    }
}
