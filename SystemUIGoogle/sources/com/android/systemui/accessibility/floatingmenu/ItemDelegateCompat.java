package com.android.systemui.accessibility.floatingmenu;

import android.content.res.Resources;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate;
import com.android.systemui.R$id;
import com.android.systemui.R$string;
import java.lang.ref.WeakReference;
/* loaded from: classes.dex */
final class ItemDelegateCompat extends RecyclerViewAccessibilityDelegate.ItemDelegate {
    private final WeakReference<AccessibilityFloatingMenuView> mMenuViewRef;

    /* access modifiers changed from: package-private */
    public ItemDelegateCompat(RecyclerViewAccessibilityDelegate recyclerViewAccessibilityDelegate, AccessibilityFloatingMenuView accessibilityFloatingMenuView) {
        super(recyclerViewAccessibilityDelegate);
        this.mMenuViewRef = new WeakReference<>(accessibilityFloatingMenuView);
    }

    @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate.ItemDelegate, androidx.core.view.AccessibilityDelegateCompat
    public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
        int i;
        int i2;
        super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
        if (this.mMenuViewRef.get() != null) {
            AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mMenuViewRef.get();
            Resources resources = accessibilityFloatingMenuView.getResources();
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R$id.action_move_top_left, resources.getString(R$string.accessibility_floating_button_action_move_top_left)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R$id.action_move_top_right, resources.getString(R$string.accessibility_floating_button_action_move_top_right)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R$id.action_move_bottom_left, resources.getString(R$string.accessibility_floating_button_action_move_bottom_left)));
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(R$id.action_move_bottom_right, resources.getString(R$string.accessibility_floating_button_action_move_bottom_right)));
            if (accessibilityFloatingMenuView.isOvalShape()) {
                i = R$id.action_move_to_edge_and_hide;
            } else {
                i = R$id.action_move_out_edge_and_show;
            }
            if (accessibilityFloatingMenuView.isOvalShape()) {
                i2 = R$string.accessibility_floating_button_action_move_to_edge_and_hide_to_half;
            } else {
                i2 = R$string.accessibility_floating_button_action_move_out_edge_and_show;
            }
            accessibilityNodeInfoCompat.addAction(new AccessibilityNodeInfoCompat.AccessibilityActionCompat(i, resources.getString(i2)));
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerViewAccessibilityDelegate.ItemDelegate, androidx.core.view.AccessibilityDelegateCompat
    public boolean performAccessibilityAction(View view, int i, Bundle bundle) {
        if (this.mMenuViewRef.get() == null) {
            return super.performAccessibilityAction(view, i, bundle);
        }
        AccessibilityFloatingMenuView accessibilityFloatingMenuView = this.mMenuViewRef.get();
        accessibilityFloatingMenuView.fadeIn();
        Rect availableBounds = accessibilityFloatingMenuView.getAvailableBounds();
        if (i == R$id.action_move_top_left) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.left, availableBounds.top);
            return true;
        } else if (i == R$id.action_move_top_right) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.right, availableBounds.top);
            return true;
        } else if (i == R$id.action_move_bottom_left) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.left, availableBounds.bottom);
            return true;
        } else if (i == R$id.action_move_bottom_right) {
            accessibilityFloatingMenuView.setShapeType(0);
            accessibilityFloatingMenuView.snapToLocation(availableBounds.right, availableBounds.bottom);
            return true;
        } else if (i == R$id.action_move_to_edge_and_hide) {
            accessibilityFloatingMenuView.setShapeType(1);
            return true;
        } else if (i != R$id.action_move_out_edge_and_show) {
            return super.performAccessibilityAction(view, i, bundle);
        } else {
            accessibilityFloatingMenuView.setShapeType(0);
            return true;
        }
    }
}
