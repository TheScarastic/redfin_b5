package com.android.systemui.statusbar.notification;

import android.content.Context;
import android.graphics.PorterDuffColorFilter;
import com.android.systemui.R$integer;
/* loaded from: classes.dex */
public class NotificationIconDozeHelper extends NotificationDozeHelper {
    private final int mImageDarkAlpha;
    private final int mImageDarkColor = -1;
    private PorterDuffColorFilter mImageColorFilter = null;
    private int mColor = -16777216;

    public NotificationIconDozeHelper(Context context) {
        this.mImageDarkAlpha = context.getResources().getInteger(R$integer.doze_small_icon_alpha);
    }

    public void setColor(int i) {
        this.mColor = i;
    }
}
