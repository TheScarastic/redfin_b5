package com.android.systemui.statusbar.notification;

import android.util.Pools;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.widget.MessagingImageMessage;
import com.android.internal.widget.MessagingPropertyAnimator;
import com.android.internal.widget.ViewClippingUtil;
import com.android.systemui.R$id;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
/* loaded from: classes.dex */
public class TransformState {
    private boolean mAlignEnd;
    private boolean mSameAsAny;
    protected TransformInfo mTransformInfo;
    protected View mTransformedView;
    public static final int ALIGN_END_TAG = R$id.align_transform_end_tag;
    private static final int TRANSFORMATION_START_X = R$id.transformation_start_x_tag;
    private static final int TRANSFORMATION_START_Y = R$id.transformation_start_y_tag;
    private static final int TRANSFORMATION_START_SCLALE_X = R$id.transformation_start_scale_x_tag;
    private static final int TRANSFORMATION_START_SCLALE_Y = R$id.transformation_start_scale_y_tag;
    private static Pools.SimplePool<TransformState> sInstancePool = new Pools.SimplePool<>(40);
    private static ViewClippingUtil.ClippingParameters CLIPPING_PARAMETERS = new ViewClippingUtil.ClippingParameters() { // from class: com.android.systemui.statusbar.notification.TransformState.1
        public boolean shouldFinish(View view) {
            if (view instanceof ExpandableNotificationRow) {
                return !((ExpandableNotificationRow) view).isChildInGroup();
            }
            return false;
        }

        public void onClippingStateChanged(View view, boolean z) {
            if (view instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) view;
                if (z) {
                    expandableNotificationRow.setClipToActualHeight(true);
                } else if (expandableNotificationRow.isChildInGroup()) {
                    expandableNotificationRow.setClipToActualHeight(false);
                }
            }
        }
    };
    private int[] mOwnPosition = new int[2];
    private float mTransformationEndY = -1.0f;
    private float mTransformationEndX = -1.0f;
    protected Interpolator mDefaultInterpolator = Interpolators.FAST_OUT_SLOW_IN;

    /* loaded from: classes.dex */
    public interface TransformInfo {
        boolean isAnimating();
    }

    public void initFrom(View view, TransformInfo transformInfo) {
        this.mTransformedView = view;
        this.mTransformInfo = transformInfo;
        this.mAlignEnd = Boolean.TRUE.equals(view.getTag(ALIGN_END_TAG));
    }

    public void transformViewFrom(TransformState transformState, float f) {
        this.mTransformedView.animate().cancel();
        if (sameAs(transformState)) {
            ensureVisible();
        } else {
            CrossFadeHelper.fadeIn(this.mTransformedView, f, true);
        }
        transformViewFullyFrom(transformState, f);
    }

    public void ensureVisible() {
        if (this.mTransformedView.getVisibility() == 4 || this.mTransformedView.getAlpha() != 1.0f) {
            this.mTransformedView.setAlpha(1.0f);
            this.mTransformedView.setVisibility(0);
        }
    }

    public void transformViewFullyFrom(TransformState transformState, float f) {
        transformViewFrom(transformState, 17, null, f);
    }

    public void transformViewFullyFrom(TransformState transformState, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        transformViewFrom(transformState, 17, customTransformation, f);
    }

    public void transformViewVerticalFrom(TransformState transformState, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        transformViewFrom(transformState, 16, customTransformation, f);
    }

    public void transformViewVerticalFrom(TransformState transformState, float f) {
        transformViewFrom(transformState, 16, null, f);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:72:0x0113  */
    /* JADX WARNING: Removed duplicated region for block: B:74:0x0118  */
    /* JADX WARNING: Removed duplicated region for block: B:76:0x011d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void transformViewFrom(com.android.systemui.statusbar.notification.TransformState r23, int r24, com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation r25, float r26) {
        /*
        // Method dump skipped, instructions count: 395
        */
        throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.notification.TransformState.transformViewFrom(com.android.systemui.statusbar.notification.TransformState, int, com.android.systemui.statusbar.ViewTransformationHelper$CustomTransformation, float):void");
    }

    /* access modifiers changed from: protected */
    public int getContentWidth() {
        return this.mTransformedView.getWidth();
    }

    protected int getContentHeight() {
        return this.mTransformedView.getHeight();
    }

    protected boolean transformScale(TransformState transformState) {
        return sameAs(transformState);
    }

    protected boolean transformRightEdge(TransformState transformState) {
        boolean z = true;
        boolean z2 = this.mAlignEnd && transformState.mAlignEnd;
        if (!this.mTransformedView.isLayoutRtl() || !transformState.mTransformedView.isLayoutRtl()) {
            z = false;
        }
        return z2 ^ z;
    }

    public boolean transformViewTo(TransformState transformState, float f) {
        this.mTransformedView.animate().cancel();
        if (!sameAs(transformState)) {
            CrossFadeHelper.fadeOut(this.mTransformedView, f);
            transformViewFullyTo(transformState, f);
            return true;
        } else if (this.mTransformedView.getVisibility() != 0) {
            return false;
        } else {
            this.mTransformedView.setAlpha(0.0f);
            this.mTransformedView.setVisibility(4);
            return false;
        }
    }

    public void transformViewFullyTo(TransformState transformState, float f) {
        transformViewTo(transformState, 17, null, f);
    }

    public void transformViewFullyTo(TransformState transformState, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        transformViewTo(transformState, 17, customTransformation, f);
    }

    public void transformViewVerticalTo(TransformState transformState, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        transformViewTo(transformState, 16, customTransformation, f);
    }

    public void transformViewVerticalTo(TransformState transformState, float f) {
        transformViewTo(transformState, 16, null, f);
    }

    private void transformViewTo(TransformState transformState, int i, ViewTransformationHelper.CustomTransformation customTransformation, float f) {
        float f2;
        boolean z;
        int i2;
        float f3;
        View view = this.mTransformedView;
        boolean z2 = (i & 1) != 0;
        boolean z3 = (i & 16) != 0;
        boolean transformScale = transformScale(transformState);
        boolean transformRightEdge = transformRightEdge(transformState);
        int contentWidth = getContentWidth();
        int contentWidth2 = transformState.getContentWidth();
        if (f == 0.0f) {
            if (z2) {
                float transformationStartX = getTransformationStartX();
                if (transformationStartX == -1.0f) {
                    transformationStartX = view.getTranslationX();
                }
                setTransformationStartX(transformationStartX);
            }
            if (z3) {
                float transformationStartY = getTransformationStartY();
                if (transformationStartY == -1.0f) {
                    transformationStartY = view.getTranslationY();
                }
                setTransformationStartY(transformationStartY);
            }
            if (!transformScale || contentWidth2 == contentWidth) {
                setTransformationStartScaleX(-1.0f);
            } else {
                setTransformationStartScaleX(view.getScaleX());
                view.setPivotX(transformRightEdge ? (float) view.getWidth() : 0.0f);
            }
            if (!transformScale || transformState.getContentHeight() == getContentHeight()) {
                setTransformationStartScaleY(-1.0f);
            } else {
                setTransformationStartScaleY(view.getScaleY());
                view.setPivotY(0.0f);
            }
            setClippingDeactivated(view, true);
        }
        float interpolation = this.mDefaultInterpolator.getInterpolation(f);
        int[] laidOutLocationOnScreen = transformState.getLaidOutLocationOnScreen();
        int[] laidOutLocationOnScreen2 = getLaidOutLocationOnScreen();
        if (z2) {
            int width = view.getWidth();
            int width2 = transformState.getTransformedView().getWidth();
            if (transformRightEdge) {
                z = false;
                i2 = (laidOutLocationOnScreen[0] + width2) - (laidOutLocationOnScreen2[0] + width);
            } else {
                z = false;
                i2 = laidOutLocationOnScreen[0] - laidOutLocationOnScreen2[0];
            }
            float f4 = (float) i2;
            if (customTransformation != null) {
                if (customTransformation.customTransformTarget(this, transformState)) {
                    f4 = this.mTransformationEndX;
                }
                Interpolator customInterpolator = customTransformation.getCustomInterpolator(1, z);
                if (customInterpolator != null) {
                    f3 = customInterpolator.getInterpolation(f);
                    view.setTranslationX(NotificationUtils.interpolate(getTransformationStartX(), f4, f3));
                }
            }
            f3 = interpolation;
            view.setTranslationX(NotificationUtils.interpolate(getTransformationStartX(), f4, f3));
        }
        if (z3) {
            float f5 = (float) (laidOutLocationOnScreen[1] - laidOutLocationOnScreen2[1]);
            if (customTransformation != null) {
                if (customTransformation.customTransformTarget(this, transformState)) {
                    f5 = this.mTransformationEndY;
                }
                Interpolator customInterpolator2 = customTransformation.getCustomInterpolator(16, false);
                if (customInterpolator2 != null) {
                    f2 = customInterpolator2.getInterpolation(f);
                    view.setTranslationY(NotificationUtils.interpolate(getTransformationStartY(), f5, f2));
                }
            }
            f2 = interpolation;
            view.setTranslationY(NotificationUtils.interpolate(getTransformationStartY(), f5, f2));
        }
        if (transformScale) {
            float transformationStartScaleX = getTransformationStartScaleX();
            if (transformationStartScaleX != -1.0f) {
                view.setScaleX(NotificationUtils.interpolate(transformationStartScaleX, ((float) contentWidth2) / ((float) contentWidth), interpolation));
            }
            float transformationStartScaleY = getTransformationStartScaleY();
            if (transformationStartScaleY != -1.0f) {
                view.setScaleY(NotificationUtils.interpolate(transformationStartScaleY, ((float) transformState.getContentHeight()) / ((float) getContentHeight()), interpolation));
            }
        }
    }

    /* access modifiers changed from: protected */
    public void setClippingDeactivated(View view, boolean z) {
        ViewClippingUtil.setClippingDeactivated(view, z, CLIPPING_PARAMETERS);
    }

    public int[] getLaidOutLocationOnScreen() {
        int[] locationOnScreen = getLocationOnScreen();
        locationOnScreen[0] = (int) (((float) locationOnScreen[0]) - this.mTransformedView.getTranslationX());
        locationOnScreen[1] = (int) (((float) locationOnScreen[1]) - this.mTransformedView.getTranslationY());
        return locationOnScreen;
    }

    public int[] getLocationOnScreen() {
        this.mTransformedView.getLocationOnScreen(this.mOwnPosition);
        int[] iArr = this.mOwnPosition;
        iArr[0] = (int) (((float) iArr[0]) - ((1.0f - this.mTransformedView.getScaleX()) * this.mTransformedView.getPivotX()));
        int[] iArr2 = this.mOwnPosition;
        iArr2[1] = (int) (((float) iArr2[1]) - ((1.0f - this.mTransformedView.getScaleY()) * this.mTransformedView.getPivotY()));
        int[] iArr3 = this.mOwnPosition;
        iArr3[1] = iArr3[1] - (MessagingPropertyAnimator.getTop(this.mTransformedView) - MessagingPropertyAnimator.getLayoutTop(this.mTransformedView));
        return this.mOwnPosition;
    }

    /* access modifiers changed from: protected */
    public boolean sameAs(TransformState transformState) {
        return this.mSameAsAny;
    }

    public void appear(float f, TransformableView transformableView) {
        if (f == 0.0f) {
            prepareFadeIn();
        }
        CrossFadeHelper.fadeIn(this.mTransformedView, f, true);
    }

    public void disappear(float f, TransformableView transformableView) {
        CrossFadeHelper.fadeOut(this.mTransformedView, f);
    }

    public static TransformState createFrom(View view, TransformInfo transformInfo) {
        if (view instanceof TextView) {
            TextViewTransformState obtain = TextViewTransformState.obtain();
            obtain.initFrom(view, transformInfo);
            return obtain;
        } else if (view.getId() == 16908723) {
            ActionListTransformState obtain2 = ActionListTransformState.obtain();
            obtain2.initFrom(view, transformInfo);
            return obtain2;
        } else if (view.getId() == 16909245) {
            MessagingLayoutTransformState obtain3 = MessagingLayoutTransformState.obtain();
            obtain3.initFrom(view, transformInfo);
            return obtain3;
        } else if (view instanceof MessagingImageMessage) {
            MessagingImageTransformState obtain4 = MessagingImageTransformState.obtain();
            obtain4.initFrom(view, transformInfo);
            return obtain4;
        } else if (view instanceof ImageView) {
            ImageTransformState obtain5 = ImageTransformState.obtain();
            obtain5.initFrom(view, transformInfo);
            return obtain5;
        } else if (view instanceof ProgressBar) {
            ProgressTransformState obtain6 = ProgressTransformState.obtain();
            obtain6.initFrom(view, transformInfo);
            return obtain6;
        } else {
            TransformState obtain7 = obtain();
            obtain7.initFrom(view, transformInfo);
            return obtain7;
        }
    }

    public void setIsSameAsAnyView(boolean z) {
        this.mSameAsAny = z;
    }

    public void recycle() {
        reset();
        if (getClass() == TransformState.class) {
            sInstancePool.release(this);
        }
    }

    public void setTransformationEndY(float f) {
        this.mTransformationEndY = f;
    }

    public float getTransformationStartX() {
        Object tag = this.mTransformedView.getTag(TRANSFORMATION_START_X);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public float getTransformationStartY() {
        Object tag = this.mTransformedView.getTag(TRANSFORMATION_START_Y);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public float getTransformationStartScaleX() {
        Object tag = this.mTransformedView.getTag(TRANSFORMATION_START_SCLALE_X);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public float getTransformationStartScaleY() {
        Object tag = this.mTransformedView.getTag(TRANSFORMATION_START_SCLALE_Y);
        if (tag == null) {
            return -1.0f;
        }
        return ((Float) tag).floatValue();
    }

    public void setTransformationStartX(float f) {
        this.mTransformedView.setTag(TRANSFORMATION_START_X, Float.valueOf(f));
    }

    public void setTransformationStartY(float f) {
        this.mTransformedView.setTag(TRANSFORMATION_START_Y, Float.valueOf(f));
    }

    private void setTransformationStartScaleX(float f) {
        this.mTransformedView.setTag(TRANSFORMATION_START_SCLALE_X, Float.valueOf(f));
    }

    private void setTransformationStartScaleY(float f) {
        this.mTransformedView.setTag(TRANSFORMATION_START_SCLALE_Y, Float.valueOf(f));
    }

    /* access modifiers changed from: protected */
    public void reset() {
        this.mTransformedView = null;
        this.mTransformInfo = null;
        this.mSameAsAny = false;
        this.mTransformationEndX = -1.0f;
        this.mTransformationEndY = -1.0f;
        this.mAlignEnd = false;
        this.mDefaultInterpolator = Interpolators.FAST_OUT_SLOW_IN;
    }

    public void setVisible(boolean z, boolean z2) {
        if (z2 || this.mTransformedView.getVisibility() != 8) {
            if (this.mTransformedView.getVisibility() != 8) {
                this.mTransformedView.setVisibility(z ? 0 : 4);
            }
            this.mTransformedView.animate().cancel();
            this.mTransformedView.setAlpha(z ? 1.0f : 0.0f);
            resetTransformedView();
        }
    }

    public void prepareFadeIn() {
        resetTransformedView();
    }

    /* access modifiers changed from: protected */
    public void resetTransformedView() {
        this.mTransformedView.setTranslationX(0.0f);
        this.mTransformedView.setTranslationY(0.0f);
        this.mTransformedView.setScaleX(1.0f);
        this.mTransformedView.setScaleY(1.0f);
        setClippingDeactivated(this.mTransformedView, false);
        abortTransformation();
    }

    public void abortTransformation() {
        View view = this.mTransformedView;
        int i = TRANSFORMATION_START_X;
        Float valueOf = Float.valueOf(-1.0f);
        view.setTag(i, valueOf);
        this.mTransformedView.setTag(TRANSFORMATION_START_Y, valueOf);
        this.mTransformedView.setTag(TRANSFORMATION_START_SCLALE_X, valueOf);
        this.mTransformedView.setTag(TRANSFORMATION_START_SCLALE_Y, valueOf);
    }

    public static TransformState obtain() {
        TransformState transformState = (TransformState) sInstancePool.acquire();
        if (transformState != null) {
            return transformState;
        }
        return new TransformState();
    }

    public View getTransformedView() {
        return this.mTransformedView;
    }

    public void setDefaultInterpolator(Interpolator interpolator) {
        this.mDefaultInterpolator = interpolator;
    }
}
