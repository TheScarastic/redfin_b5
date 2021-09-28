package com.android.systemui.statusbar.notification.stack;

import android.content.Context;
import android.content.res.Resources;
import android.util.MathUtils;
import android.view.View;
import android.view.ViewGroup;
import com.android.systemui.R$bool;
import com.android.systemui.R$dimen;
import com.android.systemui.animation.Interpolators;
import com.android.systemui.statusbar.NotificationShelf;
import com.android.systemui.statusbar.notification.row.ActivatableNotificationView;
import com.android.systemui.statusbar.notification.row.ExpandableNotificationRow;
import com.android.systemui.statusbar.notification.row.ExpandableView;
import com.android.systemui.statusbar.notification.row.FooterView;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
/* loaded from: classes.dex */
public class StackScrollAlgorithm {
    private boolean mClipNotificationScrollToTop;
    private int mCollapsedSize;
    private int mGapHeight;
    private float mHeadsUpInset;
    private final ViewGroup mHostView;
    private boolean mIsExpanded;
    private float mNotificationScrimPadding;
    private int mPaddingBetweenElements;
    private int mPinnedZTranslationExtra;
    private int mStatusBarHeight;
    private StackScrollAlgorithmState mTempAlgorithmState = new StackScrollAlgorithmState();

    /* loaded from: classes.dex */
    public interface BypassController {
        boolean isBypassEnabled();
    }

    /* loaded from: classes.dex */
    public interface SectionProvider {
        boolean beginsSection(View view, View view2);
    }

    public StackScrollAlgorithm(Context context, ViewGroup viewGroup) {
        this.mHostView = viewGroup;
        initView(context);
    }

    public void initView(Context context) {
        initConstants(context);
    }

    private void initConstants(Context context) {
        Resources resources = context.getResources();
        this.mPaddingBetweenElements = resources.getDimensionPixelSize(R$dimen.notification_divider_height);
        this.mCollapsedSize = resources.getDimensionPixelSize(R$dimen.notification_min_height);
        this.mStatusBarHeight = resources.getDimensionPixelSize(R$dimen.status_bar_height);
        this.mClipNotificationScrollToTop = resources.getBoolean(R$bool.config_clipNotificationScrollToTop);
        this.mHeadsUpInset = (float) (this.mStatusBarHeight + resources.getDimensionPixelSize(R$dimen.heads_up_status_bar_padding));
        this.mPinnedZTranslationExtra = resources.getDimensionPixelSize(R$dimen.heads_up_pinned_elevation);
        this.mGapHeight = resources.getDimensionPixelSize(R$dimen.notification_section_divider_height);
        this.mNotificationScrimPadding = (float) resources.getDimensionPixelSize(R$dimen.notification_side_paddings);
    }

    public void resetViewStates(AmbientState ambientState, int i) {
        StackScrollAlgorithmState stackScrollAlgorithmState = this.mTempAlgorithmState;
        resetChildViewStates();
        initAlgorithmState(stackScrollAlgorithmState, ambientState);
        updatePositionsForState(stackScrollAlgorithmState, ambientState);
        updateZValuesForState(stackScrollAlgorithmState, ambientState);
        updateHeadsUpStates(stackScrollAlgorithmState, ambientState);
        updatePulsingStates(stackScrollAlgorithmState, ambientState);
        updateDimmedActivatedHideSensitive(ambientState, stackScrollAlgorithmState);
        updateClipping(stackScrollAlgorithmState, ambientState);
        updateSpeedBumpState(stackScrollAlgorithmState, i);
        updateShelfState(stackScrollAlgorithmState, ambientState);
        getNotificationChildrenStates(stackScrollAlgorithmState, ambientState);
    }

    private void resetChildViewStates() {
        int childCount = this.mHostView.getChildCount();
        for (int i = 0; i < childCount; i++) {
            ((ExpandableView) this.mHostView.getChildAt(i)).resetViewState();
        }
    }

    private void getNotificationChildrenStates(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ((ExpandableNotificationRow) expandableView).updateChildrenStates(ambientState);
            }
        }
    }

    private void updateSpeedBumpState(StackScrollAlgorithmState stackScrollAlgorithmState, int i) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        int i2 = 0;
        while (i2 < size) {
            stackScrollAlgorithmState.visibleChildren.get(i2).getViewState().belowSpeedBump = i2 >= i;
            i2++;
        }
    }

    private void updateShelfState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        NotificationShelf shelf = ambientState.getShelf();
        if (shelf != null) {
            shelf.updateState(stackScrollAlgorithmState, ambientState);
            if (!shelf.getViewState().hidden) {
                float f = shelf.getViewState().yTranslation;
                Iterator<ExpandableView> it = stackScrollAlgorithmState.visibleChildren.iterator();
                while (it.hasNext()) {
                    ExpandableView next = it.next();
                    if (next.getViewState().yTranslation >= f) {
                        next.getViewState().alpha = 0.0f;
                    }
                }
            }
        }
    }

    private void updateClipping(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        float f;
        float f2 = 0.0f;
        if (ambientState.isOnKeyguard()) {
            f = 0.0f;
        } else {
            f = ambientState.getStackY() - ((float) ambientState.getScrollY());
        }
        int size = stackScrollAlgorithmState.visibleChildren.size();
        boolean z = true;
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            ExpandableViewState viewState = expandableView.getViewState();
            if (!expandableView.mustStayOnScreen() || viewState.headsUpIsVisible) {
                f2 = Math.max(f, f2);
            }
            float f3 = viewState.yTranslation;
            float f4 = ((float) viewState.height) + f3;
            boolean z2 = (expandableView instanceof ExpandableNotificationRow) && expandableView.isPinned();
            if (!this.mClipNotificationScrollToTop || ((viewState.inShelf && (!z2 || z)) || f3 >= f2 || ambientState.isShadeOpening())) {
                viewState.clipTopAmount = 0;
            } else {
                viewState.clipTopAmount = (int) (f2 - f3);
            }
            if (z2) {
                z = false;
            }
            if (!expandableView.isTransparent()) {
                if (!z2) {
                    f3 = f4;
                }
                f2 = Math.max(f2, f3);
            }
        }
    }

    private void updateDimmedActivatedHideSensitive(AmbientState ambientState, StackScrollAlgorithmState stackScrollAlgorithmState) {
        boolean isDimmed = ambientState.isDimmed();
        boolean isHideSensitive = ambientState.isHideSensitive();
        ActivatableNotificationView activatedChild = ambientState.getActivatedChild();
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            ExpandableViewState viewState = expandableView.getViewState();
            viewState.dimmed = isDimmed;
            viewState.hideSensitive = isHideSensitive;
            boolean z = activatedChild == expandableView;
            if (isDimmed && z) {
                viewState.zTranslation += ((float) ambientState.getZDistanceBetweenElements()) * 2.0f;
            }
        }
    }

    private void initAlgorithmState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int scrollY = ambientState.getScrollY();
        stackScrollAlgorithmState.scrollY = scrollY;
        stackScrollAlgorithmState.mCurrentYPosition = -scrollY;
        stackScrollAlgorithmState.mCurrentExpandedYPosition = -stackScrollAlgorithmState.scrollY;
        int childCount = this.mHostView.getChildCount();
        stackScrollAlgorithmState.visibleChildren.clear();
        stackScrollAlgorithmState.visibleChildren.ensureCapacity(childCount);
        int i = 0;
        for (int i2 = 0; i2 < childCount; i2++) {
            ExpandableView expandableView = (ExpandableView) this.mHostView.getChildAt(i2);
            if (!(expandableView.getVisibility() == 8 || expandableView == ambientState.getShelf())) {
                i = updateNotGoneIndex(stackScrollAlgorithmState, i, expandableView);
                if (expandableView instanceof ExpandableNotificationRow) {
                    ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                    List<ExpandableNotificationRow> attachedChildren = expandableNotificationRow.getAttachedChildren();
                    if (expandableNotificationRow.isSummaryWithChildren() && attachedChildren != null) {
                        for (ExpandableNotificationRow expandableNotificationRow2 : attachedChildren) {
                            if (expandableNotificationRow2.getVisibility() != 8) {
                                expandableNotificationRow2.getViewState().notGoneIndex = i;
                                i++;
                            }
                        }
                    }
                }
            }
        }
        float f = (float) (-ambientState.getScrollY());
        if (!ambientState.isOnKeyguard() || (ambientState.isBypassEnabled() && ambientState.isPulseExpanding())) {
            f += this.mNotificationScrimPadding;
        }
        stackScrollAlgorithmState.firstViewInShelf = null;
        for (int i3 = 0; i3 < stackScrollAlgorithmState.visibleChildren.size(); i3++) {
            ExpandableView expandableView2 = stackScrollAlgorithmState.visibleChildren.get(i3);
            if (childNeedsGapHeight(ambientState.getSectionProvider(), i3, expandableView2, getPreviousView(i3, stackScrollAlgorithmState))) {
                f += (float) this.mGapHeight;
            }
            if (ambientState.getShelf() != null && f >= ambientState.getStackEndHeight() - ((float) ambientState.getShelf().getIntrinsicHeight()) && !(expandableView2 instanceof FooterView) && stackScrollAlgorithmState.firstViewInShelf == null) {
                stackScrollAlgorithmState.firstViewInShelf = expandableView2;
            }
            f = f + ((float) getMaxAllowedChildHeight(expandableView2)) + ((float) this.mPaddingBetweenElements);
        }
    }

    private int updateNotGoneIndex(StackScrollAlgorithmState stackScrollAlgorithmState, int i, ExpandableView expandableView) {
        expandableView.getViewState().notGoneIndex = i;
        stackScrollAlgorithmState.visibleChildren.add(expandableView);
        return i + 1;
    }

    private ExpandableView getPreviousView(int i, StackScrollAlgorithmState stackScrollAlgorithmState) {
        if (i > 0) {
            return stackScrollAlgorithmState.visibleChildren.get(i - 1);
        }
        return null;
    }

    private void updatePositionsForState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        if (!ambientState.isOnKeyguard() || (ambientState.isBypassEnabled() && ambientState.isPulseExpanding())) {
            StackScrollAlgorithmState.access$016(stackScrollAlgorithmState, this.mNotificationScrimPadding);
            StackScrollAlgorithmState.access$116(stackScrollAlgorithmState, this.mNotificationScrimPadding);
        }
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            updateChild(i, stackScrollAlgorithmState, ambientState);
        }
    }

    private void setLocation(ExpandableViewState expandableViewState, float f, int i) {
        expandableViewState.location = 4;
        if (f <= 0.0f) {
            expandableViewState.location = 2;
        }
    }

    private float getExpansionFractionWithoutShelf(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        float f = 0.0f;
        float intrinsicHeight = ambientState.getShelf() != null && stackScrollAlgorithmState.firstViewInShelf != null ? (float) ambientState.getShelf().getIntrinsicHeight() : 0.0f;
        if (!ambientState.isOnKeyguard() || (ambientState.isBypassEnabled() && ambientState.isPulseExpanding())) {
            f = this.mNotificationScrimPadding;
        }
        return ((ambientState.getStackHeight() - intrinsicHeight) - f) / ((ambientState.getStackEndHeight() - intrinsicHeight) - f);
    }

    protected void updateChild(int i, StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int i2;
        ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
        ExpandableViewState viewState = expandableView.getViewState();
        boolean z = false;
        viewState.location = 0;
        if (!(ambientState.isShadeExpanded() && expandableView == ambientState.getTrackedHeadsUpRow())) {
            if (ambientState.isOnKeyguard()) {
                viewState.alpha = 1.0f - ambientState.getHideAmount();
            } else if (ambientState.isExpansionChanging()) {
                viewState.alpha = Interpolators.getNotificationScrimAlpha(ambientState.getExpansionFraction(), true);
            }
        }
        if (ambientState.isShadeExpanded() && expandableView.mustStayOnScreen()) {
            float f = viewState.yTranslation;
            if (f >= 0.0f) {
                viewState.headsUpIsVisible = (f + ((float) viewState.height)) + ambientState.getStackY() < ambientState.getMaxHeadsUpTranslation();
            }
        }
        float expansionFractionWithoutShelf = getExpansionFractionWithoutShelf(stackScrollAlgorithmState, ambientState);
        if (childNeedsGapHeight(ambientState.getSectionProvider(), i, expandableView, getPreviousView(i, stackScrollAlgorithmState))) {
            StackScrollAlgorithmState.access$016(stackScrollAlgorithmState, ((float) this.mGapHeight) * expansionFractionWithoutShelf);
            StackScrollAlgorithmState.access$112(stackScrollAlgorithmState, this.mGapHeight);
        }
        viewState.yTranslation = (float) stackScrollAlgorithmState.mCurrentYPosition;
        if (expandableView instanceof FooterView) {
            boolean z2 = !ambientState.isShadeExpanded();
            boolean z3 = stackScrollAlgorithmState.firstViewInShelf != null;
            if (z2) {
                viewState.hidden = true;
            } else {
                boolean z4 = ((float) (stackScrollAlgorithmState.mCurrentExpandedYPosition + expandableView.getIntrinsicHeight())) > ambientState.getStackEndHeight();
                FooterView.FooterViewState footerViewState = (FooterView.FooterViewState) viewState;
                if (z3 || z4) {
                    z = true;
                }
                footerViewState.hideContent = z;
            }
        } else {
            if (expandableView != ambientState.getTrackedHeadsUpRow()) {
                if (ambientState.isExpansionChanging()) {
                    viewState.hidden = false;
                    ExpandableView expandableView2 = stackScrollAlgorithmState.firstViewInShelf;
                    if (expandableView2 != null && i >= stackScrollAlgorithmState.visibleChildren.indexOf(expandableView2)) {
                        z = true;
                    }
                    viewState.inShelf = z;
                } else if (ambientState.getShelf() != null) {
                    boolean z5 = ambientState.isBypassEnabled() && ambientState.isOnKeyguard() && !ambientState.isPulseExpanding();
                    if (!ambientState.isShadeExpanded() || ambientState.isDozing() || z5) {
                        i2 = ambientState.getInnerHeight();
                    } else {
                        i2 = (int) ambientState.getStackHeight();
                    }
                    float intrinsicHeight = (float) (i2 - ambientState.getShelf().getIntrinsicHeight());
                    float min = Math.min(viewState.yTranslation, intrinsicHeight);
                    viewState.yTranslation = min;
                    if (min >= intrinsicHeight) {
                        viewState.hidden = !expandableView.isExpandAnimationRunning() && !expandableView.hasExpandingChild();
                        viewState.inShelf = true;
                        viewState.headsUpIsVisible = false;
                    }
                }
            }
            viewState.height = (int) (((float) getMaxAllowedChildHeight(expandableView)) * expansionFractionWithoutShelf);
        }
        StackScrollAlgorithmState.access$016(stackScrollAlgorithmState, ((float) viewState.height) + (expansionFractionWithoutShelf * ((float) this.mPaddingBetweenElements)));
        StackScrollAlgorithmState.access$112(stackScrollAlgorithmState, expandableView.getIntrinsicHeight() + this.mPaddingBetweenElements);
        setLocation(expandableView.getViewState(), (float) stackScrollAlgorithmState.mCurrentYPosition, i);
        viewState.yTranslation += ambientState.getStackY();
    }

    public float getGapHeightForChild(SectionProvider sectionProvider, int i, View view, View view2) {
        if (childNeedsGapHeight(sectionProvider, i, view, view2)) {
            return (float) this.mGapHeight;
        }
        return 0.0f;
    }

    private boolean childNeedsGapHeight(SectionProvider sectionProvider, int i, View view, View view2) {
        return sectionProvider.beginsSection(view, view2) && i > 0 && !(view2 instanceof SectionHeaderView) && !(view instanceof FooterView);
    }

    private void updatePulsingStates(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow = (ExpandableNotificationRow) expandableView;
                if (expandableNotificationRow.showingPulsing() && (i != 0 || !ambientState.isPulseExpanding())) {
                    expandableNotificationRow.getViewState().hidden = false;
                }
            }
        }
    }

    private void updateHeadsUpStates(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        ExpandableViewState expandableViewState;
        ExpandableViewState viewState;
        int size = stackScrollAlgorithmState.visibleChildren.size();
        ExpandableNotificationRow trackedHeadsUpRow = ambientState.getTrackedHeadsUpRow();
        if (!(trackedHeadsUpRow == null || (viewState = trackedHeadsUpRow.getViewState()) == null)) {
            viewState.yTranslation = MathUtils.lerp(this.mHeadsUpInset, viewState.yTranslation - ambientState.getStackTranslation(), ambientState.getAppearFraction());
        }
        ExpandableNotificationRow expandableNotificationRow = null;
        for (int i = 0; i < size; i++) {
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            if (expandableView instanceof ExpandableNotificationRow) {
                ExpandableNotificationRow expandableNotificationRow2 = (ExpandableNotificationRow) expandableView;
                if (expandableNotificationRow2.isHeadsUp() || expandableNotificationRow2.isHeadsUpAnimatingAway()) {
                    ExpandableViewState viewState2 = expandableNotificationRow2.getViewState();
                    boolean z = true;
                    if (expandableNotificationRow == null && expandableNotificationRow2.mustStayOnScreen() && !viewState2.headsUpIsVisible) {
                        viewState2.location = 1;
                        expandableNotificationRow = expandableNotificationRow2;
                    }
                    if (expandableNotificationRow != expandableNotificationRow2) {
                        z = false;
                    }
                    float f = viewState2.yTranslation + ((float) viewState2.height);
                    if (this.mIsExpanded && expandableNotificationRow2.mustStayOnScreen() && !viewState2.headsUpIsVisible && !expandableNotificationRow2.showingPulsing()) {
                        clampHunToTop(ambientState, expandableNotificationRow2, viewState2);
                        if (z && expandableNotificationRow2.isAboveShelf()) {
                            clampHunToMaxTranslation(ambientState, expandableNotificationRow2, viewState2);
                            viewState2.hidden = false;
                        }
                    }
                    if (expandableNotificationRow2.isPinned()) {
                        viewState2.yTranslation = Math.max(viewState2.yTranslation, this.mHeadsUpInset);
                        viewState2.height = Math.max(expandableNotificationRow2.getIntrinsicHeight(), viewState2.height);
                        viewState2.hidden = false;
                        if (expandableNotificationRow == null) {
                            expandableViewState = null;
                        } else {
                            expandableViewState = expandableNotificationRow.getViewState();
                        }
                        if (expandableViewState != null && !z && (!this.mIsExpanded || f > expandableViewState.yTranslation + ((float) expandableViewState.height))) {
                            int intrinsicHeight = expandableNotificationRow2.getIntrinsicHeight();
                            viewState2.height = intrinsicHeight;
                            viewState2.yTranslation = Math.min((expandableViewState.yTranslation + ((float) expandableViewState.height)) - ((float) intrinsicHeight), viewState2.yTranslation);
                        }
                        if (!this.mIsExpanded && z && ambientState.getScrollY() > 0) {
                            viewState2.yTranslation -= (float) ambientState.getScrollY();
                        }
                    }
                    if (expandableNotificationRow2.isHeadsUpAnimatingAway()) {
                        viewState2.yTranslation = Math.max(viewState2.yTranslation, this.mHeadsUpInset);
                        viewState2.hidden = false;
                    }
                }
            }
        }
    }

    private void clampHunToTop(AmbientState ambientState, ExpandableNotificationRow expandableNotificationRow, ExpandableViewState expandableViewState) {
        float max = Math.max(ambientState.getTopPadding() + ambientState.getStackTranslation(), expandableViewState.yTranslation);
        expandableViewState.height = (int) Math.max(((float) expandableViewState.height) - (max - expandableViewState.yTranslation), (float) expandableNotificationRow.getCollapsedHeight());
        expandableViewState.yTranslation = max;
    }

    private void clampHunToMaxTranslation(AmbientState ambientState, ExpandableNotificationRow expandableNotificationRow, ExpandableViewState expandableViewState) {
        float min = Math.min(ambientState.getMaxHeadsUpTranslation(), ((float) ambientState.getInnerHeight()) + ambientState.getTopPadding() + ambientState.getStackTranslation());
        float min2 = Math.min(expandableViewState.yTranslation, min - ((float) expandableNotificationRow.getCollapsedHeight()));
        expandableViewState.height = (int) Math.min((float) expandableViewState.height, min - min2);
        expandableViewState.yTranslation = min2;
    }

    protected int getMaxAllowedChildHeight(View view) {
        if (view instanceof ExpandableView) {
            return ((ExpandableView) view).getIntrinsicHeight();
        }
        return view == null ? this.mCollapsedSize : view.getHeight();
    }

    private void updateZValuesForState(StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState) {
        int size = stackScrollAlgorithmState.visibleChildren.size();
        int i = 0;
        while (true) {
            if (i >= size) {
                i = -1;
                break;
            }
            ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
            if ((expandableView instanceof ActivatableNotificationView) && (expandableView.isAboveShelf() || expandableView.showingPulsing())) {
                break;
            }
            i++;
        }
        int i2 = size - 1;
        float f = 0.0f;
        while (i2 >= 0) {
            f = updateChildZValue(i2, f, stackScrollAlgorithmState, ambientState, i2 == i);
            i2--;
        }
    }

    protected float updateChildZValue(int i, float f, StackScrollAlgorithmState stackScrollAlgorithmState, AmbientState ambientState, boolean z) {
        int i2;
        ExpandableView expandableView = stackScrollAlgorithmState.visibleChildren.get(i);
        ExpandableViewState viewState = expandableView.getViewState();
        int zDistanceBetweenElements = ambientState.getZDistanceBetweenElements();
        float baseZHeight = (float) ambientState.getBaseZHeight();
        if (expandableView.mustStayOnScreen() && !viewState.headsUpIsVisible && !ambientState.isDozingAndNotPulsing(expandableView) && viewState.yTranslation < ambientState.getTopPadding() + ambientState.getStackTranslation()) {
            if (f != 0.0f) {
                f += 1.0f;
            } else {
                f += Math.min(1.0f, ((ambientState.getTopPadding() + ambientState.getStackTranslation()) - viewState.yTranslation) / ((float) viewState.height));
            }
            viewState.zTranslation = baseZHeight + (((float) zDistanceBetweenElements) * f);
        } else if (z) {
            if (ambientState.getShelf() == null) {
                i2 = 0;
            } else {
                i2 = ambientState.getShelf().getIntrinsicHeight();
            }
            float innerHeight = ((float) (ambientState.getInnerHeight() - i2)) + ambientState.getTopPadding() + ambientState.getStackTranslation();
            float intrinsicHeight = viewState.yTranslation + ((float) expandableView.getIntrinsicHeight()) + ((float) this.mPaddingBetweenElements);
            if (innerHeight > intrinsicHeight) {
                viewState.zTranslation = baseZHeight;
            } else {
                viewState.zTranslation = baseZHeight + (Math.min((intrinsicHeight - innerHeight) / ((float) i2), 1.0f) * ((float) zDistanceBetweenElements));
            }
        } else {
            viewState.zTranslation = baseZHeight;
        }
        viewState.zTranslation += (1.0f - expandableView.getHeaderVisibleAmount()) * ((float) this.mPinnedZTranslationExtra);
        return f;
    }

    public void setIsExpanded(boolean z) {
        this.mIsExpanded = z;
    }

    /* loaded from: classes.dex */
    public static class StackScrollAlgorithmState {
        public ExpandableView firstViewInShelf;
        private int mCurrentExpandedYPosition;
        private int mCurrentYPosition;
        public int scrollY;
        public final ArrayList<ExpandableView> visibleChildren = new ArrayList<>();

        static /* synthetic */ int access$016(StackScrollAlgorithmState stackScrollAlgorithmState, float f) {
            int i = (int) (((float) stackScrollAlgorithmState.mCurrentYPosition) + f);
            stackScrollAlgorithmState.mCurrentYPosition = i;
            return i;
        }

        static /* synthetic */ int access$112(StackScrollAlgorithmState stackScrollAlgorithmState, int i) {
            int i2 = stackScrollAlgorithmState.mCurrentExpandedYPosition + i;
            stackScrollAlgorithmState.mCurrentExpandedYPosition = i2;
            return i2;
        }

        static /* synthetic */ int access$116(StackScrollAlgorithmState stackScrollAlgorithmState, float f) {
            int i = (int) (((float) stackScrollAlgorithmState.mCurrentExpandedYPosition) + f);
            stackScrollAlgorithmState.mCurrentExpandedYPosition = i;
            return i;
        }
    }
}
