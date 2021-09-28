package com.android.wm.shell.common;

import android.view.SurfaceControl;
import android.view.SurfaceSession;
/* loaded from: classes2.dex */
public class SurfaceUtils {
    public static SurfaceControl makeDimLayer(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, String str, SurfaceSession surfaceSession) {
        SurfaceControl build = new SurfaceControl.Builder(surfaceSession).setParent(surfaceControl).setColorLayer().setName(str).setCallsite("SurfaceUtils.makeDimLayer").build();
        transaction.setLayer(build, Integer.MAX_VALUE).setColor(build, new float[]{0.0f, 0.0f, 0.0f});
        return build;
    }
}
