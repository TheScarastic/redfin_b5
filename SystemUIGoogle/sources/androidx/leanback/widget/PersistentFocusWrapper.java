package androidx.leanback.widget;

import android.content.Context;
import android.graphics.Rect;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import java.util.ArrayList;
/* loaded from: classes.dex */
class PersistentFocusWrapper extends FrameLayout {
    private int mSelectedPosition = -1;
    private boolean mPersistFocusVertical = true;

    public PersistentFocusWrapper(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    int getGrandChildCount() {
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null) {
            return 0;
        }
        return viewGroup.getChildCount();
    }

    private boolean shouldPersistFocusFromDirection(int i) {
        boolean z = this.mPersistFocusVertical;
        return (z && (i == 33 || i == 130)) || (!z && (i == 17 || i == 66));
    }

    @Override // android.view.View, android.view.ViewGroup
    public void addFocusables(ArrayList<View> arrayList, int i, int i2) {
        if (hasFocus() || getGrandChildCount() == 0 || !shouldPersistFocusFromDirection(i)) {
            super.addFocusables(arrayList, i, i2);
        } else {
            arrayList.add(this);
        }
    }

    @Override // android.view.ViewParent, android.view.ViewGroup
    public void requestChildFocus(View view, View view2) {
        int i;
        super.requestChildFocus(view, view2);
        while (view2 != null && view2.getParent() != view) {
            view2 = (View) view2.getParent();
        }
        if (view2 == null) {
            i = -1;
        } else {
            i = ((ViewGroup) view).indexOfChild(view2);
        }
        this.mSelectedPosition = i;
    }

    @Override // android.view.View, android.view.ViewGroup
    public boolean requestFocus(int i, Rect rect) {
        int i2;
        ViewGroup viewGroup = (ViewGroup) getChildAt(0);
        if (viewGroup == null || (i2 = this.mSelectedPosition) < 0 || i2 >= getGrandChildCount() || !viewGroup.getChildAt(this.mSelectedPosition).requestFocus(i, rect)) {
            return super.requestFocus(i, rect);
        }
        return true;
    }

    /* access modifiers changed from: package-private */
    /* loaded from: classes.dex */
    public static class SavedState extends View.BaseSavedState {
        public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() { // from class: androidx.leanback.widget.PersistentFocusWrapper.SavedState.1
            @Override // android.os.Parcelable.Creator
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            @Override // android.os.Parcelable.Creator
            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        int mSelectedPosition;

        SavedState(Parcel parcel) {
            super(parcel);
            this.mSelectedPosition = parcel.readInt();
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        @Override // android.view.View.BaseSavedState, android.os.Parcelable, android.view.AbsSavedState
        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mSelectedPosition);
        }
    }

    @Override // android.view.View
    protected Parcelable onSaveInstanceState() {
        SavedState savedState = new SavedState(super.onSaveInstanceState());
        savedState.mSelectedPosition = this.mSelectedPosition;
        return savedState;
    }

    @Override // android.view.View
    protected void onRestoreInstanceState(Parcelable parcelable) {
        if (!(parcelable instanceof SavedState)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        this.mSelectedPosition = savedState.mSelectedPosition;
        super.onRestoreInstanceState(savedState.getSuperState());
    }
}
