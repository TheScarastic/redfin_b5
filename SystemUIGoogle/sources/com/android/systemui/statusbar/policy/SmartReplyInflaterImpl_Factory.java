package com.android.systemui.statusbar.policy;

import android.content.Context;
import com.android.systemui.statusbar.NotificationRemoteInputManager;
import com.android.systemui.statusbar.SmartReplyController;
import com.android.systemui.statusbar.phone.KeyguardDismissUtil;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SmartReplyInflaterImpl_Factory implements Factory<SmartReplyInflaterImpl> {
    private final Provider<SmartReplyConstants> constantsProvider;
    private final Provider<Context> contextProvider;
    private final Provider<KeyguardDismissUtil> keyguardDismissUtilProvider;
    private final Provider<NotificationRemoteInputManager> remoteInputManagerProvider;
    private final Provider<SmartReplyController> smartReplyControllerProvider;

    public SmartReplyInflaterImpl_Factory(Provider<SmartReplyConstants> provider, Provider<KeyguardDismissUtil> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<SmartReplyController> provider4, Provider<Context> provider5) {
        this.constantsProvider = provider;
        this.keyguardDismissUtilProvider = provider2;
        this.remoteInputManagerProvider = provider3;
        this.smartReplyControllerProvider = provider4;
        this.contextProvider = provider5;
    }

    @Override // javax.inject.Provider
    public SmartReplyInflaterImpl get() {
        return newInstance(this.constantsProvider.get(), this.keyguardDismissUtilProvider.get(), this.remoteInputManagerProvider.get(), this.smartReplyControllerProvider.get(), this.contextProvider.get());
    }

    public static SmartReplyInflaterImpl_Factory create(Provider<SmartReplyConstants> provider, Provider<KeyguardDismissUtil> provider2, Provider<NotificationRemoteInputManager> provider3, Provider<SmartReplyController> provider4, Provider<Context> provider5) {
        return new SmartReplyInflaterImpl_Factory(provider, provider2, provider3, provider4, provider5);
    }

    public static SmartReplyInflaterImpl newInstance(SmartReplyConstants smartReplyConstants, KeyguardDismissUtil keyguardDismissUtil, NotificationRemoteInputManager notificationRemoteInputManager, SmartReplyController smartReplyController, Context context) {
        return new SmartReplyInflaterImpl(smartReplyConstants, keyguardDismissUtil, notificationRemoteInputManager, smartReplyController, context);
    }
}
