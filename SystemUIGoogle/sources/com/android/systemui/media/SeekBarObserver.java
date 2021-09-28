package com.android.systemui.media;

import android.graphics.drawable.Drawable;
import android.text.format.DateUtils;
import androidx.lifecycle.Observer;
import com.android.systemui.R$dimen;
import com.android.systemui.media.SeekBarViewModel;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: SeekBarObserver.kt */
/* loaded from: classes.dex */
public final class SeekBarObserver implements Observer<SeekBarViewModel.Progress> {
    private final PlayerViewHolder holder;
    private final int seekBarDisabledHeight;
    private final int seekBarDisabledVerticalPadding;
    private final int seekBarEnabledMaxHeight;
    private final int seekBarEnabledVerticalPadding;

    public SeekBarObserver(PlayerViewHolder playerViewHolder) {
        Intrinsics.checkNotNullParameter(playerViewHolder, "holder");
        this.holder = playerViewHolder;
        this.seekBarEnabledMaxHeight = playerViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_enabled_seekbar_height);
        this.seekBarDisabledHeight = playerViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_disabled_seekbar_height);
        this.seekBarEnabledVerticalPadding = playerViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_enabled_seekbar_vertical_padding);
        this.seekBarDisabledVerticalPadding = playerViewHolder.getSeekBar().getContext().getResources().getDimensionPixelSize(R$dimen.qs_media_disabled_seekbar_vertical_padding);
    }

    public void onChanged(SeekBarViewModel.Progress progress) {
        Intrinsics.checkNotNullParameter(progress, "data");
        int i = 0;
        if (!progress.getEnabled()) {
            if (this.holder.getSeekBar().getMaxHeight() != this.seekBarDisabledHeight) {
                this.holder.getSeekBar().setMaxHeight(this.seekBarDisabledHeight);
                setVerticalPadding(this.seekBarDisabledVerticalPadding);
            }
            this.holder.getSeekBar().setEnabled(false);
            this.holder.getSeekBar().getThumb().setAlpha(0);
            this.holder.getSeekBar().setProgress(0);
            this.holder.getElapsedTimeView().setText("");
            this.holder.getTotalTimeView().setText("");
            return;
        }
        Drawable thumb = this.holder.getSeekBar().getThumb();
        if (progress.getSeekAvailable()) {
            i = 255;
        }
        thumb.setAlpha(i);
        this.holder.getSeekBar().setEnabled(progress.getSeekAvailable());
        if (this.holder.getSeekBar().getMaxHeight() != this.seekBarEnabledMaxHeight) {
            this.holder.getSeekBar().setMaxHeight(this.seekBarEnabledMaxHeight);
            setVerticalPadding(this.seekBarEnabledVerticalPadding);
        }
        int duration = progress.getDuration();
        this.holder.getSeekBar().setMax(duration);
        this.holder.getTotalTimeView().setText(DateUtils.formatElapsedTime(((long) duration) / 1000));
        Integer elapsedTime = progress.getElapsedTime();
        if (elapsedTime != null) {
            int intValue = elapsedTime.intValue();
            this.holder.getSeekBar().setProgress(intValue);
            this.holder.getElapsedTimeView().setText(DateUtils.formatElapsedTime(((long) intValue) / 1000));
        }
    }

    public final void setVerticalPadding(int i) {
        this.holder.getSeekBar().setPadding(this.holder.getSeekBar().getPaddingLeft(), i, this.holder.getSeekBar().getPaddingRight(), this.holder.getSeekBar().getPaddingBottom());
    }
}
