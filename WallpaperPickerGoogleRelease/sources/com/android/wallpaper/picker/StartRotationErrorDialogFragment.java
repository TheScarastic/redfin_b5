package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class StartRotationErrorDialogFragment extends DialogFragment {

    /* loaded from: classes.dex */
    public interface Listener {
        void retryStartRotation(int i);
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreateDialog(bundle);
        final int i = this.mArguments.getInt("network_preference");
        return new AlertDialog.Builder(getActivity(), R.style.LightDialogTheme).setMessage(getResources().getString(R.string.start_rotation_error_message)).setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() { // from class: com.android.wallpaper.picker.StartRotationErrorDialogFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i2) {
                ((Listener) StartRotationErrorDialogFragment.this.getTargetFragment()).retryStartRotation(i);
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }
}
