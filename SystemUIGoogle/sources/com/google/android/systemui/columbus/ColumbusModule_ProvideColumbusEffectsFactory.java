package com.google.android.systemui.columbus;

import com.google.android.systemui.columbus.feedback.FeedbackEffect;
import com.google.android.systemui.columbus.feedback.HapticClick;
import com.google.android.systemui.columbus.feedback.UserActivity;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import java.util.Set;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class ColumbusModule_ProvideColumbusEffectsFactory implements Factory<Set<FeedbackEffect>> {
    private final Provider<HapticClick> hapticClickProvider;
    private final Provider<UserActivity> userActivityProvider;

    public ColumbusModule_ProvideColumbusEffectsFactory(Provider<HapticClick> provider, Provider<UserActivity> provider2) {
        this.hapticClickProvider = provider;
        this.userActivityProvider = provider2;
    }

    @Override // javax.inject.Provider
    public Set<FeedbackEffect> get() {
        return provideColumbusEffects(this.hapticClickProvider.get(), this.userActivityProvider.get());
    }

    public static ColumbusModule_ProvideColumbusEffectsFactory create(Provider<HapticClick> provider, Provider<UserActivity> provider2) {
        return new ColumbusModule_ProvideColumbusEffectsFactory(provider, provider2);
    }

    public static Set<FeedbackEffect> provideColumbusEffects(HapticClick hapticClick, UserActivity userActivity) {
        return (Set) Preconditions.checkNotNullFromProvides(ColumbusModule.provideColumbusEffects(hapticClick, userActivity));
    }
}
