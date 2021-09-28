package com.android.settingslib.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
/* loaded from: classes.dex */
public class AppPreference extends Preference {
    private int mProgress;
    private boolean mProgressVisible;

    public AppPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setLayoutResource(R$layout.preference_app);
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        ProgressBar progressBar = (ProgressBar) preferenceViewHolder.findViewById(16908301);
        if (this.mProgressVisible) {
            progressBar.setProgress(this.mProgress);
            progressBar.setVisibility(0);
            return;
        }
        progressBar.setVisibility(8);
    }
}
