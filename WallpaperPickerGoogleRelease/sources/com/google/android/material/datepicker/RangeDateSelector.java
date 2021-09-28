package com.google.android.material.datepicker;

import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import androidx.coordinatorlayout.R$style;
import androidx.core.util.Pair;
import com.android.systemui.shared.R;
import com.google.android.material.resources.MaterialAttributes;
import com.google.android.material.textfield.TextInputLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Locale;
/* loaded from: classes.dex */
public class RangeDateSelector implements DateSelector<Pair<Long, Long>> {
    public static final Parcelable.Creator<RangeDateSelector> CREATOR = new Parcelable.Creator<RangeDateSelector>() { // from class: com.google.android.material.datepicker.RangeDateSelector.3
        /* Return type fixed from 'java.lang.Object' to match base method */
        @Override // android.os.Parcelable.Creator
        public RangeDateSelector createFromParcel(Parcel parcel) {
            RangeDateSelector rangeDateSelector = new RangeDateSelector();
            rangeDateSelector.selectedStartItem = (Long) parcel.readValue(Long.class.getClassLoader());
            rangeDateSelector.selectedEndItem = (Long) parcel.readValue(Long.class.getClassLoader());
            return rangeDateSelector;
        }

        /* Return type fixed from 'java.lang.Object[]' to match base method */
        @Override // android.os.Parcelable.Creator
        public RangeDateSelector[] newArray(int i) {
            return new RangeDateSelector[i];
        }
    };
    public String invalidRangeStartError;
    public Long selectedStartItem = null;
    public Long selectedEndItem = null;
    public Long proposedTextStart = null;
    public Long proposedTextEnd = null;

    public static void access$100(RangeDateSelector rangeDateSelector, TextInputLayout textInputLayout, TextInputLayout textInputLayout2, OnSelectionChangedListener onSelectionChangedListener) {
        Long l = rangeDateSelector.proposedTextStart;
        if (l == null || rangeDateSelector.proposedTextEnd == null) {
            if (textInputLayout.getError() != null && rangeDateSelector.invalidRangeStartError.contentEquals(textInputLayout.getError())) {
                textInputLayout.setError(null);
            }
            if (textInputLayout2.getError() != null && " ".contentEquals(textInputLayout2.getError())) {
                textInputLayout2.setError(null);
            }
            onSelectionChangedListener.onIncompleteSelectionChanged();
        } else if (rangeDateSelector.isValidRange(l.longValue(), rangeDateSelector.proposedTextEnd.longValue())) {
            Long l2 = rangeDateSelector.proposedTextStart;
            rangeDateSelector.selectedStartItem = l2;
            Long l3 = rangeDateSelector.proposedTextEnd;
            rangeDateSelector.selectedEndItem = l3;
            onSelectionChangedListener.onSelectionChanged(new Pair(l2, l3));
        } else {
            textInputLayout.setError(rangeDateSelector.invalidRangeStartError);
            textInputLayout2.setError(" ");
            onSelectionChangedListener.onIncompleteSelectionChanged();
        }
    }

    @Override // android.os.Parcelable
    public int describeContents() {
        return 0;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public int getDefaultThemeResId(Context context) {
        Resources resources = context.getResources();
        DisplayMetrics displayMetrics = resources.getDisplayMetrics();
        return MaterialAttributes.resolveOrThrow(context, Math.min(displayMetrics.widthPixels, displayMetrics.heightPixels) > resources.getDimensionPixelSize(R.dimen.mtrl_calendar_maximum_default_fullscreen_minor_axis) ? R.attr.materialCalendarTheme : R.attr.materialCalendarFullscreenTheme, MaterialDatePicker.class.getCanonicalName());
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Long> getSelectedDays() {
        ArrayList arrayList = new ArrayList();
        Long l = this.selectedStartItem;
        if (l != null) {
            arrayList.add(l);
        }
        Long l2 = this.selectedEndItem;
        if (l2 != null) {
            arrayList.add(l2);
        }
        return arrayList;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public Collection<Pair<Long, Long>> getSelectedRanges() {
        if (this.selectedStartItem == null || this.selectedEndItem == null) {
            return new ArrayList();
        }
        ArrayList arrayList = new ArrayList();
        arrayList.add(new Pair(this.selectedStartItem, this.selectedEndItem));
        return arrayList;
    }

    /* Return type fixed from 'java.lang.Object' to match base method */
    @Override // com.google.android.material.datepicker.DateSelector
    public Pair<Long, Long> getSelection() {
        return new Pair<>(this.selectedStartItem, this.selectedEndItem);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public String getSelectionDisplayString(Context context) {
        Pair pair;
        Pair pair2;
        Pair pair3;
        Resources resources = context.getResources();
        Long l = this.selectedStartItem;
        if (l == null && this.selectedEndItem == null) {
            return resources.getString(R.string.mtrl_picker_range_header_unselected);
        }
        Long l2 = this.selectedEndItem;
        if (l2 == null) {
            return resources.getString(R.string.mtrl_picker_range_header_only_start_selected, DateStrings.getDateString(l.longValue()));
        }
        if (l == null) {
            return resources.getString(R.string.mtrl_picker_range_header_only_end_selected, DateStrings.getDateString(l2.longValue()));
        }
        if (l == null && l2 == null) {
            pair = new Pair(null, null);
        } else {
            if (l == null) {
                pair3 = new Pair(null, DateStrings.getDateString(l2.longValue(), null));
            } else if (l2 == null) {
                pair3 = new Pair(DateStrings.getDateString(l.longValue(), null), null);
            } else {
                Calendar todayCalendar = UtcDates.getTodayCalendar();
                Calendar utcCalendar = UtcDates.getUtcCalendar();
                utcCalendar.setTimeInMillis(l.longValue());
                Calendar utcCalendar2 = UtcDates.getUtcCalendar();
                utcCalendar2.setTimeInMillis(l2.longValue());
                if (utcCalendar.get(1) != utcCalendar2.get(1)) {
                    pair2 = new Pair(DateStrings.getYearMonthDay(l.longValue(), Locale.getDefault()), DateStrings.getYearMonthDay(l2.longValue(), Locale.getDefault()));
                } else if (utcCalendar.get(1) == todayCalendar.get(1)) {
                    pair2 = new Pair(DateStrings.getMonthDay(l.longValue(), Locale.getDefault()), DateStrings.getMonthDay(l2.longValue(), Locale.getDefault()));
                } else {
                    pair2 = new Pair(DateStrings.getMonthDay(l.longValue(), Locale.getDefault()), DateStrings.getYearMonthDay(l2.longValue(), Locale.getDefault()));
                }
                pair = pair2;
            }
            pair = pair3;
        }
        return resources.getString(R.string.mtrl_picker_range_header_selected, pair.first, pair.second);
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public boolean isSelectionComplete() {
        Long l = this.selectedStartItem;
        return (l == null || this.selectedEndItem == null || !isValidRange(l.longValue(), this.selectedEndItem.longValue())) ? false : true;
    }

    public final boolean isValidRange(long j, long j2) {
        return j <= j2;
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public View onCreateTextInputView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle, CalendarConstraints calendarConstraints, final OnSelectionChangedListener<Pair<Long, Long>> onSelectionChangedListener) {
        View inflate = layoutInflater.inflate(R.layout.mtrl_picker_text_input_date_range, viewGroup, false);
        final TextInputLayout textInputLayout = (TextInputLayout) inflate.findViewById(R.id.mtrl_picker_text_input_range_start);
        final TextInputLayout textInputLayout2 = (TextInputLayout) inflate.findViewById(R.id.mtrl_picker_text_input_range_end);
        EditText editText = textInputLayout.editText;
        EditText editText2 = textInputLayout2.editText;
        if (R$style.isDateInputKeyboardMissingSeparatorCharacters()) {
            editText.setInputType(17);
            editText2.setInputType(17);
        }
        this.invalidRangeStartError = inflate.getResources().getString(R.string.mtrl_picker_invalid_range);
        SimpleDateFormat textInputFormat = UtcDates.getTextInputFormat();
        Long l = this.selectedStartItem;
        if (l != null) {
            editText.setText(textInputFormat.format(l));
            this.proposedTextStart = this.selectedStartItem;
        }
        Long l2 = this.selectedEndItem;
        if (l2 != null) {
            editText2.setText(textInputFormat.format(l2));
            this.proposedTextEnd = this.selectedEndItem;
        }
        String textInputHint = UtcDates.getTextInputHint(inflate.getResources(), textInputFormat);
        textInputLayout.setPlaceholderText(textInputHint);
        textInputLayout2.setPlaceholderText(textInputHint);
        editText.addTextChangedListener(new DateFormatTextWatcher(textInputHint, textInputFormat, calendarConstraints, textInputLayout) { // from class: com.google.android.material.datepicker.RangeDateSelector.1
            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public void onInvalidDate() {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextStart = null;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, onSelectionChangedListener);
            }

            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public void onValidDate(Long l3) {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextStart = l3;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, onSelectionChangedListener);
            }
        });
        editText2.addTextChangedListener(new DateFormatTextWatcher(textInputHint, textInputFormat, calendarConstraints, textInputLayout2) { // from class: com.google.android.material.datepicker.RangeDateSelector.2
            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public void onInvalidDate() {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextEnd = null;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, onSelectionChangedListener);
            }

            @Override // com.google.android.material.datepicker.DateFormatTextWatcher
            public void onValidDate(Long l3) {
                RangeDateSelector rangeDateSelector = RangeDateSelector.this;
                rangeDateSelector.proposedTextEnd = l3;
                RangeDateSelector.access$100(rangeDateSelector, textInputLayout, textInputLayout2, onSelectionChangedListener);
            }
        });
        editText.requestFocus();
        editText.post(
        /*  JADX ERROR: Method code generation error
            jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x009c: INVOKE  
              (r13v0 'editText' android.widget.EditText)
              (wrap: com.google.android.material.internal.ViewUtils$1 : 0x0099: CONSTRUCTOR  (r0v13 com.google.android.material.internal.ViewUtils$1 A[REMOVE]) = (r13v0 'editText' android.widget.EditText) call: com.google.android.material.internal.ViewUtils.1.<init>(android.view.View):void type: CONSTRUCTOR)
             type: VIRTUAL call: android.view.View.post(java.lang.Runnable):boolean in method: com.google.android.material.datepicker.RangeDateSelector.onCreateTextInputView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle, com.google.android.material.datepicker.CalendarConstraints, com.google.android.material.datepicker.OnSelectionChangedListener<androidx.core.util.Pair<java.lang.Long, java.lang.Long>>):android.view.View, file: classes.dex
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:285)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:248)
            	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:105)
            	at jadx.core.dex.nodes.IBlock.generate(IBlock.java:15)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
            	at jadx.core.dex.regions.Region.generate(Region.java:35)
            	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:65)
            	at jadx.core.codegen.MethodGen.addRegionInsns(MethodGen.java:261)
            	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:254)
            	at jadx.core.codegen.ClassGen.addMethodCode(ClassGen.java:349)
            	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:302)
            	at jadx.core.codegen.ClassGen.lambda$addInnerClsAndMethods$2(ClassGen.java:271)
            	at java.base/java.util.stream.ForEachOps$ForEachOp$OfRef.accept(ForEachOps.java:183)
            	at java.base/java.util.ArrayList.forEach(ArrayList.java:1541)
            	at java.base/java.util.stream.SortedOps$RefSortingSink.end(SortedOps.java:395)
            	at java.base/java.util.stream.Sink$ChainedReference.end(Sink.java:258)
            Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: com.google.android.material.internal.ViewUtils, state: GENERATED_AND_UNLOADED
            	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
            	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
            	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
            	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
            	at jadx.core.codegen.InsnGen.generateMethodArguments(InsnGen.java:976)
            	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:801)
            	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
            	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
            	... 15 more
            */
        /*
            this = this;
            r9 = r17
            r0 = 2131558558(0x7f0d009e, float:1.8742435E38)
            r1 = 0
            r2 = r18
            r3 = r19
            android.view.View r10 = r2.inflate(r0, r3, r1)
            r0 = 2131362161(0x7f0a0171, float:1.8344095E38)
            android.view.View r0 = r10.findViewById(r0)
            r11 = r0
            com.google.android.material.textfield.TextInputLayout r11 = (com.google.android.material.textfield.TextInputLayout) r11
            r0 = 2131362160(0x7f0a0170, float:1.8344093E38)
            android.view.View r0 = r10.findViewById(r0)
            r12 = r0
            com.google.android.material.textfield.TextInputLayout r12 = (com.google.android.material.textfield.TextInputLayout) r12
            android.widget.EditText r13 = r11.editText
            android.widget.EditText r14 = r12.editText
            boolean r0 = androidx.coordinatorlayout.R$style.isDateInputKeyboardMissingSeparatorCharacters()
            if (r0 == 0) goto L_0x0034
            r0 = 17
            r13.setInputType(r0)
            r14.setInputType(r0)
        L_0x0034:
            android.content.res.Resources r0 = r10.getResources()
            r1 = 2131820745(0x7f1100c9, float:1.9274214E38)
            java.lang.String r0 = r0.getString(r1)
            r9.invalidRangeStartError = r0
            java.text.SimpleDateFormat r15 = com.google.android.material.datepicker.UtcDates.getTextInputFormat()
            java.lang.Long r0 = r9.selectedStartItem
            if (r0 == 0) goto L_0x0054
            java.lang.String r0 = r15.format(r0)
            r13.setText(r0)
            java.lang.Long r0 = r9.selectedStartItem
            r9.proposedTextStart = r0
        L_0x0054:
            java.lang.Long r0 = r9.selectedEndItem
            if (r0 == 0) goto L_0x0063
            java.lang.String r0 = r15.format(r0)
            r14.setText(r0)
            java.lang.Long r0 = r9.selectedEndItem
            r9.proposedTextEnd = r0
        L_0x0063:
            android.content.res.Resources r0 = r10.getResources()
            java.lang.String r8 = com.google.android.material.datepicker.UtcDates.getTextInputHint(r0, r15)
            r11.setPlaceholderText(r8)
            r12.setPlaceholderText(r8)
            com.google.android.material.datepicker.RangeDateSelector$1 r7 = new com.google.android.material.datepicker.RangeDateSelector$1
            r0 = r7
            r1 = r17
            r2 = r8
            r3 = r15
            r4 = r11
            r5 = r21
            r6 = r11
            r9 = r7
            r7 = r12
            r16 = r8
            r8 = r22
            r0.<init>(r2, r3, r4, r5, r6, r7, r8)
            r13.addTextChangedListener(r9)
            com.google.android.material.datepicker.RangeDateSelector$2 r9 = new com.google.android.material.datepicker.RangeDateSelector$2
            r0 = r9
            r2 = r16
            r4 = r12
            r0.<init>(r2, r3, r4, r5, r6, r7, r8)
            r14.addTextChangedListener(r9)
            r13.requestFocus()
            com.google.android.material.internal.ViewUtils$1 r0 = new com.google.android.material.internal.ViewUtils$1
            r0.<init>(r13)
            r13.post(r0)
            return r10
        */
        throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.RangeDateSelector.onCreateTextInputView(android.view.LayoutInflater, android.view.ViewGroup, android.os.Bundle, com.google.android.material.datepicker.CalendarConstraints, com.google.android.material.datepicker.OnSelectionChangedListener):android.view.View");
    }

    @Override // com.google.android.material.datepicker.DateSelector
    public void select(long j) {
        Long l = this.selectedStartItem;
        if (l == null) {
            this.selectedStartItem = Long.valueOf(j);
        } else if (this.selectedEndItem != null || !isValidRange(l.longValue(), j)) {
            this.selectedEndItem = null;
            this.selectedStartItem = Long.valueOf(j);
        } else {
            this.selectedEndItem = Long.valueOf(j);
        }
    }

    @Override // android.os.Parcelable
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeValue(this.selectedStartItem);
        parcel.writeValue(this.selectedEndItem);
    }
}
