package androidx.appcompat.widget;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.CompoundButton;
/* loaded from: classes.dex */
public class AppCompatCompoundButtonHelper {
    public ColorStateList mButtonTintList = null;
    public PorterDuff.Mode mButtonTintMode = null;
    public boolean mHasButtonTint = false;
    public boolean mHasButtonTintMode = false;
    public boolean mSkipNextApply;
    public final CompoundButton mView;

    public AppCompatCompoundButtonHelper(CompoundButton compoundButton) {
        this.mView = compoundButton;
    }

    public void applyButtonTint() {
        Drawable buttonDrawable = this.mView.getButtonDrawable();
        if (buttonDrawable == null) {
            return;
        }
        if (this.mHasButtonTint || this.mHasButtonTintMode) {
            Drawable mutate = buttonDrawable.mutate();
            if (this.mHasButtonTint) {
                mutate.setTintList(this.mButtonTintList);
            }
            if (this.mHasButtonTintMode) {
                mutate.setTintMode(this.mButtonTintMode);
            }
            if (mutate.isStateful()) {
                mutate.setState(this.mView.getDrawableState());
            }
            this.mView.setButtonDrawable(mutate);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:17:0x005b A[Catch: all -> 0x0065, TryCatch #1 {all -> 0x0065, blocks: (B:3:0x001e, B:5:0x0024, B:7:0x002a, B:10:0x003b, B:12:0x0041, B:14:0x0047, B:15:0x0054, B:17:0x005b, B:20:0x0067, B:22:0x006e), top: B:30:0x001e }] */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x006e A[Catch: all -> 0x0065, TRY_LEAVE, TryCatch #1 {all -> 0x0065, blocks: (B:3:0x001e, B:5:0x0024, B:7:0x002a, B:10:0x003b, B:12:0x0041, B:14:0x0047, B:15:0x0054, B:17:0x005b, B:20:0x0067, B:22:0x006e), top: B:30:0x001e }] */
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
            android.content.res.TypedArray r5 = r0.mWrapped
            r7 = 0
            java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r4 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
            r4 = r10
            r6 = r11
            androidx.core.view.ViewCompat.Api29Impl.saveAttributeDataForStyleable(r1, r2, r3, r4, r5, r6, r7)
            r10 = 1
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0065
            if (r11 == 0) goto L_0x0038
            int r11 = r0.getResourceId(r10, r8)     // Catch: all -> 0x0065
            if (r11 == 0) goto L_0x0038
            android.widget.CompoundButton r1 = r9.mView     // Catch: NotFoundException -> 0x0038, all -> 0x0065
            android.content.Context r2 = r1.getContext()     // Catch: NotFoundException -> 0x0038, all -> 0x0065
            android.graphics.drawable.Drawable r11 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r2, r11)     // Catch: NotFoundException -> 0x0038, all -> 0x0065
            r1.setButtonDrawable(r11)     // Catch: NotFoundException -> 0x0038, all -> 0x0065
            goto L_0x0039
        L_0x0038:
            r10 = r8
        L_0x0039:
            if (r10 != 0) goto L_0x0054
            boolean r10 = r0.hasValue(r8)     // Catch: all -> 0x0065
            if (r10 == 0) goto L_0x0054
            int r10 = r0.getResourceId(r8, r8)     // Catch: all -> 0x0065
            if (r10 == 0) goto L_0x0054
            android.widget.CompoundButton r11 = r9.mView     // Catch: all -> 0x0065
            android.content.Context r1 = r11.getContext()     // Catch: all -> 0x0065
            android.graphics.drawable.Drawable r10 = androidx.appcompat.content.res.AppCompatResources.getDrawable(r1, r10)     // Catch: all -> 0x0065
            r11.setButtonDrawable(r10)     // Catch: all -> 0x0065
        L_0x0054:
            r10 = 2
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0065
            if (r11 == 0) goto L_0x0067
            android.widget.CompoundButton r11 = r9.mView     // Catch: all -> 0x0065
            android.content.res.ColorStateList r10 = r0.getColorStateList(r10)     // Catch: all -> 0x0065
            r11.setButtonTintList(r10)     // Catch: all -> 0x0065
            goto L_0x0067
        L_0x0065:
            r9 = move-exception
            goto L_0x0083
        L_0x0067:
            r10 = 3
            boolean r11 = r0.hasValue(r10)     // Catch: all -> 0x0065
            if (r11 == 0) goto L_0x007d
            android.widget.CompoundButton r9 = r9.mView     // Catch: all -> 0x0065
            r11 = -1
            int r10 = r0.getInt(r10, r11)     // Catch: all -> 0x0065
            r11 = 0
            android.graphics.PorterDuff$Mode r10 = androidx.appcompat.widget.DrawableUtils.parseTintMode(r10, r11)     // Catch: all -> 0x0065
            r9.setButtonTintMode(r10)     // Catch: all -> 0x0065
        L_0x007d:
            android.content.res.TypedArray r9 = r0.mWrapped
            r9.recycle()
            return
        L_0x0083:
            android.content.res.TypedArray r10 = r0.mWrapped
            r10.recycle()
            throw r9
        */
        throw new UnsupportedOperationException("Method not decompiled: androidx.appcompat.widget.AppCompatCompoundButtonHelper.loadFromAttributes(android.util.AttributeSet, int):void");
    }
}
