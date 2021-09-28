package com.android.systemui.tuner;

import com.android.systemui.tuner.LockscreenFragment;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class LockscreenFragment$App$$ExternalSyntheticLambda0 implements Consumer {
    public final /* synthetic */ LockscreenFragment.Adapter f$0;

    public /* synthetic */ LockscreenFragment$App$$ExternalSyntheticLambda0(LockscreenFragment.Adapter adapter) {
        this.f$0 = adapter;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.remItem((LockscreenFragment.Item) obj);
    }
}
