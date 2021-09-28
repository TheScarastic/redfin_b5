package com.android.systemui.doze;

import com.android.systemui.classifier.FalsingCollector;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class DozeFalsingManagerAdapter_Factory implements Factory<DozeFalsingManagerAdapter> {
    private final Provider<FalsingCollector> falsingCollectorProvider;

    public DozeFalsingManagerAdapter_Factory(Provider<FalsingCollector> provider) {
        this.falsingCollectorProvider = provider;
    }

    @Override // javax.inject.Provider
    public DozeFalsingManagerAdapter get() {
        return newInstance(this.falsingCollectorProvider.get());
    }

    public static DozeFalsingManagerAdapter_Factory create(Provider<FalsingCollector> provider) {
        return new DozeFalsingManagerAdapter_Factory(provider);
    }

    public static DozeFalsingManagerAdapter newInstance(FalsingCollector falsingCollector) {
        return new DozeFalsingManagerAdapter(falsingCollector);
    }
}
