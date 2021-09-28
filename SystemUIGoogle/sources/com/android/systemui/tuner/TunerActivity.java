package com.android.systemui.tuner;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toolbar;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragment;
import androidx.preference.PreferenceScreen;
import com.android.systemui.Dependency;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.demomode.DemoModeController;
import com.android.systemui.fragments.FragmentService;
/* loaded from: classes2.dex */
public class TunerActivity extends Activity implements PreferenceFragment.OnPreferenceStartFragmentCallback, PreferenceFragment.OnPreferenceStartScreenCallback {
    private final DemoModeController mDemoModeController;
    private final TunerService mTunerService;

    public TunerActivity(DemoModeController demoModeController, TunerService tunerService) {
        this.mDemoModeController = demoModeController;
        this.mTunerService = tunerService;
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        Fragment fragment;
        super.onCreate(bundle);
        getWindow().addFlags(Integer.MIN_VALUE);
        boolean z = true;
        requestWindowFeature(1);
        setContentView(R$layout.tuner_activity);
        Toolbar toolbar = (Toolbar) findViewById(R$id.action_bar);
        if (toolbar != null) {
            setActionBar(toolbar);
        }
        if (getFragmentManager().findFragmentByTag("tuner") == null) {
            String action = getIntent().getAction();
            if (action == null || !action.equals("com.android.settings.action.DEMO_MODE")) {
                z = false;
            }
            if (z) {
                fragment = new DemoModeFragment(this.mDemoModeController);
            } else {
                fragment = new TunerFragment(this.mTunerService);
            }
            getFragmentManager().beginTransaction().replace(R$id.content_frame, fragment, "tuner").commit();
        }
    }

    @Override // android.app.Activity
    protected void onDestroy() {
        super.onDestroy();
        Dependency.destroy(FragmentService.class, TunerActivity$$ExternalSyntheticLambda0.INSTANCE);
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public boolean onMenuItemSelected(int i, MenuItem menuItem) {
        if (menuItem.getItemId() != 16908332) {
            return super.onMenuItemSelected(i, menuItem);
        }
        onBackPressed();
        return true;
    }

    @Override // android.app.Activity
    public void onBackPressed() {
        if (!getFragmentManager().popBackStackImmediate()) {
            super.onBackPressed();
        }
    }

    @Override // androidx.preference.PreferenceFragment.OnPreferenceStartFragmentCallback
    public boolean onPreferenceStartFragment(PreferenceFragment preferenceFragment, Preference preference) {
        try {
            Fragment fragment = (Fragment) Class.forName(preference.getFragment()).newInstance();
            Bundle bundle = new Bundle(1);
            bundle.putString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT", preference.getKey());
            fragment.setArguments(bundle);
            FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
            setTitle(preference.getTitle());
            beginTransaction.replace(R$id.content_frame, fragment);
            beginTransaction.addToBackStack("PreferenceFragment");
            beginTransaction.commit();
            return true;
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            Log.d("TunerActivity", "Problem launching fragment", e);
            return false;
        }
    }

    @Override // androidx.preference.PreferenceFragment.OnPreferenceStartScreenCallback
    public boolean onPreferenceStartScreen(PreferenceFragment preferenceFragment, PreferenceScreen preferenceScreen) {
        FragmentTransaction beginTransaction = getFragmentManager().beginTransaction();
        SubSettingsFragment subSettingsFragment = new SubSettingsFragment();
        Bundle bundle = new Bundle(1);
        bundle.putString("androidx.preference.PreferenceFragmentCompat.PREFERENCE_ROOT", preferenceScreen.getKey());
        subSettingsFragment.setArguments(bundle);
        subSettingsFragment.setTargetFragment(preferenceFragment, 0);
        beginTransaction.replace(R$id.content_frame, subSettingsFragment);
        beginTransaction.addToBackStack("PreferenceFragment");
        beginTransaction.commit();
        return true;
    }

    /* loaded from: classes2.dex */
    public static class SubSettingsFragment extends PreferenceFragment {
        private PreferenceScreen mParentScreen;

        @Override // androidx.preference.PreferenceFragment
        public void onCreatePreferences(Bundle bundle, String str) {
            this.mParentScreen = (PreferenceScreen) ((PreferenceFragment) getTargetFragment()).getPreferenceScreen().findPreference(str);
            PreferenceScreen createPreferenceScreen = getPreferenceManager().createPreferenceScreen(getPreferenceManager().getContext());
            setPreferenceScreen(createPreferenceScreen);
            while (this.mParentScreen.getPreferenceCount() > 0) {
                Preference preference = this.mParentScreen.getPreference(0);
                this.mParentScreen.removePreference(preference);
                createPreferenceScreen.addPreference(preference);
            }
        }

        @Override // android.app.Fragment
        public void onDestroy() {
            super.onDestroy();
            PreferenceScreen preferenceScreen = getPreferenceScreen();
            while (preferenceScreen.getPreferenceCount() > 0) {
                Preference preference = preferenceScreen.getPreference(0);
                preferenceScreen.removePreference(preference);
                this.mParentScreen.addPreference(preference);
            }
        }
    }
}
