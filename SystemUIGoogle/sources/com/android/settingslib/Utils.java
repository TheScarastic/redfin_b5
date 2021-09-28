package com.android.settingslib;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.NetworkCapabilities;
import android.net.vcn.VcnTransportInfo;
import android.net.wifi.WifiInfo;
import android.os.UserHandle;
import android.provider.Settings;
import android.telephony.NetworkRegistrationInfo;
import android.telephony.ServiceState;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import com.android.internal.annotations.VisibleForTesting;
import com.android.launcher3.icons.IconFactory;
import java.text.NumberFormat;
/* loaded from: classes.dex */
public class Utils {
    @VisibleForTesting
    static final String STORAGE_MANAGER_ENABLED_PROPERTY = "ro.storage_manager.enabled";
    private static String sPermissionControllerPackageName;
    private static String sServicesSystemSharedLibPackageName;
    private static String sSharedSystemSharedLibPackageName;
    private static Signature[] sSystemSignature;
    static final int[] WIFI_PIE = {17302885, 17302886, 17302887, 17302888, 17302889};
    static final int[] SHOW_X_WIFI_PIE = {R$drawable.ic_show_x_wifi_signal_0, R$drawable.ic_show_x_wifi_signal_1, R$drawable.ic_show_x_wifi_signal_2, R$drawable.ic_show_x_wifi_signal_3, R$drawable.ic_show_x_wifi_signal_4};

    public static void updateLocationEnabled(Context context, boolean z, int i, int i2) {
        Settings.Secure.putIntForUser(context.getContentResolver(), "location_changer", i2, i);
        ((LocationManager) context.getSystemService(LocationManager.class)).setLocationEnabledForUser(z, UserHandle.of(i));
    }

    public static String formatPercentage(long j, long j2) {
        return formatPercentage(((double) j) / ((double) j2));
    }

    public static String formatPercentage(int i) {
        return formatPercentage(((double) i) / 100.0d);
    }

    public static String formatPercentage(double d) {
        return NumberFormat.getPercentInstance().format(d);
    }

    public static ColorStateList getColorAccent(Context context) {
        return getColorAttr(context, 16843829);
    }

    public static ColorStateList getColorError(Context context) {
        return getColorAttr(context, 16844099);
    }

    public static int getColorAccentDefaultColor(Context context) {
        return getColorAttrDefaultColor(context, 16843829);
    }

    public static int getColorErrorDefaultColor(Context context) {
        return getColorAttrDefaultColor(context, 16844099);
    }

    public static int getColorStateListDefaultColor(Context context, int i) {
        return context.getResources().getColorStateList(i, context.getTheme()).getDefaultColor();
    }

    public static int applyAlpha(float f, int i) {
        return Color.argb((int) (f * ((float) Color.alpha(i))), Color.red(i), Color.green(i), Color.blue(i));
    }

    public static int getColorAttrDefaultColor(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int color = obtainStyledAttributes.getColor(0, 0);
        obtainStyledAttributes.recycle();
        return color;
    }

    public static ColorStateList getColorAttr(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        try {
            return obtainStyledAttributes.getColorStateList(0);
        } finally {
            obtainStyledAttributes.recycle();
        }
    }

    public static int getThemeAttr(Context context, int i) {
        return getThemeAttr(context, i, 0);
    }

    public static int getThemeAttr(Context context, int i, int i2) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        int resourceId = obtainStyledAttributes.getResourceId(0, i2);
        obtainStyledAttributes.recycle();
        return resourceId;
    }

    public static Drawable getDrawable(Context context, int i) {
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(new int[]{i});
        Drawable drawable = obtainStyledAttributes.getDrawable(0);
        obtainStyledAttributes.recycle();
        return drawable;
    }

    public static boolean isSystemPackage(Resources resources, PackageManager packageManager, PackageInfo packageInfo) {
        if (sSystemSignature == null) {
            sSystemSignature = new Signature[]{getSystemSignature(packageManager)};
        }
        if (sPermissionControllerPackageName == null) {
            sPermissionControllerPackageName = packageManager.getPermissionControllerPackageName();
        }
        if (sServicesSystemSharedLibPackageName == null) {
            sServicesSystemSharedLibPackageName = packageManager.getServicesSystemSharedLibraryPackageName();
        }
        if (sSharedSystemSharedLibPackageName == null) {
            sSharedSystemSharedLibPackageName = packageManager.getSharedSystemSharedLibraryPackageName();
        }
        Signature[] signatureArr = sSystemSignature;
        return (signatureArr[0] != null && signatureArr[0].equals(getFirstSignature(packageInfo))) || packageInfo.packageName.equals(sPermissionControllerPackageName) || packageInfo.packageName.equals(sServicesSystemSharedLibPackageName) || packageInfo.packageName.equals(sSharedSystemSharedLibPackageName) || packageInfo.packageName.equals("com.android.printspooler") || isDeviceProvisioningPackage(resources, packageInfo.packageName);
    }

    private static Signature getFirstSignature(PackageInfo packageInfo) {
        Signature[] signatureArr;
        if (packageInfo == null || (signatureArr = packageInfo.signatures) == null || signatureArr.length <= 0) {
            return null;
        }
        return signatureArr[0];
    }

    private static Signature getSystemSignature(PackageManager packageManager) {
        try {
            return getFirstSignature(packageManager.getPackageInfo("android", 64));
        } catch (PackageManager.NameNotFoundException unused) {
            return null;
        }
    }

    public static boolean isDeviceProvisioningPackage(Resources resources, String str) {
        String string = resources.getString(17039919);
        return string != null && string.equals(str);
    }

    public static boolean isInService(ServiceState serviceState) {
        int combinedServiceState;
        return (serviceState == null || (combinedServiceState = getCombinedServiceState(serviceState)) == 3 || combinedServiceState == 1 || combinedServiceState == 2) ? false : true;
    }

    public static int getCombinedServiceState(ServiceState serviceState) {
        if (serviceState == null) {
            return 1;
        }
        int state = serviceState.getState();
        int dataRegistrationState = serviceState.getDataRegistrationState();
        if ((state == 1 || state == 2) && dataRegistrationState == 0 && isNotInIwlan(serviceState)) {
            return 0;
        }
        return state;
    }

    public static Drawable getBadgedIcon(Context context, Drawable drawable, UserHandle userHandle) {
        IconFactory obtain = IconFactory.obtain(context);
        try {
            BitmapDrawable bitmapDrawable = new BitmapDrawable(context.getResources(), obtain.createBadgedIconBitmap(drawable, userHandle, true).icon);
            obtain.close();
            return bitmapDrawable;
        } catch (Throwable th) {
            if (obtain != null) {
                try {
                    obtain.close();
                } catch (Throwable th2) {
                    th.addSuppressed(th2);
                }
            }
            throw th;
        }
    }

    public static Drawable getBadgedIcon(Context context, ApplicationInfo applicationInfo) {
        return getBadgedIcon(context, applicationInfo.loadUnbadgedIcon(context.getPackageManager()), UserHandle.getUserHandleForUid(applicationInfo.uid));
    }

    private static boolean isNotInIwlan(ServiceState serviceState) {
        NetworkRegistrationInfo networkRegistrationInfo = serviceState.getNetworkRegistrationInfo(2, 2);
        if (networkRegistrationInfo == null) {
            return true;
        }
        return !(networkRegistrationInfo.getRegistrationState() == 1 || networkRegistrationInfo.getRegistrationState() == 5);
    }

    public static Bitmap convertCornerRadiusBitmap(Context context, Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        RoundedBitmapDrawable create = RoundedBitmapDrawableFactory.create(context.getResources(), bitmap);
        create.setAntiAlias(true);
        create.setCornerRadius(f);
        Canvas canvas = new Canvas(createBitmap);
        create.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        create.draw(canvas);
        return createBitmap;
    }

    public static WifiInfo tryGetWifiInfoForVcn(NetworkCapabilities networkCapabilities) {
        if (networkCapabilities.getTransportInfo() == null || !(networkCapabilities.getTransportInfo() instanceof VcnTransportInfo)) {
            return null;
        }
        return networkCapabilities.getTransportInfo().getWifiInfo();
    }
}
