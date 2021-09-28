package com.android.systemui.tuner;

import android.os.Bundle;
import androidx.preference.PreferenceFragment;
import com.android.systemui.R$xml;
/* loaded from: classes2.dex */
public class OtherPrefs extends PreferenceFragment {
    @Override // androidx.preference.PreferenceFragment
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R$xml.other_settings);
    }
}
