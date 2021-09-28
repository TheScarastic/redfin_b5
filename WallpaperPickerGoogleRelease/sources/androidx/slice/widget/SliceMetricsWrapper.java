package androidx.slice.widget;

import android.app.slice.SliceMetrics;
import android.content.Context;
import android.net.Uri;
/* loaded from: classes.dex */
public class SliceMetricsWrapper extends SliceMetrics {
    public final SliceMetrics mSliceMetrics;

    public SliceMetricsWrapper(Context context, Uri uri) {
        this.mSliceMetrics = new SliceMetrics(context, uri);
    }
}
