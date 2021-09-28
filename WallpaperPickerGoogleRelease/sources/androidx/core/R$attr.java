package androidx.core;

import android.content.Context;
import android.os.ParcelFileDescriptor;
import android.util.Log;
import com.android.wallpaper.compat.BuildCompat;
import com.android.wallpaper.module.InjectorProvider;
import java.io.IOException;
/* loaded from: classes.dex */
public class R$attr {
    public static boolean isLockWallpaperSet(Context context) {
        boolean z = false;
        if (!BuildCompat.isAtLeastN()) {
            return false;
        }
        ParcelFileDescriptor wallpaperFile = InjectorProvider.getInjector().getWallpaperManagerCompat(context).getWallpaperFile(2);
        if (wallpaperFile != null) {
            z = true;
        }
        if (z) {
            try {
                wallpaperFile.close();
            } catch (IOException unused) {
                Log.e("LockWPStatusChecker", "IO exception when closing the lock screen wallpaper file descriptor.");
            }
        }
        return z;
    }
}
