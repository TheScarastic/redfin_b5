package com.android.systemui.privacy;

import android.content.Context;
import android.graphics.drawable.Drawable;
import com.android.systemui.R$string;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyItem.kt */
/* loaded from: classes.dex */
public enum PrivacyType {
    TYPE_CAMERA(R$string.privacy_type_camera, 17303144, "android.permission-group.CAMERA", "camera"),
    TYPE_MICROPHONE(R$string.privacy_type_microphone, 17303149, "android.permission-group.MICROPHONE", "microphone"),
    TYPE_LOCATION(R$string.privacy_type_location, 17303148, "android.permission-group.LOCATION", "location");
    
    private final int iconId;
    private final String logName;
    private final int nameId;
    private final String permGroupName;

    PrivacyType(int i, int i2, String str, String str2) {
        this.nameId = i;
        this.iconId = i2;
        this.permGroupName = str;
        this.logName = str2;
    }

    public final int getNameId() {
        return this.nameId;
    }

    public final int getIconId() {
        return this.iconId;
    }

    public final String getPermGroupName() {
        return this.permGroupName;
    }

    public final String getLogName() {
        return this.logName;
    }

    public final String getName(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getString(this.nameId);
    }

    public final Drawable getIcon(Context context) {
        Intrinsics.checkNotNullParameter(context, "context");
        return context.getResources().getDrawable(this.iconId, context.getTheme());
    }
}
