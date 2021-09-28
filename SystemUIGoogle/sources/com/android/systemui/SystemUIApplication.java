package com.android.systemui;

import android.app.ActivityThread;
import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.res.Configuration;
import android.os.Process;
import android.os.SystemProperties;
import android.os.UserHandle;
import android.util.Log;
import android.util.TimingsTraceLog;
import android.view.SurfaceControl;
import com.android.internal.protolog.common.ProtoLog;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.dagger.ContextComponentHelper;
import com.android.systemui.dagger.GlobalRootComponent;
import com.android.systemui.dagger.SysUIComponent;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.shared.system.ThreadedRendererCompat;
import com.android.systemui.util.NotificationChannels;
import java.lang.reflect.InvocationTargetException;
/* loaded from: classes.dex */
public class SystemUIApplication extends Application implements SystemUIAppComponentFactory.ContextInitializer {
    private BootCompleteCacheImpl mBootCompleteCache;
    private ContextComponentHelper mComponentHelper;
    private SystemUIAppComponentFactory.ContextAvailableCallback mContextAvailableCallback;
    private GlobalRootComponent mRootComponent;
    private SystemUI[] mServices;
    private boolean mServicesStarted;
    private SysUIComponent mSysUIComponent;

    public SystemUIApplication() {
        Log.v("SystemUIService", "SystemUIApplication constructed.");
        ProtoLog.REQUIRE_PROTOLOGTOOL = false;
    }

    @Override // android.app.Application
    public void onCreate() {
        super.onCreate();
        Log.v("SystemUIService", "SystemUIApplication created.");
        TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096);
        timingsTraceLog.traceBegin("DependencyInjection");
        this.mContextAvailableCallback.onContextAvailable(this);
        this.mRootComponent = SystemUIFactory.getInstance().getRootComponent();
        SysUIComponent sysUIComponent = SystemUIFactory.getInstance().getSysUIComponent();
        this.mSysUIComponent = sysUIComponent;
        this.mComponentHelper = sysUIComponent.getContextComponentHelper();
        this.mBootCompleteCache = this.mSysUIComponent.provideBootCacheImpl();
        timingsTraceLog.traceEnd();
        setTheme(R$style.Theme_SystemUI);
        if (Process.myUserHandle().equals(UserHandle.SYSTEM)) {
            IntentFilter intentFilter = new IntentFilter("android.intent.action.BOOT_COMPLETED");
            intentFilter.setPriority(1000);
            int gPUContextPriority = SurfaceControl.getGPUContextPriority();
            Log.i("SystemUIService", "Found SurfaceFlinger's GPU Priority: " + gPUContextPriority);
            if (gPUContextPriority == ThreadedRendererCompat.EGL_CONTEXT_PRIORITY_REALTIME_NV) {
                Log.i("SystemUIService", "Setting SysUI's GPU Context priority to: " + ThreadedRendererCompat.EGL_CONTEXT_PRIORITY_HIGH_IMG);
                ThreadedRendererCompat.setContextPriority(ThreadedRendererCompat.EGL_CONTEXT_PRIORITY_HIGH_IMG);
            }
            registerReceiver(new BroadcastReceiver() { // from class: com.android.systemui.SystemUIApplication.1
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if (!SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        SystemUIApplication.this.unregisterReceiver(this);
                        SystemUIApplication.this.mBootCompleteCache.setBootComplete();
                        if (SystemUIApplication.this.mServicesStarted) {
                            int length = SystemUIApplication.this.mServices.length;
                            for (int i = 0; i < length; i++) {
                                SystemUIApplication.this.mServices[i].onBootCompleted();
                            }
                        }
                    }
                }
            }, intentFilter);
            registerReceiver(new BroadcastReceiver() { // from class: com.android.systemui.SystemUIApplication.2
                @Override // android.content.BroadcastReceiver
                public void onReceive(Context context, Intent intent) {
                    if ("android.intent.action.LOCALE_CHANGED".equals(intent.getAction()) && SystemUIApplication.this.mBootCompleteCache.isBootComplete()) {
                        NotificationChannels.createAll(context);
                    }
                }
            }, new IntentFilter("android.intent.action.LOCALE_CHANGED"));
            return;
        }
        String currentProcessName = ActivityThread.currentProcessName();
        ApplicationInfo applicationInfo = getApplicationInfo();
        if (currentProcessName != null) {
            if (currentProcessName.startsWith(applicationInfo.processName + ":")) {
                return;
            }
        }
        startSecondaryUserServicesIfNeeded();
    }

    public void startServicesIfNeeded() {
        startServicesIfNeeded("StartServices", SystemUIFactory.getInstance().getSystemUIServiceComponents(getResources()));
    }

    /* access modifiers changed from: package-private */
    public void startSecondaryUserServicesIfNeeded() {
        startServicesIfNeeded("StartSecondaryServices", SystemUIFactory.getInstance().getSystemUIServiceComponentsPerUser(getResources()));
    }

    private void startServicesIfNeeded(String str, String[] strArr) {
        if (!this.mServicesStarted) {
            this.mServices = new SystemUI[strArr.length];
            if (!this.mBootCompleteCache.isBootComplete() && "1".equals(SystemProperties.get("sys.boot_completed"))) {
                this.mBootCompleteCache.setBootComplete();
            }
            DumpManager createDumpManager = this.mSysUIComponent.createDumpManager();
            Log.v("SystemUIService", "Starting SystemUI services for user " + Process.myUserHandle().getIdentifier() + ".");
            TimingsTraceLog timingsTraceLog = new TimingsTraceLog("SystemUIBootTiming", 4096);
            timingsTraceLog.traceBegin(str);
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                String str2 = strArr[i];
                timingsTraceLog.traceBegin(str + str2);
                long currentTimeMillis = System.currentTimeMillis();
                try {
                    SystemUI resolveSystemUI = this.mComponentHelper.resolveSystemUI(str2);
                    if (resolveSystemUI == null) {
                        resolveSystemUI = (SystemUI) Class.forName(str2).getConstructor(Context.class).newInstance(this);
                    }
                    SystemUI[] systemUIArr = this.mServices;
                    systemUIArr[i] = resolveSystemUI;
                    systemUIArr[i].start();
                    timingsTraceLog.traceEnd();
                    long currentTimeMillis2 = System.currentTimeMillis() - currentTimeMillis;
                    if (currentTimeMillis2 > 1000) {
                        Log.w("SystemUIService", "Initialization of " + str2 + " took " + currentTimeMillis2 + " ms");
                    }
                    if (this.mBootCompleteCache.isBootComplete()) {
                        this.mServices[i].onBootCompleted();
                    }
                    createDumpManager.registerDumpable(this.mServices[i].getClass().getName(), this.mServices[i]);
                } catch (ClassNotFoundException | IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e) {
                    throw new RuntimeException(e);
                }
            }
            this.mSysUIComponent.getInitController().executePostInitTasks();
            timingsTraceLog.traceEnd();
            this.mServicesStarted = true;
        }
    }

    @Override // android.app.Application, android.content.ComponentCallbacks
    public void onConfigurationChanged(Configuration configuration) {
        if (this.mServicesStarted) {
            this.mSysUIComponent.getConfigurationController().onConfigurationChanged(configuration);
            int length = this.mServices.length;
            for (int i = 0; i < length; i++) {
                SystemUI[] systemUIArr = this.mServices;
                if (systemUIArr[i] != null) {
                    systemUIArr[i].onConfigurationChanged(configuration);
                }
            }
        }
    }

    @Override // com.android.systemui.SystemUIAppComponentFactory.ContextInitializer
    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mContextAvailableCallback = contextAvailableCallback;
    }
}
