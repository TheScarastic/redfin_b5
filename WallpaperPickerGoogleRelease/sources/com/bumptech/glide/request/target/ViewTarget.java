package com.bumptech.glide.request.target;

import android.content.Context;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import com.bumptech.glide.Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0;
import com.bumptech.glide.request.Request;
import com.bumptech.glide.request.SingleRequest;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
@Deprecated
/* loaded from: classes.dex */
public abstract class ViewTarget<T extends View, Z> extends BaseTarget<Z> {
    public final SizeDeterminer sizeDeterminer;
    public final T view;

    /* loaded from: classes.dex */
    public static final class SizeDeterminer {
        public static Integer maxDisplayLength;
        public final List<SizeReadyCallback> cbs = new ArrayList();
        public SizeDeterminerLayoutListener layoutListener;
        public final View view;

        /* loaded from: classes.dex */
        public static final class SizeDeterminerLayoutListener implements ViewTreeObserver.OnPreDrawListener {
            public final WeakReference<SizeDeterminer> sizeDeterminerRef;

            public SizeDeterminerLayoutListener(SizeDeterminer sizeDeterminer) {
                this.sizeDeterminerRef = new WeakReference<>(sizeDeterminer);
            }

            @Override // android.view.ViewTreeObserver.OnPreDrawListener
            public boolean onPreDraw() {
                if (Log.isLoggable("ViewTarget", 2)) {
                    String valueOf = String.valueOf(this);
                    StringBuilder sb = new StringBuilder(valueOf.length() + 50);
                    sb.append("OnGlobalLayoutListener called attachStateListener=");
                    sb.append(valueOf);
                    Log.v("ViewTarget", sb.toString());
                }
                SizeDeterminer sizeDeterminer = this.sizeDeterminerRef.get();
                if (sizeDeterminer == null || sizeDeterminer.cbs.isEmpty()) {
                    return true;
                }
                int targetWidth = sizeDeterminer.getTargetWidth();
                int targetHeight = sizeDeterminer.getTargetHeight();
                if (!sizeDeterminer.isViewStateAndSizeValid(targetWidth, targetHeight)) {
                    return true;
                }
                Iterator it = new ArrayList(sizeDeterminer.cbs).iterator();
                while (it.hasNext()) {
                    ((SizeReadyCallback) it.next()).onSizeReady(targetWidth, targetHeight);
                }
                sizeDeterminer.clearCallbacksAndListener();
                return true;
            }
        }

        public SizeDeterminer(View view) {
            this.view = view;
        }

        public void clearCallbacksAndListener() {
            ViewTreeObserver viewTreeObserver = this.view.getViewTreeObserver();
            if (viewTreeObserver.isAlive()) {
                viewTreeObserver.removeOnPreDrawListener(this.layoutListener);
            }
            this.layoutListener = null;
            this.cbs.clear();
        }

        public final int getTargetDimen(int i, int i2, int i3) {
            int i4 = i2 - i3;
            if (i4 > 0) {
                return i4;
            }
            int i5 = i - i3;
            if (i5 > 0) {
                return i5;
            }
            if (this.view.isLayoutRequested() || i2 != -2) {
                return 0;
            }
            if (Log.isLoggable("ViewTarget", 4)) {
                Log.i("ViewTarget", "Glide treats LayoutParams.WRAP_CONTENT as a request for an image the size of this device's screen dimensions. If you want to load the original image and are ok with the corresponding memory cost and OOMs (depending on the input size), use .override(Target.SIZE_ORIGINAL). Otherwise, use LayoutParams.MATCH_PARENT, set layout_width and layout_height to fixed dimension, or use .override() with fixed dimensions.");
            }
            Context context = this.view.getContext();
            if (maxDisplayLength == null) {
                WindowManager windowManager = (WindowManager) context.getSystemService("window");
                Objects.requireNonNull(windowManager, "Argument must not be null");
                Display defaultDisplay = windowManager.getDefaultDisplay();
                Point point = new Point();
                defaultDisplay.getSize(point);
                maxDisplayLength = Integer.valueOf(Math.max(point.x, point.y));
            }
            return maxDisplayLength.intValue();
        }

        public final int getTargetHeight() {
            int paddingBottom = this.view.getPaddingBottom() + this.view.getPaddingTop();
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            return getTargetDimen(this.view.getHeight(), layoutParams != null ? layoutParams.height : 0, paddingBottom);
        }

        public final int getTargetWidth() {
            int paddingRight = this.view.getPaddingRight() + this.view.getPaddingLeft();
            ViewGroup.LayoutParams layoutParams = this.view.getLayoutParams();
            return getTargetDimen(this.view.getWidth(), layoutParams != null ? layoutParams.width : 0, paddingRight);
        }

        public final boolean isViewStateAndSizeValid(int i, int i2) {
            if (!(i > 0 || i == Integer.MIN_VALUE)) {
                return false;
            }
            return i2 > 0 || i2 == Integer.MIN_VALUE;
        }
    }

    public ViewTarget(T t) {
        Objects.requireNonNull(t, "Argument must not be null");
        this.view = t;
        this.sizeDeterminer = new SizeDeterminer(t);
    }

    @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
    public Request getRequest() {
        Object tag = this.view.getTag();
        if (tag == null) {
            return null;
        }
        if (tag instanceof Request) {
            return (Request) tag;
        }
        throw new IllegalArgumentException("You must not call setTag() on a view Glide is targeting");
    }

    @Override // com.bumptech.glide.request.target.Target
    public void getSize(SizeReadyCallback sizeReadyCallback) {
        SizeDeterminer sizeDeterminer = this.sizeDeterminer;
        int targetWidth = sizeDeterminer.getTargetWidth();
        int targetHeight = sizeDeterminer.getTargetHeight();
        if (sizeDeterminer.isViewStateAndSizeValid(targetWidth, targetHeight)) {
            ((SingleRequest) sizeReadyCallback).onSizeReady(targetWidth, targetHeight);
            return;
        }
        if (!sizeDeterminer.cbs.contains(sizeReadyCallback)) {
            sizeDeterminer.cbs.add(sizeReadyCallback);
        }
        if (sizeDeterminer.layoutListener == null) {
            ViewTreeObserver viewTreeObserver = sizeDeterminer.view.getViewTreeObserver();
            SizeDeterminer.SizeDeterminerLayoutListener sizeDeterminerLayoutListener = new SizeDeterminer.SizeDeterminerLayoutListener(sizeDeterminer);
            sizeDeterminer.layoutListener = sizeDeterminerLayoutListener;
            viewTreeObserver.addOnPreDrawListener(sizeDeterminerLayoutListener);
        }
    }

    @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
    public void onLoadCleared(Drawable drawable) {
        this.sizeDeterminer.clearCallbacksAndListener();
    }

    @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
    public void onLoadStarted(Drawable drawable) {
    }

    @Override // com.bumptech.glide.request.target.Target
    public void removeCallback(SizeReadyCallback sizeReadyCallback) {
        this.sizeDeterminer.cbs.remove(sizeReadyCallback);
    }

    @Override // com.bumptech.glide.request.target.BaseTarget, com.bumptech.glide.request.target.Target
    public void setRequest(Request request) {
        this.view.setTag(request);
    }

    public String toString() {
        String valueOf = String.valueOf(this.view);
        return Registry$NoModelLoaderAvailableException$$ExternalSyntheticOutline0.m(valueOf.length() + 12, "Target for: ", valueOf);
    }
}
