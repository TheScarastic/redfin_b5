package androidx.slice.widget;

import android.app.slice.SliceMetrics;
import android.content.Context;
import android.net.Uri;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class SliceMetricsWrapper extends SliceMetrics {
    private final SliceMetrics mSliceMetrics;

    /* access modifiers changed from: package-private */
    public SliceMetricsWrapper(Context context, Uri uri) {
        this.mSliceMetrics = new SliceMetrics(context, uri);
    }

    @Override // androidx.slice.widget.SliceMetrics
    protected void logVisible() {
        this.mSliceMetrics.logVisible();
    }

    @Override // androidx.slice.widget.SliceMetrics
    protected void logHidden() {
        this.mSliceMetrics.logHidden();
    }

    @Override // androidx.slice.widget.SliceMetrics
    protected void logTouch(int i, Uri uri) {
        this.mSliceMetrics.logTouch(i, uri);
    }
}
