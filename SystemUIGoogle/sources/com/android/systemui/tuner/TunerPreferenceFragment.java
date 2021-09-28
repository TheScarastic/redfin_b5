package com.android.systemui.tuner;

import androidx.preference.ListPreferenceDialogFragment;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import com.android.systemui.tuner.CustomListPreference;
/* loaded from: classes2.dex */
public abstract class TunerPreferenceFragment extends PreferenceFragment {
    @Override // androidx.preference.PreferenceFragment, androidx.preference.PreferenceManager.OnDisplayPreferenceDialogListener
    public void onDisplayPreferenceDialog(Preference preference) {
        ListPreferenceDialogFragment listPreferenceDialogFragment;
        if (preference instanceof CustomListPreference) {
            listPreferenceDialogFragment = CustomListPreference.CustomListPreferenceDialogFragment.newInstance(preference.getKey());
        } else {
            super.onDisplayPreferenceDialog(preference);
            listPreferenceDialogFragment = null;
        }
        listPreferenceDialogFragment.setTargetFragment(this, 0);
        listPreferenceDialogFragment.show(getFragmentManager(), "dialog_preference");
    }
}
