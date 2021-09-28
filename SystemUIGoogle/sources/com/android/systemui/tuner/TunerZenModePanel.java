package com.android.systemui.tuner;

import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.LinearLayout;
import com.android.systemui.Prefs;
import com.android.systemui.statusbar.policy.ZenModeController;
import com.android.systemui.volume.ZenModePanel;
/* loaded from: classes2.dex */
public class TunerZenModePanel extends LinearLayout implements View.OnClickListener {
    private View mButtons;
    private ZenModeController mController;
    private View mDone;
    private View.OnClickListener mDoneListener;
    private boolean mEditing;
    private View mHeaderSwitch;
    private View mMoreSettings;
    private final Runnable mUpdate = new Runnable() { // from class: com.android.systemui.tuner.TunerZenModePanel.1
        @Override // java.lang.Runnable
        public void run() {
            TunerZenModePanel.this.updatePanel();
        }
    };
    private int mZenMode;
    private ZenModePanel mZenModePanel;

    public TunerZenModePanel(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    @Override // android.view.View, android.view.ViewGroup
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        this.mEditing = false;
    }

    @Override // android.view.View.OnClickListener
    public void onClick(View view) {
        if (view == this.mHeaderSwitch) {
            this.mEditing = true;
            if (this.mZenMode == 0) {
                int i = Prefs.getInt(((LinearLayout) this).mContext, "DndFavoriteZen", 3);
                this.mZenMode = i;
                this.mController.setZen(i, null, "TunerZenModePanel");
                postUpdatePanel();
                return;
            }
            this.mZenMode = 0;
            this.mController.setZen(0, null, "TunerZenModePanel");
            postUpdatePanel();
        } else if (view == this.mMoreSettings) {
            Intent intent = new Intent("android.settings.ZEN_MODE_SETTINGS");
            intent.addFlags(268435456);
            getContext().startActivity(intent);
        } else if (view == this.mDone) {
            this.mEditing = false;
            setVisibility(8);
            this.mDoneListener.onClick(view);
        }
    }

    private void postUpdatePanel() {
        removeCallbacks(this.mUpdate);
        postDelayed(this.mUpdate, 40);
    }

    /* access modifiers changed from: private */
    public void updatePanel() {
        int i = 0;
        boolean z = this.mZenMode != 0;
        ((Checkable) this.mHeaderSwitch.findViewById(16908311)).setChecked(z);
        this.mZenModePanel.setVisibility(z ? 0 : 8);
        View view = this.mButtons;
        if (!z) {
            i = 8;
        }
        view.setVisibility(i);
    }
}
