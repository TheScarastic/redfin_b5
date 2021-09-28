package com.android.systemui.shared.system;

import android.content.Intent;
import android.view.SurfaceView;
/* loaded from: classes.dex */
public final class UniversalSmartspaceUtils {
    public static final String ACTION_REQUEST_SMARTSPACE_VIEW = "com.android.systemui.REQUEST_SMARTSPACE_VIEW";
    public static final String INTENT_BUNDLE_KEY = "bundle_key";
    private static final String SYSUI_PACKAGE = "com.android.systemui";

    private UniversalSmartspaceUtils() {
    }

    public static Intent createRequestSmartspaceIntent(SurfaceView surfaceView) {
        return new Intent(ACTION_REQUEST_SMARTSPACE_VIEW).putExtra(INTENT_BUNDLE_KEY, SurfaceViewRequestUtils.createSurfaceBundle(surfaceView)).setPackage(SYSUI_PACKAGE).addFlags(1342177280);
    }
}
