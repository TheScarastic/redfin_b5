package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.CompoundButton;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.core.widget.CompoundButtonCompat;
/* access modifiers changed from: package-private */
/* loaded from: classes.dex */
public class AppCompatCompoundButtonHelper {
    private ColorStateList mButtonTintList = null;
    private PorterDuff.Mode mButtonTintMode = null;
    private boolean mHasButtonTint = false;
    private boolean mHasButtonTintMode = false;
    private boolean mSkipNextApply;
    private final CompoundButton mView;

    /* access modifiers changed from: package-private */
    public AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }

    /* access modifiers changed from: package-private */
    /* JADX WARNING: Removed duplicated region for block: B:11:0x003d A[Catch: all -> 0x0084, TRY_ENTER, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:7:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:30:0x001d }] */
    /* JADX WARNING: Removed duplicated region for block: B:18:0x0060 A[Catch: all -> 0x0084, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:7:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:30:0x001d }] */
    /* JADX WARNING: Removed duplicated region for block: B:21:0x0071 A[Catch: all -> 0x0084, TRY_LEAVE, TryCatch #1 {all -> 0x0084, blocks: (B:3:0x001d, B:5:0x0025, B:7:0x002b, B:11:0x003d, B:13:0x0045, B:15:0x004b, B:16:0x0058, B:18:0x0060, B:19:0x0069, B:21:0x0071), top: B:30:0x001d }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void loadFromAttributes(android.util.AttributeSet r10, int r11) {
        /*
            r9 = this;
            android.widget.CompoundButton r0 = r9.mView
            android.content.Context r0 = r0.getContext()
            int[] r3 = androidx.appcompat.R$styleable.CompoundButton
            r8 = 0
            androidx.appcompat.widget.TintTypedArray r0 = androidx.appcompat.widget.TintTypedArray.obtainStyledAttributes(r0, r10, r3, r11, r8)
            android.widget.CompoundButton r1 = r9.mView
            android.content.Context r2 = r1.getContext()
            android.content.res.TypedArray r5 = r0.getWrappedTypeArray()
            r7 = 0
            r4 = r10
            r6 = r11
            androidx.core.view.ViewCompat.saveAttributeDataForStyleable(r1, r2, r3, r4, r5, r6, r7)
            int r10 = androidx.appcompat.R$styleable.CompoundButton_buttonCompat     // Catch: all -> 0x0084
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0084
            if (r11 == 0) goto L_0x003a
            int r10 = r0.getResourceId(r10, r8)     // Catch: all -> 0x0084
            if (r10 == 0) goto L_0x003a
            android.widget.CompoundButton r11 = r9.mView     // Catch: NotFoundException -> 0x003a, all -> 0x0084
            android.content.Context r1 = r11.getContext()     // Catch: NotFoundException -> 0x003a, all -> 0x0084
            android.graphics.drawable.Drawable r10 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r10)     // Catch: NotFoundException -> 0x003a, all -> 0x0084
            r11.setButtonDrawable(r10)     // Catch: NotFoundException -> 0x003a, all -> 0x0084
            r10 = 1
            goto L_0x003b
        L_0x003a:
            r10 = r8
        L_0x003b:
            if (r10 != 0) goto L_0x0058
            int r10 = androidx.appcompat.R$styleable.CompoundButton_android_button     // Catch: all -> 0x0084
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0084
            if (r11 == 0) goto L_0x0058
            int r10 = r0.getResourceId(r10, r8)     // Catch: all -> 0x0084
            if (r10 == 0) goto L_0x0058
            android.widget.CompoundButton r11 = r9.mView     // Catch: all -> 0x0084
            android.content.Context r1 = r11.getContext()     // Catch: all -> 0x0084
            android.graphics.drawable.Drawable r10 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r10)     // Catch: all -> 0x0084
            r11.setButtonDrawable(r10)     // Catch: all -> 0x0084
        L_0x0058:
            int r10 = androidx.appcompat.R$styleable.CompoundButton_buttonTint     // Catch: all -> 0x0084
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0084
            if (r11 == 0) goto L_0x0069
            android.widget.CompoundButton r11 = r9.mView     // Catch: all -> 0x0084
            android.content.res.ColorStateList r10 = r0.getColorStateList(r10)     // Catch: all -> 0x0084
            androidx.core.widget.CompoundButtonCompat.setButtonTintList(r11, r10)     // Catch: all -> 0x0084
        L_0x0069:
            int r10 = androidx.appcompat.R$styleable.CompoundButton_buttonTintMode     // Catch: all -> 0x0084
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0084
            if (r11 == 0) goto L_0x0080
            android.widget.CompoundButton r9 = r9.mView     // Catch: all -> 0x0084
            r11 = -1
            int r10 = r0.getInt(r10, r11)     // Catch: all -> 0x0084
            r11 = 0
            android.graphics.PorterDuff$Mode r10 = androidx.appcompat.widget.DrawableUtils.parseTintMode(r10, r11)     // Catch: all -> 0x0084
            androidx.core.widget.CompoundButtonCompat.setButtonTintMode(r9, r10)     // Catch: all -> 0x0084
        L_0x0080:
            r0.recycle()
            return
        L_0x0084:
            r9 = move-exception
            r0.recycle()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCompoundButtonHelper.loadFromAttributes(android.util.AttributeSet, int):void");
    }

    /* access modifiers changed from: package-private */
    public void setSupportButtonTintList(ColorStateList colorStateList) {
        this.mButtonTintList = colorStateList;
        this.mHasButtonTint = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public ColorStateList getSupportButtonTintList() {
        return this.mButtonTintList;
    }

    /* access modifiers changed from: package-private */
    public void setSupportButtonTintMode(PorterDuff.Mode mode) {
        this.mButtonTintMode = mode;
        this.mHasButtonTintMode = true;
        applyButtonTint();
    }

    /* access modifiers changed from: package-private */
    public void onSetButtonDrawable() {
        if (this.mSkipNextApply) {
            this.mSkipNextApply = false;
            return;
        }
        this.mSkipNextApply = true;
        applyButtonTint();
    }

    void applyButtonTint() {
        Drawable buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView);
        if (buttonDrawable == null) {
            return;
        }
        if (this.mHasButtonTint || this.mHasButtonTintMode) {
            Drawable mutate = DrawableCompat.wrap(buttonDrawable).mutate();
            if (this.mHasButtonTint) {
                DrawableCompat.setTintList(mutate, this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                DrawableCompat.setTintMode(mutate, this.mButtonTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(mutate);
        }
    }

    /* access modifiers changed from: package-private */
    public int getCompoundPaddingLeft(int i) {
        Drawable buttonDrawable;
        return (Build.VERSION.SDK_INT >= 17 || (buttonDrawable = CompoundButtonCompat.getButtonDrawable(this.mView)) == null) ? i : i + buttonDrawable.getIntrinsicWidth();
    }
}
