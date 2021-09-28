package com.android.systemui.media.dialog;

import android.content.Context;
import android.os.Bundle;
import androidx.core.graphics.drawable.IconCompat;
import com.android.internal.annotations.VisibleForTesting;
import com.android.internal.logging.UiEventLogger;
import com.android.systemui.R$dimen;
/* loaded from: classes.dex */
public class MediaOutputDialog extends MediaOutputBaseDialog {
    final UiEventLogger mUiEventLogger;

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    int getHeaderIconRes() {
        return 0;
    }

    /* access modifiers changed from: package-private */
    public MediaOutputDialog(Context context, boolean z, MediaOutputController mediaOutputController, UiEventLogger uiEventLogger) {
        super(context, mediaOutputController);
        this.mUiEventLogger = uiEventLogger;
        this.mAdapter = new MediaOutputAdapter(this.mMediaOutputController);
        if (!z) {
            getWindow().setType(2038);
        }
        show();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog, android.app.AlertDialog, android.app.Dialog
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        this.mUiEventLogger.log(MediaOutputEvent.MEDIA_OUTPUT_DIALOG_SHOW);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    IconCompat getHeaderIcon() {
        return this.mMediaOutputController.getHeaderIcon();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    int getHeaderIconSize() {
        return ((MediaOutputBaseDialog) this).mContext.getResources().getDimensionPixelSize(R$dimen.media_output_dialog_header_album_icon_size);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    CharSequence getHeaderText() {
        return this.mMediaOutputController.getHeaderTitle();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    CharSequence getHeaderSubtitle() {
        return this.mMediaOutputController.getHeaderSubTitle();
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseDialog
    int getStopButtonVisibility() {
        MediaOutputController mediaOutputController = this.mMediaOutputController;
        return mediaOutputController.isActiveRemoteDevice(mediaOutputController.getCurrentConnectedMediaDevice()) ? 0 : 8;
    }

    @VisibleForTesting
    /* loaded from: classes.dex */
    public enum MediaOutputEvent implements UiEventLogger.UiEventEnum {
        MEDIA_OUTPUT_DIALOG_SHOW(655);
        
        private final int mId;

        MediaOutputEvent(int i) {
            this.mId = i;
        }

        public int getId() {
            return this.mId;
        }
    }
}
