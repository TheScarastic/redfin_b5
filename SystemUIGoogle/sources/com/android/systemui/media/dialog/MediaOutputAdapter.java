package com.android.systemui.media.dialog;

import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import com.android.settingslib.Utils;
import com.android.settingslib.media.MediaDevice;
import com.android.systemui.R$drawable;
import com.android.systemui.R$string;
import com.android.systemui.media.dialog.MediaOutputBaseAdapter;
import java.util.List;
/* loaded from: classes.dex */
public class MediaOutputAdapter extends MediaOutputBaseAdapter {
    private static final boolean DEBUG = Log.isLoggable("MediaOutputAdapter", 3);
    private ViewGroup mConnectedItem;
    private boolean mIncludeDynamicGroup;

    public MediaOutputAdapter(MediaOutputController mediaOutputController) {
        super(mediaOutputController);
    }

    @Override // com.android.systemui.media.dialog.MediaOutputBaseAdapter, androidx.recyclerview.widget.RecyclerView.Adapter
    public MediaOutputBaseAdapter.MediaDeviceBaseViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        super.onCreateViewHolder(viewGroup, i);
        return new MediaDeviceViewHolder(this.mHolderView);
    }

    public void onBindViewHolder(MediaOutputBaseAdapter.MediaDeviceBaseViewHolder mediaDeviceBaseViewHolder, int i) {
        int size = this.mController.getMediaDevices().size();
        boolean z = false;
        boolean z2 = true;
        if (i == size && this.mController.isZeroMode()) {
            mediaDeviceBaseViewHolder.onBind(1, false, true);
        } else if (this.mIncludeDynamicGroup) {
            if (i == 0) {
                mediaDeviceBaseViewHolder.onBind(3, true, false);
                return;
            }
            MediaDevice mediaDevice = (MediaDevice) ((List) this.mController.getMediaDevices()).get(i - 1);
            if (i != size) {
                z2 = false;
            }
            mediaDeviceBaseViewHolder.onBind(mediaDevice, false, z2, i);
        } else if (i < size) {
            MediaDevice mediaDevice2 = (MediaDevice) ((List) this.mController.getMediaDevices()).get(i);
            boolean z3 = i == 0;
            if (i == size - 1) {
                z = true;
            }
            mediaDeviceBaseViewHolder.onBind(mediaDevice2, z3, z, i);
        } else if (DEBUG) {
            Log.d("MediaOutputAdapter", "Incorrect position: " + i);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        this.mIncludeDynamicGroup = this.mController.getSelectedMediaDevice().size() > 1;
        if (this.mController.isZeroMode() || this.mIncludeDynamicGroup) {
            return this.mController.getMediaDevices().size() + 1;
        }
        return this.mController.getMediaDevices().size();
    }

    /* access modifiers changed from: package-private */
    @Override // com.android.systemui.media.dialog.MediaOutputBaseAdapter
    public CharSequence getItemTitle(MediaDevice mediaDevice) {
        if (mediaDevice.getDeviceType() != 4 || mediaDevice.isConnected()) {
            return super.getItemTitle(mediaDevice);
        }
        String name = mediaDevice.getName();
        SpannableString spannableString = new SpannableString(this.mContext.getString(R$string.media_output_dialog_disconnected, name));
        spannableString.setSpan(new ForegroundColorSpan(Utils.getColorAttrDefaultColor(this.mContext, 16842808)), name.length(), spannableString.length(), 33);
        return spannableString;
    }

    /* loaded from: classes.dex */
    public class MediaDeviceViewHolder extends MediaOutputBaseAdapter.MediaDeviceBaseViewHolder {
        /* JADX INFO: 'super' call moved to the top of the method (can break code semantics) */
        MediaDeviceViewHolder(View view) {
            super(view);
            MediaOutputAdapter.this = r1;
        }

        /* access modifiers changed from: package-private */
        @Override // com.android.systemui.media.dialog.MediaOutputBaseAdapter.MediaDeviceBaseViewHolder
        public void onBind(MediaDevice mediaDevice, boolean z, boolean z2, int i) {
            super.onBind(mediaDevice, z, z2, i);
            boolean z3 = !MediaOutputAdapter.this.mIncludeDynamicGroup && MediaOutputAdapter.this.isCurrentlyConnected(mediaDevice);
            if (z3) {
                MediaOutputAdapter.this.mConnectedItem = this.mContainerLayout;
            }
            this.mBottomDivider.setVisibility(8);
            this.mCheckBox.setVisibility(8);
            if (!z3 || !MediaOutputAdapter.this.mController.isActiveRemoteDevice(mediaDevice) || MediaOutputAdapter.this.mController.getSelectableMediaDevice().size() <= 0) {
                this.mDivider.setVisibility(8);
                this.mAddIcon.setVisibility(8);
            } else {
                this.mDivider.setVisibility(0);
                this.mDivider.setTransitionAlpha(1.0f);
                this.mAddIcon.setVisibility(0);
                this.mAddIcon.setTransitionAlpha(1.0f);
                this.mAddIcon.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda0(this));
            }
            MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
            if (mediaOutputAdapter.mCurrentActivePosition == i) {
                mediaOutputAdapter.mCurrentActivePosition = -1;
            }
            if (mediaOutputAdapter.mController.isTransferring()) {
                if (mediaDevice.getState() != 1 || MediaOutputAdapter.this.mController.hasAdjustVolumeUserRestriction()) {
                    setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                } else {
                    setTwoLineLayout(mediaDevice, true, false, true, false);
                }
            } else if (mediaDevice.getState() == 3) {
                setTwoLineLayout(mediaDevice, false, false, false, true);
                this.mSubTitleText.setText(R$string.media_output_dialog_connect_failed);
                this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda3(this, mediaDevice));
            } else if (MediaOutputAdapter.this.mController.hasAdjustVolumeUserRestriction() || !z3) {
                setSingleLineLayout(MediaOutputAdapter.this.getItemTitle(mediaDevice), false);
                this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda4(this, mediaDevice));
            } else {
                setTwoLineLayout(mediaDevice, true, true, false, false);
                initSeekbar(mediaDevice);
                MediaOutputAdapter.this.mCurrentActivePosition = i;
            }
        }

        public /* synthetic */ void lambda$onBind$0(View view) {
            onEndItemClick();
        }

        /* access modifiers changed from: package-private */
        @Override // com.android.systemui.media.dialog.MediaOutputBaseAdapter.MediaDeviceBaseViewHolder
        public void onBind(int i, boolean z, boolean z2) {
            super.onBind(i, z, z2);
            if (i == 1) {
                this.mCheckBox.setVisibility(8);
                this.mDivider.setVisibility(8);
                this.mAddIcon.setVisibility(8);
                this.mBottomDivider.setVisibility(8);
                setSingleLineLayout(MediaOutputAdapter.this.mContext.getText(R$string.media_output_dialog_pairing_new), false);
                Drawable drawable = MediaOutputAdapter.this.mContext.getDrawable(R$drawable.ic_add);
                drawable.setColorFilter(new PorterDuffColorFilter(Utils.getColorAccentDefaultColor(MediaOutputAdapter.this.mContext), PorterDuff.Mode.SRC_IN));
                this.mTitleIcon.setImageDrawable(drawable);
                this.mContainerLayout.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda1(this));
            } else if (i == 3) {
                MediaOutputAdapter.this.mConnectedItem = this.mContainerLayout;
                this.mBottomDivider.setVisibility(8);
                this.mCheckBox.setVisibility(8);
                if (MediaOutputAdapter.this.mController.getSelectableMediaDevice().size() > 0) {
                    this.mDivider.setVisibility(0);
                    this.mDivider.setTransitionAlpha(1.0f);
                    this.mAddIcon.setVisibility(0);
                    this.mAddIcon.setTransitionAlpha(1.0f);
                    this.mAddIcon.setOnClickListener(new MediaOutputAdapter$MediaDeviceViewHolder$$ExternalSyntheticLambda2(this));
                } else {
                    this.mDivider.setVisibility(8);
                    this.mAddIcon.setVisibility(8);
                }
                this.mTitleIcon.setImageDrawable(getSpeakerDrawable());
                CharSequence sessionName = MediaOutputAdapter.this.mController.getSessionName();
                if (TextUtils.isEmpty(sessionName)) {
                    sessionName = MediaOutputAdapter.this.mContext.getString(R$string.media_output_dialog_group);
                }
                setTwoLineLayout(sessionName, true, true, false, false);
                initSessionSeekbar();
            }
        }

        public /* synthetic */ void lambda$onBind$3(View view) {
            onItemClick(1);
        }

        public /* synthetic */ void lambda$onBind$4(View view) {
            onEndItemClick();
        }

        /* renamed from: onItemClick */
        public void lambda$onBind$2(View view, MediaDevice mediaDevice) {
            if (!MediaOutputAdapter.this.mController.isTransferring()) {
                MediaOutputAdapter mediaOutputAdapter = MediaOutputAdapter.this;
                mediaOutputAdapter.mCurrentActivePosition = -1;
                playSwitchingAnim(mediaOutputAdapter.mConnectedItem, view);
                MediaOutputAdapter.this.mController.connectDevice(mediaDevice);
                mediaDevice.setState(1);
                if (!MediaOutputAdapter.this.isAnimating()) {
                    MediaOutputAdapter.this.notifyDataSetChanged();
                }
            }
        }

        private void onItemClick(int i) {
            if (i == 1) {
                MediaOutputAdapter.this.mController.launchBluetoothPairing();
            }
        }

        private void onEndItemClick() {
            MediaOutputAdapter.this.mController.launchMediaOutputGroupDialog();
        }
    }
}
