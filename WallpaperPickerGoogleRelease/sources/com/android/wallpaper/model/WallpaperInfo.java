package com.android.wallpaper.model;

import android.app.Activity;
import android.app.WallpaperColors;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public abstract class WallpaperInfo implements Parcelable {
    public static final ExecutorService sExecutor = Executors.newCachedThreadPool();
    public int mPlaceholderColor;

    public WallpaperInfo() {
        this.mPlaceholderColor = 0;
    }

    public Future<Integer> computePlaceholderColor(Context context) {
        int i = this.mPlaceholderColor;
        if (i != 0) {
            return CompletableFuture.completedFuture(Integer.valueOf(i));
        }
        return sExecutor.submit(new Callable(context.getApplicationContext()) { // from class: com.android.wallpaper.model.WallpaperInfo$$ExternalSyntheticLambda0
            public final /* synthetic */ Context f$1;

            {
                this.f$1 = r2;
            }

            @Override // java.util.concurrent.Callable
            public final Object call() {
                int i2;
                WallpaperInfo wallpaperInfo = WallpaperInfo.this;
                Context context2 = this.f$1;
                ExecutorService executorService = WallpaperInfo.sExecutor;
                synchronized (wallpaperInfo) {
                    int i3 = wallpaperInfo.mPlaceholderColor;
                    if (i3 != 0) {
                        i2 = Integer.valueOf(i3);
                    } else {
                        Bitmap lowResBitmap = wallpaperInfo.getThumbAsset(context2).getLowResBitmap(context2);
                        if (lowResBitmap == null) {
                            i2 = 0;
                        } else {
                            int argb = WallpaperColors.fromBitmap(lowResBitmap).getPrimaryColor().toArgb();
                            wallpaperInfo.mPlaceholderColor = argb;
                            i2 = Integer.valueOf(argb);
                        }
                    }
                }
                return i2;
            }
        });
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    public int getActionIconRes(Context context) {
        return R.drawable.ic_explore_24px;
    }

    public int getActionLabelRes(Context context) {
        return R.string.explore;
    }

    public String getActionUrl(Context context) {
        return null;
    }

    public abstract Asset getAsset(Context context);

    public abstract List<String> getAttributions(Context context);

    public int getBackupPermission() {
        return 1;
    }

    public String getBaseImageUrl() {
        return null;
    }

    public abstract String getCollectionId(Context context);

    public Asset getDesktopAsset(Context context) {
        return getAsset(context);
    }

    public abstract Asset getThumbAsset(Context context);

    public String getTitle(Context context) {
        return null;
    }

    public Uri getUri() {
        return null;
    }

    public android.app.WallpaperInfo getWallpaperComponent() {
        return null;
    }

    public String getWallpaperId() {
        return null;
    }

    public abstract void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i);

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
    }

    public WallpaperInfo(Parcel parcel) {
        this.mPlaceholderColor = 0;
        this.mPlaceholderColor = parcel.readInt();
    }
}
