package com.android.systemui.statusbar;

import android.app.ActivityManager;
import android.content.res.Resources;
import android.os.SystemProperties;
import android.util.IndentingPrintWriter;
import android.util.MathUtils;
import android.view.CrossWindowBlurListeners;
import android.view.SurfaceControl;
import android.view.ViewRootImpl;
import com.android.systemui.Dumpable;
import com.android.systemui.R$dimen;
import com.android.systemui.dump.DumpManager;
import java.io.FileDescriptor;
import java.io.PrintWriter;
import kotlin.Unit;
import kotlin.io.CloseableKt;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: BlurUtils.kt */
/* loaded from: classes.dex */
public class BlurUtils implements Dumpable {
    private final CrossWindowBlurListeners crossWindowBlurListeners;
    private int lastAppliedBlur;
    private final int maxBlurRadius;
    private final int minBlurRadius;
    private final Resources resources;

    public BlurUtils(Resources resources, CrossWindowBlurListeners crossWindowBlurListeners, DumpManager dumpManager) {
        Intrinsics.checkNotNullParameter(resources, "resources");
        Intrinsics.checkNotNullParameter(crossWindowBlurListeners, "crossWindowBlurListeners");
        Intrinsics.checkNotNullParameter(dumpManager, "dumpManager");
        this.resources = resources;
        this.crossWindowBlurListeners = crossWindowBlurListeners;
        this.minBlurRadius = resources.getDimensionPixelSize(R$dimen.min_window_blur_radius);
        this.maxBlurRadius = resources.getDimensionPixelSize(R$dimen.max_window_blur_radius);
        String name = BlurUtils.class.getName();
        Intrinsics.checkNotNullExpressionValue(name, "javaClass.name");
        dumpManager.registerDumpable(name, this);
    }

    public final int getMinBlurRadius() {
        return this.minBlurRadius;
    }

    public final int getMaxBlurRadius() {
        return this.maxBlurRadius;
    }

    public final int blurRadiusOfRatio(float f) {
        if (f == 0.0f) {
            return 0;
        }
        return (int) MathUtils.lerp((float) this.minBlurRadius, (float) this.maxBlurRadius, f);
    }

    public final float ratioOfBlurRadius(int i) {
        if (i == 0) {
            return 0.0f;
        }
        return MathUtils.map((float) this.minBlurRadius, (float) this.maxBlurRadius, 0.0f, 1.0f, (float) i);
    }

    /* JADX INFO: finally extract failed */
    public final void applyBlur(ViewRootImpl viewRootImpl, int i, boolean z) {
        if (viewRootImpl != null && viewRootImpl.getSurfaceControl().isValid() && supportsBlursOnWindows()) {
            SurfaceControl.Transaction createTransaction = createTransaction();
            try {
                createTransaction.setBackgroundBlurRadius(viewRootImpl.getSurfaceControl(), i);
                createTransaction.setOpaque(viewRootImpl.getSurfaceControl(), z);
                if (this.lastAppliedBlur == 0 && i != 0) {
                    createTransaction.setEarlyWakeupStart();
                }
                if (this.lastAppliedBlur != 0 && i == 0) {
                    createTransaction.setEarlyWakeupEnd();
                }
                createTransaction.apply();
                Unit unit = Unit.INSTANCE;
                CloseableKt.closeFinally(createTransaction, null);
                this.lastAppliedBlur = i;
            } catch (Throwable th) {
                try {
                    throw th;
                } catch (Throwable th2) {
                    CloseableKt.closeFinally(createTransaction, th);
                    throw th2;
                }
            }
        }
    }

    public SurfaceControl.Transaction createTransaction() {
        return new SurfaceControl.Transaction();
    }

    public boolean supportsBlursOnWindows() {
        if (!CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED || !ActivityManager.isHighEndGfx() || !this.crossWindowBlurListeners.isCrossWindowBlurEnabled() || SystemProperties.getBoolean("persist.sysui.disableBlur", false)) {
            return false;
        }
        return true;
    }

    @Override // com.android.systemui.Dumpable
    public void dump(FileDescriptor fileDescriptor, PrintWriter printWriter, String[] strArr) {
        Intrinsics.checkNotNullParameter(fileDescriptor, "fd");
        Intrinsics.checkNotNullParameter(printWriter, "pw");
        Intrinsics.checkNotNullParameter(strArr, "args");
        IndentingPrintWriter indentingPrintWriter = new IndentingPrintWriter(printWriter, "  ");
        indentingPrintWriter.println("BlurUtils:");
        indentingPrintWriter.increaseIndent();
        indentingPrintWriter.println(Intrinsics.stringPlus("minBlurRadius: ", Integer.valueOf(getMinBlurRadius())));
        indentingPrintWriter.println(Intrinsics.stringPlus("maxBlurRadius: ", Integer.valueOf(getMaxBlurRadius())));
        indentingPrintWriter.println(Intrinsics.stringPlus("supportsBlursOnWindows: ", Boolean.valueOf(supportsBlursOnWindows())));
        indentingPrintWriter.println(Intrinsics.stringPlus("CROSS_WINDOW_BLUR_SUPPORTED: ", Boolean.valueOf(CrossWindowBlurListeners.CROSS_WINDOW_BLUR_SUPPORTED)));
        indentingPrintWriter.println(Intrinsics.stringPlus("isHighEndGfx: ", Boolean.valueOf(ActivityManager.isHighEndGfx())));
    }
}
