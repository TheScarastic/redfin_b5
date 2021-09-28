package com.android.systemui.screenshot;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class ScreenshotSelectorView extends View {
    private Consumer<Rect> mOnScreenshotSelected;
    private final Paint mPaintBackground;
    private final Paint mPaintSelection;
    private Rect mSelectionRect;
    private Point mStartPoint;

    public ScreenshotSelectorView(Context context) {
        this(context, null);
    }

    public ScreenshotSelectorView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        Paint paint = new Paint(-16777216);
        this.mPaintBackground = paint;
        paint.setAlpha(160);
        Paint paint2 = new Paint(0);
        this.mPaintSelection = paint2;
        paint2.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        setOnTouchListener(new View.OnTouchListener() { // from class: com.android.systemui.screenshot.ScreenshotSelectorView$$ExternalSyntheticLambda0
            @Override // android.view.View.OnTouchListener
            public final boolean onTouch(View view, MotionEvent motionEvent) {
                return ScreenshotSelectorView.$r8$lambda$2c3GI_3mq6GhZV1VWNFjyOAGnks(ScreenshotSelectorView.this, view, motionEvent);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ boolean lambda$new$0(View view, MotionEvent motionEvent) {
        int action = motionEvent.getAction();
        if (action == 0) {
            startSelection((int) motionEvent.getX(), (int) motionEvent.getY());
            return true;
        } else if (action == 1) {
            setVisibility(8);
            Rect selectionRect = getSelectionRect();
            if (!(this.mOnScreenshotSelected == null || selectionRect == null || selectionRect.width() == 0 || selectionRect.height() == 0)) {
                this.mOnScreenshotSelected.accept(selectionRect);
            }
            stopSelection();
            return true;
        } else if (action != 2) {
            return false;
        } else {
            updateSelection((int) motionEvent.getX(), (int) motionEvent.getY());
            return true;
        }
    }

    @Override // android.view.View
    public void draw(Canvas canvas) {
        canvas.drawRect((float) ((View) this).mLeft, (float) ((View) this).mTop, (float) ((View) this).mRight, (float) ((View) this).mBottom, this.mPaintBackground);
        Rect rect = this.mSelectionRect;
        if (rect != null) {
            canvas.drawRect(rect, this.mPaintSelection);
        }
    }

    /* access modifiers changed from: package-private */
    public void setOnScreenshotSelected(Consumer<Rect> consumer) {
        this.mOnScreenshotSelected = consumer;
    }

    /* access modifiers changed from: package-private */
    public void stop() {
        if (getSelectionRect() != null) {
            stopSelection();
        }
    }

    private void startSelection(int i, int i2) {
        this.mStartPoint = new Point(i, i2);
        this.mSelectionRect = new Rect(i, i2, i, i2);
    }

    private void updateSelection(int i, int i2) {
        Rect rect = this.mSelectionRect;
        if (rect != null) {
            rect.left = Math.min(this.mStartPoint.x, i);
            this.mSelectionRect.right = Math.max(this.mStartPoint.x, i);
            this.mSelectionRect.top = Math.min(this.mStartPoint.y, i2);
            this.mSelectionRect.bottom = Math.max(this.mStartPoint.y, i2);
            invalidate();
        }
    }

    private Rect getSelectionRect() {
        return this.mSelectionRect;
    }

    private void stopSelection() {
        this.mStartPoint = null;
        this.mSelectionRect = null;
    }
}
