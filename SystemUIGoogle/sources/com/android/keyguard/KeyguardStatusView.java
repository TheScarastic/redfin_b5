package com.android.keyguard;

import android.app.ActivityManager;
import android.app.IActivityManager;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import androidx.core.graphics.ColorUtils;
import com.android.internal.widget.LockPatternUtils;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.CrossFadeHelper;
import java.util.Set;
/* loaded from: classes.dex */
public class KeyguardStatusView extends GridLayout {
    private float mChildrenAlphaExcludingSmartSpace;
    private KeyguardClockSwitch mClockView;
    private float mDarkAmount;
    private final IActivityManager mIActivityManager;
    private KeyguardSliceView mKeyguardSlice;
    private final LockPatternUtils mLockPatternUtils;
    private View mMediaHostContainer;
    private boolean mShowingHeader;
    private ViewGroup mStatusViewContainer;
    private int mTextColor;

    public KeyguardStatusView(Context context) {
        this(context, null, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public KeyguardStatusView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mDarkAmount = 0.0f;
        this.mChildrenAlphaExcludingSmartSpace = 1.0f;
        this.mIActivityManager = ActivityManager.getService();
        this.mLockPatternUtils = new LockPatternUtils(getContext());
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mStatusViewContainer = (ViewGroup) findViewById(R$id.status_view_container);
        this.mClockView = (KeyguardClockSwitch) findViewById(R$id.keyguard_clock_container);
        if (KeyguardClockAccessibilityDelegate.isNeeded(((GridLayout) this).mContext)) {
            this.mClockView.setAccessibilityDelegate(new KeyguardClockAccessibilityDelegate(((GridLayout) this).mContext));
        }
        this.mKeyguardSlice = (KeyguardSliceView) findViewById(R$id.keyguard_status_area);
        this.mTextColor = this.mClockView.getCurrentTextColor();
        this.mKeyguardSlice.setContentChangeListener(new Runnable() { // from class: com.android.keyguard.KeyguardStatusView$$ExternalSyntheticLambda0
            @Override // java.lang.Runnable
            public final void run() {
                KeyguardStatusView.this.onSliceContentChanged();
            }
        });
        onSliceContentChanged();
        this.mMediaHostContainer = findViewById(R$id.status_view_media_container);
        updateDark();
    }

    /* access modifiers changed from: private */
    public void onSliceContentChanged() {
        boolean hasHeader = this.mKeyguardSlice.hasHeader();
        if (this.mShowingHeader != hasHeader) {
            this.mShowingHeader = hasHeader;
        }
    }

    /* access modifiers changed from: package-private */
    public void setDarkAmount(float f) {
        if (this.mDarkAmount != f) {
            this.mDarkAmount = f;
            this.mClockView.setDarkAmount(f);
            CrossFadeHelper.fadeOut(this.mMediaHostContainer, f);
            updateDark();
        }
    }

    void updateDark() {
        int blendARGB = ColorUtils.blendARGB(this.mTextColor, -1, this.mDarkAmount);
        this.mKeyguardSlice.setDarkAmount(this.mDarkAmount);
        this.mClockView.setTextColor(blendARGB);
    }

    public void setChildrenAlphaExcludingClockView(float f) {
        setChildrenAlphaExcluding(f, Set.of(this.mClockView));
    }

    public void setChildrenAlphaExcluding(float f, Set<View> set) {
        this.mChildrenAlphaExcludingSmartSpace = f;
        for (int i = 0; i < this.mStatusViewContainer.getChildCount(); i++) {
            View childAt = this.mStatusViewContainer.getChildAt(i);
            if (!set.contains(childAt)) {
                childAt.setAlpha(f);
            }
        }
    }

    public float getChildrenAlphaExcludingSmartSpace() {
        return this.mChildrenAlphaExcludingSmartSpace;
    }
}
