package com.google.android.systemui.face;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.hardware.face.Face;
import android.hardware.face.FaceManager;
import android.util.Log;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.phone.SystemUIDialog;
/* loaded from: classes2.dex */
class FaceNotificationDialogFactory {
    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$createReenrollDialog$1(DialogInterface dialogInterface, int i) {
    }

    /* access modifiers changed from: private */
    public static /* synthetic */ void lambda$createReenrollFailureDialog$2(DialogInterface dialogInterface, int i) {
    }

    /* access modifiers changed from: package-private */
    public static Dialog createReenrollDialog(Context context) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(context);
        systemUIDialog.setTitle(context.getString(R$string.face_reenroll_dialog_title));
        systemUIDialog.setMessage(context.getString(R$string.face_reenroll_dialog_content));
        systemUIDialog.setPositiveButton(R$string.face_reenroll_dialog_confirm, new DialogInterface.OnClickListener(context) { // from class: com.google.android.systemui.face.FaceNotificationDialogFactory$$ExternalSyntheticLambda0
            public final /* synthetic */ Context f$0;

            {
                this.f$0 = r1;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                FaceNotificationDialogFactory.lambda$createReenrollDialog$0(this.f$0, dialogInterface, i);
            }
        });
        systemUIDialog.setNegativeButton(R$string.face_reenroll_dialog_cancel, FaceNotificationDialogFactory$$ExternalSyntheticLambda2.INSTANCE);
        return systemUIDialog;
    }

    /* access modifiers changed from: private */
    public static Dialog createReenrollFailureDialog(Context context) {
        SystemUIDialog systemUIDialog = new SystemUIDialog(context);
        systemUIDialog.setMessage(context.getText(R$string.face_reenroll_failure_dialog_content));
        systemUIDialog.setPositiveButton(R$string.ok, FaceNotificationDialogFactory$$ExternalSyntheticLambda1.INSTANCE);
        return systemUIDialog;
    }

    /* access modifiers changed from: private */
    public static void onReenrollDialogConfirm(final Context context) {
        FaceManager faceManager = (FaceManager) context.getSystemService(FaceManager.class);
        if (faceManager == null) {
            Log.e("FaceNotificationDialogF", "Not launching enrollment. Face manager was null!");
            createReenrollFailureDialog(context).show();
            return;
        }
        faceManager.remove(new Face("", 0, 0), context.getUserId(), new FaceManager.RemovalCallback() { // from class: com.google.android.systemui.face.FaceNotificationDialogFactory.1
            boolean mDidShowFailureDialog;

            public void onRemovalError(Face face, int i, CharSequence charSequence) {
                Log.e("FaceNotificationDialogF", "Not launching enrollment. Failed to remove existing face(s).");
                if (!this.mDidShowFailureDialog) {
                    this.mDidShowFailureDialog = true;
                    FaceNotificationDialogFactory.createReenrollFailureDialog(context).show();
                }
            }

            public void onRemovalSucceeded(Face face, int i) {
                if (!this.mDidShowFailureDialog && i == 0) {
                    Intent intent = new Intent("android.settings.BIOMETRIC_ENROLL");
                    intent.setPackage("com.android.settings");
                    intent.setFlags(268435456);
                    context.startActivity(intent);
                }
            }
        });
    }
}
