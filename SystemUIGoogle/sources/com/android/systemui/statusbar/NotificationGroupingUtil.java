package com.android.systemui.statusbar;

import android.app.Notification;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.NotificationHeaderView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.android.internal.widget.CachingIconView;
import com.android.internal.widget.ConversationLayout;
import com.android.internal.widget.ImageFloatingTextView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.NotificationContentView;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
/* loaded from: classes.dex */
public class NotificationGroupingUtil {
    private final HashSet<Integer> mDividers;
    private final ArrayList<Processor> mProcessors;
    private final ExpandableNotificationRow mRow;
    private static final TextViewComparator TEXT_VIEW_COMPARATOR = new TextViewComparator();
    private static final TextViewComparator APP_NAME_COMPARATOR = new AppNameComparator();
    private static final ViewComparator BADGE_COMPARATOR = new BadgeComparator();
    private static final VisibilityApplicator VISIBILITY_APPLICATOR = new VisibilityApplicator();
    private static final VisibilityApplicator APP_NAME_APPLICATOR = new AppNameApplicator();
    private static final ResultApplicator LEFT_ICON_APPLICATOR = new LeftIconApplicator();
    private static final DataExtractor ICON_EXTRACTOR = new DataExtractor() { // from class: com.android.systemui.statusbar.NotificationGroupingUtil.1
        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.DataExtractor
        public Object extractData(ExpandableNotificationRow expandableNotificationRow) {
            return expandableNotificationRow.getEntry().getSbn().getNotification();
        }
    };
    private static final IconComparator ICON_VISIBILITY_COMPARATOR = new IconComparator() { // from class: com.android.systemui.statusbar.NotificationGroupingUtil.2
        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean compare(View view, View view2, Object obj, Object obj2) {
            return hasSameIcon(obj, obj2) && hasSameColor(obj, obj2);
        }
    };
    private static final IconComparator GREY_COMPARATOR = new IconComparator() { // from class: com.android.systemui.statusbar.NotificationGroupingUtil.3
        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean compare(View view, View view2, Object obj, Object obj2) {
            return !hasSameIcon(obj, obj2) || hasSameColor(obj, obj2);
        }
    };
    private static final ResultApplicator GREY_APPLICATOR = new ResultApplicator() { // from class: com.android.systemui.statusbar.NotificationGroupingUtil.4
        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ResultApplicator
        public void apply(View view, View view2, boolean z, boolean z2) {
            CachingIconView findViewById = view2.findViewById(16908294);
            if (findViewById != null) {
                findViewById.setGrayedOut(z);
            }
        }
    };

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface DataExtractor {
        Object extractData(ExpandableNotificationRow expandableNotificationRow);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface ResultApplicator {
        void apply(View view, View view2, boolean z, boolean z2);
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public interface ViewComparator {
        boolean compare(View view, View view2, Object obj, Object obj2);

        boolean isEmpty(View view);
    }

    public NotificationGroupingUtil(ExpandableNotificationRow expandableNotificationRow) {
        ArrayList<Processor> arrayList = new ArrayList<>();
        this.mProcessors = arrayList;
        HashSet<Integer> hashSet = new HashSet<>();
        this.mDividers = hashSet;
        this.mRow = expandableNotificationRow;
        DataExtractor dataExtractor = ICON_EXTRACTOR;
        IconComparator iconComparator = ICON_VISIBILITY_COMPARATOR;
        VisibilityApplicator visibilityApplicator = VISIBILITY_APPLICATOR;
        arrayList.add(new Processor(expandableNotificationRow, 16908294, dataExtractor, iconComparator, visibilityApplicator));
        arrayList.add(new Processor(expandableNotificationRow, 16909496, dataExtractor, GREY_COMPARATOR, GREY_APPLICATOR));
        arrayList.add(new Processor(expandableNotificationRow, 16909496, dataExtractor, iconComparator, LEFT_ICON_APPLICATOR));
        arrayList.add(new Processor(expandableNotificationRow, 16909325, null, BADGE_COMPARATOR, visibilityApplicator));
        arrayList.add(new Processor(expandableNotificationRow, 16908763, null, APP_NAME_COMPARATOR, APP_NAME_APPLICATOR));
        arrayList.add(Processor.forTextView(expandableNotificationRow, 16909043));
        hashSet.add(16909044);
        hashSet.add(16909046);
        hashSet.add(16909559);
    }

    public void updateChildrenAppearance() {
        List<ExpandableNotificationRow> attachedChildren = this.mRow.getAttachedChildren();
        if (attachedChildren != null && this.mRow.isSummaryWithChildren()) {
            for (int i = 0; i < this.mProcessors.size(); i++) {
                this.mProcessors.get(i).init();
            }
            for (int i2 = 0; i2 < attachedChildren.size(); i2++) {
                ExpandableNotificationRow expandableNotificationRow = attachedChildren.get(i2);
                for (int i3 = 0; i3 < this.mProcessors.size(); i3++) {
                    this.mProcessors.get(i3).compareToGroupParent(expandableNotificationRow);
                }
            }
            for (int i4 = 0; i4 < attachedChildren.size(); i4++) {
                ExpandableNotificationRow expandableNotificationRow2 = attachedChildren.get(i4);
                for (int i5 = 0; i5 < this.mProcessors.size(); i5++) {
                    this.mProcessors.get(i5).apply(expandableNotificationRow2);
                }
                sanitizeTopLineViews(expandableNotificationRow2);
            }
        }
    }

    private void sanitizeTopLineViews(ExpandableNotificationRow expandableNotificationRow) {
        if (expandableNotificationRow.isSummaryWithChildren()) {
            sanitizeTopLine(expandableNotificationRow.getNotificationViewWrapper().getNotificationHeader(), expandableNotificationRow);
            return;
        }
        NotificationContentView privateLayout = expandableNotificationRow.getPrivateLayout();
        sanitizeChild(privateLayout.getContractedChild(), expandableNotificationRow);
        sanitizeChild(privateLayout.getHeadsUpChild(), expandableNotificationRow);
        sanitizeChild(privateLayout.getExpandedChild(), expandableNotificationRow);
    }

    private void sanitizeChild(View view, ExpandableNotificationRow expandableNotificationRow) {
        if (view != null) {
            sanitizeTopLine((ViewGroup) view.findViewById(16909246), expandableNotificationRow);
        }
    }

    private void sanitizeTopLine(ViewGroup viewGroup, ExpandableNotificationRow expandableNotificationRow) {
        boolean z;
        boolean z2;
        View view;
        if (viewGroup != null) {
            int childCount = viewGroup.getChildCount();
            View findViewById = viewGroup.findViewById(16909555);
            int i = 0;
            while (true) {
                if (i >= childCount) {
                    z = false;
                    break;
                }
                View childAt = viewGroup.getChildAt(i);
                if ((childAt instanceof TextView) && childAt.getVisibility() != 8 && !this.mDividers.contains(Integer.valueOf(childAt.getId())) && childAt != findViewById) {
                    z = true;
                    break;
                }
                i++;
            }
            findViewById.setVisibility((!z || expandableNotificationRow.getEntry().getSbn().getNotification().showsTime()) ? 0 : 8);
            View view2 = null;
            int i2 = 0;
            while (i2 < childCount) {
                View childAt2 = viewGroup.getChildAt(i2);
                if (this.mDividers.contains(Integer.valueOf(childAt2.getId()))) {
                    while (true) {
                        i2++;
                        if (i2 >= childCount) {
                            break;
                        }
                        view = viewGroup.getChildAt(i2);
                        if (this.mDividers.contains(Integer.valueOf(view.getId()))) {
                            i2--;
                            break;
                        } else if (view.getVisibility() != 8 && (view instanceof TextView)) {
                            if (view2 != null) {
                                z2 = true;
                            }
                        }
                    }
                    view = view2;
                    z2 = false;
                    childAt2.setVisibility(z2 ? 0 : 8);
                    view2 = view;
                } else if (childAt2.getVisibility() != 8 && (childAt2 instanceof TextView)) {
                    view2 = childAt2;
                }
                i2++;
            }
        }
    }

    public void restoreChildNotification(ExpandableNotificationRow expandableNotificationRow) {
        for (int i = 0; i < this.mProcessors.size(); i++) {
            this.mProcessors.get(i).apply(expandableNotificationRow, true);
        }
        sanitizeTopLineViews(expandableNotificationRow);
    }

    /* loaded from: classes.dex */
    private static class Processor {
        private final ResultApplicator mApplicator;
        private boolean mApply;
        private final ViewComparator mComparator;
        private final DataExtractor mExtractor;
        private final int mId;
        private Object mParentData;
        private final ExpandableNotificationRow mParentRow;
        private View mParentView;

        public static Processor forTextView(ExpandableNotificationRow expandableNotificationRow, int i) {
            return new Processor(expandableNotificationRow, i, null, NotificationGroupingUtil.TEXT_VIEW_COMPARATOR, NotificationGroupingUtil.VISIBILITY_APPLICATOR);
        }

        Processor(ExpandableNotificationRow expandableNotificationRow, int i, DataExtractor dataExtractor, ViewComparator viewComparator, ResultApplicator resultApplicator) {
            this.mId = i;
            this.mExtractor = dataExtractor;
            this.mApplicator = resultApplicator;
            this.mComparator = viewComparator;
            this.mParentRow = expandableNotificationRow;
        }

        public void init() {
            View view;
            NotificationHeaderView notificationHeader = this.mParentRow.getNotificationViewWrapper().getNotificationHeader();
            Object obj = null;
            if (notificationHeader == null) {
                view = null;
            } else {
                view = notificationHeader.findViewById(this.mId);
            }
            this.mParentView = view;
            DataExtractor dataExtractor = this.mExtractor;
            if (dataExtractor != null) {
                obj = dataExtractor.extractData(this.mParentRow);
            }
            this.mParentData = obj;
            this.mApply = !this.mComparator.isEmpty(this.mParentView);
        }

        public void compareToGroupParent(ExpandableNotificationRow expandableNotificationRow) {
            View contractedChild;
            View findViewById;
            if (this.mApply && (contractedChild = expandableNotificationRow.getPrivateLayout().getContractedChild()) != null && (findViewById = contractedChild.findViewById(this.mId)) != null) {
                DataExtractor dataExtractor = this.mExtractor;
                this.mApply = this.mComparator.compare(this.mParentView, findViewById, this.mParentData, dataExtractor == null ? null : dataExtractor.extractData(expandableNotificationRow));
            }
        }

        public void apply(ExpandableNotificationRow expandableNotificationRow) {
            apply(expandableNotificationRow, false);
        }

        public void apply(ExpandableNotificationRow expandableNotificationRow, boolean z) {
            boolean z2 = this.mApply && !z;
            if (expandableNotificationRow.isSummaryWithChildren()) {
                applyToView(z2, z, expandableNotificationRow.getNotificationViewWrapper().getNotificationHeader());
                return;
            }
            applyToView(z2, z, expandableNotificationRow.getPrivateLayout().getContractedChild());
            applyToView(z2, z, expandableNotificationRow.getPrivateLayout().getHeadsUpChild());
            applyToView(z2, z, expandableNotificationRow.getPrivateLayout().getExpandedChild());
        }

        private void applyToView(boolean z, boolean z2, View view) {
            View findViewById;
            if (view != null && (findViewById = view.findViewById(this.mId)) != null && !this.mComparator.isEmpty(findViewById)) {
                this.mApplicator.apply(view, findViewById, z, z2);
            }
        }
    }

    /* loaded from: classes.dex */
    private static class BadgeComparator implements ViewComparator {
        private BadgeComparator() {
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean compare(View view, View view2, Object obj, Object obj2) {
            return view.getVisibility() != 8;
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean isEmpty(View view) {
            if (!(view instanceof ImageView) || ((ImageView) view).getDrawable() != null) {
                return false;
            }
            return true;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class TextViewComparator implements ViewComparator {
        private TextViewComparator() {
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean compare(View view, View view2, Object obj, Object obj2) {
            CharSequence charSequence;
            TextView textView = (TextView) view;
            CharSequence charSequence2 = "";
            if (textView == null) {
                charSequence = charSequence2;
            } else {
                charSequence = textView.getText();
            }
            TextView textView2 = (TextView) view2;
            if (textView2 != null) {
                charSequence2 = textView2.getText();
            }
            return Objects.equals(charSequence, charSequence2);
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean isEmpty(View view) {
            return view == null || TextUtils.isEmpty(((TextView) view).getText());
        }
    }

    /* loaded from: classes.dex */
    private static abstract class IconComparator implements ViewComparator {
        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean isEmpty(View view) {
            return false;
        }

        private IconComparator() {
        }

        protected boolean hasSameIcon(Object obj, Object obj2) {
            return ((Notification) obj).getSmallIcon().sameAs(((Notification) obj2).getSmallIcon());
        }

        protected boolean hasSameColor(Object obj, Object obj2) {
            return ((Notification) obj).color == ((Notification) obj2).color;
        }
    }

    /* access modifiers changed from: private */
    /* loaded from: classes.dex */
    public static class VisibilityApplicator implements ResultApplicator {
        private VisibilityApplicator() {
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ResultApplicator
        public void apply(View view, View view2, boolean z, boolean z2) {
            if (view2 != null) {
                view2.setVisibility(z ? 8 : 0);
            }
        }
    }

    /* loaded from: classes.dex */
    private static class AppNameApplicator extends VisibilityApplicator {
        private AppNameApplicator() {
            super();
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.VisibilityApplicator, com.android.systemui.statusbar.NotificationGroupingUtil.ResultApplicator
        public void apply(View view, View view2, boolean z, boolean z2) {
            if (z2 && (view instanceof ConversationLayout)) {
                z = ((ConversationLayout) view).shouldHideAppName();
            }
            super.apply(view, view2, z, z2);
        }
    }

    /* loaded from: classes.dex */
    private static class AppNameComparator extends TextViewComparator {
        private AppNameComparator() {
            super();
        }

        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.TextViewComparator, com.android.systemui.statusbar.NotificationGroupingUtil.ViewComparator
        public boolean compare(View view, View view2, Object obj, Object obj2) {
            if (isEmpty(view2)) {
                return true;
            }
            return super.compare(view, view2, obj, obj2);
        }
    }

    /* loaded from: classes.dex */
    private static class LeftIconApplicator implements ResultApplicator {
        public static final int[] MARGIN_ADJUSTED_VIEWS = {16909525, 16908794, 16908310, 16909237, 16909234};

        private LeftIconApplicator() {
        }

        /* JADX WARNING: Removed duplicated region for block: B:13:0x003e  */
        /* JADX WARNING: Removed duplicated region for block: B:23:0x0053  */
        /* JADX WARNING: Removed duplicated region for block: B:24:0x0055  */
        /* JADX WARNING: Removed duplicated region for block: B:27:0x005b  */
        /* JADX WARNING: Removed duplicated region for block: B:37:0x007e A[ORIG_RETURN, RETURN] */
        @Override // com.android.systemui.statusbar.NotificationGroupingUtil.ResultApplicator
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void apply(android.view.View r6, android.view.View r7, boolean r8, boolean r9) {
            /*
                r5 = this;
                r6 = 16909134(0x102034e, float:2.38796E-38)
                android.view.View r6 = r7.findViewById(r6)
                android.widget.ImageView r6 = (android.widget.ImageView) r6
                if (r6 != 0) goto L_0x000c
                return
            L_0x000c:
                r9 = 16909380(0x1020444, float:2.388029E-38)
                android.view.View r9 = r7.findViewById(r9)
                android.widget.ImageView r9 = (android.widget.ImageView) r9
                r0 = 1
                r1 = 0
                if (r9 == 0) goto L_0x002c
                java.lang.Integer r2 = java.lang.Integer.valueOf(r0)
                r3 = 16909518(0x10204ce, float:2.3880676E-38)
                java.lang.Object r3 = r9.getTag(r3)
                boolean r2 = r2.equals(r3)
                if (r2 == 0) goto L_0x002c
                r2 = r0
                goto L_0x002d
            L_0x002c:
                r2 = r1
            L_0x002d:
                java.lang.Integer r3 = java.lang.Integer.valueOf(r0)
                r4 = 16909524(0x10204d4, float:2.3880693E-38)
                java.lang.Object r4 = r6.getTag(r4)
                boolean r3 = r3.equals(r4)
                if (r3 == 0) goto L_0x004f
                r3 = 0
                if (r9 != 0) goto L_0x0043
                r4 = r3
                goto L_0x0047
            L_0x0043:
                android.graphics.drawable.Drawable r4 = r9.getDrawable()
            L_0x0047:
                if (r8 == 0) goto L_0x004c
                if (r2 != 0) goto L_0x004c
                r3 = r4
            L_0x004c:
                r6.setImageDrawable(r3)
            L_0x004f:
                r3 = 8
                if (r8 == 0) goto L_0x0055
                r4 = r1
                goto L_0x0056
            L_0x0055:
                r4 = r3
            L_0x0056:
                r6.setVisibility(r4)
                if (r9 == 0) goto L_0x007e
                if (r2 != 0) goto L_0x005f
                if (r8 != 0) goto L_0x0066
            L_0x005f:
                android.graphics.drawable.Drawable r6 = r9.getDrawable()
                if (r6 == 0) goto L_0x0066
                goto L_0x0067
            L_0x0066:
                r0 = r1
            L_0x0067:
                if (r0 == 0) goto L_0x006a
                r3 = r1
            L_0x006a:
                r9.setVisibility(r3)
                int[] r6 = com.android.systemui.statusbar.NotificationGroupingUtil.LeftIconApplicator.MARGIN_ADJUSTED_VIEWS
                int r8 = r6.length
            L_0x0070:
                if (r1 >= r8) goto L_0x007e
                r9 = r6[r1]
                android.view.View r9 = r7.findViewById(r9)
                r5.adjustMargins(r0, r9)
                int r1 = r1 + 1
                goto L_0x0070
            L_0x007e:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.android.systemui.statusbar.NotificationGroupingUtil.LeftIconApplicator.apply(android.view.View, android.view.View, boolean, boolean):void");
        }

        void adjustMargins(boolean z, View view) {
            if (view != null) {
                if (view instanceof ImageFloatingTextView) {
                    ((ImageFloatingTextView) view).setHasImage(z);
                    return;
                }
                Integer num = (Integer) view.getTag(z ? 16909521 : 16909520);
                if (num != null) {
                    int complexToDimensionPixelOffset = TypedValue.complexToDimensionPixelOffset(num.intValue(), view.getResources().getDisplayMetrics());
                    if (view instanceof NotificationHeaderView) {
                        ((NotificationHeaderView) view).setTopLineExtraMarginEnd(complexToDimensionPixelOffset);
                        return;
                    }
                    ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                    if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                        ((ViewGroup.MarginLayoutParams) layoutParams).setMarginEnd(complexToDimensionPixelOffset);
                        view.setLayoutParams(layoutParams);
                    }
                }
            }
        }
    }
}
