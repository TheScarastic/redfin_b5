package com.android.systemui.media.dialog;

import android.widget.CompoundButton;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.media.dialog.MediaOutputGroupAdapter;
/* loaded from: classes.dex */
public final /* synthetic */ class MediaOutputGroupAdapter$GroupViewHolder$$ExternalSyntheticLambda0 implements CompoundButton.OnCheckedChangeListener {
    public final /* synthetic */ MediaOutputGroupAdapter.GroupViewHolder f$0;
    public final /* synthetic */ MediaDevice f$1;

    public /* synthetic */ MediaOutputGroupAdapter$GroupViewHolder$$ExternalSyntheticLambda0(MediaOutputGroupAdapter.GroupViewHolder groupViewHolder, MediaDevice mediaDevice) {
        this.f$0 = groupViewHolder;
        this.f$1 = mediaDevice;
    }

    @Override // android.widget.CompoundButton.OnCheckedChangeListener
    public final void onCheckedChanged(CompoundButton compoundButton, boolean z) {
        MediaOutputGroupAdapter.GroupViewHolder.m130$r8$lambda$nsjLXvu8CqhAPljsHrQ9lHP7bs(this.f$0, this.f$1, compoundButton, z);
    }
}
