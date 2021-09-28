package androidx.slice.builders;

import android.app.PendingIntent;
import androidx.core.graphics.drawable.IconCompat;
import androidx.slice.Slice;
import androidx.slice.core.SliceActionImpl;
/* loaded from: classes.dex */
public class SliceAction implements androidx.slice.core.SliceAction {
    private final SliceActionImpl mSliceAction;

    public SliceAction(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        this.mSliceAction = new SliceActionImpl(pendingIntent, iconCompat, i, charSequence);
    }

    public static SliceAction create(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        return new SliceAction(pendingIntent, iconCompat, i, charSequence);
    }

    public static SliceAction createDeeplink(PendingIntent pendingIntent, IconCompat iconCompat, int i, CharSequence charSequence) {
        SliceAction sliceAction = new SliceAction(pendingIntent, iconCompat, i, charSequence);
        sliceAction.mSliceAction.setActivity(true);
        return sliceAction;
    }

    @Override // androidx.slice.core.SliceAction
    public IconCompat getIcon() {
        return this.mSliceAction.getIcon();
    }

    public CharSequence getTitle() {
        return this.mSliceAction.getTitle();
    }

    @Override // androidx.slice.core.SliceAction
    public int getPriority() {
        return this.mSliceAction.getPriority();
    }

    @Override // androidx.slice.core.SliceAction
    public boolean isToggle() {
        return this.mSliceAction.isToggle();
    }

    @Override // androidx.slice.core.SliceAction
    public int getImageMode() {
        return this.mSliceAction.getImageMode();
    }

    public Slice buildSlice(Slice.Builder builder) {
        return this.mSliceAction.buildSlice(builder);
    }

    public void setPrimaryAction(Slice.Builder builder) {
        builder.addAction(this.mSliceAction.getAction(), this.mSliceAction.buildPrimaryActionSlice(builder), this.mSliceAction.getSubtype());
    }
}
