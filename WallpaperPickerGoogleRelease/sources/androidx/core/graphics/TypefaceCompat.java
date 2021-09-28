package androidx.core.graphics;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.fonts.Font;
import android.graphics.fonts.FontFamily;
import androidx.collection.LruCache;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.provider.FontsContractCompat$FontRequestCallback;
import java.util.Objects;
@SuppressLint({"NewApi"})
/* loaded from: classes.dex */
public class TypefaceCompat {
    public static final TypefaceCompatBaseImpl sTypefaceCompatImpl = new TypefaceCompatApi29Impl();
    public static final LruCache<String, Typeface> sTypefaceCache = new LruCache<>(16);

    /* loaded from: classes.dex */
    public static class ResourcesCallbackAdapter extends FontsContractCompat$FontRequestCallback {
        public ResourcesCompat.FontCallback mFontCallback;

        public ResourcesCallbackAdapter(ResourcesCompat.FontCallback fontCallback) {
            this.mFontCallback = fontCallback;
        }
    }

    public static void clearCache() {
        sTypefaceCache.trimToSize(-1);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0024, code lost:
        if (r0.equals(r4) == false) goto L_0x0028;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static android.graphics.Typeface createFromResourcesFamilyXml(android.content.Context r6, androidx.core.content.res.FontResourcesParserCompat.FamilyResourceEntry r7, android.content.res.Resources r8, int r9, int r10, androidx.core.content.res.ResourcesCompat.FontCallback r11, android.os.Handler r12, boolean r13) {
        /*
        // Method dump skipped, instructions count: 357
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.core.graphics.TypefaceCompat.createFromResourcesFamilyXml(android.content.Context, androidx.core.content.res.FontResourcesParserCompat$FamilyResourceEntry, android.content.res.Resources, int, int, androidx.core.content.res.ResourcesCompat$FontCallback, android.os.Handler, boolean):android.graphics.Typeface");
    }

    public static Typeface createFromResourcesFontFile(Context context, Resources resources, int i, String str, int i2) {
        Typeface typeface;
        Objects.requireNonNull((TypefaceCompatApi29Impl) sTypefaceCompatImpl);
        try {
            Font build = new Font.Builder(resources, i).build();
            typeface = new Typeface.CustomFallbackBuilder(new FontFamily.Builder(build).build()).setStyle(build.getStyle()).build();
        } catch (Exception unused) {
            typeface = null;
        }
        if (typeface != null) {
            sTypefaceCache.put(createResourceUid(resources, i, i2), typeface);
        }
        return typeface;
    }

    public static String createResourceUid(Resources resources, int i, int i2) {
        return resources.getResourcePackageName(i) + "-" + i + "-" + i2;
    }
}
