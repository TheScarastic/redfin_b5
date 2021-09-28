package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.util.AttributeSet;
import androidx.constraintlayout.widget.ConstraintLayout;
import com.android.systemui.bcsmartspace.R$drawable;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
import java.util.List;
/* loaded from: classes2.dex */
public class BcSmartspaceCardCombinationAtStore extends BcSmartspaceCardCombination {
    public BcSmartspaceCardCombinationAtStore(Context context) {
        super(context);
    }

    public BcSmartspaceCardCombinationAtStore(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardCombination, com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        SmartspaceAction smartspaceAction;
        List actionChips = smartspaceTarget.getActionChips();
        if (actionChips == null || actionChips.isEmpty() || (smartspaceAction = (SmartspaceAction) actionChips.get(0)) == null) {
            return false;
        }
        ConstraintLayout constraintLayout = this.mFirstSubCard;
        boolean z = (constraintLayout instanceof BcSmartspaceCardShoppingList) && ((BcSmartspaceCardShoppingList) constraintLayout).setSmartspaceActions(smartspaceTarget, smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        ConstraintLayout constraintLayout2 = this.mSecondSubCard;
        boolean z2 = constraintLayout2 != null && fillSubCard(constraintLayout2, smartspaceTarget, smartspaceAction, smartspaceEventNotifier, bcSmartspaceCardLoggingInfo);
        if (z) {
            this.mFirstSubCard.setBackgroundResource(R$drawable.bg_smartspace_combination_sub_card);
        }
        if (!z || !z2) {
            return false;
        }
        return true;
    }
}
