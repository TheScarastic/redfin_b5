package com.android.systemui.tuner;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.hardware.display.AmbientDisplayConfiguration;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$string;
import com.android.systemui.R$xml;
import com.android.systemui.shared.plugins.PluginPrefs;
/* loaded from: classes2.dex */
public class TunerFragment extends PreferenceFragment {
    private final TunerService mTunerService;
    private static final CharSequence KEY_DOZE = "doze";
    private static final String[] DEBUG_ONLY = {"nav_bar", "lockscreen", "picture_in_picture"};

    @SuppressLint({"ValidFragment"})
    public TunerFragment(TunerService tunerService) {
        this.mTunerService = tunerService;
    }

    @Override // androidx.preference.PreferenceFragment, android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setHasOptionsMenu(true);
    }

    @Override // android.app.Fragment
    public void onActivityCreated(Bundle bundle) {
        super.onActivityCreated(bundle);
        getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override // androidx.preference.PreferenceFragment
    public void onCreatePreferences(Bundle bundle, String str) {
        addPreferencesFromResource(R$xml.tuner_prefs);
        if (!PluginPrefs.hasPlugins(getContext())) {
            getPreferenceScreen().removePreference(findPreference("plugins"));
        }
        if (!alwaysOnAvailable()) {
            getPreferenceScreen().removePreference(findPreference(KEY_DOZE));
        }
        if (!Build.IS_DEBUGGABLE) {
            int i = 0;
            while (true) {
                String[] strArr = DEBUG_ONLY;
                if (i >= strArr.length) {
                    break;
                }
                Preference findPreference = findPreference(strArr[i]);
                if (findPreference != null) {
                    getPreferenceScreen().removePreference(findPreference);
                }
                i++;
            }
        }
        if (Settings.Secure.getInt(getContext().getContentResolver(), "seen_tuner_warning", 0) == 0 && getFragmentManager().findFragmentByTag("tuner_warning") == null) {
            new TunerWarningFragment().show(getFragmentManager(), "tuner_warning");
        }
    }

    private boolean alwaysOnAvailable() {
        return new AmbientDisplayConfiguration(getContext()).alwaysOnAvailable();
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        getActivity().setTitle(R$string.system_ui_tuner);
        MetricsLogger.visibility(getContext(), 227, true);
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 227, false);
    }

    @Override // android.app.Fragment
    public void onCreateOptionsMenu(Menu menu, MenuInflater menuInflater) {
        menu.add(0, 2, 0, R$string.remove_from_settings);
    }

    @Override // android.app.Fragment
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int itemId = menuItem.getItemId();
        if (itemId == 2) {
            this.mTunerService.showResetRequest(new Runnable() { // from class: com.android.systemui.tuner.TunerFragment$$ExternalSyntheticLambda0
                @Override // java.lang.Runnable
                public final void run() {
                    TunerFragment.this.lambda$onOptionsItemSelected$0();
                }
            });
            return true;
        } else if (itemId != 16908332) {
            return super.onOptionsItemSelected(menuItem);
        } else {
            getActivity().finish();
            return true;
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onOptionsItemSelected$0() {
        if (getActivity() != null) {
            getActivity().finish();
        }
    }

    /* loaded from: classes2.dex */
    public static class TunerWarningFragment extends DialogFragment {
        @Override // android.app.DialogFragment
        public Dialog onCreateDialog(Bundle bundle) {
            return new AlertDialog.Builder(getContext()).setTitle(R$string.tuner_warning_title).setMessage(R$string.tuner_warning).setPositiveButton(R$string.got_it, new DialogInterface.OnClickListener() { // from class: com.android.systemui.tuner.TunerFragment.TunerWarningFragment.1
                @Override // android.content.DialogInterface.OnClickListener
                public void onClick(DialogInterface dialogInterface, int i) {
                    Settings.Secure.putInt(TunerWarningFragment.this.getContext().getContentResolver(), "seen_tuner_warning", 1);
                }
            }).show();
        }
    }
}
