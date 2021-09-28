package androidx.slice;

import android.app.PendingIntent;
import android.app.slice.Slice;
import android.app.slice.SliceManager;
import android.app.slice.SliceSpec;
import android.content.ComponentName;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Process;
import android.util.Log;
import android.util.TypedValue;
import android.view.ContextThemeWrapper;
import androidx.core.app.CoreComponentFactory;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.SliceConvert;
import androidx.slice.SliceProvider;
import androidx.slice.compat.CompatPermissionManager;
import androidx.slice.compat.SliceProviderCompat;
import androidx.slice.core.R$drawable;
import androidx.slice.core.R$string;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
/* loaded from: classes.dex */
public abstract class SliceProvider extends ContentProvider implements CoreComponentFactory.CompatWrapped {
    private static Clock sClock;
    private static Set<SliceSpec> sSpecs;
    private String[] mAuthorities;
    private String mAuthority;
    private SliceProviderCompat mCompat;
    private List<Uri> mPinnedSliceUris;
    private Context mContext = null;
    private final Object mCompatLock = new Object();
    private final Object mPinnedSliceUrisLock = new Object();
    private final String[] mAutoGrantPermissions = new String[0];

    @Override // android.content.ContentProvider
    public final int bulkInsert(Uri uri, ContentValues[] contentValuesArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final Uri canonicalize(Uri uri) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final int delete(Uri uri, String str, String[] strArr) {
        return 0;
    }

    @Override // android.content.ContentProvider
    public final Uri insert(Uri uri, ContentValues contentValues) {
        return null;
    }

    public abstract Slice onBindSlice(Uri uri);

    public PendingIntent onCreatePermissionRequest(Uri uri, String str) {
        return null;
    }

    public abstract boolean onCreateSliceProvider();

    public void onSlicePinned(Uri uri) {
    }

    public void onSliceUnpinned(Uri uri) {
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, Bundle bundle, CancellationSignal cancellationSignal) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final Cursor query(Uri uri, String[] strArr, String str, String[] strArr2, String str2, CancellationSignal cancellationSignal) {
        return null;
    }

    @Override // android.content.ContentProvider
    public final int update(Uri uri, ContentValues contentValues, String str, String[] strArr) {
        return 0;
    }

    @Override // androidx.core.app.CoreComponentFactory.CompatWrapped
    public Object getWrapper() {
        if (Build.VERSION.SDK_INT >= 28) {
            return new android.app.slice.SliceProvider(this, this.mAutoGrantPermissions) { // from class: androidx.slice.compat.SliceProviderWrapperContainer$SliceProviderWrapper
                private String[] mAutoGrantPermissions;
                private SliceManager mSliceManager;
                private SliceProvider mSliceProvider;

                @Override // android.content.ContentProvider
                public boolean onCreate() {
                    return true;
                }

                {
                    this.mAutoGrantPermissions = (r3 == null || r3.length == 0) ? null : r3;
                    this.mSliceProvider = r2;
                }

                @Override // android.app.slice.SliceProvider, android.content.ContentProvider
                public void attachInfo(Context context, ProviderInfo providerInfo) {
                    this.mSliceProvider.attachInfo(context, providerInfo);
                    super.attachInfo(context, providerInfo);
                    this.mSliceManager = (SliceManager) context.getSystemService(SliceManager.class);
                }

                @Override // android.app.slice.SliceProvider
                public PendingIntent onCreatePermissionRequest(Uri uri) {
                    if (this.mAutoGrantPermissions != null) {
                        checkPermissions(uri);
                    }
                    PendingIntent onCreatePermissionRequest = this.mSliceProvider.onCreatePermissionRequest(uri, getCallingPackage());
                    if (onCreatePermissionRequest != null) {
                        return onCreatePermissionRequest;
                    }
                    return super.onCreatePermissionRequest(uri);
                }

                @Override // android.app.slice.SliceProvider, android.content.ContentProvider
                public Bundle call(String str, String str2, Bundle bundle) {
                    Intent intent;
                    if (this.mAutoGrantPermissions != null) {
                        Uri uri = null;
                        if ("bind_slice".equals(str)) {
                            if (bundle != null) {
                                uri = (Uri) bundle.getParcelable("slice_uri");
                            }
                        } else if ("map_slice".equals(str) && (intent = (Intent) bundle.getParcelable("slice_intent")) != null) {
                            uri = onMapIntentToUri(intent);
                        }
                        if (!(uri == null || this.mSliceManager.checkSlicePermission(uri, Binder.getCallingPid(), Binder.getCallingUid()) == 0)) {
                            checkPermissions(uri);
                        }
                    }
                    if ("androidx.remotecallback.method.PROVIDER_CALLBACK".equals(str)) {
                        return this.mSliceProvider.call(str, str2, bundle);
                    }
                    return super.call(str, str2, bundle);
                }

                private void checkPermissions(Uri uri) {
                    if (uri != null) {
                        String[] strArr = this.mAutoGrantPermissions;
                        for (String str : strArr) {
                            if (getContext().checkCallingPermission(str) == 0) {
                                this.mSliceManager.grantSlicePermission(str, uri);
                                getContext().getContentResolver().notifyChange(uri, null);
                                return;
                            }
                        }
                    }
                }

                @Override // android.app.slice.SliceProvider
                public Slice onBindSlice(Uri uri, Set<SliceSpec> set) {
                    try {
                        SliceProvider.setSpecs(SliceConvert.wrap(set));
                        set = null;
                        return SliceConvert.unwrap(this.mSliceProvider.onBindSlice(uri));
                    } catch (Exception e) {
                        Log.wtf("SliceProviderWrapper", "Slice with URI " + uri.toString() + " is invalid.", e);
                        return null;
                    } finally {
                        SliceProvider.setSpecs(set);
                    }
                }

                @Override // android.app.slice.SliceProvider
                public void onSlicePinned(Uri uri) {
                    this.mSliceProvider.onSlicePinned(uri);
                    this.mSliceProvider.handleSlicePinned(uri);
                }

                @Override // android.app.slice.SliceProvider
                public void onSliceUnpinned(Uri uri) {
                    this.mSliceProvider.onSliceUnpinned(uri);
                    this.mSliceProvider.handleSliceUnpinned(uri);
                }

                @Override // android.app.slice.SliceProvider
                public Collection<Uri> onGetSliceDescendants(Uri uri) {
                    return this.mSliceProvider.onGetSliceDescendants(uri);
                }

                @Override // android.app.slice.SliceProvider
                public Uri onMapIntentToUri(Intent intent) {
                    return this.mSliceProvider.onMapIntentToUri(intent);
                }
            };
        }
        return null;
    }

    @Override // android.content.ContentProvider
    public final boolean onCreate() {
        if (Build.VERSION.SDK_INT < 19) {
            return false;
        }
        return onCreateSliceProvider();
    }

    private SliceProviderCompat getSliceProviderCompat() {
        synchronized (this.mCompatLock) {
            if (this.mCompat == null) {
                this.mCompat = new SliceProviderCompat(this, onCreatePermissionManager(this.mAutoGrantPermissions), getContext());
            }
        }
        return this.mCompat;
    }

    protected CompatPermissionManager onCreatePermissionManager(String[] strArr) {
        Context context = getContext();
        return new CompatPermissionManager(context, "slice_perms_" + getClass().getName(), Process.myUid(), strArr);
    }

    @Override // android.content.ContentProvider
    public final String getType(Uri uri) {
        if (Build.VERSION.SDK_INT < 19) {
            return null;
        }
        return "vnd.android.slice";
    }

    @Override // android.content.ContentProvider
    public void attachInfo(Context context, ProviderInfo providerInfo) {
        super.attachInfo(context, providerInfo);
        if (this.mContext == null) {
            this.mContext = context;
            if (providerInfo != null) {
                setAuthorities(providerInfo.authority);
            }
        }
    }

    @Override // android.content.ContentProvider
    public Bundle call(String str, String str2, Bundle bundle) {
        int i = Build.VERSION.SDK_INT;
        if (i < 19 || i >= 28 || bundle == null) {
            return null;
        }
        return getSliceProviderCompat().call(str, str2, bundle);
    }

    private void setAuthorities(String str) {
        if (str == null) {
            return;
        }
        if (str.indexOf(59) == -1) {
            this.mAuthority = str;
            this.mAuthorities = null;
            return;
        }
        this.mAuthority = null;
        this.mAuthorities = str.split(";");
    }

    private boolean matchesOurAuthorities(String str) {
        String str2 = this.mAuthority;
        if (str2 != null) {
            return str2.equals(str);
        }
        String[] strArr = this.mAuthorities;
        if (strArr != null) {
            int length = strArr.length;
            for (int i = 0; i < length; i++) {
                if (this.mAuthorities[i].equals(str)) {
                    return true;
                }
            }
        }
        return false;
    }

    public Slice createPermissionSlice(Uri uri, String str) {
        Context context = getContext();
        PendingIntent onCreatePermissionRequest = onCreatePermissionRequest(uri, str);
        if (onCreatePermissionRequest == null) {
            onCreatePermissionRequest = createPermissionIntent(context, uri, str);
        }
        Slice.Builder builder = new Slice.Builder(uri);
        Slice.Builder addAction = new Slice.Builder(builder).addIcon(IconCompat.createWithResource(context, R$drawable.abc_ic_permission), (String) null, new String[0]).addHints(Arrays.asList("title", "shortcut")).addAction(onCreatePermissionRequest, new Slice.Builder(builder).build(), null);
        TypedValue typedValue = new TypedValue();
        new ContextThemeWrapper(context, 16974123).getTheme().resolveAttribute(16843829, typedValue, true);
        builder.addSubSlice(new Slice.Builder(uri.buildUpon().appendPath("permission").build()).addIcon(IconCompat.createWithResource(context, R$drawable.abc_ic_arrow_forward), (String) null, new String[0]).addText(getPermissionString(context, str), (String) null, new String[0]).addInt(typedValue.data, "color", new String[0]).addSubSlice(addAction.build(), null).build(), null);
        return builder.addHints(Arrays.asList("permission_request")).build();
    }

    private static PendingIntent createPermissionIntent(Context context, Uri uri, String str) {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(context.getPackageName(), "androidx.slice.compat.SlicePermissionActivity"));
        intent.putExtra("slice_uri", uri);
        intent.putExtra("pkg", str);
        intent.putExtra("provider_pkg", context.getPackageName());
        intent.setData(uri.buildUpon().appendQueryParameter("package", str).build());
        return PendingIntent.getActivity(context, 0, intent, 0);
    }

    private static CharSequence getPermissionString(Context context, String str) {
        PackageManager packageManager = context.getPackageManager();
        try {
            return context.getString(R$string.abc_slices_permission_request, packageManager.getApplicationInfo(str, 0).loadLabel(packageManager), context.getApplicationInfo().loadLabel(packageManager));
        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException("Unknown calling app", e);
        }
    }

    public void handleSlicePinned(Uri uri) {
        List<Uri> pinnedSlices = getPinnedSlices();
        if (!pinnedSlices.contains(uri)) {
            pinnedSlices.add(uri);
        }
    }

    public void handleSliceUnpinned(Uri uri) {
        List<Uri> pinnedSlices = getPinnedSlices();
        if (pinnedSlices.contains(uri)) {
            pinnedSlices.remove(uri);
        }
    }

    public Uri onMapIntentToUri(Intent intent) {
        throw new UnsupportedOperationException("This provider has not implemented intent to uri mapping");
    }

    public Collection<Uri> onGetSliceDescendants(Uri uri) {
        return Collections.emptyList();
    }

    public List<Uri> getPinnedSlices() {
        synchronized (this.mPinnedSliceUrisLock) {
            if (this.mPinnedSliceUris == null) {
                this.mPinnedSliceUris = new ArrayList(SliceManager.getInstance(getContext()).getPinnedSlices());
            }
        }
        return this.mPinnedSliceUris;
    }

    public void validateIncomingAuthority(String str) throws SecurityException {
        String str2;
        if (!matchesOurAuthorities(getAuthorityWithoutUserId(str))) {
            String str3 = "The authority " + str + " does not match the one of the contentProvider: ";
            if (this.mAuthority != null) {
                str2 = str3 + this.mAuthority;
            } else {
                str2 = str3 + Arrays.toString(this.mAuthorities);
            }
            throw new SecurityException(str2);
        }
    }

    private static String getAuthorityWithoutUserId(String str) {
        if (str == null) {
            return null;
        }
        return str.substring(str.lastIndexOf(64) + 1);
    }

    public static void setSpecs(Set<SliceSpec> set) {
        sSpecs = set;
    }

    public static Set<SliceSpec> getCurrentSpecs() {
        return sSpecs;
    }

    public static Clock getClock() {
        return sClock;
    }
}
