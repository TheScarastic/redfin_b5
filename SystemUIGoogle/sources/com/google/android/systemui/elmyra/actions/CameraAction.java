package com.google.android.systemui.elmyra.actions;

import android.content.Context;
import com.android.systemui.R$string;
import com.android.systemui.statusbar.phone.StatusBar;
import com.google.android.systemui.elmyra.feedback.FeedbackEffect;
import com.google.android.systemui.elmyra.sensors.GestureSensor;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes2.dex */
public class CameraAction extends ServiceAction {
    private final String mCameraPackageName;
    private final StatusBar mStatusBar;

    private CameraAction(Context context, StatusBar statusBar, List<FeedbackEffect> list) {
        super(context, list);
        this.mCameraPackageName = context.getResources().getString(R$string.google_camera_app_package_name);
        this.mStatusBar = statusBar;
    }

    @Override // com.google.android.systemui.elmyra.actions.ServiceAction
    protected boolean checkSupportedCaller() {
        return checkSupportedCaller(this.mCameraPackageName);
    }

    @Override // com.google.android.systemui.elmyra.actions.ServiceAction, com.google.android.systemui.elmyra.actions.Action
    public void onTrigger(GestureSensor.DetectionProperties detectionProperties) {
        this.mStatusBar.collapseShade();
        super.onTrigger(detectionProperties);
    }

    /* loaded from: classes2.dex */
    public static class Builder {
        private final Context mContext;
        List<FeedbackEffect> mFeedbackEffects = new ArrayList();
        private final StatusBar mStatusBar;

        public Builder(Context context, StatusBar statusBar) {
            this.mContext = context;
            this.mStatusBar = statusBar;
        }

        public Builder addFeedbackEffect(FeedbackEffect feedbackEffect) {
            this.mFeedbackEffects.add(feedbackEffect);
            return this;
        }

        public CameraAction build() {
            return new CameraAction(this.mContext, this.mStatusBar, this.mFeedbackEffects);
        }
    }
}
