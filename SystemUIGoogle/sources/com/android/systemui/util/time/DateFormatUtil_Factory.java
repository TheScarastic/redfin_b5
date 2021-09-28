package com.android.systemui.util.time;

import android.content.Context;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes2.dex */
public final class DateFormatUtil_Factory implements Factory<DateFormatUtil> {
    private final Provider<Context> contextProvider;

    public DateFormatUtil_Factory(Provider<Context> provider) {
        this.contextProvider = provider;
    }

    @Override // javax.inject.Provider
    public DateFormatUtil get() {
        return newInstance(this.contextProvider.get());
    }

    public static DateFormatUtil_Factory create(Provider<Context> provider) {
        return new DateFormatUtil_Factory(provider);
    }

    public static DateFormatUtil newInstance(Context context) {
        return new DateFormatUtil(context);
    }
}
