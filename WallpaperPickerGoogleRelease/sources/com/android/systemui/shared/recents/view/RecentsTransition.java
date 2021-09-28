package com.android.systemui.shared.recents.view;

import android.app.ActivityOptions;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Picture;
import android.os.Bundle;
import android.os.Handler;
import android.os.IRemoteCallback;
import android.os.RemoteException;
import android.view.View;
import java.util.function.Consumer;
/* loaded from: classes.dex */
public class RecentsTransition {
    public static ActivityOptions createAspectScaleAnimation(Context context, Handler handler, boolean z, AppTransitionAnimationSpecsFuture appTransitionAnimationSpecsFuture, final Runnable runnable) {
        return ActivityOptions.makeMultiThumbFutureAspectScaleAnimation(context, handler, appTransitionAnimationSpecsFuture != null ? appTransitionAnimationSpecsFuture.getFuture() : null, new ActivityOptions.OnAnimationStartedListener() { // from class: com.android.systemui.shared.recents.view.RecentsTransition.1
            private boolean mHandled;

            public void onAnimationStarted() {
                if (!this.mHandled) {
                    this.mHandled = true;
                    Runnable runnable2 = runnable;
                    if (runnable2 != null) {
                        runnable2.run();
                    }
                }
            }
        }, z);
    }

    public static Bitmap createHardwareBitmap(int i, int i2, Consumer<Canvas> consumer) {
        Picture picture = new Picture();
        consumer.accept(picture.beginRecording(i, i2));
        picture.endRecording();
        return Bitmap.createBitmap(picture);
    }

    public static Bitmap drawViewIntoHardwareBitmap(int i, int i2, final View view, final float f, final int i3) {
        return createHardwareBitmap(i, i2, new Consumer<Canvas>() { // from class: com.android.systemui.shared.recents.view.RecentsTransition.3
            public void accept(Canvas canvas) {
                float f2 = f;
                canvas.scale(f2, f2);
                int i4 = i3;
                if (i4 != 0) {
                    canvas.drawColor(i4);
                }
                View view2 = view;
                if (view2 != null) {
                    view2.draw(canvas);
                }
            }
        });
    }

    public static IRemoteCallback wrapStartedListener(final Handler handler, final Runnable runnable) {
        if (runnable == null) {
            return null;
        }
        return new IRemoteCallback.Stub() { // from class: com.android.systemui.shared.recents.view.RecentsTransition.2
            public void sendResult(Bundle bundle) throws RemoteException {
                handler.post(runnable);
            }
        };
    }
}
