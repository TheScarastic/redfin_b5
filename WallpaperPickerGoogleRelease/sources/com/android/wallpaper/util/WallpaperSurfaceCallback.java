package com.android.wallpaper.util;

import android.content.Context;
import android.view.Surface;
import android.view.SurfaceControlViewHost;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.ImageView;
import com.android.wallpaper.module.DefaultPackageStatusNotifier;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.PackageStatusNotifier;
import com.android.wallpaper.widget.PreviewPager$$ExternalSyntheticLambda1;
import java.util.concurrent.Future;
/* loaded from: classes.dex */
public class WallpaperSurfaceCallback implements SurfaceHolder.Callback {
    public PackageStatusNotifier.Listener mAppStatusListener;
    public final View mContainerView;
    public final Context mContext;
    public ImageView mHomeImageWallpaper;
    public SurfaceControlViewHost mHost;
    public Surface mLastSurface;
    public final SurfaceListener mListener;
    public PackageStatusNotifier mPackageStatusNotifier;
    public final Future<Integer> mPlaceholderColor;
    public boolean mSurfaceCreated;
    public final SurfaceView mWallpaperSurface;

    /* loaded from: classes.dex */
    public interface SurfaceListener {
        void onSurfaceCreated();
    }

    public WallpaperSurfaceCallback(Context context, View view, SurfaceView surfaceView, Future<Integer> future, SurfaceListener surfaceListener) {
        this.mContext = context.getApplicationContext();
        this.mContainerView = view;
        this.mWallpaperSurface = surfaceView;
        this.mListener = surfaceListener;
        PackageStatusNotifier packageStatusNotifier = InjectorProvider.getInjector().getPackageStatusNotifier(context);
        this.mPackageStatusNotifier = packageStatusNotifier;
        PreviewPager$$ExternalSyntheticLambda1 previewPager$$ExternalSyntheticLambda1 = new PreviewPager$$ExternalSyntheticLambda1(this);
        this.mAppStatusListener = previewPager$$ExternalSyntheticLambda1;
        ((DefaultPackageStatusNotifier) packageStatusNotifier).addListener(previewPager$$ExternalSyntheticLambda1, "android.service.wallpaper.WallpaperService");
        this.mPlaceholderColor = future;
    }

    public void cleanUp() {
        SurfaceControlViewHost surfaceControlViewHost = this.mHost;
        if (surfaceControlViewHost != null) {
            surfaceControlViewHost.release();
            this.mHost = null;
        }
        ImageView imageView = this.mHomeImageWallpaper;
        if (imageView != null) {
            imageView.setImageDrawable(null);
        }
        ((DefaultPackageStatusNotifier) this.mPackageStatusNotifier).removeListener(this.mAppStatusListener);
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0022  */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x0027  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x0062  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final void setupSurfaceWallpaper(boolean r6) {
        /*
            r5 = this;
            android.widget.ImageView r0 = new android.widget.ImageView
            android.content.Context r1 = r5.mContext
            r0.<init>(r1)
            r5.mHomeImageWallpaper = r0
            java.util.concurrent.Future<java.lang.Integer> r0 = r5.mPlaceholderColor
            r1 = 0
            if (r0 == 0) goto L_0x001d
            boolean r0 = r0.isDone()
            if (r0 == 0) goto L_0x001d
            java.util.concurrent.Future<java.lang.Integer> r0 = r5.mPlaceholderColor     // Catch: InterruptedException | ExecutionException -> 0x001d
            java.lang.Object r0 = r0.get()     // Catch: InterruptedException | ExecutionException -> 0x001d
            java.lang.Integer r0 = (java.lang.Integer) r0     // Catch: InterruptedException | ExecutionException -> 0x001d
            goto L_0x001e
        L_0x001d:
            r0 = r1
        L_0x001e:
            android.widget.ImageView r2 = r5.mHomeImageWallpaper
            if (r0 == 0) goto L_0x0027
            int r0 = r0.intValue()
            goto L_0x0030
        L_0x0027:
            android.content.Context r0 = r5.mContext
            r3 = 16844080(0x1010530, float:2.369728E-38)
            int r0 = androidx.cardview.R$attr.getColorAttr(r0, r3)
        L_0x0030:
            r2.setBackgroundColor(r0)
            android.widget.ImageView r0 = r5.mHomeImageWallpaper
            android.view.View r2 = r5.mContainerView
            int r2 = r2.getWidth()
            r3 = 1073741824(0x40000000, float:2.0)
            int r2 = android.view.View.MeasureSpec.makeMeasureSpec(r2, r3)
            android.view.View r4 = r5.mContainerView
            int r4 = r4.getHeight()
            int r3 = android.view.View.MeasureSpec.makeMeasureSpec(r4, r3)
            r0.measure(r2, r3)
            android.widget.ImageView r0 = r5.mHomeImageWallpaper
            android.view.View r2 = r5.mContainerView
            int r2 = r2.getWidth()
            android.view.View r3 = r5.mContainerView
            int r3 = r3.getHeight()
            r4 = 0
            r0.layout(r4, r4, r2, r3)
            if (r6 == 0) goto L_0x0080
            android.view.SurfaceControlViewHost r6 = r5.mHost
            if (r6 == 0) goto L_0x006b
            r6.release()
            r5.mHost = r1
        L_0x006b:
            android.view.SurfaceControlViewHost r6 = new android.view.SurfaceControlViewHost
            android.content.Context r0 = r5.mContext
            android.view.View r1 = r5.mContainerView
            android.view.Display r1 = r1.getDisplay()
            android.view.SurfaceView r2 = r5.mWallpaperSurface
            android.os.IBinder r2 = r2.getHostToken()
            r6.<init>(r0, r1, r2)
            r5.mHost = r6
        L_0x0080:
            android.view.SurfaceControlViewHost r6 = r5.mHost
            android.widget.ImageView r0 = r5.mHomeImageWallpaper
            int r1 = r0.getWidth()
            android.widget.ImageView r2 = r5.mHomeImageWallpaper
            int r2 = r2.getHeight()
            r6.setView(r0, r1, r2)
            android.view.SurfaceView r6 = r5.mWallpaperSurface
            android.view.SurfaceControlViewHost r5 = r5.mHost
            android.view.SurfaceControlViewHost$SurfacePackage r5 = r5.getSurfacePackage()
            r6.setChildSurfacePackage(r5)
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.wallpaper.util.WallpaperSurfaceCallback.setupSurfaceWallpaper(boolean):void");
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        if (this.mLastSurface != surfaceHolder.getSurface()) {
            this.mLastSurface = surfaceHolder.getSurface();
            setupSurfaceWallpaper(true);
        }
        SurfaceListener surfaceListener = this.mListener;
        if (surfaceListener != null) {
            surfaceListener.onSurfaceCreated();
        }
        this.mSurfaceCreated = true;
    }

    @Override // android.view.SurfaceHolder.Callback
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        this.mSurfaceCreated = false;
    }

    public WallpaperSurfaceCallback(Context context, View view, SurfaceView surfaceView, SurfaceListener surfaceListener) {
        this(context, view, surfaceView, null, surfaceListener);
    }
}
