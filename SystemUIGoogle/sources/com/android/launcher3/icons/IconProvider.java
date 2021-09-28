package com.android.launcher3.icons;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.XmlResourceParser;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Process;
import android.os.UserHandle;
import android.text.TextUtils;
import android.util.ArrayMap;
import android.util.Log;
import java.util.Calendar;
import java.util.Collections;
import java.util.Map;
import java.util.function.Supplier;
/* loaded from: classes.dex */
public class IconProvider {
    private static final int CONFIG_ICON_MASK_RES_ID = Resources.getSystem().getIdentifier("config_icon_mask", "string", "android");
    private static final Map<String, ThemedIconDrawable$ThemeData> DISABLED_MAP = Collections.emptyMap();
    private final String ACTION_OVERLAY_CHANGED;
    private final ComponentName mCalendar;
    private final ComponentName mClock;
    private final Context mContext;
    private Map<String, ThemedIconDrawable$ThemeData> mThemedIconMap;

    public IconProvider(Context context) {
        this(context, false);
    }

    public IconProvider(Context context, boolean z) {
        this.ACTION_OVERLAY_CHANGED = "android.intent.action.OVERLAY_CHANGED";
        this.mContext = context;
        this.mCalendar = parseComponentOrNull(context, R$string.calendar_component_name);
        this.mClock = parseComponentOrNull(context, R$string.clock_component_name);
        if (!z) {
            this.mThemedIconMap = DISABLED_MAP;
        }
    }

    public Drawable getIcon(ActivityInfo activityInfo, int i) {
        ApplicationInfo applicationInfo = activityInfo.applicationInfo;
        return getIconWithOverrides(applicationInfo.packageName, UserHandle.getUserHandleForUid(applicationInfo.uid), i, new Supplier(activityInfo, i) { // from class: com.android.launcher3.icons.IconProvider$$ExternalSyntheticLambda0
            public final /* synthetic */ ActivityInfo f$1;
            public final /* synthetic */ int f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.util.function.Supplier
            public final Object get() {
                return IconProvider.$r8$lambda$kAHCALh0e24_mhthYcY_3qohKLM(IconProvider.this, this.f$1, this.f$2);
            }
        });
    }

    private Drawable getIconWithOverrides(String str, UserHandle userHandle, int i, Supplier<Drawable> supplier) {
        int i2;
        Drawable drawable;
        ComponentName componentName = this.mCalendar;
        int i3 = 0;
        if (componentName == null || !componentName.getPackageName().equals(str)) {
            ComponentName componentName2 = this.mClock;
            if (componentName2 == null || !componentName2.getPackageName().equals(str) || !Process.myUserHandle().equals(userHandle)) {
                drawable = null;
                i2 = 0;
            } else {
                drawable = loadClockDrawable(i);
                i2 = 2;
            }
        } else {
            drawable = loadCalendarDrawable(i);
            i2 = 1;
        }
        if (drawable == null) {
            drawable = supplier.get();
        } else {
            i3 = i2;
        }
        ThemedIconDrawable$ThemeData themedIconDrawable$ThemeData = getThemedIconMap().get(str);
        return themedIconDrawable$ThemeData != null ? themedIconDrawable$ThemeData.wrapDrawable(drawable, i3) : drawable;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Removed duplicated region for block: B:13:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:8:0x001c  */
    /* renamed from: loadActivityInfoIcon */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public android.graphics.drawable.Drawable lambda$getIcon$1(android.content.pm.ActivityInfo r4, int r5) {
        /*
            r3 = this;
            int r0 = r4.getIconResource()
            if (r5 == 0) goto L_0x0019
            if (r0 == 0) goto L_0x0019
            android.content.Context r1 = r3.mContext     // Catch: NameNotFoundException | NotFoundException -> 0x0019
            android.content.pm.PackageManager r1 = r1.getPackageManager()     // Catch: NameNotFoundException | NotFoundException -> 0x0019
            android.content.pm.ApplicationInfo r2 = r4.applicationInfo     // Catch: NameNotFoundException | NotFoundException -> 0x0019
            android.content.res.Resources r1 = r1.getResourcesForApplication(r2)     // Catch: NameNotFoundException | NotFoundException -> 0x0019
            android.graphics.drawable.Drawable r5 = r1.getDrawableForDensity(r0, r5)     // Catch: NameNotFoundException | NotFoundException -> 0x0019
            goto L_0x001a
        L_0x0019:
            r5 = 0
        L_0x001a:
            if (r5 != 0) goto L_0x0026
            android.content.Context r3 = r3.mContext
            android.content.pm.PackageManager r3 = r3.getPackageManager()
            android.graphics.drawable.Drawable r5 = r4.loadIcon(r3)
        L_0x0026:
            return r5
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.launcher3.icons.IconProvider.lambda$getIcon$1(android.content.pm.ActivityInfo, int):android.graphics.drawable.Drawable");
    }

    private Map<String, ThemedIconDrawable$ThemeData> getThemedIconMap() {
        Map<String, ThemedIconDrawable$ThemeData> map = this.mThemedIconMap;
        if (map != null) {
            return map;
        }
        ArrayMap arrayMap = new ArrayMap();
        try {
            Resources resources = this.mContext.getResources();
            int identifier = resources.getIdentifier("grayscale_icon_map", "xml", this.mContext.getPackageName());
            if (identifier != 0) {
                XmlResourceParser xml = resources.getXml(identifier);
                int depth = xml.getDepth();
                while (true) {
                    int next = xml.next();
                    if (next == 2 || next == 1) {
                        break;
                    }
                }
                while (true) {
                    int next2 = xml.next();
                    if ((next2 == 3 && xml.getDepth() <= depth) || next2 == 1) {
                        break;
                    } else if (next2 == 2 && "icon".equals(xml.getName())) {
                        String attributeValue = xml.getAttributeValue(null, "package");
                        int attributeResourceValue = xml.getAttributeResourceValue(null, "drawable", 0);
                        if (attributeResourceValue != 0 && !TextUtils.isEmpty(attributeValue)) {
                            arrayMap.put(attributeValue, new ThemedIconDrawable$ThemeData(resources, attributeResourceValue));
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("IconProvider", "Unable to parse icon map", e);
        }
        this.mThemedIconMap = arrayMap;
        return arrayMap;
    }

    private Drawable loadCalendarDrawable(int i) {
        PackageManager packageManager = this.mContext.getPackageManager();
        try {
            Bundle bundle = packageManager.getActivityInfo(this.mCalendar, 8320).metaData;
            Resources resourcesForApplication = packageManager.getResourcesForApplication(this.mCalendar.getPackageName());
            int dynamicIconId = getDynamicIconId(bundle, resourcesForApplication);
            if (dynamicIconId != 0) {
                return resourcesForApplication.getDrawableForDensity(dynamicIconId, i, null);
            }
        } catch (PackageManager.NameNotFoundException unused) {
        }
        return null;
    }

    private Drawable loadClockDrawable(int i) {
        return ClockDrawableWrapper.forPackage(this.mContext, this.mClock.getPackageName(), i);
    }

    private int getDynamicIconId(Bundle bundle, Resources resources) {
        if (bundle == null) {
            return 0;
        }
        int i = bundle.getInt(this.mCalendar.getPackageName() + ".dynamic_icons", 0);
        if (i == 0) {
            return 0;
        }
        try {
            return resources.obtainTypedArray(i).getResourceId(getDay(), 0);
        } catch (Resources.NotFoundException unused) {
            return 0;
        }
    }

    /* access modifiers changed from: package-private */
    public static int getDay() {
        return Calendar.getInstance().get(5) - 1;
    }

    private static ComponentName parseComponentOrNull(Context context, int i) {
        String string = context.getString(i);
        if (TextUtils.isEmpty(string)) {
            return null;
        }
        return ComponentName.unflattenFromString(string);
    }
}
