package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class LoadWallpaperErrorDialogFragment extends DialogFragment {

    /* loaded from: classes.dex */
    public interface Listener {
        void onClickOk();
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreateDialog(bundle);
        return new AlertDialog.Builder(getActivity(), R.style.LightDialogTheme).setMessage(R.string.load_wallpaper_error_message).setPositiveButton(17039370, new DialogInterface.OnClickListener() { // from class: com.android.wallpaper.picker.LoadWallpaperErrorDialogFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i) {
                ((Listener) LoadWallpaperErrorDialogFragment.this.getTargetFragment()).onClickOk();
            }
        }).create();
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        ((Listener) getTargetFragment()).onClickOk();
    }
}
