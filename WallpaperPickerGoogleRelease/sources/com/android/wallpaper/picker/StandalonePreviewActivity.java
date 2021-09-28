package com.android.wallpaper.picker;

import android.content.Intent;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.util.Log;
import androidx.fragment.app.BackStackRecord;
import androidx.fragment.app.Fragment;
import com.android.systemui.shared.R;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.wallpaper.model.ImageWallpaperInfo;
import com.android.wallpaper.module.InjectorProvider;
import com.android.wallpaper.module.UserEventLogger;
import com.android.wallpaper.picker.AppbarFragment;
/* loaded from: classes.dex */
public class StandalonePreviewActivity extends BasePreviewActivity implements AppbarFragment.AppbarFragmentHost {
    public UserEventLogger mUserEventLogger;

    @Override // com.android.wallpaper.picker.BasePreviewActivity
    public void enableFullScreen() {
        super.enableFullScreen();
        getWindow().setFlags(QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED, QuickStepContract.SYSUI_STATE_STATUS_BAR_KEYGUARD_SHOWING_OCCLUDED);
    }

    @Override // com.android.wallpaper.picker.AppbarFragment.AppbarFragmentHost
    public boolean isUpArrowSupported() {
        return false;
    }

    public final void loadPreviewFragment() {
        Intent intent = getIntent();
        boolean booleanExtra = intent.getBooleanExtra("com.android.wallpaper.picker.testing_mode_enabled", false);
        Fragment previewFragment = InjectorProvider.getInjector().getPreviewFragment(this, new ImageWallpaperInfo(intent.getData()), 1, true, booleanExtra);
        BackStackRecord backStackRecord = new BackStackRecord(getSupportFragmentManager());
        backStackRecord.add(R.id.fragment_container, previewFragment);
        backStackRecord.commit();
    }

    @Override // android.app.Activity, android.view.Window.Callback
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getSupportFragmentManager().findFragmentById(R.id.fragment_container) == null) {
            loadPreviewFragment();
        }
    }

    @Override // com.android.wallpaper.picker.BasePreviewActivity, androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, androidx.core.app.ComponentActivity, android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_preview);
        enableFullScreen();
        UserEventLogger userEventLogger = InjectorProvider.getInjector().getUserEventLogger(getApplicationContext());
        this.mUserEventLogger = userEventLogger;
        userEventLogger.logStandalonePreviewLaunched();
        Uri data = getIntent().getData();
        if (data == null) {
            Log.e("StandalonePreview", "No URI passed in intent; exiting StandalonePreviewActivity");
            finish();
            return;
        }
        boolean z = false;
        boolean z2 = checkUriPermission(data, Binder.getCallingPid(), Binder.getCallingUid(), 1) == 0;
        this.mUserEventLogger.logStandalonePreviewImageUriHasReadPermission(z2);
        if (!z2) {
            if (getPackageManager().checkPermission("android.permission.READ_EXTERNAL_STORAGE", getPackageName()) == 0) {
                z = true;
            }
            if (!z) {
                requestPermissions(new String[]{"android.permission.READ_EXTERNAL_STORAGE"}, 1);
            }
        }
    }

    @Override // androidx.fragment.app.FragmentActivity, androidx.activity.ComponentActivity, android.app.Activity
    public void onRequestPermissionsResult(int i, String[] strArr, int[] iArr) {
        boolean z = true;
        if (i == 1) {
            if (strArr.length <= 0 || !strArr[0].equals("android.permission.READ_EXTERNAL_STORAGE") || iArr.length <= 0 || iArr[0] != 0) {
                z = false;
            }
            this.mUserEventLogger.logStandalonePreviewStorageDialogApproved(z);
            if (!z) {
                finish();
            }
            loadPreviewFragment();
        }
    }

    @Override // com.android.wallpaper.picker.AppbarFragment.AppbarFragmentHost
    public void onUpArrowPressed() {
    }
}
