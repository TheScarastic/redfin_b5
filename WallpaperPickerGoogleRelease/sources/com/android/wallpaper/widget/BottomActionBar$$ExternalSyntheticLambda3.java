package com.android.wallpaper.widget;

import com.android.wallpaper.widget.BottomActionBar;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public final /* synthetic */ class BottomActionBar$$ExternalSyntheticLambda3 implements BiConsumer {
    public final /* synthetic */ BottomActionBar.BottomAction f$0;

    public /* synthetic */ BottomActionBar$$ExternalSyntheticLambda3(BottomActionBar.BottomAction bottomAction) {
        this.f$0 = bottomAction;
    }

    @Override // java.util.function.BiConsumer
    public final void accept(Object obj, Object obj2) {
        BottomActionBar.BottomAction bottomAction = this.f$0;
        int i = BottomActionBar.$r8$clinit;
        ((BottomActionBar.BottomSheetContent) obj2).setVisibility(((BottomActionBar.BottomAction) obj).equals(bottomAction));
    }
}
