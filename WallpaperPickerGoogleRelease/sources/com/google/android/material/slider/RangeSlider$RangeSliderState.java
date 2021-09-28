package com.google.android.material.slider;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.AbsSavedState;
import androidx.appcompat.R$dimen;
/* loaded from: classes.dex */
public class RangeSlider$RangeSliderState extends AbsSavedState {
    public static final Parcelable.Creator<RangeSlider$RangeSliderState> CREATOR = new Parcelable.Creator<RangeSlider$RangeSliderState>() { // from class: com.google.android.material.slider.RangeSlider$RangeSliderState.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public RangeSlider$RangeSliderState createFromParcel(Parcel parcel) {
            return new RangeSlider$RangeSliderState(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public RangeSlider$RangeSliderState[] newArray(int i) {
            return new RangeSlider$RangeSliderState[i];
        }
    };
    public float minSeparation;
    public int separationUnit;

    public RangeSlider$RangeSliderState(Parcel parcel, R$dimen r$dimen) {
        super(parcel.readParcelable(RangeSlider$RangeSliderState.class.getClassLoader()));
        this.minSeparation = parcel.readFloat();
        this.separationUnit = parcel.readInt();
    }

    @Override // android.view.AbsSavedState, android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeFloat(this.minSeparation);
        parcel.writeInt(this.separationUnit);
    }
}
