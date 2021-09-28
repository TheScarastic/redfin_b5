package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.SpinnerAdapter;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.widget.settingsspinner.SettingsSpinner;
import com.android.settingslib.widget.settingsspinner.SettingsSpinnerAdapter;
/* loaded from: classes.dex */
public class SettingsSpinnerPreference extends Preference implements Preference.OnPreferenceClickListener {
    private SettingsSpinnerAdapter mAdapter;
    private AdapterView.OnItemSelectedListener mListener;
    private final AdapterView.OnItemSelectedListener mOnSelectedListener = new AdapterView.OnItemSelectedListener() { // from class: com.android.settingslib.widget.SettingsSpinnerPreference.1
        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long j) {
            if (SettingsSpinnerPreference.this.mPosition != i) {
                SettingsSpinnerPreference.this.mPosition = i;
                if (SettingsSpinnerPreference.this.mListener != null) {
                    SettingsSpinnerPreference.this.mListener.onItemSelected(adapterView, view, i, j);
                }
            }
        }

        @Override // android.widget.AdapterView.OnItemSelectedListener
        public void onNothingSelected(AdapterView<?> adapterView) {
            if (SettingsSpinnerPreference.this.mListener != null) {
                SettingsSpinnerPreference.this.mListener.onNothingSelected(adapterView);
            }
        }
    };
    private int mPosition;
    private boolean mShouldPerformClick;

    public SettingsSpinnerPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.settings_spinner_preference);
        setOnPreferenceClickListener(this);
    }

    @Override // androidx.preference.Preference.OnPreferenceClickListener
    public boolean onPreferenceClick(Preference preference) {
        this.mShouldPerformClick = true;
        notifyChanged();
        return true;
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        SettingsSpinner settingsSpinner = (SettingsSpinner) preferenceViewHolder.findViewById(R$id.spinner);
        settingsSpinner.setAdapter((SpinnerAdapter) this.mAdapter);
        settingsSpinner.setSelection(this.mPosition);
        settingsSpinner.setOnItemSelectedListener(this.mOnSelectedListener);
        if (this.mShouldPerformClick) {
            this.mShouldPerformClick = false;
            settingsSpinner.performClick();
        }
    }
}
