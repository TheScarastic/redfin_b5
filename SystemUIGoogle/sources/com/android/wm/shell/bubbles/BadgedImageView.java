package com.android.wm.shell.bubbles;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Outline;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.PathParser;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.ImageView;
import com.android.launcher3.icons.DotRenderer;
import com.android.launcher3.icons.IconNormalizer;
import com.android.wm.shell.animation.Interpolators;
import java.util.EnumSet;
/* loaded from: classes2.dex */
public class BadgedImageView extends ImageView {
    private float mAnimatingToDotScale;
    private BubbleViewProvider mBubble;
    private int mDotColor;
    private boolean mDotIsAnimating;
    private DotRenderer mDotRenderer;
    private float mDotScale;
    private final EnumSet<SuppressionFlag> mDotSuppressionFlags;
    private DotRenderer.DrawParams mDrawParams;
    private boolean mOnLeft;
    private Paint mPaint;
    private BubblePositioner mPositioner;
    private Rect mTempBounds;

    /* loaded from: classes2.dex */
    public enum SuppressionFlag {
        FLYOUT_VISIBLE,
        BEHIND_STACK
    }

    public BadgedImageView(Context context) {
        this(context, null);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public BadgedImageView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mDotSuppressionFlags = EnumSet.of(SuppressionFlag.FLYOUT_VISIBLE);
        this.mDotScale = 0.0f;
        this.mAnimatingToDotScale = 0.0f;
        this.mDotIsAnimating = false;
        this.mPaint = new Paint(1);
        this.mTempBounds = new Rect();
        this.mDrawParams = new DotRenderer.DrawParams();
        setFocusable(true);
        setClickable(true);
        setOutlineProvider(new ViewOutlineProvider() { // from class: com.android.wm.shell.bubbles.BadgedImageView.1
            @Override // android.view.ViewOutlineProvider
            public void getOutline(View view, Outline outline) {
                BadgedImageView.this.getOutline(outline);
            }
        });
    }

    public void getOutline(Outline outline) {
        int bubbleSize = this.mPositioner.getBubbleSize();
        int normalizedCircleSize = IconNormalizer.getNormalizedCircleSize(bubbleSize);
        int i = (bubbleSize - normalizedCircleSize) / 2;
        int i2 = normalizedCircleSize + i;
        outline.setOval(i, i, i2, i2);
    }

    public void initialize(BubblePositioner bubblePositioner) {
        this.mPositioner = bubblePositioner;
        this.mDotRenderer = new DotRenderer(this.mPositioner.getBubbleSize(), PathParser.createPathFromPathData(getResources().getString(17039948)), 100);
    }

    public void showDotAndBadge(boolean z) {
        removeDotSuppressionFlag(SuppressionFlag.BEHIND_STACK);
        animateDotBadgePositions(z);
    }

    public void hideDotAndBadge(boolean z) {
        addDotSuppressionFlag(SuppressionFlag.BEHIND_STACK);
        this.mOnLeft = z;
        hideBadge();
    }

    public void setRenderedBubble(BubbleViewProvider bubbleViewProvider) {
        this.mBubble = bubbleViewProvider;
        if (this.mDotSuppressionFlags.contains(SuppressionFlag.BEHIND_STACK)) {
            hideBadge();
        } else {
            showBadge();
        }
        this.mDotColor = bubbleViewProvider.getDotColor();
        drawDot(bubbleViewProvider.getDotPath());
    }

    @Override // android.widget.ImageView, android.view.View
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (shouldDrawDot()) {
            getDrawingRect(this.mTempBounds);
            DotRenderer.DrawParams drawParams = this.mDrawParams;
            drawParams.color = this.mDotColor;
            drawParams.iconBounds = this.mTempBounds;
            drawParams.leftAlign = this.mOnLeft;
            drawParams.scale = this.mDotScale;
            this.mDotRenderer.draw(canvas, drawParams);
        }
    }

    public void addDotSuppressionFlag(SuppressionFlag suppressionFlag) {
        if (this.mDotSuppressionFlags.add(suppressionFlag)) {
            updateDotVisibility(suppressionFlag == SuppressionFlag.BEHIND_STACK);
        }
    }

    public void removeDotSuppressionFlag(SuppressionFlag suppressionFlag) {
        if (this.mDotSuppressionFlags.remove(suppressionFlag)) {
            updateDotVisibility(suppressionFlag == SuppressionFlag.BEHIND_STACK);
        }
    }

    public void updateDotVisibility(boolean z) {
        float f = shouldDrawDot() ? 1.0f : 0.0f;
        if (z) {
            animateDotScale(f, null);
            return;
        }
        this.mDotScale = f;
        this.mAnimatingToDotScale = f;
        invalidate();
    }

    void drawDot(Path path) {
        this.mDotRenderer = new DotRenderer(this.mPositioner.getBubbleSize(), path, 100);
        invalidate();
    }

    void setDotScale(float f) {
        this.mDotScale = f;
        invalidate();
    }

    boolean getDotOnLeft() {
        return this.mOnLeft;
    }

    public float[] getDotCenter() {
        float[] fArr;
        if (this.mOnLeft) {
            fArr = this.mDotRenderer.getLeftDotPosition();
        } else {
            fArr = this.mDotRenderer.getRightDotPosition();
        }
        getDrawingRect(this.mTempBounds);
        return new float[]{((float) this.mTempBounds.width()) * fArr[0], ((float) this.mTempBounds.height()) * fArr[1]};
    }

    public String getKey() {
        BubbleViewProvider bubbleViewProvider = this.mBubble;
        if (bubbleViewProvider != null) {
            return bubbleViewProvider.getKey();
        }
        return null;
    }

    public int getDotColor() {
        return this.mDotColor;
    }

    void animateDotBadgePositions(boolean z) {
        this.mOnLeft = z;
        if (z != getDotOnLeft() && shouldDrawDot()) {
            animateDotScale(0.0f, new Runnable() { // from class: com.android.wm.shell.bubbles.BadgedImageView$$ExternalSyntheticLambda1
                @Override // java.lang.Runnable
                public final void run() {
                    BadgedImageView.$r8$lambda$ojUBGj1BG046sFWMgq39STntRq4(BadgedImageView.this);
                }
            });
        }
        showBadge();
    }

    public /* synthetic */ void lambda$animateDotBadgePositions$0() {
        invalidate();
        animateDotScale(1.0f, null);
    }

    public void setDotBadgeOnLeft(boolean z) {
        this.mOnLeft = z;
        invalidate();
        showBadge();
    }

    private boolean shouldDrawDot() {
        return this.mDotIsAnimating || (this.mBubble.showDot() && this.mDotSuppressionFlags.isEmpty());
    }

    private void animateDotScale(float f, Runnable runnable) {
        boolean z = true;
        this.mDotIsAnimating = true;
        if (this.mAnimatingToDotScale == f || !shouldDrawDot()) {
            this.mDotIsAnimating = false;
            return;
        }
        this.mAnimatingToDotScale = f;
        if (f <= 0.0f) {
            z = false;
        }
        clearAnimation();
        animate().setDuration(200).setInterpolator(Interpolators.FAST_OUT_SLOW_IN).setUpdateListener(new ValueAnimator.AnimatorUpdateListener(z) { // from class: com.android.wm.shell.bubbles.BadgedImageView$$ExternalSyntheticLambda0
            public final /* synthetic */ boolean f$1;

            {
                this.f$1 = r2;
            }

            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public final void onAnimationUpdate(ValueAnimator valueAnimator) {
                BadgedImageView.$r8$lambda$N6KHL4ZJBhm9rHWCHgyu_ws3UOA(BadgedImageView.this, this.f$1, valueAnimator);
            }
        }).withEndAction(new Runnable(z, runnable) { // from class: com.android.wm.shell.bubbles.BadgedImageView$$ExternalSyntheticLambda2
            public final /* synthetic */ boolean f$1;
            public final /* synthetic */ Runnable f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                BadgedImageView.$r8$lambda$s2TkTq4kjtBRDAlbGP_L9UvDq9Y(BadgedImageView.this, this.f$1, this.f$2);
            }
        }).start();
    }

    public /* synthetic */ void lambda$animateDotScale$1(boolean z, ValueAnimator valueAnimator) {
        float animatedFraction = valueAnimator.getAnimatedFraction();
        if (!z) {
            animatedFraction = 1.0f - animatedFraction;
        }
        setDotScale(animatedFraction);
    }

    public /* synthetic */ void lambda$animateDotScale$2(boolean z, Runnable runnable) {
        setDotScale(z ? 1.0f : 0.0f);
        this.mDotIsAnimating = false;
        if (runnable != null) {
            runnable.run();
        }
    }

    void showBadge() {
        Bitmap appBadge = this.mBubble.getAppBadge();
        if (appBadge == null) {
            setImageBitmap(this.mBubble.getBubbleIcon());
            return;
        }
        Canvas canvas = new Canvas();
        Bitmap bubbleIcon = this.mBubble.getBubbleIcon();
        Bitmap copy = bubbleIcon.copy(bubbleIcon.getConfig(), true);
        canvas.setDrawFilter(new PaintFlagsDrawFilter(4, 2));
        canvas.setBitmap(copy);
        int width = copy.getWidth();
        int i = (int) (((float) width) * 0.444f);
        Rect rect = new Rect();
        if (this.mOnLeft) {
            rect.set(0, width - i, i, width);
        } else {
            int i2 = width - i;
            rect.set(i2, i2, width, width);
        }
        canvas.drawBitmap(appBadge, (Rect) null, rect, this.mPaint);
        canvas.setBitmap(null);
        setImageBitmap(copy);
    }

    void hideBadge() {
        setImageBitmap(this.mBubble.getBubbleIcon());
    }
}
