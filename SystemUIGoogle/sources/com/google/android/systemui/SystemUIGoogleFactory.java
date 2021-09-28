package com.google.android.systemui;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Handler;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.google.android.systemui.dagger.DaggerSysUIGoogleGlobalRootComponent;
import com.google.android.systemui.dagger.SysUIGoogleSysUIComponent;
import com.google.android.systemui.gesture.BackGestureTfClassifierProviderGoogle;
import com.google.android.systemui.screenshot.ScreenshotNotificationSmartActionsProviderGoogle;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes2.dex */
public class SystemUIGoogleFactory extends SystemUIFactory {
    @Override // com.android.systemui.SystemUIFactory
    protected GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerSysUIGoogleGlobalRootComponent.builder().context(context).build();
    }

    @Override // com.android.systemui.SystemUIFactory
    public ScreenshotNotificationSmartActionsProvider createScreenshotNotificationSmartActionsProvider(Context context, Executor executor, Handler handler) {
        return new ScreenshotNotificationSmartActionsProviderGoogle(context, executor, handler);
    }

    @Override // com.android.systemui.SystemUIFactory
    public BackGestureTfClassifierProvider createBackGestureTfClassifierProvider(AssetManager assetManager, String str) {
        return new BackGestureTfClassifierProviderGoogle(assetManager, str);
    }

    @Override // com.android.systemui.SystemUIFactory
    public void init(Context context, boolean z) throws ExecutionException, InterruptedException {
        super.init(context, z);
        if (shouldInitializeComponents()) {
            ((SysUIGoogleSysUIComponent) getSysUIComponent()).createKeyguardSmartspaceController();
        }
    }
}
