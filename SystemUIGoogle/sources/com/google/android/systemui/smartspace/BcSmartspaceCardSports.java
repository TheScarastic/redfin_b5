package com.google.android.systemui.smartspace;

import android.app.smartspace.SmartspaceAction;
import android.app.smartspace.SmartspaceTarget;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.systemui.bcsmartspace.R$id;
import com.android.systemui.plugins.BcSmartspaceDataPlugin;
/* loaded from: classes2.dex */
public class BcSmartspaceCardSports extends BcSmartspaceCardSecondary {
    public BcSmartspaceCardSports(Context context) {
        super(context);
    }

    public BcSmartspaceCardSports(Context context, AttributeSet attributeSet) {
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
        boolean z = false;
        if (bundle == null) {
            return false;
        }
        if (bundle.containsKey("matchTimeSummary")) {
            setMatchTimeSummaryText(bundle.getString("matchTimeSummary"));
            z = true;
        }
        if (bundle.containsKey("firstCompetitorScore")) {
            setFirstCompetitorScore(bundle.getString("firstCompetitorScore"));
            z = true;
        }
        if (bundle.containsKey("secondCompetitorScore")) {
            setSecondCompetitorScore(bundle.getString("secondCompetitorScore"));
            z = true;
        }
        if (bundle.containsKey("firstCompetitorLogo")) {
            setFirstCompetitorLogo((Bitmap) bundle.get("firstCompetitorLogo"));
            z = true;
        }
        if (!bundle.containsKey("secondCompetitorLogo")) {
            return z;
        }
        setSecondCompetitorLogo((Bitmap) bundle.get("secondCompetitorLogo"));
        return true;
    }

    void setMatchTimeSummaryText(String str) {
        TextView textView = (TextView) findViewById(R$id.match_time_summary);
        if (textView == null) {
            Log.w("BcSmartspaceCardSports", "No match time summary view to update");
        } else {
            textView.setText(str);
        }
    }

    void setFirstCompetitorScore(String str) {
        TextView textView = (TextView) findViewById(R$id.first_competitor_score);
        if (textView == null) {
            Log.w("BcSmartspaceCardSports", "No first competitor logo view to update");
        } else {
            textView.setText(str);
        }
    }

    void setSecondCompetitorScore(String str) {
        TextView textView = (TextView) findViewById(R$id.second_competitor_score);
        if (textView == null) {
            Log.w("BcSmartspaceCardSports", "No second competitor logo view to update");
        } else {
            textView.setText(str);
        }
    }

    void setFirstCompetitorLogo(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R$id.first_competitor_logo);
        if (imageView == null) {
            Log.w("BcSmartspaceCardSports", "No first competitor logo view to update");
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }

    void setSecondCompetitorLogo(Bitmap bitmap) {
        ImageView imageView = (ImageView) findViewById(R$id.second_competitor_logo);
        if (imageView == null) {
            Log.w("BcSmartspaceCardSports", "No second competitor logo view to update");
        } else {
            imageView.setImageBitmap(bitmap);
        }
    }
}
