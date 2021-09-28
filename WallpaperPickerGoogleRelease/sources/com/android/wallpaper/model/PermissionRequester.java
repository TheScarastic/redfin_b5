package com.android.wallpaper.model;

import com.android.wallpaper.picker.MyPhotosStarter;
/* loaded from: classes.dex */
public interface PermissionRequester {
    void requestExternalStoragePermission(MyPhotosStarter.PermissionChangedListener permissionChangedListener);
}
