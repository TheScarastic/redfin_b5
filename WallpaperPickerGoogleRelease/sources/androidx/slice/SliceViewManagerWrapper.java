package androidx.slice;

import android.app.slice.SliceManager;
import android.app.slice.SliceSpec;
import android.content.ContentProviderClient;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.net.Uri;
import androidx.collection.ArrayMap;
import androidx.slice.widget.SliceLiveData;
import com.google.common.math.IntMath;
import java.util.Set;
/* loaded from: classes.dex */
public class SliceViewManagerWrapper extends SliceViewManagerBase {
    public final SliceManager mManager;
    public final ArrayMap<String, Boolean> mCachedSuspendFlags = new ArrayMap<>();
    public final ArrayMap<String, String> mCachedAuthorities = new ArrayMap<>();
    public final Set<SliceSpec> mSpecs = SliceConvert.unwrap(SliceLiveData.SUPPORTED_SPECS);

    public SliceViewManagerWrapper(Context context) {
        super(context);
        this.mManager = (SliceManager) context.getSystemService(SliceManager.class);
    }

    @Override // androidx.slice.SliceViewManager
    public Slice bindSlice(Uri uri) {
        if (isAuthoritySuspended(uri.getAuthority())) {
            return null;
        }
        return SliceConvert.wrap(this.mManager.bindSlice(uri, this.mSpecs), this.mContext);
    }

    public final boolean isAuthoritySuspended(String str) {
        String orDefault = this.mCachedAuthorities.getOrDefault(str, null);
        if (orDefault == null) {
            ProviderInfo resolveContentProvider = this.mContext.getPackageManager().resolveContentProvider(str, 0);
            if (resolveContentProvider == null) {
                return false;
            }
            orDefault = resolveContentProvider.packageName;
            this.mCachedAuthorities.put(str, orDefault);
        }
        return isPackageSuspended(orDefault);
    }

    public final boolean isPackageSuspended(String str) {
        Boolean orDefault = this.mCachedSuspendFlags.getOrDefault(str, null);
        if (orDefault == null) {
            try {
                Boolean valueOf = Boolean.valueOf((this.mContext.getPackageManager().getApplicationInfo(str, 0).flags & IntMath.MAX_SIGNED_POWER_OF_TWO) != 0);
                this.mCachedSuspendFlags.put(str, valueOf);
                orDefault = valueOf;
            } catch (PackageManager.NameNotFoundException unused) {
                return false;
            }
        }
        return orDefault.booleanValue();
    }

    @Override // androidx.slice.SliceViewManager
    public void pinSlice(Uri uri) {
        try {
            this.mManager.pinSlice(uri, this.mSpecs);
        } catch (RuntimeException e) {
            ContentProviderClient acquireContentProviderClient = this.mContext.getContentResolver().acquireContentProviderClient(uri);
            if (acquireContentProviderClient == null) {
                throw new IllegalArgumentException("No provider found for " + uri);
            }
            acquireContentProviderClient.release();
            throw e;
        }
    }

    @Override // androidx.slice.SliceViewManager
    public void unpinSlice(Uri uri) {
        try {
            this.mManager.unpinSlice(uri);
        } catch (IllegalStateException unused) {
        }
    }

    @Override // androidx.slice.SliceViewManager
    public Slice bindSlice(Intent intent) {
        throw null;
    }
}
