package com.android.systemui.controls.management;

import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import com.android.systemui.controls.management.ControlsModel;
import kotlin.jvm.functions.Function0;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.DefaultConstructorMarker;
import kotlin.jvm.internal.Intrinsics;
/* compiled from: ControlAdapter.kt */
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public final class ControlHolderAccessibilityDelegate extends AccessibilityDelegateCompat {
    private boolean isFavorite;
    private final ControlsModel.MoveHelper moveHelper;
    private final Function0<Integer> positionRetriever;
    private final Function1<Boolean, CharSequence> stateRetriever;
    public static final Companion Companion = new Companion(null);
    private static final int MOVE_BEFORE_ID = R$id.accessibility_action_controls_move_before;
    private static final int MOVE_AFTER_ID = R$id.accessibility_action_controls_move_after;

    /* JADX DEBUG: Multi-variable search result rejected for r2v0, resolved type: kotlin.jvm.functions.Function1<? super java.lang.Boolean, ? extends java.lang.CharSequence> */
    /* JADX WARN: Multi-variable type inference failed */
    public ControlHolderAccessibilityDelegate(Function1<? super Boolean, ? extends CharSequence> function1, Function0<Integer> function0, ControlsModel.MoveHelper moveHelper) {
        Intrinsics.checkNotNullParameter(function1, "stateRetriever");
        Intrinsics.checkNotNullParameter(function0, "positionRetriever");
        this.stateRetriever = function1;
        this.positionRetriever = function0;
        this.moveHelper = moveHelper;
    }

    public final void setFavorite(boolean z) {
        this.isFavorite = z;
    }

    /* compiled from: ControlAdapter.kt */
    /* loaded from: classes.dex */
    public static final class Companion {
        public /* synthetic */ Companion(DefaultConstructorMarker defaultConstructorMarker) {
            this();
        }

        private Companion() {
        }
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        Intrinsics.checkNotNullParameter(view, "host");
        Intrinsics.checkNotNullParameter(accessibilityNodeInfoCompat, "info");
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.setContextClickable(false);
        addClickAction(view, accessibilityNodeInfoCompat);
        maybeAddMoveBeforeAction(view, accessibilityNodeInfoCompat);
        maybeAddMoveAfterAction(view, accessibilityNodeInfoCompat);
        accessibilityNodeInfoCompat.setStateDescription(this.stateRetriever.invoke(Boolean.valueOf(this.isFavorite)));
        accessibilityNodeInfoCompat.setCollectionItemInfo(null);
        accessibilityNodeInfoCompat.setClassName(Switch.class.getName());
    }

    @Override // androidx.core.view.AccessibilityDelegateCompat
    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (super.performAccessibilityAction(view, i, bundle)) {
            return true;
        }
        if (i == MOVE_BEFORE_ID) {
            ControlsModel.MoveHelper moveHelper = this.moveHelper;
            if (moveHelper == null) {
                return true;
            }
            moveHelper.moveBefore(this.positionRetriever.invoke().intValue());
            return true;
        } else if (i != MOVE_AFTER_ID) {
            return false;
        } else {
            ControlsModel.MoveHelper moveHelper2 = this.moveHelper;
            if (moveHelper2 == null) {
                return true;
            }
            moveHelper2.moveAfter(this.positionRetriever.invoke().intValue());
            return true;
        }
    }

    private final void addClickAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        String str;
        if (this.isFavorite) {
            str = view.getContext().getString(R$string.accessibility_control_change_unfavorite);
        } else {
            str = view.getContext().getString(R$string.accessibility_control_change_favorite);
        }
        accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(16, str));
    }

    private final void maybeAddMoveBeforeAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ControlsModel.MoveHelper moveHelper = this.moveHelper;
        if (moveHelper == null ? false : moveHelper.canMoveBefore(this.positionRetriever.invoke().intValue())) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_BEFORE_ID, view.getContext().getString(R$string.accessibility_control_move, Integer.valueOf((this.positionRetriever.invoke().intValue() + 1) - 1))));
            accessibilityNodeInfoCompat.setContextClickable(true);
        }
    }

    private final void maybeAddMoveAfterAction(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        ControlsModel.MoveHelper moveHelper = this.moveHelper;
        if (moveHelper == null ? false : moveHelper.canMoveAfter(this.positionRetriever.invoke().intValue())) {
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(MOVE_AFTER_ID, view.getContext().getString(R$string.accessibility_control_move, Integer.valueOf(this.positionRetriever.invoke().intValue() + 1 + 1))));
            accessibilityNodeInfoCompat.setContextClickable(true);
        }
    }
}
