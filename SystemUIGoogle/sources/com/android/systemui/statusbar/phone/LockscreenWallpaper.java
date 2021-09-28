package com.android.systemui.statusbar.phone;

import android.app.ActivityManager;
import android.app.IWallpaperManager;
import android.app.IWallpaperManagerCallback;
import android.app.WallpaperColors;
import android.app.WallpaperManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.graphics.Xfermode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.DrawableWrapper;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.os.RemoteException;
import android.os.UserHandle;
import android.util.Log;
import com.android.internal.util.IndentingPrintWriter;
import com.android.keyguard.KeyguardUpdateMonitor;
import com.android.systemui.Dumpable;
import com.android.systemui.dump.DumpManager;
import com.android.systemui.keyguard.FaceAuthScreenBrightnessController;
import com.android.systemui.statusbar.NotificationMediaManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import java.util.Optional;
import libcore.io.IoUtils;
/* loaded from: classes.dex */
public class LockscreenWallpaper extends IWallpaperManagerCallback.Stub implements Runnable, Dumpable {
    private Bitmap mCache;
    private boolean mCached;
    private int mCurrentUserId = ActivityManager.getCurrentUser();
    private final Optional<FaceAuthScreenBrightnessController> mFaceAuthScreenBrightnessController;
    private final Handler mH;
    private AsyncTask<Void, Void, LoaderResult> mLoader;
    private final NotificationMediaManager mMediaManager;
    private UserHandle mSelectedUser;
    private final KeyguardUpdateMonitor mUpdateMonitor;
    private final WallpaperManager mWallpaperManager;

    public void onWallpaperColorsChanged(WallpaperColors wallpaperColors, int i, int i2) {
    }

    public LockscreenWallpaper(WallpaperManager wallpaperManager, IWallpaperManager iWallpaperManager, KeyguardUpdateMonitor keyguardUpdateMonitor, DumpManager dumpManager, NotificationMediaManager notificationMediaManager, Optional<FaceAuthScreenBrightnessController> optional, Handler handler) {
        dumpManager.registerDumpable(LockscreenWallpaper.class.getSimpleName(), this);
        this.mWallpaperManager = wallpaperManager;
        this.mUpdateMonitor = keyguardUpdateMonitor;
        this.mMediaManager = notificationMediaManager;
        this.mFaceAuthScreenBrightnessController = optional;
        this.mH = handler;
        if (iWallpaperManager != null) {
            try {
                iWallpaperManager.setLockWallpaperCallback(this);
            } catch (RemoteException e) {
                Log.e("LockscreenWallpaper", "System dead?" + e);
            }
        }
    }

    public Bitmap getBitmap() {
        if (this.mCached) {
            return this.mCache;
        }
        boolean z = true;
        if (!this.mWallpaperManager.isWallpaperSupported()) {
            this.mCached = true;
            this.mCache = null;
            return null;
        }
        LoaderResult loadBitmap = loadBitmap(this.mCurrentUserId, this.mSelectedUser);
        if (loadBitmap.success) {
            this.mCached = true;
            KeyguardUpdateMonitor keyguardUpdateMonitor = this.mUpdateMonitor;
            if (loadBitmap.bitmap == null) {
                z = false;
            }
            keyguardUpdateMonitor.setHasLockscreenWallpaper(z);
            this.mCache = loadBitmap.bitmap;
        }
        return this.mCache;
    }

    public LoaderResult loadBitmap(int i, UserHandle userHandle) {
        Bitmap faceAuthWallpaper;
        if (!this.mWallpaperManager.isWallpaperSupported()) {
            return LoaderResult.success(null);
        }
        if (this.mFaceAuthScreenBrightnessController.isPresent() && (faceAuthWallpaper = this.mFaceAuthScreenBrightnessController.get().getFaceAuthWallpaper()) != null) {
            return LoaderResult.success(faceAuthWallpaper);
        }
        if (userHandle != null) {
            i = userHandle.getIdentifier();
        }
        ParcelFileDescriptor wallpaperFile = this.mWallpaperManager.getWallpaperFile(2, i);
        try {
            if (wallpaperFile != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inPreferredConfig = Bitmap.Config.HARDWARE;
                return LoaderResult.success(BitmapFactory.decodeFileDescriptor(wallpaperFile.getFileDescriptor(), null, options));
            } else if (userHandle != null) {
                return LoaderResult.success(this.mWallpaperManager.getBitmapAsUser(userHandle.getIdentifier(), true));
            } else {
                return LoaderResult.success(null);
            }
        } catch (OutOfMemoryError e) {
            Log.w("LockscreenWallpaper", "Can't decode file", e);
            return LoaderResult.fail();
        } finally {
            IoUtils.closeQuietly(wallpaperFile);
        }
    }

    public void setCurrentUser(int i) {
        if (i != this.mCurrentUserId) {
            UserHandle userHandle = this.mSelectedUser;
            if (userHandle == null || i != userHandle.getIdentifier()) {
                this.mCached = false;
            }
            this.mCurrentUserId = i;
        }
    }

    public void onWallpaperChanged() {
        postUpdateWallpaper();
    }

    private void postUpdateWallpaper() {
        Handler handler = this.mH;
        if (handler == null) {
            Log.wtfStack("LockscreenWallpaper", "Trying to use LockscreenWallpaper before initialization.");
            return;
        }
        handler.removeCallbacks(this);
        this.mH.post(this);
    }

    @Override // java.lang.Runnable
    public void run() {
        AsyncTask<Void, Void, LoaderResult> asyncTask = this.mLoader;
        if (asyncTask != null) {
            asyncTask.cancel(false);
        }
        final int i = this.mCurrentUserId;
        final UserHandle userHandle = this.mSelectedUser;
        this.mLoader = new AsyncTask<Void, Void, LoaderResult>() { // from class: com.android.systemui.statusbar.phone.LockscreenWallpaper.1
            /* access modifiers changed from: protected */
            public LoaderResult doInBackground(Void... voidArr) {
                return LockscreenWallpaper.this.loadBitmap(i, userHandle);
            }

            /* access modifiers changed from: protected */
            public void onPostExecute(LoaderResult loaderResult) {
                super.onPostExecute((AnonymousClass1) loaderResult);
                if (!isCancelled()) {
                    if (loaderResult.success) {
                        LockscreenWallpaper.this.mCached = true;
                        LockscreenWallpaper.this.mCache = loaderResult.bitmap;
                        LockscreenWallpaper.this.mUpdateMonitor.setHasLockscreenWallpaper(loaderResult.bitmap != null);
                        LockscreenWallpaper.this.mMediaManager.updateMediaMetaData(true, true);
                    }
                    LockscreenWallpaper.this.mLoader = null;
                }
            }
        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, new Void[0]);
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        printWriter.println(LockscreenWallpaper.class.getSimpleName() + ":");
        IndentingPrintWriter increaseIndent = new IndentingPrintWriter(printWriter, "  ").increaseIndent();
        increaseIndent.println("mCached=" + this.mCached);
        increaseIndent.println("mCache=" + this.mCache);
        increaseIndent.println("mCurrentUserId=" + this.mCurrentUserId);
        increaseIndent.println("mSelectedUser=" + this.mSelectedUser);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class LoaderResult {
        public final Bitmap bitmap;
        public final boolean success;

        LoaderResult(boolean z, Bitmap bitmap) {
            this.success = z;
            this.bitmap = bitmap;
        }

        static LoaderResult success(Bitmap bitmap) {
            return new LoaderResult(true, bitmap);
        }

        static LoaderResult fail() {
            return new LoaderResult(false, null);
        }
    }

    /* loaded from: classes.dex */
    public static class WallpaperDrawable extends DrawableWrapper {
        private final ConstantState mState;
        private final Rect mTmpRect;

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return -1;
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return -1;
        }

        public WallpaperDrawable(Resources resources, Bitmap bitmap) {
            this(resources, new ConstantState(bitmap));
        }

        private WallpaperDrawable(Resources resources, ConstantState constantState) {
            super(new BitmapDrawable(resources, constantState.mBackground));
            this.mTmpRect = new Rect();
            this.mState = constantState;
        }

        public void setXfermode(Xfermode xfermode) {
            getDrawable().setXfermode(xfermode);
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        protected void onBoundsChange(Rect rect) {
            float f;
            float f2;
            int width = getBounds().width();
            int height = getBounds().height();
            int width2 = this.mState.mBackground.getWidth();
            int height2 = this.mState.mBackground.getHeight();
            if (width2 * height > width * height2) {
                f2 = (float) height;
                f = (float) height2;
            } else {
                f2 = (float) width;
                f = (float) width2;
            }
            float f3 = f2 / f;
            if (f3 <= 1.0f) {
                f3 = 1.0f;
            }
            float f4 = ((float) height2) * f3;
            float f5 = (((float) height) - f4) * 0.5f;
            this.mTmpRect.set(rect.left, rect.top + Math.round(f5), rect.left + Math.round(((float) width2) * f3), rect.top + Math.round(f4 + f5));
            super.onBoundsChange(this.mTmpRect);
        }

        @Override // android.graphics.drawable.DrawableWrapper, android.graphics.drawable.Drawable
        public ConstantState getConstantState() {
            return this.mState;
        }

        /* access modifiers changed from: package-private */
        /* loaded from: classes.dex */
        public static class ConstantState extends Drawable.ConstantState {
            private final Bitmap mBackground;

            @Override // android.graphics.drawable.Drawable.ConstantState
            public int getChangingConfigurations() {
                return 0;
            }

            ConstantState(Bitmap bitmap) {
                this.mBackground = bitmap;
            }

            @Override // android.graphics.drawable.Drawable.ConstantState
            public Drawable newDrawable() {
                return newDrawable(null);
            }

            @Override // android.graphics.drawable.Drawable.ConstantState
            public Drawable newDrawable(Resources resources) {
                return new WallpaperDrawable(resources, this);
            }
        }
    }
}
