package com.android.systemui.controls.management;

import com.android.systemui.controls.controller.ControlInfo;
import java.util.List;
/* compiled from: ControlsModel.kt */
/* loaded from: classes.dex */
public interface ControlsModel {

    /* compiled from: ControlsModel.kt */
    /* loaded from: classes.dex */
    public interface ControlsModelCallback {
        void onFirstChange();
    }

    /* compiled from: ControlsModel.kt */
    /* loaded from: classes.dex */
    public interface MoveHelper {
        boolean canMoveAfter(int i);

        boolean canMoveBefore(int i);

        void moveAfter(int i);

        void moveBefore(int i);
    }

    void changeFavoriteStatus(String str, boolean z);

    List<ElementWrapper> getElements();

    List<ControlInfo> getFavorites();

    MoveHelper getMoveHelper();
}
