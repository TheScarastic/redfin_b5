package com.android.systemui.shared.system;

import android.os.Bundle;
import android.os.IBinder;
import android.view.SurfaceControl;
import android.view.SurfaceView;
/* loaded from: classes.dex */
public class SurfaceViewRequestUtils {
    private static final String KEY_DISPLAY_ID = "display_id";
    private static final String KEY_HOST_TOKEN = "host_token";
    private static final String KEY_SURFACE_CONTROL = "surface_control";

    private SurfaceViewRequestUtils() {
    }

    public static Bundle createSurfaceBundle(SurfaceView surfaceView) {
        Bundle bundle = new Bundle();
        bundle.putBinder(KEY_HOST_TOKEN, surfaceView.getHostToken());
        bundle.putParcelable(KEY_SURFACE_CONTROL, surfaceView.getSurfaceControl());
        bundle.putInt(KEY_DISPLAY_ID, surfaceView.getDisplay().getDisplayId());
        return bundle;
    }

    public static int getDisplayId(Bundle bundle) {
        return bundle.getInt(KEY_DISPLAY_ID);
    }

    public static IBinder getHostToken(Bundle bundle) {
        return bundle.getBinder(KEY_HOST_TOKEN);
    }

    public static SurfaceControl getSurfaceControl(Bundle bundle) {
        return (SurfaceControl) bundle.getParcelable(KEY_SURFACE_CONTROL);
    }
}
