package com.android.systemui.qs.customize;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.content.res.Configuration;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toolbar;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.plugins.qs.QS;
import com.android.systemui.qs.QSDetailClipper;
import com.android.systemui.statusbar.phone.LightBarController;
import com.android.systemui.statusbar.phone.NotificationsQuickSettingsContainer;
/* loaded from: classes.dex */
public class QSCustomizer extends LinearLayout {
    private boolean isShown;
    private boolean mCustomizing;
    private boolean mIsShowingNavBackdrop;
    private NotificationsQuickSettingsContainer mNotifQsContainer;
    private boolean mOpening;
    private QS mQs;
    private final RecyclerView mRecyclerView;
    private int mX;
    private int mY;
    private final Animator.AnimatorListener mCollapseAnimationListener = new AnimatorListenerAdapter() { // from class: com.android.systemui.qs.customize.QSCustomizer.1
        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (!QSCustomizer.this.isShown) {
                QSCustomizer.this.setVisibility(8);
            }
            QSCustomizer.this.mNotifQsContainer.setCustomizerAnimating(false);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            if (!QSCustomizer.this.isShown) {
                QSCustomizer.this.setVisibility(8);
            }
            QSCustomizer.this.mNotifQsContainer.setCustomizerAnimating(false);
        }
    };
    private final QSDetailClipper mClipper = new QSDetailClipper(findViewById(R$id.customize_container));
    private final View mTransparentView = findViewById(R$id.customizer_transparent_view);

    public QSCustomizer(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        LayoutInflater.from(getContext()).inflate(R$layout.qs_customize_panel_content, this);
        Toolbar toolbar = (Toolbar) findViewById(16908710);
        TypedValue typedValue = new TypedValue();
        ((LinearLayout) this).mContext.getTheme().resolveAttribute(16843531, typedValue, true);
        toolbar.setNavigationIcon(getResources().getDrawable(typedValue.resourceId, ((LinearLayout) this).mContext.getTheme()));
        toolbar.getMenu().add(0, 1, 0, ((LinearLayout) this).mContext.getString(17041265));
        toolbar.setTitle(R$string.qs_edit);
        RecyclerView recyclerView = (RecyclerView) findViewById(16908298);
        this.mRecyclerView = recyclerView;
        DefaultItemAnimator defaultItemAnimator = new DefaultItemAnimator();
        defaultItemAnimator.setMoveDuration(150);
        recyclerView.setItemAnimator(defaultItemAnimator);
    }

    /* access modifiers changed from: package-private */
    public void updateResources() {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) this.mTransparentView.getLayoutParams();
        layoutParams.height = ((LinearLayout) this).mContext.getResources().getDimensionPixelSize(17105475);
        this.mTransparentView.setLayoutParams(layoutParams);
    }

    /* access modifiers changed from: package-private */
    public void updateNavBackDrop(Configuration configuration, LightBarController lightBarController) {
        View findViewById = findViewById(R$id.nav_bar_background);
        int i = 0;
        boolean z = configuration.smallestScreenWidthDp >= 600 || configuration.orientation != 2;
        this.mIsShowingNavBackdrop = z;
        if (findViewById != null) {
            if (!z) {
                i = 8;
            }
            findViewById.setVisibility(i);
        }
        updateNavColors(lightBarController);
    }

    /* access modifiers changed from: package-private */
    public void updateNavColors(LightBarController lightBarController) {
        lightBarController.setQsCustomizing(this.mIsShowingNavBackdrop && this.isShown);
    }

    public void setContainer(NotificationsQuickSettingsContainer notificationsQuickSettingsContainer) {
        this.mNotifQsContainer = notificationsQuickSettingsContainer;
    }

    public void setQs(QS qs) {
        this.mQs = qs;
    }

    /* access modifiers changed from: package-private */
    public void show(int i, int i2, TileAdapter tileAdapter) {
        if (!this.isShown) {
            int[] locationOnScreen = findViewById(R$id.customize_container).getLocationOnScreen();
            this.mX = i - locationOnScreen[0];
            this.mY = i2 - locationOnScreen[1];
            this.isShown = true;
            this.mOpening = true;
            setVisibility(0);
            this.mClipper.animateCircularClip(this.mX, this.mY, true, new ExpandAnimatorListener(tileAdapter));
            this.mNotifQsContainer.setCustomizerAnimating(true);
            this.mNotifQsContainer.setCustomizerShowing(true);
        }
    }

    /* access modifiers changed from: package-private */
    public void showImmediately() {
        if (!this.isShown) {
            setVisibility(0);
            this.mClipper.cancelAnimator();
            this.mClipper.showBackground();
            this.isShown = true;
            setCustomizing(true);
            this.mNotifQsContainer.setCustomizerAnimating(false);
            this.mNotifQsContainer.setCustomizerShowing(true);
        }
    }

    public void hide(boolean z) {
        if (this.isShown) {
            this.isShown = false;
            this.mClipper.cancelAnimator();
            this.mOpening = false;
            if (z) {
                this.mClipper.animateCircularClip(this.mX, this.mY, false, this.mCollapseAnimationListener);
            } else {
                setVisibility(8);
            }
            this.mNotifQsContainer.setCustomizerAnimating(z);
            this.mNotifQsContainer.setCustomizerShowing(false);
        }
    }

    @Override // android.view.View
    public boolean isShown() {
        return this.isShown;
    }

    /* access modifiers changed from: package-private */
    public void setCustomizing(boolean z) {
        this.mCustomizing = z;
        this.mQs.notifyCustomizeChanged();
    }

    public boolean isCustomizing() {
        return this.mCustomizing || this.mOpening;
    }

    public void setEditLocation(int i, int i2) {
        int[] locationOnScreen = findViewById(R$id.customize_container).getLocationOnScreen();
        this.mX = i - locationOnScreen[0];
        this.mY = i2 - locationOnScreen[1];
    }

    /* loaded from: classes.dex */
    class ExpandAnimatorListener extends AnimatorListenerAdapter {
        private final TileAdapter mTileAdapter;

        ExpandAnimatorListener(TileAdapter tileAdapter) {
            this.mTileAdapter = tileAdapter;
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationEnd(Animator animator) {
            if (QSCustomizer.this.isShown) {
                QSCustomizer.this.setCustomizing(true);
            }
            QSCustomizer.this.mOpening = false;
            QSCustomizer.this.mNotifQsContainer.setCustomizerAnimating(false);
            QSCustomizer.this.mRecyclerView.setAdapter(this.mTileAdapter);
        }

        @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
        public void onAnimationCancel(Animator animator) {
            QSCustomizer.this.mOpening = false;
            QSCustomizer.this.mQs.notifyCustomizeChanged();
            QSCustomizer.this.mNotifQsContainer.setCustomizerAnimating(false);
        }
    }

    public RecyclerView getRecyclerView() {
        return this.mRecyclerView;
    }

    public boolean isOpening() {
        return this.mOpening;
    }
}
