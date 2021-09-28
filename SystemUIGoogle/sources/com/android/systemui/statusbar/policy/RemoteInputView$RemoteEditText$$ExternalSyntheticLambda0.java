package com.android.systemui.statusbar.policy;

import android.view.ContentInfo;
import android.view.OnReceiveContentListener;
import android.view.View;
import com.android.systemui.statusbar.policy.RemoteInputView;
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0 implements OnReceiveContentListener {
    public final /* synthetic */ RemoteInputView.RemoteEditText f$0;

    public /* synthetic */ RemoteInputView$RemoteEditText$$ExternalSyntheticLambda0(RemoteInputView.RemoteEditText remoteEditText) {
        this.f$0 = remoteEditText;
    }

    public final ContentInfo onReceiveContent(View view, ContentInfo contentInfo) {
        return this.f$0.onReceiveContent(view, contentInfo);
    }
}
