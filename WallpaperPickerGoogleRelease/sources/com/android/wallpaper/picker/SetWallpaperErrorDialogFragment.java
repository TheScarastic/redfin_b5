package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LifecycleOwner;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class SetWallpaperErrorDialogFragment extends DialogFragment {

    /* loaded from: classes.dex */
    public interface Listener {
        void onClickTryAgain(int i);
    }

    public static SetWallpaperErrorDialogFragment newInstance(int i, int i2) {
        SetWallpaperErrorDialogFragment setWallpaperErrorDialogFragment = new SetWallpaperErrorDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("message", i);
        bundle.putInt("destination", i2);
        setWallpaperErrorDialogFragment.setArguments(bundle);
        return setWallpaperErrorDialogFragment;
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreateDialog(bundle);
        int i = this.mArguments.getInt("message");
        final int i2 = this.mArguments.getInt("destination");
        return new AlertDialog.Builder(getActivity(), R.style.LightDialogTheme).setMessage(i).setPositiveButton(R.string.try_again, new DialogInterface.OnClickListener() { // from class: com.android.wallpaper.picker.SetWallpaperErrorDialogFragment.1
            @Override // android.content.DialogInterface.OnClickListener
            public void onClick(DialogInterface dialogInterface, int i3) {
                LifecycleOwner targetFragment = SetWallpaperErrorDialogFragment.this.getTargetFragment();
                targetFragment = SetWallpaperErrorDialogFragment.this.getActivity();
                if (targetFragment == null) {
                }
                Listener listener = (Listener) targetFragment;
                if (listener != null) {
                    listener.onClickTryAgain(i2);
                }
            }
        }).setNegativeButton(17039360, (DialogInterface.OnClickListener) null).create();
    }
}
