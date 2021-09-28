package com.google.android.material.chip;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Outline;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.InsetDrawable;
import android.graphics.drawable.RippleDrawable;
import android.os.Bundle;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.PointerIcon;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import androidx.appcompat.R$bool;
import androidx.appcompat.widget.AppCompatCheckBox;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;
import androidx.customview.widget.ExploreByTouchHelper;
import com.android.systemui.shared.R;
import com.google.android.material.R$styleable;
import com.google.android.material.animation.MotionSpec;
import com.google.android.material.chip.ChipDrawable;
import com.google.android.material.internal.ThemeEnforcement;
import com.google.android.material.internal.ViewUtils;
import com.google.android.material.resources.MaterialResources;
import com.google.android.material.resources.TextAppearance;
import com.google.android.material.resources.TextAppearanceFontCallback;
import com.google.android.material.ripple.RippleUtils;
import com.google.android.material.shape.MaterialShapeDrawable;
import com.google.android.material.shape.ShapeAppearanceModel;
import com.google.android.material.shape.Shapeable;
import com.google.android.material.theme.overlay.MaterialThemeOverlay;
import java.lang.ref.WeakReference;
import java.util.List;
import java.util.Objects;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class Chip extends AppCompatCheckBox implements ChipDrawable.Delegate, Shapeable {
    public ChipDrawable chipDrawable;
    public boolean closeIconFocused;
    public boolean closeIconHovered;
    public boolean closeIconPressed;
    public boolean deferredCheckedValue;
    public boolean ensureMinTouchTargetSize;
    public final TextAppearanceFontCallback fontCallback;
    public InsetDrawable insetBackgroundDrawable;
    public int lastLayoutDirection;
    public int minTouchTargetSize;
    public CompoundButton.OnCheckedChangeListener onCheckedChangeListenerInternal;
    public final Rect rect;
    public final RectF rectF;
    public RippleDrawable ripple;
    public final ChipTouchHelper touchHelper;
    public static final Rect EMPTY_BOUNDS = new Rect();
    public static final int[] SELECTED_STATE = {16842913};
    public static final int[] CHECKABLE_STATE_SET = {16842911};

    /* loaded from: classes.dex */
    public class ChipTouchHelper extends ExploreByTouchHelper {
        public ChipTouchHelper(Chip chip) {
            super(chip);
        }

        public int getVirtualViewAt(float f, float f2) {
            Chip chip = Chip.this;
            Rect rect = Chip.EMPTY_BOUNDS;
            return (!chip.hasCloseIcon() || !Chip.this.getCloseIconTouchBounds().contains(f, f2)) ? 0 : 1;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void getVisibleVirtualViews(List<Integer> list) {
            list.add(0);
            Chip chip = Chip.this;
            Rect rect = Chip.EMPTY_BOUNDS;
            if (chip.hasCloseIcon()) {
                ChipDrawable chipDrawable = Chip.this.chipDrawable;
            }
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public boolean onPerformActionForVirtualView(int i, int i2, Bundle bundle) {
            if (i2 == 16) {
                if (i == 0) {
                    return Chip.this.performClick();
                }
                if (i == 1) {
                    Chip chip = Chip.this;
                    chip.playSoundEffect(0);
                    chip.touchHelper.sendEventForVirtualView(1, 1);
                }
            }
            return false;
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateNodeForHost(AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            accessibilityNodeInfoCompat.mInfo.setCheckable(Chip.this.isCheckable());
            accessibilityNodeInfoCompat.mInfo.setClickable(Chip.this.isClickable());
            if (Chip.this.isCheckable() || Chip.this.isClickable()) {
                accessibilityNodeInfoCompat.mInfo.setClassName(Chip.this.isCheckable() ? "android.widget.CompoundButton" : "android.widget.Button");
            } else {
                accessibilityNodeInfoCompat.mInfo.setClassName("android.view.View");
            }
            accessibilityNodeInfoCompat.mInfo.setText(Chip.this.getText());
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onPopulateNodeForVirtualView(int i, AccessibilityNodeInfoCompat accessibilityNodeInfoCompat) {
            String str = "";
            if (i == 1) {
                Chip chip = Chip.this;
                ChipDrawable chipDrawable = chip.chipDrawable;
                CharSequence text = chip.getText();
                Context context = Chip.this.getContext();
                Object[] objArr = new Object[1];
                if (!TextUtils.isEmpty(text)) {
                    str = text;
                }
                objArr[0] = str;
                accessibilityNodeInfoCompat.mInfo.setContentDescription(context.getString(R.string.mtrl_chip_close_icon_content_description, objArr).trim());
                accessibilityNodeInfoCompat.mInfo.setBoundsInParent(Chip.this.getCloseIconTouchBoundsInt());
                accessibilityNodeInfoCompat.addAction(AccessibilityNodeInfoCompat.AccessibilityActionCompat.ACTION_CLICK);
                accessibilityNodeInfoCompat.mInfo.setEnabled(Chip.this.isEnabled());
                return;
            }
            accessibilityNodeInfoCompat.mInfo.setContentDescription(str);
            accessibilityNodeInfoCompat.mInfo.setBoundsInParent(Chip.EMPTY_BOUNDS);
        }

        @Override // androidx.customview.widget.ExploreByTouchHelper
        public void onVirtualViewKeyboardFocusChanged(int i, boolean z) {
            if (i == 1) {
                Chip chip = Chip.this;
                chip.closeIconFocused = z;
                chip.refreshDrawableState();
            }
        }
    }

    public Chip(Context context) {
        this(context, null);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:34:0x00ab, code lost:
        if (r1 != Integer.MIN_VALUE) goto L_0x00ad;
     */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x005d  */
    /* JADX WARNING: Removed duplicated region for block: B:44:? A[RETURN, SYNTHETIC] */
    @Override // android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean dispatchHoverEvent(android.view.MotionEvent r11) {
        /*
            r10 = this;
            java.lang.Class<androidx.customview.widget.ExploreByTouchHelper> r0 = androidx.customview.widget.ExploreByTouchHelper.class
            java.lang.String r1 = "Unable to send Accessibility Exit event"
            java.lang.String r2 = "Chip"
            int r3 = r11.getAction()
            r4 = -2147483648(0xffffffff80000000, float:-0.0)
            r5 = 10
            r6 = 1
            r7 = 0
            if (r3 != r5) goto L_0x005a
            java.lang.String r3 = "mHoveredVirtualViewId"
            java.lang.reflect.Field r3 = r0.getDeclaredField(r3)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            r3.setAccessible(r6)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            com.google.android.material.chip.Chip$ChipTouchHelper r8 = r10.touchHelper     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            java.lang.Object r3 = r3.get(r8)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            java.lang.Integer r3 = (java.lang.Integer) r3     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            int r3 = r3.intValue()     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            if (r3 == r4) goto L_0x005a
            java.lang.String r3 = "updateHoveredVirtualView"
            java.lang.Class[] r8 = new java.lang.Class[r6]     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            java.lang.Class r9 = java.lang.Integer.TYPE     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            r8[r7] = r9     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            java.lang.reflect.Method r0 = r0.getDeclaredMethod(r3, r8)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            r0.setAccessible(r6)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            com.google.android.material.chip.Chip$ChipTouchHelper r3 = r10.touchHelper     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            java.lang.Object[] r8 = new java.lang.Object[r6]     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            java.lang.Integer r9 = java.lang.Integer.valueOf(r4)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            r8[r7] = r9     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            r0.invoke(r3, r8)     // Catch: NoSuchMethodException -> 0x0056, IllegalAccessException -> 0x0051, InvocationTargetException -> 0x004c, NoSuchFieldException -> 0x0047
            r0 = r6
            goto L_0x005b
        L_0x0047:
            r0 = move-exception
            android.util.Log.e(r2, r1, r0)
            goto L_0x005a
        L_0x004c:
            r0 = move-exception
            android.util.Log.e(r2, r1, r0)
            goto L_0x005a
        L_0x0051:
            r0 = move-exception
            android.util.Log.e(r2, r1, r0)
            goto L_0x005a
        L_0x0056:
            r0 = move-exception
            android.util.Log.e(r2, r1, r0)
        L_0x005a:
            r0 = r7
        L_0x005b:
            if (r0 != 0) goto L_0x00ba
            com.google.android.material.chip.Chip$ChipTouchHelper r0 = r10.touchHelper
            android.view.accessibility.AccessibilityManager r1 = r0.mManager
            boolean r1 = r1.isEnabled()
            if (r1 == 0) goto L_0x00af
            android.view.accessibility.AccessibilityManager r1 = r0.mManager
            boolean r1 = r1.isTouchExplorationEnabled()
            if (r1 != 0) goto L_0x0070
            goto L_0x00af
        L_0x0070:
            int r1 = r11.getAction()
            r2 = 7
            r3 = 256(0x100, float:3.59E-43)
            r8 = 128(0x80, float:1.794E-43)
            if (r1 == r2) goto L_0x0092
            r2 = 9
            if (r1 == r2) goto L_0x0092
            if (r1 == r5) goto L_0x0082
            goto L_0x00af
        L_0x0082:
            int r1 = r0.mHoveredVirtualViewId
            if (r1 == r4) goto L_0x00af
            if (r1 != r4) goto L_0x0089
            goto L_0x00ad
        L_0x0089:
            r0.mHoveredVirtualViewId = r4
            r0.sendEventForVirtualView(r4, r8)
            r0.sendEventForVirtualView(r1, r3)
            goto L_0x00ad
        L_0x0092:
            float r1 = r11.getX()
            float r2 = r11.getY()
            int r1 = r0.getVirtualViewAt(r1, r2)
            int r2 = r0.mHoveredVirtualViewId
            if (r2 != r1) goto L_0x00a3
            goto L_0x00ab
        L_0x00a3:
            r0.mHoveredVirtualViewId = r1
            r0.sendEventForVirtualView(r1, r8)
            r0.sendEventForVirtualView(r2, r3)
        L_0x00ab:
            if (r1 == r4) goto L_0x00af
        L_0x00ad:
            r0 = r6
            goto L_0x00b0
        L_0x00af:
            r0 = r7
        L_0x00b0:
            if (r0 != 0) goto L_0x00ba
            boolean r10 = super.dispatchHoverEvent(r11)
            if (r10 == 0) goto L_0x00b9
            goto L_0x00ba
        L_0x00b9:
            r6 = r7
        L_0x00ba:
            return r6
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.dispatchHoverEvent(android.view.MotionEvent):boolean");
    }

    @Override // android.view.View
    public boolean dispatchKeyEvent(KeyEvent keyEvent) {
        ChipTouchHelper chipTouchHelper = this.touchHelper;
        Objects.requireNonNull(chipTouchHelper);
        boolean z = false;
        int i = 0;
        z = false;
        z = false;
        z = false;
        z = false;
        z = false;
        if (keyEvent.getAction() != 1) {
            int keyCode = keyEvent.getKeyCode();
            if (keyCode != 61) {
                int i2 = 66;
                if (keyCode != 66) {
                    switch (keyCode) {
                        case 19:
                        case 20:
                        case 21:
                        case 22:
                            if (keyEvent.hasNoModifiers()) {
                                if (keyCode == 19) {
                                    i2 = 33;
                                } else if (keyCode == 21) {
                                    i2 = 17;
                                } else if (keyCode != 22) {
                                    i2 = 130;
                                }
                                int repeatCount = keyEvent.getRepeatCount() + 1;
                                boolean z2 = false;
                                while (i < repeatCount && chipTouchHelper.moveFocus(i2, null)) {
                                    i++;
                                    z2 = true;
                                }
                                z = z2;
                                break;
                            }
                            break;
                    }
                }
                if (keyEvent.hasNoModifiers() && keyEvent.getRepeatCount() == 0) {
                    int i3 = chipTouchHelper.mKeyboardFocusedVirtualViewId;
                    if (i3 != Integer.MIN_VALUE) {
                        chipTouchHelper.onPerformActionForVirtualView(i3, 16, null);
                    }
                    z = true;
                }
            } else if (keyEvent.hasNoModifiers()) {
                z = chipTouchHelper.moveFocus(2, null);
            } else if (keyEvent.hasModifiers(1)) {
                z = chipTouchHelper.moveFocus(1, null);
            }
        }
        if (!z || this.touchHelper.mKeyboardFocusedVirtualViewId == Integer.MIN_VALUE) {
            return super.dispatchKeyEvent(keyEvent);
        }
        return true;
    }

    /* JADX WARN: Type inference failed for: r2v0, types: [boolean, int] */
    /* JADX WARNING: Unknown variable types count: 1 */
    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.widget.TextView, android.widget.CompoundButton, android.view.View
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void drawableStateChanged() {
        /*
            r4 = this;
            super.drawableStateChanged()
            com.google.android.material.chip.ChipDrawable r0 = r4.chipDrawable
            r1 = 0
            if (r0 == 0) goto L_0x006e
            android.graphics.drawable.Drawable r0 = r0.closeIcon
            boolean r0 = com.google.android.material.chip.ChipDrawable.isStateful(r0)
            if (r0 == 0) goto L_0x006e
            com.google.android.material.chip.ChipDrawable r0 = r4.chipDrawable
            boolean r2 = r4.isEnabled()
            boolean r3 = r4.closeIconFocused
            if (r3 == 0) goto L_0x001c
            int r2 = r2 + 1
        L_0x001c:
            boolean r3 = r4.closeIconHovered
            if (r3 == 0) goto L_0x0022
            int r2 = r2 + 1
        L_0x0022:
            boolean r3 = r4.closeIconPressed
            if (r3 == 0) goto L_0x0028
            int r2 = r2 + 1
        L_0x0028:
            boolean r3 = r4.isChecked()
            if (r3 == 0) goto L_0x0030
            int r2 = r2 + 1
        L_0x0030:
            int[] r2 = new int[r2]
            boolean r3 = r4.isEnabled()
            if (r3 == 0) goto L_0x003e
            r3 = 16842910(0x101009e, float:2.3694E-38)
            r2[r1] = r3
            r1 = 1
        L_0x003e:
            boolean r3 = r4.closeIconFocused
            if (r3 == 0) goto L_0x0049
            r3 = 16842908(0x101009c, float:2.3693995E-38)
            r2[r1] = r3
            int r1 = r1 + 1
        L_0x0049:
            boolean r3 = r4.closeIconHovered
            if (r3 == 0) goto L_0x0054
            r3 = 16843623(0x1010367, float:2.3696E-38)
            r2[r1] = r3
            int r1 = r1 + 1
        L_0x0054:
            boolean r3 = r4.closeIconPressed
            if (r3 == 0) goto L_0x005f
            r3 = 16842919(0x10100a7, float:2.3694026E-38)
            r2[r1] = r3
            int r1 = r1 + 1
        L_0x005f:
            boolean r3 = r4.isChecked()
            if (r3 == 0) goto L_0x006a
            r3 = 16842913(0x10100a1, float:2.369401E-38)
            r2[r1] = r3
        L_0x006a:
            boolean r1 = r0.setCloseIconState(r2)
        L_0x006e:
            if (r1 == 0) goto L_0x0073
            r4.invalidate()
        L_0x0073:
            return
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.drawableStateChanged():void");
    }

    public boolean ensureAccessibleTouchTarget(int i) {
        this.minTouchTargetSize = i;
        int i2 = 0;
        if (!this.ensureMinTouchTargetSize) {
            if (this.insetBackgroundDrawable != null) {
                removeBackgroundInset();
            } else {
                updateBackgroundDrawable();
            }
            return false;
        }
        int max = Math.max(0, i - ((int) this.chipDrawable.chipMinHeight));
        int max2 = Math.max(0, i - this.chipDrawable.getIntrinsicWidth());
        if (max2 > 0 || max > 0) {
            int i3 = max2 > 0 ? max2 / 2 : 0;
            if (max > 0) {
                i2 = max / 2;
            }
            if (this.insetBackgroundDrawable != null) {
                Rect rect = new Rect();
                this.insetBackgroundDrawable.getPadding(rect);
                if (rect.top == i2 && rect.bottom == i2 && rect.left == i3 && rect.right == i3) {
                    updateBackgroundDrawable();
                    return true;
                }
            }
            if (getMinHeight() != i) {
                setMinHeight(i);
            }
            if (getMinWidth() != i) {
                setMinWidth(i);
            }
            this.insetBackgroundDrawable = new InsetDrawable((Drawable) this.chipDrawable, i3, i2, i3, i2);
            updateBackgroundDrawable();
            return true;
        }
        if (this.insetBackgroundDrawable != null) {
            removeBackgroundInset();
        } else {
            updateBackgroundDrawable();
        }
        return false;
    }

    public Drawable getBackgroundDrawable() {
        InsetDrawable insetDrawable = this.insetBackgroundDrawable;
        return insetDrawable == null ? this.chipDrawable : insetDrawable;
    }

    public final RectF getCloseIconTouchBounds() {
        this.rectF.setEmpty();
        hasCloseIcon();
        return this.rectF;
    }

    public final Rect getCloseIconTouchBoundsInt() {
        RectF closeIconTouchBounds = getCloseIconTouchBounds();
        this.rect.set((int) closeIconTouchBounds.left, (int) closeIconTouchBounds.top, (int) closeIconTouchBounds.right, (int) closeIconTouchBounds.bottom);
        return this.rect;
    }

    @Override // android.widget.TextView
    public TextUtils.TruncateAt getEllipsize() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            return chipDrawable.truncateAt;
        }
        return null;
    }

    @Override // android.widget.TextView, android.view.View
    public void getFocusedRect(Rect rect) {
        ChipTouchHelper chipTouchHelper = this.touchHelper;
        if (chipTouchHelper.mKeyboardFocusedVirtualViewId == 1 || chipTouchHelper.mAccessibilityFocusedVirtualViewId == 1) {
            rect.set(getCloseIconTouchBoundsInt());
        } else {
            super.getFocusedRect(rect);
        }
    }

    public final boolean hasCloseIcon() {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            Drawable drawable = chipDrawable.closeIcon;
            if ((drawable != null ? DrawableCompat.unwrap(drawable) : null) != null) {
                return true;
            }
        }
        return false;
    }

    public boolean isCheckable() {
        ChipDrawable chipDrawable = this.chipDrawable;
        return chipDrawable != null && chipDrawable.checkable;
    }

    @Override // android.widget.TextView, android.view.View
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        R$bool.setParentAbsoluteElevation(this, this.chipDrawable);
    }

    @Override // com.google.android.material.chip.ChipDrawable.Delegate
    public void onChipDrawableSizeChange() {
        ensureAccessibleTouchTarget(this.minTouchTargetSize);
        requestLayout();
        invalidateOutline();
    }

    @Override // android.widget.TextView, android.widget.CompoundButton, android.view.View
    public int[] onCreateDrawableState(int i) {
        int[] onCreateDrawableState = super.onCreateDrawableState(i + 2);
        if (isChecked()) {
            CheckBox.mergeDrawableStates(onCreateDrawableState, SELECTED_STATE);
        }
        if (isCheckable()) {
            CheckBox.mergeDrawableStates(onCreateDrawableState, CHECKABLE_STATE_SET);
        }
        return onCreateDrawableState;
    }

    @Override // android.widget.TextView, android.view.View
    public void onFocusChanged(boolean z, int i, Rect rect) {
        super.onFocusChanged(z, i, rect);
        ChipTouchHelper chipTouchHelper = this.touchHelper;
        int i2 = chipTouchHelper.mKeyboardFocusedVirtualViewId;
        if (i2 != Integer.MIN_VALUE) {
            chipTouchHelper.clearKeyboardFocusForVirtualView(i2);
        }
        if (z) {
            chipTouchHelper.moveFocus(i, rect);
        }
    }

    @Override // android.view.View
    public boolean onHoverEvent(MotionEvent motionEvent) {
        int actionMasked = motionEvent.getActionMasked();
        if (actionMasked == 7) {
            boolean contains = getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY());
            if (this.closeIconHovered != contains) {
                this.closeIconHovered = contains;
                refreshDrawableState();
            }
        } else if (actionMasked == 10 && this.closeIconHovered) {
            this.closeIconHovered = false;
            refreshDrawableState();
        }
        return super.onHoverEvent(motionEvent);
    }

    @Override // android.view.View
    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        int i;
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        if (isCheckable() || isClickable()) {
            accessibilityNodeInfo.setClassName(isCheckable() ? "android.widget.CompoundButton" : "android.widget.Button");
        } else {
            accessibilityNodeInfo.setClassName("android.view.View");
        }
        accessibilityNodeInfo.setCheckable(isCheckable());
        accessibilityNodeInfo.setClickable(isClickable());
        if (getParent() instanceof ChipGroup) {
            ChipGroup chipGroup = (ChipGroup) getParent();
            int i2 = -1;
            if (chipGroup.singleLine) {
                int i3 = 0;
                int i4 = 0;
                while (true) {
                    if (i3 >= chipGroup.getChildCount()) {
                        i4 = -1;
                        break;
                    }
                    if (chipGroup.getChildAt(i3) instanceof Chip) {
                        if (((Chip) chipGroup.getChildAt(i3)) == this) {
                            break;
                        }
                        i4++;
                    }
                    i3++;
                }
                i = i4;
            } else {
                i = -1;
            }
            Object tag = getTag(R.id.row_index_key);
            if (tag instanceof Integer) {
                i2 = ((Integer) tag).intValue();
            }
            accessibilityNodeInfo.setCollectionItemInfo((AccessibilityNodeInfo.CollectionItemInfo) AccessibilityNodeInfoCompat.CollectionItemInfoCompat.obtain(i2, 1, i, 1, false, isChecked()).mInfo);
        }
    }

    @Override // android.widget.TextView, android.widget.Button, android.view.View
    @TargetApi(24)
    public PointerIcon onResolvePointerIcon(MotionEvent motionEvent, int i) {
        if (!getCloseIconTouchBounds().contains(motionEvent.getX(), motionEvent.getY()) || !isEnabled()) {
            return null;
        }
        return PointerIcon.getSystemIcon(getContext(), 1002);
    }

    @Override // android.widget.TextView, android.view.View
    @TargetApi(17)
    public void onRtlPropertiesChanged(int i) {
        super.onRtlPropertiesChanged(i);
        if (this.lastLayoutDirection != i) {
            this.lastLayoutDirection = i;
            updatePaddingInternal();
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:8:0x001e, code lost:
        if (r0 != 3) goto L_0x0045;
     */
    @Override // android.widget.TextView, android.view.View
    @android.annotation.SuppressLint({"ClickableViewAccessibility"})
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean onTouchEvent(android.view.MotionEvent r6) {
        /*
            r5 = this;
            int r0 = r6.getActionMasked()
            android.graphics.RectF r1 = r5.getCloseIconTouchBounds()
            float r2 = r6.getX()
            float r3 = r6.getY()
            boolean r1 = r1.contains(r2, r3)
            r2 = 0
            r3 = 1
            if (r0 == 0) goto L_0x003e
            if (r0 == r3) goto L_0x002b
            r4 = 2
            if (r0 == r4) goto L_0x0021
            r1 = 3
            if (r0 == r1) goto L_0x0039
            goto L_0x0045
        L_0x0021:
            boolean r0 = r5.closeIconPressed
            if (r0 == 0) goto L_0x0045
            if (r1 != 0) goto L_0x0043
            r5.setCloseIconPressed(r2)
            goto L_0x0043
        L_0x002b:
            boolean r0 = r5.closeIconPressed
            if (r0 == 0) goto L_0x0039
            r5.playSoundEffect(r2)
            com.google.android.material.chip.Chip$ChipTouchHelper r0 = r5.touchHelper
            r0.sendEventForVirtualView(r3, r3)
            r0 = r3
            goto L_0x003a
        L_0x0039:
            r0 = r2
        L_0x003a:
            r5.setCloseIconPressed(r2)
            goto L_0x0046
        L_0x003e:
            if (r1 == 0) goto L_0x0045
            r5.setCloseIconPressed(r3)
        L_0x0043:
            r0 = r3
            goto L_0x0046
        L_0x0045:
            r0 = r2
        L_0x0046:
            if (r0 != 0) goto L_0x004e
            boolean r5 = super.onTouchEvent(r6)
            if (r5 == 0) goto L_0x004f
        L_0x004e:
            r2 = r3
        L_0x004f:
            return r2
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.chip.Chip.onTouchEvent(android.view.MotionEvent):boolean");
    }

    public final void removeBackgroundInset() {
        if (this.insetBackgroundDrawable != null) {
            this.insetBackgroundDrawable = null;
            setMinWidth(0);
            ChipDrawable chipDrawable = this.chipDrawable;
            setMinHeight((int) (chipDrawable != null ? chipDrawable.chipMinHeight : 0.0f));
            updateBackgroundDrawable();
        }
    }

    @Override // android.view.View
    public void setBackground(Drawable drawable) {
        if (drawable == getBackgroundDrawable() || drawable == this.ripple) {
            super.setBackground(drawable);
        } else {
            Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
        }
    }

    @Override // android.view.View
    public void setBackgroundColor(int i) {
        Log.w("Chip", "Do not set the background color; Chip manages its own background drawable.");
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundDrawable(Drawable drawable) {
        if (drawable == getBackgroundDrawable() || drawable == this.ripple) {
            super.setBackgroundDrawable(drawable);
        } else {
            Log.w("Chip", "Do not set the background drawable; Chip manages its own background drawable.");
        }
    }

    @Override // androidx.appcompat.widget.AppCompatCheckBox, android.view.View
    public void setBackgroundResource(int i) {
        Log.w("Chip", "Do not set the background resource; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintList(ColorStateList colorStateList) {
        Log.w("Chip", "Do not set the background tint list; Chip manages its own background drawable.");
    }

    @Override // android.view.View
    public void setBackgroundTintMode(PorterDuff.Mode mode) {
        Log.w("Chip", "Do not set the background tint mode; Chip manages its own background drawable.");
    }

    @Override // android.widget.CompoundButton, android.widget.Checkable
    public void setChecked(boolean z) {
        CompoundButton.OnCheckedChangeListener onCheckedChangeListener;
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable == null) {
            this.deferredCheckedValue = z;
        } else if (chipDrawable.checkable) {
            boolean isChecked = isChecked();
            super.setChecked(z);
            if (isChecked != z && (onCheckedChangeListener = this.onCheckedChangeListenerInternal) != null) {
                onCheckedChangeListener.onCheckedChanged(this, z);
            }
        }
    }

    public final void setCloseIconPressed(boolean z) {
        if (this.closeIconPressed != z) {
            this.closeIconPressed = z;
            refreshDrawableState();
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawables(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelative(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (i3 == 0) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(i, i2, i3, i4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        if (i != 0) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (i3 == 0) {
            super.setCompoundDrawablesWithIntrinsicBounds(i, i2, i3, i4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.view.View
    public void setElevation(float f) {
        super.setElevation(f);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            MaterialShapeDrawable.MaterialShapeDrawableState materialShapeDrawableState = chipDrawable.drawableState;
            if (materialShapeDrawableState.elevation != f) {
                materialShapeDrawableState.elevation = f;
                chipDrawable.updateZ();
            }
        }
    }

    @Override // android.widget.TextView
    public void setEllipsize(TextUtils.TruncateAt truncateAt) {
        if (this.chipDrawable != null) {
            if (truncateAt != TextUtils.TruncateAt.MARQUEE) {
                super.setEllipsize(truncateAt);
                ChipDrawable chipDrawable = this.chipDrawable;
                if (chipDrawable != null) {
                    chipDrawable.truncateAt = truncateAt;
                    return;
                }
                return;
            }
            throw new UnsupportedOperationException("Text within a chip are not allowed to scroll.");
        }
    }

    @Override // android.widget.TextView
    public void setGravity(int i) {
        if (i != 8388627) {
            Log.w("Chip", "Chip text must be vertically center and start aligned");
        } else {
            super.setGravity(i);
        }
    }

    @Override // android.view.View
    public void setLayoutDirection(int i) {
        if (this.chipDrawable != null) {
            super.setLayoutDirection(i);
        }
    }

    @Override // android.widget.TextView
    public void setLines(int i) {
        if (i <= 1) {
            super.setLines(i);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMaxLines(int i) {
        if (i <= 1) {
            super.setMaxLines(i);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setMaxWidth(int i) {
        super.setMaxWidth(i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.maxWidth = i;
        }
    }

    @Override // android.widget.TextView
    public void setMinLines(int i) {
        if (i <= 1) {
            super.setMinLines(i);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // com.google.android.material.shape.Shapeable
    public void setShapeAppearanceModel(ShapeAppearanceModel shapeAppearanceModel) {
        ChipDrawable chipDrawable = this.chipDrawable;
        chipDrawable.drawableState.shapeAppearanceModel = shapeAppearanceModel;
        chipDrawable.invalidateSelf();
    }

    @Override // android.widget.TextView
    public void setSingleLine(boolean z) {
        if (z) {
            super.setSingleLine(z);
            return;
        }
        throw new UnsupportedOperationException("Chip does not support multi-line text");
    }

    @Override // android.widget.TextView
    public void setText(CharSequence charSequence, TextView.BufferType bufferType) {
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            if (charSequence == null) {
                charSequence = "";
            }
            super.setText(chipDrawable.shouldDrawText ? null : charSequence, bufferType);
            ChipDrawable chipDrawable2 = this.chipDrawable;
            if (chipDrawable2 != null) {
                chipDrawable2.setText(charSequence);
            }
        }
    }

    @Override // android.widget.TextView
    public void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearance(new TextAppearance(chipDrawable.context, i));
        }
        updateTextPaintDrawState();
    }

    public final void updateBackgroundDrawable() {
        this.ripple = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(this.chipDrawable.rippleColor), getBackgroundDrawable(), null);
        this.chipDrawable.setUseCompatRipple(false);
        RippleDrawable rippleDrawable = this.ripple;
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        setBackground(rippleDrawable);
        updatePaddingInternal();
    }

    public final void updatePaddingInternal() {
        ChipDrawable chipDrawable;
        if (!TextUtils.isEmpty(getText()) && (chipDrawable = this.chipDrawable) != null) {
            int calculateCloseIconWidth = (int) (chipDrawable.calculateCloseIconWidth() + chipDrawable.chipEndPadding + chipDrawable.textEndPadding);
            ChipDrawable chipDrawable2 = this.chipDrawable;
            int calculateChipIconWidth = (int) (chipDrawable2.calculateChipIconWidth() + chipDrawable2.chipStartPadding + chipDrawable2.textStartPadding);
            if (this.insetBackgroundDrawable != null) {
                Rect rect = new Rect();
                this.insetBackgroundDrawable.getPadding(rect);
                calculateChipIconWidth += rect.left;
                calculateCloseIconWidth += rect.right;
            }
            int paddingTop = getPaddingTop();
            int paddingBottom = getPaddingBottom();
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            setPaddingRelative(calculateChipIconWidth, paddingTop, calculateCloseIconWidth, paddingBottom);
        }
    }

    public final void updateTextPaintDrawState() {
        TextPaint paint = getPaint();
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            paint.drawableState = chipDrawable.getState();
        }
        ChipDrawable chipDrawable2 = this.chipDrawable;
        TextAppearance textAppearance = chipDrawable2 != null ? chipDrawable2.textDrawableHelper.textAppearance : null;
        if (textAppearance != null) {
            textAppearance.updateDrawState(getContext(), paint, this.fontCallback);
        }
    }

    public Chip(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.chipStyle);
    }

    public Chip(Context context, AttributeSet attributeSet, int i) {
        super(MaterialThemeOverlay.wrap(context, attributeSet, i, 2131886806), attributeSet, i);
        ColorStateList colorStateList;
        int resourceId;
        this.rect = new Rect();
        this.rectF = new RectF();
        this.fontCallback = new TextAppearanceFontCallback() { // from class: com.google.android.material.chip.Chip.1
            @Override // com.google.android.material.resources.TextAppearanceFontCallback
            public void onFontRetrievalFailed(int i2) {
            }

            @Override // com.google.android.material.resources.TextAppearanceFontCallback
            public void onFontRetrieved(Typeface typeface, boolean z) {
                CharSequence charSequence;
                Chip chip = Chip.this;
                ChipDrawable chipDrawable = chip.chipDrawable;
                if (chipDrawable.shouldDrawText) {
                    charSequence = chipDrawable.text;
                } else {
                    charSequence = chip.getText();
                }
                chip.setText(charSequence);
                Chip.this.requestLayout();
                Chip.this.invalidate();
            }
        };
        Context context2 = getContext();
        if (attributeSet != null) {
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "background") != null) {
                Log.w("Chip", "Do not set the background; Chip manages its own background drawable.");
            }
            if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableLeft") != null) {
                throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableStart") != null) {
                throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
            } else if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableEnd") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (attributeSet.getAttributeValue("http://schemas.android.com/apk/res/android", "drawableRight") != null) {
                throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
            } else if (!attributeSet.getAttributeBooleanValue("http://schemas.android.com/apk/res/android", "singleLine", true) || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "lines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "minLines", 1) != 1 || attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "maxLines", 1) != 1) {
                throw new UnsupportedOperationException("Chip does not support multi-line text");
            } else if (attributeSet.getAttributeIntValue("http://schemas.android.com/apk/res/android", "gravity", 8388627) != 8388627) {
                Log.w("Chip", "Chip text must be vertically center and start aligned");
            }
        }
        ChipDrawable chipDrawable = new ChipDrawable(context2, attributeSet, i, 2131886806);
        Context context3 = chipDrawable.context;
        int[] iArr = R$styleable.Chip;
        TypedArray obtainStyledAttributes = ThemeEnforcement.obtainStyledAttributes(context3, attributeSet, iArr, i, 2131886806, new int[0]);
        chipDrawable.isShapeThemingEnabled = obtainStyledAttributes.hasValue(37);
        ColorStateList colorStateList2 = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 24);
        if (chipDrawable.chipSurfaceColor != colorStateList2) {
            chipDrawable.chipSurfaceColor = colorStateList2;
            chipDrawable.onStateChange(chipDrawable.getState());
        }
        ColorStateList colorStateList3 = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 11);
        if (chipDrawable.chipBackgroundColor != colorStateList3) {
            chipDrawable.chipBackgroundColor = colorStateList3;
            chipDrawable.onStateChange(chipDrawable.getState());
        }
        float dimension = obtainStyledAttributes.getDimension(19, 0.0f);
        if (chipDrawable.chipMinHeight != dimension) {
            chipDrawable.chipMinHeight = dimension;
            chipDrawable.invalidateSelf();
            chipDrawable.onSizeChange();
        }
        if (obtainStyledAttributes.hasValue(12)) {
            float dimension2 = obtainStyledAttributes.getDimension(12, 0.0f);
            if (chipDrawable.chipCornerRadius != dimension2) {
                chipDrawable.chipCornerRadius = dimension2;
                ShapeAppearanceModel shapeAppearanceModel = chipDrawable.drawableState.shapeAppearanceModel;
                Objects.requireNonNull(shapeAppearanceModel);
                ShapeAppearanceModel.Builder builder = new ShapeAppearanceModel.Builder(shapeAppearanceModel);
                builder.setAllCornerSizes(dimension2);
                chipDrawable.drawableState.shapeAppearanceModel = builder.build();
                chipDrawable.invalidateSelf();
            }
        }
        ColorStateList colorStateList4 = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 22);
        if (chipDrawable.chipStrokeColor != colorStateList4) {
            chipDrawable.chipStrokeColor = colorStateList4;
            if (chipDrawable.isShapeThemingEnabled) {
                chipDrawable.setStrokeColor(colorStateList4);
            }
            chipDrawable.onStateChange(chipDrawable.getState());
        }
        float dimension3 = obtainStyledAttributes.getDimension(23, 0.0f);
        if (chipDrawable.chipStrokeWidth != dimension3) {
            chipDrawable.chipStrokeWidth = dimension3;
            chipDrawable.chipPaint.setStrokeWidth(dimension3);
            if (chipDrawable.isShapeThemingEnabled) {
                chipDrawable.drawableState.strokeWidth = dimension3;
                chipDrawable.invalidateSelf();
            }
            chipDrawable.invalidateSelf();
        }
        ColorStateList colorStateList5 = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 36);
        if (chipDrawable.rippleColor != colorStateList5) {
            chipDrawable.rippleColor = colorStateList5;
            chipDrawable.compatRippleColor = chipDrawable.useCompatRipple ? RippleUtils.sanitizeRippleDrawableColor(colorStateList5) : null;
            chipDrawable.onStateChange(chipDrawable.getState());
        }
        chipDrawable.setText(obtainStyledAttributes.getText(5));
        TextAppearance textAppearance = (!obtainStyledAttributes.hasValue(0) || (resourceId = obtainStyledAttributes.getResourceId(0, 0)) == 0) ? null : new TextAppearance(chipDrawable.context, resourceId);
        textAppearance.textSize = obtainStyledAttributes.getDimension(1, textAppearance.textSize);
        chipDrawable.setTextAppearance(textAppearance);
        int i2 = obtainStyledAttributes.getInt(3, 0);
        if (i2 == 1) {
            chipDrawable.truncateAt = TextUtils.TruncateAt.START;
        } else if (i2 == 2) {
            chipDrawable.truncateAt = TextUtils.TruncateAt.MIDDLE;
        } else if (i2 == 3) {
            chipDrawable.truncateAt = TextUtils.TruncateAt.END;
        }
        chipDrawable.setChipIconVisible(obtainStyledAttributes.getBoolean(18, false));
        if (!(attributeSet == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconEnabled") == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "chipIconVisible") != null)) {
            chipDrawable.setChipIconVisible(obtainStyledAttributes.getBoolean(15, false));
        }
        Drawable drawable = MaterialResources.getDrawable(chipDrawable.context, obtainStyledAttributes, 14);
        Drawable drawable2 = chipDrawable.chipIcon;
        Drawable unwrap = drawable2 != null ? DrawableCompat.unwrap(drawable2) : null;
        if (unwrap != drawable) {
            float calculateChipIconWidth = chipDrawable.calculateChipIconWidth();
            chipDrawable.chipIcon = drawable != null ? drawable.mutate() : null;
            float calculateChipIconWidth2 = chipDrawable.calculateChipIconWidth();
            chipDrawable.unapplyChildDrawable(unwrap);
            if (chipDrawable.showsChipIcon()) {
                chipDrawable.applyChildDrawable(chipDrawable.chipIcon);
            }
            chipDrawable.invalidateSelf();
            if (calculateChipIconWidth != calculateChipIconWidth2) {
                chipDrawable.onSizeChange();
            }
        }
        if (obtainStyledAttributes.hasValue(17)) {
            ColorStateList colorStateList6 = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 17);
            chipDrawable.hasChipIconTint = true;
            if (chipDrawable.chipIconTint != colorStateList6) {
                chipDrawable.chipIconTint = colorStateList6;
                if (chipDrawable.showsChipIcon()) {
                    chipDrawable.chipIcon.setTintList(colorStateList6);
                }
                chipDrawable.onStateChange(chipDrawable.getState());
            }
        }
        float dimension4 = obtainStyledAttributes.getDimension(16, -1.0f);
        if (chipDrawable.chipIconSize != dimension4) {
            float calculateChipIconWidth3 = chipDrawable.calculateChipIconWidth();
            chipDrawable.chipIconSize = dimension4;
            float calculateChipIconWidth4 = chipDrawable.calculateChipIconWidth();
            chipDrawable.invalidateSelf();
            if (calculateChipIconWidth3 != calculateChipIconWidth4) {
                chipDrawable.onSizeChange();
            }
        }
        chipDrawable.setCloseIconVisible(obtainStyledAttributes.getBoolean(31, false));
        if (!(attributeSet == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconEnabled") == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "closeIconVisible") != null)) {
            chipDrawable.setCloseIconVisible(obtainStyledAttributes.getBoolean(26, false));
        }
        Drawable drawable3 = MaterialResources.getDrawable(chipDrawable.context, obtainStyledAttributes, 25);
        Drawable drawable4 = chipDrawable.closeIcon;
        Drawable unwrap2 = drawable4 != null ? DrawableCompat.unwrap(drawable4) : null;
        if (unwrap2 != drawable3) {
            float calculateCloseIconWidth = chipDrawable.calculateCloseIconWidth();
            chipDrawable.closeIcon = drawable3 != null ? drawable3.mutate() : null;
            chipDrawable.closeIconRipple = new RippleDrawable(RippleUtils.sanitizeRippleDrawableColor(chipDrawable.rippleColor), chipDrawable.closeIcon, ChipDrawable.closeIconRippleMask);
            float calculateCloseIconWidth2 = chipDrawable.calculateCloseIconWidth();
            chipDrawable.unapplyChildDrawable(unwrap2);
            if (chipDrawable.showsCloseIcon()) {
                chipDrawable.applyChildDrawable(chipDrawable.closeIcon);
            }
            chipDrawable.invalidateSelf();
            if (calculateCloseIconWidth != calculateCloseIconWidth2) {
                chipDrawable.onSizeChange();
            }
        }
        ColorStateList colorStateList7 = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 30);
        if (chipDrawable.closeIconTint != colorStateList7) {
            chipDrawable.closeIconTint = colorStateList7;
            if (chipDrawable.showsCloseIcon()) {
                chipDrawable.closeIcon.setTintList(colorStateList7);
            }
            chipDrawable.onStateChange(chipDrawable.getState());
        }
        float dimension5 = obtainStyledAttributes.getDimension(28, 0.0f);
        if (chipDrawable.closeIconSize != dimension5) {
            chipDrawable.closeIconSize = dimension5;
            chipDrawable.invalidateSelf();
            if (chipDrawable.showsCloseIcon()) {
                chipDrawable.onSizeChange();
            }
        }
        boolean z = obtainStyledAttributes.getBoolean(6, false);
        if (chipDrawable.checkable != z) {
            chipDrawable.checkable = z;
            float calculateChipIconWidth5 = chipDrawable.calculateChipIconWidth();
            if (!z && chipDrawable.currentChecked) {
                chipDrawable.currentChecked = false;
            }
            float calculateChipIconWidth6 = chipDrawable.calculateChipIconWidth();
            chipDrawable.invalidateSelf();
            if (calculateChipIconWidth5 != calculateChipIconWidth6) {
                chipDrawable.onSizeChange();
            }
        }
        chipDrawable.setCheckedIconVisible(obtainStyledAttributes.getBoolean(10, false));
        if (!(attributeSet == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconEnabled") == null || attributeSet.getAttributeValue("http://schemas.android.com/apk/res-auto", "checkedIconVisible") != null)) {
            chipDrawable.setCheckedIconVisible(obtainStyledAttributes.getBoolean(8, false));
        }
        Drawable drawable5 = MaterialResources.getDrawable(chipDrawable.context, obtainStyledAttributes, 7);
        if (chipDrawable.checkedIcon != drawable5) {
            float calculateChipIconWidth7 = chipDrawable.calculateChipIconWidth();
            chipDrawable.checkedIcon = drawable5;
            float calculateChipIconWidth8 = chipDrawable.calculateChipIconWidth();
            chipDrawable.unapplyChildDrawable(chipDrawable.checkedIcon);
            chipDrawable.applyChildDrawable(chipDrawable.checkedIcon);
            chipDrawable.invalidateSelf();
            if (calculateChipIconWidth7 != calculateChipIconWidth8) {
                chipDrawable.onSizeChange();
            }
        }
        if (obtainStyledAttributes.hasValue(9) && chipDrawable.checkedIconTint != (colorStateList = MaterialResources.getColorStateList(chipDrawable.context, obtainStyledAttributes, 9))) {
            chipDrawable.checkedIconTint = colorStateList;
            if (chipDrawable.checkedIconVisible && chipDrawable.checkedIcon != null && chipDrawable.checkable) {
                chipDrawable.checkedIcon.setTintList(colorStateList);
            }
            chipDrawable.onStateChange(chipDrawable.getState());
        }
        MotionSpec.createFromAttribute(chipDrawable.context, obtainStyledAttributes, 39);
        MotionSpec.createFromAttribute(chipDrawable.context, obtainStyledAttributes, 33);
        float dimension6 = obtainStyledAttributes.getDimension(21, 0.0f);
        if (chipDrawable.chipStartPadding != dimension6) {
            chipDrawable.chipStartPadding = dimension6;
            chipDrawable.invalidateSelf();
            chipDrawable.onSizeChange();
        }
        float dimension7 = obtainStyledAttributes.getDimension(35, 0.0f);
        if (chipDrawable.iconStartPadding != dimension7) {
            float calculateChipIconWidth9 = chipDrawable.calculateChipIconWidth();
            chipDrawable.iconStartPadding = dimension7;
            float calculateChipIconWidth10 = chipDrawable.calculateChipIconWidth();
            chipDrawable.invalidateSelf();
            if (calculateChipIconWidth9 != calculateChipIconWidth10) {
                chipDrawable.onSizeChange();
            }
        }
        float dimension8 = obtainStyledAttributes.getDimension(34, 0.0f);
        if (chipDrawable.iconEndPadding != dimension8) {
            float calculateChipIconWidth11 = chipDrawable.calculateChipIconWidth();
            chipDrawable.iconEndPadding = dimension8;
            float calculateChipIconWidth12 = chipDrawable.calculateChipIconWidth();
            chipDrawable.invalidateSelf();
            if (calculateChipIconWidth11 != calculateChipIconWidth12) {
                chipDrawable.onSizeChange();
            }
        }
        float dimension9 = obtainStyledAttributes.getDimension(41, 0.0f);
        if (chipDrawable.textStartPadding != dimension9) {
            chipDrawable.textStartPadding = dimension9;
            chipDrawable.invalidateSelf();
            chipDrawable.onSizeChange();
        }
        float dimension10 = obtainStyledAttributes.getDimension(40, 0.0f);
        if (chipDrawable.textEndPadding != dimension10) {
            chipDrawable.textEndPadding = dimension10;
            chipDrawable.invalidateSelf();
            chipDrawable.onSizeChange();
        }
        float dimension11 = obtainStyledAttributes.getDimension(29, 0.0f);
        if (chipDrawable.closeIconStartPadding != dimension11) {
            chipDrawable.closeIconStartPadding = dimension11;
            chipDrawable.invalidateSelf();
            if (chipDrawable.showsCloseIcon()) {
                chipDrawable.onSizeChange();
            }
        }
        float dimension12 = obtainStyledAttributes.getDimension(27, 0.0f);
        if (chipDrawable.closeIconEndPadding != dimension12) {
            chipDrawable.closeIconEndPadding = dimension12;
            chipDrawable.invalidateSelf();
            if (chipDrawable.showsCloseIcon()) {
                chipDrawable.onSizeChange();
            }
        }
        float dimension13 = obtainStyledAttributes.getDimension(13, 0.0f);
        if (chipDrawable.chipEndPadding != dimension13) {
            chipDrawable.chipEndPadding = dimension13;
            chipDrawable.invalidateSelf();
            chipDrawable.onSizeChange();
        }
        chipDrawable.maxWidth = obtainStyledAttributes.getDimensionPixelSize(4, Integer.MAX_VALUE);
        obtainStyledAttributes.recycle();
        ThemeEnforcement.checkCompatibleTheme(context2, attributeSet, i, 2131886806);
        ThemeEnforcement.checkTextAppearance(context2, attributeSet, iArr, i, 2131886806, new int[0]);
        TypedArray obtainStyledAttributes2 = context2.obtainStyledAttributes(attributeSet, iArr, i, 2131886806);
        this.ensureMinTouchTargetSize = obtainStyledAttributes2.getBoolean(32, false);
        this.minTouchTargetSize = (int) Math.ceil((double) obtainStyledAttributes2.getDimension(20, (float) Math.ceil((double) ViewUtils.dpToPx(getContext(), 48))));
        obtainStyledAttributes2.recycle();
        ChipDrawable chipDrawable2 = this.chipDrawable;
        if (chipDrawable2 != chipDrawable) {
            if (chipDrawable2 != null) {
                chipDrawable2.delegate = new WeakReference<>(null);
            }
            this.chipDrawable = chipDrawable;
            chipDrawable.shouldDrawText = false;
            chipDrawable.delegate = new WeakReference<>(this);
            ensureAccessibleTouchTarget(this.minTouchTargetSize);
        }
        WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
        chipDrawable.setElevation(getElevation());
        ThemeEnforcement.checkCompatibleTheme(context2, attributeSet, i, 2131886806);
        ThemeEnforcement.checkTextAppearance(context2, attributeSet, iArr, i, 2131886806, new int[0]);
        TypedArray obtainStyledAttributes3 = context2.obtainStyledAttributes(attributeSet, iArr, i, 2131886806);
        boolean hasValue = obtainStyledAttributes3.hasValue(37);
        obtainStyledAttributes3.recycle();
        this.touchHelper = new ChipTouchHelper(this);
        hasCloseIcon();
        ViewCompat.setAccessibilityDelegate(this, null);
        if (!hasValue) {
            setOutlineProvider(new ViewOutlineProvider() { // from class: com.google.android.material.chip.Chip.2
                @Override // android.view.ViewOutlineProvider
                @TargetApi(21)
                public void getOutline(View view, Outline outline) {
                    ChipDrawable chipDrawable3 = Chip.this.chipDrawable;
                    if (chipDrawable3 != null) {
                        chipDrawable3.getOutline(outline);
                    } else {
                        outline.setAlpha(0.0f);
                    }
                }
            });
        }
        setChecked(this.deferredCheckedValue);
        setText(chipDrawable.text);
        setEllipsize(chipDrawable.truncateAt);
        updateTextPaintDrawState();
        if (!this.chipDrawable.shouldDrawText) {
            setLines(1);
            setHorizontallyScrolling(true);
        }
        setGravity(8388627);
        updatePaddingInternal();
        if (this.ensureMinTouchTargetSize) {
            setMinHeight(this.minTouchTargetSize);
        }
        this.lastLayoutDirection = getLayoutDirection();
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set start drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set end drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setCompoundDrawablesWithIntrinsicBounds(Drawable drawable, Drawable drawable2, Drawable drawable3, Drawable drawable4) {
        if (drawable != null) {
            throw new UnsupportedOperationException("Please set left drawable using R.attr#chipIcon.");
        } else if (drawable3 == null) {
            super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        } else {
            throw new UnsupportedOperationException("Please set right drawable using R.attr#closeIcon.");
        }
    }

    @Override // android.widget.TextView
    public void setTextAppearance(int i) {
        super.setTextAppearance(i);
        ChipDrawable chipDrawable = this.chipDrawable;
        if (chipDrawable != null) {
            chipDrawable.setTextAppearance(new TextAppearance(chipDrawable.context, i));
        }
        updateTextPaintDrawState();
    }
}
