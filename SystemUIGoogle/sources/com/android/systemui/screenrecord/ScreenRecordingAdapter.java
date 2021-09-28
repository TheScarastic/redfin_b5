package com.android.systemui.screenrecord;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.android.systemui.R$id;
import com.android.systemui.R$layout;
import com.android.systemui.R$string;
import java.util.List;
/* loaded from: classes.dex */
public class ScreenRecordingAdapter extends ArrayAdapter<ScreenRecordingAudioSource> {
    private LinearLayout mInternalOption;
    private LinearLayout mMicAndInternalOption;
    private LinearLayout mMicOption;
    private LinearLayout mSelectedInternal;
    private LinearLayout mSelectedMic;
    private LinearLayout mSelectedMicAndInternal;

    public ScreenRecordingAdapter(Context context, int i, List<ScreenRecordingAudioSource> list) {
        super(context, i, list);
        initViews();
    }

    private void initViews() {
        int i = R$string.screenrecord_device_audio_label;
        this.mSelectedInternal = getSelected(i);
        int i2 = R$string.screenrecord_mic_label;
        this.mSelectedMic = getSelected(i2);
        int i3 = R$string.screenrecord_device_audio_and_mic_label;
        this.mSelectedMicAndInternal = getSelected(i3);
        LinearLayout option = getOption(i2, 0);
        this.mMicOption = option;
        option.removeViewAt(1);
        LinearLayout option2 = getOption(i3, 0);
        this.mMicAndInternalOption = option2;
        option2.removeViewAt(1);
        this.mInternalOption = getOption(i, R$string.screenrecord_device_audio_description);
    }

    private LinearLayout getOption(int i, int i2) {
        LinearLayout linearLayout = (LinearLayout) ((LayoutInflater) getContext().getSystemService("layout_inflater")).inflate(R$layout.screen_record_dialog_audio_source, (ViewGroup) null, false);
        ((TextView) linearLayout.findViewById(R$id.screen_recording_dialog_source_text)).setText(i);
        if (i2 != 0) {
            ((TextView) linearLayout.findViewById(R$id.screen_recording_dialog_source_description)).setText(i2);
        }
        return linearLayout;
    }

    private LinearLayout getSelected(int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(getContext()).inflate(R$layout.screen_record_dialog_audio_source_selected, (ViewGroup) null, false);
        ((TextView) linearLayout.findViewById(R$id.screen_recording_dialog_source_text)).setText(i);
        return linearLayout;
    }

    /* renamed from: com.android.systemui.screenrecord.ScreenRecordingAdapter$1  reason: invalid class name */
    /* loaded from: classes.dex */
    static /* synthetic */ class AnonymousClass1 {
        static final /* synthetic */ int[] $SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource;

        static {
            int[] iArr = new int[ScreenRecordingAudioSource.values().length];
            $SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource = iArr;
            try {
                iArr[ScreenRecordingAudioSource.INTERNAL.ordinal()] = 1;
            } catch (NoSuchFieldError unused) {
            }
            try {
                $SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource[ScreenRecordingAudioSource.MIC_AND_INTERNAL.ordinal()] = 2;
            } catch (NoSuchFieldError unused2) {
            }
            try {
                $SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource[ScreenRecordingAudioSource.MIC.ordinal()] = 3;
            } catch (NoSuchFieldError unused3) {
            }
        }
    }

    @Override // android.widget.ArrayAdapter, android.widget.SpinnerAdapter, android.widget.BaseAdapter
    public View getDropDownView(int i, View view, ViewGroup viewGroup) {
        int i2 = AnonymousClass1.$SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource[getItem(i).ordinal()];
        if (i2 == 1) {
            return this.mInternalOption;
        }
        if (i2 == 2) {
            return this.mMicAndInternalOption;
        }
        if (i2 != 3) {
            return super.getDropDownView(i, view, viewGroup);
        }
        return this.mMicOption;
    }

    @Override // android.widget.ArrayAdapter, android.widget.Adapter
    public View getView(int i, View view, ViewGroup viewGroup) {
        int i2 = AnonymousClass1.$SwitchMap$com$android$systemui$screenrecord$ScreenRecordingAudioSource[getItem(i).ordinal()];
        if (i2 == 1) {
            return this.mSelectedInternal;
        }
        if (i2 == 2) {
            return this.mSelectedMicAndInternal;
        }
        if (i2 != 3) {
            return super.getView(i, view, viewGroup);
        }
        return this.mSelectedMic;
    }
}
