package com.android.systemui.media;

import com.android.systemui.util.concurrency.RepeatableExecutor;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class SeekBarViewModel_Factory implements Factory<SeekBarViewModel> {
    private final Provider<RepeatableExecutor> bgExecutorProvider;

    public SeekBarViewModel_Factory(Provider<RepeatableExecutor> provider) {
        this.bgExecutorProvider = provider;
    }

    @Override // javax.inject.Provider
    public SeekBarViewModel get() {
        return newInstance(this.bgExecutorProvider.get());
    }

    public static SeekBarViewModel_Factory create(Provider<RepeatableExecutor> provider) {
        return new SeekBarViewModel_Factory(provider);
    }

    public static SeekBarViewModel newInstance(RepeatableExecutor repeatableExecutor) {
        return new SeekBarViewModel(repeatableExecutor);
    }
}
