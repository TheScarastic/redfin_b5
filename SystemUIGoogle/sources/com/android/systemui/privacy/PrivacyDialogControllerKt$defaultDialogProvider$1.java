package com.android.systemui.privacy;

import android.content.Context;
import com.android.systemui.privacy.PrivacyDialog;
import com.android.systemui.privacy.PrivacyDialogController;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyDialogController.kt */
/* loaded from: classes.dex */
public final class PrivacyDialogControllerKt$defaultDialogProvider$1 implements PrivacyDialogController.DialogProvider {
    @Override // com.android.systemui.privacy.PrivacyDialogController.DialogProvider
    public PrivacyDialog makeDialog(Context context, List<PrivacyDialog.PrivacyElement> list, Function2<? super String, ? super Integer, Unit> function2) {
        Intrinsics.checkNotNullParameter(context, "context");
        Intrinsics.checkNotNullParameter(list, "list");
        Intrinsics.checkNotNullParameter(function2, "starter");
        return new PrivacyDialog(context, list, function2);
    }
}
