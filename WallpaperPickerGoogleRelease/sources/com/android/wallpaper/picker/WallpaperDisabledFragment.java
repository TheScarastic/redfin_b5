package com.android.wallpaper.picker;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.InjectorProvider;
import java.util.Date;
import java.util.Objects;
/* loaded from: classes.dex */
public class WallpaperDisabledFragment extends Fragment {
    public static final /* synthetic */ int $r8$clinit = 0;

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        Objects.requireNonNull(InjectorProvider.getInjector().getFormFactorChecker(getActivity()));
        View inflate = layoutInflater.inflate(R.layout.fragment_disabled_by_admin, viewGroup, false);
        int i = this.mArguments.getInt("wallpaper_support_level");
        TextView textView = (TextView) inflate.findViewById(R.id.wallpaper_disabled_message);
        if (i == 1) {
            textView.setText(R.string.wallpaper_disabled_by_administrator_message);
        } else if (i == 2) {
            textView.setText(R.string.wallpaper_disabled_message);
        }
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onResume() {
        this.mCalled = true;
        InjectorProvider.getInjector().getPreferences(getActivity()).setLastAppActiveTimestamp(new Date().getTime());
    }
}
