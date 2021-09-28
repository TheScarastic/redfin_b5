package com.android.wallpaper.model;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.ContentUriAsset;
/* loaded from: classes.dex */
public class ImageCategory extends Category {
    public ImageCategory(String str, String str2, int i, int i2) {
        super(str, str2, i);
    }

    @Override // com.android.wallpaper.model.Category
    public Drawable getOverlayIcon(Context context) {
        if (getThumbnail(context) == null) {
            return context.getResources().getDrawable(R.drawable.myphotos_empty_tile_illustration);
        }
        return null;
    }

    @Override // com.android.wallpaper.model.Category
    public int getOverlayIconSizeDp() {
        return 128;
    }

    @Override // com.android.wallpaper.model.Category
    public Asset getThumbnail(Context context) {
        ContentUriAsset contentUriAsset = null;
        if (!(context.getPackageManager().checkPermission("android.permission.READ_EXTERNAL_STORAGE", context.getPackageName()) == 0)) {
            return null;
        }
        Cursor query = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "datetaken"}, null, null, "datetaken DESC");
        if (query != null) {
            if (query.moveToNext()) {
                contentUriAsset = new ContentUriAsset(context, Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + query.getString(0)), false);
            }
            query.close();
        }
        return contentUriAsset;
    }

    @Override // com.android.wallpaper.model.Category
    public void show(Activity activity, PickerIntentFactory pickerIntentFactory, int i) {
        Intent intent = new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        activity.startActivityForResult(intent, i);
    }

    @Override // com.android.wallpaper.model.Category
    public boolean supportsCustomPhotos() {
        return true;
    }
}
