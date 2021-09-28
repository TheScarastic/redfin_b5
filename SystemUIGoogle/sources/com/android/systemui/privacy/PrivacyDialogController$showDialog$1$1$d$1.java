package com.android.systemui.privacy;

import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.FunctionReferenceImpl;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
/* synthetic */ class PrivacyDialogController$showDialog$1$1$d$1 extends FunctionReferenceImpl implements Function2<String, Integer, Unit> {
    /* access modifiers changed from: package-private */
    public PrivacyDialogController$showDialog$1$1$d$1(PrivacyDialogController privacyDialogController) {
        super(2, privacyDialogController, PrivacyDialogController.class, "startActivity", "startActivity(Ljava/lang/String;I)V", 0);
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
    @Override // kotlin.jvm.functions.Function2
    public /* bridge */ /* synthetic */ Unit invoke(String str, Integer num) {
        invoke(str, num.intValue());
        return Unit.INSTANCE;
    }

    public final void invoke(String str, int i) {
        Intrinsics.checkNotNullParameter(str, "p0");
        ((PrivacyDialogController) this.receiver).startActivity(str, i);
    }
}
