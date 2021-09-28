package com.android.systemui.screenshot;

import android.content.ContentResolver;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ImageExporter_Factory implements Factory<ImageExporter> {
    private final Provider<ContentResolver> resolverProvider;

    public ImageExporter_Factory(Provider<ContentResolver> provider) {
        this.resolverProvider = provider;
    }

    @Override // javax.inject.Provider
    public ImageExporter get() {
        return newInstance(this.resolverProvider.get());
    }

    public static ImageExporter_Factory create(Provider<ContentResolver> provider) {
        return new ImageExporter_Factory(provider);
    }

    public static ImageExporter newInstance(ContentResolver contentResolver) {
        return new ImageExporter(contentResolver);
    }
}
