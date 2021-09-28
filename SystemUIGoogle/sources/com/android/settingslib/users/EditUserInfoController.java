package com.android.settingslib.users;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import com.android.internal.util.UserIcons;
import com.android.settingslib.R$id;
import com.android.settingslib.R$layout;
import com.android.settingslib.drawable.CircleFramedDrawable;
import java.io.File;
import java.util.function.BiConsumer;
/* loaded from: classes.dex */
public class EditUserInfoController {
    private Dialog mEditUserInfoDialog;
    private EditUserPhotoController mEditUserPhotoController;
    private final String mFileAuthority;
    private Bitmap mSavedPhoto;
    private boolean mWaitingForActivityResult = false;

    public EditUserInfoController(String str) {
        this.mFileAuthority = str;
    }

    private void clear() {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null) {
            editUserPhotoController.removeNewUserPhotoBitmapFile();
        }
        this.mEditUserInfoDialog = null;
        this.mSavedPhoto = null;
    }

    public void onRestoreInstanceState(Bundle bundle) {
        String string = bundle.getString("pending_photo");
        if (string != null) {
            this.mSavedPhoto = EditUserPhotoController.loadNewUserPhotoBitmap(new File(string));
        }
        this.mWaitingForActivityResult = bundle.getBoolean("awaiting_result", false);
    }

    public void onSaveInstanceState(Bundle bundle) {
        EditUserPhotoController editUserPhotoController;
        File saveNewUserPhotoBitmap;
        if (!(this.mEditUserInfoDialog == null || (editUserPhotoController = this.mEditUserPhotoController) == null || (saveNewUserPhotoBitmap = editUserPhotoController.saveNewUserPhotoBitmap()) == null)) {
            bundle.putString("pending_photo", saveNewUserPhotoBitmap.getPath());
        }
        bundle.putBoolean("awaiting_result", this.mWaitingForActivityResult);
    }

    public void startingActivityForResult() {
        this.mWaitingForActivityResult = true;
    }

    public void onActivityResult(int i, int i2, Intent intent) {
        this.mWaitingForActivityResult = false;
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        if (editUserPhotoController != null && this.mEditUserInfoDialog != null) {
            editUserPhotoController.onActivityResult(i, i2, intent);
        }
    }

    public Dialog createDialog(Activity activity, ActivityStarter activityStarter, Drawable drawable, String str, String str2, BiConsumer<String, Drawable> biConsumer, Runnable runnable) {
        Drawable drawable2;
        View inflate = LayoutInflater.from(activity).inflate(R$layout.edit_user_info_dialog_content, (ViewGroup) null);
        EditText editText = (EditText) inflate.findViewById(R$id.user_name);
        editText.setText(str);
        ImageView imageView = (ImageView) inflate.findViewById(R$id.user_photo);
        if (drawable != null) {
            drawable2 = drawable;
        } else {
            drawable2 = UserIcons.getDefaultUserIcon(activity.getResources(), -10000, false);
        }
        imageView.setImageDrawable(getUserIcon(activity, drawable2));
        if (canChangePhoto(activity)) {
            this.mEditUserPhotoController = createEditUserPhotoController(activity, activityStarter, imageView);
        } else {
            imageView.setBackground(null);
        }
        Dialog buildDialog = buildDialog(activity, inflate, editText, drawable, str, str2, biConsumer, runnable);
        this.mEditUserInfoDialog = buildDialog;
        buildDialog.getWindow().setSoftInputMode(4);
        return this.mEditUserInfoDialog;
    }

    private Drawable getUserIcon(Activity activity, Drawable drawable) {
        Bitmap bitmap = this.mSavedPhoto;
        return bitmap != null ? CircleFramedDrawable.getInstance(activity, bitmap) : drawable;
    }

    private Dialog buildDialog(Activity activity, View view, EditText editText, Drawable drawable, String str, String str2, BiConsumer<String, Drawable> biConsumer, Runnable runnable) {
        return new AlertDialog.Builder(activity).setTitle(str2).setView(view).setCancelable(true).setPositiveButton(17039370, new DialogInterface.OnClickListener(drawable, editText, str, biConsumer) { // from class: com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda1
            public final /* synthetic */ Drawable f$1;
            public final /* synthetic */ EditText f$2;
            public final /* synthetic */ String f$3;
            public final /* synthetic */ BiConsumer f$4;

            {
                this.f$1 = r2;
                this.f$2 = r3;
                this.f$3 = r4;
                this.f$4 = r5;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                EditUserInfoController.$r8$lambda$td0Vm6FdkwUDLFeBBFLth5nTFcw(EditUserInfoController.this, this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
            }
        }).setNegativeButton(17039360, new DialogInterface.OnClickListener(runnable) { // from class: com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda2
            public final /* synthetic */ Runnable f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.content.DialogInterface.OnClickListener
            public final void onClick(DialogInterface dialogInterface, int i) {
                EditUserInfoController.m28$r8$lambda$qQR0vdyjqCEdLBJ8kVFHHShCo(EditUserInfoController.this, this.f$1, dialogInterface, i);
            }
        }).setOnCancelListener(new DialogInterface.OnCancelListener(runnable) { // from class: com.android.settingslib.users.EditUserInfoController$$ExternalSyntheticLambda0
            public final /* synthetic */ Runnable f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.content.DialogInterface.OnCancelListener
            public final void onCancel(DialogInterface dialogInterface) {
                EditUserInfoController.$r8$lambda$s5F2fUsPG09Rk0DNPho5Ed5gKdM(EditUserInfoController.this, this.f$1, dialogInterface);
            }
        }).create();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$buildDialog$0(Drawable drawable, EditText editText, String str, BiConsumer biConsumer, DialogInterface dialogInterface, int i) {
        EditUserPhotoController editUserPhotoController = this.mEditUserPhotoController;
        Drawable newUserPhotoDrawable = editUserPhotoController != null ? editUserPhotoController.getNewUserPhotoDrawable() : null;
        if (newUserPhotoDrawable != null) {
            drawable = newUserPhotoDrawable;
        }
        String trim = editText.getText().toString().trim();
        if (!trim.isEmpty()) {
            str = trim;
        }
        clear();
        if (biConsumer != null) {
            biConsumer.accept(str, drawable);
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$buildDialog$1(Runnable runnable, DialogInterface dialogInterface, int i) {
        clear();
        if (runnable != null) {
            runnable.run();
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$buildDialog$2(Runnable runnable, DialogInterface dialogInterface) {
        clear();
        if (runnable != null) {
            runnable.run();
        }
    }

    boolean canChangePhoto(Context context) {
        return (PhotoCapabilityUtils.canCropPhoto(context) && PhotoCapabilityUtils.canChoosePhoto(context)) || PhotoCapabilityUtils.canTakePhoto(context);
    }

    EditUserPhotoController createEditUserPhotoController(Activity activity, ActivityStarter activityStarter, ImageView imageView) {
        return new EditUserPhotoController(activity, activityStarter, imageView, this.mSavedPhoto, this.mWaitingForActivityResult, this.mFileAuthority);
    }
}
