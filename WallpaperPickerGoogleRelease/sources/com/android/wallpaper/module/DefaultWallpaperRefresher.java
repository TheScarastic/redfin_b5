package com.android.wallpaper.module;

import android.annotation.SuppressLint;
import android.app.WallpaperManager;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.util.Log;
import androidx.slice.view.R$id;
import com.android.wallpaper.compat.WallpaperManagerCompatV16;
import com.android.wallpaper.model.WallpaperMetadata;
import com.android.wallpaper.module.WallpaperRefresher;
import java.util.List;
@SuppressLint({"ServiceCast"})
/* loaded from: classes.dex */
public class DefaultWallpaperRefresher implements WallpaperRefresher {
    public final Context mAppContext;
    public final WallpaperManager mWallpaperManager;
    public final WallpaperPreferences mWallpaperPreferences;

    /* loaded from: classes.dex */
    public class GetWallpaperMetadataAsyncTask extends AsyncTask<Void, Void, List<WallpaperMetadata>> {
        public long mCurrentHomeWallpaperHashCode;
        public long mCurrentLockWallpaperHashCode;
        public final WallpaperRefresher.RefreshListener mListener;
        public String mSystemWallpaperPackageName;
        public final WallpaperManagerCompatV16 mWallpaperManagerCompat;

        @SuppressLint({"ServiceCast"})
        public GetWallpaperMetadataAsyncTask(WallpaperRefresher.RefreshListener refreshListener) {
            this.mListener = refreshListener;
            this.mWallpaperManagerCompat = InjectorProvider.getInjector().getWallpaperManagerCompat(DefaultWallpaperRefresher.this.mAppContext);
        }

        /* Return type fixed from 'java.lang.Object' to match base method */
        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object[]] */
        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:90:0x014b */
        /* JADX DEBUG: Failed to insert an additional move for type inference into block B:51:0x0165 */
        /* JADX WARN: Multi-variable type inference failed */
        /* JADX WARN: Type inference failed for: r2v11 */
        /* JADX WARN: Type inference failed for: r2v26 */
        /* JADX WARN: Type inference failed for: r2v27 */
        /* JADX WARN: Type inference failed for: r2v29 */
        /* JADX WARN: Type inference failed for: r2v30 */
        /* JADX WARN: Type inference failed for: r2v31 */
        /* JADX WARN: Type inference failed for: r2v38, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r12v1, types: [java.io.InputStream] */
        /* JADX WARN: Type inference failed for: r2v39, types: [java.lang.String] */
        /* JADX WARN: Type inference failed for: r2v40 */
        /* JADX WARN: Type inference failed for: r12v2 */
        /* JADX WARN: Type inference failed for: r2v41, types: [long] */
        /* JADX WARN: Type inference failed for: r2v70 */
        /* JADX WARN: Type inference failed for: r2v71 */
        /* JADX WARN: Type inference failed for: r2v73 */
        /* JADX WARNING: Code restructure failed: missing block: B:25:0x0087, code lost:
            if ((r2.get(0) == null && r2.get(1) == null && r2.get(2) == null) != false) goto L_0x0089;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:40:0x012c, code lost:
            if (r20.this$0.mWallpaperPreferences.getLockWallpaperId() == r20.mWallpaperManagerCompat.getWallpaperId(2)) goto L_0x0194;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:67:0x0192, code lost:
            if (r9 == r20.mCurrentLockWallpaperHashCode) goto L_0x0194;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:68:0x0194, code lost:
            r2 = true;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:69:0x0196, code lost:
            r2 = false;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:79:0x01b6, code lost:
            if ((r2.get(0) == null && r2.get(1) == null && r2.get(2) == null) != false) goto L_0x01b8;
         */
        /* JADX WARNING: Unknown variable types count: 2 */
        @Override // android.os.AsyncTask
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public java.util.List<com.android.wallpaper.model.WallpaperMetadata> doInBackground(java.lang.Void[] r21) {
            /*
            // Method dump skipped, instructions count: 676
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.module.DefaultWallpaperRefresher.GetWallpaperMetadataAsyncTask.doInBackground(java.lang.Object[]):java.lang.Object");
        }

        public final long getCurrentHomeWallpaperHashCode() {
            if (this.mCurrentHomeWallpaperHashCode == 0) {
                this.mCurrentHomeWallpaperHashCode = R$id.generateHashCode(((BitmapDrawable) this.mWallpaperManagerCompat.getDrawable()).getBitmap());
                DefaultWallpaperRefresher.this.mWallpaperManager.forgetLoadedWallpaper();
            }
            return this.mCurrentHomeWallpaperHashCode;
        }

        /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object] */
        @Override // android.os.AsyncTask
        public void onPostExecute(List<WallpaperMetadata> list) {
            List<WallpaperMetadata> list2 = list;
            if (list2.size() > 2) {
                Log.e("DefaultWPRefresher", "Got more than 2 WallpaperMetadata objects - only home and (optionally) lock are permitted.");
            } else {
                this.mListener.onRefreshed(list2.get(0), list2.size() > 1 ? list2.get(1) : null, DefaultWallpaperRefresher.this.mWallpaperPreferences.getWallpaperPresentationMode());
            }
        }
    }

    public DefaultWallpaperRefresher(Context context) {
        Context applicationContext = context.getApplicationContext();
        this.mAppContext = applicationContext;
        this.mWallpaperPreferences = InjectorProvider.getInjector().getPreferences(applicationContext);
        this.mWallpaperManager = (WallpaperManager) context.getSystemService("wallpaper");
    }

    public void refresh(WallpaperRefresher.RefreshListener refreshListener) {
        new GetWallpaperMetadataAsyncTask(refreshListener).execute(new Void[0]);
    }
}
