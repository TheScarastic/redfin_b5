package com.android.wallpaper.picker;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import com.android.systemui.shared.R;
/* loaded from: classes.dex */
public class SetWallpaperDialogFragment extends DialogFragment {
    public static final /* synthetic */ int $r8$clinit = 0;
    public Listener mListener;
    public Button mSetHomeWallpaperButton;
    public Button mSetLockWallpaperButton;
    public int mTitleResId;
    public boolean mWithItemSelected;
    public boolean mHomeAvailable = true;
    public boolean mLockAvailable = true;

    /* loaded from: classes.dex */
    public interface Listener {
        default void onDialogDismissed(boolean z) {
        }

        void onSet(int i);
    }

    public SetWallpaperDialogFragment() {
        this.mRetainInstance = true;
        FragmentManager fragmentManager = this.mFragmentManager;
        if (fragmentManager != null) {
            fragmentManager.mNonConfig.addRetainedFragment(this);
        } else {
            this.mRetainInstanceChangedWhileDetached = true;
        }
    }

    @Override // androidx.fragment.app.DialogFragment
    public Dialog onCreateDialog(Bundle bundle) {
        super.onCreateDialog(bundle);
        Context context = getContext();
        View inflate = View.inflate(new ContextThemeWrapper(getActivity(), (int) R.style.LightDialogTheme), R.layout.dialog_set_wallpaper, null);
        inflate.findViewById(R.id.dialog_set_wallpaper_options).setClipToOutline(true);
        View inflate2 = View.inflate(context, R.layout.dialog_set_wallpaper_title, null);
        ((TextView) inflate2.findViewById(R.id.dialog_set_wallpaper_title)).setText(this.mTitleResId);
        AlertDialog create = new AlertDialog.Builder(context, R.style.LightDialogTheme).setCustomTitle(inflate2).setView(inflate).create();
        Button button = (Button) inflate.findViewById(R.id.set_home_wallpaper_button);
        this.mSetHomeWallpaperButton = button;
        button.setOnClickListener(new View.OnClickListener(this, 0) { // from class: com.android.wallpaper.picker.SetWallpaperDialogFragment$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ SetWallpaperDialogFragment f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (this.$r8$classId) {
                    case 0:
                        SetWallpaperDialogFragment setWallpaperDialogFragment = this.f$0;
                        int i = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment.onSetWallpaperButtonClick(0);
                        return;
                    case 1:
                        SetWallpaperDialogFragment setWallpaperDialogFragment2 = this.f$0;
                        int i2 = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment2.onSetWallpaperButtonClick(1);
                        return;
                    default:
                        SetWallpaperDialogFragment setWallpaperDialogFragment3 = this.f$0;
                        int i3 = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment3.onSetWallpaperButtonClick(2);
                        return;
                }
            }
        });
        Button button2 = (Button) inflate.findViewById(R.id.set_lock_wallpaper_button);
        this.mSetLockWallpaperButton = button2;
        button2.setOnClickListener(new View.OnClickListener(this, 1) { // from class: com.android.wallpaper.picker.SetWallpaperDialogFragment$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ SetWallpaperDialogFragment f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (this.$r8$classId) {
                    case 0:
                        SetWallpaperDialogFragment setWallpaperDialogFragment = this.f$0;
                        int i = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment.onSetWallpaperButtonClick(0);
                        return;
                    case 1:
                        SetWallpaperDialogFragment setWallpaperDialogFragment2 = this.f$0;
                        int i2 = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment2.onSetWallpaperButtonClick(1);
                        return;
                    default:
                        SetWallpaperDialogFragment setWallpaperDialogFragment3 = this.f$0;
                        int i3 = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment3.onSetWallpaperButtonClick(2);
                        return;
                }
            }
        });
        ((Button) inflate.findViewById(R.id.set_both_wallpaper_button)).setOnClickListener(new View.OnClickListener(this, 2) { // from class: com.android.wallpaper.picker.SetWallpaperDialogFragment$$ExternalSyntheticLambda0
            public final /* synthetic */ int $r8$classId;
            public final /* synthetic */ SetWallpaperDialogFragment f$0;

            {
                this.$r8$classId = r3;
                this.f$0 = r2;
            }

            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                switch (this.$r8$classId) {
                    case 0:
                        SetWallpaperDialogFragment setWallpaperDialogFragment = this.f$0;
                        int i = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment.onSetWallpaperButtonClick(0);
                        return;
                    case 1:
                        SetWallpaperDialogFragment setWallpaperDialogFragment2 = this.f$0;
                        int i2 = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment2.onSetWallpaperButtonClick(1);
                        return;
                    default:
                        SetWallpaperDialogFragment setWallpaperDialogFragment3 = this.f$0;
                        int i3 = SetWallpaperDialogFragment.$r8$clinit;
                        setWallpaperDialogFragment3.onSetWallpaperButtonClick(2);
                        return;
                }
            }
        });
        updateButtonsVisibility();
        return create;
    }

    @Override // androidx.fragment.app.DialogFragment, android.content.DialogInterface.OnDismissListener
    public void onDismiss(DialogInterface dialogInterface) {
        super.onDismiss(dialogInterface);
        Listener listener = this.mListener;
        if (listener != null) {
            listener.onDialogDismissed(this.mWithItemSelected);
        }
    }

    public final void onSetWallpaperButtonClick(int i) {
        this.mWithItemSelected = true;
        this.mListener.onSet(i);
        dismissInternal(false, false);
    }

    public final void updateButtonsVisibility() {
        Button button = this.mSetHomeWallpaperButton;
        int i = 0;
        if (button != null) {
            button.setVisibility(this.mHomeAvailable ? 0 : 8);
        }
        Button button2 = this.mSetLockWallpaperButton;
        if (button2 != null) {
            if (!this.mLockAvailable) {
                i = 8;
            }
            button2.setVisibility(i);
        }
    }
}
