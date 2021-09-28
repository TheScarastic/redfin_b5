package androidx.slice;

import android.content.Intent;
import android.net.Uri;
/* loaded from: classes.dex */
public abstract class SliceViewManager {

    /* loaded from: classes.dex */
    public interface SliceCallback {
    }

    public abstract Slice bindSlice(Intent intent);

    public abstract Slice bindSlice(Uri uri);

    public abstract void pinSlice(Uri uri);

    public abstract void registerSliceCallback(Uri uri, SliceCallback sliceCallback);

    public abstract void unpinSlice(Uri uri);
}
