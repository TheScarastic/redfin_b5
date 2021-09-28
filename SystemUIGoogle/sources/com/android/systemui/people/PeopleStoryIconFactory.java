package com.android.systemui.people;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.IconDrawableFactory;
import android.util.Log;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import com.android.settingslib.Utils;
import com.android.systemui.R$color;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class PeopleStoryIconFactory implements AutoCloseable {
    private int mAccentColor;
    private Context mContext;
    private float mDensity;
    private final int mIconBitmapSize;
    final IconDrawableFactory mIconDrawableFactory;
    private float mIconSize;
    private int mImportantConversationColor;
    final PackageManager mPackageManager;

    @Override // java.lang.AutoCloseable
    public void close() {
    }

    /* access modifiers changed from: package-private */
    public PeopleStoryIconFactory(Context context, PackageManager packageManager, IconDrawableFactory iconDrawableFactory, int i) {
        context.setTheme(16974563);
        float f = (float) i;
        this.mIconBitmapSize = (int) (context.getResources().getDisplayMetrics().density * f);
        float f2 = context.getResources().getDisplayMetrics().density;
        this.mDensity = f2;
        this.mIconSize = f2 * f;
        this.mPackageManager = packageManager;
        this.mIconDrawableFactory = iconDrawableFactory;
        this.mImportantConversationColor = context.getColor(R$color.important_conversation);
        this.mAccentColor = Utils.getColorAttr(context, 17956901).getDefaultColor();
        this.mContext = context;
    }

    private Drawable getAppBadge(String str, int i) {
        try {
            return Utils.getBadgedIcon(this.mContext, this.mPackageManager.getApplicationInfoAsUser(str, 128, i));
        } catch (PackageManager.NameNotFoundException unused) {
            return this.mPackageManager.getDefaultActivityIcon();
        }
    }

    public Drawable getPeopleTileDrawable(RoundedBitmapDrawable roundedBitmapDrawable, String str, int i, boolean z, boolean z2) {
        return new PeopleStoryIconDrawable(roundedBitmapDrawable, getAppBadge(str, i), this.mIconBitmapSize, this.mImportantConversationColor, z, this.mIconSize, this.mDensity, this.mAccentColor, z2);
    }

    /* loaded from: classes.dex */
    public static class PeopleStoryIconDrawable extends Drawable {
        private RoundedBitmapDrawable mAvatar;
        private Drawable mBadgeIcon;
        private float mDensity;
        private float mFullIconSize;
        private int mIconSize;
        private Paint mPriorityRingPaint;
        private boolean mShowImportantRing;
        private boolean mShowStoryRing;
        private Paint mStoryPaint;

        @Override // android.graphics.drawable.Drawable
        public int getOpacity() {
            return -3;
        }

        @Override // android.graphics.drawable.Drawable
        public void setAlpha(int i) {
        }

        PeopleStoryIconDrawable(RoundedBitmapDrawable roundedBitmapDrawable, Drawable drawable, int i, int i2, boolean z, float f, float f2, int i3, boolean z2) {
            roundedBitmapDrawable.setCircular(true);
            this.mAvatar = roundedBitmapDrawable;
            this.mBadgeIcon = drawable;
            this.mIconSize = i;
            this.mShowImportantRing = z;
            Paint paint = new Paint();
            this.mPriorityRingPaint = paint;
            paint.setStyle(Paint.Style.FILL_AND_STROKE);
            this.mPriorityRingPaint.setColor(i2);
            this.mShowStoryRing = z2;
            Paint paint2 = new Paint();
            this.mStoryPaint = paint2;
            paint2.setStyle(Paint.Style.STROKE);
            this.mStoryPaint.setColor(i3);
            this.mFullIconSize = f;
            this.mDensity = f2;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicWidth() {
            return this.mIconSize;
        }

        @Override // android.graphics.drawable.Drawable
        public int getIntrinsicHeight() {
            return this.mIconSize;
        }

        @Override // android.graphics.drawable.Drawable
        public void draw(Canvas canvas) {
            Rect bounds = getBounds();
            float min = ((float) Math.min(bounds.height(), bounds.width())) / this.mFullIconSize;
            float f = this.mDensity;
            int i = (int) (f * 2.0f);
            int i2 = (int) (f * 2.0f);
            float f2 = (float) i2;
            this.mPriorityRingPaint.setStrokeWidth(f2);
            this.mStoryPaint.setStrokeWidth(f2);
            int i3 = (int) (this.mFullIconSize * min);
            int i4 = i3 - (i * 2);
            if (this.mAvatar != null) {
                int i5 = i4 + i;
                if (this.mShowStoryRing) {
                    float f3 = (float) (i3 / 2);
                    canvas.drawCircle(f3, f3, (float) getRadius(i4, i2), this.mStoryPaint);
                    int i6 = i2 + i;
                    i += i6;
                    i5 -= i6;
                }
                this.mAvatar.setBounds(i, i, i5, i5);
                this.mAvatar.draw(canvas);
            } else {
                Log.w("PeopleStoryIconFactory", "Null avatar icon");
            }
            int min2 = Math.min((int) (this.mDensity * 40.0f), (int) (((double) i4) / 2.4d));
            if (this.mBadgeIcon != null) {
                int i7 = i3 - min2;
                if (this.mShowImportantRing) {
                    float f4 = (float) ((min2 / 2) + i7);
                    canvas.drawCircle(f4, f4, (float) getRadius(min2, i2), this.mPriorityRingPaint);
                    i7 += i2;
                    i3 -= i2;
                }
                this.mBadgeIcon.setBounds(i7, i7, i3, i3);
                this.mBadgeIcon.draw(canvas);
                return;
            }
            Log.w("PeopleStoryIconFactory", "Null badge icon");
        }

        private int getRadius(int i, int i2) {
            return (i - i2) / 2;
        }

        @Override // android.graphics.drawable.Drawable
        public void setColorFilter(ColorFilter colorFilter) {
            RoundedBitmapDrawable roundedBitmapDrawable = this.mAvatar;
            if (roundedBitmapDrawable != null) {
                roundedBitmapDrawable.setColorFilter(colorFilter);
            }
            Drawable drawable = this.mBadgeIcon;
            if (drawable != null) {
                drawable.setColorFilter(colorFilter);
            }
        }
    }
}
