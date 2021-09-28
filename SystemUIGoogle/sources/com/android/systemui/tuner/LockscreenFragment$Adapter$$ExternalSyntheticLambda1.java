package com.android.systemui.tuner;

import android.view.View;
import com.android.systemui.tuner.LockscreenFragment;
/* loaded from: classes2.dex */
public final /* synthetic */ class LockscreenFragment$Adapter$$ExternalSyntheticLambda1 implements View.OnClickListener {
    public final /* synthetic */ LockscreenFragment.Adapter f$0;
    public final /* synthetic */ LockscreenFragment.Holder f$1;

    public /* synthetic */ LockscreenFragment$Adapter$$ExternalSyntheticLambda1(LockscreenFragment.Adapter adapter, LockscreenFragment.Holder holder) {
        this.f$0 = adapter;
        this.f$1 = holder;
    }

    @Override // android.view.View.OnClickListener
    public final void onClick(View view) {
        this.f$0.lambda$onBindViewHolder$1(this.f$1, view);
    }
}
