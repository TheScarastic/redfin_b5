package com.google.android.apps.wallpaper.sync;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.ParcelFileDescriptor;
import com.android.wallpaper.module.Injector;
import com.android.wallpaper.module.InjectorProvider;
import java.io.FileNotFoundException;
import java.util.Objects;
/* loaded from: classes.dex */
public class SyncDataProvider extends ContentProvider {
    @Override // android.content.ContentProvider
    public int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public String getType(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    @Override // android.content.ContentProvider
    public boolean onCreate() {
        return false;
    }

    @Override // android.content.ContentProvider
    public ParcelFileDescriptor openFile(Uri uri, String str) throws FileNotFoundException {
        Context applicationContext = getContext().getApplicationContext();
        Injector injector = InjectorProvider.getInjector();
        injector.getPreferences(applicationContext);
        Objects.requireNonNull(injector.getFormFactorChecker(applicationContext));
        throw new FileNotFoundException("Cannot read syncdata on this form factor");
    }

    @Override // android.content.ContentProvider
    public Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }
}
