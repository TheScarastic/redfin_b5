package com.android.wallpaper.picker.individual;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import androidx.cardview.R$attr;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.R;
import com.android.wallpaper.asset.Asset;
import com.android.wallpaper.asset.ContentUriAsset;
import com.android.wallpaper.picker.MyPhotosStarter;
import java.util.Objects;
/* loaded from: classes.dex */
public class MyPhotosViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, MyPhotosStarter.PermissionChangedListener {
    public final Activity mActivity;
    public final MyPhotosStarter mMyPhotosStarter;
    public final ImageView mOverlayIconView;
    public final ImageView mThumbnailView;

    /* loaded from: classes.dex */
    public interface AssetListener {
    }

    public MyPhotosViewHolder(Activity activity, MyPhotosStarter myPhotosStarter, int i, View view) {
        super(view);
        this.mActivity = activity;
        this.mMyPhotosStarter = myPhotosStarter;
        view.getLayoutParams().height = i;
        view.findViewById(R.id.tile).setOnClickListener(this);
        this.mThumbnailView = (ImageView) view.findViewById(R.id.thumbnail);
        this.mOverlayIconView = (ImageView) view.findViewById(R.id.overlay_icon);
    }

    public static boolean isReadExternalStoragePermissionGranted(Context context) {
        return context.getPackageManager().checkPermission("android.permission.READ_EXTERNAL_STORAGE", context.getPackageName()) == 0;
    }

    public void bind() {
        if (isReadExternalStoragePermissionGranted(this.mActivity)) {
            this.mOverlayIconView.setVisibility(8);
            final Activity activity = this.mActivity;
            final AnonymousClass2 r2 = new AssetListener() { // from class: com.android.wallpaper.picker.individual.MyPhotosViewHolder.2
            };
            isReadExternalStoragePermissionGranted(activity);
            new AsyncTask<Void, Void, Asset>() { // from class: com.android.wallpaper.picker.individual.MyPhotosViewHolder.1
                /* Return type fixed from 'java.lang.Object' to match base method */
                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
                @Override // android.os.AsyncTask
                public Asset doInBackground(Void[] voidArr) {
                    Cursor query = activity.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, new String[]{"_id", "datetaken"}, null, null, "datetaken DESC LIMIT 1");
                    ContentUriAsset contentUriAsset = null;
                    if (query != null) {
                        if (query.moveToNext()) {
                            Context context = activity;
                            contentUriAsset = new ContentUriAsset(context, Uri.parse(MediaStore.Images.Media.EXTERNAL_CONTENT_URI + "/" + query.getString(0)), false);
                        }
                        query.close();
                    }
                    return contentUriAsset;
                }

                /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
                @Override // android.os.AsyncTask
                public void onPostExecute(Asset asset) {
                    Asset asset2 = asset;
                    AnonymousClass2 r22 = (AnonymousClass2) r2;
                    Objects.requireNonNull(r22);
                    if (asset2 != null) {
                        MyPhotosViewHolder myPhotosViewHolder = MyPhotosViewHolder.this;
                        Activity activity2 = myPhotosViewHolder.mActivity;
                        asset2.loadDrawable(activity2, myPhotosViewHolder.mThumbnailView, R$attr.getColorAttr(activity2, 16844080));
                    }
                }
            }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
            return;
        }
        this.mOverlayIconView.setVisibility(0);
        this.mOverlayIconView.setImageDrawable(this.mActivity.getDrawable(R.drawable.myphotos_empty_tile_illustration));
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        this.mMyPhotosStarter.requestCustomPhotoPicker(this);
    }

    @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
    public void onPermissionsDenied(boolean z) {
    }

    @Override // com.android.wallpaper.picker.MyPhotosStarter.PermissionChangedListener
    public void onPermissionsGranted() {
        bind();
    }
}
