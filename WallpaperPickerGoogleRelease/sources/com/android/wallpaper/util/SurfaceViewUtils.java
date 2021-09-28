package com.android.wallpaper.util;

import android.os.Bundle;
import android.view.SurfaceView;
/* loaded from: classes.dex */
public class SurfaceViewUtils {
    public static Bundle createSurfaceViewRequest(SurfaceView surfaceView) {
        Bundle bundle = new Bundle();
        bundle.putBinder("host_token", surfaceView.getHostToken());
        bundle.putInt("display_id", surfaceView.getDisplay().getDisplayId());
        bundle.putInt("width", surfaceView.getWidth());
        bundle.putInt("height", surfaceView.getHeight());
        return bundle;
    }
}
