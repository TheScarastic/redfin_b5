package androidx.lifecycle.runtime;

import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import androidx.core.graphics.ColorUtils;
import com.google.android.material.resources.MaterialAttributes;
import com.google.chrome.dongle.imax.wallpaper2.protos.ImaxWallpaperProto$Attribution;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class R$id {
    public static int getColor(View view, int i) {
        return MaterialAttributes.resolveOrThrow(view.getContext(), i, view.getClass().getCanonicalName());
    }

    public static int layer(int i, int i2, float f) {
        return ColorUtils.compositeColors(ColorUtils.setAlphaComponent(i2, Math.round(((float) Color.alpha(i2)) * f)), i);
    }

    public static List<String> parseAttributions(List<ImaxWallpaperProto$Attribution> list, String str) {
        ArrayList arrayList = new ArrayList();
        for (ImaxWallpaperProto$Attribution imaxWallpaperProto$Attribution : list) {
            arrayList.add(imaxWallpaperProto$Attribution.getText());
        }
        if (arrayList.size() == 0) {
            arrayList.add(str);
        }
        return arrayList;
    }

    public static int getColor(Context context, int i, int i2) {
        TypedValue resolve = MaterialAttributes.resolve(context, i);
        return resolve != null ? resolve.data : i2;
    }
}
