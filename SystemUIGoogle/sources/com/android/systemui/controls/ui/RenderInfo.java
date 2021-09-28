package com.android.systemui.controls.ui;

import android.content.ComponentName;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.ArrayMap;
import android.util.SparseArray;
import com.android.systemui.R$drawable;
import kotlin.Pair;
import kotlin.collections.MapsKt;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: RenderInfo.kt */
/* loaded from: classes.dex */
public final class RenderInfo {
    private final int enabledBackground;
    private final int foreground;
    private final Drawable icon;
    public static final Companion Companion = new Companion(null);
    private static final SparseArray<Drawable> iconMap = new SparseArray<>();
    private static final ArrayMap<ComponentName, Drawable> appIconMap = new ArrayMap<>();

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof RenderInfo)) {
            return false;
        }
        RenderInfo renderInfo = (RenderInfo) obj;
        return Intrinsics.areEqual(this.icon, renderInfo.icon) && this.foreground == renderInfo.foreground && this.enabledBackground == renderInfo.enabledBackground;
    }

    public int hashCode() {
        return (((this.icon.hashCode() * 31) + Integer.hashCode(this.foreground)) * 31) + Integer.hashCode(this.enabledBackground);
    }

    public String toString() {
        return "RenderInfo(icon=" + this.icon + ", foreground=" + this.foreground + ", enabledBackground=" + this.enabledBackground + ')';
    }

    public RenderInfo(Drawable drawable, int i, int i2) {
        Intrinsics.checkNotNullParameter(drawable, "icon");
        this.icon = drawable;
        this.foreground = i;
        this.enabledBackground = i2;
    }

    public final Drawable getIcon() {
        return this.icon;
    }

    public final int getForeground() {
        return this.foreground;
    }

    public final int getEnabledBackground() {
        return this.enabledBackground;
    }

    /* compiled from: RenderInfo.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }

        public static /* synthetic */ RenderInfo lookup$default(Companion companion, Context context, ComponentName componentName, int i, int i2, int i3, Object obj) {
            if ((i3 & 8) != 0) {
                i2 = 0;
            }
            return companion.lookup(context, componentName, i, i2);
        }

        public final RenderInfo lookup(Context context, ComponentName componentName, int i, int i2) {
            Drawable drawable;
            Intrinsics.checkNotNullParameter(context, "context");
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            if (i2 > 0) {
                i = (i * 1000) + i2;
            }
            Pair pair = (Pair) MapsKt.getValue(RenderInfoKt.deviceColorMap, Integer.valueOf(i));
            int intValue = ((Number) pair.component1()).intValue();
            int intValue2 = ((Number) pair.component2()).intValue();
            int intValue3 = ((Number) MapsKt.getValue(RenderInfoKt.deviceIconMap, Integer.valueOf(i))).intValue();
            if (intValue3 == -1) {
                drawable = (Drawable) RenderInfo.appIconMap.get(componentName);
                if (drawable == null) {
                    drawable = context.getResources().getDrawable(R$drawable.ic_device_unknown_on, null);
                    RenderInfo.appIconMap.put(componentName, drawable);
                }
            } else {
                Drawable drawable2 = (Drawable) RenderInfo.iconMap.get(intValue3);
                if (drawable2 == null) {
                    drawable2 = context.getResources().getDrawable(intValue3, null);
                    RenderInfo.iconMap.put(intValue3, drawable2);
                }
                drawable = drawable2;
            }
            Intrinsics.checkNotNull(drawable);
            Drawable newDrawable = drawable.getConstantState().newDrawable(context.getResources());
            Intrinsics.checkNotNullExpressionValue(newDrawable, "!!.constantState.newDrawable(context.resources)");
            return new RenderInfo(newDrawable, intValue, intValue2);
        }

        public final void registerComponentIcon(ComponentName componentName, Drawable drawable) {
            Intrinsics.checkNotNullParameter(componentName, "componentName");
            Intrinsics.checkNotNullParameter(drawable, "icon");
            RenderInfo.appIconMap.put(componentName, drawable);
        }

        public final void clearCache() {
            RenderInfo.iconMap.clear();
            RenderInfo.appIconMap.clear();
        }
    }
}
