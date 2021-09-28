package com.android.wm.shell.legacysplitscreen;

import android.app.Activity;
import android.app.ActivityManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import com.android.wm.shell.R;
/* loaded from: classes2.dex */
public class ForcedResizableInfoActivity extends Activity implements View.OnTouchListener {
    private final Runnable mFinishRunnable = new Runnable() { // from class: com.android.wm.shell.legacysplitscreen.ForcedResizableInfoActivity.1
        @Override // java.lang.Runnable
        public void run() {
            ForcedResizableInfoActivity.this.finish();
        }
    };

    @Override // android.app.Activity
    public void setTaskDescription(ActivityManager.TaskDescription taskDescription) {
    }

    @Override // android.app.Activity
    protected void onCreate(Bundle bundle) {
        String str;
        super.onCreate(bundle);
        setContentView(R.layout.forced_resizable_activity);
        TextView textView = (TextView) findViewById(16908299);
        int intExtra = getIntent().getIntExtra("extra_forced_resizeable_reason", -1);
        if (intExtra == 1) {
            str = getString(R.string.dock_forced_resizable);
        } else if (intExtra == 2) {
            str = getString(R.string.forced_resizable_secondary_display);
        } else {
            throw new IllegalArgumentException("Unexpected forced resizeable reason: " + intExtra);
        }
        textView.setText(str);
        getWindow().setTitle(str);
        getWindow().getDecorView().setOnTouchListener(this);
    }

    @Override // android.app.Activity
    protected void onStart() {
        super.onStart();
        getWindow().getDecorView().postDelayed(this.mFinishRunnable, 2500);
    }

    @Override // android.app.Activity
    protected void onStop() {
        super.onStop();
        finish();
    }

    @Override // android.view.View.OnTouchListener
    public boolean onTouch(View view, MotionEvent motionEvent) {
        finish();
        return true;
    }

    @Override // android.app.Activity, android.view.KeyEvent.Callback
    public boolean onKeyDown(int i, KeyEvent keyEvent) {
        finish();
        return true;
    }

    @Override // android.app.Activity
    public void finish() {
        super.finish();
        overridePendingTransition(0, R.anim.forced_resizable_exit);
    }
}
