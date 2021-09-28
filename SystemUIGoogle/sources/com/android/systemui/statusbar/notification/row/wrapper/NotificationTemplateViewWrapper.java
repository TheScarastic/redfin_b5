package com.android.systemui.statusbar.notification.row.wrapper;

import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.service.notification.StatusBarNotification;
import android.util.ArraySet;
import android.view.NotificationHeaderView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.internal.util.ContrastColorUtil;
import com.android.internal.widget.NotificationActionListLayout;
import com.android.systemui.Dependency;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.UiOffloadThread;
import com.android.systemui.statusbar.CrossFadeHelper;
import com.android.systemui.statusbar.TransformableView;
import com.android.systemui.statusbar.ViewTransformationHelper;
import com.android.systemui.statusbar.notification.ImageTransformState;
import com.android.systemui.statusbar.notification.TransformState;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.HybridNotificationView;
/* loaded from: classes.dex */
public class NotificationTemplateViewWrapper extends NotificationHeaderViewWrapper {
    private NotificationActionListLayout mActions;
    protected View mActionsContainer;
    private final boolean mAllowHideHeader;
    private boolean mCanHideHeader;
    private ArraySet<PendingIntent> mCancelledPendingIntents = new ArraySet<>();
    private int mContentHeight;
    private final int mFullHeaderTranslation;
    private float mHeaderTranslation;
    protected ImageView mLeftIcon;
    private int mMinHeightHint;
    private ProgressBar mProgressBar;
    private View mRemoteInputHistory;
    protected ImageView mRightIcon;
    protected View mSmartReplyContainer;
    private TextView mText;
    private TextView mTitle;
    private UiOffloadThread mUiOffloadThread;

    /* access modifiers changed from: protected */
    public NotificationTemplateViewWrapper(Context context, View view, ExpandableNotificationRow expandableNotificationRow) {
        super(context, view, expandableNotificationRow);
        this.mAllowHideHeader = context.getResources().getBoolean(R$bool.heads_up_notification_hides_header);
        this.mTransformationHelper.setCustomTransformation(new ViewTransformationHelper.CustomTransformation() { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper.1
            @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
            public boolean transformTo(TransformState transformState, TransformableView transformableView, float f) {
                if (!(transformableView instanceof HybridNotificationView)) {
                    return false;
                }
                TransformState currentState = transformableView.getCurrentState(1);
                CrossFadeHelper.fadeOut(transformState.getTransformedView(), f);
                if (currentState != null) {
                    transformState.transformViewVerticalTo(currentState, this, f);
                    currentState.recycle();
                }
                return true;
            }

            @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
            public boolean customTransformTarget(TransformState transformState, TransformState transformState2) {
                transformState.setTransformationEndY(getTransformationY(transformState, transformState2));
                return true;
            }

            @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
            public boolean transformFrom(TransformState transformState, TransformableView transformableView, float f) {
                if (!(transformableView instanceof HybridNotificationView)) {
                    return false;
                }
                TransformState currentState = transformableView.getCurrentState(1);
                CrossFadeHelper.fadeIn(transformState.getTransformedView(), f, true);
                if (currentState != null) {
                    transformState.transformViewVerticalFrom(currentState, this, f);
                    currentState.recycle();
                }
                return true;
            }

            @Override // com.android.systemui.statusbar.ViewTransformationHelper.CustomTransformation
            public boolean initTransformation(TransformState transformState, TransformState transformState2) {
                transformState.setTransformationStartY(getTransformationY(transformState, transformState2));
                return true;
            }

            private float getTransformationY(TransformState transformState, TransformState transformState2) {
                return ((float) ((transformState2.getLaidOutLocationOnScreen()[1] + transformState2.getTransformedView().getHeight()) - transformState.getLaidOutLocationOnScreen()[1])) * 0.33f;
            }
        }, 2);
        this.mFullHeaderTranslation = context.getResources().getDimensionPixelSize(17105376) - context.getResources().getDimensionPixelSize(17105379);
    }

    private void resolveTemplateViews(StatusBarNotification statusBarNotification) {
        ImageView imageView = (ImageView) this.mView.findViewById(16909380);
        this.mRightIcon = imageView;
        if (imageView != null) {
            imageView.setTag(ImageTransformState.ICON_TAG, getRightIcon(statusBarNotification.getNotification()));
            this.mRightIcon.setTag(TransformState.ALIGN_END_TAG, Boolean.TRUE);
        }
        ImageView imageView2 = (ImageView) this.mView.findViewById(16909134);
        this.mLeftIcon = imageView2;
        if (imageView2 != null) {
            imageView2.setTag(ImageTransformState.ICON_TAG, getLargeIcon(statusBarNotification.getNotification()));
        }
        this.mTitle = (TextView) this.mView.findViewById(16908310);
        this.mText = (TextView) this.mView.findViewById(16909525);
        View findViewById = this.mView.findViewById(16908301);
        if (findViewById instanceof ProgressBar) {
            this.mProgressBar = (ProgressBar) findViewById;
        } else {
            this.mProgressBar = null;
        }
        this.mSmartReplyContainer = this.mView.findViewById(16909460);
        this.mActionsContainer = this.mView.findViewById(16908723);
        this.mActions = this.mView.findViewById(16908722);
        this.mRemoteInputHistory = this.mView.findViewById(16909238);
        updatePendingIntentCancellations();
    }

    /* access modifiers changed from: protected */
    public final Icon getLargeIcon(Notification notification) {
        Bitmap bitmap;
        Icon largeIcon = notification.getLargeIcon();
        return (largeIcon != null || (bitmap = notification.largeIcon) == null) ? largeIcon : Icon.createWithBitmap(bitmap);
    }

    protected final Icon getRightIcon(Notification notification) {
        Icon pictureIcon;
        if (!notification.extras.getBoolean("android.showBigPictureWhenCollapsed") || !notification.isStyle(Notification.BigPictureStyle.class) || (pictureIcon = Notification.BigPictureStyle.getPictureIcon(notification.extras)) == null) {
            return getLargeIcon(notification);
        }
        return pictureIcon;
    }

    private void updatePendingIntentCancellations() {
        NotificationActionListLayout notificationActionListLayout = this.mActions;
        if (notificationActionListLayout != null) {
            int childCount = notificationActionListLayout.getChildCount();
            for (int i = 0; i < childCount; i++) {
                Button button = (Button) this.mActions.getChildAt(i);
                performOnPendingIntentCancellation(button, new Runnable(button) { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper$$ExternalSyntheticLambda3
                    public final /* synthetic */ Button f$1;

                    {
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        NotificationTemplateViewWrapper.this.lambda$updatePendingIntentCancellations$0(this.f$1);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$updatePendingIntentCancellations$0(Button button) {
        if (button.isEnabled()) {
            button.setEnabled(false);
            ColorStateList textColors = button.getTextColors();
            int[] colors = textColors.getColors();
            int[] iArr = new int[colors.length];
            float f = this.mView.getResources().getFloat(17105362);
            for (int i = 0; i < colors.length; i++) {
                iArr[i] = blendColorWithBackground(colors[i], f);
            }
            button.setTextColor(new ColorStateList(textColors.getStates(), iArr));
        }
    }

    private int blendColorWithBackground(int i, float f) {
        return ContrastColorUtil.compositeColors(Color.argb((int) (f * 255.0f), Color.red(i), Color.green(i), Color.blue(i)), resolveBackgroundColor());
    }

    private void performOnPendingIntentCancellation(View view, Runnable runnable) {
        final PendingIntent pendingIntent = (PendingIntent) view.getTag(16909286);
        if (pendingIntent != null) {
            if (this.mCancelledPendingIntents.contains(pendingIntent)) {
                runnable.run();
                return;
            }
            final NotificationTemplateViewWrapper$$ExternalSyntheticLambda0 notificationTemplateViewWrapper$$ExternalSyntheticLambda0 = new PendingIntent.CancelListener(pendingIntent, runnable) { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper$$ExternalSyntheticLambda0
                public final /* synthetic */ PendingIntent f$1;
                public final /* synthetic */ Runnable f$2;

                {
                    this.f$1 = r2;
                    this.f$2 = r3;
                }

                public final void onCancelled(PendingIntent pendingIntent2) {
                    NotificationTemplateViewWrapper.this.lambda$performOnPendingIntentCancellation$2(this.f$1, this.f$2, pendingIntent2);
                }
            };
            if (this.mUiOffloadThread == null) {
                this.mUiOffloadThread = (UiOffloadThread) Dependency.get(UiOffloadThread.class);
            }
            if (view.isAttachedToWindow()) {
                this.mUiOffloadThread.execute(new Runnable(pendingIntent, notificationTemplateViewWrapper$$ExternalSyntheticLambda0) { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper$$ExternalSyntheticLambda1
                    public final /* synthetic */ PendingIntent f$0;
                    public final /* synthetic */ PendingIntent.CancelListener f$1;

                    {
                        this.f$0 = r1;
                        this.f$1 = r2;
                    }

                    @Override // java.lang.Runnable
                    public final void run() {
                        this.f$0.registerCancelListener(this.f$1);
                    }
                });
            }
            view.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper.2
                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewAttachedToWindow(View view2) {
                    NotificationTemplateViewWrapper.this.mUiOffloadThread.execute(new NotificationTemplateViewWrapper$2$$ExternalSyntheticLambda1(pendingIntent, notificationTemplateViewWrapper$$ExternalSyntheticLambda0));
                }

                @Override // android.view.View.OnAttachStateChangeListener
                public void onViewDetachedFromWindow(View view2) {
                    NotificationTemplateViewWrapper.this.mUiOffloadThread.execute(new NotificationTemplateViewWrapper$2$$ExternalSyntheticLambda0(pendingIntent, notificationTemplateViewWrapper$$ExternalSyntheticLambda0));
                }
            });
        }
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performOnPendingIntentCancellation$2(PendingIntent pendingIntent, Runnable runnable, PendingIntent pendingIntent2) {
        this.mView.post(new Runnable(pendingIntent, runnable) { // from class: com.android.systemui.statusbar.notification.row.wrapper.NotificationTemplateViewWrapper$$ExternalSyntheticLambda2
            public final /* synthetic */ PendingIntent f$1;
            public final /* synthetic */ Runnable f$2;

            {
                this.f$1 = r2;
                this.f$2 = r3;
            }

            @Override // java.lang.Runnable
            public final void run() {
                NotificationTemplateViewWrapper.this.lambda$performOnPendingIntentCancellation$1(this.f$1, this.f$2);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$performOnPendingIntentCancellation$1(PendingIntent pendingIntent, Runnable runnable) {
        this.mCancelledPendingIntents.add(pendingIntent);
        runnable.run();
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper, com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void onContentUpdated(ExpandableNotificationRow expandableNotificationRow) {
        ImageView imageView;
        resolveTemplateViews(expandableNotificationRow.getEntry().getSbn());
        super.onContentUpdated(expandableNotificationRow);
        this.mCanHideHeader = this.mAllowHideHeader && this.mNotificationHeader != null && ((imageView = this.mRightIcon) == null || imageView.getVisibility() != 0);
        if (expandableNotificationRow.getHeaderVisibleAmount() != 1.0f) {
            setHeaderVisibleAmount(expandableNotificationRow.getHeaderVisibleAmount());
        }
    }

    /* access modifiers changed from: protected */
    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationHeaderViewWrapper
    public void updateTransformedTypes() {
        super.updateTransformedTypes();
        TextView textView = this.mTitle;
        if (textView != null) {
            this.mTransformationHelper.addTransformedView(1, textView);
        }
        TextView textView2 = this.mText;
        if (textView2 != null) {
            this.mTransformationHelper.addTransformedView(2, textView2);
        }
        ImageView imageView = this.mRightIcon;
        if (imageView != null) {
            this.mTransformationHelper.addTransformedView(3, imageView);
        }
        ProgressBar progressBar = this.mProgressBar;
        if (progressBar != null) {
            this.mTransformationHelper.addTransformedView(4, progressBar);
        }
        addViewsTransformingToSimilar(this.mLeftIcon);
        addTransformedViews(this.mSmartReplyContainer);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void setContentHeight(int i, int i2) {
        super.setContentHeight(i, i2);
        this.mContentHeight = i;
        this.mMinHeightHint = i2;
        updateActionOffset();
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public boolean shouldClipToRounding(boolean z, boolean z2) {
        View view;
        if (super.shouldClipToRounding(z, z2)) {
            return true;
        }
        if (!z2 || (view = this.mActionsContainer) == null || view.getVisibility() == 8) {
            return false;
        }
        return true;
    }

    private void updateActionOffset() {
        if (this.mActionsContainer != null) {
            this.mActionsContainer.setTranslationY((float) ((Math.max(this.mContentHeight, this.mMinHeightHint) - this.mView.getHeight()) - getHeaderTranslation(false)));
        }
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public int getHeaderTranslation(boolean z) {
        return (!z || !this.mCanHideHeader) ? (int) this.mHeaderTranslation : this.mFullHeaderTranslation;
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public void setHeaderVisibleAmount(float f) {
        float f2;
        NotificationHeaderView notificationHeaderView;
        super.setHeaderVisibleAmount(f);
        if (!this.mCanHideHeader || (notificationHeaderView = this.mNotificationHeader) == null) {
            f2 = 0.0f;
        } else {
            notificationHeaderView.setAlpha(f);
            f2 = (1.0f - f) * ((float) this.mFullHeaderTranslation);
        }
        this.mHeaderTranslation = f2;
        this.mView.setTranslationY(f2);
    }

    @Override // com.android.systemui.statusbar.notification.row.wrapper.NotificationViewWrapper
    public int getExtraMeasureHeight() {
        NotificationActionListLayout notificationActionListLayout = this.mActions;
        int extraMeasureHeight = notificationActionListLayout != null ? notificationActionListLayout.getExtraMeasureHeight() : 0;
        View view = this.mRemoteInputHistory;
        if (!(view == null || view.getVisibility() == 8)) {
            extraMeasureHeight += this.mRow.getContext().getResources().getDimensionPixelSize(R$dimen.remote_input_history_extra_height);
        }
        return extraMeasureHeight + super.getExtraMeasureHeight();
    }
}
