package com.google.android.material.datepicker;

import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.Iterator;
/* loaded from: classes2.dex */
public final class MaterialTextInputPicker<S> extends PickerFragment<S> {
    private CalendarConstraints calendarConstraints;
    private DateSelector<S> dateSelector;
    private int themeResId;

    /* access modifiers changed from: package-private */
    public static <T> MaterialTextInputPicker<T> newInstance(DateSelector<T> dateSelector, int i, CalendarConstraints calendarConstraints) {
        MaterialTextInputPicker<T> materialTextInputPicker = new MaterialTextInputPicker<>();
        Bundle bundle = new Bundle();
        bundle.putInt("THEME_RES_ID_KEY", i);
        bundle.putParcelable("DATE_SELECTOR_KEY", dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", calendarConstraints);
        materialTextInputPicker.setArguments(bundle);
        return materialTextInputPicker;
    }

    @Override // androidx.fragment.app.Fragment
    public void onSaveInstanceState(Bundle bundle) {
        super.onSaveInstanceState(bundle);
        bundle.putInt("THEME_RES_ID_KEY", this.themeResId);
        bundle.putParcelable("DATE_SELECTOR_KEY", this.dateSelector);
        bundle.putParcelable("CALENDAR_CONSTRAINTS_KEY", this.calendarConstraints);
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        if (bundle == null) {
            bundle = getArguments();
        }
        this.themeResId = bundle.getInt("THEME_RES_ID_KEY");
        this.dateSelector = (DateSelector) bundle.getParcelable("DATE_SELECTOR_KEY");
        this.calendarConstraints = (CalendarConstraints) bundle.getParcelable("CALENDAR_CONSTRAINTS_KEY");
    }

    @Override // androidx.fragment.app.Fragment
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return this.dateSelector.onCreateTextInputView(layoutInflater.cloneInContext(new ContextThemeWrapper(getContext(), this.themeResId)), viewGroup, bundle, this.calendarConstraints, new OnSelectionChangedListener<S>() { // from class: com.google.android.material.datepicker.MaterialTextInputPicker.1
            @Override // com.google.android.material.datepicker.OnSelectionChangedListener
            public void onSelectionChanged(S s) {
                Iterator<OnSelectionChangedListener<S>> it = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();
                while (it.hasNext()) {
                    it.next().onSelectionChanged(s);
                }
            }

            @Override // com.google.android.material.datepicker.OnSelectionChangedListener
            public void onIncompleteSelectionChanged() {
                Iterator<OnSelectionChangedListener<S>> it = MaterialTextInputPicker.this.onSelectionChangedListeners.iterator();
                while (it.hasNext()) {
                    it.next().onIncompleteSelectionChanged();
                }
            }
        });
    }
}
