package com.android.systemui.statusbar.policy;

import android.content.ClipData;
import com.android.systemui.statusbar.policy.RemoteInputView;
import java.util.function.Predicate;
/* loaded from: classes.dex */
public final /* synthetic */ class RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1 implements Predicate {
    public static final /* synthetic */ RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1 INSTANCE = new RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1();

    private /* synthetic */ RemoteInputView$RemoteEditText$$ExternalSyntheticLambda1() {
    }

    @Override // java.util.function.Predicate
    public final boolean test(Object obj) {
        return RemoteInputView.RemoteEditText.lambda$onReceiveContent$0((ClipData.Item) obj);
    }
}
