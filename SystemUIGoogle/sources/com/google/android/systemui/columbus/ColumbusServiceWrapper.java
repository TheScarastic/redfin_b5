package com.google.android.systemui.columbus;

import com.android.systemui.Dumpable;
import com.google.android.systemui.columbus.actions.SettingsAction;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ColumbusServiceWrapper.kt */
/* loaded from: classes2.dex */
public final class ColumbusServiceWrapper implements Dumpable {
    private final Lazy<ColumbusService> columbusService;
    private final ColumbusSettings columbusSettings;
    private final Lazy<SettingsAction> settingsAction;
    private final ColumbusServiceWrapper$settingsChangeListener$1 settingsChangeListener;
    private boolean started;

    public ColumbusServiceWrapper(ColumbusSettings columbusSettings, Lazy<ColumbusService> lazy, Lazy<SettingsAction> lazy2) {
        Intrinsics.checkNotNullParameter(columbusSettings, "columbusSettings");
        Intrinsics.checkNotNullParameter(lazy, "columbusService");
        Intrinsics.checkNotNullParameter(lazy2, "settingsAction");
        this.columbusSettings = columbusSettings;
        this.columbusService = lazy;
        this.settingsAction = lazy2;
        ColumbusServiceWrapper$settingsChangeListener$1 columbusServiceWrapper$settingsChangeListener$1 = new ColumbusServiceWrapper$settingsChangeListener$1(this);
        this.settingsChangeListener = columbusServiceWrapper$settingsChangeListener$1;
        if (columbusSettings.isColumbusEnabled()) {
            startService();
            return;
        }
        columbusSettings.registerColumbusSettingsChangeListener(columbusServiceWrapper$settingsChangeListener$1);
        lazy2.get();
    }

    /* access modifiers changed from: private */
    public final void startService() {
        this.columbusSettings.unregisterColumbusSettingsChangeListener(this.settingsChangeListener);
        this.started = true;
        this.columbusService.get();
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        if (this.started) {
            this.columbusService.get().dump(fileDescriptor, printWriter, strArr);
        }
    }
}
