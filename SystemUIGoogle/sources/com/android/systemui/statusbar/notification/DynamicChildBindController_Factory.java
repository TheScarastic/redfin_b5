package com.android.systemui.statusbar.notification;

import com.android.systemui.statusbar.notification.row.RowContentBindStage;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DynamicChildBindController_Factory implements Factory<DynamicChildBindController> {
    private final Provider<RowContentBindStage> stageProvider;

    public DynamicChildBindController_Factory(Provider<RowContentBindStage> provider) {
        this.stageProvider = provider;
    }

    @Override // javax.inject.Provider
    public DynamicChildBindController get() {
        return newInstance(this.stageProvider.get());
    }

    public static DynamicChildBindController_Factory create(Provider<RowContentBindStage> provider) {
        return new DynamicChildBindController_Factory(provider);
    }

    public static DynamicChildBindController newInstance(RowContentBindStage rowContentBindStage) {
        return new DynamicChildBindController(rowContentBindStage);
    }
}
