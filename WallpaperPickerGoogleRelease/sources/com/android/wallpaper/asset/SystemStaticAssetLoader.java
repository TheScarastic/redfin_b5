package com.android.wallpaper.asset;

import com.android.wallpaper.asset.ResourceAssetLoader;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
/* loaded from: classes.dex */
public class SystemStaticAssetLoader implements ModelLoader<SystemStaticAsset, InputStream> {

    /* loaded from: classes.dex */
    public static class SystemStaticAssetLoaderFactory implements ModelLoaderFactory<SystemStaticAsset, InputStream> {
        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<SystemStaticAsset, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new SystemStaticAssetLoader();
        }
    }

    /* Return type fixed from 'com.bumptech.glide.load.model.ModelLoader$LoadData' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<InputStream> buildLoadData(SystemStaticAsset systemStaticAsset, int i, int i2, Options options) {
        SystemStaticAsset systemStaticAsset2 = systemStaticAsset;
        return new ModelLoader.LoadData<>(systemStaticAsset2.getKey(), new ResourceAssetLoader.ResourceAssetFetcher(systemStaticAsset2));
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public /* bridge */ /* synthetic */ boolean handles(SystemStaticAsset systemStaticAsset) {
        return true;
    }
}
