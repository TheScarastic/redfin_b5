package com.android.systemui.shared.system;

import android.graphics.HardwareRenderer;
import android.view.SurfaceControl;
import android.view.View;
import android.view.ViewRootImpl;
import java.util.function.LongConsumer;
/* loaded from: classes.dex */
public class ViewRootImplCompat {
    private final ViewRootImpl mViewRoot;

    public ViewRootImplCompat(View view) {
        ViewRootImpl viewRootImpl;
        if (view == null) {
            viewRootImpl = null;
        } else {
            viewRootImpl = view.getViewRootImpl();
        }
        this.mViewRoot = viewRootImpl;
    }

    public SurfaceControl getRenderSurfaceControl() {
        ViewRootImpl viewRootImpl = this.mViewRoot;
        if (viewRootImpl == null) {
            return null;
        }
        return viewRootImpl.getSurfaceControl();
    }

    public View getView() {
        ViewRootImpl viewRootImpl = this.mViewRoot;
        if (viewRootImpl == null) {
            return null;
        }
        return viewRootImpl.getView();
    }

    public boolean isValid() {
        return this.mViewRoot != null;
    }

    public void mergeWithNextTransaction(SurfaceControl.Transaction transaction, long j) {
        ViewRootImpl viewRootImpl = this.mViewRoot;
        if (viewRootImpl != null) {
            viewRootImpl.mergeWithNextTransaction(transaction, j);
        } else {
            transaction.apply();
        }
    }

    public void registerRtFrameCallback(final LongConsumer longConsumer) {
        ViewRootImpl viewRootImpl = this.mViewRoot;
        if (viewRootImpl != null) {
            viewRootImpl.registerRtFrameCallback(new HardwareRenderer.FrameDrawingCallback() { // from class: com.android.systemui.shared.system.ViewRootImplCompat.1
                public void onFrameDraw(long j) {
                    longConsumer.accept(j);
                }
            });
        }
    }
}
