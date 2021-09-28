package com.android.systemui.screenrecord;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Switch;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import com.android.systemui.settings.UserContextProvider;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ScreenRecordDialog extends Activity {
    private Switch mAudioSwitch;
    private final RecordingController mController;
    private List<ScreenRecordingAudioSource> mModes;
    private Spinner mOptions;
    private Switch mTapsSwitch;
    private final UserContextProvider mUserContextProvider;

    public ScreenRecordDialog(RecordingController recordingController, UserContextProvider userContextProvider) {
        this.mController = recordingController;
        this.mUserContextProvider = userContextProvider;
    }

    @Override // android.app.Activity
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        Window window = getWindow();
        window.getDecorView();
        window.setLayout(-1, -2);
        window.addPrivateFlags(16);
        window.setGravity(48);
        setTitle(R$string.screenrecord_name);
        setContentView(R$layout.screen_record_dialog);
        ((TextView) findViewById(R$id.button_cancel)).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenrecord.ScreenRecordDialog$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenRecordDialog.this.lambda$onCreate$0(view);
            }
        });
        ((TextView) findViewById(R$id.button_start)).setOnClickListener(new View.OnClickListener() { // from class: com.android.systemui.screenrecord.ScreenRecordDialog$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                ScreenRecordDialog.this.lambda$onCreate$1(view);
            }
        });
        ArrayList arrayList = new ArrayList();
        this.mModes = arrayList;
        arrayList.add(ScreenRecordingAudioSource.INTERNAL);
        this.mModes.add(ScreenRecordingAudioSource.MIC);
        this.mModes.add(ScreenRecordingAudioSource.MIC_AND_INTERNAL);
        this.mAudioSwitch = (Switch) findViewById(R$id.screenrecord_audio_switch);
        this.mTapsSwitch = (Switch) findViewById(R$id.screenrecord_taps_switch);
        this.mOptions = (Spinner) findViewById(R$id.screen_recording_options);
        ScreenRecordingAdapter screenRecordingAdapter = new ScreenRecordingAdapter(getApplicationContext(), 17367049, this.mModes);
        screenRecordingAdapter.setDropDownViewResource(17367049);
        this.mOptions.setAdapter((SpinnerAdapter) screenRecordingAdapter);
        this.mOptions.setOnItemClickListenerInt(new AdapterView.OnItemClickListener() { // from class: com.android.systemui.screenrecord.ScreenRecordDialog$$ExternalSyntheticLambda2
            @Override // android.widget.AdapterView.OnItemClickListener
            public final void onItemClick(AdapterView adapterView, View view, int i, long j) {
                ScreenRecordDialog.this.lambda$onCreate$2(adapterView, view, i, j);
            }
        });
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$0(View view) {
        finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$1(View view) {
        requestScreenCapture();
        finish();
    }

    /* access modifiers changed from: private */
    public /* synthetic */ void lambda$onCreate$2(AdapterView adapterView, View view, int i, long j) {
        this.mAudioSwitch.setChecked(true);
    }

    private void requestScreenCapture() {
        ScreenRecordingAudioSource screenRecordingAudioSource;
        Context userContext = this.mUserContextProvider.getUserContext();
        boolean isChecked = this.mTapsSwitch.isChecked();
        if (this.mAudioSwitch.isChecked()) {
            screenRecordingAudioSource = (ScreenRecordingAudioSource) this.mOptions.getSelectedItem();
        } else {
            screenRecordingAudioSource = ScreenRecordingAudioSource.NONE;
        }
        this.mController.startCountdown(3000, 1000, PendingIntent.getForegroundService(userContext, 2, RecordingService.getStartIntent(userContext, -1, screenRecordingAudioSource.ordinal(), isChecked), 201326592), PendingIntent.getService(userContext, 2, RecordingService.getStopIntent(userContext), 201326592));
    }
}
