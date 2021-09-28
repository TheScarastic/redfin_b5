package com.android.systemui.controls.ui;

import android.content.Context;
import android.service.controls.Control;
/* compiled from: ControlActionCoordinator.kt */
/* loaded from: classes.dex */
public interface ControlActionCoordinator {
    void closeDialogs();

    void drag(boolean z);

    void enableActionOnTouch(String str);

    void longPress(ControlViewHolder controlViewHolder);

    void runPendingAction(String str);

    void setActivityContext(Context context);

    void setValue(ControlViewHolder controlViewHolder, String str, float f);

    void toggle(ControlViewHolder controlViewHolder, String str, boolean z);

    void touch(ControlViewHolder controlViewHolder, String str, Control control);
}
