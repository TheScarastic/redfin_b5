package com.android.wm.shell.bubbles;

import android.content.Context;
import android.content.pm.LauncherApps;
import android.content.pm.ShortcutInfo;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.drawable.AdaptiveIconDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import com.android.launcher3.icons.BaseIconFactory;
import com.android.launcher3.icons.BitmapInfo;
import com.android.launcher3.icons.ShadowGenerator;
import com.android.wm.shell.R;
/* loaded from: classes2.dex */
public class BubbleIconFactory extends BaseIconFactory {
    private int mBadgeSize = this.mContext.getResources().getDimensionPixelSize(R.dimen.bubble_badge_size);

    public BubbleIconFactory(Context context) {
        super(context, context.getResources().getConfiguration().densityDpi, context.getResources().getDimensionPixelSize(R.dimen.bubble_size));
    }

    /* access modifiers changed from: package-private */
    public Drawable getBubbleDrawable(Context context, ShortcutInfo shortcutInfo, Icon icon) {
        if (shortcutInfo != null) {
            return ((LauncherApps) context.getSystemService("launcherapps")).getShortcutIconDrawable(shortcutInfo, context.getResources().getConfiguration().densityDpi);
        }
        if (icon == null) {
            return null;
        }
        if (icon.getType() == 4 || icon.getType() == 6) {
            context.grantUriPermission(context.getPackageName(), icon.getUri(), 1);
        }
        return icon.loadDrawable(context);
    }

    /* access modifiers changed from: package-private */
    public BitmapInfo getBadgeBitmap(Drawable drawable, boolean z) {
        ShadowGenerator shadowGenerator = new ShadowGenerator(this.mBadgeSize);
        Bitmap createIconBitmap = createIconBitmap(drawable, 1.0f, this.mBadgeSize);
        if (drawable instanceof AdaptiveIconDrawable) {
            Bitmap circleBitmap = getCircleBitmap((AdaptiveIconDrawable) drawable, drawable.getIntrinsicWidth());
            int i = this.mBadgeSize;
            createIconBitmap = Bitmap.createScaledBitmap(circleBitmap, i, i, true);
        }
        if (z) {
            int color = this.mContext.getResources().getColor(R.color.important_conversation, null);
            Bitmap createBitmap = Bitmap.createBitmap(createIconBitmap.getWidth(), createIconBitmap.getHeight(), createIconBitmap.getConfig());
            Canvas canvas = new Canvas(createBitmap);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(color);
            paint.setAntiAlias(true);
            canvas.drawCircle((float) (canvas.getWidth() / 2), (float) (canvas.getHeight() / 2), (float) (canvas.getWidth() / 2), paint);
            int dimensionPixelSize = (int) ((float) this.mContext.getResources().getDimensionPixelSize(17105254));
            int i2 = dimensionPixelSize * 2;
            float f = (float) dimensionPixelSize;
            canvas.drawBitmap(Bitmap.createScaledBitmap(createIconBitmap, canvas.getWidth() - i2, canvas.getHeight() - i2, true), f, f, (Paint) null);
            shadowGenerator.recreateIcon(Bitmap.createBitmap(createBitmap), canvas);
            return createIconBitmap(createBitmap);
        }
        Canvas canvas2 = new Canvas();
        canvas2.setBitmap(createIconBitmap);
        shadowGenerator.recreateIcon(Bitmap.createBitmap(createIconBitmap), canvas2);
        return createIconBitmap(createIconBitmap);
    }

    public Bitmap getCircleBitmap(AdaptiveIconDrawable adaptiveIconDrawable, int i) {
        Drawable foreground = adaptiveIconDrawable.getForeground();
        Drawable background = adaptiveIconDrawable.getBackground();
        Bitmap createBitmap = Bitmap.createBitmap(i, i, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas();
        canvas.setBitmap(createBitmap);
        Path path = new Path();
        float f = ((float) i) / 2.0f;
        path.addCircle(f, f, f, Path.Direction.CW);
        canvas.clipPath(path);
        background.setBounds(0, 0, i, i);
        background.draw(canvas);
        int i2 = i / 5;
        int i3 = -i2;
        int i4 = i + i2;
        foreground.setBounds(i3, i3, i4, i4);
        foreground.draw(canvas);
        canvas.setBitmap(null);
        return createBitmap;
    }
}
