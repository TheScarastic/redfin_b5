package com.android.wm.shell.bubbles;

import android.graphics.Bitmap;
import android.graphics.Path;
import android.view.View;
/* loaded from: classes2.dex */
public interface BubbleViewProvider {
    Bitmap getAppBadge();

    Bitmap getBubbleIcon();

    int getDotColor();

    Path getDotPath();

    BubbleExpandedView getExpandedView();

    View getIconView();

    String getKey();

    int getTaskId();

    void setExpandedContentAlpha(float f);

    void setTaskViewVisibility(boolean z);

    boolean showDot();
}
