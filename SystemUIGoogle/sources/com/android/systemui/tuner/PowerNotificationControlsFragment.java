package com.android.systemui.tuner;

import android.app.Fragment;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;
import com.android.internal.logging.MetricsLogger;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
/* loaded from: classes2.dex */
public class PowerNotificationControlsFragment extends Fragment {
    @Override // android.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // android.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return layoutInflater.inflate(R$layout.power_notification_controls_settings, viewGroup, false);
    }

    @Override // android.app.Fragment
    public void onViewCreated(View view, Bundle bundle) {
        String str;
        super.onViewCreated(view, bundle);
        View findViewById = view.findViewById(R$id.switch_bar);
        final Switch r3 = (Switch) findViewById.findViewById(16908352);
        final TextView textView = (TextView) findViewById.findViewById(R$id.switch_text);
        r3.setChecked(isEnabled());
        if (isEnabled()) {
            str = getString(R$string.switch_bar_on);
        } else {
            str = getString(R$string.switch_bar_off);
        }
        textView.setText(str);
        r3.setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.tuner.PowerNotificationControlsFragment.1
            @Override // android.view.View.OnClickListener
            public void onClick(View view2) {
                String str2;
                boolean z = !PowerNotificationControlsFragment.this.isEnabled() ? 1 : 0;
                MetricsLogger.action(PowerNotificationControlsFragment.this.getContext(), 393, z);
                Settings.Secure.putInt(PowerNotificationControlsFragment.this.getContext().getContentResolver(), "show_importance_slider", z ? 1 : 0);
                r3.setChecked(z);
                TextView textView2 = textView;
                if (z) {
                    str2 = PowerNotificationControlsFragment.this.getString(R$string.switch_bar_on);
                } else {
                    str2 = PowerNotificationControlsFragment.this.getString(R$string.switch_bar_off);
                }
                textView2.setText(str2);
            }
        });
    }

    @Override // android.app.Fragment
    public void onResume() {
        super.onResume();
        MetricsLogger.visibility(getContext(), 392, true);
    }

    @Override // android.app.Fragment
    public void onPause() {
        super.onPause();
        MetricsLogger.visibility(getContext(), 392, false);
    }

    /* access modifiers changed from: private */
    public boolean isEnabled() {
        return Settings.Secure.getInt(getContext().getContentResolver(), "show_importance_slider", 0) == 1;
    }
}
