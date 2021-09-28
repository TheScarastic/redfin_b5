package com.android.wallpaper.asset;

import android.graphics.drawable.Drawable;
import com.android.wallpaper.asset.LiveWallpaperThumbAsset;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
/* loaded from: classes.dex */
public class LiveWallpaperThumbAssetLoader implements ModelLoader<LiveWallpaperThumbAsset, Drawable> {

    /* loaded from: classes.dex */
    public static class LiveWallpaperThumbAssetLoaderFactory implements ModelLoaderFactory<LiveWallpaperThumbAsset, Drawable> {
        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<LiveWallpaperThumbAsset, Drawable> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new LiveWallpaperThumbAssetLoader();
        }
    }

    /* loaded from: classes.dex */
    public static class LiveWallpaperThumbFetcher implements DataFetcher<Drawable> {
        public LiveWallpaperThumbAsset mLiveWallpaperThumbAsset;

        public LiveWallpaperThumbFetcher(LiveWallpaperThumbAsset liveWallpaperThumbAsset) {
            this.mLiveWallpaperThumbAsset = liveWallpaperThumbAsset;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cancel() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void cleanup() {
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public Class<Drawable> getDataClass() {
            return Drawable.class;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public DataSource getDataSource() {
            return DataSource.LOCAL;
        }

        @Override // com.bumptech.glide.load.data.DataFetcher
        public void loadData(Priority priority, DataFetcher.DataCallback<? super Drawable> dataCallback) {
            dataCallback.onDataReady(this.mLiveWallpaperThumbAsset.getThumbnailDrawable());
        }
    }

    /* Return type fixed from 'com.bumptech.glide.load.model.ModelLoader$LoadData' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<Drawable> buildLoadData(LiveWallpaperThumbAsset liveWallpaperThumbAsset, int i, int i2, Options options) {
        LiveWallpaperThumbAsset liveWallpaperThumbAsset2 = liveWallpaperThumbAsset;
        return new ModelLoader.LoadData<>(new LiveWallpaperThumbAsset.LiveWallpaperThumbKey(liveWallpaperThumbAsset2.mInfo), new LiveWallpaperThumbFetcher(liveWallpaperThumbAsset2));
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public /* bridge */ /* synthetic */ boolean handles(LiveWallpaperThumbAsset liveWallpaperThumbAsset) {
        return true;
    }
}
