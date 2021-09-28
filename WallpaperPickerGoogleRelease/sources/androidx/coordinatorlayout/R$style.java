package androidx.coordinatorlayout;

import android.os.Build;
import com.android.systemui.shared.R;
import com.android.wallpaper.model.WallpaperInfo;
import java.util.Locale;
import java.util.concurrent.ExecutorService;
/* loaded from: classes.dex */
public class R$style {
    public static int getActionIconForType(int i) {
        if (i == 2) {
            return R.drawable.ic_case_24px;
        }
        ExecutorService executorService = WallpaperInfo.sExecutor;
        return R.drawable.ic_explore_24px;
    }

    public static int getActionLabelForType(int i) {
        if (i == 2) {
            return R.string.build_case;
        }
        ExecutorService executorService = WallpaperInfo.sExecutor;
        return R.string.explore;
    }

    public static boolean isDateInputKeyboardMissingSeparatorCharacters() {
        String str = Build.MANUFACTURER;
        Locale locale = Locale.ENGLISH;
        return str.toLowerCase(locale).equals("lge") || str.toLowerCase(locale).equals("samsung");
    }
}
