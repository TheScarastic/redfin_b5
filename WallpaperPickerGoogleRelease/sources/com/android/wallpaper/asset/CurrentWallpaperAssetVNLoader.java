package com.android.wallpaper.asset;

import android.os.ParcelFileDescriptor;
import com.android.wallpaper.asset.CurrentWallpaperAssetVN;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import java.io.InputStream;
/* loaded from: classes.dex */
public class CurrentWallpaperAssetVNLoader implements ModelLoader<CurrentWallpaperAssetVN, InputStream> {

    /* loaded from: classes.dex */
    public static class CurrentWallpaperAssetVNDataFetcher implements DataFetcher<InputStream> {
        public CurrentWallpaperAssetVN mAsset;

        public CurrentWallpaperAssetVNDataFetcher(CurrentWallpaperAssetVN currentWallpaperAssetVN) {
            this.mAsset = currentWallpaperAssetVN;
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
            CurrentWallpaperAssetVN currentWallpaperAssetVN = this.mAsset;
            ParcelFileDescriptor wallpaperFile = currentWallpaperAssetVN.mWallpaperManagerCompat.getWallpaperFile(currentWallpaperAssetVN.mWallpaperManagerFlag);
            if (wallpaperFile == null) {
                dataCallback.onLoadFailed(new Exception("ParcelFileDescriptor for wallpaper is null, unable to open InputStream."));
            } else {
                dataCallback.onDataReady(new ParcelFileDescriptor.AutoCloseInputStream(wallpaperFile));
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CurrentWallpaperAssetVNLoaderFactory implements ModelLoaderFactory<CurrentWallpaperAssetVN, InputStream> {
        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<CurrentWallpaperAssetVN, InputStream> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new CurrentWallpaperAssetVNLoader();
        }
    }

    /* Return type fixed from 'com.bumptech.glide.load.model.ModelLoader$LoadData' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<InputStream> buildLoadData(CurrentWallpaperAssetVN currentWallpaperAssetVN, int i, int i2, Options options) {
        CurrentWallpaperAssetVN currentWallpaperAssetVN2 = currentWallpaperAssetVN;
        return new ModelLoader.LoadData<>(new CurrentWallpaperAssetVN.CurrentWallpaperVNKey(currentWallpaperAssetVN2.mWallpaperManager, currentWallpaperAssetVN2.mWallpaperManagerFlag), new CurrentWallpaperAssetVNDataFetcher(currentWallpaperAssetVN2));
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public /* bridge */ /* synthetic */ boolean handles(CurrentWallpaperAssetVN currentWallpaperAssetVN) {
        return true;
    }
}
