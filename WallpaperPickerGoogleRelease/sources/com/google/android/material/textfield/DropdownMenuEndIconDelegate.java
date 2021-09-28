package com.google.android.material.textfield;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.RippleDrawable;
import android.graphics.drawable.StateListDrawable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityManager;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.lifecycle.runtime.R$id;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.R;
import com.google.android.material.animation.AnimationUtils;
import com.google.android.material.elevation.ElevationOverlayProvider;
import com.google.android.material.internal.CheckableImageButton;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.shape.AbsoluteCornerSize;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.textfield.TextInputLayout;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class DropdownMenuEndIconDelegate extends EndIconDelegate {
    public AccessibilityManager accessibilityManager;
    public ValueAnimator fadeInAnim;
    public ValueAnimator fadeOutAnim;
    public StateListDrawable filledPopupBackground;
    public MaterialShapeDrawable outlinedPopupBackground;
    public final TextWatcher exposedDropdownEndIconTextWatcher = new TextWatcherAdapter() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.1
        @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
        public void afterTextChanged(Editable editable) {
            final AutoCompleteTextView access$000 = DropdownMenuEndIconDelegate.access$000(DropdownMenuEndIconDelegate.this.textInputLayout.editText);
            if (DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled() && DropdownMenuEndIconDelegate.access$200(access$000) && !DropdownMenuEndIconDelegate.this.endIconView.hasFocus()) {
                access$000.dismissDropDown();
            }
            access$000.post(new Runnable() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.1.1
                @Override // java.lang.Runnable
                public void run() {
                    boolean isPopupShowing = access$000.isPopupShowing();
                    DropdownMenuEndIconDelegate.access$300(DropdownMenuEndIconDelegate.this, isPopupShowing);
                    DropdownMenuEndIconDelegate.this.dropdownPopupDirty = isPopupShowing;
                }
            });
        }
    };
    public final View.OnFocusChangeListener onFocusChangeListener = new View.OnFocusChangeListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.2
        @Override // android.view.View.OnFocusChangeListener
        public void onFocusChange(View view, boolean z) {
            DropdownMenuEndIconDelegate.this.textInputLayout.endIconView.setActivated(z);
            if (!z) {
                DropdownMenuEndIconDelegate.access$300(DropdownMenuEndIconDelegate.this, false);
                DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
            }
        }
    };
    public final TextInputLayout.AccessibilityDelegate accessibilityDelegate = new TextInputLayout.AccessibilityDelegate(this.textInputLayout) { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.3
        @Override // com.google.android.material.textfield.TextInputLayout.AccessibilityDelegate, androidx.core.view.AccessibilityDelegateCompat
        public void onInitializeAccessibilityNodeInfo(View view, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            super.onInitializeAccessibilityNodeInfo(view, accessibilityNodeInfoCompat);
            if (!DropdownMenuEndIconDelegate.access$200(DropdownMenuEndIconDelegate.this.textInputLayout.editText)) {
                accessibilityNodeInfoCompat.mInfo.setClassName(Spinner.class.getName());
            }
            if (accessibilityNodeInfoCompat.mInfo.isShowingHintText()) {
                accessibilityNodeInfoCompat.mInfo.setHintText(null);
            }
        }

        @Override // androidx.core.view.AccessibilityDelegateCompat
        public void onPopulateAccessibilityEvent(View view, AccessibilityEvent accessibilityEvent) {
            this.mOriginalDelegate.onPopulateAccessibilityEvent(view, accessibilityEvent);
            AutoCompleteTextView access$000 = DropdownMenuEndIconDelegate.access$000(DropdownMenuEndIconDelegate.this.textInputLayout.editText);
            if (accessibilityEvent.getEventType() == 1 && DropdownMenuEndIconDelegate.this.accessibilityManager.isTouchExplorationEnabled() && !DropdownMenuEndIconDelegate.access$200(DropdownMenuEndIconDelegate.this.textInputLayout.editText)) {
                DropdownMenuEndIconDelegate.access$500(DropdownMenuEndIconDelegate.this, access$000);
            }
        }
    };
    public final TextInputLayout.OnEditTextAttachedListener dropdownMenuOnEditTextAttachedListener = new TextInputLayout.OnEditTextAttachedListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.4
        @Override // com.google.android.material.textfield.TextInputLayout.OnEditTextAttachedListener
        public void onEditTextAttached(TextInputLayout textInputLayout) {
            AutoCompleteTextView access$000 = DropdownMenuEndIconDelegate.access$000(textInputLayout.editText);
            DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate = DropdownMenuEndIconDelegate.this;
            int i = dropdownMenuEndIconDelegate.textInputLayout.boxBackgroundMode;
            if (i == 2) {
                access$000.setDropDownBackgroundDrawable(dropdownMenuEndIconDelegate.outlinedPopupBackground);
            } else if (i == 1) {
                access$000.setDropDownBackgroundDrawable(dropdownMenuEndIconDelegate.filledPopupBackground);
            }
            DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate2 = DropdownMenuEndIconDelegate.this;
            Objects.requireNonNull(dropdownMenuEndIconDelegate2);
            boolean z = false;
            if (!(access$000.getKeyListener() != null)) {
                TextInputLayout textInputLayout2 = dropdownMenuEndIconDelegate2.textInputLayout;
                int i2 = textInputLayout2.boxBackgroundMode;
                if (i2 == 1 || i2 == 2) {
                    MaterialShapeDrawable materialShapeDrawable = textInputLayout2.boxBackground;
                    int color = R$id.getColor(access$000, R.attr.colorControlHighlight);
                    int[][] iArr = {new int[]{16842919}, new int[0]};
                    if (i2 == 2) {
                        int color2 = R$id.getColor(access$000, R.attr.colorSurface);
                        MaterialShapeDrawable materialShapeDrawable2 = new MaterialShapeDrawable(materialShapeDrawable.drawableState.shapeAppearanceModel);
                        int layer = R$id.layer(color, color2, 0.1f);
                        materialShapeDrawable2.setFillColor(new ColorStateList(iArr, new int[]{layer, 0}));
                        materialShapeDrawable2.setTint(color2);
                        ColorStateList colorStateList = new ColorStateList(iArr, new int[]{layer, color2});
                        MaterialShapeDrawable materialShapeDrawable3 = new MaterialShapeDrawable(materialShapeDrawable.drawableState.shapeAppearanceModel);
                        materialShapeDrawable3.setTint(-1);
                        LayerDrawable layerDrawable = new LayerDrawable(new Drawable[]{new RippleDrawable(colorStateList, materialShapeDrawable2, materialShapeDrawable3), materialShapeDrawable});
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
                        access$000.setBackground(layerDrawable);
                    } else if (i2 == 1) {
                        int i3 = dropdownMenuEndIconDelegate2.textInputLayout.boxBackgroundColor;
                        RippleDrawable rippleDrawable = new RippleDrawable(new ColorStateList(iArr, new int[]{R$id.layer(color, i3, 0.1f), i3}), materialShapeDrawable, materialShapeDrawable);
                        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap2 = ViewCompat.sViewPropertyAnimatorMap;
                        access$000.setBackground(rippleDrawable);
                    }
                } else {
                    throw new IllegalStateException();
                }
            }
            DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate3 = DropdownMenuEndIconDelegate.this;
            Objects.requireNonNull(dropdownMenuEndIconDelegate3);
            access$000.setOnTouchListener(new View.OnTouchListener(access$000) { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.7
                public final /* synthetic */ AutoCompleteTextView val$editText;

                {
                    this.val$editText = r2;
                }

                @Override // android.view.View.OnTouchListener
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (motionEvent.getAction() == 1) {
                        if (DropdownMenuEndIconDelegate.this.isDropdownPopupActive()) {
                            DropdownMenuEndIconDelegate.this.dropdownPopupDirty = false;
                        }
                        DropdownMenuEndIconDelegate.access$500(DropdownMenuEndIconDelegate.this, this.val$editText);
                    }
                    return false;
                }
            });
            access$000.setOnFocusChangeListener(dropdownMenuEndIconDelegate3.onFocusChangeListener);
            access$000.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.8
                @Override // android.widget.AutoCompleteTextView.OnDismissListener
                public void onDismiss() {
                    DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate4 = DropdownMenuEndIconDelegate.this;
                    dropdownMenuEndIconDelegate4.dropdownPopupDirty = true;
                    dropdownMenuEndIconDelegate4.dropdownPopupActivatedAt = System.currentTimeMillis();
                    DropdownMenuEndIconDelegate.access$300(DropdownMenuEndIconDelegate.this, false);
                }
            });
            access$000.setThreshold(0);
            access$000.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
            access$000.addTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
            textInputLayout.setEndIconCheckable(true);
            textInputLayout.setErrorIconDrawable(null);
            if (access$000.getKeyListener() != null) {
                z = true;
            }
            if (!z) {
                CheckableImageButton checkableImageButton = DropdownMenuEndIconDelegate.this.endIconView;
                WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap3 = ViewCompat.sViewPropertyAnimatorMap;
                checkableImageButton.setImportantForAccessibility(2);
            }
            TextInputLayout.AccessibilityDelegate accessibilityDelegate = DropdownMenuEndIconDelegate.this.accessibilityDelegate;
            EditText editText = textInputLayout.editText;
            if (editText != null) {
                ViewCompat.setAccessibilityDelegate(editText, accessibilityDelegate);
            }
            textInputLayout.setEndIconVisible(true);
        }
    };
    @SuppressLint({"ClickableViewAccessibility"})
    public final TextInputLayout.OnEndIconChangedListener endIconChangedListener = new TextInputLayout.OnEndIconChangedListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.5
        @Override // com.google.android.material.textfield.TextInputLayout.OnEndIconChangedListener
        public void onEndIconChanged(TextInputLayout textInputLayout, int i) {
            final AutoCompleteTextView autoCompleteTextView = (AutoCompleteTextView) textInputLayout.editText;
            if (autoCompleteTextView != null && i == 3) {
                autoCompleteTextView.post(new Runnable() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.5.1
                    @Override // java.lang.Runnable
                    public void run() {
                        autoCompleteTextView.removeTextChangedListener(DropdownMenuEndIconDelegate.this.exposedDropdownEndIconTextWatcher);
                    }
                });
                if (autoCompleteTextView.getOnFocusChangeListener() == DropdownMenuEndIconDelegate.this.onFocusChangeListener) {
                    autoCompleteTextView.setOnFocusChangeListener(null);
                }
                autoCompleteTextView.setOnTouchListener(null);
                autoCompleteTextView.setOnDismissListener(null);
            }
        }
    };
    public boolean dropdownPopupDirty = false;
    public boolean isEndIconChecked = false;
    public long dropdownPopupActivatedAt = RecyclerView.FOREVER_NS;

    public DropdownMenuEndIconDelegate(TextInputLayout textInputLayout) {
        super(textInputLayout);
    }

    public static AutoCompleteTextView access$000(EditText editText) {
        if (editText instanceof AutoCompleteTextView) {
            return (AutoCompleteTextView) editText;
        }
        throw new RuntimeException("EditText needs to be an AutoCompleteTextView if an Exposed Dropdown Menu is being used.");
    }

    public static boolean access$200(EditText editText) {
        return editText.getKeyListener() != null;
    }

    public static void access$300(DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate, boolean z) {
        if (dropdownMenuEndIconDelegate.isEndIconChecked != z) {
            dropdownMenuEndIconDelegate.isEndIconChecked = z;
            dropdownMenuEndIconDelegate.fadeInAnim.cancel();
            dropdownMenuEndIconDelegate.fadeOutAnim.start();
        }
    }

    public static void access$500(DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate, AutoCompleteTextView autoCompleteTextView) {
        Objects.requireNonNull(dropdownMenuEndIconDelegate);
        if (autoCompleteTextView != null) {
            if (dropdownMenuEndIconDelegate.isDropdownPopupActive()) {
                dropdownMenuEndIconDelegate.dropdownPopupDirty = false;
            }
            if (!dropdownMenuEndIconDelegate.dropdownPopupDirty) {
                boolean z = dropdownMenuEndIconDelegate.isEndIconChecked;
                boolean z2 = !z;
                if (z != z2) {
                    dropdownMenuEndIconDelegate.isEndIconChecked = z2;
                    dropdownMenuEndIconDelegate.fadeInAnim.cancel();
                    dropdownMenuEndIconDelegate.fadeOutAnim.start();
                }
                if (dropdownMenuEndIconDelegate.isEndIconChecked) {
                    autoCompleteTextView.requestFocus();
                    autoCompleteTextView.showDropDown();
                    return;
                }
                autoCompleteTextView.dismissDropDown();
                return;
            }
            dropdownMenuEndIconDelegate.dropdownPopupDirty = false;
        }
    }

    public final MaterialShapeDrawable getPopUpMaterialShapeDrawable(float f, float f2, float f3, int i) {
        ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder();
        builder.topLeftCornerSize = new AbsoluteCornerSize(f);
        builder.topRightCornerSize = new AbsoluteCornerSize(f);
        builder.bottomLeftCornerSize = new AbsoluteCornerSize(f2);
        builder.bottomRightCornerSize = new AbsoluteCornerSize(f2);
        ShapeAppearanceModel build = builder.build();
        Context context = this.context;
        Paint paint = MaterialShapeDrawable.clearPaint;
        int resolveOrThrow = MaterialAttributes.resolveOrThrow(context, R.attr.colorSurface, "MaterialShapeDrawable");
        MaterialShapeDrawable materialShapeDrawable = new MaterialShapeDrawable();
        materialShapeDrawable.drawableState.elevationOverlayProvider = new ElevationOverlayProvider(context);
        materialShapeDrawable.updateZ();
        materialShapeDrawable.setFillColor(ColorStateList.valueOf(resolveOrThrow));
        MaterialShapeDrawable.MaterialShapeDrawableState materialShapeDrawableState = materialShapeDrawable.drawableState;
        if (materialShapeDrawableState.elevation != f3) {
            materialShapeDrawableState.elevation = f3;
            materialShapeDrawable.updateZ();
        }
        materialShapeDrawable.drawableState.shapeAppearanceModel = build;
        materialShapeDrawable.invalidateSelf();
        MaterialShapeDrawable.MaterialShapeDrawableState materialShapeDrawableState2 = materialShapeDrawable.drawableState;
        if (materialShapeDrawableState2.padding == null) {
            materialShapeDrawableState2.padding = new Rect();
        }
        materialShapeDrawable.drawableState.padding.set(0, i, 0, i);
        materialShapeDrawable.invalidateSelf();
        return materialShapeDrawable;
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public void initialize() {
        float dimensionPixelOffset = (float) this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_shape_corner_size_small_component);
        float dimensionPixelOffset2 = (float) this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_exposed_dropdown_menu_popup_elevation);
        int dimensionPixelOffset3 = this.context.getResources().getDimensionPixelOffset(R.dimen.mtrl_exposed_dropdown_menu_popup_vertical_padding);
        MaterialShapeDrawable popUpMaterialShapeDrawable = getPopUpMaterialShapeDrawable(dimensionPixelOffset, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        MaterialShapeDrawable popUpMaterialShapeDrawable2 = getPopUpMaterialShapeDrawable(0.0f, dimensionPixelOffset, dimensionPixelOffset2, dimensionPixelOffset3);
        this.outlinedPopupBackground = popUpMaterialShapeDrawable;
        StateListDrawable stateListDrawable = new StateListDrawable();
        this.filledPopupBackground = stateListDrawable;
        stateListDrawable.addState(new int[]{16842922}, popUpMaterialShapeDrawable);
        this.filledPopupBackground.addState(new int[0], popUpMaterialShapeDrawable2);
        this.textInputLayout.setEndIconDrawable(AppCompatResources.getDrawable(this.context, R.drawable.mtrl_dropdown_arrow));
        TextInputLayout textInputLayout = this.textInputLayout;
        textInputLayout.setEndIconContentDescription(textInputLayout.getResources().getText(R.string.exposed_dropdown_menu_content_description));
        TextInputLayout textInputLayout2 = this.textInputLayout;
        AnonymousClass6 r1 = new View.OnClickListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.6
            @Override // android.view.View.OnClickListener
            public void onClick(View view) {
                DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate = DropdownMenuEndIconDelegate.this;
                DropdownMenuEndIconDelegate.access$500(dropdownMenuEndIconDelegate, (AutoCompleteTextView) dropdownMenuEndIconDelegate.textInputLayout.editText);
            }
        };
        CheckableImageButton checkableImageButton = textInputLayout2.endIconView;
        View.OnLongClickListener onLongClickListener = textInputLayout2.endIconOnLongClickListener;
        checkableImageButton.setOnClickListener(r1);
        TextInputLayout.setIconClickable(checkableImageButton, onLongClickListener);
        this.textInputLayout.addOnEditTextAttachedListener(this.dropdownMenuOnEditTextAttachedListener);
        this.textInputLayout.endIconChangedListeners.add(this.endIconChangedListener);
        ValueAnimator ofFloat = ValueAnimator.ofFloat(0.0f, 1.0f);
        TimeInterpolator timeInterpolator = AnimationUtils.LINEAR_INTERPOLATOR;
        ofFloat.setInterpolator(timeInterpolator);
        ofFloat.setDuration((long) 67);
        ofFloat.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DropdownMenuEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.fadeInAnim = ofFloat;
        ValueAnimator ofFloat2 = ValueAnimator.ofFloat(1.0f, 0.0f);
        ofFloat2.setInterpolator(timeInterpolator);
        ofFloat2.setDuration((long) 50);
        ofFloat2.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.10
            @Override // android.animation.ValueAnimator.AnimatorUpdateListener
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                DropdownMenuEndIconDelegate.this.endIconView.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.fadeOutAnim = ofFloat2;
        ofFloat2.addListener(new AnimatorListenerAdapter() { // from class: com.google.android.material.textfield.DropdownMenuEndIconDelegate.9
            @Override // android.animation.AnimatorListenerAdapter, android.animation.Animator.AnimatorListener
            public void onAnimationEnd(Animator animator) {
                DropdownMenuEndIconDelegate dropdownMenuEndIconDelegate = DropdownMenuEndIconDelegate.this;
                dropdownMenuEndIconDelegate.endIconView.setChecked(dropdownMenuEndIconDelegate.isEndIconChecked);
                DropdownMenuEndIconDelegate.this.fadeInAnim.start();
            }
        });
        this.accessibilityManager = (AccessibilityManager) this.context.getSystemService("accessibility");
    }

    @Override // com.google.android.material.textfield.EndIconDelegate
    public boolean isBoxBackgroundModeSupported(int i) {
        return i != 0;
    }

    public final boolean isDropdownPopupActive() {
        long currentTimeMillis = System.currentTimeMillis() - this.dropdownPopupActivatedAt;
        return currentTimeMillis < 0 || currentTimeMillis > 300;
    }
}
