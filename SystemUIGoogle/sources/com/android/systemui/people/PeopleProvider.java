package com.android.systemui.people;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Bundle;
import android.os.UserHandle;
import android.util.Log;
import android.widget.RemoteViews;
import com.android.systemui.SystemUIAppComponentFactory;
import com.android.systemui.people.widget.PeopleSpaceWidgetManager;
/* loaded from: classes.dex */
public class PeopleProvider extends ContentProvider implements SystemUIAppComponentFactory.ContextInitializer {
    private SystemUIAppComponentFactory.ContextAvailableCallback mCallback;
    PeopleSpaceWidgetManager mPeopleSpaceWidgetManager;

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return true;
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        if (!doesCallerHavePermission()) {
            String callingPackage = getCallingPackage();
            Log.w("PeopleProvider", "API not accessible to calling package: " + callingPackage);
            throw new SecurityException("API not accessible to calling package: " + callingPackage);
        } else if (!"get_people_tile_preview".equals(str)) {
            Log.w("PeopleProvider", "Invalid method");
            throw new IllegalArgumentException("Invalid method");
        } else if (bundle != null) {
            String string = bundle.getString("shortcut_id", "");
            String string2 = bundle.getString("package_name", "");
            UserHandle userHandle = (UserHandle) bundle.getParcelable("user_handle");
            if (string.isEmpty()) {
                Log.w("PeopleProvider", "Invalid shortcut id");
                throw new IllegalArgumentException("Invalid shortcut id");
            } else if (string2.isEmpty()) {
                Log.w("PeopleProvider", "Invalid package name");
                throw new IllegalArgumentException("Invalid package name");
            } else if (userHandle != null) {
                PeopleSpaceWidgetManager peopleSpaceWidgetManager = this.mPeopleSpaceWidgetManager;
                if (peopleSpaceWidgetManager == null) {
                    Log.e("PeopleProvider", "Could not initialize people widget manager");
                    return null;
                }
                RemoteViews preview = peopleSpaceWidgetManager.getPreview(string, userHandle, string2, bundle);
                if (preview == null) {
                    return null;
                }
                Bundle bundle2 = new Bundle();
                bundle2.putParcelable("remote_views", preview);
                return bundle2;
            } else {
                Log.w("PeopleProvider", "Null user handle");
                throw new IllegalArgumentException("Null user handle");
            }
        } else {
            Log.w("PeopleProvider", "Extras can't be null");
            throw new IllegalArgumentException("Extras can't be null");
        }
    }

    private boolean doesCallerHavePermission() {
        return getContext().checkPermission("android.permission.GET_PEOPLE_TILE_PREVIEW", Binder.getCallingPid(), Binder.getCallingUid()) == 0;
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        throw new IllegalArgumentException("Invalid method");
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        throw new IllegalArgumentException("Invalid method");
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        throw new IllegalArgumentException("Invalid method");
    }

    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        throw new IllegalArgumentException("Invalid method");
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        throw new IllegalArgumentException("Invalid method");
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        this.mCallback.onContextAvailable(context);
        super.attachInfo(context, providerInfo);
    }

    @Override // com.android.systemui.SystemUIAppComponentFactory.ContextInitializer
    public void setContextAvailableCallback(SystemUIAppComponentFactory.ContextAvailableCallback contextAvailableCallback) {
        this.mCallback = contextAvailableCallback;
    }
}
