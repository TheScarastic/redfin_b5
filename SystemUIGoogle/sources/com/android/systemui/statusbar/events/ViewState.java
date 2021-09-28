package com.android.systemui.statusbar.events;

import android.graphics.Rect;
import android.view.View;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: PrivacyDotViewController.kt */
/* loaded from: classes.dex */
public final class ViewState {
    private final String contentDescription;
    private final int cornerIndex;
    private final View designatedCorner;
    private final int height;
    private final Rect landscapeRect;
    private final boolean layoutRtl;
    private final Rect portraitRect;
    private final boolean qsExpanded;
    private final int rotation;
    private final Rect seascapeRect;
    private final boolean shadeExpanded;
    private final boolean systemPrivacyEventIsActive;
    private final Rect upsideDownRect;
    private final boolean viewInitialized;

    public ViewState() {
        this(false, false, false, false, null, null, null, null, false, 0, 0, 0, null, null, 16383, null);
    }

    public final ViewState copy(boolean z, boolean z2, boolean z3, boolean z4, Rect rect, Rect rect2, Rect rect3, Rect rect4, boolean z5, int i, int i2, int i3, View view, String str) {
        return new ViewState(z, z2, z3, z4, rect, rect2, rect3, rect4, z5, i, i2, i3, view, str);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ViewState)) {
            return false;
        }
        ViewState viewState = (ViewState) obj;
        return this.viewInitialized == viewState.viewInitialized && this.systemPrivacyEventIsActive == viewState.systemPrivacyEventIsActive && this.shadeExpanded == viewState.shadeExpanded && this.qsExpanded == viewState.qsExpanded && Intrinsics.areEqual(this.portraitRect, viewState.portraitRect) && Intrinsics.areEqual(this.landscapeRect, viewState.landscapeRect) && Intrinsics.areEqual(this.upsideDownRect, viewState.upsideDownRect) && Intrinsics.areEqual(this.seascapeRect, viewState.seascapeRect) && this.layoutRtl == viewState.layoutRtl && this.rotation == viewState.rotation && this.height == viewState.height && this.cornerIndex == viewState.cornerIndex && Intrinsics.areEqual(this.designatedCorner, viewState.designatedCorner) && Intrinsics.areEqual(this.contentDescription, viewState.contentDescription);
    }

    public int hashCode() {
        boolean z = this.viewInitialized;
        int i = 1;
        if (z) {
            z = true;
        }
        int i2 = z ? 1 : 0;
        int i3 = z ? 1 : 0;
        int i4 = z ? 1 : 0;
        int i5 = i2 * 31;
        boolean z2 = this.systemPrivacyEventIsActive;
        if (z2) {
            z2 = true;
        }
        int i6 = z2 ? 1 : 0;
        int i7 = z2 ? 1 : 0;
        int i8 = z2 ? 1 : 0;
        int i9 = (i5 + i6) * 31;
        boolean z3 = this.shadeExpanded;
        if (z3) {
            z3 = true;
        }
        int i10 = z3 ? 1 : 0;
        int i11 = z3 ? 1 : 0;
        int i12 = z3 ? 1 : 0;
        int i13 = (i9 + i10) * 31;
        boolean z4 = this.qsExpanded;
        if (z4) {
            z4 = true;
        }
        int i14 = z4 ? 1 : 0;
        int i15 = z4 ? 1 : 0;
        int i16 = z4 ? 1 : 0;
        int i17 = (i13 + i14) * 31;
        Rect rect = this.portraitRect;
        int i18 = 0;
        int hashCode = (i17 + (rect == null ? 0 : rect.hashCode())) * 31;
        Rect rect2 = this.landscapeRect;
        int hashCode2 = (hashCode + (rect2 == null ? 0 : rect2.hashCode())) * 31;
        Rect rect3 = this.upsideDownRect;
        int hashCode3 = (hashCode2 + (rect3 == null ? 0 : rect3.hashCode())) * 31;
        Rect rect4 = this.seascapeRect;
        int hashCode4 = (hashCode3 + (rect4 == null ? 0 : rect4.hashCode())) * 31;
        boolean z5 = this.layoutRtl;
        if (!z5) {
            i = z5 ? 1 : 0;
        }
        int hashCode5 = (((((((hashCode4 + i) * 31) + Integer.hashCode(this.rotation)) * 31) + Integer.hashCode(this.height)) * 31) + Integer.hashCode(this.cornerIndex)) * 31;
        View view = this.designatedCorner;
        int hashCode6 = (hashCode5 + (view == null ? 0 : view.hashCode())) * 31;
        String str = this.contentDescription;
        if (str != null) {
            i18 = str.hashCode();
        }
        return hashCode6 + i18;
    }

    public String toString() {
        return "ViewState(viewInitialized=" + this.viewInitialized + ", systemPrivacyEventIsActive=" + this.systemPrivacyEventIsActive + ", shadeExpanded=" + this.shadeExpanded + ", qsExpanded=" + this.qsExpanded + ", portraitRect=" + this.portraitRect + ", landscapeRect=" + this.landscapeRect + ", upsideDownRect=" + this.upsideDownRect + ", seascapeRect=" + this.seascapeRect + ", layoutRtl=" + this.layoutRtl + ", rotation=" + this.rotation + ", height=" + this.height + ", cornerIndex=" + this.cornerIndex + ", designatedCorner=" + this.designatedCorner + ", contentDescription=" + ((Object) this.contentDescription) + ')';
    }

    public ViewState(boolean z, boolean z2, boolean z3, boolean z4, Rect rect, Rect rect2, Rect rect3, Rect rect4, boolean z5, int i, int i2, int i3, View view, String str) {
        this.viewInitialized = z;
        this.systemPrivacyEventIsActive = z2;
        this.shadeExpanded = z3;
        this.qsExpanded = z4;
        this.portraitRect = rect;
        this.landscapeRect = rect2;
        this.upsideDownRect = rect3;
        this.seascapeRect = rect4;
        this.layoutRtl = z5;
        this.rotation = i;
        this.height = i2;
        this.cornerIndex = i3;
        this.designatedCorner = view;
        this.contentDescription = str;
    }

    public /* synthetic */ ViewState(boolean z, boolean z2, boolean z3, boolean z4, Rect rect, Rect rect2, Rect rect3, Rect rect4, boolean z5, int i, int i2, int i3, View view, String str, int i4, DefaultConstructorMarker defaultConstructorMarker) {
        this((i4 & 1) != 0 ? false : z, (i4 & 2) != 0 ? false : z2, (i4 & 4) != 0 ? false : z3, (i4 & 8) != 0 ? false : z4, (i4 & 16) != 0 ? null : rect, (i4 & 32) != 0 ? null : rect2, (i4 & 64) != 0 ? null : rect3, (i4 & 128) != 0 ? null : rect4, (i4 & 256) != 0 ? false : z5, (i4 & 512) != 0 ? 0 : i, (i4 & 1024) == 0 ? i2 : 0, (i4 & 2048) != 0 ? -1 : i3, (i4 & 4096) != 0 ? null : view, (i4 & 8192) == 0 ? str : null);
    }

    public final boolean getViewInitialized() {
        return this.viewInitialized;
    }

    public final boolean getLayoutRtl() {
        return this.layoutRtl;
    }

    public final int getRotation() {
        return this.rotation;
    }

    public final View getDesignatedCorner() {
        return this.designatedCorner;
    }

    public final String getContentDescription() {
        return this.contentDescription;
    }

    public final boolean shouldShowDot() {
        return this.systemPrivacyEventIsActive && !this.shadeExpanded && !this.qsExpanded;
    }

    public final boolean needsLayout(ViewState viewState) {
        Intrinsics.checkNotNullParameter(viewState, "other");
        return this.rotation != viewState.rotation || this.layoutRtl != viewState.layoutRtl || !Intrinsics.areEqual(this.portraitRect, viewState.portraitRect) || !Intrinsics.areEqual(this.landscapeRect, viewState.landscapeRect) || !Intrinsics.areEqual(this.upsideDownRect, viewState.upsideDownRect) || !Intrinsics.areEqual(this.seascapeRect, viewState.seascapeRect);
    }

    public final Rect contentRectForRotation(int i) {
        if (i == 0) {
            Rect rect = this.portraitRect;
            Intrinsics.checkNotNull(rect);
            return rect;
        } else if (i == 1) {
            Rect rect2 = this.landscapeRect;
            Intrinsics.checkNotNull(rect2);
            return rect2;
        } else if (i == 2) {
            Rect rect3 = this.upsideDownRect;
            Intrinsics.checkNotNull(rect3);
            return rect3;
        } else if (i == 3) {
            Rect rect4 = this.seascapeRect;
            Intrinsics.checkNotNull(rect4);
            return rect4;
        } else {
            throw new IllegalArgumentException("not a rotation (" + i + ')');
        }
    }
}
