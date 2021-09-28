package com.android.systemui;

import android.app.Activity;
import android.content.ComponentName;
import android.content.pm.PackageManager;
import android.util.Slog;
import com.android.systemui.DessertCaseView;
/* loaded from: classes.dex */
public class DessertCase extends Activity {
    DessertCaseView mView;

    @Override // android.app.Activity
    public void onStart() {
        super.onStart();
        PackageManager packageManager = getPackageManager();
        ComponentName componentName = new ComponentName(this, DessertCaseDream.class);
        if (packageManager.getComponentEnabledSetting(componentName) != 1) {
            Slog.v("DessertCase", "ACHIEVEMENT UNLOCKED");
            packageManager.setComponentEnabledSetting(componentName, 1, 1);
        }
        this.mView = new DessertCaseView(this);
        DessertCaseView.RescalingContainer rescalingContainer = new DessertCaseView.RescalingContainer(this);
        rescalingContainer.setView(this.mView);
        setContentView(rescalingContainer);
    }

    @Override // android.app.Activity
    public void onResume() {
        super.onResume();
        this.mView.postDelayed(new Runnable() { // from class: com.android.systemui.DessertCase.1
            @Override // java.lang.Runnable
            public void run() {
                DessertCase.this.mView.start();
            }
        }, 1000);
    }

    @Override // android.app.Activity
    public void onPause() {
        super.onPause();
        this.mView.stop();
    }
}
