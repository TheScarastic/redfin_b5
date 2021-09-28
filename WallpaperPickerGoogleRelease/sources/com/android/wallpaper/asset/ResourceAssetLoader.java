package com.android.wallpaper.asset;

import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
/* loaded from: classes.dex */
public class ResourceAssetLoader implements ModelLoader<ResourceAsset, InputStream> {

    /* loaded from: classes.dex */
    public static class ResourceAssetFetcher implements DataFetcher<InputStream> {
        public ResourceAsset mResourceAsset;

        public ResourceAssetFetcher(ResourceAsset resourceAsset) {
            this.mResourceAsset = resourceAsset;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cancel() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cleanup() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public Class<InputStream> getDataClass() {
            return InputStream.class;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void loadData(Priority priority, DataFetcher.DataCallback<? super InputStream> dataCallback) {
            ResourceAsset resourceAsset = this.mResourceAsset;
            dataCallback.onDataReady(resourceAsset.mRes.openRawResource(resourceAsset.mResId));
        }
    }

    /* loaded from: classes.dex */
    public static class ResourceAssetLoaderFactory implements ModelLoaderFactory<ResourceAsset, InputStream> {
        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<ResourceAsset, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new ResourceAssetLoader();
        }
    }

    /* Return type fixed from 'com.bumptech.glide.load.model.ModelLoader$LoadData' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<InputStream> buildLoadData(ResourceAsset resourceAsset, int i, int i2, Options options) {
        ResourceAsset resourceAsset2 = resourceAsset;
        return new ModelLoader.LoadData<>(resourceAsset2.getKey(), new ResourceAssetFetcher(resourceAsset2));
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public /* bridge */ /* synthetic */ boolean handles(ResourceAsset resourceAsset) {
        return true;
    }
}
