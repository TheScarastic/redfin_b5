package com.android.systemui.navigationbar.gestural;

import android.content.res.AssetManager;
import java.util.HashMap;
import java.util.Map;
/* loaded from: classes.dex */
public class BackGestureTfClassifierProvider {
    public boolean isActive() {
        return false;
    }

    public float predict(Object[] objArr) {
        return -1.0f;
    }

    public void release() {
    }

    public Map<String, Integer> loadVocab(AssetManager assetManager) {
        return new HashMap();
    }
}
