package com.android.systemui.statusbar.notification.dagger;

import com.android.systemui.statusbar.notification.collection.render.NodeController;
import com.android.systemui.statusbar.notification.collection.render.SectionHeaderController;
/* compiled from: NotificationSectionHeadersModule.kt */
/* loaded from: classes.dex */
public interface SectionHeaderControllerSubcomponent {

    /* compiled from: NotificationSectionHeadersModule.kt */
    /* loaded from: classes.dex */
    public interface Builder {
        SectionHeaderControllerSubcomponent build();

        Builder clickIntentAction(String str);

        Builder headerText(int i);

        Builder nodeLabel(String str);
    }

    SectionHeaderController getHeaderController();

    NodeController getNodeController();
}
