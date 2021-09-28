package com.android.systemui.classifier;

import android.content.Context;
import android.net.Uri;
import android.provider.DeviceConfig;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.plugins.FalsingManager;
import com.android.systemui.plugins.FalsingPlugin;
import com.android.systemui.plugins.PluginListener;
import com.android.systemui.shared.plugins.PluginManager;
import com.android.systemui.util.DeviceConfigProxy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.concurrent.Executor;
import javax.inject.Provider;
/* loaded from: classes.dex */
public class FalsingManagerProxy implements FalsingManager, Dumpable {
    private final Provider<BrightLineFalsingManager> mBrightLineFalsingManagerProvider;
    private final DeviceConfigProxy mDeviceConfig;
    private final DeviceConfig.OnPropertiesChangedListener mDeviceConfigListener;
    private final DumpManager mDumpManager;
    private FalsingManager mInternalFalsingManager;
    final PluginListener<FalsingPlugin> mPluginListener;
    private final PluginManager mPluginManager;

    public /* synthetic */ void lambda$new$0(DeviceConfig.Properties properties) {
        onDeviceConfigPropertiesChanged(properties.getNamespace());
    }

    public FalsingManagerProxy(PluginManager pluginManager, Executor executor, DeviceConfigProxy deviceConfigProxy, DumpManager dumpManager, Provider<BrightLineFalsingManager> provider) {
        FalsingManagerProxy$$ExternalSyntheticLambda0 falsingManagerProxy$$ExternalSyntheticLambda0 = new DeviceConfig.OnPropertiesChangedListener() { // from class: com.android.systemui.classifier.FalsingManagerProxy$$ExternalSyntheticLambda0
            public final void onPropertiesChanged(DeviceConfig.Properties properties) {
                FalsingManagerProxy.m87$r8$lambda$hCp_tSCnO9JF4K1uJkYn_66ZM(FalsingManagerProxy.this, properties);
            }
        };
        this.mDeviceConfigListener = falsingManagerProxy$$ExternalSyntheticLambda0;
        this.mPluginManager = pluginManager;
        this.mDumpManager = dumpManager;
        this.mDeviceConfig = deviceConfigProxy;
        this.mBrightLineFalsingManagerProvider = provider;
        setupFalsingManager();
        deviceConfigProxy.addOnPropertiesChangedListener("systemui", executor, falsingManagerProxy$$ExternalSyntheticLambda0);
        AnonymousClass1 r3 = new PluginListener<FalsingPlugin>() { // from class: com.android.systemui.classifier.FalsingManagerProxy.1
            public void onPluginConnected(FalsingPlugin falsingPlugin, Context context) {
                FalsingManager falsingManager = falsingPlugin.getFalsingManager(context);
                if (falsingManager != null) {
                    FalsingManagerProxy.this.mInternalFalsingManager.cleanupInternal();
                    FalsingManagerProxy.this.mInternalFalsingManager = falsingManager;
                }
            }

            public void onPluginDisconnected(FalsingPlugin falsingPlugin) {
                FalsingManagerProxy.this.setupFalsingManager();
            }
        };
        this.mPluginListener = r3;
        pluginManager.addPluginListener(r3, FalsingPlugin.class);
        dumpManager.registerDumpable("FalsingManager", this);
    }

    private void onDeviceConfigPropertiesChanged(String str) {
        if ("systemui".equals(str)) {
            setupFalsingManager();
        }
    }

    public void setupFalsingManager() {
        FalsingManager falsingManager = this.mInternalFalsingManager;
        if (falsingManager != null) {
            falsingManager.cleanupInternal();
        }
        this.mInternalFalsingManager = this.mBrightLineFalsingManagerProvider.get();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void onSuccessfulUnlock() {
        this.mInternalFalsingManager.onSuccessfulUnlock();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isUnlockingDisabled() {
        return this.mInternalFalsingManager.isUnlockingDisabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isFalseTouch(int i) {
        return this.mInternalFalsingManager.isFalseTouch(i);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isSimpleTap() {
        return this.mInternalFalsingManager.isSimpleTap();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isFalseTap(int i) {
        return this.mInternalFalsingManager.isFalseTap(i);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isFalseDoubleTap() {
        return this.mInternalFalsingManager.isFalseDoubleTap();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isClassifierEnabled() {
        return this.mInternalFalsingManager.isClassifierEnabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean shouldEnforceBouncer() {
        return this.mInternalFalsingManager.shouldEnforceBouncer();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public Uri reportRejectedTouch() {
        return this.mInternalFalsingManager.reportRejectedTouch();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public boolean isReportingEnabled() {
        return this.mInternalFalsingManager.isReportingEnabled();
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void addFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mInternalFalsingManager.addFalsingBeliefListener(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void removeFalsingBeliefListener(FalsingManager.FalsingBeliefListener falsingBeliefListener) {
        this.mInternalFalsingManager.removeFalsingBeliefListener(falsingBeliefListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void addTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mInternalFalsingManager.addTapListener(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void removeTapListener(FalsingManager.FalsingTapListener falsingTapListener) {
        this.mInternalFalsingManager.removeTapListener(falsingTapListener);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void onProximityEvent(FalsingManager.ProximityEvent proximityEvent) {
        this.mInternalFalsingManager.onProximityEvent(proximityEvent);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        this.mInternalFalsingManager.dump(fileDescriptor, printWriter, strArr);
    }

    @Override // com.android.systemui.plugins.FalsingManager
    public void cleanupInternal() {
        this.mDeviceConfig.removeOnPropertiesChangedListener(this.mDeviceConfigListener);
        this.mPluginManager.removePluginListener(this.mPluginListener);
        this.mDumpManager.unregisterDumpable("FalsingManager");
        this.mInternalFalsingManager.cleanupInternal();
    }
}
