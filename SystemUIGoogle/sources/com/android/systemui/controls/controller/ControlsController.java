package com.android.systemui.controls.controller;

import android.content.ComponentName;
import android.service.controls.Control;
import android.service.controls.actions.ControlAction;
import com.android.systemui.controls.ControlStatus;
import com.android.systemui.util.UserAwareController;
import java.util.List;
import java.util.function.Consumer;
/* compiled from: ControlsController.kt */
/* loaded from: classes.dex */
public interface ControlsController extends UserAwareController {

    /* compiled from: ControlsController.kt */
    /* loaded from: classes.dex */
    public interface LoadData {
        List<ControlStatus> getAllControls();

        boolean getErrorOnLoad();

        List<String> getFavoritesIds();
    }

    void action(ComponentName componentName, ControlInfo controlInfo, ControlAction controlAction);

    void addFavorite(ComponentName componentName, CharSequence charSequence, ControlInfo controlInfo);

    boolean addSeedingFavoritesCallback(Consumer<Boolean> consumer);

    int countFavoritesForComponent(ComponentName componentName);

    List<StructureInfo> getFavorites();

    List<StructureInfo> getFavoritesForComponent(ComponentName componentName);

    StructureInfo getPreferredStructure();

    void onActionResponse(ComponentName componentName, String str, int i);

    void refreshStatus(ComponentName componentName, Control control);

    void seedFavoritesForComponents(List<ComponentName> list, Consumer<SeedResponse> consumer);

    void subscribeToFavorites(StructureInfo structureInfo);

    void unsubscribe();
}
