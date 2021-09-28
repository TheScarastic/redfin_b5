package com.android.systemui.screenshot;

import android.os.Handler;
import dagger.internal.Factory;
import javax.inject.Provider;
/* loaded from: classes.dex */
public final class ImageTileSet_Factory implements Factory<ImageTileSet> {
    private final Provider<Handler> handlerProvider;

    public ImageTileSet_Factory(Provider<Handler> provider) {
        this.handlerProvider = provider;
    }

    @Override // javax.inject.Provider
    public ImageTileSet get() {
        return newInstance(this.handlerProvider.get());
    }

    public static ImageTileSet_Factory create(Provider<Handler> provider) {
        return new ImageTileSet_Factory(provider);
    }

    public static ImageTileSet newInstance(Handler handler) {
        return new ImageTileSet(handler);
    }
}
