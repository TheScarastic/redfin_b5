package com.android.systemui.tuner;

import com.android.systemui.tuner.LockscreenFragment;
import java.util.function.Consumer;
/* loaded from: classes2.dex */
public final /* synthetic */ class LockscreenFragment$App$$ExternalSyntheticLambda1 implements Consumer {
    public final /* synthetic */ LockscreenFragment.App f$0;
    public final /* synthetic */ LockscreenFragment.Adapter f$1;

    public /* synthetic */ LockscreenFragment$App$$ExternalSyntheticLambda1(LockscreenFragment.App app, LockscreenFragment.Adapter adapter) {
        this.f$0 = app;
        this.f$1 = adapter;
    }

    @Override // java.util.function.Consumer
    public final void accept(Object obj) {
        this.f$0.lambda$toggleExpando$0(this.f$1, (LockscreenFragment.Item) obj);
    }
}
