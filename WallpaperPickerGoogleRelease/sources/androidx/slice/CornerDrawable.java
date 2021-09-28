package androidx.slice;

import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
/* loaded from: classes.dex */
public class CornerDrawable extends InsetDrawable {
    public float mCornerRadius;
    public final Path mPath = new Path();

    public CornerDrawable(Drawable drawable, float f) {
        super(drawable, 0);
        this.mCornerRadius = f;
    }

    @Override // android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.clipPath(this.mPath);
        super.draw(canvas);
        canvas.restoreToCount(save);
    }

    @Override // android.graphics.drawable.InsetDrawable, android.graphics.drawable.Drawable, android.graphics.drawable.DrawableWrapper
    public void onBoundsChange(Rect rect) {
        Path path = this.mPath;
        if (path != null) {
            path.reset();
            Path path2 = this.mPath;
            RectF rectF = new RectF(rect);
            float f = this.mCornerRadius;
            path2.addRoundRect(rectF, f, f, Path.Direction.CW);
        }
        super.onBoundsChange(rect);
    }
}
