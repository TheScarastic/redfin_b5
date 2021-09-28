package com.android.systemui.assist.ui;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.Log;
import android.util.PathParser;
import com.android.systemui.R$string;
import com.android.systemui.assist.ui.CornerPathRenderer;
/* loaded from: classes.dex */
public final class PathSpecCornerPathRenderer extends CornerPathRenderer {
    private final int mBottomCornerRadius;
    private final int mHeight;
    private final float mPathScale;
    private final Path mRoundedPath;
    private final int mTopCornerRadius;
    private final int mWidth;
    private final Path mPath = new Path();
    private final Matrix mMatrix = new Matrix();

    public PathSpecCornerPathRenderer(Context context) {
        this.mWidth = DisplayUtils.getWidth(context);
        this.mHeight = DisplayUtils.getHeight(context);
        this.mBottomCornerRadius = DisplayUtils.getCornerRadiusBottom(context);
        this.mTopCornerRadius = DisplayUtils.getCornerRadiusTop(context);
        Path createPathFromPathData = PathParser.createPathFromPathData(context.getResources().getString(R$string.config_rounded_mask));
        if (createPathFromPathData == null) {
            Log.e("PathSpecCornerPathRenderer", "No rounded corner path found!");
            this.mRoundedPath = new Path();
        } else {
            this.mRoundedPath = createPathFromPathData;
        }
        RectF rectF = new RectF();
        this.mRoundedPath.computeBounds(rectF, true);
        this.mPathScale = Math.min(Math.abs(rectF.right - rectF.left), Math.abs(rectF.top - rectF.bottom));
    }

    @Override // com.android.systemui.assist.ui.CornerPathRenderer
    public Path getCornerPath(CornerPathRenderer.Corner corner) {
        int i;
        int i2;
        int i3;
        if (this.mRoundedPath.isEmpty()) {
            return this.mRoundedPath;
        }
        int i4 = AnonymousClass1.$SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[corner.ordinal()];
        int i5 = 0;
        if (i4 == 1) {
            i = this.mTopCornerRadius;
            i3 = 0;
            i2 = 0;
        } else if (i4 == 2) {
            i = this.mTopCornerRadius;
            i5 = 90;
            i3 = this.mWidth;
            i2 = 0;
        } else if (i4 != 3) {
            i = this.mBottomCornerRadius;
            i2 = this.mHeight;
            i5 = 270;
            i3 = 0;
        } else {
            i = this.mBottomCornerRadius;
            i5 = 180;
            i3 = this.mWidth;
            i2 = this.mHeight;
        }
        this.mPath.reset();
        this.mMatrix.reset();
        this.mPath.addPath(this.mRoundedPath);
        Matrix matrix = this.mMatrix;
        float f = (float) i;
        float f2 = this.mPathScale;
        matrix.preScale(f / f2, f / f2);
        this.mMatrix.postRotate((float) i5);
        this.mMatrix.postTranslate((float) i3, (float) i2);
        this.mPath.transform(this.mMatrix);
        return this.mPath;
    }

    /* renamed from: com.android.systemui.assist.ui.PathSpecCornerPathRenderer$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner;

        static {
            int[] iArr = new int[CornerPathRenderer.Corner.values().length];
            $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner = iArr;
            try {
                iArr[CornerPathRenderer.Corner.TOP_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[CornerPathRenderer.Corner.TOP_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[CornerPathRenderer.Corner.BOTTOM_RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[CornerPathRenderer.Corner.BOTTOM_LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }
}
