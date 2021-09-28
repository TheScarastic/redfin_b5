package com.google.android.material.slider;

import android.os.Parcel;
import android.os.Parcelable;
import android.view.View;
import androidx.savedstate.R$id;
import java.util.ArrayList;
/* loaded from: classes.dex */
public class BaseSlider$SliderState extends View.BaseSavedState {
    public static final Parcelable.Creator<BaseSlider$SliderState> CREATOR = new Parcelable.Creator<BaseSlider$SliderState>() { // from class: com.google.android.material.slider.BaseSlider$SliderState.1
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public BaseSlider$SliderState createFromParcel(Parcel parcel) {
            return new BaseSlider$SliderState(parcel, null);
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public BaseSlider$SliderState[] newArray(int i) {
            return new BaseSlider$SliderState[i];
        }
    };
    public boolean hasFocus;
    public float stepSize;
    public float valueFrom;
    public float valueTo;
    public ArrayList<Float> values;

    public BaseSlider$SliderState(Parcel parcel, R$id r$id) {
        super(parcel);
        this.valueFrom = parcel.readFloat();
        this.valueTo = parcel.readFloat();
        ArrayList<Float> arrayList = new ArrayList<>();
        this.values = arrayList;
        parcel.readList(arrayList, Float.class.getClassLoader());
        this.stepSize = parcel.readFloat();
        this.hasFocus = parcel.createBooleanArray()[0];
    }

    @Override // android.view.View.BaseSavedState, android.os.Parcelable, android.view.AbsSavedState
    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeFloat(this.valueFrom);
        parcel.writeFloat(this.valueTo);
        parcel.writeList(this.values);
        parcel.writeFloat(this.stepSize);
        parcel.writeBooleanArray(new boolean[]{this.hasFocus});
    }
}
