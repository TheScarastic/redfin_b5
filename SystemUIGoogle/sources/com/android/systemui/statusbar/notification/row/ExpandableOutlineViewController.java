package com.android.systemui.statusbar.notification.row;
/* loaded from: classes.dex */
public class ExpandableOutlineViewController {
    private final ExpandableViewController mExpandableViewController;
    private final ExpandableOutlineView mView;

    public ExpandableOutlineViewController(ExpandableOutlineView expandableOutlineView, ExpandableViewController expandableViewController) {
        this.mView = expandableOutlineView;
        this.mExpandableViewController = expandableViewController;
    }

    public void init() {
        this.mExpandableViewController.init();
    }
}
