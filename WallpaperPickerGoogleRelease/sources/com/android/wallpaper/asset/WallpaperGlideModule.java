package com.android.wallpaper.asset;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.wallpaper.asset.CurrentWallpaperAssetVNLoader;
import com.android.wallpaper.asset.LiveWallpaperThumbAssetLoader;
import com.android.wallpaper.asset.ResourceAssetLoader;
import com.android.wallpaper.asset.SystemStaticAssetLoader;
import com.android.wallpaper.asset.WallpaperModelLoader;
import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.load.DecodeFormat;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.resource.bitmap.Downsampler;
import com.bumptech.glide.load.resource.gif.GifOptions;
import com.bumptech.glide.module.GlideModule;
import com.bumptech.glide.request.RequestOptions;
import java.io.InputStream;
/* loaded from: classes.dex */
public class WallpaperGlideModule implements GlideModule {
    @Override // com.bumptech.glide.module.AppliesOptions
    public void applyOptions(Context context, GlideBuilder glideBuilder) {
        glideBuilder.diskCacheFactory = new InternalCacheDiskCacheFactory(context, 104857600);
        MemorySizeCalculator.Builder builder = new MemorySizeCalculator.Builder(context);
        builder.bitmapPoolScreens = 2.0f;
        builder.memoryCacheScreens = 1.2f;
        glideBuilder.memorySizeCalculator = new MemorySizeCalculator(builder);
        RequestOptions requestOptions = new RequestOptions();
        DecodeFormat decodeFormat = DecodeFormat.PREFER_ARGB_8888;
        glideBuilder.defaultRequestOptions = requestOptions.set(Downsampler.DECODE_FORMAT, decodeFormat).set(GifOptions.DECODE_FORMAT, decodeFormat);
    }

    @Override // com.bumptech.glide.module.RegistersComponents
    public void registerComponents(Context context, Glide glide, Registry registry) {
        registry.append(WallpaperModel.class, Drawable.class, new WallpaperModelLoader.WallpaperModelLoaderFactory());
        registry.append(ResourceAsset.class, InputStream.class, new ResourceAssetLoader.ResourceAssetLoaderFactory());
        registry.append(SystemStaticAsset.class, InputStream.class, new SystemStaticAssetLoader.SystemStaticAssetLoaderFactory());
        registry.append(LiveWallpaperThumbAsset.class, Drawable.class, new LiveWallpaperThumbAssetLoader.LiveWallpaperThumbAssetLoaderFactory());
        registry.append(CurrentWallpaperAssetVN.class, InputStream.class, new CurrentWallpaperAssetVNLoader.CurrentWallpaperAssetVNLoaderFactory());
        registry.append("legacy_append", Drawable.class, Drawable.class, new DrawableResourceDecoder());
    }
}
