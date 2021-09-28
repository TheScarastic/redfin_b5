package com.android.systemui.controls.management;

import android.content.ComponentName;
import android.graphics.drawable.Icon;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
/* synthetic */ class ControlInfoWrapper$customIconGetter$1 extends FunctionReferenceImpl implements Function2<ComponentName, String, Icon> {
    public static final ControlInfoWrapper$customIconGetter$1 INSTANCE = new ControlInfoWrapper$customIconGetter$1();

    ControlInfoWrapper$customIconGetter$1() {
        super(2, ControlsModelKt.class, "nullIconGetter", "nullIconGetter(Landroid/content/ComponentName;Ljava/lang/String;)Landroid/graphics/drawable/Icon;", 1);
    }

    public final Icon invoke(ComponentName componentName, String str) {
        Intrinsics.checkNotNullParameter(componentName, "p0");
        Intrinsics.checkNotNullParameter(str, "p1");
        return ControlsModelKt.access$nullIconGetter(componentName, str);
    }
}
