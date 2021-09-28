package com.android.customization.model.theme;

import com.android.customization.model.CustomizationManager;
import java.util.HashSet;
import java.util.Set;
/* loaded from: classes.dex */
public class ThemeManager implements CustomizationManager<?> {
    public static final Set<String> THEME_CATEGORIES;

    static {
        HashSet hashSet = new HashSet();
        THEME_CATEGORIES = hashSet;
        hashSet.add("android.theme.customization.accent_color");
        hashSet.add("android.theme.customization.font");
        hashSet.add("android.theme.customization.adaptive_icon_shape");
        hashSet.add("android.theme.customization.icon_pack.android");
        hashSet.add("android.theme.customization.icon_pack.settings");
        hashSet.add("android.theme.customization.icon_pack.systemui");
        hashSet.add("android.theme.customization.icon_pack.launcher");
        hashSet.add("android.theme.customization.icon_pack.themepicker");
    }
}
