package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.ContentUriAsset;
import com.android.wallpaper.asset.ExifInterfaceCompat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/* loaded from: classes.dex */
public class ImageWallpaperInfo extends WallpaperInfo {
    public static final Parcelable.Creator<ImageWallpaperInfo> CREATOR = new Parcelable.Creator<ImageWallpaperInfo>() { // from class: com.android.wallpaper.model.ImageWallpaperInfo.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public ImageWallpaperInfo createFromParcel(Parcel parcel) {
            return new ImageWallpaperInfo(parcel);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public ImageWallpaperInfo[] newArray(int i) {
            return new ImageWallpaperInfo[i];
        }
    };
    public static final String[] EXIF_TAGS = {"ImageDescription", "Artist", "DateTimeOriginal", "Model"};
    public ContentUriAsset mAsset;
    public boolean mIsAssetUncached;
    public Uri mUri;

    public ImageWallpaperInfo(Uri uri) {
        this.mUri = uri;
    }

    public static List<String> getGenericAttributions(Context context) {
        return Arrays.asList(context.getResources().getString(R.string.my_photos_generic_wallpaper_title));
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getAsset(Context context) {
        if (this.mIsAssetUncached) {
            this.mAsset = new ContentUriAsset(context, this.mUri, true);
        } else if (this.mAsset == null) {
            this.mAsset = new ContentUriAsset(context, this.mUri, false);
        }
        return this.mAsset;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public List<String> getAttributions(Context context) {
        String str;
        ContentUriAsset contentUriAsset = (ContentUriAsset) getAsset(context);
        if (!contentUriAsset.isJpeg()) {
            return getGenericAttributions(context);
        }
        ArrayList arrayList = new ArrayList();
        String[] strArr = EXIF_TAGS;
        for (String str2 : strArr) {
            contentUriAsset.ensureExifInterface();
            ExifInterfaceCompat exifInterfaceCompat = contentUriAsset.mExifCompat;
            String str3 = null;
            if (exifInterfaceCompat == null) {
                Log.w("ContentUriAsset", "Unable to read EXIF tags for content URI asset");
            } else {
                ExifInterface exifInterface = exifInterfaceCompat.mFrameworkExifInterface;
                if (exifInterface != null) {
                    str = exifInterface.getAttribute(str2);
                } else {
                    str = exifInterfaceCompat.mSupportExifInterface.getAttribute(str2);
                }
                if (str != null && !str.trim().isEmpty()) {
                    str3 = str.trim();
                }
            }
            if (str3 != null) {
                if (str2 == "DateTimeOriginal") {
                    try {
                        str3 = SimpleDateFormat.getDateInstance().format(new SimpleDateFormat("yyyy:MM:dd HH:mm:ss").parse(str3));
                    } catch (ParseException e) {
                        Log.w("ImageWallpaperInfo", "Unable to parse image datetime", e);
                    }
                }
                arrayList.add(str3);
            }
        }
        if (!arrayList.isEmpty()) {
            return arrayList;
        }
        return getGenericAttributions(context);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getCollectionId(Context context) {
        return context.getString(R.string.image_wallpaper_collection_id);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Asset getThumbAsset(Context context) {
        return getAsset(context);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public String getTitle(Context context) {
        return null;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public Uri getUri() {
        return this.mUri;
    }

    @Override // com.android.wallpaper.model.WallpaperInfo
    public void showPreview(Activity activity, InlinePreviewIntentFactory inlinePreviewIntentFactory, int i) {
        activity.startActivityForResult(inlinePreviewIntentFactory.newIntent(activity, this), i);
    }

    @Override // com.android.wallpaper.model.WallpaperInfo, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mPlaceholderColor);
        parcel.writeString(this.mUri.toString());
    }

    public ImageWallpaperInfo(Uri uri, boolean z) {
        this.mUri = uri;
        this.mIsAssetUncached = z;
    }

    public ImageWallpaperInfo(Parcel parcel) {
        super(parcel);
        this.mUri = Uri.parse(parcel.readString());
    }
}
