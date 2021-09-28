package com.android.wallpaper.util;

import android.app.WallpaperColors;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.IBinder;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.service.wallpaper.IWallpaperConnection;
import android.service.wallpaper.IWallpaperEngine;
import android.service.wallpaper.IWallpaperService;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceControl;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import com.android.wallpaper.util.WallpaperConnection;
/* loaded from: classes.dex */
public class WallpaperConnection extends IWallpaperConnection.Stub implements ServiceConnection {
    public boolean mConnected;
    public final SurfaceView mContainerView;
    public final Context mContext;
    public IWallpaperEngine mEngine;
    public boolean mEngineReady;
    public final Intent mIntent;
    public boolean mIsEngineVisible;
    public boolean mIsVisible;
    public final WallpaperConnectionListener mListener;
    public final SurfaceView mSecondContainerView;
    public IWallpaperService mService;

    /* loaded from: classes.dex */
    public interface WallpaperConnectionListener {
        default void onConnected() {
        }

        default void onDisconnected() {
        }

        default void onEngineShown() {
        }

        default void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i) {
        }
    }

    public WallpaperConnection(Intent intent, Context context, WallpaperConnectionListener wallpaperConnectionListener, SurfaceView surfaceView) {
        this.mContext = context.getApplicationContext();
        this.mIntent = intent;
        this.mListener = wallpaperConnectionListener;
        this.mContainerView = surfaceView;
        this.mSecondContainerView = null;
    }

    public static boolean isPreviewAvailable() {
        try {
            return IWallpaperEngine.class.getMethod("mirrorSurfaceControl", new Class[0]) != null;
        } catch (NoSuchMethodException | SecurityException unused) {
            return false;
        }
    }

    public void attachEngine(IWallpaperEngine iWallpaperEngine, int i) {
        synchronized (this) {
            if (this.mConnected) {
                this.mEngine = iWallpaperEngine;
                if (this.mIsVisible) {
                    setEngineVisibility(true);
                }
                try {
                    this.mEngine.requestWallpaperColors();
                } catch (RemoteException e) {
                    Log.w("WallpaperConnection", "Failed requesting wallpaper colors", e);
                }
            } else {
                try {
                    iWallpaperEngine.destroy();
                } catch (RemoteException unused) {
                }
            }
        }
    }

    public boolean connect() {
        synchronized (this) {
            if (this.mConnected) {
                return true;
            }
            if (!this.mContext.bindService(this.mIntent, this, 65)) {
                return false;
            }
            this.mConnected = true;
            WallpaperConnectionListener wallpaperConnectionListener = this.mListener;
            if (wallpaperConnectionListener != null) {
                wallpaperConnectionListener.onConnected();
            }
            return true;
        }
    }

    public void disconnect() {
        synchronized (this) {
            this.mConnected = false;
            IWallpaperEngine iWallpaperEngine = this.mEngine;
            if (iWallpaperEngine != null) {
                try {
                    iWallpaperEngine.destroy();
                } catch (RemoteException unused) {
                }
                this.mEngine = null;
            }
            try {
                this.mContext.unbindService(this);
            } catch (IllegalArgumentException unused2) {
                Log.i("WallpaperConnection", "Can't unbind wallpaper service. It might have crashed, just ignoring.");
            }
            this.mService = null;
        }
        WallpaperConnectionListener wallpaperConnectionListener = this.mListener;
        if (wallpaperConnectionListener != null) {
            wallpaperConnectionListener.onDisconnected();
        }
    }

    public void engineShown(IWallpaperEngine iWallpaperEngine) {
        this.mEngineReady = true;
        SurfaceView surfaceView = this.mContainerView;
        if (surfaceView != null) {
            surfaceView.post(new Runnable(this, 0) { // from class: com.android.wallpaper.util.WallpaperConnection$$ExternalSyntheticLambda0
                public final /* synthetic */ int $r8$classId;
                public final /* synthetic */ WallpaperConnection f$0;

                {
                    this.$r8$classId = r3;
                    this.f$0 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    switch (this.$r8$classId) {
                        case 0:
                            WallpaperConnection wallpaperConnection = this.f$0;
                            wallpaperConnection.reparentWallpaperSurface(wallpaperConnection.mContainerView);
                            return;
                        case 1:
                            WallpaperConnection wallpaperConnection2 = this.f$0;
                            wallpaperConnection2.reparentWallpaperSurface(wallpaperConnection2.mSecondContainerView);
                            return;
                        default:
                            WallpaperConnection.WallpaperConnectionListener wallpaperConnectionListener = this.f$0.mListener;
                            if (wallpaperConnectionListener != null) {
                                wallpaperConnectionListener.onEngineShown();
                                return;
                            }
                            return;
                    }
                }
            });
        }
        SurfaceView surfaceView2 = this.mSecondContainerView;
        if (surfaceView2 != null) {
            surfaceView2.post(new Runnable(this, 1) { // from class: com.android.wallpaper.util.WallpaperConnection$$ExternalSyntheticLambda0
                public final /* synthetic */ int $r8$classId;
                public final /* synthetic */ WallpaperConnection f$0;

                {
                    this.$r8$classId = r3;
                    this.f$0 = r2;
                }

                @Override // java.lang.Runnable
                public final void run() {
                    switch (this.$r8$classId) {
                        case 0:
                            WallpaperConnection wallpaperConnection = this.f$0;
                            wallpaperConnection.reparentWallpaperSurface(wallpaperConnection.mContainerView);
                            return;
                        case 1:
                            WallpaperConnection wallpaperConnection2 = this.f$0;
                            wallpaperConnection2.reparentWallpaperSurface(wallpaperConnection2.mSecondContainerView);
                            return;
                        default:
                            WallpaperConnection.WallpaperConnectionListener wallpaperConnectionListener = this.f$0.mListener;
                            if (wallpaperConnectionListener != null) {
                                wallpaperConnectionListener.onEngineShown();
                                return;
                            }
                            return;
                    }
                }
            });
        }
        this.mContainerView.post(new Runnable(this, 2) { // from class: com.android.wallpaper.util.WallpaperConnection$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ WallpaperConnection f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // java.lang.Runnable
            public final void run() {
                switch (this.$r8$classId) {
                    case 0:
                        WallpaperConnection wallpaperConnection = this.f$0;
                        wallpaperConnection.reparentWallpaperSurface(wallpaperConnection.mContainerView);
                        return;
                    case 1:
                        WallpaperConnection wallpaperConnection2 = this.f$0;
                        wallpaperConnection2.reparentWallpaperSurface(wallpaperConnection2.mSecondContainerView);
                        return;
                    default:
                        WallpaperConnection.WallpaperConnectionListener wallpaperConnectionListener = this.f$0.mListener;
                        if (wallpaperConnectionListener != null) {
                            wallpaperConnectionListener.onEngineShown();
                            return;
                        }
                        return;
                }
            }
        });
    }

    public final float[] getScale(SurfaceView surfaceView) {
        Matrix matrix = new Matrix();
        float[] fArr = new float[9];
        Rect surfaceFrame = surfaceView.getHolder().getSurfaceFrame();
        DisplayMetrics displayMetrics = DisplayMetricsRetriever.getInstance().getDisplayMetrics(this.mContainerView.getResources(), this.mContainerView.getDisplay());
        matrix.postScale(((float) surfaceFrame.width()) / ((float) displayMetrics.widthPixels), ((float) surfaceFrame.height()) / ((float) displayMetrics.heightPixels));
        matrix.getValues(fArr);
        return fArr;
    }

    public final void mirrorAndReparent(SurfaceView surfaceView) {
        if (this.mEngine == null) {
            Log.i("WallpaperConnection", "Engine is null, was the service disconnected?");
            return;
        }
        try {
            SurfaceControl surfaceControl = surfaceView.getSurfaceControl();
            SurfaceControl mirrorSurfaceControl = this.mEngine.mirrorSurfaceControl();
            if (mirrorSurfaceControl != null) {
                float[] scale = getScale(surfaceView);
                SurfaceControl.Transaction transaction = new SurfaceControl.Transaction();
                transaction.setMatrix(mirrorSurfaceControl, scale[0], scale[3], scale[1], scale[4]);
                transaction.reparent(mirrorSurfaceControl, surfaceControl);
                transaction.show(mirrorSurfaceControl);
                transaction.apply();
            }
        } catch (RemoteException e) {
            Log.e("WallpaperConnection", "Couldn't reparent wallpaper surface", e);
        }
    }

    public void onLocalWallpaperColorsChanged(RectF rectF, WallpaperColors wallpaperColors, int i) {
    }

    @Override // android.content.ServiceConnection
    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        this.mService = IWallpaperService.Stub.asInterface(iBinder);
        try {
            this.mService.attach(this, this.mContainerView.getWindowToken(), 1001, true, this.mContainerView.getWidth(), this.mContainerView.getHeight(), new Rect(0, 0, 0, 0), this.mContainerView.getDisplay().getDisplayId());
        } catch (RemoteException e) {
            Log.w("WallpaperConnection", "Failed attaching wallpaper; clearing", e);
        }
    }

    @Override // android.content.ServiceConnection
    public void onServiceDisconnected(ComponentName componentName) {
        this.mService = null;
        this.mEngine = null;
        Log.w("WallpaperConnection", "Wallpaper service gone: " + componentName);
    }

    public void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i) {
        this.mContainerView.post(new WallpaperConnection$$ExternalSyntheticLambda1(this, wallpaperColors, i));
    }

    public final void reparentWallpaperSurface(final SurfaceView surfaceView) {
        if (this.mEngine == null) {
            Log.i("WallpaperConnection", "Engine is null, was the service disconnected?");
        } else if (surfaceView.getSurfaceControl() != null) {
            mirrorAndReparent(surfaceView);
        } else {
            Log.d("WallpaperConnection", "SurfaceView not initialized yet, adding callback");
            surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() { // from class: com.android.wallpaper.util.WallpaperConnection.1
                @Override // android.view.SurfaceHolder.Callback
                public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i2, int i3) {
                }

                @Override // android.view.SurfaceHolder.Callback
                public void surfaceCreated(SurfaceHolder surfaceHolder) {
                    WallpaperConnection.this.mirrorAndReparent(surfaceView);
                    surfaceView.getHolder().removeCallback(this);
                }

                @Override // android.view.SurfaceHolder.Callback
                public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
                }
            });
        }
    }

    public final void setEngineVisibility(boolean z) {
        IWallpaperEngine iWallpaperEngine = this.mEngine;
        if (iWallpaperEngine != null && z != this.mIsEngineVisible) {
            try {
                iWallpaperEngine.setVisibility(z);
                this.mIsEngineVisible = z;
            } catch (RemoteException e) {
                Log.w("WallpaperConnection", "Failure setting wallpaper visibility ", e);
            }
        }
    }

    public ParcelFileDescriptor setWallpaper(String str) {
        return null;
    }

    public WallpaperConnection(Intent intent, Context context, WallpaperConnectionListener wallpaperConnectionListener, SurfaceView surfaceView, SurfaceView surfaceView2) {
        this.mContext = context.getApplicationContext();
        this.mIntent = intent;
        this.mListener = wallpaperConnectionListener;
        this.mContainerView = surfaceView;
        this.mSecondContainerView = surfaceView2;
    }
}
