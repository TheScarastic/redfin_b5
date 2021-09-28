package com.android.customization.model.theme;

import android.content.Context;
import android.content.om.OverlayManager;
import com.android.customization.model.ResourceConstants;
import com.android.systemui.shared.R;
import java.util.ArrayList;
import java.util.Arrays;
/* loaded from: classes.dex */
public class OverlayManagerCompat {
    public OverlayManagerCompat(Context context) {
        OverlayManager overlayManager = (OverlayManager) context.getSystemService(OverlayManager.class);
        ArrayList<String> arrayList = ResourceConstants.sTargetPackages;
        if (arrayList.isEmpty()) {
            arrayList.addAll(Arrays.asList("android", "com.android.settings", "com.android.systemui"));
            arrayList.add(context.getString(R.string.launcher_overlayable_package));
            arrayList.add(context.getPackageName());
        }
        String[] strArr = (String[]) arrayList.toArray(new String[0]);
    }
}
