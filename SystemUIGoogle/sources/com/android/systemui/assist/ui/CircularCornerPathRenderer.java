package com.android.systemui.assist.ui;

import android.content.Context;
import android.graphics.Path;
import com.android.systemui.assist.ui.CornerPathRenderer;
/* loaded from: classes.dex */
public final class CircularCornerPathRenderer extends CornerPathRenderer {
    private final int mCornerRadiusBottom;
    private final int mCornerRadiusTop;
    private final int mHeight;
    private final Path mPath = new Path();
    private final int mWidth;

    public CircularCornerPathRenderer(Context context) {
        this.mCornerRadiusBottom = DisplayUtils.getCornerRadiusBottom(context);
        this.mCornerRadiusTop = DisplayUtils.getCornerRadiusTop(context);
        this.mHeight = DisplayUtils.getHeight(context);
        this.mWidth = DisplayUtils.getWidth(context);
    }

    /* renamed from: com.android.systemui.assist.ui.CircularCornerPathRenderer$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner;

        static {
            int[] iArr = new int[CornerPathRenderer.Corner.values().length];
            $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner = iArr;
            try {
                iArr[CornerPathRenderer.Corner.BOTTOM_LEFT.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[CornerPathRenderer.Corner.BOTTOM_RIGHT.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[CornerPathRenderer.Corner.TOP_RIGHT.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
            try {
                $SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[CornerPathRenderer.Corner.TOP_LEFT.ordinal()] = 4;
            } catch (NoSuchFieldError unused4) {
            }
        }
    }

    @Override // com.android.systemui.assist.ui.CornerPathRenderer
    public Path getCornerPath(CornerPathRenderer.Corner corner) {
        this.mPath.reset();
        int i = AnonymousClass1.$SwitchMap$com$android$systemui$assist$ui$CornerPathRenderer$Corner[corner.ordinal()];
        if (i == 1) {
            this.mPath.moveTo(0.0f, (float) (this.mHeight - this.mCornerRadiusBottom));
            Path path = this.mPath;
            int i2 = this.mHeight;
            int i3 = this.mCornerRadiusBottom;
            path.arcTo(0.0f, (float) (i2 - (i3 * 2)), (float) (i3 * 2), (float) i2, 180.0f, -90.0f, true);
        } else if (i == 2) {
            this.mPath.moveTo((float) (this.mWidth - this.mCornerRadiusBottom), (float) this.mHeight);
            Path path2 = this.mPath;
            int i4 = this.mWidth;
            int i5 = this.mCornerRadiusBottom;
            int i6 = this.mHeight;
            path2.arcTo((float) (i4 - (i5 * 2)), (float) (i6 - (i5 * 2)), (float) i4, (float) i6, 90.0f, -90.0f, true);
        } else if (i == 3) {
            this.mPath.moveTo((float) this.mWidth, (float) this.mCornerRadiusTop);
            Path path3 = this.mPath;
            int i7 = this.mWidth;
            int i8 = this.mCornerRadiusTop;
            path3.arcTo((float) (i7 - (i8 * 2)), 0.0f, (float) i7, (float) (i8 * 2), 0.0f, -90.0f, true);
        } else if (i == 4) {
            this.mPath.moveTo((float) this.mCornerRadiusTop, 0.0f);
            Path path4 = this.mPath;
            int i9 = this.mCornerRadiusTop;
            path4.arcTo(0.0f, 0.0f, (float) (i9 * 2), (float) (i9 * 2), 270.0f, -90.0f, true);
        }
        return this.mPath;
    }
}
