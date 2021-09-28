package com.google.android.systemui;

import android.app.AlarmManager;
import android.content.Context;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.Dumpable;
import com.android.systemui.R$bool;
import com.android.systemui.R$id;
import com.android.systemui.VendorServices;
import com.android.systemui.statusbar.FeatureFlags;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.ambientmusic.AmbientIndicationContainer;
import com.google.android.systemui.ambientmusic.AmbientIndicationService;
import com.google.android.systemui.autorotate.AutorotateDataService;
import com.google.android.systemui.columbus.ColumbusContext;
import com.google.android.systemui.columbus.ColumbusServiceWrapper;
import com.google.android.systemui.coversheet.CoversheetService;
import com.google.android.systemui.elmyra.ElmyraContext;
import com.google.android.systemui.elmyra.ElmyraService;
import com.google.android.systemui.elmyra.ServiceConfigurationGoogle;
import com.google.android.systemui.face.FaceNotificationService;
import com.google.android.systemui.input.TouchContextService;
import com.google.android.systemui.statusbar.KeyguardIndicationControllerGoogle;
import dagger.Lazy;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.ArrayList;
/* loaded from: classes2.dex */
public class GoogleServices extends VendorServices {
    private final AlarmManager mAlarmManager;
    private final AutorotateDataService mAutorotateDataService;
    private final Lazy<ColumbusServiceWrapper> mColumbusServiceLazy;
    private final FeatureFlags mFeatureFlags;
    private final KeyguardIndicationControllerGoogle mKeyguardIndicationController;
    private final Lazy<ServiceConfigurationGoogle> mServiceConfigurationGoogle;
    private ArrayList<Object> mServices = new ArrayList<>();
    private final StatusBar mStatusBar;
    private final UiEventLogger mUiEventLogger;

    public GoogleServices(Context context, Lazy<ServiceConfigurationGoogle> lazy, StatusBar statusBar, UiEventLogger uiEventLogger, Lazy<ColumbusServiceWrapper> lazy2, FeatureFlags featureFlags, KeyguardIndicationControllerGoogle keyguardIndicationControllerGoogle, AlarmManager alarmManager, AutorotateDataService autorotateDataService) {
        super(context);
        this.mServiceConfigurationGoogle = lazy;
        this.mStatusBar = statusBar;
        this.mUiEventLogger = uiEventLogger;
        this.mColumbusServiceLazy = lazy2;
        this.mFeatureFlags = featureFlags;
        this.mKeyguardIndicationController = keyguardIndicationControllerGoogle;
        this.mAlarmManager = alarmManager;
        this.mAutorotateDataService = autorotateDataService;
    }

    @Override // com.android.systemui.VendorServices, com.android.systemui.SystemUI
    public void start() {
        AmbientIndicationContainer ambientIndicationContainer = (AmbientIndicationContainer) this.mStatusBar.getNotificationShadeWindowView().findViewById(R$id.ambient_indication_container);
        ambientIndicationContainer.initializeView(this.mStatusBar, this.mFeatureFlags);
        addService(new AmbientIndicationService(this.mContext, ambientIndicationContainer, this.mKeyguardIndicationController, this.mAlarmManager));
        addService(new DisplayCutoutEmulationAdapter(this.mContext));
        addService(new CoversheetService(this.mContext));
        this.mAutorotateDataService.init();
        addService(this.mAutorotateDataService);
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.context_hub") && new ElmyraContext(this.mContext).isAvailable()) {
            addService(new ElmyraService(this.mContext, this.mServiceConfigurationGoogle.get(), this.mUiEventLogger));
        }
        if (new ColumbusContext(this.mContext).isAvailable()) {
            addService(this.mColumbusServiceLazy.get());
        }
        if (this.mContext.getPackageManager().hasSystemFeature("android.hardware.biometrics.face")) {
            addService(new FaceNotificationService(this.mContext));
        }
        if (this.mContext.getResources().getBoolean(R$bool.config_touch_context_enabled)) {
            addService(new TouchContextService(this.mContext));
        }
    }

    @Override // com.android.systemui.SystemUI, com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        for (int i = 0; i < this.mServices.size(); i++) {
            if (this.mServices.get(i) instanceof Dumpable) {
                ((Dumpable) this.mServices.get(i)).dump(fileDescriptor, printWriter, strArr);
            }
        }
    }

    private void addService(Object obj) {
        if (obj != null) {
            this.mServices.add(obj);
        }
    }
}
