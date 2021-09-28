package com.android.systemui.privacy.television;

import android.content.Context;
import com.android.systemui.privacy.PrivacyItemController;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class TvOngoingPrivacyChip_Factory implements Factory<TvOngoingPrivacyChip> {
    private final Provider<Context> contextProvider;
    private final Provider<PrivacyItemController> privacyItemControllerProvider;

    public TvOngoingPrivacyChip_Factory(Provider<Context> provider, Provider<PrivacyItemController> provider2) {
        this.contextProvider = provider;
        this.privacyItemControllerProvider = provider2;
    }

    @Override // javax.inject.Provider
    public TvOngoingPrivacyChip get() {
        return newInstance(this.contextProvider.get(), this.privacyItemControllerProvider.get());
    }

    public static TvOngoingPrivacyChip_Factory create(Provider<Context> provider, Provider<PrivacyItemController> provider2) {
        return new TvOngoingPrivacyChip_Factory(provider, provider2);
    }

    public static TvOngoingPrivacyChip newInstance(Context context, PrivacyItemController privacyItemController) {
        return new TvOngoingPrivacyChip(context, privacyItemController);
    }
}
