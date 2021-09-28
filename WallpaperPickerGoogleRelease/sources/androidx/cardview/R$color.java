package androidx.cardview;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;
import com.android.systemui.shared.R;
import com.android.systemui.shared.system.QuickStepContract;
import com.android.wallpaper.util.DisplayMetricsRetriever;
/* loaded from: classes.dex */
public class R$color {
    public static int getActivityWindowWidthPx(Activity activity) {
        Display defaultDisplay = activity.getWindowManager().getDefaultDisplay();
        Point point = new Point();
        defaultDisplay.getSize(point);
        return point.x;
    }

    public static Point getFeaturedCategoryTileSize(Activity activity) {
        Resources resources = activity.getResources();
        int activityWindowWidthPx = getActivityWindowWidthPx(activity);
        return getSquareTileSize(getNumColumns(activity, activityWindowWidthPx, 2, 2), activityWindowWidthPx, resources.getDimensionPixelSize(R.dimen.grid_item_category_padding_horizontal), resources.getDimensionPixelSize(R.dimen.category_grid_edge_space));
    }

    public static int getNumColumns(Context context, int i, int i2, int i3) {
        return ((int) (((float) i) / DisplayMetricsRetriever.getInstance().getDisplayMetrics(context.getResources(), ((WindowManager) context.getSystemService("window")).getDefaultDisplay()).density)) < 732 ? i2 : i3;
    }

    public static float getPreviewCornerRadius(Activity activity, int i) {
        return QuickStepContract.getWindowCornerRadius(Resources.getSystem()) / (((float) getActivityWindowWidthPx(activity)) / ((float) i));
    }

    public static Point getSquareTileSize(int i, int i2, int i3, int i4) {
        int round = Math.round(((float) ((i2 - ((i3 * 2) * i)) - (i4 * 2))) / ((float) i));
        return new Point(round, round);
    }
}
