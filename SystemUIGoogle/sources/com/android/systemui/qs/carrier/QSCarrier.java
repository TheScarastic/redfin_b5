package com.android.systemui.qs.carrier;

import android.content.Context;
import android.content.res.ColorStateList;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.settingslib.Utils;
import com.android.settingslib.graph.SignalDrawable;
import com.android.systemui.R$drawable;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import java.util.Objects;
/* loaded from: classes.dex */
public class QSCarrier extends LinearLayout {
    private TextView mCarrierText;
    private boolean mIsSingleCarrier;
    private CellSignalState mLastSignalState;
    private View mMobileGroup;
    private ImageView mMobileRoaming;
    private ImageView mMobileSignal;
    private boolean mProviderModelInitialized = false;
    private View mSpacer;

    public QSCarrier(Context context) {
        super(context);
    }

    public QSCarrier(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public QSCarrier(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mMobileGroup = findViewById(R$id.mobile_combo);
        this.mMobileRoaming = (ImageView) findViewById(R$id.mobile_roaming);
        this.mMobileSignal = (ImageView) findViewById(R$id.mobile_signal);
        this.mCarrierText = (TextView) findViewById(R$id.qs_carrier_text);
        this.mSpacer = findViewById(R$id.spacer);
    }

    public boolean updateState(CellSignalState cellSignalState, boolean z) {
        int i = 0;
        if (Objects.equals(cellSignalState, this.mLastSignalState) && z == this.mIsSingleCarrier) {
            return false;
        }
        this.mLastSignalState = cellSignalState;
        this.mIsSingleCarrier = z;
        boolean z2 = cellSignalState.visible && !z;
        this.mMobileGroup.setVisibility(z2 ? 0 : 8);
        this.mSpacer.setVisibility(z ? 0 : 8);
        if (z2) {
            ImageView imageView = this.mMobileRoaming;
            if (!cellSignalState.roaming) {
                i = 8;
            }
            imageView.setVisibility(i);
            ColorStateList colorAttr = Utils.getColorAttr(((LinearLayout) this).mContext, 16842806);
            this.mMobileRoaming.setImageTintList(colorAttr);
            this.mMobileSignal.setImageTintList(colorAttr);
            if (cellSignalState.providerModelBehavior) {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(((LinearLayout) this).mContext.getDrawable(R$drawable.ic_qs_no_calling_sms));
                }
                this.mMobileSignal.setImageDrawable(((LinearLayout) this).mContext.getDrawable(cellSignalState.mobileSignalIconId));
                this.mMobileSignal.setContentDescription(cellSignalState.contentDescription);
            } else {
                if (!this.mProviderModelInitialized) {
                    this.mProviderModelInitialized = true;
                    this.mMobileSignal.setImageDrawable(new SignalDrawable(((LinearLayout) this).mContext));
                }
                this.mMobileSignal.setImageLevel(cellSignalState.mobileSignalIconId);
                StringBuilder sb = new StringBuilder();
                String str = cellSignalState.contentDescription;
                if (str != null) {
                    sb.append(str);
                    sb.append(", ");
                }
                if (cellSignalState.roaming) {
                    sb.append(((LinearLayout) this).mContext.getString(R$string.data_connection_roaming));
                    sb.append(", ");
                }
                if (hasValidTypeContentDescription(cellSignalState.typeContentDescription)) {
                    sb.append(cellSignalState.typeContentDescription);
                }
                this.mMobileSignal.setContentDescription(sb);
            }
        }
        return true;
    }

    private boolean hasValidTypeContentDescription(String str) {
        return TextUtils.equals(str, ((LinearLayout) this).mContext.getString(R$string.data_connection_no_internet)) || TextUtils.equals(str, ((LinearLayout) this).mContext.getString(com.android.settingslib.R$string.cell_data_off_content_description)) || TextUtils.equals(str, ((LinearLayout) this).mContext.getString(com.android.settingslib.R$string.not_default_data_content_description));
    }

    View getRSSIView() {
        return this.mMobileGroup;
    }

    public void setCarrierText(CharSequence charSequence) {
        this.mCarrierText.setText(charSequence);
    }
}
