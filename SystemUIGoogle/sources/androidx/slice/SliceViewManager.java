package androidx.slice;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
/* loaded from: classes.dex */
public abstract class SliceViewManager {

    /* loaded from: classes.dex */
    public interface SliceCallback {
        void onSliceUpdated(Slice slice);
    }

    public abstract Slice bindSlice(Intent intent);

    public abstract Slice bindSlice(Uri uri);

    public abstract void pinSlice(Uri uri);

    public abstract void registerSliceCallback(Uri uri, SliceCallback sliceCallback);

    public abstract void unpinSlice(Uri uri);

    public abstract void unregisterSliceCallback(Uri uri, SliceCallback sliceCallback);

    public static SliceViewManager getInstance(Context context) {
        if (Build.VERSION.SDK_INT >= 28) {
            return new SliceViewManagerWrapper(context);
        }
        return new SliceViewManagerCompat(context);
    }
}
