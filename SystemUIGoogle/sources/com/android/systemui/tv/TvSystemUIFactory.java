package com.android.systemui.tv;

import android.content.Context;
import com.android.systemui.SystemUIFactory;
import com.android.systemui.dagger.GlobalRootComponent;
/* loaded from: classes2.dex */
public class TvSystemUIFactory extends SystemUIFactory {
    @Override // com.android.systemui.SystemUIFactory
    protected GlobalRootComponent buildGlobalRootComponent(Context context) {
        return DaggerTvGlobalRootComponent.builder().context(context).build();
    }
}
