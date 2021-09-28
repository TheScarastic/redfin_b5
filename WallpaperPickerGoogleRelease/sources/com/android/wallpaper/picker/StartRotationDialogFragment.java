package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.DefaultSystemFeatureChecker;
import com.android.wallpaper.module.InjectorProvider;
import java.util.Objects;
/* loaded from: classes.dex */
public class StartRotationDialogFragment extends DialogFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public boolean mIsWifiOnlyChecked;

    /* loaded from: classes.dex */
    public interface Listener {
        void onStartRotationDialogDismiss(DialogInterface dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            this.mIsWifiOnlyChecked = true;
        } else {
            this.mIsWifiOnlyChecked = bundle.getBoolean("key_is_wifi_only_checked");
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        View inflate = getActivity().getLayoutInflater().inflate(R.layout.dialog_start_rotation, (ViewGroup) null);
        ((TextView) inflate.findViewById(R.id.start_rotation_dialog_subhead)).setText(Html.fromHtml(getResources().getString(R.string.start_rotation_dialog_body)));
        CheckBox checkBox = (CheckBox) inflate.findViewById(R.id.start_rotation_wifi_only_checkbox);
        DefaultSystemFeatureChecker systemFeatureChecker = InjectorProvider.getInjector().getSystemFeatureChecker();
        Context context = getContext();
        Objects.requireNonNull(systemFeatureChecker);
        if (context.getPackageManager().hasSystemFeature("android.hardware.telephony")) {
            checkBox.setChecked(this.mIsWifiOnlyChecked);
            checkBox.setOnClickListener(new CategoryFragment$$ExternalSyntheticLambda3(this, checkBox));
        } else {
            checkBox.setVisibility(8);
        }
        return new AlertDialog.Builder(getActivity(), R.style.LightDialogTheme).setView(inflate).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).setPositiveButton(17039370, new CategoryFragment$$ExternalSyntheticLambda0(this)).create();
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ((Listener) getTargetFragment()).onStartRotationDialogDismiss(dialogInterface);
    }

    @Override // androidx.fragment.app.DialogFragment, androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putBoolean("key_is_wifi_only_checked", this.mIsWifiOnlyChecked);
    }
}
