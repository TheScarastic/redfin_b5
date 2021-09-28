package com.android.systemui.navigationbar;

import com.android.wm.shell.legacysplitscreen.LegacySplitScreen;
import java.util.function.Function;
/* loaded from: classes.dex */
public final /* synthetic */ class NavigationBar$$ExternalSyntheticLambda21 implements Function {
    public static final /* synthetic */ NavigationBar$$ExternalSyntheticLambda21 INSTANCE = new NavigationBar$$ExternalSyntheticLambda21();

    private /* synthetic */ NavigationBar$$ExternalSyntheticLambda21() {
    }

    @Override // java.util.function.Function
    public final Object apply(Object obj) {
        return Boolean.valueOf(((LegacySplitScreen) obj).isDividerVisible());
    }
}
