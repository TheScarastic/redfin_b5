package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.widget.ImageView;
import com.android.systemui.bcsmartspace.R$id;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
/* loaded from: classes2.dex */
public class BcSmartspaceCardGenericImage extends BcSmartspaceCardSecondary {
    protected ImageView mImageView;

    public BcSmartspaceCardGenericImage(Context context) {
        super(context);
    }

    public BcSmartspaceCardGenericImage(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    /* access modifiers changed from: package-private */
    @Override // com.google.android.systemui.smartspace.BcSmartspaceCardSecondary
    public boolean setSmartspaceActions(SmartspaceTarget smartspaceTarget, BcSmartspaceDataPlugin.SmartspaceEventNotifier smartspaceEventNotifier, BcSmartspaceCardLoggingInfo bcSmartspaceCardLoggingInfo) {
        Bundle bundle;
        SmartspaceAction baseAction = smartspaceTarget.getBaseAction();
        if (baseAction == null) {
            bundle = null;
        } else {
            bundle = baseAction.getExtras();
        }
        if (bundle == null || !bundle.containsKey("imageBitmap")) {
            return false;
        }
        setImageBitmap((Bitmap) bundle.get("imageBitmap"));
        return true;
    }

    /* access modifiers changed from: protected */
    @Override // android.view.View
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mImageView = (ImageView) findViewById(R$id.image_view);
    }

    /* access modifiers changed from: package-private */
    public void setImageBitmap(Bitmap bitmap) {
        this.mImageView.setImageBitmap(bitmap);
    }
}
