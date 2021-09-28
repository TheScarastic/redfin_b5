package com.android.wallpaper.picker;
/* loaded from: classes.dex */
public interface MyPhotosStarter {

    /* loaded from: classes.dex */
    public interface MyPhotosStarterProvider {
        MyPhotosStarter getMyPhotosStarter();
    }

    /* loaded from: classes.dex */
    public interface PermissionChangedListener {
        void onPermissionsDenied(boolean z);

        void onPermissionsGranted();
    }

    void requestCustomPhotoPicker(PermissionChangedListener permissionChangedListener);
}
