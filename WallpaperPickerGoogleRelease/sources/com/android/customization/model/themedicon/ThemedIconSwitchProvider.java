package com.android.customization.model.themedicon;

import android.content.ContentResolver;
import android.content.Context;
import com.android.customization.module.CustomizationPreferences;
import com.android.systemui.shared.R;
import com.android.wallpaper.module.InjectorProvider;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
/* loaded from: classes.dex */
public class ThemedIconSwitchProvider {
    public static ThemedIconSwitchProvider sThemedIconSwitchProvider;
    public final ContentResolver mContentResolver;
    public final CustomizationPreferences mCustomizationPreferences;
    public final ExecutorService mExecutorService = Executors.newSingleThreadExecutor();
    public final ThemedIconUtils mThemedIconUtils;

    /* loaded from: classes.dex */
    public interface FetchThemedIconEnabledCallback {
    }

    public ThemedIconSwitchProvider(ContentResolver contentResolver, ThemedIconUtils themedIconUtils, CustomizationPreferences customizationPreferences) {
        this.mContentResolver = contentResolver;
        this.mThemedIconUtils = themedIconUtils;
        this.mCustomizationPreferences = customizationPreferences;
    }

    public static ThemedIconSwitchProvider getInstance(Context context) {
        if (sThemedIconSwitchProvider == null) {
            Context applicationContext = context.getApplicationContext();
            sThemedIconSwitchProvider = new ThemedIconSwitchProvider(applicationContext.getContentResolver(), new ThemedIconUtils(applicationContext, applicationContext.getString(R.string.themed_icon_metadata_key)), (CustomizationPreferences) InjectorProvider.getInjector().getPreferences(applicationContext));
        }
        return sThemedIconSwitchProvider;
    }
}
