package com.android.systemui;

import android.app.ActivityThread;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Handler;
import android.os.Process;
import android.util.Log;
import com.android.internal.annotations.VisibleForTesting;
import com.android.systemui.dagger.DaggerGlobalRootComponent;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dagger.WMComponent;
import com.android.systemui.navigationbar.gestural.BackGestureTfClassifierProvider;
import com.android.systemui.screenshot.ScreenshotNotificationSmartActionsProvider;
import com.android.wm.shell.transition.Transitions;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
/* loaded from: classes.dex */
public class SystemUIFactory {
    static SystemUIFactory mFactory;
    private boolean mInitializeComponents;
    private GlobalRootComponent mRootComponent;
    private SysUIComponent mSysUIComponent;
    private WMComponent mWMComponent;

    protected SysUIComponent.Builder prepareSysUIComponentBuilder(SysUIComponent.Builder builder, WMComponent wMComponent) {
        return builder;
    }

    public static <T extends SystemUIFactory> T getInstance() {
        return (T) mFactory;
    }

    public static void createFromConfig(Context context) {
        createFromConfig(context, false);
    }

    @VisibleForTesting
    public static void createFromConfig(Context context, boolean z) {
        if (mFactory == null) {
            String string = context.getString(R$string.config_systemUIFactoryComponent);
            if (string == null || string.length() == 0) {
                throw new RuntimeException("No SystemUIFactory component configured");
            }
            try {
                SystemUIFactory systemUIFactory = (SystemUIFactory) context.getClassLoader().loadClass(string).newInstance();
                mFactory = systemUIFactory;
                systemUIFactory.init(context, z);
            } catch (Throwable th) {
                Log.w("SystemUIFactory", "Error creating SystemUIFactory component: " + string, th);
                throw new RuntimeException(th);
            }
        }
    }

    @VisibleForTesting
    static void cleanup() {
        mFactory = null;
    }

    @VisibleForTesting
    public void init(Context context, boolean z) throws ExecutionException, InterruptedException {
        SysUIComponent.Builder builder;
        this.mInitializeComponents = !z && Process.myUserHandle().isSystem() && ActivityThread.currentProcessName().equals(ActivityThread.currentPackageName());
        GlobalRootComponent buildGlobalRootComponent = buildGlobalRootComponent(context);
        this.mRootComponent = buildGlobalRootComponent;
        WMComponent build = buildGlobalRootComponent.getWMComponentBuilder().build();
        this.mWMComponent = build;
        if (this.mInitializeComponents) {
            build.init();
        }
        SysUIComponent.Builder sysUIComponent = this.mRootComponent.getSysUIComponent();
        if (this.mInitializeComponents) {
            builder = prepareSysUIComponentBuilder(sysUIComponent, this.mWMComponent).setPip(this.mWMComponent.getPip()).setLegacySplitScreen(this.mWMComponent.getLegacySplitScreen()).setSplitScreen(this.mWMComponent.getSplitScreen()).setOneHanded(this.mWMComponent.getOneHanded()).setBubbles(this.mWMComponent.getBubbles()).setHideDisplayCutout(this.mWMComponent.getHideDisplayCutout()).setShellCommandHandler(this.mWMComponent.getShellCommandHandler()).setAppPairs(this.mWMComponent.getAppPairs()).setTaskViewFactory(this.mWMComponent.getTaskViewFactory()).setTransitions(this.mWMComponent.getTransitions()).setStartingSurface(this.mWMComponent.getStartingSurface()).setTaskSurfaceHelper(this.mWMComponent.getTaskSurfaceHelper());
        } else {
            builder = prepareSysUIComponentBuilder(sysUIComponent, this.mWMComponent).setPip(Optional.ofNullable(null)).setLegacySplitScreen(Optional.ofNullable(null)).setSplitScreen(Optional.ofNullable(null)).setOneHanded(Optional.ofNullable(null)).setBubbles(Optional.ofNullable(null)).setHideDisplayCutout(Optional.ofNullable(null)).setShellCommandHandler(Optional.ofNullable(null)).setAppPairs(Optional.ofNullable(null)).setTaskViewFactory(Optional.ofNullable(null)).setTransitions(Transitions.createEmptyForTesting()).setStartingSurface(Optional.ofNullable(null)).setTaskSurfaceHelper(Optional.ofNullable(null));
        }
        SysUIComponent build2 = builder.build();
        this.mSysUIComponent = build2;
        if (this.mInitializeComponents) {
            build2.init();
        }
        this.mSysUIComponent.createDependency().start();
    }

    protected GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerGlobalRootComponent.builder().context(context).build();
    }

    /* access modifiers changed from: protected */
    public boolean shouldInitializeComponents() {
        return this.mInitializeComponents;
    }

    public GlobalRootComponent getRootComponent() {
        return this.mRootComponent;
    }

    public SysUIComponent getSysUIComponent() {
        return this.mSysUIComponent;
    }

    public String[] getSystemUIServiceComponents(Resources resources) {
        return resources.getStringArray(R$array.config_systemUIServiceComponents);
    }

    public String[] getSystemUIServiceComponentsPerUser(Resources resources) {
        return resources.getStringArray(R$array.config_systemUIServiceComponentsPerUser);
    }

    public ScreenshotNotificationSmartActionsProvider createScreenshotNotificationSmartActionsProvider(Context context, Executor executor, Handler handler) {
        return new ScreenshotNotificationSmartActionsProvider();
    }

    public BackGestureTfClassifierProvider createBackGestureTfClassifierProvider(AssetManager assetManager, String str) {
        return new BackGestureTfClassifierProvider();
    }
}
