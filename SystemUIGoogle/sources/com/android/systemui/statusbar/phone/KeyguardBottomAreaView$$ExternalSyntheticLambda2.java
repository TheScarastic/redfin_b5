package com.android.systemui.statusbar.phone;

import com.android.systemui.plugins.IntentButtonProvider;
import com.android.systemui.statusbar.policy.ExtensionController;
/* loaded from: classes.dex */
public final /* synthetic */ class KeyguardBottomAreaView$$ExternalSyntheticLambda2 implements ExtensionController.PluginConverter {
    public static final /* synthetic */ KeyguardBottomAreaView$$ExternalSyntheticLambda2 INSTANCE = new KeyguardBottomAreaView$$ExternalSyntheticLambda2();

    private /* synthetic */ KeyguardBottomAreaView$$ExternalSyntheticLambda2() {
    }

    @Override // com.android.systemui.statusbar.policy.ExtensionController.PluginConverter
    public final Object getInterfaceFromPlugin(Object obj) {
        return KeyguardBottomAreaView.lambda$onAttachedToWindow$3((IntentButtonProvider) obj);
    }
}
