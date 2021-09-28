package com.android.systemui.controls;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import java.util.LinkedHashMap;
import java.util.Map;
import kotlin.Unit;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: CustomIconCache.kt */
/* loaded from: classes.dex */
public final class CustomIconCache {
    private final Map<String, Icon> cache = new LinkedHashMap();
    private ComponentName currentComponent;

    public final void store(ComponentName componentName, String str, Icon icon) {
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (!Intrinsics.areEqual(componentName, this.currentComponent)) {
            clear();
            this.currentComponent = componentName;
        }
        synchronized (this.cache) {
            if (icon != null) {
                this.cache.put(str, icon);
            } else {
                this.cache.remove(str);
            }
        }
    }

    public final Icon retrieve(ComponentName componentName, String str) {
        Icon icon;
        Intrinsics.checkNotNullParameter(componentName, "component");
        Intrinsics.checkNotNullParameter(str, "controlId");
        if (!Intrinsics.areEqual(componentName, this.currentComponent)) {
            return null;
        }
        synchronized (this.cache) {
            icon = this.cache.get(str);
        }
        return icon;
    }

    private final void clear() {
        synchronized (this.cache) {
            this.cache.clear();
            Unit unit = Unit.INSTANCE;
        }
    }
}
