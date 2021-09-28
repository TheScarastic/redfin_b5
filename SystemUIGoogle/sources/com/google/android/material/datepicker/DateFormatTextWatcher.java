package com.google.android.material.datepicker;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import com.google.android.material.R$string;
import com.google.android.material.internal.TextWatcherAdapter;
import com.google.android.material.textfield.TextInputLayout;
import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
/* loaded from: classes2.dex */
abstract class DateFormatTextWatcher extends TextWatcherAdapter {
    private final CalendarConstraints constraints;
    private final DateFormat dateFormat;
    private final String outOfRange;
    private final Runnable setErrorCallback;
    private Runnable setRangeErrorCallback;
    private final TextInputLayout textInputLayout;

    abstract void onInvalidDate();

    abstract void onValidDate(Long l);

    /* access modifiers changed from: package-private */
    public DateFormatTextWatcher(final String str, DateFormat dateFormat, TextInputLayout textInputLayout, CalendarConstraints calendarConstraints) {
        this.dateFormat = dateFormat;
        this.textInputLayout = textInputLayout;
        this.constraints = calendarConstraints;
        this.outOfRange = textInputLayout.getContext().getString(R$string.mtrl_picker_out_of_range);
        this.setErrorCallback = new Runnable() { // from class: com.google.android.material.datepicker.DateFormatTextWatcher.1
            @Override // java.lang.Runnable
            public void run() {
                TextInputLayout textInputLayout2 = DateFormatTextWatcher.this.textInputLayout;
                DateFormat dateFormat2 = DateFormatTextWatcher.this.dateFormat;
                Context context = textInputLayout2.getContext();
                String string = context.getString(R$string.mtrl_picker_invalid_format);
                String format = String.format(context.getString(R$string.mtrl_picker_invalid_format_use), str);
                String format2 = String.format(context.getString(R$string.mtrl_picker_invalid_format_example), dateFormat2.format(new Date(UtcDates.getTodayCalendar().getTimeInMillis())));
                textInputLayout2.setError(string + "\n" + format + "\n" + format2);
                DateFormatTextWatcher.this.onInvalidDate();
            }
        };
    }

    @Override // com.google.android.material.internal.TextWatcherAdapter, android.text.TextWatcher
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        this.textInputLayout.removeCallbacks(this.setErrorCallback);
        this.textInputLayout.removeCallbacks(this.setRangeErrorCallback);
        this.textInputLayout.setError(null);
        onValidDate(null);
        if (!TextUtils.isEmpty(charSequence)) {
            try {
                Date parse = this.dateFormat.parse(charSequence.toString());
                this.textInputLayout.setError(null);
                long time = parse.getTime();
                if (!this.constraints.getDateValidator().isValid(time) || !this.constraints.isWithinBounds(time)) {
                    Runnable createRangeErrorCallback = createRangeErrorCallback(time);
                    this.setRangeErrorCallback = createRangeErrorCallback;
                    runValidation(this.textInputLayout, createRangeErrorCallback);
                    return;
                }
                onValidDate(Long.valueOf(parse.getTime()));
            } catch (ParseException unused) {
                runValidation(this.textInputLayout, this.setErrorCallback);
            }
        }
    }

    private Runnable createRangeErrorCallback(final long j) {
        return new Runnable() { // from class: com.google.android.material.datepicker.DateFormatTextWatcher.2
            @Override // java.lang.Runnable
            public void run() {
                DateFormatTextWatcher.this.textInputLayout.setError(String.format(DateFormatTextWatcher.this.outOfRange, DateStrings.getDateString(j)));
                DateFormatTextWatcher.this.onInvalidDate();
            }
        };
    }

    public void runValidation(View view, Runnable runnable) {
        view.postDelayed(runnable, 1000);
    }
}
