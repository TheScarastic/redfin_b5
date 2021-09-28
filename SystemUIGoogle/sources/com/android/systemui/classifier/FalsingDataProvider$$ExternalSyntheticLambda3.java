package com.android.systemui.classifier;

import com.android.systemui.classifier.FalsingDataProvider;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public final /* synthetic */ class FalsingDataProvider$$ExternalSyntheticLambda3 implements Consumer {
    public static final /* synthetic */ FalsingDataProvider$$ExternalSyntheticLambda3 INSTANCE = new FalsingDataProvider$$ExternalSyntheticLambda3();

    private /* synthetic */ FalsingDataProvider$$ExternalSyntheticLambda3() {
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        ((FalsingDataProvider.SessionListener) obj).onSessionStarted();
    }
}
