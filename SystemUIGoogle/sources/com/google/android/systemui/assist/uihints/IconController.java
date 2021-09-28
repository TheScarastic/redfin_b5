package com.google.android.systemui.assist.uihints;

import android.app.PendingIntent;
import android.graphics.Rect;
import android.graphics.Region;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$id;
import com.android.systemui.statusbar.policy.ConfigurationController;
import com.google.android.systemui.assist.uihints.NgaMessageHandler;
import com.google.android.systemui.assist.uihints.input.TouchActionRegion;
import java.util.Optional;
/* loaded from: classes2.dex */
public class IconController implements NgaMessageHandler.KeyboardInfoListener, NgaMessageHandler.ZerostateInfoListener, ConfigurationController.ConfigurationListener, TouchActionRegion {
    private boolean mHasAccurateLuma;
    private final KeyboardIconView mKeyboardIcon;
    private boolean mKeyboardIconRequested;
    private PendingIntent mOnKeyboardIconTap;
    private PendingIntent mOnZerostateIconTap;
    private final ViewGroup mParent;
    private final ZeroStateIconView mZeroStateIcon;
    private boolean mZerostateIconRequested;

    public IconController(LayoutInflater layoutInflater, ViewGroup viewGroup, ConfigurationController configurationController) {
        this.mParent = viewGroup;
        KeyboardIconView keyboardIconView = (KeyboardIconView) viewGroup.findViewById(R$id.keyboard);
        this.mKeyboardIcon = keyboardIconView;
        keyboardIconView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.assist.uihints.IconController$$ExternalSyntheticLambda1
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IconController.m622$r8$lambda$TKGHQaseivsWmxHVquOfAFZ3C4(IconController.this, view);
            }
        });
        ZeroStateIconView zeroStateIconView = (ZeroStateIconView) viewGroup.findViewById(R$id.zerostate);
        this.mZeroStateIcon = zeroStateIconView;
        zeroStateIconView.setOnClickListener(new View.OnClickListener() { // from class: com.google.android.systemui.assist.uihints.IconController$$ExternalSyntheticLambda0
            @Override // android.view.View.OnClickListener
            public final void onClick(View view) {
                IconController.$r8$lambda$QREIPGMxPvGuPM9x2v5ywsVuUtc(IconController.this, view);
            }
        });
        configurationController.addCallback(this);
    }

    public /* synthetic */ void lambda$new$0(View view) {
        PendingIntent pendingIntent = this.mOnKeyboardIconTap;
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.e("IconController", "Pending intent cancelled", e);
            }
        }
    }

    public /* synthetic */ void lambda$new$1(View view) {
        PendingIntent pendingIntent = this.mOnZerostateIconTap;
        if (pendingIntent != null) {
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                Log.e("IconController", "Pending intent cancelled", e);
            }
        }
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.KeyboardInfoListener
    public void onShowKeyboard(PendingIntent pendingIntent) {
        this.mKeyboardIconRequested = pendingIntent != null;
        this.mOnKeyboardIconTap = pendingIntent;
        maybeUpdateKeyboardVisibility();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.KeyboardInfoListener
    public void onHideKeyboard() {
        this.mKeyboardIconRequested = false;
        this.mOnKeyboardIconTap = null;
        maybeUpdateKeyboardVisibility();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ZerostateInfoListener
    public void onShowZerostate(PendingIntent pendingIntent) {
        this.mZerostateIconRequested = pendingIntent != null;
        this.mOnZerostateIconTap = pendingIntent;
        maybeUpdateZerostateVisibility();
    }

    @Override // com.google.android.systemui.assist.uihints.NgaMessageHandler.ZerostateInfoListener
    public void onHideZerostate() {
        this.mZerostateIconRequested = false;
        this.mOnZerostateIconTap = null;
        maybeUpdateZerostateVisibility();
    }

    @Override // com.android.systemui.statusbar.policy.ConfigurationController.ConfigurationListener
    public void onDensityOrFontScaleChanged() {
        this.mKeyboardIcon.onDensityChanged();
        this.mZeroStateIcon.onDensityChanged();
    }

    public void setHasAccurateLuma(boolean z) {
        this.mHasAccurateLuma = z;
        maybeUpdateKeyboardVisibility();
        maybeUpdateZerostateVisibility();
    }

    public void setHasDarkBackground(boolean z) {
        this.mKeyboardIcon.setHasDarkBackground(z);
        this.mZeroStateIcon.setHasDarkBackground(z);
    }

    public boolean isRequested() {
        return this.mKeyboardIconRequested || this.mZerostateIconRequested;
    }

    public boolean isVisible() {
        return this.mKeyboardIcon.getVisibility() == 0 || this.mZeroStateIcon.getVisibility() == 0;
    }

    @Override // com.google.android.systemui.assist.uihints.input.TouchActionRegion
    public Optional<Region> getTouchActionRegion() {
        Region region = new Region();
        if (this.mKeyboardIcon.getVisibility() == 0) {
            Rect rect = new Rect();
            this.mKeyboardIcon.getHitRect(rect);
            region.union(rect);
        }
        if (this.mZeroStateIcon.getVisibility() == 0) {
            Rect rect2 = new Rect();
            this.mZeroStateIcon.getHitRect(rect2);
            region.union(rect2);
        }
        return region.isEmpty() ? Optional.empty() : Optional.of(region);
    }

    private void maybeUpdateKeyboardVisibility() {
        maybeUpdateIconVisibility(this.mKeyboardIcon, this.mKeyboardIconRequested);
    }

    private void maybeUpdateZerostateVisibility() {
        maybeUpdateIconVisibility(this.mZeroStateIcon, this.mZerostateIconRequested);
    }

    private void maybeUpdateIconVisibility(View view, boolean z) {
        boolean z2 = true;
        int i = 0;
        boolean z3 = view.getVisibility() == 0;
        if (!z || !this.mHasAccurateLuma) {
            z2 = false;
        }
        if (z3 != z2) {
            if (!z2) {
                i = 8;
            }
            view.setVisibility(i);
        }
    }
}
