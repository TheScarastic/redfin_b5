package com.android.wallpaper.asset;

import android.graphics.drawable.Drawable;
import android.support.media.ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0;
import android.util.Log;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.Options;
import com.bumptech.glide.load.data.DataFetcher;
import com.bumptech.glide.load.model.ModelLoader;
import com.bumptech.glide.load.model.ModelLoaderFactory;
import com.bumptech.glide.load.model.MultiModelLoaderFactory;
import com.bumptech.glide.signature.ObjectKey;
/* loaded from: classes.dex */
public class WallpaperModelLoader implements ModelLoader<WallpaperModel, Drawable> {

    /* loaded from: classes.dex */
    public static class WallpaperFetcher implements DataFetcher<Drawable> {
        public int mHeight;
        public WallpaperModel mWallpaperModel;
        public int mWidth;

        public WallpaperFetcher(WallpaperModel wallpaperModel, int i, int i2) {
            this.mWallpaperModel = wallpaperModel;
            this.mWidth = i;
            this.mHeight = i2;
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
            Drawable drawable;
            WallpaperModel wallpaperModel = this.mWallpaperModel;
            int i = this.mWidth;
            int i2 = this.mHeight;
            if (wallpaperModel.mWallpaperSource != 0) {
                StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid wallpaper data source: ");
                m.append(wallpaperModel.mWallpaperSource);
                Log.e("WallpaperModel", m.toString());
                drawable = null;
            } else {
                drawable = wallpaperModel.mWallpaperManager.getBuiltInDrawable(i, i2, true, 0.5f, 0.5f);
            }
            dataCallback.onDataReady(drawable);
        }
    }

    /* loaded from: classes.dex */
    public static class WallpaperModelLoaderFactory implements ModelLoaderFactory<WallpaperModel, Drawable> {
        @Override // com.bumptech.glide.load.model.ModelLoaderFactory
        public ModelLoader<WallpaperModel, Drawable> build(MultiModelLoaderFactory multiModelLoaderFactory) {
            return new WallpaperModelLoader();
        }
    }

    /* Return type fixed from 'com.bumptech.glide.load.model.ModelLoader$LoadData' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, int, int, com.bumptech.glide.load.Options] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public ModelLoader.LoadData<Drawable> buildLoadData(WallpaperModel wallpaperModel, int i, int i2, Options options) {
        WallpaperModel wallpaperModel2 = wallpaperModel;
        if (wallpaperModel2.mWallpaperSource != 0) {
            StringBuilder m = ExifInterface$ByteOrderedDataInputStream$$ExternalSyntheticOutline0.m("Invalid wallpaper data source: ");
            m.append(wallpaperModel2.mWallpaperSource);
            Log.e("WallpaperModel", m.toString());
        }
        return new ModelLoader.LoadData<>(new ObjectKey(wallpaperModel2), new WallpaperFetcher(wallpaperModel2, i, i2));
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
    @Override // com.bumptech.glide.load.model.ModelLoader
    public /* bridge */ /* synthetic */ boolean handles(WallpaperModel wallpaperModel) {
        return true;
    }
}
