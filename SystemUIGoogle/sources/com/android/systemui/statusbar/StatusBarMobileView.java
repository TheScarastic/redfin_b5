package com.android.systemui.statusbar;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.android.internal.annotations.VisibleForTesting;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.DualToneHandler;
import com.android.systemui.R$dimen;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.plugins.DarkIconDispatcher;
import com.android.systemui.statusbar.phone.StatusBarSignalPolicy;
/* loaded from: classes.dex */
public class StatusBarMobileView extends FrameLayout implements DarkIconDispatcher.DarkReceiver, StatusIconDisplayable {
    private StatusBarIconView mDotView;
    private DualToneHandler mDualToneHandler;
    private boolean mForceHidden;
    private ImageView mIn;
    private View mInoutContainer;
    private ImageView mMobile;
    private SignalDrawable mMobileDrawable;
    private LinearLayout mMobileGroup;
    private ImageView mMobileRoaming;
    private View mMobileRoamingSpace;
    private ImageView mMobileType;
    private ImageView mOut;
    private boolean mProviderModel;
    private String mSlot;
    private StatusBarSignalPolicy.MobileIconState mState;
    private int mVisibleState = -1;

    public static StatusBarMobileView fromContext(Context context, String str, boolean z) {
        StatusBarMobileView statusBarMobileView = (StatusBarMobileView) LayoutInflater.from(context).inflate(R$layout.status_bar_mobile_signal_group, (ViewGroup) null);
        statusBarMobileView.setSlot(str);
        statusBarMobileView.init(z);
        statusBarMobileView.setVisibleState(0);
        return statusBarMobileView;
    }

    public StatusBarMobileView(Context context) {
        super(context);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public StatusBarMobileView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    public void getDrawingRect(Rect rect) {
        super.getDrawingRect(rect);
        float translationX = getTranslationX();
        float translationY = getTranslationY();
        rect.left = (int) (((float) rect.left) + translationX);
        rect.right = (int) (((float) rect.right) + translationX);
        rect.top = (int) (((float) rect.top) + translationY);
        rect.bottom = (int) (((float) rect.bottom) + translationY);
    }

    private void init(boolean z) {
        this.mProviderModel = z;
        this.mDualToneHandler = new DualToneHandler(getContext());
        this.mMobileGroup = (LinearLayout) findViewById(R$id.mobile_group);
        this.mMobile = (ImageView) findViewById(R$id.mobile_signal);
        this.mMobileType = (ImageView) findViewById(R$id.mobile_type);
        if (this.mProviderModel) {
            this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming_large);
        } else {
            this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming);
        }
        this.mMobileRoamingSpace = findViewById(R$id.mobile_roaming_space);
        this.mIn = (ImageView) findViewById(R$id.mobile_in);
        this.mOut = (ImageView) findViewById(R$id.mobile_out);
        this.mInoutContainer = findViewById(R$id.inout_container);
        SignalDrawable signalDrawable = new SignalDrawable(getContext());
        this.mMobileDrawable = signalDrawable;
        this.mMobile.setImageDrawable(signalDrawable);
        initDotView();
    }

    private void initDotView() {
        StatusBarIconView statusBarIconView = new StatusBarIconView(((FrameLayout) this).mContext, this.mSlot, null);
        this.mDotView = statusBarIconView;
        statusBarIconView.setVisibleState(1);
        int dimensionPixelSize = ((FrameLayout) this).mContext.getResources().getDimensionPixelSize(R$dimen.status_bar_icon_size);
        FrameLayout.LayoutParams layoutParams = new FrameLayout.LayoutParams(dimensionPixelSize, dimensionPixelSize);
        layoutParams.gravity = 8388627;
        addView(this.mDotView, layoutParams);
    }

    public void applyMobileState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z = true;
        if (mobileIconState == null) {
            if (getVisibility() == 8) {
                z = false;
            }
            setVisibility(8);
            this.mState = null;
        } else {
            StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
            if (mobileIconState2 == null) {
                this.mState = mobileIconState.copy();
                initViewState();
            } else {
                z = !mobileIconState2.equals(mobileIconState) ? updateState(mobileIconState.copy()) : false;
            }
        }
        if (z) {
            requestLayout();
        }
    }

    private void initViewState() {
        setContentDescription(this.mState.contentDescription);
        int i = 0;
        if (!this.mState.visible || this.mForceHidden) {
            this.mMobileGroup.setVisibility(8);
        } else {
            this.mMobileGroup.setVisibility(0);
        }
        this.mMobileDrawable.setLevel(this.mState.strengthId);
        StatusBarSignalPolicy.MobileIconState mobileIconState = this.mState;
        if (mobileIconState.typeId > 0) {
            this.mMobileType.setContentDescription(mobileIconState.typeContentDescription);
            this.mMobileType.setImageResource(this.mState.typeId);
            this.mMobileType.setVisibility(0);
        } else {
            this.mMobileType.setVisibility(8);
        }
        this.mMobile.setVisibility(this.mState.showTriangle ? 0 : 8);
        this.mMobileRoaming.setVisibility(this.mState.roaming ? 0 : 8);
        this.mMobileRoamingSpace.setVisibility(this.mState.roaming ? 0 : 8);
        this.mIn.setVisibility(this.mState.activityIn ? 0 : 8);
        this.mOut.setVisibility(this.mState.activityOut ? 0 : 8);
        View view = this.mInoutContainer;
        StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
        if (!mobileIconState2.activityIn && !mobileIconState2.activityOut) {
            i = 8;
        }
        view.setVisibility(i);
    }

    private boolean updateState(StatusBarSignalPolicy.MobileIconState mobileIconState) {
        boolean z;
        setContentDescription(mobileIconState.contentDescription);
        int i = 8;
        boolean z2 = false;
        int i2 = (!mobileIconState.visible || this.mForceHidden) ? 8 : 0;
        if (i2 != this.mMobileGroup.getVisibility()) {
            this.mMobileGroup.setVisibility(i2);
            z = true;
        } else {
            z = false;
        }
        int i3 = this.mState.strengthId;
        int i4 = mobileIconState.strengthId;
        if (i3 != i4) {
            this.mMobileDrawable.setLevel(i4);
        }
        int i5 = this.mState.typeId;
        int i6 = mobileIconState.typeId;
        if (i5 != i6) {
            z |= i6 == 0 || i5 == 0;
            if (i6 != 0) {
                this.mMobileType.setContentDescription(mobileIconState.typeContentDescription);
                this.mMobileType.setImageResource(mobileIconState.typeId);
                this.mMobileType.setVisibility(0);
            } else {
                this.mMobileType.setVisibility(8);
            }
        }
        this.mMobile.setVisibility(mobileIconState.showTriangle ? 0 : 8);
        this.mMobileRoaming.setVisibility(mobileIconState.roaming ? 0 : 8);
        this.mMobileRoamingSpace.setVisibility(mobileIconState.roaming ? 0 : 8);
        this.mIn.setVisibility(mobileIconState.activityIn ? 0 : 8);
        this.mOut.setVisibility(mobileIconState.activityOut ? 0 : 8);
        View view = this.mInoutContainer;
        if (mobileIconState.activityIn || mobileIconState.activityOut) {
            i = 0;
        }
        view.setVisibility(i);
        boolean z3 = mobileIconState.roaming;
        StatusBarSignalPolicy.MobileIconState mobileIconState2 = this.mState;
        if (!(z3 == mobileIconState2.roaming && mobileIconState.activityIn == mobileIconState2.activityIn && mobileIconState.activityOut == mobileIconState2.activityOut && mobileIconState.showTriangle == mobileIconState2.showTriangle)) {
            z2 = true;
        }
        boolean z4 = z | z2;
        this.mState = mobileIconState;
        return z4;
    }

    @Override // com.android.systemui.plugins.DarkIconDispatcher.DarkReceiver
    public void onDarkChanged(Rect rect, float f, int i) {
        if (!DarkIconDispatcher.isInArea(rect, this)) {
            f = 0.0f;
        }
        this.mMobileDrawable.setTintList(ColorStateList.valueOf(this.mDualToneHandler.getSingleColor(f)));
        ColorStateList valueOf = ColorStateList.valueOf(DarkIconDispatcher.getTint(rect, this, i));
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
        this.mDotView.setIconColor(i, false);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public String getSlot() {
        return this.mSlot;
    }

    public void setSlot(String str) {
        this.mSlot = str;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setStaticDrawableColor(int i) {
        ColorStateList valueOf = ColorStateList.valueOf(i);
        this.mMobileDrawable.setTintList(valueOf);
        this.mIn.setImageTintList(valueOf);
        this.mOut.setImageTintList(valueOf);
        this.mMobileType.setImageTintList(valueOf);
        this.mMobileRoaming.setImageTintList(valueOf);
        this.mDotView.setDecorColor(i);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setDecorColor(int i) {
        this.mDotView.setDecorColor(i);
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public boolean isIconVisible() {
        return this.mState.visible && !this.mForceHidden;
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public void setVisibleState(int i, boolean z) {
        if (i != this.mVisibleState) {
            this.mVisibleState = i;
            if (i == 0) {
                this.mMobileGroup.setVisibility(0);
                this.mDotView.setVisibility(8);
            } else if (i != 1) {
                this.mMobileGroup.setVisibility(4);
                this.mDotView.setVisibility(4);
            } else {
                this.mMobileGroup.setVisibility(4);
                this.mDotView.setVisibility(0);
            }
        }
    }

    @Override // com.android.systemui.statusbar.StatusIconDisplayable
    public int getVisibleState() {
        return this.mVisibleState;
    }

    @VisibleForTesting
    public StatusBarSignalPolicy.MobileIconState getState() {
        return this.mState;
    }

    @Override // android.view.View, java.lang.Object
    public String toString() {
        return "StatusBarMobileView(slot=" + this.mSlot + " state=" + this.mState + ")";
    }
}
