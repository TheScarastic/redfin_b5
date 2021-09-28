package com.android.settingslib.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import androidx.preference.Preference;
import androidx.preference.PreferenceViewHolder;
import com.android.settingslib.utils.BuildCompatUtils;
import java.util.ArrayList;
import java.util.List;
/* loaded from: classes.dex */
public class ActionButtonsPreference extends Preference {
    private static final boolean mIsAtLeastS = BuildCompatUtils.isAtLeastS();
    private View mDivider1;
    private View mDivider2;
    private View mDivider3;
    private final ButtonInfo mButton1Info = new ButtonInfo();
    private final ButtonInfo mButton2Info = new ButtonInfo();
    private final ButtonInfo mButton3Info = new ButtonInfo();
    private final ButtonInfo mButton4Info = new ButtonInfo();
    private final List<ButtonInfo> mVisibleButtonInfos = new ArrayList(4);
    private final List<Drawable> mBtnBackgroundStyle1 = new ArrayList(1);
    private final List<Drawable> mBtnBackgroundStyle2 = new ArrayList(2);
    private final List<Drawable> mBtnBackgroundStyle3 = new ArrayList(3);
    private final List<Drawable> mBtnBackgroundStyle4 = new ArrayList(4);

    public ActionButtonsPreference(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        init();
    }

    private void init() {
        setLayoutResource(R$layout.settingslib_action_buttons);
        setSelectable(false);
        Resources resources = getContext().getResources();
        fetchDrawableArray(this.mBtnBackgroundStyle1, resources.obtainTypedArray(R$array.background_style1));
        fetchDrawableArray(this.mBtnBackgroundStyle2, resources.obtainTypedArray(R$array.background_style2));
        fetchDrawableArray(this.mBtnBackgroundStyle3, resources.obtainTypedArray(R$array.background_style3));
        fetchDrawableArray(this.mBtnBackgroundStyle4, resources.obtainTypedArray(R$array.background_style4));
    }

    private void fetchDrawableArray(List<Drawable> list, TypedArray typedArray) {
        for (int i = 0; i < typedArray.length(); i++) {
            list.add(getContext().getDrawable(typedArray.getResourceId(i, 0)));
        }
    }

    @Override // androidx.preference.Preference
    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        boolean z = mIsAtLeastS;
        preferenceViewHolder.setDividerAllowedAbove(!z);
        preferenceViewHolder.setDividerAllowedBelow(!z);
        this.mButton1Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button1);
        this.mButton2Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button2);
        this.mButton3Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button3);
        this.mButton4Info.mButton = (Button) preferenceViewHolder.findViewById(R$id.button4);
        this.mDivider1 = preferenceViewHolder.findViewById(R$id.divider1);
        this.mDivider2 = preferenceViewHolder.findViewById(R$id.divider2);
        this.mDivider3 = preferenceViewHolder.findViewById(R$id.divider3);
        this.mButton1Info.setUpButton();
        this.mButton2Info.setUpButton();
        this.mButton3Info.setUpButton();
        this.mButton4Info.setUpButton();
        if (!this.mVisibleButtonInfos.isEmpty()) {
            this.mVisibleButtonInfos.clear();
        }
        updateLayout();
    }

    /* access modifiers changed from: protected */
    @Override // androidx.preference.Preference
    public void notifyChanged() {
        super.notifyChanged();
        if (!this.mVisibleButtonInfos.isEmpty()) {
            this.mVisibleButtonInfos.clear();
            updateLayout();
        }
    }

    private void updateLayout() {
        if (this.mButton1Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton1Info);
        }
        if (this.mButton2Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton2Info);
        }
        if (this.mButton3Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton3Info);
        }
        if (this.mButton4Info.isVisible() && mIsAtLeastS) {
            this.mVisibleButtonInfos.add(this.mButton4Info);
        }
        boolean z = getContext().getResources().getConfiguration().getLayoutDirection() == 1;
        int size = this.mVisibleButtonInfos.size();
        if (size != 1) {
            if (size != 2) {
                if (size != 3) {
                    if (size != 4) {
                        Log.e("ActionButtonPreference", "No visible buttons info, skip background settings.");
                    } else if (z) {
                        setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle4);
                    } else {
                        setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle4);
                    }
                } else if (z) {
                    setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle3);
                } else {
                    setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle3);
                }
            } else if (z) {
                setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle2);
            } else {
                setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle2);
            }
        } else if (z) {
            setupRtlBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle1);
        } else {
            setupBackgrounds(this.mVisibleButtonInfos, this.mBtnBackgroundStyle1);
        }
        setupDivider1();
        setupDivider2();
        setupDivider3();
    }

    private void setupBackgrounds(List<ButtonInfo> list, List<Drawable> list2) {
        for (int i = 0; i < list2.size(); i++) {
            list.get(i).mButton.setBackground(list2.get(i));
        }
    }

    private void setupRtlBackgrounds(List<ButtonInfo> list, List<Drawable> list2) {
        for (int size = list2.size() - 1; size >= 0; size--) {
            list.get((list2.size() - 1) - size).mButton.setBackground(list2.get(size));
        }
    }

    private void setupDivider1() {
        if (this.mDivider1 != null && this.mButton1Info.isVisible() && this.mButton2Info.isVisible()) {
            this.mDivider1.setVisibility(0);
        }
    }

    private void setupDivider2() {
        if (this.mDivider2 != null && this.mButton3Info.isVisible()) {
            if (this.mButton1Info.isVisible() || this.mButton2Info.isVisible()) {
                this.mDivider2.setVisibility(0);
            }
        }
    }

    private void setupDivider3() {
        if (this.mDivider3 != null && this.mVisibleButtonInfos.size() > 1 && this.mButton4Info.isVisible()) {
            this.mDivider3.setVisibility(0);
        }
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class ButtonInfo {
        private Button mButton;
        private Drawable mIcon;
        private boolean mIsEnabled = true;
        private boolean mIsVisible = true;
        private View.OnClickListener mListener;
        private CharSequence mText;

        ButtonInfo() {
        }

        void setUpButton() {
            this.mButton.setText(this.mText);
            this.mButton.setOnClickListener(this.mListener);
            this.mButton.setEnabled(this.mIsEnabled);
            this.mButton.setCompoundDrawablesWithIntrinsicBounds((Drawable) null, this.mIcon, (Drawable) null, (Drawable) null);
            if (shouldBeVisible()) {
                this.mButton.setVisibility(0);
            } else {
                this.mButton.setVisibility(8);
            }
        }

        boolean isVisible() {
            return this.mButton.getVisibility() == 0;
        }

        private boolean shouldBeVisible() {
            return this.mIsVisible && (!TextUtils.isEmpty(this.mText) || this.mIcon != null);
        }
    }
}
