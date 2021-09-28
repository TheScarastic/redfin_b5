package com.android.customization.model.color;

import android.text.TextUtils;
import android.util.Log;
import com.android.customization.model.CustomizationManager;
import com.android.customization.model.CustomizationOption;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.Collectors;
import org.json.JSONException;
import org.json.JSONObject;
/* loaded from: classes.dex */
public abstract class ColorOption implements CustomizationOption<ColorOption> {
    public static final String TIMESTAMP_FIELD = "_applied_timestamp";
    public CharSequence mContentDescription;
    public final int mIndex;
    public final boolean mIsDefault;
    public final Map<String, String> mPackagesByCategory;
    public final String mTitle;

    public ColorOption(String str, Map<String, String> map, boolean z, int i) {
        this.mTitle = str;
        this.mIsDefault = z;
        this.mIndex = i;
        this.mPackagesByCategory = Collections.unmodifiableMap((Map) map.entrySet().stream().filter(ColorOption$$ExternalSyntheticLambda2.INSTANCE).collect(Collectors.toMap(ColorOption$$ExternalSyntheticLambda0.INSTANCE, ColorOption$$ExternalSyntheticLambda1.INSTANCE)));
    }

    public JSONObject getJsonPackages(boolean z) {
        JSONObject jSONObject;
        if (this.mIsDefault) {
            jSONObject = new JSONObject();
        } else {
            JSONObject jSONObject2 = new JSONObject(this.mPackagesByCategory);
            Iterator<String> keys = jSONObject2.keys();
            HashSet hashSet = new HashSet();
            while (keys.hasNext()) {
                String next = keys.next();
                if (jSONObject2.isNull(next)) {
                    hashSet.add(next);
                }
            }
            Iterator it = hashSet.iterator();
            while (it.hasNext()) {
                jSONObject2.remove((String) it.next());
            }
            jSONObject = jSONObject2;
        }
        if (z) {
            try {
                jSONObject.put(TIMESTAMP_FIELD, System.currentTimeMillis());
            } catch (JSONException unused) {
                Log.e("ColorOption", "Couldn't add timestamp to serialized themebundle");
            }
        }
        return jSONObject;
    }

    public abstract String getSource();

    @Override // com.android.customization.model.CustomizationOption
    public String getTitle() {
        return this.mTitle;
    }

    @Override // com.android.customization.model.CustomizationOption
    public boolean isActive(CustomizationManager<ColorOption> customizationManager) {
        ColorCustomizationManager colorCustomizationManager = (ColorCustomizationManager) customizationManager;
        if (this.mIsDefault) {
            String storedOverlays = colorCustomizationManager.getStoredOverlays();
            if (!TextUtils.isEmpty(storedOverlays) && !"{}".equals(storedOverlays)) {
                if (colorCustomizationManager.mCurrentOverlays == null) {
                    colorCustomizationManager.parseSettings(colorCustomizationManager.getStoredOverlays());
                }
                if (!colorCustomizationManager.mCurrentOverlays.isEmpty()) {
                    return false;
                }
            }
            return true;
        }
        if (colorCustomizationManager.mCurrentOverlays == null) {
            colorCustomizationManager.parseSettings(colorCustomizationManager.getStoredOverlays());
        }
        Map<String, String> map = colorCustomizationManager.mCurrentOverlays;
        String currentColorSource = colorCustomizationManager.getCurrentColorSource();
        return (TextUtils.isEmpty(currentColorSource) || getSource().equals(currentColorSource)) && this.mPackagesByCategory.equals(map);
    }
}
