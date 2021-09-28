package com.android.systemui.log.dagger;

import android.content.ContentResolver;
import android.os.Looper;
import com.android.systemui.log.LogcatEchoTracker;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class LogModule_ProvideLogcatEchoTrackerFactory implements Factory<LogcatEchoTracker> {
    private final Provider<ContentResolver> contentResolverProvider;
    private final Provider<Looper> looperProvider;

    public LogModule_ProvideLogcatEchoTrackerFactory(Provider<ContentResolver> provider, Provider<Looper> provider2) {
        this.contentResolverProvider = provider;
        this.looperProvider = provider2;
    }

    @Override // javax.inject.Provider
    public LogcatEchoTracker get() {
        return provideLogcatEchoTracker(this.contentResolverProvider.get(), this.looperProvider.get());
    }

    public static LogModule_ProvideLogcatEchoTrackerFactory create(Provider<ContentResolver> provider, Provider<Looper> provider2) {
        return new LogModule_ProvideLogcatEchoTrackerFactory(provider, provider2);
    }

    public static LogcatEchoTracker provideLogcatEchoTracker(ContentResolver contentResolver, Looper looper) {
        return (LogcatEchoTracker) Preconditions.checkNotNullFromProvides(LogModule.provideLogcatEchoTracker(contentResolver, looper));
    }
}
