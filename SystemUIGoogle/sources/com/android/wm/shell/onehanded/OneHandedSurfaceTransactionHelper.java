package com.android.wm.shell.onehanded;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Rect;
import android.view.SurfaceControl;
import com.android.wm.shell.R;
import java.io.PrintWriter;
/* loaded from: classes2.dex */
public class OneHandedSurfaceTransactionHelper {
    private final float mCornerRadius;
    private final float mCornerRadiusAdjustment;
    private final boolean mEnableCornerRadius;

    /* loaded from: classes2.dex */
    interface SurfaceControlTransactionFactory {
        SurfaceControl.Transaction getTransaction();
    }

    public OneHandedSurfaceTransactionHelper(Context context) {
        Resources resources = context.getResources();
        float dimension = resources.getDimension(17105497);
        this.mCornerRadiusAdjustment = dimension;
        this.mCornerRadius = resources.getDimension(17105496) - dimension;
        this.mEnableCornerRadius = resources.getBoolean(R.bool.config_one_handed_enable_round_corner);
    }

    /* access modifiers changed from: package-private */
    public OneHandedSurfaceTransactionHelper translate(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, float f) {
        transaction.setPosition(surfaceControl, 0.0f, f);
        return this;
    }

    /* access modifiers changed from: package-private */
    public OneHandedSurfaceTransactionHelper crop(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl, Rect rect) {
        transaction.setWindowCrop(surfaceControl, rect.width(), rect.height());
        return this;
    }

    /* access modifiers changed from: package-private */
    public OneHandedSurfaceTransactionHelper round(SurfaceControl.Transaction transaction, SurfaceControl surfaceControl) {
        if (this.mEnableCornerRadius) {
            transaction.setCornerRadius(surfaceControl, this.mCornerRadius);
        }
        return this;
    }

    /* access modifiers changed from: package-private */
    public void dump(PrintWriter printWriter) {
        printWriter.println("OneHandedSurfaceTransactionHelperstates: ");
        printWriter.print("  mEnableCornerRadius=");
        printWriter.println(this.mEnableCornerRadius);
        printWriter.print("  mCornerRadiusAdjustment=");
        printWriter.println(this.mCornerRadiusAdjustment);
        printWriter.print("  mCornerRadius=");
        printWriter.println(this.mCornerRadius);
    }
}
