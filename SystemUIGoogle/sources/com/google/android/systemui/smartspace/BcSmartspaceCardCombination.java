package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.bcsmartspace.R$id;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.List;
/* loaded from: classes2.dex */
public class BcSmartspaceCardCombination extends BcSmartspaceCardSecondary {
    protected ConstraintLayout mFirstSubCard;
    protected ConstraintLayout mSecondSubCard;

    public BcSmartspaceCardCombination(Context context) {
        super(context);
    }

    public BcSmartspaceCardCombination(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View
    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mFirstSubCard = (ConstraintLayout) findViewById(R$id.first_sub_card);
        this.mSecondSubCard = (ConstraintLayout) findViewById(R$id.second_sub_card);
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        List actionChips = smartspaceTarget.getActionChips();
        if (actionChips == null || actionChips.size() < 2) {
            return false;
        }
        SmartspaceAction smartspaceAction = (SmartspaceAction) actionChips.get(0);
        SmartspaceAction smartspaceAction2 = (SmartspaceAction) actionChips.get(1);
        if (smartspaceAction == null || smartspaceAction2 == null) {
            return false;
        }
        ConstraintLayout constraintLayout = this.mFirstSubCard;
        boolean z = constraintLayout != null && fillSubCard(constraintLayout, smartspaceTarget, smartspaceAction, smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        ConstraintLayout constraintLayout2 = this.mSecondSubCard;
        boolean z2 = constraintLayout2 != null && fillSubCard(constraintLayout2, smartspaceTarget, smartspaceAction2, smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        if (!z || !z2) {
            return false;
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean fillSubCard(ConstraintLayout constraintLayout, SmartspaceTarget smartspaceTarget, SmartspaceAction smartspaceAction, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        boolean z;
        TextView textView = (TextView) constraintLayout.findViewById(R$id.sub_card_text);
        ImageView imageView = (ImageView) constraintLayout.findViewById(R$id.sub_card_icon);
        if (textView == null) {
            Log.w("BcSmartspaceCardCombination", "No sub-card text field to update");
            return false;
        } else if (imageView == null) {
            Log.w("BcSmartspaceCardCombination", "No sub-card image field to update");
            return false;
        } else {
            BcSmartSpaceUtil.setOnClickListener(constraintLayout, smartspaceTarget, smartspaceAction, "BcSmartspaceCardCombination", smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
            Drawable iconDrawable = BcSmartSpaceUtil.getIconDrawable(smartspaceAction.getIcon(), getContext());
            boolean z2 = true;
            if (iconDrawable == null) {
                imageView.setVisibility(8);
                z = false;
            } else {
                imageView.setImageDrawable(iconDrawable);
                imageView.setVisibility(0);
                z = true;
            }
            CharSequence title = smartspaceAction.getTitle();
            if (TextUtils.isEmpty(title)) {
                textView.setVisibility(8);
                z2 = z;
            } else {
                textView.setText(title);
                textView.setVisibility(0);
            }
            constraintLayout.setContentDescription(z2 ? smartspaceAction.getContentDescription() : null);
            return z2;
        }
    }
}
