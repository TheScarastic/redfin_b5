package com.google.android.material.button;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import androidx.core.view.AccessibilityDelegateCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.lifecycle.runtime.R$id;
import com.android.systemui.shared.R;
import com.google.android.material.R$styleable;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.CornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.TreeMap;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MaterialButtonToggleGroup extends LinearLayout {
    public static final /* synthetic */ int $r8$clinit = 0;
    public int checkedId;
    public final CheckedStateTracker checkedStateTracker;
    public Integer[] childOrder;
    public final Comparator<MaterialButton> childOrderComparator;
    public final LinkedHashSet<OnButtonCheckedListener> onButtonCheckedListeners;
    public final List<CornerData> originalCornerData;
    public final PressedStateTracker pressedStateTracker;
    public boolean selectionRequired;
    public boolean singleSelection;
    public boolean skipCheckedStateTracker;

    /* loaded from: classes.dex */
    public class CheckedStateTracker implements MaterialButton.OnCheckedChangeListener {
        public CheckedStateTracker(AnonymousClass1 r2) {
        }

        @Override // com.google.android.material.button.MaterialButton.OnCheckedChangeListener
        public void onCheckedChanged(MaterialButton materialButton, boolean z) {
            MaterialButtonToggleGroup materialButtonToggleGroup = MaterialButtonToggleGroup.this;
            if (!materialButtonToggleGroup.skipCheckedStateTracker) {
                if (materialButtonToggleGroup.singleSelection) {
                    materialButtonToggleGroup.checkedId = z ? materialButton.getId() : -1;
                }
                if (MaterialButtonToggleGroup.this.updateCheckedStates(materialButton.getId(), z)) {
                    MaterialButtonToggleGroup.this.dispatchOnButtonChecked(materialButton.getId(), materialButton.isChecked());
                }
                MaterialButtonToggleGroup.this.invalidate();
            }
        }
    }

    /* loaded from: classes.dex */
    public static class CornerData {
        public static final CornerSize noCorner = new AbsoluteCornerSize(0.0f);
        public CornerSize bottomLeft;
        public CornerSize bottomRight;
        public CornerSize topLeft;
        public CornerSize topRight;

        public CornerData(CornerSize cornerSize, CornerSize cornerSize2, CornerSize cornerSize3, CornerSize cornerSize4) {
            this.topLeft = cornerSize;
            this.topRight = cornerSize3;
            this.bottomRight = cornerSize4;
            this.bottomLeft = cornerSize2;
        }
    }

    /* loaded from: classes.dex */
    public interface OnButtonCheckedListener {
        void onButtonChecked(MaterialButtonToggleGroup materialButtonToggleGroup, int i, boolean z);
    }

    /* loaded from: classes.dex */
    public class PressedStateTracker implements MaterialButton.OnPressedChangeListener {
        public PressedStateTracker(AnonymousClass1 r2) {
        }
    }

    public MaterialButtonToggleGroup(Context context) {
        this(context, null);
    }

    @Override // android.view.ViewGroup
    public void addView(View view, int i, ViewGroup.LayoutParams layoutParams) {
        if (!(view instanceof MaterialButton)) {
            Log.e("MaterialButtonToggleGroup", "Child views must be of type MaterialButton.");
            return;
        }
        super.addView(view, i, layoutParams);
        MaterialButton materialButton = (MaterialButton) view;
        if (materialButton.getId() == -1) {
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            materialButton.setId(View.generateViewId());
        }
        materialButton.setMaxLines(1);
        materialButton.setEllipsize(TextUtils.TruncateAt.END);
        if (materialButton.isUsingOriginalBackground()) {
            materialButton.materialButtonHelper.checkable = true;
        }
        materialButton.onCheckedChangeListeners.add(this.checkedStateTracker);
        materialButton.onPressedChangeListenerInternal = this.pressedStateTracker;
        if (materialButton.isUsingOriginalBackground()) {
            MaterialButtonHelper materialButtonHelper = materialButton.materialButtonHelper;
            materialButtonHelper.shouldDrawSurfaceColorStroke = true;
            MaterialShapeDrawable materialShapeDrawable = materialButtonHelper.getMaterialShapeDrawable();
            MaterialShapeDrawable surfaceColorStrokeDrawable = materialButtonHelper.getSurfaceColorStrokeDrawable();
            if (materialShapeDrawable != null) {
                materialShapeDrawable.setStroke((float) materialButtonHelper.strokeWidth, materialButtonHelper.strokeColor);
                if (surfaceColorStrokeDrawable != null) {
                    surfaceColorStrokeDrawable.setStroke((float) materialButtonHelper.strokeWidth, materialButtonHelper.shouldDrawSurfaceColorStroke ? R$id.getColor(materialButtonHelper.materialButton, R.attr.colorSurface) : 0);
                }
            }
        }
        if (materialButton.isChecked()) {
            updateCheckedStates(materialButton.getId(), true);
            int id = materialButton.getId();
            this.checkedId = id;
            dispatchOnButtonChecked(id, true);
        }
        if (materialButton.isUsingOriginalBackground()) {
            ShapeAppearanceModel shapeAppearanceModel = materialButton.materialButtonHelper.shapeAppearanceModel;
            this.originalCornerData.add(new CornerData(shapeAppearanceModel.topLeftCornerSize, shapeAppearanceModel.bottomLeftCornerSize, shapeAppearanceModel.topRightCornerSize, shapeAppearanceModel.bottomRightCornerSize));
            ViewCompat.setAccessibilityDelegate(materialButton, new AccessibilityDelegateCompat() { // from class: com.google.android.material.button.MaterialButtonToggleGroup.2
                @Override // androidx.core.view.AccessibilityDelegateCompat
                public void onInitializeAccessibilityNodeInfo(View view2, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
                    int i2;
                    this.mOriginalDelegate.onInitializeAccessibilityNodeInfo(view2, accessibilityNodeInfoCompat.mInfo);
                    MaterialButtonToggleGroup materialButtonToggleGroup = MaterialButtonToggleGroup.this;
                    int i3 = MaterialButtonToggleGroup.$r8$clinit;
                    Objects.requireNonNull(materialButtonToggleGroup);
                    if (view2 instanceof MaterialButton) {
                        i2 = 0;
                        for (int i4 = 0; i4 < materialButtonToggleGroup.getChildCount(); i4++) {
                            if (materialButtonToggleGroup.getChildAt(i4) == view2) {
                                break;
                            }
                            if ((materialButtonToggleGroup.getChildAt(i4) instanceof MaterialButton) && materialButtonToggleGroup.isChildVisible(i4)) {
                                i2++;
                            }
                        }
                    }
                    i2 = -1;
                    accessibilityNodeInfoCompat.setCollectionItemInfo(AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(0, 1, i2, 1, false, ((MaterialButton) view2).isChecked()));
                }
            });
            return;
        }
        throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
    }

    public final void adjustChildMarginsAndUpdateLayout() {
        LinearLayout.LayoutParams layoutParams;
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        if (firstVisibleChildIndex != -1) {
            for (int i = firstVisibleChildIndex + 1; i < getChildCount(); i++) {
                MaterialButton childButton = getChildButton(i);
                MaterialButton childButton2 = getChildButton(i - 1);
                int min = Math.min(childButton.isUsingOriginalBackground() ? childButton.materialButtonHelper.strokeWidth : 0, childButton2.isUsingOriginalBackground() ? childButton2.materialButtonHelper.strokeWidth : 0);
                ViewGroup.LayoutParams layoutParams2 = childButton.getLayoutParams();
                if (layoutParams2 instanceof LinearLayout.LayoutParams) {
                    layoutParams = (LinearLayout.LayoutParams) layoutParams2;
                } else {
                    layoutParams = new LinearLayout.LayoutParams(layoutParams2.width, layoutParams2.height);
                }
                if (getOrientation() == 0) {
                    layoutParams.setMarginEnd(0);
                    layoutParams.setMarginStart(-min);
                    layoutParams.topMargin = 0;
                } else {
                    layoutParams.bottomMargin = 0;
                    layoutParams.topMargin = -min;
                    layoutParams.setMarginStart(0);
                }
                childButton.setLayoutParams(layoutParams);
            }
            if (!(getChildCount() == 0 || firstVisibleChildIndex == -1)) {
                LinearLayout.LayoutParams layoutParams3 = (LinearLayout.LayoutParams) ((MaterialButton) getChildAt(firstVisibleChildIndex)).getLayoutParams();
                if (getOrientation() == 1) {
                    layoutParams3.topMargin = 0;
                    layoutParams3.bottomMargin = 0;
                    return;
                }
                layoutParams3.setMarginEnd(0);
                layoutParams3.setMarginStart(0);
                layoutParams3.leftMargin = 0;
                layoutParams3.rightMargin = 0;
            }
        }
    }

    @Override // android.view.View, android.view.ViewGroup
    public void dispatchDraw(Canvas canvas) {
        TreeMap treeMap = new TreeMap(this.childOrderComparator);
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            treeMap.put(getChildButton(i), Integer.valueOf(i));
        }
        this.childOrder = (Integer[]) treeMap.values().toArray(new Integer[0]);
        super.dispatchDraw(canvas);
    }

    public final void dispatchOnButtonChecked(int i, boolean z) {
        Iterator<OnButtonCheckedListener> it = this.onButtonCheckedListeners.iterator();
        while (it.hasNext()) {
            it.next().onButtonChecked(this, i, z);
        }
    }

    @Override // android.widget.LinearLayout, android.view.View, android.view.ViewGroup
    public CharSequence getAccessibilityClassName() {
        return MaterialButtonToggleGroup.class.getName();
    }

    public final MaterialButton getChildButton(int i) {
        return (MaterialButton) getChildAt(i);
    }

    @Override // android.view.ViewGroup
    public int getChildDrawingOrder(int i, int i2) {
        Integer[] numArr = this.childOrder;
        if (numArr != null && i2 < numArr.length) {
            return numArr[i2].intValue();
        }
        Log.w("MaterialButtonToggleGroup", "Child order wasn't updated");
        return i2;
    }

    public final int getFirstVisibleChildIndex() {
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            if (isChildVisible(i)) {
                return i;
            }
        }
        return -1;
    }

    public final boolean isChildVisible(int i) {
        return getChildAt(i).getVisibility() != 8;
    }

    @Override // android.view.View
    public void onFinishInflate() {
        MaterialButton materialButton;
        super.onFinishInflate();
        int i = this.checkedId;
        if (i != -1 && (materialButton = (MaterialButton) findViewById(i)) != null) {
            materialButton.setChecked(true);
        }
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        int i = 0;
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            if ((getChildAt(i2) instanceof MaterialButton) && isChildVisible(i2)) {
                i++;
            }
        }
        accessibilityNodeInfo.setCollectionInfo((AccessibilityNodeInfo.CollectionInfo) AccessibilityNodeInfoCompat.CollectionInfoCompat.obtain(1, i, false, this.singleSelection ? 1 : 2).mInfo);
    }

    @Override // android.widget.LinearLayout, android.view.View
    public void onMeasure(int i, int i2) {
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
        super.onMeasure(i, i2);
    }

    @Override // android.view.ViewGroup
    public void onViewRemoved(View view) {
        super.onViewRemoved(view);
        if (view instanceof MaterialButton) {
            MaterialButton materialButton = (MaterialButton) view;
            materialButton.onCheckedChangeListeners.remove(this.checkedStateTracker);
            materialButton.onPressedChangeListenerInternal = null;
        }
        int indexOfChild = indexOfChild(view);
        if (indexOfChild >= 0) {
            this.originalCornerData.remove(indexOfChild);
        }
        updateChildShapes();
        adjustChildMarginsAndUpdateLayout();
    }

    public final void setCheckedStateForView(int i, boolean z) {
        View findViewById = findViewById(i);
        if (findViewById instanceof MaterialButton) {
            this.skipCheckedStateTracker = true;
            ((MaterialButton) findViewById).setChecked(z);
            this.skipCheckedStateTracker = false;
        }
    }

    public final boolean updateCheckedStates(int i, boolean z) {
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < getChildCount(); i2++) {
            MaterialButton childButton = getChildButton(i2);
            if (childButton.isChecked()) {
                arrayList.add(Integer.valueOf(childButton.getId()));
            }
        }
        if (!this.selectionRequired || !arrayList.isEmpty()) {
            if (z && this.singleSelection) {
                arrayList.remove(Integer.valueOf(i));
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    int intValue = ((Integer) it.next()).intValue();
                    setCheckedStateForView(intValue, false);
                    dispatchOnButtonChecked(intValue, false);
                }
            }
            return true;
        }
        setCheckedStateForView(i, true);
        this.checkedId = i;
        return false;
    }

    public void updateChildShapes() {
        CornerData cornerData;
        int childCount = getChildCount();
        int firstVisibleChildIndex = getFirstVisibleChildIndex();
        int i = -1;
        int childCount2 = getChildCount() - 1;
        while (true) {
            if (childCount2 < 0) {
                break;
            } else if (isChildVisible(childCount2)) {
                i = childCount2;
                break;
            } else {
                childCount2--;
            }
        }
        for (int i2 = 0; i2 < childCount; i2++) {
            MaterialButton childButton = getChildButton(i2);
            if (childButton.getVisibility() != 8) {
                if (childButton.isUsingOriginalBackground()) {
                    ShapeAppearanceModel shapeAppearanceModel = childButton.materialButtonHelper.shapeAppearanceModel;
                    Objects.requireNonNull(shapeAppearanceModel);
                    ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder(shapeAppearanceModel);
                    CornerData cornerData2 = this.originalCornerData.get(i2);
                    if (firstVisibleChildIndex != i) {
                        boolean z = getOrientation() == 0;
                        if (i2 == firstVisibleChildIndex) {
                            if (!z) {
                                CornerSize cornerSize = cornerData2.topLeft;
                                CornerSize cornerSize2 = CornerData.noCorner;
                                cornerData = new CornerData(cornerSize, cornerSize2, cornerData2.topRight, cornerSize2);
                            } else if (ViewUtils.isLayoutRtl(this)) {
                                CornerSize cornerSize3 = CornerData.noCorner;
                                cornerData = new CornerData(cornerSize3, cornerSize3, cornerData2.topRight, cornerData2.bottomRight);
                            } else {
                                CornerSize cornerSize4 = cornerData2.topLeft;
                                CornerSize cornerSize5 = cornerData2.bottomLeft;
                                CornerSize cornerSize6 = CornerData.noCorner;
                                cornerData = new CornerData(cornerSize4, cornerSize5, cornerSize6, cornerSize6);
                            }
                        } else if (i2 != i) {
                            cornerData2 = null;
                        } else if (!z) {
                            CornerSize cornerSize7 = CornerData.noCorner;
                            cornerData = new CornerData(cornerSize7, cornerData2.bottomLeft, cornerSize7, cornerData2.bottomRight);
                        } else if (ViewUtils.isLayoutRtl(this)) {
                            CornerSize cornerSize8 = cornerData2.topLeft;
                            CornerSize cornerSize9 = cornerData2.bottomLeft;
                            CornerSize cornerSize10 = CornerData.noCorner;
                            cornerData = new CornerData(cornerSize8, cornerSize9, cornerSize10, cornerSize10);
                        } else {
                            CornerSize cornerSize11 = CornerData.noCorner;
                            cornerData = new CornerData(cornerSize11, cornerSize11, cornerData2.topRight, cornerData2.bottomRight);
                        }
                        cornerData2 = cornerData;
                    }
                    if (cornerData2 == null) {
                        builder.setAllCornerSizes(0.0f);
                    } else {
                        builder.topLeftCornerSize = cornerData2.topLeft;
                        builder.bottomLeftCornerSize = cornerData2.bottomLeft;
                        builder.topRightCornerSize = cornerData2.topRight;
                        builder.bottomRightCornerSize = cornerData2.bottomRight;
                    }
                    childButton.setShapeAppearanceModel(builder.build());
                } else {
                    throw new IllegalStateException("Attempted to get ShapeAppearanceModel from a MaterialButton which has an overwritten background.");
                }
            }
        }
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.materialButtonToggleGroupStyle);
    }

    public MaterialButtonToggleGroup(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2131886824), attributeSet, i);
        this.originalCornerData = new ArrayList();
        this.checkedStateTracker = new CheckedStateTracker(null);
        this.pressedStateTracker = new PressedStateTracker(null);
        this.onButtonCheckedListeners = new LinkedHashSet<>();
        this.childOrderComparator = new Comparator<MaterialButton>() { // from class: com.google.android.material.button.MaterialButtonToggleGroup.1
            /* JADX DEBUG: Method arguments types fixed to match base method, original types: [java.lang.Object, java.lang.Object] */
            @Override // java.util.Comparator
            public int compare(MaterialButton materialButton, MaterialButton materialButton2) {
                MaterialButton materialButton3 = materialButton;
                MaterialButton materialButton4 = materialButton2;
                int compareTo = Boolean.valueOf(materialButton3.isChecked()).compareTo(Boolean.valueOf(materialButton4.isChecked()));
                if (compareTo != 0) {
                    return compareTo;
                }
                int compareTo2 = Boolean.valueOf(materialButton3.isPressed()).compareTo(Boolean.valueOf(materialButton4.isPressed()));
                return compareTo2 != 0 ? compareTo2 : Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild(materialButton3)).compareTo(Integer.valueOf(MaterialButtonToggleGroup.this.indexOfChild(materialButton4)));
            }
        };
        this.skipCheckedStateTracker = false;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(getContext(), attributeSet, R$styleable.MaterialButtonToggleGroup, i, 2131886824, new int[0]);
        boolean z = obtainStyledAttributes.getBoolean(2, false);
        if (this.singleSelection != z) {
            this.singleSelection = z;
            this.skipCheckedStateTracker = true;
            for (int i2 = 0; i2 < getChildCount(); i2++) {
                MaterialButton childButton = getChildButton(i2);
                childButton.setChecked(false);
                dispatchOnButtonChecked(childButton.getId(), false);
            }
            this.skipCheckedStateTracker = false;
            this.checkedId = -1;
            dispatchOnButtonChecked(-1, true);
        }
        this.checkedId = obtainStyledAttributes.getResourceId(0, -1);
        this.selectionRequired = obtainStyledAttributes.getBoolean(1, false);
        setChildrenDrawingOrderEnabled(true);
        obtainStyledAttributes.recycle();
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        setImportantForAccessibility(1);
    }
}
