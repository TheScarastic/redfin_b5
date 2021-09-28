package com.android.wallpaper.model;

import android.content.Context;
import android.os.Bundle;
import com.android.wallpaper.picker.SectionView;
/* loaded from: classes.dex */
public interface CustomizationSectionController<T extends SectionView> {

    /* loaded from: classes.dex */
    public interface CustomizationSectionNavigationController {
    }

    T createView(Context context);

    boolean isAvailable(Context context);

    default void onSaveInstanceState(Bundle bundle) {
    }

    default void onTransitionOut() {
    }

    default void release() {
    }
}
