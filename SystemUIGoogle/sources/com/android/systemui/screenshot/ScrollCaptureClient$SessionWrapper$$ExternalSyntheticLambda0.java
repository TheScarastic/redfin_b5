package com.android.systemui.screenshot;

import androidx.concurrent.futures.CallbackToFutureAdapter;
import com.android.systemui.screenshot.ScrollCaptureClient;
/* loaded from: classes.dex */
public final /* synthetic */ class ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda0 implements CallbackToFutureAdapter.Resolver {
    public final /* synthetic */ ScrollCaptureClient.SessionWrapper f$0;

    public /* synthetic */ ScrollCaptureClient$SessionWrapper$$ExternalSyntheticLambda0(ScrollCaptureClient.SessionWrapper sessionWrapper) {
        this.f$0 = sessionWrapper;
    }

    @Override // androidx.concurrent.futures.CallbackToFutureAdapter.Resolver
    public final Object attachCompleter(CallbackToFutureAdapter.Completer completer) {
        return ScrollCaptureClient.SessionWrapper.$r8$lambda$WO2Btku8DuKAzrj6gQCqDRC_R24(this.f$0, completer);
    }
}
