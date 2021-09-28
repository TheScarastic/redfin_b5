package com.android.systemui.statusbar.policy;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import com.android.systemui.R$array;
import com.android.systemui.controls.ControlsServiceInfo;
import com.android.systemui.controls.controller.ControlsController;
import com.android.systemui.controls.controller.SeedResponse;
import com.android.systemui.controls.dagger.ControlsComponent;
import com.android.systemui.controls.management.ControlsListingController;
import com.android.systemui.settings.UserContextProvider;
import com.android.systemui.statusbar.policy.DeviceControlsController;
import com.android.systemui.util.settings.SecureSettings;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import kotlin.collections.CollectionsKt___CollectionsKt;
import kotlin.collections.SetsKt__SetsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: DeviceControlsControllerImpl.kt */
/* loaded from: classes.dex */
public final class DeviceControlsControllerImpl implements DeviceControlsController {
    public static final Companion Companion = new Companion(null);
    private DeviceControlsController.Callback callback;
    private final Context context;
    private final ControlsComponent controlsComponent;
    private final DeviceControlsControllerImpl$listingCallback$1 listingCallback = new DeviceControlsControllerImpl$listingCallback$1(this);
    private Integer position;
    private final SecureSettings secureSettings;
    private final UserContextProvider userContextProvider;

    public DeviceControlsControllerImpl(Context context, ControlsComponent controlsComponent, UserContextProvider userContextProvider, SecureSettings secureSettings) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(controlsComponent, "controlsComponent");
        Intrinsics.checkNotNullParameter(userContextProvider, "userContextProvider");
        Intrinsics.checkNotNullParameter(secureSettings, "secureSettings");
        this.context = context;
        this.controlsComponent = controlsComponent;
        this.userContextProvider = userContextProvider;
        this.secureSettings = secureSettings;
    }

    public final Integer getPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core() {
        return this.position;
    }

    public final void setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(Integer num) {
        this.position = num;
    }

    /* compiled from: DeviceControlsControllerImpl.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    private final void checkMigrationToQs() {
        this.controlsComponent.getControlsController().ifPresent(new Consumer<ControlsController>(this) { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$checkMigrationToQs$1
            final /* synthetic */ DeviceControlsControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public final void accept(ControlsController controlsController) {
                Intrinsics.checkNotNullParameter(controlsController, "it");
                if (!controlsController.getFavorites().isEmpty()) {
                    this.this$0.setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(3);
                    DeviceControlsControllerImpl.access$fireControlsUpdate(this.this$0);
                }
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.DeviceControlsController
    public void setCallback(DeviceControlsController.Callback callback) {
        Intrinsics.checkNotNullParameter(callback, "callback");
        removeCallback();
        this.callback = callback;
        if (this.secureSettings.getInt("controls_enabled", 1) == 0) {
            fireControlsUpdate();
            return;
        }
        checkMigrationToQs();
        this.controlsComponent.getControlsListingController().ifPresent(new Consumer<ControlsListingController>(this) { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$setCallback$1
            final /* synthetic */ DeviceControlsControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public final void accept(ControlsListingController controlsListingController) {
                Intrinsics.checkNotNullParameter(controlsListingController, "it");
                controlsListingController.addCallback(this.this$0.listingCallback);
            }
        });
    }

    @Override // com.android.systemui.statusbar.policy.DeviceControlsController
    public void removeCallback() {
        this.position = null;
        this.callback = null;
        this.controlsComponent.getControlsListingController().ifPresent(new Consumer<ControlsListingController>(this) { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$removeCallback$1
            final /* synthetic */ DeviceControlsControllerImpl this$0;

            /* access modifiers changed from: package-private */
            {
                this.this$0 = r1;
            }

            public final void accept(ControlsListingController controlsListingController) {
                Intrinsics.checkNotNullParameter(controlsListingController, "it");
                controlsListingController.removeCallback(this.this$0.listingCallback);
            }
        });
    }

    public final void fireControlsUpdate() {
        Log.i("DeviceControlsControllerImpl", Intrinsics.stringPlus("Setting DeviceControlsTile position: ", this.position));
        DeviceControlsController.Callback callback = this.callback;
        if (callback != null) {
            callback.onControlsUpdate(this.position);
        }
    }

    public final void seedFavorites(List<ControlsServiceInfo> list) {
        String[] stringArray = this.context.getResources().getStringArray(R$array.config_controlsPreferredPackages);
        SharedPreferences sharedPreferences = this.userContextProvider.getUserContext().getSharedPreferences("controls_prefs", 0);
        Set<String> stringSet = sharedPreferences.getStringSet("SeedingCompleted", SetsKt__SetsKt.emptySet());
        ControlsController controlsController = this.controlsComponent.getControlsController().get();
        Intrinsics.checkNotNullExpressionValue(controlsController, "controlsComponent.getControlsController().get()");
        ControlsController controlsController2 = controlsController;
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < Math.min(2, stringArray.length); i++) {
            String str = stringArray[i];
            for (ControlsServiceInfo controlsServiceInfo : list) {
                if (str.equals(controlsServiceInfo.componentName.getPackageName()) && !stringSet.contains(str)) {
                    ComponentName componentName = controlsServiceInfo.componentName;
                    Intrinsics.checkNotNullExpressionValue(componentName, "it.componentName");
                    if (controlsController2.countFavoritesForComponent(componentName) > 0) {
                        Intrinsics.checkNotNullExpressionValue(sharedPreferences, "prefs");
                        Intrinsics.checkNotNullExpressionValue(str, "pkg");
                        addPackageToSeededSet(sharedPreferences, str);
                    } else {
                        ComponentName componentName2 = controlsServiceInfo.componentName;
                        Intrinsics.checkNotNullExpressionValue(componentName2, "it.componentName");
                        arrayList.add(componentName2);
                    }
                }
            }
        }
        if (!arrayList.isEmpty()) {
            controlsController2.seedFavoritesForComponents(arrayList, new Consumer<SeedResponse>(this, sharedPreferences) { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$seedFavorites$2
                final /* synthetic */ SharedPreferences $prefs;
                final /* synthetic */ DeviceControlsControllerImpl this$0;

                /* access modifiers changed from: package-private */
                {
                    this.this$0 = r1;
                    this.$prefs = r2;
                }

                public final void accept(SeedResponse seedResponse) {
                    Intrinsics.checkNotNullParameter(seedResponse, "response");
                    Log.d("DeviceControlsControllerImpl", Intrinsics.stringPlus("Controls seeded: ", seedResponse));
                    if (seedResponse.getAccepted()) {
                        DeviceControlsControllerImpl deviceControlsControllerImpl = this.this$0;
                        SharedPreferences sharedPreferences2 = this.$prefs;
                        Intrinsics.checkNotNullExpressionValue(sharedPreferences2, "prefs");
                        deviceControlsControllerImpl.addPackageToSeededSet(sharedPreferences2, seedResponse.getPackageName());
                        if (this.this$0.getPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core() == null) {
                            this.this$0.setPosition$frameworks__base__packages__SystemUI__android_common__SystemUI_core(7);
                        }
                        this.this$0.fireControlsUpdate();
                        Optional<ControlsListingController> controlsListingController = this.this$0.controlsComponent.getControlsListingController();
                        final DeviceControlsControllerImpl deviceControlsControllerImpl2 = this.this$0;
                        controlsListingController.ifPresent(new Consumer<ControlsListingController>() { // from class: com.android.systemui.statusbar.policy.DeviceControlsControllerImpl$seedFavorites$2.1
                            public final void accept(ControlsListingController controlsListingController2) {
                                Intrinsics.checkNotNullParameter(controlsListingController2, "it");
                                controlsListingController2.removeCallback(deviceControlsControllerImpl2.listingCallback);
                            }
                        });
                    }
                }
            });
        }
    }

    public final void addPackageToSeededSet(SharedPreferences sharedPreferences, String str) {
        Set<String> stringSet = sharedPreferences.getStringSet("SeedingCompleted", SetsKt__SetsKt.emptySet());
        Intrinsics.checkNotNullExpressionValue(stringSet, "seededPackages");
        Set<String> set = CollectionsKt___CollectionsKt.toMutableSet(stringSet);
        set.add(str);
        sharedPreferences.edit().putStringSet("SeedingCompleted", set).apply();
    }
}
