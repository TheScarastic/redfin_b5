package com.android.systemui.fragments;

import android.content.res.Configuration;
import com.android.systemui.fragments.FragmentService;
/* loaded from: classes.dex */
public final /* synthetic */ class FragmentService$FragmentHostState$$ExternalSyntheticLambda0 implements Runnable {
    public final /* synthetic */ FragmentService.FragmentHostState f$0;
    public final /* synthetic */ Configuration f$1;

    public /* synthetic */ FragmentService$FragmentHostState$$ExternalSyntheticLambda0(FragmentService.FragmentHostState fragmentHostState, Configuration configuration) {
        this.f$0 = fragmentHostState;
        this.f$1 = configuration;
    }

    @Override // java.lang.Runnable
    public final void run() {
        this.f$0.lambda$sendConfigurationChange$0(this.f$1);
    }
}
