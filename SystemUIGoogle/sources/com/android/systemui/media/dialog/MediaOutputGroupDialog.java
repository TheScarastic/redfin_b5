package com.android.systemui.media.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import com.android.systemui.R$dimen;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
/* loaded from: classes.dex */
public class MediaOutputGroupDialog extends MediaOutputBaseDialog {
    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    IconCompat getHeaderIcon() {
        return null;
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    int getStopButtonVisibility() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public MediaOutputGroupDialog(Context context, boolean z, MediaOutputController mediaOutputController) {
        super(context, mediaOutputController);
        this.mMediaOutputController.resetGroupMediaDevices();
        this.mAdapter = new MediaOutputGroupAdapter(this.mMediaOutputController);
        if (!z) {
            getWindow().setType(2038);
        }
        show();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog, android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    int getHeaderIconRes() {
        return R$drawable.ic_arrow_back;
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    int getHeaderIconSize() {
        return ((MediaOutputBaseDialog) this).mContext.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_header_back_icon_size);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    CharSequence getHeaderText() {
        return ((MediaOutputBaseDialog) this).mContext.getString(R$string.media_output_dialog_add_output);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    CharSequence getHeaderSubtitle() {
        int size = this.mMediaOutputController.getSelectedMediaDevice().size();
        if (size == 1) {
            return ((MediaOutputBaseDialog) this).mContext.getText(R$string.media_output_dialog_single_device);
        }
        return ((MediaOutputBaseDialog) this).mContext.getString(R$string.media_output_dialog_multiple_devices, Integer.valueOf(size));
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    void onHeaderIconClick() {
        this.mMediaOutputController.launchMediaOutputDialog();
    }
}
