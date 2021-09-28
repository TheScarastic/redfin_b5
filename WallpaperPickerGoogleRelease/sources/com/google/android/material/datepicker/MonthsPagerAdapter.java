package com.google.android.material.datepicker;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.core.view.ViewPropertyAnimatorCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.android.systemui.shared.R;
import com.google.android.material.datepicker.MaterialCalendar;
import java.util.Iterator;
import java.util.WeakHashMap;
/* loaded from: classes.dex */
public class MonthsPagerAdapter extends RecyclerView.Adapter<ViewHolder> {
    public final CalendarConstraints calendarConstraints;
    public final Context context;
    public final DateSelector<?> dateSelector;
    public final int itemHeight;
    public final MaterialCalendar.OnDayClickListener onDayClickListener;

    /* loaded from: classes.dex */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCalendarGridView monthGrid;
        public final TextView monthTitle;

        public ViewHolder(LinearLayout linearLayout, boolean z) {
            super(linearLayout);
            TextView textView = (TextView) linearLayout.findViewById(R.id.month_title);
            this.monthTitle = textView;
            WeakHashMap<View, ViewPropertyAnimatorCompat> weakHashMap = ViewCompat.sViewPropertyAnimatorMap;
            
            /*  JADX ERROR: Method code generation error
                jadx.core.utils.exceptions.CodegenException: Error generate insn: 0x001e: INVOKE  
                  (wrap: androidx.core.view.ViewCompat$5 : 0x0019: CONSTRUCTOR  (r1v1 androidx.core.view.ViewCompat$5 A[REMOVE]) = 
                  (wrap: ?? : ?: SGET   com.android.systemui.shared.R.id.tag_accessibility_heading int)
                  (wrap: java.lang.Class : 0x0012: CONST_CLASS   java.lang.Boolean.class)
                  (28 int)
                 call: androidx.core.view.ViewCompat.5.<init>(int, java.lang.Class, int):void type: CONSTRUCTOR)
                  (r0v2 'textView' android.widget.TextView)
                  (wrap: java.lang.Boolean : 0x001c: SGET  (r2v1 java.lang.Boolean A[REMOVE]) =  java.lang.Boolean.TRUE java.lang.Boolean)
                 type: VIRTUAL call: androidx.core.view.ViewCompat.AccessibilityViewProperty.set(android.view.View, java.lang.Object):void in method: com.google.android.material.datepicker.MonthsPagerAdapter.ViewHolder.<init>(android.widget.LinearLayout, boolean):void, file: classes.dex
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
                Caused by: jadx.core.utils.exceptions.JadxRuntimeException: Expected class to be processed at this point, class: androidx.core.view.ViewCompat, state: GENERATED_AND_UNLOADED
                	at jadx.core.dex.nodes.ClassNode.ensureProcessed(ClassNode.java:259)
                	at jadx.core.codegen.InsnGen.makeConstructor(InsnGen.java:675)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:393)
                	at jadx.core.codegen.InsnGen.addWrappedArg(InsnGen.java:141)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:120)
                	at jadx.core.codegen.InsnGen.addArg(InsnGen.java:104)
                	at jadx.core.codegen.InsnGen.addArgDot(InsnGen.java:93)
                	at jadx.core.codegen.InsnGen.makeInvoke(InsnGen.java:770)
                	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:397)
                	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:278)
                	... 15 more
                */
            /*
                this = this;
                r5.<init>(r6)
                r0 = 2131362139(0x7f0a015b, float:1.834405E38)
                android.view.View r0 = r6.findViewById(r0)
                android.widget.TextView r0 = (android.widget.TextView) r0
                r5.monthTitle = r0
                java.util.WeakHashMap<android.view.View, androidx.core.view.ViewPropertyAnimatorCompat> r1 = androidx.core.view.ViewCompat.sViewPropertyAnimatorMap
                androidx.core.view.ViewCompat$5 r1 = new androidx.core.view.ViewCompat$5
                java.lang.Class<java.lang.Boolean> r2 = java.lang.Boolean.class
                r3 = 2131362350(0x7f0a022e, float:1.8344478E38)
                r4 = 28
                r1.<init>(r3, r2, r4)
                java.lang.Boolean r2 = java.lang.Boolean.TRUE
                r1.set(r0, r2)
                r1 = 2131362134(0x7f0a0156, float:1.834404E38)
                android.view.View r6 = r6.findViewById(r1)
                com.google.android.material.datepicker.MaterialCalendarGridView r6 = (com.google.android.material.datepicker.MaterialCalendarGridView) r6
                r5.monthGrid = r6
                if (r7 != 0) goto L_0x0033
                r5 = 8
                r0.setVisibility(r5)
            L_0x0033:
                return
            */
            throw new UnsupportedOperationException("Method not decompiled: com.google.android.material.datepicker.MonthsPagerAdapter.ViewHolder.<init>(android.widget.LinearLayout, boolean):void");
        }
    }

    public MonthsPagerAdapter(Context context, DateSelector<?> dateSelector, CalendarConstraints calendarConstraints, MaterialCalendar.OnDayClickListener onDayClickListener) {
        Month month = calendarConstraints.start;
        Month month2 = calendarConstraints.end;
        Month month3 = calendarConstraints.openAt;
        if (month.compareTo(month3) > 0) {
            throw new IllegalArgumentException("firstPage cannot be after currentPage");
        } else if (month3.compareTo(month2) <= 0) {
            int i = MonthAdapter.MAXIMUM_WEEKS;
            Object obj = MaterialCalendar.MONTHS_VIEW_GROUP_TAG;
            int dimensionPixelSize = i * context.getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_day_height);
            int dimensionPixelSize2 = MaterialDatePicker.isFullscreen(context) ? context.getResources().getDimensionPixelSize(R.dimen.mtrl_calendar_day_height) : 0;
            this.context = context;
            this.itemHeight = dimensionPixelSize + dimensionPixelSize2;
            this.calendarConstraints = calendarConstraints;
            this.dateSelector = dateSelector;
            this.onDayClickListener = onDayClickListener;
            if (!this.mObservable.hasObservers()) {
                this.mHasStableIds = true;
                return;
            }
            throw new IllegalStateException("Cannot change whether this adapter has stable IDs while the adapter has registered observers.");
        } else {
            throw new IllegalArgumentException("currentPage cannot be after lastPage");
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.calendarConstraints.monthSpan;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public long getItemId(int i) {
        return this.calendarConstraints.start.monthsLater(i).firstOfMonth.getTimeInMillis();
    }

    public Month getPageMonth(int i) {
        return this.calendarConstraints.start.monthsLater(i);
    }

    public int getPosition(Month month) {
        return this.calendarConstraints.start.monthsUntil(month);
    }

    /* JADX DEBUG: Method arguments types fixed to match base method, original types: [androidx.recyclerview.widget.RecyclerView$ViewHolder, int] */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        ViewHolder viewHolder2 = viewHolder;
        Month monthsLater = this.calendarConstraints.start.monthsLater(i);
        viewHolder2.monthTitle.setText(monthsLater.getLongName(viewHolder2.itemView.getContext()));
        final MaterialCalendarGridView materialCalendarGridView = (MaterialCalendarGridView) viewHolder2.monthGrid.findViewById(R.id.month_grid);
        if (materialCalendarGridView.getAdapter() == null || !monthsLater.equals(materialCalendarGridView.getAdapter().month)) {
            MonthAdapter monthAdapter = new MonthAdapter(monthsLater, this.dateSelector, this.calendarConstraints);
            materialCalendarGridView.setNumColumns(monthsLater.daysInWeek);
            materialCalendarGridView.setAdapter((ListAdapter) monthAdapter);
        } else {
            materialCalendarGridView.invalidate();
            MonthAdapter adapter = materialCalendarGridView.getAdapter();
            for (Long l : adapter.previouslySelectedDates) {
                adapter.updateSelectedStateForDate(materialCalendarGridView, l.longValue());
            }
            DateSelector<?> dateSelector = adapter.dateSelector;
            if (dateSelector != null) {
                for (Long l2 : dateSelector.getSelectedDays()) {
                    adapter.updateSelectedStateForDate(materialCalendarGridView, l2.longValue());
                }
                adapter.previouslySelectedDates = adapter.dateSelector.getSelectedDays();
            }
        }
        materialCalendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() { // from class: com.google.android.material.datepicker.MonthsPagerAdapter.1
            @Override // android.widget.AdapterView.OnItemClickListener
            public void onItemClick(AdapterView<?> adapterView, View view, int i2, long j) {
                MonthAdapter adapter2 = materialCalendarGridView.getAdapter();
                if (i2 >= adapter2.firstPositionInMonth() && i2 <= adapter2.lastPositionInMonth()) {
                    MaterialCalendar.OnDayClickListener onDayClickListener = MonthsPagerAdapter.this.onDayClickListener;
                    long longValue = materialCalendarGridView.getAdapter().getItem(i2).longValue();
                    MaterialCalendar.AnonymousClass3 r1 = (MaterialCalendar.AnonymousClass3) onDayClickListener;
                    if (MaterialCalendar.this.calendarConstraints.validator.isValid(longValue)) {
                        MaterialCalendar.this.dateSelector.select(longValue);
                        Iterator it = MaterialCalendar.this.onSelectionChangedListeners.iterator();
                        while (it.hasNext()) {
                            ((OnSelectionChangedListener) it.next()).onSelectionChanged(MaterialCalendar.this.dateSelector.getSelection());
                        }
                        MaterialCalendar.this.recyclerView.getAdapter().mObservable.notifyChanged();
                        RecyclerView recyclerView = MaterialCalendar.this.yearSelector;
                        if (recyclerView != null) {
                            recyclerView.getAdapter().mObservable.notifyChanged();
                        }
                    }
                }
            }
        });
    }

    /* Return type fixed from 'androidx.recyclerview.widget.RecyclerView$ViewHolder' to match base method */
    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.mtrl_calendar_month_labeled, viewGroup, false);
        if (!MaterialDatePicker.isFullscreen(viewGroup.getContext())) {
            return new ViewHolder(linearLayout, false);
        }
        linearLayout.setLayoutParams(new RecyclerView.LayoutParams(-1, this.itemHeight));
        return new ViewHolder(linearLayout, true);
    }
}
