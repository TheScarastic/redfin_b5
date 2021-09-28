package com.google.android.systemui.dagger;

import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
/* loaded from: classes2.dex */
public final class SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory implements Factory<BcSmartspaceDataPlugin> {

    /* access modifiers changed from: private */
    /* loaded from: classes2.dex */
    public static final class InstanceHolder {
        private static final SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory INSTANCE = new SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory();
    }

    @Override // javax.inject.Provider
    public BcSmartspaceDataPlugin get() {
        return provideBcSmartspaceDataPlugin();
    }

    public static SystemUIGoogleModule_ProvideBcSmartspaceDataPluginFactory create() {
        return InstanceHolder.INSTANCE;
    }

    public static BcSmartspaceDataPlugin provideBcSmartspaceDataPlugin() {
        return (BcSmartspaceDataPlugin) Preconditions.checkNotNullFromProvides(SystemUIGoogleModule.provideBcSmartspaceDataPlugin());
    }
}
